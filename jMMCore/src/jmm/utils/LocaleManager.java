/**
 * Copyright (c) 2010-2015 Bryan Beck.
 * All rights reserved.
 * 
 * This project is licensed under LGPL v2.1.
 * See jMovieManager-license.txt for details.
 * 
 */
package jmm.utils;

import java.util.Locale;
import java.util.Observable;
import java.util.ResourceBundle;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;

/**
 * LocaleManager with observer pattern to notify all GUIs, if the locale was updated
 * 
 * @author Bryan Beck
 * @since 13.02.2013
 */
public class LocaleManager extends Observable {
    /** Logger. */
    private static final Logger LOG = LoggerFactory.logger(LocaleManager.class);

    //Singleton instance of LocaleManager
    private static LocaleManager instance;
    
    private ResourceBundle genreKeyAndCodecBundle;  
    
    /**
     * Creates an singleton instance of LocaleManager
     */
    private LocaleManager(){
        String iso2Language = Settings.getInstance().getPrefLanguage();
        
        boolean languageSet = false;
        for(SupportedLanguages language: SupportedLanguages.values()){
            if(iso2Language.equals(language.getLocale().getLanguage())){
                Locale.setDefault(language.getLocale());
                languageSet = true;
                break;
            }
        }
        // set default language
        if(!languageSet){
            Locale.setDefault(SupportedLanguages.en_US.getLocale());
        }
        
        updateGenreKeyAndCodecBundle();
    }
    
    /**
     * Returns an singleton instance of the localeManager
     * 
     * @return An instance of localeManager
     */
    public static LocaleManager getInstance(){
        if(instance == null){
            instance = new LocaleManager();
        }
        return instance;
    }

    /**
     * @return the genreKeyAndCodecBundle
     */
    public ResourceBundle getGenreKeyAndCodecBundle() {
        return genreKeyAndCodecBundle;
    }
    
    /**
     * Updates the resource bundle for genre keys, audio and video codecs.<br/>
     * i.e. If the Default language has changed
     */
    private void updateGenreKeyAndCodecBundle() {
        this.genreKeyAndCodecBundle = ResourceBundle.getBundle("jmm.resources.JMM"); 
    }

    /**
     * @return the currentLocale
     */
    public Locale getCurrentLocale() {
        return Locale.getDefault();
    }

    /**
     * @param currentLocale the currentLocale to set
     */
    public void setCurrentLocale(Locale currentLocale) {
        Locale.setDefault(currentLocale);
        Settings.getInstance().setPrefLanguage(currentLocale.getLanguage());
        updateGenreKeyAndCodecBundle();
        //update all GUIs
        this.setChanged();
        notifyObservers(currentLocale);
    }
    
    /**
     * Determines the key in the MovieManager propertie file by a given value and a locale
     * 
     * @param locale The locale of the String
     * @param value The value to look for the key
     * @param keyPrefix The prefix for the key
     * @return String the key, <br/> null if no key was found
     */    
    private static String determineKeyByLocale(Locale locale, String value, String keyPrefix){
        ResourceBundle bundle = ResourceBundle.getBundle("jmm.resources.JMM", locale); 
        int i = 1;
        String key = keyPrefix + ".0" + i;       
        
        while(bundle.containsKey(key)){            
            if(value.equals(bundle.getString(key))){
                return key;
            }
            i++;
            if(i < 10){
                key = keyPrefix + ".0" + i;
            }else{
                key = keyPrefix + "." + i;
            }
        }
        //Für "keine Angabe" bzw. "undefined"
        key = "Value.empty";
        if(value.equals(bundle.getString(key))){
            return key;
        }
        return null;         
    }
   
     /**
     * Determines the genre key in the MovieManager propertie file by a given genre value and a locale
     * 
     * @param locale The locale of the genre String
     * @param genre The genre to look for the key
     * @return String the genre key, <br/> null if no key was found
     */
    public static String determineGenreKeyByLocale(Locale locale, String genre){
        return determineKeyByLocale(locale, genre, "Movie.genres");
    }
    
     /**
     * @see LocaleManager#determineGenreKeyByLocale(java.util.Locale, java.lang.String) 
     */
    public String determineGenreKeyByLocale(String genre){
        return determineKeyByLocale(getCurrentLocale(), genre, "Movie.genres");
    }
    
     /**
     * Determines the videoCodec key in the MovieManager propertie file by a given videoCodec value and a locale
     * 
     * @param locale The locale of the genre String
     * @param videoCodec The videoCodec to look for the key
     * @return String the videoCodec key, <br/> null if no key was found
     */
    public static String determineVideoCodecKeyByLocale(Locale locale, String videoCodec){
        return determineKeyByLocale(locale, videoCodec, "Video.codec");        
    }  
    
     /**
     * @see LocaleManager#determineVideoCodecKeyByLocale(java.util.Locale, java.lang.String) 
     */
    public String determineVideoCodecKeyByLocale(String videoCodec){
        return determineKeyByLocale(getCurrentLocale(), videoCodec, "Video.codec");        
    }    
    
     /**
     * Determines the source key in the MovieManager propertie file by a given source value and a locale
     * 
     * @param locale The locale of the genre String
     * @param source The source to look for the key
     * @return String the source key, <br/> null if no key was found
     */
    public static String determineSourceKeyByLocale(Locale locale, String source){
        return determineKeyByLocale(locale, source, "Source");        
    } 
    
     /**
     * @see LocaleManager#determineSourceKeyByLocale(java.util.Locale, java.lang.String) 
     */
    public String determineSourceKeyByLocale(String source){
        return determineKeyByLocale(getCurrentLocale(), source, "Source");        
    }    
    
     /**
     * Determines the audioCodec key in the MovieManager propertie file by a given audioCodec value and a locale
     * 
     * @param locale The locale of the genre String
     * @param audioCodec The audioCodec to look for the key
     * @return String the audioCodec key, <br/> null if no key was found
     */
    public static String determineAudioCodecKeyByLocale(Locale locale, String audioCodec){
        return determineKeyByLocale(locale, audioCodec, "Audio.codec");        
    }     
        
     /**
     * @see LocaleManager#determineAudioCodecKeyByLocale(java.util.Locale, java.lang.String) 
     */
    public String determineAudioCodecKeyByLocale(String audioCodec){
        return determineKeyByLocale(getCurrentLocale(), audioCodec, "Audio.codec");        
    }     
    
    /**
     * Internal class who provides all supported languages.
     */
    public enum SupportedLanguages{
        en_US(Locale.US),
        de_DE(Locale.GERMANY),
        pt_BR(new Locale("pt", "BR")),
        es_ESP(new Locale("es", "ESP")),
        it_IT(Locale.ITALY);
        
        private final Locale locale;
        
        private SupportedLanguages(Locale locale){
            this.locale = locale;
        }

        /**
         * @return the locale
         */
        public Locale getLocale() {
            return locale;
        }
    }
}
