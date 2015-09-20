/**
 * Copyright (c) 2010-2015 Bryan Beck.
 * All rights reserved.
 * 
 * This project is licensed under LGPL v2.1.
 * See jMovieManager-license.txt for details.
 * 
 */
package jMovieManager.swing.gui;

import jMovieManager.swing.gui.components.MyDnDJTabbedPane;
import jMovieManager.swing.gui.components.MyJFileChooser;
import jMovieManager.swing.gui.components.ui.MyBasicMenuButtonUI;
import jMovieManager.swing.gui.components.ui.MyBasicSplitPaneUI;
import jMovieManager.swing.gui.components.ui.jMMLookAndFeel;
import jMovieManager.swing.gui.createmedia.CreateMovieGUI;
import jMovieManager.swing.gui.createmedia.CreateSerieGUI;
import jMovieManager.swing.gui.other.JMMFileFilter;
import jMovieManager.swing.gui.other.MyIconFactory;
import jMovieManager.swing.gui.splitpane.left.AbstractTabGUI;
import jMovieManager.swing.gui.splitpane.left.MovieTabGUI;
import jMovieManager.swing.gui.splitpane.left.SerieTabGUI;
import jMovieManager.swing.gui.splitpane.right.MovieDetailGUI;
import jMovieManager.swing.gui.splitpane.right.ReleaseCalendarGUI;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.ResourceBundle;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.plaf.SplitPaneUI;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.tree.TreePath;
import jmm.data.Episode;
import jmm.data.Movie;
import jmm.data.Season;
import jmm.data.Serie;
import jmm.data.collection.CollectionManager;
import jmm.data.collection.CollectionManager.Order;
import jmm.data.collection.MediaCollection;
import jmm.data.collection.MovieCollection;
import jmm.data.collection.SerieCollection;
import jmm.interfaces.CollectionObserverInterface;
import jmm.interfaces.FileTypeInterface;
import jmm.persist.PersistingManager;
import jmm.persist.RunnableImpl;
import jmm.utils.ExtendedSearch;
import jmm.utils.LocaleManager;
import jmm.utils.OperatingSystem;
import jmm.utils.Settings;
import jmm.xml.XMLManager;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;

/**
 * Main GUI for jMM
 * 
 * @author Bryan Beck
 * @since 20.10.2010
 */
public class MovieManagerGUI extends javax.swing.JFrame implements CollectionObserverInterface, Observer{  
    /** Logger. */
    private static final Logger LOG = LoggerFactory.logger(MovieManagerGUI.class);
    
    private static MovieManagerGUI instance;
        
    private DefaultComboBoxModel comboBoxData;
    private ResourceBundle bundle;
    //Componenten für das Popup Menuprivate JMenu popupImport;
    private JMenuItem popupAdd;
    private JMenuItem popupEdit;
    private JMenuItem popupDelete;
    private JMenuItem popupAbout;
    
    private final Map<Locale, JRadioButtonMenuItem> localeToMenuItemMap;
            
