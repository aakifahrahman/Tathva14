package org.tathva.triloaded.anubhava;

import java.lang.ref.WeakReference;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

public class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
    
	private final WeakReference<ImageView> imageViewReference;
	private OnBitmapLoadFinish listener;
	public String url = "";

	public BitmapWorkerTask(ImageView imageView) {
    // Use a WeakReference to ensure the ImageView can be garbage collected
		imageViewReference = new WeakReference<ImageView>(imageView);
		//this.listener = listener;
	}

// Decode image in background.
	@Override
	protected Bitmap doInBackground(String... params) {
			url = params[0];
	        return AnubhavaUtils.loadImageFromStorage(url);	
	}

// Once complete, see if ImageView is still around and set bitmap.
	@Override
	protected void onPostExecute(Bitmap bitmap) {
	    if (imageViewReference != null && bitmap != null) {
	        final ImageView imageView = imageViewReference.get();
	        if (imageView != null) {
	            imageView.setImageBitmap(bitmap);
	           // listener.onFinish();
	        }
	    }
	}
	
	public interface OnBitmapLoadFinish{
		
		void onFinish();
	}
	

}