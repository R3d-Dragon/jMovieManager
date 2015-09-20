/**
 * Copyright (c) 2010-2015 Bryan Beck.
 * All rights reserved.
 * 
 * This project is licensed under LGPL v2.1.
 * See jMovieManager-license.txt for details.
 * 
 */
package jmm.api;

import java.util.concurrent.Semaphore;

/**
 *
 * @author Bryan Beck
 * @since 07.03.2013
 */
public abstract class JMMAPI {
    protected static final String CHARSET_UTF = "UTF-8";
    protected static final String CHARSET_ISO = "ISO-8859-1";
    protected static final String USER_AGENT = "Firefox";
    //Base URL for api calls
    protected static final String JMM_BASEURL = "http://api.jmoviemanager.de";
    
    //Max Anzahl von gleichzeitigen API Zugriffen
    private static final Semaphore connectionCount = new Semaphore(5, false);
    
    /**
     * @return the conCount
     */
    public static Semaphore getConCount() {
        return connectionCount;
    }
    
    protected static Boolean apiEnabled = null;
    /**
     * Determines, if the API is accessable via jMovieManager or not
     * This method is designed as a singleton. If you want to check the API availability multiple times, you have to restart your program.
     * @return true if the API is enabled and accessable<br/> false otherwise
     */
    public abstract boolean isAPIenabled();

}
