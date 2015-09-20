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
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

/**
 * Class to define an actor
 * 
 * @author Bryan Beck
 * @since 29.08.2012
 */
@Entity
//@Table( name = "ACTOR" )
public class Actor implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)     //alternativ GenerationType.AUTO
    private Long uid;
    
    @Column(nullable=false)//, unique=true)
    private String name;
    
    private int tmdbID;
    
    private String profilePicturePath;
    
       
    @Transient
    private static final Comparator<Actor> sortByName = new Comparator<Actor>(){
            /**
             * Methode welche alle Schauspieler nach dem Namen sortiert
             * @param name1 - Der erste Name
             * @param name2 - Der zweite name
             *
             * @return a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater than the second.
             *
             * @throws  ClassCastException - if the arguments' types prevent them from being compared by this Comparator.
             */ 
            @Override
            public int compare(Actor actor1, Actor actor2) {
                return actor1.getName().compareTo(actor2.getName());
            }
        };
    
    @Transient
    private static Set<Actor> allActors = new TreeSet<Actor>(sortByName);
    
    /**
     * creates a new actor
     * @param name the name of the actor
     */
    public Actor(String name){
        this.name = name;
        this.setTmdbID(0);
        this.setProfilePicturePath("");
    }
    
     /**
     * creates a new actor
     */   
    protected Actor(){        
    }

    /**
     * @return the uid
     */
    @MyXMLAnnotation(isTransient=true)
    public Long getUid() {
        return uid;
    }
    
    public void setUid(Long uid) {
        this.uid = uid;
    }
    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        if(name == null){
            this.name = "";
        }
        else{
            this.name = name;
        }
    }

    /**
     * @return the tmdbID
     */
    public int getTmdbID() {
        return tmdbID;
    }

    /**
     * @param tmdbID the tmdbID to set
     */
    public void setTmdbID(int tmdbID) {
        this.tmdbID = tmdbID;
    }

    /**
     * @return the profilePicture
     */
    public String getProfilePicturePath() {
        return profilePicturePath;
    }

    /**
     * @param profilePicturePath the profile picture path to set
     */
    public void setProfilePicturePath(String profilePicturePath) {
        if(profilePicturePath == null){
            this.profilePicturePath = "";
        }else{
            this.profilePicturePath = profilePicturePath;
        }
    }
    
    
    /**
     * @return the actorsSet as an unmodifiable Set
     */
    public static Set<Actor> getActorsSet() {
        return allActors;
    }

    /**
     * @param actor an Actor to put into the set
     * 
     * @return true Der Schauspieler wurde hinzugefügt<br/> 
     * false Der Schauspieler ist bereits vorhanden
     */
    public static boolean putActorIntoSet(Actor actor) {
        return allActors.add(actor);
    }
}
