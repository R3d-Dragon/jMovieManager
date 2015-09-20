/**
 * Copyright (c) 2010-2015 Bryan Beck.
 * All rights reserved.
 * 
 * This project is licensed under LGPL v2.1.
 * See jMovieManager-license.txt for details.
 * 
 */
package jMovieManager.swing.gui.components.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.MenuElement;
import javax.swing.MenuSelectionManager;
import javax.swing.plaf.ComponentUI;
import jMovieManager.swing.gui.other.MyIconFactory;

/**
 *
 * @author Bryan Beck
 * @since 28.10.2012
 */
public class MyBasicCheckBoxMenuItemUI extends MyBasicMenuItemUI {
    
    /**
     * creates the UI (look and feel) for the component
     * 
     * @param b the component
     * @return the ComponentUI
     */
    public static ComponentUI createUI(JComponent c) {
        return new MyBasicCheckBoxMenuItemUI();
    }

    @Override
    protected String getPropertyPrefix() {
	return "CheckBoxMenuItem";
    }
    
    /**
     * Install the components of the menuItem and sets the button textures
     * @param menuItem 
     */
    @Override
    protected void installComponents(JMenuItem menuItem){
        super.installComponents(menuItem);
//        menuItem.setIcon(MyIconFactory.checkBox_default);
        menuItem.setDisabledIcon(MyIconFactory.checkBox_disabled);
        menuItem.setDisabledSelectedIcon(MyIconFactory.checkBox_disabled_selected);
        menuItem.setSelectedIcon(MyIconFactory.checkBox_selected);
        menuItem.setRolloverIcon(MyIconFactory.checkBox_highlight);
        menuItem.setRolloverSelectedIcon(MyIconFactory.checkBox_highlight_selected);
    }
   
    private void paintIcon(Graphics g, JMenuItem menuItem, Color holdc) {
//         if (menuItem.getIcon() != null) {
             Icon icon;
             ButtonModel model = menuItem.getModel();
             if (!model.isEnabled()) {
                 if(model.isSelected()){
                     icon = (Icon) menuItem.getDisabledSelectedIcon();
                 }else{
                     icon = (Icon) menuItem.getDisabledIcon();
                 }
             }
             else if(model.isRollover()){
                 if(model.isSelected()){
                     icon = (Icon) menuItem.getRolloverSelectedIcon();
                 }else{
                     icon = (Icon) menuItem.getRolloverIcon();
                 }
             }
             else if(model.isSelected()){
                 icon = (Icon) menuItem.getSelectedIcon();
//             }
//             else if (model.isPressed() && model.isArmed()) {
//                 icon = (Icon) menuItem.getPressedIcon();
//                 if (icon == null) {
//                     // Use default icon
//                     icon = (Icon) menuItem.getIcon();
//                 }
             } else {
                 icon = (Icon) menuItem.getIcon();
             }
 
             if (icon != null) {
                 icon.paintIcon(menuItem, g, 6, 2);
//                 icon.paintIcon(menuItem, g, icon.getIconWidth(), icon.getIconHeight());
                 g.setColor(holdc);
             }
//         }
     }
    
    @Override
    protected void paintMenuItem(Graphics g, JComponent c,
                                     Icon checkIcon, Icon arrowIcon,
                                     Color background, Color foreground,
                                     int defaultTextIconGap) {
            super.paintMenuItem(g, c, checkIcon, arrowIcon, background, foreground, defaultTextIconGap);
            Color holdc = g.getColor(); 
            paintIcon(g, (JMenuItem)c, holdc);
    }    
    
    public void processMouseEvent(JMenuItem item,MouseEvent e,MenuElement path[],MenuSelectionManager manager) {
        Point p = e.getPoint();
        if(p.x >= 0 && p.x < item.getWidth() &&
           p.y >= 0 && p.y < item.getHeight()) {
            if(e.getID() == MouseEvent.MOUSE_RELEASED) {
                manager.clearSelectedPath();
                item.doClick(0);
            } else{
                manager.setSelectedPath(path);
            }
        } else if(item.getModel().isArmed()) {
            MenuElement newPath[] = new MenuElement[path.length-1];
            int i,c;
            for(i=0,c=path.length-1;i<c;i++){
                newPath[i] = path[i];
            }
            manager.setSelectedPath(newPath);
        }
    }
}
