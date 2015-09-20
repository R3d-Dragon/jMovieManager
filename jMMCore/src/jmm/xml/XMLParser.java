/**
 * Copyright (c) 2010-2015 Bryan Beck.
 * All rights reserved.
 * 
 * This project is licensed under LGPL v2.1.
 * See jMovieManager-license.txt for details.
 * 
 */
package jmm.xml;

import java.io.*;
import java.lang.reflect.*;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.ImageIcon;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import jmm.data.Actor;
import jmm.data.Episode;
import jmm.data.Movie;
import jmm.data.Season;
import jmm.data.Serie;
import jmm.data.VideoFile;
import jmm.data.collection.MediaCollection;
import jmm.data.collection.MovieCollection;
import jmm.data.collection.CollectionManager;
import jmm.utils.Settings;
import jmm.utils.Utils;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLFilterImpl;

/**
 *
 * @author Bryan Beck
 * @since 20.6.2011
 */
abstract class XMLParser {
    /** Logger. */
    private static final Logger LOG = LoggerFactory.logger(XMLParser.class);

    private static boolean savePictures;
//    private static int imageFormat;
    
    /**
     * Methode zum Speichern des Projekts in eine XML Datei
     * DOM wird verwendet
     *
     * create an xml document using DOM validate it against the
     * books.xsd and transform it using identity transform to an xml file
     *
     * @param savePath - Der Speicherpfad für die XML
     */
    static void saveAsXML(String savePath, boolean savePics) throws FileNotFoundException, 
            ParserConfigurationException, TransformerConfigurationException, TransformerException, 
            IOException, URISyntaxException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
              
        savePictures = savePics; 
//        imageFormat = Settings.getInstance().getImageFormat();

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setValidating(true);
        documentBuilderFactory.setNamespaceAware(true);
        documentBuilderFactory.setAttribute(
                        "http://java.sun.com/xml/jaxp/properties/schemaLanguage",
                        "http://www.w3.org/2001/XMLSchema");

        //Dokument das den Aufbau der XML beschreibt                        
        InputStream fileStream = XMLParser.class.getClassLoader().getResourceAsStream("XML/movieCollection.xsd");
        documentBuilderFactory.setAttribute(
                        "http://java.sun.com/xml/jaxp/properties/schemaSource",
                        new InputSource(fileStream));

        //Erstelle neues Dokument
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

        Document document = documentBuilder.newDocument();
        //Füge dem Dokument die Elemente hinzu
        Element allCollectionsElement = document.createElement("Collections");
        document.appendChild(allCollectionsElement);
                        
        //For all collections
//        for(MediaCollection collection: DaoManager.getDao(MediaCollection.class).getAll()){
        for(String key: MediaCollection.getCollectionMap().keySet()){
            MediaCollection collection = MediaCollection.getCollectionMap().get(key);
            writeCollectionToDocument(document, allCollectionsElement, collection);           
        }                      

        //Erstelle Ausgabedatei
        DOMSource domSource = new DOMSource(document);
        //Totaler Ausgabepfad, SavePath
        TransformerFactory tFactory = TransformerFactory.newInstance();

        //Dient zur Dateieinrückung
//                        tFactory.setAttribute(TransformerFactoryImpl.INDENT_NUMBER, new Integer(4));

