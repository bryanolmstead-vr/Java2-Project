package com.bushnell;

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

//import org.w3c.dom.events.MouseEvent;

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
    public static JTextField skuPrice;
    public static JTextField skuStock;
    public static JComboBox<String> skuList;
    public static JPanel panel = new JPanel();

    public static JPanel makeGUI() {

        // create panel
        panel.removeAll();
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
        String[] skuArray = Database.getSkuList("%");
        skuList = new JComboBox<String>(skuArray);
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
                Part part = Database.getSkuData(sku);
                skuDescription.setText(part.description);
                skuPrice.setText(String.format("%.2f", part.price));
                skuStock.setText(Integer.toString(part.stock));
                }
        };
        skuList.addActionListener(skuListListener);


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
        panel.add(descriptionBox);

        // create price
        Box priceBox = Box.createHorizontalBox();
        priceBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        priceBox.setAlignmentY(Component.TOP_ALIGNMENT);
        priceBox.add(GUI.text("price", 150, 30, 20, Color.BLACK, "right", true));
        priceBox.add(Box.createRigidArea(new Dimension(50,0)));
        priceBox.add(GUI.text("$", 20, 30, 20, Color.BLACK, "left", true));
        skuPrice = GUI.textField(6, 200, 30, 20);
        skuPrice.setText(String.format("%.2f", part.price));
        priceBox.add(skuPrice);
        GUI.setDimension(priceBox, 600, 50);
        panel.add(priceBox);

        // create stock
        Box stockBox = Box.createHorizontalBox();
        stockBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        stockBox.setAlignmentY(Component.TOP_ALIGNMENT);
        stockBox.add(GUI.text("stock", 150, 30, 20, Color.BLACK, "right", true));
        stockBox.add(Box.createRigidArea(new Dimension(50,0)));
        skuStock = GUI.textField(5, 200, 30, 20);
        skuStock.setText(Integer.toString(part.stock));
        stockBox.add(skuStock);
        GUI.setDimension(stockBox, 600, 50);
        panel.add(stockBox);

        // create price listener
        skuPrice.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseExited(MouseEvent arg0) {
                    updateSku();
                }
            });
        skuPrice.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(ActionEvent event) {
                    updateSku();
                }
            });

        // create stock listener
        skuStock.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent arg0) {
                updateSku();
            }
        });
        skuStock.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                updateSku();
            }
        });

        return panel;      
    }

    public static void updateSku(){
        String sku = (String)skuList.getSelectedItem();
        Part oldPart = Database.getSkuData(sku);
        Part part = new Part();
        part.sku = sku;
        part.description = oldPart.description; 
        part.price = Double.parseDouble(skuPrice.getText());
        part.stock = Integer.parseInt(skuStock.getText());
        //System.out.println("Updating sku=" + part.sku + " description=" + part.description +
        //    " price=" + Double.toString(part.price) + " stock=" + Integer.toString(part.stock));
        boolean success = Database.updateSku(part);
        if (!success)  
            System.out.println("database update unsuccessful");
    }
}
