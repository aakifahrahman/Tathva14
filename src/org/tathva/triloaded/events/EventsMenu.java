package org.tathva.triloaded.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.tathva.triloaded.gcm.ScriptRunner;
import org.tathva.triloaded.gcm.ScriptRunner.ScriptFinishListener;
import org.tathva.triloaded.info.InfoWebView;
import org.tathva.triloaded.mainmenu.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ListView;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.SlidingDrawer.OnDrawerScrollListener;
import android.widget.TextView;
import android.widget.Toast;

public class EventsMenu extends Activity{
	
	private static final String SQLVARIABLES = "sqlvariables";
	private static final String MAXID = "maxid";
	
	Button sdbcomp, sdbwork, sdblect, sdbexh;
	Button nites;
	TextView nites_centre;

	ExpandableListAdapter adapter1;
	List<String> listDataHeader;
	HashMap<String, List<String>> listDataChild;

	ExpandableListView lvC;

	EventsMenuDB db=new EventsMenuDB(this);

	@SuppressWarnings("deprecation")
	SlidingDrawer sdcomp, sdwork, sdlect, sdexh;

	ListView lvW, lvL, lvE;
	/* r */
	private List<Event> competitionEvents;
	private List<Event> workshopEvents;
	private List<Event> lectureEvents;
	private List<Event> exhibitionEvents;
	private Updater updater;
	
