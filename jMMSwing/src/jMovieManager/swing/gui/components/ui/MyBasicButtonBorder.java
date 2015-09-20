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
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.border.AbstractBorder;
import javax.swing.plaf.UIResource;

/**
 *
 * @author Bryan Beck
 * @since 01.11.2011
 */
public class MyBasicButtonBorder extends AbstractBorder implements UIResource {
        protected static Insets borderInsets = new Insets( 3, 3, 3, 3 );
        private Color controlDkShadow = new Color(122, 138, 153);
        private Color primaryControl = new Color(184, 207, 229);
        private Color inactiveControlTextColor = new Color(153, 153, 153);

    @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            if (!(c instanceof AbstractButton)) {
                return;
            }
            
            AbstractButton button = (AbstractButton)c;
            ButtonModel model = button.getModel();

            g.translate(x, y);

            if (model.isEnabled()) {
                boolean pressed = model.isPressed();
//                boolean armed = model.isArmed();

                if ((c instanceof JButton) && ((JButton)c).isDefaultButton()) {
                    g.setColor(controlDkShadow);
                    //[r=122,g=138,b=153]
                    g.drawRect(0, 0, w - 1, h - 1);
                    g.drawRect(1, 1, w - 3, h - 3);
                }
                else if (pressed) {
                    g.setColor(controlDkShadow);
                    g.fillRect(0, 0, w, 2);
                    g.fillRect(0, 2, 2, h - 2);
                    g.fillRect(w - 1, 1, 1, h - 1);
                    g.fillRect(1, h - 1, w - 2, 1);
                }
                else if (button.isRolloverEnabled() && model.isRollover()){
                    //g.setColor(ColorInterface.selectedListText);
//                    Graphics2D g2d = (Graphics2D) g;
//                    g2d.setColor(primaryControl);
//                    g2d.drawRoundRect(0, 0, w - 1, h - 1, 5, 5);
//                    g2d.drawRoundRect(2, 2, w - 5, h - 5, 5, 5);
                    
                    g.setColor(primaryControl);
                    g.drawRect(0, 0, w - 1, h - 1);
                    g.drawRect(2, 2, w - 5, h - 5);
                    
                    g.setColor(controlDkShadow);
                    g.drawRect(1, 1, w - 3, h - 3);
                }
                else {
                    g.setColor(controlDkShadow);
                    g.drawRect(0, 0, w - 1, h - 1);
                }
            }
            else {  //disabled state
                g.setColor(inactiveControlTextColor);
                g.drawRect(0, 0, w - 1, h - 1);
                if ((c instanceof JButton) && ((JButton)c).isDefaultButton()) {
                    g.drawRect(1, 1, w - 3, h - 3);
                }
            }            
        }

        @Override
        public Insets getBorderInsets(Component c, Insets newInsets) {
            newInsets.set(3, 3, 3, 3);
            return newInsets;
        }
}
