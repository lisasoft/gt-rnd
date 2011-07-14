package com.lisasoft.face.tool;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.geotools.data.DataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.store.ContentFeatureCollection;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.FeatureIterator;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.swing.event.MapMouseEvent;
import org.geotools.swing.tool.CursorTool;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;
import org.opengis.filter.identity.FeatureId;

import com.lisasoft.face.data.FaceDAO;
import com.lisasoft.face.data.FaceDataStore;
import com.lisasoft.face.data.FaceFeatureSource;
import com.lisasoft.face.map.MapComponentImpl;
import com.lisasoft.face.map.SelectedStyleFactory;

public class MapSelectionTool 
		extends CursorTool 
		implements ActionListener {
	
	private static int SELECTION_BUFFER_RADIUS = 5;
	private MapComponentImpl mapPane;
	
	public MapSelectionTool(MapComponentImpl component) {
		mapPane = component;
	}

	public void actionPerformed(ActionEvent e) {
		mapPane.setCursorTool(this);
	}
	
	public void onMouseClicked(MapMouseEvent ev) {
		ReferencedEnvelope bbox = getFilterBox(ev.getPoint());
		if(bbox == null)
			return;
		Filter filter = SelectedStyleFactory.createBboxFilter(
				FaceFeatureSource.FACE_FEATURE_GEOMETRY_DESCRIPTOR, bbox);
//		SimpleFeatureCollection selectedFeatures = 
//			mapPane.faceLayer.
		Set<FeatureId> ids = new HashSet<FeatureId>();
		try {
			DataStore store = mapPane.getDataStore();
			SimpleFeatureSource feats = store.getFeatureSource(store.getTypeNames()[0]);
			FeatureIterator<SimpleFeature> it = feats.getFeatures(filter).features();
			while(it.hasNext()) {
				SimpleFeature feat = it.next();
				ids.add(feat.getIdentifier());
			}
			System.out.println("Selected " + ids.size() + " features.");
			mapPane.setSelection(ids);

		} catch(IOException ex) {
			System.err.println("Error determining selection.");
			ex.printStackTrace(System.err);
		}
		
	}
	
	private ReferencedEnvelope getFilterBox(Point pnt) {
		
    	/*
    	 * Create a small selection region.
    	 */
    	Rectangle screenRect = new Rectangle(
    			pnt.x-(SELECTION_BUFFER_RADIUS), 
    			pnt.y-(SELECTION_BUFFER_RADIUS), 
    			SELECTION_BUFFER_RADIUS*2, SELECTION_BUFFER_RADIUS*2);
    	
    	/*
    	 * Transform the screen rectangle to map coordinates.
    	 */
    	try {
    		AffineTransform screenToWorld = mapPane.getScreenToWorldTransform();
    		Rectangle2D worldRect = screenToWorld.createTransformedShape(screenRect).getBounds2D();
    		ReferencedEnvelope bbox = new ReferencedEnvelope(
    				worldRect, mapPane.getMapContext().getCoordinateReferenceSystem());
    		ReferencedEnvelope result = bbox.transform(CRS.decode(FaceDAO.EPSG_CODE), true);
    		return result;
    	} catch(Exception ex) {
    		System.err.println("Unable to convert to selection point to data coordinates.");
    		ex.printStackTrace(System.err);
    		return null;
    	}
    	
	}
}
