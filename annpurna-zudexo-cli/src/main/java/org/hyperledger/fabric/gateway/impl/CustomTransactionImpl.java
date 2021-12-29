package org.hyperledger.fabric.gateway.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hyperledger.fabric.gateway.ContractException;
import org.hyperledger.fabric.gateway.GatewayRuntimeException;
import org.hyperledger.fabric.gateway.Transaction;
import org.hyperledger.fabric.gateway.impl.ContractImpl;
import org.hyperledger.fabric.gateway.impl.GatewayImpl;
import org.hyperledger.fabric.gateway.impl.NetworkImpl;
import org.hyperledger.fabric.gateway.impl.TimePeriod;
import org.hyperledger.fabric.gateway.impl.TransactionImpl;
import org.hyperledger.fabric.gateway.impl.query.QueryImpl;
import org.hyperledger.fabric.gateway.spi.CommitHandler;
import org.hyperledger.fabric.gateway.spi.CommitHandlerFactory;
import org.hyperledger.fabric.gateway.spi.Query;
import org.hyperledger.fabric.gateway.spi.QueryHandler;
import org.hyperledger.fabric.sdk.ChaincodeResponse;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.Peer;
import org.hyperledger.fabric.sdk.ProposalResponse;
import org.hyperledger.fabric.sdk.QueryByChaincodeRequest;
import org.hyperledger.fabric.sdk.SDKUtils;
import org.hyperledger.fabric.sdk.ServiceDiscovery;
import org.hyperledger.fabric.sdk.TransactionProposalRequest;
import org.hyperledger.fabric.sdk.TransactionRequest;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.exception.ServiceDiscoveryException;
import org.hyperledger.fabric.sdk.helper.Utils;

import com.google.protobuf.ByteString;

import static org.hyperledger.fabric.sdk.Channel.DiscoveryOptions.createDiscoveryOptions;

public final class CustomTransactionImpl implements Transaction {
	
	private static final Log LOG = LogFactory.getLog(CustomTransactionImpl.class);


    private static final long DEFAULT_ORDERER_TIMEOUT = 60;
    private static final TimeUnit DEFAULT_ORDERER_TIMEOUT_UNIT = TimeUnit.SECONDS;
    
    private final ContractImpl contract;
    private final String name;
    private final NetworkImpl network;
    private final Channel channel;
    private final GatewayImpl gateway;
    private final CommitHandlerFactory commitHandlerFactory;
    private TimePeriod commitTimeout;
    private final QueryHandler queryHandler;
    private Map<String, byte[]> transientData = null;
    private Collection<Peer> endorsingPeers = null;
    
	public CustomTransactionImpl(final ContractImpl contract, final String name) {
        this.contract = contract;
        this.name = name;
        network = contract.getNetwork();
        channel = network.getChannel();
        gateway = network.getGateway();
        commitHandlerFactory = gateway.getCommitHandlerFactory();
        commitTimeout = gateway.getCommitTimeout();
        queryHandler = network.getQueryHandler();
    }
	

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Transaction setTransient(final Map<String, byte[]> transientData) {
        this.transientData = transientData;
        return this;
    }

    @Override
    public Transaction setCommitTimeout(final long timeout, final TimeUnit timeUnit) {
        commitTimeout = new TimePeriod(timeout, timeUnit);
        return this;
    }

    @Override
    public Transaction setEndorsingPeers(final Collection<Peer> peers) {
        endorsingPeers = peers;
        return this;
    }

    @Override
    public byte[] submit(final String... args) throws ContractException, TimeoutException, InterruptedException {
        Collection<ProposalResponse> proposalResponses = endorseTransaction(args);
        Collection<ProposalResponse> validResponses = validatePeerResponses(proposalResponses);
        Set<ProposalResponse> invalid = new HashSet<>();
        try {
			checkProposalConsistencyReadWriteSets(validResponses,invalid);
		} catch (Exception e1) {
			throw  new IllegalStateException("Read Write Set Check Failed",e1);
		}
        try {
            return commitTransaction(validResponses);
        } catch (ContractException e) {
            e.setProposalResponses(proposalResponses);
            throw e;
        }
    }

