package org.annpurna.wallet.cli.api.handler;

import org.annpurna.wallet.cli.util.ResponseUtil;
import org.annpurna.wallet.cli.exception.AnnpurnaServiceException;
import org.annpurna.wallet.cli.exception.AnnpurnaServiceException.ErrorCode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.annpurna.wallet.cli.api.model.*;
import org.annpurna.wallet.cli.api.handler.WalletApiService;
import org.annpurna.wallet.cli.api.handler.factories.WalletApiServiceFactory;

import io.swagger.annotations.ApiParam;
import io.swagger.jaxrs.*;

import javax.servlet.http.HttpServletRequest;

import java.math.BigDecimal;
import org.annpurna.wallet.cli.api.model.Tx;
import org.annpurna.wallet.cli.api.model.Wallet;

import java.util.Map;
import java.util.List;
import org.annpurna.wallet.cli.api.handler.NotFoundException;

import java.io.InputStream;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.container.ResourceInfo;
import javax.servlet.ServletConfig;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.*;
import javax.validation.constraints.*;

@Path("/wallet")


@io.swagger.annotations.Api(description = "the wallet API")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2021-12-19T23:11:31.868+05:30")
public class WalletApi  {
   private static final Logger LOGGER = LoggerFactory.getLogger(WalletApi.class);
   private final WalletApiService delegate;

   public WalletApi(@Context ServletConfig servletContext,@Context SecurityContext securityContext) {
      WalletApiService delegate = null;

      if (servletContext != null) {
         String implClass = servletContext.getInitParameter("WalletApi.implementation");
         if (implClass != null && !"".equals(implClass.trim())) {
            try {
               delegate = (WalletApiService) Class.forName(implClass).newInstance();
            } catch (Exception e) {
               throw new RuntimeException(e);
            }
         } 
      }

      if (delegate == null) {
         delegate = WalletApiServiceFactory.getWalletApi();
      }

      this.delegate = delegate;
   }

    @PUT
    @Path("/funds")
    
