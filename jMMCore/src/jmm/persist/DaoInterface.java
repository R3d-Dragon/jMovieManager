/**
 * Copyright (c) 2010-2015 Bryan Beck.
 * All rights reserved.
 * 
 * This project is licensed under LGPL v2.1.
 * See jMovieManager-license.txt for details.
 * 
 */
package jmm.persist;


import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.hibernate.criterion.MatchMode;

/**
 * Generic DAO (Data Access Object) interface
 * define access methods to the database

 * @author Bryan Beck
 * @since 16.02.2012
 * @param <T> Generic parameter for Dao
 */
public interface DaoInterface<T> {
    
	/**
	 * save object to persistent storage
	 * 
	 * @param obj
	 *            object to be saved
	 * @return true if successful, false if object is "stale" (i.e. object was
	 *         changed by an other thread or process)
	 */
	boolean save(T obj);
        
        /**
        * Saves all objects from the collection
        * 
        * @param collection the collection to save
        * @return true if the collection was saved <br/> false otherwise
        */
        boolean saveAll(Collection<T> collection);
        
        /**
        * Saves all objects from the list
        * 
        * @param list the list to save
        * @return true if the list was saved <br/> false otherwise
        */
//        boolean saveAll(MediaCollection collection);
        
	/**
	 * Fetch object from persistent storage
	 * 
	 * @param id
	 *            key property of object
	 * @return object, if found, else null
	 */
	T get(Serializable id);

	/**
	 * Fetch all objects of T in persistent storage
	 * 
	 * @return list of all objects
	 */
	List<T> getAll();

	/**
	 * executes hibernate query <br>
	 * (e.g. select address from person p join p.address)
	 * 
	 * @param query
	 *            simple HQL query string
	 * @return list of returned objects
	 */
	List<T> find(String query);

	/**
	 * executes hibernate query with parameters <br>
	 * (e.g. from person p with p.name = :name)
	 * 
	 * @param query
	 *            HQL query string containing named parameters in hibernate
	 *            style (e.g. :name)
	 * @param params
	 *            map of actual parameters with parameter name as key (without :)
	 *            and actual parameter value as value
	 * @return list of returned objects
	 */
	List<T> find(String query, Map<String, Object> params);

//	/**
//	 * Query by example. Find objects that are "similar" to the sample object.<br>
//	 * String properties are matched with <i>like</i>, so SQL wildcards (%) can
//	 * be used.
//	 * 
//	 * @param sample
//	 *            a sample object
//	 * @param mode 
//	 *            the search mode for the Properties
//	 * @param excluded
//	 *            properties not considered im matching
//	 * @return list of objects that conform to sample in all not null properties
//	 */
//	List<T> findByExample(T sample,MatchMode mode,String... excluded);
//
//	/**
//	 * Query by example. Find objects that are "similar" to the sample object.<br>
//	 * String properties are matched with <i>like</i>, so SQL wildcards (%) can
//	 * be used.
//	 * 
//	 * @param sample
//	 *            a sample object
//	 * @param excluded
//	 *            properties not considered im matching
//	 * @return list of objects that conform to sample in all not null properties
//	 */
//	List<T> findByExample(T sample,String... excluded);

	/**
	 * Delete object from persistent storage
	 * 
	 * @param obj
	 *            object to be deleted
	 */
	void delete(T obj);

	/**
	 * Delete all objects of T from persistent storage
	 */
	void deleteAll();
}
