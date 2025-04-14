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

public class Bundle {

    public static JLabel skuDescription;
    public static JLabel skuStock;
    public static JComboBox<String> skuList;

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
        titleBox.add(GUI.text("Bundle", 600, 30, 20, Color.BLACK, "center", true));
        GUI.setDimension(titleBox, 600, 100);
        panel.add(titleBox);

        // create sku entry
        Box skuBox = Box.createHorizontalBox();
        skuBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        skuBox.setAlignmentY(Component.TOP_ALIGNMENT);
        skuBox.add(GUI.text("sku", 150, 30, 20, Color.BLACK, "right", true));
        skuBox.add(Box.createRigidArea(new Dimension(50,0)));
        String[] skuArray = Database.getSkuList("SUB%");
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

        // create stock
        Box stockBox = Box.createHorizontalBox();
        stockBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        stockBox.setAlignmentY(Component.TOP_ALIGNMENT);
        stockBox.add(GUI.text("stock", 150, 30, 20, Color.BLACK, "right", true));
        stockBox.add(Box.createRigidArea(new Dimension(50,0)));
        skuStock = GUI.text(Integer.toString(part.stock), 50, 30, 20, Color.BLACK, "left", false);
        stockBox.add(skuStock);
        GUI.setDimension(stockBox, 600, 50);
        panel.add(stockBox);

        return panel;      
    }
}
