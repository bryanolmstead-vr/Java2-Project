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
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.Paths;
import java.security.ProtectionDomain;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.SpinnerNumberModel;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import com.bushnell.GUI;
import com.bushnell.Database;
import com.bushnell.Part;

public class DemandAnalysis {

    public static JPanel panel = new JPanel();
    public static JComboBox<String> skuList;
    public static String[] skuArray;
    public static JLabel skuDescription;
    public static JSpinner desiredQtySpinner = new JSpinner();
    public static JTextArea stockText = new JTextArea(30, 60);  // 30 rows and 60 columns

    public static JPanel makeGUI(String dbDir, boolean makePdf) throws IOException {

        // create panel
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // set background color
        panel.setBackground(Color.WHITE); 

        // create title
        Box titleBox = Box.createVerticalBox();
        titleBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleBox.setAlignmentY(Component.TOP_ALIGNMENT);
        titleBox.add(GUI.text("Demand Analysis", 600, 30, 20, Color.BLACK, "center", true));
        GUI.setDimension(titleBox, 600, 50);
        panel.add(titleBox);

        // create sku entry
        Box skuBox = Box.createHorizontalBox();
        skuBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        skuBox.setAlignmentY(Component.TOP_ALIGNMENT);
        skuBox.add(GUI.text("sku", 150, 30, 20, Color.BLACK, "right", true));
        skuBox.add(Box.createRigidArea(new Dimension(50,0)));
        skuArray = Database.getSkuList("SUB%");
        skuList = new JComboBox<String>(skuArray);
        GUI.setDimension(skuList, 350, 40);
        skuList.setFont(new Font("Sans-Serif", Font.BOLD, 20));
        skuBox.add(skuList);
        GUI.setDimension(skuBox, 600, 50);
        panel.add(skuBox);

        // look up initial sku
        String sku = (String) skuList.getSelectedItem();
        Part part = Database.getSkuData(sku);

        // create description
        Box descriptionBox = Box.createHorizontalBox();
        descriptionBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        descriptionBox.setAlignmentY(Component.TOP_ALIGNMENT);
        descriptionBox.add(GUI.text("description", 150, 30, 20, Color.BLACK, "right", true));
        descriptionBox.add(Box.createRigidArea(new Dimension(50,0)));
        skuDescription = GUI.text(part.description, 300, 30, 20, Color.BLACK, "left", false);
        skuDescription.setText(part.description);
        descriptionBox.add(skuDescription);
        GUI.setDimension(descriptionBox, 600, 50);
        //descriptionBox.setBorder(new LineBorder(Color.RED));
        panel.add(descriptionBox);

        // create desired qty spinner
        Box qtySpinnerBox = Box.createHorizontalBox();
        qtySpinnerBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        qtySpinnerBox.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        qtySpinnerBox.add(GUI.text("desired quantity", 175, 30, 20, Color.BLACK, "right", true));
        qtySpinnerBox.add(Box.createRigidArea(new Dimension(50,0)));
        desiredQtySpinner.setModel(new SpinnerNumberModel(1,1,100,1));
        GUI.setDimension(desiredQtySpinner, 50,30);
        qtySpinnerBox.add(desiredQtySpinner);
        qtySpinnerBox.add(Box.createRigidArea(new Dimension(400,0)));
        GUI.setDimension(qtySpinnerBox, 650, 50);
        panel.add(qtySpinnerBox);

        // create text title for sku list
        Box textTitleBox = Box.createHorizontalBox();
        textTitleBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        textTitleBox.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        textTitleBox.add(Box.createRigidArea(new Dimension(20,0)));
        String textStr = String.format("%35s  Need  Description\n", "SKU");
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
        stockText.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        stockText.setEditable(false); 
        JScrollPane scroll = new JScrollPane(stockText);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        // populate initial demand list
        updateDemandList();
        textBox.add(scroll);
        textBox.add(Box.createRigidArea(new Dimension(20,0)));
        panel.add(textBox);

        // create listener for sku selection
        ActionListener skuListListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String sku = (String) skuList.getSelectedItem();
                Part part = Database.getSkuData(sku);
                skuDescription.setText(part.description);
                updateDemandList();
            }
        };
        skuList.addActionListener(skuListListener);

        desiredQtySpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                updateDemandList();
            }
        });

        // make PDF button
        Box pdfButtonBox = Box.createVerticalBox();
        pdfButtonBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        pdfButtonBox.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        JButton pdfButton = GUI.button("Make PDF",    200, 50, 20);
        Color visualRoboticsGreen = Color.decode("#00af74");
        pdfButton.setBackground(visualRoboticsGreen);
        pdfButton.setForeground(Color.WHITE);
        pdfButtonBox.add(pdfButton);
        GUI.setDimension(pdfButtonBox, 600, 50);
        //bundleBox.setBorder(new LineBorder(Color.RED));
        panel.add(Box.createRigidArea(new Dimension(0,20)));
        panel.add(pdfButtonBox);
        panel.add(Box.createRigidArea(new Dimension(0,20)));

        // make PDF button listener
        pdfButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Making PDF file");
                try {
                    makePDF(dbDir); 
                } catch(Exception e2) {
                    System.out.println("can't save PDF file");
                    e2.printStackTrace(System.err);
                }
            }
        });

        return panel; 
    }

    public static List<Part> updateDemandList() {
        int desiredQty = (int) desiredQtySpinner.getValue();
        String sku = (String) skuList.getSelectedItem();
        List<Part> allSkuList = Database.getRequiredStock(sku, desiredQty);
        stockText.setText("");
        for( Part part2 : allSkuList) {
            String newPart = String.format("%35s  %4d  %s\n", 
            part2.sku, part2.quantity, part2.description);
            stockText.append(newPart);
        }
        return allSkuList;
    }

    public static void makePDF(String dbDir) throws IOException {
        // determine PDF filename
        // VR-StockReport-2025.04.07-13.05.pdf
        // where 13.05 is 1:05pm in 24hr time format

        Date now = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd-HH.mm");
        String formattedDateTime = formatter.format(now);
        String filenamePDF = "DemandAnalysis-" + formattedDateTime + ".pdf";

        // create PDF document
        // print N sku entries per page
        // with header on the top of each page
        // first page should have a title
        PDDocument document = new PDDocument();
        PDPage pdfPage = null;
        int skuPerPage = 40;

        // set this flag true at the beginning of each page
        // indicating the sku column title should be put on the top of the page
        boolean newPage = true;
        String columnTitle = String.format("%35s  Need  Description", "SKU");
        int skuCounter = 0;
        int pageNum = 0;

        // get sku, qty, and demand list
        List<Part> allSkuList =  updateDemandList();
        int desiredQty = (int) desiredQtySpinner.getValue();
        String sku = (String) skuList.getSelectedItem();

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
                // write Visual Robotics Demand Analysis
                contentStream.beginText();
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.COURIER_BOLD),20);
                contentStream.newLineAtOffset(150,750);
                contentStream.showText("Visual Robotics Demand Analysis");
                contentStream.endText();
                // write date and page number
                contentStream.beginText();
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.COURIER),12);
                contentStream.newLineAtOffset(220,730);
                contentStream.showText(formattedDateTime + " page " + Integer.toString(pageNum+1));
                contentStream.endText();
                // write part and desired qty
                contentStream.beginText();
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.COURIER),12);
                contentStream.newLineAtOffset(220,700);
                contentStream.showText("SKU: " + sku + "  Qty: " + Integer.toString(desiredQty));
                contentStream.endText();
                // write header for columns
                contentStream.beginText();
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.COURIER_BOLD),10);
                contentStream.newLineAtOffset(0,680);
                contentStream.showText(columnTitle);
                contentStream.endText();
                // draw line under column header
                contentStream.setLineWidth(1f);
                contentStream.moveTo(10, 675);
                contentStream.lineTo(602, 675);
                contentStream.stroke();
                // set leading and start position for rows of parts
                contentStream.beginText();
                contentStream.setLeading(14.5f);
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.COURIER),10);
                contentStream.newLineAtOffset(0,660);
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
        if (allSkuList.size() == 0) {
            // no parts - no pages made yet - make 1 page
            pdfPage = new PDPage();
            document.addPage(pdfPage);
            pdfPage = document.getPage(pageNum);
            PDPageContentStream contentStream2 = new PDPageContentStream(document, pdfPage);
            // write Visual Robotics Demand Analysis
            contentStream2.beginText();
            contentStream2.setFont(new PDType1Font(Standard14Fonts.FontName.COURIER_BOLD),20);
            contentStream2.newLineAtOffset(150,750);
            contentStream2.showText("Visual Robotics Demand Analysis");
            contentStream2.endText();
            // write date and page number
            contentStream2.beginText();
            contentStream2.setFont(new PDType1Font(Standard14Fonts.FontName.COURIER),12);
            contentStream2.newLineAtOffset(220,730);
            contentStream2.showText(formattedDateTime + " page " + Integer.toString(pageNum+1));
            contentStream2.endText();
            // write part and desired qty
            contentStream2.beginText();
            contentStream2.setFont(new PDType1Font(Standard14Fonts.FontName.COURIER),12);
            contentStream2.newLineAtOffset(220,700);
            contentStream2.showText("SKU: " + sku + "  Qty: " + Integer.toString(desiredQty));
            contentStream2.endText();
            // write header for columns
            contentStream2.beginText();
            contentStream2.setFont(new PDType1Font(Standard14Fonts.FontName.COURIER_BOLD),10);
            contentStream2.newLineAtOffset(0,680);
            contentStream2.showText(columnTitle);
            contentStream2.endText();
            // draw line under column header
            contentStream2.setLineWidth(1f);
            contentStream2.moveTo(10, 675);
            contentStream2.lineTo(602, 675);
            contentStream2.stroke();
            // write header for columns
            contentStream2.beginText();
            contentStream2.setFont(new PDType1Font(Standard14Fonts.FontName.COURIER_BOLD),10);
            contentStream2.newLineAtOffset(20,650);
            contentStream2.showText("all parts in stock");
            contentStream2.endText();
            contentStream2.close();
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

    
    public static void main(String[] args) throws Exception {
        JFrame frame = new JFrame("MRP");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(1000, 720));
        frame.setMaximumSize(new Dimension(1000, 720));
        JPanel homePanel = makeGUI("", false);
        frame.add(homePanel);
        frame.pack();   
        frame.setVisible(true); 
    }
}
