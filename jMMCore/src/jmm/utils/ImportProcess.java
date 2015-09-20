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
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;
import jmm.api.JMMAPI;
import jmm.api.thetvdb.TheTVDB;
import jmm.api.thetvdb.TheTVDBEpisode;
import jmm.api.thetvdb.TheTVDBSeries;
import jmm.data.Actor;
import jmm.data.Episode;
import jmm.data.LocalVideoFile;
import jmm.data.Movie;
import jmm.data.Season;
import jmm.data.Serie;
import jmm.data.VideoFile;
import jmm.data.collection.MediaCollection;
import jmm.data.collection.MovieCollection;
import jmm.data.collection.SerieCollection;
import jmm.interfaces.FileTypeInterface;
import jmm.persist.RunnableImpl;
import jmm.api.tmdb.TMDBMovieWrapper;
import jmm.api.tmdb.TMDBSearchWrapper;
import jmm.data.DataManager;
import jmm.data.TMDBVideoFile;
import jmm.interfaces.ExtensionInterface;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;

/**
 * ImportProcess Klasse welche den Import von Filmen, Serien und Musik übernimmt
 * 
 * @author Bryan Beck
 * @since 06.12.2011
 */
public class ImportProcess implements FileTypeInterface{
    /** Logger. */
    private static final Logger LOG = LoggerFactory.logger(ImportProcess.class);
    
    private final MediaCollection collection;
    private final List<DefaultMutableTreeNode> treeNodesToImport;
    private final boolean useDirNamesAsTitle;
    private final boolean searchOnline;
    private final boolean importThumbnail;
    private final boolean searchFileHeader; 
    private final boolean useSmartFileNames;
    
    private final FileNameFormatter fileNameFormatter;
    
   /**
    * Initializes a new import process
    *
    * @param collection the collection to add the imported files
    * @param dirsToImport Ordner, die importiert werden sollen
    * @param useDirNamesAsTitle true, Name des Ordners wird für den Import Objekt genommen<br/>
    *                    false, Name der Datei wird für das Import Objekt genommen               <br/> <b>Bei Serien nur in der 4 Stufen Architektur relevant.</b>
    * @param searchOnline true Enabled search for additional file information on tmdb.org and thetvdb.com
    * @param importThumbnail true, wenn Vorschaubilder importiert werden solln
    * @param search_FileHeader true, wenn nach Informationen zur Audio- & Videobitrate im Fileheader gesucht werden soll
    * @param formatter The FileNameFormatter to format file names.
    **/
    public ImportProcess(MediaCollection collection,
            LinkedList<DefaultMutableTreeNode> dirsToImport,
            boolean useDirNamesAsTitle,
            boolean searchOnline,
            boolean importThumbnail,
            boolean search_FileHeader,
            FileNameFormatter formatter){

            this.collection = collection;
            this.treeNodesToImport = dirsToImport;
            this.useDirNamesAsTitle = useDirNamesAsTitle;
            this.searchOnline = searchOnline;
            this.importThumbnail = importThumbnail;
            this.searchFileHeader = search_FileHeader;
            this.useSmartFileNames = formatter != null;
            this.fileNameFormatter = formatter;
    }
    
