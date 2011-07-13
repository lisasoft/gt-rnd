package com.lisasoft.face.table;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.SwingWorker;
import javax.swing.table.AbstractTableModel;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.util.NullProgressListener;
import org.opengis.feature.Feature;
import org.opengis.feature.FeatureVisitor;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.lisasoft.face.data.Face;

public class FaceTableModel<T extends Face> extends AbstractTableModel {
	
    private SimpleFeatureType schema;
    
    List<Object[]> cache = new ArrayList<Object[]>();
	
	public IOException exception;
	
	/**
     * A worker class to get the attributes of each feature and load 
     * them into the {@code TableModel}. The work is performed on a
     * background thread.
     */
    class TableWorker<T extends Face> extends SwingWorker<List<Object[]>, Object[]> {
        List<T> faces;

        /**
         * Constructor
         *
         * @param features the feature collection to be loaded into the table
         */
        TableWorker( List<T> faces ) { 
            this.faces = faces;  
            System.out.println("in table worker constructor");
        }

        /**
         * {@code SwingWorker} method to visit each feature and retrieve
         * its attributes
         */
        public List<Object[]> doInBackground() {
        	System.out.println("in doInBackground");
            List<Object[]> list = new ArrayList<Object[]>();
            
            /*try {
                features.accepts( new FeatureVisitor() {                
                    public void visit(Feature feature) {
                        SimpleFeature simple = (SimpleFeature) feature;
                        Object[] values = simple.getAttributes().toArray();
                        ArrayList<Object> row = new ArrayList<Object>( Arrays.asList( values ));
                        row.add(0, simple.getID() );
                        publish( row.toArray() );
                        
                        if( isCancelled() ) listener.setCanceled(true);
                    }
                } , listener );
            } catch (IOException e) {
                exception = e;
            }           */
            
            for(Face face : faces){
            	
            	System.out.println(face.toString());
            	/*
            	//SimpleFeature simple = (SimpleFeature) feature;
                Object[] values = face.getAttributes().toArray();
                ArrayList<Object> row = new ArrayList<Object>( Arrays.asList( values ));
                row.add(0, simple.getID() );
                publish( row.toArray() );
                */
            }
            
            return list;
        }

        /**
         * Add a batch of feature data to the table
         *
         * @param chunks batch of feature data
         */
        @Override
        protected void process(List<Object[]> chunks) {            
            int from = cache.size();
            cache.addAll( chunks );
            int to = cache.size();
            fireTableRowsInserted( from, to );
        }        
    }
	
	TableWorker load;
	
    /**
     * Constructor
     *
     * @param features the feature collection to load into the table
     */
    public FaceTableModel(List<T> faces){
    	this.load = new TableWorker(faces);
        load.execute();
        //this.schema = features.getSchema();
    }

    /**
     * Get the number of columns in the table
     *
     * @return the number of columns
     */
    public int getColumnCount() {
        if( exception != null ){
            return 1;
        }
        //return schema.getAttributeCount()+1;
        return 0;
    }

    /**
     * Get the number of rows in the table
     *
     * @return the number of rows
     */
    public int getRowCount() {
        if( exception != null ){
            return 1;
        }
        return cache.size();
    }

    /**
     * Get the value of a specified table entry
     *
     * @param rowIndex the row index
     * @param columnIndex the column index
     *
     * @return the table entry
     */
    public Object getValueAt(int rowIndex, int columnIndex) {    	
        if ( rowIndex < cache.size() ){
            Object row[] = cache.get( rowIndex );
            return row[ columnIndex ];
        }
        return null;
    }

}
