package com.lisasoft.face.tool;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;

import org.geotools.referencing.CRS;
import org.geotools.swing.event.MapMouseEvent;
import org.opengis.filter.identity.FeatureId;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import com.lisasoft.face.data.FaceDAO;
import com.lisasoft.face.data.FaceImpl;
import com.lisasoft.face.map.MapComponentImpl;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;

public class PointDragTool extends AbstractFaceTool implements ActionListener {
	
	public PointDragTool(MapComponentImpl component) {
		super(component);
	}

	public void actionPerformed(ActionEvent e) {
		mapPane.setCursorTool(this);
	}
	
	private FeatureId draggingFeature;
	private Point lastPoint;
	
	@Override
	public void onMousePressed(MapMouseEvent ev) {
		lastPoint = ev.getPoint();
		try {
			Set<FeatureId> ids = getSelectedIds(ev);
			if(ids.size() != 1) {
				System.out.println("Unable to find selection");
				return;
			}
			// Yes, I've checked.  Look up.
			draggingFeature = ids.iterator().next();
		} catch(IOException ex) {
			System.err.println("Exception selecting feature.");
			ex.printStackTrace(System.err);
		}
	}
	
	@Override
	public void onMouseDragged(MapMouseEvent ev) {
		if(draggingFeature == null)
			return;
		Graphics g = mapPane.getGraphics(); 
		Point pnt = ev.getPoint();
		try {
			File imageFile = new File("data/crosshairs.png");
			Image img = ImageIO.read(imageFile);
			int width = img.getWidth(null);
			int height = img.getHeight(null);
			mapPane.repaint();
			g.drawImage(img, (ev.getX() - width/2), (ev.getY() - height/2), null);
		} catch(IOException ex) {
			System.err.println("Failed on reading that image I put there.");
			ex.printStackTrace(System.err);
		}
	}
	
	@Override
	public void onMouseReleased(MapMouseEvent ev) {
		if(draggingFeature ==  null)
			return;
		Coordinate dropped = getCoordFromScreen(ev);
		FaceImpl oldFace = findFace(draggingFeature);
		FaceImpl newFace = cloneFace(oldFace);
		newFace.setWestOstKoordinate(new BigDecimal(dropped.x));
		newFace.setSuedNordKoordinate(new BigDecimal(dropped.y));
		List<FaceImpl> facesss = mapPane.getFaces();
		
		List<FaceImpl> list = new ArrayList<FaceImpl>();
		
		for(int i = 0; i < facesss.size(); i++) {
			FaceImpl fac = facesss.get(i);
			if(fac.getNummer().equals(oldFace.getNummer())) {
				facesss.set(i, newFace);
				list.add(newFace);
				break;
			}
		}
		
		mapPane.setFaces(facesss);
		mapPane.setSelectionWithoutHighlighting(list);
		
	}

	private Coordinate getCoordFromScreen(MapMouseEvent ev) {
		try {
			Point pnt = ev.getPoint();
			AffineTransform screenToWorld = mapPane.getScreenToWorldTransform();
			Point2D worldPoint = screenToWorld.transform(pnt, null);
			Coordinate worldCoord = new Coordinate(worldPoint.getX(), worldPoint.getY());
			GeometryFactory geomFact = new GeometryFactory();
			com.vividsolutions.jts.geom.Point worldPnt = geomFact.createPoint(worldCoord);
			MathTransform trans = CRS.findMathTransform(CRS.decode("EPSG:4326"), CRS.decode(FaceDAO.EPSG_CODE));
			double[] source = new double[2];
			source[0] = worldPnt.getX();
			source[1] = worldPnt.getY();
			double[] dest = new double[2];
			trans.transform(source, 0, dest, 0, 1);
			Coordinate finalCoord = new Coordinate(dest[0], dest[1]);
			return finalCoord;
		} catch(FactoryException ex) {
			System.err.println("Transformation exception");
			ex.printStackTrace(System.err);
		} catch(TransformException ex) {
			System.err.println("Transformation exception");
			ex.printStackTrace(System.err);
		}
		return null;
	}
	
	private FaceImpl cloneFace(FaceImpl faceIn) {
	    	FaceImpl clone = new FaceImpl(faceIn.getNummer());
	    	clone.setType(faceIn.getType());
	    	clone.setFaceFormat(faceIn.getFaceFormat());
	    	clone.setProductFormat(faceIn.getProductFormat());
	    	clone.setStatus(faceIn.getStatus());
	    	clone.setInstalled(faceIn.getInstalled());
	    	clone.setPosting(faceIn.getPosting());
	    	clone.setPeriod(faceIn.getPeriod());
	    	clone.setArea(faceIn.getArea());
	    	clone.setStreet(faceIn.getStreet());
	    	clone.setNumber(faceIn.getNumber());
	    	clone.setWestOstKoordinate(faceIn.getWestOstKoordinate());
	    	clone.setSuedNordKoordinate(faceIn.getSuedNordKoordinate());
	    	clone.setAngle(faceIn.getAngle());
	    	clone.setCategory(faceIn.getCategory());
	    	return clone;
	}
	
	private FaceImpl findFace(FeatureId id) {
		List<FaceImpl> faces = mapPane.getFaces();
		Long nummer = nummerFromFID(id);
		if(nummer == null)
			return null;
		for(FaceImpl face : faces) {
			if(face.getNummer().equals(nummer)) 
				return face;
		}
		return null;
	}
}
