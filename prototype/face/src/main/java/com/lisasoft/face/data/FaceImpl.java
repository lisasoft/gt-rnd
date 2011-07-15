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
import java.beans.PropertyChangeSupport;
import java.math.BigDecimal;

import javax.swing.event.EventListenerList;

import org.geotools.geometry.jts.JTSFactoryFinder;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

/**
 * A quick implementation of Face; needed to support all data methods in order to match table.
 * 
 * @author Jody Garnett (LISAsoft)
 */
public class FaceImpl implements Face {

    static GeometryFactory gf = JTSFactoryFinder.getGeometryFactory(null);

    long identifier;

    String type;

    String faceFormat;

    String productFormat;

    String status;

    String installed; // = tokens[5].trim();

    String posting; // = tokens[6].trim();

    String period;


    String area; // = tokens[7].trim();

    String street; // = tokens[8].trim();

    String number; // = tokens[9].trim();

    BigDecimal x; // latitude; // = Double.parseDouble(tokens[10]);

    BigDecimal y; // longitude; // = Double.parseDouble(tokens[11]);

    String angle; // = tokens[12].trim();

    String category; // = tokens[13].trim();

    public FaceImpl(long identifier) {
        this.identifier = identifier;
    }

    /**
     * Point uses a Coordinate of double for display; this may notbe sufficient given the use of
     * BigDecimal (is this a CRS measured in cm ??)
     */
    Point getLocation() {
        Coordinate coordinate = new Coordinate(x.doubleValue(), y.doubleValue());
        Point pt = gf.createPoint(coordinate);
        return pt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFaceFormat() {
        return faceFormat;
    }

    public void setFaceFormat(String faceFormat) {
        this.faceFormat = faceFormat;
    }

    public String getProductFormat() {
        return productFormat;
    }

    public void setProductFormat(String productFormat) {
        this.productFormat = productFormat;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInstalled() {
        return installed;
    }

    public void setInstalled(String installed) {
        this.installed = installed;
    }

    public String getPosting() {
        return posting;
    }

    public void setPosting(String posting) {
        this.posting = posting;
    }


    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }
    
    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAngle() {
        return angle;
    }

    public void setAngle(String angle) {
        String old = this.angle;
        this.angle = angle;
        pcs.firePropertyChange( ANGLE, old, angle );
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        String old = this.category;
        this.category = category;
        pcs.firePropertyChange(CATEGORY, old, category);
    }

    public Long getNummer() {
        return identifier;
    }

    public BigDecimal getWestOstKoordinate() {
        return x;
    }

    public void setWestOstKoordinate(BigDecimal westOstKoordinate) {
        BigDecimal old = this.x;
        x = westOstKoordinate;
        pcs.firePropertyChange(WEST_OST_KOORDINATE, old, x);
    }
    
    public void setCoords(BigDecimal westOstKoordinate, BigDecimal suedNordKoordinate) {
    	BigDecimal[] oldCoord = new BigDecimal[2];
    	BigDecimal[] newCoord = new BigDecimal[2];
    	oldCoord[0] = this.x;
    	oldCoord[1] = this.y;
    	newCoord[0] = westOstKoordinate;
    	newCoord[1] = suedNordKoordinate;
    	this.x = westOstKoordinate;
    	this.y = suedNordKoordinate;
    	pcs.firePropertyChange(WEST_OST_KOORDINATE + " " + SUED_NORD_KOORDINATE, oldCoord, newCoord);
    }

    public BigDecimal getSuedNordKoordinate() {
        return y;
    }

    public void setSuedNordKoordinate(BigDecimal suedNordKoordinate) {
        BigDecimal old = y;
        y = suedNordKoordinate;
        pcs.firePropertyChange(SUED_NORD_KOORDINATE, old, y);
    }

    
    PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    EventListenerList listeners = null;    
    synchronized public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }
    synchronized public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((angle == null) ? 0 : angle.hashCode());
        result = prime * result + ((area == null) ? 0 : area.hashCode());
        result = prime * result + ((category == null) ? 0 : category.hashCode());
        result = prime * result + ((faceFormat == null) ? 0 : faceFormat.hashCode());
        result = prime * result + (int) (identifier ^ (identifier >>> 32));
        result = prime * result + ((installed == null) ? 0 : installed.hashCode());
        result = prime * result + ((number == null) ? 0 : number.hashCode());
        result = prime * result + ((posting == null) ? 0 : posting.hashCode());
        result = prime * result + ((productFormat == null) ? 0 : productFormat.hashCode());
        result = prime * result + ((status == null) ? 0 : status.hashCode());
        result = prime * result + ((street == null) ? 0 : street.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        result = prime * result + ((x == null) ? 0 : x.hashCode());
        result = prime * result + ((y == null) ? 0 : y.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        FaceImpl other = (FaceImpl) obj;
        if (angle == null) {
            if (other.angle != null)
                return false;
        } else if (!angle.equals(other.angle))
            return false;
        if (area == null) {
            if (other.area != null)
                return false;
        } else if (!area.equals(other.area))
            return false;
        if (category == null) {
            if (other.category != null)
                return false;
        } else if (!category.equals(other.category))
            return false;
        if (faceFormat == null) {
            if (other.faceFormat != null)
                return false;
        } else if (!faceFormat.equals(other.faceFormat))
            return false;
        if (identifier != other.identifier)
            return false;
        if (installed == null) {
            if (other.installed != null)
                return false;
        } else if (!installed.equals(other.installed))
            return false;
        if (number == null) {
            if (other.number != null)
                return false;
        } else if (!number.equals(other.number))
            return false;
        if (posting == null) {
            if (other.posting != null)
                return false;
        } else if (!posting.equals(other.posting))
            return false;
        if (productFormat == null) {
            if (other.productFormat != null)
                return false;
        } else if (!productFormat.equals(other.productFormat))
            return false;
        if (status == null) {
            if (other.status != null)
                return false;
        } else if (!status.equals(other.status))
            return false;
        if (street == null) {
            if (other.street != null)
                return false;
        } else if (!street.equals(other.street))
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        if (x == null) {
            if (other.x != null)
                return false;
        } else if (!x.equals(other.x))
            return false;
        if (y == null) {
            if (other.y != null)
                return false;
        } else if (!y.equals(other.y))
            return false;
        return true;
    }
    


}
