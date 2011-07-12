package com.lisasoft.face.tool;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;
import java.util.Set;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.FeatureLayer;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Mark;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.swing.JMapPane;
import org.geotools.swing.event.MapMouseEvent;
import org.geotools.swing.tool.CursorTool;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.identity.FeatureId;
import org.opengis.style.Stroke;

import com.lisasoft.face.SelectedStyleFactory;

public class FaceSelectTool extends CursorTool implements ActionListener {
	
	JMapPane mapPane;
    private SimpleFeatureCollection faces;
    private FeatureLayer selectedFaceLayer;

	public FaceSelectTool(JMapPane pane, SimpleFeatureCollection faces, FeatureLayer selecetdFaceLayer) {
		this.mapPane = pane;
		this.faces = faces;
		this.selectedFaceLayer = selecetdFaceLayer;
	}
	public void actionPerformed(ActionEvent e) {
		mapPane.setCursorTool(this);
	}

	public void onMouseClicked(MapMouseEvent ev) {
		selectFeatures(ev);
	}

    private static int SELECTION_BUFFER_WIDTH = 10;
    void selectFeatures(MapMouseEvent ev) {
    	/*
    	 * Create a small selection region.
    	 */
    	java.awt.Point screenPos = ev.getPoint();
    	Rectangle screenRect = new Rectangle(
    			screenPos.x-(SELECTION_BUFFER_WIDTH/2), 
    			screenPos.y-(SELECTION_BUFFER_WIDTH/2), 
    			SELECTION_BUFFER_WIDTH, SELECTION_BUFFER_WIDTH);
    	
    	/*
    	 * Transform the screen rectangle to map coordinates.
    	 */
    	AffineTransform screenToWorld = mapPane.getScreenToWorldTransform();
    	Rectangle2D worldRect = screenToWorld.createTransformedShape(screenRect).getBounds2D();
    	ReferencedEnvelope bbox = new ReferencedEnvelope(
    			worldRect, mapPane.getMapContext().getCoordinateReferenceSystem());
    	
    	Name geometryDescriptor = faces.getSchema().getGeometryDescriptor().getName();
    	try {
    		ReferencedEnvelope filterBox = bbox.transform(faces.getSchema().getCoordinateReferenceSystem(), true);

    		/*
    		 * Create a Filter selecting the from the bounding box.
    		 */
    		Filter filter = SelectedStyleFactory.createBboxFilter(geometryDescriptor, filterBox);
    		SimpleFeatureCollection selectedFeatures = faces.subCollection(filter);
    		SimpleFeatureIterator iter = selectedFeatures.features();
    		Set<FeatureId> ids = new HashSet<FeatureId>();
    		try {
    			while(iter.hasNext()) {
    				SimpleFeature feature = iter.next();
    				ids.add(feature.getIdentifier());
    				System.out.println("ID - " + feature.getIdentifier());
    			}
    		} finally {
    			iter.close();
    		}
    		
    		System.out.println("Selected " + ids.size() + " features.");
    		Style style = SelectedStyleFactory.createSelectedStyle(ids, geometryDescriptor.toString());
    		selectedFaceLayer.setStyle(style);
   	        mapPane.repaint();

    	} catch(Exception ex) {
    		ex.printStackTrace();
    		return;
    	}
    }

    

}
