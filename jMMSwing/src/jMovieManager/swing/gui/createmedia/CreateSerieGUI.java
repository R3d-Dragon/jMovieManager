/**
 * Copyright (c) 2010-2015 Bryan Beck.
 * All rights reserved.
 * 
 * This project is licensed under LGPL v2.1.
 * See jMovieManager-license.txt for details.
 * 
 */
package jMovieManager.swing.gui.createmedia;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.swing.JFileChooser;
import javax.swing.ListSelectionModel;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.DefaultTableModel;
import jmm.api.thetvdb.TheTVDB;
import jmm.api.thetvdb.TheTVDBEpisode;
import jmm.api.thetvdb.TheTVDBSeries;
import jmm.data.Episode;
import jmm.data.LocalVideoFile;
import jmm.data.Season;
import jmm.data.Serie;
import jmm.data.collection.SerieCollection;
import jMovieManager.swing.gui.ChooseMediaFileDialog;
import jmm.data.DataManager;
import jMovieManager.swing.gui.MovieManagerGUI;
import jMovieManager.swing.gui.UIInterface;
import jMovieManager.swing.gui.components.MyJFileChooser;
import jMovieManager.swing.gui.components.ui.MyBasicScrollBarUI;
import jMovieManager.swing.gui.other.JMMFileFilter;
import jmm.utils.LocaleManager;
import jmm.utils.Utils;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;

/**
 * GUI to create or edit a serie
 * 
 * @author Bryan Beck
 * @since 17.08.2011
 */
public class CreateSerieGUI extends AbstractCreateGUI implements UIInterface{
    /** Logger. */
    private static final Logger LOG = LoggerFactory.logger(CreateSerieGUI.class);
    
    //Key = SeasonNr_EpisodeNr
    private Map<String, Episode> episodeMap;
    private boolean dialogCanceled;
    //Model der Tabelle
    private DefaultTableModel tableModel;

    /** 
     * Create an prefilled GUI to edit a serie
     * 
     * @see javax.swing.JDialog#JDialog(java.awt.Frame, boolean)
     * @param outdatedSerie the serie to edit
     * @param collection the collection which contains the outdatedVideoFile
     * @param collectionEditable true if the collection can be changed <br/> 
     * false otherwise (i.E. episodes cannot change the collection)
     */
    public CreateSerieGUI(java.awt.Frame parent, boolean modal, Serie outdatedSerie, final SerieCollection collection, boolean collectionEditable) {
        super(parent, modal, outdatedSerie, collection, collectionEditable);  
        init();
        this.setTitle(bundle.getString("CreateSerieGUI.headline.edit"));  
        //Fuelle die Tabelle mit den Informationen
        for(Season season: outdatedSerie.getSeasons()){
            for(Episode episode: season.getEpisodes()){
                episodeMap.put(String.valueOf(season.getSeasonNumber()) + "_" + episode.getEpisodeNumber(), episode);
                tableModel.addRow(new Object[]{season.getSeasonNumber(), episode.getEpisodeNumber(), episode.getTitle(), episode.getFilePath(0)});
            }
        } 
        tableModel.addRow(new Object[]{"", "", "", ""});
    }   
    
