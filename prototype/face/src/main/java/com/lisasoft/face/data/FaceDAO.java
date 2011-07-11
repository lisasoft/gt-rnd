/**
 * GeoTools Example
 * 
 *  (C) 2011 LISAsoft
 *  
 *  This library is free software; you can redistribute it and/or modify it under
 *  the terms of the GNU Lesser General Public License as published by the Free
 *  Software Foundation; version 2.1 of the License.
 *  
 *  This library is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 */
package com.lisasoft.face.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.geotools.data.csv.CSVDataStore;
import org.opengis.feature.simple.SimpleFeature;

import com.csvreader.CsvReader;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Point;

/**
 * This is a little mock up of a traditional data acecss object.
 * <p>
 * Internally this just reads a CSV file and creates objects. This is used to show how we can wrap
 * up an object as a feature for visual display.
 * 
 * @author Jody Garnett (LISAsoft)
 */
public class FaceDAO {

    /** This represents the contents */
    private List<Face> contents;

    public FaceDAO(File csvFile) throws IOException {
        contents = load(csvFile);
    }

    /** Acess to the data objects */
    public List<Face> contents() {
        return contents;
    }

    /**
     * Used to read in from a CSV file; created as a static method for ease of testing.
     * 
     * @param csvFile
     * @return
     * @throws FileNotFoundException
     */
    public static List<Face> load(File csvFile) throws IOException {
        CsvReader reader = new CsvReader(new FileReader(csvFile));
        boolean header = reader.readHeaders();
        if (!header) {
            throw new IOException("Unable to read csv header");
        }
        List<Face> faceList = new ArrayList<Face>();
        
        while (reader.readRecord()) {
            // we have content let us make a Face
//            double latitude = Double.parseDouble(tokens[0]);
//            double longitude = Double.parseDouble(tokens[1]);
//
//            String name = tokens[2].trim();
//            int number = Integer.parseInt(tokens[3].trim());

            // Nummer
            // int identifier = Integer.parseInt(tokens[0].trim());
            long identifier = Long.parseLong(reader.get("Nummer"));
            FaceImpl face = new FaceImpl( identifier );
            
            // String type = tokens[1].trim();
            face.setType( reader.get("Typ"));
            
            // String faceFormat = tokens[2].trim();
            face.setFaceFormat( reader.get(2));
            
            // String productFormat = tokens[3].trim();
            face.setProductFormat( reader.get(3));
            
            // String status = tokens[4].trim();
            face.setType( reader.get(4));
            
            // String installed = tokens[5].trim();
            face.setType( reader.get("Typ"));
            
            // String posting = tokens[6].trim();
            face.setType( reader.get("Typ"));
            
            // String area = tokens[7].trim();
            face.setType( reader.get("Typ"));
            // String street = tokens[8].trim();
            face.setType( reader.get("Typ"));
            // String number = tokens[9].trim();
            face.setType( reader.get("Typ"));
            // double latitude = Double.parseDouble(tokens[10]);
            face.setType( reader.get("Typ"));
            // double longitude = Double.parseDouble(tokens[11]);
            face.setType( reader.get("Typ"));
            // String angle = tokens[12].trim();
            face.setType( reader.get("Typ"));
            // String category = tokens[13].trim();
            face.setType( reader.get("Typ"));
            
            //
            // /* Longitude (= x coord) first ! */
            // //Point point = geometryFactory.createPoint(new Coordinate(longitude, latitude));
            //
            // featureBuilder.add(identifier);
            // featureBuilder.add(type);
            // featureBuilder.add(faceFormat);
            // featureBuilder.add(productFormat);
            // featureBuilder.add(status);
            // featureBuilder.add(installed);
            // featureBuilder.add(posting);
            // featureBuilder.add(area);
            // featureBuilder.add(street);
            // featureBuilder.add(number);
            // featureBuilder.add(latitude);
            // featureBuilder.add(longitude);
            // featureBuilder.add(angle);
            // featureBuilder.add(category);
        }
        return null;
    }
}
