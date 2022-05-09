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
package edu.mit.spacenet.domain.element;

import edu.mit.spacenet.domain.Environment;

/**
 * Class which is used to build the "default" resource containers used in auto-packing and
 * auto-manifesting.
 * 
 * @author Paul Grogan
 */
public abstract class ResourceContainerFactory {

  /** The Constant DCTB_TID. */
  public static final int DCTB_TID = -22;

  /** The Constant DCTB_MAX_MASS. */
  public static final double DCTB_MAX_MASS = 53.6;

  /** The Constant DCTB_MAX_VOLUME. */
  public static final double DCTB_MAX_VOLUME = 0.098;

  /**
   * Creates a new double cargo transfer bag (DCTB).
   * 
   * @return the cargo transfer bag
   */
  public static ResourceContainer createDCTB() {
    // double cargo transfer bag
    ResourceContainer ctb = new ResourceContainer();
    ctb.setTid(DCTB_TID);
    ctb.setMass(1.383);
    ctb.setMaxCargoMass(DCTB_MAX_MASS);
    ctb.setVolume(0.106);
    ctb.setMaxCargoVolume(DCTB_MAX_VOLUME);

    ctb.setName("DCTB " + ctb.getUid());
    return ctb;
  }


  /** The Constant CTB_TID. */
  public static final int CTB_TID = -10;

  /** The Constant CTB_MAX_MASS. */
  public static final double CTB_MAX_MASS = 26.8;

  /** The Constant CTB_MAX_VOLUME. */
  public static final double CTB_MAX_VOLUME = 0.049;

  /**
   * Creates a cargo transfer bag (CTB).
   * 
   * @return the cargo transfer bag
   */
  public static ResourceContainer createCTB() {
    // cargo transfer bag
    ResourceContainer ctb = new ResourceContainer();
    ctb.setTid(CTB_TID);
    ctb.setMass(0.83);
    ctb.setMaxCargoMass(CTB_MAX_MASS);
    ctb.setVolume(0.053);
    ctb.setMaxCargoVolume(CTB_MAX_VOLUME);

    ctb.setName("CTB " + ctb.getUid());
    return ctb;
  }

  /** The Constant HCTB_TID. */
  public static final int HCTB_TID = -11;

  /** The Constant HCTB_MAX_MASS. */
  public static final double HCTB_MAX_MASS = 13.4;

  /** The Constant HCTB_MAX_VOLUME. */
  public static final double HCTB_MAX_VOLUME = 0.0245;

  /**
   * Creates a half-size cargo transfer bag (CTB).
   * 
   * @return the half cargo transfer bag
   */
  public static ResourceContainer createHCTB() {
    // half cargo transfer bag
    ResourceContainer ctb = new ResourceContainer();
    ctb.setTid(HCTB_TID);
    ctb.setMass(.5532);
    ctb.setMaxCargoMass(HCTB_MAX_MASS);
    ctb.setVolume(0.0265);
    ctb.setMaxCargoVolume(HCTB_MAX_VOLUME);

    ctb.setName("Half CTB " + ctb.getUid());
    return ctb;
  }

  /** The Constant CWC_TID. */
  public static final int CWC_TID = -12;

  /**
   * Creates a contingency water container (CWC).
   * 
   * @return the contingency water container
   */
  public static ResourceContainer createCWC() {
    // contingency water container
    ResourceContainer cwc = new ResourceContainer();
    cwc.setTid(CWC_TID);
    cwc.setMass(1.361);
    cwc.setMaxCargoMass(32.8);
    cwc.setVolume(0.0328);
    cwc.setMaxCargoVolume(0.0328);

    cwc.setName("Contingency Water Container " + cwc.getUid());
    return cwc;
  }

  /** The Constant LT_TID. */
  public static final int LT_TID = -13;

  /** The Constant LT_MAX_MASS. */
  public static final double LT_MAX_MASS = 74.8;

