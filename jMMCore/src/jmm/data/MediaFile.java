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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.persistence.*;
import jmm.data.collection.MediaCollection;
import jmm.utils.Settings;

/**
 *
 * Beinhaltet alle Attribute, welche für Video und Audio Dateien relevant sind
 * Strings können nicht "null" gesetzt werden, sondern lediglich leer.
 * 
 * @author Bryan Beck
 * @since 02.04.2011
 */
@Entity 
//@Table( name = "MEDIAFILE" )
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class MediaFile implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)     //alternativ GenerationType.AUTO TABLE
    private Long uid;
    
    @ManyToOne
    @JoinColumn
    private MediaCollection collection;    //Gibt an, in welche Collection das MediaFile gehört
        
    @Column(nullable=false) //,unique=true) cant be unique cause of serie episodes or multiple titles in different collections
    private String title;
         
    @ElementCollection//(fetch= FetchType.EAGER)
    @CollectionTable(name="GenreKeys", joinColumns=@JoinColumn(name="mediafile_uid"))
    @Column(name="genreKey")
    private Set<String> genreKeys;    
    private int    playtime;
    private int    releaseYear;
    private String publisher;
    
    private double personalRating;
    //TODO: Remove for 2.0
    private String customAttributeName;
    private String customAttributeValue;
    
    @ElementCollection 
    @CollectionTable(name="CustomAttributes", joinColumns=@JoinColumn(name="mediafile_uid"))
    @Column() //name="attribute")
    private Map<String, String> customAttributes;
       
    /**
     * Hibernate Constructor without arguments
     */
    protected MediaFile(){
        this.genreKeys = new HashSet<> ();
        this.customAttributes = new HashMap<>();
        
        this.setTitle("");
        this.setPlaytime(0);
        this.setReleaseYear(0);
        this.setPublisher("");
        this.setPersonalRating(0);
        this.setCustomAttributeName("");
        this.setCustomAttributeValue("");
    }

    /**
    * @return the title
    */
    public String getTitle() {
        return title;
    }

    /**
    * @param title the title to set. If the string is null, it will be set as empty.
    */
    public void setTitle(String title) {
        if(title == null){
            this.title = "";
        }
        else{
            this.title = title;
        }
    }

    /**
    * @return the genres as an unmodifiable Set
    */
    public Set<String> getGenreKeys() {
        return Collections.unmodifiableSet(genreKeys);
    }

    /**
    * @param genres the genres to set
    */
    public void setGenreKeys(Set<String> genres) {
        removeAllGenreKeys();
        if(genres != null){
            this.genreKeys.addAll(genres);
        }
    }

    /**
    * @param genreKey the genre key to add
    */
    public void addGenreKey(String genreKey) {
        if(genreKey != null){
            
            //TODO: Workaround for version 1.01 --> 1.1 (because 2 genres were removed)
            if(genreKey.equalsIgnoreCase("Movie.genres.03")){
                genreKey = "Movie.genres.20";
            }
            else if(genreKey.equalsIgnoreCase("Movie.genres.04")){
                genreKey = "Movie.genres.05";
            }
            
            genreKeys.add(genreKey);
        }
    }

    /**
    * @param genreKey the genr key to remove
    */
    public void removeGenreKey(String genreKey) {
        if(genreKey != null){
            genreKeys.remove(genreKey);
        }
    }

    /**
    * removes all genres form the list
    */
    public void removeAllGenreKeys() {
        genreKeys.clear();
    }

    /**
    * @return the playtime
    */
    public int getPlaytime() {
        return playtime;
    }

    /**
    * @param playtime the playtime to set
    */
    public void setPlaytime(int playtime) {
        if(playtime < 0){
            this.playtime = 0;
        }
        else{
            this.playtime = playtime;
        }
    }

    /**
    * @return the releaseYear
    */
    public int getReleaseYear() {
        return releaseYear;
    }

    /**
    * @param releaseYear the releaseYear to set
    */
    public void setReleaseYear(int releaseYear) {
        if(releaseYear < 0){
            this.releaseYear = 0;
        }
        else{
            this.releaseYear = releaseYear;
        }
    }

    /**
    * @return the publisher
    */
    public String getPublisher() {
        return publisher;
    }

    /**
    * @param publisher the publisher to set. If the string is null, it will be set as empty.
    */
    public void setPublisher(String publisher) {
        if(publisher == null){
            this.publisher = "";
        }
        else{
            this.publisher = publisher;
        }
    }

    /**
     * @return the personalRating
     */
    public double getPersonalRating() {
        return personalRating;
    }

    /**
     * @param personalRating the personalRating to set
     */
    public void setPersonalRating(double personalRating) {
        if(personalRating < 0){
            this.personalRating = 0;
        }
        else if(personalRating > 10){
            this.personalRating = 10;
        }
        else{
            this.personalRating = personalRating;
        }
    }
    
    /**
     * @return the customAttributeName
     */
    public String getCustomAttributeName() {
        return customAttributeName;
    }

    /**
     * @param customAttributeName the customAttributeName to set
     */
    public void setCustomAttributeName(String customAttributeName) {
        if(customAttributeName == null){
            this.customAttributeName = "";
        }
        else{
            this.customAttributeName = customAttributeName;
        }
        convertCAToCAMap();
    }

    /**
     * @return the customAttributeValue
     */
    public String getCustomAttributeValue() {
        return customAttributeValue;
    }

    /**
     * @param customAttributeValue the customAttributeValue to set
     */
    public void setCustomAttributeValue(String customAttributeValue) {
        if(customAttributeValue == null){
            this.customAttributeValue = "";
        }
        else{
            this.customAttributeValue = customAttributeValue;
        }        
        convertCAToCAMap();
    }
    
    /**
     * Temporary method for converting CA name and value to the map. <br/>
     * This is neccessarry due to the custom attribute re-definition. <br/>
     * Can be removed in > 1.31
     */
    private void convertCAToCAMap(){
        if(!customAttributeName.isEmpty() && !customAttributeValue.isEmpty()){
            
            if(!customAttributes.containsKey(customAttributeName)){
                customAttributes.put(customAttributeName, customAttributeValue);
            }
        }
    }

    /**
     * Returns an <b>unmodifiable</b> map of customattribute keys and values.
     * @return the customAttributes
     */
    public Map<String, String> getCustomAttributes() {
        return Collections.unmodifiableMap(customAttributes);
    }

    /**
     * @param customAttributes the customAttributes to set
     */
    public void setCustomAttributes(Map<String, String> customAttributes) {
        this.customAttributes.clear();
        if(customAttributes != null){
            for(String key: customAttributes.keySet()){
                addCustomAttribute(key, customAttributes.get(key));
            }
        }
    }
    
    /**
    * removes all custom attributes from the map
    */
    public void clearCustomAttributes() {
        customAttributes.clear();
    }
    
    /**
     * Adds a custom attribute to the map
    * @param key The attribute name
    * @param value The attribute value
    * @return The old value, if exist
    */
    public String addCustomAttribute(String key, String value) {
        String oldValue = null;
        if((key != null) && (value != null)){
            oldValue = customAttributes.put(key, value);
            
            Settings.customAttributeKeys.add(key);
        }
        return oldValue;
    }

    /**
     * Removes a custom attribute from the map
    * @param key The attribute name
    * @return The value, if exist
    */
    public String removeCustomAttribute(String key) {
        String value = null;
        if(key != null){
            value = customAttributes.remove(key);
        }
        return value;
    }

    @MyXMLAnnotation(isTransient=true)
    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }
    
//    @Override
//    public boolean equals(Object value){
//        if(value instanceof MediaFile){
//            MediaFile mediaFile = (MediaFile)value;
//            if(this.getTitle().equals(mediaFile.getTitle()) &&
//              (this.getCollection().equals(mediaFile.getCollection()))){
//                return true;
//            }
//        }
////        else{
////            return super.equals(value);
////        }
//        return false;
//    }
    
    /**
     * @return the collection
     */
    @MyXMLAnnotation(isTransient=true)
    public MediaCollection getCollection() {
        return collection;
    }

    /**
     * @param collection the collection to set
     */
    public void setCollection(MediaCollection collection) {
        this.collection = collection;
    }
}