    //Run Methode, zum Speichern exportieren in eine XML Datei
    Runnable exportProcess = new Runnable() {
        @Override
        public void run() {
            if(!XMLManager.INSTANCE.exportToXML()){
                javax.swing.JOptionPane.showMessageDialog(instance, bundle.getString("MovieManagerGUI.error.fileExport"));
            }
            instance.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));                    
        }
    };
        
    //Run Methode zum aktualisieren der aktivierten GUI Elemente
    Runnable actualizeEnabledGUIElements = new Runnable() {
        @Override
        public void run() {
            updateEnabledGUIElements();
        }
    };
    
     /**
     * Konstruktor welcher die GUI mit allen Komponenten initialisiert
     */
    private MovieManagerGUI() {
        //Initialize default locale
        Locale currentLocale = LocaleManager.getInstance().getCurrentLocale();     
        bundle = ResourceBundle.getBundle("jMovieManager.swing.resources.MovieManager");
        localeToMenuItemMap = new HashMap<>();
        
        JOptionPane.setDefaultLocale(currentLocale);
        JFileChooser.setDefaultLocale(currentLocale);
        
        //Setzte OS spezifisches  Look and Feel             
//        try {            
//           //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//           //Default Nimbus
//           //UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
//           //LIB Nimbus
//           //UIManager.setLookAndFeel("org.jdesktop.swingx.plaf.nimbus.NimbusLookAndFeel");
//        }catch(Exception e) {
//           JOptionPane.showMessageDialog(this, bundle.getString("MovieManagerGUI.error.lookAndFeel"), "", JOptionPane.ERROR_MESSAGE);
//        }
        
        customizeWindowsLookAndFeel();
        initComponents();
        jTabbedPane1.addTab("+", null);
        jTabbedPane1.setEnabledAt(0, false);
        postCustomizeWindowsLookAndFeel();
//        addJMenuBarItemsAndFunction();
        initPopupMenu();
        
        //Ordne Grafik zu
        try {
            Image img = ImageIO.read( getClass().getResource( "/jMovieManager/swing/images/Logo_v.1_32x32.png" ) );
            this.setIconImage(img);
        } catch (IOException ex) {
            LOG.error("Error while setting jMM icon.", ex);
        }
        
        // create and select language jRadioMenuItems
        createLanguageMenuItems();
        JRadioButtonMenuItem menuItem = localeToMenuItemMap.get(currentLocale);
        if(menuItem != null){
            menuItem.setSelected(true);
        }else{
           localeToMenuItemMap.get(LocaleManager.SupportedLanguages.en_US.getLocale()).setSelected(true);
        }
//                 if(SystemTray.isSupported()){
//               SystemTray tray = SystemTray.getSystemTray();
//               TrayIcon trayIcon = new TrayIcon(img, "jMovieManager");
//               try {
//                   tray.add(trayIcon);
//               } catch (AWTException e1) {
//               } 
//           }
       //Mac OS Opaque
       if(OperatingSystem.isMacPlatform()){
           jMenu1.setOpaque(false);
           jMenu2.setOpaque(false);
           jMenu3.setOpaque(false);          
           jMenu5.setOpaque(false);       
           jMenu6.setOpaque(false);    
       }
                
        comboBoxData = new DefaultComboBoxModel(new String[] {
            bundle.getString("MovieManagerGUI.jComboBox1.value1"),
            bundle.getString("MovieManagerGUI.jComboBox1.value2"),
            bundle.getString("MovieManagerGUI.jComboBox1.value3"),
            bundle.getString("MovieManagerGUI.jComboBox1.value4"),
            bundle.getString("MovieManagerGUI.jComboBox1.value5")
        });
        jComboBox1.setModel(comboBoxData);
        
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                jMenuItem4ActionPerformed(null);
            }
        });

        //Linux toolbar setting
        jToolBar1.setLayout(new BoxLayout(jToolBar1, BoxLayout.LINE_AXIS));

        if(Settings.getInstance().getStartupDisplaySetting() == Settings.displaySize_MAXIMIZED){
            this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        }
        
        //GUI zentriert darstellen
        this.setLocationRelativeTo(null);           
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jMenu10 = new javax.swing.JMenu();
        jMenu9 = new javax.swing.JMenu();
        jMenu8 = new javax.swing.JMenu();
        jMenu7 = new javax.swing.JMenu();
        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        jPanel5 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jTextField1 = new javax.swing.JTextField();
        jSplitPane1 = new javax.swing.JSplitPane();
        jTabbedPane1 = new MyDnDJTabbedPane();
        jPanel6 = new javax.swing.JPanel();
        if(jMenuBar1 == null){
            jMenuBar1 = new javax.swing.JMenuBar();
        }
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jSeparator7 = new javax.swing.JPopupMenu.Separator();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem10 = new javax.swing.JMenuItem();
        jMenuItem20 = new javax.swing.JMenuItem();
        jMenu11 = new javax.swing.JMenu();
        jMenuItem21 = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JPopupMenu.Separator();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem18 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        jMenuItem17 = new javax.swing.JMenuItem();
        jMenuItem12 = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        jMenuItem24 = new javax.swing.JMenuItem();
        jMenu6 = new javax.swing.JMenu();
        jMenuItem11 = new javax.swing.JMenuItem();
        jMenuItem14 = new javax.swing.JMenuItem();
        jSeparator6 = new javax.swing.JPopupMenu.Separator();
        jMenuItem16 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem19 = new javax.swing.JMenuItem();
        jMenuItem22 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();

        jPopupMenu1.setMaximumSize(new java.awt.Dimension(150, 300));
        jPopupMenu1.setMinimumSize(new java.awt.Dimension(150, 200));
        jPopupMenu1.setPreferredSize(new java.awt.Dimension(150, 200));

        jMenu10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jMovieManager/swing/textures/gtk_close.png"))); // NOI18N
        jMenu10.setMaximumSize(new java.awt.Dimension(30, 24));
        jMenu10.setMinimumSize(new java.awt.Dimension(30, 24));
        jMenu10.setPreferredSize(new java.awt.Dimension(30, 24));
        jMenu10.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/jMovieManager/swing/textures/close_rollover.png"))); // NOI18N
        jMenu10.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/jMovieManager/swing/textures/close_rollover.png"))); // NOI18N
        jMenu10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu10MouseClicked(evt);
            }
        });

        jMenu9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jMovieManager/swing/textures/maximize2.png"))); // NOI18N
        jMenu9.setMaximumSize(new java.awt.Dimension(30, 24));
        jMenu9.setMinimumSize(new java.awt.Dimension(30, 24));
        jMenu9.setPreferredSize(new java.awt.Dimension(30, 24));
        jMenu9.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/jMovieManager/swing/textures/maximize2_rollover2.png"))); // NOI18N
        jMenu9.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/jMovieManager/swing/textures/maximize2_rollover2.png"))); // NOI18N
        jMenu9.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/jMovieManager/swing/textures/maximize2_rollover2.png"))); // NOI18N
        jMenu9.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/jMovieManager/swing/textures/maximize2_rollover2.png"))); // NOI18N
        jMenu9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu9MouseClicked(evt);
            }
        });

        jMenu8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jMovieManager/swing/textures/minimize.png"))); // NOI18N
        jMenu8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jMenu8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jMenu8.setMaximumSize(new java.awt.Dimension(30, 24));
        jMenu8.setMinimumSize(new java.awt.Dimension(30, 24));
        jMenu8.setPreferredSize(new java.awt.Dimension(30, 24));
        jMenu8.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/jMovieManager/swing/textures/minimize_rollover2.png"))); // NOI18N
        jMenu8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu8MouseClicked(evt);
            }
        });

        jMenu7.setEnabled(false);
        jMenu7.setMaximumSize(new java.awt.Dimension(32767, 40));
        jMenu7.setPreferredSize(new java.awt.Dimension(600, 40));

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("jMovieManager/swing/resources/MovieManager"); // NOI18N
        setTitle(bundle.getString("MovieManagerGUI.headline")); // NOI18N
        setMinimumSize(new java.awt.Dimension(800, 600));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowDeiconified(java.awt.event.WindowEvent evt) {
                formWindowDeiconified(evt);
            }
        });
        addWindowStateListener(new java.awt.event.WindowStateListener() {
            public void windowStateChanged(java.awt.event.WindowEvent evt) {
                formWindowStateChanged(evt);
            }
        });

        jPanel1.setPreferredSize(new java.awt.Dimension(1024, 748));
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.PAGE_AXIS));

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        jToolBar1.setMaximumSize(new java.awt.Dimension(32769, 45));
        jToolBar1.setMinimumSize(new java.awt.Dimension(0, 45));
        jToolBar1.setPreferredSize(new java.awt.Dimension(1024, 45));

        jPanel5.setMaximumSize(new java.awt.Dimension(2147483647, 45));
        jPanel5.setMinimumSize(new java.awt.Dimension(270, 45));
        jPanel5.setPreferredSize(new java.awt.Dimension(1024, 45));
        jPanel5.setLayout(new java.awt.BorderLayout());

        jPanel4.setMaximumSize(new java.awt.Dimension(305, 45));
        jPanel4.setMinimumSize(new java.awt.Dimension(305, 45));
        jPanel4.setPreferredSize(new java.awt.Dimension(335, 45));
        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jMovieManager/swing/images/buttons/sammlung.png"))); // NOI18N
        jButton3.setToolTipText(bundle.getString("MovieManagerGUI.jButton3.tooltip")); // NOI18N
        jButton3.setBorderPainted(false);
        jButton3.setContentAreaFilled(false);
        jButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton3.setMaximumSize(new java.awt.Dimension(32, 32));
        jButton3.setMinimumSize(new java.awt.Dimension(32, 32));
        jButton3.setPreferredSize(new java.awt.Dimension(32, 32));
        jButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton3);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jMovieManager/swing/images/buttons/addMovie.png"))); // NOI18N
        jButton1.setToolTipText(bundle.getString("MovieManagerGUI.jButton1.tooltip")); // NOI18N
        jButton1.setBorderPainted(false);
        jButton1.setContentAreaFilled(false);
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/jMovieManager/swing/images/buttons/addMovieDisabled.png"))); // NOI18N
        jButton1.setEnabled(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setMaximumSize(new java.awt.Dimension(32, 32));
        jButton1.setMinimumSize(new java.awt.Dimension(32, 32));
        jButton1.setPreferredSize(new java.awt.Dimension(32, 32));
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton1);

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jMovieManager/swing/images/buttons/addSerie.png"))); // NOI18N
        jButton5.setToolTipText(bundle.getString("MovieManagerGUI.jButton5.tooltip")); // NOI18N
        jButton5.setBorderPainted(false);
        jButton5.setContentAreaFilled(false);
        jButton5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton5.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/jMovieManager/swing/images/buttons/addSerieDisabled.png"))); // NOI18N
        jButton5.setEnabled(false);
        jButton5.setMaximumSize(new java.awt.Dimension(32, 32));
        jButton5.setMinimumSize(new java.awt.Dimension(32, 32));
        jButton5.setPreferredSize(new java.awt.Dimension(32, 32));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton5);

        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jMovieManager/swing/images/buttons/edit.png"))); // NOI18N
        jButton7.setToolTipText(bundle.getString("MovieManagerGUI.jButton7.tooltip")); // NOI18N
        jButton7.setBorderPainted(false);
        jButton7.setContentAreaFilled(false);
        jButton7.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton7.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/jMovieManager/swing/images/buttons/editDisabled.png"))); // NOI18N
        jButton7.setEnabled(false);
        jButton7.setMaximumSize(new java.awt.Dimension(32, 32));
        jButton7.setMinimumSize(new java.awt.Dimension(32, 32));
        jButton7.setPreferredSize(new java.awt.Dimension(32, 32));
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton7);

        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jMovieManager/swing/images/buttons/delete.png"))); // NOI18N
        jButton8.setToolTipText(bundle.getString("MovieManagerGUI.jButton8.tooltip")); // NOI18N
        jButton8.setBorderPainted(false);
        jButton8.setContentAreaFilled(false);
        jButton8.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton8.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/jMovieManager/swing/images/buttons/deleteDisabled.png"))); // NOI18N
        jButton8.setEnabled(false);
        jButton8.setMaximumSize(new java.awt.Dimension(32, 32));
        jButton8.setMinimumSize(new java.awt.Dimension(32, 32));
        jButton8.setPreferredSize(new java.awt.Dimension(32, 32));
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton8);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jMovieManager/swing/images/buttons/save.png"))); // NOI18N
        jButton2.setToolTipText(bundle.getString("MovieManagerGUI.jButton2.tooltip")); // NOI18N
        jButton2.setBorderPainted(false);
        jButton2.setContentAreaFilled(false);
        jButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton2.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/jMovieManager/swing/images/buttons/saveDisabled.png"))); // NOI18N
        jButton2.setEnabled(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setMaximumSize(new java.awt.Dimension(32, 32));
        jButton2.setMinimumSize(new java.awt.Dimension(32, 32));
        jButton2.setPreferredSize(new java.awt.Dimension(32, 32));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton2);

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jMovieManager/swing/images/buttons/open.png"))); // NOI18N
        jButton4.setToolTipText(bundle.getString("MovieManagerGUI.jButton4.tooltip")); // NOI18N
        jButton4.setBorderPainted(false);
        jButton4.setContentAreaFilled(false);
        jButton4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton4.setMaximumSize(new java.awt.Dimension(32, 32));
        jButton4.setMinimumSize(new java.awt.Dimension(32, 32));
        jButton4.setPreferredSize(new java.awt.Dimension(32, 32));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton4);

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jMovieManager/swing/images/buttons/calendar.png"))); // NOI18N
        jButton6.setToolTipText(bundle.getString("MovieManagerGUI.jButton6.tooltip")); // NOI18N
        jButton6.setBorderPainted(false);
        jButton6.setContentAreaFilled(false);
        jButton6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton6.setMaximumSize(new java.awt.Dimension(32, 32));
        jButton6.setMinimumSize(new java.awt.Dimension(32, 32));
        jButton6.setPreferredSize(new java.awt.Dimension(32, 32));
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton6);

        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jMovieManager/swing/images/buttons/playRandom.png"))); // NOI18N
        jButton9.setToolTipText(bundle.getString("MovieManagerGUI.jButton9.tooltip")); // NOI18N
        jButton9.setBorderPainted(false);
        jButton9.setContentAreaFilled(false);
        jButton9.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton9.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/jMovieManager/swing/images/buttons/playRandomDisabled.png"))); // NOI18N
        jButton9.setEnabled(false);
        jButton9.setMaximumSize(new java.awt.Dimension(32, 32));
        jButton9.setMinimumSize(new java.awt.Dimension(32, 32));
        jButton9.setPreferredSize(new java.awt.Dimension(32, 32));
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton9);

        jPanel5.add(jPanel4, java.awt.BorderLayout.WEST);

        jPanel7.setMaximumSize(new java.awt.Dimension(32767, 45));
        jPanel7.setMinimumSize(new java.awt.Dimension(200, 45));
        jPanel7.setPreferredSize(new java.awt.Dimension(685, 45));
        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 5, 10));

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText(bundle.getString("MovieManagerGUI.jLabel11")); // NOI18N
        jLabel11.setMaximumSize(new java.awt.Dimension(200, 25));
        jLabel11.setMinimumSize(new java.awt.Dimension(0, 25));
        jLabel11.setPreferredSize(new java.awt.Dimension(130, 25));
        jPanel7.add(jLabel11);

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setMaximumSize(new java.awt.Dimension(5, 25));
        jLabel12.setMinimumSize(new java.awt.Dimension(5, 25));
        jLabel12.setPreferredSize(new java.awt.Dimension(5, 25));
        jPanel7.add(jLabel12);

        jComboBox1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jComboBox1.setEnabled(false);
        jComboBox1.setMinimumSize(new java.awt.Dimension(135, 25));
        jComboBox1.setPreferredSize(new java.awt.Dimension(135, 25));
        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });
        jPanel7.add(jComboBox1);

        jTextField1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTextField1.setText(bundle.getString("MovieManagerGUI.jTextField1")); // NOI18N
        jTextField1.setEnabled(false);
        jTextField1.setMinimumSize(new java.awt.Dimension(6, 25));
        jTextField1.setPreferredSize(new java.awt.Dimension(200, 25));
        jTextField1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField1MouseClicked(evt);
            }
        });
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });
        jPanel7.add(jTextField1);

        jPanel5.add(jPanel7, java.awt.BorderLayout.EAST);

        jToolBar1.add(jPanel5);

        jPanel1.add(jToolBar1);

        jSplitPane1.setDividerLocation(260);
        jSplitPane1.setDividerSize(4);
        jSplitPane1.setMinimumSize(new java.awt.Dimension(0, 0));
        jSplitPane1.setPreferredSize(new java.awt.Dimension(1024, 700));

        jTabbedPane1.setPreferredSize(new java.awt.Dimension(260, 700));
        jTabbedPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTabbedPane1MouseClicked(evt);
            }
        });
        jTabbedPane1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tabChangeListener(evt);
            }
        });
        jTabbedPane1.addContainerListener(new java.awt.event.ContainerAdapter() {
            public void componentAdded(java.awt.event.ContainerEvent evt) {
                jTabbedPane1ComponentAdded(evt);
            }
            public void componentRemoved(java.awt.event.ContainerEvent evt) {
                jTabbedPane1ComponentRemoved(evt);
            }
        });
        jTabbedPane1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTabbedPane1KeyTyped(evt);
            }
        });
        jSplitPane1.setLeftComponent(jTabbedPane1);

        jPanel6.setMinimumSize(new java.awt.Dimension(760, 700));
        jPanel6.setPreferredSize(new java.awt.Dimension(760, 700));
        jPanel6.setLayout(new java.awt.BorderLayout());
        jSplitPane1.setRightComponent(jPanel6);

        jPanel1.add(jSplitPane1);

        jMenuBar1.setMaximumSize(new java.awt.Dimension(32769, 32769));
        jMenuBar1.setMinimumSize(new java.awt.Dimension(1, 25));
        jMenuBar1.setOpaque(false);
        jMenuBar1.setPreferredSize(new java.awt.Dimension(1024, 25));

        jMenu1.setText(bundle.getString("MovieManagerGUI.jMenu1")); // NOI18N
        jMenu1.setContentAreaFilled(false);
        jMenu1.setMinimumSize(new java.awt.Dimension(35, 19));

        jMenuItem1.setText(bundle.getString("MovieManagerGUI.jMenuItem1")); // NOI18N
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem3.setText(bundle.getString("MovieManagerGUI.jMenuItem3")); // NOI18N
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        jSeparator7.setOpaque(true);
        jMenu1.add(jSeparator7);

        jMenuItem7.setText(bundle.getString("MovieManagerGUI.jMenuItem7")); // NOI18N
        jMenuItem7.setEnabled(false);
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem7);

        jMenuItem8.setText(bundle.getString("MovieManagerGUI.jMenuItem8")); // NOI18N
        jMenuItem8.setEnabled(false);
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem8);

        jSeparator3.setOpaque(true);
        jMenu1.add(jSeparator3);

        jMenu4.setText(bundle.getString("MovieManagerGUI.jMenu4")); // NOI18N
        jMenu4.setEnabled(false);
        jMenu4.setOpaque(true);

        jMenuItem10.setText(bundle.getString("MovieManagerGUI.jMenuItem10")); // NOI18N
        jMenuItem10.setEnabled(false);
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem10);

        jMenuItem20.setText(bundle.getString("MovieManagerGUI.jMenuItem20")); // NOI18N
        jMenuItem20.setEnabled(false);
        jMenuItem20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem20ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem20);

        jMenu1.add(jMenu4);

        jMenu11.setText(bundle.getString("MovieManagerGUI.jMenu11")); // NOI18N
        jMenu11.setOpaque(true);

        jMenuItem21.setText(bundle.getString("MovieManagerGUI.jMenuItem21")); // NOI18N
        jMenuItem21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem21ActionPerformed(evt);
            }
        });
        jMenu11.add(jMenuItem21);

        jMenu1.add(jMenu11);

        jSeparator5.setOpaque(true);
        jMenu1.add(jSeparator5);

        jMenuItem4.setText(bundle.getString("MovieManagerGUI.jMenuItem4")); // NOI18N
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem4);

        jMenuBar1.add(jMenu1);

        jMenu2.setText(bundle.getString("MovieManagerGUI.jMenu2")); // NOI18N
        jMenu2.setContentAreaFilled(false);
        jMenu2.setMinimumSize(new java.awt.Dimension(65, 19));

        jMenuItem2.setText(bundle.getString("MovieManagerGUI.jMenuItem2")); // NOI18N
        jMenuItem2.setEnabled(false);
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem2);

        jMenuItem18.setText(bundle.getString("MovieManagerGUI.jMenuItem18")); // NOI18N
        jMenuItem18.setEnabled(false);
        jMenuItem18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem18ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem18);

        jMenuItem5.setText(bundle.getString("MovieManagerGUI.jMenuItem5")); // NOI18N
        jMenuItem5.setEnabled(false);
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem5);

        jMenuItem9.setText(bundle.getString("MovieManagerGUI.jMenuItem9")); // NOI18N
        jMenuItem9.setEnabled(false);
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem9);

        jSeparator4.setOpaque(true);
        jMenu2.add(jSeparator4);

        jMenuItem17.setText(bundle.getString("MovieManagerGUI.jMenuItem17")); // NOI18N
        jMenuItem17.setEnabled(false);
        jMenuItem17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem17ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem17);

        jMenuItem12.setText(bundle.getString("MovieManagerGUI.jMenuItem12")); // NOI18N
        jMenuItem12.setEnabled(false);
        jMenuItem12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem12ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem12);

        jMenuBar1.add(jMenu2);

        jMenu5.setText(bundle.getString("MovieManagerGUI.jMenu5")); // NOI18N
        jMenu5.setContentAreaFilled(false);
        jMenu5.setMinimumSize(new java.awt.Dimension(51, 19));

        jMenuItem24.setText(bundle.getString("MovieManagerGUI.jMenuItem24")); // NOI18N
        jMenuItem24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem24ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem24);

        jMenuBar1.add(jMenu5);

        jMenu6.setText(bundle.getString("MovieManagerGUI.jMenu6")); // NOI18N
        jMenu6.setContentAreaFilled(false);
        jMenu6.setMinimumSize(new java.awt.Dimension(39, 19));

        jMenuItem11.setText(bundle.getString("MovieManagerGUI.jMenuItem11")); // NOI18N
        jMenuItem11.setEnabled(false);
        jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem11ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem11);

        jMenuItem14.setText(bundle.getString("MovieManagerGUI.jMenuItem14")); // NOI18N
        jMenuItem14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem14ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem14);

        jSeparator6.setOpaque(true);
        jMenu6.add(jSeparator6);

        jMenuItem16.setText(bundle.getString("MovieManagerGUI.jMenuItem16")); // NOI18N
        jMenuItem16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem16ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem16);

        jMenuBar1.add(jMenu6);

        jMenu3.setText(bundle.getString("MovieManagerGUI.jMenu3")); // NOI18N
        jMenu3.setContentAreaFilled(false);
        jMenu3.setMinimumSize(new java.awt.Dimension(33, 19));

        jMenuItem19.setText(bundle.getString("MovieManagerGUI.jMenuItem19")); // NOI18N
        jMenuItem19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem19ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem19);

        jMenuItem22.setText(bundle.getString("MovieManagerGUI.jMenuItem22")); // NOI18N
        jMenuItem22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem22ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem22);

        jMenuItem6.setText(bundle.getString("MovieManagerGUI.jMenuItem6")); // NOI18N
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem6);

        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 743, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    /**
     * jMenuItem3 - Open <br/>
     * Load the database file manually from another filepath
     *
     * @param evt the triggered event 
     */
    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        //Überprüfe ob vorhandene Sammlung noch gespeichert werden muss
        if(PersistingManager.INSTANCE.isDocumentChanged()){
            SaveDialogGUI saveDialog = new SaveDialogGUI(this, true);
            int status = saveDialog.showDialog();

            if(status == SaveDialogGUI.APPROVE_OPTION){
                jMenuItem7ActionPerformed(evt);
            }
            else if(status == SaveDialogGUI.CANCEL_OPTION){
                return;
            }
        }
        //Erstelle einen neuen FileChooser zum Auswahl der Database File
        MyJFileChooser jFileChooser1 = new MyJFileChooser(Settings.getInstance().getHsqlDbPath(), JMMFileFilter.FilterType.DB);

        //zeige Dialog und mache etwas, wenn "Öffnen" geklickt wurde
        if(jFileChooser1.showOpenDialog(MovieManagerGUI.getInstance()) == JFileChooser.APPROVE_OPTION){
            final String openFilePath = jFileChooser1.getSelectedFile().toString().replace(".script", "");
            
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    instance.openOrRestoreDB(openFilePath);
                }
            });
            instance.clearAll();
        }
    }//GEN-LAST:event_jMenuItem3ActionPerformed
    
    /**
    * Öffnet die GUI zum hinzufügen eines neuen Films
    *
    * @param evt - Event, dass die Methode auslöst
    */
    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        CreateMovieGUI newMovieGUI = new CreateMovieGUI(this, true, true);
        Movie newMovie =  newMovieGUI.showGUI();
        if(newMovie != null){
            this.updateEnabledGUIElements();
        }                       
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    /**
     * Creates a new collection
     *
     * @param evt - Event, dass die Methode auslöst
     */
    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        //Überprüfe ob vorhandene Sammlung noch gespeichert werden muss
        if(PersistingManager.INSTANCE.isDocumentChanged()){
            SaveDialogGUI saveDialog = new SaveDialogGUI(this, true);
            int status = saveDialog.showDialog();

            if(status == SaveDialogGUI.APPROVE_OPTION){
                jMenuItem7ActionPerformed(evt);
            }
            else if(status == SaveDialogGUI.CANCEL_OPTION){
                return;
            }
        }
        
        Thread createNewCollectionThread = new Thread(new Runnable() {
            @Override
            public void run() {
                instance.clearAll(); 
                instance.setCursor(new Cursor(Cursor.WAIT_CURSOR));

                CollectionManager.INSTANCE.createNewCollections(
                        bundle.getString("DefaultCollectionName.serie"), 
                        bundle.getString("DefaultCollectionName.movie"));
                jTabbedPane1.setSelectedIndex(0);
                instance.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));   
            }
        });
        createNewCollectionThread.start();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    /**
     * @see GUI.MovieManagerGUI#jMenuItem1ActionPerformed(java.awt.event.ActionEvent) 
     */
    public void createNewCollection(){
        jMenuItem1ActionPerformed(null);
    }
    
    /**
     * jButton - Save <br/>
     * Save the database 
     *
     * @param evt the triggered event
     */
    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        //Wenn leer, dann mach "Speichern unter"
        if(Settings.getInstance().getHsqlDbPath().isEmpty()){
            jMenuItem8ActionPerformed(evt);
        }else{
            instance.setCursor(new Cursor(Cursor.WAIT_CURSOR)); 
            List<RunnableImpl> tasks = PersistingManager.INSTANCE.save();
            int totalElements = 0;
            for(RunnableImpl task: tasks){
                totalElements += task.getTotalElements();
            }
            DisplaySaveProgressGUI displaySaveProgress = new DisplaySaveProgressGUI(instance, true, totalElements);
            displaySaveProgress.setRunnableList(tasks);
            displaySaveProgress.showAndStart();
            instance.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));  
        }
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    /**
     * jButton - Save as <br/>
     * Saves the database into a file with a choosen path
     *
     * @param evt the triggered event
     */
    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        SaveDialogGUI saveDialog = new SaveDialogGUI(this, true);
        String savePath = saveDialog.initFileChooser();
        if(!savePath.isEmpty()){
            PersistingManager.INSTANCE.changeDBPath(savePath, PersistingManager.Process.SAVE);           
            jMenuItem7ActionPerformed(evt);
        }
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    /**
     * jMenuItem - Exit <br/>
     * Checks, if the data needs to be saved and exits the software
     *
     * @param evt the triggered event
     */
    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        if(this.saveAndDispose(false)){
            System.exit(0);
        }
    }//GEN-LAST:event_jMenuItem4ActionPerformed
   
    /**
     * Checks, if the data needs to be saved and disposes the GUI
     * @param createXMLBackup true If a xml export should also be started within the same path.
     * @return false, if the save dialog was cancled <br/> true otherwise
     */ 
    public boolean saveAndDispose(boolean createXMLBackup){
        //Überprüfe ob vorhandene Sammlung noch gespeichert werden muss
        if(PersistingManager.INSTANCE.isDocumentChanged()){
            SaveDialogGUI saveDialog = new SaveDialogGUI(this, true);
            int status = saveDialog.showDialog();
            
            if(status == SaveDialogGUI.APPROVE_OPTION){
                jMenuItem7ActionPerformed(null);
            }
            else if(status == SaveDialogGUI.CANCEL_OPTION){
                return false;
            }
        }
        if(createXMLBackup){
            String hsqlDbPath = Settings.getInstance().getHsqlDbPath();
            //check if an open database connection exist
            if(!hsqlDbPath.isEmpty()){
                XMLManager.INSTANCE.exportToXML(hsqlDbPath + ".xml", false);
            }
        }
        //Speichere die Settings bei Programmende
        if(!Settings.getInstance().save()){
            javax.swing.JOptionPane.showMessageDialog(this, bundle.getString("SettingsGUI.error.save"));    
        }
        PersistingManager.INSTANCE.destroy();
        this.dispose();
        return true;
    }
    
    /**
    * jMenuItem5 - update <br/>
    * Updates the selected file from the collection
    *
    * @param evt the triggered event
    */
    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        //Überprüfe, welches Panel im TabbedPane aktiv ist
        Component selectedComp = jTabbedPane1.getSelectedComponent();
        if(selectedComp != null){
            ((AbstractTabGUI)selectedComp).createUpdateSelectedMediaFileDialog(this);
        }
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    /**
    * jMenuItem9 - Delete <br/>
    * Removes the selected file from the collection
    *
    * @param evt the triggered event
    */
    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
        //Überprüfe, welches Panel im TabbedPane aktiv ist
        Component selectedComp = jTabbedPane1.getSelectedComponent();
         if(selectedComp != null){
             ((AbstractTabGUI)selectedComp).removeSelectedValue(); 
        }
         
        //Entferne Panel
        BorderLayout layout = (BorderLayout) jPanel6.getLayout();
        Component centerComponent = layout.getLayoutComponent(BorderLayout.CENTER); 
        if(centerComponent != null){
            
            
            
//            if(centerComponent instanceof AbstractDetailGUI){
//                if(((AbstractDetailGUI)centerComponent).getPictureThread().isAlive()){
//                    ((AbstractDetailGUI)centerComponent).getPictureThread().interrupt();
//                }
//            }
            
            
            
            
            jPanel6.remove(centerComponent);
        }
        jPanel6.repaint();
        SwingUtilities.invokeLater(actualizeEnabledGUIElements);
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    /**
    * Ruft die GUI "Über Movie Manager" auf
    *
    * @param evt - Event, dass die Methode auslöst
    */
    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        AboutGUI credits = new AboutGUI(this, false);
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    /**
    * jButton Methode, welche die zugehörige jMenuItem Methode aufruft
    *
    * @param evt - Event, dass die Methode auslöst
    */
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        jMenuItem2ActionPerformed(evt);
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
    * jButton Methode, welche die zugehörige jMenuItem Methode aufruft
    *
    * @param evt - Event, dass die Methode auslöst
    */
    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        jMenuItem3ActionPerformed(evt);
    }//GEN-LAST:event_jButton4ActionPerformed

    /**
    * jButton Methode, welche die zugehörige jMenuItem Methode aufruft
    *
    * @param evt - Event, dass die Methode auslöst
    */
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        jMenuItem7ActionPerformed(evt);
    }//GEN-LAST:event_jButton2ActionPerformed

    /**
    * Leert das Suchfeld
    *
    * @param evt - Event, dass die Methode auslöst
    */
    private void jTextField1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField1MouseClicked
        if((jTextField1.getText().equals(bundle.getString("MovieManagerGUI.jTextField1"))) && (jTextField1.isEnabled())){
            jTextField1.setText("");
        }
    }//GEN-LAST:event_jTextField1MouseClicked

    /**
    * jTextField1 - search <br/>
    * Looking for a file or a serie which starts with the given letters from the search box
    *
    * @param evt the triggered event
    */
    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        if(!jTextField1.getText().isEmpty()){
            Component selectedComp = jTabbedPane1.getSelectedComponent();
             if(selectedComp != null){
                 //Movie
                 ((AbstractTabGUI)selectedComp).setSelectedValue(jTextField1.getText());
            }   
        }
    }//GEN-LAST:event_jTextField1KeyReleased

    /**
    * Ruft die GUI für den FileImport auf
    *
    * @param evt - Event, dass die Methode auslöst
    */
    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
        ImportDirectoryGUI fileImport = new ImportDirectoryGUI(this, true, getTabNames());
        fileImport.showGUI();
    }//GEN-LAST:event_jMenuItem10ActionPerformed

    /**
    * Sortiert die Liste nach dem ausgewählten Kriterium
    *
    * @param evt - Event, dass die Methode auslöst
    */
    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        //0 Name, 1 Laufzeit, 2 Erscheinungsjahr, 3 IMDB Bewertung, 4 Persönlichde Bewertung
        if(evt.getStateChange() == ItemEvent.SELECTED){
            Order order;
            int index = jComboBox1.getSelectedIndex();
            if(index == 1){
                order = Order.ORDER_BY_PLAYTIME;
            } else if(index == 2){
                order = Order.ORDER_BY_YEAR;
            } else if(index == 3){
                order = Order.ORDER_BY_ONLINE_RATING;
            } else if(index == 4){
                order = Order.ORDER_BY_PERS_RATING;
            } else{
                order = Order.ORDER_BY_NAME;
            }            
            Component selectedComponent = jTabbedPane1.getSelectedComponent();
            if(selectedComponent instanceof AbstractTabGUI){
                CollectionManager.INSTANCE.orderCollectionBy(order, ((AbstractTabGUI)selectedComponent).getCollection());
            }
        }
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    /**
    * Ruft die Statistik GUI auf
    *
    * @param evt - Event, dass die Methode auslöst
    */
    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem11ActionPerformed
        StatisticGUI movieStatistic = new StatisticGUI(this, true);
    }//GEN-LAST:event_jMenuItem11ActionPerformed

    /**
     * jButton7 - Edit Button <br/>
     * Edits the selected entry from
    *
    * @param the triggered event
    */
    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        jMenuItem5ActionPerformed(evt);
    }//GEN-LAST:event_jButton7ActionPerformed

    /**
     * jButton8 - Delete Selected element <br/>
     * Deletes the selected element from the collection
    *
    * @param evt the triggered event
    */
    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        jMenuItem9ActionPerformed(evt);
    }//GEN-LAST:event_jButton8ActionPerformed

    /**
    * Ruft die GUI zum Umbenennen der Filmdateien auf
    *
    * @param evt - Event, dass die Methode auslöst
    */
    private void jMenuItem12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem12ActionPerformed
        Component selComp = jTabbedPane1.getSelectedComponent();
        if((selComp instanceof MovieTabGUI) && (!((MovieTabGUI)selComp).isSearchTab())){
            MovieCollection colForRename = ((MovieTabGUI)selComp).getCollection();
            javax.swing.JOptionPane.showMessageDialog(this, bundle.getString("RenameFilesGUI.warning.save"));          
            RenameFilesGUI renameFile = new RenameFilesGUI(this, true, colForRename);  
            renameFile.setVisible(true);
        }  
    }//GEN-LAST:event_jMenuItem12ActionPerformed
    

    /**
    * Entfernt das Filmbeschreibungspanel und fügt
    * das Panel für Neuerscheinungen auf der GUI hinzu
    *
    * @param evt - Event, dass die Methode auslöst
    */
    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        jMenuItem14ActionPerformed(evt);
    }//GEN-LAST:event_jButton6ActionPerformed

    /**
    * jMenuItem14 - Calendar <br/>
    * Removes the movie detail panel and 
    * adds the cinema panel for new releases
    *
    * @param evt the triggered event
    */
    private void jMenuItem14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem14ActionPerformed
        if(!jmm.utils.Utils.isInternetConnectionAvailable()){
            javax.swing.JOptionPane.showMessageDialog(this, bundle.getString("MovieManagerGUI.warning.noInternet"));          
            return;
        }
        Component selComp = jTabbedPane1.getSelectedComponent();
        if(selComp != null){
            ((AbstractTabGUI)selComp).clearSelection();
        }
        
        BorderLayout layout = (BorderLayout) jPanel6.getLayout();
        Component centerComp = layout.getLayoutComponent(BorderLayout.CENTER);
        if(centerComp != null){
            jPanel6.remove(centerComp);
        }

        jPanel6.add(ReleaseCalendarGUI.getInstance().getReleasePanel(), BorderLayout.CENTER);
        //this.pack();
        jPanel6.repaint();
    }//GEN-LAST:event_jMenuItem14ActionPerformed

    /**
    * jMenuItem16 - Settings GUI <br/>
    * Opens the settingsGUI
    *
    * @param evt the triggered event
    */
    private void jMenuItem16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem16ActionPerformed
        boolean oldValue = Settings.getInstance().isDisplayWatchedIcon();
        SettingsGUI settings = new SettingsGUI(this, true);
        //Wenn der Wert geändert wurde, aktualisiere die Filmliste
        if(oldValue != Settings.getInstance().isDisplayWatchedIcon()){
            Component selComp = jTabbedPane1.getSelectedComponent();
            if(selComp != null){
                selComp.repaint();
            }
        }
    }//GEN-LAST:event_jMenuItem16ActionPerformed

    /**
    * jMenuItem17 - Extended Search <br/>
    * Opens the extendedSearchGUI
    *
    * @param evt the triggered event
    */
    private void jMenuItem17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem17ActionPerformed
        if(PersistingManager.INSTANCE.isDocumentChanged()){
            javax.swing.JOptionPane.showMessageDialog(this, bundle.getString("ExtendedSearchGUI.warning.save"));
        }      
        if(!PersistingManager.INSTANCE.isDatabaseOnline()){
            return;
        }
        MediaCollection oldCollection = ExtendedSearch.getInstance().getSearchCollection();

        MediaCollection searchResults = ExtendedSearchGUI.getInstance().showFilterGUI();
        if(searchResults != null){  
            if(oldCollection != null){
                this.removeElement(oldCollection);
            }
            int tabIndex = jTabbedPane1.getTabCount()-1; 
            searchResults.setTabNumber(tabIndex);
            this.addElement(searchResults, true);
        }
    }//GEN-LAST:event_jMenuItem17ActionPerformed

    /**
     * Öffnet die GUI zum hinzufügen einer neuen Serie
     *
     * @param evt - Event, dass die Methode auslöst
     **/
