package com.lisasoft.face.Listeners;

import java.util.HashSet;
import java.util.Set;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.opengis.filter.identity.FeatureId;

import com.lisasoft.face.map.MapComponentImpl;
import com.lisasoft.face.table.FaceTable;

public class SelectionListener implements ListSelectionListener {
	
	private FaceTable table;
	
	private MapComponentImpl map;
	
	public SelectionListener(FaceTable table, MapComponentImpl map){
		this.table = table;
		this.map = map;
	}

    public void valueChanged(ListSelectionEvent e) {
        if (e.getSource() == table.getSelectionModel()
              && table.getRowSelectionAllowed()) {
            
        	//get the selected rows
            int[] sel = table.getSelectedRows();
            
            Set<FeatureId> ids = new HashSet<FeatureId>();
            
            for(int i = 0; i < sel.length; i++){
            	//ids.add(getFeatureId(sel[i]));
            }
            
            //Style style = SelectedStyleFactory.createSelectedStyle(ids, FACE_GEOMETRY_NAME);
    		//selectedFaceLayer.setStyle(style);
   	        map.repaint();
            
        }
    }
    
    public FeatureId getFeatureId(int row){
    	
        /*SimpleFeatureIterator iter = table.getfeatures();
        SimpleFeature feature;
        
		try {	
			for (int i = 0; i < row; i++){
				feature = iter.next();
			}
			feature = iter.next();    			
			
		} finally {
			iter.close();
		}*/
    	return null;
    }
}