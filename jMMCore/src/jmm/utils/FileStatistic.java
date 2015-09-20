/**
 * Copyright (c) 2010-2015 Bryan Beck.
 * All rights reserved.
 * 
 * This project is licensed under LGPL v2.1.
 * See jMovieManager-license.txt for details.
 * 
 */
package jmm.utils;

import java.io.File;
import java.util.*;
import jmm.data.Actor;
import jmm.data.Episode;
import jmm.data.Genre;
import jmm.data.MediaFile;
import jmm.data.Movie;
import jmm.data.Season;
import jmm.data.Serie;
import jmm.data.collection.MediaCollection;
import jmm.data.collection.MovieCollection;
import jmm.data.collection.SerieCollection;
import jmm.interfaces.GenreKeysInterface;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;

/**
 * Generiert eine Statistik über alle Elemente der Movie Kollektion
 * Singleton Instanz
 *
 * @author Bryan Beck
 * @since 17.04.2010
 */
public class FileStatistic implements GenreKeysInterface{
    /** Logger. */
    private static final Logger LOG = LoggerFactory.logger(FileStatistic.class);
    
    private ResourceBundle bundle;

    private int totalMediaFiles;    //Serien = Anzahl der Serien
    private int totalFiles;         //Serien = Anzahl der Episoden

    private double totalPlaytime;
    private double totalFileSizeInMByte;

    private double averageFileSizeInMByte;
    private int averageVideoBitrate;
    private int averageAudioBitrate;
    private double averagePlaytime;
    private double averageImdbRating;
    private double averagePersonalRating;

    //Einteilung in Genres
    private final Map<String, Integer> differentGenres;
    //Einteilung in Dateiendungen
    private final Map<String, Integer> differentFileEnding;
    //Einteilung nach Quellen (source)
    private final Map<String, Integer> differentSources;
    //Einteilung nach Schauspielern
    private final Map<String, Integer> differentActors;

    /**
     * Standardkonstruktor der Klasse
     */
    public FileStatistic() {
        differentGenres = new HashMap<>();
        differentFileEnding = new HashMap<>();
        differentSources = new HashMap<>();
        differentActors = new HashMap<>();
    }

    /**
     * @return the totalMediaFiles
     */
    public int getTotalMediaFiles() {
        return totalMediaFiles;
    }

    /**
     * @return the totalFiles
     */
    public int getTotalFiles() {
        return totalFiles;
    }

    /**
     * @return the totalPlaytime
     */
    public double getTotalPlaytime() {
        return totalPlaytime;
    }

    /**
     * @return the totalFileSizeInMByte
     */
    public double getTotalFileSizeInMByte() {
        return totalFileSizeInMByte;
    }

    /**
     * @return the averageFileSizeInMByte
     */
    public double getAverageFileSizeInMByte() {
        return averageFileSizeInMByte;
    }

    /**
     * @return the averageVideoBitrate
     */
    public int getAverageVideoBitrate() {
        return averageVideoBitrate;
    }

    /**
     * @return the averageAudioBitrate
     */
    public int getAverageAudioBitrate() {
        return averageAudioBitrate;
    }

    /**
     * @return the averagePlaytime
     */
    public double getAveragePlaytime() {
        return averagePlaytime;
    }

    /**
     * @return the averageImdbRating
     */
    public double getAverageImdbRating() {
        return averageImdbRating;
    }

    /**
     * @return the averagePersonalRating
     */
    public double getAveragePersonalRating() {
        return averagePersonalRating;
    }

    /**
     * @return the differentGenres
     */
    public Map<String, Integer> getDifferentGenres() {
        return Collections.unmodifiableMap(differentGenres);
    }

    /**
     * @return the differentFileEnding
     */
    public Map<String, Integer> getDifferentFileEnding() {
        return Collections.unmodifiableMap(differentFileEnding);
    }

    /**
     * @return the differentSources
     */
    public Map<String, Integer> getDifferentSources() {
        return Collections.unmodifiableMap(differentSources);
    }

    /**
     * @return the differentActors
     */
    public Map<String, Integer> getDifferentActors() {
        return Collections.unmodifiableMap(differentActors);
    }

    /**
     * Setzt alle Attribute auf den Startwert zurück,
     * sowie das Locale neu
     */
    private void resetInitValues(){
        bundle = LocaleManager.getInstance().getGenreKeyAndCodecBundle();
        differentActors.clear();
        differentFileEnding.clear();
        differentGenres.clear();
        differentSources.clear();
        totalMediaFiles = 0;
        totalFiles = 0;
        totalPlaytime = 0;
        totalFileSizeInMByte = 0;
        averageFileSizeInMByte = 0;
        averageVideoBitrate = 0;
        averageAudioBitrate = 0;
        averagePlaytime = 0;
        averageImdbRating = 0;
        averagePersonalRating = 0;
    }

