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

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Some useful functions for performing operations with dates.
 * 
 * @author Paul Grogan
 */
public class DateFunctions {

  /**
   * Gets the number of days between two dates.
   * 
   * @param d1 the first date
   * @param d2 the second date
   * 
   * @return the number of days between
   */
  public static double getDaysBetween(Date d1, Date d2) {
    long l1 = d1.getTime();
    long l2 = d2.getTime();
    return GlobalParameters.getSingleton()
        .getRoundedTime(Math.abs((l1 - l2) / (1000d * 60d * 60d * 24d)));
  }

  /**
   * Gets the date that is a number of days after a reference date.
   * 
   * @param date the reference date
   * @param offset the number of days offset
   * 
   * @return the date
   */
  public static Date getDate(Date date, Double offset) {
    GregorianCalendar g = new GregorianCalendar();
    g.setTime(date);
    g.add(Calendar.SECOND, (int) Math.round(Math.floor(offset * 24 * 60 * 60)));

    return g.getTime();
  }
}
