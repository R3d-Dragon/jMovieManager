/**
 * Copyright (c) 2010-2015 Bryan Beck.
 * All rights reserved.
 * 
 * This project is licensed under LGPL v2.1.
 * See jMovieManager-license.txt for details.
 * 
 */
package jMovieManager.swing.gui.components.ui;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import jMovieManager.swing.gui.other.MyIconFactory;

/**
 *
 * @author Bryan Beck
 * @since 18.10.2011
 */
public class MyBasicCheckBoxUI extends MyBasicRadioButtonUI{
    private final static String propertyPrefix = "CheckBox" + ".";

    /**
     * creates the UI (look and feel) for the component
     * 
     * @param b the component
     * @return the ComponentUI
     */
    public static ComponentUI createUI(JComponent b) {
        return new MyBasicCheckBoxUI();
    }
    
        // ********************************
    //        Install PLAF
    // ********************************
    @Override
    protected void installDefaults(AbstractButton b){
        super.installDefaults(b);
        b.setIcon(MyIconFactory.checkBox_default);
        b.setDisabledIcon(MyIconFactory.checkBox_disabled);
        b.setDisabledSelectedIcon(MyIconFactory.checkBox_disabled_selected);
        b.setSelectedIcon(MyIconFactory.checkBox_selected);
        b.setRolloverIcon(MyIconFactory.checkBox_highlight);
        b.setRolloverSelectedIcon(MyIconFactory.checkBox_highlight_selected);
    }

    // ********************************
    //        Uninstall PLAF
    // ********************************
    @Override
    protected void uninstallDefaults(AbstractButton b){
        super.uninstallDefaults(b);
        icon = null;
        b.setIcon(null);
        b.setDisabledIcon(null);
        b.setDisabledSelectedIcon(null);
        b.setSelectedIcon(null);
        b.setRolloverIcon(null);
        b.setRolloverSelectedIcon(null);        
    }    
    
    @Override
    public String getPropertyPrefix() {
        return propertyPrefix;
    }    
}
