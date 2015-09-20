/**
 * Copyright (c) 2010-2015 Bryan Beck.
 * All rights reserved.
 * 
 * This project is licensed under LGPL v2.1.
 * See jMovieManager-license.txt for details.
 * 
 */
package jMovieManager.swing.gui.other;

import javax.swing.table.DefaultTableModel;

/**
 * DefaultTableModel with an JLabel in the first column
 * @author Bryan Beck
 * @since 31.10.2012
 * 
 */
public class ImageTableModel extends DefaultTableModel{	 
    Object[] col = {"Bild", "Name", "Jahr",  "Art"};

    /**
     * creates a new ImageTableModel 
     */
    public ImageTableModel (){
        //Adding columns
        for(Object c: col){
            this.addColumn(c);
        }
    }

    @Override
    public Class getColumnClass(int columnIndex) {
        if(columnIndex == 0){
            return getValueAt(0, columnIndex).getClass();
        } 
        else{
            return super.getColumnClass(columnIndex);
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
}
