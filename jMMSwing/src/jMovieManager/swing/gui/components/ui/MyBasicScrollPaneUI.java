/**
 * Copyright (c) 2010-2015 Bryan Beck.
 * All rights reserved.
 * 
 * This project is licensed under LGPL v2.1.
 * See jMovieManager-license.txt for details.
 * 
 */
package jMovieManager.swing.gui.components.ui;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicScrollPaneUI;

/**
 *
 * @author Bryan Beck
 * @since 13.02.2013
 */
public class MyBasicScrollPaneUI extends BasicScrollPaneUI{
    
    /**
     * creates the UI (look and feel) for the component
     * 
     * @param b the component
     * @return the ComponentUI
     */    
    public static ComponentUI createUI(JComponent c) {
        return new MyBasicScrollPaneUI();
    }
}
