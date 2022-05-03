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
    contentPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    contentPanel.setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    c.insets = new Insets(2, 2, 2, 2);
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
    defaultDirectoryText.setPreferredSize(new Dimension(300, 20));
    contentPanel.add(defaultDirectoryText, c);
    c.gridx++;
    c.weightx = 0;
    JButton browseButton = new JButton("Browse...", IconLibrary.getIcon("folder_explore"));
    browseButton.setMargin(new Insets(3, 3, 3, 3));
    browseButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        directoryChooser
            .setCurrentDirectory(new File(SpaceNetSettings.getInstance().getDefaultDirectory()));
        int returnVal = directoryChooser.showOpenDialog(SpaceNetFrame.getInstance());
        if (returnVal == JFileChooser.APPROVE_OPTION) {
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
    SpaceNetSettings.getInstance()
        .setDefaultDirectory(defaultDirectoryText.getText().equals("")
            || defaultDirectoryText.getText().equals(System.getProperty("user.dir")) ? null
                : defaultDirectoryText.getText());
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
