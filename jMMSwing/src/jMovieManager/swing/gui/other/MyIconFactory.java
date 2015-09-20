/**
 * Copyright (c) 2010-2015 Bryan Beck.
 * All rights reserved.
 * 
 * This project is licensed under LGPL v2.1.
 * See jMovieManager-license.txt for details.
 * 
 */
package jMovieManager.swing.gui.other;

import javax.swing.ImageIcon;

/**
 * Provides a singleton instance for all icons which are used inside of jMM
 * 
 * @author Bryan Beck
 * @since 29.10.2012
 */
public abstract class MyIconFactory {
    private static final String pathToTextures = "/jMovieManager/swing/textures/";
    private static final String pathToImages = "/jMovieManager/swing/images/";

    //RadioButton
    public static final ImageIcon radioButton_default = new ImageIcon(MyIconFactory.class .getResource(pathToTextures + "radiobutton/radio.png"));
    public static final ImageIcon radioButton_disabled = new ImageIcon(MyIconFactory.class .getResource(pathToTextures + "radiobutton/radio_disabled.png"));
    public static final ImageIcon radioButton_disabled_selected = new ImageIcon(MyIconFactory.class .getResource(pathToTextures + "radiobutton/radio_disabled_selected.png"));
    public static final ImageIcon radioButton_highlight = new ImageIcon(MyIconFactory.class .getResource(pathToTextures + "radiobutton/radio_highlight.png"));
    public static final ImageIcon radioButton_highlight_selected = new ImageIcon(MyIconFactory.class .getResource(pathToTextures + "radiobutton/radio_highlight_selected.png"));
    public static final ImageIcon radioButton_selected = new ImageIcon(MyIconFactory.class .getResource(pathToTextures + "radiobutton/radio_selected.png"));

    //CheckBox
    public static final ImageIcon checkBox_default = new ImageIcon(MyIconFactory.class .getResource(pathToTextures + "checkbox/checkbox.png"));
    public static final ImageIcon checkBox_disabled = new ImageIcon(MyIconFactory.class .getResource(pathToTextures + "checkbox/checkbox_disabled.png"));
    public static final ImageIcon checkBox_disabled_selected = new ImageIcon(MyIconFactory.class .getResource(pathToTextures + "checkbox/checkbox_disabled_selected.png"));
    public static final ImageIcon checkBox_highlight = new ImageIcon(MyIconFactory.class .getResource(pathToTextures + "checkbox/checkbox_highlight.png"));
    public static final ImageIcon checkBox_highlight_selected = new ImageIcon(MyIconFactory.class .getResource(pathToTextures + "checkbox/checkbox_highlight_selected.png"));
    public static final ImageIcon checkBox_selected = new ImageIcon(MyIconFactory.class .getResource(pathToTextures + "checkbox/checkbox_selected.png"));    
    
    //Button
    public static final ImageIcon button_default = new ImageIcon(MyIconFactory.class .getResource(pathToTextures + "button.png"));    
    public static final ImageIcon button_disabled = new ImageIcon(MyIconFactory.class .getResource(pathToTextures + "button_disabled.png")); 
    public static final ImageIcon button_highlight = new ImageIcon(MyIconFactory.class .getResource(pathToTextures + "button_mouseover.png"));    

    //ProgressBar
    public static final ImageIcon progressbar_background = new ImageIcon(MyIconFactory.class .getResource(pathToTextures + "ProgressBarBackground_1px.gif")); 
    public static final ImageIcon progressbar_foreground = new ImageIcon(MyIconFactory.class .getResource(pathToTextures + "ProgressBarForeground_1px.gif")); 

    //MenuBar
    public static final ImageIcon menuBar_background = new ImageIcon(MyIconFactory.class .getResource(pathToTextures + "MenuBar_1px.gif")); 
        
