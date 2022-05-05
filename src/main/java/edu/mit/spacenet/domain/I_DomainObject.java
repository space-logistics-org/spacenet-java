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
 * The base interface for any domain-level objects.
 * 
 * @author Paul Grogan
 */
public interface I_DomainObject extends I_DomainType {

  /**
   * Gets the Unique IDentifier.
   * 
   * @return the unique identifier
   */
  public int getUid();

  /**
   * Sets the Unique Identifier.
   * 
   * @param uid the new unique identifier
   */
  public void setUid(int uid);

  /**
   * Resets the Unique IDentifier after instantiation. Used during copy processes where
   * already-instantiated domain objects must be reassigned a new identifier.
   */
  public void resetUid();

  /**
   * Prints to console without any indentation.
   */
  public void print();

  /**
   * Prints to console at a specific tab indentation level.
   * 
   * @param tabOrder the indentation level
   */
  public void print(int tabOrder);
}
