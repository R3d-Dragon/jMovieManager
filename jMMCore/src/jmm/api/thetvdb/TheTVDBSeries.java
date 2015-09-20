/**
 * Copyright (c) 2010-2015 Bryan Beck.
 * All rights reserved.
 * 
 * This project is licensed under LGPL v2.1.
 * See jMovieManager-license.txt for details.
 * 
 */
package jmm.api.thetvdb;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import jmm.data.LocalVideoFile;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;

/**
 *
 * @author Bryan Beck
 * @since 17.03.2013
 */
public class TheTVDBSeries extends LocalVideoFile{
    /** Logger. */
    private static final Logger LOG = LoggerFactory.logger(TheTVDBSeries.class);
    
    private List<TheTVDBEpisode> episodes; 
    
    private String thetvdbID;
    private String seriesID;
//    private String zap2it_id;
    
    private String imdbID;
    
//    private boolean adult;      //--> FSK 18
    private String backdropPath;
    private Date releaseDate;
    //private String popularity;
    private double theTVDBRating; 
    
    /**
    * Default constructor to create a new TMDBVideoFile
    *
    * @param title The movie or serie title
    *
     **/
    public TheTVDBSeries(String title){
        super();
        setTitle(title);
        
        episodes = new LinkedList<>();        
        this.setThetvdbID("");
        this.setSeriesID("");
        this.setImdbID("");
        this.setBackdropPath("");
        this.setReleaseDate(null);
        this.setTheTVDBRating(0);   
    }

    /**
     * @return the theTVDB url
     */
    public String getTheTVDBUrl() {
        return "http://thetvdb.com/?tab=series&id=" + getThetvdbID();
    }

    /**
     * @return the thetvdbID
     */
    public String getThetvdbID() {
        return thetvdbID;
    }

    /**
     * @param thetvdbID the thetvdbID to set
     */
    public void setThetvdbID(String thetvdbID) {
        if(thetvdbID == null){
            this.thetvdbID = "";
        }else{
            this.thetvdbID = thetvdbID;
        }
    }

    /**
     * @return the seriesID
     */
    public String getSeriesID() {
        return seriesID;
    }

    /**
     * @param seriesID the seriesID to set
     */
    public void setSeriesID(String seriesID) {
        if(seriesID == null){
            this.seriesID = "";
        }else{
            this.seriesID = seriesID;
        }
    }

    /**
     * @return the imdbUrl
     */
    public String getImdbUrl() {
        return "http://www.imdb.com/title/" + getImdbID();
    }

    /**
     * @return the imdbID
     */
    public String getImdbID() {
        return imdbID;
    }

    /**
     * @param imdbID the imdbID to set
     */
    public void setImdbID(String imdbID) {
        if(imdbID == null){
            this.imdbID = "";
        }else{
            this.imdbID = imdbID;
        }
    }

//    /**
//     * @return the adult
//     */
//    public boolean isAdult() {
//        return adult;
//    }
//
//    /**
//     * @param adult the adult to set
//     */
//    public void setAdult(boolean adult) {
//        this.adult = adult;
//    }

    /**
     * @return the backdropPath
     */
    public String getBackdropPath() {
        return backdropPath;
    }

    /**
     * @param backdropPath the backdropPath to set
     */
    public void setBackdropPath(String backdropPath) {
        if(this.backdropPath == null){
            this.backdropPath = "";
        }else{
            this.backdropPath = backdropPath;
        }
    }

    /**
     * @return the releaseDate
     */
    public Date getReleaseDate() {
        return releaseDate;
    }

    /**
     * @param releaseDate the releaseDate to set
     */
    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    /**
     * @return the thetvtbRating
     */
    public double getTheTVDBRating() {
        return theTVDBRating;
    }

    /**
     * @param thetvtbRating the rating to set
     */
    public void setTheTVDBRating(double thetvtbRating) {
        if(thetvtbRating < 0){
            this.theTVDBRating = 0;
        }
        else if(thetvtbRating > 10){
            this.theTVDBRating = 10;
        }
        else{
            this.theTVDBRating = thetvtbRating;
        }
    }    

    /**
     * @return the episodes
     */
    public List<TheTVDBEpisode> getEpisodes() {
        return episodes;
    }

    /**
     * @param episodes the episodes to set
     */
    public void setEpisodes(List<TheTVDBEpisode> episodes) {
        this.episodes = episodes;
    }
}
