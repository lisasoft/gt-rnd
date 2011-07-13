package com.lisasoft.face.table;

import java.awt.Dimension;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import org.geotools.swing.table.FeatureCollectionTableModel;

import com.lisasoft.face.Listeners.SelectionListener;
import com.lisasoft.face.data.Face;
import com.lisasoft.face.map.MapComponentImpl;

/**
 * This is a FaceTable designed to work in collaboration with a MapComponent in order to
 * allow tabular selection of Faces.
 * 
 * @author Jody Garnett (LISAsoft)
 * @author Scott Henderson (LISAsoft)
 */
public class FaceTable extends JTable {

    /** serialVersionUID */
    private static final long serialVersionUID = 3960247151794781103L;
    
    /**
     * This is the MapComponent we are working.
     */
    private MapComponentImpl map;
    
    public FaceTable( MapComponentImpl map ){
        this.map = map;
        initi();
    }

    /**
     * Responsible for adding the table model; table header model and table selection model
     * to work with MapComponentImpl.
     */
    private void initi() {
        // SH: Please configure table here; you can inner classed or break out seperate classes
    	System.out.println(map.getFaces().size());
    	if (map.getFaces() != null) {
            for(Object feat : map.getFaces()){
            	System.out.println("feat in initi: " + feat.toString());
            }
            //FeatureCollectionTableModel model = new FeatureCollectionTableModel(map.getFaces());
    		FaceTableModel model = new FaceTableModel(map.getFaces());
            
            this.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
            this.setPreferredScrollableViewportSize(new Dimension(800, 100));
            this.setModel(model);            
            
            SelectionListener listener = new SelectionListener(this, map);
            this.getSelectionModel().addListSelectionListener(listener);
            this.getColumnModel().getSelectionModel()
                .addListSelectionListener(listener);          
            
        }
    }  
    
}