private void jMenuItem18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem18ActionPerformed
    CreateSerieGUI newSerieGUI = new CreateSerieGUI(this, true, true);
    Serie newSerie =  newSerieGUI.showGUI();
    if(newSerie != null){
        this.updateEnabledGUIElements();
    }  
}//GEN-LAST:event_jMenuItem18ActionPerformed

/**
    * jButton Methode, welche die zugehörige jMenuItem Methode aufruft
    *
    * @param evt - Event, dass die Methode auslöst
    */
private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
    jMenuItem18ActionPerformed(evt);
}//GEN-LAST:event_jButton5ActionPerformed


/**
* MenuButton - Minimieren
* Minimiert das Programm
* 
* @param evt Event, welches ausgelöst wird 
*/
    private void jMenu8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu8MouseClicked
        //this.setState(JFrame.ICONIFIED); //JFrame.NORMAL
        this.setExtendedState(JFrame.ICONIFIED);
    }//GEN-LAST:event_jMenu8MouseClicked

/**
* MenuButton - Maximieren
* Maximiert das Programm 
* 
* @param evt Event, welches ausgelöst wird 
 */  
    private void jMenu9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu9MouseClicked
        if(this.getExtendedState() == JFrame.MAXIMIZED_BOTH){
            this.setExtendedState(JFrame.NORMAL);
        }
        else{
            this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        }
    }//GEN-LAST:event_jMenu9MouseClicked

