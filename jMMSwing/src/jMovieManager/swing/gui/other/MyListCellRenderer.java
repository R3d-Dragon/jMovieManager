/**
 * Copyright (c) 2010-2015 Bryan Beck.
 * All rights reserved.
 * 
 * This project is licensed under LGPL v2.1.
 * See jMovieManager-license.txt for details.
 * 
 */
package jMovieManager.swing.gui.other;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import jmm.data.Movie;
import jmm.utils.Settings;

/**
 * Eigene ListCellRenderer Klasse, um Elemente in einer jList ein Text + Icon zuweisen zu können
 * 
 * @author Bryan Beck
 * @since 24.02.2012
 */
public class MyListCellRenderer extends DefaultListCellRenderer implements ListCellRenderer<Object>{
//public class MyListCellRenderer extends DefaultListCellRenderer implements ListCellRenderer{
    
    /**
     * Constructor
     */
    public MyListCellRenderer(){
        super();                
    }
    
    @Override
    public Component getListCellRendererComponent(JList list,
                                                Object value,
                                                int index,
                                                boolean isSelected,
                                                boolean cellHasFocus){
        JLabel label = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        
        if(Settings.getInstance().isDisplayWatchedIcon()){
            // JLabel das Icon aus MyListItem zuweisen
            if((value instanceof Movie) && ((Movie)value).isWatched()){
                label.setIcon(MyIconFactory.watchedIcon);
            }
            else{
    //            label.setIcon(watchedEmptyIcon);
                label.setIcon(MyIconFactory.watchedEmptyIcon);
            }
        }
        
      //  label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
      //  label.addMouseListener(new MouseAdapter() {
            
      //      @Override
      //      public void mouseClicked(MouseEvent e) {
      //          System.out.println("UPDATE WATCHED STATE");
      //      }
      //  });
        

        return label;
    }
}