    /** 
     * Create an prefilled GUI to edit a serie
     * 
     * @see javax.swing.JDialog#JDialog(java.awt.Frame, boolean)
     * @param outdatedSerie the serie
     * @param episodeToEdit the episode to edit
     * @param collection the collection which contains the outdatedVideoFile
     * @param collectionEditable true if the collection can be changed <br/> 
     * false otherwise (i.E. episodes cannot change the collection)
     */
    public CreateSerieGUI(java.awt.Frame parent, boolean modal, Serie outdatedSerie, Episode episodeToEdit, final SerieCollection collection, boolean collectionEditable) {
        super(parent, modal, outdatedSerie, collection, collectionEditable);  
        init();
        this.setTitle(bundle.getString("CreateSerieGUI.headline.edit"));
        int selRow = -1;
        //Fuelle die Tabelle mit den Informationen
        for(Season season: outdatedSerie.getSeasons()){
            for(Episode episode: season.getEpisodes()){
                if(episode == episodeToEdit){
                    selRow = tableModel.getRowCount();
                }
                episodeMap.put(String.valueOf(season.getSeasonNumber()) + "_" + episode.getEpisodeNumber(), episode);
                tableModel.addRow(new Object[]{season.getSeasonNumber(), episode.getEpisodeNumber(), episode.getTitle(), episode.getFilePath(0)});
            }
        } 
        tableModel.addRow(new Object[]{"", "", "", ""});
        
        if(selRow > -1){
            //Select Episode 
            ListSelectionModel selectionModel = jTable1.getSelectionModel();
            selectionModel.setSelectionInterval(selRow, selRow);        
            //Edit selected Episode
            MouseEvent fakeDoubleClickMouseEvent = new MouseEvent(this, 0, 0, 0, 0, 0, 2, false);
            this.jTable1MouseClicked(fakeDoubleClickMouseEvent);
        }
    } 

    /** 
     * Creates an empty GUI to create a new serie
     * 
     * @see javax.swing.JDialog#JDialog(java.awt.Frame, boolean) 
     * @param collection the collection to add the new serie
     * @param collectionEditable true if the collection can be changed <br/> false otherwise (i.E. episodes cannot change the collection)
     */
    public CreateSerieGUI(java.awt.Frame parent, boolean modal, boolean collectionEditable) {
        super(parent, modal, collectionEditable);  
        init();        
        tableModel.addRow(new Object[]{"", "", "", ""});  
    }
    
    /**
     * initialize all requiered forms and values
     * 
     */
    private void init(){
        episodeMap = new HashMap<String, Episode>();
        dialogCanceled = false;
        initTableModel();
        initComponents();
        initTable();    
        
        if(useCustomLAF){
            this.changeUI();
        }
        
        //Füge eigene Methode bei Fensterschließen hinzu
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                dialogCanceled = true;
                dispose();
            }
        });
    }
    
    /**
     * Initialisiert das Tabellen Model
     */
    private void initTableModel(){
        tableModel = new DefaultTableModel(
                new Object [][] {                  
                },
                new String [] {
                    bundle.getString("CreateSerieGUI.tableheader.column1"),
                    bundle.getString("CreateSerieGUI.tableheader.column2"),
                    bundle.getString("CreateSerieGUI.tableheader.column3"),
                    bundle.getString("CreateSerieGUI.tableheader.column4")
                }
                ) {
                    Class[] types = new Class [] {
                        java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class
                    };
                    boolean[] canEdit = new boolean [] {
                        false, false, false, false
                    };
                    
            @Override
            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        };        
    }
    
    /**
     * initialisiert die Tabelle für Staffeln und Episoden
     */
    private void initTable(){
        //Serien Tab
        super.getTabbedPane().addTab(bundle.getString("CreateSerieGUI.jPanel10"), jPanel10);  
                
        jTable1.getTableHeader().setReorderingAllowed(false);
//        SelectionListener listener = new SelectionListener(jTable1, parent);
//        jTable1.getSelectionModel().addListSelectionListener(listener);
//        jTable1.getColumnModel().getSelectionModel().addListSelectionListener(listener);
//        jTable1.getModel().addTableModelListener(new MyTableModelListener(jTable1));
        
        jTable1.getColumnModel().getColumn(0).setWidth(50);
        jTable1.getColumnModel().getColumn(0).setPreferredWidth(50);
        jTable1.getColumnModel().getColumn(1).setWidth(50);
        jTable1.getColumnModel().getColumn(1).setPreferredWidth(50);
        jTable1.getColumnModel().getColumn(2).setWidth(300);
        jTable1.getColumnModel().getColumn(2).setPreferredWidth(300); 
        jTable1.getColumnModel().getColumn(3).setWidth(200);
        jTable1.getColumnModel().getColumn(3).setPreferredWidth(200);   
      //  tableModel = (DefaultTableModel)jTable1.getModel();
    }
       
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel10 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();

        jPanel10.setOpaque(false);
        jPanel10.setPreferredSize(new java.awt.Dimension(600, 450));

        jTable1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTable1.setModel(tableModel);
        jTable1.setRowHeight(20);
        jTable1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jTable1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTable1KeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 580, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 428, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel9.setText("Erstelle Episoden...");
        jLabel9.setMaximumSize(new java.awt.Dimension(170, 20));
        jLabel9.setMinimumSize(new java.awt.Dimension(170, 20));
        jLabel9.setPreferredSize(new java.awt.Dimension(170, 20));

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("jMovieManager/swing/resources/MovieManager"); // NOI18N
        setTitle(bundle.getString("CreateSerieGUI.headline")); // NOI18N

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * jTable1MouseClicked - Edit Value <br/>
     * Edits a table row
     * 
     * @param evt the triggered event
     */