    /**
     * Returns a list of import tasks
     * 
     * @return A list of import tasks
     */
    public List<RunnableImpl> getImportTasks(){
        List<RunnableImpl> importTasks = new LinkedList<>(); 
        //Find out how many tasks should be created
        int numberOfThreads = JMMAPI.getConCount().availablePermits();    //default 5
        final int numberOfImportsPerThread = treeNodesToImport.size() / numberOfThreads;
        Integer iStart = 0;
        final List<List<DefaultMutableTreeNode>> listOfLists = new LinkedList<>();
        //Unterteile die Liste in x gleichgroße subliste
        for(int i = 0; i < numberOfThreads; i ++){
            final List<DefaultMutableTreeNode> sublist;
            if(i == numberOfThreads-1){ //last thread
                sublist = treeNodesToImport.subList(iStart, treeNodesToImport.size());
                LOG.debug("Import-Thread " + i + " has a subList of " + sublist.size() + " elements.");
            }
            else{
                sublist = treeNodesToImport.subList(iStart, iStart+ numberOfImportsPerThread);
                LOG.debug("Import-Thread " + i + " has a subList of " + sublist.size() + " elements.");
            }
            listOfLists.add(sublist);
            iStart += numberOfImportsPerThread;
        }

        for(final List<DefaultMutableTreeNode> sublist: listOfLists){
            if(sublist.size() > 0){
               if(collection instanceof  MovieCollection){ 
                  RunnableImpl task = new RunnableImpl(collection.size()) {
                      @Override
                      public void run() {
                          for(final DefaultMutableTreeNode movieTreeNode: sublist){
                              Movie movie = startMovieImport(movieTreeNode);
                              //update DisplayImportProgressGUI
                              this.setChanged();
                              this.notifyObservers(movie);
                              DataManager.INSTANCE.addMovieToCollection(movie, (MovieCollection)collection);
                              //Abbruchfunktion
                              if (Thread.currentThread().isInterrupted()) {
                                  break;
                              }
                          }
                      }
                  };    
                  importTasks.add(task);
               }
               else if(collection instanceof SerieCollection){
                   RunnableImpl task = new RunnableImpl(collection.size()) {
                   @Override
                   public void run() {
                       for(final DefaultMutableTreeNode movieTreeNode: sublist){
                           Serie serie = startSerieImport(movieTreeNode);
                           //update DisplayImportProgressGUI
                           this.setChanged();
                           this.notifyObservers(serie);
                           DataManager.INSTANCE.addSerieToCollection(serie, (SerieCollection)collection);
                           //Abbruchfunktion
                           if (Thread.currentThread().isInterrupted()) {
                               break;
                           }
                       }
                   }
                  };
                  importTasks.add(task);
               }
            }
        }
        return importTasks;
    }    

   /**
    * Searches recursivly, determines all video files and adds them on the rootnode
    * 
    * A Tree with the levels below will be created:
    * <html><ul>
    * <li>Level 1 - Serie Folder</li>
    * <li>Level 2 - Season Folder</li>
    * <li>Level 3 - Episode File</li>
    * </ul></html>
    *
    * @param root the root node to add determined files
    * @param dirsToImport the dirs to import
    **/
    public static void determineSerieDirsToImport(DefaultMutableTreeNode root, File[] dirsToImport){ 
        if(dirsToImport != null){
            for(File dir: dirsToImport){
                DefaultMutableTreeNode leaf = new DefaultMutableTreeNode(dir);
                String lcFileName = dir.getName().toLowerCase();
                if((dir.exists()) && (dir.isDirectory())){
                    if(root.getLevel() == 0){
                        //Create Serie Node
                        root.add(leaf);
                        determineSerieDirsToImport(leaf, dir.listFiles());
                        if(leaf.getChildCount() == 0){
                            root.remove(leaf);
                        }
                    }
                    else if(root.getLevel() == 1){
                        //Create Season Node
                        root.add(leaf);
                        determineSerieDirsToImport(leaf, dir.listFiles());
                        if(leaf.getChildCount() == 0){
                            root.remove(leaf);
                        }
                    }
                    else{
                        //Create Episode
                        determineSerieDirsToImport(root, dir.listFiles());
                    }   

                }
                else if(dir.exists() && dir.isFile()){
                    if(root.getLevel() == 0){
                        //Create Serie Node
                        DefaultMutableTreeNode serieNode = new DefaultMutableTreeNode(dir.getName());
                        root.add(serieNode);
                        //Create Season Node
                        root = serieNode;
                    }
                    if(root.getLevel() == 1){
                        //Create Season Node
                        DefaultMutableTreeNode seasonNode = new DefaultMutableTreeNode("1");
                        root.add(seasonNode);
                        root = seasonNode;
                    }

                    //A season already exist
                    for(String extension: ExtensionInterface.video_Extensions){
                        if(lcFileName.endsWith(extension)){ 
                            root.add(leaf);
                            leaf.setAllowsChildren(false);
                            break;
                        }
                    }                     
                }
            }
        }
    }      
    
