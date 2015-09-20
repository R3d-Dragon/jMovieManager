/**
 * Copyright (c) 2010-2015 Bryan Beck.
 * All rights reserved.
 * 
 * This project is licensed under LGPL v2.1.
 * See jMovieManager-license.txt for details.
 * 
 */
package jmm.utils;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URLDecoder;
import java.util.HashSet;
import java.util.Observable;
import java.util.Set;
import org.apache.log4j.PropertyConfigurator;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;

/**
 * Alle Einstellungen, die gespeichert werden sollen sind in der Klasse zu finden.
 * 
 * Kommuniziert direkt mit der GUI.
 * 
 * @author Bryan Beck
 * @since 09.05.2010
 */
public final class Settings extends Observable{    
    /** Logger. */
    private static final Logger LOG = LoggerFactory.logger(Settings.class);
    
    public static Settings instance;
    private static final String settingsFilePath = "jmm.properties";
    public static final double currentVersion = 1.50;
    //General settings
    private boolean exportPicturesIntoXML;
    private boolean savePictures;       //stores the pictures into at the database path
    private String prefLanguage;
    private boolean checkForUpdateOnStartup;
    //Display
    private boolean displayWatchedIcon;
    private boolean overlapFSKImage;
    
    //recently opened file
    private String recentFile;
    //SavePath for XML Files
    private String xmlPath;  
    private String hsqlDbPath;
    //Screen resolution on startup
    private int startupDisplaySetting;
    public static final int displaySize_DEFAULT = 0;
    public static final int displaySize_MAXIMIZED = 1;
    
    //releaseCalendar
    private boolean showUpcoming;
    private boolean showNowPlaying;
    private boolean showTopRated;
    private boolean showPopular;
    /**
     * Redirect on mouse click
     */
    public static final int REDIRECT_TMDB = 0;
    public static final int REDIRECT_IMDB = 1;
    public static final int REDIRECT_AMAZON = 2; 
    private int redirectTo;
               
    //Logging unexspected errors and report them
    public static final String emailTo = "bugs@jmoviemanager.de";
    public static final String emailFrom = "report@jmoviemanager.de";
    public static final int maxNumberOfBugReports = 10;
    //Anzahl der bereits verwendeten Bugsreports
    private int numberOfBugReports;
    //Angabe ob bug report ohne GUI fenster automatisch gesendet werden soll
    private boolean sendBugReportAutomatically;
    
    private boolean waringDataStructureShown;
   
    private static String jmmDirPath = "";
    /** The absolute path to the logFile. */
    private static String logFilePath;
    
