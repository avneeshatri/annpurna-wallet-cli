package org.annpurna.cli.common.contract;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.List;

import org.annpurna.cli.common.utils.CommonUtils;
import org.annpurna.cli.common.utils.ResourceAdapter;

import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Network;
import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.Wallets;
import org.hyperledger.fabric.gateway.Gateway.Builder;

import com.annpurna.cli.common.model.AnnpurnaWallet;
import com.annpurna.cli.common.util.JsonParser;
import com.annpurna.wallet.crypto.CryptoUtil;

public class AnnpurnaWalletOperations {

	private static ResourceAdapter props = ResourceAdapter.getInstance("annpurna-net.properties");
	private static Gateway gateway ;
	private static Contract contract ;
	
	private boolean isOpen() {
		if(gateway != null && contract !=null) {
			return true ;
		}	
		return false ;
	}
	
	public void close() {
		gateway.close(); 
		gateway = null;
		contract = null;
	}
	
	private Gateway createGatewayBuilder() throws IOException {

		Builder builder = Gateway.createBuilder();
		
		// A wallet stores a collection of identities
		Path walletPath = Paths.get(props.getProperty("user.wallet.path"));
		Wallet wallet = Wallets.newFileSystemWallet(walletPath);
		System.out.println("Read wallet info from: " + walletPath);

		String userName = props.getProperty("org.user.id");

		Path connectionProfile = Paths.get(props.getProperty("connection.profile.path"));

		// Set connection options on the gateway builder
		builder.identity(wallet, userName).networkConfig(connectionProfile)
		.discovery(true);

		gateway =  builder.connect();
		
		return gateway;
	}
	
	private Contract getContract() throws IOException {
		if(isOpen()) {
			return contract ;
		} else {
			String contractName=props.getProperty("contract.name");
			String chaincodeId=props.getProperty("chaincode.name");
			String channelName = props.getProperty("channel.name");
			
			// Access PaperNet network
			System.out.println("Use network channel: "+channelName);
			Network network = createGatewayBuilder().getNetwork(channelName);
			
			// Get addressability to commercial paper contract
			System.out.println("chain code:"+chaincodeId+", smart contract"+contractName);
			contract = network.getContract(chaincodeId, contractName);
		}
		return contract ;
	}
	
	public String transferTo(String recipeient , Long ammount) throws Exception  {
		System.out.println("Transfer to "+recipeient+" partner wallet transaction.");
		byte[] response = getContract().submitTransaction("transferTo", recipeient, String.valueOf(ammount));
		return JsonParser.deserialize(CommonUtils.deserialize(response),String.class);
	}
	
	public AnnpurnaWallet readWallet(String secret) throws Exception{
		System.out.println("Submit read wallet transaction.");
		String userSign = "" ;
		String walletId  = CryptoUtil.generateWalletId(secret);
		
		if(secret != null && !secret.isBlank()) {
			byte[] signature = CryptoUtil.signWithPrivatekey(walletId.getBytes(),
					CryptoUtil.generatePKCS8EncodedPrivateKey(CryptoUtil.base64Decoded(secret)));
			userSign = CryptoUtil.base64EndoedString(signature) ;
		}
		byte[] response = getContract().submitTransaction("ReadWallet", walletId ,  userSign);
		String responseStr = CommonUtils.deserialize(response);
		System.out.println("Wallet:"+responseStr);
		return JsonParser.deserialize(responseStr,AnnpurnaWallet.class);
	}
	
	public Long getBalance(String secret) throws Exception{
		System.out.println("Submit balanceOf wallet transaction.");
		String userSign =  "" ;
		String walletId = CryptoUtil.generateWalletId(secret) ;
		
		if(secret != null && !secret.isBlank()) {
			byte[] signature = CryptoUtil.signWithPrivatekey(walletId.getBytes(), 
					CryptoUtil.generatePKCS8EncodedPrivateKey(CryptoUtil.base64Decoded(secret)));
			userSign = CryptoUtil.base64EndoedString(signature) ;
		}
		byte[] response = getContract().submitTransaction("balanceOf", walletId , userSign );
		String responseStr = CommonUtils.deserialize(response);
		System.out.println("Balance:"+responseStr);
		return Long.valueOf(responseStr) ;
	}
	
