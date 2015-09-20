/**
 * Copyright (c) 2010-2015 Bryan Beck.
 * All rights reserved.
 * 
 * This project is licensed under LGPL v2.1.
 * See jMovieManager-license.txt for details.
 * 
 */
package jMovieManager.swing.gui.createmedia;

import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.*;
import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import jmm.data.Actor;
import jmm.data.LocalVideoFile;
import jmm.data.VideoFile; 
import jmm.data.collection.MediaCollection;
import jmm.data.collection.MovieCollection;
import jmm.data.collection.SerieCollection;
import jmm.interfaces.GenreKeysInterface;
import jMovieManager.swing.gui.ColorInterface;
import jMovieManager.swing.gui.IPropertyChangeKeys;
import jMovieManager.swing.gui.UIInterface;
import jMovieManager.swing.gui.UpdateGUI;
import jMovieManager.swing.gui.components.MyJFileChooser;
import jMovieManager.swing.gui.other.JMMFileFilter;
import jmm.data.VideoFile.FSK;
import jMovieManager.swing.gui.components.ui.MyBasicMenuButtonUI;
import javax.swing.table.DefaultTableModel;
import jmm.utils.LocaleManager;
import jmm.utils.MaxLengthDocument;
import jmm.utils.PictureManager;
import jmm.utils.Settings;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;

/**
 * GUI to create or edit a videoFile
 * 
 * @author Bryan Beck
 * @since 20.10.2010
 */
public abstract class AbstractCreateGUI extends javax.swing.JDialog implements GenreKeysInterface, UIInterface, ColorInterface{
    /** Logger. */
    private static final Logger LOG = LoggerFactory.logger(AbstractCreateGUI.class);
    
    protected String title;
    protected String originalTitle;
    protected Set<String> genreKeys;
    protected int    playtime;
    protected int    releaseYear;
    protected String description;
    protected String publisher;
    protected Set<Actor> actors;
    protected String director;
    protected FSK fsk;
    protected String imagePath;
    protected ImageIcon image;
    protected String trailerPath;
    protected double imdbRating;
    protected double personalRating;
    protected Map<String, String> customAttributes;

    protected String oldTitle;
    protected MediaCollection oldCollection;
    protected MediaCollection collection;

    private DefaultListModel genresListModel;
    private DefaultListModel actorsListModel;
    private DefaultComboBoxModel collectionModel;
    private DefaultTableModel tableModel;
    
    //localizedGenreString, genreKey
    protected Map<String, String> localizedGenresMap;
    
    protected ResourceBundle bundle;   
    protected ResourceBundle genreKeyAndLocaleBundle;    
    
   /** 
     * Create an prefilled GUI to edit a mediaFile
     * 
     * @see javax.swing.JDialog#JDialog(java.awt.Frame, boolean)
     * @param outdatedVideoFile  the file to edit
     * @param collection the collection which contains the outdatedVideoFile
     * @param collectionEditable true if the collection can be changed <br/> false otherwise (i.E. episodes cannot change the collection)
     */
    public AbstractCreateGUI(java.awt.Frame parent, boolean modal, final LocalVideoFile outdatedVideoFile, final MediaCollection collection, boolean collectionEditable){
        this(parent, modal, collectionEditable);
        
        this.oldCollection = collection;
        collectionModel.setSelectedItem(collection);
        
        //Aktualisiere die Komponenten
        jTextField1.setText(outdatedVideoFile.getTitle());
        jTextField2.setText(outdatedVideoFile.getOriginalTitle());
        jTextArea1.setText(outdatedVideoFile.getDescription());

        this.setSelectedGenres(outdatedVideoFile);

        if(outdatedVideoFile.getPlaytime() != 0){
            jTextField6.setText(Integer.toString(outdatedVideoFile.getPlaytime()));
        }
        if(outdatedVideoFile.getReleaseYear() != 0){
            jTextField5.setText(Integer.toString(outdatedVideoFile.getReleaseYear()));
        }
        FSK localfsk = outdatedVideoFile.getFsk();
        if(localfsk == FSK.FSK_0){
            jButton2ActionPerformed(null);
        }
        else if(localfsk == FSK.FSK_6){
            jButton6ActionPerformed(null);
        }
        else if(localfsk == FSK.FSK_12){
            jButton7ActionPerformed(null);
        }
        else if(localfsk == FSK.FSK_16){
            jButton8ActionPerformed(null);
        }
        else if(localfsk == FSK.FSK_18){
            jButton9ActionPerformed(null);
        }
        this.fsk = localfsk;

        //Grafik aktualisiern
        jLabel38.setText("");
        if(null != outdatedVideoFile.getImage() || (!outdatedVideoFile.getImagePath().isEmpty())){
            jLabel38.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            Thread pictureThread = new Thread(new Runnable() {
                @Override
                 public void run() {
                    //Setze Image Pfad
                    imagePath = outdatedVideoFile.getImagePath();
                    image = outdatedVideoFile.getImage();
                     if((null != outdatedVideoFile.getImage()) && (outdatedVideoFile.getImage().getIconWidth() > 0) && (outdatedVideoFile.getImage().getIconHeight() > 0)){
                        jLabel38.setText("");
                        //ImageIcon to bufferedImage
                        Icon icon = outdatedVideoFile.getImage(); 
                        BufferedImage buffImage = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_RGB);
                        Graphics2D g2d = (Graphics2D) buffImage.getGraphics();
                        icon.paintIcon(null, g2d, 0, 0);
                        g2d.dispose();
//                        BufferedImage buffIMage = (BufferedImage)outdatedVideoFile.getImage().getImage();
                        jLabel38.setIcon(PictureManager.getScaledImage(buffImage, PictureManager.thumbnail_widht, PictureManager.thumbnail_height));
//                        jLabel38.setIcon(new ImageIcon(outdatedVideoFile.getImage().getImage().getScaledInstance(PictureManager.thumbnail_widht, PictureManager.thumbnail_height, Image.SCALE_SMOOTH)));
                     }
                     else{
                         Icon scaledIcon   = PictureManager.getScaledImage(outdatedVideoFile.getImagePath(),PictureManager.thumbnail_widht,PictureManager.thumbnail_height);
                         if((null != scaledIcon)){ 
                             jLabel38.setText("");
                             jLabel38.setIcon(scaledIcon);
                         }else{
                             jLabel38.setIcon(null);
                             jLabel38.setText(bundle.getString("AbstractCreateGUI.error.noPicture"));
                         }
                     }
                     jButton4.setEnabled(true);
                     jLabel38.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            });
            pictureThread.start();

        }else{
            jLabel38.setIcon(null);
            jLabel38.setText(bundle.getString("AbstractCreateGUI.error.noPicture"));
        }

        jTextField12.setText(outdatedVideoFile.getTrailerPath());
        if(outdatedVideoFile.getOnlineRating() == 10){
            jTextField3.setText("10");
        }else{
            jTextField3.setText(String.valueOf(outdatedVideoFile.getOnlineRating()));
        }
        if(outdatedVideoFile.getPersonalRating() == 10){
            jTextField9.setText("10");
        }else{
            jTextField9.setText(String.valueOf(outdatedVideoFile.getPersonalRating()));
        }

        //Dient zur Überprüfung, ob Movie schon vorhanden ist (beim drücken des Hinzufügen Buttons)
        oldTitle = outdatedVideoFile.getTitle();
        
        //Publisher
        if(!outdatedVideoFile.getPublisher().isEmpty()){
            jTextField7.setText(outdatedVideoFile.getPublisher());
        }
        //Director
        if(!outdatedVideoFile.getDirector().isEmpty()){
            jTextField8.setText(outdatedVideoFile.getDirector());
        }
        int i = 0;
        
        tableModel.getDataVector().removeAllElements();
        for (Iterator<String> it = outdatedVideoFile.getCustomAttributes().keySet().iterator(); it.hasNext();) {
            String key = it.next();
            String value = outdatedVideoFile.getCustomAttributes().get(key);
            tableModel.insertRow(i, new Object[]{key, value});
            i++;
        }        
        fillTableWithExistingKeys();
                       
        if(!outdatedVideoFile.getActors().isEmpty()){
            for(Actor actor: outdatedVideoFile.getActors()){
                actorsListModel.addElement(actor.getName());            
                //this.actors.addLast(actor);
            }
        }
        jList3.setSelectionInterval(0, actorsListModel.size()-1);
        jTextField1.requestFocusInWindow();
        
        //Button Name ändern
        jButton5.setText(bundle.getString("AbstractCreateGUI.jButton5.update"));
    }

