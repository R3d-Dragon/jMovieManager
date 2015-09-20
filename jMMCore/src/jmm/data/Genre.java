/**
 * Copyright (c) 2010-2015 Bryan Beck.
 * All rights reserved.
 * 
 * This project is licensed under LGPL v2.1.
 * See jMovieManager-license.txt for details.
 * 
 */
package jmm.data;

import jmm.interfaces.GenreKeysInterface;

/**
 * Class to define genres
 * 
 * @author Bryan Beck
 * @since 24.02.2013
 */
//@Entity
//@Table( name = "GENRE" )
public class Genre implements GenreKeysInterface{
       
//    @Id
//    @GeneratedValue(strategy = GenerationType.TABLE)     //alternativ GenerationType.AUTO
//    private Long uid;
    
//    @Column(nullable=false, unique=true)
    private String genreKey;
    
//    @Column(unique=true)
    private int tmdbID;
    
    //All TMDB Genres as a singleton instance
    public static final Genre empty = new Genre(GENRE_EMPTY, 0);
    public static final Genre action = new Genre(GENRE_ACTION, 28);
    public static final Genre adventure = new Genre(GENRE_ADVENTURE, 12);
    public static final Genre animation = new Genre(GENRE_ANIMATION, 16);
    public static final Genre comedy = new Genre(GENRE_COMEDY, 35);
    public static final Genre crime = new Genre(GENRE_CRIME, 80);
    public static final Genre disaster = new Genre(GENRE_DISASTER, 105);
    public static final Genre documentary = new Genre(GENRE_DOCUMENTARY, 99);
    public static final Genre drama = new Genre(GENRE_DRAMA, 18);
    public static final Genre eastern = new Genre(GENRE_EASTERN, 82);
    public static final Genre erotic = new Genre(GENRE_EROTIC, 2916);
    public static final Genre family = new Genre(GENRE_FAMILY, 10751);
    public static final Genre fan_film = new Genre(GENRE_FAN_FILM, 10750);
    public static final Genre fantasy = new Genre(GENRE_FANTASY, 14);
    public static final Genre film_noir = new Genre(GENRE_FILM_NOIR, 10753);
    public static final Genre foreign = new Genre(GENRE_FOREIGN, 10769);
    public static final Genre history = new Genre(GENRE_HISTORY, 36);
    public static final Genre holiday = new Genre(GENRE_HOLIDAY, 10595);
    public static final Genre horror = new Genre(GENRE_HORROR, 27);
    public static final Genre indie = new Genre(GENRE_INDIE, 10756);
    public static final Genre music = new Genre(GENRE_MUSIC, 10402);
    public static final Genre musical = new Genre(GENRE_MUSICAL, 22);
    public static final Genre mystery = new Genre(GENRE_MYSTERY, 9648);
    public static final Genre neo_noir = new Genre(GENRE_NEO_NOIR, 10754);
    public static final Genre road_movie = new Genre(GENRE_ROAD_MOVIE, 1115);
    public static final Genre romance = new Genre(GENRE_ROMANCE, 10749);
    public static final Genre science_fiction = new Genre(GENRE_SCIENCE_FICTION, 878);
    public static final Genre gshort = new Genre(GENRE_SHORT, 10755);
    public static final Genre sport = new Genre(GENRE_SPORT, 9805);
    public static final Genre sporting_event = new Genre(GENRE_SPORTING_EVENT, 10758);
    public static final Genre sports_film = new Genre(GENRE_SPORTS_FILM, 10757);
    public static final Genre suspense = new Genre(GENRE_SUSPENSE, 10748);
    public static final Genre tv_movie = new Genre(GENRE_TV_MOVIE, 10770);
    public static final Genre thriller = new Genre(GENRE_THRILLER, 53);
    public static final Genre war = new Genre(GENRE_WAR, 10752);
    public static final Genre western = new Genre(GENRE_WESTERN, 37);
    
       
    //All genres as instances
    private static final Genre[] GENRES = 
    {
        Genre.empty,
        Genre.action,
        Genre.adventure,
        Genre.animation,
        Genre.comedy,
        Genre.crime,
        Genre.disaster,
        Genre.documentary,
        Genre.drama,
        Genre.eastern,
        Genre.erotic,
        Genre.family,
        Genre.fan_film,
        Genre.fantasy,
        Genre.film_noir,
        Genre.foreign,
        Genre.history,
        Genre.holiday,
        Genre.horror,
        Genre.indie,
        Genre.music,
        Genre.musical,
        Genre.mystery,
        Genre.neo_noir,
        Genre.road_movie,
        Genre.romance,
        Genre.science_fiction,
        Genre.gshort,
        Genre.sport,
        Genre.sporting_event,
        Genre.sports_film,
        Genre.suspense,
        Genre.tv_movie,
        Genre.thriller,
        Genre.war,
        Genre.western
    }; 
    
    /**
     * creates a new genre
     * 
     * @param genreKey the genre key
     * @param tmdbID the TMDB ID
     */
    private Genre(String genreKey, int tmdbID){
        this.setGenreKey(genreKey);
        this.setTmdbID(tmdbID);
    }
    
//     /**
//     * creates a new genre
//     */   
//    protected Genre(){   
//    }
//
//    /**
//     * @return the uid
//     */
//    @MyXMLAnnotation(isTransient=true)
//    public Long getUid() {
//        return uid;
//    }
//    
//    public void setUid(Long uid) {
//        this.uid = uid;
//    }

    /**
     * @return the genreKey
     */
    public String getGenreKey() {
        return genreKey;
    }

    /**
     * @param genreKey the key to set
     */
    private  void setGenreKey(String genreKey) {
        if(genreKey == null){
            this.genreKey = "";
        }else{
            this.genreKey = genreKey;
        }
    }
    
    /**
     * @return the tmdbID
     */
    public int getTmdbID() {
        return tmdbID;
    }

    /**
     * @param tmdbID the tmdbID to set
     */
    private void setTmdbID(int tmdbID) {
        this.tmdbID = tmdbID;
    }
    
    /**
     * @see Object#toString() 
     * @return The genre key
     */
    @Override
    public String toString(){
        return getGenreKey();
    }
    
    /**
     * returns the genre with the given tmdb id
     * @param tmdbID the id to look for
     * @return the genre with the id <br/> null otherwise
     */
    public static Genre getGenreByTMDBID(int tmdbID){
        Genre result = null;
        for(Genre genre: GENRES){
            if(genre.getTmdbID() == tmdbID){
                result = genre;
                break;
            }
        }
        return result;
    }
    
    /**
     * returns the genre with the given tmdb id
     * @param key the id to look for
     * @return the genre with the id <br/> null otherwise
     */
    public static Genre getGenreByKey(String key){
        Genre result = null;
        if(key != null){
            for(Genre genre: GENRES){
                if(genre.getGenreKey().equalsIgnoreCase(key)){
                    result = genre;
                    break;
                }
            }
        }
        return result;
    }
}
