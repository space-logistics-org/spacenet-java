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

import edu.mit.spacenet.util.IdGenerator;

/**
 * The base class for any classes utilizing a database-storage format that also require a
 * specialized method for comparisons of objects that may reside at different memory locations, but
 * correspond to one logical object.
 * 
 * @author Paul Grogan
 */
public abstract class DomainObject extends DomainType implements I_DomainObject {
  private int uid;

  /**
   * The default constructor automatically assigns the unique identifier and defaults the type
   * identifier to -1.
   */
  public DomainObject() {
    super();
    uid = IdGenerator.getUid();
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.I_DomainObject#getUid()
   */
  public int getUid() {
    return uid;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.I_DomainObject#setUid(int)
   */
  public void setUid(int uid) {
    this.uid = uid;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.I_DomainObject#resetUid()
   */
  public void resetUid() {
    uid = IdGenerator.getUid();
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.DomainType#equals(java.lang.Object)
   */
  public boolean equals(Object object) {
    /*
     * Due to the serialization process used in SpaceNet, domain objects (i.e. elements) must use
     * unique identification numbers instead of memory addresses to determine equality.
     */
    if (object instanceof DomainObject && getClass().equals(object.getClass()))
      return getUid() == ((DomainObject) object).getUid();
    else
      return false;
  }
}
