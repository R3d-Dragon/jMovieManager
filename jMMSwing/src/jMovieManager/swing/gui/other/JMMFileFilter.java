/**
 * Copyright (c) 2010-2015 Bryan Beck.
 * All rights reserved.
 * 
 * This project is licensed under LGPL v2.1.
 * See jMovieManager-license.txt for details.
 * 
 */
package jMovieManager.swing.gui.other;

import java.io.File;
import java.util.ResourceBundle;
import javax.swing.filechooser.FileFilter;
import jmm.interfaces.ExtensionInterface;

/**
 * Contains all FileFilters for JFileChooser Dialogs
 * 
 * @since 20.08.2012
 * @author Bryan Beck
 */
public abstract class JMMFileFilter implements ExtensionInterface{
        
    public static enum FilterType{
        XML,
        DB,
        VIDEO_EXTENSION,
        PICTURE_EXTENSION
    }
    
    public static FileFilter createFileFilter(FilterType fileFilter){
        ResourceBundle bundle;
        final String description;
        final String[] extensions;
    
        bundle = ResourceBundle.getBundle("jMovieManager.swing.resources.MovieManager");
        if(fileFilter == FilterType.XML){
            description = bundle.getString("SaveDialogGUI.jFileChooser.descXML");
            extensions = new String[]{".xml"};
        }
        else if(fileFilter == FilterType.DB){
            description = bundle.getString("SaveDialogGUI.jFileChooser.descDB");
            extensions = new String[]{".script"};
        }
        else if(fileFilter == FilterType.VIDEO_EXTENSION){
            description = bundle.getString("CreateMovieGUI.jFileChooser2.desc");
            extensions = video_Extensions;
        }
        else if(fileFilter == FilterType.PICTURE_EXTENSION){
            description = bundle.getString("AbstractCreateGUI.jFileChooser1.desc");
            extensions = picture_Extensions;
        }
        else{
            return null;
        }
        
        return new FileFilter(){
            @Override
            public boolean accept(File file){
                if(file == null){
                    return false;
                }
                //zeigt alle Ordner an
                if(file.isDirectory()){
                    return true;
                }
                for(String extension: extensions){
                    if(file.getName().toLowerCase().endsWith(extension)){
                        return true;
                    }
                }
                return false;
            }

            @Override
            public String getDescription(){
                //Dieser Text wird unter "Dateityp" angezeigt
                return description;
            }
        };
    }
}
