/**
 * Copyright (c) 2010-2015 Bryan Beck.
 * All rights reserved.
 * 
 * This project is licensed under LGPL v2.1.
 * See jMovieManager-license.txt for details.
 * 
 */
package jmm.utils;

import jmm.interfaces.OSConstantsInterface;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.URI;
import java.net.URISyntaxException;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;

/**
 * Hier sind alle OS abhaengigen Methoden zu finden
 * 
 * 
 * @author Bryan Beck
 * @since 25.04.2011
 */
public abstract class OperatingSystem implements OSConstantsInterface{
    /** Logger. */
    private static final Logger LOG = LoggerFactory.logger(OperatingSystem.class);

    /**
     * Ermittelt mit hilfe der "osName.name" Eigenschaft unter welchem
     * Betriebssystem die Anwendung laeuft
     *
     * @return true wenn die Anwendung unter Windows lauft
     */
    public static boolean isWindowsPlatform()
    {
        String osName = System.getProperty("os.name");
        //        String osArch = System.getProperty("os.arch");
        //        String osVersion = System.getProperty("os.version");
        if ( osName != null && osName.toLowerCase().startsWith(WIN_ID)){
            return true;
        }
        else{
            return false;
        }
    }
    
    /**
     * Ermittelt mit hilfe der "osName.name" Eigenschaft unter welchem
     * Betriebssystem die Anwendung laeuft
     *
     * @return true wenn die Anwendung unter Unix lauft
     */
    public static boolean isUnixPlatform()
    {
        String osName = System.getProperty("os.name");
        //        String osArch = System.getProperty("os.arch");
        //        String osVersion = System.getProperty("os.version");
        if ( osName != null && osName.toLowerCase().startsWith(UNIX_ID)){
            return true;
        }
        else{
            return false;
        }
    }
    
    /**
     * Ermittelt mit hilfe der "osName.name" Eigenschaft unter welchem
     * Betriebssystem die Anwendung laeuft
     *
     * @return true wenn die Anwendung unter Mac lauft
     */
    public static boolean isMacPlatform()
    {
        String osName = System.getProperty("os.name");
        //        String osArch = System.getProperty("os.arch");
        //        String osVersion = System.getProperty("os.version");
        if ( osName != null && osName.toLowerCase().startsWith(MAC_ID)){
            return true;
        }
        else{
            return false;
        }
    }    
    
    /**
     * Returns true, if the operating system is an 32 bit architecture
     * @return true If the OS is an 32 bit architecture <br/> false otherwise
     */ 
    public static boolean is_x86(){
        String osArch = System.getProperty("os.arch");
        if(osArch != null && osArch.equalsIgnoreCase("x86")){
            return true;
        }else{
            return false;
        }        
    }
    
    /**
     * Returns true, if the operating system is an 64 bit architecture
     * @return true If the OS is an 64 bit architecture <br/> false otherwise
     */
    public static boolean is_x64(){
        String osArch = System.getProperty("os.arch");
        if(osArch != null && (osArch.equalsIgnoreCase("amd64") || osArch.equalsIgnoreCase("x86_64"))){
            return true;
        }else{
            return false;
        }
    }
    
