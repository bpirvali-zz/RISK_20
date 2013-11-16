package com.intuit.bre.ruledata;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.intuit.bre.ruledata.interfaces.MerchantCache;

/* @SINGLE-THREADED
 * 
 * This class will just keep a cache of most recent key-value pairs for a merchant.
 * This cache is capable of keeping different values for different dates for a certain merchant!
 * */
// TODO:BP This cache could be optimized to get CompositeKey as a parameter to mitigate excessive CompositeKey creation!
public class BasicMerchantCacheImpl implements MerchantCache {
//	private long maNumber=-1;
	private String maNumber = "";
	private Map<CompositeKey, String> mapStr = null;
	private Map<CompositeKey, Integer> mapInt = null;
	private Map<CompositeKey, Long> mapLong = null;
	private Map<CompositeKey, Double> mapDouble = null;
	private Map<CompositeKey, Date> mapDate = null;
	
	private static class CompositeKey {
		//long maNumber;
		Date BCD;
		String key;
		
		public CompositeKey(Date BCD, String key) {
			this.BCD = BCD;
			this.key = key;
		}
		
	    @Override
	    public boolean equals(Object obj) {
	        if(obj != null && obj instanceof CompositeKey) {
	        	CompositeKey ck = (CompositeKey)obj;
	            return key.equals(ck.key) && BCD.equals(ck.BCD);
	        }
	        return false;
	    }

	    @Override
	    public int hashCode() {
	        int hash = 1;
	        hash = hash * 13 + (BCD == null ? 0 : BCD.hashCode());
	        hash = hash * 17 + (key == null ? 0 : key.hashCode());
	        return hash;	    	
	    }		
	}
	
	@Override
	public String getString(String key, String merchantAccountNumber, Date BCD) {
		return (isCachedStr(merchantAccountNumber, BCD, key)==true ? mapStr.get(new CompositeKey(BCD,key)) : null);
	}

	@Override
	public Integer getInteger(String key, String merchantAccountNumber, Date BCD) {
		return (isCachedInt(merchantAccountNumber, BCD, key)==true ? mapInt.get(new CompositeKey(BCD,key)) : null);
	}

	@Override
	public Long getLong(String key, String merchantAccountNumber, Date BCD) {
		return (isCachedLong(merchantAccountNumber, BCD, key)==true ? mapLong.get(new CompositeKey(BCD,key)) : null);
	}

	@Override
	public Double getDouble(String key, String merchantAccountNumber, Date BCD) {
		return (isCachedDouble(merchantAccountNumber, BCD, key)==true ? mapDouble.get(new CompositeKey(BCD,key)) : null);
	}

	@Override
	public Date getDate(String key, String merchantAccountNumber, Date BCD) {
		return (isCachedDate(merchantAccountNumber, BCD, key)==true ? mapDate.get(new CompositeKey(BCD,key)) : null);
	}
	
	/*
	@Override
	public void putString(String merchantAccountNumber, Date BCD, String key, String val) {
		resetCache(merchantAccountNumber);
		mapStr.put(new CompositeKey(BCD,key), val);
	}

	@Override
	public void putInteger(String merchantAccountNumber, Date BCD, String key, int val) {
		resetCache(merchantAccountNumber);
		mapInt.put(new CompositeKey(BCD,key), val);
	}

	@Override
	public void putLong(String merchantAccountNumber, Date BCD, String key, long val) {
		resetCache(merchantAccountNumber);
		mapLong.put(new CompositeKey(BCD,key), val);
	}

	@Override
	public void putDouble(String merchantAccountNumber, Date BCD, String key, double val) {
		resetCache(merchantAccountNumber);
		mapDouble.put(new CompositeKey(BCD,key), val);
	}

	@Override
	public void putDate(String merchantAccountNumber, Date BCD, String key, Date val) {
		resetCache(merchantAccountNumber);
		mapDate.put(new CompositeKey(BCD,key), val);
	}
	*/	

	@Override
	public void putValue(String merchantAccountNumber, Date BCD, String key, RuleDataType type, DataElement val) {
		resetCache(merchantAccountNumber);
		if (type==RuleDataType.STRING)
			mapStr.put(new CompositeKey(BCD,key), convertToString(val.getValue()));
		else if (type==RuleDataType.INTEGER)
			mapInt.put(new CompositeKey(BCD,key), convertToInteger(val.getValue()));
		else if (type==RuleDataType.LONG)
			mapLong.put(new CompositeKey(BCD,key), convertToLong(val.getValue()));
		else if (type==RuleDataType.DOUBLE)
			mapDouble.put(new CompositeKey(BCD,key), convertToDouble(val.getValue()));
		else if (type==RuleDataType.DATE)
			mapDate.put(new CompositeKey(BCD,key), (Date)val.getValue());
		else 
			throw new RuntimeException("Error: Cache failed to identify the data type");
	}	

