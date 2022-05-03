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

/**
 * The environment enumeration is used to differentiate between pressurized and unpressurized cargo.
 * 
 * @author Paul Grogan
 */
public enum Environment {

  /** Pressurized environments. */
  PRESSURIZED("Pressurized"),

  /** Unpressurized environments. */
  UNPRESSURIZED("Unpressurized");

  private String name;

  private Environment(String name) {
    this.name = name;
  }

  /**
   * Gets the name.
   * 
   * @return the name
   */
  public String getName() {
    return name;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Enum#toString()
   */
  public String toString() {
    return name;
  }

  /**
   * Gets the single instance of Environment.
   * 
   * @param name the name
   * 
   * @return single instance of Environment
   */
  public static Environment getInstance(String name) {
    for (Environment environment : values()) {
      if (environment.getName().toLowerCase().equals(name.toLowerCase()))
        return environment;
    }
    return null;
  }
}
