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