/**
* MenuButton - Schließen
* Schließt das Programm 
* 
* @param evt Event, welches ausgelöst wird
*/
    private void jMenu10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu10MouseClicked
        jMenuItem4ActionPerformed(null);
    }//GEN-LAST:event_jMenu10MouseClicked

    /**
    * Menubutton - Update Check
    * 
    * @param evt Event, welches ausgelöst wird 
    */
    private void jMenuItem19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem19ActionPerformed
        final UpdateGUI updateGUI = new UpdateGUI(this, true);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                updateGUI.updateAvailable();
            }
        });
        updateGUI.setVisible(true);
    }//GEN-LAST:event_jMenuItem19ActionPerformed

    /**
    * Menubutton - XML Import
    * 
    * @param evt Event, welches ausgelöst wird 
    */    
    private void jMenuItem20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem20ActionPerformed
        //Erstelle einen neuen FileChooser zum Auswahl der XML Datei
        MyJFileChooser jFileChooser1 = new MyJFileChooser(Settings.getInstance().getXMLPath(), JMMFileFilter.FilterType.XML);
        
        //zeige Dialog und mache etwas, wenn "Öffnen" geklickt wurde
        if(jFileChooser1.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
            final String savePath = jFileChooser1.getSelectedFile().toString();
            
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    //clearAll();
                    instance.openXMLFile(savePath);
                }
            });
        }
    }//GEN-LAST:event_jMenuItem20ActionPerformed

    /**
     * MenuItem - Export XML File <br/>
     * 
     * Export the current Database into an XML file
     * @param evt the triggered event 
     * 
     */
    private void jMenuItem21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem21ActionPerformed
       MyJFileChooser xmlFileChooser = new MyJFileChooser(Settings.getInstance().getXMLPath(), JMMFileFilter.FilterType.XML);
       //Mehrfachauswahl erlaubt
       xmlFileChooser.setMultiSelectionEnabled(false);
       xmlFileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
       
       //zeige Dialog und mache etwas, wenn "Speichern" geklickt wurde
        if(xmlFileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION){
            String filePath = xmlFileChooser.getSelectedFile().getAbsolutePath();
            if(!filePath.endsWith(".xml")){
                filePath = filePath + ".xml";
            }            
            Settings.getInstance().setXMLPath(filePath);
            //Run exportProcess asynchron zur GUI, damit kein OutOfMemoryError auftritt
            instance.setCursor(new Cursor(Cursor.WAIT_CURSOR));            
            Thread exportThread = new Thread(exportProcess);
            exportThread.start();
//            SwingUtilities.invokeLater(exportProcess);                       
        }
    }//GEN-LAST:event_jMenuItem21ActionPerformed

    /**
     * jTabbedPane1 - Edit Tab <br/>
     * Edit an existing tab on double click
     * 
     * @param evt the triggered event
     */
    private void jTabbedPane1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabbedPane1MouseClicked
        //Determine the tab where the click event come from
        int tabIndex = jTabbedPane1.indexAtLocation(evt.getX(), evt.getY());
        //If last Tab was selected ("+")
        if(tabIndex == jTabbedPane1.getTabCount()-1){
            AddTabGUI newTab = new AddTabGUI(this, true);
            newTab.setVisible(true);
            //wenn dialog nicht abbgebrochen
            if(newTab.getTabName() != null){          
                if(newTab.getKindOfTab() == FileTypeInterface.MediaType.MOVIE){
                    CollectionManager.INSTANCE.createCollection(newTab.getTabName(), true, newTab.getPosition()-1);
                }
                else{               
                    CollectionManager.INSTANCE.createCollection(newTab.getTabName(), false, newTab.getPosition()-1);
                }
                //Wähle neuen Tab aus
                int newSelTabIndex = newTab.getPosition()-1;
                jTabbedPane1.setSelectedIndex(newSelTabIndex);
                updateEnabledGUIElements();
            }
        }
        else if((evt.getClickCount() > 1)){
           Component selTab = jTabbedPane1.getSelectedComponent();
           if((selTab != null) && (!((AbstractTabGUI)selTab).isSearchTab())){                       
               AddTabGUI newTab = new AddTabGUI(this, true, selTab.getName(), jTabbedPane1.getSelectedIndex()+1 , ((AbstractTabGUI)selTab).getKindOfTab());
               newTab.setVisible(true);

               //actualize the Tab name and position
               if(newTab.getTabName() != null){
                   if(newTab.isDeleteTab()){
                       removeSelectedTab();
                   }else{
                       CollectionManager.INSTANCE.updateCollection(selTab.getName(), newTab.getTabName(), newTab.getPosition()-1);
                   }
               }                  
           }  
       } 
    }//GEN-LAST:event_jTabbedPane1MouseClicked
    
    /**
     * Will be performed if another tab is selected
     * @param evt the triggered event
     */
    private void tabChangeListener(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabChangeListener
       Component selComp = jTabbedPane1.getSelectedComponent();
       if((selComp != null) && (selComp instanceof AbstractTabGUI)){
           AbstractTabGUI selTab = (AbstractTabGUI)selComp;
           Order order = selTab.getCollection().getOrdering();
           
           if(order != null){
               int selIndex = -1;
                if(order == Order.ORDER_BY_NAME){
                    selIndex = 0;
                }
                else if(order == Order.ORDER_BY_PLAYTIME){
                    selIndex = 1;
                }
                else if(order == Order.ORDER_BY_YEAR){
                    selIndex = 2;
                }
                else if(order == Order.ORDER_BY_ONLINE_RATING){
                    selIndex = 3;
                }
                else if(order == Order.ORDER_BY_PERS_RATING){
                    selIndex = 4;
                }
                //unregister and register listener
                ItemListener[] listenerArray = jComboBox1.getItemListeners();
                for(ItemListener listener: listenerArray){
                    jComboBox1.removeItemListener(listener);
                }
                jComboBox1.setSelectedIndex(selIndex);
                for(ItemListener listener: listenerArray){
                    jComboBox1.addItemListener(listener);
                }
           }             
       }
        
        updateEnabledGUIElements();
    }//GEN-LAST:event_tabChangeListener

    /**
     * jTabbedPane1 - Remove Tag <br/>
     * Deletes the tab if ENTF is pressed
     * 
     * @param evt the triggered event
     */
    private void jTabbedPane1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTabbedPane1KeyTyped
        if(evt.getKeyChar() == KeyEvent.VK_DELETE){
            removeSelectedTab();
        }
    }//GEN-LAST:event_jTabbedPane1KeyTyped

    /**
     * jTabbedPane1 - Update collection tab numbers <br/>
     * Updates all collections to the latest tab number after a component was added
     * 
     * @param evt the triggered event 
     */
    private void jTabbedPane1ComponentAdded(java.awt.event.ContainerEvent evt) {//GEN-FIRST:event_jTabbedPane1ComponentAdded
        updateTabNumbersOnCollections();
    }//GEN-LAST:event_jTabbedPane1ComponentAdded

    /**
     * jTabbedPane1 - Update collection tab numbers <br/>
     * Updates all collections to the latest tab number after a component was removed
     * 
     * @param evt the triggered event 
     */
    private void jTabbedPane1ComponentRemoved(java.awt.event.ContainerEvent evt) {//GEN-FIRST:event_jTabbedPane1ComponentRemoved
        updateTabNumbersOnCollections();
    }//GEN-LAST:event_jTabbedPane1ComponentRemoved

    /**
     * jMenuItem22 - Open help board <br/>
     * Opens the browser with the jMM board
     * @param evt the triggered event
     */
    private void jMenuItem22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem22ActionPerformed
        if(jmm.utils.Utils.isInternetConnectionAvailable()){
            try {
                OperatingSystem.openBrowser("http://board.jmoviemanager.de/wbb/");
            } catch (IOException | URISyntaxException ex) {
                LOG.error("URL: " + "http://board.jmoviemanager.de/wbb/" + " could not be opened in the browser.", ex);
            } 
        }
    }//GEN-LAST:event_jMenuItem22ActionPerformed

    /**
     * jMenu5 - Other language <br/>
     * Opens the browser and calls the support us website
     * @param evt the triggered event
     */
    private void jMenuItem24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem24ActionPerformed
        if(jmm.utils.Utils.isInternetConnectionAvailable()){
            try {
                OperatingSystem.openBrowser("http://jmoviemanager.de/index.php/supportus");
            } catch (IOException | URISyntaxException ex) {
                LOG.error("URL: " + "http://jmoviemanager.de/index.php/supportus" + " could not be opened in the browser.", ex);
            } 
        }    
    }//GEN-LAST:event_jMenuItem24ActionPerformed


    /**
     * jFrame - Maximize Window <br/>
     * Determines, if the window has been maximize and updates the Settings
     * @param evt the triggered event
     */
    private void formWindowStateChanged(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowStateChanged
        if(evt.getNewState() == Frame.MAXIMIZED_BOTH){
            Settings.getInstance().setStartupDisplaySetting(Settings.displaySize_MAXIMIZED);
        }else if(evt.getNewState() != Frame.ICONIFIED){
            Settings.getInstance().setStartupDisplaySetting(Settings.displaySize_DEFAULT);
        }
    }//GEN-LAST:event_formWindowStateChanged
 
    /**
    * jButton Methode, welche die zugehörige jMenuItem Methode aufruft
    *
    * @param evt - Event, dass die Methode auslöst
    */
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        jMenuItem1ActionPerformed(evt);
    }//GEN-LAST:event_jButton3ActionPerformed
 
    /**
    * jButton9 - play random <br/>
    * Randomly select and play a media file from the currently opened collection
    * 
    * @param evt the triggered event
    */
    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        AbstractTabGUI tab = (AbstractTabGUI)jTabbedPane1.getSelectedComponent();
        if(!tab.getCollection().isEmpty()){
            Random random = new Random();
            String title;
            String filePath;
            int maxLoop = 5;
            if(tab instanceof  MovieTabGUI){
                Movie movie;
                do{
                    int index = random.nextInt(tab.getCollection().size());
                    movie = ((MovieTabGUI)tab).getCollection().get(index);
                    maxLoop--;
                }while(movie.getFilePaths().isEmpty() && maxLoop > 0);
                if(maxLoop == 0){
                    return;
                }
                
                title = movie.toString();
                filePath = movie.getFilePath(0);  
                //Select tab content
                tab.setSelectedValue(title);
            }
            else{
                Serie serie;
                Season season;
                Episode episode;
                do{
                    int index = random.nextInt(tab.getCollection().size());
                    serie = ((SerieTabGUI)tab).getCollection().get(index);
                    
                    index = random.nextInt(serie.getSeasons().size());
                    season = serie.getSeason(index);
                    
                    index = random.nextInt(season.getEpisodes().size());
                    episode = season.getEpisode(index);
                    maxLoop--;
                }while(episode.getFilePaths().isEmpty() && maxLoop > 0);
                if(maxLoop == 0){
                    return;
                }
//                title = episode.toString();
                filePath = episode.getFilePath(0);  
                //Select tab content
                ((SerieTabGUI)tab).setSelectedValue(episode);
            }
            try {
                //Play
                OperatingSystem.openFile(filePath);
            } catch (IOException ex) {
                LOG.error("Cannot open file: " + filePath, ex);
            }
            //minimize GUI
//            MovieManagerGUI.getInstance().setExtendedState(JFrame.ICONIFIED);  
        }
    }//GEN-LAST:event_jButton9ActionPerformed

    /**
    * Method which will be triggered if the application will be de-iconified<br/>
    * 
    * @param evt the triggered event
    */
    private void formWindowDeiconified(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowDeiconified
        if(Settings.getInstance().getStartupDisplaySetting() == Settings.displaySize_MAXIMIZED){
            this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        }                
    }//GEN-LAST:event_formWindowDeiconified

    /**
     * removes the selected tab from the database
     * and the gui (observer pattern)
     */
    private void removeSelectedTab(){
        Component selComp = jTabbedPane1.getSelectedComponent();  
        if((selComp instanceof AbstractTabGUI)){
            MediaCollection collection = ((AbstractTabGUI)selComp).getCollection();
            if(javax.swing.JOptionPane.showConfirmDialog(this, 
                    bundle.getString("ConfirmDialogGUI.removeTab.message"),
                    bundle.getString("ConfirmDialogGUI.removeTab.title"),
                    JOptionPane.YES_NO_CANCEL_OPTION, 
                    JOptionPane.PLAIN_MESSAGE) == JOptionPane.YES_OPTION){
                //Entferne Tab
                if(!((AbstractTabGUI)selComp).isSearchTab()){
                    CollectionManager.INSTANCE.removeCollection(collection);
                }else{
                    this.removeElement(collection);
                }
                jTabbedPane1.setSelectedIndex(0);
            }
        }
    }
    
    /**
     * Checks if the java version is > 1.7. <br/>
     * If not, the software will be closed.
     */
    private void checkJavaVersion(){
        String javaVersion = System.getProperty("java.version");
        double version = Double.valueOf(javaVersion.substring(0, 3));
        if(version <= 1.6){
            javax.swing.JOptionPane.showMessageDialog(this, 
                    "<html>" + 
                    bundle.getString("MovieManagerGUI.warning.javaVersion1") + 
                    "<b>" + javaVersion + "</b>.<br/>" + 
                    bundle.getString("MovieManagerGUI.warning.javaVersion2") +
                    "</html>" ); 
            //redirect to java download
            try {
            OperatingSystem.openBrowser("http://www.java.com/");
            } catch (IOException | URISyntaxException ex) { 
                LOG.error("URL: " + "http://www.java.com/" + " could not be opened in the browser.", ex);
            }
            System.exit(0);
        }            
    }
    
    /**
     * Creates a jRadioButtonMenuItem for each language which is supported by jMM.
     */
    private void createLanguageMenuItems(){
        int i = 0;
        for(LocaleManager.SupportedLanguages sLanguage: LocaleManager.SupportedLanguages.values()){
            final Locale locale = sLanguage.getLocale();
            JRadioButtonMenuItem radioButtonMenuItem = new JRadioButtonMenuItem();
            
            buttonGroup1.add(radioButtonMenuItem);
            radioButtonMenuItem.setText(bundle.getString("MovieManagerGUI.jRadioButtonMenuItem." + locale.toString())); // NOI18N
            radioButtonMenuItem.setMinimumSize(new java.awt.Dimension(0, 22));
            radioButtonMenuItem.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    LocaleManager.getInstance().setCurrentLocale(locale);
                }
            });
            jMenu5.add(radioButtonMenuItem, i);
            i++;
            localeToMenuItemMap.put(locale, radioButtonMenuItem);
        }
    }
    
