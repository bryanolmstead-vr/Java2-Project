package com.bushnell;

// Java imports

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.CardLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.bushnell.UpdateStock;

// Class to get images from the resources directory
class GetImage {
    public JLabel getImage(String filename, int w, int h) {
        return new JLabel(new ImageIcon(new ImageIcon(getClass().getResource("/resources/"+filename)).getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH)));
    }
}

public class Home {
    public JPanel makeGUI() {
        // create panel
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(1280, 720));
        panel.setMaximumSize(new Dimension(1280, 720));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setAlignmentY(Component.TOP_ALIGNMENT);

        // set background color
        Color black = Color.decode("#000000");
        panel.setBackground(black); 

        // create homeBox that has 2 side by side sections: menuBox and subMenuBox
        Box homeBox = Box.createHorizontalBox();
        homeBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        homeBox.setAlignmentY(Component.TOP_ALIGNMENT);

        // make menuBox 200x700 pixels
        Box menuBox = Box.createVerticalBox();
        menuBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        menuBox.setAlignmentY(Component.TOP_ALIGNMENT);
        menuBox.setPreferredSize(new Dimension(200, 700));
        menuBox.setMaximumSize(new Dimension(200, 700));

        // let subMenuBox take up the rest of the space
        Box subMenuBox = Box.createHorizontalBox();
        subMenuBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        subMenuBox.setAlignmentY(Component.TOP_ALIGNMENT);

        // let the menuBox be 10 pixels in from the left side
        // add 10 pixels between the menuBox and subMenuBox
        // have 10 pixel spacing from right side
        // have homeBox be 10 pixels down from the top
        // have 10 pixel spacing from the bottom
        homeBox.add(Box.createRigidArea(new Dimension(10,0)));
        homeBox.add(menuBox);
        homeBox.add(Box.createRigidArea(new Dimension(10,0)));
        homeBox.add(subMenuBox);
        homeBox.add(Box.createRigidArea(new Dimension(10,0)));
        panel.add(Box.createRigidArea(new Dimension(0,10)));
        panel.add(homeBox);
        panel.add(Box.createRigidArea(new Dimension(0,10)));
        
        // fill in menuBox with Visual Robotics logo
        // below that "MRP System" spaced down by 10 pixels
        // spacing of 50 pixels
        // below that have 4 buttons
        // spacing between buttons is 20 pixels

        // place Visual Robotics image as a JLabel
        JLabel logo = new GetImage().getImage("VisualRoboticsLogo.png", 180, 51);
        logo.setOpaque(false); 
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);
        menuBox.add(logo);
        menuBox.add(Box.createRigidArea(new Dimension(0,10)));
        Color visualRoboticsGreen = Color.decode("#00af74");

        
        // add title "MRP System"
        menuBox.add(GUI.text("MRP System", 200, 30, 20, Color.WHITE, "left", true));  
        menuBox.add(Box.createRigidArea(new Dimension(0,50)));

        // create 4 buttons
        Box buttonBox = Box.createVerticalBox();
        buttonBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        JButton updateStockButton    = GUI.button("Update Stock",    200, 50, 20);
        JButton stockReportButton    = GUI.button("Stock Report",    200, 50, 20);
        JButton bundleButton         = GUI.button("Bundle",          200, 50, 20);
        JButton demandAnalysisButton = GUI.button("Demand Analysis", 200, 50, 20);

        // set colors for buttons
        updateStockButton.setBackground(visualRoboticsGreen);
        stockReportButton.setBackground(visualRoboticsGreen);
        bundleButton.setBackground(visualRoboticsGreen);
        demandAnalysisButton.setBackground(visualRoboticsGreen);
        updateStockButton.setForeground(Color.WHITE);
        stockReportButton.setForeground(Color.WHITE);
        bundleButton.setForeground(Color.WHITE);
        demandAnalysisButton.setForeground(Color.WHITE);

        // add the buttons
        buttonBox.add(updateStockButton);
        buttonBox.add(Box.createRigidArea(new Dimension(0,20)));
        buttonBox.add(stockReportButton);
        buttonBox.add(Box.createRigidArea(new Dimension(0,20)));
        buttonBox.add(bundleButton);
        buttonBox.add(Box.createRigidArea(new Dimension(0,20)));
        buttonBox.add(demandAnalysisButton);
        menuBox.add(buttonBox);

        // create panels for each sub-menu
        JPanel updateStockPanel    = UpdateStock.makeGUI();
        JPanel stockReportPanel    = new JPanel();
        JPanel bundlePanel         = new JPanel();
        JPanel demandAnalysisPanel = new JPanel();

        // add text on each JPanel 
        stockReportPanel.add(GUI.text("Stock Report",       200, 30, 20, Color.BLACK, "center", true));  
        bundlePanel.add(GUI.text("Bundle",                  200, 30, 20, Color.BLACK, "center", true));  
        demandAnalysisPanel.add(GUI.text("Demand Analysis", 200, 30, 20, Color.BLACK, "center", true));
        stockReportPanel.setBackground(Color.GRAY);
        bundlePanel.setBackground(Color.GRAY);
        demandAnalysisPanel.setBackground(Color.GRAY);   

        // create a card panel (only one panel visible at a time)
        JPanel cardPanel = new JPanel(new CardLayout());
        cardPanel.add(updateStockPanel,    "Update Stock");
        cardPanel.add(stockReportPanel,    "Stock Report");
        cardPanel.add(bundlePanel,         "Bundle");
        cardPanel.add(demandAnalysisPanel, "Demand Analysis");
        subMenuBox.add(cardPanel);

        CardLayout cardLayout = (CardLayout) cardPanel.getLayout();
        cardLayout.show(cardPanel,"Update Stock");

        // button listeners
        updateStockButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cardLayout = (CardLayout) cardPanel.getLayout();
                cardLayout.show(cardPanel,"Update Stock");
            }
        });

        stockReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cardLayout = (CardLayout) cardPanel.getLayout();
                cardLayout.show(cardPanel,"Stock Report");
            }
        });

        bundleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cardLayout = (CardLayout) cardPanel.getLayout();
                cardLayout.show(cardPanel,"Bundle");
            }
        });

        demandAnalysisButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cardLayout = (CardLayout) cardPanel.getLayout();
                cardLayout.show(cardPanel,"Demand Analysis");
            }
        });

        panel.setVisible(true);   
        return panel;    
    }
}

