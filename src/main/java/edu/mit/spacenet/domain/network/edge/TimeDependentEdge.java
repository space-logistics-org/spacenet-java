/*
 * Copyright 2010 MIT Strategic Engineering Research Group
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.mit.spacenet.domain.network.edge;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import edu.mit.spacenet.domain.network.node.OrbitalNode;

public class TimeDependentEdge extends Edge {
	private OrbitalNode origin, destination;
	
	@Override
	public OrbitalNode getOrigin() {
		return origin;
	}
	public void setOrigin(OrbitalNode origin) {
		this.origin = origin;
	}
	@Override
	public OrbitalNode getDestination() {
		return destination;
	}
	public void setDestination(OrbitalNode destination) {
		this.destination = destination;
	}
	
	private String filePath;
	
	@SuppressWarnings("deprecation")
	public List<SpaceEdge> getEdges(Date departureDate, double originPeriapsis, double originApoapsis, double destinationPeriapsis, double destinationApoapsis, double departureC3Limit, double arrivalC3Limit, boolean usesAerocapture) throws IOException {
		List<SpaceEdge> edges = new ArrayList<SpaceEdge>();
		
		// add code to open file @ filePath
		// read data into SpaceEdge structures
		// add SpaceEdge structures into List
		
		// create poi file system
		// open poi workbook
		// open poi worksheet
		
		POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(filePath));
    	HSSFWorkbook wb = new HSSFWorkbook(fs);
		
    	HSSFSheet infoSheet = wb.getSheetAt(0);
    	HSSFRow infoRow = infoSheet.getRow(2);
    	
    	// date of origin (0,0) in the C3 chart
    	// Date originDate = infoRow.getCell(0).getDateCellValue();
    	int originDateJulian = (int) infoRow.getCell(1).getNumericCellValue();
    	// departure date axis
    	// int xAxisRange = (int) infoRow.getCell(2).getNumericCellValue();
    	// int xCellNum = (int) infoRow.getCell(3).getNumericCellValue();
    	int xStepSize = (int) infoRow.getCell(4).getNumericCellValue();
    	// TOF axis
    	// int yAxisRange = (int) infoRow.getCell(5).getNumericCellValue();
    	int yCellNum = (int) infoRow.getCell(6).getNumericCellValue();
    	int yStepSize = (int) infoRow.getCell(7).getNumericCellValue();
    	// departure/arrival planet
    	// String departurePlanet = infoRow.getCell(8).toString();
    	double radiusOriginBody = infoRow.getCell(9).getNumericCellValue();
    	double muOriginBody = infoRow.getCell(10).getNumericCellValue();
    	// String arrivalPlanet = infoRow.getCell(11).toString();
    	double radiusDestinationBody = infoRow.getCell(12).getNumericCellValue();
    	double muDestinationBody = infoRow.getCell(13).getNumericCellValue();
    	
		// Julian Day for departure date
    	int departureDateYear = departureDate.getYear();
    	int departureDateMonth = departureDate.getMonth();
    	int departureDateDate = departureDate.getDate();
    	int departureDateHour = 12;
    	int departureDateMinute = 0;
    	int departureDateSecond = 0;
    	double departureDateHMD = departureDateHour+departureDateMinute/60+departureDateSecond/(60*60);
    	double departureDateJulian = 367*departureDateYear-Math.floor(7*(departureDateYear+Math.floor((departureDateMonth+9)/12))/4)+Math.floor(275*departureDateMonth/9)+departureDateDate+departureDateHMD/24+1721013.5;
    	
    	// radii of origin and destination orbits
    	double radiusOriginPeriapsis = radiusOriginBody + originPeriapsis; // [km]
    	double radiusOriginApoapsis = radiusOriginBody + originApoapsis; // [km]
    	double radiusDestinationPeriapsis = radiusDestinationBody + destinationPeriapsis; // [km]
    	double radiusDestinationApoapsis = radiusDestinationBody + destinationApoapsis; // [km]
    	
    	// departure Julian Date -> row number
    	int departureDateRowNumber = (int) Math.ceil((departureDateJulian-originDateJulian)/xStepSize);
    	
    	// C3d worksheet
    	// C3d row
    	HSSFSheet C3dSheet = wb.getSheetAt(1);
    	HSSFRow C3dRow = C3dSheet.getRow(departureDateRowNumber);
    	
    	// C3a worksheet
    	// C3a row
    	HSSFSheet C3aSheet = wb.getSheetAt(2);
    	HSSFRow C3aRow = C3aSheet.getRow(departureDateRowNumber);
    	
    	// for each row until end
		for(int j = 0; j <= yCellNum - 1; j++) {
			// read C3d and C3a
			double C3d = C3dRow.getCell(j).getNumericCellValue();
			double C3a = C3aRow.getCell(j).getNumericCellValue();
			
			// check against departure limit
			// check against arrival limit			
			// if within bounds:
			if(C3d <= departureC3Limit && C3a <= arrivalC3Limit) {
				// create new space edge
				SpaceEdge edge = new SpaceEdge();
				
				// copy same information (id, name, origin, destination, description)
				edge.setTid(getTid());
				edge.setName(getName());
				edge.setOrigin(getOrigin());
				edge.setDestination(getDestination());
				edge.setDescription(getDescription());
				
				// read and fill duration and burns
				// TOF = duration [days]
				double TOF = j*yStepSize;
				edge.setDuration(TOF);
				
				// delta-V's = burn [m/s]
				// C3 -> delta-V conversion
				// assuming both departure and arrival at periapsis
				double departureDeltaV = (Math.sqrt(C3d+2*muOriginBody/radiusOriginPeriapsis)-Math.sqrt(muOriginBody/radiusOriginPeriapsis)*Math.sqrt(2*radiusOriginApoapsis/(radiusOriginPeriapsis+radiusOriginApoapsis)))*1000;
				double arrivalDeltaV = (Math.sqrt(C3a+2*muDestinationBody/radiusDestinationPeriapsis)-Math.sqrt(muDestinationBody/radiusDestinationPeriapsis)*Math.sqrt(2*radiusDestinationApoapsis/(radiusDestinationPeriapsis+radiusDestinationApoapsis)))*1000;
				edge.getBurns().add(new Burn(0, BurnType.OMS, departureDeltaV));
				if(usesAerocapture == false) {
					edge.getBurns().add(new Burn(TOF, BurnType.OMS, arrivalDeltaV));
				}
				
				// add new edge to list
				edges.add(edge);
				
			// end if
			}
		// end for
		}
		
		wb.close();
		
		return edges;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	@Override
	public EdgeType getEdgeType() {
		return EdgeType.TIME_DEPENDENT;
	}
}
