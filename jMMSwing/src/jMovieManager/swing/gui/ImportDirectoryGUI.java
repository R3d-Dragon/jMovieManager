/**
 * Copyright (c) 2010-2015 Bryan Beck.
 * All rights reserved.
 * 
 * This project is licensed under LGPL v2.1.
 * See jMovieManager-license.txt for details.
 * 
 */
package jMovieManager.swing.gui;

import java.awt.Cursor;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JTree;
import javax.swing.border.TitledBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import jmm.api.thetvdb.TheTVDB;
import jmm.api.tmdb.TMDBMovieWrapper;
import jmm.data.collection.MediaCollection;
import jmm.data.collection.MovieCollection;
import jmm.data.collection.SerieCollection;
import jmm.interfaces.FileTypeInterface;
import jMovieManager.swing.gui.components.MyJFileChooser;
import jMovieManager.swing.gui.other.MyFile;
import java.util.ArrayList;
import java.util.Arrays;
import jmm.utils.FileNameFormatter;
import jmm.utils.ImportProcess;
import jmm.utils.OperatingSystem;
import jmm.utils.Utils;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;

/**
 * Wizard to import directories
 * 
 * @author Bryan Beck
 * @since 12.12.2011
 */
public class ImportDirectoryGUI extends javax.swing.JDialog implements ColorInterface {
    /** Logger. */
    private static final Logger LOG = LoggerFactory.logger(ImportDirectoryGUI.class);
    
    private ResourceBundle bundle;
    private MediaCollection collection;
    
    //Model and map for movies
    private DefaultTreeModel moviesTreeModel;
    private DefaultMutableTreeNode moviesRootNode;
    private Map<String, DefaultMutableTreeNode> moviesMap;
    //Model and map for series
    private DefaultTreeModel seriesTreeModel;
    private DefaultMutableTreeNode seriesRootNode;
    private Map<String, DefaultMutableTreeNode> seriesMap;
    
    private Map<MyFile, FileTypeInterface.MediaType> dirsToImport; //Which dirs are added to the trees and to which tree
    private DefaultComboBoxModel tabNamesModel;
    
    //Example string for custom replacement
    private final String CUSTOM_REPLACEMENT_EXAMPLE = "Word 1;Word 2;Word 3;Word 4";
    
//    private Frame parentFrame;
    
