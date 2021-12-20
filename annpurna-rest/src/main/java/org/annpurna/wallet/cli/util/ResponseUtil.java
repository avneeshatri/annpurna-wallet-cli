package org.annpurna.wallet.cli.util;



import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.annpurna.wallet.cli.exception.AnnpurnaServiceException;
import org.annpurna.wallet.cli.exception.AnnpurnaServiceException.ErrorCode;
import org.annpurna.wallet.cli.model.AnnpurnaApiResponse;

public class ResponseUtil {

	public static Response getErrorResponse(AnnpurnaServiceException e){

		AnnpurnaApiResponse apiResponse = new AnnpurnaApiResponse() ;
		apiResponse.setStatus(AnnpurnaApiResponse.FAILED);
		apiResponse.setMsg(e.getErrorCode().getErrorMessage());
		
		ResponseBuilder responseBuilder = Response.serverError().entity(apiResponse) ;
		
		if(e.getErrorCode() == ErrorCode.AUTHENTICATION_ERROR) {
			responseBuilder.status(Status.UNAUTHORIZED) ;
		} else if(e.getErrorCode() == ErrorCode.AUTHORIZATION_ERROR) {
			responseBuilder.status(Status.FORBIDDEN) ;
		} else if(e.getErrorCode() == ErrorCode.NOT_FOUND_ERROR) {
			responseBuilder.status(Status.NOT_FOUND) ;
		} else if(e.getErrorCode() == ErrorCode.UNEXPECTED_ERROR) {
			responseBuilder.status(Status.INTERNAL_SERVER_ERROR) ;
		} else if (e.getErrorCode() != null) {
			responseBuilder.status(Status.INTERNAL_SERVER_ERROR) ;
		} else {
			responseBuilder.status(Status.INTERNAL_SERVER_ERROR) ;
		}
		return responseBuilder.build() ;
	}
	
	public static Response getErrorResponse(Throwable e){
		AnnpurnaApiResponse apiResponse = new AnnpurnaApiResponse() ;
		apiResponse.setStatus(AnnpurnaApiResponse.FAILED);
		apiResponse.setMsg(e.getMessage());
		return Response.serverError().entity(apiResponse).build() ;
	}
	
	public static Response getSuccessResponse(String message, Object data) {
		AnnpurnaApiResponse apiResponse = new AnnpurnaApiResponse() ;
		apiResponse.setStatus(AnnpurnaApiResponse.SUCCESS);
		apiResponse.setMsg(message);
		apiResponse.setData(data);
		
		return Response.ok().entity(apiResponse).build() ;
	}
	
	
}
