package org.annpurna.wallet.cli.exception;

public class AnnpurnaServiceException extends Exception {
	private ErrorCode errorCode ;
	
	public AnnpurnaServiceException(Exception e) {
		super(e);
	}
	
	public ErrorCode getErrorCode() {
		return errorCode;
	}



	public void setErrorCode(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}



	public enum ErrorCode {
		NOT_FOUND_ERROR("Not Found"),
		AUTHENTICATION_ERROR("Authentication Failure"),
		AUTHORIZATION_ERROR("Authorization Exception"),
		UNEXPECTED_ERROR("Unexpected Exception");
		
		private String errorMessage ;
		
		
		ErrorCode(String errorMsg){
			this.errorMessage = errorMsg;
		}


		public String getErrorMessage() {
			return errorMessage;
		}


		public void setErrorMessage(String errorMessage) {
			this.errorMessage = errorMessage;
		}
		
	}

}
