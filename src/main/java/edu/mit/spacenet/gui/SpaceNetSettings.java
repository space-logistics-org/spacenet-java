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
    if (instance == null)
      loadSettings();
    return instance;
  }

  /**
   * Gets the default directory.
   * 
   * @return the default directory
   */
  public String getDefaultDirectory() {
    if (defaultDirectory == null)
      return System.getProperty("user.dir");
    else
      return defaultDirectory;
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
      instance = (SpaceNetSettings) inStream.readObject();
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
