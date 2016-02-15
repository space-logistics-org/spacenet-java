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
package edu.mit.spacenet.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.mit.spacenet.util.IconLibrary;

/**
 * A dialog to edit the global SpaceNet preferences.
 * 
 * @author Paul Grogan
 */
public class SpaceNetSettingsDialog extends JDialog {
	private static final long serialVersionUID = -802497885876127342L;
	
	private JFileChooser directoryChooser;
	private JCheckBox autoRefreshCheck;
	private JTextField defaultDirectoryText;
	
	/**
	 * The constructor.
	 * 
	 * @param spaceNetFrame the parent SpaceNet frame
	 */
	public SpaceNetSettingsDialog() {
		super(SpaceNetFrame.getInstance(), "SpaceNet Settings", true);
		directoryChooser = new JFileChooser();
		directoryChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		buildDialog();
		pack();
		setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
	}
	
	/**
	 * Builds the dialog components.
	 */
	private void buildDialog() {
		JPanel contentPanel = new JPanel();
		contentPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		contentPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(2,2,2,2);
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0;
		c.weighty = 0;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.LINE_END;
		contentPanel.add(new JLabel("Default directory: "), c);
		c.gridx++;
		c.weightx = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.LINE_START;
		defaultDirectoryText = new JTextField();
		defaultDirectoryText.setEnabled(false);
		defaultDirectoryText.setPreferredSize(new Dimension(300,20));
		contentPanel.add(defaultDirectoryText, c);
		c.gridx++;
		c.weightx = 0;
		JButton browseButton = new JButton("Browse...", IconLibrary.getIcon("folder_explore"));
		browseButton.setMargin(new Insets(3,3,3,3));
		browseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				directoryChooser.setCurrentDirectory(new File(SpaceNetSettings.getInstance().getDefaultDirectory()));
				int returnVal = directoryChooser.showOpenDialog(SpaceNetFrame.getInstance());
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					defaultDirectoryText.setText(directoryChooser.getSelectedFile().getAbsolutePath());
				}
			}
		});
		contentPanel.add(browseButton, c);
		c.gridy++;
		c.gridx = 0;
		c.gridwidth = 3;
		autoRefreshCheck = new JCheckBox("Auto-refresh charts (lags with large scenarios)");
		contentPanel.add(autoRefreshCheck, c);
		
		c.gridy++;
		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.NONE;
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 0));
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				savePreferences();
				setVisible(false);
			}
		});
		buttonPanel.add(okButton);
		getRootPane().setDefaultButton(okButton);
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		buttonPanel.add(cancelButton);
		contentPanel.add(buttonPanel, c);
		setContentPane(contentPanel);
	}
	
	/**
	 * Initializes the dialog.
	 */
	private void initialize() {
		defaultDirectoryText.setText(SpaceNetSettings.getInstance().getDefaultDirectory());
		autoRefreshCheck.setSelected(SpaceNetSettings.getInstance().isAutoRefresh());
	}
	
	/**
	 * Save preferences.
	 */
	private void savePreferences() {
		SpaceNetSettings.getInstance().setAutoRefresh(autoRefreshCheck.isSelected());
		SpaceNetSettings.getInstance().setDefaultDirectory(defaultDirectoryText.getText().equals("")||defaultDirectoryText.getText().equals(System.getProperty("user.dir"))?null:defaultDirectoryText.getText());
	}

	/**
	 * Show dialog.
	 */
	public void showDialog() {
		initialize();
		pack();
		setLocationRelativeTo(getParent());
		setVisible(true);
	}
}
