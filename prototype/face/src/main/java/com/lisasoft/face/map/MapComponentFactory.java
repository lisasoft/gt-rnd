package com.lisasoft.face.map;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.data.DataStore;
import org.geotools.data.DefaultRepository;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.map.DefaultMapContext;
import org.geotools.map.FeatureLayer;
import org.geotools.map.GridReaderLayer;
import org.geotools.map.MapContext;
import org.geotools.referencing.CRS;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.styling.ChannelSelection;
import org.geotools.styling.ContrastEnhancement;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.SLD;
import org.geotools.styling.SelectedChannelType;
import org.geotools.styling.Style;
import org.geotools.swing.JMapPane;
import org.geotools.swing.action.InfoAction;
import org.geotools.swing.action.PanAction;
import org.geotools.swing.action.ZoomInAction;
import org.geotools.swing.action.ZoomOutAction;
import org.geotools.swing.table.FeatureCollectionTableModel;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.identity.FeatureId;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.style.ContrastMethod;

import com.lisasoft.face.data.Face;
import com.lisasoft.face.data.FaceImpl;
import com.lisasoft.face.table.FaceTable;
import com.lisasoft.face.tool.FaceSelectTool;
import com.lisasoft.face.tool.MapSelectionTool;

public class MapComponentFactory {
    private File dataRoot = new File(".");
    
    public MapComponentFactory() {
	}
    
	public MapComponentImpl buildMapComponent() throws IOException {
		MapComponentImpl component = new MapComponentImpl();
		loadShapefileData(component.repo);
		loadRaster(component.raster);
		MapContext map = createMap(component.repo, component.raster);
        component.setRenderer(new StreamingRenderer());
        component.setMapContext(map);
        component.setSize(800, 500);
		
		return component;
	}
	
	public MapComponentImpl buildMapComponent(JToolBar toolBar) 
			throws IOException {
		MapComponentImpl component = buildMapComponent();
		
        toolBar.setOrientation(JToolBar.HORIZONTAL);
        toolBar.setFloatable(false);

        JButton zoomInBtn = new JButton(new ZoomInAction(component));
        toolBar.add(zoomInBtn);

        JButton zoomOutBtn = new JButton(new ZoomOutAction(component));
        toolBar.add(zoomOutBtn);
        
        JButton panBtn = new JButton(new PanAction(component));
        toolBar.add(panBtn);
        
        JButton infoBtn = new JButton(new InfoAction(component));
        toolBar.add(infoBtn);
        
        JButton selectButton = new JButton("Select");
        toolBar.add(selectButton);
        
        selectButton.addActionListener(new MapSelectionTool(component));

        toolBar.setSize(800, 100);
		
		return component;
	}
	
	/**
     * Displays a GeoTIFF file overlaid with a Shapefile
     * 
     * @param rasterFile the GeoTIFF file
     * @param shpFile the Shapefile
     */
    private MapContext createMap(DefaultRepository repo,
            Map<String, AbstractGridCoverage2DReader> raster) {

        // Set up a MapContext with the two layers
        final MapContext map = new DefaultMapContext();

        // use rasters as "basemap"
        for (Entry<String, AbstractGridCoverage2DReader> entry : raster.entrySet()) {
            // Initially display the raster in greyscale using the
            // data from the first image band
            String name = entry.getKey();
            AbstractGridCoverage2DReader reader = entry.getValue();

            Style style = SelectedStyleFactory.createRasterStyle(reader);
            GridReaderLayer layer = new GridReaderLayer(reader, style);
            if (reader.getInfo() != null && reader.getInfo().getTitle() != null) {
                layer.setTitle(reader.getInfo().getTitle());
            }
            map.addLayer(layer);
        }
        // add shapefiles on top
        for (DataStore dataStore : repo.getDataStores()) {

            try {
                for (String typeName : dataStore.getTypeNames()) {
                    SimpleFeatureSource featureSource = dataStore.getFeatureSource(typeName);
                    
                    Style style = SLD.createPolygonStyle(Color.RED, null, 0.0f);
                    FeatureLayer layer = new FeatureLayer(featureSource, style);

                    if (featureSource.getInfo() != null
                            && featureSource.getInfo().getTitle() != null) {
                        layer.setTitle(featureSource.getInfo().getTitle());
                    }

                    map.addLayer(layer);
                }
            } catch (IOException e) {
                System.err.print("Could not load " + dataStore);
            }
        }

        // configure map
        map.setTitle("Prototype");

        return map;

    }
    
    private void loadShapefileData(DefaultRepository repo) {
        if (dataRoot.exists() && dataRoot.isDirectory()) {
            // check for shapefiles
            //
            File[] shapefiles = dataRoot.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return name.toUpperCase().endsWith(".SHP");
                }
            });
            for (File shp : shapefiles) {
                try {
                    File prj = checkPRJ(shp);

                    FileDataStore dataStore = FileDataStoreFinder.getDataStore(shp);
                    if (dataStore != null) {
                        for (String typeName : dataStore.getTypeNames()) {
                            repo.register(typeName, dataStore);
                        }
                    }
                } catch (IOException eek) {
                    System.err.println("Unable to load shapefile " + shp + ":" + eek);
                    eek.printStackTrace(System.err);
                }
            }
        }
    }
    
    private void loadRaster(Map<String, AbstractGridCoverage2DReader> raster) {
        if (dataRoot.exists() && dataRoot.isDirectory()) {
            // check for geotiff files
            File[] tiffFiles = dataRoot.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return name.toUpperCase().endsWith(".TIF")
                            || name.toUpperCase().endsWith(".TIFF");
                }
            });
            for (File tif : tiffFiles) {
                try {
                    // create PRJ files for .TIF images that do not have one
                    File prj = checkPRJ(tif);
                    
                    AbstractGridFormat format = GridFormatFinder.findFormat(tif);
                    AbstractGridCoverage2DReader reader = format.getReader(tif);
                    if (reader == null) {
                        System.err.println("Unable to load " + tif);
                        continue;
                    }
                    String fileName = tif.getName();
                    String name = fileName.substring(0, fileName.lastIndexOf(".") - 1);
                    raster.put(name, reader);
                } catch (Throwable eek) {
                    System.err.println("Unable to load " + tif + ":" + eek);
                    eek.printStackTrace(System.err);
                }
            }
        }
    }

    private File checkPRJ(File targetFile) {
        // why does Java not have a method to make this easy ...
        String base = targetFile.getName().substring(0, targetFile.getName().lastIndexOf("."));

        // this is a world plus image file
        File prj = new File(targetFile.getParentFile(), base + ".prj");
        if (!prj.exists()) {
            FileWriter writer = null;

            // prj not provided going to assume EPSG:4326 and write one out
            try {
                // true is ask for easting / northing order to match the data
                CoordinateReferenceSystem crs = CRS.decode("EPSG:4326",true);
                String wkt = crs.toWKT();

                writer = new FileWriter(prj);
                writer.append(wkt);
            } catch (NoSuchAuthorityCodeException e) {
                System.out.println("Did you an include an EPSG jar on the CLASSPATH?");
            } catch (FactoryException e) {
                System.out.println("Did you an include an EPSG jar on the CLASSPATH?");
            } catch (IOException e) {
                System.out.println("Unable to generate " + prj);
            } finally {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException e) {
                        System.out.println("Trouble closing " + prj);
                    }
                }
            }
        }
        return prj;
    }
}
