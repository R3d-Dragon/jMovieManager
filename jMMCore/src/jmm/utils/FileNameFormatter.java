/**
 * Copyright (c) 2010-2015 Bryan Beck.
 * All rights reserved.
 * 
 * This project is licensed under LGPL v2.1.
 * See jMovieManager-license.txt for details.
 * 
 */
package jmm.utils;

import java.util.Map;
import java.util.TreeMap;
import jmm.interfaces.RegexInterface;
import static jmm.interfaces.RegexInterface.REGEX_YEAR;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;

/**
 * Utility class to format file names.
 * 
 * @author Bryan Beck
 * @since 20-09.2013
 */
public class FileNameFormatter implements RegexInterface{
    /** Logger. */
    private static final Logger LOG = LoggerFactory.logger(FileNameFormatter.class);

    
    private final static Character[] possibleSeperators = new Character[]{' ', '.', ',', '_', '-'};        
    private final static String[] searchList = { "Ä", "ä", "Ö", "ö", "Ü", "ü", "ß" };
    private final static String[] replaceList = { "Ae", "ae", "Oe", "oe", "Ue", "ue", "ss" };
    
    //Elements which could be part of a filename
    private boolean replace_FileEnding;
    private boolean replace_AudioChannel;
    private boolean replace_MostOccuredSeperator;
    private boolean replace_ReleaseGroups;
    private boolean replace_VideoCodec;
    private boolean replace_AudioCodec;
    private boolean replace_Source;    
    private boolean replace_QualitySetting;
    private boolean replace_VideoFormat;
    private boolean replace_Language;
    private boolean replace_ReleaseYear;
    private boolean convertUmlauts;
    
    private String customReplacement;
    
    private FileNameFormatter( 
            boolean replaceFileEnding,
            boolean replaceAudioChannel, 
            boolean replaceMostOccuredSeperator,
            boolean replaceReleaseGroups, 
            boolean replaceVideoCodec, 
            boolean replaceAudioCodec, 
            boolean replaceSource, 
            boolean replaceQualitySetting,
            boolean replaceVideoFormat,
            boolean replaceLanguage,
            boolean replaceReleseYear,
            boolean convertUmlauts,
            String customReplacement){
        this.replace_FileEnding = replaceFileEnding;
        this.replace_AudioChannel = replaceAudioChannel;
        this.replace_MostOccuredSeperator = replaceMostOccuredSeperator;
        this.replace_ReleaseGroups = replaceReleaseGroups;
        this.replace_VideoCodec = replaceVideoCodec;
        this.replace_AudioCodec = replaceAudioCodec;
        this.replace_Source = replaceSource;
        this.replace_QualitySetting = replaceQualitySetting;
        this.replace_VideoFormat = replaceVideoFormat;
        this.replace_Language = replaceLanguage;
        this.replace_ReleaseYear = replaceReleseYear;
        this.convertUmlauts = convertUmlauts;
        this.customReplacement = customReplacement;
    }
    
