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
package edu.mit.spacenet.gui.component;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

/**
 * A component that appears to be a button but produces a pop-up menu when
 * clicked.
 */
public class DropDownButton extends Box {
	private static final long serialVersionUID = -3201696540484920791L;
	private JButton button;
    private DropDownMenu menu;

    /**
     * Instantiates a new drop down button.
     * 
     * @param text the text
     * @param icon the icon
     */
    public DropDownButton(String text, Icon icon) {
        super(BoxLayout.X_AXIS);

        menu = new DropDownMenu();
        JMenuBar bar = new JMenuBar();
        bar.add(menu);
        bar.setMaximumSize(new Dimension(0,100));
        bar.setMinimumSize(new Dimension(0,1));
        bar.setPreferredSize(new Dimension(0,1));
        add(bar);

        button = new JButton(text, icon);
        button.addMouseListener(new MouseAdapter() {
        	boolean pressHidPopup = false;
            public void mouseClicked(MouseEvent e) {
                if (!pressHidPopup) menu.doClick(0);
            }
            public void mousePressed(MouseEvent e) {
                menu.dispatchMouseEvent(e);
                if (menu.isPopupMenuVisible())
                    pressHidPopup = false;
                else
                    pressHidPopup = true;

            }
        });
        add(button);
    }

    /**
     * Gets the button.
     * 
     * @return the button
     */
    public JButton getButton() { 
    	return button;
    }
    
    /**
     * Gets the menu.
     * 
     * @return the menu
     */
    public JMenu getMenu() { 
    	return menu;
    }
    private class DropDownMenu extends JMenu {
		private static final long serialVersionUID = 5291067347951781168L;

		public void dispatchMouseEvent(MouseEvent e) {
            processMouseEvent(e);
        }
    }
}