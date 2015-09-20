/**
 * Copyright (c) 2010-2015 Bryan Beck.
 * All rights reserved.
 * 
 * This project is licensed under LGPL v2.1.
 * See jMovieManager-license.txt for details.
 * 
 */
package jmm.persist;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import jmm.data.Actor;
import jmm.data.Episode;
import jmm.data.Movie;
import jmm.data.Season;
import jmm.data.Serie;
import jmm.data.collection.CollectionManager;
import jmm.data.collection.MediaCollection;
import jmm.data.collection.MovieCollection;
import jmm.data.collection.SerieCollection;
import jmm.interfaces.ExtensionInterface;
import jmm.utils.FileManager;
import jmm.utils.PictureManager;
import jmm.utils.Settings;
import org.hibernate.Hibernate;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.hsqldb.server.Server;
import org.jboss.logging.Logger;

/**
 * Manager class for save, open, etc. operations
 * 
 * @author Bryan Beck
 * @since 29.03.2013
 */
public enum PersistingManager {
    /**
     * Singleton instance
     */
    INSTANCE;
  
    /** Logger. */
    private static final Logger LOG = LoggerFactory.logger(PersistingManager.class);
    
    /**
     * Reference to the HSQLDB database server
     */
    private static final Server storageServer; 
         
    static{
        storageServer = new Server(); 
        //TODO: put into log file
        storageServer.setLogWriter(new PrintWriter(System.out)); // can use custom writer
        storageServer.setErrWriter(new PrintWriter(System.err)); // can use custom writer
        storageServer.setSilent(true);
        storageServer.setAddress("localhost"); //muss localhost, da 127.0.0.1 nicht unter MAC OSX funktioniert
        storageServer.setDatabaseName(0, "jmm");
        storageServer.setNoSystemExit(true);
    }
    
    /**
     * Determines, if the document has changed since the last save or not
     */
    private boolean documentChanged = false;

//    /**
//     * Deletes all database values
//     */
//    private void dropAllDatabaseEntries() {
//        DaoImpl<MediaCollection> daoMediaColloection = (DaoImpl<MediaCollection>) DaoManager.getDao(MediaCollection.class);
//        DaoImpl<Movie> daoMovie = (DaoImpl<Movie>) DaoManager.getDao(Movie.class);
//        DaoImpl<Episode> daoEpisode = (DaoImpl<Episode>) DaoManager.getDao(Episode.class);
//        DaoImpl<Season> daoSeason = (DaoImpl<Season>) DaoManager.getDao(Season.class);
//        DaoImpl<Serie> daoSerie = (DaoImpl<Serie>) DaoManager.getDao(Serie.class);
//
//        daoEpisode.deleteAll();
//        daoSeason.deleteAll();
//        daoSerie.deleteAll();
//        daoMovie.deleteAll();
//        daoMediaColloection.deleteAll();
//    }



    /**
     * Methode, welche zurückgibt, ob sich das Projekt während dem letzten
     * Speichern verändert hat
     *
     * @return true - Dokument hat sich verändert<br/>
     * false - Dokument ist unverändert
     *
     */
    public boolean isDocumentChanged() {
        return documentChanged;
    }

    /**
     * Methode zum verändern des Speicherstatus, dient zur Überprüfung, ob neu
     * gespeichert werden muss
     *
     * @param documentChanged - Ob sich das Dokument geändert hat
     */
    public void setDocumentChanged(boolean documentChanged) {
        this.documentChanged = documentChanged;
    }

    /**
     * Defines, if the changeDBPath method is executed within a save or an open process
     */
    public enum Process {
        SAVE,
        OPEN
    }

    /**
     * Changes the path to the database
     *
     * @param newHsqlDbPath the new path to the database
     * @param process - the process
     * @return true database path has changed <br/> false otherwise
     */
    public boolean changeDBPath(String newHsqlDbPath, Process process) {
        String oldHsqlDbPath = Settings.getInstance().getHsqlDbPath();
        
        if (this.isDatabaseOnline()) {
//            if(oldHsqlDbPath.equals(newHsqlDbPath) && (process == Process.SAVE)){
//                //Server online and old path is the same as the new one
//                return false;
//            }
            //close open connections
            this.destroy();
        }else{
            //i.E. after createNewCollection(), delete old Files first
            if(oldHsqlDbPath.isEmpty() && process == Process.SAVE){
                //delete old files                
                for (String ending : ExtensionInterface.hsqlDB_Extensions) {
                    File oldFile = new File(newHsqlDbPath + ending);
                    if (oldFile.exists() && oldFile.canRead()) {
                        try {
                            if(!oldFile.delete()){
                                LOG.error("Cannot delete file: " + oldFile.getAbsolutePath());
                            }
                        } catch (SecurityException ex) {
                            LOG.error("Cannot delete file because of a security reason.", ex);
                        }
                    }
                }
            }
        }

        //Copy Database files to destination  
        if (!oldHsqlDbPath.isEmpty() && (!oldHsqlDbPath.equals(newHsqlDbPath)) && (process == Process.SAVE)) {
            for (String ending : ExtensionInterface.hsqlDB_Extensions) {
                if(!ending.equalsIgnoreCase(".lck")){
                    File oldFile = new File(oldHsqlDbPath + ending);
                    File newFile = new File(newHsqlDbPath + ending);
                    if (oldFile.exists() && oldFile.canRead()) {
                        try {
                            //Override existing file in new path
                            FileManager.copy(oldFile, newFile, true);
                        } catch (IOException ex) {
                            LOG.error("Error while copying old database to the new filepath.", ex);
                        }
                    }
                }
            }
        }
//        jdbc:hsqldb:file:data/localBD
        storageServer.setDatabasePath(0, "file:" + newHsqlDbPath +";shutdown=true");
        storageServer.start();
        
//        if((serverState == 1 ) || (serverState == 4)){  //if hsqldb server was online or opened before
        DbHibernate.openDatabase();
        
//        }  
        //After everything is working, set the new database path as default
        Settings.getInstance().setHsqlDbPath(newHsqlDbPath);
        return true;
    }

