/**
 * Copyright (c) 2010-2015 Bryan Beck.
 * All rights reserved.
 * 
 * This project is licensed under LGPL v2.1.
 * See jMovieManager-license.txt for details.
 * 
 */
package jMovieManager.swing.handler;

import jmm.log.MyExceptionHandler;
import jMovieManager.swing.gui.BugReportGUI;
import jMovieManager.swing.gui.MovieManagerGUI;
import jmm.utils.Settings;

/**
 * 
 * @author Bryan Beck
 * @since 12.04.2013
 */
public class GUIExceptionHandler extends MyExceptionHandler{
    
    private BugReportGUI reportError = new BugReportGUI(MovieManagerGUI.getInstance(), true);
    /**
   * @see UncaughtExceptionHandler#uncaughtException(java.lang.Thread, java.lang.Throwable) 
   */
  @Override
  public void uncaughtException(Thread t, Throwable ex) {
      super.uncaughtException(t, ex);
      if(jmm.utils.Utils.isInternetConnectionAvailable()){
        if(Settings.getInstance().getNumberOfBugReports() < Settings.maxNumberOfBugReports){
            showErrorReportDialog();
        }
      }
  }            
  
  /**
   * Displays a ErrorReportGUI to send the bug to the developers
   */
  private void showErrorReportDialog(){
//      BugReportGUI reportError = new BugReportGUI(MovieManagerGUI.getInstance(), true);
      //if(!Settings.getInstance().isSendBugReportAutomatically()){
          reportError.setVisible(true);
//      }else{
//          reportError.sendBugReport();
//      }
  }
}
