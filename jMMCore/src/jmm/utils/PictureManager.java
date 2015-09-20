/**
 * Copyright (c) 2010-2015 Bryan Beck.
 * All rights reserved.
 * 
 * This project is licensed under LGPL v2.1.
 * See jMovieManager-license.txt for details.
 * 
 */
package jmm.utils;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.color.CMMException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import jmm.data.Episode;
import jmm.data.VideoFile;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;

/**
 * Manages all picture related operation
 * 
 * @author Bryan Beck
 * @since 10.01.2013
 */
public class PictureManager {
    /** Logger. */
    private static final Logger LOG = LoggerFactory.logger(PictureManager.class);
    
    //for display in AbstractDetailGUI
    public static final int picture_width = 270;
    public static final int picture_height = 405;
    //for preview in AbstractCreateGUI
    public static final int thumbnail_widht = 150;
    public static final int thumbnail_height = 225;
    //for preview in ChooseMediaFileDialog
    public static final int tmdb_thumbnail_widht = 92;  //75;
    public static final int tmdb_thumbnail_height = 138;//95;
    
    /**
     * Returns the hash of the file. <br/> 
     * The hash is generated over different videoFile attributes.
     * 
     * @param videoFile The videoFile to generate the hash
     * @return the hash value <br/> -1 If an error occured
     */
    public static int getHash(VideoFile videoFile){
        int hash = -1;
        try{
        if(videoFile != null){
            if(videoFile instanceof Episode){
                Episode episode = (Episode)videoFile;
                String collectionName = episode.getSeason().getSerie().getCollection().getName();
                 hash = (collectionName + episode.getSeason().getSeasonNumber() + episode.getTitle() + episode.getImagePath()).hashCode();
            }else{
                hash = (videoFile.getCollection().getName() + videoFile.getTitle() + videoFile.getImagePath()).hashCode();
            }
        }
        }catch(NullPointerException ex){
            LOG.error("Collection for videoFile" + videoFile.getTitle() + " is null.", ex);
            //TODO: Fehler rausbekommen:
            /*
            Nov 13, 2013 11:30:26 PM jmm.log.MyExceptionHandler uncaughtException
            Information: Config Details 
            Operating system:		Windows 8
            System version:			6.2
            System architecture:		amd64
            Java version:			1.7.0_45
            Java vendor:			Oracle Corporation
            Internet con available:		true
            jMM Version:			1.31
            jMM Language:			de_DE
            MediaInfo library found:	true
            HSQL DB server state:		ONLINE

            Nov 13, 2013 11:30:26 PM jmm.log.MyExceptionHandler uncaughtException
            Schwerwiegend: Uncaught exception in thread: AWT-EventQueue-0
            java.lang.NullPointerException
                    at jmm.utils.PictureManager.getHash(PictureManager.java:44)
                    at jMovieManager.swing.gui.splitpane.right.AbstractDetailGUI.updateVideoDetail(AbstractDetailGUI.java:811)
                    at jMovieManager.swing.gui.splitpane.right.MovieDetailGUI.updateMovieDetail(MovieDetailGUI.java:326)
                    at jMovieManager.swing.gui.splitpane.right.EpisodeDetailGUI.updateEpisodeDetail(EpisodeDetailGUI.java:78)
                    at jMovieManager.swing.gui.splitpane.left.SerieTabGUI.jTree1ValueChanged(SerieTabGUI.java:198)
                    at jMovieManager.swing.gui.splitpane.left.SerieTabGUI.access$100(SerieTabGUI.java:47)
                    at jMovieManager.swing.gui.splitpane.left.SerieTabGUI$2.valueChanged(SerieTabGUI.java:120)
                    at javax.swing.JTree.fireValueChanged(Unknown Source)
                    at javax.swing.JTree$TreeSelectionRedirector.valueChanged(Unknown Source)
                    at javax.swing.tree.DefaultTreeSelectionModel.fireValueChanged(Unknown Source)
                    at javax.swing.tree.DefaultTreeSelectionModel.notifyPathChange(Unknown Source)
                    at javax.swing.tree.DefaultTreeSelectionModel.setSelectionPaths(Unknown Source)
                    at javax.swing.tree.DefaultTreeSelectionModel.setSelectionPath(Unknown Source)
                    at javax.swing.JTree.setSelectionPath(Unknown Source)
                    at javax.swing.plaf.basic.BasicTreeUI.selectPathForEvent(Unknown Source)
                    at javax.swing.plaf.basic.BasicTreeUI$Handler.handleSelection(Unknown Source)
                    at javax.swing.plaf.basic.BasicTreeUI$Handler.mousePressed(Unknown Source)
                    at java.awt.AWTEventMulticaster.mousePressed(Unknown Source)
                    at java.awt.Component.processMouseEvent(Unknown Source)
                    at javax.swing.JComponent.processMouseEvent(Unknown Source)
                    at java.awt.Component.processEvent(Unknown Source)
                    at java.awt.Container.processEvent(Unknown Source)
                    at java.awt.Component.dispatchEventImpl(Unknown Source)
                    at java.awt.Container.dispatchEventImpl(Unknown Source)
                    at java.awt.Component.dispatchEvent(Unknown Source)
                    at java.awt.LightweightDispatcher.retargetMouseEvent(Unknown Source)
                    at java.awt.LightweightDispatcher.processMouseEvent(Unknown Source)
                    at java.awt.LightweightDispatcher.dispatchEvent(Unknown Source)
                    at java.awt.Container.dispatchEventImpl(Unknown Source)
                    at java.awt.Window.dispatchEventImpl(Unknown Source)
                    at java.awt.Component.dispatchEvent(Unknown Source)
                    at java.awt.EventQueue.dispatchEventImpl(Unknown Source)
                    at java.awt.EventQueue.access$200(Unknown Source)
                    at java.awt.EventQueue$3.run(Unknown Source)
                    at java.awt.EventQueue$3.run(Unknown Source)
                    at java.security.AccessController.doPrivileged(Native Method)
                    at java.security.ProtectionDomain$1.doIntersectionPrivilege(Unknown Source)
                    at java.security.ProtectionDomain$1.doIntersectionPrivilege(Unknown Source)
                    at java.awt.EventQueue$4.run(Unknown Source)
                    at java.awt.EventQueue$4.run(Unknown Source)
                    at java.security.AccessController.doPrivileged(Native Method)
                    at java.security.ProtectionDomain$1.doIntersectionPrivilege(Unknown Source)
                    at java.awt.EventQueue.dispatchEvent(Unknown Source)
                    at java.awt.EventDispatchThread.pumpOneEventForFilters(Unknown Source)
                    at java.awt.EventDispatchThread.pumpEventsForFilter(Unknown Source)
                    at java.awt.EventDispatchThread.pumpEventsForHierarchy(Unknown Source)
                    at java.awt.EventDispatchThread.pumpEvents(Unknown Source)
                    at java.awt.EventDispatchThread.pumpEvents(Unknown Source)
                    at java.awt.EventDispatchThread.run(Unknown Source)
             */
        }
        return hash;
    }
       
