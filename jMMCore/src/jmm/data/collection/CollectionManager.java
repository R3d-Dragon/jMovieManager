/**
 * Copyright (c) 2010-2015 Bryan Beck.
 * All rights reserved.
 * 
 * This project is licensed under LGPL v2.1.
 * See jMovieManager-license.txt for details.
 * 
 */
package jmm.data.collection;

import java.util.Iterator;
import jmm.persist.DaoInterface;
import jmm.persist.DaoManager;
import jmm.persist.PersistingManager;
import jmm.utils.Settings;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;

/**
 * Manager class for CRUD operation on collections
 * 
 * @author Bryan Beck
 * @since 29.03.2013
 */
public enum CollectionManager {
    /**
     * Singleton instance of the DataManager
     */ 
    INSTANCE;

    /** Logger. */
    private static final Logger LOG = LoggerFactory.logger(CollectionManager.class);
    
    /**
     * Enum for collection ordering
     */
    public static enum Order {
        ORDER_BY_NAME,
        ORDER_BY_PLAYTIME,
        ORDER_BY_YEAR,
        ORDER_BY_ONLINE_RATING,
        ORDER_BY_PERS_RATING
    };
    
    /**
     * Removes all collections and values <br/> 
     * Creates two new default collections
     * @param serieCollName - the name of the serie collection
     * @param movieCollName - the name of the movie collection
     */
    public void createNewCollections(String serieCollName, String movieCollName) {
        //close open connections
        PersistingManager.INSTANCE.destroy();
        MediaCollection.clearCollectionMap();
        //create two default collections
        MovieCollection movieCollection;
        SerieCollection serieCollection;
        
        movieCollection = new MovieCollection(movieCollName, 0);
        serieCollection = new SerieCollection(serieCollName, 0); 

        MediaCollection.putCollectionIntoCollectionMap(serieCollection);
        MediaCollection.putCollectionIntoCollectionMap(movieCollection);

        Settings.getInstance().setHsqlDbPath("");
        PersistingManager.INSTANCE.setDocumentChanged(true);
    }
    
    /**
     * Creates a new collection and adds them to the collectionMap
     *
     * @param name the name of the collection
     * @param isMovie true if the collection is a movie collection<br/> false
     * otherwise
     * @param tabNr the tab number of the collection
     * @return the created collection
     */
    public MediaCollection createCollection(String name, boolean isMovie, int tabNr) {
        MediaCollection collection;
        if (isMovie) {
            collection = new MovieCollection(name, tabNr);
        } else {
            collection = new SerieCollection(name, tabNr);
        }
        MediaCollection.putCollectionIntoCollectionMap(collection);
        PersistingManager.INSTANCE.setDocumentChanged(true);
        return collection;
    }
    
        /**
     * Updates a collection inside the collectionMap
     *
     * @param oldName the old name of the collection
     * @param newName the name of the collection
     * @param newTabNr the tab number of the collection
     */
    public void updateCollection(String oldName, String newName, int newTabNr) {
        MediaCollection.updateCollectionFromCollectionMap(oldName, newName, newTabNr);
        PersistingManager.INSTANCE.setDocumentChanged(true);
    }

    /**
     * Removes a collection from the collectionMap
     *
     * @param collection the collection to delete
     */
    public void removeCollection(MediaCollection collection) {
        MediaCollection.removeCollectionFromCollectionMap(collection);

        if (PersistingManager.INSTANCE.isDatabaseOnline()) { 
            DaoInterface<MediaCollection> daoMediaCollection = DaoManager.getDao(MediaCollection.class);
            daoMediaCollection.delete(collection);
        }
        PersistingManager.INSTANCE.setDocumentChanged(true);
    }

        /**
     * Überprüft ob eine Collection Instanz in der Sammlung angelegt wurde
     *
     * @return true Instanz existiert<br/>false Instanz existiert nicht
     *
     */
    public boolean isACollectionCreated() {
        if (MediaCollection.getCollectionMap().isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * Überprüft ob mind eine Collection innerhalb der Map über Einträge verfügt
     *
     * @return true mind. 1 Eintrag existiert <br/> false otherwise
     *
     */
    public boolean areCollectionsEmpty() {
        boolean empty;
        Iterator<String> keyIterator = MediaCollection.getCollectionMap().keySet().iterator();
        empty = keyIterator.hasNext();
        while (keyIterator.hasNext()) {
            String key = keyIterator.next();
            if (!MediaCollection.getCollectionMap().get(key).isEmpty()) {
                empty = false;
            }
        }
        return empty;
    }
    
    /**
     * Sorts a given collection by a specific order
     *
     * @param order the sort order
     * @param collection the collection to sort
     */
    public void orderCollectionBy(Order order, MediaCollection collection) {
        collection.setOrdering(order);
        if (order == Order.ORDER_BY_NAME) {
            collection.orderByName();
        } else if (order == Order.ORDER_BY_PLAYTIME) {
            collection.orderByPlaytime();
        } else if (order == Order.ORDER_BY_YEAR) {
            collection.orderByReleaseYear();
        } else if (order == Order.ORDER_BY_ONLINE_RATING) {
            //TODO: If MusicCollection is implemented, refactoring GUI
            if (collection instanceof MovieCollection) {
                ((MovieCollection) collection).orderByOnlineRating();
            } else if (collection instanceof SerieCollection) {
                ((SerieCollection) collection).orderByOnlineRating();
            }
        } else if (order == Order.ORDER_BY_PERS_RATING) {
            collection.orderByPersonalRating();
        }
    }
}
