package com.intuit.bre.ruledata;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

public class DataElement {
	private final String name;
	private final String type;
	private final Object value;
	
	public static final String TYPE_STRING="STRING";
	public static final String TYPE_LONG="LONG";
	public static final String TYPE_INT="INTEGER";
	public static final String TYPE_DATE="DATE";
	public static final String TYPE_DOUBLE="DOUBLE";
	public static final String TYPE_TIME="TIME";
	public static final String TYPE_TIMESTAMP="TIMESTAMP";

	//	private static class ValueObj {
//		private final String type;
//		private final Object value;
//		
//		public ValueObj(String type, Object val) {
//			this.type = type;
//			this.value = val;
//		}
//
//		public String getType() {
//			return type;
//		}
//
//		public Object getValue() {
//			return value;
//		}
//	}

//	private static ValueObj normalizeSQLTypes(String sqlType, Object val) {
//		ValueObj v = null;
//		if (sqlType.contains("CHAR")) {
//			v = new ValueObj(DataElement.TYPE_STRING,val);
//		} else if (sqlType.contains("BIGINT")) {
//			Long l = (Types.)
//			v = new ValueObj(DataElement.TYPE_LONG,val);
//		}
//			return DataElement.TYPE_STRING;
//		
//		if (sqlType.equals("BIGINT"))
//			return DataElement.TYPE_LONG;
//		
//		if (sqlType.equals("SMALLINT"))
//			return DataElement.TYPE_INT;
//		
//		if (sqlType.equals("DATE"))
//			return DataElement.TYPE_DATE;
//		
//		if (sqlType.equals("FLOAT"))
//			return DataElement.TYPE_DOUBLE;
//		
//		if (sqlType.equals("REAL"))
//			return DataElement.TYPE_DOUBLE;
//		
//		if (sqlType.equals("NUMBER"))
//			return DataElement.TYPE_DOUBLE;
//		
//		return sqlType;
//		
//	}
 
 	
	public DataElement(String name, String type, Object value) {
		this.name = name;
		/*
		this.type = normalizeSQLTypes(type);
		if (sqlType.contains("CHAR")) {
			this.type = 
		}
			return DataElement.TYPE_STRING;
		*/
		this.type = type;
		this.value = value;
	}

	/*
	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}
	*/

	public Object getValue() {
		return value;
	}

	/*
	public String toString() {
		String val="";
		if (type.equals(TYPE_STRING))
			val = (String)value;
		else if (type.equals(TYPE_LONG))
			val = ((Long)value).toString();
		else if (type.equals(TYPE_INT))
			val = ((Integer)value).toString();
		else if (type.equals(TYPE_DATE))
			val = ((Date)value).toString();
		else if (type.equals(TYPE_TIME))
			val = ((Time)value).toString();
		else if (type.equals(TYPE_TIMESTAMP))
			val = ((Timestamp)value).toString();
		else if (type.equals(TYPE_DOUBLE))
			val = ((Double)value).toString();
		
		return ("DataElement(" + name + "," + type + "," + val + ")");
	}
	*/
}
