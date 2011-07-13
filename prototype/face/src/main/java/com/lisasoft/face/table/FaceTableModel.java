package com.lisasoft.face.table;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingWorker;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import com.lisasoft.face.data.FaceImpl;

public class FaceTableModel extends AbstractTableModel {
	
	/** serialVersionUID */
    private static final long serialVersionUID = 3514815125584103L;
	
    String[] columnNames = {"Nummer",
            "Typ",
            "Flächenart",
            "PF",
            "Status",
            "montiert am",
            "Aushang-Periodizität",
            "Gebiet",
            "Strasse",
            "Haus-Nr.",
            "West-Ost",
            "Süd-Nord",
            "Drehwinkel",
            "category"};
    
    List<Object[]> cache = new ArrayList<Object[]>();
	
	public IOException exception;
	
	/**
     * A worker class to get the attributes of each feature and load 
     * them into the {@code TableModel}. The work is performed on a
     * background thread.
     */
    class TableWorker extends SwingWorker<List<Object[]>, Object[]> {
        List<FaceImpl> faces;

        /**
         * Constructor
         *
         * @param features the feature collection to be loaded into the table
         */
        TableWorker(List<FaceImpl> faces ) { 
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
            
            for(FaceImpl face : faces){
            	
            	System.out.println(face.getNummer());
            	ArrayList<Object> row = new ArrayList<Object>();
                row.add(face.getNummer());
                row.add(face.getType());
                row.add(face.getFaceFormat());
                row.add(face.getProductFormat());
                row.add(face.getStatus());
                row.add(face.getInstalled());
                row.add(face.getPosting());
                row.add(face.getArea());
                row.add(face.getStreet());
                row.add(face.getNumber());
                row.add(face.getWestOstKoordinate());
                row.add(face.getSuedNordKoordinate());
                row.add(face.getAngle());
                row.add(face.getCategory());
                
                publish( row.toArray() );
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
            System.out.println("Processing: " + from + " to " + to);
            fireTableRowsInserted( from, to );
        }        
    }
	
	TableWorker load;
	
    /**
     * Constructor
     *
     * @param features the feature collection to load into the table
     */
    public FaceTableModel(List<FaceImpl> faces){
    	this.load = new TableWorker(faces);
        load.execute();
        DefaultTableColumnModel columns = new DefaultTableColumnModel();
        
        

        //columns.addColumn(arg0);
        //this.schema = faces.getSchema();
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
        return columnNames.length;
    }
    
    public String getColumnName(int col) {
        return columnNames[col];
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
