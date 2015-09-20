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
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.*;

/**
 * Klasse zum anlegen neuer Staffeln
 * 
 * @author Bryan Beck
 * @since 20.07.2011
 */
@Entity 
//@Table( name = "SEASON" )
@Inheritance(strategy = InheritanceType.JOINED)
public class Season implements Serializable, Cloneable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)     //alternativ GenerationType.AUTO
    private Long uid;
    
    @ManyToOne
    @JoinColumn
    private Serie serie;    //Gibt an, zu welcher Serie die Season gehört
        
    @OneToMany(targetEntity=Episode.class, mappedBy="season", orphanRemoval=true)
    private List<Episode> episodes;
    
    private int seasonNumber;

    /**
     * Vollständiger Konstruktor zum erstellen einer neuen Staffel
     *
     * @param seasonNumber - Die Staffelnummer
     * @param episodes Die Episoden einer Staffel
     */
    public Season(int seasonNumber, LinkedList<Episode> episodes){
        this.episodes = new LinkedList<Episode>();
        this.setSeasonNumber(seasonNumber);
        this.setEpisodes(episodes);
    }
    
    /**
     * Konstruktor zum erstellen einer neuen Staffel
     *
     * @param seasonNumber - Die Staffelnummer
     */
    public Season(int seasonNumber){
        this.episodes = new LinkedList<Episode>();
        this.setSeasonNumber(seasonNumber);
    }
    
    /**
     * Hibernate Constructor without arguments
     */
    protected Season(){}
    
    /**
    * Methode die ein bestimmten episode anhand des Index zurückgibt.
    * Null wenn keine episode unter dem Index gefunden wird
    * @param index the index
    * @return the episode
    */
    public Episode getEpisode(int index) {
        if(index < episodes.size()){
            return episodes.get(index);
        }
        return null;
    }
    
    /**
    * Gibt die Episode mit der EpisodeNr zurück
     * 
    * @param episodeNr the episode Number
    * @return the episode, <br/> null, if there is no episode with the episodeNr 
    */
    public Episode getEpisodeNr(int episodeNr) {
        for(Episode episode: episodes){
            if(episode.getEpisodeNumber() == episodeNr){
                return episode;
            }
        }
        return null;
    }     
    
    /**
     * Returns the <b>first</b> episode with the given title
     *
     * @param title the title of the episode
     *
     * @return The <b>first</b> episode with the title <br/> null, if no episode was found with this title
     */
    public Episode getEpisode(String title){
        Episode mediaFile;

        int size = episodes.size();
        for (int i= 0; i < size; i++){
            mediaFile  = episodes.get(i);
            if(mediaFile.getTitle().equals(title)){
                return mediaFile;
            }
        }
        return null;
    }    

    /**
     * @return the episodes
     */
    public List<Episode> getEpisodes() {
        return Collections.unmodifiableList(episodes);
    }

    /**
     * @param episodes the episodes to set
     */
    public void setEpisodes(LinkedList<Episode> episodes) {
        this.removeAllEpisodes();
        if(episodes != null){
            for(Episode episode: episodes){
                this.addEpisode(episode);
            }
        }
    }
    
    /**
     * Overrides a episode with the same episodeNr and sets the new one into the season
     * @param episode the episode to set
     * @return the old episode
     */    
    public Episode setEpisode(Episode episode){
        Episode oldEpisode = null;
        if((episode != null)){
            oldEpisode = this.getEpisodeNr(episode.getEpisodeNumber());
            this.removeEpisode(oldEpisode);
            this.addEpisode(episode);
        }
        return oldEpisode;        
    }

    /**
    * @param episode the episode to add
    * @return true If the episode was added to the season <br/>
    * false otherwise
    */
    public boolean addEpisode(Episode episode) {
        if((episode != null) &&(!this.containsEpisodeNr(episode.getEpisodeNumber()))){
            episodes.add(episode);
            episode.setSeason(this);          
            return true;
        }
        return false;
    }
    
    
     /**
     * Überprüft, ob ein bereits eine Episode mit der Nummer existiert.
     *
     * @param episodeNr Die Episodennummer
     *
     * @return true, wenn Episode bereits vorhanden
     */
    public boolean containsEpisodeNr(int episodeNr){
        for(Episode tempEpisode: episodes){
            if(tempEpisode.getEpisodeNumber() == episodeNr){
                return true;
            }
        }
        return false;
    }

    /**
    * @param episode the episode to remove
    * 
    * @return true If the episode was removed from the season <br/>
    * false otherwise
    */
    public boolean removeEpisode(Episode episode) {
        if(episode != null){
            if(episodes.remove(episode)){
                episode.setSeason(null);
                return true;
            }
        }
        return false;
    }

    /**
    * @param  the index to remove
    */
    public void removeEpisode(int index) {
        if(index <= episodes.size()){
            this.removeEpisode(episodes.get(index));
        }
    }
    
    /**
     * Removes the <b>first</b> episode with the given title from the season list
     * 
    * @param title the title of the episode
    */   
    public Episode removeEpisode(String title){
        Episode objectToRemove = this.getEpisode(title);
        this.removeEpisode(objectToRemove);
        return objectToRemove;
    }

    /**
    * removes all episodes form the list
    */
    public void removeAllEpisodes() {
        for(int i = episodes.size()-1; i >= 0; i--){
            this.removeEpisode(episodes.get(i));
        }
//        for(Episode episode: episodes){
//            this.removeEpisode(episode);
//        }
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
        if(seasonNumber < 0){
            this.seasonNumber = 0;
        }
        else{
            this.seasonNumber = seasonNumber;
        }
    }
    
    /**
     * Methode welche alle Episoden IN DER DATENSTRUKTUR anhand der EpisodeNr sortiert
     *
     *  The sorting algorithm is a modified mergesort (in which the merge is omitted if the highest element in the low sublist is less than the lowest element in the high sublist).
     *  This algorithm offers guaranteed n log(n) performance.
     *
     */
    public void orderEpisodesByNumber(){
        Collections.sort(episodes, new Comparator<Episode>() {
            /**
             * Methode welche alle Episoden IN DER DATENSTRUKTUR anhand der EpisodeNr sortiert
             * @param o1 - the first object to be compared.
             * @param o2 - the second object to be compared.
             *
             * @return a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater than the second.
             *
             * @throws  ClassCastException - if the arguments' types prevent them from being compared by this Comparator.
             */
            @Override
            public int compare(Episode o1, Episode o2) {
                return o1.getEpisodeNumber() - o2.getEpisodeNumber();
            }
        });
    }

    /**
     * @return the uid
     */
    @MyXMLAnnotation(isTransient=true)
    public Long getUid() {
        return uid;
    }

    /**
     * @param uid the uid to set
     */
    public void setUid(Long uid) {
        this.uid = uid;
    }

    /**
     * @return the serie
     */
    @MyXMLAnnotation(isTransient=true)
    public Serie getSerie() {
        return serie;
    }

    /**
     * @param serie the serie to set
     */
    public void setSerie(Serie serie) {
        this.serie = serie;
    }
    
        /**
     * @see Object#toString() 
     */
    @Override
    public String toString(){
        return String.valueOf(this.getSeasonNumber());
    }
    
    /**
     * Creates a copy of the entire object instance <b>without episode or serie references</b>. <br/>
     * 
     * @see Object#clone() 
     * @return A copy of this object instance
     */
    @Override
    public Season clone() {
        Season clone = new Season(this.getSeasonNumber());
        return clone;
    }
}
