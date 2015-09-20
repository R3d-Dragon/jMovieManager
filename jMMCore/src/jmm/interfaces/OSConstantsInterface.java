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
 *
 * @author Bryan Beck
 * @since 25.04.2011
 */
public interface OSConstantsInterface {
    
    /** Windows **/
    //Notwendig fuer die Plattform Idendifikation
    public static final String WIN_ID = "windows";
    //Der Standardbrowser
    public static final String WIN_PATH = "rundll32";
    //Das Flag zum aufrufen der URL
    public static final String WIN_FLAG = "url.dll,FileProtocolHandler";
    //Name der MediaInfo Library
    public static final String WIN_X64_MEDIAINFO = "MediaInfo.dll";
    public static final String WIN_X86_MEDIAINFO = "MediaInfo_i386.dll";

    /** LINUX **/
    //Notwendig fuer die Plattform Idendifikation
    public static final String UNIX_ID = "linux";
    // The default browser under unix.
    public static final String UNIX_PATH = "netscape";
    // The flag to display a url.
    public static final String UNIX_FLAG = "-remote openURL";
    //Name der MediaInfo Library
    public static final String UNIX_UBUNTU_LIBZEN = "/usr/lib/libzen.so.0";
    //public static final String UNIX_UBUNTU_LIBZEN2 = "/usr/lib/libzen.so.0.0.0";
    public static final String UNIX_UBUNTU_MEDIAINFO = "/usr/lib/libmediainfo.so.0";
    //public static final String UNIX_UBUNTU_MEDIAINFO2 = "/usr/lib/libmediainfo.so.0.0.0";
    
    /** MAC OS **/
    //Notwendig fuer die Plattform Idendifikation
    public static final String MAC_ID = "mac";
    //Der Standardbrowser
    public static final String MAC_PATH = "netscape";
    //Das Flag zum aufrufen der URL
    public static final String MAC_FLAG = "-remote openURL";   
    //Name der MediaInfo Library 
    //Rename libmediainfo.0.0.0.dylib to libmediainfo.dylib   
    public static final String MAC_MEDIAINFO = "libmediainfo.dylib";   
}
