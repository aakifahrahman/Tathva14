package org.tathva.triloaded.anubhava;

import java.io.File;
import java.lang.ref.WeakReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.tathva.triloaded.anubhava.FetchImageTask.OnCompletionListener;
import org.tathva.triloaded.anubhava.ScriptRunner.ScriptFinishListener;
import org.tathva.triloaded.customviews.ProfileImage;


import org.tathva.triloaded.mainmenu.R;

import com.facebook.Request;
import com.facebook.Session;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class AnubhavaPhoto extends LinearLayout {
		
	private TextView user;
	private TextView postMessage;
	
	private ProfileImage picture;
	private ImageView postImage;
	private Context context;
	private ProgressBar imageLoad;
	
	
	public AnubhavaPhoto(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	@Override
	protected void onFinishInflate() {
		
		super.onFinishInflate();
		
		user = (TextView) findViewById(R.id.anubhava_usertext);
		postMessage = (TextView) findViewById(R.id.postMessage);
		postImage = (ImageView) findViewById(R.id.postImage);
		picture = (ProfileImage) findViewById(R.id.profilepicture);
		imageLoad = (ProgressBar) findViewById(R.id.imageLoad);
	}
	
	public void setProfileImage(Bitmap bmp){
		picture.setProfileImage(bmp);
		
	}
	public void setData(String username, String caption){
		user.setText(username);
		postMessage.setText(caption);
	}
	public void setPostImage(Bitmap bmp){
		postImage.setImageBitmap(bmp);
	}
	public ImageView getPostImage(){
		return postImage;
	}
	public void showLoading(){
		imageLoad.setVisibility(View.VISIBLE);
	}
	public void setBlank(){
		//postImage.setImageResource(R.drawable.whitebackground);
		Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.anu_whitebackground);
		postImage.setImageBitmap(bm);
		//postImage.setVisibility(View.INVISIBLE);
	}
	
	public void makeVisible(){
		postImage.setVisibility(View.VISIBLE);
	}
	public void stopLoading(){
		imageLoad.setVisibility(View.INVISIBLE);
	}
	
	public interface FectchCallBack{
		public void onFinish(String url);
	}
	
	
}
