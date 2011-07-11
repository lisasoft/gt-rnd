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

import java.beans.BeanInfo;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.geotools.data.Query;
import org.geotools.data.store.ContentDataStore;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.NameImpl;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * This is a really simple DataStore that is built around a List<Face>.
 * <p>
 * It provides wrappers making the contents available as Feature for use the rendering engine.
 * <p>
 * The first draft is read-only; as such we do not need to worry about transactions or modifications
 * to the wrapped objects.
 * 
 * @author Jody Garnett
 */
public class FaceDataStore extends ContentDataStore {

    private FaceDAO data;

    public FaceDataStore(FaceDAO data) {
        this.data = data;
    }

    protected List<Name> createTypeNames() throws IOException {
        return Collections.singletonList((Name) new NameImpl("Face"));
    }

    @Override
    protected ContentFeatureSource createFeatureSource(ContentEntry entry) throws IOException {
        return new FaceFeatureSource(entry, Query.ALL);
    }

    public FaceDAO getData() {
        return data;
    }

    public SimpleFeature toFeature(SimpleFeatureType schema, Face face) {
        BeanInfo info = getData().getBeanInfo();
        
        SimpleFeatureBuilder build = new SimpleFeatureBuilder(schema);
        
        return null;
    }

}