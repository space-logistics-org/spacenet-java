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
package edu.mit.spacenet.gui.component;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JList;

import edu.mit.spacenet.domain.I_Container;
import edu.mit.spacenet.domain.element.I_Carrier;
import edu.mit.spacenet.domain.network.edge.Edge;
import edu.mit.spacenet.domain.network.node.Node;

/**
 * This class extends a combo box to provide icons for objects that implement the Container
 * interface.
 * 
 * @author Paul Grogan
 */
public class ContainerComboBox<E extends I_Container> extends JComboBox<E> {
  private static final long serialVersionUID = -4738778912721458792L;
  private ImageIcon transparentIcon =
      new ImageIcon(getClass().getClassLoader().getResource("icons/transparent_icon.png"));

  /**
   * Instantiates a new container combo box.
   */
  public ContainerComboBox() {
    setRenderer(new DefaultListCellRenderer() {
      private static final long serialVersionUID = -2255885956722142642L;

      public Component getListCellRendererComponent(JList<?> list, Object value, int index,
          boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof I_Carrier) {
          setIcon(((I_Carrier) value).getIcon());
        } else if (value instanceof Node) {
          setIcon(((Node) value).getNodeType().getIcon());
        } else if (value instanceof Edge) {
          setIcon(((Edge) value).getEdgeType().getIcon());
        } else if (value == null) {
          setIcon(transparentIcon);
        }
        return this;
      }
    });
  }
}
