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
package edu.mit.spacenet.gui.manifest;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;

import edu.mit.spacenet.domain.element.ElementType;
import edu.mit.spacenet.domain.element.ResourceContainerFactory;
import edu.mit.spacenet.gui.component.DropDownButton;

/**
 * A custom drop down button that allows the addition of new containers to the manifest tab.
 */
public class AddContainerButton extends DropDownButton {
  private static final long serialVersionUID = 259141624873390794L;

  private ManifestTab manifestTab;

  /**
   * Instantiates a new adds the container button.
   * 
   * @param text the text
   * @param icon the icon
   */
  public AddContainerButton(ManifestTab manifestTab) {
    super("Add", new ImageIcon(
        manifestTab.getClass().getClassLoader().getResource("icons/package_add.png")));
    this.manifestTab = manifestTab;
    buildMenu();
  }

  /**
   * Builds the menu.
   */
  private void buildMenu() {
    JMenuItem addCtb = new JMenuItem("Add Crew Transfer Bag (CTB)",
        ElementType.RESOURCE_CONTAINER.getIconType().getIcon());
    addCtb.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        manifestTab.getManifest().addContainer(ResourceContainerFactory.createCTB());
        manifestTab.updateView();
      }
    });
    getMenu().add(addCtb);
    JMenuItem addHalfCtb = new JMenuItem("Add Half Crew Transfer Bag (CTB)",
        ElementType.RESOURCE_CONTAINER.getIconType().getIcon());
    addHalfCtb.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        manifestTab.getManifest().addContainer(ResourceContainerFactory.createHCTB());
        manifestTab.updateView();
      }
    });
    getMenu().add(addHalfCtb);
    JMenuItem addDoubleCtb = new JMenuItem("Add Double Crew Transfer Bag (CTB)",
        ElementType.RESOURCE_CONTAINER.getIconType().getIcon());
    addDoubleCtb.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        manifestTab.getManifest().addContainer(ResourceContainerFactory.createDCTB());
        manifestTab.updateView();
      }
    });
    getMenu().add(addDoubleCtb);
    JMenuItem addLt =
        new JMenuItem("Add Liquid Tank", ElementType.RESOURCE_CONTAINER.getIconType().getIcon());
    addLt.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        manifestTab.getManifest().addContainer(ResourceContainerFactory.createLT());
        manifestTab.updateView();
      }
    });
    getMenu().add(addLt);
    JMenuItem addLtd = new JMenuItem("Add Liquid Tank Derivative",
        ElementType.RESOURCE_CONTAINER.getIconType().getIcon());
    addLtd.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        manifestTab.getManifest().addContainer(ResourceContainerFactory.createLTD());
        manifestTab.updateView();
      }
    });
    getMenu().add(addLtd);
    JMenuItem addGt =
        new JMenuItem("Add Gas Tank", ElementType.RESOURCE_CONTAINER.getIconType().getIcon());
    addGt.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        manifestTab.getManifest().addContainer(ResourceContainerFactory.createGT());
        manifestTab.updateView();
      }
    });
    getMenu().add(addGt);
    JMenuItem addGtd = new JMenuItem("Add Gas Tank Derivative",
        ElementType.RESOURCE_CONTAINER.getIconType().getIcon());
    addGtd.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        manifestTab.getManifest().addContainer(ResourceContainerFactory.createGTD());
        manifestTab.updateView();
      }
    });
    getMenu().add(addGtd);
    JMenuItem addShoss =
        new JMenuItem("Add Shoss Box", ElementType.RESOURCE_CONTAINER.getIconType().getIcon());
    addShoss.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        manifestTab.getManifest().addContainer(ResourceContainerFactory.createShoss());
        manifestTab.updateView();
      }
    });
    getMenu().add(addShoss);
    JMenuItem addPressShoss = new JMenuItem("Add Pressurized Shoss Box",
        ElementType.RESOURCE_CONTAINER.getIconType().getIcon());
    addPressShoss.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        manifestTab.getManifest().addContainer(ResourceContainerFactory.createPressShoss());
        manifestTab.updateView();
      }
    });
    getMenu().add(addPressShoss);
  }
}
