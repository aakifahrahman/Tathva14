package org.tathva.triloaded.anubhava;


import java.lang.ref.WeakReference;

import org.tathva.triloaded.customviews.CustomListView;
import org.tathva.triloaded.customviews.LoaderImage;
import org.tathva.triloaded.mainmenu.R;

import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.NewPermissionsRequest;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.FacebookDialog.Callback;
import com.facebook.widget.FacebookDialog.PendingCall;

import android.app.Activity;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.os.Build;

public class AnubhavaMain extends Activity{

	private static String APP_ID = "279143188956828";
	private static String APP_SECRET = "09d230d7c7c2afa383ee225409309e1e";
	private UiLifecycleHelper uiHelper;
	
	private boolean isLoaded = false;
	private int loadCount = 4;
	
	/** Fb state handler **/
	private Session.StatusCallback callback = new Session.StatusCallback() {
	    @Override
	    public void call(Session session, SessionState state, Exception exception) {
	    	onSessionStateChange(session, state, exception);
	    }
	};
	
	private LoaderImage mainloader;
	private CustomListView photoList;
//	private List<Photo> photos;
	private PostList   posts;
	private int postSelection = -1;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
        setContentView(R.layout.anubhava_main);
       
        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);
        Log.i("maindebug","THE ONCREATE CALLED AGAIN");
        Log.d("aaki hashkey", AnubhavaUtils.keyHash(this));
        
        
       }
    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
    	Log.i("debug", "state");
    	 Log.i("maindebug","THE ONCREATE CALLED AGAIN");
         Log.i("aaki hashkey", AnubhavaUtils.keyHash(this));
         AnubhavaUtils.keyHash(AnubhavaMain.this);
    	if (state.isOpened()) {
            
            if(AnubhavaUtils.getFbId(this).isEmpty()){
            	//Register User!!
            	Log.i("debug", "Registering User");
            	AnubhavaUtils.newUser(this,session);
            }
            setContentView(R.layout.anubhava_main);
            
        	LoadThreads();
           
            
        } else if (state.isClosed()) {
            Log.i("debug", "Logged out...");
            AnubhavaUtils.clearFbDetails(this);
            setContentView(R.layout.anubhava_login); 
        }
    }
   
    private void LoadThreads() {
    	if(isLoaded){
    		Log.i("debug", "already load thread called");
    		return;
    	}
		isLoaded = true;
		Log.i("debug", "load thread called");
		
		
    	
		photoList = (CustomListView) findViewById(R.id.photolist);
    	mainloader = (LoaderImage) findViewById(R.id.loaderView);
    	
        posts = new PostList(this,photoList,mainloader,loadCount);
    	photoList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position,
					long id) {
				postSelection = position;
				Photo photo = posts.getItem(position);
				loadCount = posts.getLoadCount();
				Intent intent = new Intent(AnubhavaMain.this, PostActivity.class);
				intent.putExtra(AnubhavaDB.postTable_id, photo.getId());
				intent.putExtra(AnubhavaDB.postTable_user_name, photo.getUser_name());
				intent.putExtra(AnubhavaDB.postTable_caption, photo.getCaption());
				intent.putExtra(AnubhavaDB.postTable_user_id, photo.getUser_id());
				intent.putExtra(AnubhavaDB.postTable_local_post_url, photo.getLocal_post_url());
				startActivity(intent);
			}
		});
    	
    	Button postButton = (Button) findViewById(R.id.postButton);
    	postButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(AnubhavaMain.this,SharePhoto.class));		
			}
		});
    	Button profileButton = (Button) findViewById(R.id.profileButton);
    	profileButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ProfileDialog dialog = new ProfileDialog(AnubhavaMain.this);
				dialog.show();
				
			}
		});
    	
    }
	    
    @Override
    public void onResume() {
        super.onResume();
        isLoaded = false;
        Session session = Session.getActiveSession();
        if (session != null &&
               (session.isOpened() || session.isClosed()) ) {
            onSessionStateChange(session, session.getState(), null);
        }else{
        	setContentView(R.layout.anubhava_login);
        }

        uiHelper.onResume();
        
        if(postSelection != -1){
        	photoList.setSelection(postSelection);
        }
    }

   /* @Override
	public void callback(int result, int operation) {
		switch(operation){
		case AnubhavaGraph.PHOTOPOST_OPERATION:break;
		case AnubhavaGraph.USER_OPERATION:break;
		case AnubhavaGraph.SHARE_OPERATION:break;
		case AnubhavaGraph.GET_POSTS_OPERATION:
			circle.setVisibility(View.INVISIBLE);
			FbList fblist = new FbList(this, photos);
			photoList.setAdapter(fblist);
			break;
		}
		
	}*/
    

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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    
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
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        overridePendingTransition (R.anim.activity_open_scale, R.anim.activity_close_translate);
    }

}
