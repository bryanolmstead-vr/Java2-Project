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
import java.util.List;

//import org.w3c.dom.events.MouseEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
//import javax.swing.JButton;
//import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
//import javax.swing.plaf.metal.MetalCheckBoxIcon;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

import com.bushnell.GUI;
import com.bushnell.Database;
import com.bushnell.Part;

public class StockReport {

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
        titleBox.add(GUI.text("Stock Report", 600, 30, 20, Color.BLACK, "center", true));
        GUI.setDimension(titleBox, 600, 100);
        panel.add(titleBox);

        // create text title for sku list
        Box textTitleBox = Box.createHorizontalBox();
        textTitleBox.add(Box.createRigidArea(new Dimension(20,0)));
        String textStr = String.format("%35s %8s %5s  %s\n", "SKU", "Price", "Stock", "Description");
        JLabel textTitle = GUI.text(textStr, 500, 30, 14, Color.BLACK, "left", true);
        textTitle.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
        textTitleBox.add(textTitle);
        textTitleBox.add(Box.createRigidArea(new Dimension(20,0)));
        panel.add(textTitleBox);

        // create text pane
        Box textBox = Box.createHorizontalBox();
        textBox.add(Box.createRigidArea(new Dimension(20,0)));
        JTextArea stockText = new JTextArea(30, 60);  // 30 rows and 60 columns
        stockText.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        stockText.setEditable(false); 
        JScrollPane scroll = new JScrollPane(stockText);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        List<Part> allSkuList = Database.getAllSkuData();
        for( Part part : allSkuList) {
            stockText.append(String.format("%35s %8.2f %4d   %s\n", part.sku, part.price, part.stock, part.description));
        }
        textBox.add(scroll);
        textBox.add(Box.createRigidArea(new Dimension(20,0)));
        panel.add(textBox);

        return panel; 
    }
}
