/**
 * Copyright (c) 2010-2015 Bryan Beck.
 * All rights reserved.
 * 
 * This project is licensed under LGPL v2.1.
 * See jMovieManager-license.txt for details.
 * 
 */
package jMovieManager.swing.gui.createmedia;

import jmm.utils.OperatingSystem;
import jmm.utils.MaxLengthDocument;
import jmm.utils.MediaInfo;
import jMovieManager.swing.gui.components.MyJFileChooser;
import jmm.data.DataManager;
import jMovieManager.swing.gui.ColorInterface;
import jMovieManager.swing.gui.other.JMMFileFilter;
import java.awt.Frame;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import jmm.api.tmdb.TMDBMovieWrapper;
import jmm.api.tmdb.TMDBSearchWrapper;
import jmm.data.LocalVideoFile;
import jmm.data.collection.MovieCollection;
import jmm.interfaces.AudioInterface;
import jmm.interfaces.SourceInterface;
import jmm.interfaces.VideoInterface;
import jmm.data.Movie;
import jmm.data.TMDBVideoFile;
import jMovieManager.swing.gui.ChooseMediaFileDialog;
import jMovieManager.swing.gui.IPropertyChangeKeys;
import jMovieManager.swing.gui.MovieManagerGUI;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import jmm.utils.LocaleManager;
import jmm.utils.Utils;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;

/**
 * GUI to create or edit a movie
 * 
 * @author Bryan Beck
 * @since 17.08.2011
 */
public class CreateMovieGUI extends AbstractCreateGUI implements ColorInterface{
    /** Logger. */
    private static final Logger LOG = LoggerFactory.logger(CreateMovieGUI.class);
    
    protected String videoCodec;
    protected int    videoBitrate;
    protected String audioCodec;
    protected int    audioBitrate;
    protected int    audioChannels;
    protected String videoSource;
    protected LinkedList<String> filePaths;
    protected boolean watched;
    protected float averageFPS;
    protected int width;
    protected int height;
    
    protected boolean dialogCanceled;
    
    //Die jeweiligen Listmodels für das Medium
    private DefaultComboBoxModel videoCodecsListModel;
    private DefaultComboBoxModel videoSourceListModel;
    private DefaultComboBoxModel audioCodecListModel;
    private DefaultComboBoxModel audioChannelListModel;
    
    protected HashMap<String, String> localizedElementsMap;
    
    /** 
     * Create an prefilled GUI to edit a movie
     * 
     * @see javax.swing.JDialog#JDialog(java.awt.Frame, boolean)
     * @param outdatedMovie the movie to edit
     * @param collection the collection which contains the outdatedVideoFile
     * @param collectionEditable true if the collection can be changed <br/> 
     * false otherwise (i.E. episodes cannot change the collection)
     */
    public CreateMovieGUI(java.awt.Frame parent, boolean modal, final Movie outdatedMovie, final MovieCollection collection, boolean collectionEditable){   
        super(parent, modal, outdatedMovie, collection, collectionEditable);
        init(parent);
        this.setTitle(bundle.getString("CreateMovieGUI.headline.edit"));
        //GUI mit Werten belegen
        jCheckBox1.setSelected(outdatedMovie.isWatched());                                  //Setzte gesehen Flag
        if(genreKeyAndLocaleBundle.containsKey(outdatedMovie.getVideoCodec())){
            jComboBox9.setSelectedItem(genreKeyAndLocaleBundle.getString(outdatedMovie.getVideoCodec()));    //Setzte VideoCodec
        }        
        if(genreKeyAndLocaleBundle.containsKey(outdatedMovie.getAudioCodec())){
            jComboBox8.setSelectedItem(genreKeyAndLocaleBundle.getString(outdatedMovie.getAudioCodec()));    //Setzte AudioCodec
        }        
        if(outdatedMovie.getVideoBitrate() != 0){                                           //Setzte VideoBitrate
            jTextField13.setText(Integer.toString(outdatedMovie.getVideoBitrate()));
        }
        if(outdatedMovie.getAudioBitrate() != 0){                                           //Setzte AudioBitrate
            jTextField12.setText(Integer.toString(outdatedMovie.getAudioBitrate()));
        }
        jComboBox7.setSelectedItem(Integer.toString(outdatedMovie.getAudioChannels()));     //Setzte Audio Kanäle
        
        if(outdatedMovie.getAverageFPS() > 1){
            jTextField14.setText(Float.toString(outdatedMovie.getAverageFPS()));            //Setze FPS
        }
        if((outdatedMovie.getWidth() != 0) && (outdatedMovie.getHeight() != 0)){            //Setze Auflösung
            jTextField15.setText(Integer.toString(outdatedMovie.getWidth()));
            jTextField18.setText(Integer.toString(outdatedMovie.getHeight()));
        }
        if(genreKeyAndLocaleBundle.containsKey(outdatedMovie.getVideoSource())){
            jComboBox6.setSelectedItem(genreKeyAndLocaleBundle.getString(outdatedMovie.getVideoSource()));   //Setzte Quelle
        }       
        
        for(int i = 0; i < 3; i ++){                                                        //Setzte 3 Dateipfade
            String filePath = outdatedMovie.getFilePath(i); 
            if(filePath != null){
                if(i == 0){
                    jTextField16.setText(filePath);
                }
                else if(i == 1){
                    jTextField17.setText(filePath);                    
                }
                else if(i == 2){
                    jTextField20.setText(filePath);
                }
                this.filePaths.add(filePath);
            }
        }
    }
    
