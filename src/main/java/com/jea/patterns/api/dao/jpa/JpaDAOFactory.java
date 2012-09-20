package com.jea.patterns.api.dao.jpa;

import com.jea.patterns.api.dao.DAOFactory;
import com.jea.patterns.api.dao.PatternDAO;

public class JpaDAOFactory extends DAOFactory{
	
	private static JpaPatternDao patternDAO;
	
	@Override
	public  PatternDAO getPatternDAO() {
		if (patternDAO == null) {
			patternDAO = new JpaPatternDao();
		}
		return patternDAO;
	}

}
