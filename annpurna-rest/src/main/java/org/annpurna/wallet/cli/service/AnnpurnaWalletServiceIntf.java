package org.annpurna.wallet.cli.service;

import java.util.List;

import org.annpurna.wallet.cli.api.model.Wallet;
import org.annpurna.wallet.cli.exception.AnnpurnaServiceException;


public interface AnnpurnaWalletServiceIntf {

	Wallet createWallet() throws AnnpurnaServiceException ;
	Wallet createPartnerWallet(String mspId) throws AnnpurnaServiceException ;
	boolean transferTo(String walletId,Long value) throws AnnpurnaServiceException ;
	Wallet readWallet(String secret)throws AnnpurnaServiceException ;
	void addFunds(Long value) throws AnnpurnaServiceException ;
	Long getBalance(String secret)throws AnnpurnaServiceException ;
	void transfer(String senderSecret, String recipientWalletId, long amount )throws AnnpurnaServiceException ;
	List<Wallet> getWalletHistory(String secret) throws AnnpurnaServiceException ;
}