private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
    int selectedRow = jTable1.getSelectedRow();
    if((evt.getClickCount() == 2) && (selectedRow > -1)){
        Object episodeCell = jTable1.getValueAt(selectedRow, 1);
        Object seasonCell = jTable1.getValueAt(selectedRow, 0);
        Object filePathCell = jTable1.getValueAt(selectedRow, 3); 
        int seasonNr = -1; 
        int episodeNr = -1;
        Episode oldEpisode = null;  

        //Suche Episode
        if((!seasonCell.toString().isEmpty()) && (!episodeCell.toString().isEmpty())){
            oldEpisode = episodeMap.get(seasonCell.toString() + "_" + episodeCell.toString());  
            seasonNr = Integer.valueOf(seasonCell.toString());
            episodeNr = Integer.valueOf(episodeCell.toString());
        }

        //Wenn Episode angelegt und Spalte gewählt, dann jFileChooser
        if((jTable1.getSelectedColumn() == 3) && (oldEpisode != null)){
            MyJFileChooser filePathChooser;
            
            if((filePathCell != null) && (!filePathCell.toString().isEmpty())){
                filePathChooser = new MyJFileChooser(filePathCell.toString(), JMMFileFilter.FilterType.VIDEO_EXTENSION);   
            }
            else{
                filePathChooser = new MyJFileChooser("", JMMFileFilter.FilterType.VIDEO_EXTENSION);            
            }
            //Mehrfachauswahl erlaubt
            filePathChooser.setMultiSelectionEnabled(false);

            //zeige Dialog und mache etwas, wenn "Öffnen" geklickt wurde
            if(filePathChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
                String filePath = filePathChooser.getSelectedFile().getAbsolutePath();
                oldEpisode.removeAllFilePaths();
                
                jTable1.setValueAt(filePath, selectedRow, 3);
                oldEpisode.addFilePath(filePath);
            }
        }   
        else if(jTable1.getSelectedRow() > -1){
            CreateEpisodeGUI newEpisodeGUI;
            if(oldEpisode == null){
                newEpisodeGUI = new CreateEpisodeGUI(MovieManagerGUI.getInstance(), true);                         //Neue Episode anlegen
            }
            else{
                if((seasonNr > -1) && (episodeNr > -1)){
                    oldEpisode = episodeMap.remove(seasonNr + "_" + episodeNr);
                }
                 newEpisodeGUI = new CreateEpisodeGUI(MovieManagerGUI.getInstance(), true, oldEpisode, seasonNr);  //vorhandene Episode editieren
            }
            boolean first = true;
            do{
                if(!first){
                    javax.swing.JOptionPane.showMessageDialog(this, bundle.getString("CreateSerieGUI.error.episodeNrExist"));
                }
                oldEpisode = newEpisodeGUI.showGUI();
                first = false;
            }while((oldEpisode != null) &&
                    (episodeMap.containsKey(String.valueOf(newEpisodeGUI.getSeasonNr()) + "_" + String.valueOf(oldEpisode.getEpisodeNumber()))));
            
            if(oldEpisode != null){
                //Episode editieren erfolgreich abgeschlossen --> update
                seasonNr = newEpisodeGUI.getSeasonNr();
                episodeNr = oldEpisode.getEpisodeNumber();
                episodeMap.put(String.valueOf(seasonNr) + "_" + String.valueOf(episodeNr), oldEpisode);
                
                //Füge Episodendetails in die GUI ein
                tableModel.removeRow(selectedRow);
                tableModel.insertRow(selectedRow, new Object[]{seasonNr, episodeNr, oldEpisode.getTitle(), oldEpisode.getFilePath(0)});

                if(selectedRow + 1 == jTable1.getRowCount()){
                    //Neue Zeile einfügen
                    tableModel.addRow(new Object[]{"", "", "", ""});
                }
                jTable1.getSelectionModel().setSelectionInterval(selectedRow, selectedRow);
                //tableModel.addRow(new Object[]{season.getSeasonNumber(), episode.getEpisodeNumber(), episode.getTitle(), ""});                            
            }
            else{
                //Abbruch Episode editieren
                episodeMap.put(seasonNr + "_" + episodeNr, oldEpisode);
            }
        }   
    }
}//GEN-LAST:event_jTable1MouseClicked

