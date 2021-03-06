package com.lisasoft.face.tool;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Set;

import org.geotools.swing.event.MapMouseEvent;
import org.opengis.filter.identity.FeatureId;

import com.lisasoft.face.map.MapComponentImpl;

public class MapSelectionTool 
		extends AbstractFaceTool
		implements ActionListener {
	
	public MapSelectionTool(MapComponentImpl component) {
		super(component);
	}

	public void actionPerformed(ActionEvent e) {
		mapPane.setCursorTool(this);
	}
	
	public void onMouseClicked(MapMouseEvent ev) {

		try{
			Set<FeatureId> ids = getSelectedIds(ev);
			mapPane.setSelection(ids);

		} catch(IOException ex) {
			System.err.println("Error determining selection.");
			ex.printStackTrace(System.err);
		}
	}
}
