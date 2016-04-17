package com.googlecode.jmeter.plugins.webdriver.config.gui;

import java.awt.Component;

import javax.swing.JOptionPane;

public class MessageDialog {	
	public void show(Component component, String message, String title, int messageType){
		JOptionPane.showMessageDialog(component, message, title, messageType);
	}
}
