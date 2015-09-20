/**
 * Copyright (c) 2010-2015 Bryan Beck.
 * All rights reserved.
 * 
 * This project is licensed under LGPL v2.1.
 * See jMovieManager-license.txt for details.
 * 
 */
package jmm.utils;

import java.util.HashMap;
import jmm.data.Movie;
import jmm.interfaces.RegexInterface;
import static jmm.interfaces.RegexInterface.REGEX_RATING;
import static jmm.interfaces.RegexInterface.REPLACEMENT_RATING;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;

/**
 *
 * @author Bryan Beck 
 *
 * @since 20.10.2010
 */
public class MediaInfo implements RegexInterface{
    /** Logger. */
    private static final Logger LOG = LoggerFactory.logger(MediaInfo.class);

    private final String mediaInfoVersion;
    private final MediaInfoDLLWrapper mediaInfoObject; 
    private static MediaInfo instance = new MediaInfo();

    /**
     * Singleton Konstruktor, damit die Library nur 1x geladen wird
     */
    private MediaInfo(){
        mediaInfoVersion = MediaInfoDLLWrapper.Option_Static("Info_Version");
        //Disable Internet for MediaLib
        MediaInfoDLLWrapper.Option_Static("Internet", "No");
        //Debug Only
//      String availableParameters = MediaInfoDLLWrapper.Option_Static("Info_Parameters");
//      System.out.println(availableParameters);
//        System.out.println(mediaInfoVersion);
        mediaInfoObject = new MediaInfoDLLWrapper();
    }

    /**
     * Singleton Instanz der Klasse MediaInfo
     * @return instance - Die Instanz der Klasse
     */
    public static MediaInfo getInstance(){
        if(instance == null){
            instance = new MediaInfo();
        }
        return instance;
    }

