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
package com.lisasoft.face.map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.data.DataStore;
import org.geotools.data.DefaultRepository;
import org.geotools.map.MapContext;
import org.geotools.swing.JMapPane;

import com.lisasoft.face.data.Face;

/**
 * This is a quick implementaion of MapComponent using an internal JMapPane for the display
 * of feature information.
 * <p>
 * Direct access is provided to the JMapPane allowing it to be easily configured by the
 * application.
 * 
 * @author Jody Garnett (LISAsoft)
 */
public class MapComponentImpl<T extends Face> extends JMapPane implements MapComponent {
    private static final long serialVersionUID = 152022981506025080L;
    private MapContext map;
    List<T> faceList;
    List<T> selectedList;
    
    MapComponentImpl() {
    	super();
    	this.faceList = new ArrayList<T>();
    	this.selectedList = new ArrayList<T>();
    }

    /**
     * TBD
     */
    private JMapPane mapPane;
    /**
     * Repository used to hold on to DataStores.
     */
    private DefaultRepository repo;

    /** Used to hold on to rasters */
    private Map<String, AbstractGridCoverage2DReader> raster;
    
    // MAP COMPONENT INTERFACE    
    public List<T> getFaces() {
    	return Collections.unmodifiableList(faceList);
    }
    
    public void setFaces(List faces) {
    	faceList.clear();
    	faceList.addAll(faces);
    }

    public List<T> getSelection() {
    	return Collections.unmodifiableList(selectedList);
    }

    public void setSelection(List faces) {
    	selectedList.clear();
    	selectedList.addAll(faces);
    }

    public void addMapSelectionListener(SelectionListener listener) {
        listenerList.add(SelectionListener.class, listener );
    }

    public void removeMapSelectionListener(SelectionListener listener) {
        listenerList.remove(SelectionListener.class, listener );
    }
    
    public void dispose() {
        for (DataStore dataStore : repo.getDataStores()) {
            try {
                dataStore.dispose();
            } catch (Throwable eek) {
                System.err.print("Error cleaning up " + dataStore + ":" + eek);
            }
        }
    }
}