    /** 
     * Creates an empty GUI to create a new video file
     * 
     * @see javax.swing.JDialog#JDialog(java.awt.Frame, boolean) 
     * @param collectionEditable true if the collection can be changed <br/> false otherwise (i.E. episodes cannot change the collection)
     */
    public AbstractCreateGUI(java.awt.Frame parent, boolean modal, boolean collectionEditable) {
        super(parent, modal);
        //Setzte Locales
        bundle = ResourceBundle.getBundle("jMovieManager.swing.resources.MovieManager"); 
        genreKeyAndLocaleBundle = LocaleManager.getInstance().getGenreKeyAndCodecBundle();

        genresListModel = new DefaultListModel();
        actorsListModel = new DefaultListModel();
        collectionModel = new DefaultComboBoxModel();
        initTableModel();
        this.genreKeys = new HashSet<String> ();
        this.actors = new HashSet<Actor> ();
        this.customAttributes = new HashMap<String, String>();
        this.localizedGenresMap = new TreeMap<String, String>(new Comparator<String>() {
                /**
                * Order key by name. A-Z
                * @param o1 - the first object to be compared.
                * @param o2 - the second object to be compared.
                *
                * @return a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater than the second.
                */
                @Override
                public int compare(String o1, String o2) {
                    return (o1.compareToIgnoreCase(o2));
                }
            }
        );
        
        this.addLocalizedElementsToModel(GENRE_KEYS, genresListModel);
        this.addCollectionsToComboboxModel();
        initComponents();
        
        initTable();
        if(!collectionEditable){
            jComboBox1.setEnabled(false);
        }
        
        fillTableWithExistingKeys();
        
        //GUI zentriert darstellen
        this.setLocationRelativeTo(parent);
        jTextField4.setForeground(UIManager.getColor("TextField.inactiveForeground"));

        oldTitle= "";
        //Initialisiere alle Variablen
        title= "";
        originalTitle = "";
        playtime = 0;
        releaseYear = 0;
        description = "";
        director = "";
        publisher = "";
//        fsk = ;
        imagePath = "";
        trailerPath = "";
//        image = null;
        imdbRating = 0;
        personalRating = 0;
        
        jTextField1.requestFocusInWindow();
        
        Thread disabledAPI = new Thread(new Runnable() {
            @Override
            public void run() {
                if(!isInternetAndAPIAvailable()){
                    jButton3.setEnabled(false);
                }
            }
        });
        disabledAPI.start();
    }
    
        /**
     * Initialisiert das Tabellen Model
     */
    private void initTableModel(){
        tableModel = new DefaultTableModel(
                new Object [][] {                  
                },
                new String [] {
                    
                    bundle.getString("AbstractCreateGUI.jTable1.column0"),
                    bundle.getString("AbstractCreateGUI.jTable1.column1")
                }
                ) {
                    Class[] types = new Class [] {
                        java.lang.String.class, java.lang.String.class
                    };
                    boolean[] canEdit = new boolean [] {
                        true, true
                    };
                    
            @Override
            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        };        
    }
    
    /**
     * Fills the jTable1 with existing CustomAttribute Keys. <br/>
     * Also adds an empty row at last.
     */
    private void fillTableWithExistingKeys(){
        Set<String> existingKeys = new HashSet<String>();
        for(int i = 0; i < tableModel.getRowCount(); i ++){
            Object key = tableModel.getValueAt(i, 0);
            if((key != null) && (!key.toString().trim().isEmpty())){
                existingKeys.add(key.toString());
            }
        }
        for(String key: Settings.getInstance().customAttributeKeys){
            if(!existingKeys.contains(key)){
                tableModel.addRow(new Object[]{key, ""});
            }
        }
        tableModel.addRow(new Object[]{"", ""});
    }
    
