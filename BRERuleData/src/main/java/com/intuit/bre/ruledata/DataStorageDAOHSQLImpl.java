package com.intuit.bre.ruledata;

import com.intuit.bre.ruledata.interfaces.DataStorageDAO;

public class DataStorageDAOHSQLImpl extends AbstractJDBCDAO implements
		DataStorageDAO {
	public DataStorageDAOHSQLImpl(DataStorageUtil.DBInfo dbInfo) {
		super(dbInfo.getDriver(), dbInfo.getURL(),
				dbInfo.getUserName(), dbInfo.getPassWord());
	}}
