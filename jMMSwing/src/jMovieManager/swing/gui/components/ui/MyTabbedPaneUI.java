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
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicGraphicsUtils;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.plaf.metal.MetalTabbedPaneUI;
import javax.swing.text.View;

/**
 *
 * @author Bryan Beck
 * @since 20.10.2011
 */
public class MyTabbedPaneUI extends MetalTabbedPaneUI{
    
    /**
     * creates the UI (look and feel) for the component
     * 
     * @param b the component
     * @return the ComponentUI
     */    
    public static ComponentUI createUI(JComponent c) {
        return new MyTabbedPaneUI();
    }  
    
    @Override
    protected void paintContentBorderTopEdge( Graphics g, int tabPlacement,
                                              int selectedIndex,
                                              int x, int y, int w, int h ) {
        boolean leftToRight = MyTabbedPaneUI.isLeftToRight(tabPane);
        int right = x + w - 1;
        Rectangle selRect = selectedIndex < 0? null :
                               getTabBounds(selectedIndex, calcRect);
   //     if (ocean) {
            g.setColor(UIManager.getColor("TabbedPane.borderHightlightColor"));
           // g.setColor(oceanSelectedBorderColor);
//        }
//        else {
//            g.setColor(selectHighlight);
//        }

        // Draw unbroken line if tabs are not on TOP, OR
        // selected tab is not in run adjacent to content, OR
        // selected tab is not visible (SCROLL_TAB_LAYOUT)
        //
         if (tabPlacement != TOP || selectedIndex < 0 ||
            (selRect.y + selRect.height + 1 < y) ||
            (selRect.x < x || selRect.x > x + w)) {
            g.drawLine(x, y, x+w-2, y);
//            if (ocean && tabPlacement == TOP) {
//                g.setColor(MetalLookAndFeel.getWhite());
                g.drawLine(x, y + 1, x+w-2, y + 1);
//            }
        } else {
            // Break line to show visual connection to selected tab
            boolean lastInRun = isLastInRun(selectedIndex);

            if ( leftToRight || lastInRun ) {
                g.drawLine(x, y, selRect.x, y);
//                g.drawLine(x, y, selRect.x + 1, y);
            } else {
                g.drawLine(x, y, selRect.x, y);
            }

            if (selRect.x + selRect.width < right - 1) {
                if ( leftToRight && !lastInRun ) {
                    g.drawLine(selRect.x + selRect.width, y, right - 1, y);
                } else {
                    g.drawLine(selRect.x + selRect.width - 1, y, right - 1, y);
                }
            } else {
               // g.setColor(shadow);
                g.drawLine(x+w-2, y, x+w-2, y);
            }

         //   if (ocean) {
             //   g.setColor(MetalLookAndFeel.getWhite());
//
//                if ( leftToRight || lastInRun ) {
//                    g.drawLine(x, y + 1, selRect.x + 1, y + 1);
//                } else {
//                    g.drawLine(x, y + 1, selRect.x, y + 1);
//                }
//
//                if (selRect.x + selRect.width < right - 1) {
//                    if ( leftToRight && !lastInRun ) {
//                        g.drawLine(selRect.x + selRect.width, y + 1,
//                                   right - 1, y + 1);
//                    } else {
//                        g.drawLine(selRect.x + selRect.width - 1, y + 1,
//                                   right - 1, y + 1);
//                    }
//                } else {
//                    g.setColor(shadow);
//                    g.drawLine(x+w-2, y + 1, x+w-2, y + 1);
//                }
       //     }
        }
    }    
        
    private boolean isLastInRun( int tabIndex ) {
        int run = getRunForTab( tabPane.getTabCount(), tabIndex );
        int lastIndex = lastTabInRun( tabPane.getTabCount(), run );
        return tabIndex == lastIndex;
    }
    /*
     * Convenience function for determining ComponentOrientation.  Helps us
     * avoid having Munge directives throughout the code.
     */
    static boolean isLeftToRight( Component c ) {
        return c.getComponentOrientation().isLeftToRight();
    }
    