    /** 
     * Create an GUI to create a new movie
     * 
     * @see javax.swing.JDialog#JDialog(java.awt.Frame, boolean)
     * @param collectionEditable true if the collection can be changed <br/> false otherwise (i.E. episodes cannot change the collection)
     */
    public CreateMovieGUI(java.awt.Frame parent, boolean modal, boolean collectionEditable) {
        super(parent, modal, collectionEditable);
        init(parent);
    }

     /**
     * Zusammengeführte Initialisierung beider Konstruktoren
     * 
     * @param season Die Staffel
     */
    private void init(Frame parent){
        filePaths = new LinkedList<String>();
        videoCodecsListModel = new DefaultComboBoxModel();
        videoSourceListModel = new DefaultComboBoxModel();
        audioCodecListModel = new DefaultComboBoxModel();
        audioChannelListModel = new DefaultComboBoxModel();  
        this.localizedElementsMap = new HashMap<String, String>();

        videoCodec = "";
        videoBitrate = 0;
        audioCodec = "";
        audioBitrate = 0;
        audioChannels = 0;
        videoSource = "";
        //Füge die lokalisierten Werte den ListModels hinzu 
        this.addLocalizedElementsToModel(VideoInterface.VIDEO_CODECS, videoCodecsListModel);
        this.addLocalizedElementsToModel(SourceInterface.SOURCES, videoSourceListModel);
        this.addLocalizedElementsToModel(AudioInterface.AUDIO_CODECS, audioCodecListModel);
        this.addLocalizedElementsToModel(AudioInterface.AUDIO_CHANNELS, audioChannelListModel);

        initComponents();
        
        addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                //set watched if personal rating was changed
                if(evt.getPropertyName().equals(IPropertyChangeKeys.PERSONAL_RATING) && ((Double)evt.getNewValue()) > 0){
                    jCheckBox1.setSelected(true);
                }
            }
        });
        
        this.setLocationRelativeTo(parent);
        dialogCanceled = false;
       
       if(useCustomLAF){
            this.changeUI();
        }
                
        //Watched Checkbox
