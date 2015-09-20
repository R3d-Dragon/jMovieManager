/**
 * Copyright (c) 2010-2015 Bryan Beck.
 * All rights reserved.
 * 
 * This project is licensed under LGPL v2.1.
 * See jMovieManager-license.txt for details.
 * 
 */
package jmm.utils;

import java.util.*;
import jmm.data.MediaFile;
import jmm.data.Movie;
import jmm.data.Serie;
import jmm.data.VideoFile.FSK;
import jmm.data.collection.MediaCollection;
import jmm.data.collection.MovieCollection;
import jmm.data.collection.SerieCollection;
import jmm.persist.DaoInterface;
import jmm.persist.DaoManager;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;

/**
 *
 * ExtendedSearch Klasse, die alle Medienobjekte (Filme, Serien) anhand von bestimmten Kriterien 
 * 
 * @author Bryan Beck
 * @since 20.06.2011
 */
public class ExtendedSearch {
    /** Logger. */
    private static final Logger LOG = LoggerFactory.logger(ExtendedSearch.class);
    
    private MediaCollection<? extends MediaFile> searchCollection;

    private final DaoInterface<MovieCollection> movieCollectionDao;
    private final DaoInterface<SerieCollection> serieCollectionDao;
    private final DaoInterface<Movie> movieDao;
    private final DaoInterface<Serie> serieDao;
    
    private static ExtendedSearch instance;
    
    /**
     * Singleton Construtor to reference all required DAO instances
     * 
     */
    private ExtendedSearch(){
        movieCollectionDao = (DaoInterface<MovieCollection>)DaoManager.getDao(MovieCollection.class);
        serieCollectionDao = (DaoInterface<SerieCollection>)DaoManager.getDao(SerieCollection.class);
        movieDao = (DaoInterface<Movie>)DaoManager.getDao(Movie.class);
        serieDao =(DaoInterface<Serie>)DaoManager.getDao(Serie.class);
    }
    
    /**
     * Singleton instance from extendedSearch
     * @return the singleton instance
     */
    public static ExtendedSearch getInstance(){
        if(instance == null){
            instance = new ExtendedSearch();
        }
        return instance;
    }

    /**
     * @return the searchCollection
     */
    public MediaCollection<? extends MediaFile> getSearchCollection() {
        return searchCollection;
//        return Collections.unmodifiableCollection(searchCollection);
    }
    