//    /**
//     * Displays a warning message for 1 time
//     */
//    private void displayWarningMessage(){
//        if(!Settings.getInstance().isWaringDataStructureShown()){
//            javax.swing.JOptionPane.showMessageDialog(this, bundle.getString("MovieManagerGUI.warning.dataStructure"));
//            Settings.getInstance().setWaringDataStructureShown(true);
//        }
//    }
    
   /**
   * Singleton Instanz der GUI Klasse
   */
   public static MovieManagerGUI getInstance(){
       if(instance == null){
           instance = new MovieManagerGUI();
           
           instance.checkJavaVersion();
//           instance.displayWarningMessage();
            /*
             * Register Observer
             */
           MediaCollection.addCollectionObserver(instance);
           LocaleManager.getInstance().addObserver(instance); 
       }
       return instance;
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
    }
      
//    /**
//     * Returns the number of tabs in the tabbedpane.
//     * 
//     * @return an integer specifying the number of tabbed pages
//     */
//    public int getTabCount(){
//        return jTabbedPane1.getTabCount();
//    }
    
    /**
     * Läd eine XML Datei und importiert die Elemente in den Datenbestand und GUI
     * @param filePath Der Dateipfad
     * @return true Import war erfolgreich
     */
    public boolean openXMLFile(String filePath){
        boolean success = false;
        File openFile = new File(filePath);
        if(openFile.exists() && openFile.isFile()){
            this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            this.setStatisticButtonEnabled(false);
            if(XMLManager.INSTANCE.importFromXML(filePath)){
                updateEnabledGUIElements();
            }
            else{
            javax.swing.JOptionPane.showMessageDialog(instance, bundle.getString("MovieManagerGUI.error.fileOpen"));        
            }
            this.setStatisticButtonEnabled(true);
            this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            success = true;
        }          
        return success;
    }
    
    /**
     * Load the database into the GUI.
     * 
     * @param filePath the filepath from the database file
     * @return true if the database was successfull opened <br/> 
     *         false otherwise
     */
    public boolean openOrRestoreDB(final String filePath){
        //TODO: Disable all GUI Elements
        
        this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        this.setStatisticButtonEnabled(false);
        
        boolean openSuccess;
        try{
            openSuccess = PersistingManager.INSTANCE.open(filePath);
        }catch(Exception ex){
            LOG.error("Cannot open database: " + filePath, ex);
            openSuccess = false;
        }

        //try to restore database with xml backup
        boolean importSuccess = false;
        if(!openSuccess){
            importSuccess = XMLManager.INSTANCE.restore(filePath);
        }

        if(openSuccess || importSuccess){
            updateEnabledGUIElements();
        }
        else{              
            javax.swing.JOptionPane.showMessageDialog(this, bundle.getString("MovieManagerGUI.error.fileOpen"));        
        }
        this.setStatisticButtonEnabled(true);
        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); 
        
        //TODO: Enable all GUI Elements
        
        return openSuccess || importSuccess;
    }     
       

    /**
     * De-/Aktiviert den Statistik Menüpunkt <br/>
     * Wird benötigt, damit während des Ladeprozesses keine Zugriffsverletzung auftritt
     * @param enabled false Menüpunkt wird deaktiviert<br/>true ansonsten
     */
    public void setStatisticButtonEnabled(boolean enabled){
        jMenuItem11.setEnabled(enabled);
    }
        
   /**
   * Methode welche die Componenten des jList1 PopupMenu initialisiert
   */
   private void initPopupMenu(){
       popupAdd = new JMenuItem(bundle.getString("MovieManagerGUI.jMenuItem2"));
       popupAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jMenuItem2ActionPerformed(e);
            }
        });
       popupAdd.setEnabled(false);


       popupEdit = new JMenuItem(bundle.getString("MovieManagerGUI.jMenuItem5"));
       popupEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jMenuItem5ActionPerformed(e);
            }
        });
        popupEdit.setEnabled(false);

       popupDelete = new JMenuItem(bundle.getString("MovieManagerGUI.jMenuItem9"));
       popupDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jMenuItem9ActionPerformed(e);
            }
        });
        popupDelete.setEnabled(false);
        
       popupAbout = new JMenuItem(bundle.getString("MovieManagerGUI.jMenuItem6"));
       popupAbout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jMenuItem6ActionPerformed(e);
            }
        });
        
       jPopupMenu1.add(new JSeparator());
       jPopupMenu1.add(popupAdd);
       jPopupMenu1.add(popupEdit);
       jPopupMenu1.add(popupDelete);
       jPopupMenu1.add(new JSeparator());
       jPopupMenu1.add(new JSeparator());
       jPopupMenu1.add(popupAbout);