    /**
     * Generates a fileStatistic over the complete collection
     * 
     * @param collection the collection to analyse
     */
    public void generateFileStatistic(MediaCollection<? extends MediaFile> collection){
        this.resetInitValues();
        if(collection instanceof MovieCollection){
            generateMovieStatistic((MovieCollection)collection);
        }
        else if(collection instanceof SerieCollection){
            generateSerieStatistic((SerieCollection)collection);
        }
    }
    
    /**
    * Generates a fileStatistic for a serie collection
    * @param collection the serieCollection
    */
    private void generateSerieStatistic(SerieCollection collection){
        //TODO: Refactoren, sobald einzelne Episoden bei IMDB ausgelegen werden!
        List<Serie> serieList = collection.getAll();

        if(serieList.size() > 0){
            //Anzahl aller Filme
            totalMediaFiles = serieList.size();
            for(Serie serie: serieList){ 
                //Einteilung in Genres
                Integer count;
                if(serie.getGenreKeys().isEmpty()){
                    count = differentGenres.get(Genre.empty.getGenreKey());
                            //bundle.getString("Value.empty"));
                    if(null == count){
                        differentGenres.put(Genre.empty.getGenreKey(), 1);
                            //bundle.getString("Value.empty"), 1);
                    }else{
                        differentGenres.put(Genre.empty.getGenreKey(), count +1);
                            //bundle.getString("Value.empty"), count +1);
                    }
                }else{
                    for(String genreKey: serie.getGenreKeys()){
                        count = differentGenres.get(genreKey);
                        if(null == count){
                            differentGenres.put(genreKey, 1);
                        }else{
                            differentGenres.put(genreKey, count +1);
                        }
                    }
                }       
                //Einteilung der Schauspieler
                if(serie.getActors().isEmpty()){
                    count = differentActors.get(bundle.getString("Value.empty"));
                    if(null == count){
                        differentActors.put(bundle.getString("Value.empty"), 1);
                    }else{
                        differentActors.put(bundle.getString("Value.empty"), count +1);
                    }
                }else{
                    for(Actor actor: serie.getActors()){
                        count = differentActors.get(actor.getName());
                        if(null == count){
                            differentActors.put(actor.getName(), 1);
                        }else{
                            differentActors.put(actor.getName(), count +1);
                        }
                    }
                }
                
                averageImdbRating += serie.getOnlineRating();
                averagePersonalRating += serie.getPersonalRating();
                
                for(Season season: serie.getSeasons()){
                    for(Episode episode: season.getEpisodes()){
                        int i;
                        //Gesamte Laufzeit
                        totalPlaytime += episode.getPlaytime();
                        //Gesamt Files
                        totalFiles ++;
                        //Gesamter Speicherbedarf
                        for(String filePath: episode.getFilePaths()){
                            File movieFile = new File(filePath);
                            totalFileSizeInMByte += (((movieFile.length()/1024)/1024));
                            //Dateiendung
                            String absolutePath = filePath;
                            i = absolutePath.lastIndexOf('.');
                            if(i > -1){
                                String fileEnding = absolutePath.substring(i);
                                count = getDifferentFileEnding().get(fileEnding);
                                if(null == count){
                                    differentFileEnding.put(fileEnding, 1);
                                }
                                else{
                                    differentFileEnding.put(fileEnding, count +1);
                                }
                            }
                        }
                        //Addiere die Durchschnittswerte auf
                        averageVideoBitrate += episode.getVideoBitrate();
                        averageAudioBitrate += episode.getAudioBitrate();
                        
                        //Einteilung in Source
                        if(episode.getVideoSource().isEmpty()){
                            count = differentSources.get(bundle.getString("Value.empty"));
                            if(null == count){
                                differentSources.put(bundle.getString("Value.empty"), 1);
                            }else{
                                differentSources.put(bundle.getString("Value.empty"), count +1);
                            }
                        }else{
                            count = differentSources.get(episode.getVideoSource());
                            if(null == count){
                                differentSources.put(episode.getVideoSource(), 1);
                            }else{
                                differentSources.put(episode.getVideoSource(), count + 1);
                            }
                        }                                                   
                    }
                }   
            }
            
                //Bilde Durchschnittswerte
                if(getTotalFiles() == 0){       //Episoden
                    averageFileSizeInMByte = 0;
                    averagePlaytime = 0;
                    averageVideoBitrate = 0;
                    averageAudioBitrate = 0;
                }else{
                    averageFileSizeInMByte = getTotalFileSizeInMByte() / getTotalFiles();
                    averagePlaytime = getTotalPlaytime() / getTotalFiles();
                    averageVideoBitrate = getAverageVideoBitrate() / getTotalFiles();
                    averageAudioBitrate = getAverageAudioBitrate() / getTotalFiles();
                }
                if(getTotalMediaFiles() == 0){  //Serien
    //                averagePlaytime = 0;
    //                averageVideoBitrate = 0;
    //                averageAudioBitrate = 0;
                    averageImdbRating = 0;
                    averagePersonalRating = 0;
                }else{
    //                averagePlaytime = getTotalPlaytime() / getTotalMediaFiles();
    //                averageVideoBitrate = getAverageVideoBitrate() / getTotalMediaFiles();
    //                averageAudioBitrate = getAverageAudioBitrate() / getTotalMediaFiles();
                    averageImdbRating = getAverageImdbRating() / getTotalMediaFiles();
                    averagePersonalRating = getAveragePersonalRating() / getTotalMediaFiles();
                }
        }
    }
    
    
    /**
    * Generates a fileStatistic for a movie collection
    * @param collection the movieCollection
    */
    private void generateMovieStatistic(MovieCollection collection){
        List<Movie> movieFiles = collection.getAll();

        if(movieFiles.size() > 0){
            //Anzahl aller Filme
            totalMediaFiles = movieFiles.size();
            for(Movie tempMovie: movieFiles){
                analyseMovie(tempMovie);
            }
            //Bilde Durchschnittswerte
            if(getTotalFiles() == 0){
                averageFileSizeInMByte = 0;
            }else{
                averageFileSizeInMByte = getTotalFileSizeInMByte() / getTotalFiles();
            }
            if(getTotalMediaFiles() == 0){
                averagePlaytime = 0;
                averageVideoBitrate = 0;
                averageAudioBitrate = 0;
                averageImdbRating = 0;
                averagePersonalRating = 0;
            }else{
                averagePlaytime = getTotalPlaytime() / getTotalMediaFiles();
                averageVideoBitrate = getAverageVideoBitrate() / getTotalMediaFiles();
                averageAudioBitrate = getAverageAudioBitrate() / getTotalMediaFiles();
                averageImdbRating = getAverageImdbRating() / getTotalMediaFiles();
                averagePersonalRating = getAveragePersonalRating() / getTotalMediaFiles();
            }
        }
    }
    
