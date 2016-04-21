package projectpackage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.awt.*;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.data.ShapeFeature;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.Google;
//import de.fhpotsdam.unfolding.providers.Google.GoogleMapProvider;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.utils.ScreenPosition;
import de.fhpotsdam.unfolding.geo.Location;
import processing.core.PApplet;
import processing.core.PImage;
import controlP5.*;

public class AirportMap extends PApplet {
	public int xbase=250,ybase=100;
	UnfoldingMap map;
	private List<Marker> airportList;
	PImage airicon;
	List<Marker> routeList;
	private String stringsource;
	private String stringdest;
		
	private AirportMarker lastSelected;
	HashMap<Integer, AirportMarker> keytoairport = new HashMap<Integer, AirportMarker>();
	
	ControlP5 cp5;
	
	public void setup() {
		// setting up PApplet
		size(1920,1080,P2D);
		
		cp5 = new ControlP5(this);
		
		cp5.addTextfield("SOURCE")
	     .setPosition(25,ybase)
	     .setSize(200,50)
	     .setFocus(true)
	     ;
		
		cp5.addTextfield("DESTINATION")
	     .setPosition(25,ybase+100)
	     .setSize(200,50)
	     .setFocus(true)
	     ;
		
		// setting up map and default events
		map = new UnfoldingMap(this, xbase, ybase, 1000, 880, new Google.GoogleMapProvider());
		MapUtils.createDefaultEventDispatcher(this, map);
		
		// get features from airport data
		List<PointFeature> features = parseAirports(this, "../data/airports.dat");
		
		// list for markers, hashmap for quicker access when matching with routes
		airportList = new ArrayList<Marker>();
		HashMap<Integer, Location> airports = new HashMap<Integer, Location>();
		
		airicon = loadImage("../data/airport-icon.png");
		
		// create markers from features
		for(PointFeature feature : features) {
			AirportMarker m = new AirportMarker(feature);
	
			m.setId(feature.getId());
			m.setIcon(airicon);
			m.setMap(map);
			airportList.add(m);
			
			// put airport in hashmap with OpenFlights unique id for key
			airports.put(Integer.parseInt(feature.getId()), feature.getLocation());
			keytoairport.put(Integer.parseInt(feature.getId()), m);
		
		}
		
		
		// parse route data
		List<ShapeFeature> routes = parseRoutes(this, "../data/routes.dat");
		routeList = new ArrayList<Marker>();
		AirportMarker sourcetemp = null;
		AirportMarker desttemp = null;
		for(ShapeFeature route : routes) {
			
			// get source and destination airportIds
			int source = Integer.parseInt((String)route.getProperty("source"));
			int dest = Integer.parseInt((String)route.getProperty("destination"));
			
			// get locations for airports on route
			if(airports.containsKey(source) && airports.containsKey(dest)) {
				route.addLocation(airports.get(source));
				route.addLocation(airports.get(dest));
				
			AirportRouteMarker sl = new AirportRouteMarker(route.getLocations(), route.getProperties());
		    sl.setSourceCode(source);
		    sl.setDestCode(dest);
		    
		    sourcetemp = keytoairport.get(source);
		    sl.setSource(sourcetemp);
		    
		    desttemp = keytoairport.get(dest);
		    sl.setDest(desttemp);
			//System.out.println(sl.getProperties());
			
		    sl.setMap(map);
		    
			routeList.add(sl);
			}
		}
		
		//UNCOMMENT IF YOU WANT TO SEE ALL ROUTES
		map.addMarkers(routeList);
		
	    map.addMarkers(airportList);
	}
	
	AirportMarker SourceAirportforBox, DestAirportforBox;
	public int intsource, intdest;
	String AirportName, CityName, CountryName, Altitude,AirportCode;
	
	public void draw() {
		background(100,100,100);
		
		drawProjectTitle();
		
		map.draw();
		drawZoomButtons();
		
		stringsource = cp5.get(Textfield.class,"SOURCE").getText();
		stringdest = cp5.get(Textfield.class,"DESTINATION").getText();
		
		try
		{
		    intsource = Integer.parseInt(stringsource);
		}
		catch(Exception e)
		{
			intsource = 0;
		}
		
		try
		{
			intdest = Integer.parseInt(stringdest);
		}
		catch(Exception e)
		{
			intdest = 0;
		}
		
		try
		{
			SourceAirportforBox = keytoairport.get(intsource);
		}
		catch(Exception e)
		{
			SourceAirportforBox = null;
		}
		
		try
		{
			DestAirportforBox = keytoairport.get(intdest);
		}
		catch(Exception e)
		{
			DestAirportforBox = null;
		}
		
		DrawBoxesWithSourceAndDestination();
		
		for(Marker route : routeList)
		{
			if(route.getProperty("source").equals(stringsource) && route.getProperty("destination").equals(stringdest))
			{
				route.setHidden(false);
			}
			else
			{
				route.setHidden(true);
			}
		}	
    }
	
