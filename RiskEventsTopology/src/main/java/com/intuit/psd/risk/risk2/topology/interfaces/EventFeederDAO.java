package com.intuit.psd.risk.risk2.topology.interfaces;

import java.util.List;

import com.intuit.psd.risk.risk2.topology.tuples.CardBatchEvent;



public interface EventFeederDAO {
	
	/**
	 * Returns the events for this worker that
	 * has not been successfully processed.  Size is limited
	 * by batch max size.  Return empty list if no events are found.
	 * @param workerId
	 * @return
	 */
	public Object getNextRiskEventList();
	public void init(List<Object> l);
	//public String getSQL();
	public void updateForMerchantHang(CardBatchEvent event);

}
