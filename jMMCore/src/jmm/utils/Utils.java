/**
 * Copyright (c) 2010-2015 Bryan Beck.
 * All rights reserved.
 * 
 * This project is licensed under LGPL v2.1.
 * See jMovieManager-license.txt for details.
 * 
 */
package jmm.utils;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import jmm.interfaces.RegexInterface;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;

/**
 * Utilitie methods for all classes
 *
 * @author Bryan Beck
 *
 * Created on 21.11.2010, 19:58:53
 */
public abstract class Utils implements RegexInterface {
    /** Logger. */
    private static final Logger LOG = LoggerFactory.logger(Utils.class);

    /**
     * Methode zur Umwandlung von Hex zu ASCII Zeichen in einem String richtige
     * Darstellung von umlauten (äüöß"')
     *
     * @param text - String der umgewandelt werden soll
     * @return convertedString - Formatierter String
    *
     */
    public static String convertHextoASCII(String text) {
        String convertedString = text;
        String replacement;
        int decimal;
        int iStart;

        while (convertedString.contains("&#x")) {
            iStart = convertedString.indexOf("&#x");
            replacement = convertedString.substring(iStart + 3, iStart + 5);
            decimal = Integer.parseInt(replacement, 16);
            replacement = ((char) decimal) + "";
            convertedString = convertedString.replaceFirst("&#x[A-Z0-9]{2};", replacement);
        }

        convertedString = convertedString.replace("&#39;", "'");
        convertedString = convertedString.replace('"' + "", "");
        convertedString = convertedString.replace("&quot;", "");
        return convertedString;
    }

    /**
     * Methode zur Umwandlung von ASCII zu Hex Zeichen in einem String Benötigt,
     * damit Suche bei IMDB_DE funktioniert (Umlaute in Hex umwandeln)
     *
     * @param text - String der umgewandelt werden soll
     * @return convertedString - Formatierter String
    *
     */
    public static String convertASCIItoHex(String text) {
        String convertedString = text;
        //Formatiere Umlaute des String
        convertedString = convertedString.replace("&", "&#x26;");
        convertedString = convertedString.replace("'", "&#x27;");
        convertedString = convertedString.replace('"' + "", "&#x22;");
        convertedString = convertedString.replace("ß", "&#xDF;");

        convertedString = convertedString.replace("á", "&#xE1;");
        convertedString = convertedString.replace("â", "&#xE2;");
        convertedString = convertedString.replace("ã", "&#xE3;");
        convertedString = convertedString.replace("ä", "&#xE4;");
        convertedString = convertedString.replace("å", "&#xE5;");
        convertedString = convertedString.replace("æ", "&#xE6;");
        convertedString = convertedString.replace("ç", "&#xE7;");
        convertedString = convertedString.replace("è", "&#xE8;");
        convertedString = convertedString.replace("é", "&#xE9;");

        convertedString = convertedString.replace("ô", "&#xF4;");
        convertedString = convertedString.replace("õ", "&#xF5;");
        convertedString = convertedString.replace("ö", "&#xF6;");

        convertedString = convertedString.replace("ü", "&#xFC;");

        return convertedString;
    }

    /**
     * Formatiert folgende Audio Codecs: 55 - MP3 129 - AC3 2000 - AC3 A_AC3 -
     * AC3 A_DTS - DTS
     *
     * @param audioCodec Unformatierter Audio Codec String (z.b. 55 oder A_DTS)
     * @return Formatierter Audio Codec String (z.b. MP3 oder DTS)
    *
     */
    public static String formatAudioCodec(String audioCodec) {
        if (audioCodec != null) {
            String formattedAudioCodec;
            if (audioCodec.equals("55")) {
                formattedAudioCodec = "MP3";
            } else if (audioCodec.equals("129")) {
                formattedAudioCodec = "AC3";
            } else if (audioCodec.equals("2000")) {
                formattedAudioCodec = "AC3";
            } else {
                //Regex A_ entfernen
                formattedAudioCodec = audioCodec.replaceAll("A_", "");
            }
            return formattedAudioCodec;
        }
        return audioCodec;
    }

