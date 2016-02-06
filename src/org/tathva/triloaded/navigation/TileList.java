package org.tathva.triloaded.navigation;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import org.tathva.triloaded.customviews.Tile;
import org.tathva.triloaded.customviews.Tile.OnExpandListener;
import org.tathva.triloaded.customviews.Tile.OnOptionClickListener;
import org.tathva.triloaded.mainmenu.R;

import android.R.integer;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class TileList extends ListView{
	
	private List<String> atms = new Vector<String>();
	private List<String> canteens = new Vector<String>();
	private List<String> dbs = new Vector<String>();
	private List<String> grounds = new Vector<String>();
	private List<String> hostels = new Vector<String>();
	private List<String> labs = new Vector<String>();
	private List<String> lecturehalls = new Vector<String>();
	private List<String> otherspots = new Vector<String>();
	private List<String> pgblocks = new Vector<String>();
	
	private int[] imagelist = new int[]{R.drawable.nav_menu_atm,R.drawable.nav_menu_canteen,
			R.drawable.nav_menu_db,R.drawable.nav_menu_ground,R.drawable.nav_menu_hostel,
			R.drawable.nav_menu_lab,R.drawable.nav_menu_lecture, R.drawable.nav_menu_other,R.drawable.nav_menu_pg};
	
	private List<String> strings = Arrays.asList("ATMS",
			"Canteens","Department Buildings","Grounds","Hostels",
			"Labs","Lecture Halls","Other Spots","PG blocks");
	
	private Context context;
	private CustomList listAdapter;
	private OnOptionClickListener optionlistener;

	public TileList(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
        
	}
	public TileList(Context context) {
		super(context);
	}

	public void setData(List<Location> loclist){
		for(Location l: loclist){
			l.getName();
			if(l.getType().equals("OTHER SPOTS")){
				otherspots.add(l.getName());
			}else if(l.getType().equals("LECTURE HALLS")){
				lecturehalls.add(l.getName());
			}else if(l.getType().equals("GROUNDS")){
				grounds.add(l.getName());
			}else if(l.getType().equals("LABS")){
				labs.add(l.getName());
			}else if(l.getType().equals("PG BLOCKS")){
				pgblocks.add(l.getName());
			}else if(l.getType().equals("DEPARTMENT BUILDINGS")){
				dbs.add(l.getName());
			}else if(l.getType().equals("CANTEENS")){
				canteens.add(l.getName());
			}else if(l.getType().equals("HOSTELS")){
				hostels.add(l.getName());
			}else if(l.getType().equals("ATMS")){
				atms.add(l.getName());
			}
		}
		listAdapter = new CustomList(context, strings);
        this.setAdapter(listAdapter);
	}
	
	public void setOnOptionClickListener(OnOptionClickListener listener){
		this.optionlistener= listener;
	}
	
	private class CustomList extends ArrayAdapter<String>{
		List<String> strings;
		boolean[] states = new boolean[25];
		public CustomList(Context context,List<String> objects) {
			super(context,R.layout.places,objects);
			strings = objects;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if(convertView == null){
				LayoutInflater inflator = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				 v = inflator.inflate(R.layout.places, null);
				 states[position] = false;
			
			}else if(!states[position]){	
				LayoutInflater inflator = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				 v = inflator.inflate(R.layout.places, null);
				 states[position] = false;
			}
			
			Tile tile = (Tile) v.findViewById(R.id.tile);
			tile.setPosition(position);
			tile.setOnOptionClickListener(optionlistener);
			tile.setOnExpandListener(new OnExpandListener() {
				
				@Override
				public void onExpand(int position) {
					for(int i=0;i<listAdapter.getCount();i++){
						if(position == i){
							states[position]=true;
						}else{
							states[i] =false;
						}
					}
					for(int i =0;i<getChildCount();i++){
							View v = getChildAt(i);
							Tile tile = (Tile) v.findViewById(R.id.tile);
							if(tile.isOpen() && tile.getPosition() != position){
								tile.close();
							}
						
					}
					
				}
			});
			
			tile.setHeading(strings.get(position));
			tile.setImage(imagelist[position]);
			
			switch(position){
			case 0:tile.setStrings(atms);break;
			case 1: tile.setStrings(canteens);break;
			case 2: tile.setStrings(dbs);break;
			case 3: tile.setStrings(grounds);break;
			case 4: tile.setStrings(hostels);break;
			case 5: tile.setStrings(labs);break;
			case 6: tile.setStrings(lecturehalls);break;
			case 7: tile.setStrings(otherspots);break;
			case 8: tile.setStrings(pgblocks);break;		
			
			}
			return v;
		}
		
	}

}
