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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.data.DataStore;
import org.geotools.data.DefaultRepository;
import org.geotools.filter.identity.FeatureIdImpl;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContext;
import org.geotools.map.event.MapLayerEvent;
import org.geotools.map.event.MapLayerListEvent;
import org.geotools.styling.Style;
import org.geotools.swing.JMapPane;
import org.opengis.filter.identity.FeatureId;

import com.lisasoft.face.data.Face;
import com.lisasoft.face.data.FaceDAO;
import com.lisasoft.face.data.FaceDataStore;
import com.lisasoft.face.data.FaceFeatureSource;
import com.lisasoft.face.data.FaceImpl;

/**
 * This is a quick implementaion of MapComponent extending JMapPane for the display of feature
 * information.
 * <p>
 * Direct access is provided to the JMapPane allowing it to be easily configured by the application.
 * 
 * @author Jody Garnett (LISAsoft)
 */
public class MapComponentImpl extends JMapPane implements MapComponent {

	private static final long serialVersionUID = 152022981506025080L;

	/*
	 * The artifacts required for the face layer.
	 */
	FaceDAO faces;

	/**
	 * This is a map layer used to display faces provided by setFaces.
	 */
	public FeatureLayer faceLayer;

	public FaceDataStore faceStore;

	/*
	 * The artifacts required for the selection layer; it is responsible for holding onto a list.
	 */
	List<FaceImpl> selectedFaces;

	/**
	 * This is a map layer used to display faces provided by setSelection
	 */
	FeatureLayer selectedLayer;

	/**
	 * This is a DataStore provided by selectedFaces.
	 */
	FaceDataStore selectedStore;

	/**
	 * Repository used to hold on to DataStores.
	 */
	DefaultRepository repo;

	/**
	 * Used to hold on to rasters
	 */
	Map<String, AbstractGridCoverage2DReader> raster;

	/**
	 * Eating our own dog food; this internal listener redraws the map if the selection changes.
	 * <p>
	 * Either using: a selection tool, the table or a straight call to setSelection
	 */
	SelectionListener selectionRefresh = new SelectionListener() {
		public void selectionChanged() {
			// Explicit this reference is a good programming practice
			MapComponentImpl.this.repaintMap( selectedLayer );
		}
	};

	public FaceDataStore getDataStore() {
		return faceStore;
	}

	/**
	 * This is a listener used to watch the FaceDAO in order to notice if the data is edited (either
	 * by and edit tool or by the table).
	 */
	PropertyChangeListener dataChangeListener = new PropertyChangeListener() {
		public void propertyChange(PropertyChangeEvent evt) {
			FaceImpl face = (FaceImpl) evt.getSource();
			String property = evt.getPropertyName();
			if (Face.SUED_NORD_KOORDINATE.equals(property) || Face.WEST_OST_KOORDINATE.equals(property)
					|| Face.ANGLE.equals(property)) {
				// if we had seperate layers we could check if face was in the selected
				// set and just redraw what was needed.
				MapComponentImpl.this.repaintMap( faceLayer );
				MapComponentImpl.this.repaintMap( selectedLayer );
			}
		}
	};

	MapComponentImpl() throws IOException {
		super();
		this.repo = new DefaultRepository();
		this.raster = new HashMap<String, AbstractGridCoverage2DReader>();
		System.out.println("Initialising empty FaceDAO");
		this.faces = new FaceDAO(new ArrayList<FaceImpl>(0));
		this.addMapSelectionListener(selectionRefresh);
	}

	@Override
	public void setMapContext(MapContext context) {
		super.setMapContext(context);
		updateFaceLayers();
	}

	/**
	 * Ask the map to repaint the following layer.
	 */
	public void repaintMap(Layer layer){
		MapLayerEvent event = new MapLayerEvent( layer, MapLayerEvent.DATA_CHANGED );
		MapLayerListEvent event2 = new MapLayerListEvent( getMapContext(), layer, -1, event );
		MapComponentImpl.this.layerMoved( event2 );
	}

	/**
	 * This method should be called whenever the faces object is replaced.  Otherwise the 
	 * layers rendered will not reflect the correct dataset.
	 */
	private void updateFaceLayers() {
		if(this.faceLayer != null) {
			this.getMapContext().removeLayer(faceLayer);
			this.faceLayer = null;
		}
		if(this.selectedLayer != null) {
			this.getMapContext().removeLayer(selectedLayer);
			this.selectedLayer = null;
		}
		if(this.faceStore != null) {
			this.faceStore.dispose();
			this.faceStore = null;
		}
		if(this.selectedStore != null) {
			this.selectedStore.dispose();
			this.selectedStore = null;
		}
		try {
			this.faceStore = new FaceDataStore(this.faces);
			this.selectedStore = new FaceDataStore(this.faces);
			faceLayer = new FeatureLayer(
					this.faceStore.getFeatureSource(this.faceStore
							.getTypeNames()[0]),
							SelectedStyleFactory.createSimpleFaceStyle());
			this.getMapContext().addLayer(faceLayer);
			Style selectedStyle = null;
			if(this.selectedFaces != null && this.selectedFaces.size() > 0) {
				selectedStyle = SelectedStyleFactory.createSelectedStyle(
						getFidList(selectedFaces), 
						FaceFeatureSource.FACE_FEATURE_GEOMETRY_DESCRIPTOR);
			} else {
				selectedStyle = SelectedStyleFactory.createExcludeStyle();
			}
			selectedLayer = new FeatureLayer(
					this.selectedStore.getFeatureSource(this.faceStore
							.getTypeNames()[0]), selectedStyle);
			this.getMapContext().addLayer(selectedLayer);
		} catch (IOException ex) {
			System.err.println("Failure to create the expected face layers.");
			ex.printStackTrace(System.err);
		}
	}

