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
 * Alle definierten Video Codecs sind hier zu finden
 * 
 * @author Bryan Beck
 * @since 19.12.2011
 */
public interface VideoInterface {    
    public static final String VIDEO_CODEC_EMPTY = "Value.empty";
    public static final String VIDEO_CODEC_AVC = "Video.codec.01";
    public static final String VIDEO_CODEC_DIVX = "Video.codec.02";
    public static final String VIDEO_CODEC_MPEG4 = "Video.codec.03";
    public static final String VIDEO_CODEC_XVID = "Video.codec.04";

    public static final String[] VIDEO_CODECS = 
    {
        VIDEO_CODEC_EMPTY, 
        VIDEO_CODEC_AVC, 
        VIDEO_CODEC_DIVX, 
        VIDEO_CODEC_MPEG4,
        VIDEO_CODEC_XVID
    };          
}
