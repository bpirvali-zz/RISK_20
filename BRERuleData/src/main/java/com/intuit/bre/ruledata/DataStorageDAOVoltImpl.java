package com.intuit.bre.ruledata;

import com.intuit.bre.ruledata.interfaces.DataStorageDAO;

public class DataStorageDAOVoltImpl extends AbstractJDBCDAO implements
		DataStorageDAO {

	public DataStorageDAOVoltImpl(DataStorageUtil.DBInfo dbInfo) {
		super(dbInfo.getDriver(), dbInfo.getURL(),
				dbInfo.getUserName(), dbInfo.getPassWord());

	}
}
