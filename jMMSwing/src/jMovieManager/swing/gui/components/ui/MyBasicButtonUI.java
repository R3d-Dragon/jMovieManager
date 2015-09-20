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
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicGraphicsUtils;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.text.View;
import jMovieManager.swing.gui.components.MyBasicArrowButton;
import jMovieManager.swing.gui.other.MyIconFactory;

/**
 *
 * @author Bryan Beck
 * @since 01.11.2011
 */
public class MyBasicButtonUI extends BasicButtonUI{
   
     /**
     * creates the UI (look and feel) for the component
     * 
     * @param b the component
     * @return the ComponentUI
     */
    public static ComponentUI createUI(JComponent c) {
        return new MyBasicButtonUI();
    }
    
    @Override
    protected void installDefaults(AbstractButton b) {
        super.installDefaults(b);
        if(!(b instanceof MyBasicArrowButton)){
            //b.setBorder(null);
            b.setOpaque(false);
            b.setBorderPainted(false);         
            //b.setIcon(MyIconFactory.button_default);
            //b.setRolloverIcon(null);
        }
    }    
    
    /* These rectangles/insets are allocated once for all
     * ButtonUI.paint() calls.  Re-using rectangles rather than
     * allocating them in each paint call substantially reduced the time
     * it took paint to run.  Obviously, this method can't be re-entered.
     */
    private static Rectangle viewRect = new Rectangle();
    private static Rectangle textRect = new Rectangle();
    private static Rectangle iconRect = new Rectangle();

    // ********************************
    //          Paint Methods
    // ********************************

    /**
     * If necessary paints the background of the component, then
     * invokes <code>paint</code>.
     *
     * @param g Graphics to paint to
     * @param c JComponent painting on
     * @throws NullPointerException if <code>g</code> or <code>c</code> is
     *         null
     * @see javax.swing.plaf.ComponentUI#update
     * @see javax.swing.plaf.ComponentUI#paint
     * @since 1.5
     */
    @Override
    public void update(Graphics g, JComponent c) {
            ImageIcon imageToDraw;
            AbstractButton button = (AbstractButton)c;
            if(button.getIcon() == null){
                if(!c.isEnabled()){
                    imageToDraw = MyIconFactory.button_disabled;
                }
                else if(button.isRolloverEnabled() && button.getModel().isRollover()){
                    imageToDraw = MyIconFactory.button_highlight;
                }
                else{
                    imageToDraw = MyIconFactory.button_default;
                }

                g.drawImage(imageToDraw.getImage(),     //    the image to be rendered
                    0,                                  //    dx1 - the x coordinate of the first corner of the destination rectangle.
                    0,                                  //    dy1 - the y coordinate of the first corner of the destination rectangle.
                    c.getWidth(),                       //    dx2 - the x coordinate of the second corner of the destination rectangle.
                    c.getHeight(),                      //    dy2 - the y coordinate of the second corner of the destination rectangle.
                    0,                                  //    sx1 - the x coordinate of the first corner of the source rectangle.
                    0,                                  //    sy1 - the y coordinate of the first corner of the source rectangle.
                    imageToDraw.getIconWidth(),         //    sx2 - the x coordinate of the second corner of the source rectangle.
                    imageToDraw.getIconHeight(),        //    sy2 - the y coordinate of the second corner of the source rectangle.
                    null                                //    Observer, der benachrichtigt werden soll
                );
            }
            
        paint(g, c);
    }    
    
    /**
     * Zeichenmethode des Buttons
     * 
     * @param g
     * @param c 
     */
    @Override
    public void paint(Graphics g, JComponent c){
        AbstractButton b = (AbstractButton) c;
//        ButtonModel model = b.getModel();

        String text = layout(b, g.getFontMetrics(), //SwingUtilities2.getFontMetrics(b, g),
               b.getWidth(), b.getHeight());

        clearTextShiftOffset();

        // perform UI specific press action, e.g. Windows L&F shifts text
//        if (model.isArmed() && model.isPressed()) {
//            paintButtonPressed(g,b);
//        }
//
        // Paint the Icon
        if(b.getIcon() != null) {
            paintIcon(g,c,iconRect);
        }

        if (text != null && !text.equals("")){
            View v = (View) c.getClientProperty(BasicHTML.propertyKey);
            if (v != null) {
                v.paint(g, textRect);
            } else {
                paintText(g, b, textRect, text);
            }
        }

//        if (b.isFocusPainted() && b.hasFocus()) {
//            // paint UI specific focus
//            paintFocus(g,b,viewRect,textRect,iconRect);
//        }
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
        //[r=122,g=138,b=153] //controlDkShadow
        //[r=184,g=207,b=229] //info      getPrimaryControl
        //[r=153,g=153,b=153] //getInactiveControlTextColor
        if(b.isRolloverEnabled() && b.getModel().isRollover()){
            //this.drawMouseoverEffect(g, b, textRect);     
            Color oldColor = b.getForeground();
            b.setForeground(Color.WHITE);
            //b.setForeground(ColorInterface.selectedListText);
            paintText(g, (JComponent)b, textRect, text);
            b.setForeground(oldColor);
        }
        else if(!b.getModel().isEnabled()){
            /*** paint the text disabled ***/
            //*DEPRECADED*
            //FontMetrics fm = SwingUtilities2.getFontMetrics(b, g);
            FontMetrics fm = g.getFontMetrics();
                      
            int mnemonicIndex = b.getDisplayedMnemonicIndex();

            /*** paint the text disabled ***/
            g.setColor(UIManager.getColor("Button.disabledForeground"));
            //*DEPRECADED*
//            SwingUtilities2.drawStringUnderlineCharAt(b, g,text, mnemonicIndex,
//                                          textRect.x, textRect.y + fm.getAscent());
            BasicGraphicsUtils.drawStringUnderlineCharAt(g,text, mnemonicIndex,
                                          textRect.x, textRect.y + fm.getAscent());
//            g.setColor(UIManager.getColor("Button.disabledForeground").darker());
//            SwingUtilities2.drawStringUnderlineCharAt(b, g,text, mnemonicIndex,
//                                          textRect.x - 1, textRect.y + fm.getAscent() - 1);           
        }
        else{
            paintText(g, (JComponent)b, textRect, text);  
        }
    }
    
    /**
     * 
     * @param b
     * @param fm
     * @param width
     * @param height
     * @return 
     */
    private String layout(AbstractButton b, FontMetrics fm,
                          int width, int height) {
        Insets i = b.getInsets();
        viewRect.x = i.left;
        viewRect.y = i.top;
        viewRect.width = width - (i.right + viewRect.x);
        viewRect.height = height - (i.bottom + viewRect.y);

        textRect.x = textRect.y = textRect.width = textRect.height = 0;
        iconRect.x = iconRect.y = iconRect.width = iconRect.height = 0;

        // layout the text and icon
        return SwingUtilities.layoutCompoundLabel(
            b, fm, b.getText(), b.getIcon(),
            b.getVerticalAlignment(), b.getHorizontalAlignment(),
            b.getVerticalTextPosition(), b.getHorizontalTextPosition(),
            viewRect, iconRect, textRect,
            b.getText() == null ? 0 : b.getIconTextGap());
    }
}
