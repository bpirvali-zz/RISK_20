package com.intuit.psd.risk.risk2.topology.interfaces;

import java.util.List;

/**
 * 
 * @author bpirvali
 *
 */
public interface RiskFactory {
	public EventFeederDAO createEventFeederDAO();
	public <T> T createRiskEvent(List<Object> list, RowMapper<T> rowMapper);
}
