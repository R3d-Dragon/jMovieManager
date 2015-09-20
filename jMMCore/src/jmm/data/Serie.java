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
import java.util.*;
import javax.persistence.*;

/**
 *
 * Klasse zum anlegen neuer Serien
 * 
 * @author Bryan Beck
 * @since 20.07.2011
 */
@Entity 
//@Table( name = "SERIE" )
@Inheritance(strategy = InheritanceType.JOINED)
public class Serie extends LocalVideoFile implements Serializable, Cloneable{
   
    @OneToMany(targetEntity=Season.class, mappedBy="serie", orphanRemoval=true) //, fetch= FetchType.EAGER)     //Fetching Eager in order to add all seasons / episodes to the gui on open()
    private List<Season> seasons;
    
    /**
     * Creates a new serie
     *
     * @param title The title of the serie
     */
    public Serie(String title){
        this();
        setTitle(title);
    }
    
    /**
     * Hibernate Constructor without arguments
     */
    protected Serie(){
        super();
        seasons = new LinkedList<Season>(); 
    }

    /**
    * Methode die ein bestimmten season anhand des Index zurückgibt.
    * Null wenn keine season unter dem Index gefunden wird
    * @param index the index
    * @return the season
    */
    public Season getSeason(int index) {
        if(index < seasons.size()){
            return seasons.get(index);
        }
        return null;
    }
    
    /**
    * Gibt die Serie mit der SerienNr zurück
     * 
    * @param seasonNr the season Number
    * @return the season, <br/> null, if there is no season with the seasonNr 
    */
    public Season getSeasonNr(int seasonNr) {
        for(Season season: seasons){
            if(season.getSeasonNumber() == seasonNr){
                return season;
            }
        }
        return null;
    }    
    
    /**
     * @return the seasons
     */
    public List<Season> getSeasons() {
        return Collections.unmodifiableList(seasons);
    }

    /**
     * @param seasons the seasons to set
     */
    public void setSeasons(List<Season> seasons) {
        removeAllSeasons();       
        if(seasons != null){
            for(Season season: seasons){
                this.addSeason(season);
            }
        }
    }
    
    /**
     * Overrides a season with the same seasonNr and sets the new one into the serie
     * @param season the season to set
     * @return the old season
     */
    public Season setSeason(Season season){
        Season oldSeason = null;
        if((season != null)){
            oldSeason = this.getSeasonNr(season.getSeasonNumber());
            this.removeSeason(oldSeason);
            this.addSeason(season);
        }
        return oldSeason;
    }

    /**
     * Adds a new season if the season number doesn't already exist
     * 
    * @param season the season to add
    * @return true If the season was added to the serie <br/>
    * false otherwise
    */
    public boolean addSeason(Season season) {
        if((season != null) && (!this.containsSeasonNr((season.getSeasonNumber())))){
//        if((season != null) && (!seasons.contains(season))){
            seasons.add(season);
            season.setSerie(this);
            return true;
        }
        return false;
    }

     /**
     * Überprüft, ob ein bereits eine Season mit der Nummer existiert.
     *
     * @param seasonNr Die Seasonnnummer
     *
     * @return true, wenn Season bereits vorhanden
     */
    public boolean containsSeasonNr(int seasonNr){
        for(Season tempSeason: seasons){
            if(tempSeason.getSeasonNumber() == seasonNr){
                return true;
            }
        }
        return false;
    }    
    
    /**
    * @param season the season to remove
    * @return true If the season was removed from the serie <br/>
    * false otherwise
    */
    public boolean removeSeason(Season season) {
        if(season != null){
            seasons.remove(season);
            season.setSerie(null);
            return true;
        }
        return false;
    }

    /**
    * @param i the index to remove
    * @return true If the season was removed from the serie <br/>
    * false otherwise
    */
    public void removeSeason(int i) {
        if(i <= seasons.size()){
            removeSeason(seasons.get(i));
        }
    }

    /**
    * removes all seasons form the list
    */
    public void removeAllSeasons() {
        for(int i = seasons.size()-1; i >= 0; i--){
            this.removeSeason(seasons.get(i));
        }
//        for(Season season: seasons){
//            this.removeSeason(season);
//        }
    }
    
    /**
     * Methode welche alle Seasons IN DER DATENSTRUKTUR anhand der SeasonNr sortiert
     *
     *  The sorting algorithm is a modified mergesort (in which the merge is omitted if the highest element in the low sublist is less than the lowest element in the high sublist).
     *  This algorithm offers guaranteed n log(n) performance.
     *
     */
    public void orderSeasonsByNumber(){
        Collections.sort(seasons, new Comparator<Season>() {
            /**
             * Methode welche alle Seasons IN DER DATENSTRUKTUR anhand der SeasonNr sortiert
             * @param o1 - the first object to be compared.
             * @param o2 - the second object to be compared.
             *
             * @return a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater than the second.
             *
             * @throws  ClassCastException - if the arguments' types prevent them from being compared by this Comparator.
             */
            @Override
            public int compare(Season o1, Season o2) {
                return o1.getSeasonNumber() - o2.getSeasonNumber();
            }
        });
    }       
      
    /**
     * @see Object#toString() 
     */
    @Override
    public String toString(){
        return this.getTitle();
    }
      
    /**
     * Creates a copy of the entire object instance <b>without collection and season references</b>. <br/>
     * 
     * @see Object#clone() 
     * @return A copy of this object instance
     */
    @Override
    public Serie clone() {
        Serie clone = new Serie(this.getTitle());
        clone.setOriginalTitle(this.getOriginalTitle());
        clone.setGenreKeys(this.getGenreKeys());
        clone.setPlaytime(this.getPlaytime());
        clone.setReleaseYear(this.getReleaseYear());
        clone.setDescription(this.getDescription());
        clone.setPublisher(this.getPublisher());
        clone.setActors(this.getActors());
        clone.setDirector(this.getDirector());
        clone.setFsk(this.getFsk());
//        clone.setVideoCodec(this.getVideoCodec());
//        clone.setVideoBitrate(this.getVideoBitrate());
//        clone.setAudioCodec(this.getAudioCodec());
//        clone.setAudioBitrate(this.getAudioBitrate());
//        clone.setAudioChannels(this.getAudioChannels());
//        clone.setVideoSource(this.getVideoSource());
//        clone.setAverageFPS(this.getAverageFPS());
//        clone.setWidth(this.getWidth());
//        clone.setHeight(this.getHeight());
        clone.setImagePath(this.getImagePath());
        clone.setImage(this.getImage());
//        clone.setFilePaths(this.getFilePaths());
        clone.setTrailerPath(this.getTrailerPath());
        clone.setOnlineRating(this.getOnlineRating());
        clone.setPersonalRating(this.getPersonalRating());
        clone.setCustomAttributes(this.getCustomAttributes());
//        clone.setWatched(this.isWatched());        
        return clone;
    }
}
