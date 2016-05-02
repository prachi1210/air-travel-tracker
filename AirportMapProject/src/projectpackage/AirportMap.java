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
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int xbase=30,ybase=80;
	UnfoldingMap map;
	private List<Marker> airportList;
	PImage airicon;
	List<Marker> routeList;
	private String stringsource;
		
	private AirportMarker lastSelected;
	HashMap<Integer, AirportMarker> keytoairport = new HashMap<Integer, AirportMarker>();
	
	ControlP5 cp5;
	int mygray = color(100,100,100);
	int myyellow = color(213,213,63);
	
	public void setup() {
		// setting up PApplet
		size(1000,500,P2D);
		
		cp5 = new ControlP5(this);
		
		cp5.addTextfield("Search for Airport using unique OpenFlights ID")
	     .setPosition(xbase,ybase)
	     .setSize(150,30)
	     .setFocus(true)
	     .setFont(createFont("calibri",20))
		 .setColorBackground(myyellow)
		 .setColor(color(0,0,0))
	     ;
	     
	     cp5.addBang("clear")
	     .setPosition(xbase+200,ybase)
	     .setSize(40,30)
	     .getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER)
	     .setColor(myyellow);
	     ;
		
		// setting up map and default events
		map = new UnfoldingMap(this, 450, 80, 500, 400, new Google.GoogleMapProvider());
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
	
	AirportMarker SourceAirportforBox,tempDest;
	public int intsource, intdest, flightcount;
	String AirportName, CityName, CountryName, Altitude,AirportCode, deststring;
	Boolean airportnotfound;
	
	public void draw() {
		
		background(100,100,100);
		
		drawProjectTitle();
		HelpBox();
		
		map.draw();
		
		stringsource = cp5.get(Textfield.class,"Search for Airport using unique OpenFlights ID").getText();
		
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
			SourceAirportforBox = keytoairport.get(intsource);
			airportnotfound = false;
		}
		catch(Exception e)
		{
			SourceAirportforBox = null;
			airportnotfound = true;
		}
		
		DrawBox();
	
		if(keytoairport.containsKey(intsource))
		{
			for(Marker airport : airportList)
			{
				airport.setHidden(true);
			}
		}
		else
		{
			//System.out.println("A");
			
			for(Marker airport : airportList)
			{
				airport.setHidden(false);
			}
		}
		
		try
		{
		     SourceAirportforBox.setHidden(false);
		}
		catch( Exception e )
		{
		}
		
		flightcount = 0;
			
			for(Marker route : routeList)
		{
			if(route.getProperty("source").equals(stringsource))
			{
				flightcount++;
				deststring = route.getStringProperty("destination");
				intdest = Integer.parseInt(deststring);
				
				route.setHidden(false);
				
				tempDest = keytoairport.get(intdest);
				tempDest.setHidden(false);
			}
			else
			{
				route.setHidden(true);
			}
		}	
		
    }
	
	public void HelpBox()
	{
		//To Do
		fill(myyellow);
		String message = "HELP : - Hover over your airport";
		String message2 = "and find out it's Unique OpenFlights ID";
		String message3 = "Double-Click or Scroll to zoom in/out";
		String message4 = "Press R at anytime to reset map's zoom";
		
		rect(xbase,ybase+325,250,75,10);
		textSize(12);
		fill(0,0,0);
		text(message,xbase+15,ybase+350);
		text(message2,xbase+15,ybase+365);
		text(message3,xbase+15,ybase+380);
		text(message4,xbase+15,ybase+395);
	}
	
	public void drawProjectTitle()
	{
		//To Do
		fill(0,0,0);
		textFont(createFont("arial",30));
		text("World Airport Tracker",275,50);
	}
	
	public void DrawBox()
	{
		fill(myyellow);
		rect(xbase,ybase+50,250,250,10);
		
		fill(0,0,0);
		textSize(15);
		text("AIRPORT INFORMATION :- ",xbase+15,ybase+75);
		
		textSize(12);
		if(SourceAirportforBox != null)
		{
			AirportName = SourceAirportforBox.getName();
			CityName = SourceAirportforBox.getCity();
			CountryName = SourceAirportforBox.getCountry();
			Altitude = "Altitude of the airport : " + SourceAirportforBox.getAltitude();
			AirportCode = "ID : " + SourceAirportforBox.getId();
			
			text("OpenFlights "+ AirportCode,xbase+15,ybase+100);
			text("Airport Name :- " + AirportName,xbase+15,ybase+120);
			text("City :- " + CityName,xbase+15,ybase+140);
			text("Country :- " + CountryName,xbase+15,ybase+160);
			text(Altitude + " metres",xbase+15,ybase+180);
			text("Flights reaching to "+flightcount+" destinations!",xbase+15,ybase+200);
		}
		else
		{
			text("Valid search has not been made",xbase+15,ybase+200);
		}
	}
	
	public void clear() {
		  cp5.get(Textfield.class,"Search for Airport using unique OpenFlights ID").clear();
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
	
	public void keyPressed()
	{
		if(key == 'r')
		{
			map.zoomToLevel(1);
		}
		
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
