package com.lisasoft.face;

import javax.swing.JFrame;
import javax.swing.JTable;

import com.lisasoft.face.data.FaceDAO;
import com.lisasoft.face.map.MapComponent;
import com.lisasoft.face.map.MapComponentImpl;
import com.lisasoft.face.table.FaceTable;

/**
 * This is a prototype application *just* showing how to integrate a MapComponent with an existing
 * application.
 * <p>
 * As such the details of this application are not all that interesting; they do serve to illustrate
 * how to:
 * <ol>
 * <li>set up a MapContent (this is used as the background for the MapComponent)</li>
 * <li>set up a MapComponent (this is actually a simple JPanel consisting of a JMapPane</li>
 * <li>set up a toolbar using some of the actions available for controlling MapComponent</li>
 * </ul> * In all cases this is straight forward application of the components provided by GeoTools.
 * <p>
 * Here is the interesting bit:
 * <ul>
 * <li>set up a MapComponentTable (actually a small JPanel consisting of a JTable working against
 * the MapComponent table model)</li>
 * <li>Custom table model; just backed by the Faces provided to MapComponentTable</li>
 * <li>Custom tool to "select" Faces in the MapComponent; this will update both update an internal
 * *filter* used to display selected faces; and update a list selection model published for use with
 * MapComponentTable.</li>
 * <li>Custom DataStore used to present the Faces provided to MapComponent to the GeoTools rendering
 * system. This is used by *two* layers. One layer to display the sites; and a second to display the
 * selection (These two lays are added to the MapContent).</li>
 * </ul>>
 * 
 * Implementation Notes:
 * <ul>
 * <li>SH: is creating the layout of this form using Eclipse 3.7 window builder tools (this is not
 * really important or interesting; just FYI)</li>
 * </ul>
 * 
 * @author Scott Henderson (LISASoft)
 * @author Jody Garnett (LISASoft)
 */
public class FacePrototype extends JFrame {

    /** serialVersionUID */
    private static final long serialVersionUID = 3738236346475642092L;
    
    /**
     * Change this to match the EPSG code for your spatial reference system.
     * 
     * See: http://spatialreference.org/ref/?search=ch1903
     */
    public static String EPSG_CODE = "EPSG:2056";
    
    /**
     * Used to load data; and provde access.
     */
    FaceDAO data;
    
    /**
     * This is the map component; it handles all "GIS" functionality
     * and is just treated as a display of Face POJOs similar to a table.
     */
    MapComponentImpl map;
    
    /**
     * Uses a JTableModel and ListSelectionModel.
     */
    FaceTable table;
    
}