    /**
     * Formatiert folgende Video Codecs:
     *
     * 27 - AVC divx - DIVX
     *
     * V_MPEG4/ISO/AVC - MPEG4/ISO/AVC XVID - XVID
     *
     * @param videoCodec Unformatierter Video Codec String
     * @return Formatierter Video Codec String 
    *
     */
    public static String formatVideoCodec(String videoCodec) {
        if (videoCodec != null) {
            String formattedVideoCodec;
            if (videoCodec.equals("27")) {
                formattedVideoCodec = "AVC";
            } else if (videoCodec.equalsIgnoreCase("divx")) {
                formattedVideoCodec = "DIVX";
            } else if (videoCodec.equalsIgnoreCase("XVID")) {
                formattedVideoCodec = "XVID";
            } else {
                //Regex V_ entfernen
                formattedVideoCodec = videoCodec.replaceAll("V_", "");
            }
            return formattedVideoCodec;
        }
        return videoCodec;
    }

    /**
     * Convert ImageIcon to Base64 String
     *
     * @param image Das Bild, welches konvertiert werden soll
     * @param imageFormat 1 = JPG, 2 = PNG, 3 = GIF
     * @return Der konvertierte String
    *
     */
    public static String ImageToBase64(ImageIcon image, int imageFormat) throws IOException {
        String base64String;
        byte[] imageInByte;
        BufferedImage bi;
        if ((image.getIconWidth() < PictureManager.picture_width) && (image.getIconHeight() < PictureManager.picture_height)) {
            bi = new BufferedImage(image.getIconWidth(), image.getIconHeight(), BufferedImage.TYPE_INT_RGB);
        } else if (image.getIconWidth() < PictureManager.picture_width) {
            bi = new BufferedImage(image.getIconWidth(), PictureManager.picture_height, BufferedImage.TYPE_INT_RGB);
        } else if (image.getIconHeight() < PictureManager.picture_height) {
            bi = new BufferedImage(PictureManager.picture_width, image.getIconHeight(), BufferedImage.TYPE_INT_RGB);
        } else {
            bi = new BufferedImage(PictureManager.picture_width, PictureManager.picture_height, BufferedImage.TYPE_INT_RGB);
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        //Zeichne das Bild neu auf das BufferedImage
        Graphics2D g2 = bi.createGraphics();
        g2.drawImage(image.getImage(), 0, 0, null);
        g2.dispose();

        //Quality loose
        if (imageFormat == 1) {
            ImageIO.write(bi, "JPG", baos);
        } else if (imageFormat == 2) {
            ImageIO.write(bi, "PNG", baos);
        } else if (imageFormat == 3) {
            ImageIO.write(bi, "GIF", baos);
        } else {
            return "";
        }


        //Kein Quality loose
//            IIOImage img = new IIOImage(bi, null, null);
//            ImageWriter writer = ImageIO.getImageWritersBySuffix("jpg").next();
//            ImageWriteParam param = writer.getDefaultWriteParam();
//            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
//            //Quality Settings 0 - lowest, 1 - best  0.854 def
//            param.setCompressionQuality(0.854F);
//
//            writer.setOutput(ImageIO.createImageOutputStream(baos));
//            writer.write(null, img, param);

        //Gebe die Resourcen des BufferedImage Objekts wieder frei
        bi.flush();
        imageInByte = baos.toByteArray();
        baos.flush();
        baos.close();

        //DEBUG ONLY
        //saveImage(imageInByte);

        //base64String = new String(imageInByte, "ISO-8859-1"); 
        base64String = Base64.encodeToString(imageInByte, false);
        //base64String = new sun.misc.BASE64Encoder().encode(imageInByte);
        //base64String = javax.xml.bind.DatatypeConverter.printBase64Binary(imageInByte);
//        } catch (Exception ex) {
//            System.out.println("Fehler beim Umwandeln des Bildes in einen String");
//            System.out.println(ex.toString());
//        }
        return base64String;
    }

    /**
     * Konvertiert einen Base64 String zu einem ImageIcon
     *
     * @param base64String Der String, welcher konvertiert werden soll
     * @return Das konvertierte ImageIcon Objekt<br/> null, wenn der String null
     * oder leer ist
    *
     */
    public static ImageIcon Base64ToImage(String base64String) {
        if ((base64String != null) && (!base64String.isEmpty())) {
            byte[] byteArray;
            byteArray = Base64.decodeFast(base64String.toCharArray());
            //byteArray = new sun.misc.BASE64Decoder().decodeBuffer(base64String);
            //byteArray = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64String);

            //DEBUG ONLY
            //saveImage(byteArray);
            return new ImageIcon(byteArray);
        }
        return null;
        //return Toolkit.getDefaultToolkit().createImage(byteArray);
    }

    /**
     * Testet, ob eine Verbindung zum Internet besteht
     *
     * @return true, wenn Verbindung besteht
    *
     */
    public static boolean isInternetConnectionAvailable() {
        URLConnection con;
        try {
            con = new URL("http://www.google.com").openConnection();
            //Fake den Firefox, um HTTP Anfragen senden zu dürfen
            con.addRequestProperty("user-agent", "Firefox");
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            //Connection established
            br.close();
        } catch (IOException ex) {
            String cause = ex.toString().split(":")[0];
            if (cause.equals("java.net.UnknownHostException")) {
                return false;
            }
        }
        return true;
    }

    /**
     * Überprüft den übergebenen String anhang von Regex Regeln und gibt die
     * darin enthaltene Staffel Nr. zurück
     *
     * @param seasonString Der String der durchsucht werden soll
     * @return Die Staffel Nr. <br/>-1 Wenn keine Staffel Nr gefunden wurde
     */
    public static int getSeasonNr(String seasonString) {
        int seasonNr = -1;

        //Staffel 01, Staffel 1, Staffel01, Staffel1, Staffel10, sTaFfEl10
        //Season 01, Season 1, Season01, Season1, sEaSoN10
        //S 01, S 1, S01, S1, s10

        //Allex weg exxen, was nicht wie die oben genannten Strings aufgebaut ist
        seasonString = seasonString.toUpperCase();

        //Beliebige Anzahl zeichen, Staffel|Season|S, Leehrzeichen oder nicht, Zweistellig 0-9, beliebige anzahl Zeichen   
        Pattern extractSeason = Pattern.compile("(STAFFEL|SEASON|S)( ??)[0-9]{1,2}");
        Matcher m = extractSeason.matcher(seasonString);
        while (m.find()) {
            String s = m.group();
            s = s.replaceAll(REGEX_DIGITS_ONLY, REPLACEMENT_DIGITS_ONLY);
            if (!s.isEmpty()) {
                seasonNr = Integer.valueOf(s);
            }
        }
        if (seasonNr == -1) {
            String numbersOnly = seasonString.replaceAll(REGEX_DIGITS_ONLY, REPLACEMENT_DIGITS_ONLY);
            if (!numbersOnly.isEmpty() && numbersOnly.length() > 1) {
                //Nehme die letzten 2 Zahlen im String
                seasonNr = Integer.valueOf(numbersOnly.substring(numbersOnly.length() - 2));
            } else if (!numbersOnly.isEmpty() && numbersOnly.length() == 1) {
                //Letze Zahl im String
                seasonNr = Integer.valueOf(numbersOnly.substring(numbersOnly.length() - 1));
            }
        }
        return seasonNr;
    }

    /**
     * Überprüft den übergebenen String anhang von Regex Regeln und gibt die
     * darin enthaltene Episoden Nr. zurück
     *
     * @param episodeString Der String der durchsucht werden soll
     * @return Die Episoden Nr. <br/>-1 Wenn keine Episoden Nr gefunden wurde
     */
    public static int getEpisodeNr(String episodeString) {
        int episodeNr = -1;

        //Allex weg exxen, was nicht wie die oben genannten Strings aufgebaut ist
        episodeString = episodeString.toUpperCase();
        //Beliebige Anzahl zeichen, Staffel|Season|S, Leehrzeichen oder nicht, Dreistellig 0-9, beliebige anzahl Zeichen   

        Pattern extractSeason = Pattern.compile("(EPISODE|FOLGE|X|E|F)( ??)[0-9]{1,3}");
        Matcher m = extractSeason.matcher(episodeString);
        while (m.find()) {
            String s = m.group();
            s = s.replaceAll(REGEX_DIGITS_ONLY, REPLACEMENT_DIGITS_ONLY);
            if (!s.isEmpty()) {
                episodeNr = Integer.valueOf(s);
            }
        }
        if (episodeNr == -1) {
            String numbersOnly = episodeString.replaceAll(REGEX_DIGITS_ONLY, REPLACEMENT_DIGITS_ONLY);
            if (!numbersOnly.isEmpty() && numbersOnly.length() > 1) {
                //Nehme die letzten 2 Zahlen im String
                episodeNr = Integer.valueOf(numbersOnly.substring(numbersOnly.length() - 2));
            } else if (!numbersOnly.isEmpty() && numbersOnly.length() == 1) {
                //Letze Zahl im String
                episodeNr = Integer.valueOf(numbersOnly.substring(numbersOnly.length() - 1));
            }
        }
        return episodeNr;
    }
}
