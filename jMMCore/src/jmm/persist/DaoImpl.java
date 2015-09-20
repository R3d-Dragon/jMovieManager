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
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.StaleObjectStateException;
import org.hibernate.Transaction;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;

/**
 * Dao implementation for Hibernate 4.x persistence layer
 * Encapsulate all database operations with hibernate persistence layer
 * 
 * @author Bryan Beck
 * @since 16.02.2012
 * 
 * @param <T> a persistet type that is handled by this dao
 */ 
class DaoImpl<T> implements DaoInterface<T>{

	// Attribute Definitions
	@SuppressWarnings("unchecked")
	private Class persistClassType;

	private DbHibernate dbAccess;

	/**
	 * create a new Dao object
	 * 
	 * @param persistType
	 *            The class of the PersistType. Must be given because Java
	 *            eliminates generic type parameters at compile time and
	 *            Hibernate needs type information at runtime!
	 * @param dbac
	 *            The Hibernate database access object
	 */
	@SuppressWarnings("unchecked")
	DaoImpl(Class persistType, DbHibernate dbac) {
		persistClassType = persistType;
		dbAccess = dbac;
	}

	// Hibernate specific (?) operations
	/**
	 * commits a transaction on all daos in this thread that are linked to the
	 * same dbAccess
	 */
	void commit() {
            Session s = dbAccess.getActiveSession();
            Transaction t = s.getTransaction();
            if (t != null) {
                t.commit();
            }
	}

	/**
	 * performs a rollback on all daos in this thread that are linked to the
	 * same dbAccess
	 */
	void rollback() {
            Session s = dbAccess.getActiveSession();
            Transaction t = s.getTransaction();
            if (t != null) {
                    t.rollback();
            }
	}

//	/**
//	 * closes the database session on this dao's connection to the database.
//	 * <br>
//	 * affects all daos in the same thread that are linked to the same database
//	 * connection. <br>
//	 * closing the session will commit open transaction, 
//	 * @see architecture.hibernate.DbHibernate#closeSession()
//	 */
//	public void closeSession() {
//            dbAccess.closeSession();
//	}

	// Implemented abstract operations

    /**
     * (non-Javadoc)
     * 
     * @see architecture.hibernate.DataAccess#save(java.lang.Object)
     * also starts a transaction, if none is active
     */
    @Override
    public boolean save(T obj) {
        Session s = dbAccess.getActiveSession();
        try {
                s.saveOrUpdate(obj);
                return true;
        } catch (StaleObjectStateException stex) {
                s.refresh(obj);
                return false;
        } catch (RuntimeException rex) {
                Transaction t = s.getTransaction();
                if (t != null) {
                        t.rollback();
                }
                s.close();
                throw rex;
        }
    }
    
    /**
     * (non-Javadoc)
     * 
     * @see jmm.persist.DaoInterface#saveAll(java.util.Collection) 
     */    
    @Override
    public boolean saveAll(Collection<T> collectionToSave){
        boolean success = true;
        if((collectionToSave != null ) && (!collectionToSave.isEmpty())){
            try{
                for(T listElement: collectionToSave){          
                    this.save(listElement);
                }
//                this.commit();
            }catch (Exception ex){
                success = false;
            }
        }
        return success;
    } 
    
    /*
     * (non-Javadoc)
     * 
     * @see architecture.hibernate.DataAccess#fetch(java.io.Serializable)
     * also starts a transaction, if none is active
     */
    @SuppressWarnings("unchecked")
    @Override
    public T get(Serializable id) {
        Session s = dbAccess.getActiveSession();
        T obj = (T) s.get(persistClassType, id);
        return obj;
    }

    /*
     * (non-Javadoc)
     * 
     * @see architecture.hibernate.DataAccess#fetchAll()
     * also starts a transaction, if none is active
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<T> getAll() {
        Session s = dbAccess.getActiveSession();
        List<T> result = (List<T>) s.createQuery(
                        "from " + persistClassType.getCanonicalName()).list();
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see architecture.hibernate.DataAccess#find(java.lang.String)
     * also starts a transaction, if none is active
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<T> find(String query) {
        Session s = dbAccess.getActiveSession();
        List<T> result = (List<T>) s.createQuery(query)
                        .list();
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see architecture.hibernate.DataAccess#find(java.lang.String,
     *      java.util.Map)
     * also starts a transaction, if none is active
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<T> find(String query, Map<String, Object> params) {
        Session s = dbAccess.getActiveSession();
        Query hibernateQuery;
        if(query.startsWith("SELECT ")){
            hibernateQuery = s.createSQLQuery(query);
        }
        else{
            hibernateQuery = s.createQuery(query);
        }
        for (String pname : params.keySet()) {
                hibernateQuery.setParameter(pname, params.get(pname));
        }
        List<T> result = (List<T>) hibernateQuery.list();
        return result;
    }
    
//    /**
//     * sucht nach einem Beispiel
//     * @param sample - Beispiel
//     * @param excluded - exclude Felder
//     * @param mode - Vergleichsmodus
//     * @return 
//     */
//    @Override
//    public List<T> findByExample(T sample,MatchMode mode,String... excluded) {
//        Example qbe = Example.create(sample).ignoreCase().excludeZeroes()
//                                        .enableLike(mode);
//        for (String excludedProperty : excluded) {
//                qbe.excludeProperty(excludedProperty);
//        }
//        Session s = dbAccess.getActiveSession();
//        return (List<T>) s.createCriteria(persistClassType).add(qbe).list();
//    }
//    /**
//     * sucht nach einen Beispiel. Vorkommen im String egal. 
//     * @param sample - Beispiel
//     * @param excluded - exclude Felder
//     */
//    @SuppressWarnings("unchecked")
//    @Override
//    public List<T> findByExample(T sample,String... excluded) {
//        return findByExample(sample, MatchMode.ANYWHERE,excluded);
//    }

    /*
     * (non-Javadoc)
     * 
     * @see architecture.hibernate.DataAccess#delete(java.lang.Object)
     * also starts a transaction, if none is active
     */
    @Override
    public void delete(T obj) {
        Session s = dbAccess.getActiveSession();
        s.delete(obj);
    }

    /*
     * (non-Javadoc)
     * 
     * @see architecture.hibernate.DataAccess#deleteAll()
     * also starts a transaction, if none is active
     */
    @Override
    public void deleteAll() {
        Session s = dbAccess.getActiveSession();
        for(T value: getAll()){
            s.delete(value);
        }
//        s.createQuery("delete " + persistClassType.getCanonicalName())
//                        .executeUpdate();
    }

    // Attribute Accessors

    @SuppressWarnings("unchecked")
    Class getPersistClassType() {
        return persistClassType;
    }

//    @SuppressWarnings({ "unused", "unchecked" })
//    private void setAccessedType(Class pAccessedType) {
//        persistClassType = pAccessedType;
//    }

    DbHibernate getDbAccess() {
        return dbAccess;
    }
}
