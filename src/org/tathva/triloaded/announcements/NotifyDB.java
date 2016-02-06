package org.tathva.triloaded.announcements;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;

public class NotifyDB extends SQLiteOpenHelper {
	
	private String server = "http://kr.comze.com";

	public NotifyDB(Context context) {
		super(context, "tathva.db",null, 1);
	}

	public NotifyDB(Context context, String name, CursorFactory factory,
			int version, DatabaseErrorHandler errorHandler) {
		super(context, name, factory, version, errorHandler);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
//		String sql="CREATE TABLE Announcements( code TEXT, title TEXT PRIMARY KEY, body TEXT, time TEXT);";
//		db.execSQL(sql);

	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

	}
	
	public boolean fetchAnnoucements(){
		
		URL url;
		try {
			url = new URL(server+"/get_announcements.php?maxid="+getMaxLocalId());
			HttpURLConnection con=(HttpURLConnection) url.openConnection();
			InputStreamReader isr=new InputStreamReader(con.getInputStream());
			BufferedReader reader=new BufferedReader(isr);
			
			String  line;
			StringBuilder sb=new StringBuilder();
			
			while((line=reader.readLine())!=null){
				sb.append(line);
			}
			
			Log.d("out", sb.toString());
			String json = sb.toString();
			//add to database
			JSONArray array = new JSONArray(json);
			for(int i =0;i<array.length();i++){
				
				JSONObject ob = array.getJSONObject(i);
				String title,body,time,type;
				int id;
				id = ob.getInt("id");
				title = ob.getString("title");
				body = ob.getString("body");
				time = ob.getString("time");
				type = ob.getString("type");
				
				Log.d("notify db", title);
				
				ContentValues val=new ContentValues();
				val.put("id", id);
				val.put("title", title);
				val.put("body", body);
				val.put("time", time);
				val.put("type", type);
				SQLiteDatabase db=getWritableDatabase();
				Log.i("debu  insert from ", type+"");
				if(!type.equals(null))
				db.insert("announcements", null, val);
				db.close();
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	private String getMaxLocalId() {
			SQLiteDatabase db=this.getReadableDatabase();
			String query= "SELECT max(`id`) FROM Announcements;";
			Cursor cursor=db.rawQuery(query, null);
			cursor.moveToFirst();
			String update=cursor.getString(0);
			cursor.close();
//			db.close();
			if(update==null)
				update="0";
			return update;

	}

	public void updateFromServer(){
		new AsyncTask<Void, Void, Void>(){

			@Override
			protected Void doInBackground(Void... arg0) {
				try{
					URL url= new URL(server+"/get_announcements.php?maxid=");
					HttpURLConnection con=(HttpURLConnection) url.openConnection();
					InputStreamReader isr=new InputStreamReader(con.getInputStream());
					BufferedReader reader=new BufferedReader(isr);
					
					String  line;
					StringBuilder sb=new StringBuilder();
					
					while((line=reader.readLine())!=null){
						sb.append(line);
					}
					String json = sb.toString();
					//add to database
					JSONArray array = new JSONArray(json);
					for(int i =0;i<array.length();i++){
						
						JSONObject ob = array.getJSONObject(i);
						String title,body,time, type;
						int id;
						id = ob.getInt("id");
						title = ob.getString("title");
						body = ob.getString("body");
						time = ob.getString("time");
						type = ob.getString("type");
						
						Log.d("notify db", title);
						
						ContentValues val=new ContentValues();
						val.put("id", id);
						val.put("title", title);
						val.put("body", body);
						val.put("time", time);
						val.put("type", type);
						
						SQLiteDatabase db=getWritableDatabase();
						db.insert("announcements", null, val);
						db.close();
						
					}
					//return json
					Log.d("updater", sb.toString());
					
					
				}
				catch(Exception e){
					Log.e("error", e.getMessage());
				}
				return null;
			}
			
		}.execute();
	}
	
	public void Add(Announcement announcement){
		SQLiteDatabase db=getWritableDatabase();
		ContentValues val=new ContentValues();
		val.put("id", announcement.type);
		val.put("title", announcement.title);
		val.put("body", announcement.body);
		val.put("time", announcement.time);
		try{
			db.insert("Announcements", null, val);
		}
		catch(Exception e){
			Log.d("Error inserting", e.getMessage());
		}
		
		
	}
	
	public List<Announcement> getAll(){
		SQLiteDatabase db=getReadableDatabase();
		List<Announcement> list=new ArrayList<Announcement>();
		Cursor cur=db.rawQuery("SELECT * FROM Announcements ORDER BY time DESC", null);
		if(cur.moveToFirst()){
			do{
				Announcement an=new Announcement(
						cur.getInt(cur.getColumnIndex("id")), 
						cur.getString(cur.getColumnIndex("type")),
						cur.getString(cur.getColumnIndex("title")),
						cur.getString(cur.getColumnIndex("body")),
						cur.getString(cur.getColumnIndex("time")));
				list.add(an);
			}while(cur.moveToNext());
		}
		
		return list;
	}

}
