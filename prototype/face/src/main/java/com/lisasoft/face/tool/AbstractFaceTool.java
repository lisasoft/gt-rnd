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
package com.lisasoft.face.tool;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.geotools.data.DataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.FeatureIterator;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.swing.event.MapMouseEvent;
import org.geotools.swing.tool.CursorTool;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;
import org.opengis.filter.identity.FeatureId;

import com.lisasoft.face.data.FaceDAO;
import com.lisasoft.face.data.FaceFeatureSource;
import com.lisasoft.face.map.MapComponentImpl;
import com.lisasoft.face.map.SelectedStyleFactory;

public abstract class AbstractFaceTool extends CursorTool implements
		ActionListener {

	protected static int SELECTION_BUFFER_RADIUS = 5;
	protected MapComponentImpl mapPane;
	Set<FeatureId> ids = new HashSet<FeatureId>();
	
	public AbstractFaceTool(MapComponentImpl component) {
		mapPane = component;
	}
	
	protected Long nummerFromFID(FeatureId id) {
		String numeric = id.toString().substring("Face.".length());
		try {
		return Long.parseLong(numeric);
		} catch(NumberFormatException ex) {
			System.err.println("Bollucks'd the fid conversion.");
			ex.printStackTrace(System.err);
			return null;
		}
	}

	protected Set<FeatureId> getSelectedIds(MapMouseEvent ev) throws IOException {
		ReferencedEnvelope bbox = getFilterBox(ev.getPoint());
		if(bbox == null)
			return null;
		Filter filter = SelectedStyleFactory.createBboxFilter(
				FaceFeatureSource.FACE_FEATURE_GEOMETRY_DESCRIPTOR, bbox);
		
		DataStore store = mapPane.getDataStore();
		SimpleFeatureSource feats = store.getFeatureSource(store.getTypeNames()[0]);
		FeatureIterator<SimpleFeature> it = feats.getFeatures(filter).features();
		
		//deselect features if user clicks empty space but only if control is not being held down
		if(!it.hasNext() && !ev.isControlDown()){
			ids = new HashSet<FeatureId>();
		}
		
		while(it.hasNext()) {
			SimpleFeature feat = it.next();
			if(!ev.isControlDown()){
				//using is not holding control, only add the one feature
				ids = new HashSet<FeatureId>();
				ids.add(feat.getIdentifier());
			} else if(ids.contains(feat.getIdentifier())){
				//user is holding control and feature is already selected, deselect
				ids.remove(feat.getIdentifier());
			} else {
				//user is holding control and feature is not already selected
				ids.add(feat.getIdentifier());
			}
		}
		System.out.println("Selected " + ids.size() + " features.");
		return ids;
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

