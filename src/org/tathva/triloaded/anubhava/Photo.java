package org.tathva.triloaded.anubhava;


import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public class Photo {
	
	private String id;
	private String user_id;
	private String user_name;
	private String caption;
	private String image_url;
	private String local_post_url;
	private String local_profile_url;
	
	private boolean isBiengDownloaded = false;
	
	private Context context;
	
	public Photo(JSONObject object,Context c){
		context = c;
		try {
			id = object.getString("id");
			user_id = object.getString("user_id");
			user_name = object.getString("user_name");
			caption = object.getString("caption");
			image_url= object.getString("image_url");
			 
			local_post_url = AnubhavaUtils.getImagesDirectory(context)+"/"+id+"image.jpg";
			local_profile_url = AnubhavaUtils.getImagesDirectory(context)+"/"+id+"profile.jpg";
	        	
		} catch (JSONException e) {
			 Log.i("debug", "Json parse photo error "+ e.toString());
			e.printStackTrace();
		}
		
	}
	
	public Photo(String id,String user_id,String user_name,
		String caption,String image_url,
		String local_post_url, String local_profile_url){
		
		this.id = id;
		this.user_id = user_id;
		this.user_name = user_name;
		this.caption = caption;
		this.image_url = image_url;
		this.local_post_url = local_post_url;
		this.local_profile_url= local_profile_url;
		
	}
	
	/*public void getProfileImage(){
		profileImage = AnubhavaUtils.loadImageFromStorage(local_profile_url);
		postImage = AnubhavaUtils.loadImageFromStorage(local_post_url);
		if(postImage != null){
			isPostLoaded = true;
		}
		if(profileImage != null){
			setProfileLoaded(true);
		}
	}*/
	
	public String getId(){
		return id;
	}
	public String getUser_id(){
		return user_id;
	}
	public String getUser_name(){
		return user_name;
	}
	
	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getimage_url() {
		return image_url;
	}

	public void setimage_url(String image_url) {
		this.image_url = image_url;
	}

	public String getLocal_post_url() {
		return local_post_url;
	}

	public void setLocal_post_url(String local_post_url) {
		this.local_post_url = local_post_url;
	}

	public String getLocal_profile_url() {
		return local_profile_url;
	}

	public void setLocal_profile_url(String local_profile_url) {
		this.local_profile_url = local_profile_url;
	}

	public boolean checkProfileExits(){
		return checkIfFileExits(local_profile_url);
	}
	
	public boolean checkPostExits(){
		return checkIfFileExits(local_post_url);
	}
	private boolean checkIfFileExits(String url){
		File file = new File(url);
		return file.exists();
	}

	public boolean isBiengDownloaded() {
		return isBiengDownloaded;
	}

	public void setBiengDownloaded(boolean isBiengDownloaded) {
		this.isBiengDownloaded = isBiengDownloaded;
	}
	
	public String getProfile_pic_url(){
		return "https://graph.facebook.com/"+user_id+"/picture?height=100&width=100" ;	   
	}

	
}
