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
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicMenuUI;
import jMovieManager.swing.gui.ColorInterface;

/**
 *
 * @author Bryan Beck
 * @since 11.10.2011
 * 
 */
public class MyBasicMenuUI extends BasicMenuUI{  
    
     /**
     * creates the UI (look and feel) for the component
     * 
     * @param b the component
     * @return the ComponentUI
     */    
    public static ComponentUI createUI(JComponent e) {
        return new MyBasicMenuUI();
    }  
    
   @Override
    protected void installDefaults() {
        super.installDefaults();
        menuItem.setRolloverEnabled(true);
        
        //Setzte neuen Border
        //((JMenu)menuItem).getPopupMenu().setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        ((JMenu)menuItem).getPopupMenu().setBorder(BorderFactory.createLineBorder(ColorInterface.popupMenuBorder));
       // menuItem.setBorder(null);
        menuItem.setBorderPainted(false);
    }

    /**
     * Draws the background of the menu.
     * @since 1.4
     */
    @Override
    protected void paintBackground(Graphics g, JMenuItem menuItem, Color bgColor) {
        if(menuItem.isOpaque()){
            super.paintBackground(g, menuItem, bgColor);
        }
        JMenu menu = (JMenu)menuItem;
        ButtonModel model = menu.getModel();       
        
        //Wenn Mouseover oder selektiert + Menu
        if (model.isRollover() || (model.isArmed() || (menuItem instanceof JMenu && model.isSelected()))){
            MyBasicMenuItemUI.drawBackground(g, menuItem, bgColor);
//            Color oldColor = g.getColor();
//            int menuWidth = menu.getWidth();
//            int menuHeight = menu.getHeight();
//            int arc = 5;
//            Graphics2D g2 = (Graphics2D)g;       
//            
//            g2.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//            RoundRectangle2D.Float rect = new RoundRectangle2D.Float(
//                    2,
//                    2,
//                    menuWidth-4,
//                    (menuHeight-4)/2,
//                    arc,
//                    arc
//            );
//          g2.setPaint(new Color(166, 166, 166, 100));
//    //      Area area = new Area(new Rectangle2D.Float(2,2,rect.getWidth(),(rect.getHeight()-4)/2));
//    //      area.exclusiveOr(new Area(rect));
//    //      g2.setClip(area);
//          g2.fill(rect);
//      
//      
//            g2.setPaint(new Color(166, 166, 166, 40));      
//          rect.setRoundRect(2, (menuHeight-4)/2, menuWidth-4, menuHeight-4, arc, arc);
//            g2.fill(rect);
//      
//        //  arc = 10;
//
//          g2.setPaint(oldColor);

    //  g2.setPaint(new Color(190, 190, 190));
   //   g2.drawRoundRect(2, 2, menuWidth-4, menuHeight-4, arc, arc);
     // g2.setClip(null);               
            
        }
//        else if (model.isArmed() || (menuItem instanceof JMenu &&
//                                     model.isSelected())) {
//        //  g2.setPaint(new Color(190, 190, 190));
//       //   g2.drawRoundRect(2, 2, menuWidth-4, menuHeight-4, arc, arc);
//         // g2.setClip(null);    
//        }

        
//        
//                        g.setColor(new Color(255, 255, 255, 130));
////                g.drawLine(0,0, menuWidth - 1,0);
////                g.drawLine(0,0, 0,menuHeight - 2);
////
////                g.setColor(Color.RED);
////                g.drawLine(menuWidth - 1,0, menuWidth - 1,menuHeight - 2);
////                g.drawLine(0,menuHeight - 2, menuWidth - 1,menuHeight - 2);
//        
////        g.setColor(Color.WHITE);
//        g.drawRoundRect(1, 1, menuWidth-3, menuHeight-3, arc, arc);
////        g.setColor(thumbColor);
//        g.fillRoundRect(1, 1, menuWidth-3, menuHeight-3, arc, arc);
//
//        g.setColor(thumbHighlightColor);
//        g.drawLine(1, 1, 1, h-2);
//        g.drawLine(2, 1, w-3, 1);
//
//        g.setColor(thumbLightShadowColor);
//        g.drawLine(2, h-2, w-2, h-2);
//        g.drawLine(w-2, 1, w-2, h-3);
//
//        g.translate(-thumbBounds.x, -thumbBounds.y);        
        
        
        
        
        
        
        
        
     //       super.paintBackground(g, menu, bgColor);
//            return;
//        }

//        Color oldColor = g.getColor();
//        int menuWidth = menu.getWidth();
//        int menuHeight = menu.getHeight();
//
//        UIDefaults table = UIManager.getLookAndFeelDefaults();
//        Color highlight = table.getColor("controlLtHighlight");
//        Color shadow = table.getColor("controlShadow");
//
//        g.setColor(menu.getBackground());
//        g.fillRect(0,0, menuWidth, menuHeight);
//
//        
//        if (menu.isOpaque()) {
//            if (model.isArmed() || model.isSelected()) {
//                // Draw a lowered bevel border
//                g.setColor(shadow);
//                g.drawLine(0,0, menuWidth - 1,0);
//                g.drawLine(0,0, 0,menuHeight - 2);
//
//                g.setColor(highlight);
//                g.drawLine(menuWidth - 1,0, menuWidth - 1,menuHeight - 2);
//                g.drawLine(0,menuHeight - 2, menuWidth - 1,menuHeight - 2);
//            } else if (model.isRollover() && model.isEnabled()) {
//                // Only paint rollover if no other menu on menubar is selected
//                boolean otherMenuSelected = false;
//                MenuElement[] menus = ((JMenuBar)menu.getParent()).getSubElements();
//                for (int i = 0; i < menus.length; i++) {
//                    if (((JMenuItem)menus[i]).isSelected()) {
//                        otherMenuSelected = true;
//                        break;
//                    }
//                }
//                if (!otherMenuSelected) {
////                    if (XPStyle.getXP() != null) {
//                        g.setColor(selectionBackground); // Uses protected field.
//                        g.fillRect(0, 0, menuWidth, menuHeight);
////                    } else {
//                        // Draw a raised bevel border
//                        g.setColor(highlight);
//                        g.drawLine(0,0, menuWidth - 1,0);
//                        g.drawLine(0,0, 0,menuHeight - 2);
//
//                        g.setColor(shadow);
//                        g.drawLine(menuWidth - 1,0, menuWidth - 1,menuHeight - 2);
//                        g.drawLine(0,menuHeight - 2, menuWidth - 1,menuHeight - 2);
////                    }
//                }
//            }
//        }
//        g.setColor(oldColor);
    }
    
    @Override
    protected MouseInputListener createMouseInputListener(JComponent c) {
        return new MyMouseInputHandler();
    }
        
   /**
     * This class implements a mouse handler that sets the rollover flag to
     * true when the mouse enters the menu and false when it exits.
     * @since 1.4
     */
    protected class MyMouseInputHandler extends BasicMenuUI.MouseInputHandler {
        @Override
        public void mouseEntered(MouseEvent evt) {
            super.mouseEntered(evt);

            JMenu menu = (JMenu)evt.getSource();
            if (menu.isTopLevelMenu() && menu.isRolloverEnabled()) {
                menu.getModel().setRollover(true);
                menuItem.repaint();
            }
        }

        @Override
        public void mouseExited(MouseEvent evt) {
            super.mouseExited(evt);

            JMenu menu = (JMenu)evt.getSource();
            ButtonModel model = menu.getModel();
            if (menu.isRolloverEnabled()) {
                model.setRollover(false);
                menuItem.repaint();
            }
        }
    }
}

