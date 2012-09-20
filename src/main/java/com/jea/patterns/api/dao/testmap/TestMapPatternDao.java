package com.jea.patterns.api.dao.testmap;

import java.util.Collection;

import com.jea.patterns.api.dao.PatternDAO;
import com.jea.patterns.api.model.Pattern;

public class TestMapPatternDao extends TestMapDao<Pattern, Integer> 
	implements PatternDAO {
	
	private static final TestMapPatternDao instance = new TestMapPatternDao();
	
	public static TestMapPatternDao getInstance() {
		return instance; 
	}
	
	private TestMapPatternDao() {
		Pattern pattern = new Pattern(1, "Singleton");
		pattern.setIntent("Ensure a class only has one instance, and provide a global point of access to it");
		contentProvider.put(Integer.valueOf(1), pattern);
		pattern = new Pattern(2, "Prototype");
		pattern.setIntent("Create new objects copying from a prototype");
		contentProvider.put(Integer.valueOf(2), pattern);
	}

	@Override
	public void insertPattern(Pattern n) {
		contentProvider.put(Integer.valueOf(n.getId()), n);
	}

	@Override
	public boolean deletePattern(int id) {
		return contentProvider.remove(Integer.valueOf(id)) != null;
	}

	@Override
	public Pattern findPatternById(int id) {
		return contentProvider.get(Integer.valueOf(id));
	}

	@Override
	public boolean updatePattern(Pattern newPattern) {
		return (contentProvider.put(Integer.valueOf(newPattern.getId()), 
				newPattern) != null);
	}

	@Override
	public boolean containsPattern(int id) {
		return contentProvider.containsKey(String.valueOf(id));
	}

	@Override
	public Collection<Pattern> getAllPatterns() {
		return contentProvider.values();
	}

	@Override
	public void persist(Pattern entity) {
		contentProvider.put(Integer.valueOf(entity.getId()), entity);
	}

	@Override
	public void remove(Pattern entity) {
		contentProvider.remove(String.valueOf(entity.getId()));
	}

	@Override
	public Pattern findById(Integer id) {
		contentProvider.get(String.valueOf(id));
		return null;
	}
}
