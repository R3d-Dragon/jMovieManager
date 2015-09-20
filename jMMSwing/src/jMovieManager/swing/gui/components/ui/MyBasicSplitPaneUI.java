/**
 * Copyright (c) 2010-2015 Bryan Beck.
 * All rights reserved.
 * 
 * This project is licensed under LGPL v2.1.
 * See jMovieManager-license.txt for details.
 * 
 */
package jMovieManager.swing.gui.components.ui;

import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

/**
 *
 * @author Bryan Beck
 * @since 10.10.2011
 */
public class MyBasicSplitPaneUI extends BasicSplitPaneUI {
    
    /**
     * creates the UI (look and feel) for the component
     * 
     * @param b the component
     * @return the ComponentUI
     */
    public static ComponentUI createUI(JComponent x) {
        return new MyBasicSplitPaneUI();
    }
        
     /**
     * Creates the default divider.
     */
    @Override
    public BasicSplitPaneDivider createDefaultDivider() {
        return new BasicSplitPaneDivider(this){
            /**
             * Paints the divider.</br> 
             * <b>Zeichne den Divier nicht.</b>
             */
            @Override
            public void paint(Graphics g) {
//              super.paint(g);
//
//              // Paint the border.
//              Border   border = getBorder();
//
//              if (border != null) {
//                  Dimension     size = getSize();
//
//                  border.paintBorder(this, g, 0, 0, size.width, size.height);
//              }
            }            
        };
    }
}
