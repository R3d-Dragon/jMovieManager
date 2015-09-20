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
import java.util.ResourceBundle;
import javax.swing.JComboBox;
import jmm.data.collection.MovieCollection;
import jmm.utils.FileManager;
import jmm.utils.FileManager.Case;
import jmm.utils.FileManager.RenamePattern;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;

/**
 * GUI to specify a rename pattern
 * 
 * @author Bryan Beck
 * @since 20.10.2010
 */
public class RenameFilesGUI extends javax.swing.JDialog{
    /** Logger. */
    private static final Logger LOG = LoggerFactory.logger(RenameFilesGUI.class);
    
    //TODO: File renaming for series
    private String example;
    private String fileEnding = ".avi";
    
    private String param1;
    private String param2;
    private String param3;
    private String param4;
    private String param5;
    private char seperator;
    private String preAttach;
    private String postAttach;
    private Case fileNameCase;
    private MovieCollection collectionToRename;
    
    private ResourceBundle bundle;   
       
    /** Creates new form RenameFilesGUI */
    public RenameFilesGUI(java.awt.Frame parent, boolean modal, MovieCollection collection) {
        super(parent, modal);
        bundle = ResourceBundle.getBundle("jMovieManager.swing.resources.MovieManager"); 
        collectionToRename = collection;
        fileNameCase = Case.DEFAULT;
        seperator = '.';
        preAttach = "";
        postAttach = "";
        
        initComponents();
        //GUI zentriert darstellen
        this.setLocationRelativeTo(parent);
                
        jComboBox5.addItem(bundle.getString("RenameFilesGUI.jComboBox.value0"));
        jComboBox5.addItem(bundle.getString("RenameFilesGUI.jComboBox.value1"));
        jComboBox5.addItem(bundle.getString("RenameFilesGUI.jComboBox.value2"));
        jComboBox5.addItem(bundle.getString("RenameFilesGUI.jComboBox.value3"));
        jComboBox5.addItem(bundle.getString("RenameFilesGUI.jComboBox.value4"));
        jComboBox5.addItem(bundle.getString("RenameFilesGUI.jComboBox.value5"));
        jComboBox5.addItem(bundle.getString("RenameFilesGUI.jComboBox.value6"));
        jComboBox5.addItem(bundle.getString("RenameFilesGUI.jComboBox.value7"));

        jComboBox4.addItem(bundle.getString("RenameFilesGUI.jComboBox.value0"));
        jComboBox4.addItem(bundle.getString("RenameFilesGUI.jComboBox.value1"));
        jComboBox4.addItem(bundle.getString("RenameFilesGUI.jComboBox.value2"));
        jComboBox4.addItem(bundle.getString("RenameFilesGUI.jComboBox.value3"));
        jComboBox4.addItem(bundle.getString("RenameFilesGUI.jComboBox.value4"));
        jComboBox4.addItem(bundle.getString("RenameFilesGUI.jComboBox.value5"));
        jComboBox4.addItem(bundle.getString("RenameFilesGUI.jComboBox.value6"));
        jComboBox4.addItem(bundle.getString("RenameFilesGUI.jComboBox.value7"));

        jComboBox3.addItem(bundle.getString("RenameFilesGUI.jComboBox.value0"));
        jComboBox3.addItem(bundle.getString("RenameFilesGUI.jComboBox.value1"));
        jComboBox3.addItem(bundle.getString("RenameFilesGUI.jComboBox.value2"));
        jComboBox3.addItem(bundle.getString("RenameFilesGUI.jComboBox.value3"));
        jComboBox3.addItem(bundle.getString("RenameFilesGUI.jComboBox.value4"));
        jComboBox3.addItem(bundle.getString("RenameFilesGUI.jComboBox.value5"));
        jComboBox3.addItem(bundle.getString("RenameFilesGUI.jComboBox.value6"));
        jComboBox3.addItem(bundle.getString("RenameFilesGUI.jComboBox.value7"));

        jComboBox2.addItem(bundle.getString("RenameFilesGUI.jComboBox.value0"));
        jComboBox2.addItem(bundle.getString("RenameFilesGUI.jComboBox.value1"));
        jComboBox2.addItem(bundle.getString("RenameFilesGUI.jComboBox.value2"));
        jComboBox2.addItem(bundle.getString("RenameFilesGUI.jComboBox.value3"));
        jComboBox2.addItem(bundle.getString("RenameFilesGUI.jComboBox.value4"));
        jComboBox2.addItem(bundle.getString("RenameFilesGUI.jComboBox.value5"));
        jComboBox2.addItem(bundle.getString("RenameFilesGUI.jComboBox.value6"));
        jComboBox2.addItem(bundle.getString("RenameFilesGUI.jComboBox.value7"));

        jComboBox1.addItem(bundle.getString("RenameFilesGUI.jComboBox.value0"));
        jComboBox1.addItem(bundle.getString("RenameFilesGUI.jComboBox.value1"));
        jComboBox1.addItem(bundle.getString("RenameFilesGUI.jComboBox.value2"));
        jComboBox1.addItem(bundle.getString("RenameFilesGUI.jComboBox.value3"));
        jComboBox1.addItem(bundle.getString("RenameFilesGUI.jComboBox.value4"));
        jComboBox1.addItem(bundle.getString("RenameFilesGUI.jComboBox.value5"));
        jComboBox1.addItem(bundle.getString("RenameFilesGUI.jComboBox.value6"));
        jComboBox1.addItem(bundle.getString("RenameFilesGUI.jComboBox.value7"));
        
        jComboBox7.addItem(bundle.getString("RenameFilesGUI.jComboBox7.value0"));        
        jComboBox7.addItem(bundle.getString("RenameFilesGUI.jComboBox7.value1"));         
        jComboBox7.addItem(bundle.getString("RenameFilesGUI.jComboBox7.value2"));       
        updateLabel();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jComboBox2 = new javax.swing.JComboBox();
        jComboBox3 = new javax.swing.JComboBox();
        jComboBox4 = new javax.swing.JComboBox();
        jComboBox5 = new javax.swing.JComboBox();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jComboBox6 = new javax.swing.JComboBox();
        jLabel12 = new javax.swing.JLabel();
        jComboBox7 = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("jMovieManager/swing/resources/MovieManager"); // NOI18N
        setTitle(bundle.getString("RenameFilesGUI.headline")); // NOI18N
        setResizable(false);

        jButton2.setText(bundle.getString("RenameFilesGUI.jButton2")); // NOI18N
        jButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton2.setMaximumSize(new java.awt.Dimension(90, 23));
        jButton2.setMinimumSize(new java.awt.Dimension(90, 23));
        jButton2.setPreferredSize(new java.awt.Dimension(90, 23));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton2);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setText(bundle.getString("RenameFilesGUI.jLabel1")); // NOI18N