    /**
    * Stellt eine Datei oder eine URL im Standardbrowser des Systems dar.
    *
    * @param url URL oder Dateipfad (Muss mit "http://" oder "file://" beginnen).
    * @throws IOException Will be thrown if the url cannot be opened.
    * @throws URISyntaxException Will be thrown if the url is invalid.
    */
    public static void openBrowser(String url) throws IOException, URISyntaxException{
        //Neue Java 1.6 Klasse
        //TODO: Desktop Klasse funktioniert nicht unter OpenJDK bzw ist nicht in der version 1.6 
        if(Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)){
            Desktop.getDesktop().browse(new URI(url));
        }    
        else{
            String cmd;
            if (isWindowsPlatform()){
                    // cmd = 'rundll32 url.dll,FileProtocolHandler http://...'
                    cmd = WIN_PATH + " " + WIN_FLAG + " " + url;
                    Process p = Runtime.getRuntime().exec(cmd);
            }
            else if(isUnixPlatform()){
                    // Under Unix, Netscape has to be running for the "-remote"
                    // command to work.  So, we try sending the command and
                    // check for an exit value.  If the exit command is 0,
                    // it worked, otherwise we need to start the browser.
                    // cmd = 'netscape -remote openURL(http://www.javaworld.com)'
                    cmd = UNIX_PATH + " " + UNIX_FLAG + "(" + url + ")";
                    Process p = Runtime.getRuntime().exec(cmd);
                    try
                    {
                        // wait for exit code -- if it's 0, command worked,
                        // otherwise we need to start the browser up.
                        int exitCode = p.waitFor();
                        if (exitCode != 0)
                        {
                            // Command failed, start up the browser
                            // cmd = 'netscape http://www.javaworld.com'
                            cmd = UNIX_PATH + " "  + url;
                            Runtime.getRuntime().exec(cmd);
                        }
                    }
                    catch(InterruptedException ex){
                        LOG.error("Error while bringin up browser, cmd='" + cmd + "'", ex);
                    }
            }             
        }
    }
    
    /**
    * Oeffnet die Datei mit dem Standardprogramm
    *
    * @param filePath Der absolute Pfad der Datei
    */   
    public static void openFile(String filePath) throws IOException{
        //Neue Java 1.6 Klasse
        if(Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.OPEN)){
            File fileToOpen = new File(filePath);
            if(fileToOpen.exists() && fileToOpen.isFile()){
                Desktop.getDesktop().open(new File(filePath));
            }
        }    
        else{
            if(isWindowsPlatform()){
                //Runtime.getRuntime().exec(filePath);
                new ProcessBuilder( "cmd", "/c", filePath).start();       
            }
            else if(isUnixPlatform()){
                new ProcessBuilder( "cmd", "/c", filePath).start();               
            }       
        }
    }
    
    /**
    * Sucht nach der MediaInfo Library zum auslesen der Header Informationen<br/><br/>
    * Die Library muss im gleichen Verzeichnis wie die jMovieManager.jar liegen
    * 
    * @return true, wenn Library gefunden wurde
    *
    **/
    public static boolean lookForLibrary(){     
        File library = null;
        
        if(isWindowsPlatform()){
            //Suche im gleichen Verzeichnis für WINDOWS
            if(is_x86()){
                library = new File(WIN_X86_MEDIAINFO);
            }else if(is_x64()){
                library = new File(WIN_X64_MEDIAINFO);
            }
            if(library != null && library.exists() && library.isFile()){
                return true;
            }                           
        }
        else if(isUnixPlatform()){
            library = new File(UNIX_UBUNTU_LIBZEN);
            File lib2 = new File(UNIX_UBUNTU_MEDIAINFO);
            //Suche im Linux Lib Verzeichnis
            if(library.exists() && lib2.exists() && library.isFile() && lib2.isFile()){
                return true;
            }
        }
        else if(isMacPlatform()){
            library = new File(MAC_MEDIAINFO);            
            if(library != null && library.exists() && library.isFile()){
                return true;
            }
        }        
        return false;
    }
    
    /**
     * Determines if another instance of the jMM application is running on the system or not. <br/>
     * This check will open a new socket to port 40101. 
     * If this operation fails, another jMM application is running.
     * 
     * @return true If another jMM instance is running.<br/>false otherwise.
     */
    public static boolean isAnotherJMMInstanceRunning(){
        try {
            final ServerSocket socket = new ServerSocket(40101);
            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        socket.close();
                    } catch (IOException ex) {
                        LOG.error("Cannot close socket.", ex);
                    }
                }
            }));
        }
        catch (IOException e) {
            LOG.warn("Another instance of jMM is already running.", e);
            return true;
        }
        return false;
    }
}
