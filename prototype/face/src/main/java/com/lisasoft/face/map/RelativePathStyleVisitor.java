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

import org.geotools.styling.ExternalGraphic;
import org.geotools.styling.visitor.DuplicatingStyleVisitor;
import org.opengis.metadata.citation.OnLineResource;

/**
 * 
 * @author jody
 */
public class RelativePathStyleVisitor extends DuplicatingStyleVisitor {

    /**
     * External graphics are used to refer to images on disk.
     */
    public void visit(ExternalGraphic exgr) {
        super.visit(exgr);
    }
    
}