//        super.getJPanel17().remove(4);
//        super.getJPanel17().remove(3);
       super.getJPanel17().add(jPanel21);
       super.getJPanel17().add(jLabel1);
        super.getJPanel17().add(jCheckBox1);
        super.getJPanel17().add(jPanel16);
        
        //Film Tab
        super.getTabbedPane().addTab(bundle.getString("CreateMovieGUI.jPanel11"), jPanel11);
        //File Info Button der GUI hinzufügen
        super.getButtonPanel().add(jButton4, 1);

        //Füge eigene Methode bei Fensterschließen hinzu
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                dialogCanceled = true;
                dispose();
            }
        });        
        
        lookForLibrary();        
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel11 = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jTextField13 = new javax.swing.JTextField();
        jComboBox9 = new javax.swing.JComboBox();
        jLabel33 = new javax.swing.JLabel();
        jTextField14 = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();
        jTextField15 = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        jTextField18 = new javax.swing.JTextField();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        jComboBox8 = new javax.swing.JComboBox();
        jLabel29 = new javax.swing.JLabel();
        jTextField12 = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jComboBox7 = new javax.swing.JComboBox();
        jPanel7 = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        filler4 = new javax.swing.Box.Filler(new java.awt.Dimension(12, 23), new java.awt.Dimension(12, 23), new java.awt.Dimension(12, 23));
        jLabel26 = new javax.swing.JLabel();
        jComboBox6 = new javax.swing.JComboBox();
        jPanel4 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(12, 23), new java.awt.Dimension(12, 23), new java.awt.Dimension(12, 23));
        jLabel37 = new javax.swing.JLabel();
        jTextField16 = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jButton6 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(12, 23), new java.awt.Dimension(12, 23), new java.awt.Dimension(12, 23));
        jLabel38 = new javax.swing.JLabel();
        jTextField17 = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jButton7 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(12, 23), new java.awt.Dimension(12, 23), new java.awt.Dimension(12, 23));
        jLabel41 = new javax.swing.JLabel();
        jTextField20 = new javax.swing.JTextField();
        jPanel10 = new javax.swing.JPanel();
        jButton8 = new javax.swing.JButton();
        jPanel18 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jButton4 = new javax.swing.JButton();
        jCheckBox1 = new javax.swing.JCheckBox();
        jPanel16 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();

        jPanel11.setOpaque(false);
        jPanel11.setPreferredSize(new java.awt.Dimension(600, 450));
        jPanel11.setLayout(new javax.swing.BoxLayout(jPanel11, javax.swing.BoxLayout.PAGE_AXIS));

        jPanel19.setOpaque(false);
        jPanel19.setLayout(new javax.swing.BoxLayout(jPanel19, javax.swing.BoxLayout.LINE_AXIS));

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("jMovieManager/swing/resources/MovieManager"); // NOI18N
        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(null, ColorInterface.list_tree_BorderShadow), bundle.getString("CreateMovieGUI.jPanel12"))); // NOI18N
        jPanel12.setOpaque(false);
        jPanel12.setPreferredSize(new java.awt.Dimension(280, 150));

        jLabel28.setText(bundle.getString("CreateMovieGUI.jLabel28")); // NOI18N

        jLabel31.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel31.setText(bundle.getString("CreateMovieGUI.jLabel31")); // NOI18N
        jLabel31.setMaximumSize(new java.awt.Dimension(70, 20));
        jLabel31.setMinimumSize(new java.awt.Dimension(70, 20));
        jLabel31.setPreferredSize(new java.awt.Dimension(70, 20));

        jTextField13.setDocument(new MaxLengthDocument(6, MaxLengthDocument.INTEGER));
        jTextField13.setPreferredSize(new java.awt.Dimension(100, 20));

        jComboBox9.setModel(videoCodecsListModel);
        jComboBox9.setMinimumSize(new java.awt.Dimension(90, 20));
        jComboBox9.setPreferredSize(new java.awt.Dimension(120, 20));

        jLabel33.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel33.setText(bundle.getString("CreateMovieGUI.jLabel33")); // NOI18N
        jLabel33.setMaximumSize(new java.awt.Dimension(70, 20));
        jLabel33.setMinimumSize(new java.awt.Dimension(70, 20));
        jLabel33.setPreferredSize(new java.awt.Dimension(70, 20));

        jTextField14.setDocument(new MaxLengthDocument(6, MaxLengthDocument.DOUBLE));
        jTextField14.setPreferredSize(new java.awt.Dimension(100, 20));

        jLabel34.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel34.setText(bundle.getString("CreateMovieGUI.jLabel34")); // NOI18N
        jLabel34.setMaximumSize(new java.awt.Dimension(70, 20));
        jLabel34.setMinimumSize(new java.awt.Dimension(70, 20));
        jLabel34.setPreferredSize(new java.awt.Dimension(70, 20));

        jTextField15.setDocument(new MaxLengthDocument(4, MaxLengthDocument.INTEGER));
        jTextField15.setPreferredSize(new java.awt.Dimension(100, 20));

        jLabel35.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel35.setText(bundle.getString("CreateMovieGUI.jLabel35")); // NOI18N
        jLabel35.setMaximumSize(new java.awt.Dimension(70, 20));
        jLabel35.setMinimumSize(new java.awt.Dimension(70, 20));
        jLabel35.setPreferredSize(new java.awt.Dimension(70, 20));

        jTextField18.setDocument(new MaxLengthDocument(4, MaxLengthDocument.INTEGER));
        jTextField18.setPreferredSize(new java.awt.Dimension(100, 20));

        jLabel39.setText("x");

        jLabel40.setText(bundle.getString("CreateMovieGUI.jLabel39")); // NOI18N

        jLabel42.setText(bundle.getString("CreateMovieGUI.jLabel42")); // NOI18N

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField15, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel39)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField18, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel40))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel12Layout.createSequentialGroup()
                                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jComboBox9, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jTextField13, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(jPanel12Layout.createSequentialGroup()
                                .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel42)
                            .addComponent(jLabel28))))
                .addContainerGap(58, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel28))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel42))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel39)
                    .addComponent(jTextField18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel40))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jPanel19.add(jPanel12);

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(null, ColorInterface.list_tree_BorderShadow), bundle.getString("CreateMovieGUI.jPanel6"))); // NOI18N
        jPanel6.setOpaque(false);
        jPanel6.setPreferredSize(new java.awt.Dimension(280, 150));

        jLabel32.setText(bundle.getString("CreateMovieGUI.jLabel32")); // NOI18N

        jComboBox8.setModel(audioCodecListModel);
        jComboBox8.setPreferredSize(new java.awt.Dimension(120, 20));

        jLabel29.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel29.setText(bundle.getString("CreateMovieGUI.jLabel29")); // NOI18N
        jLabel29.setMaximumSize(new java.awt.Dimension(70, 20));
        jLabel29.setMinimumSize(new java.awt.Dimension(70, 20));
        jLabel29.setPreferredSize(new java.awt.Dimension(70, 20));

        jTextField12.setDocument(new MaxLengthDocument(6, MaxLengthDocument.INTEGER));
        jTextField12.setPreferredSize(new java.awt.Dimension(100, 20));

        jLabel30.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel30.setText(bundle.getString("CreateMovieGUI.jLabel30")); // NOI18N
        jLabel30.setMaximumSize(new java.awt.Dimension(70, 20));
        jLabel30.setMinimumSize(new java.awt.Dimension(70, 20));
        jLabel30.setPreferredSize(new java.awt.Dimension(70, 20));

        jLabel27.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel27.setText(bundle.getString("CreateMovieGUI.jLabel27")); // NOI18N
        jLabel27.setMaximumSize(new java.awt.Dimension(70, 20));
        jLabel27.setMinimumSize(new java.awt.Dimension(70, 20));
        jLabel27.setPreferredSize(new java.awt.Dimension(70, 20));

        jComboBox7.setModel(audioChannelListModel);
        jComboBox7.setPreferredSize(new java.awt.Dimension(120, 20));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jComboBox7, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextField12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel32)))
                .addContainerGap(58, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel32))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(43, Short.MAX_VALUE))
        );

        jPanel19.add(jPanel6);

        jPanel11.add(jPanel19);

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(null, ColorInterface.list_tree_BorderShadow), bundle.getString("CreateMovieGUI.jPanel7"))); // NOI18N
        jPanel7.setOpaque(false);
        jPanel7.setLayout(new javax.swing.BoxLayout(jPanel7, javax.swing.BoxLayout.PAGE_AXIS));

        jPanel20.setMaximumSize(new java.awt.Dimension(32767, 45));
        jPanel20.setMinimumSize(new java.awt.Dimension(0, 45));
        jPanel20.setOpaque(false);
        jPanel20.setPreferredSize(new java.awt.Dimension(588, 45));
        jPanel20.setLayout(new javax.swing.BoxLayout(jPanel20, javax.swing.BoxLayout.LINE_AXIS));
        jPanel20.add(filler4);

        jLabel26.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel26.setText(bundle.getString("CreateMovieGUI.jLabel26")); // NOI18N
        jLabel26.setMaximumSize(new java.awt.Dimension(81, 20));
        jLabel26.setMinimumSize(new java.awt.Dimension(81, 20));
        jLabel26.setPreferredSize(new java.awt.Dimension(81, 20));
        jPanel20.add(jLabel26);

        jComboBox6.setModel(videoSourceListModel);
        jComboBox6.setMaximumSize(new java.awt.Dimension(130, 20));
        jComboBox6.setMinimumSize(new java.awt.Dimension(130, 20));
        jComboBox6.setPreferredSize(new java.awt.Dimension(130, 20));
        jPanel20.add(jComboBox6);

        jPanel4.setMaximumSize(new java.awt.Dimension(32767, 20));
        jPanel4.setMinimumSize(new java.awt.Dimension(5, 20));
        jPanel4.setOpaque(false);
        jPanel4.setPreferredSize(new java.awt.Dimension(350, 20));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 365, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        jPanel20.add(jPanel4);

        jPanel7.add(jPanel20);

        jPanel13.setMaximumSize(new java.awt.Dimension(2147483647, 23));
        jPanel13.setMinimumSize(new java.awt.Dimension(100, 23));
        jPanel13.setOpaque(false);
        jPanel13.setPreferredSize(new java.awt.Dimension(570, 23));
        jPanel13.setLayout(new javax.swing.BoxLayout(jPanel13, javax.swing.BoxLayout.LINE_AXIS));
        jPanel13.add(filler1);

        jLabel37.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel37.setText(bundle.getString("CreateMovieGUI.jLabel37")); // NOI18N
        jLabel37.setMaximumSize(new java.awt.Dimension(81, 20));
        jLabel37.setMinimumSize(new java.awt.Dimension(81, 20));
        jLabel37.setPreferredSize(new java.awt.Dimension(81, 20));
        jPanel13.add(jLabel37);

        jTextField16.setMinimumSize(new java.awt.Dimension(1, 20));
        jTextField16.setPreferredSize(new java.awt.Dimension(400, 20));
        jPanel13.add(jTextField16);

        jPanel3.setMaximumSize(new java.awt.Dimension(5, 20));
        jPanel3.setMinimumSize(new java.awt.Dimension(5, 20));
        jPanel3.setOpaque(false);
        jPanel3.setPreferredSize(new java.awt.Dimension(5, 20));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        jPanel13.add(jPanel3);

        jButton6.setText(bundle.getString("CreateMovieGUI.jButton6")); // NOI18N
        jButton6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel13.add(jButton6);

        jPanel5.setMaximumSize(new java.awt.Dimension(5, 20));
        jPanel5.setMinimumSize(new java.awt.Dimension(5, 20));
        jPanel5.setOpaque(false);
        jPanel5.setPreferredSize(new java.awt.Dimension(5, 20));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        jPanel13.add(jPanel5);

        jPanel7.add(jPanel13);

        jPanel15.setMaximumSize(new java.awt.Dimension(32767, 10));
        jPanel15.setMinimumSize(new java.awt.Dimension(0, 10));
        jPanel15.setOpaque(false);
        jPanel15.setPreferredSize(new java.awt.Dimension(588, 10));

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 588, Short.MAX_VALUE)
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        jPanel7.add(jPanel15);

        jPanel14.setMaximumSize(new java.awt.Dimension(2147483647, 23));
        jPanel14.setMinimumSize(new java.awt.Dimension(100, 23));
        jPanel14.setOpaque(false);
        jPanel14.setLayout(new javax.swing.BoxLayout(jPanel14, javax.swing.BoxLayout.LINE_AXIS));
        jPanel14.add(filler2);

        jLabel38.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel38.setText(bundle.getString("CreateMovieGUI.jLabel37")); // NOI18N
        jLabel38.setMaximumSize(new java.awt.Dimension(81, 20));
        jLabel38.setMinimumSize(new java.awt.Dimension(81, 20));
        jLabel38.setPreferredSize(new java.awt.Dimension(81, 20));
        jPanel14.add(jLabel38);

        jTextField17.setMinimumSize(new java.awt.Dimension(1, 20));
        jTextField17.setPreferredSize(new java.awt.Dimension(400, 20));
        jPanel14.add(jTextField17);

        jPanel1.setMaximumSize(new java.awt.Dimension(5, 20));
        jPanel1.setMinimumSize(new java.awt.Dimension(5, 20));
        jPanel1.setOpaque(false);
        jPanel1.setPreferredSize(new java.awt.Dimension(5, 20));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        jPanel14.add(jPanel1);

        jButton7.setText(bundle.getString("CreateMovieGUI.jButton6")); // NOI18N
        jButton7.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jPanel14.add(jButton7);

        jPanel2.setMaximumSize(new java.awt.Dimension(5, 20));
        jPanel2.setMinimumSize(new java.awt.Dimension(5, 20));
        jPanel2.setOpaque(false);
        jPanel2.setPreferredSize(new java.awt.Dimension(5, 20));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        jPanel14.add(jPanel2);

        jPanel7.add(jPanel14);

        jPanel9.setMaximumSize(new java.awt.Dimension(32767, 10));
        jPanel9.setMinimumSize(new java.awt.Dimension(0, 10));
        jPanel9.setOpaque(false);
        jPanel9.setPreferredSize(new java.awt.Dimension(588, 10));

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 588, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        jPanel7.add(jPanel9);

        jPanel17.setMaximumSize(new java.awt.Dimension(2147483647, 23));
        jPanel17.setMinimumSize(new java.awt.Dimension(100, 23));
        jPanel17.setOpaque(false);
        jPanel17.setLayout(new javax.swing.BoxLayout(jPanel17, javax.swing.BoxLayout.LINE_AXIS));
        jPanel17.add(filler3);

        jLabel41.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel41.setText(bundle.getString("CreateMovieGUI.jLabel37")); // NOI18N
        jLabel41.setMaximumSize(new java.awt.Dimension(81, 20));
        jLabel41.setMinimumSize(new java.awt.Dimension(81, 20));
        jLabel41.setPreferredSize(new java.awt.Dimension(81, 20));
        jPanel17.add(jLabel41);

        jTextField20.setMinimumSize(new java.awt.Dimension(1, 20));
        jTextField20.setPreferredSize(new java.awt.Dimension(400, 20));
        jPanel17.add(jTextField20);

        jPanel10.setMaximumSize(new java.awt.Dimension(5, 20));
        jPanel10.setMinimumSize(new java.awt.Dimension(5, 20));
        jPanel10.setOpaque(false);

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        jPanel17.add(jPanel10);

        jButton8.setText(bundle.getString("CreateMovieGUI.jButton6")); // NOI18N
        jButton8.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        jPanel17.add(jButton8);

        jPanel18.setMaximumSize(new java.awt.Dimension(5, 20));
        jPanel18.setMinimumSize(new java.awt.Dimension(5, 20));
        jPanel18.setOpaque(false);

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        jPanel17.add(jPanel18);

        jPanel7.add(jPanel17);

        jPanel8.setMinimumSize(new java.awt.Dimension(0, 0));
        jPanel8.setOpaque(false);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 588, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 142, Short.MAX_VALUE)
        );

        jPanel7.add(jPanel8);

        jPanel11.add(jPanel7);

        jButton4.setText(bundle.getString("CreateMovieGUI.jButton4")); // NOI18N
        jButton4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton4.setMaximumSize(new java.awt.Dimension(100, 25));
        jButton4.setMinimumSize(new java.awt.Dimension(100, 25));
        jButton4.setPreferredSize(new java.awt.Dimension(100, 25));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jCheckBox1.setContentAreaFilled(false);
        jCheckBox1.setMargin(new java.awt.Insets(2, 0, 2, 2));
        jCheckBox1.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        jCheckBox1.setMinimumSize(new java.awt.Dimension(20, 20));
        jCheckBox1.setPreferredSize(new java.awt.Dimension(20, 20));

        jPanel16.setMinimumSize(new java.awt.Dimension(130, 20));
        jPanel16.setOpaque(false);
        jPanel16.setPreferredSize(new java.awt.Dimension(130, 20));

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 120, Short.MAX_VALUE)
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        jPanel21.setMaximumSize(new java.awt.Dimension(70, 20));
        jPanel21.setMinimumSize(new java.awt.Dimension(70, 20));
        jPanel21.setOpaque(false);

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 70, Short.MAX_VALUE)
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setLabelFor(jCheckBox1);
        jLabel1.setText(bundle.getString("AbstractCreateGUI.jCheckbox1")); // NOI18N
        jLabel1.setMaximumSize(new java.awt.Dimension(70, 20));
        jLabel1.setMinimumSize(new java.awt.Dimension(68, 20));
        jLabel1.setPreferredSize(new java.awt.Dimension(70, 20));

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(bundle.getString("CreateMovieGUI.headline")); // NOI18N

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Auswahl des Dateipfades
     *
     * @param evt Event, welches ausgelöst wurde
     */  
