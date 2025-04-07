package com.bushnell;

import java.io.File;
import java.net.URL;

// Java imports
import javax.swing.JFrame;
import javax.swing.JPanel;

// imports from classmates
//import com.bushnell.Home;

public class App {
    public static void main(String[] args) throws Exception {
        JFrame frame = new JFrame("MRP");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Home home = new Home();

        /*
        // this didn't work because my directory has commas and spaces
        // get location of jar file (where PDF file should go)
        String jarPath = App.class
                .getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .getPath();
        File jarFile = new File(jarPath);
        String jarDirectoryPath = jarFile.getParent();
        jarDirectoryPath = jarDirectoryPath.replace(",", "%2C");
        System.out.println("Path to the JAR file: " + jarDirectoryPath);
        */

        // just put PDF files here
        String directory = "c:/users/bryan/Desktop";

        JPanel homePanel = home.makeGUI(directory);
        frame.add(homePanel);
        frame.pack();   
        frame.setVisible(true);   
    }
}
