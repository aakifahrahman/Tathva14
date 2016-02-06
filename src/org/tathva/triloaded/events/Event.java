package org.tathva.triloaded.events;

import android.util.Log;

public class Event {
	
	public static final int COMPETITION = 1;
	public static final int WORKSHOP = 2;
	public static final int EXHIBITION = 3;
	public static final int LECTURES = 4;
	
	public String eventId;
	public String eventName;
	public int eventType;
	public String contents;
	public String branch;
	public String time_d1,time_d2,time_d3;
	public String venue_d1,venue_d2,venue_d3;
	//public String status_d1,status_d2,status_d3;
	public String contactname, contactnumber;
	public String HTML;
	public byte[] img1,img2,img3;

	public String time;
	public String venue;
	public String Update;
	/**/
	public String results;
	public String image_url;
	
	public Event(String eventId,String eventName,int eventType) {
		
		this.eventId = eventId;
		this.eventName = eventName;
		this.eventType = eventType;
		this.branch=branch;
	}
	

	public Event(String id, String name,  String type, String branch, String contents, String image_url){
		
		this.eventId=id;
		this.eventName=name;
		this.contents=contents;
		this.branch=branch;
		this.image_url = image_url;

	}
	
	public void setTiming(String  time_d1, String time_d2, String time_d3){
		this.time_d1=time_d1;
		this.time_d2=time_d2;
		this.time_d3=time_d3;
	}
	
	public void setVenue(String venue_d1, String venue_d2, String venue_d3){
		Log.i("set Venue",venue_d1+venue_d2+venue_d3);
		this.venue_d1=venue_d1;
		this.venue_d2=venue_d2;
		this.venue_d3=venue_d3;
	}
	/*
	public void setStatus(String status_d1, String status_d2, String status_d3){
		this.status_d1=status_d1;
		this.status_d2=status_d2;
		this.status_d3=status_d3;
	}
	*/
	
	public void setContact(String contactname, String contactnumber){
		this.contactname=contactname;
		this.contactnumber=contactnumber;
	}
	
	public void setHTML(String HTML, byte[] img1, byte[] img2, byte[] img3){
		this.HTML=HTML;
		this.img1=img1;
		this.img2=img2;
		this.img3=img3;
	}
	
	public void setResults(String results){
		this.results = results;
	}
	
}
