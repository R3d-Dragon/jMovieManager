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
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import javax.swing.ButtonModel;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicMenuItemUI;

/**
 *
 * @author Bryan Beck
 * @since 10.12.2011
 */
public class MyBasicMenuItemUI extends BasicMenuItemUI {
    
     /**
     * creates the UI (look and feel) for the component
     * 
     * @param b the component
     * @return the ComponentUI
     */
    public static ComponentUI createUI(JComponent c) {
        return new MyBasicMenuItemUI();
    }        

   @Override
    protected void installDefaults() {
        super.installDefaults();
        menuItem.setRolloverEnabled(true);
        //Entferne Border
       // menuItem.setBorder(null);
        menuItem.setBorderPainted(false);
    }

    /**
     * Draws the background of the menu.
     * @since 1.4
     */
    @Override
    protected void paintBackground(Graphics g, JMenuItem menuItem, Color bgColor) {
        super.paintBackground(g, menuItem, bgColor);
        ButtonModel model = menuItem.getModel();       
        
        //Wenn Mouseover oder selektiert + Menu
        if (model.isRollover() || (model.isArmed() || (menuItem instanceof JMenu && model.isSelected()))){
            this.drawBackground(g, menuItem, bgColor);
        }
    }
    
    public static void drawBackground(Graphics g, JMenuItem menuItem, Color bgColor){
        Color oldColor = g.getColor();
        int menuWidth = menuItem.getWidth();
        int menuHeight = menuItem.getHeight();
        int arc = 5;
        Graphics2D g2 = (Graphics2D)g;       

        g2.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        RoundRectangle2D.Float rect = new RoundRectangle2D.Float(
                2,
                2,
                menuWidth-4,
                menuHeight-4,
                arc,
                arc
        );
//        g2.setPaint(new Color(243, 233, 164, 240));       //Gelb
        g2.setPaint(new Color(140, 140, 140));         //Grau
//      Area area = new Area(new Rectangle2D.Float(2,2,rect.getWidth(),(rect.getHeight()-4)/2));
//      area.exclusiveOr(new Area(rect));
//      g2.setClip(area);
        g2.drawRoundRect(2, 2, menuWidth-4, menuHeight-4, arc, arc);
        g2.setPaint(new Color(180, 180, 180, 60));         //Hell Grau
        g2.fill(rect);
        
        g2.setPaint(new Color(180, 180, 180, 30));       //Gelb
        rect.setRoundRect(2, 2, menuWidth-4, (menuHeight-4)/2, arc, arc);
        g2.fill(rect);
//
//        g2.setPaint(new Color(218, 152, 10, 240));       //Gelb
//        g2.setPaint(new Color(166, 166, 166, 60));          //Grau
//        rect.setRoundRect(2, (menuHeight-4)/2, menuWidth-4, menuHeight-4, arc, arc);
//        g2.fill(rect);
        //Setzte alte Farbe
        g2.setPaint(oldColor);          
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
    protected class MyMouseInputHandler extends BasicMenuItemUI.MouseInputHandler {
        @Override
        public void mouseEntered(MouseEvent evt) {
            super.mouseEntered(evt);

            JMenuItem menu = (JMenuItem)evt.getSource();
            if (menu.isRolloverEnabled()) {
                menu.getModel().setRollover(true);
                menuItem.repaint();
            }
        }

        @Override
        public void mouseExited(MouseEvent evt) {
            super.mouseExited(evt);

            JMenuItem menu = (JMenuItem)evt.getSource();
            ButtonModel model = menu.getModel();
            if (menu.isRolloverEnabled()) {
                model.setRollover(false);
                menuItem.repaint();
            }
        }
    } 
}
