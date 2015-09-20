/**
 * Copyright (c) 2010-2015 Bryan Beck.
 * All rights reserved.
 * 
 * This project is licensed under LGPL v2.1.
 * See jMovieManager-license.txt for details.
 * 
 */
package jmm.data.collection;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import javax.persistence.*;
import jmm.data.MediaFile;
import jmm.data.collection.CollectionManager.Order;
import jmm.interfaces.CollectionObserverInterface;
import jmm.xml.MyXMLAnnotation;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;

/**
 *
 * 
 * @author Bryan Beck
 * @param <T>
 * @date 02.12.2010
 */
@Entity 
//@Table( name = "MEDIACOLLECTION" )
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class MediaCollection<T extends MediaFile> extends Observable implements Serializable, Cloneable{
    /** Logger. */
    private static final Logger LOG = LoggerFactory.logger(MediaCollection.class);
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)     //alternativ GenerationType.AUTO Table
    private Long uid;

    @Column(nullable=false) //,unique=true)
    private String name;
    
    @Column(nullable=false) //, unique=true)
    private int tabNumber;
                                                                   
    //orphanRemoval == Bei einem Löschvorgang der Mediacollection, lösche alle Elemente der collection 
    @OneToMany(targetEntity=MediaFile.class, mappedBy="collection", fetch= FetchType.EAGER, orphanRemoval=true)
    protected List<T> mediaCollection;
    
    @Transient
    private boolean searchCollection;
    
    private Order ordering;
    
    /**
     * Observer list for all listeners, who wants to get informed if a collection was added, removed or updated from the map
     */
    @Transient
    private static List<CollectionObserverInterface> collectionObservers;
    
    /**
     * Map which contains all collections
     */
    @Transient
    private static Map<String, MediaCollection> collectionMap;
    
    static{
        collectionMap = new HashMap<String, MediaCollection>();
        collectionObservers = new LinkedList<CollectionObserverInterface>();
    }

    /**
    * Creates a new media collection
    * 
    * @param name the name of the collection
    * @param tabNr the number of the tab
    */
    protected MediaCollection(String name, int tabNr){
        this();
        this.setName(name);
        this.setTabNumber(tabNr);
    }
    
    /**
    * Creates a new media collection
    *
    */
    protected MediaCollection(){
        super();
        mediaCollection = new LinkedList<T>();
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
     * @return the tabNumber
     */
    public int getTabNumber() {
        return tabNumber;
    }

    /**
     * @param tabNumber the tabNumber to set
     * @throws RuntimeException If the tabNumber is < 0
     */
    public void setTabNumber(int tabNumber) throws RuntimeException{
        if(tabNumber < 0){
            throw new RuntimeException("Tab index cannot be < 0");
        }
        this.tabNumber = tabNumber;
    }

    /**
     * @return the searchCollection
     */
    @MyXMLAnnotation(isTransient=true)
    public boolean isSearchCollection() {
        return searchCollection;
    }

    /**
     * @param searchCollection the searchCollection to set
     */
    public void setSearchCollection(boolean searchCollection) {
        this.searchCollection = searchCollection;
    }

    /**
     * @return the ordering
     */
    public Order getOrdering() {
        return ordering;
    }

    /**
     * @param ordering the ordering to set
     */
    public void setOrdering(Order ordering) {
        this.ordering = ordering;
    }
    
    /****************************************************************
     * Collection Observer (To add, update or remove GUI Tabs)      *
     ****************************************************************/
    
    /**
    * Adds an observer to notify
    * @param o the observer to notify
    * @return true if the observer was successfull added<br/> false otherwise
    */
    public static boolean addCollectionObserver( CollectionObserverInterface o ) {
        return collectionObservers.add( o );
    }
    
    /**
    * Removes an observer to notify
    * @param o the observer to remove
    * @return true if the observer was successfull removed<br/> false otherwise
    */  
    public static boolean removeCollectionObserver( CollectionObserverInterface o ) {
        return collectionObservers.remove( o );
    }     
    
    /****************************************************************
     * Default methods                                              *
     ****************************************************************/    
    
    /**
     * Sets a list of values into the collection
     * @param values the values to set
     */
    public void set(List<T> values){
        this.removeAll();
        if(values != null){
            for(T value: values){
                this.add(value);
            }
        }
    }
    
    /**
     * Overrides a value with the same title and sets the new one into the collection
     * @param value the value to set
     * @return the old value
     */    
    public T set(T value){ //, int index){
        T oldValue = null;
        if((value != null)){
            oldValue = this.get(value.getTitle());
            this.remove(oldValue);
            this.add(value);
        }
        return oldValue;
    }
    
    /**
     * Adds a new element to the collection if the name doesn't already exist
     *
    * @param value the element to add
    * @return true If the element was added to the collection <br/>
    * false otherwise
    */
    public boolean add(T value){
        if(!this.contains(value.getTitle())){
            mediaCollection.add(value);
            if(!isSearchCollection()){
                value.setCollection(this);
            }
            this.setChanged();
            notifyObservers(value);
            return true;
        }
        return false;
    }
    
    /**
    * Adds an list with values to the collection
     *
    * @param values The list with the values
    */
    public void addAll(List<T> values){
        if((values != null) && (!values.isEmpty())){
            for(T value: values){
                this.add(value);
            }
        }
    }

     /**
    * Removes an element from the collection
    * @param value the object to remoeve
    * 
    * @return   true if the value was removed<br/> 
    *           false otherwise
    */
    public boolean remove(T value){
        if(value != null){
            mediaCollection.remove(value);
            if(!isSearchCollection()){
                value.setCollection(null);
            }
            this.setChanged();
            this.notifyObservers(value);
            return true;
        }
        return false;
    }

   /**
    * Methode, welche eine MediaFile aus der Sammlung entfernt
    * @param name Der Name des MediaFiles
    *
    * @return T Das gefundene Objekt <br/>
    * null, Wenn kein Objekt unter dem Name gefunden wurde
     */
    public T remove(String name){
        T objectToRemove = this.get(name);
        this.remove(objectToRemove);
        return objectToRemove;
    }

    /**
    * Removes all elements from the collection
    *
    */
    public void removeAll(){
        for (int i= mediaCollection.size()-1; i >= 0; i--){
//        for(T value: mediaCollection){
            this.remove(mediaCollection.get(i));
        }
//        notifyObservers();
    }
       
    /**
     * Überprüft, ob ein Objekt mit dem Namen bereits in der Collection vorhanden ist
     *
     * @param name Der Name des Objekts
     *
     * @return true, wenn der Name bereits vorhanden ist
     */
    public boolean contains(String name){
        if(this.get(name) != null){
            return true;
        }
        return false;
    }
    
    /**
     * Returns true if this list contains no elements.
     * 
     * @return true if this list contains no elements
     */
    public boolean isEmpty(){
        return mediaCollection.isEmpty();
    }
    
    /**
     * @see java.util.List#size() 
     * 
     * @return the number of elements in this list
     */
    public int size(){
        return mediaCollection.size();
    }
    
    /**
     * @see java.util.List#get(int) 
     * @param index the index
     * @return the element at the specified position in this list
     */
    public T get(int index){
        return mediaCollection.get(index);
    }

    /**
     * Methode welche eine Objekt anhand des Titels sucht und zurückgibt
     *
     * @param title Der Titel des MediaFiles
     *
     * @return mediaFile Die gefundene mediaFile <br/> null, wenn nichts gefunden wurde
     */
    public T get(String title){
        T mediaFile;

        int size = mediaCollection.size();
        for (int i= 0; i < size; i++){
            mediaFile  = mediaCollection.get(i);
            if(mediaFile.getTitle().equals(title)){
                return mediaFile;
            }
        }
        return null;
    }

     /**
     * Gibt eine readOnly Liste mit allen MediaFiles zurück
     *
     * @return List<T> Liste mit allen Elementen
     */
    public List<T> getAll(){
        return Collections.unmodifiableList(mediaCollection);
    }

    /**
    * Methode welche alle Elemente der Collection anhand des Titels sortiert
    *
    *  The sorting algorithm is a modified mergesort (in which the merge is omitted if the highest element in the low sublist is less than the lowest element in the high sublist).
    *  This algorithm offers guaranteed n log(n) performance.
    *
    */
    public void orderByName(){
        Collections.sort(mediaCollection, new Comparator<T>() {
            /**
             * Methode welche alle Filme IN DER DATENSTRUKTUR anhand der Laufzeit sortiert
             * @param o1 - the first object to be compared.
             * @param o2 - the second object to be compared.
             *
             * @return a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater than the second.
             *
             * @throws  ClassCastException - if the arguments' types prevent them from being compared by this Comparator.
             */
            @Override
            public int compare(T o1, T o2) {
                return (o1.getTitle().compareToIgnoreCase(o2.getTitle()));
            }
        });
        this.setChanged();
        notifyObservers();
    }

    /**
    * Methode welche alle Elemente der Collection anhand der Laufzeit sortiert
    *
    *  The sorting algorithm is a modified mergesort (in which the merge is omitted if the highest element in the low sublist is less than the lowest element in the high sublist).
    *  This algorithm offers guaranteed n log(n) performance.
    *
    */
    public void orderByPlaytime(){
        Collections.sort(mediaCollection, new Comparator<T>() {
            /**
             * Methode welche alle Filme IN DER DATENSTRUKTUR anhand der Laufzeit sortiert
             * @param o1 - the first object to be compared.
             * @param o2 - the second object to be compared.
             *
             * @return a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater than the second.
             *
             * @throws  ClassCastException - if the arguments' types prevent them from being compared by this Comparator.
             */
            @Override
            public int compare(T o1, T o2) {
                return (o1.getPlaytime() - o2.getPlaytime());
            }
        });
        this.setChanged();
        notifyObservers();
    }

    /**
    * Methode welche alle Elemente der Collection anhand des Erscheinugnsjahrs sortiert
    *
    *  The sorting algorithm is a modified mergesort (in which the merge is omitted if the highest element in the low sublist is less than the lowest element in the high sublist).
    *  This algorithm offers guaranteed n log(n) performance.
    *
    */
    public void orderByReleaseYear(){
        Collections.sort(mediaCollection, new Comparator<T>() {
            /**
             * Methode welche alle Filme IN DER DATENSTRUKTUR anhand der Laufzeit sortiert
             * @param o1 - the first object to be compared.
             * @param o2 - the second object to be compared.
             *
             * @return a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater than the second.
             *
             * @throws  ClassCastException - if the arguments' types prevent them from being compared by this Comparator.
             */
            @Override
            public int compare(T o1, T o2) {
                return (o1.getReleaseYear() - o2.getReleaseYear());
            }
        });
        this.setChanged();
        notifyObservers();
    }
    

    /**
     * Methode welche alle Filme in der Collection anhand des Imdb Ratings sortiert
     *
     *  The sorting algorithm is a modified mergesort (in which the merge is omitted if the highest element in the low sublist is less than the lowest element in the high sublist).
     *  This algorithm offers guaranteed n log(n) performance.
     */
    public void orderByPersonalRating(){
        Collections.sort(mediaCollection, new Comparator<T>() {
            /**
             * Methode welche alle Filme IN DER DATENSTRUKTUR anhand der Laufzeit sortiert
             * @param o1 - the first object to be compared.
             * @param o2 - the second object to be compared.
             *
             * @return a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater than the second.
             *
             * @throws  ClassCastException - if the arguments' types prevent them from being compared by this Comparator.
             */
            @Override
            public int compare(T o1, T o2) {
                return ((int)(o1.getPersonalRating()*100) - (int)(o2.getPersonalRating()*100));
            }
        });
        this.setChanged();   
        notifyObservers();
    }    

    /**
     * Returns the primary key for the collection
     * 
     * @return the uid
     */
    public Long getUid() {
        return uid;
    }
    
    /**
     * Returns the name of the collection
     * @return the name of the collection
     */
    @Override
    public String toString(){
        return getName();
    }
    
    /**
     * Returns an unmodifiable version of the collectionMap <br/>
     * 
     * @return the collectionMap
     */
    public static Map<String, MediaCollection> getCollectionMap() {
        return Collections.unmodifiableMap(collectionMap);
    }
    
    /**
     * Puts a new collection into the collection map and notifies all observer
     * 
     * @see java.util.Map#put(java.lang.Object, java.lang.Object) 
     */
    public static MediaCollection putCollectionIntoCollectionMap(MediaCollection collection){
        MediaCollection oldValue = collectionMap.put(collection.getName(), collection);
        //no for each loop cause of multiThreadding on import process
        for(int i= 0; i < collectionObservers.size(); i++){
//        for(CollectionObserverInterface o: collectionObservers){
            collectionObservers.get(i).addElement(collection);
//            o.addElement( collection );             
        }      
        return oldValue;
    }
   
    /**
     * Removes a collection from the collection map and notifies all observer
     * 
     * @see java.util.Map#remove(java.lang.Object) 
     */
    public static MediaCollection removeCollectionFromCollectionMap(MediaCollection collection){
        MediaCollection oldValue = collectionMap.remove(collection.getName());
        for(CollectionObserverInterface o: collectionObservers){
            o.removeElement( collection );             
        }      
        return oldValue;
    }    
    
    /**
     * Updates a collection from the collection map and notifies all observer
     * 
     * @see java.util.Map#remove(java.lang.Object) 
     * @return true if the collection was found and successfully updated <br/>false otherwise
     */
    public static boolean updateCollectionFromCollectionMap(String oldName, String newName, int newTabNumber){
        boolean success = false;
        MediaCollection collection = collectionMap.remove(oldName);
        if(collection != null){
            MediaCollection oldCollection;
            try {
                oldCollection = (MediaCollection)collection.clone();
                //set new values
                collection.setName(newName);
                collection.setTabNumber(newTabNumber);
                collectionMap.put(newName, collection);
                success = true;
                for(CollectionObserverInterface o: collectionObservers){
                    o.update(oldCollection, collection );             
                }                         
            } catch (CloneNotSupportedException ex) {
                LOG.error("Collection cannot be cloned.", ex);
            }  
        }
        return success;
    }   
      
    /**
     * Removes all elements from each collection and <br/>
     * removes all collections from the collection map <br/>
     * Notifies all observers 
     * 
     * @see java.util.Map#clear() 
     */
    public static void clearCollectionMap(){
        if(!collectionMap.isEmpty()){
            Iterator<String> keyIterator = collectionMap.keySet().iterator();
            while(keyIterator.hasNext()){
                String key = keyIterator.next();
                MediaCollection collection = collectionMap.get(key);
//                collection.removeAll();
                keyIterator.remove();
                removeCollectionFromCollectionMap(collection);
            } 
        }
    }   
}
