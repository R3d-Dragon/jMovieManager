/**
 * Copyright (c) 2010-2015 Bryan Beck.
 * All rights reserved.
 * 
 * This project is licensed under LGPL v2.1.
 * See jMovieManager-license.txt for details.
 * 
 */
package jMovieManager.swing.gui.components.ui;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.border.BevelBorder;
import javax.swing.plaf.ComboBoxUI;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.ComboPopup;
import jMovieManager.swing.gui.ColorInterface;
import jMovieManager.swing.gui.components.MyBasicArrowButton;

/**
 *
 * @author Bryan Beck
 * @since 10.10.2011
 */
public class MyBasicComboBoxUI extends BasicComboBoxUI implements ColorInterface{
    
    /**
     * creates the UI (look and feel) for the component
     * 
     * @param b the component
     * @return the ComponentUI
     */
    public static ComboBoxUI createUI(JComponent c) {
        return new MyBasicComboBoxUI();
    }
    
    /**
     * Creates and initializes the components which make up the
     * aggregate combo box. This method is called as part of the UI
     * installation process.
     */
    @Override
    protected void installComponents() {
        arrowButton = createArrowButton();
        comboBox.add( arrowButton );

        if (arrowButton != null)  {
            configureArrowButton();
        }

        if ( comboBox.isEditable() ) {
            addEditor();
        }
        arrowButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        listBox.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
        comboBox.add( currentValuePane );       
    }
    
    /**
     * Creates the popup portion of the combo box.
     *
     * @return an instance of <code>ComboPopup</code>
     * @see ComboPopup
     */
    @Override
    protected ComboPopup createPopup() {
        return new MyBasicComboPopup( comboBox );
    }
    
    @Override 
    protected JButton createArrowButton() {
        return new MyBasicArrowButton(
                BasicArrowButton.SOUTH,
                basicArrowButton_Background, //UIManager.getColor("ScrollBar.thumb"),             //Background
                basicArrowButton_Background, //UIManager.getColor("ScrollBar.thumbShadow"),
                basicArrowButton_Foreground, //UIManager.getColor("ScrollBar.thumbDarkShadow"),   //Rand unten Rechts , Pfeilfarbe
                basicArrowButton_Background); //UIManager.getColor("ScrollBar.thumbHighlight"));   
    }    
}
