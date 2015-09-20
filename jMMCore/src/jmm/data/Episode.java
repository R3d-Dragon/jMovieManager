/**
 * Copyright (c) 2010-2015 Bryan Beck.
 * All rights reserved.
 * 
 * This project is licensed under LGPL v2.1.
 * See jMovieManager-license.txt for details.
 * 
 */
package jmm.data;

import jmm.xml.MyXMLAnnotation;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * Klasse zum anlegen einer neuen Episode
 * 
 * @author Bryan Beck
 * @since 20.07.2011
 */
@Entity 
//@Table( name = "EPISODE" )
@Inheritance(strategy = InheritanceType.JOINED)
public class Episode extends Movie implements Serializable, Cloneable{
    private int episodeNumber; 
    
    @ManyToOne
    @JoinColumn
    private Season season;  //Gibt an, zu welcher Season die Episode gehört
       
    /**
    * Creates a new episode
    *
    * @param title The title of the episode
    * @param episodeNumber The episode number
    */
    public Episode(String title, int episodeNumber){
        super(title);
        this.setEpisodeNumber(episodeNumber);
    }    
        
    /**
     * Hibernate Constructor without arguments
     */
    protected Episode(){
        super();
    }

    /**
     * @return the episodeNumber
     */
    public int getEpisodeNumber() {
        return episodeNumber;
    }

    /**
     * @param episodeNumber the episodeNumber to set
     */
    public void setEpisodeNumber(int episodeNumber) {
        if(episodeNumber < 0){
            this.episodeNumber = 0;
        }
        else{
            this.episodeNumber = episodeNumber;           
        }
    }

    /**
     * @return the season
     */
    @MyXMLAnnotation(isTransient=true)
    public Season getSeason() {
        return season;
    }

    /**
     * @param season the season to set
     */
    public void setSeason(Season season) {
        this.season = season;
    }
    
    /**
     * @see Object#toString() 
     */
    @Override
    public String toString(){
        return this.getEpisodeNumber() + " - " + this.getTitle();
    }
    
    /**
     * Creates a copy of the entire object instance <b>without the collection  or season reference</b>. <br/>
     * 
     * @see Object#clone() 
     * @return A copy of this object instance
     */
    @Override
    public Episode clone() {
        Episode clone = new Episode(this.getTitle(), this.getEpisodeNumber());
        clone.setOriginalTitle(this.getOriginalTitle());
        clone.setGenreKeys(this.getGenreKeys());
        clone.setPlaytime(this.getPlaytime());
        clone.setReleaseYear(this.getReleaseYear());
        clone.setDescription(this.getDescription());
        clone.setPublisher(this.getPublisher());
        clone.setActors(this.getActors());
        clone.setDirector(this.getDirector());
        clone.setFsk(this.getFsk());
        clone.setVideoCodec(this.getVideoCodec());
        clone.setVideoBitrate(this.getVideoBitrate());
        clone.setAudioCodec(this.getAudioCodec());
        clone.setAudioBitrate(this.getAudioBitrate());
        clone.setAudioChannels(this.getAudioChannels());
        clone.setVideoSource(this.getVideoSource());
        clone.setAverageFPS(this.getAverageFPS());
        clone.setWidth(this.getWidth());
        clone.setHeight(this.getHeight());
        clone.setImagePath(this.getImagePath());
        clone.setImage(this.getImage());
        clone.setFilePaths(this.getFilePaths());
        clone.setTrailerPath(this.getTrailerPath());
        clone.setOnlineRating(this.getOnlineRating());
        clone.setPersonalRating(this.getPersonalRating());
        clone.setCustomAttributes(this.getCustomAttributes());
        clone.setWatched(this.isWatched());
        return clone;
    }
}
