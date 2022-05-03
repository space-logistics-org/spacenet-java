/*
 * Copyright 2010 MIT Strategic Engineering Research Group
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
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
    c.insets = new Insets(2, 2, 2, 2);
    c.gridx = 0;
    c.gridy = 0;
    c.weightx = 0;
    c.fill = GridBagConstraints.NONE;
    c.anchor = GridBagConstraints.LINE_END;
    add(new JLabel("Host: "), c);
    c.gridx += 2;
    add(new JLabel("Port: "), c);
    c.gridy++;
    c.gridx = 0;
    add(new JLabel("User: "), c);
    c.gridx += 2;
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
    c.gridx += 2;
    portModel = new SpinnerNumberModel(dataSource.getPort().intValue(), 1, 10000, 1);
    portSpinner = new JSpinner(portModel);
    portSpinner.setPreferredSize(new Dimension(100, 20));
    add(portSpinner, c);
    c.gridy++;
    c.gridx = 1;
    userText = new JTextField(15);
    userText.setText(dataSource.getUser());
    add(userText, c);
    c.gridx += 2;
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
    testButton = new JButton("Test",
        new ImageIcon(getClass().getClassLoader().getResource("icons/database_go.png")));
    testButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        saveData();
        try {
          dataSource.createConnection();
          dataSource.loadLibraries();
          JOptionPane.showMessageDialog(getDialog(), "Success!", "Test Connection",
              JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
          JOptionPane.showMessageDialog(getDialog(), "Failure", "Test Connection",
              JOptionPane.ERROR_MESSAGE);
        }
      }
    });
    add(testButton, c);
  }

  /*
   * (non-Javadoc)
   * 
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

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.gui.data.AbstractDataSourcePanel#getDataSource()
   */
  @Override
  public Database getDataSource() {
    return dataSource;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.gui.data.AbstractDataSourcePanel#canLoad()
   */
  @Override
  public boolean canLoad() {
    return hostText.getText().length() > 0 && databaseText.getText().length() > 0
        && userText.getText().length() > 0;
  }
}
