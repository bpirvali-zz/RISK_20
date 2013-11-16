package com.intuit.psd.risk.processor.utilities;
import java.text.SimpleDateFormat;
import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.ParameterException;
public class DateConverter implements IStringConverter<java.util.Date> {	
	@Override
	public java.util.Date convert(String value) {		
		java.util.Date date = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");		dateFormat.setLenient(false);
		try {
			date = dateFormat.parse(value);
		} catch (Exception e) {
			throw new ParameterException("Invalid date " + value);
		}
		/*if (!dateFormat.format(date).equals(value)) {
			throw new ParameterException("Invalid date " + value);
		}*/		return date;	}}