    /**
     * Returns a a list of tasks which must be executed in order to save
     *
     * @return A list of save processes (tasks)
     */
    public List<RunnableImpl> save() {
        final List<RunnableImpl> runnableList = new LinkedList<>();
        final List<MediaCollection> mediaCollList = new LinkedList<>();
        int totalElements = 0;
        Iterator<String> collIterator = MediaCollection.getCollectionMap().keySet().iterator();
        while (collIterator.hasNext()) {
            String key = collIterator.next();
            MediaCollection collection = MediaCollection.getCollectionMap().get(key);
            if (collection instanceof MovieCollection) {
                totalElements += collection.size();                     //Anzahl der Filme
            } else if (collection instanceof SerieCollection) {
                totalElements += collection.size();                     //Anzahl der Serien
                for (Object serieObj : collection.getAll()) {
                    Serie serie = (Serie) serieObj;
                    totalElements += serie.getSeasons().size();         //Anzahl der Staffeln
                    for (Season season : serie.getSeasons()) {
                        totalElements += season.getEpisodes().size();   //Anzahl der Episoden
                    }
                }
            }
            mediaCollList.add(collection);
        }

        RunnableImpl saveDatabase = new RunnableImpl(totalElements) {
            @Override
            public void run() {
                DaoImpl<MediaCollection> daoMediaCollection = (DaoImpl<MediaCollection>) DaoManager.getDao(MediaCollection.class);
                DaoImpl<Movie> daoMovie = (DaoImpl<Movie>) DaoManager.getDao(Movie.class);
                DaoImpl<Serie> daoSerie = (DaoImpl<Serie>) DaoManager.getDao(Serie.class);
                DaoImpl<Season> daoSeason = (DaoImpl<Season>) DaoManager.getDao(Season.class);
                DaoImpl<Episode> daoEpisode = (DaoImpl<Episode>) DaoManager.getDao(Episode.class);
                DaoImpl<Actor> daoActors = (DaoImpl<Actor>)DaoManager.getDao(Actor.class); 

                for (MediaCollection collection : mediaCollList) {
//                    if(Thread.currentThread().isInterrupted()){
//                        //TODO: Überprüfen
//                        daoMediaCollection.rollback();
////                        daoMediaCollection.commit();
//                        return;
//                    }
                    //saves files within the collection
                    if (collection instanceof MovieCollection) {
                        for (Object movieObj : collection.getAll()) {
                            Movie movie = (Movie) movieObj;
                            //Benachrichtige SaveProgressGUI 
                            this.setChanged();
                            this.notifyObservers(movie);
                            daoMovie.save(movie);
                            daoActors.saveAll(movie.getActors());
                        }
                    } else if (collection instanceof SerieCollection) {
                        for (Object serieObj : collection.getAll()) {
                            Serie serie = (Serie) serieObj;
                            //For alle seasons
                            for (Season season : serie.getSeasons()) {
                                //Benachrichtige SaveProgressGUI 
                                this.setChanged();
                                this.notifyObservers(season);
                                for (Object episodeObj : season.getEpisodes()) {
                                    Episode episode = (Episode) episodeObj;
                                    //Benachrichtige SaveProgressGUI 
                                    this.setChanged();
                                    this.notifyObservers(episode);
                                    daoEpisode.save(episode);
                                    daoActors.saveAll(episode.getActors());
                                }
                                daoSeason.save(season);
                            }
                            daoSerie.save(serie);
                            daoActors.saveAll(serie.getActors());
                        }
                    }
                    //                else if(collIterator instanceof MusicCollection){
                    //                    
                    //                }
                    daoMediaCollection.save(collection);
                }
//                for(Actor actor: Actor.getActorsSet()){
//                    daoActors.save(actor);
//                }
                daoActors.commit();     
                daoEpisode.commit();
                daoMovie.commit();
                daoSeason.commit();
                daoSerie.commit();
                
                
                daoMediaCollection.commit();
                daoMediaCollection.getDbAccess().getActiveSession().flush();
                //            daoMediaCollection.closeSession();       
                setDocumentChanged(false);
            }
        };
        runnableList.add(saveDatabase);

        if (Settings.getInstance().isSavePictures()) {
            RunnableImpl savePictures = new RunnableImpl(totalElements) {
                @Override
                public void run() {
                        for (MediaCollection collection : mediaCollList) {
                            //saves files within the collection
                            if (collection instanceof MovieCollection) {
                                for (Object movieObj : collection.getAll()) {
                                    Movie movie = (Movie) movieObj;
                                    //Benachrichtige SaveProgressGUI 
                                    this.setChanged();
                                    this.notifyObservers(movie);
                                    try {
                                        PictureManager.saveImage(movie);
                                    } catch (IOException ex) {
                                        LOG.error("Cannot save picture: " + movie.getImagePath(), ex);
                                    }
                                    //Abbruchfunktion für Bilder speichern
                                    if (Thread.currentThread().isInterrupted()) {
                                        return;
                                    }
                                }
                            } else if (collection instanceof SerieCollection) {
                                for (Object serieObj : collection.getAll()) {
                                    Serie serie = (Serie) serieObj;
                                    //Benachrichtige SaveProgressGUI 
                                    this.setChanged();
                                    this.notifyObservers(serie);
                                    try {
                                        PictureManager.saveImage(serie);
                                    } catch (IOException ex) {
                                        LOG.error("Cannot save picture: " + serie.getImagePath(), ex);
                                    }
                                    //Abbruchfunktion für Bilder speichern
                                    if (Thread.currentThread().isInterrupted()) {
                                        return;
                                    }

                                    //For alle seasons
                                    for (Season season : serie.getSeasons()) {
                                        for (Object episodeObj : season.getEpisodes()) {
                                            Episode episode = (Episode) episodeObj;
                                            //Benachrichtige SaveProgressGUI 
                                            this.setChanged();
                                            this.notifyObservers(episode);
                                            try {
                                                PictureManager.saveImage(episode);
                                            } catch (IOException ex) {
                                                LOG.error("Cannot save picture: " + episode.getImagePath(), ex);
                                            }
                                            //Abbruchfunktion für Bilder speichern
                                            if (Thread.currentThread().isInterrupted()) {
                                                return;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                }
            };
            runnableList.add(savePictures);
        }
        return runnableList;
    }
      
    /**
     * Loads the database behind the given string
     *
     * @param hsqlDBPath The path to the Database files
     * @return true if the database was successfully loaded <br/> false
     * otherwise
     */
    public boolean open(String hsqlDBPath) {
        //check if the file exist  
        File openFile = new File(hsqlDBPath);
        if (!openFile.exists() || !openFile.isFile()) {
            openFile = new File(hsqlDBPath + ".script");
            if (!openFile.exists() || !openFile.isFile()) {
                return false;
            }
        }

        MediaCollection.clearCollectionMap();
        changeDBPath(hsqlDBPath, Process.OPEN);
        DaoInterface<MediaCollection> daoMediaCollection = (DaoInterface<MediaCollection>) DaoManager.getDao(MediaCollection.class);
        
        //If an exception occured during the import (due to different jmm versions), it is at this line
        List<MediaCollection> mediaCollectionList = daoMediaCollection.find("FROM MediaCollection ORDER BY tabNumber ASC");
//        throw new NullPointerException();

        for (int i = 0; i < mediaCollectionList.size(); i++) {
            MediaCollection collection = mediaCollectionList.get(i);
            if (!Hibernate.isInitialized(collection)) {
                Hibernate.initialize(collection);
            }
            MediaCollection.putCollectionIntoCollectionMap(collection);
            CollectionManager.INSTANCE.orderCollectionBy(collection.getOrdering(), collection);
        }        
        setDocumentChanged(false);
        return true;
    }

    /**
     * Determines if the database is connected and online or not
     * @return true if the database is online and connected <br/> false otherwise
     */
    public boolean isDatabaseOnline(){
        int serverState = storageServer.getState();
        if ((serverState == 1) || (serverState == 4)) {   //ONLINE or OPENING
            return true;
        }
        return false;
    }

    /**
     * Returns the current server state. <br/>
     * 1 - ONLINE <br/>
     * 4 - OPENING <br/>
     * 8 - CLOSING <br/>
     * 16 - SHUTDOWN <br/>
     * @return 
     */
    public int getServerState(){
        return storageServer.getState();
    }
    
    /**
     * Needs to be executed before shutdown the software<br/> Close all DAO
     * objects and the session <br/> Shutdown the HSQL storage server
     *
     */
    public void destroy() {
        if (isDatabaseOnline()) { 
            storageServer.shutdown();
            storageServer.signalCloseAllServerConnections();
            DaoManager.closeSession();
            DbHibernate.closeDatabase();
            DaoManager.clearDaos();
        }
    }
}
