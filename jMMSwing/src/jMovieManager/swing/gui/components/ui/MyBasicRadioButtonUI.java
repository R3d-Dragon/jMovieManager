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
import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicGraphicsUtils;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.plaf.basic.BasicRadioButtonUI;
import javax.swing.text.View;
import jMovieManager.swing.gui.other.MyIconFactory;

/**
 *
 * @author Bryan Beck
 * @since 03.11.2011
 */
public class MyBasicRadioButtonUI extends BasicRadioButtonUI{

    /**
     * creates the UI (look and feel) for the component
     * 
     * @param b the component
     * @return the ComponentUI
     */
    public static ComponentUI createUI(JComponent b) {
        return new MyBasicRadioButtonUI();
    }
    
     // ********************************
    //        Install PLAF
    // ********************************
    @Override
    protected void installDefaults(AbstractButton b){
        super.installDefaults(b);
        b.setIcon(MyIconFactory.radioButton_default);
        b.setDisabledIcon(MyIconFactory.radioButton_disabled);
        b.setDisabledSelectedIcon(MyIconFactory.radioButton_disabled_selected);
        b.setSelectedIcon(MyIconFactory.radioButton_selected);
        b.setRolloverIcon(MyIconFactory.radioButton_highlight);
        b.setRolloverSelectedIcon(MyIconFactory.radioButton_highlight_selected);
    }

    // ********************************
    //        Uninstall PLAF
    // ********************************
    @Override
    protected void uninstallDefaults(AbstractButton b){
        super.uninstallDefaults(b);
        b.setIcon(null);
        b.setDisabledIcon(null);
        b.setDisabledSelectedIcon(null);
        b.setSelectedIcon(null);
        b.setRolloverIcon(null);
        b.setRolloverSelectedIcon(null);        
    }
        
    /* These Dimensions/Rectangles are allocated once for all
     * RadioButtonUI.paint() calls.  Re-using rectangles
     * rather than allocating them in each paint call substantially
     * reduced the time it took paint to run.  Obviously, this
     * method can't be re-entered.
     */
    private static Dimension size = new Dimension();
    private static Rectangle viewRect = new Rectangle();
    private static Rectangle iconRect = new Rectangle();
    private static Rectangle textRect = new Rectangle();

    /**
     * paint the radio button
     */
    @Override
    public synchronized void paint(Graphics g, JComponent c) {
        AbstractButton b = (AbstractButton) c;
        ButtonModel model = b.getModel();

        Font f = c.getFont();
        g.setFont(f);
        //FontMetrics fm = SwingUtilities2.getFontMetrics(c, g, f);
        FontMetrics fm = g.getFontMetrics();

        Insets i = c.getInsets();
        size = b.getSize(size);
        viewRect.x = i.left;
        viewRect.y = i.top;
        viewRect.width = size.width - (i.right + viewRect.x);
        viewRect.height = size.height - (i.bottom + viewRect.y);
        iconRect.x = iconRect.y = iconRect.width = iconRect.height = 0;
        textRect.x = textRect.y = textRect.width = textRect.height = 0;

        Icon altIcon = b.getIcon();
//        Icon selectedIcon = null;
//        Icon disabledIcon = null;

        String text = SwingUtilities.layoutCompoundLabel(
            c, fm, b.getText(), altIcon != null ? altIcon : getDefaultIcon(),
            b.getVerticalAlignment(), b.getHorizontalAlignment(),
            b.getVerticalTextPosition(), b.getHorizontalTextPosition(),
            viewRect, iconRect, textRect,
            b.getText() == null ? 0 : b.getIconTextGap());

        // fill background
        if(c.isOpaque()) {
            g.setColor(b.getBackground());
            g.fillRect(0,0, size.width, size.height);
        }


        // Paint the radio button
        if(altIcon != null) {

            if(!model.isEnabled()) {
                if(model.isSelected()) {
                   altIcon = b.getDisabledSelectedIcon();
                } else {
                   altIcon = b.getDisabledIcon();
                }
            } else if(model.isPressed() && model.isArmed()) {
                altIcon = b.getPressedIcon();
                if(altIcon == null) {
                    // Use selected icon
                    altIcon = b.getSelectedIcon();
                }
            } else if(model.isSelected()) {
                if(b.isRolloverEnabled() && model.isRollover()) {
                        altIcon = b.getRolloverSelectedIcon();
                        if (altIcon == null) {
                                altIcon = b.getSelectedIcon();
                        }
                } else {
                        altIcon = b.getSelectedIcon();
                }
            } else if(b.isRolloverEnabled() && model.isRollover()) {
                altIcon = b.getRolloverIcon();
            }

            if(altIcon == null) {
                altIcon = b.getIcon();
            }

            altIcon.paintIcon(c, g, iconRect.x, iconRect.y);

        } else {
            getDefaultIcon().paintIcon(c, g, iconRect.x, iconRect.y);   //Position des iconRectangles
        }


        // Draw the Text
        if(text != null) {
            View v = (View) c.getClientProperty(BasicHTML.propertyKey);
            if (v != null) {
                v.paint(g, textRect);
            } else {
                paintText(g, b, textRect, text);
            }
            if(b.hasFocus() && b.isFocusPainted() &&
               textRect.width > 0 && textRect.height > 0 ) {
                paintFocus(g, textRect, size);
            }
        }
    } 
    
