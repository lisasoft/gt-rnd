package com.lisasoft.face.table;

import javax.swing.JTable;

import com.lisasoft.face.map.MapComponentImpl;

/**
 * This is a FaceTable designed to work in collaboration with a MapComponent in order to
 * allow tabular selection of Faces.
 * 
 * @author Jody Garnett (LISAsoft)
 * @author Scott Henderson (LISAsoft)
 */
public class FaceTable extends JTable {

    /** serialVersionUID */
    private static final long serialVersionUID = 3960247151794781103L;
    
    /**
     * This is the MapComponent we are working.
     */
    private MapComponentImpl map;
    
    public FaceTable( MapComponentImpl map ){
        this.map = map;
        initi();
    }

    /**
     * Responsible for adding the table model; table header model and table selection model
     * to work with MapComponentImpl.
     */
    private void initi() {
        // SH: Please configure table here; you can inner classed or break out seperate classes
    }
    
}