        jLabel2.setText(bundle.getString("RenameFilesGUI.jLabel2")); // NOI18N

        jLabel4.setText(bundle.getString("RenameFilesGUI.jLabel4")); // NOI18N

        jLabel5.setText(bundle.getString("RenameFilesGUI.jLabel5")); // NOI18N

        jLabel6.setText(bundle.getString("RenameFilesGUI.jLabel6")); // NOI18N

        jLabel7.setText(bundle.getString("RenameFilesGUI.jLabel7")); // NOI18N

        jLabel8.setText(bundle.getString("RenameFilesGUI.jLabel8")); // NOI18N

        jLabel9.setText(bundle.getString("RenameFilesGUI.jLabel9")); // NOI18N

        jLabel10.setText(bundle.getString("RenameFilesGUI.jLabel10")); // NOI18N

        jLabel11.setText(bundle.getString("RenameFilesGUI.jLabel11")); // NOI18N

        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });

        jComboBox2.setEnabled(false);
        jComboBox2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox2ItemStateChanged(evt);
            }
        });

        jComboBox3.setEnabled(false);
        jComboBox3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox3ItemStateChanged(evt);
            }
        });

        jComboBox4.setEnabled(false);
        jComboBox4.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox4ItemStateChanged(evt);
            }
        });

        jComboBox5.setEnabled(false);
        jComboBox5.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox5ItemStateChanged(evt);
            }
        });

        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField2KeyTyped(evt);
            }
        });

        jTextField3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField3KeyTyped(evt);
            }
        });

        jComboBox6.setModel(new javax.swing.DefaultComboBoxModel(new String[] { ".", ",", "-", "_" }));
        jComboBox6.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox6ItemStateChanged(evt);
            }
        });

        jLabel12.setText(bundle.getString("RenameFilesGUI.jLabel12")); // NOI18N

        jComboBox7.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox7ItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 331, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBox5, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBox4, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBox2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBox3, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(jLabel11)
                            .addComponent(jLabel10)
                            .addComponent(jLabel12))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBox7, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextField3)
                            .addComponent(jTextField2)
                            .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12)
                    .addComponent(jComboBox7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyTyped
        preAttach = jTextField2.getText() + evt.getKeyChar();
        updateLabel();
    }//GEN-LAST:event_jTextField2KeyTyped

    private void jTextField3KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField3KeyTyped
        postAttach = jTextField3.getText() + evt.getKeyChar();
        updateLabel();
    }//GEN-LAST:event_jTextField3KeyTyped

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        param1 = jComboBox1.getSelectedItem().toString();
        if(param1.equals(bundle.getString("RenameFilesGUI.jComboBox.value0"))){
            jComboBox2.setEnabled(false);
            jComboBox2.setSelectedIndex(0);
            jComboBox3.setEnabled(false);
            jComboBox3.setSelectedIndex(0);
            jComboBox4.setEnabled(false);
            jComboBox4.setSelectedIndex(0);
            jComboBox5.setEnabled(false);
            jComboBox5.setSelectedIndex(0);
        }else{
            jComboBox2.setEnabled(true);
        }
        updateLabel();
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jComboBox2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox2ItemStateChanged
        param2 = jComboBox2.getSelectedItem().toString();
        if(param2.equals(bundle.getString("RenameFilesGUI.jComboBox.value0"))){
            jComboBox3.setEnabled(false);
            jComboBox3.setSelectedIndex(0);
            jComboBox4.setEnabled(false);
            jComboBox4.setSelectedIndex(0);
            jComboBox5.setEnabled(false);
            jComboBox5.setSelectedIndex(0);
        }else{
            jComboBox3.setEnabled(true);
        }
        updateLabel();
    }//GEN-LAST:event_jComboBox2ItemStateChanged

    private void jComboBox3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox3ItemStateChanged
        param3 = jComboBox3.getSelectedItem().toString();
        if(param3.equals(bundle.getString("RenameFilesGUI.jComboBox.value0"))){
            jComboBox4.setEnabled(false);
            jComboBox4.setSelectedIndex(0);
            jComboBox5.setEnabled(false);
            jComboBox5.setSelectedIndex(0);
        }else{
            jComboBox4.setEnabled(true);
        }
        updateLabel();
    }//GEN-LAST:event_jComboBox3ItemStateChanged

    private void jComboBox4ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox4ItemStateChanged
        param4  = jComboBox4.getSelectedItem().toString();
        if(param4.equals(bundle.getString("RenameFilesGUI.jComboBox.value0"))){
            jComboBox5.setEnabled(false);
            jComboBox5.setSelectedIndex(0);
        }else{
            jComboBox5.setEnabled(true);
        }
        updateLabel();
    }//GEN-LAST:event_jComboBox4ItemStateChanged

    private void jComboBox5ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox5ItemStateChanged
         param5 = jComboBox5.getSelectedItem().toString();
         updateLabel();
    }//GEN-LAST:event_jComboBox5ItemStateChanged

    private void jComboBox6ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox6ItemStateChanged
        seperator = jComboBox6.getSelectedItem().toString().charAt(0);
        updateLabel();
    }//GEN-LAST:event_jComboBox6ItemStateChanged

    /**
     * determined the rename pattern due to the selected index of the combobox
     * @param combobox the box with the selected index
     * @return the rename pattern
     */
    private RenamePattern determineRenamePatternParam(JComboBox combobox){
        int selIndex = combobox.getSelectedIndex();
        RenamePattern pattern;
        switch(selIndex){
            case 1:  pattern = RenamePattern.TITLE;
                    break;
            case 2:  pattern = RenamePattern.ORIGINAL_TITLE;
                    break;
            case 3:  pattern = RenamePattern.PLAYTIME;
                    break;
            case 4:  pattern = RenamePattern.RELEASE_YEAR;
                    break;
            case 5:  pattern = RenamePattern.VIDEO_CODEC;
                    break;
            case 6:  pattern = RenamePattern.AUDIO_CODEC;
                    break;
            case 7:  pattern = RenamePattern.SOURCE;
                    break;
            default: pattern = RenamePattern.NONE;
        }
        return pattern;
    }
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if((jComboBox1.getSelectedIndex() < 1) &&
                (jTextField2.getText().trim().isEmpty()) &&
                (jTextField3.getText().trim().isEmpty())){
            javax.swing.JOptionPane.showMessageDialog(this, bundle.getString("RenameFilesGUI.error.noPattern"));            
            return;  
        }    
        RenamePattern params[] = new RenamePattern[]{
            determineRenamePatternParam(jComboBox1),
            determineRenamePatternParam(jComboBox2),
            determineRenamePatternParam(jComboBox3),
            determineRenamePatternParam(jComboBox4),
            determineRenamePatternParam(jComboBox5)
        };    
        this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        FileManager fileManager = new FileManager(collectionToRename, 
                params, 
                jTextField2.getText(),
                jTextField3.getText(),
                jComboBox6.getSelectedItem().toString(),
                fileNameCase);
        fileManager.renameFiles();

        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * jCombobox7 - update case settings <br/>
     * Updates the case settings for the rename pattern
     * 
     * @param evt the triggered event
     */
    private void jComboBox7ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox7ItemStateChanged
        if(jComboBox7.getSelectedIndex() == 1){
            fileNameCase = Case.UPPER_CASE;
        } else if(jComboBox7.getSelectedIndex() == 2){
            fileNameCase = Case.LOWER_CASE;
        } else{
            fileNameCase = Case.DEFAULT;
        }
        updateLabel();
    }//GEN-LAST:event_jComboBox7ItemStateChanged

    /**
    * Methode zum aktualisieren des Beispiel jLabels
    */
    private void updateLabel(){
        example = "";
        if(preAttach.equals("\b") || preAttach.trim().isEmpty()){
            preAttach = "";
        }
        if(postAttach.equals("\b") || postAttach.trim().isEmpty()){
            postAttach = "";
        }        
        if(!preAttach.isEmpty()){
            example = preAttach;
        }
        if((param1 != null) && (!param1.isEmpty()) && (!param1.equals(bundle.getString("RenameFilesGUI.jComboBox.value0")))){
            if(preAttach.isEmpty()){
                example += param1;
            }else{
                example += seperator + param1;
            }
        }
        if((param2 != null) && (!param2.isEmpty()) && (!param2.equals(bundle.getString("RenameFilesGUI.jComboBox.value0")))){
            example += seperator + param2;
        }
        if((param3 != null) && (!param3.isEmpty()) && (!param3.equals(bundle.getString("RenameFilesGUI.jComboBox.value0")))){
            example += seperator + param3;
        }
        if((param4 != null) && (!param4.isEmpty()) &&(!param4.equals(bundle.getString("RenameFilesGUI.jComboBox.value0")))){
            example += seperator + param4;
        }
        if((param5 != null) && (!param5.isEmpty()) && (!param5.equals(bundle.getString("RenameFilesGUI.jComboBox.value0")))){
            example += seperator + param5;
        }
        if(!postAttach.isEmpty()){
            if(example.isEmpty()){
                example += postAttach;
            }else{
                example += seperator + postAttach;
            }
        }
        example += fileEnding;
        
        if(fileNameCase == Case.UPPER_CASE){
            example = example.toUpperCase();
        }
        else if(fileNameCase == Case.LOWER_CASE){
            example = example.toLowerCase();   
        }        
        jLabel3.setText(example);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JComboBox jComboBox3;
    private javax.swing.JComboBox jComboBox4;
    private javax.swing.JComboBox jComboBox5;
    private javax.swing.JComboBox jComboBox6;
    private javax.swing.JComboBox jComboBox7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    // End of variables declaration//GEN-END:variables
}
