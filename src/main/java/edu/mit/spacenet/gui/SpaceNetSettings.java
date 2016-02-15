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
package edu.mit.spacenet.gui;

import java.awt.Rectangle;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * The Class SpaceNetSettings.
 */
public class SpaceNetSettings implements Serializable {
	private static final long serialVersionUID = 6683421611404213906L;
	private static final String filePath = "config";
	private String defaultDirectory;
	private boolean autoRefresh;
	private Rectangle lastBounds;
	
	/**
	 * Instantiates a new space net settings.
	 */
	private SpaceNetSettings() {
		lastBounds = new Rectangle();
		autoRefresh = true;
	}
	
	private static SpaceNetSettings instance;
	
	/**
	 * Gets the single instance of SpaceNetSettings.
	 * 
	 * @return single instance of SpaceNetSettings
	 */
	public static SpaceNetSettings getInstance() {
		if(instance==null) loadSettings();
		return instance;
	}
	
	/**
	 * Gets the default directory.
	 * 
	 * @return the default directory
	 */
	public String getDefaultDirectory() {
		if(defaultDirectory==null) return System.getProperty("user.dir");
		else return defaultDirectory;
	}
	
	/**
	 * Sets the default directory.
	 * 
	 * @param defaultDirectory the new default directory
	 */
	public void setDefaultDirectory(String defaultDirectory) {
		this.defaultDirectory = defaultDirectory;
	}
	
	/**
	 * Gets the last bounds.
	 *
	 * @return the last bounds
	 */
	public Rectangle getLastBounds() {
		return lastBounds;
	}
	
	/**
	 * Sets the last bounds.
	 *
	 * @param bounds the new last bounds
	 */
	public void setLastBounds(Rectangle bounds) {
		this.lastBounds = bounds;
	}
	
	/**
	 * Load settings.
	 */
	public static void loadSettings() {
		FileInputStream fis;
		try {
			fis = new FileInputStream(filePath);
			ObjectInputStream inStream = new ObjectInputStream(fis);
			instance = (SpaceNetSettings)inStream.readObject();
			inStream.close();
			fis.close();
		} catch (Exception e) {
			// there was a problem loading the settings, reverting to defaults
			instance = new SpaceNetSettings();
		}
	}
	
	/**
	 * Save settings.
	 */
	public static void saveSettings() {
    	SpaceNetSettings.getInstance().setLastBounds(SpaceNetFrame.getInstance().getBounds());
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(filePath);
			ObjectOutputStream outStream = new ObjectOutputStream(fos);
			outStream.writeObject(SpaceNetSettings.getInstance());
			outStream.flush();
			outStream.close();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Checks if is auto refresh.
	 * 
	 * @return true, if is auto refresh
	 */
	public boolean isAutoRefresh() {
		return autoRefresh;
	}

	/**
	 * Sets the auto refresh.
	 * 
	 * @param autoRefresh the new auto refresh
	 */
	public void setAutoRefresh(boolean autoRefresh) {
		this.autoRefresh = autoRefresh;
	}
}
