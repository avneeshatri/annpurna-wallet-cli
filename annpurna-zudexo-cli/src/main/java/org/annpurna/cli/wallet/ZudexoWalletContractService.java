package org.annpurna.cli.wallet;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

import org.annpurna.cli.common.utils.CommonUtils;
import org.annpurna.cli.common.utils.ResourceAdapter;
import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.ContractException;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Gateway.Builder;
import org.hyperledger.fabric.gateway.Network;
import org.hyperledger.fabric.gateway.Transaction;
import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.Wallets;
import org.hyperledger.fabric.gateway.impl.ContractImpl;
import org.hyperledger.fabric.gateway.impl.CustomTransactionImpl;
import org.hyperledger.fabric.gateway.impl.TransactionImpl;
import org.hyperledger.fabric.sdk.ProposalResponse;
import org.hyperledger.fabric.sdk.SDKUtils;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;

import com.annpurna.cli.common.model.AnnpurnaWallet;
import com.annpurna.cli.common.util.JsonParser;
import com.annpurna.wallet.crypto.CryptoUtil;
import com.sun.tools.javac.code.Attribute.Array;

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
				Contract contract = network.getContract(chaincodeId,contractName);
				
				contract.addContractListener(new AnnpurnaWalletEventListner());
				
				setUpZudexo(contract,100000L);
				System.out.println(getBalance(contract, ZudexoMSP, ""));
				setUpZiggy(contract, 10000L);
				System.out.println(getBalance(contract, ZudexoMSP, ""));
				// Create wallet
				AnnpurnaWallet wallet = createWallet(contract);
				System.out.println("wallet :"+wallet);
				
				// Read wallet
				System.out.println(readWallet(contract, wallet.getId(), wallet.getSecret()));
			
				System.out.println(getBalance(contract, wallet.getId(), wallet.getSecret()));
				
				
				
				System.out.println(getBalance(contract, ZudexoMSP, ""));
				
				transaction(contract, 100L);
				
				System.out.println(getBalance(contract, "ZudexoMSP", ""));
				
				
				List<AnnpurnaWallet> history = getWalletHistory(contract,wallet.getSecret()) ;// getWalletHistory(contract,"MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCQYbgrfUF9xQptDhiKJervWiJRVh7eTLf6vGvKRTcbpY24jllcECw2paChVxPMxcQWhaQ0fLo8qsgeZ/KRYZSOkeFsiEiLzvy2idDkfHBr6Vgt4MDmD8N3FKqOUJyhM6h0mBhIyN3A2MZygvbyKmcufge7s/ZSoNWzSSjSmn3ZqejZrlqu1mXCP4vzal75pitnEzytHLi2RIRopReLLEyGt3SPjQCo6R3UIMmjhhm2hJHbk953MVwrtsjDcpocl+0+240D45uyy/1Z8rJwTZuCtQ8pX3kKaEDefaEQLL28pZr1SLC2IA0Lb2Kn3jef02uFcw0Xi9IuzAYbzkolsc4DAgMBAAECggEAcSYjzvE6gfYJVa3WTW2p0Coy+ssjJdO52yYO01Wq+l+j0R69qQiDnc/vMoGrYq8aHQxew0N8ME8mxa1wy75NTe3FW+jx8Z8lLWiT7HwZHnigQlQA2EtCQ/BOOXmzRiHVwonvfhrsrlU0MEmyfdX9RtI9/TvrrpWc5ARRmbq5JfpqW8fp7IOFsyki8HsfKIkpXQEJpxvxPEnzxnLhFIIa5X1/Z5pn+YjPIYjVqL+gTodcEM4Chrm64nUBWM31o5hgcYRoieGBUCcxW35Ebk620xlQ/Lg46yq29FEHEDyXyDbnYUOYbUQCWrfEcQrszg/Z3UwmyNEV2EBDoj+J+qIfAQKBgQD58YhEdRjVPhRn5M/OQa1n+EiFUvGwaXIFRV97wactEXINrnmEPH8G+vStJgUvqhK8BhCo0py/V7kIpKZcJxfMc4u/cBe7ti9OHx2v24y8eXtq03rfrJiNP4BC82nQ13j5eeUPXWGx5VEdQmv+S9sW/QL6MmwQ//FFhzmwYuP/5wKBgQCT4VvPduwMVf+lcdLKmXcor39l9z/OR74AF8EvSg/40iOQKiifQUPs2Q2H6Bqg7STnGWj0doDTjl6MiE/CQf3Lki+dBIOQoGC0UgqaXSbEY+auRjdFxU9K1VcH0NIJvbRJ9GVqjbdCl42be7Mm3FxMVTUyiAQCDbo8Baa5DWPthQKBgQDI9J0vDUbODH+zmJBLsRqOwVztdObFg8Ic4s4GXoje/2vH/EPPQiSPpC4dGLiKvh8Z9XZnhg5o+UrX3Cm1ZekIhM4TOTibv7sBAJuJzOe2kaXdGfNs6wQsbKXfLn4hX1zwmETGubnWoh05fTH+31ZjgUFnkCq68E04LaUAYEIYeQKBgEtNSk8Oo4x0jkVU/J2mIANbumfVy2vjJGEvt3O5JwDxIKLclOrjdQ86R6WZD1Y667uyOLujTF5t9L9i+hpVOENdIVm3Xrdas2OfEz8fiAohROohXyvsGUBl+ndEYnALZa7zAGGN4G/MdJNkHgn/1BAao4gjssbI7rh5uDgrFWNxAoGAbSM+XiSp3aa4Q1ha+dPz6tt+sdLTeqa+V5ZkF3ynRZORYXf9vDtbuL49vCX625NZkM97D7t5BAT/J4U24MOYTaskzKMazB6eQ+ihnfXvlWVVb9OdY6BOp9Kp+BGYSkS9H6or6IJPpLC1KPU077oKQN3XafOM08INY0V5LTa2Lh8=");
				System.out.println("History:"+history);
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
		JsonParser.deserialize(CommonUtils.deserialize(response),AnnpurnaWallet.class);
		return wallet ;
	}
	
	private static AnnpurnaWallet createPartnerWallet(Contract contract , String orgMspId) throws Exception {
		System.out.println("Submit create partner wallet transaction.");
		
		byte[] response = submit(contract,"CreatePartnerWallet", orgMspId);
		return JsonParser.deserialize(CommonUtils.deserialize(response),AnnpurnaWallet.class);
	}

	
	private static AnnpurnaWallet addFunds(Contract contract , long value) throws Exception {
		System.out.println("Submit add funds transaction.");
		
		byte[] response = submit(contract,"AddFunds", String.valueOf(value));
		return JsonParser.deserialize(CommonUtils.deserialize(response),AnnpurnaWallet.class);
	}
	
	
	private static List<AnnpurnaWallet> getWalletHistory(Contract contract , String secret) throws Exception {
		System.out.println("Submit get wallet history transaction.");
		
		String walletId = CryptoUtil.generateWalletId(secret) ;

		byte[] signature = CryptoUtil.signWithPrivatekey(walletId.getBytes(), 
				CryptoUtil.generatePKCS8EncodedPrivateKey(CryptoUtil.base64Decoded(secret)));
		String userSign = CryptoUtil.base64EndoedString(signature) ;
		byte[] response = contract.submitTransaction("GetWalletHistory", walletId, userSign);
		String responseStr = CommonUtils.deserialize(response);
		System.out.println("Wallet:"+responseStr);
		List<AnnpurnaWallet> list = Arrays.asList(JsonParser.deserialize(responseStr,AnnpurnaWallet[].class));
        return list;
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
		byte[] response = contract.submitTransaction("BalanceOf", walletId , userSign );
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
		byte[] response = contract.submitTransaction("TransferTo", recipeient, String.valueOf(ammount));
		return JsonParser.deserialize(CommonUtils.deserialize(response),String.class);
	}
	
	private static void transaction(Contract contract,long ammount) throws Exception {
		AnnpurnaWallet wallet =createWallet(contract) ;
		System.out.println(readWallet(contract, wallet.getId(), wallet.getSecret()));
		transferTo(contract,wallet.getId(),ammount);
		System.out.println(getBalance(contract, wallet.getId(), wallet.getSecret()) );
	}
	
    public static byte[] submit(Contract contract ,String name, final String... args) throws ContractException, TimeoutException, InterruptedException {
    	CustomTransactionImpl tx = createTransaction(contract,name) ;

    	return tx.submit(args) ;

    }
  
    public  static CustomTransactionImpl createTransaction(Contract contract ,final String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Transaction must be a non-empty string");
        }
        String qualifiedName = getQualifiedName("",name);
        return new CustomTransactionImpl((ContractImpl) contract, qualifiedName);
    }
    
    private static String getQualifiedName(String name,final String tname) {
        return name.isEmpty() ? tname : name + ':' + tname;
    }


}
