/**
 * Copyright (c) 2010-2015 Bryan Beck.
 * All rights reserved.
 * 
 * This project is licensed under LGPL v2.1.
 * See jMovieManager-license.txt for details.
 * 
 */
package jmm.api.thetvdb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import jmm.api.JMMAPI;
import jmm.data.Actor;
import jmm.data.VideoFile;
import jmm.interfaces.RegexInterface;
import jmm.utils.LocaleManager;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLFilterImpl;

/**
 * Wrapper class for TMDB API calls
 * 
 * @author Bryan Beck
 * @since 07.03.2013
 */
public class TheTVDB extends JMMAPI{
    /** Logger. */
    private static final Logger LOG = LoggerFactory.logger(TheTVDB.class);
    
    //URL to call
    private static final String JMM_THETVDB_API = JMM_BASEURL + "/jMM_THETVDB_API.php";
//    http://www.thetvdb.com/api/97C6F9D1DE117054/mirrors.xml
//    http://thetvdb.com/api/97C6F9D1DE117054/languages.xml
    //http://www.thetvdb.com/api/GetSeries.php?seriesname=lost

//Alle Details:
//http://www.thetvdb.com/api/97C6F9D1DE117054/series/73739/all/de.xml
//Einzeln:
//http://www.thetvdb.com/api/97C6F9D1DE117054/series/73739/banners.xml
//http://www.thetvdb.com/api/97C6F9D1DE117054/series/73739/actors.xml
//http://www.thetvdb.com/api/97C6F9D1DE117054/series/73739/de.xml
//Grafiken:
//http://www.thetvdb.com/banners/ graphical/73739-g4.jpg
//http://www.thetvdb.com/banners/ fanart/original/73739-34.jpg
//http://www.thetvdb.com/banners/ posters/73739-1.jpg
//http://www.thetvdb.com/banners/ episodes/73739/348495.jpg
    
    private static final String API_BASEURL = "http://thetvdb.com"; //"http://www.thetvdb.com/";  
    private static final String API_IMAGE = "/banners/";
    
    private static final String API = "/api";
    private static final String API_FIND_SERIE = "/GetSeries.php";
    private static final String API_SERIE_DETAILS = "/series";
    //{api_key}
    private static final String API_SERIE_DETAILS_ALL = "/all";
    private static final String API_SERIE_DETAILS_BANNERS = "/banners.xml";
    private static final String API_SERIE_DETAILS_ACTORS = "/actors.xml";
    
    
    protected static final String PARAM_LANGUAGE = "language";
    protected static final String PARAM_TITLE = "seriesname";
    
    //parameter map for get requests   
    protected HashMap<String, String> paramsMap;
    
    /**
     * Search for series by title. <br/>
     * <b>Bold</b> parameters are <b>required</b>.
     * 
     * @param title <b>the series title</b>
     * @param language ISO 639-1 code.
     * @param maxResults the maximum number of results
     * @return A list of TheTVDB series
     */
    public List<TheTVDBSeries> searchSerie(String title, String language, int maxResults){
        //http://www.thetvdb.com/api/GetSeries.php?seriesname=lost&language=de
        paramsMap.clear(); 
        
        //requiered parameters
        if(title == null){
            throw new NullPointerException("Param: " + "title" + "must not be null");
        }
        paramsMap.put(PARAM_TITLE, title);
        //optional parameters
        if(language != null){
            paramsMap.put(PARAM_LANGUAGE, language);
        }
        

        String url = buildUrl(API_BASEURL + API + API_FIND_SERIE);
        String xmlRespone = callAPI(url);
        
        return openXMLSAX(xmlRespone, maxResults);
    }
    
