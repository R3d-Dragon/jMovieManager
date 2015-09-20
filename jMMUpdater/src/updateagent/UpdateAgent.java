/**
 * Copyright (c) 2010-2015 Bryan Beck.
 * All rights reserved.
 * 
 * This project is licensed under LGPL v2.1.
 * See jMovieManager-license.txt for details.
 * 
 */
package updateagent;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

/**
 * Performs an silent software update
 * 
 * @author Bryan Beck
 * @since 20.12.2012
 */
public class UpdateAgent extends Observable{
    
    private Map<String, File> outdatedFilesMap;
    private File baseDIR;

    private boolean displayProgressBar;
    private DisplayUpdateProgressGUI progressGUI;
    
    /**
     * Starts the UpdateAgent and initialize the download
     * 
     * @param args [0] Dir path to the resources to update <br/> 
     * [1] true  A process bar will be displayed <br/> false otherwise
     * [2-n] Download URLs
     * 
     */
    public static void main(String[] args){
        //G:\Bryan\Projekte\NetBeans\jMovieManager\dist\
        //false
        if(args.length > 2){
            try{
                UpdateAgent agent = new UpdateAgent(args);
                agent.performSilentUpdate();
            }catch (IOException e){
                System.err.println(e.toString());
            }
        }else{
            throw new RuntimeException("invalid args, see JavaDoc for mor information");
        }
    }
    
    /**
     * Creates a new update agent
     * 
     * @param displayProgressBar true A process bar will be displayed <br/> false otherwise
     * @param dirPath Absolute path to the dir, where the old files are stored
     * @param resources A list of download resources
     * 
     * @throws FileNotFoundException If no file was found on the dirPath.
     * @throws IOException  
     * @throws MalformedURLException If the downloadURL specifies an unknown protocol.
     */
    public UpdateAgent(String[] args) throws FileNotFoundException, IOException, MalformedURLException{    
        outdatedFilesMap = new HashMap<String, File>();
        baseDIR = new File(args[0]);
        if(!baseDIR.exists() || !baseDIR.isDirectory()){
            throw new FileNotFoundException("Dir not found: " + args[0]);
        }
        try{
            this.displayProgressBar = Boolean.valueOf(args[1]); 
        }catch (Exception e){
            throw new NullPointerException("Invalid argument for args[1]: " + args[1]);
        }
        if(displayProgressBar){
            progressGUI = new DisplayUpdateProgressGUI(null, false, args.length-2);
            this.addObserver(progressGUI);
            progressGUI.setVisible(true);
        }
        
        for(int i = 2; i < args.length; i ++){           
            int index = args[i].indexOf("=");
            String fileName;
            String downloadURL;
            if(index > -1){
                fileName = args[i].substring(0, index);
                downloadURL = args[i].substring(index+1);
                
                
                File subDir = baseDIR;
                while(fileName.contains("/")){
                    int index2 = fileName.indexOf("/");
                    if(index2 > 0){
                        String dirName = fileName.substring(0, index2);
                        subDir = new File(subDir, dirName);
                        fileName = fileName.substring(index2+1);
                    }
                }
                File file = new File(subDir, fileName);
//                if(file.exists() && file.isFile()){
                    outdatedFilesMap.put(downloadURL, file);
//                }
            }            
        }
    }
    
    /**
     * performs an silent update <br/>
     * 
     * Downloads the files from the downloadURLs <br/>
     * Deletes the old one. <br/>
     * Renames the new one as the old one. <br/>
     * Executes the new file.
     */
    public final void performSilentUpdate() throws IOException{      
        //Download all files and rename the old one, if exist
        File fileToExecute = null;
        for(String downloadURL: outdatedFilesMap.keySet()){
            File oldFile = outdatedFilesMap.get(downloadURL);
            String filePath = oldFile.getAbsolutePath();
            
            this.setChanged();
            this.notifyObservers(oldFile.getName());
            
            if(oldFile.exists() && oldFile.isFile()){
                if(this.renameTo(oldFile, filePath + ".old")){
                    oldFile = new File(filePath + ".old");
                }
            }else{
                oldFile = null;
            }
            File newFile = this.downloadFile(filePath, new URL(downloadURL));
            newFile.setReadable(true);
            newFile.setWritable(true);
            if(oldFile != null && oldFile.exists() && oldFile.isFile()){
                this.deleteFile(oldFile);
            }
            
//            outdatedFilesMap.put(downloadURL, newFile);
            if(filePath.contains("jMovieManager.jar")){
                newFile.setExecutable(true);
                fileToExecute = newFile;
            }
        }
        
        if(fileToExecute != null){      
            this.execute(fileToExecute);
        }
        System.exit(0);
    }   
    
    /**
     * Executes a file in a seperate process.
     * 
     * @param file The file to execute.
     * @throws IOException - If an I/O error occurs
     */
    private void execute(File file) throws IOException{
        Process ps=Runtime.getRuntime().exec(new String[]{"java","-jar",
            file.getAbsolutePath()
        });                   
    }
    
    /**
     * Renames a file.
     * 
     * @param file The file to rename.
     * @param newFileName The new filename.
     * @return true If the file was successfully renamed. <br/> false otherwise
     */
    private boolean renameTo(File file, String newFileName){
        File newwFile = new File(newFileName);
        if(file.canRead() && !newwFile.exists()){
            return file.renameTo(newwFile);
        }
        return false;
    }
    
    /**
     * Deletes a file.
     * 
     * @param file The file to delete.
     * @return true If the file was successfully deleted. <br/> false otherwise
     */
    private boolean deleteFile(File file){
        if(file.exists() && file.canWrite()){
            return file.delete();
        }
        return false;
    }    
    
         
    /**
     * Downloads a file from the url to the given filePath
     * 
     * @param filePath The filePath for the file to download
     * @param downloadUrl The download URL 
     * 
     * @throws IOException - if an I/O error occurs. 
     * 
     * @return The downloaded file.
     */
    private File downloadFile(String filePath, URL downloadURL) throws IOException {
    	BufferedInputStream in = null;
    	FileOutputStream fout = null;
        try {    	
            in = new BufferedInputStream((downloadURL).openStream());
            fout = new FileOutputStream(filePath);
            byte data[] = new byte[1024];
            int count;
            while ((count = in.read(data, 0, 1024)) != -1){
                fout.write(data, 0, count);
            }
        } finally{
            if (in != null){
                in.close();
            }
            if (fout != null) {
                fout.close();
            }
            return new File(filePath);
    	}    
    }
}
