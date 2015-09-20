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
 * @author Bryan Beck
 * @since 19.08.2011
 */
@Entity 
//@Table( name = "LOCALVIDEOFILE" )
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class LocalVideoFile extends VideoFile implements Serializable{
    
    private String trailerPath;
    private double onlineRating;
       
     /**
     * Hibernate Constructor without arguments
     */
    protected LocalVideoFile(){
        super();
        
        this.setTrailerPath("");
        this.setOnlineRating(0);
    } 
        
    /**
     * @return the trailerPath
     */
    public String getTrailerPath() {
        return trailerPath;
    }

    /**
     * @param trailerPath the trailerPath to set
     */
    public void setTrailerPath(String trailerPath) {
        if(trailerPath == null){
            this.trailerPath = "";
        }
        else{
            this.trailerPath = trailerPath;
        }
    }
    
     /**
     * @return the imdbRating
     */
    public double getOnlineRating() {
        return onlineRating;
    }

    /**
     * @param onlineRating the online rating to set
     */
    public void setOnlineRating(double onlineRating) {
        if(onlineRating < 0){
            this.onlineRating = 0;
        }
        else if(onlineRating > 10){
            this.onlineRating = 10;
        }
        else{
            this.onlineRating = onlineRating;
        }
    }
}
