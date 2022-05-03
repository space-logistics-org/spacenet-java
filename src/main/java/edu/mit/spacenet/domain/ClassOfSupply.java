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
package edu.mit.spacenet.domain;

import java.awt.Color;

/**
 * This enumeration lists the ten main classes of supply, and the sub-classes of supply.
 * 
 * @author Paul Grogan
 */
public enum ClassOfSupply {

  /** The COS 0. */
  COS0(0, "None", new Color(0, 0, 0)),

  /** The COS 1. */
  COS1(1, "Propellants and Fuels", new Color(0xff, 0x99, 0xff)),

  /** The COS 2. */
  COS2(2, "Crew Provisions", new Color(0, 0xcc, 0)),

  /** The COS 3. */
  COS3(3, "Crew Operations", new Color(0, 0, 0xcc)),

  /** The COS 4. */
  COS4(4, "Maintenance and Upkeep", new Color(0xff, 0xcc, 0)),

  /** The COS 5. */
  COS5(5, "Stowage and Restraint ", new Color(0xcc, 0, 0)),

  /** The COS 6. */
  COS6(6, "Exploration and Research", new Color(0xff, 0x99, 0)),

  /** The COS 7. */
  COS7(7, "Waste and Disposal", new Color(0x99, 0x66, 0)),

  /** The COS 8. */
  COS8(8, "Habitation and Infrastructure", new Color(0x66, 0, 0x99)),

  /** The COS 9. */
  COS9(9, "Transportation and Carriers", new Color(0, 0x66, 0x99)),

  /** The COS 10. */
  COS10(10, "Miscellaneous", new Color(0x99, 0x99, 0x99)),

  /** The COS 101. */
  COS101(101, "Cryogens", null),

  /** The COS 102. */
  COS102(102, "Hypergols", null),

  /** The COS 103. */
  COS103(103, "Nuclear Fuel", null),

  /** The COS 104. */
  COS104(104, "Petroleum Fuels", null),

  /** The COS 105. */
  COS105(105, "Other Fuels", null),

  /** The COS 106. */
  COS106(106, "Green Propellant", null),

  /** The COS 201. */
  COS201(201, "Water and Support Equipment", null),

  /** The COS 202. */
  COS202(202, "Food and Support Equipment", null),

  /** The COS 203. */
  COS203(203, "Gases", null),

  /** The COS 204. */
  COS204(204, "Hygiene Items", null),

  /** The COS 205. */
  COS205(205, "Clothing", null),

  /** The COS 206. */
  COS206(206, "Personal Items", null),

  /** The COS 301. */
  COS301(301, "Office Equipment and Supplies", null),

  /** The COS 302. */
  COS302(302, "EVA Equipment and Consumables", null),

  /** The COS 303. */
  COS303(303, "Health Equipment and Consumables", null),

  /** The COS 304. */
  COS304(304, "Safety Equipment", null),

  /** The COS 305. */
  COS305(305, "Communications Equipment", null),

  /** The COS 306. */
  COS306(306, "Computers and Support Equipment", null),

  /** The COS 401. */
  COS401(401, "Spares and Repair Parts", null),

  /** The COS 402. */
  COS402(402, "Maintenance Tools", null),

  /** The COS 403. */
  COS403(403, "Lubricants and Bulk Chemicals", null),

  /** The COS 404. */
  COS404(404, "Batteries", null),

  /** The COS 405. */
  COS405(405, "Cleaning Equipment and Consumables", null),

  /** The COS 501. */
  COS501(501, "Cargo Containers and Restraints", null),

  /** The COS 502. */
  COS502(502, "Inventory Management Equipment", null),

  /** The COS 601. */
  COS601(601, "Science Payloads and Instruments", null),

  /** The COS 602. */
  COS602(602, "Field Equipment", null),

  /** The COS 603. */
  COS603(603, "Samples", null),

  /** The COS 701. */
  COS701(701, "Waste", null),

  /** The COS 702. */
  COS702(702, "Waste Management Equipment", null),

  /** The COS 703. */
  COS703(703, "Failed Parts", null),

  /** The COS 801. */
  COS801(801, "Habitation Facilities", null),

  /** The COS 802. */
  COS802(802, "Surface Mobility Systems", null),

  /** The COS 803. */
  COS803(803, "Power Systems", null),

  /** The COS 804. */
  COS804(804, "Robotic Systems", null),

  /** The COS 805. */
  COS805(805, "Resource Utilization Systems", null),

