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