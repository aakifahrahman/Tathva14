package org.tathva.triloaded.anubhava;

import java.io.File;
import java.util.List;
import java.util.Vector;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class AnubhavaDB extends SQLiteOpenHelper {

	public static final String database ="anubhava";
	public static final String postTable ="posts";
	public static final String postTable_id ="id";
	public static final String postTable_user_id ="user_id";
	public static final String postTable_user_name ="user_name";
	public static final String postTable_caption ="caption";
	public static final String postTable_image_url ="image_url";	
	public static final String postTable_local_post_url = "local_post_url";	
	public static final String postTable_local_profile_url ="local_profile_url";
	
	private Context context;
	private int MAX_LOCAL_POSTS =  30;
	
	public AnubhavaDB(Context context) {
		super(context, database, null, 1);
		this.context = context;
		 
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL("CREATE TABLE "+postTable+" ("+postTable_id+ " INTEGER PRIMARY KEY, "+
				postTable_user_id+ " TEXT,"+
				postTable_user_name+" TEXT,"+
				postTable_caption+ " TEXT,"+
				postTable_image_url+ " TEXT,"+
				postTable_local_post_url+" TEXT,"+
				postTable_local_profile_url+" TEXT);");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS "+postTable);
		db.execSQL("CREATE TABLE "+postTable+" ("+postTable_id+ " INTEGER PRIMARY KEY, "+
				postTable_user_id+ " TEXT,"+
				postTable_user_name+" TEXT,"+
				postTable_caption+ " TEXT,"+
				postTable_image_url+ " TEXT,"+
				postTable_local_post_url+" TEXT,"+
				postTable_local_profile_url+" TEXT);");
		
	}
	
	public int getCount(){
		SQLiteDatabase db=this.getReadableDatabase();
		String query= "SELECT count(*) FROM posts;";
		Cursor cursor=db.rawQuery(query, null);
		cursor.moveToFirst();
		String count = cursor.getString(0);
		cursor.close();
		
		if(count==null)
			count="0";
		
		return Integer.parseInt(count);
	}
	
	public void deleteFromEnd(){
		SQLiteDatabase db=this.getWritableDatabase();
		int count = getCount();
		Log.i("bug", "count :"+count);
		if(count > MAX_LOCAL_POSTS){
			//have to delete 
			int toDelete = count - MAX_LOCAL_POSTS;
			String sql = "SELECT * FROM posts ORDER BY id ASC LIMIT 0,"+toDelete;
			Log.i("bug", sql);
			Cursor cur = db.rawQuery(sql,new String[]{});
			cur.moveToFirst();
			for(int i=0;i<cur.getCount();i++){
					File post = new File(cur.getString(5));
					File prof = new File(cur.getString(6));
					Boolean b = post.delete();
					Boolean c = prof.delete();
					Log.d("getPosts", "iterates :: "+i+": delete-"+b+" "+c);
				
					cur.moveToNext();	
			}
			cur.close();
			db.execSQL("DELETE FROM posts WHERE id IN (SELECT id FROM posts ORDER BY id ASC LIMIT 0,"+toDelete+" )");
			
		}
	}
	
	public void deletePost(String num){
		SQLiteDatabase db=this.getWritableDatabase();
		db.execSQL("DELETE FROM posts WHERE id="+num);
	}
	
	public void addPhotosAfterLoadMore(List<Photo> photos){
		SQLiteDatabase db=this.getWritableDatabase();
		
		/*if(getCount() == MAX_LOCAL_POSTS){
			//Dont write Anymore!!!
			return;
		}else{
			int maxWritable = MAX_LOCAL_POSTS - getCount();
			if(photos.size()>maxWritable){
				for(int i = 0;i<maxWritable;i++){
					 Photo photo = photos.get(i);
					 writeValues(photo, db);
				}
			}else{
				for(int i = 0;i<photos.size();i++){
					 Photo photo = photos.get(i);
					 writeValues(photo, db);
				}
			}
		}*/
	}
	
	public void updatePosts(List<Photo> photos){
		SQLiteDatabase db=this.getWritableDatabase();
		if(photos == null){
			return;
		}
		//if database contains more than 30,
		//  delete photos.size() number of elements from db
		// and add new photos
		Log.d("count", "local count "+ String.valueOf(getCount()));
		Log.d("count", "new count "+ String.valueOf(photos.size()));
		/*int excess=(getCount()+photos.size())-MAX_LOCAL_POSTS;
//		if(getCount()>photos.size())
		
		if(excess>0){
			Log.i("debug", "excess of posts!! delete "+excess+" posts");
			deleteFromEnd(excess);
			
		}
//			//
//			;
//		}
		/*
		File file = new File(path);
		if(file.exists()){
			file.delete();
		}
		*/
		
		for(int i = 0;i<photos.size();i++){
			 Photo photo = photos.get(i);
			 writeValues(photo, db);
		}
		
	}
	
	private void writeValues(Photo photo, SQLiteDatabase db){
		ContentValues cv = new ContentValues();
		 cv.put(postTable_id,photo.getId() );
		 cv.put(postTable_user_id, photo.getUser_id());
		 cv.put(postTable_user_name, photo.getUser_name());
		 cv.put(postTable_caption, photo.getCaption());
		 cv.put(postTable_image_url, photo.getimage_url());
		 cv.put(postTable_local_post_url, photo.getLocal_post_url());
		 cv.put(postTable_local_profile_url, photo.getLocal_profile_url());
				
		 db.insert(postTable, null, cv);
		 Log.d("insert","inserting postid = "+photo.getId());
	}
	
	public List<Photo> getPosts(String minid,int count){
		Log.i("debug","db.getPosts is Called :"+minid);
		
		SQLiteDatabase DB = this.getReadableDatabase();
		Cursor cur=null;
		if(minid!=null)
			cur = DB.rawQuery("SELECT * FROM "+postTable+" WHERE id < "+ minid +" ORDER BY id DESC LIMIT 0,4",new String[]{});
		else
			cur = DB.rawQuery("SELECT * FROM "+postTable+" ORDER BY id DESC LIMIT 0,"+count,new String[]{});
		
		
		List <Photo> data = new Vector<Photo>();
		cur.moveToFirst();
		for(int i=0;i<cur.getCount();i++){
			
				Photo photo = new Photo(cur.getString(0),
										cur.getString(1),
										cur.getString(2),
										cur.getString(3),
										cur.getString(4),
										cur.getString(5),
										cur.getString(6));
				data.add(photo);
				Log.d("getPosts", photo.getCaption());
				cur.moveToNext();	
		}
		cur.close();
		return data;
		
	}
	
	public String getMaxUpdate(){
		SQLiteDatabase db=this.getReadableDatabase();
		String query= "SELECT max(`id`) FROM posts;";
		Cursor cursor=db.rawQuery(query, null);
		cursor.moveToFirst();
		String update=cursor.getString(0);
		cursor.close();
//		db.close();
		if(update==null)
			update="0";
		return update;
	}
	
	public String getMinId(){
		SQLiteDatabase db=this.getReadableDatabase();
		String query= "SELECT min(`id`) FROM posts;";
		Cursor cursor=db.rawQuery(query, null);
		cursor.moveToFirst();
		String update=cursor.getString(0);
		cursor.close();
//		db.close();
		if(update==null)
			update="0";
		return update;
	}

	

}