    /**
     * init method for table
     */
    private void initTable(){               
        jTable1.getTableHeader().setReorderingAllowed(false);
        
//        jTable1.getColumnModel().getColumn(0).setWidth(250);
//        jTable1.getColumnModel().getColumn(0).setPreferredWidth(250);
//        jTable1.getColumnModel().getColumn(1).setWidth(250);
//        jTable1.getColumnModel().getColumn(1).setPreferredWidth(250);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel33 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jPanel34 = new javax.swing.JPanel();
        jPanel31 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jPanel32 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jPanel10 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jPanel19 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jPanel20 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jPanel22 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jTextField12 = new javax.swing.JTextField();
        jPanel24 = new javax.swing.JPanel();
        jPanel25 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jPanel9 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jList3 = new javax.swing.JList();
        jTextField4 = new javax.swing.JTextField();
        jPanel18 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList();
        jPanel11 = new javax.swing.JPanel();
        jPanel26 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel35 = new javax.swing.JPanel();
        jPanel37 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jLabel38 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jPanel41 = new javax.swing.JPanel();
        jPanel39 = new javax.swing.JPanel();
        jComboBox1 = new javax.swing.JComboBox();
        jPanel40 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jTextField9 = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("jMovieManager/swing/resources/MovieManager"); // NOI18N
        setTitle(bundle.getString("AbstractCreateGUI.headline")); // NOI18N
        setMinimumSize(new java.awt.Dimension(800, 545));

        jTabbedPane1.setMinimumSize(new java.awt.Dimension(1, 1));
        jTabbedPane1.setPreferredSize(new java.awt.Dimension(600, 500));

        jPanel1.setOpaque(false);
        jPanel1.setPreferredSize(new java.awt.Dimension(600, 450));

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(null, ColorInterface.list_tree_BorderShadow), bundle.getString("AbstractCreateGUI.jPanel2"))); // NOI18N
        jPanel2.setMinimumSize(new java.awt.Dimension(1, 1));
        jPanel2.setOpaque(false);
        jPanel2.setPreferredSize(new java.awt.Dimension(550, 400));

        jPanel4.setMinimumSize(new java.awt.Dimension(1, 1));
        jPanel4.setOpaque(false);
        jPanel4.setPreferredSize(new java.awt.Dimension(520, 200));
        jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.PAGE_AXIS));

        jPanel33.setMinimumSize(new java.awt.Dimension(200, 20));
        jPanel33.setOpaque(false);
        jPanel33.setPreferredSize(new java.awt.Dimension(440, 20));
        jPanel33.setLayout(new javax.swing.BoxLayout(jPanel33, javax.swing.BoxLayout.LINE_AXIS));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setText(bundle.getString("AbstractCreateGUI.jLabel1")); // NOI18N
        jLabel1.setMaximumSize(new java.awt.Dimension(100, 20));
        jLabel1.setMinimumSize(new java.awt.Dimension(100, 20));
        jLabel1.setPreferredSize(new java.awt.Dimension(100, 20));
        jPanel33.add(jLabel1);

        jTextField1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTextField1.setMaximumSize(new java.awt.Dimension(2147483647, 20));
        jTextField1.setMinimumSize(new java.awt.Dimension(0, 20));
        jTextField1.setPreferredSize(new java.awt.Dimension(340, 20));
        jPanel33.add(jTextField1);

        jPanel4.add(jPanel33);

        jPanel34.setOpaque(false);
        jPanel34.setPreferredSize(new java.awt.Dimension(440, 5));

        javax.swing.GroupLayout jPanel34Layout = new javax.swing.GroupLayout(jPanel34);
        jPanel34.setLayout(jPanel34Layout);
        jPanel34Layout.setHorizontalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 543, Short.MAX_VALUE)
        );
        jPanel34Layout.setVerticalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 6, Short.MAX_VALUE)
        );

        jPanel4.add(jPanel34);

        jPanel31.setMinimumSize(new java.awt.Dimension(291, 20));
        jPanel31.setOpaque(false);
        jPanel31.setPreferredSize(new java.awt.Dimension(440, 20));
        jPanel31.setLayout(new javax.swing.BoxLayout(jPanel31, javax.swing.BoxLayout.LINE_AXIS));

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setText(bundle.getString("AbstractCreateGUI.jLabel2")); // NOI18N
        jLabel2.setMaximumSize(new java.awt.Dimension(100, 20));
        jLabel2.setMinimumSize(new java.awt.Dimension(100, 20));
        jLabel2.setPreferredSize(new java.awt.Dimension(100, 20));
        jPanel31.add(jLabel2);

        jTextField2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTextField2.setMaximumSize(new java.awt.Dimension(2147483647, 20));
        jTextField2.setMinimumSize(new java.awt.Dimension(0, 20));
        jTextField2.setPreferredSize(new java.awt.Dimension(340, 20));
        jPanel31.add(jTextField2);

        jPanel4.add(jPanel31);

        jPanel32.setOpaque(false);
        jPanel32.setPreferredSize(new java.awt.Dimension(440, 5));

        javax.swing.GroupLayout jPanel32Layout = new javax.swing.GroupLayout(jPanel32);
        jPanel32.setLayout(jPanel32Layout);
        jPanel32Layout.setHorizontalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 543, Short.MAX_VALUE)
        );
        jPanel32Layout.setVerticalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 6, Short.MAX_VALUE)
        );

        jPanel4.add(jPanel32);

        jPanel5.setOpaque(false);
        jPanel5.setPreferredSize(new java.awt.Dimension(450, 20));
        jPanel5.setLayout(new javax.swing.BoxLayout(jPanel5, javax.swing.BoxLayout.LINE_AXIS));

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel5.setText(bundle.getString("AbstractCreateGUI.jLabel5")); // NOI18N
        jLabel5.setMaximumSize(new java.awt.Dimension(100, 20));
        jLabel5.setMinimumSize(new java.awt.Dimension(100, 20));
        jLabel5.setPreferredSize(new java.awt.Dimension(100, 20));
        jPanel5.add(jLabel5);

        jTextField6.setDocument(new MaxLengthDocument(4, MaxLengthDocument.INTEGER));
        jTextField6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTextField6.setMaximumSize(new java.awt.Dimension(2147483647, 20));
        jTextField6.setMinimumSize(new java.awt.Dimension(50, 20));
        jTextField6.setPreferredSize(new java.awt.Dimension(50, 20));
        jPanel5.add(jTextField6);

        jPanel10.setMaximumSize(new java.awt.Dimension(3, 20));
        jPanel10.setMinimumSize(new java.awt.Dimension(3, 20));
        jPanel10.setOpaque(false);
        jPanel10.setPreferredSize(new java.awt.Dimension(3, 20));

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 3, Short.MAX_VALUE)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        jPanel5.add(jPanel10);

        jLabel16.setText(bundle.getString("AbstractCreateGUI.jLabel16")); // NOI18N
        jLabel16.setMaximumSize(new java.awt.Dimension(67, 20));
        jLabel16.setMinimumSize(new java.awt.Dimension(67, 20));
        jLabel16.setPreferredSize(new java.awt.Dimension(67, 20));
        jPanel5.add(jLabel16);

        jLabel21.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel21.setText(bundle.getString("AbstractCreateGUI.jLabel21")); // NOI18N
        jLabel21.setMaximumSize(new java.awt.Dimension(70, 20));
        jLabel21.setMinimumSize(new java.awt.Dimension(70, 20));
        jLabel21.setPreferredSize(new java.awt.Dimension(70, 20));
        jPanel5.add(jLabel21);

        jTextField7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTextField7.setMaximumSize(new java.awt.Dimension(2147483647, 20));
        jTextField7.setMinimumSize(new java.awt.Dimension(1, 1));
        jTextField7.setPreferredSize(new java.awt.Dimension(151, 20));
        jPanel5.add(jTextField7);

        jPanel4.add(jPanel5);

        jPanel19.setOpaque(false);
        jPanel19.setPreferredSize(new java.awt.Dimension(450, 5));

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 543, Short.MAX_VALUE)
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 6, Short.MAX_VALUE)
        );

        jPanel4.add(jPanel19);

        jPanel6.setMinimumSize(new java.awt.Dimension(450, 20));
        jPanel6.setOpaque(false);
        jPanel6.setPreferredSize(new java.awt.Dimension(450, 20));
        jPanel6.setLayout(new javax.swing.BoxLayout(jPanel6, javax.swing.BoxLayout.LINE_AXIS));

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel6.setText(bundle.getString("AbstractCreateGUI.jLabel6")); // NOI18N
        jLabel6.setMaximumSize(new java.awt.Dimension(100, 20));
        jLabel6.setMinimumSize(new java.awt.Dimension(100, 20));
        jLabel6.setPreferredSize(new java.awt.Dimension(100, 20));
        jPanel6.add(jLabel6);

        jTextField5.setDocument(new MaxLengthDocument(4, MaxLengthDocument.INTEGER));
        jTextField5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTextField5.setMaximumSize(new java.awt.Dimension(2147483647, 20));
        jTextField5.setMinimumSize(new java.awt.Dimension(50, 20));
        jTextField5.setPreferredSize(new java.awt.Dimension(50, 20));
        jPanel6.add(jTextField5);

        jPanel20.setMaximumSize(new java.awt.Dimension(70, 20));
        jPanel20.setMinimumSize(new java.awt.Dimension(70, 20));
        jPanel20.setOpaque(false);
        jPanel20.setPreferredSize(new java.awt.Dimension(70, 20));

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 70, Short.MAX_VALUE)
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        jPanel6.add(jPanel20);

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel7.setText(bundle.getString("AbstractCreateGUI.jLabel7")); // NOI18N
        jLabel7.setMaximumSize(new java.awt.Dimension(70, 20));
        jLabel7.setMinimumSize(new java.awt.Dimension(70, 20));
        jLabel7.setPreferredSize(new java.awt.Dimension(70, 20));
        jPanel6.add(jLabel7);

        jTextField8.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTextField8.setMargin(new java.awt.Insets(2, 10, 2, 2));
        jTextField8.setMaximumSize(new java.awt.Dimension(2147483647, 20));
        jTextField8.setMinimumSize(new java.awt.Dimension(1, 1));
        jTextField8.setPreferredSize(new java.awt.Dimension(151, 20));
        jPanel6.add(jTextField8);

        jPanel4.add(jPanel6);

        jPanel22.setOpaque(false);
        jPanel22.setPreferredSize(new java.awt.Dimension(450, 5));

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 543, Short.MAX_VALUE)
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 6, Short.MAX_VALUE)
        );

        jPanel4.add(jPanel22);

        jPanel17.setMinimumSize(new java.awt.Dimension(80, 20));
        jPanel17.setOpaque(false);
        jPanel17.setPreferredSize(new java.awt.Dimension(80, 20));
        jPanel17.setLayout(new javax.swing.BoxLayout(jPanel17, javax.swing.BoxLayout.LINE_AXIS));

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel10.setText(bundle.getString("AbstractCreateGUI.jLabel10")); // NOI18N
        jLabel10.setMaximumSize(new java.awt.Dimension(100, 20));
        jLabel10.setMinimumSize(new java.awt.Dimension(100, 20));
        jLabel10.setPreferredSize(new java.awt.Dimension(100, 20));
        jPanel17.add(jLabel10);

        jTextField12.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTextField12.setMargin(new java.awt.Insets(2, 10, 2, 2));
        jTextField12.setMaximumSize(new java.awt.Dimension(2147483647, 20));
        jTextField12.setMinimumSize(new java.awt.Dimension(50, 20));
        jTextField12.setPreferredSize(new java.awt.Dimension(50, 20));
        jTextField12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField12MouseClicked(evt);
            }
        });
        jPanel17.add(jTextField12);

        jPanel4.add(jPanel17);

        jPanel24.setOpaque(false);
        jPanel24.setPreferredSize(new java.awt.Dimension(440, 5));

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 543, Short.MAX_VALUE)
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 6, Short.MAX_VALUE)
        );

        jPanel4.add(jPanel24);

        jPanel25.setMinimumSize(new java.awt.Dimension(440, 65));
        jPanel25.setOpaque(false);
        jPanel25.setPreferredSize(new java.awt.Dimension(440, 65));
        jPanel25.setLayout(new javax.swing.BoxLayout(jPanel25, javax.swing.BoxLayout.LINE_AXIS));

        jLabel17.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel17.setText(bundle.getString("AbstractCreateGUI.jLabel17")); // NOI18N
        jLabel17.setMaximumSize(new java.awt.Dimension(100, 20));
        jLabel17.setMinimumSize(new java.awt.Dimension(100, 20));
        jLabel17.setPreferredSize(new java.awt.Dimension(100, 20));
        jPanel25.add(jLabel17);

        jPanel12.setOpaque(false);
        jPanel12.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 18, 0));

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jMovieManager/swing/images/fsk/FSK-0.png"))); // NOI18N
        jButton2.setToolTipText(bundle.getString("FSK.0.tooltip")); // NOI18N
        buttonGroup1.add(jButton2);
        jButton2.setContentAreaFilled(false);
        jButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setMaximumSize(new java.awt.Dimension(65, 65));
        jButton2.setMinimumSize(new java.awt.Dimension(65, 65));
        jButton2.setPreferredSize(new java.awt.Dimension(65, 65));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel12.add(jButton2);

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jMovieManager/swing/images/fsk/FSK-6.png"))); // NOI18N
        jButton6.setToolTipText(bundle.getString("FSK.6.tooltip")); // NOI18N
        buttonGroup1.add(jButton6);
        jButton6.setContentAreaFilled(false);
        jButton6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton6.setMaximumSize(new java.awt.Dimension(65, 65));
        jButton6.setMinimumSize(new java.awt.Dimension(65, 65));
        jButton6.setPreferredSize(new java.awt.Dimension(65, 65));
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel12.add(jButton6);

        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jMovieManager/swing/images/fsk/FSK-12.png"))); // NOI18N
        jButton7.setToolTipText(bundle.getString("FSK.12.tooltip")); // NOI18N
        buttonGroup1.add(jButton7);
        jButton7.setContentAreaFilled(false);
        jButton7.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton7.setMaximumSize(new java.awt.Dimension(65, 65));
        jButton7.setMinimumSize(new java.awt.Dimension(65, 65));
        jButton7.setPreferredSize(new java.awt.Dimension(65, 65));
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jPanel12.add(jButton7);

        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jMovieManager/swing/images/fsk/FSK-16.png"))); // NOI18N
        jButton8.setToolTipText(bundle.getString("FSK.16.tooltip")); // NOI18N
        buttonGroup1.add(jButton8);
        jButton8.setContentAreaFilled(false);
        jButton8.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton8.setMaximumSize(new java.awt.Dimension(65, 65));
        jButton8.setMinimumSize(new java.awt.Dimension(65, 65));
        jButton8.setPreferredSize(new java.awt.Dimension(65, 65));
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        jPanel12.add(jButton8);

        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jMovieManager/swing/images/fsk/FSK-18.png"))); // NOI18N
        jButton9.setToolTipText(bundle.getString("FSK.18.tooltip")); // NOI18N
        buttonGroup1.add(jButton9);
        jButton9.setContentAreaFilled(false);
        jButton9.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton9.setMaximumSize(new java.awt.Dimension(55, 55));
        jButton9.setMinimumSize(new java.awt.Dimension(65, 65));
        jButton9.setPreferredSize(new java.awt.Dimension(65, 65));
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        jPanel12.add(jButton9);

        jPanel25.add(jPanel12);

        jPanel4.add(jPanel25);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(null, ColorInterface.list_tree_BorderShadow), bundle.getString("AbstractCreateGUI.jPanel3"))); // NOI18N
        jPanel3.setOpaque(false);

        jTextArea1.setColumns(20);
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 511, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 543, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 575, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab(bundle.getString("AbstractCreateGUI.jPanel1"), jPanel1); // NOI18N

        jPanel9.setOpaque(false);
        jPanel9.setPreferredSize(new java.awt.Dimension(600, 450));

        jPanel15.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(null, ColorInterface.list_tree_BorderShadow), bundle.getString("AbstractCreateGUI.jPanel15"))); // NOI18N
        jPanel15.setOpaque(false);

        jScrollPane4.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jScrollPane4.setPreferredSize(new java.awt.Dimension(1, 1));

        jList3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jList3.setModel(actorsListModel);
        jScrollPane4.setViewportView(jList3);

        jTextField4.setText(bundle.getString("AbstractCreateGUI.jTextField4")); // NOI18N
        jTextField4.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField4FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField4FocusLost(evt);
            }
        });
        jTextField4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField4KeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 553, Short.MAX_VALUE)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(jTextField4, javax.swing.GroupLayout.DEFAULT_SIZE, 543, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel18.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(null, ColorInterface.list_tree_BorderShadow), bundle.getString("AbstractCreateGUI.jPanel18"))); // NOI18N
        jPanel18.setOpaque(false);

        jScrollPane3.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jScrollPane3.setPreferredSize(new java.awt.Dimension(1, 1));

        jList2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jList2.setModel(genresListModel);
        jScrollPane3.setViewportView(jList2);

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 553, Short.MAX_VALUE))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 196, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel18, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel15, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab(bundle.getString("AbstractCreateGUI.jPanel9"), jPanel9); // NOI18N

        jPanel26.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(null, ColorInterface.list_tree_BorderShadow), bundle.getString("AbstractCreateGUI.jPanel36"))); // NOI18N
        jPanel26.setOpaque(false);
        jPanel26.setPreferredSize(new java.awt.Dimension(220, 100));

        jTable1.setModel(tableModel);
        jTable1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jTable1PropertyChange(evt);
            }
        });
        jTable1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTable1KeyTyped(evt);
            }
        });
        jScrollPane2.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 543, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 406, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel26, javax.swing.GroupLayout.DEFAULT_SIZE, 575, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel26, javax.swing.GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab(bundle.getString("AbstractCreateGUI.jPanel11"), jPanel11); // NOI18N

        getContentPane().add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        jPanel35.setMinimumSize(new java.awt.Dimension(200, 1));
        jPanel35.setPreferredSize(new java.awt.Dimension(200, 500));

        jPanel37.setMaximumSize(new java.awt.Dimension(100, 20));
        jPanel37.setMinimumSize(new java.awt.Dimension(100, 0));
        jPanel37.setPreferredSize(new java.awt.Dimension(100, 20));

        javax.swing.GroupLayout jPanel37Layout = new javax.swing.GroupLayout(jPanel37);
        jPanel37.setLayout(jPanel37Layout);
        jPanel37Layout.setHorizontalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel37Layout.setVerticalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        jPanel35.add(jPanel37);

        jPanel21.setMaximumSize(new java.awt.Dimension(190, 230));
        jPanel21.setMinimumSize(new java.awt.Dimension(190, 230));
        jPanel21.setPreferredSize(new java.awt.Dimension(190, 230));
        jPanel21.setLayout(new javax.swing.BoxLayout(jPanel21, javax.swing.BoxLayout.LINE_AXIS));

        jPanel14.setMaximumSize(new java.awt.Dimension(20, 225));
        jPanel14.setMinimumSize(new java.awt.Dimension(20, 100));
        jPanel14.setPreferredSize(new java.awt.Dimension(20, 225));

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 225, Short.MAX_VALUE)
        );

        jPanel21.add(jPanel14);

        jLabel38.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel38.setText(bundle.getString("AbstractCreateGUI.jLabel38")); // NOI18N
        jLabel38.setBorder(new javax.swing.border.LineBorder(ColorInterface.list_tree_BorderShadow, 1, true));
        jLabel38.setMaximumSize(new java.awt.Dimension(150, 225));
        jLabel38.setMinimumSize(new java.awt.Dimension(150, 225));
        jLabel38.setPreferredSize(new java.awt.Dimension(150, 225));
        jPanel21.add(jLabel38);

        jPanel13.setMaximumSize(new java.awt.Dimension(20, 225));
        jPanel13.setMinimumSize(new java.awt.Dimension(20, 100));
        jPanel13.setPreferredSize(new java.awt.Dimension(20, 225));
        jPanel13.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 6));

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jMovieManager/swing/images/importDirectoryAdd.png"))); // NOI18N
        jButton1.setToolTipText(bundle.getString("AbstractCreateGUI.jButton1.tooltip")); // NOI18N
        jButton1.setBorderPainted(false);
        jButton1.setContentAreaFilled(false);
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.setMaximumSize(new java.awt.Dimension(16, 16));
        jButton1.setMinimumSize(new java.awt.Dimension(16, 16));
        jButton1.setPreferredSize(new java.awt.Dimension(16, 16));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel13.add(jButton1);

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jMovieManager/swing/images/importDirectoryDelete.png"))); // NOI18N
        jButton4.setToolTipText(bundle.getString("AbstractCreateGUI.jButton4.tooltip")); // NOI18N
        jButton4.setBorderPainted(false);
        jButton4.setContentAreaFilled(false);
        jButton4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton4.setEnabled(false);
        jButton4.setMaximumSize(new java.awt.Dimension(16, 16));
        jButton4.setMinimumSize(new java.awt.Dimension(16, 16));
        jButton4.setPreferredSize(new java.awt.Dimension(16, 16));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel13.add(jButton4);

        jPanel21.add(jPanel13);

        jPanel35.add(jPanel21);

        jPanel41.setMaximumSize(new java.awt.Dimension(180, 3));
        jPanel41.setMinimumSize(new java.awt.Dimension(180, 0));
        jPanel41.setPreferredSize(new java.awt.Dimension(180, 3));

        javax.swing.GroupLayout jPanel41Layout = new javax.swing.GroupLayout(jPanel41);
        jPanel41.setLayout(jPanel41Layout);
        jPanel41Layout.setHorizontalGroup(
            jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 180, Short.MAX_VALUE)
        );
        jPanel41Layout.setVerticalGroup(
            jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 3, Short.MAX_VALUE)
        );

        jPanel35.add(jPanel41);

        jPanel39.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(null, ColorInterface.list_tree_BorderShadow), bundle.getString("AbstractCreateGUI.jPanel39"))); // NOI18N
        jPanel39.setMaximumSize(new java.awt.Dimension(180, 60));
        jPanel39.setMinimumSize(new java.awt.Dimension(180, 0));
        jPanel39.setPreferredSize(new java.awt.Dimension(180, 60));

        jComboBox1.setModel(collectionModel);
        jComboBox1.setPreferredSize(new java.awt.Dimension(150, 20));
        jPanel39.add(jComboBox1);

        jPanel35.add(jPanel39);

        jPanel40.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(null, ColorInterface.list_tree_BorderShadow), bundle.getString("AbstractCreateGUI.jPanel40"))); // NOI18N
        jPanel40.setMinimumSize(new java.awt.Dimension(180, 80));
        jPanel40.setPreferredSize(new java.awt.Dimension(180, 80));
        jPanel40.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jPanel8.setMaximumSize(new java.awt.Dimension(160, 20));
        jPanel8.setMinimumSize(new java.awt.Dimension(160, 20));
        jPanel8.setPreferredSize(new java.awt.Dimension(160, 20));
        jPanel8.setLayout(new javax.swing.BoxLayout(jPanel8, javax.swing.BoxLayout.LINE_AXIS));

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel12.setText(bundle.getString("AbstractCreateGUI.jLabel12")); // NOI18N
        jLabel12.setMaximumSize(new java.awt.Dimension(100, 20));
        jLabel12.setMinimumSize(new java.awt.Dimension(100, 20));
        jLabel12.setPreferredSize(new java.awt.Dimension(100, 20));
        jPanel8.add(jLabel12);

        jTextField3.setDocument(new MaxLengthDocument(3, MaxLengthDocument.DOUBLE));
        jTextField3.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField3.setMinimumSize(new java.awt.Dimension(30, 20));
        jTextField3.setPreferredSize(new java.awt.Dimension(30, 20));
        jTextField3.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField3FocusLost(evt);
            }
        });
        jPanel8.add(jTextField3);

        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText(bundle.getString("AbstractCreateGUI.jLabel19")); // NOI18N
        jLabel19.setMaximumSize(new java.awt.Dimension(30, 20));
        jLabel19.setMinimumSize(new java.awt.Dimension(30, 20));
        jLabel19.setPreferredSize(new java.awt.Dimension(30, 20));
        jPanel8.add(jLabel19);

        jPanel40.add(jPanel8);

        jPanel16.setMaximumSize(new java.awt.Dimension(160, 20));
        jPanel16.setMinimumSize(new java.awt.Dimension(160, 20));
        jPanel16.setPreferredSize(new java.awt.Dimension(160, 20));
        jPanel16.setLayout(new javax.swing.BoxLayout(jPanel16, javax.swing.BoxLayout.LINE_AXIS));

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel13.setText(bundle.getString("AbstractCreateGUI.jLabel13")); // NOI18N
        jLabel13.setMaximumSize(new java.awt.Dimension(100, 20));
        jLabel13.setMinimumSize(new java.awt.Dimension(100, 20));
        jLabel13.setPreferredSize(new java.awt.Dimension(100, 20));
        jPanel16.add(jLabel13);

        jTextField9.setDocument(new MaxLengthDocument(3, MaxLengthDocument.DOUBLE));
        jTextField9.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField9.setMinimumSize(new java.awt.Dimension(30, 20));
        jTextField9.setPreferredSize(new java.awt.Dimension(30, 20));
        jTextField9.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField9FocusLost(evt);
            }
        });
        jPanel16.add(jTextField9);

        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel20.setText(bundle.getString("AbstractCreateGUI.jLabel20")); // NOI18N
        jLabel20.setMaximumSize(new java.awt.Dimension(30, 20));
        jLabel20.setMinimumSize(new java.awt.Dimension(30, 20));
        jLabel20.setPreferredSize(new java.awt.Dimension(30, 20));
        jPanel16.add(jLabel20);

        jPanel40.add(jPanel16);

        jPanel35.add(jPanel40);

        getContentPane().add(jPanel35, java.awt.BorderLayout.EAST);

        jPanel7.setMinimumSize(new java.awt.Dimension(310, 45));
        jPanel7.setPreferredSize(new java.awt.Dimension(310, 45));
        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 15, 10));

        jButton3.setText(bundle.getString("AbstractCreateGUI.jButton3")); // NOI18N
        jButton3.setActionCommand("jButton3");
        jButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton3.setMaximumSize(new java.awt.Dimension(100, 25));
        jButton3.setMinimumSize(new java.awt.Dimension(100, 25));
        jButton3.setPreferredSize(new java.awt.Dimension(100, 25));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel7.add(jButton3);

        jButton5.setText(bundle.getString("AbstractCreateGUI.jButton5")); // NOI18N
        jButton5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton5.setMaximumSize(new java.awt.Dimension(110, 25));
        jButton5.setMinimumSize(new java.awt.Dimension(110, 25));
        jButton5.setPreferredSize(new java.awt.Dimension(110, 25));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel7.add(jButton5);

        getContentPane().add(jPanel7, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents
                
    /**
     * jButton3 - TMDB Info <br/>
     * Searches on tmdb for information related to the title
     *
     * @param evt the triggered event
     */
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        if(!jTextField1.getText().isEmpty()){
            final AbstractCreateGUI instance = this;
            Thread getOnlineInformation = new Thread(new Runnable() {
                @Override
                public void run() {
                    instance.getAddButton().setEnabled(false);
                    instance.setCursor(new Cursor(Cursor.WAIT_CURSOR));
                    
                    LocalVideoFile result = searchForTitle(jTextField1.getText());           
                    if(null != result){
                        clearData();
                        if(!result.getTitle().isEmpty()){
                            jTextField1.setText(result.getTitle());
                        }
                        if(!result.getOriginalTitle().isEmpty()){
                            jTextField2.setText(result.getOriginalTitle());
                        }else{
                            jTextField2.setText(jTextField1.getText());
                        }
                        if(!result.getDescription().isEmpty()){
                            jTextArea1.setText(result.getDescription());
                            //Scroll wieder hoch, wenn es eine zu lange Beschreibung ist
                            jTextArea1.select(0, 0);
                        }
                        //Genres selektieren
                        instance.setSelectedGenres(result);
                        
                        //Actors hinzufügen und selektieren
                        instance.addAndSetSelectedActors(result);

                        if(0 != result.getPlaytime()){
                            jTextField6.setText(Integer.toString(result.getPlaytime()));
                        }
                        if(0 != result.getReleaseYear()){
                            jTextField5.setText(Integer.toString(result.getReleaseYear()));
                        }
                        if(0 != result.getOnlineRating()){
                            jTextField3.setText(String.valueOf(result.getOnlineRating()));
                        }
                        if(!result.getPublisher().isEmpty()){
                            jTextField7.setText(result.getPublisher());
                        }
                        if(!result.getDirector().isEmpty()){
                            jTextField8.setText(result.getDirector());
                        }
                        if(result.getFsk() == FSK.FSK_UNKNOWN){
                            //jTextField17.setText(result.getFsk());
                        }

                        if(!result.getImagePath().isEmpty()){
//                            try {
                                //Grafik aktualisiern
                                jLabel38.setText("");
                                ImageIcon scaledIcon = PictureManager.getScaledImage(result.getImagePath(), PictureManager.thumbnail_widht,PictureManager.thumbnail_height);
                                //Füge das Bild aufs Label
                                jLabel38.setIcon(scaledIcon);
                                imagePath = result.getImagePath();
                                image = PictureManager.getScaledImage(result.getImagePath(), PictureManager.picture_width, PictureManager.picture_height);
//                            } catch (MalformedURLException ex) {
//                                javax.swing.JOptionPane.showMessageDialog(instance, bundle.getString("AbstractCreateGUI.error.noPicture"));
//                            }
                        }
                    }
                    instance.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    instance.getAddButton().setEnabled(true);
                }
            });
            getOnlineInformation.start();
        }else{
            javax.swing.JOptionPane.showMessageDialog(this, bundle.getString("AbstractCreateGUI.error.titleLeft"));
        }
    }//GEN-LAST:event_jButton3ActionPerformed

   /**
     * Hinzufügen
     * 
     * Nimmt alle Eingabewerte aus der GUI, überprüft diese und übergibt Sie an den Controller
     *
     * @param evt Event, welches ausgelöst wurde
     */
    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        try{
            startButtonPressed(evt);
        }catch(IllegalArgumentException ex){
            return;
        }
        //Dispose GUI and release ressources
        this.dispose();
    }//GEN-LAST:event_jButton5ActionPerformed
  
     
    /**
     * Searches for the specific title on a given API. 
     * 
     * @param title The title to search
     * @return LocalVideoFile The search result
     */    
    protected abstract LocalVideoFile searchForTitle(String title);
    
    /**
     * Aktion die ausgeführt werden soll, nachdem der Start Button gedrückt wurde
     * 
     * @param evt The triggered event
     */
    protected void startButtonPressed(java.awt.event.ActionEvent evt) throws IllegalArgumentException{
        if(jComboBox1.isEnabled()){
            Object mediaCollObj = jComboBox1.getSelectedItem();
            if(mediaCollObj == null){
                javax.swing.JOptionPane.showMessageDialog(this, bundle.getString("AbstractCreateGUI.error.noCollection"));
                throw new IllegalArgumentException();
            }
            this.collection = (MediaCollection)mediaCollObj;
        }
        
        //Überprüfe, ob bereits ein Objekt mit dem Titel existiert, bevor die Werte gefüllt werden
        title = jTextField1.getText().trim();
        if(title.isEmpty()){
            javax.swing.JOptionPane.showMessageDialog(this, bundle.getString("AbstractCreateGUI.error.titleLeft"));
            throw new IllegalArgumentException();
        }  
        else if((!oldTitle.equals(title) && collection != null && collection.get(title) != null)){
            if(this instanceof CreateSerieGUI){
                javax.swing.JOptionPane.showMessageDialog(this, bundle.getString("AbstractCreateGUI.error.serieExist"));
                throw new IllegalArgumentException();
            }
            else if(this instanceof CreateMovieGUI){
                javax.swing.JOptionPane.showMessageDialog(this, bundle.getString("AbstractCreateGUI.error.movieExist"));
                throw new IllegalArgumentException();
            }
        }
        
        genreKeys.clear();
        actors.clear();
        customAttributes.clear();
        //Film existiert noch nicht, belege alle Attribute    
        originalTitle = jTextField2.getText().trim();

        if(jTextArea1.getText().length() > 19999){
            javax.swing.JOptionPane.showMessageDialog(this, bundle.getString("AbstractCreateGUI.error.maxDescLength"));
            throw new IllegalArgumentException();
        }
        description = jTextArea1.getText();

        publisher = jTextField7.getText();
        director = jTextField8.getText();

        //Custom Attributes
        if(jTable1.getCellEditor() != null){
            jTable1.getCellEditor().stopCellEditing();
        }
        for(int i = 0; i < jTable1.getRowCount(); i++){
            Object key = ((Vector)tableModel.getDataVector().elementAt(i)).elementAt(0);
            Object value = ((Vector)tableModel.getDataVector().elementAt(i)).elementAt(1);
            if((key != null) && (!key.toString().isEmpty()) && (value != null) && (!value.toString().isEmpty())){
                customAttributes.put(key.toString(), value.toString());
            }
        }
        trailerPath = jTextField12.getText();

        //Integer Fields
        try{
            if(!jTextField6.getText().isEmpty()){
                playtime = Integer.parseInt(jTextField6.getText().trim());
            }
        }catch(NumberFormatException evt6){
             javax.swing.JOptionPane.showMessageDialog(this, bundle.getString("AbstractCreateGUI.error.playtime"));
             throw new IllegalArgumentException(evt6);
        }

        try{
            if(!jTextField5.getText().isEmpty()){
                releaseYear = Integer.parseInt(jTextField5.getText());
            }
        }catch(NumberFormatException evt6){
             javax.swing.JOptionPane.showMessageDialog(this, bundle.getString("AbstractCreateGUI.error.releaseYear"));
             throw new IllegalArgumentException(evt6);
        }

        //Double Fields
        try{
            if(!jTextField3.getText().isEmpty()){
                imdbRating = Double.parseDouble(jTextField3.getText());
            }
        }catch(NumberFormatException evt6){
             javax.swing.JOptionPane.showMessageDialog(this, bundle.getString("AbstractCreateGUI.error.imdbRating"));
             throw new IllegalArgumentException(evt6);
        }
        try{
            if(!jTextField9.getText().isEmpty()){
                personalRating = Double.parseDouble(jTextField9.getText());
            }
        }catch(NumberFormatException evt7){
             javax.swing.JOptionPane.showMessageDialog(this, bundle.getString("AbstractCreateGUI.error.persRating"));
             throw new IllegalArgumentException(evt7);
        }

        //jList
        if(!jList2.getSelectedValuesList().isEmpty()){
            for(Object element: jList2.getSelectedValuesList()){
                //Speichere den nicht lokalisierten Propertie Key als Genre ab
                genreKeys.add(localizedGenresMap.get(element.toString()));
            }
        }
        if(!jList3.getSelectedValuesList().isEmpty()){
            for(Object element: jList3.getSelectedValuesList()){
                actors.add(new Actor(element.toString()));
            }
        }       
    }
    
    /**
     * Überprüft, ob im Feld "Persönliche Bewertung" eine Zahl steht
     *
     * @param evt Event, welches ausgelöst wurde
     */
    private void jTextField9FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField9FocusLost
        if(!jTextField9.getText().isEmpty()){
            try{
                if(Double.parseDouble(jTextField9.getText()) > 10){
                    javax.swing.JOptionPane.showMessageDialog(this, bundle.getString("AbstractCreateGUI.error.jTextField7.number"));        
                    jTextField9.requestFocusInWindow();
                    return;
                }
            }catch(NumberFormatException e){
                javax.swing.JOptionPane.showMessageDialog(this, bundle.getString("AbstractCreateGUI.error.jTextField7.number"));        
            }
            firePropertyChange(IPropertyChangeKeys.PERSONAL_RATING, personalRating, Double.parseDouble(jTextField9.getText()));
        }
}//GEN-LAST:event_jTextField9FocusLost

    /**
     * Überprüft, ob im Feld "IMDB Bewertung" eine Zahl steht
     *
     * @param evt Event, welches ausgelöst wurde
     */    
    private void jTextField3FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField3FocusLost
        if(!jTextField3.getText().isEmpty()){
            try{
                if(Double.parseDouble(jTextField3.getText()) > 10){
                    javax.swing.JOptionPane.showMessageDialog(this, bundle.getString("AbstractCreateGUI.error.jTextField6.number"));          
                    jTextField3.requestFocusInWindow();
                }
            }catch(NumberFormatException e){
                javax.swing.JOptionPane.showMessageDialog(this, bundle.getString("AbstractCreateGUI.error.jTextField6.number"));          
            }
        }
}//GEN-LAST:event_jTextField3FocusLost

    /**
     * Löscht den Vorschautext beim Schauspieler hinzufügen
     *
     * @param evt Event, welches ausgelöst wurde
     */
