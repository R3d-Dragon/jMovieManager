/**
 * Copyright (c) 2010-2015 Bryan Beck.
 * All rights reserved.
 * 
 * This project is licensed under LGPL v2.1.
 * See jMovieManager-license.txt for details.
 * 
 */
package jMovieManager.swing.gui.components;

import java.awt.Graphics;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author Bryan Beck
 * @since 25.02.2013
 */
public class MyOverlapJLabel extends JLabel{
    
    private ImageIcon watermark;
       
    /**
     * @see JLabel#JLabel(javax.swing.Icon) 
     *
     * @param watermark  The second image to overlap over the first one
     */
    public MyOverlapJLabel(Icon image, ImageIcon watermark) {
        super(image);
        this.watermark = watermark;
    }
       
    /**
     * @see JLabel#JLabel() 
     */
    public MyOverlapJLabel() {
        super();
    }

    /**
     * @return the watermark image
     */
    public ImageIcon getWatermark() {
        return watermark;
    }

    /** 
     * @param watermark the watermark image to set
     */
    public void setWatermark(ImageIcon watermark) {
        if(this.watermark != null){
          //  this.watermark.getImage().flush();
        }
        this.watermark = watermark;
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if((getWatermark() != null) && (getIcon() != null)){
            int imageWidth = watermark.getIconWidth();
            int imageHeight = watermark.getIconHeight();
            int startX = 10;//this.getWidth()- imageWidth;
            int startY = getIcon().getIconHeight() - imageHeight - 10;
            g.drawImage(getWatermark().getImage(), startX, startY, imageWidth, imageHeight, this);
        }
    }
}
