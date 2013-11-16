package com.intuit.psd.risk.processor;

import java.io.IOError;

import junit.framework.Assert;

import org.testng.annotations.Test;

import com.intuit.psd.risk.processor.exceptions.RiskException;

public class RiskProcessorResponseTest {
	
	@Test
	public void testGetterAndSetterMethods() throws Exception {
		RiskProcessorResponse riskProcessorResponse = new RiskProcessorResponse(); 
		IOError ioError = new IOError(new Throwable());
		String errorMessage = "test error message";
		RiskException exception = new RiskException("test risk exception");
		
		riskProcessorResponse.setError(ioError);
		riskProcessorResponse.setErrorCode(123);
		riskProcessorResponse.setErrorMessage(errorMessage);
		riskProcessorResponse.setException(exception);
		
		Assert.assertEquals(riskProcessorResponse.getError(), ioError);
		Assert.assertEquals(riskProcessorResponse.getErrorCode(), 123);
		Assert.assertEquals(riskProcessorResponse.getErrorMessage(), errorMessage);
		Assert.assertEquals(riskProcessorResponse.getException(), exception);
	}

}
