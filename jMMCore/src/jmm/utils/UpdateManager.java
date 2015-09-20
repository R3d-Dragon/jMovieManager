/**
 * Copyright (c) 2010-2015 Bryan Beck.
 * All rights reserved.
 * 
 * This project is licensed under LGPL v2.1.
 * See jMovieManager-license.txt for details.
 * 
 */
package jmm.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.List;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;

/**
 *
 * @author Bryan Beck
 * @since 21.12.2012
 */
public abstract class UpdateManager {
    /** Logger. */
    private static final Logger LOG = LoggerFactory.logger(UpdateManager.class);
    
    /**
     * Creates a new silentUpdateProcess
     * 
     * @param resources the resources to download and to update
     * @return The thread with the process
     */
    public static Thread createSilentUpdateProcess(final LinkedList<String> resources){
        return new Thread(){
            @Override
            public void run() {
                try{
//                  //String jarPath = "G:\\Bryan\\Projekte\\NetBeans\\jMovieManager\\dist\\jMovieManager.jar";
//                  String jarPath = UpdateManager.class.getProtectionDomain().getCodeSource().getLocation().getFile();
//                  String decodedjarPath = URLDecoder.decode(jarPath, "UTF-8");
//                  File jar = new File(decodedjarPath);
//                  if(jar != null && jar.exists()){
                      File dir = Settings.getInstance().getSettingsFile().getParentFile();
                      if(dir.exists() && dir.isDirectory()){
  //                                System.out.println(jar.getAbsolutePath());
//                                  System.out.println(dir.getAbsolutePath());
                          //only works with distributed .jar file
                          //Does not work on debug mode inside the IDE
                              File updateAgent = new File(dir, "lib");
                              if(updateAgent.exists() && updateAgent.isDirectory()){
                                  updateAgent = new File(updateAgent, "UpdateAgent.jar");
                                  if(updateAgent.exists() && updateAgent.isFile()){
//                                      System.out.println(updateAgent.getAbsolutePath());
                                      resources.addFirst("true");                        //2 Parameter
                                      resources.addFirst(dir.getAbsolutePath());          //1 Parameter
                                      resources.addFirst(updateAgent.getAbsolutePath());
                                      resources.addFirst("-jar");
                                      resources.addFirst("java");
//                                      for(String arg: resources){
//                                          System.out.print(arg);
//                                      }
                                      Process ps=Runtime.getRuntime().exec(resources.toArray(new String[resources.size()])); 
                                      ps.waitFor();
                                  }
                              }

                      }                     
//                  }
              }catch (IOException | InterruptedException e){
                  LOG.error("Cannot find update application for silent update.", e);
              }
            }
        };
    }
            
    /**
     * Returns a list of requiered resources (download urls) for a silent update
     * @return A list of download urls <br/> null If a full update is requiered
     */
    public static List<String> getRequieredResources(){
        List<String> resources = new LinkedList<String>();         
        URLConnection con;
        final String baseURL = "http://files.jmoviemanager.de/silent/resources.txt";
        final char delimiter = ';';
        try{
            con = new URL(baseURL).openConnection();
            //Fake den Firefox, um HTTP Anfragen senden zu dürfen
            con.addRequestProperty("user-agent", "Firefox");
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            String line;
            while((line = br.readLine()) != null){
                if(!line.startsWith("#") && (!line.trim().isEmpty())){
                    if(line.startsWith("full-update=")){
                        if(line.contains(String.valueOf(Settings.currentVersion) + delimiter)){
                            return null;
                        }
                    }
                    else if(line.startsWith("update=") || line.startsWith("partial-update=")){
                        //ignore these two lines
                    }
                    else{
                        resources.add(line);
                    }
                }
            }
            br.close();
        }catch(FileNotFoundException e){
            LOG.error("File not found.", e);
        }catch(IOException e){
            LOG.warn("Please check your internet connectivity.", e);
        }   
        return resources;
    }
    
    
    /**
     * Checks if an update is available
     * @return the number of the latest version
     */        
    public static double getLatestVersion(){       
        URLConnection con;
        final String baseURL = "http://files.jmoviemanager.de/rls/latestVersion.txt";
        double latestVersion = Settings.currentVersion;
        try{
            con = new URL(baseURL).openConnection();
            //Fake den Firefox, um HTTP Anfragen senden zu dürfen
            con.addRequestProperty("user-agent", "Firefox");
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            String line;
            while((line = br.readLine()) != null){
                try{
                    latestVersion = Double.valueOf(line);
                }
                catch(NumberFormatException ex){
                    LOG.error("Cannot parse double value: " + line, ex);
                }
            }
            br.close();
        }catch(FileNotFoundException e){
            LOG.error("File not found.", e);
        }catch(IOException e){
            LOG.warn("Please check your internet connectivity.", e);
        }
        return latestVersion;
    }
}
