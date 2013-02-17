/*!
 * AtlantBH Custom Jmeter Components v1.0.0
 * http://www.atlantbh.com/jmeter-components/
 *
 * Copyright 2011, AtlantBH
 *
 * Licensed under the under the Apache License, Version 2.0.
 */

package com.atlantbh.jmeter.plugins.hbasecomponents.config.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.apache.jmeter.config.gui.AbstractConfigGui;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jorphan.gui.JLabeledTextField;

import com.atlantbh.jmeter.plugins.hbasecomponents.config.HBaseConnection;

/**
 * GUI for {@link HBaseConnection}
 *
 */
public class HBaseConnectionGui  extends AbstractConfigGui {

	private static final long serialVersionUID = -2817355736341379758L;
	
	private JLabeledTextField zkHostTF;
	private JLabeledTextField connNameTF;
	
	public HBaseConnectionGui() {
		super();
		init();
	}
	
	private void init() {
		setBorder(makeBorder());
        setLayout(new BorderLayout(0, 10));
	
        JPanel vertPanel = new VerticalPanel();
        vertPanel.add(makeTitlePanel());		
        add(vertPanel, BorderLayout.NORTH);
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		
		mainPanel.add(connNameTF = createTF("Connection name"));
		mainPanel.add(zkHostTF = createTF("ZK host"));

		add(mainPanel, BorderLayout.CENTER);
	}
	
	
	public String getStaticLabel() {
		return "HBase Connection";	
	}	

	@Override
	public String getLabelResource() {
		return "hbase_connection";
	}
	
	@Override
	public TestElement createTestElement() {
		HBaseConnection conn = new HBaseConnection();
		modifyTestElement(conn);
		return conn;
	}


	@Override
	public void modifyTestElement(TestElement te) {
		super.configureTestElement(te);
		
		HBaseConnection con = (HBaseConnection)te;

		con.setZkName(connNameTF.getText());
		con.setZkHost(zkHostTF.getText());
		con.setName(getName());
		con.setComment(getComment());

		/*con.setConnection(
				con.getPropertyAsString("ZK_NAME"),	
				new HBaseConnectionVariable(
						con.getPropertyAsString("ZK_HOST"), 
						con.getPropertyAsString("ZK_NAME")
				)
		);*/
		
	}

    //TODO typeCB
	public void configure(TestElement el) {
        super.configure(el);
        HBaseConnection con = (HBaseConnection) el;

        connNameTF.setText(con.getZkName());
        zkHostTF.setText(con.getZkHost());
        setName(con.getName());
        setComment(con.getComment());
        /*
    	con.setConnection(
    			
				con.getPropertyAsString("ZK_NAME"),	
				
				new HBaseConnectionVariable(
						con.getPropertyAsString("ZK_HOST"), 
						con.getPropertyAsString("ZK_NAME")
				)
		); */       
    }		
	
    //TODO typeCB
    public void clearGui() {
        super.clearGui();
        connNameTF.setText("");
        zkHostTF.setText("");
    }  	
	
	private JLabeledTextField createTF(String name) {
		JLabeledTextField tf = new JLabeledTextField(name);
		tf.setMaximumSize(new Dimension(10000, 26));
		tf.setBorder(new EmptyBorder(3, 0, 3, 0));
		tf.getComponents()[0].setPreferredSize(new Dimension(150, tf.getComponents()[0].getPreferredSize().height));
		return tf;
	}

}
