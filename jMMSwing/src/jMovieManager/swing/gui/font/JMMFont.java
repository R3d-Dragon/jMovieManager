/**
 * Copyright (c) 2010-2015 Bryan Beck.
 * All rights reserved.
 * 
 * This project is licensed under LGPL v2.1.
 * See jMovieManager-license.txt for details.
 * 
 */
package jMovieManager.swing.gui.font;

import jMovieManager.swing.gui.createmedia.CreateSerieGUI;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;

/**
 * Die Basisfont für alle GUI Klassen
 * 
 * @author Bryan Beck
 * @since 14.11.2011
 */
public abstract class JMMFont {
    /** Logger. */
    private static final Logger LOG = LoggerFactory.logger(JMMFont.class);
    
    public static final String fontName = "Tahoma.ttf";
    private static Font baseFont; 
    
    public static final Font labelFont;           //Bold
    
    public static final Font smallIOFont;         //Plain
    public static final Font normalIOFont;        //Plain
    public static final Font largeIOFont;         //Plain
    public static final Font borderFont;
    public static final Font smallBorderFont;
    public static final Font buttonFont;          //Plain
    
    //statischer Initialisierungsblock für Font
    static{
        try {
            setBaseFont(Font.createFont(Font.TRUETYPE_FONT, JMMFont.class.getResourceAsStream("/jMovieManager/swing/gui/font/" + fontName)));
        } catch (FontFormatException | IOException ex) {
            LOG.error(fontName + " not loaded. Use default Font.", ex);
            setBaseFont(new Font("serif", Font.PLAIN, 12));
        }
        finally{             
            //Customize Base font
            labelFont = getBaseFont().deriveFont(Font.BOLD, 12);
            smallIOFont = getBaseFont().deriveFont(Font.PLAIN, 11);
            normalIOFont = getBaseFont().deriveFont(Font.PLAIN, 12);
            largeIOFont = getBaseFont().deriveFont(Font.PLAIN, 14);
            
            borderFont = getBaseFont().deriveFont(3, 13);
            smallBorderFont = getBaseFont().deriveFont(3, 12);
            buttonFont = getBaseFont().deriveFont(Font.BOLD, 11.4F);
        }
    }

    /**
     * @return the baseFont
     */
    public static Font getBaseFont() {
        return baseFont;
    }

    /**
     * @param aBaseFont the baseFont to set
     */
    public static void setBaseFont(Font aBaseFont) {
        baseFont = aBaseFont;
    }    
}
