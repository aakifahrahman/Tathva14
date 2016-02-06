package org.tathva.triloaded.events;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.tathva.triloaded.customviews.NavigationDialog;
import org.tathva.triloaded.customviews.ResultsDialog;
import org.tathva.triloaded.customviews.ScheduleWindow;
import org.tathva.triloaded.customviews.ShareDailog;
import org.tathva.triloaded.customviews.NavigationDialog.OnClickPlaceListener;
import org.tathva.triloaded.customviews.ShareDailog.OnClickEventListener;
import org.tathva.triloaded.gcm.ScriptRunner;
import org.tathva.triloaded.gcm.ScriptRunner.ScriptFinishListener;
import org.tathva.triloaded.mainmenu.R;
import org.tathva.triloaded.navigation.Map;

import com.facebook.Session;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphObject;
import com.facebook.model.OpenGraphAction;
import com.facebook.model.OpenGraphObject;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.WebDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class NormalEvent extends Activity implements OnClickListener{
	
	/*fb*/
	private static String APP_ID = "279143188956828";
	private static String APP_SECRET = "09d230d7c7c2afa383ee225409309e1e";
	private UiLifecycleHelper uiHelper;
	private boolean canShowAction;
	private boolean canShowDialog;
	private ProgressDialog progressDialog;
	private boolean canMessage;

	
	EventDB db=new EventDB(this);
	private String EventId;
	private Event event;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		uiHelper = new UiLifecycleHelper(this, null);
	    uiHelper.onCreate(savedInstanceState);
	   
		setContentView(R.layout.normalevent);
		
		Intent i=getIntent();
		EventId=i.getExtras().getString("id");
		
		//Load Data of competition with id = competitionId
		TextView title = (TextView) findViewById(R.id.statusText);

		
		ScriptRunner run = new ScriptRunner(new ScriptFinishListener() {
			
		@Override
		public void finish(String result, int resultCode) {
			if(resultCode == ScriptRunner.SUCCESS){
				try {
					Log.i("debug", result);
					JSONObject ob = new JSONObject(result);
					Log.i("debug", result);
					String info_ver  = ob.getString("info_version");
					String content_ver = ob.getString("content_version");
					
					if(!info_ver.equals(db.getInfoVersion(EventId))){
						//update info
						String results = "";
						String t1 =  ob.getString("time_d1");
						String t2 =  ob.getString("time_d2");
						String t3 =  ob.getString("time_d3");
						String v1 =  ob.getString("venue_d1");
						String v2 =  ob.getString("venue_d2");
						String v3 =  ob.getString("venue_d3");
						String imageurl = ob.getString("image_url");
						String contactname = ob.getString("contactname");
						String contactnumber = ob.getString("contactnumber");
						
						db.updateScheduleData(EventId,t1,t2,t3,v1,v2,v3,results,imageurl,contactname,contactnumber,info_ver);
						
					}
					
					if(!content_ver.equals(db.getContentVersion(EventId))){
						//upadte content
						Updater updater=new Updater(NormalEvent.this);
						updater.updateEventContents(EventId);
						db.putContentVersion(EventId, content_ver);
			
					}
					
					
				} catch (JSONException e) {
					e.printStackTrace();
					
				}
				
			}
			
		}
	});
	
	run.execute("http://kr.comze.com/getEventInfo.php?id="+EventId);
		
		
		event=db.getEvent(EventId);
		title.setText(event.eventName);
		
		File outDir=getCacheDir();
		try {
//			Log.d("eventId", event.eventId);
//			Log.d("eventId", event.HTML);
			
			if(event.HTML!=null){
				File outFile= new File(outDir.getPath(),"page.html");
				BufferedWriter writer= new BufferedWriter(new FileWriter(outFile));
				writer.write(event.HTML);
				writer.close();
				
				if(event.img1!=null){
					File outImg= new File(outDir.getPath(),"1.jpg");
					FileOutputStream fos=new FileOutputStream(outImg,false);
					fos.write(event.img1);
					fos.close();
				}
				
				if(event.img2!=null){
					File outImg= new File(outDir.getPath(),"2.jpg");
					FileOutputStream fos=new FileOutputStream(outImg,false);
					fos.write(event.img2);
					fos.close();
				}
				if(event.img3!=null){
					File outImg= new File(outDir.getPath(),"3.jpg");
					FileOutputStream fos=new FileOutputStream(outImg,false);
					fos.write(event.img3);
					fos.close();
				}
				WebView wv1=(WebView) findViewById(R.id.normalevent_webview);
				wv1.clearCache(true);
				wv1.loadUrl("file://"+outFile.getAbsolutePath());
				wv1.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		Button callBtn = (Button) findViewById(R.id.callButton);
		Button navBtn = (Button) findViewById(R.id.navButton);
		Button scheduleBtn = (Button) findViewById(R.id.scheduleButton);
		Button fShareBtn = (Button) findViewById(R.id.fButton);
		
		
		
		callBtn.setOnClickListener(this);
		navBtn.setOnClickListener(this);
		scheduleBtn.setOnClickListener(this);
		fShareBtn.setOnClickListener(this);
		
		
		
		canShowAction = FacebookDialog.canPresentOpenGraphActionDialog(getApplicationContext(),
                FacebookDialog.OpenGraphActionDialogFeature.OG_ACTION_DIALOG);
	    canShowDialog = FacebookDialog.canPresentShareDialog(getApplicationContext(), 
                FacebookDialog.ShareDialogFeature.SHARE_DIALOG);
	    canMessage = FacebookDialog.canPresentMessageDialog(this,
	    		FacebookDialog.MessageDialogFeature.MESSAGE_DIALOG);
	    
	    progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("please wait..");
		progressDialog.setIndeterminate(false);
		progressDialog.setCancelable(false);
		
		
	}

	@Override
	public void onClick(View btn) {
		switch(btn.getId()){
			case R.id.callButton :
				callEventManager("+91" + event.contactnumber,event.contactname);
				break;
			case R.id.navButton : 
				if(event.venue_d1 != null || event.venue_d2 != null || event.venue_d3 != null){
					Log.i("debug", event.venue_d1+" "+event.venue_d2+" "+event.venue_d3);
					if(event.venue_d1 !=null && event.venue_d2 == null && event.venue_d3 == null)
					{
						Intent i=new Intent(NormalEvent.this,Map.class);
						i.putExtra("place", event.venue_d1);
						startActivity(i);
						return;
					}
					
					NavigationDialog navDialog = new NavigationDialog(NormalEvent.this,event, new OnClickPlaceListener() {
						@Override
						public void onClick(int n) {
							Intent i=new Intent(NormalEvent.this,Map.class);
							switch(n){
							case NavigationDialog.DAY_ONE : i.putExtra("place", event.venue_d1);break;
							case NavigationDialog.DAY_TWO : i.putExtra("place", event.venue_d2); break;
							case NavigationDialog.DAY_THREE : i.putExtra("place", event.venue_d3);break;
							}
							startActivity(i);
						}
					});
					navDialog.show();
				}else{
					Toast.makeText(getApplicationContext(),
							"Location not updated.",
							Toast.LENGTH_SHORT).show();
					}
				break;
			case R.id.scheduleButton:	
					ScheduleWindow schedule = new ScheduleWindow(NormalEvent.this,event);
					schedule.show(); 
					break;
			case R.id.fButton: 
					ShareDailog dialog = new ShareDailog(this,canShowAction,canMessage, new OnClickEventListener() {		
						@Override
						public void onClick(int n) {
							switch(n){
							case ShareDailog.PARTICIATE: 
								graphStory();
								break;
							case ShareDailog.SHARE:
								shareEvent();
								break;
							case ShareDailog.SEND_MESSAGE:
								sendMessage();
								break;
							}	
						}
					});
					dialog.show();
					break;
		}
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    progressDialog.hide();
	    uiHelper.onActivityResult(requestCode, resultCode, data, new FacebookDialog.Callback() {
	        @Override
	        public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
	            Log.e("debug", String.format("Error: %s", error.toString())+" .. "+data);
	        }

	        @Override
	        public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
	            Log.i("debug", "Success!");
	        }
	    });
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    uiHelper.onSaveInstanceState(outState);
	}

	@Override
	public void onPause() {
	    super.onPause();
	    uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
	    super.onDestroy();
	    uiHelper.onDestroy();
	}
	
	@Override
	protected void onResume() {
	    super.onResume();
	    uiHelper.onResume();
	}
	private void graphStory() {
		//Set story data according to db!!
		OpenGraphObject competition = OpenGraphObject.Factory.createForPost("tathvatriloaded:event");
		competition.setProperty("title", event.eventName);
		competition.setProperty("description", event.contents);
		competition.setProperty("image", event.image_url);
		competition.setProperty("url", "http://tathva.org/events");


		OpenGraphAction action = GraphObject.Factory.create(OpenGraphAction.class);
		action.setType("tathvatriloaded:participate");
		action.setProperty("event", competition);
		action.setProperty("expires_in","2000000000"); 
		action.setProperty("image",event.image_url);
		
		FacebookDialog fbDialog = new FacebookDialog.OpenGraphActionDialogBuilder(
		        this, action, "event").build();
	    uiHelper.trackPendingDialogCall(fbDialog.present());
	    progressDialog.show();
		
	}
	
	private void shareEvent() {
		if(canShowDialog){
			FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(this)
            .setName(event.eventName)
			.setDescription(event.contents)
			.setPicture(event.image_url)
			.setLink("http://tathva.org/events")
            .build();
			uiHelper.trackPendingDialogCall(shareDialog.present());
			progressDialog.show();
			
		}else{
			publishFeedDialog();
		}
		
	}
	
	private void publishFeedDialog() {
	    Bundle params = new Bundle();
	    params.putString("name", event.eventName);
	    params.putString("description",event.contents);
	    params.putString("link", "http://tathva.org/events");
	    params.putString("picture", event.image_url);

	    WebDialog feedDialog = (
	        new WebDialog.FeedDialogBuilder(this,Session.getActiveSession(),
	            params))
	        .setOnCompleteListener(null)
	        .build();
	    feedDialog.show();
	   // progressDialog.show();
	}

	private void sendMessage(){
		FacebookDialog.MessageDialogBuilder builder = new FacebookDialog.MessageDialogBuilder(this)
		.setName(event.eventName)
		.setDescription(event.contents)
		.setPicture(event.image_url)
		.setLink("http://tathva.org/events")
	    .setCaption("ideate innovate revloutionize");
	    FacebookDialog dialog = builder.build();
		uiHelper.trackPendingDialogCall(dialog.present());
		progressDialog.show();

	}
	
	public void callEventManager(final String phn, String contactName) {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle("Call Event Manager");
		alertDialogBuilder
				.setMessage(
						"Are You sure you want to call " + contactName + " ? ")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Intent callIntent = new Intent(
										Intent.ACTION_CALL);
								callIntent.setData(Uri.parse("tel:" + phn));
								startActivity(callIntent);
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}
	
	@Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        overridePendingTransition (R.anim.activity_open_scale, R.anim.activity_close_translate);
    }
	
}

