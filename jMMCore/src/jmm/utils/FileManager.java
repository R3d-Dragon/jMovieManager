/**
 * Copyright (c) 2010-2015 Bryan Beck.
 * All rights reserved.
 * 
 * This project is licensed under LGPL v2.1.
 * See jMovieManager-license.txt for details.
 * 
 */
package jmm.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.List;
import jmm.data.Movie;
import jmm.data.collection.MovieCollection;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;

/**
 * FileManager which provides Methods to modify physical files (i.E. copy /
 * rename / delete )
 *
 * @author Bryan Beck
 * @since 23.11.2012
 */
public class FileManager {
    /** Logger. */
    private static final Logger LOG = LoggerFactory.logger(FileManager.class);

    /**
     * Defines the Pattern options to rename your files
     */
    public enum RenamePattern {

        NONE,
        TITLE,
        ORIGINAL_TITLE,
        PLAYTIME,
        RELEASE_YEAR,
        VIDEO_CODEC,
        AUDIO_CODEC,
        SOURCE
    }

    /**
     * Defines if the filename should be written in upper, lower case or without
     * modification
     */
    public enum Case {

        UPPER_CASE,
        LOWER_CASE,
        DEFAULT
    }
    private final MovieCollection collection;
    private final RenamePattern[] params;
    private final String preAttach;
    private final String postAttach;
    private final String seperator;
    public  Case fileNameCase;

    /**
     * Creates a new FileManager
     *
     * @param collection the collection where the element filepaths should be
     * renamed
     * @param params RenamePattern options to specify your own pattern
     * @param preAttach The pre-attachment string
     * @param postAttach The post-attachment string
     * @param seperator The seperator between each RenamePattern option (i.E. -
     * , _ . )
     * @param fileNameCase Defines if the filename should be written in upper,
     * lower case or without modification
     *
     */
    public FileManager(MovieCollection collection, RenamePattern[] params, String preAttach, String postAttach, String seperator, Case fileNameCase) {
        this.collection = collection;
        this.params = params;
        this.preAttach = preAttach;
        this.postAttach = postAttach;
        this.seperator = seperator;
        this.fileNameCase = fileNameCase;
    }