private void jTextField4FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField4FocusGained
    jTextField4.setText("");
    jTextField4.setForeground(UIManager.getColor("TextField.foreground"));
}//GEN-LAST:event_jTextField4FocusGained

    /**
     * Fügt den Vorschautext beim in das Feld ein
     *
     * @param evt Event, welches ausgelöst wurde
     */
private void jTextField4FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField4FocusLost
    if(jTextField4.getText().isEmpty()){
        jTextField4.setText(bundle.getString("AbstractCreateGUI.jTextField4"));
        jTextField4.setForeground(UIManager.getColor("TextField.inactiveForeground"));
    }
}//GEN-LAST:event_jTextField4FocusLost

    /**
     * Sucht ein Schauspieler und fügt diesen der Liste hinzu
     *
     * @param evt Event, welches ausgelöst wurde
     */
private void jTextField4KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField4KeyReleased
    if((evt.getKeyChar() == KeyEvent.VK_ENTER) && (!actorsListModel.contains(jTextField4.getText()))){
        actorsListModel.addElement(jTextField4.getText());
        int[] selIndices = new int[jList3.getSelectedIndices().length + 1];
        System.arraycopy(jList3.getSelectedIndices(), 0, selIndices, 0, selIndices.length - 1);
        
        //Letzte stelle das neue Element
        selIndices[selIndices.length-1] = actorsListModel.indexOf(jTextField4.getText());
        jList3.setSelectedIndices(selIndices);
        
        jTextField4.setText("");
    }
    else if((evt.getKeyChar() == KeyEvent.VK_BACK_SPACE) || (evt.getKeyChar() == KeyEvent.VK_DELETE)){
        //do nothing
    }
    else{
        //Suchanfrage
        String actorToSearch = jTextField4.getText();
        if((!actorToSearch.isEmpty()) && (actorToSearch.length() > 2)){
            for(Actor actor: Actor.getActorsSet()){
                if(actor.getName().startsWith(actorToSearch)){
                    jTextField4.setText(actor.getName());
                    jTextField4.select(actorToSearch.length(), actor.getName().length());                   
                    break;
                }
            }   
        }
    }
}//GEN-LAST:event_jTextField4KeyReleased

    /**
     * jButton4 - Remove Thumbnail <br/>
     * Removes a thumbnail picture from the gui
     *
     * @param evt The triggered event
     */
    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        jLabel38.setIcon(null);
        jLabel38.setText(bundle.getString("AbstractCreateGUI.jLabel38"));
        imagePath = "";
        image = null;
        jButton4.setEnabled(false);
    }//GEN-LAST:event_jButton4ActionPerformed

    /**
     * jButton1 - Add Thumbnail <br/>
     * Adds a thumbnail picture to the gui
     *
     * @param evt The triggered event
     */
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        MyJFileChooser jFileChooser1 = new MyJFileChooser("", JMMFileFilter.FilterType.PICTURE_EXTENSION);
        //zeige Dialog und mache etwas, wenn "Öffnen" geklickt wurde
        if(jFileChooser1.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
            //Delete oldFile in pics folder, ix exist            
            String path = jFileChooser1.getSelectedFile().toString();
            jLabel38.setText("");
            Icon scaledIcon = PictureManager.getScaledImage(path, PictureManager.thumbnail_widht, PictureManager.thumbnail_height);
            //Füge das Bild aufs Label
            jLabel38.setIcon(scaledIcon);
            imagePath = path;
            image = PictureManager.getScaledImage(path, PictureManager.picture_width, PictureManager.picture_height);
            
            jButton4.setEnabled(true);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * jButton2 - FSK 0 <br/>
     * Sets the fsk to the specific value
     * @param evt the triggered event
     */
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if(jButton2.isSelected()){
            this.fsk = FSK.FSK_UNKNOWN;
            buttonGroup1.clearSelection();
        }else{         
            this.fsk = FSK.FSK_0;
            buttonGroup1.setSelected(jButton2.getModel(), true);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * jButton6 - FSK 6 <br/>
     * Sets the fsk to the specific value
     * @param evt the triggered event
     */
    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        if(jButton6.isSelected()){
            this.fsk = FSK.FSK_UNKNOWN;
            buttonGroup1.clearSelection();
        }else{
            this.fsk = FSK.FSK_6;
            buttonGroup1.setSelected(jButton6.getModel(), true);
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    /**
     * jButton7 - FSK 12 <br/>
     * Sets the fsk to the specific value
     * @param evt the triggered event
     */
    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        if(jButton7.isSelected()){
            this.fsk = FSK.FSK_UNKNOWN;
            buttonGroup1.clearSelection();
        }else{
            this.fsk = FSK.FSK_12;
            buttonGroup1.setSelected(jButton7.getModel(), true);
        }
    }//GEN-LAST:event_jButton7ActionPerformed

    /**
     * jButton8 - FSK 16 <br/>
     * Sets the fsk to the specific value
     * @param evt the triggered event
     */
    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        if(jButton8.isSelected()){
            this.fsk = FSK.FSK_UNKNOWN;
            buttonGroup1.clearSelection();
        }else{
            this.fsk = FSK.FSK_16;
            buttonGroup1.setSelected(jButton8.getModel(), true);
        }
    }//GEN-LAST:event_jButton8ActionPerformed

    /**
     * jButton9 - FSK 18 <br/>
     * Sets the fsk to the specific value
     * @param evt the triggered event
     */
    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        if(jButton9.isSelected()){
            this.fsk = FSK.FSK_UNKNOWN;
            buttonGroup1.clearSelection();
        }else{
            this.fsk = FSK.FSK_18;
            buttonGroup1.setSelected(jButton9.getModel(), true);
        }
    }//GEN-LAST:event_jButton9ActionPerformed

   /**
     * jTextField2 - Set Trailer Path <br/>
     * Opens a fileChooser to select a local media trailer
     * 
     * @param evt the triggered event
     */
    private void jTextField12MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField12MouseClicked
        MyJFileChooser chooseTrailer = new MyJFileChooser(trailerPath, JMMFileFilter.FilterType.VIDEO_EXTENSION);
        if(chooseTrailer.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
            String selFilePath = chooseTrailer.getSelectedFile().getAbsolutePath();
            jTextField12.setText(selFilePath);
        }
    }//GEN-LAST:event_jTextField12MouseClicked

    /**
    * Event, welches bei Tastendruck ausgelöst wird<br/>
    * Löscht eine Zeile, wenn ENTF gedrückt wurde
    * 
    * @param evt Das Event, welches ausgelöst wurde
    */
    private void jTable1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable1KeyTyped
        if(evt.getKeyChar() == KeyEvent.VK_DELETE){
            int selRow = jTable1.getSelectedRow();
            if((selRow > -1) && (selRow+1 != jTable1.getRowCount())){
                tableModel.removeRow(selRow);
                jTable1.clearSelection();
            }
        }
    }//GEN-LAST:event_jTable1KeyTyped

    /**
     * jTable1 - check for empty rows.<br/>
     * Determins if there is at least one empty row to create new custom properties
     * @param evt The triggered event
     */
    private void jTable1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jTable1PropertyChange
        int selectedRow = jTable1.getSelectedRow();
        if((selectedRow > -1 ) && (selectedRow == jTable1.getRowCount()-1)){
            if((evt.getNewValue() != null) && (!evt.getNewValue().toString().isEmpty())){
                tableModel.addRow(new Object[]{"", ""});
            }      
        }
    }//GEN-LAST:event_jTable1PropertyChange

    /**
     * Gibt das Panel mit den Buttons zurück
     **/
    public JPanel getButtonPanel(){
        return jPanel7;
    }
    
    /**
     * Gibt das Panel mit den Buttons zurück
     **/
    public JTabbedPane getTabbedPane(){
        return jTabbedPane1;
    }
    
    /**
     * Gibt das rechte Sidebar Panel zurück
     **/    
    public JPanel getRightPanel(){
        return jPanel35;
    }
    
    public JPanel getJPanel17(){
        return jPanel17;
    }
    
    public JButton getAddButton(){
        return jButton5;
    }
    
    /**
     * removes all data from the gui
     */
    private void clearData(){
        jTextField1.setText("");
        jTextField2.setText("");
        jTextField3.setText("");
        jTextField4.setText(bundle.getString("AbstractCreateGUI.jTextField4"));
        jTextField5.setText("");
        jTextField6.setText("");
        jTextField7.setText("");
        jTextField8.setText("");
        jTextField9.setText("");

        jTextArea1.setText("");
        jList2.clearSelection();
        jList3.clearSelection();
        actorsListModel.removeAllElements();
        jLabel38.setIcon(null);
        jLabel38.setText(bundle.getString("AbstractCreateGUI.jLabel38"));
    }
       
    /**
     * Updates all attributes from the localVideoFile
     * @param videoFile
     */
    protected void updateLocalVideoFileData(LocalVideoFile videoFile){     
        videoFile.setTitle(title);
        videoFile.setOriginalTitle(originalTitle);
        videoFile.setGenreKeys(genreKeys);
        videoFile.setPlaytime(playtime);
        videoFile.setReleaseYear(releaseYear);
        videoFile.setDescription(description);
        videoFile.setPublisher(publisher);
        videoFile.setActors(actors);
        videoFile.setDirector(director);
        videoFile.setFsk(fsk);
        videoFile.setImagePath(imagePath);
        videoFile.setImage(image);
        videoFile.setTrailerPath(trailerPath);
        videoFile.setOnlineRating(imdbRating);
        videoFile.setPersonalRating(personalRating);
        
        videoFile.clearCustomAttributes();
        for(String key: customAttributes.keySet()){
            String value = customAttributes.get(key);
            if((value != null) && (!value.trim().isEmpty())){
                videoFile.addCustomAttribute(key, value);
            }
        }
    }
    
    /**
     * Selektiert die Genres, der übergebenen videoFile
     * @param outdatedVideoFile 
     */
    private void setSelectedGenres(VideoFile outdatedVideoFile){
        if(!outdatedVideoFile.getGenreKeys().isEmpty()){
            int[] selectedIndices = new int[outdatedVideoFile.getGenreKeys().size()];
            int currentIndex;
            
            Iterator<String> genreKeysIterator = outdatedVideoFile.getGenreKeys().iterator();
            String genreKey;
            int i = 0;
            while(genreKeysIterator.hasNext()){
                genreKey = genreKeysIterator.next(); 
                if(genreKey != null && genreKeyAndLocaleBundle.containsKey(genreKey)){
                    currentIndex = genresListModel.indexOf(genreKeyAndLocaleBundle.getString(genreKey));
                    if(currentIndex > -1){
                        selectedIndices[i] = currentIndex;
                    }
                }
                i++;
            }           
            jList2.setSelectedIndices(selectedIndices);
        }
        else{
            jList2.setSelectedIndex(-1);
        }
    }

    /**
     * Fügt Schauspieler zur Auswahlliste hinzu und selektiert diese
     * @param outdatedVideoFile 
     */
    private void addAndSetSelectedActors(VideoFile outdatedVideoFile){
        if(!outdatedVideoFile.getActors().isEmpty()){
            int[] selectedIndices = new int[outdatedVideoFile.getActors().size()];
            
            Iterator<Actor> actorsIterator = outdatedVideoFile.getActors().iterator();
            Actor actor;
            int i = 0;
            while(actorsIterator.hasNext()){
                actor = actorsIterator.next();
                if(!actorsListModel.contains(actor.getName())){
                    actorsListModel.addElement(actor.getName());

                }                                       
                selectedIndices[i] = actorsListModel.indexOf(actor.getName());  
                i++;
            }
            jList3.setSelectedIndices(selectedIndices);
        }
    }    
    
    /**
     * fills the collectionModel with elements
     */
    private void addCollectionsToComboboxModel(){
        Iterator<MediaCollection> collIterator = MediaCollection.getCollectionMap().values().iterator();
        while(collIterator.hasNext()){
            MediaCollection localCollection = collIterator.next();
            //Episodes will be ignored, only season and movies
            if(!(this instanceof CreateEpisodeGUI)){
                if(((localCollection instanceof SerieCollection) && (this instanceof CreateSerieGUI)) ||
                   ((localCollection instanceof MovieCollection) && (this instanceof CreateMovieGUI))){
                    collectionModel.addElement(localCollection);
                }
            }
        }
    }
    
    /**
     * Methode welche dem jeweiligen DefaultListModel die lokalisierten Werte aus der 
     * MovieManager.properties Datei hinzufügt
     * 
     * @param genreKeys Die keys für die lokalisierten Werte
     * @param modelToAddValues Das Model, dem die lokalisierten Werte hinzugefügt werden sollen
     */
    final protected void addLocalizedElementsToModel(String[] genreKeys, DefaultListModel modelToAddValues){
         for(String genreKey: genreKeys){
            //Füge die Genres einer Hashmap hinzu, damit nach Auswahl nicht Lokalisierte Wert gespeichert werden kann 
            localizedGenresMap.put(genreKeyAndLocaleBundle.getString(genreKey), genreKey);
            modelToAddValues.addElement(genreKeyAndLocaleBundle.getString(genreKey));
        }       
    }  
    
    /**
     * Tests, if there is an internet connection available and the api is enabled. <br/>
     * @return true If both is available <br/> false otherwise
    **/    
    protected abstract boolean isInternetAndAPIAvailable();
         
    /**
    * Methode zum Anpassen der GUI an das neue Look and Feel
    **/
    @Override
    public void changeUI(){        
        jButton2.setUI((BasicButtonUI)MyBasicMenuButtonUI.createUI(jButton2));
        jButton6.setUI((BasicButtonUI)MyBasicMenuButtonUI.createUI(jButton6));
        jButton7.setUI((BasicButtonUI)MyBasicMenuButtonUI.createUI(jButton7));
        jButton8.setUI((BasicButtonUI)MyBasicMenuButtonUI.createUI(jButton8));
        jButton9.setUI((BasicButtonUI)MyBasicMenuButtonUI.createUI(jButton9));
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JList jList2;
    private javax.swing.JList jList3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
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
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel37;
    private javax.swing.JPanel jPanel39;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel40;
    private javax.swing.JPanel jPanel41;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField12;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    // End of variables declaration//GEN-END:variables
}
