package com.lisasoft.face.data;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.CopyOnWriteArrayList;

import org.geotools.data.FeatureReader;
import org.geotools.data.store.ContentState;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

public class FaceFeatureReader implements FeatureReader<SimpleFeatureType, SimpleFeature> {

    private ContentState state;
    private Iterator<FaceImpl> iterator;

    Face next; // next face
    Face face; // current face
    
    public FaceFeatureReader(ContentState state) {
        this.state = state;
        FaceDataStore dataStore = (FaceDataStore) state.getEntry().getDataStore();
        
        // we grab a threadsafe copy on write list here to represent reading        
        CopyOnWriteArrayList<FaceImpl> contents = dataStore.getData().contents();
        iterator = contents.iterator();        
    }

    FaceDataStore getDataStore(){
        return (FaceDataStore) state.getEntry().getDataStore();
    }
    
    public SimpleFeatureType getFeatureType() {
        return state.getFeatureType();
    }

    public SimpleFeature next() throws IOException, IllegalArgumentException,
            NoSuchElementException {
        if( next != null ){
            face = next;
            next = null;
        }
        else {
            face = nextFace();
        }
        SimpleFeature feature = getDataStore().toFeature( getFeatureType(), face );
        return feature;
    }

    public boolean hasNext() throws IOException {
        if( next != null ){
            return true;
        }
        else {
            next = nextFace();
            return next != null;
        }
    }
    
    private Face nextFace(){
        return iterator == null ? null : iterator.next();
    }

    public void close() throws IOException {
        iterator = null;
        next = null;
        face = null;
    }

}