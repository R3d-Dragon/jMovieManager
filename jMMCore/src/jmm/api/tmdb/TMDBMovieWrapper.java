/**
 * Copyright (c) 2010-2015 Bryan Beck.
 * All rights reserved.
 * 
 * This project is licensed under LGPL v2.1.
 * See jMovieManager-license.txt for details.
 * 
 */
package jmm.api.tmdb;

import jmm.data.TMDBVideoFile;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import jmm.data.Actor;
import jmm.data.Genre;
import jmm.data.VideoFile;
import static jmm.api.tmdb.TMDBWrapper.API_BASE_IMAGEURL;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;

/**
 * Wrapper class for TMDB Movie API calls
 * 
 * @author Bryan Beck
 * @since 22.02.2013
 */
public class TMDBMovieWrapper extends TMDBWrapper {
    /** Logger. */
    private static final Logger LOG = LoggerFactory.logger(TMDBMovieWrapper.class);
    
    /**
     * Creates a new TMDBMovieWrapper
     */
    public TMDBMovieWrapper(){
        super();
    }
    
    /**
     * Get the basic movie information for a specific movie id. <br/>
     * <b>Bold</b> parameters are <b>required</b>.
     * 
     * @param id <b>the movie id</b>
     * @param language ISO 639-1 code.
     * @param append_to_response Comma separated, any movie method
     * @return the movie wth the given id <br/>null, if no movie was found with the given id
     */
    public TMDBVideoFile findMovie(String id, String language, String append_to_response){
        paramsMap.clear();
        //requiered parameters
        if(id == null){
            throw new NullPointerException("Param: " + "id" + "must not be null");
        }
        //optional parameters
        if(language != null){
            paramsMap.put(PARAM_LANGUAGE, language);
        }
        if(append_to_response != null){
            paramsMap.put(PARAM_APPEND, append_to_response);
        }
        
        String url = buildUrl(API_MOVIE + "/" + id);
        JsonElement element = callAPI(url);  
        //handle response
        TMDBVideoFile searchResult = null;
        if(!element.isJsonNull()){
            String title = element.getAsJsonObject().get("title").getAsString();
            searchResult = new TMDBVideoFile(title);
            JsonElement value;
            value = element.getAsJsonObject().get("adult");
            if(!value.isJsonNull()){                    
                searchResult.setAdult(value.getAsBoolean());
                if(searchResult.isAdult()){
                    searchResult.setFsk(VideoFile.FSK.FSK_18);
                }
            }
            value = element.getAsJsonObject().get("backdrop_path");
            if(!value.isJsonNull()){                    
                searchResult.setBackdropPath(API_BASE_IMAGEURL + Backdrop_Sizes.w780 + value.getAsString());
            }
            //genres
            value = element.getAsJsonObject().get("genres");
            if(!value.isJsonNull()){                    
                JsonArray genres = value.getAsJsonArray();
                if(genres.isJsonArray()){
                    Iterator<JsonElement> genresIterator = genres.iterator();
                    while(genresIterator.hasNext()){
                        JsonElement genreElement = genresIterator.next();
                        value = genreElement.getAsJsonObject().get("id");
                        if(!value.isJsonNull()){
                            int genreID = value.getAsInt();
                            Genre genre = Genre.getGenreByTMDBID(genreID);
                            if(genre != null){
                                searchResult.addGenreKey(genre.getGenreKey());
                            }else{
                                value = genreElement.getAsJsonObject().get("name");
                                String exceptionString; 
                                if(!value.isJsonNull()){
                                    exceptionString = "Missing genre in resource bundle: " + value.getAsString() + " " + genreID;
                                }else{
                                    exceptionString = "Missing genre in resource bundle: " + genreID;
                                }
                                LOG.warn(exceptionString);
                            }
                        }  
//                            value = genreElement.getAsJsonObject().get("name");
//                            if(!value.isJsonNull()){                    
//                                String localizedGenre = value.getAsString();
//                                String genreKey = LocaleManager.getInstance().determineGenreKeyByLocale(localizedGenre);
//                                if(genreKey == null){
//                                    MyExceptionHandler.uncaughtExceptionLogger.warning("Missing genre in resource bundle: " + localizedGenre);
//                                }else{
//                                    searchResult.addGenre(genreKey);
//                                }
//                            }
                    }
                }
            }
            value = element.getAsJsonObject().get("id");
            if(!value.isJsonNull()){                    
                searchResult.setTmdbID(value.getAsString());
            }
            value = element.getAsJsonObject().get("imdb_id");
            if(!value.isJsonNull()){                    
                searchResult.setImdbID(value.getAsString());
            }
            value = element.getAsJsonObject().get("original_title");
            if(!value.isJsonNull()){                       
                searchResult.setOriginalTitle(value.getAsString());
            }      
            value = element.getAsJsonObject().get("overview");
            if(!value.isJsonNull()){                    
                searchResult.setDescription(value.getAsString());
            }
            value = element.getAsJsonObject().get("poster_path");
            if(!value.isJsonNull()){                  
                searchResult.setImagePath(API_BASE_IMAGEURL + Poster_Sizes.w342 + value.getAsString());
            }
            //Publisher
            value = element.getAsJsonObject().get("production_companies");
            if(!value.isJsonNull()){                  
                JsonArray publishers = value.getAsJsonArray();
                if(publishers.isJsonArray()){
                    Iterator<JsonElement> publishersIterator = publishers.iterator();
                    while(publishersIterator.hasNext()){
                        //Set only the first publisher
                        JsonElement publisherElement = publishersIterator.next();
                        value = publisherElement.getAsJsonObject().get("name");
                        if(!value.isJsonNull()){                
                            String publisher = value.getAsString();
                            searchResult.setPublisher(publisher);
                            break;
                        }
                    }
                }
            }
            value = element.getAsJsonObject().get("runtime");
            if(!value.isJsonNull()){             
                searchResult.setPlaytime(value.getAsInt());
            }
            value = element.getAsJsonObject().get("vote_average");
            if(!value.isJsonNull()){             
                searchResult.setTmdbRating(value.getAsDouble());
                searchResult.setOnlineRating(value.getAsDouble());
            }
            //release date
            value = element.getAsJsonObject().get("release_date");
            if(!value.isJsonNull()){  
                String rlsDateString = value.getAsString();    
                try{
                    Date date = new SimpleDateFormat("yyyy-mm-dd").parse(rlsDateString);
                    searchResult.setReleaseDate(date);
                    String releaseYear = new SimpleDateFormat("yyyy").format(date);
                    if(!releaseYear.isEmpty()){
                        searchResult.setReleaseYear(Integer.valueOf(releaseYear));
                    }
                } catch (ParseException | NumberFormatException ex) {
                    LOG.warn("Release Year " + rlsDateString + "cannot be parsed into a valid format for title: " + title + ".", ex);
                    searchResult.setReleaseYear(0);
                }
            }

            //{"adult":false,"backdrop_path":"/eZxahf9ATsv3oQPtvZPqSZkAO7V.jpg","belongs_to_collection":{"id":1241,"name":"Harry Potter Collection",
            //"poster_path":"/fuWOg0iLKPRGTlg7lq4tWDBt5tu.jpg","backdrop_path":"/tpDcuXZGqEoU6CxuJ7e4S2NTIoS.jpg"},"budget":250000000,
            //"genres":[{"id":12,"name":"Abenteuer"},{"id":18,"name":"Drama"},{"id":14,"name":"Fantasy"},{"id":10751,"name":"Familie"}],
            //"homepage":"http://harrypotter.warnerbros.de/hp7a/","id":12444,"imdb_id":"tt0926084","original_title":"Harry Potter and the Deathly Hallows: Part 1",
            //"overview":"Harry sieht sich einer vollkommen veränderten Welt gegenüber. Die Todesesser haben das Zauberministerium unter ihre Kontrolle gebracht und es tobt ein offener Kampf zwischen den Mächten des Guten und Bösen. Harry hat sich mit Hermine (Emma Watson) und Ron (Rupert Grint) auf die Suche nach den “Horkuxen“, magischen Objekten, die die Unsterblichkeit von Lord Voldemort (Ralph Fiennes) garantieren und zerstört werden müssen. Der dunkle Lord hat seinerseits ein Kopfgeld auf Harry ausgesetzt, dener will sich das Vergnügen den “Jungen der lebte“ mit den eigenen Händen zu töten, nicht nehmen lassen. Unterdessen stößt Harry auf die Legende von den Heiligtümern des Todes, die ihm im Kampf gegen seinen Erzfeind das Leben retten könnte. Und so strebt alles unaufhaltsam der finalen Konfrontation zwischen den beiden Magiern entegegen, auf die Harry sich seit Beginn seiner Schullaufbahn vorbereitet hat.",
            //"popularity":305437.691,"poster_path":"/3ssNt5cCFYGne7OrBFkFFUVq8Cx.jpg","production_companies":[{"name":"Warner Bros. Pictures","id":174}],
            //"production_countries":[{"iso_3166_1":"US","name":"United States of America"},{"iso_3166_1":"GB","name":"United Kingdom"}],
            //"release_date":"2010-11-19","revenue":954305868,"runtime":146,
            //"spoken_languages":[{"iso_639_1":"en","name":"English"},{"iso_639_1":"fr","name":"Français"},{"iso_639_1":"ja","name":"???"}],
            //"status":"Released","tagline":"","title":"Harry Potter und die Heiligtümer des Todes - Teil 1","vote_average":8.3,"vote_count":74}
            if(append_to_response != null && append_to_response.equalsIgnoreCase("casts")){
                addActorsAndDirector(element, searchResult);
            }
        }
        return searchResult;
    }
    