  /** The Constant LT_MAX_VOLUME. */
  public static final double LT_MAX_VOLUME = 0.0748;

  /**
   * Creates a liquid tank (LT).
   * 
   * @return the liquid tank
   */
  public static ResourceContainer createLT() {
    // liquid tank
    ResourceContainer lt = new ResourceContainer();
    lt.setTid(LT_TID);
    lt.setMass(34.37);
    lt.setMaxCargoMass(LT_MAX_MASS);
    lt.setVolume(0.0748);
    lt.setMaxCargoVolume(LT_MAX_VOLUME);

    lt.setName("Liquid Tank " + lt.getUid());
    return lt;
  }

  /** The Constant LTD_TID. */
  public static final int LTD_TID = -14;

  /** The Constant LTD_MAX_MASS. */
  public static final double LTD_MAX_MASS = 24.9333;

  /** The Constant LTD_MAX_VOLUME. */
  public static final double LTD_MAX_VOLUME = 0.0249;

  /**
   * Creates a derivative liquid tank (LT).
   * 
   * @return the liquid tank
   */
  public static ResourceContainer createLTD() {
    // liquid tank derivative
    ResourceContainer ltd = new ResourceContainer();
    ltd.setTid(LTD_TID);
    ltd.setMass(11.4567);
    ltd.setMaxCargoMass(LTD_MAX_MASS);
    ltd.setVolume(0.0249);
    ltd.setMaxCargoVolume(LTD_MAX_VOLUME);

    ltd.setName("Liquid Tank Derivative " + ltd.getUid());
    return ltd;
  }

  /** The Constant SHOSS_TID. */
  public static final int SHOSS_TID = -15;

  /** The Constant SHOSS_MAX_MASS. */
  public static final double SHOSS_MAX_MASS = 200;

  /** The Constant SHOSS_MAX_VOLUME. */
  public static final double SHOSS_MAX_VOLUME = 0.4444;

  /**
   * Creates a SHOSS box.
   * 
   * @return the SHOSS box
   */
  public static ResourceContainer createShoss() {
    // shoss box
    ResourceContainer shoss = new ResourceContainer();
    shoss.setTid(SHOSS_TID);
    shoss.setMass(120);
    shoss.setMaxCargoMass(SHOSS_MAX_MASS);
    shoss.setVolume(0.4444);
    shoss.setMaxCargoVolume(SHOSS_MAX_VOLUME);

    shoss.setName("Shoss " + shoss.getUid());
    return shoss;
  }

  /** The Constant PSHOSS_TID. */
  public static final int PSHOSS_TID = -16;

  /** The Constant PSHOSS_MAX_MASS. */
  public static final double PSHOSS_MAX_MASS = 200;

  /** The Constant PSHOSS_MAX_VOLUME. */
  public static final double PSHOSS_MAX_VOLUME = 0.8;

  /**
   * Creates a pressurized SHOSS box.
   * 
   * @return the pressurized SHOSS box
   */
  public static ResourceContainer createPressShoss() {
    // pressurized shoss box
    ResourceContainer pshoss = new ResourceContainer();
    pshoss.setTid(PSHOSS_TID);
    pshoss.setMass(0);
    pshoss.setMaxCargoMass(PSHOSS_MAX_MASS);
    pshoss.setVolume(0.8);
    pshoss.setMaxCargoVolume(PSHOSS_MAX_VOLUME);

    pshoss.setName("Press. Shoss " + pshoss.getUid());
    pshoss.setCargoEnvironment(Environment.PRESSURIZED);
    return pshoss;
  }

  /** The Constant GT_TID. */
  public static final int GT_TID = -17;

  /** The Constant GT_MAX_MASS. */
  public static final double GT_MAX_MASS = 100;

  /** The Constant GT_MAX_VOLUME. */
  public static final double GT_MAX_VOLUME = 2.75;