	private Set<FeatureId> getFidList(List<FaceImpl> faces) {
		Set<FeatureId> fids = new HashSet<FeatureId>();
		for(FaceImpl face : faces) {
			FeatureId fid = new FeatureIdImpl("Face." + face.getNumber());
			System.out.println("Fiding up " + fid);
		}
		return fids;
	}

	/**
	 * Retrieves a list of all face of all Faces.
	 */
	public List<FaceImpl> getFaces() {
		return (List<FaceImpl>) (faces != null ? faces.contents() : Collections.emptyList());
	}

	/**
	 * This method updates the contents of our FaceDAO. 
	 * It does so by creating a new FaceDAO object.  This means that listeners to the old
	 * object will be lost (with the exception of this object which re-registers itself).
	 * It also replaces the map layers and feature stores to use the new dataset.
	 */
	public void setFaces(List<FaceImpl> faces) {
		try {
			System.out.println("Receiving " + faces.size() + " faces.");
			this.faces = new FaceDAO(faces);
			this.faces.addPropertyChangeListener( dataChangeListener );
			List<FaceImpl> list = Collections.emptyList();

			setSelection(list);
			updateFaceLayers();
		} catch (IOException ex) {
			System.err.println("Error accepting faces.");
			ex.printStackTrace(System.err);
			this.faces = null;
		}
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		super.addPropertyChangeListener(listener);
		if(this.faces != null)
			faces.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		super.removePropertyChangeListener(listener);
		if(this.faces != null)
			faces.removePropertyChangeListener(listener);
	}

	/**
	 * Returns a thread-safe list of all selected Face objects, or an empty list.
	 */
	public List<FaceImpl> getSelection() {
		return (List<FaceImpl>) (selectedFaces != null ? 
				new CopyOnWriteArrayList<FaceImpl>(selectedFaces) : 
					Collections.emptyList());
	}

	/**
	 * Informs the map component that something outside the control of 
	 * the map has changed the selection, and the map must respond.
	 * @param ids
	 */
	public void setSelection(Set<FeatureId> ids) {
		List<FaceImpl> selections = new ArrayList<FaceImpl>(ids.size());
		for(FeatureId fid : ids) {
			if(fid == null)
				continue;
			String fidstring = fid.getID().substring("Face.".length());
			long id = Long.parseLong(fidstring);
			selections.add(faces.lookup(id));
		}
		setSelection(selections, ids);
	}

	/**
	 * Informs the map component that something outside the control of 
	 * the map has changed the selection, and the map must respond.
	 * @param faces 
	 */
	public void setSelection(List<FaceImpl> faces) {
		Set<FeatureId> ids = new HashSet<FeatureId>();
		for(FaceImpl face : faces) {
			FeatureId id = new FeatureIdImpl("Face." + face.getNummer());
			ids.add(id);
		}
		setSelection(faces, ids);
	}

	/**
	 * behaves the same as setSelection(List<FaceImpl> faces) but the difference being
	 * it does not circle the features as highlighted, it only selects the feature
	 * in the table.
	 * @param faces 
	 */
	public void setSelectionWithoutHighlighting(List<FaceImpl> faces) {
		Set<FeatureId> ids = new HashSet<FeatureId>();
		setSelection(faces, ids);
	}

	/**
	 * Internal method to update the selected layer style and selectedFaces in one
	 * atomic action.
	 * @param faces
	 * @param ids
	 */
	private void setSelection(List<FaceImpl> faces, Set<FeatureId> ids) {
		this.selectedFaces = faces;
		this.selectedLayer.setStyle(SelectedStyleFactory.createSelectedStyle(
				ids, FaceFeatureSource.FACE_FEATURE_GEOMETRY_DESCRIPTOR));
		
		repaint();
		fireMapSelection();		
	}

	/**
	 * This is a really simple event notification.
	 */
	protected void fireMapSelection() {
		for (SelectionListener listener : listenerList.getListeners(SelectionListener.class)) {
			try {
				listener.selectionChanged(); // ping!!
			} catch (Throwable t) {
				System.err.println("Listener " + listener
						+ " was unable to process map selection notification:" + t);
				t.printStackTrace(System.err);
			}
		}
	}

	/**
	 * Register interest in knowing when something has been selected on the map.
	 */
	public void addMapSelectionListener(SelectionListener listener) {
		listenerList.add(SelectionListener.class, listener);
	}

	/**
	 * Register disinterest in knowing when something has been selected on the map.
	 */
	public void removeMapSelectionListener(SelectionListener listener) {
		listenerList.remove(SelectionListener.class, listener);
	}

	public void dispose() {
		this.faceStore.dispose();
		this.selectedStore.dispose();
		for (DataStore dataStore : repo.getDataStores()) {
			try {
				dataStore.dispose();
			} catch (Throwable eek) {
				System.err.print("Error cleaning up " + dataStore + ":" + eek);
			}
		}
	}
}