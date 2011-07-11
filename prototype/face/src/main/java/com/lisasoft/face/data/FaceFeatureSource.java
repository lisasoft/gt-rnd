package com.lisasoft.face.data;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;

import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.csv.CSVDataStore;
import org.geotools.data.csv.CSVFeatureReader;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Point;

public class FaceFeatureSource extends ContentFeatureSource {

    public FaceFeatureSource(ContentEntry entry, Query query) {
        super( entry, query );
    }

    /**
     * Access parent CSVDataStore
     */
    public FaceDataStore getDataStore(){
        return (FaceDataStore) super.getDataStore();
    }
    
    @Override
    protected ReferencedEnvelope getBoundsInternal(Query query) throws IOException {
        ReferencedEnvelope bbox = new ReferencedEnvelope( getCRS() );
        for( Face face : getDataStore().getData().contents() ){ 
            bbox.expandToInclude( FaceDAO.getLocation(face).getCoordinate() );
        }
        return bbox;
    }

    
    @Override
    protected int getCountInternal(Query query) throws IOException {
        return getDataStore().getData().contents().size();
    }

    @Override
    protected FeatureReader<SimpleFeatureType, SimpleFeature> getReaderInternal(Query query)
            throws IOException {
        return new FaceFeatureReader( getState() );
    }


    public CoordinateReferenceSystem getCRS() {
        try {
            CoordinateReferenceSystem crs = CRS.decode("EPSG:2056");
            return crs;
        } catch (Exception e) {
            return null;
        }
    }
    
    @Override
    protected SimpleFeatureType buildFeatureType() throws IOException {
        BeanInfo info = getDataStore().getData().getBeanInfo();
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();

        String name = info.getBeanDescriptor().getName();
        builder.setName( name );
        CoordinateReferenceSystem crs = getCRS();
        builder.setCRS( crs );
        
        builder.add("Point", Point.class, crs );
        
        for( PropertyDescriptor property : info.getPropertyDescriptors() ){
            builder.add( property.getName(), property.getPropertyType() );
        }        
        return builder.buildFeatureType();        
    }
    
}