private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
    openJFileChooser(jTextField16);
    //TODO: Verify input filepath --> File not found --> Message and delete
}//GEN-LAST:event_jButton6ActionPerformed

    /**
     * Datei Info
     * 
     * Sucht im Fileheader nach Informationen zum Titel,
     * sofern ein Dateipfad angegeben wurde
     *
     * @param evt Event, welches ausgelöst wurde
     */
private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
    LinkedList<String> localFilePaths = new LinkedList<String>();
    if(!jTextField16.getText().isEmpty()){
        localFilePaths.add(jTextField16.getText());
    }
    if(!jTextField17.getText().isEmpty()){
        localFilePaths.add(jTextField17.getText());
    }
    if(!jTextField20.getText().isEmpty()){
        localFilePaths.add(jTextField20.getText());
    }
    
    if(!localFilePaths.isEmpty()){
        Movie tempMovie = new Movie("mediaInfo");
        MediaInfo.getInstance().analyseFileHeader(tempMovie, localFilePaths.get(0));
        clearData();
//            Movie tempMovie = Controller.getInstance().analyseFileHeader(localFilePaths);
        
//            if(tempMovie.getPlaytime() != 0){
//                jTextField6.setText(Integer.toString(tempMovie.getPlaytime()));
//            }

            if(!tempMovie.getVideoCodec().isEmpty() && (genreKeyAndLocaleBundle.containsKey(tempMovie.getVideoCodec()))){
                jComboBox9.setSelectedItem(genreKeyAndLocaleBundle.getString(tempMovie.getVideoCodec()));
            }
            if(tempMovie.getVideoBitrate() != 0){
                jTextField13.setText(Integer.toString(tempMovie.getVideoBitrate()));
            }
            if(!tempMovie.getAudioCodec().isEmpty() && (genreKeyAndLocaleBundle.containsKey(tempMovie.getAudioCodec()))){
               jComboBox8.setSelectedItem(genreKeyAndLocaleBundle.getString(tempMovie.getAudioCodec()));
            }
            if(tempMovie.getAudioBitrate() != 0){
                jTextField12.setText(Integer.toString(tempMovie.getAudioBitrate()));
            }
            if(tempMovie.getAudioChannels() != 0){
                jComboBox7.setSelectedItem(String.valueOf(tempMovie.getAudioChannels()));
            }
            if(tempMovie.getAverageFPS() > 1){
                jTextField14.setText(String.valueOf(tempMovie.getAverageFPS()));
            }            
            if(tempMovie.getWidth() != 0){
                jTextField15.setText(String.valueOf(tempMovie.getWidth()));
            }              
            if(tempMovie.getHeight() != 0){
                jTextField18.setText(String.valueOf(tempMovie.getHeight()));
            }               
        }else{
            javax.swing.JOptionPane.showMessageDialog(this, bundle.getString("CreateMovieGUI.error.pathLeft"));
        }
}//GEN-LAST:event_jButton4ActionPerformed

