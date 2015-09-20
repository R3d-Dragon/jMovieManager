/**
 * Copyright (c) 2010-2015 Bryan Beck.
 * All rights reserved.
 * 
 * This project is licensed under LGPL v2.1.
 * See jMovieManager-license.txt for details.
 * 
 */
package jmm.persist;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

/**
 * Hibernate Database Access <br/>
 * 
 * class to manage a connection to a single database using hibernate. the
 * basic configuration is in file hibernate.cfg.xml in the root directory
 * of the class path. additional configuration should be made 
 * using JPA annotations.
 * 
 * @author Bryan Beck
 * @since 16.02.2012
 */
public class DbHibernate {

    // Attribute Definitions
    /**
     * holds the database configuration object
     */
    private static final Configuration configuration;
    /**
     * is used to create new session objects
     */
    private static SessionFactory sessionFactory;
    
    /**
     * required for the SessionFactory
     */
    private static ServiceRegistry serviceRegistry;

    /**
     * holds the currently open active session to the database
     */
    private Session activeSession;

    // Operations
    /**
     * close database connection at the end of the program
     */
    static void closeDatabase() {
        sessionFactory.close();
    }
    
    // Implemented abstract operations
    /**
     * open a session and associate it with a transaction
     */
    Session openSession() {
        activeSession = sessionFactory.openSession();
        return activeSession;
    }

    /**
     * close active session  
     * <strike> and automatically commit open transactions </strike>
     */
    void closeSession() {
        if (activeSession != null && activeSession.isOpen()) {  
            //Save last update before closing the session
//            Transaction t = activeSession.getTransaction();
//            if (t.isActive()) {
//                t.commit();
//            }
//            activeSession.createconnection().createStatement().execute("SHUTDOWN"); 
            activeSession.disconnect();
            activeSession.close();
        }
    }

    // Attribute Accessors
    /**
     * create a new session if necessary. also start a transaction if necessary.
     *
     * @return an open session
     */
    Session getActiveSession() {
        if (activeSession == null || !activeSession.isOpen()) {
            openSession();
        }
        Transaction t = activeSession.getTransaction();
        if (t == null || !t.isActive()) {
            activeSession.beginTransaction();
        }
        return activeSession;
    }

    /**
     * initialize database connection
     */
    static {
        try {    
            configuration = new org.hibernate.cfg.Configuration(); //AnnotationConfiguration();
            configuration.setNamingStrategy(new LocalNamingStrategy()); 
            configuration.configure("hibernate.cfg.xml");            
            openDatabase();
        } catch (HibernateException ex) {
            System.out.println(ex);
            throw new RuntimeException("Hibernate Exception", ex);
        }
    }    
    
    /**
     * creates a new ServiceRegistry and a new SessionFactory 
     */
    static void openDatabase(){       
        serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
    }
}
