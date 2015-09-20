/**
 * Copyright (c) 2010-2015 Bryan Beck.
 * All rights reserved.
 * 
 * This project is licensed under LGPL v2.1.
 * See jMovieManager-license.txt for details.
 * 
 */
package jmm.data;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

/**
 *
 * Beinhaltet alle Attribute, welche für Lokale, physisch vorhandene Video Dateien relevant sind.
 * Strings können nicht "null" gesetzt werden, sondern lediglich leer.
 *
 * @author Bryan Beck
 * @since 02.04.2011
 */
@Entity 
//@Table( name = "PHYSICALVIDEOFILE" )
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class PhysicalVideoFile extends LocalVideoFile implements Serializable{
    private String videoCodec; 
    private int    videoBitrate;
    private String audioCodec;
    private int    audioBitrate;
    private int    audioChannels;
    private String videoSource;
    
    private float averageFPS;
    private int width;
    private int height;
    
    /**
     * Hibernate Constructor without arguments
     */
    protected PhysicalVideoFile(){
        super();
        
        this.setVideoCodec("");
        this.setVideoBitrate(0);
        this.setAudioCodec("");
        this.setAudioBitrate(0);
        this.setAudioChannels(0);
        this.setVideoSource("");       
        this.setAverageFPS(0);
        this.setWidth(0);
        this.setHeight(0);
    }

    /**
     * @return the videoCodec
     */
    public String getVideoCodec() {
        return videoCodec;
    }

    /**
     * @param videoCodec the videoCodec to set
     */
    public void setVideoCodec(String videoCodec) {
        if(videoCodec == null){
            this.videoCodec = "";
        }
        else{
            this.videoCodec = videoCodec;
        }
    }

    /**
     * @return the videoBitrate
     */
    public int getVideoBitrate() {
        return videoBitrate;
    }

    /**
     * @param videoBitrate the videoBitrate to set
     */
    public void setVideoBitrate(int videoBitrate) {
        if(videoBitrate < 0){
            this.videoBitrate = 0;
        }
        else{
            this.videoBitrate = videoBitrate;
        }
    }

    /**
     * @return the audioCodec
     */
    public String getAudioCodec() {
        return audioCodec;
    }

    /**
     * @param audioCodec the audioCodec to set
     */
    public void setAudioCodec(String audioCodec) {
        if(audioCodec == null){
            this.audioCodec = "";
        }
        else{
            this.audioCodec = audioCodec;
        }
    }

    /**
     * @return the audioBitrate
     */
    public int getAudioBitrate() {
        return audioBitrate;
    }

    /**
     * @param audioBitrate the audioBitrate to set
     */
    public void setAudioBitrate(int audioBitrate) {
        if(audioBitrate < 0){
            this.audioBitrate = 0;
        }
        else{
            this.audioBitrate = audioBitrate;
        }
    }

    /**
     * @return the audioChannels
     */
    public int getAudioChannels() {
        return audioChannels;
    }

    /**
     * @param audioChannels the audioChannels to set
     */
    public void setAudioChannels(int audioChannels) {
        if(audioChannels < 0){
            this.audioChannels = 0;
        }
        else{
            this.audioChannels = audioChannels;
        }
    }

    /**
     * @return the videoSource
     */
    public String getVideoSource() {
        return videoSource;
    }

    /**
     * @param videoSource the videoSource to set
     */
    public void setVideoSource(String videoSource) {
        if(videoSource == null){
            this.videoSource = "";
        }
        else{
            this.videoSource = videoSource;
        }
    }

    /**
     * @return the averageFPS
     */
    public float getAverageFPS() {
        return averageFPS;
    }

    /**
     * @param averageFPS the averageFPS to set
     */
    public void setAverageFPS(float averageFPS) {
        if(averageFPS < 0){
            this.averageFPS = 0;
        }
        else{
            this.averageFPS = averageFPS;
        }
    }

    /**
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * @param width the width to set
     */
    public void setWidth(int width) {
        if(width < 0){
            this.width = 0;
        }
        else{
            this.width = width;
        }
    }

    /**
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * @param height the height to set
     */
    public void setHeight(int height) {
        if(height < 0){
            this.height = 0;
        }
        else{
            this.height = height;
        }
    }
}
