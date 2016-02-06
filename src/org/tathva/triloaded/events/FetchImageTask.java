package org.tathva.triloaded.events;

import java.io.InputStream;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

public class FetchImageTask extends AsyncTask<String, Void, Bitmap> {
	
	OnCompletionListener listener;
	
	public FetchImageTask(OnCompletionListener listener) {
		this.listener = listener;
	}
	
	@Override
	protected Bitmap doInBackground(String... urls) {
		String url = urls[0];
		Bitmap image =null;
		try{
			//InputStream in = (InputStream) new URL(url).getContent();
			//Log.i("debug", "in "+in.toString());
			URL img = new URL(url);
			image = BitmapFactory.decodeStream(img.openConnection().getInputStream());
		}catch(Exception e){
			Log.i("debug", "Image load error"+e.toString());
		}
		return image;
	}
	
	
	
	@Override
	protected void onPostExecute(Bitmap result) {
		listener.onComplete(result);
	}
	public interface OnCompletionListener{
		public void onComplete(Bitmap bitmap);
	}
}
