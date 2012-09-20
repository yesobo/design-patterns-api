package com.jea.patterns.api.dao.jpa;

import java.util.Collection;

import javax.persistence.Query;

import com.jea.patterns.api.dao.PatternDAO;
import com.jea.patterns.api.model.Pattern;

public class JpaPatternDao extends JpaDao<Pattern, Integer> 
	implements PatternDAO{

	@Override
	public void insertPattern(Pattern n) {
		entityManager.getTransaction().begin();
		entityManager.persist(n);
		entityManager.flush();
		entityManager.getTransaction().commit();
	}

	@Override
	public boolean deletePattern(int id) {
		entityManager.getTransaction().begin();
		int numberDeleted = 
			entityManager.createQuery("DELETE FROM Pattern pa WHERE pa.id=:id")
			.setParameter("id",id).executeUpdate();
		entityManager.flush();
		entityManager.getTransaction().commit();
		return numberDeleted > 0;
	}

	@Override
	public Pattern findPatternById(int id) {
		return entityManager.find(Pattern.class, id);
	}

	@Override
	public boolean updatePattern(Pattern newPattern) {
		return entityManager.merge(newPattern) != null;
	}

	@Override
	public boolean containsPattern(int id) {
		return findPatternById(id) != null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<Pattern> getAllPatterns() {
		Query query = entityManager.createQuery("SELECT p FROM Pattern p");
	    return query.getResultList();
	}

}
