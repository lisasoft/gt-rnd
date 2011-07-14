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
package com.lisasoft.face.Listeners;

import java.util.HashSet;
import java.util.Set;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.geotools.filter.identity.FeatureIdImpl;
import org.opengis.filter.identity.FeatureId;

import com.lisasoft.face.map.MapComponent;
import com.lisasoft.face.map.MapComponentImpl;
import com.lisasoft.face.table.FaceTable;

/**
 * This is a SelectionListener designed to work in collaboration with FaceTable in order to
 * allow tabular selection of Faces.
 *
 * @author Scott Henderson (LISAsoft)
 */
public class SelectionListener 
		implements ListSelectionListener, 
		MapComponent.SelectionListener {
	
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
            	FeatureId id = getFeatureId(sel[i]);
            	if(id != null)
            		ids.add(id);
            }
            map.setSelection(ids);
        }
    }
    
    public FeatureId getFeatureId(int row){
    	Object obj = table.getModel().getValueAt(row, 0);
    	if(obj instanceof Long) {
    		Long nummer = (Long)obj;
    		FeatureId id = new FeatureIdImpl("Face." + nummer);
    		return id;
    	}
    	return null;
    }

	public void selectionChanged() {
//		map.getSelection();
	
	}
}