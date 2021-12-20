package org.annpurna.wallet.cli.api.handler;

import org.annpurna.wallet.cli.util.ResponseUtil;
import org.annpurna.wallet.cli.exception.AnnpurnaServiceException;
import org.annpurna.wallet.cli.exception.AnnpurnaServiceException.ErrorCode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.annpurna.wallet.cli.api.model.*;
import org.annpurna.wallet.cli.api.handler.HealthApiService;
import org.annpurna.wallet.cli.api.handler.factories.HealthApiServiceFactory;

import io.swagger.annotations.ApiParam;
import io.swagger.jaxrs.*;

import javax.servlet.http.HttpServletRequest;


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

@Path("/health")


@io.swagger.annotations.Api(description = "the health API")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2021-12-19T23:11:31.868+05:30")
public class HealthApi  {
   private static final Logger LOGGER = LoggerFactory.getLogger(HealthApi.class);
   private final HealthApiService delegate;

   public HealthApi(@Context ServletConfig servletContext,@Context SecurityContext securityContext) {
      HealthApiService delegate = null;

      if (servletContext != null) {
         String implClass = servletContext.getInitParameter("HealthApi.implementation");
         if (implClass != null && !"".equals(implClass.trim())) {
            try {
               delegate = (HealthApiService) Class.forName(implClass).newInstance();
            } catch (Exception e) {
               throw new RuntimeException(e);
            }
         } 
      }

      if (delegate == null) {
         delegate = HealthApiServiceFactory.getHealthApi();
      }

      this.delegate = delegate;
   }

    @GET
    
    
    @Produces({ "application/json" })  
    @io.swagger.annotations.ApiOperation(value = "Health Check Api.", notes = "Health check api.", response = String.class, authorizations = {
        @io.swagger.annotations.Authorization(value = "action", scopes = {
            @io.swagger.annotations.AuthorizationScope(scope = "READ", description = "Get Opertion.")
        }),
        @io.swagger.annotations.Authorization(value = "resource", scopes = {
            @io.swagger.annotations.AuthorizationScope(scope = "HEALTH", description = "Health check Opertion.")
        })
    }, tags={ "Health", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful Operation.", response = String.class),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized.", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Resource Unavailable.", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Error.", response = Void.class) })
    public Response health(@Context SecurityContext securityContext, @Context HttpServletRequest httpServletRequest, @Context ResourceInfo resourceInfo)
    throws NotFoundException {
    	try {
    			LOGGER.info("Request received for {}","Health Check Api.");
    					
        		Response response =  delegate.health(securityContext,httpServletRequest);
        		LOGGER.info("Request processed successfully for {}","Health Check Api.");
        		return response;
        	} catch (AnnpurnaServiceException e){
        		LOGGER.error("Error Occurred while processing Health Check Api. ["+ e.getMessage() + "]",e);
        		return ResponseUtil.getErrorResponse(e);
        	} catch (Throwable e){
        		LOGGER.error("Error Occurred while processing Health Check Api. ["+ e.getMessage() + "]",e);
        		return ResponseUtil.getErrorResponse(e);
        	}
    }
}

