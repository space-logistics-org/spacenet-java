/*
 * Copyright 2010 MIT Strategic Engineering Research Group
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.mit.spacenet.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

/**
 * Dialog that displays information about SpaceNet, including the version
 * number, the authors, and any licensing details.
 * 
 * @author Paul Grogan
 */
public class AboutDialog extends JDialog {
	private static final long serialVersionUID = -802497885876127342L;
	
	/**
	 * Initializes a new about dialog.
	 * 
	 * @param spaceNetFrame the parent space net frame
	 */
	public AboutDialog() {
		super(SpaceNetFrame.getInstance(), "About SpaceNet", true);
		setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		buildDialog();
		pack();
	}
	
	/**
	 * Builds the dialog.
	 */
	private void buildDialog() {
		JPanel contentPanel = new JPanel();
		contentPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		contentPanel.setBackground(new Color(153,51,51));
		contentPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(2,2,2,2);
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0;
		c.weighty = 0;
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.HORIZONTAL;
		JLabel mitLogo = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("icons/mit_footer.png")));
		mitLogo.setOpaque(false);
		contentPanel.add(mitLogo, c);
		c.gridy++;
		c.fill = GridBagConstraints.NONE;
		JLabel spaceNetLogo = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("icons/spacenet_splash.png")));
		spaceNetLogo.setPreferredSize(new Dimension(138,119));
		contentPanel.add(spaceNetLogo, c);
		c.gridy++;
		JLabel titleLabel = new JLabel("SpaceNet 2.5r2");
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD).deriveFont(18f));
		contentPanel.add(titleLabel, c);
		c.gridy++;
		JLabel websiteLabel = new JLabel("http://spacenet.mit.edu");
		websiteLabel.setForeground(Color.WHITE);
		contentPanel.add(websiteLabel, c);
		c.gridy++;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		JTextPane textPane = new JTextPane();
		textPane.setText("Copyright (c) 2010 MIT Strategic Engineering Research Group" +
				"\n\nSpaceNet 2.5r2 is released under the GNU General Public License (GPL) Version 3." +
				"\n\nThis project was paritally supported by the Jet Propulsion Laboratory (JPL) " +
				"under the Strategic University Relations Program (SURP) and contract number 1344341." +
				"\n\nResearch Advisor:\tOlivier de Weck" +
				"\n\nJPL Contacts:\t\tGene Lee\n\t\tLiz Jordan" +
				"\n\nLead Developer:\tPaul Grogan" +
				"\n\nContributors:\t\tNii Armar\n\t\tIvo Ferreira\n\t\tAbe Grindle\n\t\tTakuto Ishimatsu\n\t\tBasant Sagar\n\t\tRobert Shishko\n\t\tAfreen Siddiqi" +
				"\n\nPast Developers:\tJaemyung Ahn\n\t\tErica Gralla\n\t\tDiego Klabjan\n\t\tJason Mellein\n\t\tSarah Shull" +
				"\n\nTest Subjects:\t\tTorin Clark\n\t\tBen Corbin\n\t\tJustin Kaderka\n\t\tGreg O'Neill\n\t\tDaniel Selva\n\t\tNarek Shougarian\n\t\tAnthony Wicht\n\t\tHoward Yue" +
				"\n\nIcon Set:\t\tMark James, FamFamFam Silk" +
				"\n\nLook and Feel:\t\tNNL Technology, InfoNode");
		textPane.setEditable(false);
		textPane.setCaretPosition(0);
		JScrollPane textScroll = new JScrollPane(textPane);
		textScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		textScroll.setPreferredSize(new Dimension(300,200));
		contentPanel.add(textScroll, c);
		c.weighty = 0;
		c.gridy++;
		c.fill = GridBagConstraints.NONE;
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		contentPanel.add(okButton, c);
		getRootPane().setDefaultButton(okButton);
		
		setContentPane(contentPanel);
	}
	
	/**
	 * Show dialog.
	 */
	public void showDialog() {
		pack();
		setLocationRelativeTo(getParent());
		setVisible(true);
	}
}