    /**
     * Creates a new FileNameFormatter.
     * 
     * @param replaceAudioAndVideoCodec
     * @param replaceReleaseGroups
     * @param replaceSource
     * @param replaceQualitySetting
     * @param replaceLanguage
     * @param replaceReleseYear
     * @param convertUmlauts true If äöüß should be converted into ae, oe, ue, ss
     * @param customReplacement
     * 
     * @return The FileNameFormatter
     */
    public static FileNameFormatter createFileNameFormatter(boolean replaceAudioAndVideoCodec, 
            boolean replaceReleaseGroups, 
            boolean replaceSource, 
            boolean replaceQualitySetting,
            boolean replaceLanguage,
            boolean replaceReleseYear,
            boolean convertUmlauts,
            String customReplacement){
        if(customReplacement == null){
            customReplacement = "";
        }
        
        return new FileNameFormatter(true,
                replaceAudioAndVideoCodec, 
                true,
                replaceReleaseGroups, 
                replaceAudioAndVideoCodec, 
                replaceAudioAndVideoCodec, 
                replaceSource, 
                replaceQualitySetting, 
                replaceAudioAndVideoCodec, 
                replaceLanguage, 
                replaceReleseYear,
                convertUmlauts,
                customReplacement);
    }
    
    
    /**
     * Formats the filename to be more human readable
     *
     * @param filename The filename
     * @return The formatted filename
     */
    public String format(String filename) {  
        if((filename == null) || (filename.isEmpty())){
            return filename;
        }
        String formattedFileName = filename.toUpperCase();
        
        if(formattedFileName.equals("AUDIO_TS") || formattedFileName.equals("VIDEO_TS")){
            return filename;
        }
                
        //Replace fileending
        if(replace_FileEnding){
            formattedFileName = formattedFileName.replaceAll("\\.(\\w{3,4})$", "");     //4 chars for DIVX
        }      
        //Replace Audio Channel Setting
        if(replace_AudioChannel){
            formattedFileName = formattedFileName.replaceAll("(5\\.1)", "");
        }
        //Replace most occured seperator
        if(replace_MostOccuredSeperator){
            String seperator = determineSeperator(formattedFileName);
            formattedFileName = formattedFileName.replaceAll("[" + seperator + "]", " ");
            //Replace all seperators
    //        formattedFileName = formattedFileName.replaceAll("[.,_-]", " ");
        }
        
        formattedFileName = formattedFileName + " ";

        //replace release groups
        if(replace_ReleaseGroups){
            formattedFileName = formattedFileName.replaceAll("-[A-Z]+\\w* $", " ");
            formattedFileName = formattedFileName.replace("-23THSTREET2", " ");
            formattedFileName = formattedFileName.replace("-23THSTREET", " ");
    //        formattedFileName = formattedFileName.replaceAll("^(\\w{+})-", "");
    //        final String rlsGroups = "AOE|CFY|CIS|HDC|HDS|ICQ4711|MRP|NSANE|NDCC|OYHD|PB|POE|PSO|PWND|RSG|RWP|SOF|SONS|SOV|TVS|VCF|VICE";
    //        formattedFileName = formattedFileName.replaceAll("( (" + rlsGroups + ")|(" + rlsGroups + ") )", " ");
        }
        
        //Replace video codecs
        if(replace_VideoCodec){
            formattedFileName = formattedFileName.replaceAll("( AVC )", " ");
            formattedFileName = formattedFileName.replaceAll("( DIVX PLUS )", " ");
            formattedFileName = formattedFileName.replaceAll("( DIVX )", " ");
            formattedFileName = formattedFileName.replaceAll("( X264 )", " ");
            formattedFileName = formattedFileName.replaceAll("( H(\\.{0,1})264 )", " ");
            formattedFileName = formattedFileName.replaceAll("( XVID )", " ");
            formattedFileName = formattedFileName.replaceAll("( MPEG2 )", " ");
            formattedFileName = formattedFileName.replaceAll("( MPEG )", " ");
            formattedFileName = formattedFileName.replaceAll("( VC(-{0,1})1 )", " ");
        }

        //Replace audio codecs
        if(replace_AudioCodec){
            formattedFileName = formattedFileName.replaceAll("( AC3D )", " ");
            formattedFileName = formattedFileName.replaceAll("( AC3 )", " ");
            formattedFileName = formattedFileName.replaceAll("( DTSHD MA )", " ");
            formattedFileName = formattedFileName.replaceAll("( DTSHD )", " ");
            formattedFileName = formattedFileName.replaceAll("( DTS-HD MA )", " ");
            formattedFileName = formattedFileName.replaceAll("( DTS-HD )", " ");
            formattedFileName = formattedFileName.replaceAll("( DTSMA )", " ");
            formattedFileName = formattedFileName.replaceAll("( DTS )", " ");
        }

        //replace source release information
        if(replace_Source){
            formattedFileName = formattedFileName.replaceAll("( R[1-9]{1} )", " ");
            formattedFileName = formattedFileName.replaceAll("( HDRIP )", " ");
            formattedFileName = formattedFileName.replaceAll("( HDTVRIP )", " ");
            formattedFileName = formattedFileName.replaceAll("( HDTV )", " ");
            formattedFileName = formattedFileName.replaceAll("( HDDVD )", " ");
            formattedFileName = formattedFileName.replaceAll("( DVD-RIP )", " ");
            formattedFileName = formattedFileName.replaceAll("( DVDRIP )", " ");
            formattedFileName = formattedFileName.replaceAll("( DVDSCR )", " ");
            formattedFileName = formattedFileName.replaceAll("( BLURAY )", " ");
            formattedFileName = formattedFileName.replaceAll("( BLU-RAY )", " ");
            formattedFileName = formattedFileName.replaceAll("( BDRIP )", " ");
            formattedFileName = formattedFileName.replaceAll("( BRRIP )", " ");
            formattedFileName = formattedFileName.replaceAll("( BD )", " ");
            formattedFileName = formattedFileName.replaceAll("( WEBRIP )", " ");
            formattedFileName = formattedFileName.replaceAll("( WEB-DL )", " ");
            formattedFileName = formattedFileName.replaceAll("( REMUX )", " ");
            formattedFileName = formattedFileName.replaceAll("( RETAIL )", " ");
        }


        if (formattedFileName.contains("READ") && formattedFileName.contains("NFO")) {
            formattedFileName = formattedFileName.replaceAll("( READ )", " ");
            formattedFileName = formattedFileName.replaceAll("( NFO )", " ");
        }
        
        //Replace release group quality settings
        if(replace_QualitySetting){
            if (formattedFileName.contains("LINE") && formattedFileName.contains("DUBBED")) {
                formattedFileName = formattedFileName.replaceAll("( LINE )", " ");
            }
            formattedFileName = formattedFileName.replaceAll("( DUBBED )", " ");
            formattedFileName = formattedFileName.replaceAll("( (HDTS|TS|TELESYNC) )", " ");
            formattedFileName = formattedFileName.replaceAll("( (LD|MD|REENCODE) )", " ");
            formattedFileName = formattedFileName.replaceAll("( (DL) )", " ");
            //ML (multilanguage)
        }

        //replace video format
        if(replace_VideoFormat){
            formattedFileName = formattedFileName.replaceAll("(HD 1080(P|I)|HD 720(P|I))", "");
            formattedFileName = formattedFileName.replaceAll("(1080(P|I)|720(P|I))", "");
            formattedFileName = formattedFileName.replaceAll("( CD[1-9]{1} )", " ");
        }

        //Possible wrong replacements
        //INTERNAL, ENCOUNTERS,PROPER, UNCUT, UNRATED, PS, PB, RETAIL, EXTENDED CUT

        //Replace language
        if(replace_Language){
            final String languages = "ENGLISH|GERMAN";
            formattedFileName = formattedFileName.replaceAll("( (" + languages + ") )", " ");
        }

        //Repleace Season and Episode tags
//        Pattern extractSeason = Pattern.compile("(EPISODE|FOLGE|X|E|F)( ??)[0-9]{1,3}");
//        Pattern extractSeason = Pattern.compile("(STAFFEL|SEASON|S)( ??)[0-9]{1,2}");
//        Matcher m = extractSeason.matcher(seasonString);
//        while (m.find()) {
//            String s = m.group();            
//            s = s.replaceAll(REGEX_DIGITS_ONLY, REPLACEMENT_DIGITS_ONLY);
//            if(!s.isEmpty()){
//                seasonNr = Integer.valueOf(s);
//            }
//        }              
        //Replace releaseYear
        if(replace_ReleaseYear){
            //replace all years in braces
            formattedFileName = formattedFileName.replaceAll(" \\((" + REGEX_YEAR + ")\\) ", " ");
            formattedFileName = formattedFileName.replaceAll(" \\[(" + REGEX_YEAR + ")\\] ", " ");
            formattedFileName = formattedFileName.replaceAll(" \\{(" + REGEX_YEAR + ")\\} ", " ");
            //replace years without braces
            //be sure that the string contains more than the year
            if(!formattedFileName.replaceAll("( +)", " ").trim().matches(REGEX_YEAR)){
                formattedFileName = formattedFileName.replaceAll(" (" + REGEX_YEAR + ") ", " ");
            }
        }
        
        //custom replacement
        if(!customReplacement.isEmpty()){
            for(String part: customReplacement.split(";")){
                formattedFileName = formattedFileName.replaceAll("(" + part + ")", " ");
            }
        }
        
        //replace öäüß with ae, oe, ue, ss
        if(convertUmlauts){
            for(int i = 0; i < searchList.length; i ++){
                formattedFileName = formattedFileName.replaceAll(searchList[i], replaceList[i]);
            }
        }
        
        //check for double spaces
        formattedFileName = formattedFileName.replaceAll("( +)", " ").trim();
                        
        String newFileName = "";
        //be sure that we didn't delete the filename
        if(formattedFileName.isEmpty()){
            newFileName = filename;
        }else{
            //replace UPPER case with normal letters from the original file string
            for(String part: formattedFileName.split(" ")){
                    newFileName += part.substring(0,1) + part.substring(1).toLowerCase() + " ";
            }
            //Pattern.compile(strptrn, Pattern.CASE_INSENSITIVE + Pattern.LITERAL).matcher(str1).find();
        }
        return newFileName.trim();
    }
    
