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
import javax.swing.ImageIcon;
import jmm.persist.PersistingManager;

/**
 *
 * Beinhaltet alle Attribute, welche für Video Dateien relevant sind.
 * Strings können nicht "null" gesetzt werden, sondern lediglich leer.
 *
 * @author Bryan Beck
 * @since 02.04.2011
 */
@Entity 
//@Table( name = "VIDEOFILE" )
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class VideoFile extends MediaFile implements Serializable{
    private String originalTitle;
    
    @Column(length=20000)
    private String description;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "Videofile_Actor", 
            joinColumns = { @JoinColumn(name = "videofile_uid") }, 
            inverseJoinColumns = { @JoinColumn(name = "actor_uid") })
    private Set<Actor> actors;
    private String director;
    
    //FSK Types
    public static enum FSK{
        FSK_UNKNOWN,
        FSK_0,
        FSK_6,
        FSK_12,
        FSK_16,
        FSK_18
    };
    
    private FSK fsk;
    
    private String imagePath;
    
    @Transient
    private ImageIcon image;     
        
    /**
     * Hibernate Constructor without arguments
     */
    protected VideoFile(){
        super();
        this.actors = new HashSet<Actor> ();
        
        this.setOriginalTitle("");
        this.setDescription("");
        this.setDirector("");
        this.setFsk(FSK.FSK_UNKNOWN);
        this.setImagePath("");
    }

    /**
     * @return the originalTitle
     */
    public String getOriginalTitle() {
        return originalTitle;
    }

    /**
     * @param originalTitle the originalTitle to set
     */
    public void setOriginalTitle(String originalTitle) {
        if(originalTitle == null){
            this.originalTitle = "";
        }
        else{
            this.originalTitle = originalTitle;
        }
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        if(description == null){
            this.description = "";
        }
        else if(description.length() > 19999){
            this.description = description.substring(0, 19999);
        }
        else{
            this.description = description;
        }
    }

    /**
     * @return the actors as an unmodifiable set
     */
    public Set<Actor> getActors() {
        return actors;
    }
    
    /**
     * returns an actor with the given name
     * @param name the name of the actor
     * @return the actor<br/> null if no actor was found
     */
    public Actor getActor(String name){
        Actor actor = null;
        
        for(Actor value: getActors()){
            if(value.getName().equals(name)){
                actor = value;
                break;
            }
        }
        return actor;
    }

    /**
     * @param actors the actors to set
     */
    public void setActors(Set<Actor> actors) {
        removeAllActors();
        if(actors != null){
            for(Actor actor: actors){                
                addActor(actor);
            }
        }
    }
    
    /**
    * @param actor the actor to add
    * 
    * @return true If the element was added to the set <br/>
    * false otherwise
    */
   public boolean addActor(Actor actor) {
        if(actor != null){
            if(this.getActor(actor.getName()) == null){
                actors.add(actor);
                Actor.putActorIntoSet(actor);    
                return true;
            }
        }
        return false;
    }

    /**
    * @param actor the actor to remove
    * 
    * @return   true if the actor was removed<br/> 
    *           false otherwise
    */
    public boolean removeActor(Actor actor) {
        if(actor != null){
            return actors.remove(actor);
        }
        return false;
    }
    
    /**
    * removes all actors form the list
    */
    public void removeAllActors() {
        actors.clear();
    }

    /**
     * @return the director
     */
    public String getDirector() {
        return director;
    }

    /**
     * @param director the director to set
     */
    public void setDirector(String director) {
        if(director == null){
            this.director = "";
        }
        else{
            this.director = director;
        }
    }

    /**
     * @return the fsk
     */
    public FSK getFsk() {
        return fsk;
    }

    /**
     * @param fsk the fsk to set
     */
    public void setFsk(FSK fsk) {
        if(fsk == null){
            this.fsk = FSK.FSK_UNKNOWN;
        }
        else{
            this.fsk = fsk;
        }
    }

    /**
     * @return the imagePath
     */
    public String getImagePath() {
        //TODO: temporary workaround
        //new in 1.4: URI changed from "http://d3gtl9l2a4fn1j.cloudfront.net/t/p/" to "http://image.tmdb.org/t/p/"
        if(imagePath.startsWith("http://d3gtl9l2a4fn1j.cloudfront.net/t/p/")){
            imagePath = imagePath.replace("http://d3gtl9l2a4fn1j.cloudfront.net/t/p/", "http://image.tmdb.org/t/p/"); 
            PersistingManager.INSTANCE.setDocumentChanged(true);
        }
        return imagePath;
    }

    /**
     * @param imagePath the imagePath to set
     */
    public void setImagePath(String imagePath) {
        if(imagePath == null){
            this.imagePath = "";
        }
        else{            
            this.imagePath = imagePath;
        }
    }
    
    /**
     * @return the image as an byte array
     */
    public ImageIcon getImage() {
        return image;
    }

    /**
     * @param image the image to set as an byte array. <br/> 
     * Can be null
     */
    public void setImage(ImageIcon image) {
        this.image = image;
    }       
}
