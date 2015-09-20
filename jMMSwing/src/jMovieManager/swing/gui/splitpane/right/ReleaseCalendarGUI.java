/**
 * Copyright (c) 2010-2015 Bryan Beck.
 * All rights reserved.
 * 
 * This project is licensed under LGPL v2.1.
 * See jMovieManager-license.txt for details.
 * 
 */
package jMovieManager.swing.gui.splitpane.right;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import jMovieManager.swing.gui.UIInterface;
import jMovieManager.swing.gui.components.ui.MyBasicScrollBarUI;
import jmm.api.tmdb.TMDBMovieWrapper;
import jmm.data.TMDBVideoFile;
import jmm.data.VideoFile;
import jMovieManager.swing.gui.ColorInterface;
import java.io.IOException;
import java.net.URISyntaxException;
import jmm.utils.LocaleManager;
import jmm.utils.OperatingSystem;
import jmm.utils.PictureManager;
import jmm.utils.Settings;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;

/**
 * Displays information about upcoming, now playing and top rated movies
 * 
 * @author Bryan Beck
 * @since 20.10.2010
 */
public class ReleaseCalendarGUI extends javax.swing.JFrame implements UIInterface, Observer{
    /** Logger. */
    private static final Logger LOG = LoggerFactory.logger(ReleaseCalendarGUI.class);
    
    public static ReleaseCalendarGUI instance;
    private ResourceBundle bundle;
    private ResourceBundle genreKeyAndCodecBundle;
       
    private static final int maxResults = 10;
    
    //tmdbID, element
    
    /**
     * Task to retrieve information about upcoming cinema movies
     */
    private Thread getUpcomingCinemaInformationThread;
    private Runnable getUpcomingCinemaInformation = new Runnable() {
        @Override
        public void run() {
            Map<String, TMDBVideoFile> upcomingMovies = new HashMap<String, TMDBVideoFile>();
            List<TMDBVideoFile> results = new TMDBMovieWrapper().findUpcomingMovies(null, LocaleManager.getInstance().getCurrentLocale().getLanguage(), maxResults);
            for(TMDBVideoFile result: results){
                upcomingMovies.put(result.getTmdbID(), result);
            }
            updateAllReleases(upcomingMovies, jPanel3);
        }
    };    
    
    /**
     * Task to retrieve information aboutnow playing cinema movies
     */
    private Thread getNowPlayingCinemaInformationThread;
    private Runnable getNowPlayingCinemaInformation = new Runnable() {
        @Override
        public void run() {
            Map<String, TMDBVideoFile> nowPlayingMovies = new HashMap<String, TMDBVideoFile>();
            List<TMDBVideoFile> results = new TMDBMovieWrapper().findNowPlayingMovies(null, LocaleManager.getInstance().getCurrentLocale().getLanguage(), maxResults);
            for(TMDBVideoFile result: results){
                nowPlayingMovies.put(result.getTmdbID(), result);
            }
            updateAllReleases(nowPlayingMovies, jPanel8);
        }
    };
    
    /**
     * Task to retrieve information about top rated movies
     */
    private Thread getTopRatedInformationThread;
    private Runnable getTopRatedInformation = new Runnable() {
        @Override
        public void run() {
            Map<String, TMDBVideoFile> topRatedMovies = new HashMap<String, TMDBVideoFile>();
            List<TMDBVideoFile> results = new TMDBMovieWrapper().findTopRatedMovies(null, LocaleManager.getInstance().getCurrentLocale().getLanguage(), maxResults);
            for(TMDBVideoFile result: results){
                topRatedMovies.put(result.getTmdbID(), result);
            }
            updateAllReleases(topRatedMovies, jPanel6);
        }
    };
            
