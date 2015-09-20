/**
 * Copyright (c) 2010-2015 Bryan Beck.
 * All rights reserved.
 * 
 * This project is licensed under LGPL v2.1.
 * See jMovieManager-license.txt for details.
 * 
 */
package jmm.interfaces;

import jmm.data.collection.MediaCollection;

/**
 * Interface to implement the observer pattern <br/>
 * Informs all subjects if a collection has been <b>created</b>, <b>updated</b> or <b>deleted </b>
 * 
 * @author Bryan Beck
 * @since 24.08.2012
 */
public interface CollectionObserverInterface {
    
    /**
    * Informs all subjects if a collection has been updated
    * 
    * @param oldCollection The old collection
    * @param newCollection The updated collection
    */          
    public void update(MediaCollection oldCollection, MediaCollection newCollection ); 
       
    /**
    * Informs all subjects if a collection has been created
    * 
    * @param collection The created collection
    */
    public void addElement( MediaCollection  collection );
    
    /**
    * Informs all subjects if a collection has been deleted
    * 
    * @param collection The deleted collection
    */          
    public void removeElement( MediaCollection collection );
}
