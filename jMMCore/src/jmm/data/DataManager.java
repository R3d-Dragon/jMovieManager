/**
 * Copyright (c) 2010-2015 Bryan Beck.
 * All rights reserved.
 * 
 * This project is licensed under LGPL v2.1.
 * See jMovieManager-license.txt for details.
 * 
 */
package jmm.data;

import java.util.List;
import jmm.data.collection.CollectionManager;
import jmm.data.collection.MediaCollection;
import jmm.data.collection.MovieCollection;
import jmm.data.collection.SerieCollection;
import jmm.persist.PersistingManager;
import jmm.utils.PictureManager;

/**
 * Manager class for CRUD operations on persisted classes (i.e. movie, episode, serie, ...)
 *
 * @author Bryan Beck
 * @since 20.10.2010
 */
public enum DataManager {
    /**
     * Singleton instance of the DataManager
     */
    INSTANCE;
    
    /**
     * Methode wird für den Import verwendet und ist synchronized, damit nicht
     * zeitgleich Objekte mit dem gleichen Namen angelegt werden können. Fügt
     * ein Film zur Datenbasis und der GUI hinzu. Existiert bereits ein Objekt
     * mit dem gleichen Namen, wird der Originaltitel anstelle des eigentlichen
     * Titels verwendet. Existiert bereits ein Objekt mit dem Originaltitel,
     * wird an den Namen ein Postfix angehangen z.B. Film_2
     *
     * @param movie the movie object
     * @param collection the collection to add the movie
     *
     */
    public synchronized void addMovieToCollection(Movie movie, MovieCollection collection) {
        final String newTitle;
        if (!movie.getTitle().isEmpty()) {
            if (!collection.contains(movie.getTitle())) {
                newTitle = movie.getTitle();
            } else if (movie.getOriginalTitle() != null && !movie.getOriginalTitle().isEmpty() && !collection.contains(movie.getOriginalTitle())) {
                newTitle = movie.getOriginalTitle();
            } else {
                int i = 1;
                while (collection.contains(movie.getTitle() + '_' + i)) {
                    i++;
                }
                newTitle = movie.getTitle() + "_" + i;
            }
            movie.setTitle(newTitle);
            collection.add(movie);
            PersistingManager.INSTANCE.setDocumentChanged(true);
        }
    }
       
    /**
     * Updates the title of a movie and puts them into a new collection.
     * Informs the GUI that the data structure has changed.
     * 
     * @param newCollection The new collection of the movie.
     * @param movie The Movie
     * @param newTitle The new movie title
     * 
     * @return The updated movie
     */
    public Movie updateMovie(MovieCollection newCollection, 
            Movie movie, 
            String newTitle){
        Movie newMovie = movie;
        if ((newCollection != null) && (null != movie) && (null != newTitle) && (!newTitle.isEmpty())) {
            MediaCollection oldCollection = movie.getCollection();
            if((oldCollection != null) && (!oldCollection.equals(newCollection))){
                //Different collections, remove movie from the old collection
                movie.getCollection().remove(movie);
                newMovie = movie.clone();
                newMovie.setTitle(newTitle);
                addMovieToCollection(newMovie, newCollection);
            }
            else if(oldCollection == null){
                //new Collection, add movie to the new collection
                newMovie = movie;
                newMovie.setTitle(newTitle);
                addMovieToCollection(newMovie, newCollection);
            }
            else{
                //Same collections, do not update collection
                newMovie = movie;
                newMovie.setTitle(newTitle);
            }
            CollectionManager.INSTANCE.orderCollectionBy(newCollection.getOrdering(), newCollection);            
            PersistingManager.INSTANCE.setDocumentChanged(true);
        }
        return newMovie;
    }
      
    /**
     * Updates the title of a movie, stored in a collection.
     * Informs the GUI that the data structure has changed.
     * 
     * @param movie The movie
     * @param newTitle The new movie title
     * 
     * @return The updated movie
     */
    public Movie updateMovie(Movie movie,
            String newTitle) { 
        return updateMovie((MovieCollection)movie.getCollection(), movie, newTitle);
    }
    
    /**
     * Removes the element from the collection and updates document status
     *
     * @param collection the collection to remove the value
     * @param movieTitle the title of the movie
     * @return the removed movie <br/> null if the title was not found
     */
    public Movie removeMovie(MovieCollection collection, String movieTitle) {            
        Movie movie = collection.get(movieTitle);
        if (movie != null) {
            PictureManager.deleteImage(movie);
            collection.remove(movie);
            
            PersistingManager.INSTANCE.setDocumentChanged(true);
        }
        return movie;
    }
    
    /**
     * Methode wird für den Import verwendet und ist synchronized, damit nicht
     * zeitgleich Objekte mit dem gleichen Namen angelegt werden können. Fügt
     * eine Serie zur Datenbasis <b>und der GUI</b> hinzu. Existiert bereits ein
     * Objekt mit dem gleichen Namen, wird der Originaltitel anstelle des
     * eigentlichen Titels verwendet. Existiert bereits ein Objekt mit dem
     * Originaltitel, wird an den Namen ein Postfix angehangen z.B. Film_2
     *
     * @param serie the serie object
     * @param collection the collection to add the serie
     *
     */
    public synchronized void addSerieToCollection(Serie serie, SerieCollection collection) {
        final String newTitle;
        if (!serie.getTitle().isEmpty()) {
            if (!collection.contains(serie.getTitle())) {
                newTitle = serie.getTitle();
            } else if (serie.getOriginalTitle() != null && !serie.getOriginalTitle().isEmpty() && !collection.contains(serie.getOriginalTitle())) {
                newTitle = serie.getOriginalTitle();
            } else {
                int i = 1;
                while (collection.contains(serie.getTitle() + '_' + i)) {
                    i++;
                }
                newTitle = serie.getTitle() + "_" + i;
            }
            serie.setTitle(newTitle);
            collection.add(serie);
            PersistingManager.INSTANCE.setDocumentChanged(true);
        }
    }
           