    /**
     * Searches recursivly, determines all video files and adds them on the rootnode
     * 
     * A Tree with the levels below will be created:
     * <html><ul>
     * <li>Level 1 - Movie File <b>OR</b> Movie Folder if createSepFileEntries is false</li>
     * <li>Level 2 - Movie File <b>IF</B> createSepFileEntries is false</li>
     * </ul></html>
     *
     * @param root the root node to add determined files
     * @param dirsToImport the dirs to import
     * @param createSepFileEntries true, a movie will be created for each file. <br/> Otherwise all files in a folder will be combined in one movie
     * @param recursive true, if all subfolders should be imported too
     * @param recDepth the depth of recursive steps (default = 0; requiered, if recursive == false)
    **/      
    public static void determineMovieDirsToImport(DefaultMutableTreeNode root, File[] dirsToImport, boolean createSepFileEntries, boolean recursive, int recDepth){  
        if(dirsToImport != null){
            for(int i = 0; i < dirsToImport.length; i ++){
                File dir = dirsToImport[i];
                DefaultMutableTreeNode leaf = new DefaultMutableTreeNode(dir);
                String lcFileName = dir.getName().toLowerCase();            

                if(dir.exists() && dir.isDirectory() && (recursive || recDepth == 0)){
                    recDepth ++;
                    determineMovieDirsToImport(root, dir.listFiles(), createSepFileEntries, recursive, recDepth);
                }
                else if(dir.exists() && dir.isFile()){
                    for(String extension: ExtensionInterface.video_Extensions){
                        if(lcFileName.endsWith(extension)){                       
                            if(createSepFileEntries){
                                root.add(leaf);
                                leaf.setAllowsChildren(false);
                            }
                            else{
                                if(root.getLevel() == 0){
                                    //Add Parent folder 
                                    File parentFile = dir.getParentFile();
                                    if(parentFile.getName().equalsIgnoreCase("VIDEO_TS")){
                                        parentFile = parentFile.getParentFile();
                                    }
                                    if((root.getUserObject() != null ) && 
                                            (((File)root.getUserObject()).equals(parentFile))){
                                        root.add(leaf);
                                        leaf.setAllowsChildren(false);
                                    }
                                    else{
                                        DefaultMutableTreeNode parent = new DefaultMutableTreeNode(parentFile);
                                        root.add(parent);
                                        recDepth ++;
                                        determineMovieDirsToImport(parent, Arrays.copyOfRange(dirsToImport, i, dirsToImport.length), createSepFileEntries, recursive, recDepth);
                                        return;
                                    }
                                }
                                else{
                                    //Add all files
                                    root.add(leaf);
                                    leaf.setAllowsChildren(false);
                                }             
                            }
                            break;
                        }
                    }
                }
            }   
        }
    }
    
