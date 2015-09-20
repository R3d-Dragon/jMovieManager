/**
 * Copyright (c) 2010-2015 Bryan Beck.
 * All rights reserved.
 * 
 * This project is licensed under LGPL v2.1.
 * See jMovieManager-license.txt for details.
 * 
 */
package jMovieManager.swing.gui;

/**
 * UI Interface, in der alle verwendeten UI Konstanten und Methoden definiert sind
 * 
 * @author Bryan Beck
 * @since 29.10.2011
 */
public interface UIInterface {
    //Wird für Konstruktoren der GUI Klassen benötigt, gibt an, ob das neue Look and Feel genutzt werden soll
    public static final Boolean useCustomLAF = Boolean.TRUE;
        
   /**
    * Methode zum Anpassen der GUI an das neue Look and Feel
    **/
    void changeUI();
}
