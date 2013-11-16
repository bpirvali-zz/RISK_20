package com.intuit.psd.risk.risk2.topology.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;

import com.intuit.psd.risk.risk2.topology.SpringObjectMgr;
import com.intuit.psd.risk.risk2.topology.interfaces.EventFeederDAO;
import com.intuit.psd.risk.risk2.topology.tuples.CardBatchEvent;

public class CardBatchEventFeederDAOImpl implements EventFeederDAO,Serializable {
	private static final Logger logger = LoggerFactory.getLogger(CardBatchEventFeederDAOImpl.class);
	private static final long serialVersionUID = 1L;

	transient private JdbcTemplate 		jdbcTemplate;
	transient private PreparedStatement	ps;
	
	private static final String sSQL = "SELECT merchant_account_number, event_timestamp, system_id, event_category_id, event_type_id, event_id " + 
	                      "FROM bre.incoming_risk_events " + 
			              "WHERE worker_id=? AND event_category_id=0 AND event_type_id=0"+ 
			              "AND (processing_status=0 OR (processing_status=3 AND retries<?)) AND ROWNUM<?";
	
	private int	workerID = -1;
	private int	retries = -1;
	private int batch_size = -1;
	
//	public int getWorkerID() {
//		return workerID;
//	}
//
//	public int getRetries() {
//		return retries;
//	}

	public void init(List<Object> l) {
		this.workerID = (Integer)l.get(0);
		this.retries = (Integer)l.get(1);
		this.batch_size = (Integer)l.get(2);
		if (jdbcTemplate==null) 
			jdbcTemplate = (JdbcTemplate)SpringObjectMgr.getInstance().getBean("jdbcTemplate");
	}	
	
	private class MyPreparedStatementCreator implements PreparedStatementCreator {
		public PreparedStatement createPreparedStatement(Connection conn)
				throws SQLException {
			//if (ps ==null)
				ps = conn.prepareStatement(sSQL);
			if (workerID==-1 || retries==-1)
				throw new RuntimeException("worker-id or retries not initialized");
			ps.setInt(1, workerID);
			ps.setInt(2, retries);
			ps.setInt(3, batch_size);
			return ps;
		}
	}
	
	private class MyPreparedStatementCallback implements PreparedStatementCallback<Object> {
		public Object doInPreparedStatement(PreparedStatement ps)
				throws SQLException, DataAccessException {
			Map<String, CardBatchEvent> map = new HashMap<String, CardBatchEvent>();
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				CardBatchEvent e = new CardBatchEvent(
						rs.getString(1), 
						rs.getTimestamp(2),
						rs.getInt(3),
						rs.getInt(4),
						rs.getInt(5),
						rs.getInt(6)
						);
				map.put(e.getKey(), e );
			}
			return map;
		}
	}
	
	public Object getNextRiskEventList() {
		try {
			return jdbcTemplate.execute(new MyPreparedStatementCreator(), new MyPreparedStatementCallback());
		} catch(DataAccessException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

//	public String getSQL() {
//		return sSQL;
//	}

	public void updateForMerchantHang(CardBatchEvent event) {
		StringBuilder sb = new StringBuilder();
		
    	sb.append("UPDATE bre.incoming_risk_events SET processing_status=3, time_processed=?, error_msg = error_msg || '[ ]' || 'BOLT HANG!!!'");
    	sb.append(", retries=retries+1 ");
    	sb.append("WHERE merchant_account_number = ? ");
    	sb.append("AND event_timestamp = ? ");
    	sb.append("AND system_id = ? ");
    	sb.append("AND event_category_id = ? ");
    	sb.append("AND event_type_id = ? ");
    	sb.append("AND event_id = ? ");
		
    	try {
	    	jdbcTemplate.update(sb.toString(), new Object[] {new Date(), event.accNo, 
	    		event.eventTimestamp,
	    		event.systemID,
	    		event.eventCatID,
	    		event.eventTypeID,
	    		event.eventID
	    		});
		} catch(DataAccessException e) {
			logger.error(e.getMessage(), e);
		}
	}
}
