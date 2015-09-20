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
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;
import jmm.data.Episode;
import jmm.utils.Settings;

/**
 * Own TreeCellRenderer class to display, if a episode is watched or not 
 * 
 * @author Bryan Beck
 * @since 13.02.2013
 */
public class MyTreeCellRenderer extends DefaultTreeCellRenderer implements TreeCellRenderer{
    
    /**
     * Constructor
     */
    public MyTreeCellRenderer(){
        super();                
    }
    /**
     * @see DefaultTreeCellRenderer#getTreeCellRendererComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int, boolean) 
     */
    @Override
    public Component getTreeCellRendererComponent(JTree tree,
                                                Object value,                                           
                                                boolean selected, 
                                                boolean expanded,
                                                boolean leaf, 
                                                int row, 
                                                boolean hasFocus){
        JLabel label = (JLabel)super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);                 
        
        if(Settings.getInstance().isDisplayWatchedIcon()){
            Object userObject = ((DefaultMutableTreeNode)value).getUserObject();
            if((userObject != null) && (userObject instanceof Episode) && (leaf) && (((Episode)userObject).isWatched())){
                label.setIcon(MyIconFactory.watchedIcon);
            }
        }

        return label;
    }
}