    /**
    * Creates an scaled image without usage of image.getScaledInstance()
    *
    * @param uri filePath or url to the image
    * @param width the width of the scaled image
    * @param height the height of the scaled image
    *
    * @return the scaled imageIcon or<br/>
    *  null If an error occured
    */
    public static ImageIcon getScaledImage(String uri, int width, int height){
        ImageIcon icon = null;
        try {
            LOG.debug("Try to scale image with URI: " + uri + ", Width: " +  width + ", Height: " + height);
            BufferedImage image = null;
            if(uri.startsWith("http")){
                image = ImageIO.read(new URL(uri));
            }
            else{
                File picture = new File(uri);
                if(picture.exists() && picture.isFile()){
                    image = ImageIO.read(picture);
                }
            }
            if(image != null){
                icon = getScaledImage(image, width, height);
                image.flush();
            }
        } catch (IOException ex) {
            LOG.error("Error while parsing picture with URI: " + uri + ".", ex);
            LOG.info("Width: " + width + " Height: " + height);
        } catch (OutOfMemoryError ex){
            LOG.error("The picture with URI: " + uri + " is too large.", ex);
            LOG.info("Width: " + width + " Height: " + height);
        } catch(CMMException ex){
            LOG.error("Invalid image format for image with URI: " + uri, ex);
            LOG.info("Width: " + width + " Height: " + height);
        }
        return icon;
    }
    
