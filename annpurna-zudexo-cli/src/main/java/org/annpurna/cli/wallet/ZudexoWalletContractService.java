package org.annpurna.cli.wallet;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.UUID;

import org.annpurna.cli.common.utils.CommonUtils;
import org.annpurna.cli.common.utils.ResourceAdapter;
import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Gateway.Builder;
import org.hyperledger.fabric.gateway.Network;
import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.Wallets;

import com.annpurna.cli.common.model.AnnpurnaWallet;
import com.annpurna.cli.common.util.JsonParser;
import com.annpurna.wallet.crypto.CryptoUtil;

public class ZudexoWalletContractService {

	private static ResourceAdapter props = ResourceAdapter.getInstance("annpurna-net.properties");
	private static final String ZudexoMSP = "ZudexoMSP";
	private static final String ZiggyMSP = "ZiggyMSP" ;
	
	
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
				
				contract.addContractListener(new AnnpurnaWalletEventListner());
				
				setUpZudexo(contract,100000000000000L);
				System.out.println(getBalance(contract, ZudexoMSP, ""));
				setUpZiggy(contract, 10000L);
				System.out.println(getBalance(contract, ZudexoMSP, ""));
				// Create wallet
				AnnpurnaWallet wallet = createWallet(contract);
				System.out.println("wallet :"+wallet);
				
				// Read wallet
				System.out.println(readWallet(contract, wallet.getId(), wallet.getSecret()));
			
				System.out.println(getBalance(contract, wallet.getId(), wallet.getSecret()));
				
				/*
				
				System.out.println(getBalance(contract, ZudexoMSP, ""));
				
				transaction(contract, 100L);
				
				System.out.println(getBalance(contract, "ZudexoMSP", ""));
				
				*/
				
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
		builder.identity(wallet, userName).networkConfig(connectionProfile)
		.discovery(true);
		
		return builder;
	}
	
	
	private static AnnpurnaWallet createWallet(Contract contract ) throws Exception {
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
		byte[] response = contract.submitTransaction("CreateWallet", walletJson, CryptoUtil.base64EndoedString(signature));
		return JsonParser.deserialize(CommonUtils.deserialize(response),AnnpurnaWallet.class);
	}
	
	private static AnnpurnaWallet createPartnerWallet(Contract contract , String orgMspId) throws Exception {
		System.out.println("Submit create partner wallet transaction.");
		
		byte[] response = contract.submitTransaction("CreatePartnerWallet", orgMspId);
		return JsonParser.deserialize(CommonUtils.deserialize(response),AnnpurnaWallet.class);
	}
	
	private static AnnpurnaWallet addFunds(Contract contract , long value) throws Exception {
		System.out.println("Submit create partner wallet transaction.");
		
		byte[] response = contract.submitTransaction("addFunds", String.valueOf(value));
		return JsonParser.deserialize(CommonUtils.deserialize(response),AnnpurnaWallet.class);
	}
	
	private static void setUpZudexo(Contract contract,long value) throws Exception {
		
		System.out.println(createPartnerWallet(contract,ZudexoMSP));
		System.out.println(addFunds(contract,value));
	}
	
	private static void setUpZiggy(Contract contract,long value) throws Exception {
		
		System.out.println(createPartnerWallet(contract,ZiggyMSP));
		System.out.println(transferTo(contract,ZiggyMSP,value));
	}
	
	private static AnnpurnaWallet readWallet(Contract contract ,String walletId , String secret) throws Exception{
		System.out.println("Submit read wallet transaction.");
		byte[] signature = CryptoUtil.signWithPrivatekey(walletId.getBytes(),
				CryptoUtil.generatePKCS8EncodedPrivateKey(CryptoUtil.base64Decoded(secret)));
		byte[] response = contract.submitTransaction("ReadWallet", walletId ,  CryptoUtil.base64EndoedString(signature));
		String responseStr = CommonUtils.deserialize(response);
		System.out.println("Wallet:"+responseStr);
		return JsonParser.deserialize(responseStr,AnnpurnaWallet.class);
	}
	
	private static String getBalance(Contract contract ,String walletId, String secret) throws Exception{
		System.out.println("Submit balanceOf wallet transaction.");
		String userSign =  "" ;
				
		if(secret != null && !secret.isBlank()) {
			byte[] signature = CryptoUtil.signWithPrivatekey(walletId.getBytes(), 
					CryptoUtil.generatePKCS8EncodedPrivateKey(CryptoUtil.base64Decoded(secret)));
			userSign = CryptoUtil.base64EndoedString(signature) ;
		}
		byte[] response = contract.submitTransaction("balanceOf", walletId , userSign );
		String responseStr = CommonUtils.deserialize(response);
		System.out.println("Balance:"+responseStr);
		return responseStr ;
	}
	
	private static void approve(Contract contract ,String ownerWalletId,String ownerSecret, String spenderWallet,String ammount) throws Exception {
		byte[] signature = CryptoUtil.signWithPrivatekey(ownerWalletId.getBytes(), 
				CryptoUtil.generatePKCS8EncodedPrivateKey(CryptoUtil.base64Decoded(ownerSecret)));
		byte[] response = contract.submitTransaction("approve", CryptoUtil.base64EndoedString(signature),ownerWalletId,spenderWallet,ammount);
		String responseStr = CommonUtils.deserialize(response);
		System.out.println("IsApproved:"+responseStr);
	}

	private static void allowance(Contract contract ,String ownerWalletId, String ownerSecret, String spenderWallet) throws Exception {
		byte[] signature = CryptoUtil.signWithPrivatekey(ownerWalletId.getBytes(),
				CryptoUtil.generatePKCS8EncodedPrivateKey(CryptoUtil.base64Decoded(ownerSecret)));
		byte[] response = contract.submitTransaction("allowance", CryptoUtil.base64EndoedString(signature),ownerWalletId,spenderWallet);
		String responseStr = CommonUtils.deserialize(response);
		System.out.println("Approved ammount:"+responseStr);
	}
	
	public static String transferTo(Contract contract , String recipeient , Long ammount) throws Exception  {
		System.out.println("Transfer to "+recipeient+" partner wallet transaction.");
		byte[] response = contract.submitTransaction("transferTo", recipeient, String.valueOf(ammount));
		return JsonParser.deserialize(CommonUtils.deserialize(response),String.class);
	}
	
	private static void transaction(Contract contract,long ammount) throws Exception {
		AnnpurnaWallet wallet =createWallet(contract) ;
		System.out.println(readWallet(contract, wallet.getId(), wallet.getSecret()));
		transferTo(contract,wallet.getId(),ammount);
		System.out.println(getBalance(contract, wallet.getId(), wallet.getSecret()) );
	}
}