    /**
     * filters all movies from the database
     * 
     * @param collectionTitle the title of the search collection
     * @param collection The collection to search through. <br/>Can be <b>null</b>
     * @param title The title
     * @param oTitle The original title
     * @param genreKeys The genres
     * @param playtimeFrom The minimum playtime
     * @param playtimeTo The maximum playtime
     * @param rlsYearFrom The minimum release year
     * @param rlsYearTo The maximum release year
     * @param widthFrom The minimum x resolution
     * @param heightFrom The minimum y resolution
     * @param widthTo The maximum x resolution
     * @param heightTo The maximum y resolution
     * @param publisher The publisher
     * @param actors The actors
     * @param director The director
     * @param imdbRatingFrom The minimum imdb rating
     * @param imdbRatingTo The maximum imdb rating
     * @param persRatingFrom The minimum personal rating
     * @param persRatingTo The maximum personal rating
     * @param fskList A list of FSK ratings to search
     * @param watched True, False or null (for both)
     */
    public void filterMovies(String collectionTitle,
            MovieCollection collection,
            String title,
            String oTitle,
            Set<String> genreKeys,
            int playtimeFrom,
            int playtimeTo,
            int rlsYearFrom,
            int rlsYearTo,
            int widthFrom,
            int heightFrom,
            int widthTo,
            int heightTo,
            String publisher,
            Set<String> actors,
            String director,
            double imdbRatingFrom,
            double imdbRatingTo,
            double persRatingFrom,
            double persRatingTo, 
            List<FSK> fskList,
            Boolean watched){
        //Darf NICHT, da ansonsten  Exception
        //org.hibernate.NonUniqueObjectException: a different object with the same identifier value was already associated with the session: [jmoviemanager.Collection.MovieCollection#1]
//        movieCollectionDao.getDbAccess().getActiveSession().clear();
//        movieDao.getDbAccess().getActiveSession().clear();
        
        StringBuilder sqlStringBuilder = new StringBuilder("FROM MediaFile me WHERE ");
        Map<String, Object> queryParams = new HashMap<String, Object>();
        List<MovieCollection> movieCollectionList;
        if(collection == null){
            movieCollectionList = movieCollectionDao.find("FROM MovieCollection");
        }else{
            queryParams.put("uidParam", collection.getUid());            
            movieCollectionList = movieCollectionDao.find("FROM MovieCollection mc WHERE mc.uid=:uidParam", queryParams);
            queryParams.clear();
        }

        List<Movie> results;
        if(!movieCollectionList.isEmpty()){
            buildBasicQuery(sqlStringBuilder, 
                    queryParams, 
                    title, 
                    oTitle, 
                    genreKeys, 
                    playtimeFrom, 
                    playtimeTo, 
                    rlsYearFrom,
                    rlsYearTo, 
                    publisher, 
                    actors, 
                    director, 
                    imdbRatingFrom, 
                    imdbRatingTo, 
                    persRatingFrom, 
                    persRatingTo,
                    fskList);
            //additional query attributes
            if(widthFrom > 0){
                sqlStringBuilder.append("me.width>=:widthFromParam AND ");
                queryParams.put("widthFromParam", widthFrom);
            }
            if(heightFrom > 0){
                 sqlStringBuilder.append("me.height>=:heightFromParam AND ");
                queryParams.put("heightFromParam", heightFrom);               
            }
            if(widthTo > 0){
                sqlStringBuilder.append("me.width<=:widthToParam AND ");
                queryParams.put("widthToParam", widthTo);                
            }
            if(heightTo > 0){
                sqlStringBuilder.append("me.height<=:heightToParam AND ");
                queryParams.put("heightToParam", heightTo);                
            }
            if(watched != null){
                sqlStringBuilder.append("me.uid IN (SELECT uid FROM Movie where watched=:watchedParam) AND ");
                queryParams.put("watchedParam", watched);
            }

            sqlStringBuilder.append("(");
            boolean first = true;
            int i = 1;
            for(MovieCollection mCollection: movieCollectionList){
                queryParams.put("collectionParam" +i, mCollection);
                if(first){
                    sqlStringBuilder.append("me.collection=:collectionParam").append(i);
                    first = false;
                }
                else{
                    sqlStringBuilder.append(" OR me.collection=:collectionParam").append(i);
                }
                i++;
            }
            sqlStringBuilder.append(")");

//            System.out.println(sqlStringBuilder.toString());
            results = movieDao.find(sqlStringBuilder.toString(), queryParams);
        }else{
            results = new LinkedList<>();
        }
        
        searchCollection = new MovieCollection(collectionTitle + " (" + results.size() + ")", 0);        
        searchCollection.setSearchCollection(true);
        ((MovieCollection)searchCollection).addAll(results);
        searchCollection.orderByName();
    }
    
