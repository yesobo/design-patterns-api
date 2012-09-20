package com.jea.patterns.api.dao;

import java.util.Collection;

import com.jea.patterns.api.model.Pattern;

public interface PatternDAO extends Dao<Pattern, Integer>{
	public void insertPattern(Pattern n);
	public boolean deletePattern(int id);
	public Pattern findPatternById(int id);
	public boolean updatePattern(Pattern newPattern);
	public boolean containsPattern(int id);
	public Collection<Pattern> getAllPatterns();
}