    private void analyseMovie(Movie movie){
        Integer count;
        int i;
        //Gesamte Laufzeit
        totalPlaytime += movie.getPlaytime();
        //Gesamt Files
        totalFiles += movie.getFilePaths().size();
        //Gesamter Speicherbedarf
        for(String filePath: movie.getFilePaths()){
            File movieFile = new File(filePath);
            totalFileSizeInMByte += (((movieFile.length()/1024)/1024));
            //Dateiendung
            String absolutePath = filePath;
            i = absolutePath.lastIndexOf('.');
            if(i > -1){
                String fileEnding = absolutePath.substring(i);
                count = getDifferentFileEnding().get(fileEnding);
                if(null == count){
                    differentFileEnding.put(fileEnding, 1);
                }
                else{
                    differentFileEnding.put(fileEnding, count +1);
                }
            }
        }
        //Addiere die Durchschnittswerte auf
        averageVideoBitrate += movie.getVideoBitrate();
        averageAudioBitrate += movie.getAudioBitrate();
        averageImdbRating += movie.getOnlineRating();
        averagePersonalRating += movie.getPersonalRating();

        //Einteilung in Genres
        if(movie.getGenreKeys().isEmpty()){
            count = differentGenres.get(Genre.empty.getGenreKey());
                    //bundle.getString("Value.empty"));
            if(null == count){
                differentGenres.put(Genre.empty.getGenreKey(), 1);
                    //bundle.getString("Value.empty"), 1);
            }else{
                differentGenres.put(Genre.empty.getGenreKey(), count +1);
                    //bundle.getString("Value.empty"), count +1);
            }
        }else{
            for(String genreKey: movie.getGenreKeys()){
                count = differentGenres.get(genreKey);
                if(null == count){
                    differentGenres.put(genreKey, 1);
                }else{
                    differentGenres.put(genreKey, count +1);
                }
            }
        }
        //Einteilung in Source
        if(movie.getVideoSource().isEmpty()){
            count = differentSources.get(bundle.getString("Value.empty"));
            if(null == count){
                differentSources.put(bundle.getString("Value.empty"), 1);
            }else{
                differentSources.put(bundle.getString("Value.empty"), count +1);
            }
        }else{
            count = differentSources.get(movie.getVideoSource());
            if(null == count){
                differentSources.put(movie.getVideoSource(), 1);
            }else{
                differentSources.put(movie.getVideoSource(), count + 1);
            }
        }
        //Einteilung der Schauspieler
        if(movie.getActors().isEmpty()){
            count = differentActors.get(bundle.getString("Value.empty"));
            if(null == count){
                differentActors.put(bundle.getString("Value.empty"), 1);
            }else{
                differentActors.put(bundle.getString("Value.empty"), count +1);
            }
        }else{
            for(Actor actor: movie.getActors()){
                count = differentActors.get(actor.getName());
                if(null == count){
                    differentActors.put(actor.getName(), 1);
                }else{
                    differentActors.put(actor.getName(), count +1);
                }
            }
        }        
    }
}
