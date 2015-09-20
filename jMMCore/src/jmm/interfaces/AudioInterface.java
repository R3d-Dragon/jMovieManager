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
 * Alle definierten Audio Kanäle und Codecs sind hier zu finden
 * 
 * @author Bryan Beck
 * @since 19.12.2011
 */
public interface AudioInterface {
    public static final String AUDIO_CODEC_EMPTY = "Value.empty";
    public static final String AUDIO_CODEC_WAV = "Audio.codec.01";
    public static final String AUDIO_CODEC_MPEG = "Audio.codec.02";
    public static final String AUDIO_CODEC_MP3 = "Audio.codec.03";
    public static final String AUDIO_CODEC_AC3 = "Audio.codec.04";
    public static final String AUDIO_CODEC_DTS = "Audio.codec.05";
    
    public static final String[] AUDIO_CODECS = 
    {
        AUDIO_CODEC_EMPTY, 
        AUDIO_CODEC_WAV, 
        AUDIO_CODEC_MPEG, 
        AUDIO_CODEC_MP3,
        AUDIO_CODEC_AC3,
        AUDIO_CODEC_DTS
    };    
    
    public static final String AUDIO_CHANNEL_EMPTY = "Value.empty";
    public static final String AUDIO_CHANNEL_1 = "Audio.channel.01";
    public static final String AUDIO_CHANNEL_2 = "Audio.channel.02";
    public static final String AUDIO_CHANNEL_3 = "Audio.channel.03";
    public static final String AUDIO_CHANNEL_4 = "Audio.channel.04";
    public static final String AUDIO_CHANNEL_5 = "Audio.channel.05";
    public static final String AUDIO_CHANNEL_6 = "Audio.channel.06";
    public static final String AUDIO_CHANNEL_7 = "Audio.channel.07";
    public static final String AUDIO_CHANNEL_8 = "Audio.channel.08";
    
    public static final String[] AUDIO_CHANNELS = 
    {
        AUDIO_CHANNEL_EMPTY, 
        AUDIO_CHANNEL_1, 
        AUDIO_CHANNEL_2, 
        AUDIO_CHANNEL_3,
        AUDIO_CHANNEL_4,
        AUDIO_CHANNEL_5,
        AUDIO_CHANNEL_6,
        AUDIO_CHANNEL_7,
        AUDIO_CHANNEL_8
    };      
}
