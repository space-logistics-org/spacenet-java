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
package edu.mit.spacenet.io;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * A filter for file dialogs to only show .xml files.
 * 
 * @author Paul Grogan
 */
public class XMLFileFilter extends FileFilter {
	
	/* (non-Javadoc)
	 * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
	 */
	public boolean accept(File f) {
		if(f.isDirectory()) {
			return true;
		}
		String extension = null;
		
		String s = f.getName();
		int i = s.lastIndexOf('.');
		if (i > 0 &&  i < s.length() - 1) {
			extension = s.substring(i+1).toLowerCase();
        }
		
		if(extension != null) {
			if(extension.equals("xml")) return true;
			else return false;
		} else return false;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.filechooser.FileFilter#getDescription()
	 */
	public String getDescription() {
		return "XML Files";
	}
}
