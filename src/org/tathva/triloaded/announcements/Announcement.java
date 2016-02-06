package org.tathva.triloaded.announcements;

public class Announcement {
	String type, title, body, time;
	int id;
	
	public Announcement(int id, String type, String title, String body, String time) {
		this.id=id;
		this.type=type;
		this.title=title;
		this.body=body;
		this.time=time;
	}

	public int getId(){
		return id;
	}
	
	public String gettype(){
		return type;
	}
	
	public String getTitle(){
		return title;
	}
	
	public String getBody(){
		return body;
	}
	
	public String getTime(){
		return time;
	}	
	
}
