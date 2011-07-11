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