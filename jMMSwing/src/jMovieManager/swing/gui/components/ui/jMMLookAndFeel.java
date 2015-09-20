/**
 * Copyright (c) 2010-2015 Bryan Beck.
 * All rights reserved.
 * 
 * This project is licensed under LGPL v2.1.
 * See jMovieManager-license.txt for details.
 * 
 */
package jMovieManager.swing.gui.components.ui;

import java.awt.Color;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.border.EtchedBorder;
import jMovieManager.swing.gui.ColorInterface;
import jMovieManager.swing.gui.font.JMMFont;
import jMovieManager.swing.gui.other.MyIconFactory;

/**
 * Provides look and feel specific settings
 * 
 * @author Bryan Beck
 * @since 14.02.2013
 */
public abstract class jMMLookAndFeel {
    
    /**
     * Returns the default UI LAF settings 
     * @return the default settings
     */
    public static Object[] getDefaultUISettings(){
        Object[] newDefaults = {            
            //UI Zuweisungen
            "OptionPaneUI", MyOptionPaneUI.class.getName(),                 //UI Klassenname   
            MyOptionPaneUI.class.getName(), MyOptionPaneUI.class,           //Referenz des Klassennamens zur Klasse
            
            "FileChooserUI", MyFileChooserUI.class.getName(),          
            MyFileChooserUI.class.getName(), MyFileChooserUI.class,
            
            "MenuUI", MyBasicMenuUI.class.getName(),                        //UI Klassenname  
            MyBasicMenuUI.class.getName(), MyBasicMenuUI.class,             //Referenz des Klassennamens zur Klasse            
            
            "MenuItemUI", MyBasicMenuItemUI.class.getName(),              
            MyBasicMenuItemUI.class.getName(), MyBasicMenuItemUI.class,     
            
            "CheckBoxMenuItemUI", MyBasicCheckBoxMenuItemUI.class.getName(),              
            MyBasicCheckBoxMenuItemUI.class.getName(), MyBasicCheckBoxMenuItemUI.class, 
            
            "RadioButtonMenuItemUI", MyBasicRadioButtonMenuItemUI.class.getName(),              
            MyBasicRadioButtonMenuItemUI.class.getName(), MyBasicRadioButtonMenuItemUI.class, 
            
            "ButtonUI", MyBasicButtonUI.class.getName(),                     
            MyBasicButtonUI.class.getName(), MyBasicButtonUI.class,         
            
            "TabbedPaneUI", MyTabbedPaneUI.class.getName(),                
            MyTabbedPaneUI.class.getName(), MyTabbedPaneUI.class,           
            
            "ComboBoxUI", MyBasicComboBoxUI.class.getName(),                
            MyBasicComboBoxUI.class.getName(), MyBasicComboBoxUI.class,                       
            
            "ScrollBarUI", MyBasicScrollBarUI.class.getName(),              //Standard UI Klasse mit createUI(null) Aufruf
            MyBasicScrollBarUI.class.getName(), MyBasicScrollBarUI.class,   //Muss für anderen Hintergrund mit createUI(component) aufgerufen werden!     
        
            "ScrollPaneUI", MyBasicScrollPaneUI.class.getName(),              
            MyBasicScrollPaneUI.class.getName(), MyBasicScrollPaneUI.class,    
            
            "RadioButtonUI", MyBasicRadioButtonUI.class.getName(),             
            MyBasicRadioButtonUI.class.getName(), MyBasicRadioButtonUI.class,
            
            "CheckBoxUI", MyBasicCheckBoxUI.class.getName(),                  
            MyBasicCheckBoxUI.class.getName(), MyBasicCheckBoxUI.class,      
 
            "ProgressBarUI", MyBasicProgressBarUI.class.getName(),          
            MyBasicProgressBarUI.class.getName(), MyBasicProgressBarUI.class,   
            
            "SeparatorUI", MyBasicSeparatorUI.class.getName(),          
            MyBasicSeparatorUI.class.getName(), MyBasicSeparatorUI.class,              
            
            "PopupMenuSeparatorUI", MyBasicPopupMenuSeperatorUI.class.getName(),          
            MyBasicPopupMenuSeperatorUI.class.getName(), MyBasicPopupMenuSeperatorUI.class,      
            
            
            // Buttons
            "Button.font", JMMFont.buttonFont,
            "Button.background", ColorInterface.list_tree_Background,
//            // Button.foreground, Button.shadow, Button.darkShadow,
//            // Button.disabledForground, and Button.disabledShadow are only
//            // used for Windows Classic. Windows XP will use colors
//            // from the current visual style.
            "Button.foreground", ColorInterface.listText,
//            "Button.shadow", ColorInterface.list_tree_BorderShadow,
//            "Button.select", ColorInterface.selectedListText,           //Selektierter Background
//            "Button.darkShadow", ControlDarkShadowColor,
//            "Button.light", ColorInterface.list_tree_BorderShadow,
//            "Button.highlight", ColorInterface.selectedListText,
            "Button.disabledForeground", ColorInterface.darkTextButton,

            //            "Button.disabledShadow", ControlHighlightColor,
            "Button.focus", ColorInterface.list_tree_Background,
//            "Button.dashedRectGapX", new XPValue(Integer.valueOf(3), Integer.valueOf(5)),
//            "Button.dashedRectGapY", new XPValue(Integer.valueOf(3), Integer.valueOf(4)),
//            "Button.dashedRectGapWidth", new XPValue(Integer.valueOf(6), Integer.valueOf(10)),
//            "Button.dashedRectGapHeight", new XPValue(Integer.valueOf(6), Integer.valueOf(8)),
//            "Button.textShiftOffset", new XPValue(Integer.valueOf(0),
//                                                  Integer.valueOf(1)),
            // W2K keyboard navigation hidding.

               //Checkbox
            "CheckBox.font", JMMFont.normalIOFont,
//            "CheckBox.interiorBackground", WindowBackgroundColor,
            "CheckBox.background", ColorInterface.panelBackground,
            "CheckBox.foreground", ColorInterface.listText,
//            "CheckBox.shadow", ControlShadowColor,
//            "CheckBox.darkShadow", ControlDarkShadowColor,
            "CheckBox.icon", MyIconFactory.checkBox_default,
//            "CheckBox.light", ControlLightColor,
//            "CheckBox.highlight", ColorInterface.panelBackground,
            "CheckBox.focus", ColorInterface.list_tree_Background, //ColorInterface.panelBackground,           //Umrandung bei Focus
            
            //CheckboxMenuItem
//            "CheckBoxMenuItem.font", JMMFont.normalIOFont,
            "CheckBoxMenuItem.background", ColorInterface.menuItemBackground,
            "CheckBoxMenuItem.foreground", ColorInterface.menuItemForeground,
            "CheckBoxMenuItem.selectionForeground", ColorInterface.textfieldForeground,
            "CheckBoxMenuItem.selectionBackground", ColorInterface.menuItemBackground,
//            "CheckBoxMenuItem.acceleratorForeground", MenuTextColor,
//            "CheckBoxMenuItem.acceleratorSelectionForeground", SelectionTextColor,
//            "CheckBoxMenuItem.commandSound", "win.sound.menuCommand",     
            
            "CheckBoxMenuItem.checkIcon", MyIconFactory.checkBox_default,
            
            

            "TextField.font", JMMFont.normalIOFont,
            "TextField.background", ColorInterface.selectionBackground, //new Color(56, 56, 56),
            "TextField.foreground", ColorInterface.textfieldForeground, //new Color(255, 214, 52),
//            "TextField.shadow", ControlShadowColor,
//            "TextField.darkShadow", ControlDarkShadowColor,
//            "TextField.light", ControlLightColor,
//            "TextField.highlight", ControlHighlightColor,
            "TextField.inactiveForeground", ColorInterface.darkText, //ColorInterface.darkText,      // for disabled
//            "TextField.inactiveBackground", ReadOnlyTextBackground,                       // for readonly
//            "TextField.disabledBackground", DisabledTextBackground,                       // for disabled
            "TextField.selectionBackground", ColorInterface.selectionBackground,
            "TextField.selectionForeground", ColorInterface.selectedListText,               //Textselektierung
            "TextField.caretForeground", ColorInterface.caretColor,                         //Caret Color - Farbe der Mausposition in jTextFields
            
            //Combobox
            "ComboBox.font", JMMFont.normalIOFont,
            "ComboBox.background", ColorInterface.selectionBackground,
            "ComboBox.foreground", ColorInterface.textfieldForeground,
//            "ComboBox.buttonBackground", selectionBackground,
//            "ComboBox.buttonShadow", Color.WHITE, //ControlShadowColor,
//            "ComboBox.buttonDarkShadow", Color.WHITE, //new Color(128, 128, 128),
//            "ComboBox.buttonHighlight", Color.WHITE, //ControlHighlightColor,
            "ComboBox.selectionBackground", ColorInterface.selectionBackground,
            "ComboBox.selectionForeground", ColorInterface.selectedListText, //Color.WHITE,
//            "ComboBox.editorBorder", new XPValue(new EmptyBorder(1,2,1,1),
//                                                 new EmptyBorder(1,4,1,4)),
            "ComboBox.disabledBackground",ColorInterface.selectionBackground,
//                        new XPColorValue(Part.CP_COMBOBOX, State.DISABLED,
//                        Prop.FILLCOLOR, DisabledTextBackground),
            "ComboBox.disabledForeground", ColorInterface.darkText,
//                        new XPColorValue(Part.CP_COMBOBOX, State.DISABLED,
//                        Prop.TEXTCOLOR, InactiveTextColor),
            "ComboBox.padding", new Insets(2, 5, 2, 2),


            //EditorPane
//            "EditorPane.font", ControlFont,
//            "EditorPane.background", WindowBackgroundColor,
//            "EditorPane.foreground", WindowTextColor,
//            "EditorPane.selectionBackground", SelectionBackgroundColor,
//            "EditorPane.selectionForeground", SelectionTextColor,
//            "EditorPane.caretForeground", WindowTextColor,
//            "EditorPane.inactiveForeground", InactiveTextColor,
//            "EditorPane.inactiveBackground", WindowBackgroundColor,
//            "EditorPane.disabledBackground", DisabledTextBackground,

            //FileChooser
            "FileChooser.homeFolderIcon", MyIconFactory.fileChooser_homeFolderIcon,
//            "FileChooser.listFont", IconFont,
//            "FileChooser.listViewBackground", new XPColorValue(Part.LVP_LISTVIEW, null, Prop.FILLCOLOR,
//                                                               WindowBackgroundColor),
//            "FileChooser.listViewBorder", new XPBorderValue(Part.LVP_LISTVIEW,
//                                                  new SwingLazyValue(
//                                                        "javax.swing.plaf.BorderUIResource",
//                                                        "getLoweredBevelBorderUIResource")),

//            "FileChooser.listViewWindowsStyle", Boolean.TRUE,

//            "FileChooser.viewMenuIcon", new LazyWindowsIcon("fileChooserIcon ViewMenu",
//                                                            "icons/ListView.gif"),
            "FileChooser.upFolderIcon", MyIconFactory.fileChooser_upFolderIcon,
            "FileChooser.newFolderIcon", MyIconFactory.fileChooser_newFolderIcon,
//            "FileChooser.useSystemExtensionHiding", Boolean.TRUE,
            "FileChooser.detailsViewIcon", MyIconFactory.fileChooser_detailsViewIcon,
            "FileChooser.detailsViewSelectedIcon", MyIconFactory.fileChooser_detailsViewSelectedIcon,
            "FileChooser.listViewIcon", MyIconFactory.fileChooser_listViewIcon,
            "FileChooser.listViewSelectedIcon", MyIconFactory.fileChooser_listViewSelectedIcon,
//            "FileChooser.lookInLabelMnemonic", Integer.valueOf(KeyEvent.VK_I),
//            "FileChooser.fileNameLabelMnemonic", Integer.valueOf(KeyEvent.VK_N),
//            "FileChooser.filesOfTypeLabelMnemonic", Integer.valueOf(KeyEvent.VK_T),
//            "FileChooser.usesSingleFilePane", Boolean.TRUE,
//            "FileChooser.noPlacesBar", new DesktopProperty("win.comdlg.noPlacesBar",
//                                                           Boolean.FALSE),
            
            "FileView.directoryIcon", MyIconFactory.fileChooser_directoryIcon,
            "FileView.fileIcon", MyIconFactory.fileChooser_fileIcon,
//            "FileView.computerIcon", MyIconFactory.fileChooser_computerIcon, 
            "FileView.hardDriveIcon", MyIconFactory.fileChooser_hardDriveIcon, 
            
//            UIManager.getSwingUtilities2.makeIcon(getClass(),
//                                                               WindowsLookAndFeel.class,
//                                                               "icons/HardDrive.gif"),
//            "FileView.floppyDriveIcon", SwingUtilities2.makeIcon(getClass(),
//                                                                 WindowsLookAndFeel.class,
//                                                                 "icons/FloppyDrive.gif"),

            //FormattedTextField
//            "FormattedTextField.font", ControlFont,
            
            //InternalFrame
//            "InternalFrame.titleFont", WindowFont,
//            "InternalFrame.titlePaneHeight",   TitlePaneHeight,
//            "InternalFrame.titleButtonWidth",  TitleButtonWidth,
//            "InternalFrame.titleButtonHeight", TitleButtonHeight,
//            "InternalFrame.titleButtonToolTipsOn", hotTrackingOn,
//            "InternalFrame.borderColor", ColorInterface.selectedListText,
//            "InternalFrame.borderShadow", ControlShadowColor,
//            "InternalFrame.borderDarkShadow", ControlDarkShadowColor,
//            "InternalFrame.borderHighlight", ControlHighlightColor,
//            "InternalFrame.borderLight", ControlLightColor,
//            "InternalFrame.borderWidth", WindowBorderWidth,
//            "InternalFrame.minimizeIconBackground", ControlBackgroundColor,
//            "InternalFrame.resizeIconHighlight", ControlLightColor,
//            "InternalFrame.resizeIconShadow", ControlShadowColor,
//            "InternalFrame.activeBorderColor", new DesktopProperty(
//                                                       "win.frame.activeBorderColor",
//                                                       table.get("windowBorder")),
//            "InternalFrame.inactiveBorderColor", new DesktopProperty(
//                                                       "win.frame.inactiveBorderColor",
//                                                       table.get("windowBorder")),
//            "InternalFrame.activeTitleBackground", new DesktopProperty(
//                                                        "win.frame.activeCaptionColor",
//                                                         table.get("activeCaption")),
//            "InternalFrame.activeTitleGradient", new DesktopProperty(
//                                                        "win.frame.activeCaptionGradientColor",
//                                                         table.get("activeCaption")),
//            "InternalFrame.activeTitleForeground", new DesktopProperty(
//                                                        "win.frame.captionTextColor",
//                                                         table.get("activeCaptionText")),
//            "InternalFrame.inactiveTitleBackground", new DesktopProperty(
//                                                        "win.frame.inactiveCaptionColor",
//                                                         table.get("inactiveCaption")),
//            "InternalFrame.inactiveTitleGradient", new DesktopProperty(
//                                                        "win.frame.inactiveCaptionGradientColor",
//                                                         table.get("inactiveCaption")),
//            "InternalFrame.inactiveTitleForeground", new DesktopProperty(
//                                                        "win.frame.inactiveCaptionTextColor",
//                                                         table.get("inactiveCaptionText")),
//
//            "InternalFrame.maximizeIcon",
//                WindowsIconFactory.createFrameMaximizeIcon(),
//            "InternalFrame.minimizeIcon",
//                WindowsIconFactory.createFrameMinimizeIcon(),
//            "InternalFrame.iconifyIcon",
//                WindowsIconFactory.createFrameIconifyIcon(),
//            "InternalFrame.closeIcon",
//                WindowsIconFactory.createFrameCloseIcon(),
//            "InternalFrame.icon",
//                new SwingLazyValue(
//        "com.sun.java.swing.plaf.windows.WindowsInternalFrameTitlePane$ScalableIconUIResource",
//                    // The constructor takes one arg: an array of UIDefaults.LazyValue
//                    // representing the icons
//                    new Object[][] { {
//                        SwingUtilities2.makeIcon(getClass(), BasicLookAndFeel.class, "icons/JavaCup16.png"),
//                        SwingUtilities2.makeIcon(getClass(), WindowsLookAndFeel.class, "icons/JavaCup32.png")
//                    } }),
//
//            // Internal Frame Auditory Cue Mappings
//            "InternalFrame.closeSound", "win.sound.close",
//            "InternalFrame.maximizeSound", "win.sound.maximize",
//            "InternalFrame.minimizeSound", "win.sound.minimize",
//            "InternalFrame.restoreDownSound", "win.sound.restoreDown",
//            "InternalFrame.restoreUpSound", "win.sound.restoreUp",
//
//            "InternalFrame.windowBindings", new Object[] {
//                "shift ESCAPE", "showSystemMenu",
//                  "ctrl SPACE", "showSystemMenu",
//                      "ESCAPE", "hideSystemMenu"},

            // Label
            "Label.font", JMMFont.normalIOFont,
//            "Label.background", ControlBackgroundColor,
            "Label.foreground", ColorInterface.listText,
            "Label.disabledForeground", ColorInterface.menuItemDisabledForeground,
//            "Label.disabledShadow", ControlHighlightColor,

            // List.
            "List.font", JMMFont.largeIOFont,     
            "List.background", ColorInterface.list_tree_Background, //new Color(29, 29, 29),
            "List.foreground", ColorInterface.listText,
            "List.selectionBackground", ColorInterface.selectionBackground, //selectionBackground, //new Color(56, 56, 56),
            "List.selectionForeground", ColorInterface.selectedListText,
//            "List.lockToPositionOnScroll", Boolean.TRUE,

            // PopupMenu
//            "PopupMenu.font", MenuFont,
            "PopupMenu.background", ColorInterface.popupMenuBorder,          //Popupmenu + MenuItems oben, unten
//            "PopupMenu.foreground", ColorInterface.selectedListText,          //Popupmenu + MenuItems
//            "PopupMenu.popupSound", "win.sound.menuPopup",        //
//            "PopupMenu.consumeEventOnClose", Boolean.TRUE,        //
            
            // Menus
//            "Menu.font", MenuFont,
            "Menu.foreground",  ColorInterface.menuItemForeground,
            "Menu.background",  ColorInterface.menuItemBackground, //new Color(89, 89, 89),
//            "Menu.useMenuBarBackgroundForTopLevel", Boolean.TRUE,
            "Menu.selectionForeground", Color.WHITE, //ColorInterface.menuItemForeground,
            "Menu.selectionBackground", ColorInterface.menuItemBackground, 
//            "Menu.acceleratorForeground", Color.BLACK,
//            "Menu.acceleratorSelectionForeground", Color.BLACK,
            
            "Menu.menuPopupOffsetX", Integer.valueOf(0),
            "Menu.menuPopupOffsetY", Integer.valueOf(0),
//            "Menu.submenuPopupOffsetX", Integer.valueOf(-4),
//            "Menu.submenuPopupOffsetY", Integer.valueOf(-3),
//            "Menu.crossMenuMnemonic", Boolean.FALSE,
//            "Menu.preserveTopLevelSelection", Boolean.TRUE,

            // MenuBar.
//            "MenuBar.font", MenuFont,
//            "MenuBar.background", new XPValue(MenuBarBackgroundColor,
//                                              MenuBackgroundColor),
//            "MenuBar.foreground", MenuTextColor,
//            "MenuBar.shadow", ControlShadowColor,
//            "MenuBar.highlight", ControlHighlightColor,
//            "MenuBar.height", menuBarHeight,
//            "MenuBar.rolloverEnabled", hotTrackingOn,
//            "MenuBar.windowBindings", new Object[] {
//                "F10", "takeFocus" },

                //MenuItem
//            "MenuItem.font", MenuFont,
//            "MenuItem.acceleratorFont", MenuFont,
            "MenuItem.foreground", ColorInterface.menuItemForeground,
            "MenuItem.background", ColorInterface.menuItemBackground, //new Color(89, 89, 89),
            "MenuItem.selectionForeground", Color.WHITE, //ColorInterface.menuItemForeground, 
            "MenuItem.selectionBackground", ColorInterface.menuItemBackground, //ColorInterface.menuItemSelectedBackground, //new Color(255, 255, 255),
            "MenuItem.disabledForeground", ColorInterface.menuItemDisabledForeground,
//            "MenuItem.acceleratorForeground", MenuTextColor,
//            "MenuItem.acceleratorSelectionForeground", SelectionTextColor,
//            "MenuItem.acceleratorDelimiter", Color.BLACK,
            
            //RadioButton
//            "RadioButton.font", ControlFont,
//            "RadioButton.interiorBackground", WindowBackgroundColor,
            "RadioButton.background", ColorInterface.panelBackground,
            "RadioButton.foreground", ColorInterface.listText,
            "RadioButton.disabledForeground", ColorInterface.darkTextButton,
            
//            "RadioButton.shadow", ControlShadowColor,
//            "RadioButton.darkShadow", ControlDarkShadowColor,
            "RadioButton.icon", MyIconFactory.radioButton_default,
//            "RadioButton.light", ControlLightColor,
//            "RadioButton.highlight", ControlHighlightColor,
            "RadioButton.focus", ColorInterface.list_tree_Background, // ColorInterface.panelBackground,
            
            //RadioButtonMenuItem
//            "RadioButtonMenuItem.font", MenuFont,
            "RadioButtonMenuItem.foreground", ColorInterface.menuItemForeground,
            "RadioButtonMenuItem.background", ColorInterface.menuItemBackground,
            "RadioButtonMenuItem.selectionForeground", Color.WHITE, 
            "RadioButtonMenuItem.selectionBackground", ColorInterface.menuItemBackground,
            "RadioButtonMenuItem.disabledForeground", ColorInterface.menuItemDisabledForeground,
            "RadioButtonMenuItem.checkIcon", MyIconFactory.radioButton_default,
//            "RadioButtonMenuItem.acceleratorForeground", MenuTextColor,
//            "RadioButtonMenuItem.acceleratorSelectionForeground", SelectionTextColor,

            // OptionPane.
            "OptionPane.font", JMMFont.smallIOFont,
            "OptionPane.messageFont", JMMFont.normalIOFont,
            "OptionPane.buttonFont", JMMFont.buttonFont,
            "OptionPane.background", ColorInterface.panelBackground,
            "OptionPane.foreground", ColorInterface.listText,
            "OptionPane.buttonMinimumWidth", 80, //new XPDLUValue(50, 50, SwingConstants.EAST),
            "OptionPane.messageForeground",  ColorInterface.listText,
//            "OptionPane.errorIcon",       new LazyWindowsIcon("optionPaneIcon Error",
//                                                              "icons/Error.gif"),
//            "OptionPane.informationIcon", new LazyWindowsIcon("optionPaneIcon Information",
//                                                              "icons/Inform.gif"),
//            "OptionPane.questionIcon",    new LazyWindowsIcon("optionPaneIcon Question",
//                                                              "icons/Question.gif"),
//            "OptionPane.warningIcon",     new LazyWindowsIcon("optionPaneIcon Warning",
//                                                              "icons/Warn.gif"),
//            "OptionPane.windowBindings", new Object[] {
//                "ESCAPE", "close" },
//                 // Option Pane Auditory Cue Mappings
//            "OptionPane.errorSound", "win.sound.hand", // Error
//            "OptionPane.informationSound", "win.sound.asterisk", // Info Plain
//            "OptionPane.questionSound", "win.sound.question", // Question
//            "OptionPane.warningSound", "win.sound.exclamation", // Warning

            // *** FormattedTextField
//            "FormattedTextField.inactiveBackground", ReadOnlyTextBackground,
//            "FormattedTextField.disabledBackground", DisabledTextBackground,

            // *** Panel
//            "Panel.font", ControlFont,
            "Panel.background", ColorInterface.panelBackground, //new Color(18, 18, 18),
//            "Panel.foreground", WindowTextColor,

            // *** PasswordField
//            "PasswordField.font", ControlFont,
//            "PasswordField.background", TextBackground,
//            "PasswordField.foreground", WindowTextColor,
//            "PasswordField.inactiveForeground", InactiveTextColor,      // for disabled
//            "PasswordField.inactiveBackground", ReadOnlyTextBackground, // for readonly
//            "PasswordField.disabledBackground", DisabledTextBackground, // for disabled
//            "PasswordField.selectionBackground", SelectionBackgroundColor,
//            "PasswordField.selectionForeground", SelectionTextColor,
//            "PasswordField.caretForeground",WindowTextColor,
//            "PasswordField.echoChar", new XPValue(new Character((char)0x25CF),
//                                                  new Character('*')),

            // *** ProgressBar
            "ProgressBar.useCustomUI", Boolean.TRUE,
//            "ProgressBar.font", ControlFont,
//            "ProgressBar.foreground",  Color.GREEN,
//            "ProgressBar.background", Color.YELLOW,
//            "ProgressBar.shadow", ControlShadowColor,
//            "ProgressBar.highlight", Color.YELLOW,
//            "ProgressBar.selectionForeground", ControlBackgroundColor,
//            "ProgressBar.selectionBackground", SelectionBackgroundColor,
//            "ProgressBar.cellLength", Integer.valueOf(7),
//            "ProgressBar.cellSpacing", Integer.valueOf(2),
//            "ProgressBar.indeterminateInsets", new Insets(3, 3, 3, 3),

            // *** ScrollBar.
            "ScrollBar.background", ColorInterface.list_tree_Background, //panelBackground, //new Color(29, 29, 29),
//            "ScrollBar.foreground", new Color(128, 255, 128),
            "ScrollBar.track", ColorInterface.list_tree_Background, //panelBackground,                      //Hintergrund der Scrollbar
//            "ScrollBar.trackForeground", new Color(128, 255, 128),
            "ScrollBar.trackHighlight", ColorInterface.list_tree_Background, //panelBackground,
//            "ScrollBar.trackHighlightForeground", new Color(128, 255, 128),
            "ScrollBar.thumb", ColorInterface.scrollbarThumb,                       //Mittelding
            "ScrollBar.thumbHighlight", ColorInterface.scrollbarThumb,             //Linker Border
            "ScrollBar.thumbDarkShadow", ColorInterface.scrollbarThumb,            //Mittelding             //Rechter Border
            "ScrollBar.thumbShadow", ColorInterface.scrollbarThumb,                //Unten Rechts Border
            "ScrollBar.width", 8,

            // *** ScrollPane.
//            "ScrollPane.font", ControlFont,
            "ScrollPane.background", ColorInterface.panelBackground,
//            "ScrollPane.foreground", ColorInterface.panelBackground,
            "ScrollPane.border", null, //BorderFactory.createEmptyBorder(5, 5, 5, 5), //BorderFactory.createEtchedBorder(EtchedBorder.RAISED),// ColorInterface.titledBorderColor,ColorInterface.titledBorderColor ),

            // *** Separator
            "Separator.background", ColorInterface.seperatorBackground, //new Color(111, 111, 111),
            "Separator.foreground", ColorInterface.seperatorForeground, //new Color(111, 111, 111),

            // *** Slider.
//            "Slider.font", ControlFont,
//            "Slider.foreground", ControlBackgroundColor,
//            "Slider.background", ControlBackgroundColor,
//            "Slider.highlight", ControlHighlightColor,
//            "Slider.shadow", ControlShadowColor,
//            "Slider.focus", ControlDarkShadowColor,

            // Spinner
//            "Spinner.font", ControlFont,

            // *** SplitPane
            "SplitPane.background", ColorInterface.panelBackground, //new Color(51, 51, 51), //Color.BLACK,       
//            "SplitPane.highlight", Color.BLACK,     //SplitPane
//            "SplitPane.shadow", ControlShadowColor,
//            "SplitPane.darkShadow", ControlDarkShadowColor,
//            "SplitPane.dividerSize", Integer.valueOf(20),

            // *** TabbedPane
//            "TabbedPane.tabsOverlapBorder", BorderFactory.createBevelBorder(BevelBorder.RAISED),
//            "TabbedPane.tabInsets",         new XPValue(new InsetsUIResource(1, 4, 1, 4),
//                                                        new InsetsUIResource(0, 4, 1, 4)),
//            "TabbedPane.tabAreaInsets",     new XPValue(new InsetsUIResource(3, 2, 2, 2),
//                                                        new InsetsUIResource(3, 2, 0, 2)),
//            "TabbedPane.font", ControlFont,
            "TabbedPane.background", ColorInterface.panelBackground, // new Color(51, 51, 51),             //nicht selektierte Tabs
            "TabbedPane.selected", ColorInterface.list_tree_Background, //panel_list_tree_background,          //Inhalt Selektierter Tab
//            "TabbedPane.unselectedBackground", ColorInterface.panelBackground,                                //Inhalt unselektierter Tab
            
            "TabbedPane.contentAreaColor", ColorInterface.list_tree_Background,                                  //Inhalt der TabbedPane
            "TabbedPane.foreground", ColorInterface.listText,
            "TabbedPane.inactiveForeground", ColorInterface.menuItemDisabledForeground,
            
            
            
            //Border
            "TabbedPane.borderHightlightColor", ColorInterface.list_tree_BorderHighlight,                       //Left Border
            "TabbedPane.selectHighlight", ColorInterface.list_tree_Background, //list_tree_BorderShadow,                      //Selektierter Tab, TOP LEFT Border
////             "TabbedPane.unselectedBackground", ColorInterface.orange,
           "TabbedPane.highlight", ColorInterface.orange,
            "TabbedPane.light",  ColorInterface.panelBackground, //new Color(180, 180, 180),          //nicht selektierter Tab, Left Top Border
//            "TabbedPane.shadow", ColorInterface.orange,
            "TabbedPane.darkShadow", ColorInterface.list_tree_BorderShadow, //new Color(100, 100, 100),      //nicht selektierter Tab Shadow, Right Bottom Border
            "TabbedPane.focus", ColorInterface.list_tree_Background,                                                                //Tab fokussiert
            
                
            // *** Table
//            "Table.font", ControlFont,
            "Table.foreground", ColorInterface.listText,  // cell text color
            "Table.background", ColorInterface.list_tree_Background,  // cell background color
//            "Table.highlight", ControlHighlightColor,
//            "Table.light", ControlLightColor,
//            "Table.shadow", ControlShadowColor,
//            "Table.darkShadow", ControlDarkShadowColor,
            "Table.selectionForeground", ColorInterface.selectedListText,
            "Table.selectionBackground", ColorInterface.selectionBackground, //new Color(56, 56, 56),
            "Table.gridColor", ColorInterface.list_tree_BorderShadow,// grid line color
            "Table.focusCellBackground", ColorInterface.selectionBackground, //new Color(56, 56, 56),
            "Table.focusCellForeground", ColorInterface.selectedListText,
            //"Table.ascendingSortIcon", new ImageIcon(getClass().getResource("/jMovieManager/swing/textures/gtk_close.png")).getImage(), //Arrow_up.png")),
//            
//            "Table.sortIconHighlight", ControlShadowColor,
//            "Table.sortIconLight", white,
//
//            "TableHeader.font", ControlFont,
            "TableHeader.foreground", ColorInterface.listText, //darkText, // header text color
            "TableHeader.background", ColorInterface.selectionBackground, // new Color(56, 56, 56), // header background
//            "TableHeader.focusCellBackground",
//                new XPValue(XPValue.NULL_VALUE,     // use default bg from XP styles
//                            WindowBackgroundColor), // or white bg otherwise

            // *** TextArea
            "TextArea.font", JMMFont.largeIOFont,
            "TextArea.background", ColorInterface.selectionBackground, //new Color(56, 56, 56),
            "TextArea.foreground", Color.WHITE,
            "TextArea.inactiveForeground", ColorInterface.darkText,
//            "TextArea.inactiveBackground", WindowBackgroundColor,
//            "TextArea.disabledBackground", DisabledTextBackground,
            "TextArea.selectionBackground", ColorInterface.selectionBackground,
            "TextArea.selectionForeground", ColorInterface.selectedListText,
            "TextArea.caretForeground", ColorInterface.caretColor,
            "TextArea.border", BorderFactory.createEmptyBorder(2, 5, 5, 2),

            // *** TextField
            "TextField.font", JMMFont.normalIOFont,
            "TextField.background", ColorInterface.selectionBackground, //new Color(56, 56, 56),
            "TextField.foreground", Color.WHITE, //new Color(255, 214, 52),
//            "TextField.shadow", ControlShadowColor,
//            "TextField.darkShadow", ControlDarkShadowColor,
//            "TextField.light", ControlLightColor,
//            "TextField.highlight", ControlHighlightColor,
            "TextField.inactiveForeground", ColorInterface.darkText,             // for disabled
//            "TextField.inactiveBackground", ReadOnlyTextBackground,           // for readonly
//            "TextField.disabledBackground", DisabledTextBackground,           // for disabled
            "TextField.selectionBackground", ColorInterface.selectionBackground,
            "TextField.selectionForeground", ColorInterface.selectedListText,              //Textselektierung
//            "TextField.caretForeground", WindowTextColor,
            "TextField.border", BorderFactory.createEmptyBorder(2, 5, 2, 2),

            // *** TextPane
            "TextPane.font", JMMFont.largeIOFont,
            "TextPane.background", ColorInterface.panelBackground,
            "TextPane.foreground", ColorInterface.listText, 
            "TextPane.selectionBackground", ColorInterface.panelBackground,
            "TextPane.selectionForeground", Color.WHITE,
            "TextPane.inactiveBackground", ColorInterface.panelBackground,
            "TextPane.disabledBackground", ColorInterface.panelBackground,
//            "TextPane.caretForeground", new Color(18, 18, 18),

            // *** TitledBorder
            "TitledBorder.font", JMMFont.borderFont,
            "TitledBorder.titleColor", ColorInterface.titledBorderForeground, 
//            new XPColorValue(Part.BP_GROUPBOX, null, Prop.TEXTCOLOR,
//                                         WindowTextColor),

            // *** ToggleButton
//            "ToggleButton.font", ControlFont,
//            "ToggleButton.background", ControlBackgroundColor,
//            "ToggleButton.foreground", ControlTextColor,
//            "ToggleButton.shadow", ControlShadowColor,
//            "ToggleButton.darkShadow", ControlDarkShadowColor,
//            "ToggleButton.light", ControlLightColor,
//            "ToggleButton.highlight", ControlHighlightColor,
//            "ToggleButton.focus", ControlTextColor,
//            "ToggleButton.textShiftOffset", Integer.valueOf(1),

            // *** ToolBar
//            "ToolBar.font", MenuFont,
//            "ToolBar.background", ControlBackgroundColor,
//            "ToolBar.foreground", ControlTextColor,
//            "ToolBar.shadow", ControlShadowColor,
//            "ToolBar.darkShadow", ControlDarkShadowColor,
//            "ToolBar.light", ControlLightColor,
//            "ToolBar.highlight", ControlHighlightColor,
//            "ToolBar.dockingBackground", ControlBackgroundColor,
//            "ToolBar.dockingForeground", red,
//            "ToolBar.floatingBackground", ControlBackgroundColor,
//            "ToolBar.floatingForeground", darkGray,
//            "ToolBar.separatorSize", null,

            // *** ToolTip
//            "ToolTip.font", JMMFont.normalIOFont,
            "ToolTip.background", ColorInterface.tooltipBackground,        //new DesktopProperty("win.tooltip.backgroundColor", table.get("info")),
            "ToolTip.foreground", Color.WHITE,    //new DesktopProperty("win.tooltip.textColor", table.get("infoText")),
             //"ToolTip.border", BorderFactory.createEtchedBorder(EtchedBorder.RAISED, new Color(210, 210, 210), new Color(128, 128, 128)), 
            "ToolTip.border", BorderFactory.createEtchedBorder(EtchedBorder.RAISED), 
            "ToolTip.borderInactive", BorderFactory.createEtchedBorder(EtchedBorder.RAISED),
//            "ToolTip.border", BorderFactory.createLineBorder(ColorInterface.popupMenuBorder, 1, true), 
//            "ToolTip.borderInactive", BorderFactory.createLineBorder(ColorInterface.popupMenuBorder, 1, true),
            "ToolTip.foregroundInactive", ColorInterface.darkText,
            "ToolTip.backgroundInactive", ColorInterface.tooltipBackground,

        // *** ToolTipManager
//            "ToolTipManager.enableToolTipMode", "activeApplication",

        // *** Tree
//            "Tree.selectionBorderColor", black,
//            "Tree.drawDashedFocusIndicator", Boolean.TRUE,
            "Tree.font", JMMFont.largeIOFont,
            "Tree.background", ColorInterface.list_tree_Background, //new Color(29, 29, 29),
            "Tree.foreground", ColorInterface.listText,
            "Tree.hash", Color.WHITE,                                   //Farbe der Linien
//            "Tree.leftChildIndent", Integer.valueOf(8),
//            "Tree.rightChildIndent", Integer.valueOf(11),
            "Tree.paintLines", Boolean.TRUE,                                        //zeichne Linien
           // "Tree.lineTypeDashed", Boolean.TRUE,                                    //gestrichelte Linien
            "Tree.textForeground", ColorInterface.listText,
            "Tree.textBackground", ColorInterface.list_tree_Background, //new Color(29, 29, 29),
            "Tree.selectionForeground", ColorInterface.selectedListText,
            "Tree.selectionBackground", ColorInterface.selectionBackground,
            "Tree.expandedIcon",  MyIconFactory.tree_expandedIcon,
            "Tree.collapsedIcon", MyIconFactory.tree_collapsedIcon,
            "Tree.openIcon",  MyIconFactory.tree_openIcon,           //Ordner offen
            "Tree.closedIcon", MyIconFactory.tree_closedIcon,        //Ordner zu
            "Tree.leafIcon",  MyIconFactory.tree_leafIcon
//            // *** Viewport
//            "Viewport.font", ControlFont,
//            "Viewport.background", ControlBackgroundColor,
//            "Viewport.foreground", WindowTextColor,
        };   
        return newDefaults;
    }
}
