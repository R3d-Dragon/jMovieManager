/**
 * Copyright (c) 2010-2015 Bryan Beck.
 * All rights reserved.
 * 
 * This project is licensed under LGPL v2.1.
 * See jMovieManager-license.txt for details.
 * 
 */
package jmm.log;

import java.lang.Thread.UncaughtExceptionHandler;
import jmm.persist.PersistingManager;
import jmm.utils.LocaleManager;
import jmm.utils.OperatingSystem;
import jmm.utils.Settings;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;

/**
 * Exception handler to log and report uncaught runtime exceptions
 * 
 * @author Bryan Beck
 * @since 28.11.2012
 */
public class MyExceptionHandler implements UncaughtExceptionHandler {
    /** Logger. */
    private static final Logger LOG = LoggerFactory.logger(MyExceptionHandler.class);
        
  /**
   * @see UncaughtExceptionHandler#uncaughtException(java.lang.Thread, java.lang.Throwable) 
   */
  @Override
  public void uncaughtException(Thread t, Throwable ex) {
      String LINE_SEPARATOR = System.getProperty("line.separator");
      StringBuilder sb = new StringBuilder("Config Details ");
      sb.append(LINE_SEPARATOR).append("Operating system:\t\t").append(System.getProperty("os.name"));
      sb.append(LINE_SEPARATOR).append("System version:\t\t\t").append(System.getProperty("os.version"));
      sb.append(LINE_SEPARATOR).append("System architecture:\t\t").append(System.getProperty("os.arch"));
      sb.append(LINE_SEPARATOR).append("Java version:\t\t\t").append(System.getProperty("java.version"));      
      sb.append(LINE_SEPARATOR).append("Java vendor:\t\t\t").append(System.getProperty("java.vendor"));  
//      sb.append(LINE_SEPARATOR).append("Java VM version:\t\t").append(System.getProperty("java.vm.version"));      
//      sb.append(LINE_SEPARATOR).append("Java VM vendor:\t\t\t").append(System.getProperty("java.vm.vendor"));  
      sb.append(LINE_SEPARATOR).append("Internet con available:\t\t").append(String.valueOf(jmm.utils.Utils.isInternetConnectionAvailable()));
      sb.append(LINE_SEPARATOR).append("jMM Version:\t\t\t").append(String.valueOf(Settings.currentVersion));
      sb.append(LINE_SEPARATOR).append("jMM Language:\t\t\t").append(String.valueOf(LocaleManager.getInstance().getCurrentLocale()));
      sb.append(LINE_SEPARATOR).append("MediaInfo library found:\t").append(String.valueOf(OperatingSystem.lookForLibrary()));
      sb.append(LINE_SEPARATOR).append("HSQL DB server state:\t\t");
      int serverState = PersistingManager.INSTANCE.getServerState();
      if(serverState == 1){
          sb.append("ONLINE");
      }else if(serverState == 4){
          sb.append("OPENING");
      }else if(serverState == 8){
          sb.append("CLOSING"); 
      }else if(serverState == 16){
          sb.append("SHUTDOWN"); 
      }
      sb.append(LINE_SEPARATOR);
      LOG.info(sb.toString());       
      //Schwerwiegender Fehler
      LOG.error("Uncaught exception in thread: " + t.getName(), ex);
  }
}