	public void drawProjectTitle()
	{
		fill(0,0,0);
		textSize(30);
		text("Flights Data",100,50);
	}
	
	public void RouteVisibility()
	{
		//To Do : Make hashmap(precompute) to avoid going to all routes
	}
	
	public void DrawBoxesWithSourceAndDestination()
	{
		fill(255,255,255);
		rect(25,ybase+200,200,300,10);
		rect(25,ybase+550,200,300,10);
		
		
		fill(0,0,0);
		textSize(15);
		
		if(SourceAirportforBox != null)
		{
			AirportName = SourceAirportforBox.getName();
			CityName = SourceAirportforBox.getCity();
			CountryName = SourceAirportforBox.getCountry();
			Altitude = "Altitude : " + SourceAirportforBox.getAltitude();
			AirportCode = "ID : " + SourceAirportforBox.getId();
			
			text(AirportCode,30,ybase+230);
			text(AirportName,30,ybase+280);
			text(CityName,30,ybase+330);
			text(CountryName,30,ybase+380);
			text(Altitude,30,ybase+430);
		}
		
		
		if(DestAirportforBox != null)
		{
			AirportName = DestAirportforBox.getName();
			CityName = DestAirportforBox.getCity();
			CountryName = DestAirportforBox.getCountry();
			Altitude = "Altitude : " + DestAirportforBox.getAltitude();
			AirportCode = "ID : " + DestAirportforBox.getId();
			
			text(AirportCode,30,ybase+580);
			text(AirportName,30,ybase+630);
			text(CityName,30,ybase+680);
			text(CountryName,30,ybase+730);
			text(Altitude,30,ybase+780);
		}
	}
	
	public void drawZoomButtons()
	{
		//To Do  
		
	}
	
	//Only for sketch purpose. No real purpose.
	public void mouseReleased()
	{
		System.out.println(mouseX + " " + mouseY);
	}

	public void mouseMoved()
	{
		// clear the last selection
		if (lastSelected != null) {
			lastSelected.setSelected(false);
			lastSelected = null;
		}
		
		selectMarkerIfHover(airportList);
		//loop();
	}
	
	private void selectMarkerIfHover(List<Marker> markers)
	{
		// Abort if there's already a marker selected
		if (lastSelected != null) {
			return;
		}
		
		for (Marker m : markers) 
		{
			AirportMarker marker = (AirportMarker)m;
			if (marker.isInside(map, mouseX, mouseY)) {
				lastSelected = marker;
				marker.setSelected(true);
				return;
			}
		}
	}
	
	public static List<PointFeature> parseAirports(PApplet p, String fileName) {
		List<PointFeature> features = new ArrayList<PointFeature>();

		String[] rows = p.loadStrings(fileName);
		for (String row : rows) {
			
			// hot-fix for altitude when lat lon out of place
			int i = 0;
			
			// split row by commas not in quotations
			String[] columns = row.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
			
			// get location and create feature
			//System.out.println(columns[6]);
			float lat = Float.parseFloat(columns[6]);
			float lon = Float.parseFloat(columns[7]);
			
			Location loc = new Location(lat, lon);
			PointFeature point = new PointFeature(loc);
			
			// set ID to OpenFlights unique identifier
			point.setId(columns[0]);
			
			// get other fields from csv
			point.addProperty("name", columns[1]);
			point.putProperty("city", columns[2]);
			point.putProperty("country", columns[3]);
			
			// pretty sure IATA/FAA is used in routes.dat
			// get airport IATA/FAA code
			if(!columns[4].equals("")) {
				point.putProperty("code", columns[4]);
			}
			// get airport ICAO code if no IATA
			else if(!columns[5].equals("")) {
				point.putProperty("code", columns[5]);
			}
			
			point.putProperty("altitude", columns[8 + i]);
			
			features.add(point);
		}

		return features;
		
	}
	
	public static List<ShapeFeature> parseRoutes(PApplet p, String fileName) {
		List<ShapeFeature> routes = new ArrayList<ShapeFeature>();
		
		String[] rows = p.loadStrings(fileName);
		
		for(String row : rows) {
			String[] columns = row.split(",");
			
			ShapeFeature route = new ShapeFeature(Feature.FeatureType.LINES);
			
			// set id to be OpenFlights identifier for source airport
			
			// check that both airports on route have OpenFlights Identifier
			if(!columns[3].equals("\\N") && !columns[5].equals("\\N")){
				// set "source" property to be OpenFlights identifier for source airport
				route.putProperty("source", columns[3]);
				// "destination property" -- OpenFlights identifier
				route.putProperty("destination", columns[5]);
				
				routes.add(route);
			}
		}
			
		
		return routes;
		
	}
}