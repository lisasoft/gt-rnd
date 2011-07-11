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

import com.lisasoft.face.data.Face;

/**
 * The following interface is used to facilitate interaction between the MapComponents
 * and the rest of the application.
 * <p>
 * It is assumed the rest of the system is using a DAO to access the database and is using
 * POJOs at the application layer.
 * <p>
 * GIS systems are typically performance based; offering their own data abstract Feature
 * rather than Objects. Feature is a dynamic type system (determined at runtime); with the
 * type system consisting of a FeatureType. Because this is a dynamic type system it is slihgtly
 * easier to have a "feature relational mapper" than a Java "object relational mapper".
 */
public interface MapComponent {
    
    public interface SelectionListener {
        public void selectionChanged();
    }
    /**
    * Returns the list of faces currently displayed in the GIS map component.
    * @return the list of faces currently displayed in the GIS map component
    */
    public List<? extends Face> getFaces();

    /**
    * Sets the list of faces to be displayed in the GIS map component.
    * 
    * @param faces the list of faces to be displayed in the GIS map component
    */
    public void setFaces(List<? extends Face> faces);

    /**
    * Returns the currently selected faces in the GIS map component.
    * 
    * @return the currently selected faces in the GIS map component
    */
    public List<? extends Face> getSelection();

    /**
    * Sets the currently selected faces in the GIS map component.
    * 
    * @param faces the faces to be selected in the GIS map component
    */
    public void setSelection(List<? extends Face> faces);

    /**
    * Registers a listener that is notified whenever the user changes the selection in the GIS map component by
    * clicking (or ctrl clicking) on a face in the map.
    * @param listener the listener to be added
    */
    public void addMapSelectionListener(MapComponent.SelectionListener listener);

    /**
    * Deregisters a listener.
    * @param listener the listener to be deregistered.
    */
    public void removeMapSelectionListener(MapComponent.SelectionListener listener);

}