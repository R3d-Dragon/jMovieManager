/**
 * Copyright (c) 2010-2015 Bryan Beck.
 * All rights reserved.
 * 
 * This project is licensed under LGPL v2.1.
 * See jMovieManager-license.txt for details.
 * 
 */
package jMovieManager.swing.gui.components.ui;

import java.awt.Container;
import java.awt.Cursor;
import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicOptionPaneUI;

/**
 *
 * @author Bryan Beck
 * @since 10.11.2011
 */
public class MyOptionPaneUI extends BasicOptionPaneUI {
    
    /**
     * creates the UI (look and feel) for the component
     * 
     * @param b the component
     * @return the ComponentUI
     */
    public static ComponentUI createUI(JComponent x) {
        return new MyOptionPaneUI();
    } 
    
    /**
     * Creates the appropriate object to represent each of the objects in
     * <code>buttons</code> and adds it to <code>container</code>. This
     * differs from addMessageComponents in that it will recurse on
     * <code>buttons</code> and that if button is not a Component
     * it will create an instance of JButton.
     */
    @Override
    protected void addButtonComponents(Container container, Object[] buttons,
                                 int initialIndex) {
        super.addButtonComponents(container, buttons, initialIndex);
        
        for(Object buttonObject: container.getComponents()){
            if(buttonObject instanceof AbstractButton){
                AbstractButton button = (AbstractButton) buttonObject;
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
                button.setUI((MyBasicButtonUI)MyBasicButtonUI.createUI(button));
            }
        }        
    }
}