    /**
     * Task to retrieve information about popular movies
     */
    private Thread getPopularMovieInformationThread;
    private Runnable getPopularMovieInformation = new Runnable() {
        @Override
        public void run() {
            Map<String, TMDBVideoFile> popularMovies = new HashMap<String, TMDBVideoFile>();
            List<TMDBVideoFile> results = new TMDBMovieWrapper().findPopularMovies(null, LocaleManager.getInstance().getCurrentLocale().getLanguage(), maxResults);
            for(TMDBVideoFile result: results){
                popularMovies.put(result.getTmdbID(), result);
            }
            updateAllReleases(popularMovies, jPanel7);
        }
    };
    
    
    /** Creates new form ReleaseCalendarGUI */
    private ReleaseCalendarGUI() {
        bundle = ResourceBundle.getBundle("jMovieManager.swing.resources.MovieManager");
        genreKeyAndCodecBundle = LocaleManager.getInstance().getGenreKeyAndCodecBundle();
        initComponents();
        
        if(useCustomLAF){
            this.changeUI();
        }
        //Set Tooltip Dismiss Timer to 1 minute
        ToolTipManager.sharedInstance().setDismissDelay(60000);
        //Einstellung der Scrollgeschwindigkeit
        JScrollBar horScrollBar = jScrollPane3.getHorizontalScrollBar();
        horScrollBar.setUnitIncrement(30);
        //horScrollBar.setBlockIncrement(30);
        horScrollBar = jScrollPane4.getHorizontalScrollBar();
        horScrollBar.setUnitIncrement(30);
        //horScrollBar.setBlockIncrement(30);     
        horScrollBar = jScrollPane5.getHorizontalScrollBar();
        horScrollBar.setUnitIncrement(30);
        //horScrollBar.setBlockIncrement(30);
        horScrollBar = jScrollPane6.getHorizontalScrollBar();
        horScrollBar.setUnitIncrement(30);
        //horScrollBar.setBlockIncrement(30);
        
        initReleasePanels();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel18 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jPanel7 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jPanel8 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jPanel6 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel10 = new javax.swing.JPanel();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("jMovieManager/swing/resources/MovieManager"); // NOI18N
        jPanel18.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(null, ColorInterface.list_tree_BorderShadow), bundle.getString("ReleaseCalendarGUI.jPanel17"))); // NOI18N
        jPanel18.setMaximumSize(new java.awt.Dimension(32779, 230));
        jPanel18.setMinimumSize(new java.awt.Dimension(760, 230));
        jPanel18.setPreferredSize(new java.awt.Dimension(760, 230));
        jPanel18.setLayout(new javax.swing.BoxLayout(jPanel18, javax.swing.BoxLayout.LINE_AXIS));

        jScrollPane6.setBorder(null);
        jScrollPane6.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane6.setPreferredSize(new java.awt.Dimension(670, 233));

        jPanel7.setPreferredSize(new java.awt.Dimension(670, 233));
        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 15, 0));
        jScrollPane6.setViewportView(jPanel7);

        jPanel18.add(jScrollPane6);

        jPanel9.setPreferredSize(new java.awt.Dimension(760, 1));

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 760, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 2, Short.MAX_VALUE)
        );

        jPanel2.setPreferredSize(new java.awt.Dimension(760, 1));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 760, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1, Short.MAX_VALUE)
        );

        jPanel1.setPreferredSize(new java.awt.Dimension(760, 1));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 760, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 2, Short.MAX_VALUE)
        );

        jPanel16.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(null, ColorInterface.list_tree_BorderShadow), bundle.getString("ReleaseCalendarGUI.jPanel16"))); // NOI18N
        jPanel16.setMaximumSize(new java.awt.Dimension(32779, 230));
        jPanel16.setMinimumSize(new java.awt.Dimension(760, 230));
        jPanel16.setPreferredSize(new java.awt.Dimension(760, 230));
        jPanel16.setLayout(new javax.swing.BoxLayout(jPanel16, javax.swing.BoxLayout.LINE_AXIS));

        jScrollPane5.setBorder(null);
        jScrollPane5.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane5.setPreferredSize(new java.awt.Dimension(670, 233));

        jPanel8.setPreferredSize(new java.awt.Dimension(670, 233));
        jPanel8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 15, 0));
        jScrollPane5.setViewportView(jPanel8);

        jPanel16.add(jScrollPane5);

        jPanel17.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(null, ColorInterface.list_tree_BorderShadow), bundle.getString("ReleaseCalendarGUI.jPanel17"))); // NOI18N
        jPanel17.setMaximumSize(new java.awt.Dimension(32779, 230));
        jPanel17.setMinimumSize(new java.awt.Dimension(760, 230));
        jPanel17.setPreferredSize(new java.awt.Dimension(760, 230));
        jPanel17.setLayout(new javax.swing.BoxLayout(jPanel17, javax.swing.BoxLayout.LINE_AXIS));

        jScrollPane4.setBorder(null);
        jScrollPane4.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane4.setPreferredSize(new java.awt.Dimension(670, 233));

        jPanel6.setPreferredSize(new java.awt.Dimension(670, 233));
        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 15, 0));
        jScrollPane4.setViewportView(jPanel6);

        jPanel17.add(jScrollPane4);

        jPanel5.setPreferredSize(new java.awt.Dimension(760, 1));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 760, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 2, Short.MAX_VALUE)
        );

        jPanel15.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(null, ColorInterface.list_tree_BorderShadow), bundle.getString("ReleaseCalendarGUI.jPanel15"))); // NOI18N
        jPanel15.setMaximumSize(new java.awt.Dimension(32779, 230));
        jPanel15.setMinimumSize(new java.awt.Dimension(760, 230));
        jPanel15.setPreferredSize(new java.awt.Dimension(760, 230));
        jPanel15.setLayout(new javax.swing.BoxLayout(jPanel15, javax.swing.BoxLayout.LINE_AXIS));

        jScrollPane3.setBorder(null);
        jScrollPane3.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane3.setPreferredSize(new java.awt.Dimension(755, 230));

        jPanel3.setMinimumSize(new java.awt.Dimension(8, 25));
        jPanel3.setPreferredSize(new java.awt.Dimension(640, 210));
        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 15, 0));
        jScrollPane3.setViewportView(jPanel3);

        jPanel15.add(jScrollPane3);

        jPanel4.setPreferredSize(new java.awt.Dimension(760, 1));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 760, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 2, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel14.setMinimumSize(new java.awt.Dimension(760, 700));
        jPanel14.setPreferredSize(new java.awt.Dimension(760, 700));
        jPanel14.setLayout(new javax.swing.BoxLayout(jPanel14, javax.swing.BoxLayout.PAGE_AXIS));

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jPanel10.setLayout(new javax.swing.BoxLayout(jPanel10, javax.swing.BoxLayout.PAGE_AXIS));
        jScrollPane1.setViewportView(jPanel10);

        jPanel14.add(jScrollPane1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 760, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 700, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    /** 
    * Singleton Konstruktor der Instanz
    * 
    * @return instance Die Instanz der Klasse
    */
    public static ReleaseCalendarGUI getInstance(){
        if(instance == null){
            instance = new ReleaseCalendarGUI();
            LocaleManager.getInstance().addObserver(instance);
            Settings.getInstance().addObserver(instance);
        }
        return instance;
    }

    /**
    * Gibt das Rootpanel zurück auf dem alle
    * Komponenten enthalten sind
    *
    * @return jPanel Das RootPanel
    */
    public JPanel getReleasePanel(){
        return jPanel14;
    }
    
    /**
    * Observer method for locale update <br/>
    * Updates all localized GUI elements
    * 
    * @param o the observerable object
    * @param arg the updated locale
    */
   @Override
    public void update(Observable o, Object arg) {
       if(arg instanceof Locale){
           updateGUILocalization();
       }
       initReleasePanels();
    }

   /**
   * Aktualisiert alle Labels, Buttons, Tooltips und sonstigen Texte der GUI
   * auf das gesetzte Locale
   */
   private void updateGUILocalization(){
       bundle = ResourceBundle.getBundle("jMovieManager.swing.resources.MovieManager");
       genreKeyAndCodecBundle = LocaleManager.getInstance().getGenreKeyAndCodecBundle();
       ((TitledBorder)jPanel15.getBorder()).setTitle(bundle.getString("ReleaseCalendarGUI.jPanel15"));
       ((TitledBorder)jPanel16.getBorder()).setTitle(bundle.getString("ReleaseCalendarGUI.jPanel16"));
       ((TitledBorder)jPanel17.getBorder()).setTitle(bundle.getString("ReleaseCalendarGUI.jPanel17"));
       ((TitledBorder)jPanel18.getBorder()).setTitle(bundle.getString("ReleaseCalendarGUI.jPanel18"));
   }
   
   private void initReleasePanels(){
       jPanel10.setVisible(false);
       jPanel10.removeAll();
       jPanel10.add(jPanel1);
       if(Settings.getInstance().isShowUpcoming()){
           jPanel10.add(jPanel15);
            jPanel10.add(jPanel2);
            initUpcomingCinemaReleases();
        }
        if(Settings.getInstance().isShowNowPlaying()){
            jPanel10.add(jPanel16);
            jPanel10.add(jPanel4);
            initNowPlayingCinemaReleases();
        }
        if(Settings.getInstance().isShowTopRated()){
            jPanel10.add(jPanel17);
            jPanel10.add(jPanel5);
            initTopRatedMovies();
        }
        if(Settings.getInstance().isShowPopular()){
            jPanel10.add(jPanel18);
            jPanel10.add(jPanel9);
            initPopularMovies();
        }
        this.pack();
        jPanel10.setVisible(true);
   }

    /*
     * initializes upcoming cinema releases
     */
    private void initUpcomingCinemaReleases(){
        if(getUpcomingCinemaInformationThread != null && getUpcomingCinemaInformationThread.isAlive()){
            getUpcomingCinemaInformationThread.interrupt();
        }
        getUpcomingCinemaInformationThread = new Thread(getUpcomingCinemaInformation);
        getUpcomingCinemaInformationThread.start();
    }

    /*
     * initializes now playing cinema releases
     */
    private void initNowPlayingCinemaReleases(){
        if(getNowPlayingCinemaInformationThread != null && getNowPlayingCinemaInformationThread.isAlive()){
            getNowPlayingCinemaInformationThread.interrupt();
        }
        getNowPlayingCinemaInformationThread = new Thread(getNowPlayingCinemaInformation);
        getNowPlayingCinemaInformationThread.start();
    }

    /*
     * initializes top rated movies
     */
    private void initTopRatedMovies(){
        if(getTopRatedInformationThread != null && getTopRatedInformationThread.isAlive()){
            getTopRatedInformationThread.interrupt();
        }
        getTopRatedInformationThread = new Thread(getTopRatedInformation);
        getTopRatedInformationThread.start();
    }
       
    /*
     * initializes top rated movies
     */
    private void initPopularMovies(){
        if(getPopularMovieInformationThread != null && getPopularMovieInformationThread.isAlive()){
            getPopularMovieInformationThread.interrupt();
        }
        getPopularMovieInformationThread = new Thread(getPopularMovieInformation);
        getPopularMovieInformationThread.start();
    }
    
    /**
    * Erstellt erstmalig alle benötigten Komponenten in dem jeweiligen jScrollPane
    *
    * @param releases A list of releases to show on the GUI
    * @param releasePanel Das Panel, auf dem die Listenobjekte hinzugefügt werden sollen
    */
    private void updateAllReleases(final Map<String, TMDBVideoFile> releases, JPanel releasePanel){
        releasePanel.removeAll();
        releasePanel.setPreferredSize(new Dimension(670, 233));
        releasePanel.setSize(new Dimension(670, 233));

        int i = 0;
        for(String key: releases.keySet()){
            final TMDBVideoFile release = releases.get(key);
            final JLabel pictureLabel = new JLabel();
            pictureLabel.setMinimumSize(new Dimension(140, 200));
            pictureLabel.setMaximumSize(new Dimension(140, 200));
            pictureLabel.setPreferredSize(new Dimension(140, 200));
            pictureLabel.setHorizontalAlignment(SwingConstants.CENTER);
            pictureLabel.setVerticalAlignment(SwingConstants.CENTER);
            pictureLabel.setBorder(null);

            //Setzte Tooltip
            pictureLabel.setToolTipText(generateToolTip(release));
            pictureLabel.setName(release.getTmdbID());
            
            //Setzte Vorschaubild
            pictureLabel.setText(bundle.getString("ReleaseCalendarGUI.picture"));
            pictureLabel.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            Thread setPicture = new Thread(new Runnable() {
                @Override
                public void run() {
                    if(!release.getImagePath().isEmpty()){
                        Icon scaledIcon = PictureManager.getScaledImage(release.getImagePath(), 140, 200);
                        if(null != scaledIcon){
                            pictureLabel.setText("");
                            pictureLabel.setIcon(scaledIcon);
                        }else{
                            pictureLabel.setIcon(null);
                            pictureLabel.setText(bundle.getString("ReleaseCalendarGUI.error.picture"));
                        }
                    }else{
                        pictureLabel.setIcon(null);
                        pictureLabel.setText(bundle.getString("ReleaseCalendarGUI.error.picture"));
                    }
                    pictureLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                } 
            });
            setPicture.start();        
            
            
            pictureLabel.addMouseListener(new java.awt.event.MouseAdapter(){
                @Override
                public void mouseClicked(MouseEvent e) {
                   // if (e.getClickCount()==2){
                            //Öffne den Standardbrowser
                        String url;
                        if((Settings.getInstance().getRedirectTo() == Settings.REDIRECT_IMDB) && 
                                (!release.getImdbID().isEmpty())){
                            url = release.getImdbUrl();
                        }else{
                            url = release.getTmdbUrl();
                        }
                        try {
                            OperatingSystem.openBrowser(url); 
                        } catch (IOException | URISyntaxException ex) {
                            LOG.error("URL: " + url + " could not be opened in the browser.", ex);
                        }                        
                   // }
                }
                
                @Override
                public void mouseEntered(final MouseEvent e) {
                    super.mouseEntered(e);
                    //get detailed information 
//                    TMDBVideoFile selElement = releases.get(pictureLabel.getName());
                    if(release.getImdbID().isEmpty()){
                        pictureLabel.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                        Thread updateToolTip = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                TMDBVideoFile detailedMovie = new TMDBMovieWrapper().findMovie(release.getTmdbID(), LocaleManager.getInstance().getCurrentLocale().getLanguage(), null);
                                if(detailedMovie != null){
                                    release.setImdbID(detailedMovie.getImdbID());
                                    pictureLabel.setToolTipText(generateToolTip(detailedMovie));
                                    pictureLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                                    //fake mouse move to update the displayed tooltip
        //                            MouseEvent phantom = new MouseEvent(
        //                                pictureLabel,
        //                                MouseEvent.MOUSE_MOVED,
        //                                System.currentTimeMillis(),
        //                                0,
        //                                0,
        //                                0,
        //                                0,
        //                                true);
                                    SwingUtilities.invokeLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            ToolTipManager.sharedInstance().mouseMoved(e);
                                        }
                                    }); 
                                }
//                                releases.remove(pictureLabel.getName());
                            }
                        });
                        updateToolTip.start();
                    }
                }
            });
            if(i > 3){
                Dimension panelSize = new Dimension(
                        (int)releasePanel.getSize().getWidth() + (int)pictureLabel.getPreferredSize().getWidth() -5, 
                        releasePanel.getSize().height);
                releasePanel.setPreferredSize(panelSize);
                releasePanel.setSize(panelSize);
            }
            releasePanel.add(pictureLabel);
            i++;
            
            //i.e. If language is changed while executing this method
            if(Thread.currentThread().isInterrupted()){
                return;
            }
        }
    }
        
    /**
    * Erzeugt den Tooltip eines ReleaseDate Objekts als String
    *
    * @param ReleaseDate Das Objekt, aus dem der Tooltip erzeugt werden soll
    * @return toolTip Der fertige HTML tooltip als String
    */
    private String generateToolTip(TMDBVideoFile releaseToUpdate){
            //Setze Tooltip
            String toolTip = "<html>" + "<PRE>" + "<FONT SIZE="+'"'+"4"+'"'+">";
            if(releaseToUpdate != null){
                if(!releaseToUpdate.getTitle().isEmpty()){
                    toolTip += identText(bundle.getString("ReleaseCalendarGUI.tooltip.title"), "<b>" + releaseToUpdate.getTitle() + "</b>") + "<br/>";
                }
                if(!releaseToUpdate.getOriginalTitle().isEmpty()){
                    toolTip += identText("", "<i>" + releaseToUpdate.getOriginalTitle()+ "</i>") + "<br/><br/>";
                }       

                if(releaseToUpdate.getTmdbRating() > 0){
                    toolTip += identText(bundle.getString("ReleaseCalendarGUI.tooltip.rating"), releaseToUpdate.getTmdbRating() + "/10") + "<br/>";
                }  
                if(releaseToUpdate.getReleaseDate() != null){
                    String formattedRlsDate;
                    if(LocaleManager.getInstance().getCurrentLocale().equals(LocaleManager.SupportedLanguages.de_DE.getLocale())){
                        formattedRlsDate = new SimpleDateFormat("dd.mm.yyyy").format(releaseToUpdate.getReleaseDate());
                    }else{
                        formattedRlsDate = new SimpleDateFormat("mm-dd-yyyy").format(releaseToUpdate.getReleaseDate());
                    }                
                    toolTip += identText(bundle.getString("ReleaseCalendarGUI.tooltip.date"), formattedRlsDate) + "<br/>";
                }
                if(!releaseToUpdate.getGenreKeys().isEmpty()){
                    Iterator<String> genreKeys = releaseToUpdate.getGenreKeys().iterator();
                    String genreKey = genreKeys.next();
                    StringBuilder value = new StringBuilder();
                   
                    while(genreKeys.hasNext()){
                        if(genreKeyAndCodecBundle.containsKey(genreKey)){
                            value.append(genreKeyAndCodecBundle.getString(genreKey)).append(", ");
                        }
                        genreKey = genreKeys.next();
                    }
                    //Das letzte Genre
                    if(genreKeyAndCodecBundle.containsKey(genreKey)){
                        value.append(genreKeyAndCodecBundle.getString(genreKey));
                    }
                    toolTip += identText(bundle.getString("ReleaseCalendarGUI.tooltip.genre"), value.toString()) + "<br/>";
                }
                if(releaseToUpdate.getFsk() != VideoFile.FSK.FSK_UNKNOWN){
                    toolTip += identText(bundle.getString("ReleaseCalendarGUI.tooltip.fsk"), releaseToUpdate.getFsk().toString()) + "<br/>";
                }
                if(releaseToUpdate.getPlaytime() != 0){
                    toolTip += identText(bundle.getString("ReleaseCalendarGUI.tooltip.playtime"), releaseToUpdate.getPlaytime() + " min") + "<br/>";
                }
                if(!releaseToUpdate.getDescription().isEmpty()){
                    String description = releaseToUpdate.getDescription();
                    toolTip += "<br />" + bundle.getString("ReleaseCalendarGUI.tooltip.desc") + "<br/>";

                    toolTip += "<div style=" + '"' + "width: 400px;" + '"' + ">";
                        toolTip += "<p align=" + '"' + "justify" + '"' + ">";
                            toolTip += description;
                        toolTip += "</p>";
                    toolTip += "</div>";
                }
            }
                toolTip += "</FONT>" + "</PRE>"+ "</html>";
            return toolTip;
    }
    
    private static final int identLength = 15;
    /**
     * Idents the value and returns a complete row
     * @param key the key
     * @param value the value to ident
     * @return the complete row
     */
    private String identText(String key, String value){
        int keyLength = key.length();
        StringBuilder row = new StringBuilder(key);
        while(keyLength < identLength){
            row.append(" ");
            keyLength ++;
        }
        row.append(value);
        return row.toString();
    }
        
   /**
    * Methode zum Anpassen der GUI an das neue Look and Feel
    **/
    @Override
   public void changeUI(){
        jScrollPane3.getHorizontalScrollBar().setUI((MyBasicScrollBarUI)MyBasicScrollBarUI.createUI(jPanel3)); 
        jScrollPane3.getVerticalScrollBar().setUI((MyBasicScrollBarUI)MyBasicScrollBarUI.createUI(jPanel3));   
        jScrollPane4.getHorizontalScrollBar().setUI((MyBasicScrollBarUI)MyBasicScrollBarUI.createUI(jPanel8));  
        jScrollPane4.getVerticalScrollBar().setUI((MyBasicScrollBarUI)MyBasicScrollBarUI.createUI(jPanel8)); 
        jScrollPane5.getHorizontalScrollBar().setUI((MyBasicScrollBarUI)MyBasicScrollBarUI.createUI(jPanel6));  
        jScrollPane5.getVerticalScrollBar().setUI((MyBasicScrollBarUI)MyBasicScrollBarUI.createUI(jPanel6));    
   }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    // End of variables declaration//GEN-END:variables
}
