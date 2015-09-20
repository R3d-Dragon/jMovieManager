/**
 * Copyright (c) 2010-2015 Bryan Beck.
 * All rights reserved.
 * 
 * This project is licensed under LGPL v2.1.
 * See jMovieManager-license.txt for details.
 * 
 */
package jMovieManager.swing.gui;

import java.awt.Color;

/**
 * Color Interface, in der alle verwendeten GUI Farben definiert sind
 * @author Bryan Beck
 * @since 10.10.2011
 */
public interface ColorInterface {    
    public static final  Color MenuText = new Color(210, 210, 210);
    public static final  Color listText = new Color(166, 166, 166);
    
    //Dunkelgraue Farbe für Deaktivierte Texte 
    public static final  Color darkText = new Color(135, 135, 135);
    public static final  Color darkTextButton = new Color(60, 60, 60);

    public static final  Color textareaForeground = Color.WHITE;
    public static final  Color textfieldForeground = Color.WHITE;

    
    //Caret Color - Farbe der Mausposition in jTextFields
    public static final Color caretColor = Color.WHITE;
    
    //Farbe für TitledBorder
    public static final  Color titledBorderForeground =  new Color(58, 131, 200); //MenuText; //new Color(80, 80, 210);//new Color(255, 214, 52); //new Color(180, 180, 180);1
    public static final  Color titledBorderColor = new Color(80, 80, 210);
 
    //Gelbe Farbe für Selektionstexte
    public static final  Color selectedListText = new Color(255, 214, 52);  

    public static final  Color orange = new Color(234, 149, 59);
    //Hintergrundfarbe für Panel, Liste und Trees
    public static final  Color panelBackground = new Color(18, 18, 18);
    public static final  Color list_tree_Background = new Color(30, 30, 30);
    public static final  Color list_tree_BorderHighlight = new Color(99, 99, 99);
    public static final  Color list_tree_BorderShadow = new Color(63, 63, 63);
     
    //Hintergrundfarbe für Eingabemasken Textfield, TextArea, Combobox
    public static final  Color selectionBackground = new Color(56, 56, 56);
    
    //Farbe für den Rand der Menus
    public static final Color popupMenuBorder = new Color(128, 128, 128);
    //Seperator in Menu, MenuItems
    public static final  Color menuItemBackground = list_tree_Background; //new Color(89, 89, 89);
    public static final  Color menuItemForeground = new Color(210, 210, 210);
    public static final  Color menuItemDisabledForeground = new Color(60, 60, 60);
    public static final  Color menuItemSelectedBackground = new Color(255, 255, 255);
    
    public static final  Color seperatorBackground = list_tree_Background; //new Color(89, 89, 89);
    public static final  Color seperatorForeground = new Color(111, 111, 111);   
    
    //Scrollbar
    public static final  Color scrollbarTrackHightlight = new Color(89, 89, 89);   
    public static final  Color scrollbarThumb = new Color(128, 128, 128);   
    
    //Tooltip
    public static final  Color tooltipBackground = new Color(38, 38, 38);    
    
    //Farben der Pfeile für Combobox, ScrollPane und Scrollbar
    public static final Color basicArrowButton_Foreground = new Color(128, 128, 128);
    public static final Color basicArrowButton_Background = new Color(29, 29, 29);
    
    
    public static final Color debug_Pink = new Color(255,20, 147);
}