    /**
    * Creates an scaled image without usage of image.getScaledInstance()
    *
    * @param image the bufferedImage
    * @param width the width of the scaled image
    * @param height the height of the scaled image
    *
    * @return the scaled imageIcon or<br/>
    * null If an error occured
    */
    public static ImageIcon getScaledImage(BufferedImage image, int width, int height){
        ImageIcon icon = null;
        try {
            ImageScaler scaler = new ImageScaler();
            image = scaler.scaleImage(image, new Dimension(width, height));
            icon =  new ImageIcon(image);
        } catch (OutOfMemoryError ex) {
            LOG.error("Error while scaling image. Image may be too large.", ex);
        }
        return icon;
    }
    /**
     * Saves the image from the videoFile into the "hsqldb_path:/filename.pics/"
     * 
     * @param file the file to get the image
     * 
     * @throws MalformedURLException If the imagePath contains a unknown URL or a unknown protocol)
     */
    public static void saveImage(VideoFile file) throws MalformedURLException, IOException{
        if((file.getImagePath().isEmpty())){
            return;
        }
        String fileName = String.valueOf(PictureManager.getHash(file));
        String dirPath = Settings.getInstance().getHsqlDbPath() + ".pics";                        
        File outputFile = new File(dirPath);
        if(!outputFile.exists()){
            outputFile.mkdirs();
        }
        if(outputFile.exists() && outputFile.isDirectory()){       
            outputFile = new File(dirPath, fileName+ ".jpg");
            if(!outputFile.exists()){  
                //Get Image
                ImageIcon imageIcon = file.getImage();
                if(imageIcon == null){
                    return;
//                    String imagePath = file.getImagePath();
//                    if(imagePath.startsWith("http")){
//                        imageIcon = new ImageIcon(new URL(imagePath));
//                    }else{
//                        imageIcon = new ImageIcon(imagePath);
//                    }
                }
                //write Picture to file         
                BufferedImage bi = new BufferedImage(imageIcon.getIconWidth(), imageIcon.getIconHeight(), BufferedImage.TYPE_INT_RGB);
                
                Graphics2D g2 = bi.createGraphics();
                g2.drawImage(imageIcon.getImage(), 0, 0, null);
                g2.dispose();
                // Now bi contains the img.
                // Note: img may have transparent pixels in it; if so, okay.
                // If not and you can use TYPE_INT_RGB you will get better
                // performance with it in the jvm.
                LOG.debug("Save image into: " + outputFile.getAbsolutePath());
                ImageIO.write(bi,"JPG",outputFile);
        //        ImageIO.write(bi,"PNG",baos);
        //        ImageIO.write(bi,"GIF",baos);
                bi.flush();
            }
        }
    }
    
    /**
     * Deletes the image from the "hsqldb_path:/filename.pics/" , if exist
     * 
     * @param file the file to get the image
     * @return true If the image file was found and delete <br/> false otherwise
     * 
     */
    public static boolean deleteImage(VideoFile file){
        if((file.getImagePath().isEmpty())){
            return false;
        }
        String fileName = String.valueOf(PictureManager.getHash(file));
        String dirPath = Settings.getInstance().getHsqlDbPath() + ".pics";                        
        File outputFile = new File(dirPath);
        if(outputFile.exists() && outputFile.isDirectory()){       
            outputFile = new File(dirPath, fileName+ ".jpg");
            if(outputFile.exists()){  
                return outputFile.delete();
            }
        } 
        return false;
    }
}
