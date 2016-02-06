package org.tathva.triloaded.anubhava;

import java.net.URL;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

public class ImagesDownloadTask extends AsyncTask<String, Void,Boolean> {
	
	List<Photo> photos;
	OnLoadFinishListener listener;
	Context context;
	
	public ImagesDownloadTask(List<Photo> photos, Context context, OnLoadFinishListener listener) {
		this.photos = photos;
		this.listener =listener;
		this.context = context;
		
	}
	
	@Override
	protected Boolean doInBackground(String... params) {
		for(int i=0;i<photos.size();i++){
			Bitmap postpicture = downloadNewPhoto(photos.get(i).getimage_url());
			Bitmap profile = null;
			if(!photos.get(i).checkProfileExits()){
		         profile = downloadNewPhoto(photos.get(i).getProfile_pic_url());
			}
			if(postpicture!=null){
				AnubhavaUtils.saveToInternalSorage(postpicture,photos.get(i).getId()+"image.jpg",context);
			}
			if(profile!=null){
				AnubhavaUtils.saveToInternalSorage(profile,photos.get(i).getId()+"profile.jpg",context);
			}
			photos.get(i).setBiengDownloaded(false);
		}
		return true;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		
		super.onPostExecute(result);
		listener.onFinish();
		
	}
	
	private Bitmap downloadNewPhoto(String url){
		Bitmap b = null;
		try{
			URL img = new URL(url);
			b = BitmapFactory.decodeStream(img.openConnection().getInputStream());
		}catch(Exception e){
			Log.i("debug", "Image load error"+e.toString());
		}
		return b;

	}
	
	public interface OnLoadFinishListener{
		void onFinish();
	}
}
