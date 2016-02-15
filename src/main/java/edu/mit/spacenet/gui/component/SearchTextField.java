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

import java.awt.Color;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JTextField;

/**
 * A subclass of a text field that provides support for a default message
 * prompting for a search term.
 * 
 * @author Paul Grogan
 */
public class SearchTextField extends JTextField {
	private static final long serialVersionUID = 3843718651416873933L;
	private String defaultText;
	
	/**
	 * The constructor.
	 * 
	 * @param defaultText the default prompt
	 */
	public SearchTextField(String defaultText) {
		super(defaultText);
		this.defaultText = defaultText;

        setFont(getFont().deriveFont(Font.ITALIC));
        setForeground(Color.LIGHT_GRAY);
        
		addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if(getText().equals(getDefaultText())) {
                	setText("");
                	setFont(getFont().deriveFont(Font.PLAIN));
                	setForeground(Color.BLACK);
                }
            }
            public void focusLost(FocusEvent e) {
                if(getText().equals("")) {
                    setText(getDefaultText());
                    setFont(getFont().deriveFont(Font.ITALIC));
                    setForeground(Color.LIGHT_GRAY);
                }
            }
        });
	}
	
	/**
	 * Gets the default prompt.
	 * 
	 * @return the default prompt
	 */
	public String getDefaultText() {
		return defaultText;
	}
}
