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
package edu.mit.spacenet.util;

import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

/**
 * Loads and stores all of the icons in one place for quick retrieval.
 */
public class IconLibrary {
  private static Map<String, ImageIcon> library;
  private static String[] files = {"icons/spacenet_icon_16.png", "icons/spacenet_icon_32.png",
      "icons/sun_icon.png", "icons/venus_icon.png", "icons/earth_icon.png", "icons/moon_icon.png",
      "icons/mars_icon.png", "icons/phobos_icon.png", "icons/jupiter_icon.png",
      "icons/earth_moon_icon.png", "icons/earth_mars_icon.png", "icons/solar_icon.png",
      "icons/bullet_purple.png", "icons/bullet_red.png", "icons/bullet_green.png",
      "icons/bullet_yellow.png", "icons/edge_yellow.png", "icons/edge_red.png",
      "icons/edge_green.png", "icons/bullet_pink.png", "icons/bullet_blue.png",
      "icons/bullet_wrench.png", "icons/clock_play.png", "icons/clock_pause.png",
      "icons/clock_stop.png", "icons/clock_red.png", "icons/brick.png", "icons/user.png",
      "icons/user_female.png", "icons/user_green.png", "icons/user_orange.png",
      "icons/user_red.png", "icons/package.png", "icons/lorry.png", "icons/car.png",
      "icons/crew_module.png", "icons/service_module.png", "icons/ascent_stage.png",
      "icons/descent_stage.png", "icons/lunar_lander.png", "icons/crew_capsule.png",
      "icons/launch_abort.png", "icons/ares_i_fs.png", "icons/ares_i_us.png",
      "icons/ares_v_srbs.png", "icons/ares_v_core.png", "icons/ares_v_eds.png",
      "icons/astronaut_white.png", "icons/astronaut_orange.png", "icons/shuttle.png",
      "icons/orbiter.png", "icons/habitat.png", "icons/power_supply.png", "icons/comment.png",
      "icons/add.png", "icons/arrow_right.png", "icons/delete.png", "icons/arrow_switch.png",
      "icons/comment.png", "icons/flame.png", "icons/bullet_red.png", "icons/bullet_black.png",
      "icons/door_open.png", "icons/flag_blue.png", "icons/flag_red.png", "icons/flag_green.png",
      "icons/flag_yellow.png", "icons/database.png", "icons/page_white_excel.png",
      "icons/database_edit.png", "icons/database_yellow.png", "icons/database_refresh.png",
      "icons/database_go.png", "icons/folder_explore.png", "icons/cog_edit.png"};

  /**
   * Gets the icon.
   * 
   * @param name the icon name (file name without extension, e.g. icons/icon_name.png is icon_name)
   * 
   * @return the icon
   */
  public static ImageIcon getIcon(String name) {
    if (library == null)
      initialize();
    return library.get(name);
  }

  /**
   * Initializes the library.
   */
  private static void initialize() {
    library = new HashMap<String, ImageIcon>();
    for (String file : files) {
      try {
        library.put(file.substring(file.lastIndexOf('/') + 1, file.lastIndexOf(".")),
            new ImageIcon(ClassLoader.getSystemResource(file)));
      } catch (Exception ex) {
        System.err.println("An error occurred while loading image " + file);
        ex.printStackTrace();
      }
    }
  }
}
