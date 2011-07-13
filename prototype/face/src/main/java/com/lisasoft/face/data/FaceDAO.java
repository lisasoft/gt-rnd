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

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.geotools.data.csv.CSVDataStore;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.opengis.feature.simple.SimpleFeature;

import com.csvreader.CsvReader;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
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
    
    /**
     * Change this to match the EPSG code for your spatial reference system.
     * 
     * See: http://spatialreference.org/ref/?search=ch1903
     */
    public static String EPSG_CODE = "EPSG:2056";
    
    static GeometryFactory gf = JTSFactoryFinder.getGeometryFactory(null);

    /**
     * This represents the contents - use CopyOnWriteArrayList to
     * account for concurrent access
     */
    private CopyOnWriteArrayList<FaceImpl> contents;

    public FaceDAO(List<FaceImpl> faceList) throws IOException {
        contents = new CopyOnWriteArrayList<FaceImpl>( faceList );
    }
    
    public FaceDAO(File csvFile) throws IOException {
        contents = new CopyOnWriteArrayList<FaceImpl>( load(csvFile) );
    }
    
    static     Point getLocation(Face face ) {
        double x = face.getWestOstKoordinate().doubleValue();
        double y = face.getSuedNordKoordinate().doubleValue();
        Coordinate coordinate = new Coordinate(x, y);
        return gf.createPoint(coordinate);
    }
    /** Thread-safe access to the data objects */
    public CopyOnWriteArrayList<FaceImpl> contents() {
        return contents;
    }
    /**
     * Used to access the bean info; this information is used to dynamically
     * generate the FeatureType and Features.
     * @return bean info for Face interface
     */
    public BeanInfo getBeanInfo(){
        BeanInfo info;
        try {
            info = Introspector.getBeanInfo( Face.class );
        } catch (IntrospectionException e) {
            return null;
        }
        return info;
        
    }
    /**
     * Used to read in from a CSV file; created as a static method for ease of testing.
     * 
     * @param csvFile
     * @return
     * @throws FileNotFoundException
     */
    public static List<FaceImpl> load(File csvFile) throws IOException {
        CsvReader reader = new CsvReader(new FileReader(csvFile));
        boolean header = reader.readHeaders();
        if (!header) {
            throw new IOException("Unable to read csv header");
        }
        List<FaceImpl> faceList = new ArrayList<FaceImpl>();

        while (reader.readRecord()) {
            // we have content let us make a Face
            // double latitude = Double.parseDouble(tokens[0]);
            // double longitude = Double.parseDouble(tokens[1]);
            //
            // String name = tokens[2].trim();
            // int number = Integer.parseInt(tokens[3].trim());

            // Nummer
            // int identifier = Integer.parseInt(tokens[0].trim());
            long identifier = Long.parseLong(reader.get("Nummer"));
            FaceImpl face = new FaceImpl(identifier);

            // String type = tokens[1].trim();
            face.setType(reader.get("Typ"));

            // String faceFormat = tokens[2].trim();
            face.setFaceFormat(reader.get(3));

            // String productFormat = tokens[3].trim();
            face.setProductFormat(reader.get(4));

            // String status = tokens[4].trim();
            face.setStatus(reader.get(5));

            // String installed = tokens[5].trim();
            face.setInstalled(reader.get(6));

            // String posting = tokens[6].trim();
            face.setPosting(reader.get(7));

            // String area = tokens[7].trim();
            face.setArea(reader.get(8));

            // String street = tokens[8].trim();
            face.setStreet(reader.get(9));

            // String number = tokens[9].trim();
            face.setNumber(reader.get(10));

            double x = Double.parseDouble(reader.get(11));
            face.setWestOstKoordinate(new BigDecimal(x));

            // double longitude = Double.parseDouble(tokens[11]);
            double y = Double.parseDouble(reader.get(12));
            face.setSuedNordKoordinate(new BigDecimal(y));

            // String angle = tokens[12].trim();
            face.setAngle(reader.get(13));

            // String category = tokens[13].trim();
            face.setCategory(reader.get(14));

            faceList.add(face);
        }
        return faceList;
    }

}
