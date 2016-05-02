package projectpackage;

import processing.core.PGraphics;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;
import de.fhpotsdam.unfolding.utils.MapPosition;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

public class AirportRouteMarker extends SimpleLinesMarker{
	public int xbase=450,ybase=80;
	private int SourceCode,DestCode;
	UnfoldingMap map;
	private AirportMarker source = null;
	private AirportMarker dest = null;
	private ScreenPosition sourcescreen = null;
	private ScreenPosition destscreen = null;
	
	public AirportRouteMarker(java.util.List<Location> locations, java.util.HashMap<java.lang.String,java.lang.Object> properties)
	{
		super(locations,properties);
	}
	
	public void draw(PGraphics pg, java.util.List<MapPosition> mapPositions)
	{
		if(!hidden)
		{
			pg.pushStyle();
			
			sourcescreen = source.getScreenPosition(map);
			destscreen = dest.getScreenPosition(map);
			
			pg.fill(150, 30, 30);
			pg.line(sourcescreen.x-xbase,sourcescreen.y-ybase,destscreen.x-xbase,destscreen.y-ybase);
			
			// Restore previous drawing style
			pg.popStyle();
		}
	}
	
	public void setSourceCode(int code)
	{
		SourceCode = code;
	}
	public void setDestCode(int code)
	{
		DestCode = code;
	}
	
	public int getSourceCode()
	{
		return SourceCode;
	}
	public int getDestCode()
	{
		return DestCode;
	}
	
	public void setSource(AirportMarker m)
	{
		source = m;
	}
	public void setDest(AirportMarker m)
	{
		dest = m;
	}
	
	public AirportMarker getSource()
	{
		return source;
	}
	
	public AirportMarker getDest()
	{
		return dest;
	}
	
	public void setMap(UnfoldingMap m)
	{
		map=m;
	}
}
