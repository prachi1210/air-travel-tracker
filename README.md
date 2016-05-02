# Air Travel Tracker
**Table of Contents** 


  1. [Overview:](#overview)
  2. [Introduction:](#introduction)
  3. [Demo:](#demo)
  4. [Installation (Eclipse Java Mars 4.5.2):](#installation)
  5. [Features:](#features)
  6. [License:](#license)
  7. [Contact us:](#contact-us)

####Overview:
A simple Java Applet built in [Processing](https://processing.org/) using the [Unfolding Maps](http://unfoldingmaps.org/) library 

####Introduction:
Air Travel Tracker is an interactive map (with basic interactions such as zoom and pan) with geopositioned markers for displaying airport data on the map. Each aiport is marked with a small <img src=AirportMapProject/data/airport-icon.png width=20 height=20 /> icon. All airports are put in a hashmap with OpenFlights unique id for key.

####Demo:
![](screengrab/ezgif.com-crop%20(1).gif?raw=true)

####Installation 
  **(For Developers using Eclipse Java Mars 4.5.2):**
  1. Import the project into your workspace.
  2. This project requires the [Unfolding Maps](https://github.com/tillnagel/unfolding/releases/download/v0.9.6/Unfolding_for_processing_0.9.6.zip) and [controlP5](http://www.sojamo.de/libraries/controlP5/) libraries to run.
      1. Go to JRE System Library in Package Explorer
      2. Build Path > Configure Build Path
      3. Libraries > Add External JARs
  3. Rename package according to your local machine

####Features:
  1. Displays all airports on an interactive zoom (Scroll/Double click to zoom in | R to zoom out)
  2. Displays Unique OpenFlights id when mouse hovers the airport
  3. Enables search of airports using OpenFlights Id and displays the following information:
      * OpenFlights Id
      * Airport Name
      * City
      * Country Name
      * Altitude
      * No of destinations for flights originating from airport
  4. Prints routes to all destinations from given airport
      

####License:
[MIT](https://github.com/prachi1210/air-travel-tracker/blob/master/LICENSE)

####Contact us:
Feel free to contact us for any support, query, suggestion or even drop a Hi!
   
   [Prachi Manchanda](mailto:prachi121096@gmail.com)
   
   [Raj Singh](mailto:raj1996@gmail.com)