    /**
     * Creates new form ImportDirectoryGUI
     *
     * @param tabNames all tab names in a list
     * @see javax.swing.JDialog#JDialog(java.awt.Dialog, boolean)
     */
    public ImportDirectoryGUI(java.awt.Frame parent, boolean modal, List<String> tabNames) {
        //TODO: Drag & Drop von einzelnen Tree Elementen
        //TODO: Fading POpup zur HIlfestellung, das Drag & Drop verfügbar ist

        super(parent, modal);
//        this.parentFrame = parent;
        bundle = ResourceBundle.getBundle("jMovieManager.swing.resources.MovieManager");
        moviesRootNode = new DefaultMutableTreeNode("Filme");
        seriesRootNode = new DefaultMutableTreeNode("Serien");
        moviesMap = new HashMap<String, DefaultMutableTreeNode>();
        seriesMap = new HashMap<String, DefaultMutableTreeNode>();
        dirsToImport = new HashMap<MyFile, FileTypeInterface.MediaType>();
        moviesTreeModel = new DefaultTreeModel(moviesRootNode);
        seriesTreeModel = new DefaultTreeModel(seriesRootNode);
        tabNamesModel = new DefaultComboBoxModel();
//                jTree1.setPreferredSize(null);

        initComponents();
        initDragAndDropOnWindows();
        for (String tabName : tabNames) {
            tabNamesModel.addElement(tabName);
        }
        this.setLocationRelativeTo(parent);
        testInternetAndAPI();
        lookForLibrary();
        //Füge eigene Methode bei Fensterschließen hinzu
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                jButton4ActionPerformed(null);
            }
        });
    }

    /**
     * Creates new form ImportDirectoryGUI with prefilled values <br/>
     *
     * @param tabNames all tab names in a list
     * @param files The files to import
     * @param collection the collection to add the files
     * @see javax.swing.JDialog#JDialog(java.awt.Dialog, boolean)
     *
     */
    public ImportDirectoryGUI(java.awt.Frame parent, boolean modal, List<String> tabNames, final List<File> files, MediaCollection collection) {
        this(parent, modal, tabNames);

        this.collection = collection;
        tabNamesModel.setSelectedItem(collection.getName());
        jComboBox1ItemStateChanged(null);

        addDirsToModel(files);
    }
    
    public static void main(String args[]){
        
        System.out.println("Start test");
        boolean x = false;
        if((x = true) | eval()) {
            System.out.println("If");
        } else {
            System.out.println("Else");
        }
    }
    
    public static boolean eval() {
        System.out.println("eval true");
        return false;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup3 = new javax.swing.ButtonGroup();
        jPanel20 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        movieTree = new javax.swing.JTree();
        jPanel21 = new javax.swing.JPanel();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jCheckBox10 = new javax.swing.JCheckBox();
        jPanel1 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jPanel14 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jRadioButton7 = new javax.swing.JRadioButton();
        jRadioButton6 = new javax.swing.JRadioButton();
        jPanel9 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jCheckBox4 = new javax.swing.JCheckBox();
        jCheckBox3 = new javax.swing.JCheckBox();
        jPanel13 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jCheckBox5 = new javax.swing.JCheckBox();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox6 = new javax.swing.JCheckBox();
        jPanel4 = new javax.swing.JPanel();
        jPanel22 = new javax.swing.JPanel();
        jCheckBox13 = new javax.swing.JCheckBox();
        jPanel11 = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jCheckBox2 = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        jCheckBox8 = new javax.swing.JCheckBox();
        jLabel2 = new javax.swing.JLabel();
        jCheckBox9 = new javax.swing.JCheckBox();
        jLabel4 = new javax.swing.JLabel();
        jCheckBox12 = new javax.swing.JCheckBox();
        jLabel8 = new javax.swing.JLabel();
        jCheckBox11 = new javax.swing.JCheckBox();
        jLabel6 = new javax.swing.JLabel();
        jCheckBox7 = new javax.swing.JCheckBox();
        jLabel9 = new javax.swing.JLabel();
        jCheckBox14 = new javax.swing.JCheckBox();
        jLabel10 = new javax.swing.JLabel();
        jPanel23 = new javax.swing.JPanel();
        jPanel24 = new javax.swing.JPanel();
        jTextField3 = new javax.swing.JTextField();
        jPanel25 = new javax.swing.JPanel();
        jPanel27 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        seriesTree = new javax.swing.JTree();
        jPanel10 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel7 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        jPanel20.setMinimumSize(new java.awt.Dimension(200, 250));
        jPanel20.setPreferredSize(new java.awt.Dimension(350, 250));
        jPanel20.setLayout(new javax.swing.BoxLayout(jPanel20, javax.swing.BoxLayout.LINE_AXIS));

        jScrollPane2.setBackground(ColorInterface.list_tree_Background);
        jScrollPane2.setBorder(javax.swing.BorderFactory.createEtchedBorder(null, ColorInterface.list_tree_BorderShadow));

        movieTree.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 1, 1));
        movieTree.setModel(moviesTreeModel);
        movieTree.setRootVisible(false);
        movieTree.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                movieTreeValueChanged(evt);
            }
        });
        movieTree.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                movieTreeKeyTyped(evt);
            }
        });
        jScrollPane2.setViewportView(movieTree);

        jPanel20.add(jScrollPane2);

        jPanel21.setMaximumSize(new java.awt.Dimension(26, 32767));
        jPanel21.setMinimumSize(new java.awt.Dimension(26, 100));
        jPanel21.setPreferredSize(new java.awt.Dimension(26, 518));

        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jMovieManager/swing/images/importDirectoryAdd.png"))); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("jMovieManager/swing/resources/MovieManager"); // NOI18N
        jButton7.setToolTipText(bundle.getString("ImportDirectoryGUI.jButton1.tooltip")); // NOI18N
        jButton7.setBorderPainted(false);
        jButton7.setContentAreaFilled(false);
        jButton7.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton7.setMaximumSize(new java.awt.Dimension(16, 16));
        jButton7.setMinimumSize(new java.awt.Dimension(16, 16));
        jButton7.setPreferredSize(new java.awt.Dimension(16, 16));
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jMovieManager/swing/images/importDirectoryDelete.png"))); // NOI18N
        jButton8.setToolTipText(bundle.getString("ImportDirectoryGUI.jButton3.tooltip")); // NOI18N
        jButton8.setBorderPainted(false);
        jButton8.setContentAreaFilled(false);
        jButton8.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton8.setEnabled(false);
        jButton8.setMaximumSize(new java.awt.Dimension(16, 16));
        jButton8.setMinimumSize(new java.awt.Dimension(16, 16));
        jButton8.setPreferredSize(new java.awt.Dimension(16, 16));
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(5, Short.MAX_VALUE))
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(189, Short.MAX_VALUE))
        );

        jPanel20.add(jPanel21);

        jLabel5.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        jLabel5.setLabelFor(jCheckBox10);
        jLabel5.setText("(DVDRIP, BLURAY, BD, RETAIL, HDTV, ...)");
        jLabel5.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 37, 5, 0));
        jLabel5.setMaximumSize(new java.awt.Dimension(300, 19));
        jLabel5.setMinimumSize(new java.awt.Dimension(300, 19));
        jLabel5.setPreferredSize(new java.awt.Dimension(300, 19));

        jCheckBox10.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox10.setSelected(true);
        jCheckBox10.setText(bundle.getString("ImportDirectoryGUI.jCheckBox10")); // NOI18N
        jCheckBox10.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 20, 2, 0));
        jCheckBox10.setMaximumSize(new java.awt.Dimension(300, 23));
        jCheckBox10.setMinimumSize(new java.awt.Dimension(300, 23));
        jCheckBox10.setOpaque(false);
        jCheckBox10.setPreferredSize(new java.awt.Dimension(300, 23));

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle(bundle.getString("ImportDirectoryGUI.headline")); // NOI18N
        setMinimumSize(new java.awt.Dimension(815, 500));
        setPreferredSize(new java.awt.Dimension(815, 500));
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.LINE_AXIS));

        jPanel1.setMinimumSize(new java.awt.Dimension(800, 500));
        jPanel1.setName(""); // NOI18N
        jPanel1.setPreferredSize(new java.awt.Dimension(800, 500));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel17.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 5, 0));
        jPanel17.setLayout(new javax.swing.BoxLayout(jPanel17, javax.swing.BoxLayout.LINE_AXIS));

        jTabbedPane1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 2, 0));

        jPanel2.setMaximumSize(new java.awt.Dimension(450, 98301));
        jPanel2.setMinimumSize(new java.awt.Dimension(450, 320));
        jPanel2.setOpaque(false);
        jPanel2.setPreferredSize(new java.awt.Dimension(450, 562));
        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.PAGE_AXIS));

        jPanel12.setMinimumSize(new java.awt.Dimension(430, 70));
        jPanel12.setOpaque(false);
        jPanel12.setPreferredSize(new java.awt.Dimension(430, 70));

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel7.setText(bundle.getString("ImportDirectoryGUI.jLabel7")); // NOI18N

        jComboBox1.setModel(tabNamesModel);
        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(49, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel12);

        jPanel14.setMinimumSize(new java.awt.Dimension(430, 80));
        jPanel14.setOpaque(false);
        jPanel14.setPreferredSize(new java.awt.Dimension(430, 100));

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel3.setText(bundle.getString("ImportDirectoryGUI.jLabel3")); // NOI18N

        buttonGroup3.add(jRadioButton7);
        jRadioButton7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jRadioButton7.setSelected(true);
        jRadioButton7.setText(bundle.getString("ImportDirectoryGUI.jRadioButton7")); // NOI18N
        jRadioButton7.setOpaque(false);

        buttonGroup3.add(jRadioButton6);
        jRadioButton6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jRadioButton6.setText(bundle.getString("ImportDirectoryGUI.jRadioButton6")); // NOI18N
        jRadioButton6.setOpaque(false);

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 389, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jRadioButton6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 399, Short.MAX_VALUE)
                            .addComponent(jRadioButton7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(31, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jRadioButton7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jRadioButton6)
                .addContainerGap(54, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel14);

        jPanel9.setMinimumSize(new java.awt.Dimension(430, 150));
        jPanel9.setOpaque(false);
        jPanel9.setPreferredSize(new java.awt.Dimension(430, 150));
        jPanel9.setLayout(new java.awt.GridLayout(1, 2));

        jPanel8.setMaximumSize(new java.awt.Dimension(225, 165));
        jPanel8.setMinimumSize(new java.awt.Dimension(225, 165));
        jPanel8.setOpaque(false);

        jPanel15.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(null, ColorInterface.list_tree_BorderShadow), bundle.getString("ImportDirectoryGUI.jPanel15"))); // NOI18N
        jPanel15.setOpaque(false);

        jCheckBox4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox4.setSelected(true);
        jCheckBox4.setText(bundle.getString("ImportDirectoryGUI.jCheckBox4")); // NOI18N
        jCheckBox4.setToolTipText(bundle.getString("ImportDirectoryGUI.jCheckBox4.tooltip")); // NOI18N
        jCheckBox4.setOpaque(false);

        jCheckBox3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox3.setSelected(true);
        jCheckBox3.setText(bundle.getString("ImportDirectoryGUI.jCheckBox3")); // NOI18N
        jCheckBox3.setToolTipText(bundle.getString("ImportDirectoryGUI.jCheckBox3.tooltip")); // NOI18N
        jCheckBox3.setOpaque(false);
        jCheckBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckBox3, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                    .addComponent(jCheckBox4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jCheckBox3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCheckBox4)
                .addContainerGap(33, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(64, Short.MAX_VALUE)
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel9.add(jPanel8);

        jPanel13.setMaximumSize(new java.awt.Dimension(225, 165));
        jPanel13.setMinimumSize(new java.awt.Dimension(225, 165));
        jPanel13.setOpaque(false);

        jPanel16.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(null, ColorInterface.list_tree_BorderShadow), bundle.getString("ImportDirectoryGUI.jPanel16"))); // NOI18N
        jPanel16.setOpaque(false);

        jCheckBox5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox5.setText(bundle.getString("ImportDirectoryGUI.jCheckBox5")); // NOI18N
        jCheckBox5.setToolTipText(bundle.getString("ImportDirectoryGUI.jCheckBox5.tooltip")); // NOI18N
        jCheckBox5.setOpaque(false);

        jCheckBox1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox1.setSelected(true);
        jCheckBox1.setText(bundle.getString("ImportDirectoryGUI.jCheckBox1")); // NOI18N
        jCheckBox1.setToolTipText(bundle.getString("ImportDirectoryGUI.jCheckBox1.tooltip")); // NOI18N
        jCheckBox1.setOpaque(false);
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jCheckBox6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox6.setText(bundle.getString("ImportDirectoryGUI.jCheckBox6")); // NOI18N
        jCheckBox6.setToolTipText(bundle.getString("ImportDirectoryGUI.jCheckBox6.tooltip")); // NOI18N
        jCheckBox6.setActionCommand("jCheckBox6");
        jCheckBox6.setOpaque(false);
        jCheckBox6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jCheckBox6, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                    .addComponent(jCheckBox1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCheckBox5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jCheckBox1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCheckBox5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCheckBox6)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addContainerGap(64, Short.MAX_VALUE)
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel9.add(jPanel13);

        jPanel2.add(jPanel9);

        jTabbedPane1.addTab(bundle.getString("ImportDirectoryGUI.jPanel2"), jPanel2); // NOI18N

        jPanel4.setOpaque(false);
        jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.PAGE_AXIS));

        jPanel22.setMaximumSize(new java.awt.Dimension(32767, 39));
        jPanel22.setMinimumSize(new java.awt.Dimension(0, 39));
        jPanel22.setOpaque(false);
        jPanel22.setPreferredSize(new java.awt.Dimension(450, 39));

        jCheckBox13.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox13.setSelected(true);
        jCheckBox13.setText(bundle.getString("ImportDirectoryGUI.jCheckBox13")); // NOI18N
        jCheckBox13.setMaximumSize(new java.awt.Dimension(93, 25));
        jCheckBox13.setMinimumSize(new java.awt.Dimension(93, 25));
        jCheckBox13.setOpaque(false);
        jCheckBox13.setPreferredSize(new java.awt.Dimension(93, 25));
        jCheckBox13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox13ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jCheckBox13, javax.swing.GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jCheckBox13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.add(jPanel22);

        jPanel11.setOpaque(false);

        jPanel19.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(null, ColorInterface.list_tree_BorderShadow), bundle.getString("ImportDirectoryGUI.jPanel19"))); // NOI18N
        jPanel19.setOpaque(false);
        jPanel19.setLayout(new javax.swing.BoxLayout(jPanel19, javax.swing.BoxLayout.PAGE_AXIS));

        jCheckBox2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox2.setSelected(true);
        jCheckBox2.setText(bundle.getString("ImportDirectoryGUI.jCheckBox2")); // NOI18N
        jCheckBox2.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 20, 2, 0));
        jCheckBox2.setOpaque(false);
        jPanel19.add(jCheckBox2);

        jLabel1.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        jLabel1.setText("(ä, ö, ü, ß --> ae, oe, ue, ss)");
        jLabel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 37, 5, 0));
        jPanel19.add(jLabel1);

        jCheckBox8.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox8.setSelected(true);
        jCheckBox8.setText(bundle.getString("ImportDirectoryGUI.jCheckBox8")); // NOI18N
        jCheckBox8.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 20, 2, 0));
        jCheckBox8.setMaximumSize(new java.awt.Dimension(300, 23));
        jCheckBox8.setMinimumSize(new java.awt.Dimension(300, 23));
        jCheckBox8.setOpaque(false);
        jCheckBox8.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel19.add(jCheckBox8);

        jLabel2.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        jLabel2.setLabelFor(jCheckBox8);
        jLabel2.setText("(DIVX, XVID, MPEG, AC3, DTS, ...)");
        jLabel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 37, 5, 0));
        jLabel2.setMaximumSize(new java.awt.Dimension(300, 19));
        jLabel2.setMinimumSize(new java.awt.Dimension(300, 19));
        jLabel2.setPreferredSize(new java.awt.Dimension(300, 19));
        jPanel19.add(jLabel2);

        jCheckBox9.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox9.setSelected(true);
        jCheckBox9.setText(bundle.getString("ImportDirectoryGUI.jCheckBox9")); // NOI18N
        jCheckBox9.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 20, 2, 0));
        jCheckBox9.setMaximumSize(new java.awt.Dimension(300, 23));
        jCheckBox9.setMinimumSize(new java.awt.Dimension(300, 23));
        jCheckBox9.setOpaque(false);
        jCheckBox9.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel19.add(jCheckBox9);

        jLabel4.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        jLabel4.setLabelFor(jCheckBox9);
        jLabel4.setText("(1999, {2001}, (2005), [2012], ...)");
        jLabel4.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 37, 5, 0));
        jLabel4.setMaximumSize(new java.awt.Dimension(300, 19));
        jLabel4.setMinimumSize(new java.awt.Dimension(300, 19));
        jLabel4.setPreferredSize(new java.awt.Dimension(300, 19));
        jPanel19.add(jLabel4);

        jCheckBox12.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox12.setSelected(true);
        jCheckBox12.setText(bundle.getString("ImportDirectoryGUI.jCheckBox12")); // NOI18N
        jCheckBox12.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 20, 2, 0));
        jCheckBox12.setMaximumSize(new java.awt.Dimension(300, 23));
        jCheckBox12.setMinimumSize(new java.awt.Dimension(300, 23));
        jCheckBox12.setOpaque(false);
        jCheckBox12.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel19.add(jCheckBox12);

        jLabel8.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        jLabel8.setLabelFor(jCheckBox12);
        jLabel8.setText("(BLURAY, BD, TS, DVDRIP, DL, MD, Retail, ...)");
        jLabel8.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 37, 5, 0));
        jLabel8.setMaximumSize(new java.awt.Dimension(300, 19));
        jLabel8.setMinimumSize(new java.awt.Dimension(300, 19));
        jLabel8.setPreferredSize(new java.awt.Dimension(300, 19));
        jPanel19.add(jLabel8);

        jCheckBox11.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox11.setSelected(true);
        jCheckBox11.setText(bundle.getString("ImportDirectoryGUI.jCheckBox11")); // NOI18N
        jCheckBox11.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 20, 2, 0));
        jCheckBox11.setMaximumSize(new java.awt.Dimension(300, 23));
        jCheckBox11.setMinimumSize(new java.awt.Dimension(300, 23));
        jCheckBox11.setOpaque(false);
        jCheckBox11.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel19.add(jCheckBox11);

        jLabel6.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        jLabel6.setLabelFor(jCheckBox11);
        jLabel6.setText("(-AOE, -CIS, -SOF, -HDC, ...)");
        jLabel6.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 37, 5, 0));
        jLabel6.setMaximumSize(new java.awt.Dimension(300, 19));
        jLabel6.setMinimumSize(new java.awt.Dimension(300, 19));
        jLabel6.setPreferredSize(new java.awt.Dimension(300, 19));
        jPanel19.add(jLabel6);

        jCheckBox7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox7.setSelected(true);
        jCheckBox7.setText(bundle.getString("ImportDirectoryGUI.jCheckBox7")); // NOI18N
        jCheckBox7.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 20, 2, 0));
        jCheckBox7.setMaximumSize(new java.awt.Dimension(300, 23));
        jCheckBox7.setMinimumSize(new java.awt.Dimension(300, 23));
        jCheckBox7.setOpaque(false);
        jCheckBox7.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel19.add(jCheckBox7);

        jLabel9.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        jLabel9.setLabelFor(jCheckBox7);
        jLabel9.setText("(English, German, ...)");
        jLabel9.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 37, 5, 0));
        jLabel9.setMaximumSize(new java.awt.Dimension(300, 19));
        jLabel9.setMinimumSize(new java.awt.Dimension(300, 19));
        jLabel9.setPreferredSize(new java.awt.Dimension(300, 19));
        jPanel19.add(jLabel9);

        jCheckBox14.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox14.setText(bundle.getString("ImportDirectoryGUI.jCheckBox14")); // NOI18N
        jCheckBox14.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 20, 2, 0));
        jCheckBox14.setMaximumSize(new java.awt.Dimension(300, 23));
        jCheckBox14.setMinimumSize(new java.awt.Dimension(300, 23));
        jCheckBox14.setOpaque(false);
        jCheckBox14.setPreferredSize(new java.awt.Dimension(300, 23));
        jCheckBox14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox14ActionPerformed(evt);
            }
        });
        jPanel19.add(jCheckBox14);

        jLabel10.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        jLabel10.setText(bundle.getString("ImportDirectoryGUI.jLabel10")); // NOI18N
        jLabel10.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 37, 5, 0));
        jLabel10.setMaximumSize(new java.awt.Dimension(300, 19));
        jLabel10.setMinimumSize(new java.awt.Dimension(300, 19));
        jLabel10.setPreferredSize(new java.awt.Dimension(300, 19));
        jPanel19.add(jLabel10);

        jPanel23.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jPanel23.setMaximumSize(new java.awt.Dimension(2147483647, 23));
        jPanel23.setMinimumSize(new java.awt.Dimension(8, 23));
        jPanel23.setOpaque(false);
        jPanel23.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel23.setLayout(new javax.swing.BoxLayout(jPanel23, javax.swing.BoxLayout.LINE_AXIS));

        jPanel24.setMaximumSize(new java.awt.Dimension(37, 23));
        jPanel24.setMinimumSize(new java.awt.Dimension(37, 23));
        jPanel24.setOpaque(false);
        jPanel24.setPreferredSize(new java.awt.Dimension(37, 23));

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 37, Short.MAX_VALUE)
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 23, Short.MAX_VALUE)
        );

        jPanel23.add(jPanel24);

        jTextField3.setText(CUSTOM_REPLACEMENT_EXAMPLE);
        jTextField3.setEnabled(false);
        jTextField3.setMaximumSize(new java.awt.Dimension(2147483647, 23));
        jTextField3.setMinimumSize(new java.awt.Dimension(6, 23));
        jTextField3.setPreferredSize(new java.awt.Dimension(300, 23));
        jTextField3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField3MouseClicked(evt);
            }
        });
        jPanel23.add(jTextField3);

        jPanel25.setMaximumSize(new java.awt.Dimension(10, 23));
        jPanel25.setMinimumSize(new java.awt.Dimension(10, 23));
        jPanel25.setOpaque(false);
        jPanel25.setPreferredSize(new java.awt.Dimension(10, 23));

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 23, Short.MAX_VALUE)
        );

        jPanel23.add(jPanel25);

        jPanel19.add(jPanel23);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, 391, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        jPanel4.add(jPanel11);

        jTabbedPane1.addTab(bundle.getString("ImportDirectoryGUI.jPanel4"), jPanel4); // NOI18N

        jPanel17.add(jTabbedPane1);

        jPanel27.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 7, 0, 5));
        jPanel27.setLayout(new javax.swing.BoxLayout(jPanel27, javax.swing.BoxLayout.LINE_AXIS));

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(null, ColorInterface.list_tree_BorderShadow), bundle.getString("ImportDirectoryGUI.jPanel5"))); // NOI18N
        jPanel5.setMinimumSize(new java.awt.Dimension(200, 250));
        jPanel5.setPreferredSize(new java.awt.Dimension(350, 250));
        jPanel5.setLayout(new javax.swing.BoxLayout(jPanel5, javax.swing.BoxLayout.LINE_AXIS));

        jPanel18.setMinimumSize(new java.awt.Dimension(200, 250));
        jPanel18.setPreferredSize(new java.awt.Dimension(350, 250));
        jPanel18.setLayout(new javax.swing.BoxLayout(jPanel18, javax.swing.BoxLayout.LINE_AXIS));

        jScrollPane1.setBackground(ColorInterface.list_tree_Background);
        jScrollPane1.setBorder(javax.swing.BorderFactory.createEtchedBorder(null, ColorInterface.list_tree_BorderShadow));

        seriesTree.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 1, 1));
        seriesTree.setModel(seriesTreeModel);
        seriesTree.setRootVisible(false);
        seriesTree.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                seriesTreeValueChanged(evt);
            }
        });
        seriesTree.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                seriesTreeKeyTyped(evt);
            }
        });
        jScrollPane1.setViewportView(seriesTree);

        jPanel18.add(jScrollPane1);

        jPanel10.setMaximumSize(new java.awt.Dimension(26, 32767));
        jPanel10.setMinimumSize(new java.awt.Dimension(26, 100));
        jPanel10.setPreferredSize(new java.awt.Dimension(26, 518));

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jMovieManager/swing/images/importDirectoryAdd.png"))); // NOI18N
        jButton1.setToolTipText(bundle.getString("ImportDirectoryGUI.jButton1.tooltip")); // NOI18N
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

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jMovieManager/swing/images/importDirectoryDelete.png"))); // NOI18N
        jButton3.setToolTipText(bundle.getString("ImportDirectoryGUI.jButton3.tooltip")); // NOI18N
        jButton3.setBorderPainted(false);
        jButton3.setContentAreaFilled(false);
        jButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton3.setEnabled(false);
        jButton3.setMaximumSize(new java.awt.Dimension(16, 16));
        jButton3.setMinimumSize(new java.awt.Dimension(16, 16));
        jButton3.setPreferredSize(new java.awt.Dimension(16, 16));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(5, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(362, Short.MAX_VALUE))
        );

        jPanel18.add(jPanel10);

        jPanel5.add(jPanel18);

        jPanel27.add(jPanel5);

        jPanel17.add(jPanel27);

        jPanel1.add(jPanel17, java.awt.BorderLayout.CENTER);

        jPanel3.setMinimumSize(new java.awt.Dimension(205, 35));
        jPanel3.setPreferredSize(new java.awt.Dimension(800, 35));
        jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.PAGE_AXIS));

        jPanel6.setMaximumSize(new java.awt.Dimension(32767, 5));
        jPanel6.setPreferredSize(new java.awt.Dimension(560, 5));
        jPanel6.setLayout(new javax.swing.BoxLayout(jPanel6, javax.swing.BoxLayout.LINE_AXIS));

        jSeparator1.setMaximumSize(new java.awt.Dimension(32767, 5));
        jSeparator1.setPreferredSize(new java.awt.Dimension(800, 5));
        jPanel6.add(jSeparator1);

        jPanel3.add(jPanel6);

        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jButton2.setText(bundle.getString("ImportDirectoryGUI.jButton2")); // NOI18N
        jButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton2.setEnabled(false);
        jButton2.setMaximumSize(new java.awt.Dimension(90, 23));
        jButton2.setMinimumSize(new java.awt.Dimension(90, 23));
        jButton2.setPreferredSize(new java.awt.Dimension(90, 23));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel7.add(jButton2);

        jButton4.setText(bundle.getString("ImportDirectoryGUI.jButton4")); // NOI18N
        jButton4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton4.setMaximumSize(new java.awt.Dimension(100, 23));
        jButton4.setMinimumSize(new java.awt.Dimension(100, 23));
        jButton4.setPreferredSize(new java.awt.Dimension(100, 23));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel7.add(jButton4);

        jPanel3.add(jPanel7);

        jPanel1.add(jPanel3, java.awt.BorderLayout.SOUTH);

        getContentPane().add(jPanel1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * jButton2 - Import <br/> Starts the import process
     *
     * @param evt the triggered event
     */
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        //Starte Import
        boolean importThumbnail = jCheckBox5.isSelected();
        boolean searchOnline = jCheckBox3.isSelected();
        boolean search_Fileheader = jCheckBox4.isSelected();
        boolean useDirNames;
        String customReplacement;
        //Benennung
        if(jRadioButton6.isSelected()) {
            useDirNames = false;
        } else {
            useDirNames = true;
        }
        if((jCheckBox14.isEnabled()) && (!jTextField3.getText().equals(CUSTOM_REPLACEMENT_EXAMPLE))){
            customReplacement = jTextField3.getText();
        }else{
            customReplacement = "";
        }
        FileNameFormatter formatter = null;
        if(jCheckBox13.isSelected()){
            formatter = FileNameFormatter.createFileNameFormatter(jCheckBox8.isSelected(), jCheckBox11.isSelected(), jCheckBox12.isSelected(), //12 instead of 10 
                    jCheckBox12.isSelected(), jCheckBox7.isSelected(), jCheckBox9.isSelected(), jCheckBox2.isSelected(), customReplacement);
        }
        
        //Create import tasks
        ImportProcess importFiles;
        int numberOfElementsToImport;
        if (collection instanceof MovieCollection) {
            importFiles = new ImportProcess(collection,
                    new LinkedList<DefaultMutableTreeNode>(moviesMap.values()), //Verzeichnispfade zum importieren
                    useDirNames, //Ordnernamen als Titel verwenden
                    searchOnline,
                    importThumbnail,
                    search_Fileheader,
                    formatter);
            numberOfElementsToImport = moviesMap.size();
        } else if (collection instanceof SerieCollection) {
            importFiles = new ImportProcess(collection,
                    new LinkedList<DefaultMutableTreeNode>(seriesMap.values()), //Verzeichnispfade zum importieren
                    useDirNames, //Ordnernamen als Titel verwenden
                    searchOnline,
                    importThumbnail,
                    search_Fileheader,
                    formatter);
            numberOfElementsToImport = seriesMap.size();
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, bundle.getString("ImportDirectoryGUI.error.noCollection"));
            return;
        }

        DisplayImportProgressGUI displayImportProgress = new DisplayImportProgressGUI(MovieManagerGUI.getInstance(), true, numberOfElementsToImport);
        displayImportProgress.setRunnableList(importFiles.getImportTasks());
        //start import
        displayImportProgress.showAndStart();
        this.dispose();                             
    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * jButton4 - Cancel <br/> Disposes the GUI
     *
     * @param evt the triggered Event
     */
    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton4ActionPerformed

    /**
     * jButton1 - Open Filechooser <br/> Opens a jFileChooser to select one or
     * multiple files
     *
     * @param evt the triggered event
     */
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        openFileChooser();
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * jButton3 - remove selected value <br/> Removes the selected value from
     * the list or the tree
     *
     * @param evt the triggered event
     */
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        if (collection instanceof MovieCollection) {
            TreePath[] selectionPath = movieTree.getSelectionPaths();
            for(TreePath path: selectionPath){
                moviesMap.remove(path.getLastPathComponent().toString());
                MutableTreeNode nodeToRemove = (MutableTreeNode) path.getLastPathComponent();
                if(!nodeToRemove.getParent().equals(moviesRootNode)){
                    MutableTreeNode parent = (MutableTreeNode)nodeToRemove.getParent();
                    if(parent.getChildCount() == 1){
                        //remove parent also
                        moviesTreeModel.removeNodeFromParent(parent);
                    }
                }
                moviesTreeModel.removeNodeFromParent(nodeToRemove);
            }
        } else if (collection instanceof SerieCollection) {
            TreePath[] selectionPath = seriesTree.getSelectionPaths();
            for(TreePath path: selectionPath){
                if (path.getPathCount() == 2) {
                    seriesMap.remove(path.getLastPathComponent().toString());
                }
                MutableTreeNode nodeToRemove = (MutableTreeNode) path.getLastPathComponent();
                seriesTreeModel.removeNodeFromParent(nodeToRemove);
            }
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    /**
     * jComboBox1 - Selected value changed <br/> Adds/Removes the List/Tree
     * Panel from the GUI, if another collection is selected
     *
     * @param evt the triggered event
     */
    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        collection = MediaCollection.getCollectionMap().get(jComboBox1.getSelectedItem().toString());
        if (collection == null) {
            javax.swing.JOptionPane.showMessageDialog(this, bundle.getString("ImportDirectoryGUI.error.noCollection"));
            return;
        }
        if (collection instanceof MovieCollection) {
            //add jList panel
            jPanel5.removeAll();
            jPanel5.add(jPanel20);
            jButton2.setEnabled(true);
            
            jRadioButton6.setSelected(false);
            jRadioButton7.setSelected(true);
            updateNumberOfPreviewElements(moviesMap);
            
            //enable recursive and seperate checkbox
            jCheckBox1.setEnabled(true);
            jCheckBox6.setEnabled(true);
        } else if (collection instanceof SerieCollection) {
            //add jTree panel
            jPanel5.removeAll();
            jPanel5.add(jPanel18);
            jButton2.setEnabled(true);
            jRadioButton7.setSelected(false);
            jRadioButton6.setSelected(true);
            updateNumberOfPreviewElements(seriesMap);
            
            //Disable recursive and seperate checkbox
            jCheckBox1.setEnabled(false);
            jCheckBox1.setSelected(true);
            jCheckBox6.setEnabled(false);
            jCheckBox6.setSelected(false);
        }
        
        updateRemoveButton();
        this.repaint();
        jPanel5.repaint();
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    /*
     * jTree1 - Updates the remove Button
     * @evt the triggered event
     */
    private void seriesTreeValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_seriesTreeValueChanged
        updateRemoveButton();
    }//GEN-LAST:event_seriesTreeValueChanged

    /*
     * updates the imdb buttons and en/disables the thumbnail checkbox
     * @param evt the triggered event
     */
    private void jCheckBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox3ActionPerformed
        updateImportThumbnailButton();
    }//GEN-LAST:event_jCheckBox3ActionPerformed

    /**
     * updated the list or tree with the latest settings
     *
     * @param evt
     */
    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        actualizeTrees();
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    /**
     * updated the list or tree with the latest settings
     *
     * @param evt the triggered event
     */
    private void jCheckBox6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox6ActionPerformed
        actualizeTrees();
    }//GEN-LAST:event_jCheckBox6ActionPerformed

    /*
     * jTree2 - Updates the remove Button
     * @evt the triggered event
     */
    private void movieTreeValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_movieTreeValueChanged
        updateRemoveButton();
    }//GEN-LAST:event_movieTreeValueChanged

    /**
     * @see #jButton1ActionPerformed(java.awt.event.ActionEvent) 
     *
     */
    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        jButton1ActionPerformed(evt);
    }//GEN-LAST:event_jButton7ActionPerformed

    /**
     * @see #jButton3ActionPerformed(java.awt.event.ActionEvent) 
     *
     */
    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        jButton3ActionPerformed(evt);
    }//GEN-LAST:event_jButton8ActionPerformed

    /**
     * Determines if the delete button was pressed and removes the selected value
     * @see #jButton3ActionPerformed(java.awt.event.ActionEvent) 
     * @param evt the triggered event
     */
    private void seriesTreeKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_seriesTreeKeyTyped
        if(evt.getKeyChar() == KeyEvent.VK_DELETE){
            jButton3ActionPerformed(null);
        }
    }//GEN-LAST:event_seriesTreeKeyTyped

    /**
     * @see #seriesTreeKeyTyped(java.awt.event.KeyEvent) 
     */
    private void movieTreeKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_movieTreeKeyTyped
        seriesTreeKeyTyped(evt);
    }//GEN-LAST:event_movieTreeKeyTyped

    private void jCheckBox13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox13ActionPerformed
        boolean enabled = jCheckBox13.isSelected();
        jPanel19.setEnabled(enabled);
            
        jCheckBox2.setEnabled(enabled);
        jCheckBox7.setEnabled(enabled);
        jCheckBox8.setEnabled(enabled);
        jCheckBox9.setEnabled(enabled);
        jCheckBox10.setEnabled(enabled);
        jCheckBox11.setEnabled(enabled);
        jCheckBox12.setEnabled(enabled);
        jCheckBox14.setEnabled(enabled);
        if(jCheckBox14.isEnabled() && jCheckBox14.isSelected()){
            jTextField3.setEnabled(true);
        }else{
            jTextField3.setEnabled(false);
        }
        
        jLabel2.setEnabled(enabled);
        jLabel4.setEnabled(enabled);
        jLabel5.setEnabled(enabled);
        jLabel6.setEnabled(enabled);
        jLabel8.setEnabled(enabled);
        jLabel9 .setEnabled(enabled);
        jLabel10 .setEnabled(enabled);
    }//GEN-LAST:event_jCheckBox13ActionPerformed

    /**
     * jCheckBox14 - Dis-/Enabled custom replacement <br/>
     * @param evt The triggered event
     */
    private void jCheckBox14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox14ActionPerformed
        jTextField3.setEnabled(jCheckBox14.isSelected());
    }//GEN-LAST:event_jCheckBox14ActionPerformed

    /**
     * 
     * @param evt 
     */
    private void jTextField3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField3MouseClicked
        if((jTextField3.getText().equals(CUSTOM_REPLACEMENT_EXAMPLE)) && (jTextField3.isEnabled())){
            jTextField3.setText("");
        }
    }//GEN-LAST:event_jTextField3MouseClicked

    /**
     * updates the imdb buttons and en/disables the thumbnail checkbox
     */
    private void updateImportThumbnailButton() {
        if (jCheckBox3.isSelected()){
            jCheckBox5.setEnabled(true);
        } 
        else {
            //Thumbnail
            jCheckBox5.setSelected(false);
            jCheckBox5.setEnabled(false);
        }
    }

    /**
     * enables or disables the remove button
     */
    private void updateRemoveButton() {
        if (((collection instanceof MovieCollection) && (movieTree.getSelectionPath() != null))
                || ((collection instanceof SerieCollection) && (seriesTree.getSelectionPath() != null))) {
            jButton3.setEnabled(true);
            jButton8.setEnabled(true);
        } else {
            jButton3.setEnabled(false);
            jButton8.setEnabled(false);
        }
    }
    
    /**
     * Macht die GUI sichtbar startet nach Auswahl den Import Prozess
     */
    public void showGUI() {
        this.setVisible(true);
    }

    /**
     * Öffnet einen MyFileChooser zum Auswählen der Import Verzeichnisse
     */
    private void openFileChooser() {
        MyJFileChooser jFileChooser1 = new MyJFileChooser("", null);
        jFileChooser1.setAcceptAllFileFilterUsed(true);
        jFileChooser1.setMultiSelectionEnabled(true);
        jFileChooser1.setDialogType(JFileChooser.OPEN_DIALOG);
        
        jFileChooser1.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        //zeige Dialog und mache etwas, wenn "Öffnen" geklickt wurde
        if (jFileChooser1.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            //Erstelle Pfad zur Datei
            File[] dirs = jFileChooser1.getSelectedFiles();
            addDirsToModel(Arrays.asList(dirs));
        }
    }

    /**
     * actualizes all tress with the latest settings
     */
    private void actualizeTrees() {
        final ImportDirectoryGUI instance = this;
        Thread updateTreesTask = new Thread(new Runnable() {
            @Override
            public void run() {
                instance.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                seriesMap.clear();
                moviesMap.clear();
                clearTrees();                               
                for (MyFile dir : dirsToImport.keySet()) {
                    FileTypeInterface.MediaType type = dirsToImport.get(dir);
                    if (type == FileTypeInterface.MediaType.MOVIE) {
                        DefaultMutableTreeNode movieRootNode = new DefaultMutableTreeNode(dir);
                        ImportProcess.determineMovieDirsToImport(movieRootNode, new MyFile[]{dir}, jCheckBox6.isSelected(), jCheckBox1.isSelected(), 0);
                        addTreeNodeToGUI(movieRootNode, movieTree, moviesTreeModel, moviesRootNode, moviesMap);
                    } else if (type == FileTypeInterface.MediaType.SERIE) {
                        DefaultMutableTreeNode seriesNode = new DefaultMutableTreeNode();
                        ImportProcess.determineSerieDirsToImport(seriesNode, new MyFile[]{dir});
                        addTreeNodeToGUI(seriesNode, seriesTree, seriesTreeModel, seriesRootNode, seriesMap);
                    }
                }
                instance.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        });
        updateTreesTask.start();
    }
    
    /**
     * removes all values from both trees
     */
    private void clearTrees(){
        for(int i = moviesRootNode.getChildCount()-1; i >= 0; i --){
            moviesTreeModel.removeNodeFromParent((DefaultMutableTreeNode)moviesRootNode.getChildAt(i));
        }
        for(int i = seriesRootNode.getChildCount()-1; i >= 0; i --){
            seriesTreeModel.removeNodeFromParent((DefaultMutableTreeNode)seriesRootNode.getChildAt(i));
        }        
    }

    /**
     * Adds a TreeNode to the GUI
     *
     * @param treeNodeToAdd the treeNode to add to the gui
     * @param tree the tree to update
     * @param treeModel the treeModel to insert the node
     * @param rootNode the root node of the treeModel
     * @param map the map to put the values in
     */
    private synchronized void addTreeNodeToGUI(
            DefaultMutableTreeNode treeNodeToAdd,
            JTree tree,
            DefaultTreeModel treeModel,
            MutableTreeNode rootNode,
            Map<String, DefaultMutableTreeNode> map) {
          while(treeNodeToAdd.getChildCount() > 0){           
//        for (int i = treeNodeToAdd.getChildCount()-1; i >= 0; i--) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeNodeToAdd.getChildAt(0);
            Object userObj = node.getUserObject();
            String name;
            if(userObj instanceof File){
                name = ((File)userObj).getName();
            }
            else{
                name = userObj.toString();
            }
            DefaultMutableTreeNode oldTree = map.put(name, node);
            if((oldTree != null) && (oldTree.getParent() != null)){
                treeModel.removeNodeFromParent(oldTree);
            }
            treeModel.insertNodeInto(node, rootNode, rootNode.getChildCount());
        }
        tree.expandPath(new TreePath(rootNode));
        updateNumberOfPreviewElements(map);
    }
    
    /**
     * updates the number of preview elements, displayed on the border label
     * 
     * @param map the map to get the number of elements ( size() )
     */ 
    private void updateNumberOfPreviewElements(Map<String, DefaultMutableTreeNode> map){
        TitledBorder border = (TitledBorder)jPanel5.getBorder();
        if(map.size() > 0){
            border.setTitle(bundle.getString("ImportDirectoryGUI.jPanel5") + " (" + map.size() + ")");
        }
        else{
            border.setTitle(bundle.getString("ImportDirectoryGUI.jPanel5"));
        }
        jPanel5.repaint();
    }
    
    /**
     * <b>This will be executed asynchronously</b><br/>.
     * @param files 
     */
    private void addDirsToModel(final List<File> files){
        final ImportDirectoryGUI instance = this;
        Thread updateUI = new Thread(new Runnable() {
            @Override
            public void run() {
                jButton2.setEnabled(false);
                instance.setCursor(new Cursor(Cursor.WAIT_CURSOR));

                List<Thread> taskList = new ArrayList<Thread>();
                for (final File file : files) {
                    Thread task = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            addDirToModel(file);
                        }
                    });
                    taskList.add(task);
                    task.start();
                }

                //Wait until each thread is finished
                for(Thread task: taskList){
                    try {
                        task.join();
                    } catch (InterruptedException ex) {
                        LOG.error("Error occured while synchronizing import threads for directory import.", ex);
                    }
                }

                instance.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                jButton2.setEnabled(true);
            }
        });
        updateUI.start();
    }
    /**
     * Adds a file to the list or tree model
     *
     * @param file the dir to import
     */
    private void addDirToModel(final File file) {
        MyFile newFile = new MyFile(file);
        if (collection instanceof MovieCollection) {
            dirsToImport.put(newFile, FileTypeInterface.MediaType.MOVIE);
            DefaultMutableTreeNode movieRootNode = new DefaultMutableTreeNode(newFile);
            ImportProcess.determineMovieDirsToImport(movieRootNode, new MyFile[]{newFile}, jCheckBox6.isSelected(), jCheckBox1.isSelected(), 0);
            addTreeNodeToGUI(movieRootNode, movieTree, moviesTreeModel, moviesRootNode, moviesMap);
        } else if (collection instanceof SerieCollection) {
            //TODO: Ordne Season nach Season in der Vorschau, ebenso Episoden
            dirsToImport.put(newFile, FileTypeInterface.MediaType.SERIE);
            DefaultMutableTreeNode seriesNode = new DefaultMutableTreeNode();
            ImportProcess.determineSerieDirsToImport(seriesNode, new MyFile[]{newFile});
            addTreeNodeToGUI(seriesNode, seriesTree, seriesTreeModel, seriesRootNode, seriesMap);
        }
    }
    
    /**
     * Initialisiert file drop auf Windows
     *
     * @see http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4899516
     */
    private void initDragAndDropOnWindows() {
        DropTarget target = new DropTarget(seriesTree, new DropTargetAdapter() {
            @Override
            public void drop(DropTargetDropEvent dtde) {
                if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                    dtde.acceptDrop(dtde.getDropAction());
                    try {
                        //siehe: http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4899516
                        List<File> list = (List<File>) dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                        //Hier steht der eigentliche Drop Code     
                        addDirsToModel(list);
                    } catch (UnsupportedFlavorException | IOException ex) {
                        LOG.error("Error while dropping file to series tree.", ex);
                    }
                }
            }
        });
        seriesTree.setDropTarget(target);

        DropTarget listTarget = new DropTarget(movieTree, new DropTargetAdapter() {
            @Override
            public void drop(DropTargetDropEvent dtde) {
                if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                    dtde.acceptDrop(dtde.getDropAction());
                    try {
                        //siehe: http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4899516
                        List<File> list = (List<File>) dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                        //Hier steht der eigentliche Drop Code    
                        addDirsToModel(list);
                    } catch (UnsupportedFlavorException | IOException ex) {
                        LOG.error("Error while dropping file to movies tree.", ex);
                    }
                }
            }
        });
        movieTree.setDropTarget(listTarget);
    }

    /**
     * Tests, if there is an internet connection available and the api is enabled. <br/>
     * Decativate import buttons, if one of them is not available
    **/ 
    private void testInternetAndAPI() {
        Thread testConnection = new Thread(new Runnable() {
            @Override
            public void run() {
                if (!Utils.isInternetConnectionAvailable() || !new TMDBMovieWrapper().isAPIenabled() || !new TheTVDB().isAPIenabled()) {
                    jCheckBox3.setSelected(false);
                    jCheckBox3.setEnabled(false);
                }
            }
        });
        testConnection.start();
    }
    
    /**
     * Sucht nach der Mediainfo Library und deaktiiviert Import Optionen, wenn
     * diese nicht gefunden wurde
    *
     */
    private void lookForLibrary() {
        if (!OperatingSystem.lookForLibrary()) {
            jCheckBox4.setSelected(false);
            jCheckBox4.setEnabled(false);
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox10;
    private javax.swing.JCheckBox jCheckBox11;
    private javax.swing.JCheckBox jCheckBox12;
    private javax.swing.JCheckBox jCheckBox13;
    private javax.swing.JCheckBox jCheckBox14;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckBox4;
    private javax.swing.JCheckBox jCheckBox5;
    private javax.swing.JCheckBox jCheckBox6;
    private javax.swing.JCheckBox jCheckBox7;
    private javax.swing.JCheckBox jCheckBox8;
    private javax.swing.JCheckBox jCheckBox9;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
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
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JRadioButton jRadioButton6;
    private javax.swing.JRadioButton jRadioButton7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTree movieTree;
    private javax.swing.JTree seriesTree;
    // End of variables declaration//GEN-END:variables
}