	/* r*/

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/** Fullscreen **/
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.events_menu);
		
		updater = new Updater(this);
		
		Typeface fontastic = Typeface.createFromAsset(getAssets(), "fonts/fontastique.ttf");
		
		Typeface tf1 = Typeface.createFromAsset(getAssets(),
				"fonts/fontastique.ttf");
		Typeface fQuotes = Typeface.createFromAsset(getAssets(),
				"fonts/OpenSans-Regular.ttf");
		
		
		//ver 1.2 Update
		final int localmaxid = getMaxId(this);
		
		ScriptRunner run = new ScriptRunner(new ScriptFinishListener() {
			
			@Override
			public void finish(String result, int resultCode) {
				
				if(resultCode == ScriptRunner.SUCCESS){
					try {
						JSONObject obj = new JSONObject(result);
						int id = obj.getInt("maxid");
						Log.i("result", " "+id);
						
						if(id>localmaxid){
							//need updation
							ScriptRunner runner = new ScriptRunner(new ScriptFinishListener() {
								
								@Override
								public void finish(String result, int resultCode) {
									
									if(resultCode == ScriptRunner.SUCCESS){
										try {
											JSONArray jArray = new JSONArray(result);
											for(int i=0;i<jArray.length();i++){
												JSONObject ob = jArray.getJSONObject(i);
												String sql = ob.getString("sqls");
												updater.ExecSQL(sql);
												
												if(i+1 == jArray.length()){
													Log.i("maxid", "maxid reaced");
													setMaxId(EventsMenu.this, ob.getInt("id"));
													
												}
											}
										
										} catch (JSONException e) {
											
											e.printStackTrace();
										}
									}
									
								}
							});
							runner.execute("http://kr.comze.com/Update/getSqls.php?maxid="+localmaxid);
						}
					
					} catch (JSONException e) {
					
						e.printStackTrace();
					}
					
				}
				
			}
		});
		
		run.execute("http://kr.comze.com/Update/SqlMaxId.php");
		
		//end of Update
		
		
		nites = (Button) findViewById(R.id.nitesBtn);
		nites.setTypeface(fontastic);
		nites.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(EventsMenu.this, NormalEvent.class);
				i.putExtra("id", "NIT");
				startActivity(i);
				overridePendingTransition (R.anim.activity_open_translate, R.anim.activity_close_scale);
			}
		});

		/** COMPETITITON DRAWER **/
		sdbcomp = (Button) findViewById(R.id.sdbCompetitions);
		sdcomp = (SlidingDrawer) findViewById(R.id.sdCompetitions);
		//qComp = (TextView)findViewById(R.id.tvQuotesComp);
		//qComp.setTypeface(fQuotes);

		sdbcomp.setTypeface(tf1);
		sdcomp.setOnDrawerOpenListener(new OpenListener(R.id.sdbCompetitions));
		sdcomp.setOnDrawerCloseListener(new CloseListener(R.id.sdbCompetitions));
		sdcomp.setOnDrawerScrollListener(new ScrollListener(R.id.sdbCompetitions));

		/** LECTURES DRAWER **/
		sdblect = (Button) findViewById(R.id.sdbLectures);
		sdlect = (SlidingDrawer) findViewById(R.id.sdLectures);
		//qLect = (TextView)findViewById(R.id.tvQuotesLect);
		//qLect.setTypeface(fQuotes);

		sdblect.setTypeface(tf1);
		sdlect.setOnDrawerOpenListener(new OpenListener(R.id.sdbLectures));
		sdlect.setOnDrawerCloseListener(new CloseListener(R.id.sdbLectures));	
		sdlect.setOnDrawerScrollListener(new ScrollListener(R.id.sdbLectures));

		/** WORKSHOP DRAWER **/
		sdbwork = (Button) findViewById(R.id.sdbWorkshops);
		sdwork = (SlidingDrawer) findViewById(R.id.sdWorkshops);
		//qWork = (TextView)findViewById(R.id.tvQuotesWork);
		//qWork.setTypeface(fQuotes);

		sdbwork.setTypeface(tf1);
		sdwork.setOnDrawerOpenListener(new OpenListener(R.id.sdbWorkshops));
		sdwork.setOnDrawerCloseListener(new CloseListener(R.id.sdbWorkshops));
		sdwork.setOnDrawerScrollListener(new ScrollListener(R.id.sdbWorkshops));

		/** EXHIBITIONS DRAWER **/
		sdbexh = (Button) findViewById(R.id.sdbExhibitions);
		sdexh = (SlidingDrawer) findViewById(R.id.sdExhibitions);
		//qExhi = (TextView)findViewById(R.id.tvQuotesExhi);
		//qExhi.setTypeface(fQuotes);

		sdbexh.setTypeface(tf1);
		sdexh.setOnDrawerOpenListener(new OpenListener(R.id.sdbExhibitions));
		sdexh.setOnDrawerCloseListener(new CloseListener(R.id.sdbExhibitions));
		sdexh.setOnDrawerScrollListener(new ScrollListener(R.id.sdbExhibitions));

		/*List view click event */
		OnItemClickListener itemClick = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position,
					long id) {
				Event ob = (Event) parent.getItemAtPosition(position);
				String eventId = ob.eventId;
				Intent intent = null;
				switch(ob.eventType){
					case Event.LECTURES: 	
					case Event.WORKSHOP:	
					case Event.EXHIBITION:	intent = new Intent(EventsMenu.this, NormalEvent.class);break;
					default: return;
				}
				intent.putExtra("id", eventId);
				startActivity(intent);
				overridePendingTransition (R.anim.activity_open_translate, R.anim.activity_close_scale);
			}
			
		};
		
		/*Setting Up ListView */
		lvL = (ListView) findViewById(R.id.listViewL);
		lvW = (ListView) findViewById(R.id.listViewW);
		lvE = (ListView) findViewById(R.id.listViewE);
		

		EventListAdapter exhibitionAdapter = new EventListAdapter(this, R.layout.events_list_item,db.getExhibitionsList());
		lvE.setAdapter(exhibitionAdapter);
		lvE.setOnItemClickListener(itemClick);
	
		EventListAdapter lecturepAdapter = new EventListAdapter(this, R.layout.events_list_item, db.getLecturesList());
		lvL.setAdapter(lecturepAdapter);
		lvL.setOnItemClickListener(itemClick);
				
		EventListAdapter workshopAdapter = new EventListAdapter(this, R.layout.events_list_item, db.getWorkshopsList());
		lvW.setAdapter(workshopAdapter);
		lvW.setOnItemClickListener(itemClick);
		
		/*Expandable Drawer */
		lvC = (ExpandableListView) findViewById(R.id.EXPlistViewC);
		prepareListData();
		sdbcomp.setTypeface(tf1);
		adapter1 = new ExpandableListAdapter(this, listDataHeader,
				listDataChild);
		// setting list adapter
		lvC.setAdapter(adapter1);
		lvC.setDividerHeight(0);
		// Listview Group click listener
		lvC.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				return false;
			}
		});

		// Listview Group expanded listener
		lvC.setOnGroupExpandListener(new OnGroupExpandListener() {

			@Override
			public void onGroupExpand(int groupPosition) {
			}
		});

		// Listview Group collasped listener
		lvC.setOnGroupCollapseListener(new OnGroupCollapseListener() {

			@Override
			public void onGroupCollapse(int groupPosition) {

			}
		});

		// Listview on child click listener
		lvC.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
					
				Intent intent = new Intent(EventsMenu.this, Competition.class);
				String eventId,name;
				name=listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);
				eventId=db.getEventId(name);
				intent.putExtra("id", eventId);
				
				startActivity(intent);
				overridePendingTransition (R.anim.activity_open_translate, R.anim.activity_close_scale);
				return false;
			}
		});
		

	}

	@SuppressWarnings("deprecation")
	public void closeAllDrawersExcept(int openOnlyThis) {
		// TODO Auto-generated method stub
		switch (openOnlyThis) {
		case R.id.sdCompetitions:
		case R.id.sdbCompetitions:
			sdwork.close();
			sdlect.close();
			sdexh.close();
			break;
		case R.id.sdLectures:
		case R.id.sdbLectures:
			sdwork.close();
			sdcomp.close();
			sdexh.close();
			break;
		case R.id.sdWorkshops:
		case R.id.sdbWorkshops:
			sdexh.close();
			sdlect.close();
			sdcomp.close();
			break;
		case R.id.sdExhibitions:
		case R.id.sdbExhibitions:
			sdwork.close();
			sdlect.close();
			sdcomp.close();
			break;
		default:
			break;
		}

	}

	private class OpenListener implements OnDrawerOpenListener{
		int id;
		public OpenListener(int id) {
			this.id = id;
		}
		@Override
		public void onDrawerOpened() {
			switch(id){
			case R.id.sdbCompetitions : sdbcomp.setBackgroundResource(R.drawable.drawer_right_open);
										
										closeAllDrawersExcept(R.id.sdCompetitions);
										break;
			case R.id.sdbLectures: 		sdblect.setBackgroundResource(R.drawable.drawer_bottom_open);
									  	
									  	closeAllDrawersExcept(R.id.sdLectures);
									  	break;
			case R.id.sdbExhibitions: 	sdbexh.setBackgroundResource(R.drawable.drawer_left_open);
										
										closeAllDrawersExcept(R.id.sdExhibitions); 
										break;
			case R.id.sdbWorkshops:		sdbwork.setBackgroundResource(R.drawable.drawer_top_open);
										
										closeAllDrawersExcept(R.id.sdWorkshops);
										break;
			}
			
		}
	}
	
	private class CloseListener implements OnDrawerCloseListener{
		int id;
		public CloseListener(int id) {
			this.id = id;
		}
		@Override
		public void onDrawerClosed() {
			switch(id){
			case R.id.sdbCompetitions : sdbcomp.setBackgroundResource(R.drawable.drawer_right_def);
										
										break;
			case R.id.sdbLectures: 		sdblect.setBackgroundResource(R.drawable.drawer_bottom_def);
										
									  	break;
			case R.id.sdbExhibitions: 	sdbexh.setBackgroundResource(R.drawable.drawer_left_def);
										
										break;
			case R.id.sdbWorkshops:		sdbwork.setBackgroundResource(R.drawable.drawer_top_def);
										
										break;
			}	
		}
	}
	
	private class ScrollListener implements OnDrawerScrollListener{
		int id;
		public ScrollListener(int id) {
			this.id = id;
		}
		@Override
		public void onScrollEnded() {
			
		}
		@Override
		public void onScrollStarted() {
			switch(id){
			case R.id.sdbCompetitions :	closeAllDrawersExcept(R.id.sdCompetitions);break;
			case R.id.sdbLectures: 		closeAllDrawersExcept(R.id.sdLectures);break;
			case R.id.sdbExhibitions: 	closeAllDrawersExcept(R.id.sdExhibitions);break;
			case R.id.sdbWorkshops:		closeAllDrawersExcept(R.id.sdWorkshops);break;
			}	
			
		}
	}
	
	private void prepareListData() {

		listDataHeader =  db.getBranchList();
		listDataChild = new HashMap<String, List<String>>();

		for(String s: listDataHeader){
			List<String> competitions= db.getCompList(s);
			listDataChild.put(s, competitions);
		}
		
	}
	
	 @Override
	    public void onBackPressed() {
	        // TODO Auto-generated method stub
	        super.onBackPressed();
	        overridePendingTransition (R.anim.activity_open_scale, R.anim.activity_close_translate);
	    }
	 
	 public static int getMaxId(Context context) {
			SharedPreferences Info = context.getSharedPreferences(SQLVARIABLES, 0);
	        int count = Info.getInt(MAXID, 0);
	        return count;
	    }
		
		public static void setMaxId(Context context,int count) {
			SharedPreferences Info = context.getSharedPreferences(SQLVARIABLES, 0);
	        SharedPreferences.Editor editor = Info.edit();
	        editor.putInt(MAXID, count);
	        editor.commit();
	    }
	 
	 
}