    /**
    * Creates a new movie for the specific TreeNode<br/>
    * 
    * @param movieTreeNode The root node of the tree<br/><b>The tree must have 1 or 2 levels.</b><br/>
    * <html><ul>
    * <li>Level 1 - movie dir / movie name</li>
    * <li>Level 2 - movie file</li>
    * </ul></html>
    * @return The created movie<br/> null, if the rootNode is empty or doesn't contain any user objects
    *
    **/       
    private Movie startMovieImport(DefaultMutableTreeNode movieTreeNode){ 
        Object movieObj = movieTreeNode.getUserObject();
        List<File> movieFiles = new LinkedList<>();
        Movie movieToImport;
        String movieTitle;
        File tempFile = (File)movieObj; 
        /**
         * Struktur der Movie file hirarchie geht den treeNodes, NICHT nach der file.parentFile(), usw. hirarchie 
         */
        if(movieTreeNode.getDepth() == 1){          //tempFile is a dir
            if(useDirNamesAsTitle){
                movieTitle = tempFile.getName();    //dir name
            }
            else{
                //First file = movie title
                tempFile = (File)((DefaultMutableTreeNode)movieTreeNode.getChildAt(0)).getUserObject();
                int lastDot = tempFile.getName().lastIndexOf(".");
                movieTitle = tempFile.getName().substring(0, lastDot);
            }
            //add all childs 
            for(int i = 0; i < movieTreeNode.getChildCount(); i++){
                DefaultMutableTreeNode childNode = (DefaultMutableTreeNode)movieTreeNode.getChildAt(i);
                movieFiles.add((File)childNode.getUserObject());
            }
        }
        else{                                       //tempFile is a file
            if(useDirNamesAsTitle){
                movieTitle = tempFile.getParentFile().getName();
                
                if(movieTitle.equalsIgnoreCase("VIDEO_TS")){
                    movieTitle = tempFile.getParentFile().getParentFile().getName();
                }
            }
            else{
                int lastDot = tempFile.getName().lastIndexOf(".");
                movieTitle = tempFile.getName().substring(0, lastDot);
            }
            movieFiles.add(tempFile);
        }
        //Erstelle Movie Objekt
        if(useSmartFileNames){
            movieTitle = fileNameFormatter.format(movieTitle);
        }
        movieToImport = new Movie(movieTitle);

        //Füge FilePaths hinzu                  
        for(File file: movieFiles){
            movieToImport.addFilePath(file.getAbsolutePath());
        }
        
        importFileHeader(movieToImport);
        importOnlineInformation(movieToImport);
        
        return movieToImport;
    }
    
    /**
    * Creates a new serie for the specific TreeNode<br/>
    * 
    * @param serieTreeNode The root node of the tree<br/><b>The tree must have 3 levels.</b><br/>
    * <html><ul>
    * <li>Level 1 - Serie</li>
    * <li>Level 2 - Season</li>
    * <li>Level 3 - Episode</li>
    * </ul></html>
    * @return The created serie<br/> null, if the rootNode is empty or doesn't contain any user objects
    *
    **/     
    private Serie startSerieImport(DefaultMutableTreeNode serieTreeNode){
        Object serieObj = serieTreeNode.getUserObject();
        Serie serieToImport = null;
        //Import Serie
        if(serieObj != null){  
            String serieTitle = serieObj.toString();
            if(useSmartFileNames){
                serieTitle = fileNameFormatter.format(serieTitle);
            }
            serieToImport = new Serie(serieTitle);
                        
            //Import Season
            for(int j = 0; j < serieTreeNode.getChildCount(); j++){
                DefaultMutableTreeNode seasonTreeNode = (DefaultMutableTreeNode)serieTreeNode.getChildAt(j);
                Object seasonObj = seasonTreeNode.getUserObject();
                if(seasonObj != null){
                    Season seasonToImport;
                    if(seasonObj instanceof File){
                        seasonToImport = new Season(Utils.getSeasonNr(((File)seasonObj).getName()));
                    }
                    else{
                        seasonToImport = new Season(Utils.getSeasonNr(seasonObj.toString()));
                    }
                    //Wenn Episode keine Nr zugewiesen werden konnte
                    if(seasonToImport.getSeasonNumber() == 0){
                         //Nehm die letzte hinzugefügte Season und addiere +1 auf die SeasonNr
                         int numberOfSeasons = serieToImport.getSeasons().size();
                         if(numberOfSeasons > 0){
                             seasonToImport.setSeasonNumber(serieToImport.getSeason(numberOfSeasons-1).getSeasonNumber()+1);
                         }
                         else{
                             //Wenn noch keine Episode vorhanden
                             seasonToImport.setSeasonNumber(1);
                         }
                    }
                     //Überprüfe, ob Season mit der selben Nummer bereits angelegt wurde
                     boolean numberExist = false;
                     for(Season season: serieToImport.getSeasons()){
                         if(season.getSeasonNumber() == seasonToImport.getSeasonNumber()){
                             numberExist = true;
                             seasonToImport = season;
                         }
                     }
                     //Wenn die Nr noch nicht existiert, füge das Objekt hinzu
                     if(!numberExist){
                         serieToImport.addSeason(seasonToImport);   
                     }
                     //Import Episode
                     addEpisodesFromTreeNodeToSeason(seasonTreeNode, seasonToImport);
                     seasonToImport.orderEpisodesByNumber();
                }
            }
  
            importOnlineInformation(serieToImport);                   
            
            serieToImport.orderSeasonsByNumber();
        }
        return serieToImport;
    }
    
