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
package com.lisasoft.face.data;

import java.beans.PropertyChangeListener;

/**
 * This is an example plain old java object used for this prototype.
 */
public interface Face {

    public static final String CATEGORY = "category";

    public static final String SUED_NORD_KOORDINATE = "suedNordKoordinate";

    public static final String WEST_OST_KOORDINATE = "westOstKoordinate";
    
    public static final String ANGLE = "angle";
    /**
     * @return a Number, which identifies the face and is displayed? in the Gis Component
     */
    public java.lang.Long getNummer();

    /**
     * @return the West- / East Coordinates of the Face shown in Gis Component
     */
    public java.math.BigDecimal getWestOstKoordinate();

    /**
     * @param westOstKoordinate the West- / East Coordinates of the Face shown in Gis Component
     */
    public void setWestOstKoordinate(final java.math.BigDecimal westOstKoordinate);

    /**
     * @return the South- / West Coordinates of the Face shown in Gis Component
     */
    public java.math.BigDecimal getSuedNordKoordinate();

    /**
     * @param suedNordKoordinate the South- / West Coordinates of the Face shown in Gis Component
     */
    public void setSuedNordKoordinate(final java.math.BigDecimal suedNordKoordinate);

    /**
     * This is the angle / bearing. 
     * @return angle or bearing.
     */
    public String getAngle();
    
    /**
     * Registers a property change listener that is notified whenever a property of the face is
     * changed.
     * 
     * @param listener the new property change listener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener);

    /**
     * Removes a property change listener.
     * 
     * @param listener the listener to be removed
     */
    public void removePropertyChangeListener(PropertyChangeListener listener);
}
