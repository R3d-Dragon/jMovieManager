/**
 * Copyright (c) 2010-2015 Bryan Beck.
 * All rights reserved.
 * 
 * This project is licensed under LGPL v2.1.
 * See jMovieManager-license.txt for details.
 * 
 */
package jMovieManager.swing.gui.components.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ResourceBundle;
import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalFileChooserUI;
import jMovieManager.swing.gui.ColorInterface;
import jmm.persist.PersistingManager;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;
import sun.swing.FilePane;

/**
 *
 * @author Bryan Beck
 * @since 07.11.2011
 */
public class MyFileChooserUI extends MetalFileChooserUI{
    /** Logger. */
    private static final Logger LOG = LoggerFactory.logger(MyFileChooserUI.class);
    
    /**
     * creates the UI (look and feel) for the component
     * 
     * @param b the component
     * @return the ComponentUI
     */
    public static ComponentUI createUI(JComponent c) {
        return new MyFileChooserUI((JFileChooser) c);
    }    
    
    /**
     * Konstruktor
     * @param filechooser Swing Komponente
     */
    public MyFileChooserUI(JFileChooser filechooser) {
        super(filechooser);
    }
    
    
    @Override
    public void installComponents(final JFileChooser fc) {
        super.installComponents(fc);        
        ResourceBundle bundle = ResourceBundle.getBundle("jMovieManager.swing.resources.MovieManager");
        //TODO: Nicht Optimal, allerdings erfordert der direkte Zugriff das komplette überschreiben des FileChooserUI's
        try{       
            //Get Filepane to update the contextMenu
            BorderLayout fcLayout = (BorderLayout)fc.getLayout();
            FilePane filePane = (FilePane)fcLayout.getLayoutComponent(BorderLayout.CENTER);
            JPopupMenu popupMenu = filePane.getComponentPopupMenu();
            //TODO: MyJPopupMenuUI 
            popupMenu.setBorder(javax.swing.BorderFactory.createEtchedBorder(null, ColorInterface.list_tree_BorderShadow));
//            popupMenu.setBorder(null);
//            popupMenu.setBorderPainted(false);
            
            JMenu menu = (JMenu)filePane.getComponentPopupMenu().getComponent(0);
            menu.setOpaque(true);
            
            //Ändere ComboBox UI's
            JComboBox fileChooseComboBox = (JComboBox)((JPanel)fc.getComponent(0)).getComponent(2);
            fileChooseComboBox.setUI(MyBasicComboBoxUI.createUI(fileChooseComboBox));

            JComboBox tempfilterComboBox = (JComboBox)((JPanel)this.getBottomPanel().getComponent(2)).getComponent(1); 
            tempfilterComboBox.setUI(MyBasicComboBoxUI.createUI(tempfilterComboBox));  
                                         
            //Ändere die Aktionsbuttons im oberen Panel
            JPanel topPanel = (JPanel)((JPanel)fc.getComponent(0)).getComponent(0);
            Component[] topPanelComps = topPanel.getComponents();
           // for(Component topPanelComponent: topPanel.getComponents()){
            for(int i = 0; i < topPanelComps.length; i++){      //Index 0, 2, 4
                if(topPanelComps[i] instanceof AbstractButton){
                    AbstractButton topButton = (AbstractButton)topPanelComps[i];
                    topButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    topButton.setBorderPainted(false);
                    topButton.setContentAreaFilled(false);
                    topButton.setUI((MyBasicMenuButtonUI)MyBasicMenuButtonUI.createUI(topButton));
                }
            }
            JToggleButton listButton = (JToggleButton)topPanel.getComponent(6);
            JToggleButton detailButton = (JToggleButton)topPanel.getComponent(7);      
            listButton.setSelectedIcon(UIManager.getIcon("FileChooser.listViewSelectedIcon"));
            detailButton.setSelectedIcon(UIManager.getIcon("FileChooser.detailsViewSelectedIcon"));
            //detailButton.setLocation(listButton.getX()+listButton.getWidth(), listButton.getY());
          // detailButton.setBounds(listButton.getX()+listButton.getWidth(), listButton.getY(), detailButton.getWidth(), detailButton.getHeight());

            //Ändere die 2 Buttons am Boden
            for(Component button: this.getButtonPanel().getComponents()){
                //Setzte neues Button UI
                if(button instanceof JButton){
                    ((JButton)button).setUI((MyBasicButtonUI)MyBasicButtonUI.createUI((JButton)button));
                    button.setCursor(new Cursor(Cursor.HAND_CURSOR));
                }
            }
            
            /*
             * add custom checkbox to show hidden files
             */
            final JCheckBox showHiddenFiles = new JCheckBox(bundle.getString("MyJFileChooser.showHiddenFiles"));
            showHiddenFiles.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    if(showHiddenFiles.isSelected()){
                        fc.setFileHidingEnabled(false);
                    }else{
                        fc.setFileHidingEnabled(true);
                    }
                }
            });
            JPanel checkBoxPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
//            JPanel checkBoxPanel = new JPanel();
//            checkBoxPanel.setLayout(new BoxLayout(checkBoxPanel, BoxLayout.LINE_AXIS));
            JPanel emptyPanel = new JPanel();
            JPanel filesOfTypePanel = (JPanel)this.getBottomPanel().getComponent(2);
            Dimension prefDim = filesOfTypePanel.getComponent(0).getPreferredSize();

            emptyPanel.setPreferredSize(new Dimension((int)prefDim.getWidth()-5, (int)prefDim.getHeight()));
            emptyPanel.setMinimumSize(new Dimension((int)prefDim.getWidth()-5, (int)prefDim.getHeight()));
            emptyPanel.setMaximumSize(new Dimension((int)prefDim.getWidth()-5, (int)prefDim.getHeight()));
                
            checkBoxPanel.add(emptyPanel);
            checkBoxPanel.add(showHiddenFiles);
            this.getBottomPanel().add(checkBoxPanel,3);
            fc.setFileHidingEnabled(true);
        }
        catch(ClassCastException ex){
            LOG.error("Cannot cast class (FileChooser, ComboBox or FilePane).", ex);
        }
    }
     
    @Override
    protected JPanel createList(JFileChooser fc) {   
        return super.createList(fc);
    }
}