//       jTabbedPane1.add(jPopupMenu1);
   }

   /**
    * Clears the center component and remove the search tab, if exist
   */
   public void clearAll(){
       int tabCount = jTabbedPane1.getTabCount();
       //ignore the tab (+) 
       for(int i = 0; i < tabCount; i ++){
           Component tab = jTabbedPane1.getComponentAt(i);
           if((tab != null) && (tab instanceof  AbstractTabGUI)){
               if(((AbstractTabGUI)tab).isSearchTab()){
                   jTabbedPane1.remove(i);
                   //                   jTabbedPane1.remove(tab);
                   break;
               }
           }
       }
       
        Component centerComponent = ((BorderLayout) jPanel6.getLayout()).getLayoutComponent(BorderLayout.CENTER);
        if((centerComponent != null)&& (centerComponent.equals(MovieDetailGUI.getInstance().getRootPanel()))){
            jPanel6.remove(centerComponent);
        }
        
        SwingUtilities.invokeLater(new Runnable() {
           @Override
           public void run() {
               jPanel6.repaint();
           }
       });
//        this.repaint();
        updateEnabledGUIElements();
   }    
   
//   private void updateComponentLocalization(Class guiClass, ResourceBundle bundle, JComponent component){
//       int compCount = component.getComponentCount();
//       for(int i = 0; i < compCount; i++){
//          JComponent compToLocalize = (JComponent) component.getComponent(i);
//          
//          updateComponentLocalization(guiClass, bundle, compToLocalize);
//          
//          if(compToLocalize instanceof AbstractButton){ //JMenu, JMenuItem, JButton
//              AbstractButton button = (AbstractButton)compToLocalize;
//              String resourceName = guiClass.getName() + "." + button.getName();
//              try{
//                  button.setText(bundle.getString(resourceName));
//                  button.setToolTipText(bundle.getString(resourceName + ".tooltip"));
//              }
//              catch(Exception ex){   //MissingResourceException, NullPointerException      
//              }
//          }
//          else if(compToLocalize instanceof JLabel){
//              JLabel label = (JLabel)compToLocalize;
//              String resourceName = guiClass.getName() + "." + label.getName();
//              try{
//                  label.setText(bundle.getString(resourceName));
//              }
//              catch(Exception ex){   //MissingResourceException, NullPointerException      
//              }              
//          }
//          else if(compToLocalize instanceof JTabbedPane){
//              JTabbedPane tabbedPane = (JTabbedPane)compToLocalize;
//              LinkedList<JComponent> tabs = new LinkedList<JComponent>();
//              int tabCount = tabbedPane.getComponentCount();
//              
//              for(int j = 0; j < tabCount; j ++){
//                  //Sichere das Tab
//                  JPanel tab = (JPanel)tabbedPane.getComponent(j);
//                  String resourceName = guiClass.getName() + "." + tab.getName();
//                  //Entferne das Tab
//                  tabbedPane.remove(j);
//                  //Füge es mit neuem Namen hinz
//                  tabbedPane.insertTab(bundle.getString(resourceName), null, tab, "", j);
//              }            
//          }
//          else if(compToLocalize instanceof JPanel){
//              JPanel panel = (JPanel)compToLocalize;
//              
//              if((panel.getBorder() != null) && (panel.getBorder() instanceof TitledBorder)){
//                  String resourceName = guiClass.getName() + "." + panel.getName();
//                  TitledBorder titledBorder = (TitledBorder)panel.getBorder();
//                  try{
//                      titledBorder.setTitle(bundle.getString(resourceName));
//                  }
//                  catch(Exception ex){   //MissingResourceException, NullPointerException   
//                  }      
//              }
//          }          
//       }
//   }
   
   /**
   * Aktualisiert alle Labels, Buttons, Tooltips und sonstigen Texte der GUI
   * auf das gesetzte Locale
   */
   private void updateGUILocalization(){
        bundle = ResourceBundle.getBundle("jMovieManager.swing.resources.MovieManager");
        JOptionPane.setDefaultLocale(LocaleManager.getInstance().getCurrentLocale());
        JFileChooser.setDefaultLocale(LocaleManager.getInstance().getCurrentLocale());
        this.setTitle(bundle.getString("MovieManagerGUI.headline"));
       
      //TODO: Reflection: Gehe alle UI Elemente durch und setzte neuen Text
      // Utils.updateAllComponentLocalizations(bundle, this.getRootPane());

       jButton1.setToolTipText(bundle.getString("MovieManagerGUI.jButton1.tooltip"));
       jButton2.setToolTipText(bundle.getString("MovieManagerGUI.jButton2.tooltip"));
       jButton3.setToolTipText(bundle.getString("MovieManagerGUI.jButton3.tooltip"));
       jButton4.setToolTipText(bundle.getString("MovieManagerGUI.jButton4.tooltip"));
       jButton5.setToolTipText(bundle.getString("MovieManagerGUI.jButton5.tooltip"));
       jButton6.setToolTipText(bundle.getString("MovieManagerGUI.jButton6.tooltip"));
       jButton7.setToolTipText(bundle.getString("MovieManagerGUI.jButton7.tooltip"));
       jButton8.setToolTipText(bundle.getString("MovieManagerGUI.jButton8.tooltip"));
       jButton9.setToolTipText(bundle.getString("MovieManagerGUI.jButton9.tooltip"));

       jComboBox1.removeAll();
       //comboBoxData.removeAllElements();
       comboBoxData = new DefaultComboBoxModel(new String[] {
           bundle.getString("MovieManagerGUI.jComboBox1.value1"),
           bundle.getString("MovieManagerGUI.jComboBox1.value2"),
           bundle.getString("MovieManagerGUI.jComboBox1.value3"),
           bundle.getString("MovieManagerGUI.jComboBox1.value4"),
           bundle.getString("MovieManagerGUI.jComboBox1.value5")
       });    
       jComboBox1.setModel(comboBoxData);
       
       jLabel11.setText(bundle.getString("MovieManagerGUI.jLabel11"));
       jMenu1.setText(bundle.getString("MovieManagerGUI.jMenu1"));
       jMenu2.setText(bundle.getString("MovieManagerGUI.jMenu2"));
       jMenu3.setText(bundle.getString("MovieManagerGUI.jMenu3"));
       jMenu4.setText(bundle.getString("MovieManagerGUI.jMenu4"));
       jMenu5.setText(bundle.getString("MovieManagerGUI.jMenu5"));
       jMenu6.setText(bundle.getString("MovieManagerGUI.jMenu6"));
       jMenuItem1.setText(bundle.getString("MovieManagerGUI.jMenuItem1"));
       jMenuItem2.setText(bundle.getString("MovieManagerGUI.jMenuItem2"));
       jMenuItem3.setText(bundle.getString("MovieManagerGUI.jMenuItem3"));
       jMenuItem4.setText(bundle.getString("MovieManagerGUI.jMenuItem4"));
       jMenuItem5.setText(bundle.getString("MovieManagerGUI.jMenuItem5"));
       jMenuItem6.setText(bundle.getString("MovieManagerGUI.jMenuItem6"));
       jMenuItem7.setText(bundle.getString("MovieManagerGUI.jMenuItem7"));
       jMenuItem8.setText(bundle.getString("MovieManagerGUI.jMenuItem8"));
       jMenuItem9.setText(bundle.getString("MovieManagerGUI.jMenuItem9"));
       jMenuItem10.setText(bundle.getString("MovieManagerGUI.jMenuItem10"));
       jMenuItem11.setText(bundle.getString("MovieManagerGUI.jMenuItem11"));
       jMenuItem12.setText(bundle.getString("MovieManagerGUI.jMenuItem12"));
       jMenuItem14.setText(bundle.getString("MovieManagerGUI.jMenuItem14"));
       jMenuItem16.setText(bundle.getString("MovieManagerGUI.jMenuItem16"));
       jMenuItem17.setText(bundle.getString("MovieManagerGUI.jMenuItem17"));
       jMenuItem18.setText(bundle.getString("MovieManagerGUI.jMenuItem18"));
       jMenuItem19.setText(bundle.getString("MovieManagerGUI.jMenuItem19"));
       jMenuItem20.setText(bundle.getString("MovieManagerGUI.jMenuItem20"));       
       jMenuItem21.setText(bundle.getString("MovieManagerGUI.jMenuItem21")); 
       jMenuItem22.setText(bundle.getString("MovieManagerGUI.jMenuItem22"));
       jMenuItem24.setText(bundle.getString("MovieManagerGUI.jMenuItem24"));
       // update languages for jRadioMenuItems
       for(Locale locale: localeToMenuItemMap.keySet()){
           JRadioButtonMenuItem radioButtonMenuItem = localeToMenuItemMap.get(locale);
           radioButtonMenuItem.setText(bundle.getString("MovieManagerGUI.jRadioButtonMenuItem." + locale.toString())); // NOI18N
       }

       jTextField1.setText(bundle.getString("MovieManagerGUI.jTextField1"));

       popupAdd.setText(bundle.getString("MovieManagerGUI.jMenuItem2"));
       popupEdit.setText(bundle.getString("MovieManagerGUI.jMenuItem5"));
       popupDelete.setText(bundle.getString("MovieManagerGUI.jMenuItem9"));
       popupAbout.setText(bundle.getString("MovieManagerGUI.jMenuItem6"));
   }

   /**
   * De- & Aktiviert Buttons und Menüpunkte, je nach zustand der GUI
   */
   public void updateEnabledGUIElements(){
       if(CollectionManager.INSTANCE.isACollectionCreated()){
           //Ist die Collection erstellt, und die Buttons noch nicht aktiviert?
           if(!jMenuItem2.isEnabled()){
               //Film hinzufügen
                jMenuItem2.setEnabled(true);
                jButton1.setEnabled(true);
                popupAdd.setEnabled(true);
                //Serie hinzufügen
                jMenuItem18.setEnabled(true);
                jButton5.setEnabled(true);
                //Speichern
                jMenuItem7.setEnabled(true);
                jButton2.setEnabled(true);
                //Speichern unter
                jMenuItem8.setEnabled(true);
                //Importieren aktiviern
                jMenu4.setEnabled(true);
                jMenuItem10.setEnabled(true);
                jMenuItem20.setEnabled(true);
           }
           //Ist mind. 1 Serie oder Film angelegt?
           if((!jTextField1.isEnabled()) && 
                   !CollectionManager.INSTANCE.areCollectionsEmpty()){
//                   (!Controller.getInstance().getAllMovies().isEmpty() || 
//                   !Controller.getInstance().getAllSeries().isEmpty())){
                 //Suche aktivieren
                jTextField1.setEnabled(true);
                //Erweiterte Suche aktivieren
                jMenuItem17.setEnabled(true);
                //Sortierung aktivieren
                jComboBox1.setEnabled(true);
                //Statistik aktivieren
                jMenuItem11.setEnabled(true);
                //Filme umbenennen aktivieren
                jMenuItem12.setEnabled(true);
                //Ordne nach Namen
                //jComboBox1.setSelectedIndex(0);
                //jComboBox1ItemStateChanged(null);
           }
           //Ist die letzte Serie und Film gelöscht wurden?
           else if((jTextField1.isEnabled()) && 
                   CollectionManager.INSTANCE.areCollectionsEmpty()){
//                   (Controller.getInstance().getAllMovies().isEmpty() && 
//                   Controller.getInstance().getAllSeries().isEmpty())){
                //Suche deaktivieren
                jTextField1.setEnabled(false);
                //Erweiterte Suche aktivieren
                jMenuItem17.setEnabled(false);
                //Sortierung deaktivieren
                jComboBox1.setEnabled(false);
                //Statistik deaktivieren
                jMenuItem11.setEnabled(false);
                //Filme umbenennen deaktivieren
                jMenuItem12.setEnabled(false);
           }
           Component selComp = jTabbedPane1.getSelectedComponent();
           //random play
           if((selComp instanceof  AbstractTabGUI) && (!((AbstractTabGUI)selComp).getCollection().isEmpty())){
                jButton9.setEnabled(true);            
           }else{
                jButton9.setEnabled(false);            
           }
           
           if(selComp instanceof MovieTabGUI){
               if(!((MovieTabGUI)selComp).isSearchTab()){
                   jMenuItem12.setEnabled(true);
                   if(((MovieTabGUI)selComp).getSelectedValue() != null){
                        //Film selektiert
                        enableMediaEditingAndRemoving(); 
                    }
                    else{
                        //Film deselektiert
                        disableMediaEditingAndRemoving();
                    }
               }else{
                   //Keine Filme editierbar, wenn search tab ausgewählt
                   disableMediaEditingAndRemoving();
                   jMenuItem12.setEnabled(false);
               }
//               jComboBox1.setEnabled(true); 
           }
           else if(selComp instanceof SerieTabGUI){
               jMenuItem12.setEnabled(false);
               if(!((SerieTabGUI)selComp).isSearchTab()){
                   
                   TreePath selectionPath = ((SerieTabGUI)selComp).getSelectionPath();
                   if(selectionPath != null){
                       //Serie oder Episode selektiert
                       if((selectionPath.getPathCount() == 2) || (selectionPath.getPathCount() == 4)){ 
                           enableMediaEditingAndRemoving();  
                       }
                       else{
                           //Season selektiert
                           disableMediaEditingAndRemoving();
                       }
                   }else{
                       //Nichts selektiert
                       disableMediaEditingAndRemoving();
                   }
               }else{
                   //Keine Serie editierbar, wenn search tab ausgewählt
                   disableMediaEditingAndRemoving();
               }
//               jComboBox1.setEnabled(false);
           }                          
       }       
   } 
   
   /**
    * Aktiviert die Buttons und Menüpunkte zum editieren und entfernen von Media Dateien
    */
   private void enableMediaEditingAndRemoving(){
       //bearbeiten deaktivieren
       jMenuItem5.setEnabled(true);
       jButton7.setEnabled(true);
       popupEdit.setEnabled(true);
       //entfernen aktivieren
       jMenuItem9.setEnabled(true);
       jButton8.setEnabled(true);
       popupDelete.setEnabled(true); 
   }
   
   /**
    * Deaktiviert die Buttons und Menüpunkte zum editieren und entfernen von Media Dateien
    */
   private void disableMediaEditingAndRemoving(){
       //bearbeiten deaktivieren
       jMenuItem5.setEnabled(false);
       jButton7.setEnabled(false);
       popupEdit.setEnabled(false);
       //entfernen deaktivieren
       jMenuItem9.setEnabled(false);
       jButton8.setEnabled(false);
       popupDelete.setEnabled(false);
   }
   
   /**
    * Updates all TabNumbers from each collection
    */
   private void updateTabNumbersOnCollections(){
       int tabCount = jTabbedPane1.getTabCount();
       MediaCollection collection;
       //ignore the tab (+) 
       for(int i = 0; i < tabCount; i ++){
           Component tab = jTabbedPane1.getComponentAt(i);
           if(tab != null){
               collection = MediaCollection.getCollectionMap().get(tab.getName());
               if(collection != null){ //in case of "Search Results" Tab
                   collection.setTabNumber(i);
               }
           }
       }
   }
    
   /**
    * Adds a new tab into the jTabbedPane <br/>
    * if there is a new collection created
    * 
    * @param collection The created collection
    */
   @Override
   public void addElement(MediaCollection collection){
       this.addElement(collection, false);
   }  
   
    /**
    * Adds a new tab into the jTabbedPane <br/>
    * if there is a new collection created
    * 
    * @param collection The created collection
    * @param searchTab if true, the tab is not editable
    */  
   private void addElement(MediaCollection collection, boolean searchTab){
       AbstractTabGUI newTab;
       if(collection instanceof MovieCollection){
           newTab = new MovieTabGUI((MovieCollection)collection, searchTab);
       }
       else{
           newTab = new SerieTabGUI((SerieCollection)collection, searchTab);
       }
       LocaleManager.getInstance().addObserver(newTab);     

       if(collection.getTabNumber() > jTabbedPane1.getTabCount()){
           jTabbedPane1.addTab(collection.getName(), newTab);
       }else{
           jTabbedPane1.insertTab(collection.getName(), null, newTab, null, collection.getTabNumber());
       }
       if(!collection.isEmpty()){
           newTab.updateTabValues();
       }
//       updateTabNumbersOnCollections();
       //Select added Tab
       jTabbedPane1.setSelectedIndex(collection.getTabNumber());
   }
   
   /**
    * Updates a tab name with the new collection name<br/>
    * 
    * @param oldCollection The old collection
    * @param newCollection The new collection
    */
   @Override
   public void update(MediaCollection oldCollection, MediaCollection newCollection){
       jTabbedPane1.removeTabAt(oldCollection.getTabNumber());
       addElement(newCollection);      
   }
   
   /**
    * Removes a tab from the jTabbedPane <br/>
    * if there was a collection deleted
    * 
    * @param collection The removed collection
    */
   @Override
   public void removeElement(MediaCollection collection){
       jTabbedPane1.removeTabAt(collection.getTabNumber());
   } 
   
   public JPanel getRightSplitPane(){
       return jPanel6;
   }
   
   /**
    * returns the collection names of each tab in a list
    * @return A list with the names of each tab
    */
   public List<String> getTabNames(){
       List<String> tabNames = new LinkedList<String>();
       int tabCount = jTabbedPane1.getTabCount();
       //ignore the tab (+) 
       for(int i = 0; i < tabCount; i ++){
           Component tab = jTabbedPane1.getComponentAt(i);
           if(tab != null){
               String tabName = tab.getName();
               tabNames.add(tabName);
           }
       }
       return tabNames;
   }
   
   