	private static void checkProposalConsistencyReadWriteSets(Collection<? extends ProposalResponse> proposalResponses,
            Set<ProposalResponse> invalid) throws InvalidArgumentException {
		int consistencyGroups = getProposalConsistencySets(proposalResponses, invalid).size();
		System.out.println("Consistency Groups :" + consistencyGroups);
		
		if(consistencyGroups!=1) {
			throw new IllegalStateException("Consistency Group is not equal to 1");
		}
		
		
	}
	

    public static Collection<Set<ProposalResponse>> getProposalConsistencySets(Collection<? extends ProposalResponse> proposalResponses,
                                                                               Set<ProposalResponse> invalid) throws InvalidArgumentException {
    	System.out.println("Geting Proposal Consistency Set");

        if (proposalResponses == null) {
            throw new InvalidArgumentException("proposalResponses collection is null");
        }

        if (proposalResponses.isEmpty()) {
            throw new InvalidArgumentException("proposalResponses collection is empty");
        }

        if (null == invalid) {
            throw new InvalidArgumentException("invalid set is null.");
        }

        HashMap<ByteString, Set<ProposalResponse>> ret = new HashMap<>();

        for (ProposalResponse proposalResponse : proposalResponses) {

            if (proposalResponse.isInvalid()) {
                invalid.add(proposalResponse);
            } else {
                // payload bytes is what's being signed over so it must be consistent.
            	
                final ByteString payloadBytes = proposalResponse. getProposalResponse().getPayload();
                System.out.println(payloadBytes);
                if (payloadBytes == null) {
                    throw new InvalidArgumentException(("proposalResponse.getPayloadBytes() was null from peer: %s"+
                            proposalResponse.getPeer()));
                } else if (payloadBytes.isEmpty()) {
                    throw new InvalidArgumentException(("proposalResponse.getPayloadBytes() was empty from peer: "+
                            proposalResponse.getPeer()));
                }
                Set<ProposalResponse> set = ret.computeIfAbsent(payloadBytes, k -> new HashSet<>());
                set.add(proposalResponse);
            }
        }

        if ( ret.values().size() > 1) {

            StringBuilder sb = new StringBuilder(1000);

            int i = 0;
            String sep = "";

            for (Map.Entry<ByteString, Set<ProposalResponse>> entry : ret.entrySet()) {
                ByteString bytes = entry.getKey();
                Set<ProposalResponse> presp = entry.getValue();

                sb.append(sep)
                        .append("Consistency set: ").append(i++).append(" bytes size: ").append(bytes.size())
                        .append(" bytes: ")
                        .append(Utils.toHexString(bytes.toByteArray())).append(" [");

                String psep = "";

                for (ProposalResponse proposalResponse : presp) {
                    sb.append(psep).append(proposalResponse.getPeer());
                    psep = ", ";
                }
                sb.append("]");
                sep = ", ";
            }
            System.out.println(sb.toString()) ;
            LOG.debug(sb.toString());

        }

        return ret.values();

    }
    private Collection<ProposalResponse> endorseTransaction(final String... args) {
        try {
            TransactionProposalRequest request = newProposalRequest(args);
            return sendTransactionProposal(request);
        } catch (InvalidArgumentException | ProposalException | ServiceDiscoveryException e) {
            throw new GatewayRuntimeException(e);
        }
    }

    private Collection<ProposalResponse> sendTransactionProposal(final TransactionProposalRequest request)
            throws ProposalException, InvalidArgumentException, ServiceDiscoveryException {
        if (endorsingPeers != null) {
            return channel.sendTransactionProposal(request, endorsingPeers);
        } else if (network.getGateway().isDiscoveryEnabled()) {
            Channel.DiscoveryOptions discoveryOptions = createDiscoveryOptions()
                    .setEndorsementSelector(ServiceDiscovery.EndorsementSelector.ENDORSEMENT_SELECTION_RANDOM)
                    .setInspectResults(true)
                    .setForceDiscovery(true);
            return channel.sendTransactionProposalToEndorsers(request, discoveryOptions);
        } else {
            return channel.sendTransactionProposal(request);
        }
    }

