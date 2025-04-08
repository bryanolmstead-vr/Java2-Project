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
import java.text.SimpleDateFormat;
import java.util.Date;

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
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import com.bushnell.GUI;
import com.bushnell.Database;
import com.bushnell.Part;

public class StockReport {

    public static JPanel makeGUI(String dbDir, boolean makePdf) throws IOException {

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

        // only make PDF file when asked (when button is pressed)
        if (makePdf) {
            // determine PDF filename
            // VR-StockReport-2025.04.07-13.05.pdf
            // where 13.05 is 1:05pm in 24hr time format

            Date now = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd-HH.mm");
            String formattedDateTime = formatter.format(now);
            String filenamePDF = "VR-StockReport-" + formattedDateTime + ".pdf";

            // create PDF document
            // print N sku entries per page
            // with header on the top of each page
            // first page should have a title
            PDDocument document = new PDDocument();
            PDPage pdfPage = null;
            int skuPerPage = 45;

            // set this flag true at the beginning of each page
            // indicating the sku column title should be put on the top of the page
            boolean newPage = true;
            String columnTitle = String.format("%35s %8s %6s %s", "SKU", "Price", "Stock", "Description");
            int skuCounter = 0;
            int pageNum = 0;

            // define contentStream to use later
            PDPageContentStream contentStream = null;

            // loop through all skus
            for( Part part : allSkuList) {
                if (newPage) {
                    // create new page and prepare to write on it
                    pdfPage = new PDPage();
                    document.addPage(pdfPage);
                    pdfPage = document.getPage(pageNum);
                    contentStream = new PDPageContentStream(document, pdfPage);
                    // write Visual Robotics Stock Report
                    contentStream.beginText();
                    contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.COURIER_BOLD),20);
                    contentStream.newLineAtOffset(150,750);
                    contentStream.showText("Visual Robotics Stock Report");
                    contentStream.endText();
                    // write date and page number
                    contentStream.beginText();
                    contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.COURIER),12);
                    contentStream.newLineAtOffset(220,730);
                    contentStream.showText(formattedDateTime + " page " + Integer.toString(pageNum+1));
                    contentStream.endText();
                    // write header for columns
                    contentStream.beginText();
                    contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.COURIER_BOLD),10);
                    contentStream.newLineAtOffset(0,700);
                    contentStream.showText(columnTitle);
                    contentStream.endText();
                    // draw line under column header
                    contentStream.setLineWidth(1f);
                    contentStream.moveTo(10, 695);
                    contentStream.lineTo(602, 695);
                    contentStream.stroke();
                    // set leading and start position for rows of parts
                    contentStream.beginText();
                    contentStream.setLeading(14.5f);
                    contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.COURIER),10);
                    contentStream.newLineAtOffset(0,685);
                    newPage = false;
                }
                String newPart = String.format("%35s %8s %4d   %s", 
                    part.sku, String.format("$%.2f", part.price), part.stock, part.description);
                contentStream.showText(newPart);
                contentStream.newLine();
                skuCounter++;
                if (skuCounter % skuPerPage == 0) {
                    // close this page and prepare for a new page
                    contentStream.endText();
                    contentStream.close();
                    pageNum++;
                    newPage = true;
                }
            }
            if (!newPage) {
                // close the remainder of the last page
                contentStream.endText();
                contentStream.close();
            }

            //save PDF document
            String fullPath = Paths.get(dbDir, filenamePDF).toString();
            try {
                File existingFile = new File(fullPath);
                if (existingFile.exists()) {
                    existingFile.delete();
                }                
                document.save(fullPath);
                //System.out.println(fullPath);
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