  /**
   * Creates a gas tank (GT).
   * 
   * @return the gas tank
   */
  public static ResourceContainer createGT() {
    // gas tank
    ResourceContainer gt = new ResourceContainer();
    gt.setTid(GT_TID);
    gt.setMass(108);
    gt.setMaxCargoMass(GT_MAX_MASS);
    gt.setVolume(2.75);
    gt.setMaxCargoVolume(GT_MAX_VOLUME);

    gt.setName("Gas Tank " + gt.getUid());
    return gt;
  }

  /** The Constant GTD_TID. */
  public static final int GTD_TID = -17;

  /** The Constant GTD_MAX_MASS. */
  public static final double GTD_MAX_MASS = 10;

  /** The Constant GTD_MAX_VOLUME. */
  public static final double GTD_MAX_VOLUME = 0.275;

  /**
   * Creates a derivative gas tank (GT).
   * 
   * @return the derivative gas tank
   */
  public static ResourceContainer createGTD() {
    // gas tank derivative
    ResourceContainer gtd = new ResourceContainer();
    gtd.setTid(GTD_TID);
    gtd.setMass(10.8);
    gtd.setMaxCargoMass(GTD_MAX_MASS);
    gtd.setVolume(0.275);
    gtd.setMaxCargoVolume(GTD_MAX_VOLUME);

    gtd.setName("Gas Tank Derivative " + gtd.getUid());
    return gtd;
  }

  /** The Constant O2T_TID. */
  public static final int O2T_TID = -18;

  /**
   * Creates an oxygen tank (O2T).
   * 
   * @return the oxygen tank
   */
  public static ResourceContainer createO2T() {
    // oxygen tank
    ResourceContainer o2t = new ResourceContainer();
    o2t.setTid(O2T_TID);
    o2t.setMass(108);
    o2t.setMaxCargoMass(108.9);
    o2t.setVolume(2.75);
    o2t.setMaxCargoVolume(2.275);

    o2t.setName("O2 Tank " + o2t.getUid());
    return o2t;
  }

  /** The Constant O2TD_TID. */
  public static final int O2TD_TID = -19;

  /**
   * Creates a derivative oxygen tank (O2T).
   * 
   * @return the derivative oxygen tank
   */
  public static ResourceContainer createO2TD() {
    // oxygen tank derivative
    ResourceContainer o2td = new ResourceContainer();
    o2td.setTid(O2TD_TID);
    o2td.setMass(10.8);
    o2td.setMaxCargoMass(10.89);
    o2td.setVolume(0.275);
    o2td.setMaxCargoVolume(0.275);

    o2td.setName("O2 Tank Derivative " + o2td.getUid());
    return o2td;
  }

  /** The Constant N2T_TID. */
  public static final int N2T_TID = -20;

  /**
   * Creats a nitrogen tank (N2T).
   * 
   * @return the nitrogen tank
   */
  public static ResourceContainer createN2T() {
    // nitrogen tank
    ResourceContainer n2t = new ResourceContainer();
    n2t.setTid(N2T_TID);
    n2t.setMass(108);
    n2t.setMaxCargoMass(94.8);
    n2t.setVolume(2.732);
    n2t.setMaxCargoVolume(2.732);

    n2t.setName("N2 Tank " + n2t.getUid());
    return n2t;
  }

  /** The Constant N2TD_TID. */
  public static final int N2TD_TID = -21;

  /**
   * Creates a derivative nitrogen tank (N2T).
   * 
   * @return the derivative nitrogen tank
   */
  public static ResourceContainer createN2TD() {
    // nitrogen tank derivative
    ResourceContainer n2td = new ResourceContainer();
    n2td.setTid(N2TD_TID);
    n2td.setMass(10.8);
    n2td.setMaxCargoMass(9.48);
    n2td.setVolume(0.2732);
    n2td.setMaxCargoVolume(0.2732);

    n2td.setName("N2 Tank Derivative " + n2td.getUid());
    return n2td;
  }
}