    /**
     * Updates the title of a serie and puts them into a new collection.
     * Informs the GUI that the data structure has changed.
     * 
     * @param newCollection The new collection of the serie
     * @param serie The serie
     * @param newTitle The new series title
     * @param seasonList A list of seasons
     * 
     * @return The updated serie
     */
    public Serie updateSerie(SerieCollection newCollection, 
            Serie serie, 
            String newTitle,
            List<Season> seasonList){
        Serie newSerie = serie;
        if ((newCollection != null) && (null != serie) && (null != newTitle) && (!newTitle.isEmpty())) {
            MediaCollection oldCollection = serie.getCollection();
            if((oldCollection != null) && (!oldCollection.equals(newCollection))){
                //Different collections, remove serie from the old collection
                serie.getCollection().remove(serie);
                newSerie = serie.clone();
                newSerie.setTitle(newTitle);
                //SeasonList is already cloned completly
                newSerie.setSeasons(seasonList);
                newSerie.orderSeasonsByNumber();      
                addSerieToCollection(newSerie, newCollection);
            }
            else if(oldCollection == null){
                //new Collection, add serie to the new collection
                newSerie = serie;
                newSerie.setTitle(newTitle);
                newSerie.setSeasons(seasonList);
                newSerie.orderSeasonsByNumber();       
                addSerieToCollection(newSerie, newCollection);
            }
            else{
                //Same collections, do not update collection
                newSerie = serie;
                newSerie.setTitle(newTitle);
                newSerie.setSeasons(seasonList);
                newSerie.orderSeasonsByNumber();      
            }
            CollectionManager.INSTANCE.orderCollectionBy(newCollection.getOrdering(), newCollection);            
            PersistingManager.INSTANCE.setDocumentChanged(true); 
        }
        return newSerie;
    }
    
    /**
     * Updates the title of a serie.
     * Informs the GUI that the data structure has changed.
     * 
     * @param serie The serie
     * @param newTitle The new series title
     * @param seasonList A list of seasons
     * 
     * @return The updated serie
     */
    public Serie updateSerie(Serie serie,
            String newTitle,
            List<Season> seasonList) { 
        return updateSerie((SerieCollection)serie.getCollection(), serie, newTitle, seasonList);
    }
    
    /**
     * removes the element from the collection and updates document status
     *
     * @param collection the collection to remove the value
     * @param serieTitle the title of the serie
     * @return the removed serie <br/> null if the title was not found
     */
    public Serie removeSerie(SerieCollection collection, String serieTitle) {
        Serie serie = collection.get(serieTitle);
        if (serie != null) {
            PictureManager.deleteImage(serie);
            collection.remove(serie);
        
            PersistingManager.INSTANCE.setDocumentChanged(true);
        }
        return serie;
    }   
    
    /**
     * Adds a season to a serie.
     * Notifies the GUI for data change. <br/>
     * If a season with the season number already exist, the season number will be increased.
     * 
     * @param season The season
     * @param serie The serie to add the season
     *
     */
    public void addSeasonToSerie(Season season, Serie serie) {
        final int seasonNr;
        if (season.getSeasonNumber() > 0){
            if (!serie.containsSeasonNr(season.getSeasonNumber())){
                seasonNr = season.getSeasonNumber();
            } else {
                int i = season.getSeasonNumber() + 1;
                while (serie.containsSeasonNr(i)) {
                    i++;
                }
                seasonNr = i;
            }
            season.setSeasonNumber(seasonNr);
            serie.addSeason(season);
            PersistingManager.INSTANCE.setDocumentChanged(true);
        }
    }
       
    /**
     * removes the season from the serie and updates document status
     *
     * @param serie the serie where the season belongs to
     * @param seasonNr the number of the season
     * @return the removed season <br/> null if the seasonNr was not found
     */
    public Season removeSeason(Serie serie, int seasonNr) {
        Season season = serie.getSeasonNr(seasonNr);
        if (season != null) {        
            serie.removeSeason(season);
            PersistingManager.INSTANCE.setDocumentChanged(true);
        }
        return season;
    }
    
    /**
     * Adds a episode to a season.
     * Notifies the GUI for data change. <br/>
     * If a episode with the episode number already exist, the episode number will be increased.
     * 
     * @param episode The episode
     * @param season The season to add the episode
     *
     */
    public void addEpisodeToSeason(Episode episode, Season season) {
        final int episodeNr;
        if (episode.getEpisodeNumber() > 0){
            if (!season.containsEpisodeNr(episode.getEpisodeNumber())){
                episodeNr = episode.getEpisodeNumber();
            } else {
                int i = episode.getEpisodeNumber() + 1;
                while (season.containsEpisodeNr(i)) {
                    i++;
                }
                episodeNr = i;
            }
            episode.setEpisodeNumber(episodeNr);
            season.addEpisode(episode);
            PersistingManager.INSTANCE.setDocumentChanged(true);
        }
    }

    /**
     * removes the episode from the season and updates document status
     *
     * @param season the season to remove
     * @param episodeTitle the title of the episode
     * @return the removed episode <br/> null if the title was not found
     */
    public Episode removeEpisode(Season season, String episodeTitle) {
        Episode episode = season.getEpisode(episodeTitle);
        if (episode != null) { 
            PictureManager.deleteImage(episode);
            season.removeEpisode(episode);
            PersistingManager.INSTANCE.setDocumentChanged(true);
        }
        return episode;
    }               
}
