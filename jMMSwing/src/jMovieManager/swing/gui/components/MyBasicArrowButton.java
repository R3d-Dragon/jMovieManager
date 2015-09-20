/**
 * Copyright (c) 2010-2015 Bryan Beck.
 * All rights reserved.
 * 
 * This project is licensed under LGPL v2.1.
 * See jMovieManager-license.txt for details.
 * 
 */
package jMovieManager.swing.gui.components;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.UIManager;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicArrowButton;

/**
 * Eigener Scroll Button, welcher größere Pfeile hat und keinen black drop shadow zeichnet
 * 
 * @author Bryan Beck
 * @since 09.10.2011
 */
public class MyBasicArrowButton extends BasicArrowButton{
    
        private Color shadow;
        private Color darkShadow;
        private Color highlight;
        /**
         * Creates a {@code BasicArrowButton} whose arrow
         * is drawn in the specified direction and with the specified
         * colors.
         *
         * @param direction the direction of the arrow; one of
         *        {@code SwingConstants.NORTH}, {@code SwingConstants.SOUTH},
         *        {@code SwingConstants.EAST} or {@code SwingConstants.WEST}
         * @param background the background color of the button
         * @param shadow the color of the shadow
         * @param darkShadow the color of the dark shadow
         * @param highlight the color of the highlight
         * @since 1.4
         */
        public MyBasicArrowButton(int direction, Color background, Color shadow,
                         Color darkShadow, Color highlight) {
            super(direction, background, shadow, darkShadow, highlight);
            
            this.shadow = shadow;
            this.darkShadow = darkShadow;
            this.highlight = highlight;
            //this.setOpaque(true);
            //this.setBorderPainted(true);
        }

        /**
         * Creates a {@code BasicArrowButton} whose arrow
         * is drawn in the specified direction.
         *
         * @param direction the direction of the arrow; one of
         *        {@code SwingConstants.NORTH}, {@code SwingConstants.SOUTH},
         *        {@code SwingConstants.EAST} or {@code SwingConstants.WEST}
         */
        public MyBasicArrowButton(int direction) {
            this(direction, UIManager.getColor("control"), UIManager.getColor("controlShadow"),
                 UIManager.getColor("controlDkShadow"), UIManager.getColor("controlLtHighlight"));
        }    
        
        @Override
        public void paint(Graphics g) {
            Color origColor;
            boolean isPressed, isEnabled;
            int w, h, size;

            w = getSize().width;
            h = getSize().height;
            origColor = g.getColor();
            isPressed = getModel().isPressed();
            isEnabled = isEnabled();

            g.setColor(getBackground());
            g.fillRect(1, 1, w-2, h-2);

            /// Draw the proper Border
            if (getBorder() != null && !(getBorder() instanceof UIResource)) {
                paintBorder(g);
            } else if (isPressed) {
                g.setColor(shadow);
                g.drawRect(0, 0, w-1, h-1);
            } else {
                // Using the background color set above
                g.drawLine(0, 0, 0, h-1);
                g.drawLine(1, 0, w-2, 0);

                g.setColor(highlight);    // inner 3D border
                g.drawLine(1, 1, 1, h-3);
                g.drawLine(2, 1, w-3, 1);

                g.setColor(shadow);       // inner 3D border
                g.drawLine(1, h-2, w-2, h-2);
                g.drawLine(w-2, 1, w-2, h-3);

//                g.setColor(darkShadow);     // black drop shadow  __|
                g.drawLine(0, h-1, w-1, h-1);
                g.drawLine(w-1, h-1, w-1, 0);
            }

            // If there's no room to draw arrow, bail
            if(h < 5 || w < 5)      {
                g.setColor(origColor);
                return;
            }

            if (isPressed) {
                g.translate(1, 1);
            }

            // Draw the arrow
            size = Math.min((h - 4) / 3, (w - 4) / 3);
            size = Math.max(size, 4);                               //Customized
            paintTriangle(g, (w - size) / 2, (h - size) / 2,
                                size, direction, isEnabled);

            // Reset the Graphics back to it's original settings
            if (isPressed) {
                g.translate(-1, -1);
            }
            g.setColor(origColor);
        }        
}
