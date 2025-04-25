package com.bushnell;

import com.bushnell.GUI;
import com.bushnell.Database;
import com.bushnell.Part;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Font;
//import java.awt.event.FocusEvent;
//import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

//import org.w3c.dom.events.MouseEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
//import javax.swing.JButton;
//import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
//import javax.swing.JTextField;
//import javax.swing.plaf.metal.MetalCheckBoxIcon;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.JSeparator;

public class Bundle {

    public static JLabel skuDescription;
    public static JLabel skuStock;
    public static JComboBox<String> skuList;
    public static JTextArea stockText;
    public static String[] skuArray;
    public static JScrollPane scroll;

    public static JPanel makeGUI() {

        System.out.println("called Bundle.makeGUI()");

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
        titleBox.add(GUI.text("Bundle", 600, 30, 20, Color.BLACK, "center", true));
        GUI.setDimension(titleBox, 600, 100);
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

        // create stock
        Box stockBox = Box.createHorizontalBox();
        stockBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        stockBox.setAlignmentY(Component.TOP_ALIGNMENT);
        stockBox.add(GUI.text("stock", 150, 30, 20, Color.BLACK, "right", true));
        stockBox.add(Box.createRigidArea(new Dimension(50,0)));
        skuStock = GUI.text(Integer.toString(part.stock), 50, 30, 20, Color.BLACK, "left", false);
        stockBox.add(skuStock);
        GUI.setDimension(stockBox, 600, 50);
        //stockBox.setBorder(new LineBorder(Color.RED));
        panel.add(stockBox);

        // make bundle button
        Box bundleBox = Box.createVerticalBox();
        bundleBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        bundleBox.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        JButton bundleButton = GUI.button("Bundle",    200, 50, 20);
        Color visualRoboticsGreen = Color.decode("#00af74");
        bundleButton.setBackground(visualRoboticsGreen);
        bundleButton.setForeground(Color.WHITE);
        bundleBox.add(bundleButton);
        GUI.setDimension(bundleBox, 600, 50);
        //bundleBox.setBorder(new LineBorder(Color.RED));
        panel.add(bundleBox);

        // create Subcomponents title
        Box subcomponentsTitleBox = Box.createVerticalBox();
        subcomponentsTitleBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        subcomponentsTitleBox.setAlignmentY(Component.TOP_ALIGNMENT);
        subcomponentsTitleBox.add(Box.createRigidArea(new Dimension(0,20)));
        subcomponentsTitleBox.add(GUI.text("Subcomponents", 600, 50, 20, Color.BLACK, "center", true));
        GUI.setDimension(subcomponentsTitleBox, 600, 70);
        //subcomponentsTitleBox.setBorder(new LineBorder(Color.RED));
        panel.add(subcomponentsTitleBox);

        // create text title for bundle list
        Box textTitleBox = Box.createHorizontalBox();
        textTitleBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        textTitleBox.setAlignmentY(Component.CENTER_ALIGNMENT);
        String textStr = "Stock  Qty                           Part  Description";
        JLabel textTitle = GUI.text(textStr, 600, 50, 20, Color.BLACK, "left", true);
        textTitle.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
        textTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        textTitleBox.add(textTitle);
        GUI.setDimension(textTitleBox, 1010, 30);
        //textTitle.setBorder(new LineBorder(Color.RED));
        //textTitleBox.setBorder(new LineBorder(Color.BLUE));
        panel.add(textTitleBox);

        // create text pane
        Box textBox = Box.createHorizontalBox();
        textBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        textBox.setAlignmentY(Component.TOP_ALIGNMENT);
        textBox.add(Box.createRigidArea(new Dimension(20,0)));
        stockText = new JTextArea(10, 60);  // 10 rows and 60 columns
        stockText.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        stockText.setEditable(false); 
        scroll = new JScrollPane(stockText);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        // fill in with subcomponents and set button status
        Boolean canBundle = updateSkuChildren(sku);
        bundleButton.setEnabled(canBundle);
        if (canBundle) {
            bundleButton.setBackground(visualRoboticsGreen);
        } else {
            bundleButton.setBackground(Color.GRAY);
        }
        textBox.add(scroll);
        textBox.add(Box.createRigidArea(new Dimension(20,0)));
        panel.add(textBox);

        // create listener for sku selection
        ActionListener skuListListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox<String> source = (JComboBox<String>) e.getSource();       
                System.out.println("Bundle: updating skuList");
                String sku = (String) source.getSelectedItem();
                System.out.println("sku = " + sku);
                Part part = Database.getSkuData(sku);
                skuDescription.setText(part.description);
                skuStock.setText(Integer.toString(part.stock));
                Boolean canBundle = updateSkuChildren(sku);
                bundleButton.setEnabled(canBundle);
                if (canBundle) {
                    bundleButton.setBackground(visualRoboticsGreen);
                } else {
                    bundleButton.setBackground(Color.GRAY);
                }
            }
        };
        skuList.addActionListener(skuListListener);

        return panel;      
    }

    public static Boolean updateSkuChildren(String sku) {
        // fill in list with children of sku
        List<Part> allSkuList = Database.getAllSkuChildrenData(sku);
        stockText.setText("");
        Boolean canBundle = true;
        for( Part part1 : allSkuList) {
            String newPart = String.format("%5d %4d %30s  %s\n", part1.stock, part1.quantity, part1.sku, part1.description);
            stockText.append(newPart);
            if (part1.stock < part1.quantity) {
                canBundle = false;
            }
        }
        stockText.revalidate();
        stockText.repaint();
        System.out.println("updated stockText:" + stockText.getText());
        return canBundle;
    }

    public static void main(String[] args) throws Exception {
        JFrame frame = new JFrame("MRP");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(1000, 720));
        frame.setMaximumSize(new Dimension(1000, 720));
        JPanel homePanel = makeGUI();
        frame.add(homePanel);
        frame.pack();   
        frame.setVisible(true); 
    }
}
