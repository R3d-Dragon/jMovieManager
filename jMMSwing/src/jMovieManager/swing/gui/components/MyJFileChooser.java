/**
 * Copyright (c) 2010-2015 Bryan Beck.
 * All rights reserved.
 * 
 * This project is licensed under LGPL v2.1.
 * See jMovieManager-license.txt for details.
 * 
 */
package jMovieManager.swing.gui.components;

import java.awt.Component;
import java.awt.HeadlessException;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import jMovieManager.swing.gui.other.JMMFileFilter;
import jMovieManager.swing.gui.other.JMMFileFilter.FilterType;
import jmm.utils.Settings;

/**
 * A custom jFileChooser which automatically sets and loads the recentFile from the Settings class
 * 
 * @author Bryan Beck
 * @since 28.10.2012
 */
public class MyJFileChooser extends JFileChooser{
        
    /**
     * Constructs a MyJFileChooser
     * 
     * @param currentDirectoryPath the path to the file or directory to open
     * @param filterType the kind of filter (displayed files)
     * @see JFileChooser#JFileChooser(java.lang.String) 
     *
     */
    public MyJFileChooser(String currentDirectoryPath, FilterType filterType) {
        super(currentDirectoryPath);

        //set the current dir
        if(currentDirectoryPath.isEmpty()){
            File recentFile = new File(Settings.getInstance().getRecentFile());
            if(recentFile.exists() && recentFile.canRead()){
                this.setCurrentDirectory(recentFile);
            }
        }
        //deactivate "show all"
        this.setAcceptAllFileFilterUsed(false);
        //create file filter
        FileFilter fileFilter = JMMFileFilter.createFileFilter(filterType);
        if(fileFilter != null){
            this.setFileFilter(fileFilter);
        }
    }
    
//    /**
//     * Creates a new dialog and add an option to display hidden files
//     * @see JFileChooser#createDialog(java.awt.Component) 
//     */
//    @Override
//    protected JDialog createDialog(Component parent) throws HeadlessException {
//        JDialog dialog = super.createDialog(parent);
//        return dialog;
//    }
    
    /**
     * 
     * @see JFileChooser#showDialog(java.awt.Component, java.lang.String)  
     */
    @Override
    public int showDialog(Component parent, String approveButtonText) throws HeadlessException {
        int status = super.showDialog(parent, approveButtonText);
        //set the recently opened file
        if(status == JFileChooser.APPROVE_OPTION){
            String recentFilePath = this.getSelectedFile().getAbsolutePath();
            Settings.getInstance().setRecentFile(recentFilePath);
        }
        return status;
    }
}