        Transformer transformer = tFactory.newTransformer();

        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        //Dient zur Dateieinrückung
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.MEDIA_TYPE, "text/xml");


        FileOutputStream outputStream = new FileOutputStream(savePath);                        
        //OutputStreamWriter outputWriter = new OutputStreamWriter(outputStream,Charset.defaultCharset());
        OutputStreamWriter outputWriter = new OutputStreamWriter(outputStream,Charset.forName("UTF-8"));
        StreamResult outputResult = new StreamResult(outputWriter);
        //Wenn hier Nullpointer Exception, ein Attribut eines Movie Objektes == Null
        transformer.transform(domSource, outputResult);
        outputWriter.flush();
        outputWriter.close();
        outputStream.flush();
        outputStream.close();
    }
    
    /**
     * Methode zum Speichern des Projekts in eine XML Datei
     * DOM wird verwendet
     *
     * create an xml document using DOM validate it against the
     * books.xsd and transform it using identity transform to an xml file
     *
     * @param savePath - Der Speicherpfad für die XML
     */
    static void saveAsXML(String savePath) throws FileNotFoundException, 
            ParserConfigurationException, TransformerConfigurationException, TransformerException, 
            IOException, URISyntaxException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
        saveAsXML(savePath, Settings.getInstance().isExportPicturesIntoXML());
    }
    
    /**
     * 
     * @param document the document to export
     * @param rootNode the rootNode to attach all elements
     * @param collection the collection to parse
     */    
    private static void writeCollectionToDocument(Document document, Element rootNode, MediaCollection collection) throws NullPointerException, IllegalAccessException, InvocationTargetException, IOException{         
        Element collectionElement = document.createElement("Collection");
        //Name
        Attr titleCollectionAttr = document.createAttribute("Name");
        titleCollectionAttr.setValue(collection.getName());
        collectionElement.setAttributeNode(titleCollectionAttr);
        //TabNumber
        Element tabNumberElement = document.createElement("TabNumber");
        Text tabNumberText = document.createTextNode(Integer.valueOf(collection.getTabNumber()).toString());
        tabNumberElement.appendChild(tabNumberText);
        collectionElement.appendChild(tabNumberElement);          
        
        rootNode.appendChild(collectionElement);
        
        if(collection instanceof MovieCollection){
            writeListToDocument(document, collectionElement, collection.getAll(), "Movies");
        }
        else{
            writeListToDocument(document, collectionElement, collection.getAll(), "Series");
        }
    }
    
    /**
     * 
     * @param document Das document Objekt <b>Darf nicht null sein!</b>
     * @param rootNode Der Wurzel collectionNode, an dem alle Listenelemente angefügt werden sollen <b>Darf nicht null sein!</b>
     * @param list     Die Liste, die dem document hinzugefügt werden soll <b>Darf nicht null sein!</b>
     * @param listName Der Name der Liste im document, <b>Darf nicht null oder empty sein!</b>
     */
    private static void writeListToDocument(Document document, Element rootNode, List<?> list, String listName) throws NullPointerException, IllegalAccessException, InvocationTargetException, IOException{       
        Element elementNode = document.createElement(listName);
        rootNode.appendChild(elementNode);
        for(Object objectToSave : list){
            //Element Tag
            Element mediaFileElement = document.createElement(listName.substring(0, listName.length()-1));
            elementNode.appendChild(mediaFileElement);
            
            //Für alle getter Methoden des Objekts, ohne Parameter wird ein XML Attribut angelegt
            for(Method getterMethod: objectToSave.getClass().getMethods()){
                String getterMethodName = getterMethod.getName();
                MyXMLAnnotation xmlAnnotation = getterMethod.getAnnotation(MyXMLAnnotation.class);
                if(((getterMethodName.startsWith("get")) || 
                        getterMethodName.startsWith("is")) &&                    //boolean Werte
                        (getterMethod.getParameterTypes().length == 0) &&        //keine Parameter
                        (!getterMethodName.equals("getClass")) &&                //keine Object Attribute speichern
                        (!Modifier.isStatic(getterMethod.getModifiers())) &&     //keine Statische Methode
                        (!getterMethodName.equals("getUid")) &&                  //keine UID speichern
                        ((xmlAnnotation == null) ||(!xmlAnnotation.isTransient()))//keine joinColumn Annotation
                        ){                  
                    if(getterMethodName.startsWith("get")){
                        getterMethodName = getterMethodName.replace("get", "");
                    }
                    else if(getterMethodName.startsWith("is")){
                        getterMethodName = getterMethodName.replace("is", "");
                    }
                    Element methodName = document.createElement(getterMethodName);

                    //Falls es eine Liste oder ein Set ist
                    if((getterMethod.getReturnType().equals(List.class)) || (getterMethod.getReturnType().equals(Set.class))){
                        List listFromGetter;
                        if(getterMethod.getReturnType().equals(Set.class)){
                            Set setFromGetter = (Set) getterMethod.invoke(objectToSave);
                            listFromGetter = new LinkedList(setFromGetter);
                        }
                        else{
                            listFromGetter = (List)getterMethod.invoke(objectToSave);
                        }
                        
                        //Überprüfe, ob die Liste vom genrischen typ <String> ist
                        Type returnType = getterMethod.getGenericReturnType();
                        if(returnType instanceof ParameterizedType){
                            ParameterizedType type = (ParameterizedType) returnType;
                            Type[] typeArguments = type.getActualTypeArguments();
                            //Nur 1 durchlauf, da Rückgabewert von Methoden nur 1 sein kann
                            for(Type typeArgument : typeArguments){
                                Class typeArgClass = (Class) typeArgument;
                                if(typeArgClass.equals(String.class)){  
                                    //Liste mit Stringelementen
                                    if(!listFromGetter.isEmpty()){
                                        mediaFileElement.appendChild(methodName);
                                        for(Object listToSaveElement: listFromGetter){
                                            if(!listToSaveElement.toString().isEmpty()){
                                                Element listElement = document.createElement(methodName.getTagName().substring(0, methodName.getTagName().length()-1));
                                                Text listText = document.createTextNode(listToSaveElement.toString());
                                                listElement.appendChild(listText);
                                                methodName.appendChild(listElement);    
                                            }
                                        }
                                    }  
                                }
                                else{
                                    //Liste mit anderen Elementen 
                                    writeListToDocument(document, mediaFileElement, listFromGetter, getterMethod.getName().replace("get", ""));
                                }
                            }
                        }                                   
                    }                     
                    else if(getterMethod.getReturnType().equals(ImageIcon.class)){
                        if(savePictures){
                            //ImageIcon zu Base64 String konvertieren
                            ImageIcon tempIcon = (ImageIcon)getterMethod.invoke(objectToSave);
                            if(tempIcon != null){
                                String base64String = Utils.ImageToBase64(tempIcon, 1);
                                Text imageStringText = document.createTextNode(base64String);
                                methodName.appendChild(imageStringText);
                                mediaFileElement.appendChild(methodName);
                            }
                        }
                    }
                    else if(getterMethod.getName().equals("getTitle")){    //Setzte Titel als Attribut
                        Attr title = document.createAttribute("Title");
                        title.setValue(getterMethod.invoke(objectToSave).toString());
                        mediaFileElement.setAttributeNode(title);                                  
                    }
                    else{ //String, int, double, boolean ...
                        Text stringText = document.createTextNode(getterMethod.invoke(objectToSave).toString());
                        if(!stringText.getData().isEmpty() && !stringText.getData().equals("0") && !stringText.getData().equals("0.0")){
                            methodName.appendChild(stringText);
                            mediaFileElement.appendChild(methodName);
                        }          
                    }
                }
            }                   
        }        
    }

    /**
     * This shows how to parse an XML File using SAX and display the same
     * information from the xml file by implementing the ContentHandler interface
     *
     * Methode zum  öffnen einer vorhandenen XML Datei und generieren der Java Objekte
     * mittels SAX Parser (Sequenziel, read only)
     *
     * @param openPath - Der Pfad der XML Datei, die geöffnet werden soll
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    static void openXMLSAX(String openPath) throws IOException, SAXException, ParserConfigurationException, NumberFormatException{
            //Erstelle neuen Input Stream
            FileInputStream instream ;
            InputSource is;

            SAXParserFactory spf = SAXParserFactory.newInstance();
            spf.setNamespaceAware(true);
            XMLReader xmlReader = spf.newSAXParser().getXMLReader();
            //Setzt den ContentHandler, der die XML auswertert und verarbeitet
            xmlReader.setContentHandler(new MyContentHandler());
            //Setzte Pfad für die XML Datei
            instream = new FileInputStream(openPath);
            is = new InputSource(instream);
            //Parse die XML
            xmlReader.parse(is);
	}
}
/**
 * Innere Klasse MyContentHandler zum auswerten der XML Datei
 */
