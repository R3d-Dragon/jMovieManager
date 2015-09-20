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
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Own TableCellRenderer to display JLabels
 * 
 * @author Bryan Beck
 * @since 31.10.2012
 */
public class ImageTableCellRenderer extends DefaultTableCellRenderer {    
     
    /**
     * @see DefaultTableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int) 
     */
     @Override
     public Component getTableCellRendererComponent(
             JTable table, Object value, boolean isSelected,
             boolean hasFocus, int row, int column){
         if(column == 0 && value != null && value instanceof String && value.toString().startsWith("http")){
//         if(value instanceof JLabel){
             JLabel label = new JLabel();
//             JLabel label = (JLabel)value;
             //to make label foreground n background visible you need to
             // setOpaque -> true
             label.setOpaque(true);
             fillColor(table,label,isSelected);
             try{
                 label.setIcon(new ImageIcon(new URL(value.toString())));
             }catch( Exception ex){}
                 //label.setIcon(PictureManager.getScaledImage(value.toString(),PictureManager.tmdb_thumbnail_widht, PictureManager.tmdb_thumbnail_height));          
      
            label.setHorizontalAlignment(SwingConstants.CENTER);
             return label;
         } else{
             JLabel renderedLabel = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
             renderedLabel.setHorizontalAlignment(SwingConstants.CENTER);
             return renderedLabel;
//             return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
         }
     }
     
     /**
      * highlight the label, if it is selected
      */
    public void fillColor(JTable t,JLabel l,boolean isSelected ){
        //setting the background and foreground when JLabel is selected
        if(isSelected){
            l.setBackground(t.getSelectionBackground());
            l.setForeground(t.getSelectionForeground());
        }

        else{
            l.setBackground(t.getBackground());
            l.setForeground(t.getForeground());
        }
    }
}
