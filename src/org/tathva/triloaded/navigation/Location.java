package org.tathva.triloaded.navigation;

public class Location {

	String place,lat,lon,type;
	
	public Location(String place, String lat, String lon,String type) {
		this.place=place;
		this.lat=lat;
		this.lon=lon;
		this.type = type;
	}
	
	public String getPos(){
		return lat+","+lon;
	}
	
	public String getName(){
		return place;
	}
	
	public String getLat(){
		return lat;
	}
	
	public String getLon(){
		return lon;
	}
	public String getType(){
		return type;
	}
		
}