    @Produces({ "application/json" })  
    @io.swagger.annotations.ApiOperation(value = "Add funds Api.", notes = "Api to add funds to wallet.", response = Wallet.class, authorizations = {
        @io.swagger.annotations.Authorization(value = "action", scopes = {
            @io.swagger.annotations.AuthorizationScope(scope = "UPDATE", description = "Update Meta Operation.")
        }),
        @io.swagger.annotations.Authorization(value = "resource", scopes = {
            @io.swagger.annotations.AuthorizationScope(scope = "WALLET", description = "Wallet Operations.")
        })
    }, tags={ "Wallet", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful Operation.", response = Wallet.class),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized.", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Resource Unavailable.", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Error.", response = Void.class) })
    public Response addFunds(@ApiParam(value = "Ammount to be added in funds.") @QueryParam("fund") BigDecimal fund
,@Context SecurityContext securityContext, @Context HttpServletRequest httpServletRequest, @Context ResourceInfo resourceInfo)
    throws NotFoundException {
    	try {
    			LOGGER.info("Request received for {}","Add funds Api.");
    					
        		Response response =  delegate.addFunds(fund,securityContext,httpServletRequest);
        		LOGGER.info("Request processed successfully for {}","Add funds Api.");
        		return response;
        	} catch (AnnpurnaServiceException e){
        		LOGGER.error("Error Occurred while processing Add funds Api. ["+ e.getMessage() + "]",e);
        		return ResponseUtil.getErrorResponse(e);
        	} catch (Throwable e){
        		LOGGER.error("Error Occurred while processing Add funds Api. ["+ e.getMessage() + "]",e);
        		return ResponseUtil.getErrorResponse(e);
        	}
    }
    @GET
    @Path("/balance")
    
    @Produces({ "application/json" })  
    @io.swagger.annotations.ApiOperation(value = "Transfer funds Api.", notes = "Api to transfer funds to user wallet.", response = BigDecimal.class, authorizations = {
        @io.swagger.annotations.Authorization(value = "action", scopes = {
            @io.swagger.annotations.AuthorizationScope(scope = "READ", description = "Get Opertion.")
        }),
        @io.swagger.annotations.Authorization(value = "resource", scopes = {
            @io.swagger.annotations.AuthorizationScope(scope = "WALLET", description = "Wallet Operations.")
        })
    }, tags={ "Wallet", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful Operation.", response = BigDecimal.class),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized.", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Resource Unavailable.", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Error.", response = Void.class) })
    public Response balance(@ApiParam(value = "User secret." ,required=true)@HeaderParam("x-user-secret") String xUserSecret
,@Context SecurityContext securityContext, @Context HttpServletRequest httpServletRequest, @Context ResourceInfo resourceInfo)
    throws NotFoundException {
    	try {
    			LOGGER.info("Request received for {}","Transfer funds Api.");
    					
        		Response response =  delegate.balance(xUserSecret,securityContext,httpServletRequest);
        		LOGGER.info("Request processed successfully for {}","Transfer funds Api.");
        		return response;
        	} catch (AnnpurnaServiceException e){
        		LOGGER.error("Error Occurred while processing Transfer funds Api. ["+ e.getMessage() + "]",e);
        		return ResponseUtil.getErrorResponse(e);
        	} catch (Throwable e){
        		LOGGER.error("Error Occurred while processing Transfer funds Api. ["+ e.getMessage() + "]",e);
        		return ResponseUtil.getErrorResponse(e);
        	}
    }
    @POST
    @Path("/createPartner/{mspId}")
    
    @Produces({ "application/json" })  
    @io.swagger.annotations.ApiOperation(value = "Create partner wallet Api.", notes = "Create partner wallet api.", response = Wallet.class, authorizations = {
        @io.swagger.annotations.Authorization(value = "action", scopes = {
            @io.swagger.annotations.AuthorizationScope(scope = "CREATE", description = "Update Meta Operation.")
        }),
        @io.swagger.annotations.Authorization(value = "resource", scopes = {
            @io.swagger.annotations.AuthorizationScope(scope = "WALLET", description = "Wallet Operations.")
        })
    }, tags={ "Wallet", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful Operation.", response = Wallet.class),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized.", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Resource Unavailable.", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Error.", response = Void.class) })
    public Response createPartnerWallet(@ApiParam(value = "member mspId.",required=true) @PathParam("mspId") String mspId
,@Context SecurityContext securityContext, @Context HttpServletRequest httpServletRequest, @Context ResourceInfo resourceInfo)
    throws NotFoundException {
    	try {
    			LOGGER.info("Request received for {}","Create partner wallet Api.");
    					
        		Response response =  delegate.createPartnerWallet(mspId,securityContext,httpServletRequest);
        		LOGGER.info("Request processed successfully for {}","Create partner wallet Api.");
        		return response;
        	} catch (AnnpurnaServiceException e){
        		LOGGER.error("Error Occurred while processing Create partner wallet Api. ["+ e.getMessage() + "]",e);
        		return ResponseUtil.getErrorResponse(e);
        	} catch (Throwable e){
        		LOGGER.error("Error Occurred while processing Create partner wallet Api. ["+ e.getMessage() + "]",e);
        		return ResponseUtil.getErrorResponse(e);
        	}
    }
    @POST
    
    
    @Produces({ "application/json" })  
    @io.swagger.annotations.ApiOperation(value = "Wallet Api.", notes = "Create wallet api.", response = Wallet.class, authorizations = {
        @io.swagger.annotations.Authorization(value = "action", scopes = {
            @io.swagger.annotations.AuthorizationScope(scope = "CREATE", description = "Update Meta Operation.")
        }),
        @io.swagger.annotations.Authorization(value = "resource", scopes = {
            @io.swagger.annotations.AuthorizationScope(scope = "WALLET", description = "Wallet Operations.")
        })
    }, tags={ "Wallet", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful Operation.", response = Wallet.class),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized.", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Resource Unavailable.", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Error.", response = Void.class) })
    public Response createWallet(@Context SecurityContext securityContext, @Context HttpServletRequest httpServletRequest, @Context ResourceInfo resourceInfo)
    throws NotFoundException {
    	try {
    			LOGGER.info("Request received for {}","Wallet Api.");
    					
        		Response response =  delegate.createWallet(securityContext,httpServletRequest);
        		LOGGER.info("Request processed successfully for {}","Wallet Api.");
        		return response;
        	} catch (AnnpurnaServiceException e){
        		LOGGER.error("Error Occurred while processing Wallet Api. ["+ e.getMessage() + "]",e);
        		return ResponseUtil.getErrorResponse(e);
        	} catch (Throwable e){
        		LOGGER.error("Error Occurred while processing Wallet Api. ["+ e.getMessage() + "]",e);
        		return ResponseUtil.getErrorResponse(e);
        	}
    }
    @GET
    
    
    @Produces({ "application/json" })  
    @io.swagger.annotations.ApiOperation(value = "Read wallet Api.", notes = "Read wallet api.", response = Wallet.class, authorizations = {
        @io.swagger.annotations.Authorization(value = "action", scopes = {
            @io.swagger.annotations.AuthorizationScope(scope = "READ", description = "Get Opertion.")
        }),
        @io.swagger.annotations.Authorization(value = "resource", scopes = {
            @io.swagger.annotations.AuthorizationScope(scope = "WALLET", description = "Wallet Operations.")
        })
    }, tags={ "Wallet", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful Operation.", response = Wallet.class),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized.", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Resource Unavailable.", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Error.", response = Void.class) })
    public Response readWallet(@ApiParam(value = "User secret." ,required=true)@HeaderParam("x-user-secret") String xUserSecret
,@Context SecurityContext securityContext, @Context HttpServletRequest httpServletRequest, @Context ResourceInfo resourceInfo)
    throws NotFoundException {
    	try {
    			LOGGER.info("Request received for {}","Read wallet Api.");
    					
        		Response response =  delegate.readWallet(xUserSecret,securityContext,httpServletRequest);
        		LOGGER.info("Request processed successfully for {}","Read wallet Api.");
        		return response;
        	} catch (AnnpurnaServiceException e){
        		LOGGER.error("Error Occurred while processing Read wallet Api. ["+ e.getMessage() + "]",e);
        		return ResponseUtil.getErrorResponse(e);
        	} catch (Throwable e){
        		LOGGER.error("Error Occurred while processing Read wallet Api. ["+ e.getMessage() + "]",e);
        		return ResponseUtil.getErrorResponse(e);
        	}
    }
    @PUT
    @Path("/transfer")
    
    @Produces({ "application/json" })  
    @io.swagger.annotations.ApiOperation(value = "Transfer funds Api.", notes = "Api to transfer funds to wallet.", response = Wallet.class, authorizations = {
        @io.swagger.annotations.Authorization(value = "action", scopes = {
            @io.swagger.annotations.AuthorizationScope(scope = "UPDATE", description = "Update Meta Operation.")
        }),
        @io.swagger.annotations.Authorization(value = "resource", scopes = {
            @io.swagger.annotations.AuthorizationScope(scope = "WALLET", description = "Wallet Operations.")
        })
    }, tags={ "Wallet", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful Operation.", response = Wallet.class),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized.", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Resource Unavailable.", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Error.", response = Void.class) })
    public Response transfer(@ApiParam(value = "Transaction details." ,required=true) Tx tx
,@Context SecurityContext securityContext, @Context HttpServletRequest httpServletRequest, @Context ResourceInfo resourceInfo)
    throws NotFoundException {
    	try {
    			LOGGER.info("Request received for {}","Transfer funds Api.");
    					
        		Response response =  delegate.transfer(tx,securityContext,httpServletRequest);
        		LOGGER.info("Request processed successfully for {}","Transfer funds Api.");
        		return response;
        	} catch (AnnpurnaServiceException e){
        		LOGGER.error("Error Occurred while processing Transfer funds Api. ["+ e.getMessage() + "]",e);
        		return ResponseUtil.getErrorResponse(e);
        	} catch (Throwable e){
        		LOGGER.error("Error Occurred while processing Transfer funds Api. ["+ e.getMessage() + "]",e);
        		return ResponseUtil.getErrorResponse(e);
        	}
    }
    @PUT
    @Path("/transferTo")
    
    @Produces({ "application/json" })  
    @io.swagger.annotations.ApiOperation(value = "Transfer funds Api.", notes = "Api to transfer funds to user wallet.", response = Wallet.class, authorizations = {
        @io.swagger.annotations.Authorization(value = "action", scopes = {
            @io.swagger.annotations.AuthorizationScope(scope = "UPDATE", description = "Update Meta Operation.")
        }),
        @io.swagger.annotations.Authorization(value = "resource", scopes = {
            @io.swagger.annotations.AuthorizationScope(scope = "WALLET", description = "Wallet Operations.")
        })
    }, tags={ "Wallet", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful Operation.", response = Wallet.class),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized.", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Resource Unavailable.", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Error.", response = Void.class) })
    public Response transferTo(@ApiParam(value = "Transaction details." ,required=true) Tx tx
,@Context SecurityContext securityContext, @Context HttpServletRequest httpServletRequest, @Context ResourceInfo resourceInfo)
    throws NotFoundException {
    	try {
    			LOGGER.info("Request received for {}","Transfer funds Api.");
    					
        		Response response =  delegate.transferTo(tx,securityContext,httpServletRequest);
        		LOGGER.info("Request processed successfully for {}","Transfer funds Api.");
        		return response;
        	} catch (AnnpurnaServiceException e){
        		LOGGER.error("Error Occurred while processing Transfer funds Api. ["+ e.getMessage() + "]",e);
        		return ResponseUtil.getErrorResponse(e);
        	} catch (Throwable e){
        		LOGGER.error("Error Occurred while processing Transfer funds Api. ["+ e.getMessage() + "]",e);
        		return ResponseUtil.getErrorResponse(e);
        	}
    }
}

