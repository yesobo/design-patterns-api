package com.jea.patterns.api.dao.testmap;

import com.jea.patterns.api.dao.DAOFactory;
import com.jea.patterns.api.dao.PatternDAO;

public class TestMapDAOFactory extends DAOFactory {
	
	public PatternDAO getPatternDAO() {
		return TestMapPatternDao.getInstance();
	}
}