    private byte[] commitTransaction(final Collection<ProposalResponse> validResponses)
            throws TimeoutException, ContractException, InterruptedException {
        ProposalResponse proposalResponse = validResponses.iterator().next();
        String transactionId = proposalResponse.getTransactionID();

        CommitHandler commitHandler = commitHandlerFactory.create(transactionId, network);
        commitHandler.startListening();

        try {
            Channel.TransactionOptions transactionOptions = Channel.TransactionOptions.createTransactionOptions()
                    .nOfEvents(Channel.NOfEvents.createNoEvents()); // Disable default commit wait behaviour
            channel.sendTransaction(validResponses, transactionOptions)
                    .get(DEFAULT_ORDERER_TIMEOUT, DEFAULT_ORDERER_TIMEOUT_UNIT);
        } catch (TimeoutException e) {
            commitHandler.cancelListening();
            throw e;
        } catch (Exception e) {
            commitHandler.cancelListening();
            throw new ContractException("Failed to send transaction to the orderer", e);
        }

        commitHandler.waitForEvents(commitTimeout.getTime(), commitTimeout.getTimeUnit());

        try {
            return proposalResponse.getChaincodeActionResponsePayload();
        } catch (InvalidArgumentException e) {
            throw new GatewayRuntimeException(e);
        }
    }

    private TransactionProposalRequest newProposalRequest(final String... args) {
        TransactionProposalRequest request = network.getGateway().getClient().newTransactionProposalRequest();
        configureRequest(request, args);
        if (transientData != null) {
            try {
                request.setTransientMap(transientData);
            } catch (InvalidArgumentException e) {
                // Only happens if transientData is null
                throw new IllegalStateException(e);
            }
        }
        return request;
    }

    private void configureRequest(final TransactionRequest request, final String... args) {
        request.setChaincodeName(contract.getChaincodeId());
        request.setFcn(name);
        request.setArgs(args);
    }

    private Collection<ProposalResponse> validatePeerResponses(final Collection<ProposalResponse> proposalResponses)
            throws ContractException {
        final Collection<ProposalResponse> validResponses = new ArrayList<>();
        final Collection<String> invalidResponseMsgs = new ArrayList<>();
        proposalResponses.forEach(response -> {
            String peerUrl = response.getPeer() != null ? response.getPeer().getUrl() : "<unknown>";
            if (response.getStatus().equals(ChaincodeResponse.Status.SUCCESS)) {
                LOG.debug(String.format("validatePeerResponses: valid response from peer %s", peerUrl));
                validResponses.add(response);
            } else {
                LOG.warn(String.format("validatePeerResponses: invalid response from peer %s, message %s", peerUrl, response.getMessage()));
                invalidResponseMsgs.add(response.getMessage());
            }
        });

        if (validResponses.size() < 1) {
            String msg = String.format("No valid proposal responses received. %d peer error responses: %s",
                    invalidResponseMsgs.size(), String.join("; ", invalidResponseMsgs));
            LOG.error(msg);
            throw new ContractException(msg, proposalResponses);
        }

        return validResponses;
    }

    @Override
    public byte[] evaluate(final String... args) throws ContractException {
        QueryByChaincodeRequest request = newQueryRequest(args);
        Query query = new QueryImpl(network.getChannel(), request);

        ProposalResponse response = queryHandler.evaluate(query);

        try {
            return response.getChaincodeActionResponsePayload();
        } catch (InvalidArgumentException e) {
            throw new ContractException(response.getMessage(), e);
        }
    }

    private QueryByChaincodeRequest newQueryRequest(final String... args) {
        QueryByChaincodeRequest request = gateway.getClient().newQueryProposalRequest();
        configureRequest(request, args);
        if (transientData != null) {
            try {
                request.setTransientMap(transientData);
            } catch (InvalidArgumentException e) {
                // Only happens if transientData is null
                throw new IllegalStateException(e);
            }
        }
        return request;
    }
}
