/**
 * Copyright (c) 2010-2015 Bryan Beck.
 * All rights reserved.
 * 
 * This project is licensed under LGPL v2.1.
 * See jMovieManager-license.txt for details.
 * 
 */
package jMovieManager.swing.gui.other;

import java.io.File;
import java.net.URI;

/**
 * This class extends the java.io.File class and overrides the toString() method
 * 
 * @author Bryan Beck
 * @since 21.09.2012
 */
public class MyFile extends File{
    
    /**
     * @see java.io.File#File(java.lang.String) 
     */
    public MyFile(String pathname) {
        super(pathname);
    }

    /**
     * @see java.io.File#File(java.lang.String, java.lang.String) 
     */
    public MyFile(String parent, String child) {
        super(parent, child);	
    }

    /**
     * @see java.io.File#File(java.io.File, java.lang.String) 
     */
    public MyFile(File parent, String child) {
        super(parent, child);
    }

    /**
     * @see java.io.File#File(java.net.URI) 
     */
    public MyFile(URI uri) {
        super(uri);
    }    
    
    
    public MyFile(File file){
        super(file.getAbsolutePath());
    }
    
    /**
     * Returns the pathname string of this abstract pathname.  This is just the
     * string returned by the <code>{@link #getName() }</code> method.
     *
     * @return  The name of the file
     */
    @Override
    public String toString() {
	return getName();
    }    
    
     /**
     * @see java.io.File#listFiles() 
     */
    @Override
    public MyFile[] listFiles() {
	String[] ss = list();
	if (ss == null){
            return null;
        }
	int n = ss.length;
	MyFile[] fs = new MyFile[n];
	for (int i = 0; i < n; i++) {
	    fs[i] = new MyFile(this, ss[i]);
	}
	return fs;
    }
    
     /**
     * @see java.io.File#getParentFile() 
     */
    @Override
    public File getParentFile() {
        File file = super.getParentFile();
	if (file == null){
            return null;
        }
	return new MyFile(file);
    }
}
