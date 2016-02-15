/*
 * Copyright (c) 2010 MIT Strategic Engineering Research Group
 * 
 * This file is part of SpaceNet 2.5r2.
 * 
 * SpaceNet 2.5r2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * SpaceNet 2.5r2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with SpaceNet 2.5r2.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.mit.spacenet.gui.data;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import edu.mit.spacenet.data.Database;

/**
 * A data source panel for excel spreadsheet data sources.
 * 
 * @author Paul Grogan
 */
public class MySqlDataSourcePanel extends AbstractDataSourcePanel {
	private static final long serialVersionUID = 2234336773732183925L;
	
	private Database dataSource;
	private JTextField hostText, databaseText, userText, passwordText;
	private JSpinner portSpinner;
	private SpinnerNumberModel portModel;
	private JButton testButton;
	
	/**
	 * Instantiates a new my sql data source panel.
	 * 
	 * @param dialog the dialog
	 * @param dataSource the data source
	 */
	protected MySqlDataSourcePanel(DataSourceDialog dialog, Database dataSource) {
		super(dialog, dataSource);
		this.dataSource = dataSource;
		
		buildPanel();
	}
	
	/**
	 * Builds the panel.
	 */
	private void buildPanel() {
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(2,2,2,2);
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.LINE_END;
		add(new JLabel("Host: "), c);
		c.gridx+=2;
		add(new JLabel("Port: "), c);
		c.gridy++;
		c.gridx = 0;
		add(new JLabel("User: "), c);
		c.gridx+=2;
		add(new JLabel("Password: "), c);
		c.gridy++;
		c.gridx = 0;
		add(new JLabel("Database: "), c);
		
		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.LINE_START;
		hostText = new JTextField();
		hostText.setText(dataSource.getHost());
		add(hostText, c);
		c.gridx+=2;
		portModel = new SpinnerNumberModel(dataSource.getPort().intValue(),1,10000,1);
		portSpinner = new JSpinner(portModel);
		portSpinner.setPreferredSize(new Dimension(100,20));
		add(portSpinner, c);
		c.gridy++;
		c.gridx = 1;
		userText = new JTextField(15);
		userText.setText(dataSource.getUser());
		add(userText, c);
		c.gridx+=2;
		passwordText = new JPasswordField();
		passwordText.setText(dataSource.getPassword());
		add(passwordText, c);
		c.gridy++;
		c.gridx = 1;
		databaseText = new JTextField();
		databaseText.setText(dataSource.getDatabase());
		add(databaseText, c);
		c.gridx++;
		c.gridwidth = 2;
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.NONE;
		testButton = new JButton("Test", new ImageIcon(getClass().getClassLoader().getResource("icons/database_go.png")));
		testButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveData();
				try {
					dataSource.createConnection();
					dataSource.loadLibraries();
					JOptionPane.showMessageDialog(getDialog(), 
							"Success!", 
							"Test Connection", 
							JOptionPane.INFORMATION_MESSAGE);
				} catch(Exception ex) {
					JOptionPane.showMessageDialog(getDialog(), 
							"Failure", 
							"Test Connection", 
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		add(testButton, c);
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.gui.data.AbstractDataSourcePanel#saveData()
	 */
	@Override
	public void saveData() {
		dataSource.setHost(hostText.getText());
		dataSource.setPort(portModel.getNumber().intValue());
		dataSource.setDatabase(databaseText.getText());
		dataSource.setUser(userText.getText());
		dataSource.setPassword(passwordText.getText());
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.gui.data.AbstractDataSourcePanel#getDataSource()
	 */
	@Override
	public Database getDataSource() {
		return dataSource;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.gui.data.AbstractDataSourcePanel#canLoad()
	 */
	@Override
	public boolean canLoad() {
		return hostText.getText().length()>0 
			&& databaseText.getText().length()>0
			&& userText.getText().length()>0;
	}
}
