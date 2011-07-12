package com.lisasoft.face;

import java.awt.Color;
import java.util.Set;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Mark;
import org.geotools.styling.Rule;
import org.geotools.styling.SLD;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.Symbolizer;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.identity.FeatureId;
import org.opengis.style.PointSymbolizer;
import org.opengis.style.Stroke;

/**
 * This factory creates styles used to represent selected faces.  It is currently 
 * just a collection of static helper methods and parameters.
 * 
 * @author mleslie
 */
public class SelectedStyleFactory {
    /**
     * Used to create GeoTools styles; based on OGC Style Layer Descriptor specification.
     */
    private static StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);

    /**
     * Used to create GeoTools filters; to query data.
     */
    private static FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);

    /**
     * The fill colour used in the selection ring.
     */
    private static Color SELECTED_FILL_COLOR = Color.YELLOW;
    
    /**
     * The opacity of the fill applied to the selection ring, (1.0 being completely opaque).
     */
    private static double SELECTED_FILL_OPACITY = 0.0;
    
    /**
     * The colour of the circle used for the selection ring.
     */
    private static Color SELECTED_STROKE_COLOR = new Color(255, 0, 255);
    
    /**
     * The width of the circle drawn for the selection ring.
     */
    private static double SELECTED_STROKE_WIDTH = 2.5;
    
    /**
     * The overall diameter of the selection ring.
     */
    private static double SELECTED_POINT_SIZE = 20.0;
    
    /**
     * Creates a selected style, using the standard attributes, against the provided set of ids.
     * 
     * @param ids Set of Feature IDs of selected items.
     * @param geometryDescriptor the name of the geometry attribute to be drawn.
     * @return Style that will render a ring around all provided ids
     */
    public static Style createSelectedStyle(Set<FeatureId> ids, String geometryDescriptor) {
    	/*
    	 * First create the normal selection styler.
    	 */
    	org.geotools.styling.Symbolizer symbolizer = null;
    	org.opengis.style.Fill fill = sf.createFill(ff.literal(SELECTED_FILL_COLOR), ff.literal(SELECTED_FILL_OPACITY));
    	Stroke stroke = sf.createStroke(ff.literal(SELECTED_STROKE_COLOR), ff.literal(SELECTED_STROKE_WIDTH));
    	Mark mark = sf.getCircleMark();
    	mark.setFill(fill);
    	mark.setStroke(stroke);
    	
    	org.geotools.styling.Graphic graphic = sf.createDefaultGraphic();
    	graphic.graphicalSymbols().clear();
    	graphic.graphicalSymbols().add(mark);
    	graphic.setSize(ff.literal(SELECTED_POINT_SIZE));
    	
    	symbolizer = sf.createPointSymbolizer(graphic, geometryDescriptor);
    	
    	
    	org.geotools.styling.Rule selectedRule = sf.createRule();
    	selectedRule.symbolizers().add(symbolizer);
    	if(ids == null || ids.isEmpty()) {
    		System.out.println("Empty id set found.  Creating EXCLUDE filter.");
    		selectedRule.setFilter(Filter.EXCLUDE);
    	} else {
    		System.out.println("" + ids.size() + " ids found.  Creating filter.");
    		selectedRule.setFilter(ff.id(ids));
    	}
    	
    	FeatureTypeStyle fts = sf.createFeatureTypeStyle();
    	fts.rules().add(selectedRule);
    	
    	/*
    	 * Next the other styler.  This has been left for debugging goodness, but isn't really 
    	 * appropriate for a selection layer.
    	 */
    	/*
    	Mark otherMark = sf.getCircleMark();
    	org.opengis.style.Fill otherFill = sf.createFill(ff.literal(SELECTED_FILL_COLOR), ff.literal(0.0));
    	mark.setStroke(stroke);
    	mark.setFill(otherFill);
    	
    	org.geotools.styling.Graphic otherGraphic = sf.createDefaultGraphic();
    	otherGraphic.graphicalSymbols().clear();
    	otherGraphic.graphicalSymbols().add(otherMark);
    	otherGraphic.setSize(ff.literal(SELECTED_POINT_SIZE));
    	
    	org.geotools.styling.Symbolizer otherSymbolizer = 
    		sf.createPointSymbolizer(graphic, faces.getSchema().getGeometryDescriptor().getName().toString());
    	org.geotools.styling.Rule otherRule = sf.createRule();
    	otherRule.setElseFilter(true);
    	otherRule.symbolizers().add(otherSymbolizer);
    	
    	fts.rules().add(otherRule);
    	*/
    	
    	Style style = sf.createStyle();
    	style.featureTypeStyles().add(fts);
    	return style;
    }
    
    /**
     * Used to generate a style that will render nothing.  This is accomplished by creating an exclusion filter
     * that will pass no features.  Used on the selection layer before any selection (even empty selection) 
     * can take place.
     * 
     * @return Style that will render nothing on everything
     */
    public static Style createExcludeStyle() {
    	org.geotools.styling.Style basicStyle = SLD.createPointStyle("circle", Color.BLACK, Color.WHITE, 0.0f, 1);
    	Symbolizer sym = SLD.pointSymbolizer(basicStyle);
    	basicStyle.featureTypeStyles().clear();
    	
    	Rule rule = sf.createRule();
    	rule.setFilter(Filter.EXCLUDE);
    	rule.symbolizers().add(sym);
    	
    	FeatureTypeStyle fts = sf.createFeatureTypeStyle();
    	fts.rules().add(rule);
    	
    	basicStyle.featureTypeStyles().add(fts);
    	return basicStyle;
    }
    
    public static Filter createBboxFilter(Name geometryDescriptor, ReferencedEnvelope filterBox) {
    	return ff.intersects(ff.property(geometryDescriptor), ff.literal(filterBox));
    }
}
