package com.jea.patterns.api.dao.jpa;

import java.lang.reflect.ParameterizedType;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;

import com.jea.patterns.api.dao.Dao;

public class JpaDao<E, K> implements Dao<E, K>{
	protected Class entityClass;
	
	 EntityManager entityManager = Persistence
	            .createEntityManagerFactory("design-patterns-api")
	            .createEntityManager();
	
	//Uses reflection to get the entity class (by Arjan Blokzijl)
	public JpaDao() {
		ParameterizedType genericSuperclass = 
				(ParameterizedType) getClass().getGenericSuperclass();
		this.entityClass = (Class) genericSuperclass.getActualTypeArguments()[1];
	}
	
	public void persist(E entity) {
		entityManager.persist(entity);
	}
	
	public void remove(E entity) {
		entityManager.remove(entity);
	}
	
	public E findById(K id) {
		return (E) entityManager.find(entityClass, id);
	}
}
