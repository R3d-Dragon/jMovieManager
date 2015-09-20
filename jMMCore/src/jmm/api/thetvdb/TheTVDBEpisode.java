/**
 * Copyright (c) 2010-2015 Bryan Beck.
 * All rights reserved.
 * 
 * This project is licensed under LGPL v2.1.
 * See jMovieManager-license.txt for details.
 * 
 */
package jmm.api.thetvdb;

import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;

/**
 *
 * @author Bryan Beck
 * @since 17.03.2013
 */
public class TheTVDBEpisode extends TheTVDBSeries{ 
    /** Logger. */
    private static final Logger LOG = LoggerFactory.logger(TheTVDBEpisode.class);
    
    private int episodeNumber;
    private String productionCode;
    
    private int seasonNumber;
    private String seasonID;
    
    /**
    * Default constructor to create a new "the tv database" episode
    *
    * @param title the episode title
    *
    **/
    public TheTVDBEpisode(String title){
        super(title);  
        this.setEpisodeNumber(0);
        this.setProductionCode("");
        this.setSeasonNumber(0);
        this.setSeasonID("");
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
        this.episodeNumber = episodeNumber;
    }

    /**
     * @return the productionCode
     */
    public String getProductionCode() {
        return productionCode;
    }

    /**
     * @param productionCode the productionCode to set
     */
    public void setProductionCode(String productionCode) {
        if(productionCode == null){
            this.productionCode = "";
        }
        else{
            this.productionCode = productionCode;
        }
    }

    /**
     * @return the seasonNumber
     */
    public int getSeasonNumber() {
        return seasonNumber;
    }

    /**
     * @param seasonNumber the seasonNumber to set
     */
    public void setSeasonNumber(int seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    /**
     * @return the seasonID
     */
    public String getSeasonID() {
        return seasonID;
    }

    /**
     * @param seasonID the seasonID to set
     */
    public void setSeasonID(String seasonID) {
        if(seasonID == null){
            this.seasonID = "";
        }
        else{
            this.seasonID = seasonID;
        }
    }
}