//   private Point point = new Point();
//   private void addJMenuBarItemsAndFunction(){
//       jMenuBar1.add(jMenu7);
//       jMenuBar1.add(jMenu8);
//       jMenuBar1.add(jMenu9);
//       jMenuBar1.add(jMenu10);
//       
//       jMenu7.addMouseListener(new MouseAdapter() {  
//            @Override
//           public void mousePressed(MouseEvent e) {  
//                //Linke Maustaste und nicht maximiert
//               if((!e.isMetaDown()) && (instance.getExtendedState() != JFrame.MAXIMIZED_BOTH)){  
//                   point.x = e.getX();  
//                   point.y = e.getY();  
//               }  
//           }  
//       });  
//       jMenu7.addMouseMotionListener(new MouseMotionAdapter() {  
//            @Override
//           public void mouseDragged(MouseEvent e) {  
//                //Linke Maustaste und nicht maximiert
//               if((!e.isMetaDown()) && (instance.getExtendedState() != JFrame.MAXIMIZED_BOTH)){  
//                   Point p = getLocation();  
//                   setLocation(p.x + e.getX() - point.x,  
//                           p.y + e.getY() - point.y);  
//               }  
//           }  
//       });       
//   }
   
   /**
    * Ändert die Farbpalette für das Windows Look & Feel
    */
   private void customizeWindowsLookAndFeel(){         
       //this.setUndecorated(true);  
       jMenuBar1 = new JMenuBar() {
            @Override
            protected void paintComponent(Graphics g) {
                //super.paintComponent(g);
                    g.drawImage(MyIconFactory.menuBar_background.getImage(), //    the image to be rendered
                        0,                                                  //    dx1 - the x coordinate of the first corner of the destination rectangle.
                        0,                                                  //    dy1 - the y coordinate of the first corner of the destination rectangle.
                        this.getWidth(),                                    //    dx2 - the x coordinate of the second corner of the destination rectangle.
                        this.getHeight(),                                   //    dy2 - the y coordinate of the second corner of the destination rectangle.
                        0,                                                  //    sx1 - the x coordinate of the first corner of the source rectangle.
                        0,                                                  //    sy1 - the y coordinate of the first corner of the source rectangle.
                        MyIconFactory.menuBar_background.getIconWidth(),    //    sx2 - the x coordinate of the second corner of the source rectangle.
                        MyIconFactory.menuBar_background.getIconHeight(),   //    sy2 - the y coordinate of the second corner of the source rectangle.
                        null                                                //    Observer, der benachrichtigt werden soll
                    );
                }
        };
              
//       Enumeration xx = UIManager.getLookAndFeelDefaults().keys();
//       while(xx.hasMoreElements()){
//           Object key = xx.nextElement();
//           Object value = UIManager.getLookAndFeelDefaults().get(key);
//           if(value != null && value.toString().endsWith("UI")){
//               System.out.println(value.toString());
//           }
//       }
       UIManager.getLookAndFeelDefaults().putDefaults(jMMLookAndFeel.getDefaultUISettings());       
   }
   
   /** 
    * Customize Look and Feel
    * Wird aufgerufen, <b>nachdem</b> initialisieren der Componenten
    */
   private void postCustomizeWindowsLookAndFeel(){
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                 @Override
                 public void run() {
                     jButton1.setUI((BasicButtonUI)MyBasicMenuButtonUI.createUI(jButton1));
                     jButton2.setUI((BasicButtonUI)MyBasicMenuButtonUI.createUI(jButton2));
                     jButton3.setUI((BasicButtonUI)MyBasicMenuButtonUI.createUI(jButton3));
                     jButton4.setUI((BasicButtonUI)MyBasicMenuButtonUI.createUI(jButton4));
                     jButton5.setUI((BasicButtonUI)MyBasicMenuButtonUI.createUI(jButton5));
                     jButton6.setUI((BasicButtonUI)MyBasicMenuButtonUI.createUI(jButton6));
                     jButton7.setUI((BasicButtonUI)MyBasicMenuButtonUI.createUI(jButton7));
                     jButton8.setUI((BasicButtonUI)MyBasicMenuButtonUI.createUI(jButton8));
                     jButton9.setUI((BasicButtonUI)MyBasicMenuButtonUI.createUI(jButton9));

                    //jTextField1.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 2));
                     
                    jToolBar1.setBorder(null);
                    //Umrandung der SplitPane
                    jSplitPane1.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
                    //Zeichne den Divider nicht!
                    jSplitPane1.setUI((SplitPaneUI)MyBasicSplitPaneUI.createUI(jSplitPane1));        
                    
                    //addJMenuBarItemsAndFunction();

                 //   SwingUtilities.updateComponentTreeUI(this);                
                 }
             });
        } catch (InterruptedException | InvocationTargetException ex) {
            LOG.error("Cannot post update LookAndFeel.", ex);
        }
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
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu10;
    private javax.swing.JMenu jMenu11;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenu jMenu8;
    private javax.swing.JMenu jMenu9;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem14;
    private javax.swing.JMenuItem jMenuItem16;
    private javax.swing.JMenuItem jMenuItem17;
    private javax.swing.JMenuItem jMenuItem18;
    private javax.swing.JMenuItem jMenuItem19;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem20;
    private javax.swing.JMenuItem jMenuItem21;
    private javax.swing.JMenuItem jMenuItem22;
    private javax.swing.JMenuItem jMenuItem24;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private javax.swing.JPopupMenu.Separator jSeparator6;
    private javax.swing.JPopupMenu.Separator jSeparator7;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables
}
