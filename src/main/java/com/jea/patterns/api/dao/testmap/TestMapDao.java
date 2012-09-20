package com.jea.patterns.api.dao.testmap;

import java.util.HashMap;
import java.util.Map;

import com.jea.patterns.api.dao.Dao;

public abstract class TestMapDao<E, K> implements Dao<E, K>{

	protected Map<K, E> contentProvider = new HashMap<K, E>();
}