    //FileChooser
    public static final ImageIcon fileChooser_homeFolderIcon = new ImageIcon(MyIconFactory.class .getResource(pathToImages + "home.png")); 
    public static final ImageIcon fileChooser_upFolderIcon = new ImageIcon(MyIconFactory.class .getResource(pathToImages + "open.png")); 
    public static final ImageIcon fileChooser_newFolderIcon = new ImageIcon(MyIconFactory.class .getResource(pathToImages + "node.png")); 
    public static final ImageIcon fileChooser_detailsViewIcon = new ImageIcon(MyIconFactory.class .getResource(pathToImages + "details.gif")); 
    public static final ImageIcon fileChooser_detailsViewSelectedIcon = new ImageIcon(MyIconFactory.class .getResource(pathToImages + "detailsSelected.gif")); 
    public static final ImageIcon fileChooser_listViewIcon = new ImageIcon(MyIconFactory.class .getResource(pathToImages + "list.gif")); 
    public static final ImageIcon fileChooser_listViewSelectedIcon = new ImageIcon(MyIconFactory.class .getResource(pathToImages + "listSelected.gif")); 
    public static final ImageIcon fileChooser_directoryIcon = fileChooser_newFolderIcon; //new ImageIcon(MyIconFactory.class .getResource(pathToImages + "/node.png")); 
    public static final ImageIcon fileChooser_fileIcon = new ImageIcon(MyIconFactory.class .getResource(pathToImages + "leaf.png")); 
    public static final ImageIcon fileChooser_hardDriveIcon = new ImageIcon(MyIconFactory.class .getResource(pathToImages + "disc.png")); 
//    public static final ImageIcon fileChooser_computerIcon = fileChooser_hardDriveIcon; 

    //Tree
    public static final ImageIcon tree_expandedIcon = new ImageIcon(MyIconFactory.class .getResource(pathToImages + "tree_expand.png")); 
    public static final ImageIcon tree_collapsedIcon = new ImageIcon(MyIconFactory.class .getResource(pathToImages + "tree_collapse.png")); 
    public static final ImageIcon tree_openIcon = fileChooser_newFolderIcon; 
    public static final ImageIcon tree_closedIcon = fileChooser_newFolderIcon;
    public static final ImageIcon tree_leafIcon = fileChooser_fileIcon;
        
    //RadioButtonMenuItem
    public static final ImageIcon radioButtonMenuItemUnchecked = new ImageIcon(MyIconFactory.class.getResource(pathToImages + "langUnchecked.gif"));
    public static final ImageIcon radioButtonMenuItemChecked = new ImageIcon(MyIconFactory.class.getResource(pathToImages + "langChecked.gif"));
    
    //watched
    public static final ImageIcon watchedIcon = new ImageIcon(MyIconFactory.class.getResource(pathToImages + "watched.png"));
    public static final ImageIcon watchedEmptyIcon = radioButtonMenuItemUnchecked; //new ImageIcon(MyIconFactory.class.getResource(pathToImages + "emptyPixel.gif"));

    //FSK
    public static final ImageIcon FSK_0 = new ImageIcon(MyIconFactory.class.getResource(pathToImages + "fsk/FSK-0.png"));
    public static final ImageIcon FSK_6 = new ImageIcon(MyIconFactory.class.getResource(pathToImages + "fsk/FSK-6.png"));
    public static final ImageIcon FSK_12 = new ImageIcon(MyIconFactory.class.getResource(pathToImages + "fsk/FSK-12.png"));
    public static final ImageIcon FSK_16 = new ImageIcon(MyIconFactory.class.getResource(pathToImages + "fsk/FSK-16.png"));
    public static final ImageIcon FSK_18 = new ImageIcon(MyIconFactory.class.getResource(pathToImages + "fsk/FSK-18.png"));
                
//    private static Icon internalFrameAltMaximizeIcon;
//    private static Icon internalFrameCloseIcon;
//    private static Icon internalFrameDefaultMenuIcon;
//    private static Icon internalFrameMaximizeIcon;
//    private static Icon internalFrameMinimizeIcon;
//    private static Icon treeComputerIcon;
//    private static Icon treeFloppyDriveIcon;
//    private static Icon treeHardDriveIcon;
}
