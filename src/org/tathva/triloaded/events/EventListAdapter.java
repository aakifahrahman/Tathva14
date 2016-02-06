package org.tathva.triloaded.events;

import java.util.List;

import org.tathva.triloaded.mainmenu.R;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;


public class EventListAdapter extends ArrayAdapter<Event> {
	
	
	
	private Context context;
	private List<Event> eventsData;
	LayoutInflater inflator;

	
	public EventListAdapter(Context context, int resource,List<Event> eventsData) {
		super(context, resource, eventsData);
		this.context = context;
		this.eventsData = eventsData;
		
		inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if(convertView == null){
			 v = inflator.inflate(R.layout.events_list_item, null);
		}
		
		Typeface fList = Typeface.createFromAsset(context.getAssets(),
				"fonts/OpenSans-Regular.ttf");

		TextView tv = (TextView) v.findViewById(R.id.tvListItem);
		tv.setTypeface(fList);
		tv.setText(getItem(position).eventName);
		
		
		return v;
	}
	

}

//public class EventListAdapter extends ArrayAdapter<String> {
//	
//	
//	
//	private Context context;
//	private List<String> eventsList;
//	LayoutInflater inflator;
//
//	
//	public EventListAdapter(Context context, int resource,List<String> eventsData) {
//		super(context, resource, eventsData);
//		this.context = context;
//		this.eventsList = eventsData;
//		
//		inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		
//	}
//	
//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
//		View v = convertView;
//		if(convertView == null){
//			 v = inflator.inflate(R.layout.event_list_item, null);
//		}
//		
//		TextView tv = (TextView) v.findViewById(R.id.listItem);
//		tv.setText(getItem(position));
//		
//		return v;
//	}
//	
//
//}

