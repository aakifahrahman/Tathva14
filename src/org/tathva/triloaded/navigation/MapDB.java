package org.tathva.triloaded.navigation;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class MapDB  extends SQLiteOpenHelper {
	

	public MapDB(Context context) {
		super(context, "tathva.db", null, 1);
		// TODO Auto-generated constructor stub
	}

	public MapDB(Context context, String name, CursorFactory factory,
			int version, DatabaseErrorHandler errorHandler) {
		super(context, name, factory, version, errorHandler);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String command;
		/*command="CREATE TABLE locations (field1,field2,field3,field4)";
		db.execSQL(command);*/

	}
	

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	public List<Location> getLocations(){
		List<Location> list=new ArrayList<Location>();
		String query="SELECT * FROM locations";
		SQLiteDatabase db=this.getReadableDatabase();
		Cursor cursor=db.rawQuery(query, null);
		
		if(cursor.moveToFirst()){
			do{
				Location loc=new Location(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3));
				list.add(loc);
			}while(cursor.moveToNext());
		}
		
		return list;
	}
	
	public Location getPlace(String place){
		if(place==null || place.equals("")){
			return null;
		}
		SQLiteDatabase db=this.getReadableDatabase();
		
		Cursor cursor=db.query("locations", null, "place=?",new String[]{place}, null,null,null);
		if(cursor!=null)
			cursor.moveToFirst();
		
		Location loc=new Location(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3));
		return loc;
	}

}