   /**
    * Methode zum analysieren des Fileheaders mittels MediaInfo.dll<br/>
    * <b>Hashmap           - Die Map mit den Informationen</b>
    * <ul>
    * <li>playtime          - in ms</li>
    * <li>overallBitrate    - in bps</li>
    * <li>format            - Container</li>
    * <li>formatInfo        - Beschreibung</li>
    *
    * <li>numberOfVideoStreams</li>
    * <li>videoFormat</li>
    * <li>videoFormatInfo</li>
    * <li>videoCodecID</li>
    * <li>videoCodecIDInfo</li>
    * <li>videoBitrate      - in bps</li>
    * <li>videoPlaytime     - in ms</li>
    * <li>averageFPS</li>
    * <li>averageFPSNominal</li>
    * <li>width</li>
    * <li>widthOriginal</li>
    * <li>height</li>
    * <li>heightOriginal</li>
    *
    * <li>numberOfAudioStreams</li>
    * <li>audioFormat</li>
    * <li>audioFormatInfo</li>
    * <li>audioCodecID</li>
    * <li>audioCodecIDInfo</li>
    * <li>audioBitrate      - in bps</li>
    * <li>audioChannels</li>
    * <li>audioPlaytime</li>
    * </ul>
    * 
    * @return true If the file header analyse was successfull <br/> false otherwise
     */
    private synchronized boolean analyseFileHeader(HashMap map, String filePath){
        //File Einlesen und analysieren
        if((filePath != null) && (mediaInfoObject.Open(filePath) == 1)){
            //General
            map.put("playtime", (mediaInfoObject.Get(MediaInfoDLLWrapper.StreamKind.General, 0 , "Duration")));
            map.put("overallBitrate", (mediaInfoObject.Get(MediaInfoDLLWrapper.StreamKind.General, 0 , "OverallBitRate")));
            map.put("format", (mediaInfoObject.Get(MediaInfoDLLWrapper.StreamKind.General, 0 , "Format")));
            map.put("formatInfo", (mediaInfoObject.Get(MediaInfoDLLWrapper.StreamKind.General, 0 , "Format/Info")));

            //Video
            map.put("numberOfVideoStreams",(mediaInfoObject.Get(MediaInfoDLLWrapper.StreamKind.Video, 0, "StreamCount")));
            map.put("videoFormat",(mediaInfoObject.Get(MediaInfoDLLWrapper.StreamKind.Video, 0, "Format")));
            map.put("videoFormatInfo",(mediaInfoObject.Get(MediaInfoDLLWrapper.StreamKind.Video, 0, "Format/Info")));
            map.put("videoCodecID",(mediaInfoObject.Get(MediaInfoDLLWrapper.StreamKind.Video, 0, "CodecID")));
            map.put("videoCodecIDInfo",(mediaInfoObject.Get(MediaInfoDLLWrapper.StreamKind.Video, 0, "CodecID/Info")));
            map.put("videoBitrate",(mediaInfoObject.Get(MediaInfoDLLWrapper.StreamKind.Video, 0, "BitRate")));
            map.put("videoBitrateNominal",(mediaInfoObject.Get(MediaInfoDLLWrapper.StreamKind.Video, 0, "BitRate_Nominal")));
            map.put("videoPlaytime",(mediaInfoObject.Get(MediaInfoDLLWrapper.StreamKind.Video, 0, "Duration")));
            map.put("averageFPS",(mediaInfoObject.Get(MediaInfoDLLWrapper.StreamKind.Video, 0, "FrameRate")));
            map.put("averageFPSNominal",(mediaInfoObject.Get(MediaInfoDLLWrapper.StreamKind.Video, 0, "FrameRate_Nominal")));
            map.put("width",(mediaInfoObject.Get(MediaInfoDLLWrapper.StreamKind.Video, 0, "Width")));
            map.put("widthOriginal",(mediaInfoObject.Get(MediaInfoDLLWrapper.StreamKind.Video, 0, "Width_Original")));
            map.put("height",(mediaInfoObject.Get(MediaInfoDLLWrapper.StreamKind.Video, 0, "Height")));
            map.put("heightOriginal",(mediaInfoObject.Get(MediaInfoDLLWrapper.StreamKind.Video, 0, "Height_Original")));
            
            //Audio
            map.put("numberOfAudioStreams",(mediaInfoObject.Get(MediaInfoDLLWrapper.StreamKind.Audio, 0, "StreamCount")));
            map.put("audioFormat",(mediaInfoObject.Get(MediaInfoDLLWrapper.StreamKind.Audio, 0, "Format")));
            map.put("audioFormatInfo",(mediaInfoObject.Get(MediaInfoDLLWrapper.StreamKind.Audio, 0, "Format/Info")));
            map.put("audioCodecID",(mediaInfoObject.Get(MediaInfoDLLWrapper.StreamKind.Audio, 0, "CodecID")));
            map.put("audioCodecIDInfo",(mediaInfoObject.Get(MediaInfoDLLWrapper.StreamKind.Audio, 0, "CodecID/Info")));
            map.put("audioBitrate",(mediaInfoObject.Get(MediaInfoDLLWrapper.StreamKind.Audio, 0, "BitRate")));
            map.put("audioBitrateNominal",(mediaInfoObject.Get(MediaInfoDLLWrapper.StreamKind.Audio, 0, "BitRate_Nominal")));
            map.put("audioChannels",(mediaInfoObject.Get(MediaInfoDLLWrapper.StreamKind.Audio, 0, "Channel(s)")));
            map.put("audioPlaytime",(mediaInfoObject.Get(MediaInfoDLLWrapper.StreamKind.Audio, 0, "Duration")));
            
            mediaInfoObject.Close();
            return true;
        }
        return false;
    }
    
