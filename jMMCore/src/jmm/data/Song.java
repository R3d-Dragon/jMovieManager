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

/**
 * Beinhaltet alle Attribute, welche für Musik relevant sind
 * 
 * @author Bryan Beck
 * @since 16.02.2012
 */
//@Entity
//@Table( name = "SONG" )
//@Inheritance(strategy = InheritanceType.JOINED)
public class Song extends MediaFile implements Serializable, Cloneable{        
    private String filePath;
    private String audioCodec;
    private int    audioBitrate;
    private int    audioChannels;
    private boolean listened;
    
    /**
    * Creates a new song
    *
    * @param title The title of the song
    */    
    public Song(String title){
        this();
        setTitle(title);            
    }    
    
    /**
     * Hibernate Constructor without arguments
     */
    protected Song(){
        this.setFilePath("");
        this.setAudioCodec("");
        this.setAudioBitrate(0);
        this.setAudioChannels(0);
        this.setListened(false);
    }
    
    /**
     * @return the filePath
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * @param filePath the filePath to set
     */
    public void setFilePath(String filePath) {
        if(filePath == null){
            this.filePath = "";
        }
        else{
            this.filePath = filePath;
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
     * @return the listened
     */
    public boolean isListened() {
        return listened;
    }

    /**
     * @param listened the listened to set
     */
    public void setListened(boolean listened) {
        this.listened = listened;
    }
     
    /**
     * Creates a copy of the entire object instance <b>without the collection reference</b>. <br/>
     * 
     * @see Object#clone() 
     * @return A copy of this object instance
     */
    @Override
    public Song clone() {
        Song clone = new Song(this.getTitle());
//        clone.setOriginalTitle(this.getOriginalTitle());
        clone.setGenreKeys(this.getGenreKeys());
        clone.setPlaytime(this.getPlaytime());
        clone.setReleaseYear(this.getReleaseYear());
//        clone.setDescription(this.getDescription());
        clone.setPublisher(this.getPublisher());
//        clone.setActors(this.getActors());
//        clone.setDirector(this.getDirector());
//        clone.setFsk(this.getFsk());
//        clone.setVideoCodec(this.getVideoCodec());
//        clone.setVideoBitrate(this.getVideoBitrate());
        clone.setAudioCodec(this.getAudioCodec());
        clone.setAudioBitrate(this.getAudioBitrate());
        clone.setAudioChannels(this.getAudioChannels());
//        clone.setVideoSource(this.getVideoSource());
//        clone.setAverageFPS(this.getAverageFPS());
//        clone.setWidth(this.getWidth());
//        clone.setHeight(this.getHeight());
//        clone.setImagePath(this.getImagePath());
//        clone.setImage(this.getImage());
//        clone.setFilePaths(this.getFilePaths());
//        clone.setTrailerPath(this.getTrailerPath());
//        clone.setImdbRating(this.getImdbRating());
        clone.setPersonalRating(this.getPersonalRating());
        clone.setCustomAttributeName(this.getCustomAttributeName());
        clone.setCustomAttributeValue(this.getCustomAttributeValue());
//        clone.setWatched(this.isWatched());        
        return clone;
    }
}
