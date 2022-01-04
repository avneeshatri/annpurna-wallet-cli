package org.annpurna.wallet.cli.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.annpurna.cli.common.contract.AnnpurnaWalletOperations;
import org.annpurna.cli.common.utils.ResourceAdapter;
import org.annpurna.wallet.cli.api.model.Wallet;
import org.annpurna.wallet.cli.exception.AnnpurnaServiceException;
import org.annpurna.wallet.cli.service.AnnpurnaWalletServiceIntf;

import com.annpurna.cli.common.model.AnnpurnaWallet;

public class AnnpurnaWalletServiceImplementation implements AnnpurnaWalletServiceIntf {
	
	private AnnpurnaWalletOperations hlfWalletContractOps ;
	
	public AnnpurnaWalletServiceImplementation() {
		hlfWalletContractOps = new AnnpurnaWalletOperations();
	}
	
	
	@Override
	public Wallet createPartnerWallet(String mspId) throws AnnpurnaServiceException{
		// TODO Auto-generated method stub
		try {
			return transformToWallet(hlfWalletContractOps.createPartnerWallet(mspId) );
		} catch (Exception e) {
			throw new AnnpurnaServiceException(e);
		}
	}

	@Override
	public boolean transferTo(String walletId, Long value) throws AnnpurnaServiceException {
		// TODO Auto-generated method stub
		try {
			hlfWalletContractOps.transferTo(walletId, value);
			return true ;
		} catch (Exception e) {
			throw new AnnpurnaServiceException(e);
		}
	}

	@Override
	public Wallet readWallet(String secret) throws AnnpurnaServiceException {
		try {
			return transformToWallet(hlfWalletContractOps.readWallet(secret) );
		} catch (Exception e) {
			throw new AnnpurnaServiceException(e);
		}
	}

	@Override
	public void addFunds(Long value) throws AnnpurnaServiceException {
		// TODO Auto-generated method stub
		try {
			hlfWalletContractOps.addFunds(value);
		} catch (Exception e) {
			throw new AnnpurnaServiceException(e);
		}
		
	}

	@Override
	public Long getBalance(String secret) throws AnnpurnaServiceException {
		// TODO Auto-generated method stub
		try {
			return hlfWalletContractOps.getBalance(secret) ;
		} catch (Exception e) {
			throw new AnnpurnaServiceException(e);
		}
	}

	@Override
	public void transfer(String senderSecret, String recipientWalletId, long amount) throws AnnpurnaServiceException {
		// TODO Auto-generated method stub
		try {
			hlfWalletContractOps.transfer(senderSecret, recipientWalletId, amount); ;
		} catch (Exception e) {
			throw new AnnpurnaServiceException(e);
		}
	}
	
	@Override
	public Wallet createWallet() throws AnnpurnaServiceException{
		// TODO Auto-generated method stub
		try {
			return transformToWallet(hlfWalletContractOps.createWallet() );
		} catch (Exception e) {
			throw new AnnpurnaServiceException(e);
		}
	}
	private static Wallet transformToWallet (AnnpurnaWallet wallet) {
		Wallet w = new Wallet();
		w.setId(wallet.getId());
		w.setCreatedBy(wallet.getCreatedBy());
		w.setCreatedOn(new Date(wallet.getCreatedOn()));
		w.setSecret(wallet.getSecret());
		if(wallet.getValue()!=null)
			w.setBalance(BigDecimal.valueOf(wallet.getValue()));
		
		return w;
	}

	private static List<Wallet> transformToWallet (List<AnnpurnaWallet> wallets) {
		List<Wallet> wlist = new ArrayList<Wallet>();
		for(AnnpurnaWallet wallet : wallets) {
			wlist.add(transformToWallet(wallet));
		}
		
		return wlist;
	}

	@Override
	public List<Wallet> getWalletHistory(String secret) throws AnnpurnaServiceException {
		try {
			return transformToWallet(hlfWalletContractOps.getWalletHistory(secret));
		} catch (Exception e) {
			throw new AnnpurnaServiceException(e);
		}
	}
}
