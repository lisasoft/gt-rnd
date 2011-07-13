package com.lisasoft.face.data;

import java.io.File;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

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

    public void testDAO() throws Exception {
        File csvFile = new File("./data/senario.csv");
        List<FaceImpl> list = FaceDAO.load(csvFile);
        assertTrue( "Loaded", list.size() > 0 );
        
        Face face = list.get(0);
        
        System.out.println( face );
    }
}
