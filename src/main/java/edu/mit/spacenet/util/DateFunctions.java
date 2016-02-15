/*
 * Copyright (c) 2010 MIT Strategic Engineering Research Group
 * 
 * This file is part of SpaceNet 2.5r2.
 * 
 * SpaceNet 2.5r2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * SpaceNet 2.5r2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with SpaceNet 2.5r2.  If not, see <http://www.gnu.org/licenses/>.
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
		return GlobalParameters.getRoundedTime(Math.abs((l1-l2)/(1000d*60d*60d*24d)));
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
		g.add(Calendar.SECOND, 
				(int) Math.round(Math.floor(offset * 24 * 60 * 60)));
		
		return g.getTime();
	}
}