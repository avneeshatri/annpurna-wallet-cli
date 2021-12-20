package org.annpurna.wallet.cli.api.handler.impl;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.annpurna.wallet.cli.api.handler.ApiResponseMessage;
import org.annpurna.wallet.cli.api.handler.NotFoundException;
import org.annpurna.wallet.cli.api.handler.WalletApiService;
import org.annpurna.wallet.cli.api.model.Tx;
import org.annpurna.wallet.cli.api.model.Wallet;
import org.annpurna.wallet.cli.exception.AnnpurnaServiceException;
import org.annpurna.wallet.cli.service.impl.AnnpurnaWalletServiceImplementation;
import org.annpurna.wallet.cli.util.ResponseUtil;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2021-12-06T20:05:59.369+05:30")
public class WalletApiServiceImplementation extends WalletApiService {
	
	private AnnpurnaWalletServiceImplementation walletServiceImpl ;
	
	public WalletApiServiceImplementation(){
		walletServiceImpl = new AnnpurnaWalletServiceImplementation();
	}
	
    @Override
    public Response addFunds( BigDecimal fund, SecurityContext securityContext,HttpServletRequest httpServletRequest) throws NotFoundException , AnnpurnaServiceException {
    	walletServiceImpl.addFunds(fund.longValue());
        return ResponseUtil.getSuccessResponse("Funds added", null);
    }
    @Override
    public Response balance(String secret, SecurityContext securityContext,HttpServletRequest httpServletRequest) throws NotFoundException , AnnpurnaServiceException {
    	Long balance = walletServiceImpl.getBalance(secret);
        return ResponseUtil.getSuccessResponse("User balance", balance);
    }
    @Override
    public Response createPartnerWallet(String mspId ,SecurityContext securityContext,HttpServletRequest httpServletRequest) throws NotFoundException , AnnpurnaServiceException {
        Wallet wallet = walletServiceImpl.createPartnerWallet(mspId);
        return ResponseUtil.getSuccessResponse("Partner Wallet created", wallet);
    }
    @Override
    public Response createWallet(SecurityContext securityContext,HttpServletRequest httpServletRequest) throws NotFoundException , AnnpurnaServiceException {
    	Wallet wallet = walletServiceImpl.createWallet();
        return ResponseUtil.getSuccessResponse("Wallet created", wallet);
    }
    @Override
    public Response readWallet(String secret, SecurityContext securityContext,HttpServletRequest httpServletRequest) throws NotFoundException , AnnpurnaServiceException {
    	Wallet wallet = walletServiceImpl.readWallet(secret);
        return ResponseUtil.getSuccessResponse("Wallet Fetched", wallet);
    }
    @Override
    public Response transfer(Tx tx, SecurityContext securityContext,HttpServletRequest httpServletRequest) throws NotFoundException , AnnpurnaServiceException {
    	walletServiceImpl.transfer(tx.getSenderSecret() , tx.getRecipientWalletId(), tx.getAmount().longValue());
    	return ResponseUtil.getSuccessResponse("Transfer successfull", null);
    }
    @Override
    public Response transferTo(Tx tx, SecurityContext securityContext,HttpServletRequest httpServletRequest) throws NotFoundException , AnnpurnaServiceException {
    	walletServiceImpl.transferTo(tx.getRecipientWalletId(), tx.getAmount().longValue());
    	return ResponseUtil.getSuccessResponse("Transfer to successfull", null);
    }
}
