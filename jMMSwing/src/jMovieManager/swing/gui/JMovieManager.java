/**
 * Copyright (c) 2010-2015 Bryan Beck.
 * All rights reserved.
 * 
 * This project is licensed under LGPL v2.1.
 * See jMovieManager-license.txt for details.
 * 
 */
package jMovieManager.swing.gui;

import jMovieManager.swing.handler.GUIExceptionHandler;
import java.io.File;
import java.util.ResourceBundle;
import jmm.utils.OperatingSystem;
import jmm.utils.Settings;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;

/**
 *
 * @author Bryan Beck
 * @since 01.10.2010
 */
public class JMovieManager {
    /** Logger. */
    private static final Logger LOG = LoggerFactory.logger(JMovieManager.class);
        
    /**
     * Mainklasse zum starten der Applikation
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) { 
        Settings.getInstance().load();
        ResourceBundle bundle = ResourceBundle.getBundle("jMovieManager.swing.resources.MovieManager");
        LOG.debug("Debugging enabled for jMM.");
        //initialize main gui
        Thread.setDefaultUncaughtExceptionHandler(new GUIExceptionHandler());
        final MovieManagerGUI instance = MovieManagerGUI.getInstance();
        
        if(OperatingSystem.isAnotherJMMInstanceRunning()){
            javax.swing.JOptionPane.showMessageDialog(instance, bundle.getString("MovieManagerGUI.anotherInstanceIsRunning"));
            System.exit(0);
        }
                
        instance.setVisible(true);   
                
        Thread postInitializeTasks = new Thread(new Runnable() {
            @Override
            public void run() {
                String hsqlDbPath = Settings.getInstance().getHsqlDbPath();
                File file = new File(hsqlDbPath + ".script");
                if(file.exists() && file.isFile()){
                    if(!instance.openOrRestoreDB(hsqlDbPath)){
                        instance.createNewCollection();
                    }
                }
                else{
                    instance.createNewCollection();
                }
                //Check for Updates
                if(Settings.getInstance().isCheckForUpdateOnStartup()){
                    UpdateGUI searchForUpdates = new UpdateGUI(instance, true);
                    if(searchForUpdates.updateAvailable()){
                        searchForUpdates.setVisible(true);
                    }
                }
            }
        });
        postInitializeTasks.start();
        
        // debug only
//        throw new RuntimeException("Testttttt");
    }
}