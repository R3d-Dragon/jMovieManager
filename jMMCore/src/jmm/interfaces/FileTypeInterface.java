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
 * Defines the kind of file, selection or import
 * 
 * @author Bryan Beck
 * @since 20.09.2012
 */
public interface FileTypeInterface {
    /**
     * Describes if something is a movie, a serie or music
     */
    public static enum MediaType{
        MOVIE,
        SERIE,
        MUSIC
    }
}
