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
 * Implements all regex patterns used in jMM
 * 
 * @author Bryan Beck
 * @since 23.12.2012
 */
public interface RegexInterface {
    final public static String REGEX_DIGITS_ONLY = "[^0-9]";
    final public static String REPLACEMENT_DIGITS_ONLY = "";
    
    final public static String REGEX_IMDB_YEAR = " \\([0-9]*\\)";
    final public static String REPLACEMENT_IMDB_YEAR = "";
    
    final public static String REGEX_YEAR = "19\\d{2}|20\\d{2}";
    
    //Alles was keine Zahl oder . ist
    final public static String REGEX_RATING = "[^0-9.]";
    final public static String REGEX_COMPLETE_RATING = "[0-9]+(\\.[0-9]+){0,1}";
    final public static String REPLACEMENT_RATING = "";
    
    final public static String REGEX_REMOVE_HTML_TAGS = "\\<.*?\\>";
    final public static String REPLACEMENT_REMOVE_HTML_TAGS = "";    
    
    final public static String REGEX_HTML_BREAK = "<br/>";
    final public static String REPLACEMENT_HTML_BREAK = "\n";        
    
    final public static String REGEX_IMDB_IMAGE_RESIZE_SX = "_SX[0-9]{2,3}_";
    final public static String REGEX_IMDB_IMAGE_RESIZE_SY = "_SY[0-9]{2,3}_";
                                                              //_CR0,0,75,95_
    final public static String REGEX_IMDB_IMAGE_RESIZE_CR_XY = "_CR[0-9]{1,2},[0-9],[0-9]{2,3},[0-9]{2,3}_";
    
    //TODO: Feature: Regex implementieren, welche aus filmtiteln crew, rlsdate usw. raushaut und einen standard titel zurückgibt
    //TODO: Import Option: Intelligente Dateinamen (suche)
    final public static String REGEX_FORMAT_FILENAME = "";
    final public static String REPLACEMENT_FORMAT_FILENAME = "";
}