    @Override
    protected void paintText(Graphics g, int tabPlacement,
                         Font font, FontMetrics metrics, int tabIndex,
                         String title, Rectangle textRect,
                         boolean isSelected) {
        g.setFont(font);

        View v = getTextViewForTab(tabIndex);
        if (v != null) {
            // html
            v.paint(g, textRect);
        } else {
            // plain text
            int mnemIndex = tabPane.getDisplayedMnemonicIndexAt(tabIndex);
            Color selectedFG = UIManager.getColor("TabbedPane.selectedForeground");
            if (tabPane.isEnabled() && tabPane.isEnabledAt(tabIndex)) {
                Color fg = tabPane.getForegroundAt(tabIndex);
                if (isSelected && (fg instanceof UIResource)) {
                    if (selectedFG != null) {
                        fg = selectedFG;
                    }
                }
                else if(!isSelected){                               //Customized
                    fg = UIManager.getColor("TabbedPane.inactiveForeground");
                }                                                   //END Customized
                g.setColor(fg);            
                //*DEPRECATED*
//                SwingUtilities2.drawStringUnderlineCharAt(tabPane, g,
//                             title, mnemIndex,
//                             textRect.x, textRect.y + metrics.getAscent());
                BasicGraphicsUtils.drawStringUnderlineCharAt(g,
                             title, mnemIndex,
                             textRect.x, textRect.y + metrics.getAscent());
            } else { // tab disabled
//Customized 2 
                //Paint the inactive tab ("+") as a selected one
                if (tabPane.isEnabled() && 
                        !tabPane.isEnabledAt(tabIndex)) {
                    g.setColor(tabPane.getForegroundAt(tabIndex));
                }
                else{
                    g.setColor(tabPane.getBackgroundAt(tabIndex).brighter());
                }
//End Customized 2
                //*DEPRECATED*
//                SwingUtilities2.drawStringUnderlineCharAt(tabPane, g,
//                             title, mnemIndex,
//                             textRect.x, textRect.y + metrics.getAscent());
                BasicGraphicsUtils.drawStringUnderlineCharAt(g,
                             title, mnemIndex,
                             textRect.x, textRect.y + metrics.getAscent());                
                g.setColor(tabPane.getBackgroundAt(tabIndex).darker());
                //*DEPRECATED*
//                SwingUtilities2.drawStringUnderlineCharAt(tabPane, g,
//                             title, mnemIndex,
//                             textRect.x - 1, textRect.y + metrics.getAscent() - 1);
//                BasicGraphicsUtils.drawStringUnderlineCharAt(g,
//                             title, mnemIndex,
//                             textRect.x - 1, textRect.y + metrics.getAscent() - 1);                
            }
        }
    }
        
     /**
     * Bugfix on startup
     *  java.lang.ArrayIndexOutOfBoundsException: 1
     *	at javax.swing.plaf.basic.BasicTabbedPaneUI.paintTabArea(BasicTabbedPaneUI.java:817)
     * 
     * @see BasicTabbedPaneUI#paintTabArea(java.awt.Graphics, int, int) 
     */
    @Override
    protected void paintTabArea(Graphics g, int tabPlacement, int selectedIndex) {
        int tabCount = tabPane.getTabCount();

        Rectangle iconRect = new Rectangle(),
                  textRect = new Rectangle();
        Rectangle clipRect = g.getClipBounds();  

        // Paint tabRuns of tabs from back to front
        for (int i = runCount - 1; i >= 0; i--) {
            int start = tabRuns[i];
            int next = tabRuns[(i == runCount - 1)? 0 : i + 1];
            int end = (next != 0? next - 1: tabCount - 1);
            for (int j = start; j <= end; j++) {
                //Bugfix java.lang.ArrayIndexOutOfBoundsException
                if(rects.length > j){
                    if (j != selectedIndex && rects[j].intersects(clipRect)) {
                        //TODO: ArrayOutOfBound is still there in java 1.7
                        /*
                         * java.lang.ArrayIndexOutOfBoundsException: 5
                            at javax.swing.plaf.basic.BasicTabbedPaneUI.getTabBounds(Unknown Source)
                            at javax.swing.plaf.basic.BasicTabbedPaneUI.getTabBounds(Unknown Source)
                            at javax.swing.plaf.metal.MetalTabbedPaneUI.shouldFillGap(Unknown Source)
                            at javax.swing.plaf.metal.MetalTabbedPaneUI.paintTopTabBorder(Unknown Source)
                            at javax.swing.plaf.metal.MetalTabbedPaneUI.paintTabBorder(Unknown Source)
                            at javax.swing.plaf.basic.BasicTabbedPaneUI.paintTab(Unknown Source)
                            at jMovieManager.swing.gui.components.ui.MyTabbedPaneUI.paintTabArea(MyTabbedPaneUI.java:205)
                         */
                        paintTab(g, tabPlacement, rects, j, iconRect, textRect);
                    }
                }
            }
        }

        // Paint selected tab if its in the front run
        // since it may overlap other tabs
        if (selectedIndex >= 0 && rects[selectedIndex].intersects(clipRect)) {
            paintTab(g, tabPlacement, rects, selectedIndex, iconRect, textRect);
        }
    }
   
//        /**
//     * @see BasicTabbedPaneUI#getTabBounds(int, java.awt.Rectangle) 
//     */
//    @Override
//    protected Rectangle getTabBounds(int tabIndex, Rectangle dest) {
//        if(rects.length > tabIndex){
//            return super.getTabBounds(tabIndex, dest);
//        }
//        else{
//            return null;
//        }
//    }
}
