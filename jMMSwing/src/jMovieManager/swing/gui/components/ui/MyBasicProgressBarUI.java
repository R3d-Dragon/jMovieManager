/**
 * Copyright (c) 2010-2015 Bryan Beck.
 * All rights reserved.
 * 
 * This project is licensed under LGPL v2.1.
 * See jMovieManager-license.txt for details.
 * 
 */
package jMovieManager.swing.gui.components.ui;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import javax.swing.JComponent;
import javax.swing.JProgressBar;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicProgressBarUI;
import jMovieManager.swing.gui.other.MyIconFactory;

/**
 *
 * @author Bryan Beck
 * @since 18.10.2011
 */
public class MyBasicProgressBarUI extends BasicProgressBarUI{

    /**
     * creates the UI (look and feel) for the component
     * 
     * @param b the component
     * @return the ComponentUI
     */ 
    public static ComponentUI createUI(JComponent x) {
        return new MyBasicProgressBarUI();
    }  
    
    /**
     * All purpose paint method that should do the right thing for almost
     * all linear, determinate progress bars. By setting a few values in
     * the defaults
     * table, things should work just fine to paint your progress bar.
     * Naturally, override this if you are making a circular or
     * semi-circular progress bar.
     *
     * @see #paintIndeterminate
     *
     * @since 1.4
     */
    @Override
    protected void paintDeterminate(Graphics g, JComponent c) {
        if (!(g instanceof Graphics2D)) {
            return;
        }

        Insets b = progressBar.getInsets(); // area for border
        int barRectWidth = progressBar.getWidth() - (b.right + b.left);
        int barRectHeight = progressBar.getHeight() - (b.top + b.bottom);

        if (barRectWidth <= 0 || barRectHeight <= 0) {
            return;
        }

        int cellLength = getCellLength();
        int cellSpacing = getCellSpacing();
        // amount of progress to draw
        int amountFull = getAmountFull(b, barRectWidth, barRectHeight);

        Graphics2D g2 = (Graphics2D)g;
        g2.setColor(progressBar.getForeground());

        if (progressBar.getOrientation() == JProgressBar.HORIZONTAL) {
            // draw the cells
            if (cellSpacing == 0 && amountFull > 0) {
                // draw one big Rect because there is no space between cells
                g2.setStroke(new BasicStroke((float)barRectHeight,
                        BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
            } else {
                // draw each individual cell
                g2.setStroke(new BasicStroke((float)barRectHeight,
                        BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
                        0.f, new float[] { cellLength, cellSpacing }, 0.f));
            }

            if (c.getComponentOrientation().isLeftToRight()) {
                    g2.drawImage(MyIconFactory.progressbar_foreground.getImage(),//          the image to be rendered
                        b.left,                                 //    dx1 - the x coordinate of the first corner of the destination rectangle.
                        b.top,                                  //    dy1 - the y coordinate of the first corner of the destination rectangle.
                        amountFull + b.left,                    //    dx2 - the x coordinate of the second corner of the destination rectangle.
                        b.top + barRectHeight,                  //    dy2 - the y coordinate of the second corner of the destination rectangle.
                        0,                                      //    sx1 - the x coordinate of the first corner of the source rectangle.
                        0,                                      //    sy1 - the y coordinate of the first corner of the source rectangle.
                        MyIconFactory.progressbar_foreground.getIconWidth(),   //    sx2 - the x coordinate of the second corner of the source rectangle.
                        MyIconFactory.progressbar_foreground.getIconHeight(),  //    sy2 - the y coordinate of the second corner of the source rectangle.
                        null                                    //          Observer, der benachrichtigt werden soll
                    );
//                g2.drawLine(b.left, (barRectHeight/2) + b.top,
//                        amountFull + b.left, (barRectHeight/2) + b.top);
            } else {
                g2.drawLine((barRectWidth + b.left),
                        (barRectHeight/2) + b.top,
                        barRectWidth + b.left - amountFull,
                        (barRectHeight/2) + b.top);
            }

        } else { // VERTICAL
            // draw the cells
            if (cellSpacing == 0 && amountFull > 0) {
                // draw one big Rect because there is no space between cells
                g2.setStroke(new BasicStroke((float)barRectWidth,
                        BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
            } else {
                // draw each individual cell
                g2.setStroke(new BasicStroke((float)barRectWidth,
                        BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
                        0f, new float[] { cellLength, cellSpacing }, 0f));
            }

            g2.drawLine(barRectWidth/2 + b.left,
                    b.top + barRectHeight,
                    barRectWidth/2 + b.left,
                    b.top + barRectHeight - amountFull);
        }

        // Deal with possible text painting
        if (progressBar.isStringPainted()) {
            paintString(g, b.left, b.top,
                        barRectWidth, barRectHeight,
                        amountFull, b);
        }
    }
}