  /** The COS 806. */
  COS806(806, "Orbiting Service Systems", null),

  /** The COS 901. */
  COS901(901, "Carriers, Non-propulsive Elements", null),

  /** The COS 902. */
  COS902(902, "Propulsive Elements", null),

  /** The COS 4011. */
  COS4011(4011, "Spares", null),

  /** The COS 4012. */
  COS4012(4012, "Repair Parts", null),

  /** The COS 8041. */
  COS8041(8041, "Science Robotics", null),

  /** The COS 8042. */
  COS8042(8042, "Construction/Maintenance Robotics", null),

  /** The COS 9021. */
  COS9021(9021, "Launch Vehicles", null),

  /** The COS 9022. */
  COS9022(9022, "Upper Stages/In-Space Propulsion Systems", null),

  /** The COS 9023. */
  COS9023(9023, "Descent Stages", null),

  /** The COS 9024. */
  COS9024(9024, "Ascent Stages", null);

  private int id;
  private String name;
  private Color color;

  /**
   * The default constructor.
   * 
   * @param id the class of supply number
   * @param name the name to display
   * @param color the default color to display in charts)
   */
  private ClassOfSupply(int id, String name, Color color) {
    this.id = id;
    this.name = name;
    this.color = color;
  }

  /**
   * Gets a particular instance of a class of supply based on its number.
   * 
   * @param id the class of supply number
   * 
   * @return the class of supply
   */
  public static ClassOfSupply getInstance(int id) {
    for (ClassOfSupply cos : ClassOfSupply.values()) {
      if (cos.getId() == id)
        return cos;
    }
    return null;
  }

  /**
   * Gets the class of supply number.
   * 
   * @return the class of supply number
   */
  public int getId() {
    return id;
  }

  /**
   * Gets the class of supply name.
   * 
   * @return the class of supply name
   */
  public String getName() {
    return name;
  }

  public String toString() {
    return "COS " + getId() + ": " + getName();
  }

  /**
   * Gets the color to be used in charts.
   * 
   * @return the color
   */
  public Color getColor() {
    if (color == null)
      return getBaseClass().getColor();
    else
      return color;
  }

  /**
   * Gets the base class of supply (i.e. 0 through 10)
   * 
   * @return the base class of supply
   */
  public ClassOfSupply getBaseClass() {
    for (int i = 0; i <= 10; i++) {
      if (this.isInstanceOf(ClassOfSupply.getInstance(i)))
        return ClassOfSupply.getInstance(i);
    }
    return null;
  }

  /**
   * Determines whether this class of supply is an instance (equal to or a subclass of) the given
   * class of supply.
   * 
   * @param cos the class of supply to compare against
   * 
   * @return true if this is an instance of cos, false otherwise
   */
  public boolean isInstanceOf(ClassOfSupply cos) {
    if (this.equals(cos) || this.isSubclassOf(cos))
      return true;
    else
      return false;
  }

  /**
   * Determines whether this class of supply is a subclass (i.e. child, more specific) of the given
   * class of supply.
   * 
   * @param cos the class of supply to compare against
   * 
   * @return true if this is a subclass of cos, false otherwise
   */
  public boolean isSubclassOf(ClassOfSupply cos) {
    String superclassId = new Integer(cos.getId()).toString();
    String subclassId = new Integer(id).toString();
    if (superclassId.length() >= subclassId.length())
      return false;
    if (id == 10 || cos.getId() == 10)
      return false;
    // above is a HACK to account for poor naming convention
    for (int i = 0; i < superclassId.length(); i++) {
      if (superclassId.charAt(i) != subclassId.charAt(i))
        return false;
    }
    return true;
  }

  /**
   * Determines whether this class of supply is a superclass (i.e. parent, less specific) of the
   * given class of supply.
   * 
   * @param cos the class of supply to compare against
   * 
   * @return true if this is a superclass of cos, false otherwise
   */
  public boolean isSuperclassOf(ClassOfSupply cos) {
    String superclassId = new Integer(id).toString();
    String subclassId = new Integer(cos.getId()).toString();
    if (superclassId.length() >= subclassId.length())
      return false;
    if (id == 10 || cos.getId() == 10)
      return false;
    // above is a HACK to account for poor naming convention
    for (int i = 0; i < superclassId.length(); i++) {
      if (superclassId.charAt(i) != subclassId.charAt(i))
        return false;
    }
    return true;
  }
}
