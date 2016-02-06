package org.tathva.triloaded.gcm;

import org.json.JSONArray;
import org.json.JSONObject;
import org.tathva.triloaded.events.Updater;

import android.util.Log;

public class Notification {

	static final String NOTIFICATION_TYPE = "type";
	
	static final String EVENT_ID = "eventId";
	static final String EVENT_SQL = "eventsql";
	
	static final String ANNOUNCEMENT_ID = "announcement_id";
	static final String ANNOUNCEMENT_TYPE = "announcement_type";
	static final String ANNOUNCEMENT_HEADING = "announcement_heading";
	static final String ANNOUNCEMENT_TEXT = "announcement_text";
	static final String ANNOUNCEMENT_TIME = "announcement_time";
	
	static final int TYPE_EVENT_UPDATE = 1;
	static final int TYPE_ANNOUNCEMENT = 2;
	
	static final int ANN_TYPE_GENERAL = 1;
	static final int ANN_TYPE_EVENT = 2;
	static final int ANN_TYPE_LOST = 3;
	
	private int type;
	private int id;
	
	private String eventId;
	private String sql;
	
	private int anmtType;
	private String anmtHeading;
	private String anmtText;
	private String anmtTime;
	
	
	public Notification(String eventId,String sql) {
		this.eventId = eventId;
		this.sql = sql;
		type = TYPE_EVENT_UPDATE;
	}
	
	public Notification(int id, int anmtType, String anmtHeading,String anmtText,String anmtTime) {
		this.id = id;
		this.anmtType = anmtType;
		this.anmtHeading = anmtHeading;
		this.anmtText = anmtText;
		this.anmtTime = anmtTime;
		type = TYPE_ANNOUNCEMENT;
		
	}
	
	public Notification(String jString) {
		
		try{

			JSONObject data = new JSONObject(jString);
			type = data.getInt(NOTIFICATION_TYPE);
			if(type == TYPE_ANNOUNCEMENT){
				anmtType = data.getInt(ANNOUNCEMENT_TYPE);
				anmtHeading = data.getString(ANNOUNCEMENT_HEADING);
				anmtText = data.getString(ANNOUNCEMENT_TEXT);
				anmtTime = data.getString(ANNOUNCEMENT_TIME);
			}else if(type == TYPE_EVENT_UPDATE){
				eventId = data.getString(EVENT_ID);
				sql = data.getString(EVENT_SQL);
				
			}
			
			
		}catch(Exception e){
			Log.i("anas","Error creating Notification : "+e.toString());
		}
	}
	public int getAnnouncementId(){
		return id;
	}
	public int getType(){
		return type;
	}
	public String getEventId(){
		return eventId;
	}
	public String getSql(){
		return sql;
	}
	
	public int getAnnouncementType(){
		return anmtType;
	}
	
	public String getAnnouncementHeading(){
		return anmtHeading;
	}
	public String getAnnouncementText(){
		return anmtText;
	}
	public String getAnnouncementTime(){
		return anmtTime;
	}
	
	
}