    @Override
    protected void paintFocus(Graphics g, Rectangle textRect, Dimension size){
//        g.setColor(getFocusColor());
//        g.drawRect(t.x-1, t.y-1, t.width+1, t.height+1);
    }
        
    /**
     * Method which renders the text of the current button.
     * <p>
     * @param g Graphics context
     * @param b Current button to render
     * @param textRect Bounding rectangle to render the text.
     * @param text String to render
     * @since 1.4
     */
    @Override
    protected void paintText(Graphics g, AbstractButton b, Rectangle textRect, String text) {
        ButtonModel model = b.getModel();
        //*DEPRECATED*
//        FontMetrics fm = SwingUtilities2.getFontMetrics(b, g);
        FontMetrics fm = g.getFontMetrics();
        int mnemonicIndex = b.getDisplayedMnemonicIndex();

        /* Draw the Text */
        if(model.isEnabled()) {
            /*** paint the text normally */
            
            //Wenn Rollover, dann zeichne Text weiß
            if(b.isRolloverEnabled() && model.isRollover()) {
                g.setColor(Color.WHITE);
            }
            else{
                g.setColor(b.getForeground());
            }
            //*DEPRECATED*
//            SwingUtilities2.drawStringUnderlineCharAt(b, g,text, mnemonicIndex,
//                                          textRect.x + getTextShiftOffset(),
//                                          textRect.y + fm.getAscent() + getTextShiftOffset());
            BasicGraphicsUtils.drawStringUnderlineCharAt(g, text, mnemonicIndex,
                              textRect.x + getTextShiftOffset(),
                              textRect.y + fm.getAscent() + getTextShiftOffset());
        }
        else {
            /*** paint the text disabled ***/
//            g.setColor(b.getBackground().brighter());
            g.setColor(UIManager.getColor("RadioButton.disabledForeground"));
            //*DEPRECATED*
//            SwingUtilities2.drawStringUnderlineCharAt(b, g,text, mnemonicIndex,
//                                          textRect.x, textRect.y + fm.getAscent());
            BasicGraphicsUtils.drawStringUnderlineCharAt(g, text, mnemonicIndex,
                                          textRect.x, textRect.y + fm.getAscent());
//            g.setColor(b.getBackground().darker());
//            SwingUtilities2.drawStringUnderlineCharAt(c, g,text, mnemonicIndex,
//                                          textRect.x - 1, textRect.y + fm.getAscent() - 1);
        }
    }

}
