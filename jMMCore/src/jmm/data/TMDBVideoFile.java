/**
 * Copyright (c) 2010-2015 Bryan Beck.
 * All rights reserved.
 * 
 * This project is licensed under LGPL v2.1.
 * See jMovieManager-license.txt for details.
 * 
 */
package jmm.data;

import java.util.Date;

/**
 *
 * @author Bryan Beck
 * @since 22.02.2013
 */
public class TMDBVideoFile extends LocalVideoFile{
    
    //private String tmdbUrl;
    private String tmdbID;
    //private String imdbUrl;
    private String imdbID;
    
    private boolean adult;      //--> FSK 18
    private String backdropPath;
    private Date releaseDate;
    //private String popularity;
    private double tmdbRating;
            
    /**
    * Creates a new TMDBVideoFile
    *
    * @param title The title of the tmdb movie
    *
     **/
    public TMDBVideoFile(String title){
        super();
        setTitle(title);
        
        this.setTmdbID("");
        this.setImdbID("");
        this.setAdult(false);
        this.setBackdropPath("");
        this.setReleaseDate(null);
        this.setTmdbRating(0);   
    }

    /**
     * @return the tmdbUrl
     */
    public String getTmdbUrl() {
        return "http://www.themoviedb.org/movie/" + getTmdbID();
    }
//
//    /**
//     * @param tmdbUrl the tmdbUrl to set
//     */
//    public void setTmdbUrl(String tmdbUrl) {
//        if(tmdbUrl == null){
//            this.tmdbUrl = "";
//        }else{
//            this.tmdbUrl = tmdbUrl;
//        }
//    }

    /**
     * @return the tmdbID
     */
    public String getTmdbID() {
        return tmdbID;
    }

    /**
     * @param tmdbID the tmdbID to set
     */
    public void setTmdbID(String tmdbID) {
        if(tmdbID == null){
            this.tmdbID = "";
        }else{
            this.tmdbID = tmdbID;
        }
    }

    /**
     * @return the imdbUrl
     */
    public String getImdbUrl() {
        return "http://www.imdb.com/title/" + getImdbID();
    }
//
//    /**
//     * @param imdbUrl the imdbUrl to set
//     */
//    public void setImdbUrl(String imdbUrl) {
//        if(imdbUrl == null){
//            this.imdbUrl = "";
//        }else{
//            this.imdbUrl = imdbUrl;
//        }
//    }

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

    /**
     * @return the adult
     */
    public boolean isAdult() {
        return adult;
    }

    /**
     * @param adult the adult to set
     */
    public void setAdult(boolean adult) {
        this.adult = adult;
    }

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
     * @return the tmdbRating
     */
    public double getTmdbRating() {
        return tmdbRating;
    }

    /**
     * @param tmdbRating the tmdbRating to set
     */
    public void setTmdbRating(double tmdbRating) {
        if(tmdbRating < 0){
            this.tmdbRating = 0;
        }
        else if(tmdbRating > 10){
            this.tmdbRating = 10;
        }
        else{
            this.tmdbRating = tmdbRating;
        }
    }
}
