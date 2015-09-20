/**
 * Copyright (c) 2010-2015 Bryan Beck.
 * All rights reserved.
 * 
 * This project is licensed under LGPL v2.1.
 * See jMovieManager-license.txt for details.
 * 
 */
package jmm.xml;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import jmm.data.collection.MediaCollection;
import jmm.persist.PersistingManager;
import jmm.utils.Settings;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;
import org.xml.sax.SAXException;

/**
 * Manager class for import and export operations
 * 
 * @author Bryan Beck
 * @since 23.09.2013
 */
public enum XMLManager {
    /**
     * Singleton instance
     */
    INSTANCE;
    
    /** Logger. */
    private static final Logger LOG = LoggerFactory.logger(XMLManager.class);
    
    /**
     * Exports the database into a new xml file. <br/>
     * The filepath is given by  {@see Settings#getXMLPath() }.
     * 
     * @return true If the export was successfull.<br/>false Otherwise
     */
    public boolean exportToXML() {
        return exportToXML(Settings.getInstance().getXMLPath());
    }
    
    /**
     * Exports the database into a new xml file.
     * 
     * @param filePath The path to the xml file.
     * @see PersistingManager#exportToXML() 
     * @return true If the export was successfull.<br/>false Otherwise
     */
    public boolean exportToXML(String filePath){
        try {
            XMLParser.saveAsXML(filePath);
        } catch (ParserConfigurationException | TransformerException | IOException | URISyntaxException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            LOG.error("Error occured during the xml export.", ex);
            return false;
        }
        return true;
    }
    
    /**
     * Exports the database into a new xml file.
     * 
     * @param filePath The path to the xml file.
     * @param savePictures True if the pictures should be exported.<br/>false otherwise
     * @see PersistingManager#exportToXML(java.lang.String)  
     * @return true If the export was successfull.<br/>false Otherwise
     */
    public boolean exportToXML(String filePath, boolean savePictures){
        try {
            XMLParser.saveAsXML(filePath, savePictures);
        } catch (ParserConfigurationException | TransformerException | IOException | URISyntaxException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            LOG.error("Error occured during the xml export.", ex);
            return false;
        }
        return true;
    }
    
    /**
     * Methode zum Öffnen des Projekts in einer XML Datei
     *
     * @param savePath - Der Pfad, zum öffnen der XML
     * @return true - XML Datei wurde erfolgreich geöffnet<br/>
     * false - XML Datei konnte nicht geöffnet werden
     */
    public boolean importFromXML(String savePath) {
        try {
            XMLParser.openXMLSAX(savePath);
        } catch (IOException | SAXException | ParserConfigurationException | NumberFormatException ex) {
            LOG.error("Error occured during the xml import.", ex);
            return false;
        }

        Settings.getInstance().setXMLPath(savePath);
        PersistingManager.INSTANCE.setDocumentChanged(true);
        return true;
    }
    
    /**
     * Trys to restore the database, using the backup xml file.
     *
     * @param hsqlDBPath The path to the Database files
     * @return true if the database was successfully restored.<br/> 
     * false otherwise
     */
    public boolean restore(String hsqlDBPath){
        boolean success = false;

        final File backupXML = new File(hsqlDBPath + ".xml");
        if(backupXML.exists() && backupXML.isFile()){
            MediaCollection.clearCollectionMap();
            if(importFromXML(backupXML.getAbsolutePath())){
                //Delete backup file
//                backupXML.delete();
                success = true;
            }
        }
        return success;
    }
}
