/**
 * Copyright (c) 2010-2015 Bryan Beck.
 * All rights reserved.
 * 
 * This project is licensed under LGPL v2.1.
 * See jMovieManager-license.txt for details.
 * 
 */
package jmm.persist;

import org.hibernate.cfg.ImprovedNamingStrategy;

/**
 * define a local naming strategy for all tables that  don't have an
 * explicit table name given in annotations or xml config file
 * 
 * @author Bryan Beck
 * @since 16.02.2012
 *
 */
public class LocalNamingStrategy extends ImprovedNamingStrategy { 

    //private static final long serialVersionUID = 1L;

    /**
     * prepend a "t_" before the default table name given
     * by hibernate
     */
    @Override
    public String tableName(String tableName) {
        String tname = super.tableName(tableName); // kein "t_" + 
        return tname;
    }

    /**
     * prepend a "t_" before the default table name given
     * by hibernate
     */
    @Override
    public String classToTableName(String className) {
        return  super.classToTableName(className); // kein "t_" +
    }
}