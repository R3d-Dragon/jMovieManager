/**
 * Copyright (c) 2010-2015 Bryan Beck.
 * All rights reserved.
 * 
 * This project is licensed under LGPL v2.1.
 * See jMovieManager-license.txt for details.
 * 
 */
package jmm.interfaces;

/**
 * Alle definierten Quellen sind hier zu finden
 * 
 * @author Bryan Beck
 * @since 19.12.2011
 */
public interface SourceInterface {
    public static final String SOURCE_EMPTY = "Value.empty";
    public static final String SOURCE_TV = "Source.01";
    public static final String SOURCE_HDTV = "Source.02";
    public static final String SOURCE_DVD = "Source.03";
    public static final String SOURCE_BLURAY = "Source.04";

    public static final String[] SOURCES = 
    {
        SOURCE_EMPTY, 
        SOURCE_TV, 
        SOURCE_HDTV, 
        SOURCE_DVD,
        SOURCE_BLURAY
    };            
}
