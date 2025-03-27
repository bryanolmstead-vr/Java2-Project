package com.bushnell;

// Java imports

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.CardLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

// Class to get images from the resources directory
class GetImage {
    public JLabel getImage(String filename) {
        return new JLabel(new ImageIcon(getClass().getResource("/resources/"+filename)));
    }
}

public class Home {
    public JPanel makeGUI() {
        // create panel
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setPreferredSize(new Dimension(1400, 800));
        panel.setMaximumSize(new Dimension(1400, 800));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setAlignmentY(Component.TOP_ALIGNMENT);

        // set background color
        String bushnellBlueHashCode = "#011E40";
        Color bushnellBlue = Color.decode(bushnellBlueHashCode);
        panel.setBackground(bushnellBlue); 

        // create menu box and submenu box
        Box homeBox = Box.createHorizontalBox();
        homeBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        homeBox.setAlignmentY(Component.TOP_ALIGNMENT);
        Box menuBox = Box.createVerticalBox();
        menuBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        menuBox.setAlignmentY(Component.TOP_ALIGNMENT);

        Box subMenuBox = Box.createHorizontalBox();
        subMenuBox.setPreferredSize(new Dimension(1280, 720));
        subMenuBox.setMaximumSize(new Dimension(1280, 720));
        subMenuBox.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        subMenuBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        subMenuBox.setAlignmentY(Component.TOP_ALIGNMENT);
        homeBox.add(Box.createRigidArea(new Dimension(20,0)));
        homeBox.add(menuBox);
        homeBox.add(Box.createRigidArea(new Dimension(20,0)));
        homeBox.add(subMenuBox);
        homeBox.add(Box.createRigidArea(new Dimension(20,0)));
        panel.add(Box.createRigidArea(new Dimension(0,20)));
        panel.add(homeBox);
        
        // create menu box

        // place an image on the panel as a JLabel
        JLabel logo = new GetImage().getImage("BushnellLogo.png");
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);
        menuBox.add(Box.createRigidArea(new Dimension(0,10)));
        menuBox.add(logo);
        menuBox.add(Box.createRigidArea(new Dimension(0,50)));
        
        // title
        Box titleBox = Box.createVerticalBox();
        titleBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        titleBox.add(GUI.text("Counseling Department", 300, 30, 20, Color.WHITE, "left"));
        titleBox.add(Box.createRigidArea(new Dimension(0,20)));
        titleBox.add(GUI.text("Online Kiosk", 300, 30, 20, Color.WHITE, "left"));
        titleBox.add(Box.createRigidArea(new Dimension(0,50)));
        menuBox.add(titleBox);

        // create 3 buttons
        Box buttonBox = Box.createVerticalBox();
        buttonBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        JButton appointmentButton = GUI.button("Appointment", 300, 50, 20);
        JButton clientInformationButton = GUI.button("Client Information", 300, 50, 20);
        JButton onlineAdviceButton      = GUI.button("Online Advice", 300, 50, 20);
        buttonBox.add(appointmentButton);
        buttonBox.add(Box.createRigidArea(new Dimension(0,20)));
        buttonBox.add(clientInformationButton);
        buttonBox.add(Box.createRigidArea(new Dimension(0,20)));
        buttonBox.add(onlineAdviceButton);
        menuBox.add(buttonBox);

        // create panels for each sub-menu
        JPanel appointmentPanel       = Appointment.makeGUI();
        JPanel clientInformationPanel = ClientInformation.makeGUI();
        JPanel onlineAdvicePanel      = OnlineAdvice.makeGUI();

        // create a card panel (only one panel visible at a time)
        JPanel cardPanel = new JPanel(new CardLayout());
        cardPanel.add(appointmentPanel,"appointment");
        cardPanel.add(clientInformationPanel,"clientInformation");
        cardPanel.add(onlineAdvicePanel,"onlineAdvice");
        subMenuBox.add(cardPanel);

        CardLayout cardLayout = (CardLayout) cardPanel.getLayout();
        cardLayout.show(cardPanel,"appointment");

        // button listeners
        appointmentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cardLayout = (CardLayout) cardPanel.getLayout();
                cardLayout.show(cardPanel,"appointment");
            }
        });

        clientInformationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cardLayout = (CardLayout) cardPanel.getLayout();
                cardLayout.show(cardPanel,"clientInformation");
            }
        });

        onlineAdviceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cardLayout = (CardLayout) cardPanel.getLayout();
                cardLayout.show(cardPanel,"onlineAdvice");
            }
        });

        panel.setVisible(true);   
        return panel;    
    }
}