    /**
     * Renames all the filepaths in the collection after the given pattern
     *
     */
    public void renameFiles() {
        //TODO: Erweitern auf Serien, Songs
        List<String> filePaths;
        int numberOfFilePaths;
        int iStart;
        int iStop;
        int indexOfLastSeperator;
        String newFileName;

        for (Movie movie : collection.getAll()) {
            newFileName = "";
            //filePaths.clear();
            if (!movie.getFilePaths().isEmpty()) {
                filePaths = movie.getFilePaths();
                numberOfFilePaths = filePaths.size();
                for (int i = 0; i < numberOfFilePaths; i++) {
                    File movieFile = new File(movie.getFilePath(i));
                    iStart = 0;
                    iStop = 0;
                    //Create newFileName
                    if (!preAttach.isEmpty()) {
                        newFileName = preAttach + seperator;
                    }

                    for (int j = 0; j < params.length; j++) {
                        switch (params[j]) {
                            case NONE:
                                break;
                            case TITLE:
                                newFileName += movie.getTitle() + seperator;
                                break;
                            case ORIGINAL_TITLE:
                                newFileName += movie.getOriginalTitle() + seperator;
                                break;
                            case PLAYTIME:
                                newFileName += movie.getPlaytime() + seperator;
                                break;
                            case RELEASE_YEAR:
                                newFileName += movie.getReleaseYear() + seperator;
                                break;
                            case VIDEO_CODEC:
                                newFileName += movie.getVideoCodec() + seperator;
                                break;
                            case AUDIO_CODEC:
                                newFileName += movie.getAudioCodec() + seperator;
                                break;
                            case SOURCE:
                                newFileName += movie.getVideoSource() + seperator;
                                break;
                            default:
                                break;
                        }
                    }

                    if (!postAttach.isEmpty()) {
                        newFileName += postAttach + seperator;
                    }
                    //< > ? " : | \ / *
                    newFileName = newFileName.replace("<", "");
                    newFileName = newFileName.replace(">", "");
                    newFileName = newFileName.replace("?", "");
                    newFileName = newFileName.replace('"' + "", "");
                    newFileName = newFileName.replace(":", "");
                    newFileName = newFileName.replace("|", "");
                    newFileName = newFileName.replace("\\", "");
                    newFileName = newFileName.replace("/", "");
                    newFileName = newFileName.replace("*", "");
                    //entferne 2 serperator hintereinander
                    while ((iStart > -1) && (iStop > -1)) {
                        iStart = newFileName.indexOf(seperator, iStop + 1);
                        iStop = newFileName.indexOf(seperator, iStart + 1);
                        if ((iStart > -1) && (iStop > -1) && (iStart + 1 == iStop)) {
                            newFileName = newFileName.substring(0, iStart) + newFileName.substring(iStop);
                        }
                    }
                    //Remove last Seperator
                    indexOfLastSeperator = newFileName.lastIndexOf(seperator);
                    if (indexOfLastSeperator > -1) {
                        newFileName = newFileName.substring(0, indexOfLastSeperator);
                    }
                    //Add extinction
                    iStop = movieFile.getAbsolutePath().lastIndexOf(".");
                    newFileName += movieFile.getAbsolutePath().substring(iStop);

                    //Groß- und Kleinschreibung des Dateinamens
                    if (fileNameCase == Case.UPPER_CASE) {
                        newFileName = newFileName.toUpperCase();
                    } else if (fileNameCase == Case.LOWER_CASE) {
                        newFileName = newFileName.toLowerCase();
                    }

                    File newFile = new File(movieFile.getParentFile(), newFileName);
                    //check if new filename length > 255
                    if (newFile.getAbsolutePath().length() < 255) {
                        //check if file already exists
                        if (!newFile.exists()) {
                            //Rename Movie File
                            movieFile.renameTo(newFile);
                            //actualize filePath in Movie Object
                            movie.replaceFilePath(i, newFile.getAbsolutePath());
                        }
                    }
                }
            }
        }
    }

    /**
     * copies a file or directory from the source to the destination
     *
     * @param destination the destination file
     * @param source the source file to copy
     * @param recursive true, if all files inside the dir should be copied too
     *
     * @throws FileNotFoundException If the source was not found
     * @throws IOException
     */
    public static void copy(File source, File destination, boolean recursive) throws IOException {
        if (!source.exists()) {
            throw new IllegalArgumentException("Source directory (" + source.getPath() + ") doesn't exist.");
        }
        if (source.isDirectory()) {
            copyDirectory(source, destination, recursive);
        } else {
            copyFile(source, destination);
        }
    }

    /**
     * copies a dir from the source to the destination
     *
     * @param destination the destination file
     * @param source the source file to copy
     * @param recursive true, if all files inside the dir should be copied too
     *
     * @throws FileNotFoundException If the source was not found
     * @throws IOException
     */
    private static void copyDirectory(File source, File destination, boolean recursive) throws IOException {
        if (!source.isDirectory()) {
            throw new IllegalArgumentException("Source (" + source.getPath() + ") must be a directory.");
        }
        destination.mkdirs();
        File[] files = source.listFiles();

        if (recursive) {
            for (File file : files) {
                if (file.isDirectory()) {
                    copyDirectory(file, new File(destination, file.getName()), recursive);
                } else {
                    copyFile(file, new File(destination, file.getName()));
                }
            }
        }
    }


    /**
     * copies a file from the source to the destination
     *
     * @param source the source file to copy
     * @param destination the destination file
     *
     * @throws FileNotFoundException If the source was not found
     * @throws IOException
     */
    private static void copyFile(File source, File destination) throws IOException {
        if (!source.isFile()) {
            throw new IllegalArgumentException("Source (" + source.getPath() + ") must be a file.");
        }

        FileChannel sourceChannel = new FileInputStream( source ).getChannel();
        FileChannel targetChannel = new FileOutputStream( destination ).getChannel();
        sourceChannel.transferTo(0, sourceChannel.size(), targetChannel);
        sourceChannel.close();
        targetChannel.close();
    }
}