    /**
     * Filters all series from the database
     * 
     * @param collectionTitle the title of the search collection
     * @param collection The collection to search through. <br/>Can be <b>null</b>
     * @param title The title
     * @param oTitle The original title
     * @param genreKeys The genres
     * @param playtimeFrom The minimum playtime
     * @param playtimeTo The maximum playtime
     * @param rlsYearFrom The minimum release year
     * @param rlsYearTo The maximum release year
     * @param publisher The publisher
     * @param actors The actors
     * @param director The director
     * @param imdbRatingFrom The minimum imdb rating
     * @param imdbRatingTo The maximum imdb rating
     * @param persRatingFrom The minimum personal rating
     * @param persRatingTo  The maximum personal rating
     * @param fskList A list of FSK ratrings to search
     */
    public void filterSeries(String collectionTitle,
            SerieCollection collection,
            String title,
            String oTitle,
            Set<String> genreKeys,
            int playtimeFrom,
            int playtimeTo,
            int rlsYearFrom,
            int rlsYearTo,
            String publisher,
            Set<String> actors,
            String director,
            double imdbRatingFrom,
            double imdbRatingTo,
            double persRatingFrom,
            double persRatingTo,
            List<FSK> fskList){
        //Darf NICHT, da ansonsten  Exception
        //org.hibernate.NonUniqueObjectException: a different object with the same identifier value was already associated with the session: [jmoviemanager.Collection.MovieCollection#1]
//        serieCollectionDao.getDbAccess().getActiveSession().clear();
//        serieDao.getDbAccess().getActiveSession().clear();
                
        StringBuilder sqlStringBuilder = new StringBuilder("FROM MediaFile me WHERE ");
        Map<String, Object> queryParams = new HashMap<>();
        List<SerieCollection> serieCollectionList;
        if(collection == null){
            serieCollectionList = serieCollectionDao.find("FROM SerieCollection");
        }else{
            queryParams.put("uidParam", collection.getUid());
            serieCollectionList = serieCollectionDao.find("FROM SerieCollection sc WHERE sc.uid=:uidParam", queryParams);
            queryParams.clear();
        }
        
        List<Serie> results;
        if(!serieCollectionList.isEmpty()){
            buildBasicQuery(sqlStringBuilder, 
                    queryParams, 
                    title, 
                    oTitle, 
                    genreKeys, 
                    playtimeFrom, 
                    playtimeTo, 
                    rlsYearFrom,
                    rlsYearTo, 
                    publisher, 
                    actors, 
                    director, 
                    imdbRatingFrom, 
                    imdbRatingTo, 
                    persRatingFrom, 
                    persRatingTo,
                    fskList);

            sqlStringBuilder.append("(");
            boolean first = true;
            int i = 1;
            for(SerieCollection sCollection: serieCollectionList){
                queryParams.put("collectionParam" +i, sCollection);
                if(first){
                    sqlStringBuilder.append("me.collection=:collectionParam").append(i);
                    first = false;
                }
                else{
                    sqlStringBuilder.append(" OR me.collection=:collectionParam").append(i);
                }
                i++;
            }
            sqlStringBuilder.append(")");

    //        System.out.println(sqlStringBuilder.toString());
            results = serieDao.find(sqlStringBuilder.toString(), queryParams);
        }
        else{
            results = new LinkedList<>();
        }

        searchCollection = new SerieCollection(collectionTitle + " (" + results.size() + ")", 0);
        searchCollection.setSearchCollection(true);
        ((SerieCollection)searchCollection).addAll(results);    
        searchCollection.orderByName();
    }    
    