private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
    openJFileChooser(jTextField17);
}//GEN-LAST:event_jButton7ActionPerformed

private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
    openJFileChooser(jTextField20);
}//GEN-LAST:event_jButton8ActionPerformed

    /**
     * Methode welche dem jeweiligen DefaultListModel die lokalisierten Werte aus der 
     * MovieManager.properties Datei hinzufügt
     * 
     * @param keys Die keys für die lokalisierten Werte
     * @param modelToAddValues Das Model, dem die lokalisierten Werte hinzugefügt werden sollen
     */
    final protected void addLocalizedElementsToModel(String[] keys, DefaultComboBoxModel modelToAddValues){
        for(String key: keys){
            //Füge die Elemente einer Hashmap hinzu, damit nach Auswahl nicht Lokalisierte Wert gespeichert werden kann 
            localizedElementsMap.put(genreKeyAndLocaleBundle.getString(key), key);
            modelToAddValues.addElement(genreKeyAndLocaleBundle.getString(key));
        }       
    }    
    
    /**
     * Macht die GUI sichtbar und gibt das neu erstellte Filmobjekt zurück
     * 
     * @return movie Das erstellte bzw. aktualisierte Movie Objekt<br/>null, wenn der Dialog abgebrochen wurde
     */
    public Movie showGUI(){
        this.setVisible(true);
        Movie movie = null;

        //nach Dispose Aufruf
        if(!dialogCanceled){
            if((oldCollection != null) && (oldCollection.get(oldTitle) != null)){
                //update existing movie, same collection
                movie = (Movie)oldCollection.get(oldTitle);
            }
            else if(collection.get(oldTitle) != null){
                //update existing movie, new collection
                movie = (Movie)collection.get(oldTitle);
//                oldTitle = "";
            }else{
                //create new movie
                movie = new Movie(title);
            }       
            updateMovieData(movie);
            movie = DataManager.INSTANCE.updateMovie((MovieCollection)collection, movie, title);
        }    
        return movie;
    }
    
    /**
     * Updates all attributes from the movie
     * @param movie 
     */
    protected void updateMovieData(Movie movie){
        super.updateLocalVideoFileData(movie);
        
        movie.setVideoCodec(videoCodec);
        movie.setVideoBitrate(videoBitrate);
        movie.setAudioCodec(audioCodec);
        movie.setAudioBitrate(audioBitrate);
        movie.setAudioChannels(audioChannels);
        movie.setVideoSource(videoSource);
        movie.setAverageFPS(averageFPS);
        movie.setWidth(width);
        movie.setHeight(height);
  
        movie.setFilePaths(filePaths);

        movie.setWatched(watched);
    }
    
     /**
     * removes all data from the gui
     */
    protected void clearData(){
        jTextField12.setText("");
        jTextField13.setText("");
        jTextField14.setText("");
        jTextField15.setText("");
//        jTextField16.setText("");     //FilePath
//        jTextField17.setText("");//FilePath
        jTextField18.setText("");
//        jTextField20.setText("");//FilePath

//        jComboBox6.setSelectedIndex(-1); //Source
        jComboBox7.setSelectedIndex(-1);
        jComboBox8.setSelectedIndex(-1);
        jComboBox9.setSelectedIndex(-1);
        jCheckBox1.setSelected(false);
    }
            
    /**
     * Öffnet ein JFileChooser Dialog und schriebt die geöffnete Datei in das vorgegebene TextFeld
     * 
     * @param fieldToEnterFilePath Das jTextField, in das der geöffnete Dateipfad geschrieben werden soll
     */
    protected void openJFileChooser(JTextField fieldToEnterFilePath){
        MyJFileChooser filePathChooser = new MyJFileChooser(fieldToEnterFilePath.getText(), JMMFileFilter.FilterType.VIDEO_EXTENSION);
        //Mehrfachauswahl erlaubt
        filePathChooser.setMultiSelectionEnabled(false);

        //zeige Dialog und mache etwas, wenn "Öffnen" geklickt wurde
        if(filePathChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){            
            File filePath = filePathChooser.getSelectedFile();            
            fieldToEnterFilePath.setText(filePath.getAbsolutePath());
        }        
    }

    @Override
    protected LocalVideoFile searchForTitle(String title){
        List<TMDBVideoFile> searchResults = new TMDBSearchWrapper().searchMovie(title, null, LocaleManager.getInstance().getCurrentLocale().getLanguage(), true, null, 10);
        TMDBVideoFile result = null;
        if(!searchResults.isEmpty()){
            ChooseMediaFileDialog mediaFileSelection = new ChooseMediaFileDialog(MovieManagerGUI.getInstance(), true, title, searchResults);
            result = (TMDBVideoFile)mediaFileSelection.showDialog();
            if(result != null){ 
                //Get detailed information about the choosen result
                result = new TMDBMovieWrapper().findMovie(result.getTmdbID(), LocaleManager.getInstance().getCurrentLocale().getLanguage(), "casts");
            }
            else if(!mediaFileSelection.isCanceled()){
                javax.swing.JOptionPane.showMessageDialog(this, bundle.getString("AbstractCreateGUI.error.titleNotFound"));
            }
        }
        return result;
    }
    
    /**
     * Aktion die ausgeführt werden soll, nachdem der Start Button gedrückt wurde
     * 
     * @param evt Das Event, welches ausgelöst wurde
     */
    @Override
    protected void startButtonPressed(java.awt.event.ActionEvent evt) throws IllegalArgumentException{
        super.startButtonPressed(evt);
        
        //Film spezifische Aktionen ausführen
        if(!jTextField13.getText().isEmpty()){
            videoBitrate = Integer.parseInt(jTextField13.getText());
        }
        if(!jTextField12.getText().isEmpty()){
            audioBitrate = Integer.parseInt(jTextField12.getText());
        }
        if(!jTextField14.getText().isEmpty()){
            try{
                averageFPS = Float.parseFloat(jTextField14.getText());
            }catch(NumberFormatException ex){
                javax.swing.JOptionPane.showMessageDialog(this, bundle.getString("CreateMovieGUI.error.averageFPS"));      
                throw new IllegalArgumentException();
            }
        }
        if(!jTextField15.getText().isEmpty()){
            width = Integer.parseInt(jTextField15.getText());
        }        
        if(!jTextField18.getText().isEmpty()){
            height = Integer.parseInt(jTextField18.getText());
        }          
        
        //ComboBoxen
        if(jComboBox9.getSelectedIndex() >= 0){
            videoCodec = localizedElementsMap.get(jComboBox9.getSelectedItem().toString());
        }
        if(jComboBox8.getSelectedIndex() >= 0){
            audioCodec = localizedElementsMap.get(jComboBox8.getSelectedItem().toString());
        }
        if(jComboBox7.getSelectedIndex() >= 0){
            try{
                audioChannels = Integer.parseInt(jComboBox7.getSelectedItem().toString());
            }
            catch(NumberFormatException ex){    //"keine Angabe" oder "undefined"
                audioChannels = 0;
            }
        }
        if(jComboBox6.getSelectedIndex() >= 0){
            videoSource = localizedElementsMap.get(jComboBox6.getSelectedItem().toString());
        }
        
        //FilePaths
        filePaths.clear();
        if(!jTextField16.getText().isEmpty()){
            filePaths.add(jTextField16.getText());
        }
        if(!jTextField17.getText().isEmpty()){
            filePaths.add(jTextField17.getText());
        }
        if(!jTextField20.getText().isEmpty()){
            filePaths.add(jTextField20.getText());
        } 
        if(jCheckBox1.isSelected()){
            watched = true;
        }
        else{
            watched = false;
        }
    }
        
    /**
    * Sucht nach der Mediainfo Library und deaktiiviert
    * Import Optionen, wenn diese nicht gefunden wurde
    **/
    final protected void lookForLibrary(){       
         if(!OperatingSystem.lookForLibrary()){
             jButton4.setEnabled(false);            
         }
    }
    

    @Override
    protected boolean isInternetAndAPIAvailable(){      
        return (Utils.isInternetConnectionAvailable() && new TMDBMovieWrapper().isAPIenabled());
    }
    
    /**
    * Methode zum Anpassen der GUI an das neue Look and Feel
    **/
    @Override
    public void changeUI(){
        super.changeUI();
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler filler3;
    private javax.swing.Box.Filler filler4;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JComboBox jComboBox6;
    private javax.swing.JComboBox jComboBox7;
    private javax.swing.JComboBox jComboBox8;
    private javax.swing.JComboBox jComboBox9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    protected javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JTextField jTextField12;
    private javax.swing.JTextField jTextField13;
    private javax.swing.JTextField jTextField14;
    private javax.swing.JTextField jTextField15;
    private javax.swing.JTextField jTextField16;
    private javax.swing.JTextField jTextField17;
    private javax.swing.JTextField jTextField18;
    private javax.swing.JTextField jTextField20;
    // End of variables declaration//GEN-END:variables
}