    /**
     * Searches in the fileheader for video information <br/>
     * and put them into the movie object
     * 
     *
     * @param movie the movie to put the fileheader information
     * @param filePath the filepath to analyse
    */
    public void analyseFileHeader(Movie movie, String filePath){ 
        HashMap<String, String> tempHashMap = new HashMap<>();
        if(MediaInfo.getInstance().analyseFileHeader(tempHashMap, filePath)){
            String value;
            //Playtime
            if(!tempHashMap.get("playtime").isEmpty()){
                value = tempHashMap.get("playtime").replaceAll(REGEX_DIGITS_ONLY, REPLACEMENT_DIGITS_ONLY);
                if(!value.isEmpty()){
                    movie.setPlaytime((int)((Double.parseDouble(value) / 1000) / 60));
                }
            }
            //Video Codec
            String key = Utils.formatVideoCodec(tempHashMap.get("videoCodecID"));
            movie.setVideoCodec(LocaleManager.getInstance().determineVideoCodecKeyByLocale(key));
            //Video Bitrate
            if(!tempHashMap.get("videoBitrate").isEmpty()){
                value = tempHashMap.get("videoBitrate").replaceAll(REGEX_DIGITS_ONLY, REPLACEMENT_DIGITS_ONLY);
                if(!value.isEmpty()){
                    movie.setVideoBitrate(Integer.parseInt(value) / 1024);
                }
            }else if(!tempHashMap.get("videoBitrateNominal").isEmpty()){
                value = tempHashMap.get("videoBitrateNominal").replaceAll(REGEX_DIGITS_ONLY, REPLACEMENT_DIGITS_ONLY);
                if(!value.isEmpty()){
                    movie.setVideoBitrate(Integer.parseInt(value) / 1024);
                }
            }           
            //Audio Codec
            key = Utils.formatAudioCodec(tempHashMap.get("audioCodecID"));
            movie.setAudioCodec(LocaleManager.getInstance().determineAudioCodecKeyByLocale(key));
             //Audio Bitrate
            value = tempHashMap.get("audioBitrate");
            if(!value.isEmpty()){
                if(value.contains(" / ")){
                    value = value.substring(0, value.indexOf(" / ")).replaceAll(REGEX_DIGITS_ONLY, REPLACEMENT_DIGITS_ONLY);
                    if(!value.isEmpty()){
                        movie.setAudioBitrate(Integer.parseInt(value) / 1024);
                    }
                }else{
                    value = value.replaceAll(REGEX_DIGITS_ONLY, REPLACEMENT_DIGITS_ONLY);
                    if(!value.isEmpty()){
                        movie.setAudioBitrate(Integer.parseInt(value) / 1024);
                    }
                }
            }else if(!tempHashMap.get("audioBitrateNominal").isEmpty()){
                value = tempHashMap.get("audioBitrateNominal");
                if(value.contains(" / ")){
                    value = value.substring(0, value.indexOf(" / ")).replaceAll(REGEX_DIGITS_ONLY, REPLACEMENT_DIGITS_ONLY);
                    if(!value.isEmpty()){
                        movie.setAudioBitrate(Integer.parseInt(value) / 1024);
                    }
                }else{
                    value = value.replaceAll(REGEX_DIGITS_ONLY, REPLACEMENT_DIGITS_ONLY);
                    if(!value.isEmpty()){
                        movie.setAudioBitrate(Integer.parseInt(value) / 1024);
                    }
                }
            }
            //Audio Channels             
            value = tempHashMap.get("audioChannels");
            if(!value.isEmpty()){
                if(value.contains(" / ")){
                    value = value.substring(0, value.indexOf(" / ")).replaceAll(REGEX_DIGITS_ONLY, REPLACEMENT_DIGITS_ONLY);
                    if(!value.isEmpty()){
                        movie.setAudioChannels(Integer.parseInt(value));
                    }
                }else{
                    value = value.replaceAll(REGEX_DIGITS_ONLY, REPLACEMENT_DIGITS_ONLY);
                    if(!value.isEmpty()){
                        movie.setAudioChannels(Integer.parseInt(value));
                    }
                }
            }
            //FPS
            if(!tempHashMap.get("averageFPS").isEmpty()){
                value = tempHashMap.get("averageFPS").replaceAll(REGEX_RATING, REPLACEMENT_RATING);
                if(!value.isEmpty()){
                    movie.setAverageFPS(Float.parseFloat(value));
                }
            }
            else if(!tempHashMap.get("averageFPSNominal").isEmpty()){
                value = tempHashMap.get("averageFPSNominal").replaceAll(REGEX_RATING, REPLACEMENT_RATING);
                if(!value.isEmpty()){
                    movie.setAverageFPS(Float.parseFloat(value));
                }
            }

            //Width
            if(!tempHashMap.get("width").isEmpty()){
                value = tempHashMap.get("width").replaceAll(REGEX_DIGITS_ONLY, REPLACEMENT_DIGITS_ONLY);
                if(!value.isEmpty()){
                    movie.setWidth(Integer.parseInt(value));
                }
            }
            else if(!tempHashMap.get("widthOriginal").isEmpty()){
                value = tempHashMap.get("widthOriginal").replaceAll(REGEX_DIGITS_ONLY, REPLACEMENT_DIGITS_ONLY);
                if(!value.isEmpty()){
                    movie.setWidth(Integer.parseInt(value));
                }
            }
            //Height
            if(!tempHashMap.get("height").isEmpty()){         
                value = tempHashMap.get("height").replaceAll(REGEX_DIGITS_ONLY, REPLACEMENT_DIGITS_ONLY);            
                if(!value.isEmpty()){
                    movie.setHeight(Integer.parseInt(value));
                }
            }
            else if(!tempHashMap.get("heightOriginal").isEmpty()){
                value = tempHashMap.get("heightOriginal").replaceAll(REGEX_DIGITS_ONLY, REPLACEMENT_DIGITS_ONLY);
                if(!value.isEmpty()){
                    movie.setHeight(Integer.parseInt(value));
                }
            }
        }
    }
}
