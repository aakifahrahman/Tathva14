package org.tathva.triloaded.anubhava;

import java.io.File;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.tathva.triloaded.anubhava.CountingMultipartEntity.ProgressListener;

import android.os.AsyncTask;
import android.util.Log;

public class Uploader implements ProgressListener{

	private ProgressCounter counter;
	private UploadFinishListener upListener;
	
	public Uploader() {
		// TODO Auto-generated constructor stub
	}
	
	public void postFields(final String user, final String caption, final String image_path,
			ProgressCounter counter, UploadFinishListener listener){
		
		this.counter = counter;
		this.upListener = listener;
		
		new AsyncTask<Void, Void, Void>(){

			@Override
			protected Void doInBackground(Void... params) {
				try{
					
					// the file to be posted
//					String textFile = Environment.getExternalStorageDirectory() + "/sample.txt";
					
					Log.v("Uploader", "textFile: " + image_path);
					
					// the URL where the file will be posted
					String postReceiverUrl = "http://kr.comze.com/upload_file.php";
					Log.v("Uploader", "postURL: " + postReceiverUrl);
					
					// new HttpClient
					HttpClient httpClient = new DefaultHttpClient();
					
					// post header
					HttpPost httpPost = new HttpPost(postReceiverUrl);
					
					File file = new File(image_path);
					FileBody fileBody = new FileBody(file);
			
					CountingMultipartEntity reqEntity = new CountingMultipartEntity(
							HttpMultipartMode.BROWSER_COMPATIBLE,Uploader.this);
					
				
					reqEntity.addPart("uploadedfile", fileBody);
					reqEntity.addPart("user", new StringBody(user));
					reqEntity.addPart("caption", new StringBody(caption));
					httpPost.setEntity(reqEntity);
					
					// execute HTTP post request
					HttpResponse response = httpClient.execute(httpPost);
					HttpEntity resEntity = response.getEntity();
					
					if (resEntity != null) {
						
						String responseStr = EntityUtils.toString(resEntity).trim();
						Log.v("Uploader", "Response: " +  responseStr);
						
						// you can add an if statement here and do other actions based on the response
					}
					
				} catch (NullPointerException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
			
			protected void onPostExecute(Void result) {
				upListener.onFinish();
				
			};
			
		}.execute();
		
	}
	

	@Override
	public void transferred(long num) {
		Log.i("upload Progress", "++ "+ num);
		counter.uploaded(num);
		
	}
	
	 public static interface ProgressCounter {
	        void uploaded(long num);
	    }
	 
	 public static interface UploadFinishListener{
		 void onFinish();
	 }

}
