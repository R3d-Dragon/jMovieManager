/**
 * Copyright (c) 2010-2015 Bryan Beck.
 * All rights reserved.
 * 
 * This project is licensed under LGPL v2.1.
 * See jMovieManager-license.txt for details.
 * 
 */
package jmm.api.tmdb;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import jmm.api.JMMAPI;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;

/**
 * Wrapper class for TMDB API calls
 * 
 * @author Bryan Beck
 * @since 20.02.2013
 */
public abstract class TMDBWrapper extends JMMAPI{
    /** Logger. */
    private static final Logger LOG = LoggerFactory.logger(TMDBWrapper.class);
    
    //URL to call
    private static final String JMM_TMDB_API = JMM_BASEURL + "/jMM_TMDB_API.php";
    
    private static final String API_BASEURL = "http://api.themoviedb.org"; //"http://themoviedb.apiary.io";
    private static final String API_VERSION = "/3";
   
    protected static final String API_BASE_IMAGEURL = "http://image.tmdb.org/t/p/"; //"http://d3gtl9l2a4fn1j.cloudfront.net/t/p/";
    //protected static final String API_BASE_IMAGEURL = "http://cf2.imgobject.com/t/p/";
    
    protected static final String API_MOVIE = "/movie";
    protected static final String API_PERSON = "/person";
    protected static final String API_COMPANY = "/company";
    protected static final String API_GENRE = "/genre";
//    protected static final String API_AUTH = "/authentication";
//    protected static final String API_COLLECTION = "/collection";
//    protected static final String BASE_ACCOUNT = "/account";
    protected static final String API_SEARCH = "/search";
//    protected static final String API_LIST = "/list";
//    protected static final String API_KEYWORD = "/keyword";
       
    //TMDB Parameters
    protected static final String PARAM_APPEND = "append_to_response";
    protected static final String PARAM_ADULT = "include_adult";
//    protected static final String PARAM_API_KEY = "api_key";
    protected static final String PARAM_COUNTRY = "country";
    protected static final String PARAM_FAVORITE = "favorite";
    protected static final String PARAM_ID = "id";
    protected static final String PARAM_LANGUAGE = "language";
//    protected static final String PARAM_MOVIE_ID = "movie_id";
    protected static final String PARAM_MOVIE_WATCHLIST = "movie_watchlist";
    protected static final String PARAM_PAGE = "page";
    protected static final String PARAM_QUERY = "query";
//    protected static final String PARAM_SESSION = "session_id";
//    protected static final String PARAM_TOKEN = "request_token";
    protected static final String PARAM_VALUE = "value";
    protected static final String PARAM_YEAR = "year";
    
    //parameter map for get requests   
    protected HashMap<String, String> paramsMap;
    //TODO: Implement rest of the API (including v3)
    //30 requests every 10 seconds per IP
    //Maximum 20 simultaneous connections PER IP, according to http://help.themoviedb.org/discussions/problems/14-api-requests-quotas
    
    public static enum Poster_Sizes{
        w90,    //inofficial
        w92,
        w154,
        w185,
        w300,   //inofficial
        w342,
        w500,
        original
    };
    
    public static enum Backdrop_Sizes{
      w300,
      w780,
      w1280,
      original
    };
    
    public static enum Profile_Sizes{
        w45,
        w185,
        h632,
        original
    };  
    
    public static enum Logo_Sizes{
        w45,
        w92,
        w154,
        w185,
        w300,
        w500,
        original
    }; 
    
    /**
     * Creates a new TMDBWrapper
     */
    public TMDBWrapper(){
        paramsMap = new HashMap<>();
    }
        
    /**
     * Builds the url to cal, including all params from the paramsMap
     * 
     * @param apiFunction the TMDB api function to call
     * 
     * @return the complete url
     */
    protected String buildUrl(String apiFunction){
        StringBuilder requestUrlBuilder = new StringBuilder(JMM_TMDB_API);
        requestUrlBuilder.append("?").append("url=").append(API_BASEURL).append(API_VERSION).append(apiFunction);
        for(String key: paramsMap.keySet()){
            String value = paramsMap.get(key);
            value = value.replaceAll(" ", "%20");
            requestUrlBuilder.append("&").append(key).append("=").append(value);
        }
        return requestUrlBuilder.toString();
    }
    
    /**
     * Calls the url and returns the response as a JsonElement,
     * containing all return parameters in key, value pairs
     * 
     * @param tmdbURL the url to open (including all parameters)
     * @return The JsonElement <br/> null, if there was en error during API call
     */
    protected static JsonElement callAPI(String tmdbURL){
        if(tmdbURL == null){
            throw new NullPointerException();
        }

        StringBuilder responseBuilder = new StringBuilder();
        String line;
        URLConnection con;
        BufferedReader br = null;
        JsonElement root = JsonNull.INSTANCE;

        try { 
            getConCount().acquire();
            con = new URL(tmdbURL).openConnection();
            br = new BufferedReader(new InputStreamReader(con.getInputStream(), CHARSET_UTF));
            //Solange der Reader bereit zum lesen ist(und das Ende des Dokuments noch nicht erreicht)
            while ((line = br.readLine()) != null) {
                responseBuilder.append(line);
            }
            root = new JsonParser().parse(responseBuilder.toString());
        } catch (JsonSyntaxException | IOException | InterruptedException ex) {
            String cause = ex.toString().split(":")[0];
            if(cause.equals("java.net.UnknownHostException")){
                LOG.warn("Please check your internet connectivity.");
            }else{
                LOG.error("Error while calling TMDB API: " + escapeAPIKey(ex));
            }
        } finally{
            //Schliese den Reader
            if(br != null){
                try {
                    br.close();
                } catch (IOException ex) {
                    LOG.error("Reader cannot be closed.", ex);
                }
            }
            getConCount().release();
        }
//        System.out.println(responseBuilder.toString());
        //Create JsonElement and parse the response String
        return root;
    }  
    
    private static String escapeAPIKey(Exception ex){
        int iStart = ex.toString().indexOf("api_key=");
        int iStop = ex.toString().indexOf("&", iStart+1);
        if((iStart > -1) && (iStop > -1)){
            return ex.toString().substring(0, iStart) + ex.toString().substring(iStop+1);
        }
        
        return ex.toString(); //("api_key=(0-9a-z)* &{0,1}", "");
    }
    
    /**
     * Determines, if the TMDB API is accessable via jMovieManager or not
     * This method is designed as a singleton. If you want to check the API availability multiple times, you have to restart your program.
     * @return true if the API is enabled and accessable<br/> false otherwise
     */
    @Override
    public boolean isAPIenabled(){
        if(apiEnabled == null){
            apiEnabled = false;
            URLConnection con;
            final String baseURL = JMM_BASEURL + "/newconfig.txt";

            try{
                con = new URL(baseURL).openConnection();
                //Fake den Firefox, um HTTP Anfragen senden zu dürfen
                con.addRequestProperty("user-agent", "Firefox");
                try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), CHARSET_UTF))) {
                    String line;
                    while((line = br.readLine()) != null){
                        if(line.contains("TMDB_API_ENABLED")){
                            if(line.contains("true")){
                                apiEnabled = true;
                            }
                            break;
                        }
                    }
                    br.close();
                }
            }catch(FileNotFoundException e){
                LOG.error("File not found.", e);
            }catch(IOException e){
                LOG.warn("Please check your internet connectivity.", e);
            }
        }
        return apiEnabled;
    }
}
