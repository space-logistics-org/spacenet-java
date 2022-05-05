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
package edu.mit.spacenet.gui.command;

import edu.mit.spacenet.gui.SpaceNetFrame;

/**
 * The command to create a new scenario.
 * 
 * @author Paul Grogan
 */
public class NewScenarioCommand implements I_Command {
  private SpaceNetFrame spaceNetFrame;

  /**
   * The constructor.
   * 
   * @param spaceNetFrame the SpaceNet frame
   */
  public NewScenarioCommand(SpaceNetFrame spaceNetFrame) {
    this.spaceNetFrame = spaceNetFrame;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.gui.command.I_Command#execute()
   */
  public void execute() {
    spaceNetFrame.newScenario();
  }
}