    /**
     * Builds a query for basic attirbutes in movies and series
     * 
     * @param title The title
     * @param oTitle The original title
     * @param genreKeys The genres
     * @param playtimeFrom The minimum playtime
     * @param playtimeTo The maximum playtime
     * @param rlsYearFrom The minimum release year
     * @param rlsYearTo The maximum release year
     * @param publisher The publisher
     * @param actors The actors
     * @param director The director
     * @param imdbRatingFrom The minimum imdb rating
     * @param imdbRatingTo The maximum imdb rating
     * @param persRatingFrom The minimum personal rating
     * @param persRatingTo  The maximum personal rating
     * @param fskList A list of FSK ratings to search
     */
    private void buildBasicQuery(StringBuilder sqlStringBuilder, Map<String, Object> queryParams,
            String title,
            String oTitle,
            Set<String> genreKeys,
            int playtimeFrom,
            int playtimeTo,
            int rlsYearFrom,
            int rlsYearTo,
            String publisher,
            Set<String> actors,
            String director,
            double imdbRatingFrom,
            double imdbRatingTo,
            double persRatingFrom,
            double persRatingTo,
            List<FSK> fskList
            ){
        if(!title.isEmpty()){
            sqlStringBuilder.append("UPPER(me.title) LIKE UPPER(:titleParam) AND ");
            queryParams.put("titleParam", "%" + title + "%");
        }
        if(!oTitle.isEmpty()){
            sqlStringBuilder.append("UPPER(me.originalTitle) LIKE UPPER(:oTitleParam) AND ");
            queryParams.put("oTitleParam", "%" + oTitle + "%");
        }
        if(!genreKeys.isEmpty()){
            int i = 1;
            for(String genreKey: genreKeys){
                queryParams.put("genresParam" +i, genreKey);
                sqlStringBuilder.append("me.uid IN (SELECT me.uid FROM MediaFile me JOIN me.genreKeys gen where gen=:genresParam").append(i).append(") AND ");
                i++;
            }
        }
        if(playtimeFrom > 0){
            sqlStringBuilder.append("me.playtime>=:playtimeFromParam AND ");
            queryParams.put("playtimeFromParam", playtimeFrom);
        }
        if(playtimeTo > 0){
            sqlStringBuilder.append("me.playtime<=:playtimeToParam AND ");  
            queryParams.put("playtimeToParam", playtimeTo);
        }
        if(rlsYearFrom > 0){
            sqlStringBuilder.append("me.releaseYear>=:rlsYearFromParam AND ");
            queryParams.put("rlsYearFromParam", rlsYearFrom);
        }
        if(rlsYearTo > 0){
            sqlStringBuilder.append("me.releaseYear<=:rlsYearToParam AND ");
            queryParams.put("rlsYearToParam", rlsYearTo);
        }     
        if(!publisher.isEmpty()){
            sqlStringBuilder.append("UPPER(me.publisher) LIKE UPPER(:publisherParam) AND ");
            queryParams.put("publisherParam", "%" + publisher + "%");
        }     
        if(!actors.isEmpty()){
            int i = 1;
            for(String actor: actors){
                sqlStringBuilder.append("me.uid IN (SELECT v.uid FROM VideoFile v JOIN v.actors act where UPPER(act.name) LIKE UPPER(:actorsParam").append(i).append(")").append(") AND ");
                queryParams.put("actorsParam" +i, "%" + actor + "%");
                i++;       
            }      
        }        
        if(!director.isEmpty()){
            sqlStringBuilder.append("me.uid IN (SELECT uid FROM VideoFile where UPPER(director) LIKE UPPER(:directorParam)) AND ");
            queryParams.put("directorParam", "%" + director + "%");
        }           
        if(imdbRatingFrom > 0){
            sqlStringBuilder.append("me.uid IN (SELECT uid FROM LocalVideoFile where online_rating>=:imdbRatingFromParam) AND ");
            queryParams.put("imdbRatingFromParam", imdbRatingFrom);
        }        
        if(imdbRatingTo > 0){
            sqlStringBuilder.append("me.uid IN (SELECT uid FROM LocalVideoFile where online_rating<=:imdbRatingToParam) AND ");
            queryParams.put("imdbRatingToParam", imdbRatingTo);
        }         
        if(persRatingFrom > 0){
            sqlStringBuilder.append("me.personalRating>=:persRatingFromParam AND ");
            queryParams.put("persRatingFromParam", persRatingFrom);
        }  
        if(persRatingTo > 0){
            sqlStringBuilder.append("me.personalRating<=:persRatingToParam AND ");
            queryParams.put("persRatingToParam", persRatingTo);
        } 
        if(!fskList.isEmpty()){
            int fskSize = fskList.size();
            sqlStringBuilder.append("me.uid IN (SELECT uid FROM VideoFile where (");
            for(int i = 0; i < fskSize; i ++){
                FSK fsk = fskList.get(i);
                //If the last element is selected
                if(i+1 == fskSize){
                    sqlStringBuilder.append("fsk=:fskRating").append(i).append(")");
                }else{
                    sqlStringBuilder.append("fsk=:fskRating").append(i).append(" OR ");
                }
                queryParams.put("fskRating" +i, fsk);
            }
            sqlStringBuilder.append(") AND ");
        }
    }
}
