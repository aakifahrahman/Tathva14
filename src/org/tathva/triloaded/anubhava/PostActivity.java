package org.tathva.triloaded.anubhava;

import org.tathva.triloaded.anubhava.ScriptRunner.ScriptFinishListener;
import org.tathva.triloaded.customviews.ProfileImage;


import org.tathva.triloaded.mainmenu.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class PostActivity extends Activity {
	
	ImageView post;
	TextView userText, captionText;
	Button delete;
	
	String postPath;
	String profilePath;
	String username;
	String caption;
	private String userid;
	private String id;
	ProgressDialog progress;
	AnubhavaDB db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.anubhava_post_activity);
		
		progress = new ProgressDialog(this);
		db = new AnubhavaDB(this);
		
		Bundle values  = getIntent().getExtras();
		postPath = values.getString(AnubhavaDB.postTable_local_post_url);
		username = values.getString(AnubhavaDB.postTable_user_name);
		userid = values.getString(AnubhavaDB.postTable_user_id);
		id = values.getString(AnubhavaDB.postTable_id);
		caption = values.getString(AnubhavaDB.postTable_caption);
		
		Log.i("debug", "d :"+userid);
		Log.i("debug", "d :"+AnubhavaUtils.getFbId(this));
		
		
		post = (ImageView) findViewById(R.id.post_iv);
		userText = (TextView) findViewById(R.id.anubhava_username);
		captionText = (TextView) findViewById(R.id.anubhava_captiontext);
		delete = (Button) findViewById(R.id.deleteBtn);
		
		if(!AnubhavaUtils.getFbId(this).equals(userid)){
			delete.setVisibility(View.GONE);
		}else{
			delete.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
				 progress.setMessage("Deleting photo...");
				 progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				 progress.setIndeterminate(false);
				 progress.show();
					String s ="http://kr.comze.com/deletepost.php?postid="+id;
					ScriptRunner r = new ScriptRunner(new ScriptFinishListener() {
						
						@Override
						public void finish(String result, int resultCode) {
							if(resultCode == ScriptRunner.SUCCESS){
								db.deletePost(id);
								progress.cancel();
								PostActivity.this.finish();
							}
						}
					});
					r.execute(s);
				}
			});
		}
		
		captionText.setText(caption);
		userText.setText(username);
		post.setImageBitmap(AnubhavaUtils.loadImageFromStorage(postPath));
		
		
	}
}
