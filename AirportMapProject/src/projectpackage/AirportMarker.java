package projectpackage;

import java.util.List;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;

public class AirportMarker extends SimplePointMarker{
	
	private int radius = 5;
	PImage img;
	UnfoldingMap map;
	private int zoomLevel;
	
	public void setIcon(PImage temp)
	{
		img = temp;
	}
	
	public void setMap(UnfoldingMap map1)
	{
		map = map1;
	}
	
	public AirportMarker(Feature city) {
		super(((PointFeature)city).getLocation(), city.getProperties());
	
	}
	
	public void draw(PGraphics pg, float x, float y) {
		if (!hidden) {
			drawMarker(pg, x, y);
			if (selected) {
				showTitle(pg, x, y);
			}
		}
	}
	
	public void drawMarker(PGraphics pg, float x, float y) {
		pg.fill(11);
		
		zoomLevel = map.getZoomLevel();
		
		radius = (zoomLevel*2) + 5;
		
		img.resize(radius, radius);
		pg.image(img, x - radius/2, y - radius/2);
		if (selected) {
			showTitle(pg, x, y);
		}
	}

	public void showTitle(PGraphics pg, float x, float y) {
		 // show rectangle with title
		// show routes
		
		String id = "ID : " + getId();
		String name = getName();
		
		pg.pushStyle();
		pg.fill(255,255,255);
		pg.textSize(12);
		pg.rectMode(PConstants.CORNER);
		pg.rect(x,y-8*radius,Math.max(pg.textWidth(id),pg.textWidth(name))+6,8*radius);
		pg.fill(0,0,0);
		pg.textAlign(PConstants.LEFT,PConstants.TOP);
		pg.text(id,x+3,y-7*radius);
		pg.text(name,x+3,y-4*radius);
		
		pg.popStyle();
	}
	
	public String getName()
	{
		return getStringProperty("name");
	}	
	
	public String getCity()
	{
		return getStringProperty("city");
	}
	
	public String getCountry()
	{
		return getStringProperty("country");
	}
	
	public String getAltitude()
	{
		return getStringProperty("altitude");
	}
}
