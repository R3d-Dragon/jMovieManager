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
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.*;

/**
 *
 * Beinhaltet alle Attribute, welche für einen Film relevant sind.
 * Strings können nicht "null" gesetzt werden, sondern lediglich "".
 *
 * @author Bryan Beck
 * @since 02.04.2011
 */
@Entity 
//@Table( name = "MOVIE" )
@Inheritance(strategy = InheritanceType.JOINED)
public class Movie extends PhysicalVideoFile implements Serializable, Cloneable{
    @ElementCollection 
    @CollectionTable(name="Filepaths", joinColumns=@JoinColumn(name="movie_uid"))
    @Column(name="filepath")
    private List<String> filePaths;
        
    private boolean watched;
 
    /**
    * Creates a new movie
    *
    * @param title The title of the movie
    */
    public Movie(String title){
        this();
        setTitle(title);
    }
    
    /**
     * Hibernate Constructor without arguments
     */
    protected Movie(){
        super();
        filePaths = new LinkedList<String>();
        this.setWatched(false);
    }

    /**
    * Methode die ein bestimmten filePath anhand des Index zurückgibt.
    * Null wenn kein filePath unter dem Index gefunden wird
    * @param i the index
    * @return the filePath
    */
    public String getFilePath(int i) {
        if(i < filePaths.size()){
            return filePaths.get(i);
        }
        return null;
    }

    /**
     * @return the filePaths
     */
    public List<String> getFilePaths() {
        return Collections.unmodifiableList(filePaths);
    }

    /**
     * @param filePaths the filePaths to set
     */
    public void setFilePaths(List<String> filePaths) {
        removeAllFilePaths();
        if(filePaths != null){
            this.filePaths.addAll(filePaths);
        }
    }

    /**
    * @param filePath the filePath to add
    */
    public void addFilePath(String filePath) {
        if(filePath != null){
            if(!filePaths.contains(filePath)){
                filePaths.add(filePath);
            }
        }
    }

    /**
    * @param i the index to set
    * @param filePath the filePath to set
    */
    public void replaceFilePath(int i, String filePath) {
        if(i <= filePaths.size()){
            if(filePath == null){
                filePaths.set(i, "");
            }
            else{
                filePaths.set(i, filePath);
            }
        }
    }

    /**
    * @param filePath the filePath to remove
    */
    public void removeFilePath(String filePath) {
        if((filePath != null) && (filePaths.contains(filePath))){
            filePaths.remove(filePath);
        }
    }

    /**
    * @param i the index to remove
    */
    public void removeFilePath(int i) {
        if(i <= filePaths.size()){
            filePaths.remove(i);
        }
    }

    /**
    * removes all filePaths form the list
    */
    public void removeAllFilePaths() {
        filePaths.clear();
    }

    /**
     * @return the watched
     */
    public boolean isWatched() {
        return watched;
    }

    /**
     * @param watched the watched to set
     */
    public void setWatched(boolean watched) {
        this.watched = watched;
    }
      
    /**
     * @see Object#toString() 
     */
    @Override
    public String toString(){
        return this.getTitle();
    }
    
    /**
     * Creates a copy of the entire object instance <b>without the collection reference</b>. <br/>
     * 
     * @see Object#clone() 
     * @return A copy of this object instance
     */
    @Override
    public Movie clone() {
        Movie clone = new Movie(this.getTitle());
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
