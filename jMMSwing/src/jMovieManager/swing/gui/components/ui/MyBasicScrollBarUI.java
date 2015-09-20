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
import java.awt.Rectangle;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicScrollBarUI;
import jMovieManager.swing.gui.ColorInterface;
import jMovieManager.swing.gui.components.MyBasicArrowButton;

/**
 * Angepasste Scrollbar UI Componente, mit eigenen Farben, Größen und Eigenschaften
 * 
 * @author Bryan Beck
 * @since 09.10.2011
 */
public class MyBasicScrollBarUI extends BasicScrollBarUI implements ColorInterface{
    
    protected Color basicArrowButtonBackground;
    protected Color mytrackColor;
    
    /**
     * Erstellt ein neues ScrollbarUI
     * @param c Der Hintergrund der Komponente C, wird auch für die Scrollbar verwendet <br/> Wenn null, wird eine Standardfarbe verwendet
     * @return Die neu erstellte ComponentUI
     */
    public static ComponentUI createUI(JComponent c)    {
        MyBasicScrollBarUI basicScrollBarUI = new MyBasicScrollBarUI();
        if(c != null){
            basicScrollBarUI.basicArrowButtonBackground = c.getBackground();
            basicScrollBarUI.mytrackColor = c.getBackground();
        }
        else{
            basicScrollBarUI.basicArrowButtonBackground = basicArrowButton_Background;
            basicScrollBarUI.mytrackColor = UIManager.getColor("ScrollBar.track");
        }
        
        return basicScrollBarUI;
    }
    
    @Override
    protected void installComponents(){
        switch (scrollbar.getOrientation()) {
        case JScrollBar.VERTICAL:
            incrButton = createIncreaseButton(SOUTH);
            decrButton = createDecreaseButton(NORTH);
            break;

        case JScrollBar.HORIZONTAL:
            if (scrollbar.getComponentOrientation().isLeftToRight()) {    
                incrButton = createIncreaseButton(EAST);
                decrButton = createDecreaseButton(WEST);
            } else {
                incrButton = createIncreaseButton(WEST);
                decrButton = createDecreaseButton(EAST);
            }
            break;
        }        
        incrButton.setBorderPainted(false);
        incrButton.setBorder(null);
        incrButton.setContentAreaFilled(false);
        decrButton.setBorderPainted(false);
        decrButton.setBorder(null);
        decrButton.setContentAreaFilled(false);
        
        scrollbar.add(incrButton);
        scrollbar.add(decrButton); 
        // Force the children's enabled state to be updated.
        scrollbar.setEnabled(scrollbar.isEnabled());
    }      
    
    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds)
    {
        if(thumbBounds.isEmpty() || !scrollbar.isEnabled())     {
            return;
        }

        int w = thumbBounds.width;
        int h = thumbBounds.height;
        int arc = 5;

        g.translate(thumbBounds.x, thumbBounds.y);

        //Zeichne Thumb
        g.setColor(thumbDarkShadowColor);
        g.drawRoundRect(0, 0, w-1, h-1, arc, arc);
        g.setColor(thumbColor);
        g.fillRoundRect(0, 0, w-1, h-1, arc, arc);

        g.setColor(thumbHighlightColor);
        g.drawLine(1, 1, 1, h-2);
        g.drawLine(2, 1, w-3, 1);

        g.setColor(thumbLightShadowColor);
        g.drawLine(2, h-2, w-2, h-2);
        g.drawLine(w-2, 1, w-2, h-3);

        g.translate(-thumbBounds.x, -thumbBounds.y);
    }
    
    
    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds)
    {
        g.setColor(mytrackColor);
        g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);            
            
        if(trackHighlight == DECREASE_HIGHLIGHT)        {
            paintDecreaseHighlight(g);
        }
        else if(trackHighlight == INCREASE_HIGHLIGHT)           {
            paintIncreaseHighlight(g);
        }
    }
        
    @Override
    protected JButton createDecreaseButton(int orientation)  {
        return new MyBasicArrowButton(orientation,
                basicArrowButtonBackground, //UIManager.getColor("ScrollBar.thumb"),             //Background
                basicArrowButtonBackground, //UIManager.getColor("ScrollBar.thumbShadow"),
                basicArrowButton_Foreground, //UIManager.getColor("ScrollBar.thumbDarkShadow"),   //Rand unten Rechts , Pfeilfarbe
                basicArrowButtonBackground); //UIManager.getColor("ScrollBar.thumbHighlight"));   
    }

    @Override
    protected JButton createIncreaseButton(int orientation)  {
        return new MyBasicArrowButton(orientation,
                basicArrowButtonBackground, //UIManager.getColor("ScrollBar.thumb"),             //Background
                basicArrowButtonBackground, //UIManager.getColor("ScrollBar.thumbShadow"),
                basicArrowButton_Foreground, //UIManager.getColor("ScrollBar.thumbDarkShadow"),   //Rand unten Rechts , Pfeilfarbe
                basicArrowButtonBackground); //UIManager.getColor("ScrollBar.thumbHighlight"));   
    }
}
