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

/**
 * A factory for creating elements.
 * 
 * @author Paul Grogan
 */
public abstract class ElementFactory {

  /**
   * Factory method that creates a new element based on the passed element type. Throws a runtime
   * exception if an unsupported element type is used.
   * 
   * @param type the element type to create
   * 
   * @return the newly-created element
   */
  public static I_Element createElement(ElementType type) {
    switch (type) {
      case ELEMENT:
        return new Element();
      case CREW_MEMBER:
        return new CrewMember();
      case RESOURCE_CONTAINER:
        return new ResourceContainer();
      case RESOURCE_TANK:
        return new ResourceTank();
      case CARRIER:
        return new Carrier();
      case SURFACE_VEHICLE:
        return new SurfaceVehicle();
      case PROPULSIVE_VEHICLE:
        return new PropulsiveVehicle();
      default:
        throw new RuntimeException("Unsupported Element");
    }
  }
}
