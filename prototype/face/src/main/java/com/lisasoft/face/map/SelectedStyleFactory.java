package com.lisasoft.face.map;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Set;

import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.styling.ChannelSelection;
import org.geotools.styling.ContrastEnhancement;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Mark;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.SLD;
import org.geotools.styling.SLDParser;
import org.geotools.styling.SelectedChannelType;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.Symbolizer;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.identity.FeatureId;
import org.opengis.style.ContrastMethod;
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
    
    public static Filter createBboxFilter(String geometryDescriptor, ReferencedEnvelope filterBox) {
    	return ff.intersects(ff.property(geometryDescriptor), ff.literal(filterBox));
    }
    
    /**
     * This method examines the names of the sample dimensions in the provided coverage looking for
     * "red...", "green..." and "blue..." (case insensitive match). If these names are not found it
     * uses bands 1, 2, and 3 for the red, green and blue channels. It then sets up a raster
     * symbolizer and returns this wrapped in a Style.
     * 
     * @param reader
     * 
     * @return a new Style object containing a raster symbolizer set up for RGB image
     */
    public static Style createRasterStyle(AbstractGridCoverage2DReader reader) {
        GridCoverage2D cov = null;
        try {
            cov = reader.read(null);
        } catch (IOException giveUp) {
            throw new RuntimeException(giveUp);
        }
        // We need at least three bands to create an RGB style
        int numBands = cov.getNumSampleDimensions();
        if (numBands < 3) {
            // assume the first brand
            return createGreyscaleStyle(1);
        }
        // Get the names of the bands
        String[] sampleDimensionNames = new String[numBands];
        for (int i = 0; i < numBands; i++) {
            GridSampleDimension dim = cov.getSampleDimension(i);
            sampleDimensionNames[i] = dim.getDescription().toString();
        }
        final int RED = 0, GREEN = 1, BLUE = 2;
        int[] channelNum = { -1, -1, -1 };
        // We examine the band names looking for "red...", "green...", "blue...".
        // Note that the channel numbers we record are indexed from 1, not 0.
        for (int i = 0; i < numBands; i++) {
            String name = sampleDimensionNames[i].toLowerCase();
            if (name != null) {
                if (name.matches("red.*")) {
                    channelNum[RED] = i + 1;
                } else if (name.matches("green.*")) {
                    channelNum[GREEN] = i + 1;
                } else if (name.matches("blue.*")) {
                    channelNum[BLUE] = i + 1;
                }
            }
        }
        // If we didn't find named bands "red...", "green...", "blue..."
        // we fall back to using the first three bands in order
        if (channelNum[RED] < 0 || channelNum[GREEN] < 0 || channelNum[BLUE] < 0) {
            channelNum[RED] = 1;
            channelNum[GREEN] = 2;
            channelNum[BLUE] = 3;
        }
        // Now we create a RasterSymbolizer using the selected channels
        SelectedChannelType[] sct = new SelectedChannelType[cov.getNumSampleDimensions()];
        ContrastEnhancement ce = sf.contrastEnhancement(ff.literal(1.0), ContrastMethod.NORMALIZE);
        for (int i = 0; i < 3; i++) {
            sct[i] = sf.createSelectedChannelType(String.valueOf(channelNum[i]), ce);
        }
        RasterSymbolizer sym = sf.getDefaultRasterSymbolizer();
        ChannelSelection sel = sf.channelSelection(sct[RED], sct[GREEN], sct[BLUE]);
        sym.setChannelSelection(sel);

        return SLD.wrapSymbolizers(sym);
    }
    
    /**
     * Create a Style to display the specified band of the GeoTIFF image as a greyscale layer.
     * <p>
     * This method is a helper for createGreyScale() and is also called directly by the
     * displayLayers() method when the application first starts.
     * 
     * @param band the image band to use for the greyscale display
     * 
     * @return a new Style instance to render the image in greyscale
     */
    private static Style createGreyscaleStyle(int band) {
        ContrastEnhancement ce = sf.contrastEnhancement(ff.literal(1.0), ContrastMethod.NORMALIZE);
        SelectedChannelType sct = sf.createSelectedChannelType(String.valueOf(band), ce);

        RasterSymbolizer sym = sf.getDefaultRasterSymbolizer();
        ChannelSelection sel = sf.channelSelection(sct);
        sym.setChannelSelection(sel);

        return SLD.wrapSymbolizers(sym);
    }
    
    public static Style createSimpleFaceStyle() {
    		return SLD.createPointStyle("triangle",Color.BLACK,Color.YELLOW,1.0f,16);
    }
    
    public static Style createFaceStyle() {
    	Style style = null;
    	FileInputStream inputStream;
    	try {
    		inputStream = new FileInputStream(new File("./data/rotating_symbol.sld"));
    		SLDParser stylereader = new SLDParser(sf, inputStream);

    		Style styles[] = stylereader.readXML();

    		if(styles.length > 0) {
    			style = styles[0];
    		}
    	} catch (FileNotFoundException e) {
    		System.err.println("Unable to load expected SLD document.");
    		e.printStackTrace(System.err);
    	}
    	if(style ==  null)
    		style = createSimpleFaceStyle();
    	return style;
    }
}

