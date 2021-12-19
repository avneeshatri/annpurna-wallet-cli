package org.annpurna.cli.wallet;

import  org.hyperledger.fabric.gateway.ContractEvent;

import java.util.function.Consumer; 

public class AnnpurnaWalletEventListner implements Consumer<ContractEvent> {

	@Override
	public void accept(ContractEvent t) {
		// TODO Auto-generated method stub
		System.out.println("Evnet Name:"+t.getName()) ;
		System.out.println("Chaincode Id:"+t.getChaincodeId()) ;
		System.out.println("Transaction Event:"+t.getTransactionEvent()) ;
		System.out.println("Payload:"+new String(t.getPayload().get(),java.nio.charset.Charset.forName("UTF-8"))) ;
		
	}

}
