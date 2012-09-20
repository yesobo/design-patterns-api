package com.jea.patterns.api.dao;

import com.jea.patterns.api.dao.jpa.JpaDAOFactory;
import com.jea.patterns.api.dao.testmap.TestMapDAOFactory;

public abstract class DAOFactory {
	
	// List of DAO types supported by the factory
	public static final int TEST_MAP = 1;
	public static final int MY_SQL = 2;
	public static final int JPA = 3;
	
	public abstract PatternDAO getPatternDAO();
	
	public static DAOFactory getDAOFactory(int whichFactory) {
		switch (whichFactory) {
		case TEST_MAP:
			return new TestMapDAOFactory();
		case JPA:
			return new JpaDAOFactory();
		default:
			return null;
		}
	}
}