/**
* Event, welches bei Tastendruck ausgelöst wird<br/>
* Löscht eine Zeile, wenn ENTF gedrückt wurde
* 
* @param evt Das Event, welches ausgelöst wurde
*/
private void jTable1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable1KeyPressed
    if(evt.getKeyChar() == KeyEvent.VK_DELETE){
        int selRow = jTable1.getSelectedRow();
        if((selRow > -1) && (selRow+1 != jTable1.getRowCount())){
            Object episodeCell = jTable1.getValueAt(selRow, 1);
            Object seasonCell = jTable1.getValueAt(selRow, 0);

            if((!seasonCell.toString().isEmpty()) && (!episodeCell.toString().isEmpty())){
                if(episodeMap.remove(seasonCell.toString() + "_" + episodeCell.toString()) != null){
                    //Lösche Element von der GUI
                    tableModel.removeRow(selRow);               
                }              
            }
        }
    }
}//GEN-LAST:event_jTable1KeyPressed

    @Override
    protected LocalVideoFile searchForTitle(String title){
        List<TheTVDBSeries> searchResults = new TheTVDB().searchSerie(title, LocaleManager.getInstance().getCurrentLocale().getLanguage(), 10);
        TheTVDBSeries result = null;
        if(searchResults != null && !searchResults.isEmpty()){
            ChooseMediaFileDialog mediaFileSelection = new ChooseMediaFileDialog(MovieManagerGUI.getInstance(), true, title, searchResults);
            result = (TheTVDBSeries)mediaFileSelection.showDialog();
            if(result != null){ 
                //Get detailed information about the choosen result
                result = new TheTVDB().findSerie(result.getThetvdbID(), LocaleManager.getInstance().getCurrentLocale().getLanguage());
                updateSeriesDataOnGUI(result);
            }
            else if(!mediaFileSelection.isCanceled()){
                javax.swing.JOptionPane.showMessageDialog(this, bundle.getString("AbstractCreateGUI.error.titleNotFound"));
            }
        }
        
        return result;
    }
    /**
     * Updates the GUI with series data
     * 
     * @param seriesObject the object for updating the GUI
     */    
    protected void updateSeriesDataOnGUI(final TheTVDBSeries seriesObject){
        jTable1.clearSelection();

        //Fuelle die Tabelle mit den Informationen des imdb Objekts
        if(seriesObject != null && seriesObject.getEpisodes() != null){
            //Entferne alle bisherigen Daten
            tableModel.getDataVector().removeAllElements();
            for(TheTVDBEpisode value: seriesObject.getEpisodes()){
    //                    try {
    //                        //Erst wenn die erste Episode ausgelesen wurde, gehts weiter
    //                        episodeSemaphore.acquire();
    //                    } catch (InterruptedException ex) {
    //                        System.err.println("Exception bei Semaphore Acquire aufgetreten: " + ex.toString());
    //                    }
                int episodeNr = value.getEpisodeNumber();
                int seasonNr = value.getSeasonNumber();

                //zähle key solange hoch, bis keine episode in der map vorhanden
                //ntowendig, falls episodeNr nicht richtig ausgelesen wurde auf IMDB
                while(episodeMap.containsKey(String.valueOf(seasonNr) + "_" + String.valueOf(episodeNr))){
                    episodeNr++;
                }                    
                Episode episode = new Episode(value.getTitle(), episodeNr);
                episode.setOriginalTitle(value.getOriginalTitle());
                episode.setGenreKeys(value.getGenreKeys());
                episode.setPlaytime(value.getPlaytime());
                episode.setReleaseYear(value.getReleaseYear());   
                episode.setDescription(value.getDescription());
                episode.setPublisher(value.getPublisher());
                episode.setActors(value.getActors());
                episode.setDirector(value.getDirector());
                episode.setFsk(value.getFsk());
                episode.setImagePath(value.getImagePath());
                episode.setOnlineRating(value.getOnlineRating());

                //no previous value
                episodeMap.put(String.valueOf(seasonNr) + "_" + String.valueOf(episodeNr), episode);
                tableModel.addRow(new Object[]{seasonNr, episodeNr, episode.getTitle(), ""});  
            }     
            //Eine Leerzeite zum editieren
            tableModel.addRow(new Object[]{"", "", "", ""});      
        }    
    }
        
   /**
     * Macht die GUI sichtbar und gibt das neu erstellte Filmobjekt zurück
     * 
     * @return serie Das erstellte bzw. aktualisierte Serie Objekt <br/>null, wenn der Dialog abgebrochen wurde
     */
    public Serie showGUI(){
        this.setVisible(true);
        Serie serie = null;

        //nach Dispose Aufruf
        if(!dialogCanceled){ 
            List<Season> seasonList = new LinkedList<Season>();
            Iterator<String> keyIterator = episodeMap.keySet().iterator();  
            while(keyIterator.hasNext()){
                String key = keyIterator.next();
                Episode episode = episodeMap.get(key);
                if(episode != null){
                    int seasonNr = Integer.valueOf(key.substring(0, key.indexOf("_")));
                    Season seasonToPut = null;
                    //Überprüfe, ob Season bereits angelegt wurde
                    for(Season season: seasonList){
                        if(season.getSeasonNumber() == seasonNr){
                            seasonToPut = season;
                            break;
                        }
                    }
                    //Lege neue Season an
                    if(seasonToPut == null){
                        seasonToPut = new Season(seasonNr);
                        seasonList.add(seasonToPut);
                    }               
                    Episode updatedEpisode = episode.clone(); 
                    
                    DataManager.INSTANCE.addEpisodeToSeason(updatedEpisode, seasonToPut);                            
                    seasonToPut.orderEpisodesByNumber();                     
                }
            }
            
            if((oldCollection != null) && (oldCollection.get(oldTitle) != null)){
                //update existing serie, same collection
                serie = (Serie)oldCollection.get(oldTitle);
            }
            else if(collection.get(oldTitle) != null){
                //update existing serie, new collection
                serie = (Serie)collection.get(oldTitle);
//                oldTitle = "";
            }else{
                //create new serie
                serie = new Serie(title);
            }       
            updateSerieData(serie);
            serie = DataManager.INSTANCE.updateSerie((SerieCollection)collection, serie, title, seasonList);
        }  
        return serie;
    }    
      
    /**
     * Updates all attributes from the serie
     * 
     * @param serie
     */
    private void updateSerieData(Serie serie){
        updateLocalVideoFileData(serie);
    }
    
    @Override
    protected boolean isInternetAndAPIAvailable(){      
        return (Utils.isInternetConnectionAvailable() && new TheTVDB().isAPIenabled());
    }
        
    @Override
    public void changeUI(){
        super.changeUI();  
        jScrollPane2.getHorizontalScrollBar().setUI((BasicScrollBarUI)MyBasicScrollBarUI.createUI(jScrollPane2));
        jScrollPane2.getVerticalScrollBar().setUI((BasicScrollBarUI)MyBasicScrollBarUI.createUI(jScrollPane2));  
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
