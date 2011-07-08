package com.lisasoft.face.data;

import java.beans.PropertyChangeListener;

/**
 * This is an example plain old java object used for this prototype.
 */
public interface Face {

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
