package org.tathva.triloaded.customviews;

import org.tathva.triloaded.events.Event;
import org.tathva.triloaded.mainmenu.R;

import com.google.android.gms.internal.ev;

import android.R.anim;
import android.app.ActionBar.LayoutParams;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;
import android.widget.TextView;

public class ScheduleWindow extends Dialog {

	Event event;
	
	public ScheduleWindow(Context context, Event event) {
		super(context,R.style.ScheduleDialog);
		
		this.event=event;
		 
	} 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		getWindow().setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		
		
		TabHost tab = (TabHost) getLayoutInflater().inflate(R.layout.schedule_details, null);
		tab.setup();
		
		TabHost.TabSpec tabpage1 = tab.newTabSpec("tag1");
		tabpage1.setIndicator(getTabIndicator(getContext(),"OCT 31"));
		tabpage1.setContent(new ContentFactory(event));
		tab.addTab(tabpage1);
		
		TabHost.TabSpec tabpage2 = tab.newTabSpec("tag2");
		tabpage2.setIndicator(getTabIndicator(getContext(),"NOV 1"));
		tabpage2.setContent(new ContentFactory(event));
		tab.addTab(tabpage2);
		
		TabHost.TabSpec tabpage3 = tab.newTabSpec("tag3");
		tabpage3.setIndicator(getTabIndicator(getContext(),"NOV 2"));
		tabpage3.setContent(new ContentFactory(event));
		tab.addTab(tabpage3);
		
		setContentView(tab);
		
	}
	private View getTabIndicator(Context context,String text) {
		View view = LayoutInflater.from(context).inflate(R.layout.tabindicator, null);
		TextView tv = (TextView) view.findViewById(R.id.tabText);
		tv.setText(text);
		return view;
	}
	
	
	private class ContentFactory implements TabContentFactory{
		Event event;
		public ContentFactory(Event e) {
			this.event = e;
		}
		@Override
		public View createTabContent(String tag) {
			View view = LayoutInflater.from(getContext()).inflate(R.layout.schedule_content, null);
			//TextView statusTv = (TextView) view.findViewById(R.id.statusText);
			TextView timeTv = (TextView) view.findViewById(R.id.timeText);
			TextView venueTv = (TextView) view.findViewById(R.id.venueText);
			//Setting Data
			if(tag.equals("tag1")){
				/*if(event.status_d1==null){
					statusTv.setText("Status : Not Updated");
				}
				else{
					statusTv.setText("Status : "+event.status_d1);
				}*/
				
				Log.i("tag",""+ event.venue_d1);
				
				if(event.time_d1==null){
					timeTv.setText("Time : Not Updated");
				}
				else{
					if(!event.time_d1.equals("na")){
						timeTv.setText("Time : "+event.time_d1);
					}else{
						timeTv.setText("Not happening on this day!!");
					}	
				}
				
				if(event.venue_d1==null){
					venueTv.setText("Venue : Not Updated");
				}
				else{
					if(!event.venue_d1.equals("na")){
						venueTv.setText("Venue : "+event.venue_d1);
					}else{
						venueTv.setText("");
					}
				}
			}
			else if(tag.equals("tag2")){
				Log.i("tag", ""+event.venue_d2);
				/*if(event.status_d2==null){
					statusTv.setText("Status : Not Updated");
				}
				else{
					statusTv.setText("Status : "+event.status_d2);
				}
				*/
				
				if(event.time_d2==null){
					timeTv.setText("Time : Not Updated");
				}
				else{
					if(!event.time_d2.equals("na")){
						timeTv.setText("Time : "+event.time_d2);
					}else{
						timeTv.setText("Not happening on this day!!");
					}
				}
				
				if(event.venue_d2==null){
					venueTv.setText("Venue : Not Updated");
				}
				else{
					if(!event.venue_d2.equals("na")){
						venueTv.setText("Venue : "+event.venue_d2);
					}else{
						venueTv.setText("");
					}
				}
			}
			else{
				Log.i("tag", ""+event.venue_d2);
				/*if(event.status_d3==null){
					statusTv.setText("Status : Not Updated");
				}
				else{
					statusTv.setText("Status : "+event.status_d3);
				}
				*/
				
				if(event.time_d3==null){
					timeTv.setText("Time : Not Updated");
				}
				else{
					if(!event.time_d3.equals("na")){
						timeTv.setText("Time : "+event.time_d3);
					}else{
						timeTv.setText("Not happening on this day!!");
					}
				}
				
				if(event.venue_d3==null){
					venueTv.setText("Venue : Not Updated");
				}
				else{
					if(!event.venue_d3.equals("na")){
						venueTv.setText("Venue : "+event.venue_d3);
					}else{
						venueTv.setText("");
					}
				}
			}

			
			return view;
		}
		
	}
	
	
	
}
