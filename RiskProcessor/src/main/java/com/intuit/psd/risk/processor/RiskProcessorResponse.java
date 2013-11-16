package com.intuit.psd.risk.processor;

/**
 * This is the error class that is used to encapsulate errorMessage, errorCode and exception details
 * into a single object that will be returned to the wrapper in STORM.
 * 
 * @author asookazian
 *
 */
public class RiskProcessorResponse implements java.io.Serializable {
	
	private static final long serialVersionUID = 3724935023646994234L;
	private String errorMessage;
	private int errorCode;
	private Throwable error;	private Exception exception ;
	
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	public int getErrorCode() {
		return errorCode;	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}	
	public Throwable getError() {
		return error;
	}
	public void setError(Throwable error) {
		this.error = error;
	}		public Exception getException() {		return exception;	}	public void setException(Exception exception) {		this.exception = exception;	}	
}
