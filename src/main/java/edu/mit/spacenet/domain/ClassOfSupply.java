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

  /** No specified class of supply. */
  COS0(0, "None", new Color(0, 0, 0)),

  /** Propellants and fuels. */
  COS1(1, "Propellants and Fuels", new Color(0xff, 0x99, 0xff)),

  /** Crew provisions. */
  COS2(2, "Crew Provisions", new Color(0, 0xcc, 0)),

  /** Crew operations. */
  COS3(3, "Crew Operations", new Color(0, 0, 0xcc)),

  /** Maintenance and upkeep. */
  COS4(4, "Maintenance and Upkeep", new Color(0xff, 0xcc, 0)),

  /** Stowage and restraint. */
  COS5(5, "Stowage and Restraint ", new Color(0xcc, 0, 0)),

  /** Exploration and research. */
  COS6(6, "Exploration and Research", new Color(0xff, 0x99, 0)),

  /** Waste and disposal. */
  COS7(7, "Waste and Disposal", new Color(0x99, 0x66, 0)),

  /** Habitation and infrastructure. */
  COS8(8, "Habitation and Infrastructure", new Color(0x66, 0, 0x99)),

  /** Transportation and carriers. */
  COS9(9, "Transportation and Carriers", new Color(0, 0x66, 0x99)),

  /** Miscellaneous. */
  COS10(10, "Miscellaneous", new Color(0x99, 0x99, 0x99)),

  /** Cryogens. */
  COS101(101, "Cryogens", null),

  /** Hypergols. */
  COS102(102, "Hypergols", null),

  /** Nuclear fuel. */
  COS103(103, "Nuclear Fuel", null),

  /** Petroleum fuels. */
  COS104(104, "Petroleum Fuels", null),

  /** Other fuels. */
  COS105(105, "Other Fuels", null),

  /** Green propellant. */
  COS106(106, "Green Propellant", null),

  /** Water and support equipment. */
  COS201(201, "Water and Support Equipment", null),

  /** Food and support equipment. */
  COS202(202, "Food and Support Equipment", null),

  /** Gases. */
  COS203(203, "Gases", null),

  /** Hygiene items. */
  COS204(204, "Hygiene Items", null),

  /** Clothing. */
  COS205(205, "Clothing", null),

  /** Personal items. */
  COS206(206, "Personal Items", null),

  /** Office equipment and supplies. */
  COS301(301, "Office Equipment and Supplies", null),

  /** EVA equipment and consumables. */
  COS302(302, "EVA Equipment and Consumables", null),

  /** Health equipment and consumables. */
  COS303(303, "Health Equipment and Consumables", null),

  /** Safety equipment. */
  COS304(304, "Safety Equipment", null),

  /** Communications equipment. */
  COS305(305, "Communications Equipment", null),

  /** Computers and support equipment. */
  COS306(306, "Computers and Support Equipment", null),

  /** Spares and repair parts. */
  COS401(401, "Spares and Repair Parts", null),

  /** Maintenance tools. */
  COS402(402, "Maintenance Tools", null),

  /** Lubricants and bulk chemicals. */
  COS403(403, "Lubricants and Bulk Chemicals", null),

  /** Batteries. */
  COS404(404, "Batteries", null),

  /** Cleaning equipment and consumables. */
  COS405(405, "Cleaning Equipment and Consumables", null),

  /** Cargo containers and restraints. */
  COS501(501, "Cargo Containers and Restraints", null),

  /** Inventory management equipment. */
  COS502(502, "Inventory Management Equipment", null),

  /** Science payloads and instruments. */
  COS601(601, "Science Payloads and Instruments", null),

  /** Field equipment. */
  COS602(602, "Field Equipment", null),

  /** Samples. */
  COS603(603, "Samples", null),

  /** Waste. */
  COS701(701, "Waste", null),

  /** Waste management equipment. */
  COS702(702, "Waste Management Equipment", null),

  /** Failed parts. */
  COS703(703, "Failed Parts", null),

  /** Habitation facilities. */
  COS801(801, "Habitation Facilities", null),

  /** Surface mobility systems. */
  COS802(802, "Surface Mobility Systems", null),

  /** Power systems. */
  COS803(803, "Power Systems", null),

  /** Robotic systems. */
  COS804(804, "Robotic Systems", null),

  /** Resource utilization systems. */
  COS805(805, "Resource Utilization Systems", null),

  /** Orbiting service systems. */
  COS806(806, "Orbiting Service Systems", null),

  /** Carriers and non-propulsive elements. */
  COS901(901, "Carriers, Non-propulsive Elements", null),

  /** Propulsive elements. */
  COS902(902, "Propulsive Elements", null),

  /** Spares. */
  COS4011(4011, "Spares", null),

  /** Repair parts. */
  COS4012(4012, "Repair Parts", null),

  /** Science robotics. */
  COS8041(8041, "Science Robotics", null),

  /** Construction and maintenance robotics. */
  COS8042(8042, "Construction/Maintenance Robotics", null),

  /** Launch vehicles. */
  COS9021(9021, "Launch Vehicles", null),

  /** Upper stages and in-space propulsion systems. */
  COS9022(9022, "Upper Stages/In-Space Propulsion Systems", null),

  /** Descent stages. */
  COS9023(9023, "Descent Stages", null),

  /** Ascent stages. */
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
