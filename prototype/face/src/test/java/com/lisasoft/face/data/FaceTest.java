package com.lisasoft.face.data;

import java.io.File;
import java.io.IOException;
import java.util.List;

import junit.framework.TestCase;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.store.ContentFeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.Feature;
import org.opengis.feature.FeatureVisitor;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Point;

/**
 * Unit test for simple App.
 */
public class FaceTest extends TestCase {
    /**
     * Create the test case
     * 
     * @param testName name of the test case
     */
    public FaceTest(String testName) {
        super(testName);
    }

    public void testDAOload() throws Exception {
        File csvFile = new File("./data/senario.csv");
        List<FaceImpl> list = FaceDAO.load(csvFile);
        assertTrue("Loaded", list.size() > 0);

        Face face = list.get(0);

        System.out.println(face);
    }

    public void testDataStore() throws IOException {
        File csvFile = new File("./data/senario.csv");
        FaceDAO data = new FaceDAO( csvFile );

        FaceDataStore store = new FaceDataStore(data);
        String[] typeNames = store.getTypeNames();
        assertEquals(typeNames.length , 1 );
        assertEquals( "Face", typeNames[0]);
        
        final SimpleFeatureSource source = store.getFeatureSource("Face");
        final SimpleFeatureType schema = source.getSchema();
        GeometryDescriptor geom = schema.getGeometryDescriptor();
        assertEquals( "Point", geom.getLocalName() );
        assertEquals( Point.class, geom.getType().getBinding());
        CoordinateReferenceSystem crs = geom.getCoordinateReferenceSystem();
        assertNotNull( crs );        
        ReferencedEnvelope bounds = source.getBounds();
        assertEquals( crs, bounds.getCoordinateReferenceSystem() );
        assertFalse( bounds.isEmpty() );
        assertFalse( bounds.isNull() );
        
        SimpleFeatureCollection features = source.getFeatures();
        assertEquals( 5, features.size() );
        
        SimpleFeatureIterator iterator = features.features();
        try {
            while ( iterator.hasNext() ){
                SimpleFeature feature = iterator.next();
                assertEquals( schema, feature.getFeatureType() );
                Point point = (Point) feature.getDefaultGeometry();
                assertNotNull( point );
                String angle = (String) feature.getAttribute("angle");
                assertNotNull( angle );
            }
        }
        finally {
            iterator.close();
        }
        features.accepts( new FeatureVisitor() {
            public void visit(Feature f) {
                SimpleFeature feature = (SimpleFeature) f;
                assertEquals( schema, feature.getFeatureType() );
                Point point = (Point) feature.getDefaultGeometry();
                assertNotNull( point );
                String angle = (String) feature.getAttribute("angle");
                assertNotNull( angle );
            }
        }, null );
    }
}
