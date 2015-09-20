/**
 * Copyright (c) 2010-2015 Bryan Beck.
 * All rights reserved.
 * 
 * This project is licensed under LGPL v2.1.
 * See jMovieManager-license.txt for details.
 * 
 */
package jMovieManager.swing.gui.components.ui;

import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.plaf.basic.BasicComboPopup;

/**
 * Klasse für das Popupmenu von ComboBoxen, 
 * damit die Vertical und Horizontal Scrollbar ebenfalls ein angepasstes UI besitzen
 * 
 * @author Bryan Beck
 * @since 07.11.2011
 */
public class MyBasicComboPopup extends BasicComboPopup{
    
    /**
     * Konstruktor
     * @param combo 
     */
    public MyBasicComboPopup( JComboBox combo ) {
        super(combo);
    }
    
    /**
     * Creates the scroll pane which houses the scrollable list.
     */
    @Override
    protected JScrollPane createScroller() {
        JScrollPane sp = new JScrollPane( list,
                                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );
        sp.setHorizontalScrollBar(null);
        sp.getVerticalScrollBar().setUI((MyBasicScrollBarUI)MyBasicScrollBarUI.createUI(sp));   //Modified
        return sp;
    }

//    /**
//     * Configures the scrollable portion which holds the list within
//     * the combo box popup. This method is called when the UI class
//     * is created.
//     */
//    @Override
//    protected void configureScroller() {
//        scroller.setFocusable( false );
//        scroller.getVerticalScrollBar().setFocusable( false );
//        scroller.setBorder( null );
//    }    
}