     /**
     * Get the basic movie information for a specific serie id. <br/>
     * <b>Bold</b> parameters are <b>required</b>.
     * 
     * @param id <b>the movie id</b>
     * @param language ISO 639-1 code.
     * @return A TheTVDB serie
     */
    public TheTVDBSeries findSerie(String id, String language){
        //http://www.thetvdb.com/api/97C6F9D1DE117054/series/73739/all/de.xml
        paramsMap.clear();
        //requiered parameters
        if(id == null){
            throw new NullPointerException("Param: " + "id" + "must not be null");
        }
        //optional parameters
//        if(language != null){
//            paramsMap.put(PARAM_LANGUAGE, language);
//        }
        
        String url = buildUrl(API_BASEURL + API + "/api_key" + API_SERIE_DETAILS + "/" + id + API_SERIE_DETAILS_ALL + "/");
        if(language != null){
            url += language + ".xml";
        }else{
            url += "en" + ".xml";
        }
         
        String xmlRespone = callAPI(url);
        List<TheTVDBSeries> results = openXMLSAX(xmlRespone, 999);
        if(results != null && !results.isEmpty()){
            return results.get(0);
        }
        return null;
    }
    
    /**
     * Creates a new TMDBWrapper
     */
    public TheTVDB(){
        paramsMap = new HashMap<>();
    }
            
