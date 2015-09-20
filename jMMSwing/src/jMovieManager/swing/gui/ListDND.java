//package jMovieManager.swing.gui;
//
//
//import java.awt.*;
//import java.awt.event.*;
//import java.util.*;
//import javax.swing.*;
///**
// *
// * @author Bryan
// */
//public class ListDND {
//    ReportListTransferHandler arrayListHandler = new ReportListTransferHandler();
//
//    private JPanel getContent() {
//        JPanel panel = new JPanel(new GridLayout(1,0));
//        panel.add(getListComponent("left"));
//        panel.add(getListComponent("right"));
//        return panel;
//    }
//
//    private JScrollPane getListComponent(String s) {
//        DefaultListModel model = new DefaultListModel();
//        for(int j = 0; j < 5; j++)
//            model.addElement(s + " " + (j+1));
//        JList list = new JList(model);
//        list.setName(s);
//        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
//        list.setTransferHandler(arrayListHandler);
//        list.setDragEnabled(true);
//        return new JScrollPane(list);
//    }
//
//    public static void main(String[] args) {
//        JFrame f = new JFrame();
//        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        f.getContentPane().add(new ListDND().getContent());
//        f.setSize(400,200);
//        f.setLocationRelativeTo(null);
//        f.setVisible(true);
//    }
//}
