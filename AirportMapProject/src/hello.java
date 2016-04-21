import java.awt.*;

import processing.core.PApplet;

public class hello extends PApplet{
TextField textField = new TextField(25);
TextField textField1 = new TextField(25);
//color bg = color(33,33,33,255);
String s;
public void setup(){
 size(800,800);
 add(textField);
 add(textField1);
}

public void draw(){
 background(100,100,100);
 s = textField.getText();
 
 if(s.equals("10"))
 {
	 background(0);
 }
}
}