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
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.border.TitledBorder;
import jmm.data.Movie;
import jMovieManager.swing.gui.MovieManagerGUI;
import jMovieManager.swing.gui.components.ui.MyBasicButtonUI;
import jMovieManager.swing.gui.font.JMMFont;
import java.io.IOException;
import java.net.URISyntaxException;
import jmm.utils.LocaleManager;
import jmm.utils.OperatingSystem;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;

/**
 *
 * @author Bryan Beck
 * @since 25.10.2011
 * 
 */
public class MovieDetailGUI extends AbstractDetailGUI {
    /** Logger. */
    private static final Logger LOG = LoggerFactory.logger(MovieDetailGUI.class);
    
    private static MovieDetailGUI instance;

    /** Creates new form MovieDetailGUI */
    public MovieDetailGUI() {
        super();
        initComponents();
        super.getRightPanel().add(jPanel34, 2);      
        if(useCustomLAF){
            this.changeUI();
        }
        
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel34 = new javax.swing.JPanel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(5, 75), new java.awt.Dimension(5, 100), new java.awt.Dimension(5, 100));
        jPanel1 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        jPanel33 = new javax.swing.JPanel();
        jLabel61 = new javax.swing.JLabel();
        jLabel62 = new javax.swing.JLabel();
        jPanel35 = new javax.swing.JPanel();
        jLabel63 = new javax.swing.JLabel();
        jLabel64 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jPanel27 = new javax.swing.JPanel();
        jLabel49 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jPanel28 = new javax.swing.JPanel();
        jLabel51 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        jPanel29 = new javax.swing.JPanel();
        jLabel53 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        jPanel30 = new javax.swing.JPanel();
        jLabel55 = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        jPanel31 = new javax.swing.JPanel();
        jLabel57 = new javax.swing.JLabel();
        jLabel58 = new javax.swing.JLabel();
        jPanel32 = new javax.swing.JPanel();
        jLabel59 = new javax.swing.JLabel();
        jLabel60 = new javax.swing.JLabel();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("jMovieManager/swing/resources/MovieManager"); // NOI18N
        jPanel34.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), bundle.getString("AbstractDetailGUI.jPanel34"))); // NOI18N
        jPanel34.setMaximumSize(new java.awt.Dimension(32779, 125));
        jPanel34.setMinimumSize(new java.awt.Dimension(460, 125));
        jPanel34.setPreferredSize(new java.awt.Dimension(460, 125));
        jPanel34.setLayout(new javax.swing.BoxLayout(jPanel34, javax.swing.BoxLayout.LINE_AXIS));
        jPanel34.add(filler1);

        jPanel1.setMaximumSize(new java.awt.Dimension(32767, 100));
        jPanel1.setPreferredSize(new java.awt.Dimension(460, 100));
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.PAGE_AXIS));

        jPanel17.setMaximumSize(new java.awt.Dimension(32767, 25));
        jPanel17.setPreferredSize(new java.awt.Dimension(470, 25));
        jPanel17.setLayout(new java.awt.GridLayout(1, 2));

        jPanel33.setMaximumSize(new java.awt.Dimension(32767, 25));
        jPanel33.setMinimumSize(new java.awt.Dimension(0, 25));
        jPanel33.setLayout(new javax.swing.BoxLayout(jPanel33, javax.swing.BoxLayout.LINE_AXIS));

        jLabel61.setFont(JMMFont.labelFont);
        jLabel61.setText(bundle.getString("MovieDetailGUI.jLabel61")); // NOI18N
        jLabel61.setMaximumSize(new java.awt.Dimension(120, 25));
        jLabel61.setMinimumSize(new java.awt.Dimension(120, 25));
        jLabel61.setPreferredSize(new java.awt.Dimension(120, 25));
        jPanel33.add(jLabel61);

        jLabel62.setFont(JMMFont.smallIOFont);
        jLabel62.setMaximumSize(new java.awt.Dimension(32767, 25));
        jLabel62.setMinimumSize(new java.awt.Dimension(120, 25));
        jLabel62.setPreferredSize(new java.awt.Dimension(120, 25));
        jPanel33.add(jLabel62);

        jPanel17.add(jPanel33);

        jPanel35.setMaximumSize(new java.awt.Dimension(32767, 25));
        jPanel35.setMinimumSize(new java.awt.Dimension(0, 25));
        jPanel35.setName(""); // NOI18N
        jPanel35.setLayout(new javax.swing.BoxLayout(jPanel35, javax.swing.BoxLayout.LINE_AXIS));

        jLabel63.setFont(JMMFont.labelFont);
        jLabel63.setText(bundle.getString("MovieDetailGUI.jLabel63")); // NOI18N
        jLabel63.setMaximumSize(new java.awt.Dimension(110, 25));
        jLabel63.setMinimumSize(new java.awt.Dimension(110, 25));
        jLabel63.setPreferredSize(new java.awt.Dimension(110, 25));
        jPanel35.add(jLabel63);

        jLabel64.setFont(JMMFont.smallIOFont);
        jLabel64.setMaximumSize(new java.awt.Dimension(32767, 25));
        jLabel64.setMinimumSize(new java.awt.Dimension(120, 25));
        jLabel64.setPreferredSize(new java.awt.Dimension(120, 25));
        jPanel35.add(jLabel64);

        jPanel17.add(jPanel35);

        jPanel1.add(jPanel17);

        jPanel14.setMaximumSize(new java.awt.Dimension(32767, 25));
        jPanel14.setPreferredSize(new java.awt.Dimension(470, 25));
        jPanel14.setLayout(new java.awt.GridLayout(1, 2));

        jPanel27.setMaximumSize(new java.awt.Dimension(32767, 25));
        jPanel27.setMinimumSize(new java.awt.Dimension(0, 25));
        jPanel27.setLayout(new javax.swing.BoxLayout(jPanel27, javax.swing.BoxLayout.LINE_AXIS));

        jLabel49.setFont(JMMFont.labelFont);
        jLabel49.setText(bundle.getString("MovieDetailGUI.jLabel49")); // NOI18N
        jLabel49.setMaximumSize(new java.awt.Dimension(120, 25));
        jLabel49.setMinimumSize(new java.awt.Dimension(120, 25));
        jLabel49.setPreferredSize(new java.awt.Dimension(120, 25));
        jPanel27.add(jLabel49);

        jLabel50.setFont(JMMFont.smallIOFont);
        jLabel50.setMaximumSize(new java.awt.Dimension(32767, 25));
        jLabel50.setMinimumSize(new java.awt.Dimension(120, 25));
        jLabel50.setPreferredSize(new java.awt.Dimension(120, 25));
        jPanel27.add(jLabel50);

        jPanel14.add(jPanel27);

        jPanel28.setMaximumSize(new java.awt.Dimension(32767, 25));
        jPanel28.setMinimumSize(new java.awt.Dimension(0, 25));
        jPanel28.setLayout(new javax.swing.BoxLayout(jPanel28, javax.swing.BoxLayout.LINE_AXIS));

        jLabel51.setFont(JMMFont.labelFont);
        jLabel51.setText(bundle.getString("MovieDetailGUI.jLabel51")); // NOI18N
        jLabel51.setMaximumSize(new java.awt.Dimension(110, 25));
        jLabel51.setMinimumSize(new java.awt.Dimension(110, 25));
        jLabel51.setPreferredSize(new java.awt.Dimension(110, 25));
        jPanel28.add(jLabel51);

        jLabel52.setFont(JMMFont.smallIOFont);
        jLabel52.setMaximumSize(new java.awt.Dimension(32767, 25));
        jLabel52.setMinimumSize(new java.awt.Dimension(120, 25));
        jLabel52.setPreferredSize(new java.awt.Dimension(120, 25));
        jPanel28.add(jLabel52);

        jPanel14.add(jPanel28);

        jPanel1.add(jPanel14);

        jPanel15.setMaximumSize(new java.awt.Dimension(32767, 25));
        jPanel15.setPreferredSize(new java.awt.Dimension(470, 25));
        jPanel15.setLayout(new java.awt.GridLayout(1, 2));

        jPanel29.setMaximumSize(new java.awt.Dimension(32767, 25));
        jPanel29.setMinimumSize(new java.awt.Dimension(0, 25));
        jPanel29.setLayout(new javax.swing.BoxLayout(jPanel29, javax.swing.BoxLayout.LINE_AXIS));

        jLabel53.setFont(JMMFont.labelFont);
        jLabel53.setText(bundle.getString("MovieDetailGUI.jLabel53")); // NOI18N
        jLabel53.setMaximumSize(new java.awt.Dimension(120, 25));
        jLabel53.setMinimumSize(new java.awt.Dimension(120, 25));
        jLabel53.setPreferredSize(new java.awt.Dimension(120, 25));
        jPanel29.add(jLabel53);

        jLabel54.setFont(JMMFont.smallIOFont);
        jLabel54.setMaximumSize(new java.awt.Dimension(32767, 25));
        jLabel54.setMinimumSize(new java.awt.Dimension(120, 25));
        jLabel54.setPreferredSize(new java.awt.Dimension(120, 25));
        jPanel29.add(jLabel54);

        jPanel15.add(jPanel29);

        jPanel30.setMaximumSize(new java.awt.Dimension(32767, 25));
        jPanel30.setMinimumSize(new java.awt.Dimension(0, 25));
        jPanel30.setName(""); // NOI18N
        jPanel30.setLayout(new javax.swing.BoxLayout(jPanel30, javax.swing.BoxLayout.LINE_AXIS));

        jLabel55.setFont(JMMFont.labelFont);
        jLabel55.setText(bundle.getString("MovieDetailGUI.jLabel55")); // NOI18N
        jLabel55.setMaximumSize(new java.awt.Dimension(110, 25));
        jLabel55.setMinimumSize(new java.awt.Dimension(110, 25));
        jLabel55.setPreferredSize(new java.awt.Dimension(110, 25));
        jPanel30.add(jLabel55);

        jLabel56.setFont(JMMFont.smallIOFont);
        jLabel56.setMaximumSize(new java.awt.Dimension(32767, 25));
        jLabel56.setMinimumSize(new java.awt.Dimension(120, 25));
        jLabel56.setPreferredSize(new java.awt.Dimension(120, 25));
        jPanel30.add(jLabel56);

        jPanel15.add(jPanel30);

        jPanel1.add(jPanel15);

        jPanel16.setMaximumSize(new java.awt.Dimension(32767, 25));
        jPanel16.setPreferredSize(new java.awt.Dimension(470, 25));
        jPanel16.setLayout(new java.awt.GridLayout(1, 2));

        jPanel31.setMaximumSize(new java.awt.Dimension(32767, 25));
        jPanel31.setMinimumSize(new java.awt.Dimension(0, 25));
        jPanel31.setLayout(new javax.swing.BoxLayout(jPanel31, javax.swing.BoxLayout.LINE_AXIS));

        jLabel57.setFont(JMMFont.labelFont);
        jLabel57.setText(bundle.getString("MovieDetailGUI.jLabel57")); // NOI18N
        jLabel57.setMaximumSize(new java.awt.Dimension(120, 25));
        jLabel57.setMinimumSize(new java.awt.Dimension(120, 25));
        jLabel57.setPreferredSize(new java.awt.Dimension(120, 25));
        jPanel31.add(jLabel57);

        jLabel58.setFont(JMMFont.smallIOFont);
        jLabel58.setMaximumSize(new java.awt.Dimension(32767, 25));
        jLabel58.setMinimumSize(new java.awt.Dimension(120, 25));
        jLabel58.setPreferredSize(new java.awt.Dimension(120, 25));
        jPanel31.add(jLabel58);

        jPanel16.add(jPanel31);

        jPanel32.setMaximumSize(new java.awt.Dimension(32767, 25));
        jPanel32.setMinimumSize(new java.awt.Dimension(0, 25));
        jPanel32.setName(""); // NOI18N
        jPanel32.setLayout(new javax.swing.BoxLayout(jPanel32, javax.swing.BoxLayout.LINE_AXIS));

        jLabel59.setFont(JMMFont.labelFont);
        jLabel59.setText(bundle.getString("MovieDetailGUI.jLabel59")); // NOI18N
        jLabel59.setMaximumSize(new java.awt.Dimension(110, 25));
        jLabel59.setMinimumSize(new java.awt.Dimension(110, 25));
        jLabel59.setPreferredSize(new java.awt.Dimension(110, 25));
        jPanel32.add(jLabel59);

        jLabel60.setFont(JMMFont.smallIOFont);
        jLabel60.setMaximumSize(new java.awt.Dimension(32767, 25));
        jLabel60.setMinimumSize(new java.awt.Dimension(120, 25));
        jLabel60.setPreferredSize(new java.awt.Dimension(120, 25));
        jPanel32.add(jLabel60);

        jPanel16.add(jPanel32);

        jPanel1.add(jPanel16);

        jPanel34.add(jPanel1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 760, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 700, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
    /**
    * Singleton Konstruktor der Klasse MovieDetailGUI
    *
    * @return instance Die Instanz der Klasse
    */
    public static MovieDetailGUI getInstance(){
        if(instance == null){
            instance = new MovieDetailGUI();
            LocaleManager.getInstance().addObserver(instance);
        }
        return instance;
    }
                
   /**
   * Aktualisiert alle Labels, Buttons, Tooltips und sonstigen Texte der GUI
   * auf das gesetzte Locale
   */
    @Override
   protected void updateGUILocalization(){
       super.updateGUILocalization();
       jLabel49.setText(bundle.getString("MovieDetailGUI.jLabel49"));
       jLabel51.setText(bundle.getString("MovieDetailGUI.jLabel51"));
       jLabel53.setText(bundle.getString("MovieDetailGUI.jLabel53"));
       jLabel55.setText(bundle.getString("MovieDetailGUI.jLabel55"));
       jLabel57.setText(bundle.getString("MovieDetailGUI.jLabel57"));
       jLabel59.setText(bundle.getString("MovieDetailGUI.jLabel59"));
       jLabel61.setText(bundle.getString("MovieDetailGUI.jLabel61"));
       jLabel63.setText(bundle.getString("MovieDetailGUI.jLabel63"));
       //Titled Borders
       ((TitledBorder)jPanel34.getBorder()).setTitle(bundle.getString("AbstractDetailGUI.jPanel34"));
   }

    /**
    * Aktualisiert das jPanel 2 mit
    * Informationen des Movie Objekts
    *
    * @param movie Das Movie Objekt
    */
    public void updateMovieDetail(final Movie movie){ 
        super.updateVideoDetail(movie);
       
        //Video Codec
        if(!movie.getVideoCodec().isEmpty() && genreKeyAndCodecBundle.containsKey(movie.getVideoCodec())) {
            jLabel54.setText(genreKeyAndCodecBundle.getString(movie.getVideoCodec()));
        }else{
            jLabel54.setText(genreKeyAndCodecBundle.getString("Value.empty"));
        }
        //Video Bitrate
        if(movie.getVideoBitrate() == 0){
            jLabel50.setText(genreKeyAndCodecBundle.getString("Value.empty"));
        }else{
            jLabel50.setText(Integer.toString(movie.getVideoBitrate()) + ' ' + bundle.getString("MovieDetailGUI.jLabel50"));
        }
        //Audio Codec
        if(!movie.getAudioCodec().isEmpty() && genreKeyAndCodecBundle.containsKey(movie.getAudioCodec())){
            jLabel56.setText(genreKeyAndCodecBundle.getString(movie.getAudioCodec()));
        }else{
            jLabel56.setText(genreKeyAndCodecBundle.getString("Value.empty"));
        }
        //Audio Bitrate
        if(movie.getAudioBitrate() == 0){
            jLabel52.setText(genreKeyAndCodecBundle.getString("Value.empty"));
        }else{
            jLabel52.setText(Integer.toString(movie.getAudioBitrate()) + ' ' + bundle.getString("MovieDetailGUI.jLabel52"));
        }
        //Audio Channels
        if(movie.getAudioChannels() == 0){
            jLabel60.setText(genreKeyAndCodecBundle.getString("Value.empty"));
        }else{
            jLabel60.setText(Integer.toString(movie.getAudioChannels()));
        }
        //Quelle
        if(!movie.getVideoSource().isEmpty() && genreKeyAndCodecBundle.containsKey(movie.getVideoSource())){
            jLabel58.setText(genreKeyAndCodecBundle.getString(movie.getVideoSource()));
        }else{
            jLabel58.setText(genreKeyAndCodecBundle.getString("Value.empty"));
        }
        //Resolution
        if((movie.getHeight()== 0) && (movie.getWidth() == 0)){
            jLabel62.setText(genreKeyAndCodecBundle.getString("Value.empty"));
        }else{
            jLabel62.setText(Integer.toString(movie.getWidth()) + " x " + movie.getHeight() + " " + bundle.getString("MovieDetailGUI.jLabel62"));
        }
        //FPS
        if(movie.getAverageFPS() == 0){
            jLabel64.setText(genreKeyAndCodecBundle.getString("Value.empty"));
        }else{
            jLabel64.setText(String.valueOf(movie.getAverageFPS()) + " " + bundle.getString("MovieDetailGUI.jLabel64"));
        }
        Dimension buttonDimension = new Dimension(100, 26);
        //Erstelle Trailer Button
        JButton trailerButton = new JButton(bundle.getString("AbstractDetailGUI.trailerButton"));
        trailerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        trailerButton.setUI((MyBasicButtonUI)MyBasicButtonUI.createUI(trailerButton));
        //trailerButton.setBorder(new MyBasicButtonBorder());
        trailerButton.setPreferredSize(buttonDimension);
        trailerButton.setMaximumSize(buttonDimension);
        trailerButton.setMinimumSize(buttonDimension);
        //trailerButton.setBorderPainted(false);
        trailerButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if(!movie.getTrailerPath().isEmpty()){
                    //Führe die Datei mit dem Standardprogramm aus, welches bei Windows hinterlegt ist
                    try{
                        OperatingSystem.openFile(movie.getTrailerPath());
                        //MovieManagerGUI.getInstance().setExtendedState(JFrame.ICONIFIED);   //minimiere GUI
                    }catch (IOException exp){
                        javax.swing.JOptionPane.showMessageDialog(null, bundle.getString("MovieDetailGUI.error.openFile"));
                        LOG.error("Cannot open file: " + movie.getTrailerPath(), exp);
                    }
                }else{
                    String title = movie.getTitle();
                    String searchURL = "http://www.youtube.com/results?search_query=" + title.replace(' ', '+'); // + "&hd=1";  //"fmt=22";
               
                    try {
                        //Öffne den Standardbrowser
                        OperatingSystem.openBrowser(searchURL); 
//                        MovieManagerGUI.getInstance().setExtendedState(JFrame.ICONIFIED);   //minimiere GUI
                    } catch (IOException | URISyntaxException ex) {
                        LOG.error("URL: " + searchURL + " could not be opened in the browser.", ex);
                    }
                }
            }
        });
        buttonPanel.add(trailerButton);

        if(!movie.getFilePaths().isEmpty()){
            //Generiere dynamisch mehrere Play Buttons, je nach Anzahl der hinterlegten FilePaths
            int moviePart = 0;
            for(final String filePath: movie.getFilePaths()){
                JButton playButton;
                if(movie.getFilePaths().size() > 1){
                    playButton = new JButton(bundle.getString("AbstractDetailGUI.playButton.part") + ' ' +  (moviePart+1));
                }else{
                    playButton = new JButton(bundle.getString("AbstractDetailGUI.playButton"));
                }
                playButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                playButton.setUI((MyBasicButtonUI)MyBasicButtonUI.createUI(playButton));
                playButton.setPreferredSize(buttonDimension);
                playButton.setMaximumSize(buttonDimension);
                playButton.setMinimumSize(buttonDimension);

                playButton.addActionListener(new java.awt.event.ActionListener() {
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        //Führe die Datei mit dem Standardprogramm aus, welches bei Windows hinterlegt ist
                        try{
                            OperatingSystem.openFile(filePath);
                            //set selected movie/episode as watched
                            movie.setWatched(true);
                            MovieManagerGUI.getInstance().setExtendedState(JFrame.ICONIFIED);   //minimiere GUI
                        }catch (IOException exp){
                            javax.swing.JOptionPane.showMessageDialog(null, bundle.getString("MovieDetailGUI.error.openFile"));
                            LOG.error("Cannot open file: " + filePath, exp);
                        }
                    }
                });
                moviePart ++;
                buttonPanel.add(playButton);
            }
        }
        this.pack();    //required for dynamic buttons
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.Box.Filler filler1;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel35;
    // End of variables declaration//GEN-END:variables
}