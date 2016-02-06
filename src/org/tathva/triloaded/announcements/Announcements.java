package org.tathva.triloaded.announcements;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.tathva.triloaded.gcm.GcmIntentService;
import org.tathva.triloaded.gcm.Notification;
import org.tathva.triloaded.mainmenu.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.TextView;

public class Announcements extends Activity implements OnClickListener {

	Button events, lost, general, all;
	NotifyDB db;
	AnnExpandableListAdapter adapter1;
	List<String> listDataHeader;
	HashMap<String, List<String>> listDataChild;
	ExpandableListView announcements;
	List<Notification> notices = new Vector<Notification>();
	List<Announcement> list;
	int i;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		/** Fullscreen **/
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.announcements);
		
		announcements = (ExpandableListView) findViewById(R.id.EXPlvAnnouncement);
		events = (Button) findViewById(R.id.bEvents);
		lost = (Button) findViewById(R.id.bLost);
		general = (Button) findViewById(R.id.bGeneral);
		all = (Button) findViewById(R.id.bAll);
		
		db=new NotifyDB(this);
		
		//update checking code
//		if(db.checkUpdate())
	//		db.updateFromServer();
		
		new AsyncTask<Void, Void, Void>(){

			@Override
			protected Void doInBackground(Void... params) {
				db.fetchAnnoucements();
				return null;
			}
			protected void onPostExecute(Void result) {
				
				createList();
			}
			
		}.execute();
		
		
		
		Typeface bFont = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf");
		
		events.setTypeface(bFont);
		lost.setTypeface(bFont);
		general.setTypeface(bFont);
		all.setTypeface(bFont);
		
		events.setOnClickListener(this);
		lost.setOnClickListener(this);
		general.setOnClickListener(this);
		all.setOnClickListener(this);
		
		

		Log.i("aaki", "announcements");
		createList();

	}
	
	private void createList() {
		//
		list = db.getAll();
		notices.clear();
		if(list.size() != 0){
			for(i=0; i<list.size();i++){
				Log.i("debug"," "+list.get(i).gettype());
				notices.add(new Notification(list.get(i).id, Integer.valueOf(list.get(i).gettype()),
						list.get(i).getTitle(),
						list.get(i).getBody(),
						list.get(i).getTime()));
			}
			adapter1 = new AnnExpandableListAdapter(this, notices, 0);
	    	announcements.setAdapter(adapter1);
	    	announcements.setDividerHeight(1);
		}
	};
	
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.bEvents:
			notices.clear();
			
			if(list.size() != 0){
				for(i=0; i<list.size();i++){
					if(list.get(i).gettype().equals("2")){
						notices.add(new Notification(list.get(i).id, Integer.valueOf(list.get(i).gettype()),
								list.get(i).getTitle(),
								list.get(i).getBody(),
								list.get(i).getTime()));
						
					}
				}
			}
	    	adapter1 = new AnnExpandableListAdapter(this, notices, 2);
	    	announcements.setAdapter(adapter1);
	    	announcements.setDividerHeight(1);
			break;
			
		case R.id.bLost:
			notices.clear();
			if(list.size() != 0){
				for(i=0; i<list.size();i++){
					if(list.get(i).gettype().equals("3")){
						notices.add(new Notification(list.get(i).id,Integer.parseInt(list.get(i).gettype()),
								list.get(i).getTitle(),
								list.get(i).getBody(),
								list.get(i).getTime()));
					}
				}
			}
	    	adapter1 = new AnnExpandableListAdapter(this, notices, 3);
	    	announcements.setAdapter(adapter1);
	    	announcements.setDividerHeight(1);
			break;

		case R.id.bGeneral:
			notices.clear();
			if(list.size() != 0){
				for(i=0; i<list.size();i++){
					Log.i("bug", list.get(i).gettype());
					if(list.get(i).gettype().equals("1")){
						notices.add(new Notification(list.get(i).id, Integer.valueOf(list.get(i).gettype()),
								list.get(i).getTitle(),
								list.get(i).getBody(),
								list.get(i).getTime()));
					}
				}
			}
	    	adapter1 = new AnnExpandableListAdapter(this, notices, 1);
	    	announcements.setAdapter(adapter1);
	    	announcements.setDividerHeight(1);
			break;

		case R.id.bAll:
			notices.clear();
			if(list.size() != 0){
				for(i=0; i<list.size();i++){
					notices.add(new Notification(list.get(i).id, Integer.valueOf(list.get(i).gettype()),
							list.get(i).getTitle(),
							list.get(i).getBody(),
							list.get(i).getTime()));
				}
			}
	    	adapter1 = new AnnExpandableListAdapter(this, notices, 0);
	    	Log.i("debug", "bug"+announcements);
	    	announcements.setAdapter(adapter1);
	    	announcements.setDividerHeight(1);
			break;

		default:
			break;
		}

	}
	
	@Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        overridePendingTransition (R.anim.activity_open_scale, R.anim.activity_close_translate);
    }

}
