package org.annpurna.cli.wallet;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

import org.annpurna.cli.common.utils.CommonUtils;
import org.annpurna.cli.common.utils.ResourceAdapter;
import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.ContractException;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Gateway.Builder;

import com.annpurna.cli.common.model.AnnpurnaWallet;
import com.annpurna.cli.common.model.CommercialPaper;
import com.annpurna.cli.common.util.JsonParser;
import com.annpurna.cli.common.util.JsonParser;
import com.annpurna.wallet.crypto.CryptoUtil;

import org.hyperledger.fabric.gateway.GatewayException;
import org.hyperledger.fabric.gateway.Network;
import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.Wallets;

public class WalletContractService {

	private static ResourceAdapter props = ResourceAdapter.getInstance("annpurna-net.properties");

	public static void main(String[] args) {
		

			String contractName=props.getProperty("contract.name");
			String chaincodeId=props.getProperty("chaincode.name");
			String channelName = props.getProperty("channel.name");
			
			// Connect to gateway using application specified parameters
			try(Gateway gateway = createGatewayBuilder().connect()) {

				// Access PaperNet network
				System.out.println("Use network channel: "+channelName);
				Network network = gateway.getNetwork(channelName);

				// Get addressability to commercial paper contract
				System.out.println("chain code:"+chaincodeId+", smart contract"+contractName);
				Contract contract = network.getContract(chaincodeId, contractName);

				String walletId = UUID.randomUUID().toString() ;
				// Create wallet

				System.out.println(createWallet(contract,walletId));
				
				// Read wallet
				System.out.println(readWallet(contract, walletId));
			
				System.out.println(getBalance(contract, walletId));
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		} 
		
		System.out.println("Process Ended successfully");
	}
	
	
	private static Builder createGatewayBuilder() throws IOException {
		Builder builder = Gateway.createBuilder();

		// A wallet stores a collection of identities
		Path walletPath = Paths.get(props.getProperty("user.wallet.path"));
		Wallet wallet = Wallets.newFileSystemWallet(walletPath);
		System.out.println("Read wallet info from: " + walletPath);

		String userName = props.getProperty("org.user.id");

		Path connectionProfile = Paths.get(props.getProperty("connection.profile.path"));

		// Set connection options on the gateway builder
		builder.identity(wallet, userName).networkConfig(connectionProfile).discovery(true);
		
		return builder;
	}
	
	
	private static AnnpurnaWallet createWallet(Contract contract ,String walletId) throws Exception {
		System.out.println("Submit create wallet transaction.");
		AnnpurnaWallet wallet = new AnnpurnaWallet();
		wallet.setId(walletId);
		wallet.setValue(100L);
		byte[] signature = CryptoUtil.signWithPrivatekey(wallet.getId().getBytes(), props.getProperty("user.priv.key.path"));
		wallet.setOwner(CryptoUtil.base64EndoedString(CryptoUtil.loadPEM(props.getProperty("user.pub.key.path"))));
		String walletJson = JsonParser.serialize(wallet);
		byte[] response = contract.submitTransaction("CreateWallet", walletJson, CryptoUtil.base64EndoedString(signature));
		return JsonParser.deserialize(CommonUtils.deserialize(response),AnnpurnaWallet.class);
	}
	
	private static AnnpurnaWallet readWallet(Contract contract ,String walletId) throws Exception{
		System.out.println("Submit read wallet transaction.");
		byte[] signature = CryptoUtil.signWithPrivatekey(walletId.getBytes(), props.getProperty("user.priv.key.path"));
		byte[] response = contract.submitTransaction("ReadWallet", walletId ,  CryptoUtil.base64EndoedString(signature));
		String responseStr = CommonUtils.deserialize(response);
		System.out.println("Wallet:"+responseStr);
		return JsonParser.deserialize(responseStr,AnnpurnaWallet.class);
	}
	
	private static String getBalance(Contract contract ,String walletId) throws Exception{
		System.out.println("Submit balanceOf wallet transaction.");
		byte[] signature = CryptoUtil.signWithPrivatekey(walletId.getBytes(), props.getProperty("user.priv.key.path"));
		byte[] response = contract.submitTransaction("balanceOf", walletId ,  CryptoUtil.base64EndoedString(signature));
		String responseStr = CommonUtils.deserialize(response);
		System.out.println("Balance:"+responseStr);
		return responseStr ;
	}

}
