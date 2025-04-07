package com.bushnell;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.io.File;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.Paths;
import java.security.ProtectionDomain;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import com.bushnell.GUI;
import com.bushnell.Database;
import com.bushnell.Part;

public class StockReport {

    public static JPanel makeGUI(String dbDir, boolean makePdf) {

        // create panel
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // set background color
        panel.setBackground(Color.WHITE); 

        // create title
        Box titleBox = Box.createVerticalBox();
        titleBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleBox.setAlignmentY(Component.TOP_ALIGNMENT);
        titleBox.add(GUI.text("Stock Report", 600, 30, 20, Color.BLACK, "center", true));
        GUI.setDimension(titleBox, 600, 50);
        panel.add(titleBox);



        // create text title for sku list
        Box textTitleBox = Box.createHorizontalBox();
        textTitleBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        textTitleBox.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        textTitleBox.add(Box.createRigidArea(new Dimension(20,0)));
        String textStr = String.format("%35s %8s %6s %s\n", "SKU", "Price", "Stock", "Description");
        JTextArea textTitle = new JTextArea(1,60);
        textTitle.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
        textTitle.setEditable(false);
        textTitle.append(textStr);
        textTitle.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
        textTitleBox.add(textTitle);
        textTitleBox.add(Box.createRigidArea(new Dimension(20,0)));
        panel.add(textTitleBox);

        // create text pane
        Box textBox = Box.createHorizontalBox();
        textBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        textBox.setAlignmentY(Component.TOP_ALIGNMENT);
        textBox.add(Box.createRigidArea(new Dimension(20,0)));
        JTextArea stockText = new JTextArea(30, 60);  // 30 rows and 60 columns
        stockText.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        stockText.setEditable(false); 
        JScrollPane scroll = new JScrollPane(stockText);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        List<Part> allSkuList = Database.getAllSkuData();
        for( Part part : allSkuList) {
            String newPart = String.format("%35s %8s %4d   %s\n", 
            part.sku, String.format("$%.2f", part.price), part.stock, part.description);
            stockText.append(newPart);
        }
        textBox.add(scroll);
        textBox.add(Box.createRigidArea(new Dimension(20,0)));
        panel.add(textBox);

        makePdf = true;
        if (makePdf) {
            // create PDF document
            // print N sku entries per page
            // with header on the top of each page
            PDDocument document = new PDDocument();
            //PDPage pdfPage = null;
                    
            /*
            // set this flag true at the beginning of each page
            // indicating the title should be put on the top of the page
            boolean newPage = true;
            String topOfPageStr = String.format("%35s %8s %6s %s\n", "SKU", "Price", "Stock", "Description");
            int skuCounter = 0;
            int skuPerPage = 20;

            // loop through all skus
            for( Part part : allSkuList) {
                if (newPage) {
                    // put the list title at the top of a new page
                    pdfPage = new PDPage();
                    document.addPage(pdfPage);
                    // write topOfPageStr underlined at the top of the page
                    newPage = false;
                }
                String newPart = String.format("%35s %8s %4d   %s", 
                    part.sku, String.format("$%.2f", part.price), part.stock, part.description);
                // write newPart in document
                skuCounter++;
                if (skuCounter % skuPerPage == 0) {
                    // create a new page
                    newPage = true;
                }
            }
            */

            //save PDF document
            String fileName = "StockReport.pdf";
            String fullPath = Paths.get(dbDir, fileName).toString();
            try {
                File existingFile = new File(fullPath);
                if (existingFile.exists()) {
                    existingFile.delete();
                }                
                document.save(fullPath);
                System.out.print("saved pdf file in " + fullPath);
            } catch(Exception e) {
                System.out.println("can't save PDF file");
                e.printStackTrace(System.err);
            }
            try {
                document.close();
            } catch (IOException e) {
                e.printStackTrace(System.err);
            }
        }

        return panel; 
    }
}
