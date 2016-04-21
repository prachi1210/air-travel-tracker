import processing.core.PApplet;
import processing.core.PFont;
import controlP5.*;

public class controlp5textfield extends PApplet {
	/**
	* ControlP5 Textfield
	*
	*
	* find a list of public methods available for the Textfield Controller
	* at the bottom of this sketch.
	*
	* by Andreas Schlegel, 2012
	* www.sojamo.de/libraries/controlp5
	*
	*/


	ControlP5 cp5;

	String textValue = "";

	public void setup() {
	  size(700,400);
	  
	  PFont font = createFont("arial",20);
	  
	  cp5 = new ControlP5(this);
	  
	  cp5.addTextfield("input")
	     .setPosition(20,100)
	     .setSize(200,40)
	     .setFont(font)
	     .setFocus(true)
	     .setColor(color(255,0,0))
	     ;
	                 
	  cp5.addTextfield("textValue")
	     .setPosition(20,170)
	     .setSize(200,40)
	     .setFont(createFont("arial",20))
	     .setAutoClear(false)
	     ;
	       
	  cp5.addBang("clear")
	     .setPosition(240,170)
	     .setSize(80,40)
	     .getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER)
	     ;    
	  
	  cp5.addTextfield("default")
	     .setPosition(20,350)
	     .setAutoClear(false)
	     ;
	     
	  textFont(font);
	}

	public void draw() {
	  background(0);
	  fill(255);
	  text(cp5.get(Textfield.class,"input").getText(), 360,130);
	  text(textValue, 360,180);
	}

	public void clear() {
	  cp5.get(Textfield.class,"textValue").clear();
	}

	void controlEvent(ControlEvent theEvent) {
	  if(theEvent.isAssignableFrom(Textfield.class)) {
	    println("controlEvent: accessing a string from controller '"
	            +theEvent.getName()+"': "
	            +theEvent.getStringValue()
	            );
	  }
	}


	public void input(String theText) {
	  // automatically receives results from controller input
	  println("a textfield event for controller 'input' : "+theText);
	}

}
