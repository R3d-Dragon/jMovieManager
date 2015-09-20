/**
 * Copyright (c) 2010-2015 Bryan Beck.
 * All rights reserved.
 * 
 * This project is licensed under LGPL v2.1.
 * See jMovieManager-license.txt for details.
 * 
 */
package jMovieManager.swing.gui.components.ui;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

/**
 *
 * @author Bryan Beck
 * @since 25.01.2012
 */
public class MyBasicPopupMenuSeperatorUI extends MyBasicSeparatorUI{
    
     /**
     * creates the UI (look and feel) for the component
     * 
     * @param b the component
     * @return the ComponentUI
     */ 
    public static ComponentUI createUI( JComponent c ){
        return new MyBasicPopupMenuSeperatorUI();
    }
    
    @Override
    public void paint( Graphics g, JComponent c )
    {
        Dimension s = c.getSize();

        g.setColor( c.getForeground() );
        g.drawLine( 0, 0, s.width, 0 );

        g.setColor( c.getBackground() );
        g.drawLine( 0, 1, s.width, 1 );
    }    
}