    /**
     * Adds all files from the TreeNode to the season
     * 
     * @param seasonTreeNode the TreeNode with all files
     * @param season the season to add the episodes
     */
    private void addEpisodesFromTreeNodeToSeason(DefaultMutableTreeNode seasonTreeNode, Season season){
       for(int j = 0; j < seasonTreeNode.getChildCount(); j++){
           DefaultMutableTreeNode episodeTreeNode = (DefaultMutableTreeNode)seasonTreeNode.getChildAt(j);
           File episodeFile = (File)episodeTreeNode.getUserObject();

           if(episodeFile.exists() && episodeFile.isFile()){
               String episodeName;
               //Only for 4 Step Serie Import and movies
               if(useDirNamesAsTitle){
                   episodeName = episodeFile.getParentFile().getName();
               }  else{
//                   int lastDot = episodeFile.getName().lastIndexOf(".");
                   episodeName = episodeFile.getName(); //.substring(0, lastDot);
               }    //Episodenname = Filename    
               if(useSmartFileNames){
                   episodeName = fileNameFormatter.format(episodeName);
               }
               Episode episodeToImport = new Episode(episodeName, (Utils.getEpisodeNr(episodeFile.getName())));

               //Filepath hinzufügen
                episodeToImport.addFilePath(episodeFile.getAbsolutePath());                                          
                       
               //Wenn Episode keine Nr zugewiesen werden konnte
               if(episodeToImport.getEpisodeNumber() == 0){
                    //Nehm die letzte hinzugefügte Episode und addiere +1 auf die EpisodeNr
                    int numberOfEpisodes = season.getEpisodes().size();
                    if(numberOfEpisodes > 0){
                        episodeToImport.setEpisodeNumber(season.getEpisode(numberOfEpisodes-1).getEpisodeNumber()+1);
                    }
                    else{
                        //Wenn noch keine Episode vorhanden
                        episodeToImport.setEpisodeNumber(1);
                    }
               }
                //Überprüfe, ob Episode mit der selben Nummer bereits angelegt wurde
                boolean numberExist = false;
                for(Episode episode: season.getEpisodes()){
                    if(episode.getEpisodeNumber() == episodeToImport.getEpisodeNumber()){
                        numberExist = true;
                        episodeToImport = episode;
                    }
                }
                //Überprüfe, ob Episode mit dem gleicher Nr bereits existiert
                //Wenn die Nr noch nicht existiert, füge das Objekt hinzu
                if(!numberExist){
                    season.addEpisode(episodeToImport); 
                }
                
                importFileHeader(episodeToImport);
           }
       }       
    }
                                   