    /**
     * Builds the url to cal, including all params from the paramsMap
     * 
     * @param apiurl the TMDB api function to call
     * 
     * @return the complete url
     */
    protected String buildUrl(String apiurl){
        StringBuilder requestUrlBuilder = new StringBuilder(JMM_THETVDB_API);
        requestUrlBuilder.append("?").append("url=").append(apiurl);
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
     * @param url the url to open (including all parameters)
     * @return The JsonElement <br/> null, if there was en error during API call
     */
    protected static String callAPI(String url){
        if(url == null){
            throw new NullPointerException();
        }

        StringBuilder responseBuilder = new StringBuilder();
        String line;
        URLConnection con;
        BufferedReader br = null;

        try { 
            getConCount().acquire();
            con = new URL(url).openConnection();
            br = new BufferedReader(new InputStreamReader(con.getInputStream(), CHARSET_UTF));
            //Solange der Reader bereit zum lesen ist(und das Ende des Dokuments noch nicht erreicht)
            while ((line = br.readLine()) != null) {
                responseBuilder.append(line);
            }
        } catch (IOException | InterruptedException ex) {
            String cause = ex.toString().split(":")[0];
            if(cause.equals("java.net.UnknownHostException")){
                LOG.warn("Cannot open connection. \n Please check your internet connectivity.", ex);
            }
            LOG.error("Error occured while executing TheTVDB API.", ex);
        } finally{
            //Schliese den Reader
            if(br != null){
                try {
                    br.close();
                } catch (IOException ex) {
                    LOG.error("Error occured while closing TheTVDB reader.", ex);
                }
            }
            getConCount().release();
        }
//        System.out.println(responseBuilder.toString());
        return responseBuilder.toString();
    }  
    
    /**
     * Determines, if the TheTVDB API is accessable via jMovieManager or not
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
                        if(line.contains("TheTVDB_API_ENABLED")){
                            if(line.contains("true")){
                                apiEnabled = true;
                            }
                            break;
                        }
                    }   
                    br.close();
                }
            }catch(IOException ex){
                LOG.error("Please check your internet connectivity.", ex);
            }
        }
        return apiEnabled;
    }
    
     /**
     * This shows how to parse an XML File using SAX and display the same
     * information from the xml file by implementing the ContentHandler interface
     *
     * Methode zum  öffnen einer vorhandenen XML Datei und generieren der Java Objekte
     * mittels SAX Parser (Sequenziel, read only)
     *
     * @param xmlString The xml string to parse
     */
    private List<TheTVDBSeries> openXMLSAX(String xmlString, int maxResults){
        List<TheTVDBSeries> results = null;
        
        try{
            SAXParserFactory spf = SAXParserFactory.newInstance();
//            spf.setNamespaceAware(true);
            XMLReader xmlReader = spf.newSAXParser().getXMLReader();
            TheTVDBContentHandler handler = new TheTVDBContentHandler();
            xmlReader.setContentHandler(handler);
            //Parse die XML
            StringReader sr = new StringReader(xmlString);
            xmlReader.parse(new InputSource(sr));
            
            if(handler.searchResults.size() > maxResults){
                results = handler.searchResults.subList(0, maxResults);
            }else{
                results = handler.searchResults;
            }
        }catch (ParserConfigurationException | SAXException | IOException ex){
            LOG.error("Error while parsing XML: " + xmlString, ex);
        }
        return results;
    }
    /*
     * <Data>
     * <Series>
     * <seriesid>73739</seriesid>
     * <language>de</language>
     * <SeriesName>Lost</SeriesName>
     * <banner>graphical/73739-g4.jpg</banner>
     * <Overview>Im Bruchteil einer Sekunde gerät das Leben einer Gruppe Reisender aus den Fugen: Ein Flugzeug stürzt auf einer scheinbar einsamen Insel im Pazifik ab - weitab vom Kurs. Nur 48 der Insassen überleben das Inferno. Ihre Hoffnung auf Rettung erweist sich schnell als Illusion und die Überlebenden lernen, sich in der Wildnis zurechtzufinden. Jeder von ihnen hat etwas zu verbergen, jeder eine dunkle Vergangenheit, die langsam ans Tageslicht gelangt. Doch jeder entwickelt auch ungeahnte Stärken, mit denen er den Herausforderungen, die überall lauern, begegnen kann. Denn die gefährlichsten Geheimnisse birgt die Insel selbst.</Overview>
     * <FirstAired>2004-09-22</FirstAired>
     * <IMDB_ID>tt0411008</IMDB_ID>
     * <zap2it_id>SH672362</zap2it_id>
     * <id>73739</id>
     * </Series>
     */
    /**
     * Inner class to parse the xml results
     */
    class TheTVDBContentHandler extends XMLFilterImpl {
        public final List<TheTVDBSeries> searchResults = new LinkedList<>();
        private TheTVDBSeries series;
        
//        private List<TheTVDBEpisode> episodes = new LinkedList<TheTVDBEpisode>();;
        private TheTVDBEpisode episode;
        //String mit dem aktuellen XML Tag, der gerad eingelesen wurde
        private String actualTag = "";
        
        //Attribute
//        private String title;
//        private String imageString;
//
//        private ImageIcon  image;
//
//        private MediaCollection collection;
//        private int tabNumber;
//        private Movie movieToLoad;
//        private Serie serieToLoad;
//        private Season seasonToLoad;
//        private Episode episodeToLoad;
//        private Actor actorToLoad;
//        private Genre genreToLoad;
//
        private int currentActiveTag = NO_TAG;
        private static final int NO_TAG = -1;
//        private int previousActiveTag = -1;
//        private static final int MOVIE_TAG   = 0;
        private static final int SERIE_TAG   = 1;
//        private static final int SEASON_TAG  = 2;
        private static final int EPISODE_TAG = 3;
//        private static final int ACTOR_TAG = 4;
//        private static final int GENRE_TAG = 6;

        /**
         * Geht alle add bzw. set Methoden des Objekts durch, und überprüft, ob es eine Methode mit dem Namen des actualTags gibt. <br/>
         * Wenn ja, wird der Inhalt von gotString in die Methode hineingegeben, mittels Reflektion
         * 
         * @param objectToLoad Das Objekt, das auf add() bzw. set() Methoden überprüft wird
         * @param gotString  Der String, welcher als Parameter in die add() bzw. set() Methode gegeben wird
         */
        private void loadCharsToObject(TheTVDBSeries objectToLoad, String gotString){
            if(actualTag.equalsIgnoreCase("seriesid")){
                objectToLoad.setSeriesID(gotString);
            }
//                    else if(actualTag.equalsIgnoreCase("language")){
////                        objectToLoad.setLa
//                    }
            else if(actualTag.equalsIgnoreCase("SeriesName")){
                objectToLoad.setTitle(gotString);
            }
            else if(actualTag.equalsIgnoreCase("banner") || actualTag.equalsIgnoreCase("poster")){
                objectToLoad.setImagePath(API_BASEURL + API_IMAGE + gotString);
            }
//                    else if(actualTag.equalsIgnoreCase("fanart")){
//                        objectToLoad.setImagePath(API_BASEURL + API_IMAGE + gotString);
//                    }
//            else if(actualTag.equalsIgnoreCase("poster")){
//                objectToLoad.setImagePath(API_BASEURL + API_IMAGE + gotString);
//            }                    
            else if(actualTag.equalsIgnoreCase("Overview")){
                objectToLoad.setDescription(gotString);
            }
            else if(actualTag.equalsIgnoreCase("FirstAired")){
                try{
                    Date date = new SimpleDateFormat("yyyy-mm-dd").parse(gotString);
                    objectToLoad.setReleaseDate(date);
                    String releaseYear = new SimpleDateFormat("yyyy").format(date);
                    if(!releaseYear.isEmpty()){
                        objectToLoad.setReleaseYear(Integer.valueOf(releaseYear));
                    }
                }catch(ParseException | NumberFormatException e){
                    LOG.error("Error while parsing release year: " + gotString, e);
                }
            }
            else if(actualTag.equalsIgnoreCase("IMDB_ID")){
                objectToLoad.setImdbID(gotString);
            }
//                    else if(actualTag.equalsIgnoreCase("zap2it_id")){
////                        objectToLoad.setZ
//                    }
            else if(actualTag.equalsIgnoreCase("id")){
                objectToLoad.setThetvdbID(gotString);
            }
            else if(actualTag.equalsIgnoreCase("Actors")){
                Set<Actor> actors = new HashSet<>();
                for(String actorName: gotString.split("\\|")){
                    if(!actorName.trim().isEmpty()){
                        Actor actor = new Actor(actorName.trim());
                        actors.add(actor);
                    }
                }
                objectToLoad.setActors(actors);
            }
            else if(actualTag.equalsIgnoreCase("ContentRating")){
                String rating = gotString.replaceAll(RegexInterface.REGEX_DIGITS_ONLY, RegexInterface.REPLACEMENT_DIGITS_ONLY);
                if(!rating.isEmpty()){
                    int ratingNr = Integer.valueOf(rating);
                    //Replace by fsk
                    if(ratingNr > 17){
                        objectToLoad.setFsk(VideoFile.FSK.FSK_18);
                    }
                    else if(ratingNr > 15){
                        objectToLoad.setFsk(VideoFile.FSK.FSK_16);
                    }
                    else if(ratingNr > 11){
                        objectToLoad.setFsk(VideoFile.FSK.FSK_12);
                    }
                    else if(ratingNr > 5){
                        objectToLoad.setFsk(VideoFile.FSK.FSK_6);
                    }
                    else{
                        objectToLoad.setFsk(VideoFile.FSK.FSK_UNKNOWN);
                    }
                }                        
            }
            else if(actualTag.equalsIgnoreCase("Genre")){
                Set<String> genreKeys = new HashSet<>();
                for(String enUSGenreName: gotString.split("\\|")){
                    if(!enUSGenreName.trim().isEmpty()){
                        String genreKey = LocaleManager.determineGenreKeyByLocale(LocaleManager.SupportedLanguages.en_US.getLocale(), enUSGenreName.trim());
                         if(genreKey != null){
                             genreKeys.add(genreKey);
                        }
                    }
                }
                objectToLoad.setGenreKeys(genreKeys);
            }
            else if(actualTag.equalsIgnoreCase("Network")){
                objectToLoad.setPublisher(gotString);
            }
            else if(actualTag.equalsIgnoreCase("Rating")){
                objectToLoad.setTheTVDBRating(Double.valueOf(gotString));
                objectToLoad.setOnlineRating(Double.valueOf(gotString));
            }
//                    else if(actualTag.equalsIgnoreCase("Runtime")){
//                        objectToLoad.setPlaytime(Integer.valueOf(gotString));
//                    }
            /*
             * Episode Stuff only
             */
            else if(actualTag.equalsIgnoreCase("Director")){
                objectToLoad.setDirector(gotString);
            }            
            else if(actualTag.equalsIgnoreCase("EpisodeName")){
                objectToLoad.setTitle(gotString);
            }
            else if(actualTag.equalsIgnoreCase("EpisodeNumber")){
                ((TheTVDBEpisode)objectToLoad).setEpisodeNumber(Integer.valueOf(gotString));
            }  
            else if(actualTag.equalsIgnoreCase("GuestStars")){
                //TODO: + Series Authors
                Set<Actor> actors = new HashSet<>();
                for(String actorName: gotString.split("\\|")){
                    if(!actorName.trim().isEmpty()){
                        Actor actor = new Actor(actorName.trim());
                        actors.add(actor);
                    }
                }
                objectToLoad.setActors(actors);
            }  
            else if(actualTag.equalsIgnoreCase("ProductionCode")){
                ((TheTVDBEpisode)objectToLoad).setProductionCode(gotString);
            }  
            else if(actualTag.equalsIgnoreCase("SeasonNumber")){
                ((TheTVDBEpisode)objectToLoad).setSeasonNumber(Integer.valueOf(gotString));
            }  
            else if(actualTag.equalsIgnoreCase("filename")){
                ((TheTVDBEpisode)objectToLoad).setImagePath(API_BASEURL + API_IMAGE + gotString);
            }  
            else if(actualTag.equalsIgnoreCase("seasonid")){
                ((TheTVDBEpisode)objectToLoad).setSeasonID(gotString);
            }  
        }

        /**
         * Filter a character data event.
         *
         * @param ch - Feld von Characktern
         * @param start - Index, ab dem gestartet werden soll
         * @param length - The number of characters to use from the array.
         */
        @Override
        public void characters(char[] ch, int start, int length) {
                String gotString = new String(ch, start, length);
                
                if(currentActiveTag == SERIE_TAG){
                    loadCharsToObject(series, gotString);
                }
                else if(currentActiveTag == EPISODE_TAG){
                    loadCharsToObject(episode, gotString);
                }
        }

       /**
         * Methode wird aufgerufen, wenn ein neues XML Dokument geöffnet wird
         */
        @Override
        public void startDocument(){
        }

       /**
         * Methode wird aufgerufen, wenn das Ende des XML Dokuments erreicht wurde
         */
        @Override
        public void endDocument(){
        }

       /**
         * Filter a start element event.
         * Methode die aufgerufen wird, wenn ein <> Tag eines Elements kommt
         *
         * @param namespaceURI - The element's Namespace URI, or the empty string.
         * @param localName - Der Name des XML Tags, oder leerer String
         * @param qName - The element's qualified (prefixed) name, or the empty string.
         * @param attributes - Das Attribut des Elements
         */
        @Override
        public void startElement(String namespaceURI, String localName,	String qName, Attributes attributes) {
            if(qName.equalsIgnoreCase("Series")){
                series = new TheTVDBSeries("");
                currentActiveTag = SERIE_TAG;
            }
            else if(qName.equalsIgnoreCase("Episode")){
                episode = new TheTVDBEpisode("");
                currentActiveTag = EPISODE_TAG;
            }
            else{
                actualTag = qName;
            }
        }

         /**
          * Filter an end element event.
          * Methode die aufgerufen wird, wenn ein </> Tag eines Elements kommt
          *
          * @param uri - The element's Namespace URI, or the empty string
          * @param localName - The element's local name, or the empty string
          * @param qName - The element's qualified (prefixed) name, or the empty string
         */
        @Override
        public void endElement(String namespaceURI, String localName, String qName) {
            if(qName.equalsIgnoreCase("Series")){
//                series.setEpisodes(episodes);
                searchResults.add(series);
//                series = null;
//                episodes = null;
                currentActiveTag = NO_TAG;
            }
            else if(qName.equalsIgnoreCase("Episode")){
                series.getEpisodes().add(episode);
//                episodes.add(episode);
//                episode = null;
                currentActiveTag = SERIE_TAG;
            }
            //Setze aktuellen Tag zurück
            actualTag = "";
        }
    }    
}