    private void addActorsAndDirector(JsonElement rootElement, TMDBVideoFile videoFile){
        //handle response
        if(!rootElement.isJsonNull()){
            JsonElement value;
            //Publisher
            value = rootElement.getAsJsonObject().get("casts");
            if(value != null && !value.isJsonNull()){                  
                value = value.getAsJsonObject().get("cast");
                if(value.isJsonArray()){
                    JsonArray cast = value.getAsJsonArray();
                    Iterator<JsonElement> castIterator = cast.iterator();
                    while(castIterator.hasNext()){
                        JsonElement actorElement = castIterator.next();
                        value = actorElement.getAsJsonObject().get("name");
                        if(!value.isJsonNull()){  
                            Actor actor = new Actor(value.getAsString());
                            
                            value = actorElement.getAsJsonObject().get("id");
                            if(!value.isJsonNull()){
                                actor.setTmdbID(value.getAsInt());
                            }  
                            value = actorElement.getAsJsonObject().get("profile_path");
                            if(!value.isJsonNull()){
                                actor.setProfilePicturePath(API_BASE_IMAGEURL + Profile_Sizes.w185 + value.getAsString());
                            }
                            videoFile.addActor(actor);
                        }
                    }
                }
                value = rootElement.getAsJsonObject().get("casts");
                value = value.getAsJsonObject().get("crew");
                if(value.isJsonArray()){
                    JsonArray crew = value.getAsJsonArray();
                    //Look for director
                    Iterator<JsonElement> crewIterator = crew.iterator();
                    while(crewIterator.hasNext()){
                        JsonElement crewElement = crewIterator.next();
                        value = crewElement.getAsJsonObject().get("job");
                        if(!value.isJsonNull() && (value.getAsString().equalsIgnoreCase("Director"))){  
                            value = crewElement.getAsJsonObject().get("name");
                            if(!value.isJsonNull()){
                                videoFile.setDirector(value.getAsString());
                            }
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Get the cast information for a specific movie id. <br/>
     * <b>Bold</b> parameters are <b>required</b>.
     * 
     * @param id <b>the movie id</b>
     * @param append_to_response Comma separated, any movie method
     * 
     */
    public TMDBVideoFile findCasts(String id, String append_to_response){
        paramsMap.clear();
        //requiered parameters
        if(id == null){
            throw new NullPointerException("Param: " + "id" + "must not be null");
        }
        //optional parameters
        if(append_to_response != null){
            paramsMap.put(PARAM_APPEND, append_to_response);
        }
        
        String url = buildUrl(API_MOVIE + "/" + id  + "/casts");
        JsonElement response = callAPI(url);  
        //handle response
        TMDBVideoFile searchResult = new TMDBVideoFile(id);
        addActorsAndDirector(response, searchResult);
        return searchResult;
    }    
    
    /**
     * Get the list of upcoming movies. This list refreshes every day. The maximum number of items this list will include is 100. <br/>
     * <b>Bold</b> parameters are <b>required</b>.
     * 
     * @param page
     * @param language ISO 639-1 code.
     * @return A list of upcoming movies
     * @param maxResults the maximum number of results
     */
    public List<TMDBVideoFile> findUpcomingMovies(String page, String language, int maxResults){
        paramsMap.clear();
        //optional parameters
        if(page != null){
            paramsMap.put(PARAM_PAGE, page);
        }
        if(language != null){
            paramsMap.put(PARAM_LANGUAGE, language);
        }
        
        String url = buildUrl(API_MOVIE + "/" + "upcoming");
        JsonElement response = callAPI(url);
        //handle response
        List<TMDBVideoFile> searchResults = new LinkedList<TMDBVideoFile>();
        if(!response.isJsonNull()){
            JsonElement key = response.getAsJsonObject().get("results");
            if(key.isJsonArray()){
                JsonArray elements = key.getAsJsonArray();
                int arraySize = elements.size();
                if(arraySize > maxResults){
                    arraySize = maxResults;
                }
                for(int i = 0; i < arraySize; i++){
                    JsonElement element = elements.get(i);
                    String title = element.getAsJsonObject().get("title").getAsString();         
                    TMDBVideoFile upcomingMovie = new TMDBVideoFile(title);
                    try {
                        JsonElement value;
                        value = element.getAsJsonObject().get("backdrop_path");
                        if(!value.isJsonNull()){  
                            upcomingMovie.setBackdropPath(API_BASE_IMAGEURL + Backdrop_Sizes.w300 + value.getAsString());
                        }
                        value = element.getAsJsonObject().get("id");
                        if(!value.isJsonNull()){  
                            upcomingMovie.setTmdbID(value.getAsString());
                        }
                        value = element.getAsJsonObject().get("original_title");
                        if(!value.isJsonNull()){  
                            upcomingMovie.setOriginalTitle(value.getAsString());
                        }
                        value = element.getAsJsonObject().get("poster_path");
                        if(!value.isJsonNull()){  
                            upcomingMovie.setImagePath(API_BASE_IMAGEURL + Poster_Sizes.w154 + value.getAsString());
                        }
                        value = element.getAsJsonObject().get("vote_average");
                        if(!value.isJsonNull()){ 
                            upcomingMovie.setTmdbRating(value.getAsDouble());
                        }
                        //release date
                        value = element.getAsJsonObject().get("release_date");
                        if(!value.isJsonNull()){ 
                            String rlsDateString = value.getAsString();    
                            Date date = new SimpleDateFormat("yyyy-mm-dd").parse(rlsDateString);
                            upcomingMovie.setReleaseDate(date);
                            String releaseYear = new SimpleDateFormat("yyyy").format(date);
                            if(!releaseYear.isEmpty()){
                                upcomingMovie.setReleaseYear(Integer.valueOf(releaseYear));
                            }
                        }
                    } catch (ParseException | NumberFormatException ex) {
                        LOG.error("Erorr while Parsing JSon ELement: " + element.getAsString(), ex);
                    }
                    searchResults.add(upcomingMovie);
                    //{"page":1,"results":[
                    //{"backdrop_path":null,"id":107811,"original_title":"21 and Over","release_date":"2013-03-01","poster_path":"/6nHApzZiih5MTQAZq02DLnyrY8m.jpg","title":"21 and Over","vote_average":0.0,"vote_count":0},
                    //{...}
                    //],"total_pages":5,"total_results":100}
                }
            }
        }
        return searchResults;
    }

    /**
     * Get the list of movies playing in theatres. This list refreshes every day. The maximum number of items this list will include is 100. <br/>
     * <b>Bold</b> parameters are <b>required</b>.
     * 
     * @param page
     * @param language ISO 639-1 code.
     * @return A list of now playing movies
     * @param maxResults the maximum number of results
     */
    public List<TMDBVideoFile> findNowPlayingMovies(String page, String language, int maxResults){
        paramsMap.clear();
        //optional parameters
        if(page != null){
            paramsMap.put(PARAM_PAGE, page);
        }
        if(language != null){
            paramsMap.put(PARAM_LANGUAGE, language);
        }
        
        String url = buildUrl(API_MOVIE + "/" + "now_playing");
        JsonElement response = callAPI(url);             
        //handle response
        List<TMDBVideoFile> searchResults = new LinkedList<TMDBVideoFile>();
        if(!response.isJsonNull()){
            JsonElement key = response.getAsJsonObject().get("results");
            if(key.isJsonArray()){
                JsonArray elements = key.getAsJsonArray();
                int arraySize = elements.size();
                if(arraySize > maxResults){
                    arraySize = maxResults;
                }
                for(int i = 0; i < arraySize; i++){
                    JsonElement element = elements.get(i);
                    String title = element.getAsJsonObject().get("title").getAsString();         
                    TMDBVideoFile nowPlayingMovie = new TMDBVideoFile(title);
                    try {
                        JsonElement value;
                        value = element.getAsJsonObject().get("backdrop_path");
                        if(!value.isJsonNull()){  
                            nowPlayingMovie.setBackdropPath(API_BASE_IMAGEURL + Backdrop_Sizes.w300 + value.getAsString());
                        }
                        value = element.getAsJsonObject().get("id");
                        if(!value.isJsonNull()){  
                            nowPlayingMovie.setTmdbID(value.getAsString());
                        }
                        value = element.getAsJsonObject().get("original_title");
                        if(!value.isJsonNull()){  
                            nowPlayingMovie.setOriginalTitle(value.getAsString());
                        }
                        value = element.getAsJsonObject().get("poster_path");
                        if(!value.isJsonNull()){  
                            nowPlayingMovie.setImagePath(API_BASE_IMAGEURL + Poster_Sizes.w154 + value.getAsString());
                        }
                        value = element.getAsJsonObject().get("vote_average");
                        if(!value.isJsonNull()){ 
                            nowPlayingMovie.setTmdbRating(value.getAsDouble());
                        }
                        //release date
                        value = element.getAsJsonObject().get("release_date");
                        if(!value.isJsonNull()){ 
                            String rlsDateString = value.getAsString();    
                            Date date = new SimpleDateFormat("yyyy-mm-dd").parse(rlsDateString);
                            nowPlayingMovie.setReleaseDate(date);
                            String releaseYear = new SimpleDateFormat("yyyy").format(date);
                            if(!releaseYear.isEmpty()){
                                nowPlayingMovie.setReleaseYear(Integer.valueOf(releaseYear));
                            }
                        }
                    } catch (ParseException | NumberFormatException ex) {
                        LOG.error("Error while parsing JSon Element: " + element.getAsString(), ex);
                    }
                    searchResults.add(nowPlayingMovie);
                    //{"page":1,"results":
                    //[{"backdrop_path":"/AbHkwA7Wesm1zg2NcrrpviJ2LDW.jpg","id":145135,"original_title":"Dark Skies","release_date":"2013-02-22","poster_path":"/3CxmCk8Q60a6ilXsc7pEfCtfTJk.jpg","title":"Dark Skies","vote_average":0.0,"vote_count":0},
                    //{...}
                    //],"total_pages":3,"total_results":52}
                }
            }
        }
        return searchResults;
    }

    /**
     * Get the list of popular movies on The Movie Database. This list refreshes every day. <br/>
     * <b>Bold</b> parameters are <b>required</b>.
     * 
     * @param page
     * @param language ISO 639-1 code.
     * @return A list of popular movies
     * @param maxResults the maximum number of results
     */
    public List<TMDBVideoFile> findPopularMovies(String page, String language, int maxResults){
        paramsMap.clear();
        //optional parameters
        if(page != null){
            paramsMap.put(PARAM_PAGE, page);
        }
        if(language != null){
            paramsMap.put(PARAM_LANGUAGE, language);
        }
        
        String url = buildUrl(API_MOVIE + "/" + "popular");
        JsonElement response = callAPI(url);     
        
        //handle response
        List<TMDBVideoFile> searchResults = new LinkedList<TMDBVideoFile>();
        if(!response.isJsonNull()){
            JsonElement key = response.getAsJsonObject().get("results");
            if(key.isJsonArray()){
                JsonArray elements = key.getAsJsonArray();
                int arraySize = elements.size();
                if(arraySize > maxResults){
                    arraySize = maxResults;
                }
                for(int i = 0; i < arraySize; i++){
                    JsonElement element = elements.get(i);
                    String title = element.getAsJsonObject().get("title").getAsString();         
                    TMDBVideoFile popularMovie = new TMDBVideoFile(title);
                    try {
                        JsonElement value;
                        value = element.getAsJsonObject().get("backdrop_path");
                        if(!value.isJsonNull()){  
                            popularMovie.setBackdropPath(API_BASE_IMAGEURL + Backdrop_Sizes.w300 + value.getAsString());
                        }
                        value = element.getAsJsonObject().get("id");
                        if(!value.isJsonNull()){  
                            popularMovie.setTmdbID(value.getAsString());
                        }
                        value = element.getAsJsonObject().get("original_title");
                        if(!value.isJsonNull()){  
                            popularMovie.setOriginalTitle(value.getAsString());
                        }
                        value = element.getAsJsonObject().get("poster_path");
                        if(!value.isJsonNull()){  
                            popularMovie.setImagePath(API_BASE_IMAGEURL + Poster_Sizes.w154 + value.getAsString());
                        }
                        value = element.getAsJsonObject().get("vote_average");
                        if(!value.isJsonNull()){ 
                            popularMovie.setTmdbRating(value.getAsDouble());
                        }
                        //release date
                        value = element.getAsJsonObject().get("release_date");
                        if(!value.isJsonNull()){ 
                            String rlsDateString = value.getAsString();    
                            Date date = new SimpleDateFormat("yyyy-mm-dd").parse(rlsDateString);
                            popularMovie.setReleaseDate(date);
                            String releaseYear = new SimpleDateFormat("yyyy").format(date);
                            if(!releaseYear.isEmpty()){
                                popularMovie.setReleaseYear(Integer.valueOf(releaseYear));
                            }
                        }
                    } catch (ParseException | NumberFormatException ex) {
                        LOG.error("Error while parsing JSon Element: " + element.getAsString(), ex);
                    }
                    searchResults.add(popularMovie);
                    //{"page":1,"results":
                    //[{"backdrop_path":"/xizM09IAYfm8W8U4NIdlqzRjHck.jpg","id":47964,"original_title":"A Good Day to Die Hard","release_date":"2013-02-14","poster_path":"/pjtg4pZE9ItGMNhwB5JDObk8TA4.jpg","title":"Stirb langsam - Ein guter Tag zum Sterben","vote_average":5.8,"vote_count":0},
                    //{..}
                    //],"total_pages":25,"total_results":490}
                }
            }
        }
        return searchResults;
    }

    /**
     * Get the list of top rated movies. By default, this list will only include movies that have 10 or more votes. This list refreshes every day. <br/>
     * <b>Bold</b> parameters are <b>required</b>.
     * 
     * @param page
     * @param language ISO 639-1 code.
     * @return A list of top rated movies
     * @param maxResults the maximum number of results
     */
    public List<TMDBVideoFile> findTopRatedMovies(String page, String language, int maxResults){
        paramsMap.clear();
        //optional parameters
        if(page != null){
            paramsMap.put("page", page);
        }
        if(language != null){
            paramsMap.put("language", language);
        }
        
        String url = buildUrl(API_MOVIE + "/" + "top_rated");
        JsonElement response = callAPI(url);     
        
        //handle response
        List<TMDBVideoFile> searchResults = new LinkedList<TMDBVideoFile>();
        if(!response.isJsonNull()){
            JsonElement key = response.getAsJsonObject().get("results");
            if(key.isJsonArray()){
                JsonArray elements = key.getAsJsonArray();
                int arraySize = elements.size();
                if(arraySize > maxResults){
                    arraySize = maxResults;
                }
                for(int i = 0; i < arraySize; i++){
                    JsonElement element = elements.get(i);
                    String title = element.getAsJsonObject().get("title").getAsString();         
                    TMDBVideoFile topRatedMovie = new TMDBVideoFile(title);
                    try {
                        JsonElement value;
                        value = element.getAsJsonObject().get("backdrop_path");
                        if(!value.isJsonNull()){  
                            topRatedMovie.setBackdropPath(API_BASE_IMAGEURL + Backdrop_Sizes.w300 + value.getAsString());
                        }
                        value = element.getAsJsonObject().get("id");
                        if(!value.isJsonNull()){  
                            topRatedMovie.setTmdbID(value.getAsString());
                        }
                        value = element.getAsJsonObject().get("original_title");
                        if(!value.isJsonNull()){  
                            topRatedMovie.setOriginalTitle(value.getAsString());
                        }
                        value = element.getAsJsonObject().get("poster_path");
                        if(!value.isJsonNull()){  
                            topRatedMovie.setImagePath(API_BASE_IMAGEURL + Poster_Sizes.w154 + value.getAsString());
                        }
                        value = element.getAsJsonObject().get("vote_average");
                        if(!value.isJsonNull()){ 
                            topRatedMovie.setTmdbRating(value.getAsDouble());
                        }
                        //release date
                        value = element.getAsJsonObject().get("release_date");
                        if(!value.isJsonNull()){ 
                            String rlsDateString = value.getAsString();    
                            Date date = new SimpleDateFormat("yyyy-mm-dd").parse(rlsDateString);
                            topRatedMovie.setReleaseDate(date);
                            String releaseYear = new SimpleDateFormat("yyyy").format(date);
                            if(!releaseYear.isEmpty()){
                                topRatedMovie.setReleaseYear(Integer.valueOf(releaseYear));
                            }
                        }
                    } catch (ParseException | NumberFormatException ex) {
                        LOG.error("Error while parsing JSon Element: " + element.getAsString(), ex);
                    }
                    searchResults.add(topRatedMovie);
                    //{"page":1,"results":
                    //[{"backdrop_path":"/xBKGJQsAIeweesB79KC89FpBrVr.jpg","id":278,"original_title":"The Shawshank Redemption","release_date":"1994-09-14","poster_path":"/f0YDB0jZOwSIMcrEU1Vo8eh2Dh8.jpg","title":"Die Verurteilten","vote_average":9.1,"vote_count":211},
                    //{...}
                    //],"total_pages":78,"total_results":1556}
                }
            }
        }
        return searchResults;
    }
}