	public AnnpurnaWallet createPartnerWallet( String orgMspId) throws Exception {
		System.out.println("Submit create partner wallet transaction.");
		byte[] response = getContract().submitTransaction("CreatePartnerWallet", orgMspId);
		return JsonParser.deserialize(CommonUtils.deserialize(response),AnnpurnaWallet.class);
	}

	public AnnpurnaWallet addFunds(long value) throws Exception {
		System.out.println("Submit create partner wallet transaction.");
		byte[] response = getContract().submitTransaction("addFunds", String.valueOf(value));
		return JsonParser.deserialize(CommonUtils.deserialize(response),AnnpurnaWallet.class);
	}
	
	public AnnpurnaWallet createWallet() throws Exception {
		System.out.println("Submit create wallet transaction.");
		KeyPair keyPair = CryptoUtil.generateKeyPair() ;
		PrivateKey privKey = keyPair.getPrivate();
		String base64EncodedPrivKey = CryptoUtil.base64EndoedString(privKey.getEncoded());
		
		PublicKey pubKey = keyPair.getPublic();
		String base64EncodedPubKey = CryptoUtil.base64EndoedString(pubKey.getEncoded());
		
		
		AnnpurnaWallet wallet = new AnnpurnaWallet();
		wallet.setId(CryptoUtil.generateSHA3Hash(base64EncodedPrivKey));
		wallet.setOwner(base64EncodedPubKey);
		wallet.setSecret(base64EncodedPrivKey);
		
		byte[] signature = CryptoUtil.signWithPrivatekey(wallet.getId().getBytes(), 
				CryptoUtil.generatePKCS8EncodedPrivateKey(CryptoUtil.base64Decoded(base64EncodedPrivKey)));
		
		System.out.println("Verification status:"+CryptoUtil.verifySinature(CryptoUtil.base64Decoded(CryptoUtil.base64EndoedString(signature)),
				wallet.getId().getBytes(), CryptoUtil.generateX509EncodedPublicKey(CryptoUtil.base64Decoded(wallet.getOwner()))));
		
		String walletJson = JsonParser.serialize(wallet);
		byte[] response = getContract().submitTransaction("CreateWallet", walletJson, CryptoUtil.base64EndoedString(signature));
		return JsonParser.deserialize(CommonUtils.deserialize(response),AnnpurnaWallet.class);
	}
	
	public void transfer(String secret , String recipentWalletId, long amount ) throws Exception {
		if(secret != null && !secret.isBlank()) {
			String senderWalletId = CryptoUtil.generateWalletId(secret) ;
			byte[] signature = CryptoUtil.signWithPrivatekey(senderWalletId.getBytes(), 
					CryptoUtil.generatePKCS8EncodedPrivateKey(CryptoUtil.base64Decoded(secret)));
			String userSign = CryptoUtil.base64EndoedString(signature) ;
			byte[] response = getContract().submitTransaction("transfer", userSign, senderWalletId, recipentWalletId ,String.valueOf(amount));
		}
	}
	
	public List<AnnpurnaWallet> getWalletHistory(String secret) throws Exception {
		System.out.println("Submit get wallet history transaction.");
		String walletId = CryptoUtil.generateWalletId(secret) ;

		byte[] signature = CryptoUtil.signWithPrivatekey(walletId.getBytes(), 
				CryptoUtil.generatePKCS8EncodedPrivateKey(CryptoUtil.base64Decoded(secret)));
		String userSign = CryptoUtil.base64EndoedString(signature) ;
		
		byte[] response = getContract().submitTransaction("GetWalletHistory", walletId, userSign);
		String responseStr = CommonUtils.deserialize(response);
		List<AnnpurnaWallet> list = Arrays.asList(JsonParser.deserialize(responseStr,AnnpurnaWallet[].class));
        return list;
	}
}
