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

import java.awt.AWTException;
import java.awt.AWTKeyStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.FocusTraversalPolicy;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.ImageCapabilities;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.MenuComponent;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.dnd.DropTarget;
import java.awt.event.ActionListener;
import java.awt.event.ComponentListener;
import java.awt.event.ContainerListener;
import java.awt.event.FocusListener;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyListener;
import java.awt.event.InputMethodListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.im.InputContext;
import java.awt.im.InputMethodRequests;
import java.awt.image.ColorModel;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.awt.image.RenderedImage;
import java.awt.image.VolatileImage;
import java.awt.peer.ComponentPeer;
import java.beans.PropertyChangeListener;
import java.beans.VetoableChangeListener;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EventListener;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.Map;

import javax.accessibility.AccessibleContext;
import javax.swing.InputVerifier;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRootPane;
import javax.swing.JToolTip;
import javax.swing.KeyStroke;
import javax.swing.TransferHandler;
import javax.swing.border.Border;
import javax.swing.event.AncestorListener;
import javax.swing.event.EventListenerList;
import javax.swing.plaf.PanelUI;

import org.geotools.map.MapContext;
import org.geotools.map.event.MapBoundsEvent;
import org.geotools.map.event.MapLayerListEvent;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.data.DataStore;
import org.geotools.data.DefaultRepository;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.renderer.GTRenderer;
import org.geotools.swing.JMapPane;
import org.geotools.swing.MapLayerTable;
import org.geotools.swing.event.MapMouseListener;
import org.geotools.swing.event.MapPaneListener;
import org.geotools.swing.tool.CursorTool;
import org.opengis.geometry.Envelope;

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
    
    MapComponentImpl(JMapPane mapPane) {
    	this.mapPane = mapPane;
    	this.map = mapPane.getMapContext();
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