    /**
     * Determines the most occurenced separator within the filename.
     *
     * @param filename The filename to analyse
     * @return The most occurence separator
     */
    private static String determineSeperator(String filename) {
        Map<Character, Integer> seperatorMap = new TreeMap<Character, Integer>();

        for (int i = 0; i < filename.length(); i++) {
            for (Character seperator : possibleSeperators) {
                if(filename.charAt(i) == seperator.charValue()){
                    Integer value = seperatorMap.get(seperator);
                    if(value == null){
                        seperatorMap.put(seperator, 1);
                    }else{
                        seperatorMap.put(seperator, seperatorMap.get(seperator)+1);
                    }
                    break;
                }
            }
        }
        
        //Get seperator with the highest value
        Integer highestValue = 0;
        Character mostoccured = null;
        for(Character character: seperatorMap.keySet()){
            Integer value = seperatorMap.get(character);
            if(value > highestValue){
                highestValue = value;
                mostoccured = character;
            }
        }
        
        return String.valueOf(mostoccured);
    }
    
//    private String replace_AudioChannel(String fileName){
//        
//    }
//    
//    private String replace_AudioCodec(String fileName){
//        
//    }
//    private String replace_FileEnding(String fileName){
//        
//    }
//    
//    private String replace_Language(String fileName){
//        
//    }
//    
//    private String repleace_MostOccuredSeperator(String fileName){
//        
//    }
//    
//    private String replace_QualitySetting(String fileName){
//        
//    }
//    
//    private String replace_ReleaseGroups(String fileName){
//        
//    }
//    
//    private String replace_ReleaseYear(String fileName){
//        
//    }
//    
//    private String replace_Source(String fileName){
//        
//    }
//    
//    private String replace_VideoCodec(String fileName){
//        
//    }
//    
//    private String replace_VideoFormat(String fileName){
//        
//    }
    
//    public static void main(String args[]) {
//        TreeSet<String> sortedTreeSet = new TreeSet<String>(new Comparator<String>() {
//            @Override
//            public int compare(String o1, String o2) {
//                return o1.compareTo(o2);
//            }
//        });
//        File file = new File("Y:\\Filme");
//        if (file.exists() && file.isDirectory()) {
//            for (File childDir : file.listFiles()) {
////                sortedTreeSet.add(childDir.getName());
//                for (File childFile : childDir.listFiles()) {
//                    sortedTreeSet.add(childFile.getName());
//                }
//            }
//        }
//        for (String fileName : sortedTreeSet) {
//            System.out.println(fileName + "\t - \t" + formatFileName(fileName));
//        }
//    }
}
