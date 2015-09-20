/**
 * Copyright (c) 2010-2015 Bryan Beck.
 * All rights reserved.
 * 
 * This project is licensed under LGPL v2.1.
 * See jMovieManager-license.txt for details.
 * 
 */
package jmm.persist;

import java.util.HashMap;
import org.hibernate.Session;

/**
 * Manages all DAO instances for each Entitie, whos mapped to the database
 * Provides singleton instances for each DAO class
 * 
 * @author Bryan Beck
 * @since 12.03.2012
 */
public abstract class DaoManager {        
    private static final DbHibernate hibernateORM;
    private static final HashMap<Long, Class<?>> longToClassMap;
    private static final HashMap<Class<?>, DaoInterface<?>> classToDaoMap;

    /**
     * static initialization for maps
     */
    static{
        hibernateORM = new DbHibernate();
        longToClassMap = new HashMap<>();
        classToDaoMap = new HashMap<>();
    }
			
    /**
     * Creates a new DAO object for the specific class
     * 
     * @param clazz the class for the DAO object
     * @return a new DAO object
     * 
     * @throws NullPointerException If no database was set 
     * @throws RuntimeException If no DBConfig annotation was found for the specific class
     */
    private static <T> DaoInterface<T> createDao(Class<T> clazz)
                    throws NullPointerException, RuntimeException{		
        DaoInterface<T> dao = new DaoImpl<>(clazz, hibernateORM);        
        String key = dao.getClass().getName();
        longToClassMap.put(Long.valueOf(key.hashCode()), clazz);
        classToDaoMap.put(clazz, dao);
        return dao;
    }

    /**
     * Returns a DAO object for the specific class
     * 
     * @param clazz the prototype class for the DAO object
     * @return the DAO object 
     * 
     */
    @SuppressWarnings("unchecked")
    public static <T> DaoInterface<T> getDao(Class<T> clazz){
        DaoInterface<T> dao;
        if(classToDaoMap.containsKey(clazz)){
                dao = (DaoImpl<T>) classToDaoMap.get(clazz);
        }
        else{
                dao = createDao(clazz);
        }
        return dao;
    }

    /**
     * Returns a DAO object for the specific class
     * 
     * @param completeClassName the name of the table, including prefix
     * @return the DAO object <br/> null if classID was not found
     */
    static DaoInterface<?> getDao(String completeClassName){
        Long serialID;
        serialID = Long.valueOf(completeClassName.hashCode());
        DaoInterface<?> dao;
        if(longToClassMap.containsKey(serialID)){
                Class<?> classKey = longToClassMap.get(serialID);
                dao = (DaoImpl<?>)classToDaoMap.get(classKey);
        }
        else{
                dao = null;
        }
        return dao;
    }

    /**
     * Returns the class for a given table name
     * 
     * @param completeClassName the name of the table, including prefix
     * @return the class with the given table name <br/> null if no class was found
     */
    static Class<?> getClass(String completeClassName){
            Long serialID;
            serialID = Long.valueOf(completeClassName.hashCode());
            return longToClassMap.get(serialID);
    }

    /**
     * Deletes all references to DAO Objects
     */
    static void clearDaos(){
            longToClassMap.clear();
            classToDaoMap.clear();
    }    
    
    /**
     * Close active session <b>without</b> commiting open transactions
     */
    static void closeSession(){
        hibernateORM.closeSession();
    }
    
    /**
     * Flushs the current active session <br/>
     * Must be called if a child updates its parent. <br/>
     * <b>i.e. </b> <br/>
     * parent.remove(child) <br/>
     * DaoManager.flush() <br/>
     * newParent.add(child) 
     * 
     * @see Session#flush() 
     */
    static void flush(){
        hibernateORM.getActiveSession().flush();
    }
}
