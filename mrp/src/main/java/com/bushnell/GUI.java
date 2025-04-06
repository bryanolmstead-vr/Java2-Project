package com.bushnell;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class GUI {

    // set dimensions of a component
    public static void setDimension(Component component, int w, int h) {
		component.setMinimumSize(new Dimension(w, h));
		component.setPreferredSize(new Dimension(w, h));
		component.setMaximumSize(new Dimension(w, h));
	}

    // create a text field with defined alignment
    public static JLabel text(String textStr, int width, int height, int fontSize, Color color, String alignment, boolean bold) {
        JLabel label = new JLabel(textStr);
        setDimension(label, width, height);
        if (bold)
            label.setFont(new Font("Sans-Serif", Font.BOLD, fontSize));
        else
            label.setFont(new Font("Sans-Serif", Font.PLAIN, fontSize)); 
        label.setForeground(color);
        if (alignment == "left")
            label.setHorizontalAlignment(SwingConstants.LEFT);
        else if (alignment == "right")
            label.setHorizontalAlignment(SwingConstants.RIGHT);
        else
            label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        return label;
    }

    // create a centered button
    public static JButton button(String textStr, int width, int height, int fontSize) {
        JButton button = new JButton(textStr);      
        setDimension(button, width, height);            
        button.setFont(new Font("Sans-Serif", Font.BOLD, fontSize));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        return button;
    }

    // create an editable text field
    public static JTextField textField(int numChars, int width, int height, int fontSize) {
        JTextField textField = new JTextField(numChars);
        setDimension(textField, width, height);
        textField.setFont(new Font("Sans-Serif", Font.BOLD, 20));
        return textField;
    }
}

