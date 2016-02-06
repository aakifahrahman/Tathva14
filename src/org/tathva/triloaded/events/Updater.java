package org.tathva.triloaded.events;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.tathva.triloaded.gcm.ScriptRunner;
import org.tathva.triloaded.gcm.ScriptRunner.ScriptFinishListener;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.renderscript.Script;
import android.util.Log;

public class Updater extends SQLiteOpenHelper {

	private String server = "http://kr.comze.com";
	
	public Updater(Context context) {
		super(context, "tathva.db", null,1);
		// TODO Auto-generated constructor stub
	}
	
	public void ExecSQL(String sql){
		try{
			if(sql!=null){
				SQLiteDatabase db=getReadableDatabase();
				db.execSQL(sql);
				db.close();
			}
		}catch(Exception e){
			Log.i("bug", "sql "+e.toString());
		}
	}
	
//	public void Update(){
//		new AsyncTask<Void, Void, Void>(){
//
//			@Override
//			protected Void doInBackground(Void... arg0) {
//				try {
//					URL url=new URL("http://tri.comule.com/sql/sqlupdate.sql");
//					HttpURLConnection con= (HttpURLConnection) url.openConnection();
//					InputStreamReader isr= new InputStreamReader(con.getInputStream());
//					BufferedReader reader= new BufferedReader(isr);
//					String line;
//					List<String> sql=new ArrayList<String>();
//					
//					while((line=reader.readLine())!=null){
//						if(line.startsWith("<!--"))
//							break;
//						sql.add(line);
//					}
//					
//					SQLiteDatabase db=getReadableDatabase();
//					for(String com: sql){
//						Log.d("sql",com);
//					db.execSQL(com);
//					
//					}
//					db.close();
//					
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				return null;
//			}
//			
//		//}.execute();
//		};
//	}
	
	public void updateEventContents(final String eventID){
		Log.d("updater", "updating "+ eventID);
		new AsyncTask<Void, Void, Void>(){

			@Override
			protected Void doInBackground(Void... arg0) {
				try{
					URL url= new URL(server+"/contenthtml.php?EventId="+eventID);
					HttpURLConnection con=(HttpURLConnection) url.openConnection();
					InputStreamReader isr=new InputStreamReader(con.getInputStream());
					BufferedReader reader=new BufferedReader(isr);
					
					String  line;
					StringBuilder sb=new StringBuilder();
					
					while((line=reader.readLine())!=null){
						sb.append(line);
					}
					Log.d("updater", sb.toString());
					byte[] img1,img2,img3;
					img1=downloadImage(server+"/img1.php?EventId="+eventID);
					img2=downloadImage(server+"/img2.php?EventId="+eventID);
					img3=downloadImage(server+"/img3.php?EventId="+eventID);
					ContentValues val=new ContentValues();
					val.put("HTML", sb.toString());
					val.put("img1", img1);
					val.put("img2", img2);
					val.put("img3", img3);
					
					SQLiteDatabase db=getWritableDatabase();
					db.update("events", val, "id=?", new String[]{eventID});
					db.close();
				}
				catch(Exception e){
					Log.e("error", e.getMessage());
				}
				return null;
			}
			
		}.execute();
	}
	
	public byte[] downloadImage(String addr){
		Log.d("downloadimg", "downloading "+ addr);
		ByteArrayOutputStream bais = new ByteArrayOutputStream();
		InputStream is = null;
		try {
			  URL url=new URL(addr);
			  is = url.openStream ();
			  byte[] byteChunk = new byte[4096]; // Or whatever size you want to read in at a time.
			  int n;
		
			  while ( (n = is.read(byteChunk)) > 0 ) {
			    bais.write(byteChunk, 0, n);
			  }
		}
		catch(Exception e){
			
		}
		return bais.toByteArray();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}
	

}
