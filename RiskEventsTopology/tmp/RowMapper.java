package com.intuit.psd.risk.risk2.topology.interfaces;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public interface RowMapper<T> {
	public T mapRow(ResultSet rs);
	//public void setPreparedStatement(PreparedStatement ps);
}
