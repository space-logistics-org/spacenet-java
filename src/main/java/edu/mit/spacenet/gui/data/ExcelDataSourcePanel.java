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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;

import edu.mit.spacenet.data.Spreadsheet_2_5;
import edu.mit.spacenet.gui.SpaceNetSettings;
import edu.mit.spacenet.io.XLSFileFilter;

/**
 * A data source panel for excel spreadsheet data sources.
 * 
 * @author Paul Grogan
 */
public class ExcelDataSourcePanel extends AbstractDataSourcePanel {
	private static final long serialVersionUID = 2234336773732183925L;
	
	private Spreadsheet_2_5 dataSource;
	private JTextField filePathText;
	
	/**
	 * Instantiates a new excel data source panel.
	 * 
	 * @param dialog the dialog
	 * @param dataSource the data source
	 */
	protected ExcelDataSourcePanel(DataSourceDialog dialog, Spreadsheet_2_5 dataSource) {
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
		c.weightx = 0;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.LINE_END;
		add(new JLabel("File Path: "), c);
		
		c.gridx++;
		c.weightx = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.LINE_START;
		filePathText = new JTextField(30);
		filePathText.setEnabled(false);
		filePathText.setText(dataSource.getFilePath());
		add(filePathText, c);
		
		c.gridx++;
		c.weightx = 0;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.LINE_START;
		JButton browseButton = new JButton("Browse...", new ImageIcon(getClass().getClassLoader().getResource("icons/folder_explore.png")));
		browseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser(SpaceNetSettings.getInstance().getDefaultDirectory());
				fileChooser.setFileFilter(new XLSFileFilter());
				int returnVal = fileChooser.showOpenDialog(getDialog());
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					filePathText.setText(fileChooser.getSelectedFile().getAbsolutePath());
					loadAndUpdateView();
				}
			}
		});
		add(browseButton, c);
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.gui.data.AbstractDataSourcePanel#saveData()
	 */
	@Override
	public void saveData() {
		dataSource.setFilePath(filePathText.getText());
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.gui.data.AbstractDataSourcePanel#getDataSource()
	 */
	@Override
	public Spreadsheet_2_5 getDataSource() {
		return dataSource;
	}
	

	/* (non-Javadoc)
	 * @see edu.mit.spacenet.gui.data.AbstractDataSourcePanel#canLoad()
	 */
	@Override
	public boolean canLoad() {
		return filePathText.getText().length()>0;
	}
}
