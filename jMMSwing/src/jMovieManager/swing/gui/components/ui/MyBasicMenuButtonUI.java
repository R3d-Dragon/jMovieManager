/**
 * Copyright (c) 2010-2015 Bryan Beck.
 * All rights reserved.
 * 
 * This project is licensed under LGPL v2.1.
 * See jMovieManager-license.txt for details.
 * 
 */
package jMovieManager.swing.gui.components.ui;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicButtonUI;

/**
 *
 * @author Bryan Beck
 * @since 14.10.2011
 */
public class MyBasicMenuButtonUI extends BasicButtonUI{
    
   /**
     * creates the UI (look and feel) for the component
     * 
     * @param b the component
     * @return the ComponentUI
     */
    public static ComponentUI createUI(JComponent c) {
        return new MyBasicMenuButtonUI();
    }   
    
    // ********************************
    //          Paint Methods
    // ********************************
    
    @Override
    protected void paintIcon(Graphics g, JComponent c, Rectangle iconRect){
            AbstractButton b = (AbstractButton) c;
            ButtonModel model = b.getModel();
            Icon icon = b.getIcon();
            Icon tmpIcon = null;

            if(icon == null) {
               return;
            }

            Icon selectedIcon = null;

            /* the fallback icon should be based on the selected state */
            if (model.isSelected()) {
                selectedIcon = b.getSelectedIcon();
                if (selectedIcon != null) {
                    icon = selectedIcon;
                }
                else{
                    this.drawMouseoverEffect(g, c, iconRect); 
                }
            }

            if(!model.isEnabled()) {
                if(model.isSelected()) {
                   tmpIcon = b.getDisabledSelectedIcon();
                   if (tmpIcon == null) {
                       tmpIcon = selectedIcon;
                   }
                }

                if (tmpIcon == null) {
                    tmpIcon = b.getDisabledIcon();
                }
            } else if(model.isPressed() && model.isArmed()) {
                tmpIcon = b.getPressedIcon();
                if(tmpIcon != null) {
                    // revert back to 0 offset
                    clearTextShiftOffset();
                }
            }
//            else if(b.isRolloverEnabled() && model.isRollover()) {
//                if(model.isSelected()) {
//                   tmpIcon = b.getRolloverSelectedIcon();
//                   if (tmpIcon == null) {
//                       tmpIcon = selectedIcon;
//                   }
//                }
//
//                if (tmpIcon == null) {
//                    tmpIcon = b.getRolloverIcon();
//                }
//            }

            if(tmpIcon != null) {
                icon = tmpIcon;
            }

            if(model.isPressed() && model.isArmed()) {
                icon.paintIcon(c, g, iconRect.x + getTextShiftOffset(),
                        iconRect.y + getTextShiftOffset());
            } else {
                icon.paintIcon(c, g, iconRect.x, iconRect.y);
            }
            
            if(b.isRolloverEnabled() && model.isRollover()){
                this.drawMouseoverEffect(g, c, iconRect);                
            }
    }

    /**
     * Zeichnet den Mouseover Effekt eines Buttons
     **/
    public static void drawMouseoverEffect(Graphics g, JComponent component, Rectangle iconRect){
        Color oldColor = g.getColor();
        int width = component.getWidth();
        int height = component.getHeight();
        int arc = 5;
        Graphics2D g2 = (Graphics2D)g;       

        g2.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        RoundRectangle2D.Float rect = new RoundRectangle2D.Float(
                0,
                0,
                width,
                height,
                arc,
                arc
        );

//      Area area = new Area(new Rectangle2D.Float(2,2,rect.getWidth(),(rect.getHeight()-4)/2));
//      area.exclusiveOr(new Area(rect));
//      g2.setClip(area);
        g2.setPaint(new Color(80, 80, 80));       //Dunkel Grau  
        g2.drawRoundRect(0, 0, width, height, arc, arc);
        g2.setPaint(new Color(222, 222, 222, 40));         //Hell Grau
        g2.fill(rect);
        //Setzte alte Farbe
        g2.setPaint(oldColor);          
    }    
}
