package com.bushnell;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Font;
//import java.awt.event.FocusEvent;
//import java.awt.event.FocusListener;

import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.Box;
import javax.swing.BoxLayout;
//import javax.swing.JButton;
//import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
//import javax.swing.plaf.metal.MetalCheckBoxIcon;

import com.bushnell.GUI;
import com.bushnell.Database;
import com.bushnell.Part;

public class UpdateStock {

    public static JLabel skuDescription;
    public static JLabel skuPrice;
    public static JLabel skuStock;

    public static JPanel makeGUI() {

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
        titleBox.add(GUI.text("Update Stock", 600, 30, 20, Color.BLACK, "center", true));
        GUI.setDimension(titleBox, 600, 100);
        panel.add(titleBox);

        // create sku entry
        Box skuBox = Box.createHorizontalBox();
        skuBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        skuBox.setAlignmentY(Component.TOP_ALIGNMENT);
        skuBox.add(GUI.text("sku", 150, 30, 20, Color.BLACK, "right", true));
        skuBox.add(Box.createRigidArea(new Dimension(50,0)));
        String[] skuArray = Database.getSkuList();
        JComboBox<String> skuList = new JComboBox<>(skuArray);
        GUI.setDimension(skuList, 350, 40);
        skuList.setFont(new Font("Sans-Serif", Font.BOLD, 20));
        skuBox.add(skuList);
        GUI.setDimension(skuBox, 600, 50);
        panel.add(skuBox);

        // create listener for sku selection
        ActionListener skuListListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String sku = (String) skuList.getSelectedItem();
                //System.out.println("User selected sku = " + sku);
                Part part = Database.getSkuData(sku);
                skuDescription.setText(part.description);
                skuPrice.setText(String.format("%.2f", part.price));
                skuStock.setText(Integer.toString(part.stock));
                }
        };
        skuList.addActionListener(skuListListener);


        // look up initial sku
        String sku = (String) skuList.getSelectedItem();
        //System.out.println("Initial sku = " + sku);
        Part part = Database.getSkuData(sku); 

        // create description
        Box descriptionBox = Box.createHorizontalBox();
        descriptionBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        descriptionBox.setAlignmentY(Component.TOP_ALIGNMENT);
        descriptionBox.add(GUI.text("description", 150, 30, 20, Color.BLACK, "right", true));
        descriptionBox.add(Box.createRigidArea(new Dimension(50,0)));
        skuDescription = GUI.text(part.description, 300, 30, 20, Color.BLACK, "left", false);
        descriptionBox.add(skuDescription);
        GUI.setDimension(descriptionBox, 600, 50);
        panel.add(descriptionBox);

        // create price
        Box priceBox = Box.createHorizontalBox();
        priceBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        priceBox.setAlignmentY(Component.TOP_ALIGNMENT);
        priceBox.add(GUI.text("price", 150, 30, 20, Color.BLACK, "right", true));
        priceBox.add(Box.createRigidArea(new Dimension(50,0)));
        priceBox.add(GUI.text("$", 20, 30, 20, Color.BLACK, "left", true));
        skuPrice = GUI.text(String.format("%.2f", part.price), 200, 30, 20, Color.BLACK, "left", false);
        priceBox.add(skuPrice);
        GUI.setDimension(priceBox, 600, 50);
        panel.add(priceBox);

        // create stock
        Box stockBox = Box.createHorizontalBox();
        stockBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        stockBox.setAlignmentY(Component.TOP_ALIGNMENT);
        stockBox.add(GUI.text("stock", 150, 30, 20, Color.BLACK, "right", true));
        stockBox.add(Box.createRigidArea(new Dimension(50,0)));
        skuStock = GUI.text(Integer.toString(part.stock), 200, 30, 20, Color.BLACK, "left", false);
        stockBox.add(skuStock);
        GUI.setDimension(stockBox, 600, 50);
        panel.add(stockBox);

        Database.checkConnection();
        Database.getSkuList();

        return panel;      
    }
}
