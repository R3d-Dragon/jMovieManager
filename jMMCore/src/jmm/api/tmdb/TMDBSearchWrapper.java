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
import java.util.LinkedList;
import java.util.List;
import static jmm.api.tmdb.TMDBWrapper.callAPI;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;

/**
 * Wrapper class for TMDB Search API calls
 * 
 * @author Bryan Beck
 * @since 22.02.2013
 */
public class TMDBSearchWrapper extends TMDBWrapper{
    /** Logger. */
    private static final Logger LOG = LoggerFactory.logger(TMDBSearchWrapper.class);
       
    /**
     * Creates a new TMDBMovieWrapper
     */
    public TMDBSearchWrapper(){
        super();
    }
    
    /**
     * Search for movies by title. <br/>
     * <b>Bold</b> parameters are <b>required</b>.
     * 
     * @param query <b>the movie title</b>
     * @param page
     * @param language ISO 639-1 code.
     * @param include_adult Toggle the inclusion of adult titles. Expected value is: true or false
     * @param year Filter results to only include this value.
     * @param maxResults the maximum number of results
     */
    public List<TMDBVideoFile> searchMovie(String query, String page, String language, Boolean include_adult, String year, int maxResults){
        paramsMap.clear(); 
        //requiered parameters
        if(query == null){
            throw new NullPointerException("Param: " + "query" + "must not be null");
        }
        paramsMap.put(PARAM_QUERY, query);
        //optional parameters
        if(page != null){
            paramsMap.put(PARAM_PAGE, page);
        }
        if(language != null){
            paramsMap.put(PARAM_LANGUAGE, language);
        }
        if(include_adult != null){
            paramsMap.put(PARAM_ADULT, include_adult.toString());
        }
        if(year != null){
            paramsMap.put(PARAM_YEAR, year);
        }

        String url = buildUrl(API_SEARCH + API_MOVIE);
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
                    if(element == null || element.isJsonNull()){
                        continue;
                    }
                    String title = element.getAsJsonObject().get("title").getAsString();         
                    TMDBVideoFile file = new TMDBVideoFile(title);
                    JsonElement value;
                    value = element.getAsJsonObject().get("adult");
                    if(!value.isJsonNull()){                      
                        file.setAdult(value.getAsBoolean());
                    }
                    value = element.getAsJsonObject().get("backdrop_path");
                    if(!value.isJsonNull()){  
                        file.setBackdropPath(API_BASE_IMAGEURL + Backdrop_Sizes.w300 + value.getAsString());
                    }
                    value = element.getAsJsonObject().get("id");
                    if(!value.isJsonNull()){  
                        file.setTmdbID(value.getAsString());
                    }
                    value = element.getAsJsonObject().get("original_title");
                    if(!value.isJsonNull()){  
                        file.setOriginalTitle(value.getAsString());
                    }
                    value = element.getAsJsonObject().get("poster_path");
                    if(!value.isJsonNull()){  
                        file.setImagePath(API_BASE_IMAGEURL + Poster_Sizes.w92 + value.getAsString());
                    }
                    value = element.getAsJsonObject().get("vote_average");
                    if(!value.isJsonNull()){ 
                        file.setTmdbRating(value.getAsDouble());
                    }
                    //release date
                    value = element.getAsJsonObject().get("release_date");
                    if(!value.isJsonNull()){ 
                        String rlsDateString = value.getAsString(); 
                        try{
                            if(!rlsDateString.isEmpty()){
                                Date date = new SimpleDateFormat("yyyy-mm-dd").parse(rlsDateString);
                                file.setReleaseDate(date);
                                String releaseYear = new SimpleDateFormat("yyyy").format(date);
                                if(!releaseYear.isEmpty()){
                                    file.setReleaseYear(Integer.valueOf(releaseYear));
                                }
                            }
                        } catch (ParseException | NumberFormatException ex) {
                            LOG.warn("Release Year " + rlsDateString + "cannot be parsed into a valid format for title: " + title + ".", ex);
                            file.setReleaseYear(0);
                        }                            
                    }
                    searchResults.add(file);
                    //{"adult":false,"backdrop_path":"/eZxahf9ATsv3oQPtvZPqSZkAO7V.jpg","id":12444,"original_title":"Harry Potter and the Deathly Hallows: Part 1",
                    //"release_date":"2010-11-19","poster_path":"/3ssNt5cCFYGne7OrBFkFFUVq8Cx.jpg","popularity":330304.17,
                    //"title":"Harry Potter und die Heiligtümer des Todes - Teil 1","vote_average":8.3,"vote_count":75}                
                }
            }
        }
        return searchResults;
    }
}