    /**
     * Searches online for information related to the videoFile title
     * 
     * @param videoFile the videoFile to fill in information
     */
    private void importOnlineInformation(LocalVideoFile videoFile){
        if(searchOnline){
            String language = LocaleManager.getInstance().getCurrentLocale().getLanguage();
            if(videoFile instanceof Movie){
                List<TMDBVideoFile> results = new TMDBSearchWrapper().searchMovie(videoFile.getTitle(), null, language, true, null, 5);
                if(!results.isEmpty()){
                    TMDBVideoFile result = results.get(0);
                    result = new TMDBMovieWrapper().findMovie(result.getTmdbID(), language, "casts");
                    mapData(result, videoFile);
                }
            }
            else if(videoFile instanceof Serie){
                Serie serie = (Serie)videoFile;
                List<TheTVDBSeries> results = new TheTVDB().searchSerie(videoFile.getTitle(), language, 5);
                if(results != null && !results.isEmpty()){
                    TheTVDBSeries result = results.get(0);
                    result = new TheTVDB().findSerie(result.getThetvdbID(), language);
                    mapData(result, videoFile);
                    for(TheTVDBEpisode value: result.getEpisodes()){
                        //map every episode
                        Season season = serie.getSeasonNr(value.getSeasonNumber());
                        if(season != null){
                            Episode episode = season.getEpisodeNr(value.getEpisodeNumber());
                            if(episode != null){
                                mapData(value, episode);
                            }
                        }                       
                    }
                }
            }
        }       
        //Importiere Bilder
        importThumbnail(videoFile);
    }
        
    /**
     * Imports the file header of the first filepath to the movie object.
     * File header information are i.e. (runtime, videoCodec, videoBitrate, ...)
     * 
     * @param movieOrEpisode Das Video Objekt, dessen Header durchsucht werden soll
     */
    private void importFileHeader(Movie movieOrEpisode){
        if(searchFileHeader){
            MediaInfo.getInstance().analyseFileHeader(movieOrEpisode, movieOrEpisode.getFilePath(0));              
        }
        if(movieOrEpisode.getVideoCodec().isEmpty()){
            movieOrEpisode.setVideoCodec("Value.empty");
        }
        if(movieOrEpisode.getAudioCodec().isEmpty()){
            movieOrEpisode.setAudioCodec("Value.empty");
        }       
        //TODO: Source rausbekommen
        //Setzte Quelle auf Leer
        movieOrEpisode.setVideoSource("Value.empty");
    }
    
    /**
     * Setzt das Vorschaubild eines Video Objekts
     * @param videoFile Das Video Objekt, dessen Vorschaubild gesetzt werden soll
     */
    private void importThumbnail(VideoFile videoFile){
        if(importThumbnail && !videoFile.getImagePath().isEmpty()){
//            try {
            /**
            * Genau genommen lädt getImage() das Bild nicht sofort, anders als read() von ImageIO.
            * Ein Image-Objekt wird gültig erzeugt und das Objekt mit der Grafik in Verbindung gebracht,
            * aber es wird erst dann aus der Datei beziehungsweise dem Netz geladen,
            * wenn der erste Zeichenaufruf stattfindet.
            * Somit schützt uns die Bibliothek vor unvorhersehbaren Ladevorgängen für Bilder,
            * die später oder gar nicht genutzt werden.
            **/
                videoFile.setImage(PictureManager.getScaledImage(videoFile.getImagePath(), PictureManager.picture_width, PictureManager.picture_height));  
        }
    }
    
        /**
     * Maps the <b>not null</b> and <b>not empty</b> data from source to target
     * 
     * @param source The source file
     * @param target The target file
     */
    private void mapData(LocalVideoFile source, LocalVideoFile target){
        //Konvertiere Daten
        if(source != null){
            if(!source.getTitle().isEmpty()){
                target.setTitle(source.getTitle());
            }
            if(!source.getOriginalTitle().isEmpty()){
                target.setOriginalTitle(source.getOriginalTitle());
            }else{
                target.setOriginalTitle(source.getTitle());
            }
            //Genres
            for(String genreKey: source.getGenreKeys()){
                target.addGenreKey(genreKey);
            }
            if(!source.getDescription().isEmpty()){
                target.setDescription(source.getDescription());
            }
            target.setImagePath(source.getImagePath());
            target.setPublisher(source.getPublisher());
            for(Actor actor: source.getActors()){
                target.addActor(actor);
            }
            target.setDirector(source.getDirector());
            target.setFsk(source.getFsk());
            target.setPlaytime(source.getPlaytime());
            target.setReleaseYear(source.getReleaseYear());
            target.setOnlineRating(source.getOnlineRating());  
        }
    }
}
