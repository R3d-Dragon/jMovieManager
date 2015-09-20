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
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import jmm.data.Movie;

/**
 *
 * @author Bryan Beck
 * @since 20.07.2011
 */
@Entity 
//@Table( name = "MOVIECOLLECTION" )
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class MovieCollection extends MediaCollection<Movie> implements Serializable{
  
    /**
     * Creates a new movie collection
     * 
     * @param name the name of the collection
     * @param tabNr the number of the tab
     */
    public MovieCollection(String name, int tabNr){
        super(name, tabNr);
    }   
    /**
     * Creates a new movie collection
     */
    protected MovieCollection(){
        super();
    }
        
    /**
     * Methode welche alle Filme in der Collection anhand des Imdb Ratings sortiert
     *
     *  The sorting algorithm is a modified mergesort (in which the merge is omitted if the highest element in the low sublist is less than the lowest element in the high sublist).
     *  This algorithm offers guaranteed n log(n) performance.
     */
    public void orderByOnlineRating(){
        Collections.sort(mediaCollection, new Comparator<Movie>() {
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
            public int compare(Movie o1, Movie o2) {
                return ((int)(o1.getOnlineRating()*100) - (int)(o2.getOnlineRating()*100));
            }
        });
        this.setChanged();
        notifyObservers();
    }
}
