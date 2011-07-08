package com.lisasoft.face.map;

import java.util.List;

import javax.swing.JPanel;

import org.geotools.swing.JMapPane;

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
public class MapComponentImpl extends JPanel implements MapComponent {
    private static final long serialVersionUID = 152022981506025080L;

    public List<? extends Face> getFaces() {
        return null;
    }

    public void setFaces(List<? extends Face> faces) {
    }

    public List<? extends Face> getSelection() {
        return null;
    }

    public void setSelection(List<? extends Face> faces) {
    }

    public void addMapSelectionListener(SelectionListener listener) {
    }

    public void removeMapSelectionListener(SelectionListener listener) {
    }

}