    //Dynamische Initialisierung des Dateipfades zum jMM Verzeichnis
    static{
        //PropertiesConfigurator is used to configure logger from properties file
	PropertyConfigurator.configure("log4j.properties");
        
        try{
            String decodedFilePath = URLDecoder.decode(Settings.class.getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8");
            File jar = new File(decodedFilePath);
            if(jar.exists()){
                //TODO: refactoring into the gui project. maybe split settings into 2 classes (GUI settings, data settings)
                //Since the client < > server refactoring
                jmmDirPath = jar.getParentFile().getParentFile().getAbsolutePath(); 
//                jmmDirPath = jar.getParentFile().getAbsolutePath();
            }
        }catch(UnsupportedEncodingException e){
            LOG.error("Cannot decode relative filepath to the jMM application directoy.", e);
        }
    }
        
    //TODO: Make them unique per collection and not global
    public static Set<String> customAttributeKeys;
    
    /**
     * private Construtor for default settings
     */
    private Settings(){
        customAttributeKeys = new HashSet();
        this.setExportPicturesIntoXML(false);
        this.setSavePictures(true);
        this.setPrefLanguage(LocaleManager.SupportedLanguages.en_US.getLocale().getLanguage());
        this.setCheckForUpdateOnStartup(true);
        this.setDisplayWatchedIcon(true);
        this.setOverlapFSKImage(true);
        this.setRecentFile("");
        this.setXMLPath("");
        this.setHsqlDbPath("");
        this.setStartupDisplaySetting(displaySize_DEFAULT);
        this.setShowUpcoming(true);
        this.setShowNowPlaying(true);
        this.setShowTopRated(true);
        this.setShowPopular(false);
        this.setRedirectTo(REDIRECT_TMDB);
        this.setNumberOfBugReports(0);
        this.setSendBugReportAutomatically(false);
        this.setWaringDataStructureShown(false);
    }

    /**
     * @return the exportPicturesIntoXML
     */
    public boolean isExportPicturesIntoXML() {
        return exportPicturesIntoXML;
    }

    /**
     * @param exportPictures the exportPictures to set
     */
    public void setExportPicturesIntoXML(boolean exportPictures) {
        this.exportPicturesIntoXML = exportPictures;
    }

    /**
     * @return the savePictures
     */
    public boolean isSavePictures() {
        return savePictures;
    }

    /**
     * @param savePictures the savePictures to set
     */
    public void setSavePictures(boolean savePictures) {
        this.savePictures = savePictures;
    }

    /**
     * @return the prefLanguage
     */
    public String getPrefLanguage() {
        return prefLanguage;
    }

    /**
     * @param prefLanguage the prefLanguage to set
     */
    public void setPrefLanguage(String prefLanguage) {
        this.prefLanguage = prefLanguage;
    }

    /**
     * @return the checkForUpdateOnStartup
     */
    public boolean isCheckForUpdateOnStartup() {
        return checkForUpdateOnStartup;
    }

    /**
     * @param checkForUpdateOnStartup the checkForUpdateOnStartup to set
     */
    public void setCheckForUpdateOnStartup(boolean checkForUpdateOnStartup) {
        this.checkForUpdateOnStartup = checkForUpdateOnStartup;
    }

    /**
     * @return the displayWatchedIcon
     */
    public boolean isDisplayWatchedIcon() {
        return displayWatchedIcon;
    }

    /**
     * @param displayWatchedIcon the displayWatchedIcon to set
     */
    public void setDisplayWatchedIcon(boolean displayWatchedIcon) {
        this.displayWatchedIcon = displayWatchedIcon;
    }

    /**
     * @return the overlapFSKImage
     */
    public boolean isOverlapFSKImage() {
        return overlapFSKImage;
    }

    /**
     * @param overlapFSKImage the overlapFSKImage to set
     */
    public void setOverlapFSKImage(boolean overlapFSKImage) {
        this.overlapFSKImage = overlapFSKImage;
    }

    /**
     * @return the recentFile
     */
    public String getRecentFile() {
        return recentFile;
    }

    /**
     * @param recentFile the recentFile to set
     */
    public void setRecentFile(String recentFile) {
        if(recentFile == null){
            this.recentFile = "";
        }
        else{
            this.recentFile = recentFile;
        }
    }

    /**
     * @return the xmlPath
     */
    public String getXMLPath() {
        return xmlPath;
    }

    /**
     * @param savePath the xmlPath to set
     */
    public void setXMLPath(String savePath) {
        if(savePath == null){
            this.xmlPath = "";
        }
        else{
            this.xmlPath = savePath;
        }
    }

    /**
     * @return the hsqlDbPath
     */
    public String getHsqlDbPath() {
        return hsqlDbPath;
    }

    /**
     * @param hsqlDbPath the hsqlDbPath to set
     */
    public void setHsqlDbPath(String hsqlDbPath) {
        if(hsqlDbPath == null){
            this.hsqlDbPath = "";
        }
        else{
            this.hsqlDbPath = hsqlDbPath;
        }
    }

    /**
     * @return the startupDisplaySetting
     */
    public int getStartupDisplaySetting() {
        return startupDisplaySetting;
    }

    /**
     * @param startupDisplaySetting the startupDisplaySetting to set
     */
    public void setStartupDisplaySetting(int startupDisplaySetting) {
        this.startupDisplaySetting = startupDisplaySetting;
    }

    /**
     * @return the showUpcoming
     */
    public boolean isShowUpcoming() {
        return showUpcoming;
    }

    /**
     * @param showUpcoming the showUpcoming to set
     */
    public void setShowUpcoming(boolean showUpcoming) {
        this.showUpcoming = showUpcoming;
    }

    /**
     * @return the showNowPlaying
     */
    public boolean isShowNowPlaying() {
        return showNowPlaying;
    }

    /**
     * @param showNowPlaying the showNowPlaying to set
     */
    public void setShowNowPlaying(boolean showNowPlaying) {
        this.showNowPlaying = showNowPlaying;
    }

    /**
     * @return the showTopRated
     */
    public boolean isShowTopRated() {
        return showTopRated;
    }

    /**
     * @param showTopRated the showTopRated to set
     */
    public void setShowTopRated(boolean showTopRated) {
        this.showTopRated = showTopRated;
    }

    /**
     * @return the showPopular
     */
    public boolean isShowPopular() {
        return showPopular;
    }

    /**
     * @param showPopular the showPopular to set
     */
    public void setShowPopular(boolean showPopular) {
        this.showPopular = showPopular;
    }

    /**
     * @return the redirectTo
     * @see Settings#REDIRECT_TMDB
     * @see Settings#REDIRECT_IMDB
     * @see Settings#REDIRECT_AMAZON
     */
    public int getRedirectTo() {
        return redirectTo;
    }

    /**
     * @param redirectTo the redirectTo to set
     * @see Settings#REDIRECT_TMDB
     * @see Settings#REDIRECT_IMDB
     * @see Settings#REDIRECT_AMAZON
     */
    public void setRedirectTo(int redirectTo) {
        this.redirectTo = redirectTo;
    }

    /**
     * @return the settingsFile
     */
    public static File getSettingsFile() {
        return new File(jmmDirPath, settingsFilePath);
    }
    
    /**
     * @return the logFile
     */
    public static String getLogFile() {
        if(logFilePath == null){
            logFilePath = new File("jMMLog.log").getAbsolutePath();
            LOG.debug("Determined path to Log file: " + logFilePath);
        }
        return logFilePath;
    }

    /**
     * @return the numberOfBugReports
     */
    public int getNumberOfBugReports() {
        return numberOfBugReports;
    }

    /**
     * @param numberOfBugReports the numberOfBugReports to set
     */
    public void setNumberOfBugReports(int numberOfBugReports) {
        this.numberOfBugReports = numberOfBugReports;
    }

    /**
     * @return the sendBugReportAutomatically
     */
    public boolean isSendBugReportAutomatically() {
        return sendBugReportAutomatically;
    }

    /**
     * @param sendBugReportAutomatically the sendBugReportAutomatically to set
     */
    public void setSendBugReportAutomatically(boolean sendBugReportAutomatically) {
        this.sendBugReportAutomatically = sendBugReportAutomatically;
    }

    /**
     * @return the waringDataStructureShown
     */
    public boolean isWaringDataStructureShown() {
        return waringDataStructureShown;
    }

    /**
     * @param waringDataStructureShown the waringDataStructureShown to set
     */
    public void setWaringDataStructureShown(boolean waringDataStructureShown) {
        this.waringDataStructureShown = waringDataStructureShown;
    }
    
    /**
     * To String method for the getCustomAttributeKeys. <br/>
     * Required for serializing.
     * @return All customAttributeKeys as a String
     */
    private String getCustomAttributeKeys(){
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for(String key: customAttributeKeys){
            if(first){
                sb.append(key);
                first = false;
            }else{
                sb.append(",").append(key);
            }
        }
        return sb.toString();
    }
    
    /**
     * To String method for the setCustomAttributeKeys. <br/>
     * Required for serializing.
     * @param All customAttributeKeys as a String
     */    
    private void setCustomAttributeKeys(String customAttributeKey){
        if(customAttributeKey != null){
            for(String key: customAttributeKey.split(",")){
                customAttributeKeys.add(key);
            }
        }
    }
    
   /**
   * Singleton Instanz der Settings Klasse
   */
   public static Settings getInstance(){
       if(instance == null){
           instance = new Settings();
       }
       return instance;
   }
   
   /**
    * @see Observable#notifyObservers()  
    */
    @Override
   public void notifyObservers(){
        this.setChanged();
        super.notifyObservers(instance);
   }
   
   /**
   * Creates or overwrites jmm.properties and saves the current settings.
   * @return true The settings were successfully saved.<br/>false otherwise
   */   
   public boolean save(){
       boolean success = false;
       File propertieFile = getSettingsFile();
       String LINE_SEPARATOR = System.getProperty("line.separator");
       try {
           FileWriter fw = new FileWriter(propertieFile);   //Überschreibe existierende File
           fw.write("#Do not modify this file. This file is used to save settings form jMovieManager.");
           fw.write(System.getProperty("line.separator"));
            //save values by reflection
           for(Method getterMethod: Settings.class.getDeclaredMethods()){
                String getterMethodName = getterMethod.getName();

                if(((getterMethodName.startsWith("get")) || getterMethodName.startsWith("is")) &&  //boolean Werte
                        (getterMethod.getParameterTypes().length == 0) &&                          //keine Parameter
                        (!Modifier.isStatic(getterMethod.getModifiers()))                          //keine Statische Methode
                   ){                      
                    if(getterMethodName.startsWith("get")){
                        getterMethodName = getterMethodName.substring(3);
                    }
                    else if(getterMethodName.startsWith("is")){
                        getterMethodName = getterMethodName.substring(2);
                    }
                    Object getterMethodValue = getterMethod.invoke(instance);
                    
                    if((getterMethodValue != null) && (!getterMethodName.isEmpty())){
                       fw.write(getterMethodName + "=" + getterMethodValue + LINE_SEPARATOR);
                   }
                }
            }                              
           //write file
           fw.flush();
           fw.close();
           success = true;
       } catch (IOException | IllegalAccessException | InvocationTargetException e) {
           LOG.error("Cannot write configuration file (jmm.properties) to the file system.", e);
       }
       return success;
   }
   
   /**
    * Load the default settings from the jmm.properties 
    * 
    * @return true if the settings was successfully load <br/> false otherwise
   */      
   public boolean load() {
       boolean success = false;
       String text;
       int index;
       File file = getSettingsFile();
       if(file.exists() && file.canRead()){
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                while((text = br.readLine()) != null){
                    if(!text.startsWith("#") && (text.contains("="))){
                        index = text.indexOf('='); 
                        //set loaded values by reflection
                        for(Method setterMethod: Settings.class.getDeclaredMethods()){
                            String setterMethodName = setterMethod.getName();
                            if(((setterMethodName.startsWith("set")) || (setterMethodName.startsWith("add"))) && 
                                (setterMethodName.endsWith(text.substring(0, index)))){
                                Object value = text.substring(index+1);
                                //Gehe alle Parameter der Methode durch
                                for(Class parameterClass: setterMethod.getParameterTypes()){
                                    try{
                                        if(parameterClass.equals(double.class)){
                                            setterMethod.invoke(instance, Double.valueOf(value.toString()));
                                        }
                                        else if(parameterClass.equals(int.class)){
                                            setterMethod.invoke(instance, Integer.valueOf(value.toString()));
                                        }
                                        else if(parameterClass.equals(float.class)){
                                            setterMethod.invoke(instance, Float.valueOf(value.toString()));
                                        }
                                        else if(parameterClass.equals(boolean.class)){
                                            setterMethod.invoke(instance, Boolean.valueOf(value.toString()));
                                        }
                                        else if(parameterClass.equals(String.class)){
                                            setterMethod.invoke(instance, value.toString());
                                        }
//                                        else if(parameterClass.equals(Enum.class)){
//                                            Enum.valueOf(parameterClass, text)
//                                            setterMethod.invoke(instance, value.toString());
//                                        }
                                    }catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException iae){
                                        LOG.error("The file format of the settings file (jmm.properties) is invalid.", iae);
                                    }
                                    finally{
                                        break;     //Nur 1 Durchgang, da nur 1 Übergabeparameter bei set & add Methoden
                                    }
                                }
                            }
                        }
                    }
                }
                success = true;
            } catch (IOException | SecurityException ex) {
                LOG.error("Cannot load settings file (jmm.properties).", ex);
                //Load default settings
                //instance = new Settings();
            }  
       }
       
       return success;
   }
}