class MyContentHandler extends XMLFilterImpl {        
        /** Logger. */
        private static final Logger LOG = LoggerFactory.logger(MyContentHandler.class);
    
        //String mit dem aktuellen XML Tag, der gerad eingelesen wurde
        private String actualTag = "";
        //Attribute
        private String title;
        private String imageString;
        
        private ImageIcon  image;
        
        private MediaCollection collection;
        private int tabNumber;
        private Movie movieToLoad;
        private Serie serieToLoad;
        private Season seasonToLoad;
        private Episode episodeToLoad;
        private Actor actorToLoad;
        
        private int currentActiveTag = -1;
        private int previousActiveTag = -1;
        private static final int MOVIE_TAG   = 0;
        private static final int SERIE_TAG   = 1;
        private static final int SEASON_TAG  = 2;
        private static final int EPISODE_TAG = 3;
        private static final int ACTOR_TAG = 4;

        
        /**
         * Geht alle add bzw. set Methoden des Objekts durch, und überprüft, ob es eine Methode mit dem Namen des actualTags gibt. <br/>
         * Wenn ja, wird der Inhalt von gotString in die Methode hineingegeben, mittels Reflektion
         * 
         * @param objectToLoad Das Objekt, das auf add() bzw. set() Methoden überprüft wird
         * @param gotString  Der String, welcher als Parameter in die add() bzw. set() Methode gegeben wird
         */
        private void loadCharsToObject(Object objectToLoad, String gotString){
            for(Method setterMethod: objectToLoad.getClass().getMethods()){
                    if(((setterMethod.getName().startsWith("set")) || 
                        (setterMethod.getName().startsWith("add"))) && 
                            (!actualTag.trim().isEmpty()) && (
                            setterMethod.getName().endsWith(actualTag) 
                            //TODO: This is a Workaround for version 1.01 --> 1.1 (because 2 genres were removed)
                            || actualTag.equals("Genre") && setterMethod.getName().equalsIgnoreCase("addGenreKey")                            
                            )){
                        //Gehe alle Parameter der Methode durch
                        for(Class parameterClass: setterMethod.getParameterTypes()){
                            try{
                                if(parameterClass.equals(double.class)){
                                    setterMethod.invoke(objectToLoad, Double.valueOf(gotString));
                                }
                                else if(parameterClass.equals(int.class)){
                                    setterMethod.invoke(objectToLoad, Integer.valueOf(gotString));
                                }
                                else if(parameterClass.equals(float.class)){
                                    setterMethod.invoke(objectToLoad, Float.valueOf(gotString));
                                }
                                else if(parameterClass.equals(boolean.class)){
                                    setterMethod.invoke(objectToLoad, Boolean.valueOf(gotString));
                                }
                                else if(parameterClass.equals(String.class)){
                                    setterMethod.invoke(objectToLoad, gotString);
                                }
                                else if(parameterClass.equals(ImageIcon.class)){
                                    imageString += gotString;
                                }
                                else if(parameterClass.equals(VideoFile.FSK.class)){
                                    setterMethod.invoke(objectToLoad, VideoFile.FSK.valueOf(gotString));
                                }
                                else if(parameterClass.equals(Map.class)){
                                    if(!gotString.isEmpty() && gotString.length() > 2){
                                        Map<String, String> map = new HashMap<String, String>();
                                        
                                        gotString = gotString.substring(1, gotString.length()-1);
                                        for(String keyValue: gotString.split(", ")){
                                            int sepIndex = keyValue.indexOf("=");
                                            String key = keyValue.substring(0, sepIndex);
                                            String value = keyValue.substring(sepIndex+1);
                                            map.put(key, value);
                                        }
                                        setterMethod.invoke(objectToLoad, map);
                                    }
                                }
                            }catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException iae){
                                LOG.error("Error occured while parsing the xml file." , iae);
                            }
                            finally{
                                return;     //Nur 1 Durchgang, da nur 1 Übergabeparameter bei set & add Methoden
                            }
                        }
                        //return;           //Mehrere Durchgänge, bei einer Methode
                    }
                }
        }
        
        /**
         * Filter a character data event.
         *
         * @param ch - Feld von Characktern
         * @param start - Index, ab dem gestartet werden soll
         * @param length - The number of characters to use from the array.
         */
        @Override
	public void characters(char[] ch, int start, int length) throws NumberFormatException {
		String gotString = new String(ch, start, length);                           
                if(actualTag.equalsIgnoreCase("TabNumber")){             
                    tabNumber = Integer.valueOf(gotString);
                }
                //Reflection
                //Für alle getter Methoden des Objekts, ohne Parameter wird ein XML Attribut angelegt
                else if(currentActiveTag == MOVIE_TAG){
                    loadCharsToObject(movieToLoad, gotString);
                }
                else if(currentActiveTag == SERIE_TAG){
                    loadCharsToObject(serieToLoad, gotString);
                }
                else if(currentActiveTag == SEASON_TAG){
                    loadCharsToObject(seasonToLoad, gotString);
                }
                else if(currentActiveTag == EPISODE_TAG){
                    loadCharsToObject(episodeToLoad, gotString);
                }
                else if(currentActiveTag == ACTOR_TAG){
                    loadCharsToObject(actorToLoad, gotString);
                }
	}

       /**
         * Methode wird aufgerufen, wenn ein neues XML Dokument geöffnet wird
         */
        @Override
        public void startDocument(){
            //Erstelle neue Sammlung
//            if(!CollectionManager.INSTANCE.isACollectionCreated()){
//                CollectionManager.INSTANCE.createNewCollections();
//            }
        }

       /**
         * Methode wird aufgerufen, wenn das Ende des XML Dokuments erreicht wurde
         */
        @Override
        public void endDocument(){
            //System.out.println("XML Datei erfolgreich geöffnet.");
        }

       /**
         * Filter a start element event.
         * Methode die aufgerufen wird, wenn ein <> Tag eines Elements kommt
         *
         * @param namespaceURI - The element's Namespace URI, or the empty string.
         * @param localName - Der Name des XML Tags, oder leerer String
         * @param qName - The element's qualified (prefixed) name, or the empty string.
         * @param attributes - Das Attribut des Elements
         */
        @Override
	public void startElement(String namespaceURI, String localName,	String qName, Attributes attributes) {
            if(localName.equals("Collection")){
                title = attributes.getValue("Name");
                tabNumber = -1;
            }
            else if(localName.equals("Movies")){
                collection = MediaCollection.getCollectionMap().get(title);
                if(collection == null){
                    collection = CollectionManager.INSTANCE.createCollection(title, true, tabNumber);
                }
            }
            else if(localName.equals("Series")){
                collection = MediaCollection.getCollectionMap().get(title);
                if(collection == null){
                    collection = CollectionManager.INSTANCE.createCollection(title, false, tabNumber);
                }
            }
            else if (localName.equals("Movie")) {
                //Setzte alle Elemente zurück
                currentActiveTag = MOVIE_TAG;
                imageString         = "";
                image               = null;
                title = attributes.getValue("Title"); 
                movieToLoad = new Movie(title);
            }
            else if(localName.equals("Serie")){
                //Setzte alle Elemente zurück
                currentActiveTag = SERIE_TAG;
                imageString         = "";
                image               = null;                    
                title = attributes.getValue("Title"); 
                serieToLoad = new Serie(title);                
            }   
            else if(localName.equals("Season")){
                //Setzte alle Elemente zurück
                currentActiveTag = SEASON_TAG;
                seasonToLoad = new Season(0);
                serieToLoad.addSeason(seasonToLoad);
            }    
            else if(localName.equals("Episode")){
                //Setzte alle Elemente zurück   
                currentActiveTag = EPISODE_TAG;
                imageString         = "";
                image               = null;   
                title = attributes.getValue("Title");               
                episodeToLoad = new Episode(title, 0); 
                seasonToLoad.addEpisode(episodeToLoad);
            }
            else if(localName.equals("Actor")){
                previousActiveTag = currentActiveTag;
                currentActiveTag = ACTOR_TAG;
                actorToLoad = new Actor("");
            }
            else{
                actualTag = localName;
            }
	}

         /**
          * Filter an end element event.
          * Methode die aufgerufen wird, wenn ein </> Tag eines Elements kommt
          *
          * @param uri - The element's Namespace URI, or the empty string
          * @param localName - The element's local name, or the empty string
          * @param qName - The element's qualified (prefixed) name, or the empty string
         */
        @Override
	public void endElement(String namespaceURI, String localName, String qName) {
            if(localName.equals("Movie")){
                image = Utils.Base64ToImage(imageString);
                movieToLoad.setImage(image);
                if(!collection.add(movieToLoad)){
                    collection.set(movieToLoad);
                }
            }
            else if(localName.equals("Serie")){
                image = Utils.Base64ToImage(imageString);
                serieToLoad.setImage(image);
                if(!collection.add(serieToLoad)){
                    collection.set(serieToLoad);
                }
            }
            else if(localName.equals("Season")){
//                serieToLoad.addSeason(seasonToLoad);
                currentActiveTag = SERIE_TAG;
            }
            else if(localName.equals("Episode")){
                image = Utils.Base64ToImage(imageString);
                episodeToLoad.setImage(image);                
//                seasonToLoad.addEpisode(episodeToLoad);
                currentActiveTag = SEASON_TAG;
            }
            else if(localName.equals("Actor")){
                currentActiveTag = previousActiveTag;
                if(currentActiveTag == MOVIE_TAG){
                    movieToLoad.addActor(actorToLoad);
                }
                else if(currentActiveTag == SERIE_TAG){
                    serieToLoad.addActor(actorToLoad);
                }
                else if(currentActiveTag == EPISODE_TAG){
                    episodeToLoad.addActor(actorToLoad);
                }
            }
            //Setze aktuellen Tag zurück
            actualTag = "";
        }
}
