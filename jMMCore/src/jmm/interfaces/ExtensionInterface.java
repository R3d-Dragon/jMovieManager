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
 * Interface for all extensions (i.e. video, picture extensions)
 * 
 * @author Bryan Beck
 * @since 29.03.2013
 */
public interface ExtensionInterface {
    /**
     * All supported video extensions
     */
    public static String[] video_Extensions = new String[]{".mkv", ".ts", ".m2ts", ".avi", ".mpg", ".mpeg", ".wmv", ".mp4", ".m4v" , ".flv", "video_ts.ifo"}; // , "vob"};
   
    /**
     * All supported picture extensions
     */
    public static String[] picture_Extensions = new String[]{".gif", ".jpg", ".jpeg", ".png"};
    
    /**
     * All extensions which are used by the hsql db server
     */
    public static String[] hsqlDB_Extensions = new String[]{ ".lck", ".log", ".script", ".properties", ".tmp", ".pics"};
}