	@Override
	public boolean isCachedStr(String merchantAccountNumber, Date BCD, String key) {
		if (merchantAccountNumber.equalsIgnoreCase(maNumber)) {
			CompositeKey ck = new CompositeKey(BCD, key);
			if (mapStr.containsKey(ck))
				return true;
		}
		return false;	
	}
	
	@Override
	public boolean isCachedInt(String merchantAccountNumber, Date BCD, String key) {
		if (merchantAccountNumber.equalsIgnoreCase(maNumber))
			if (mapInt.containsKey(new CompositeKey(BCD,key)))
				return true;
		return false;
	}

	@Override
	public boolean isCachedLong(String merchantAccountNumber, Date BCD, String key) {
		if (merchantAccountNumber.equalsIgnoreCase(maNumber))
			if (mapLong.containsKey(new CompositeKey(BCD,key)))
				return true;
		return false;
	}

	@Override
	public boolean isCachedDouble(String merchantAccountNumber, Date BCD, String key) {
		if (merchantAccountNumber.equalsIgnoreCase(maNumber))
			if (mapDouble.containsKey(new CompositeKey(BCD,key)))
				return true;
		return false;
	}

	@Override
	public boolean isCachedDate(String merchantAccountNumber, Date BCD, String key) {
	//	if (maNumber==merchantAccountNumber)
			if(merchantAccountNumber.equalsIgnoreCase(maNumber))
			if (mapDate.containsKey(new CompositeKey(BCD,key)))
				return true;
		return false;
	}
	
	private void resetCache(String merchantAccountNumber) {
		if (!maNumber.equalsIgnoreCase(merchantAccountNumber)) {
			maNumber=merchantAccountNumber;
			mapStr = new HashMap<CompositeKey, String>();
			mapInt = new HashMap<CompositeKey, Integer>();
			mapLong = new HashMap<CompositeKey, Long>();
			mapDouble = new HashMap<CompositeKey, Double>();
			mapDate = new HashMap<CompositeKey, Date>();
		}
	}
	
	private static Long convertToLong(Object obj) {
		if (obj == null) {
			return null;
		}
		
		if (obj instanceof Long) {
			return (Long)obj;
		} else if (obj instanceof Double) {
			Double D = (Double)obj;
			return new Long(D.longValue());
		} else if (obj instanceof Integer) {
			Integer I = (Integer)obj;
			return new Long(I.longValue());
		} else if (obj instanceof String) {
			return Long.valueOf((String)obj);
		} 
		
		throw new RuntimeException("Error: Unsupported object type!");
	}

	private static Double convertToDouble(Object obj) {
		if (obj == null) {
			return null;
		}
		
		if (obj instanceof Double) {
			return (Double)obj;
		} else if (obj instanceof Long) {
			Long l = (Long)obj;
			return new Double(l.doubleValue());
		} else if (obj instanceof Integer) {
			Integer I = (Integer)obj;
			return new Double(I.doubleValue());
		} else if (obj instanceof String) {
			return Double.valueOf((String)obj);
		}
		
		throw new RuntimeException("Error: Unsupported object type!");
	}

	private static Integer convertToInteger(Object obj) {
		if (obj == null) {
			return null;
		}
		
		if (obj instanceof Integer) {
			return (Integer)obj;
		} else if (obj instanceof Long) {
			Long l = (Long)obj;
			return new Integer(l.intValue());
		} else if (obj instanceof Double) {
			Double D = (Double)obj;
			return new Integer(D.intValue());
		} else if (obj instanceof String) {
			return Integer.valueOf((String)obj);
		}
		
		throw new RuntimeException("Error: Unsupported object type!");
	}

	private static String convertToString(Object obj) {
		if (obj == null) {
			return null;
		}
		
		return obj.toString();
	}

	
	/*
	private boolean isCached(long merchantAccountNumber ) {
		if (maNumber==merchantAccountNumber)
			return true;
		
		return false;	
	}
	*/
}
