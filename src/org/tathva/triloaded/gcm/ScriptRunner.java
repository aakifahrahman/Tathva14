package org.tathva.triloaded.gcm;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.AsyncTask;
import android.util.Log;

public class ScriptRunner extends AsyncTask<String, Void, String> {
	
	public static final int SUCCESS = 0;
	public static final int FAIL = 1;
	
	private String objectString;
	private boolean isDone = false;
	private int code;
	private ScriptFinishListener listener;
	public ScriptRunner(ScriptFinishListener listener) {
		this.listener = listener;
	}
	
	@Override
	protected String doInBackground(String... s) {
		objectString = " ";
	    InputStream iStream;
	    InputStreamReader iReader; 
	    URL url;
		HttpURLConnection urlConnection = null;
		try{
			
			 URL chknet = new URL("http://kr.comze.com/checknet.htm");
		 	  HttpURLConnection con = (HttpURLConnection) chknet.openConnection();
		 	 BufferedInputStream bis = new BufferedInputStream(con.getInputStream());
			 InputStreamReader isr= new InputStreamReader(bis,"iso-8859-1");
	 	   
			 BufferedReader Reader = new BufferedReader(isr,8);
		 	 StringBuilder sBuild = new StringBuilder();
		 	 String out = Reader.readLine();
		 	 Log.d("bug",out);
		 	 if(!out.equals("NETOK")){
		 		 isDone = false;
		 		 return "";
		 	 }
	 	  
			 url = new URL(s[0]);
			 urlConnection = (HttpURLConnection) url.openConnection();
			 iStream = new BufferedInputStream(urlConnection.getInputStream());
			 iReader = new InputStreamReader(iStream,"iso-8859-1");
	 	   
			 BufferedReader bReader = new BufferedReader(iReader,8);
		 	 StringBuilder strBuild = new StringBuilder();
		 	 String line = null;
		 	 while((line = bReader.readLine()) != null){
		 		strBuild.append(line+"\n");
		 	 }
		 	 iStream.close();
		 	 
		 	 objectString = strBuild.toString();
		     isDone = true;
	 
	    }catch(Exception e){
	 	   
	 	   Log.e("anas","Error in COnnection"+e.toString());
	 	   isDone = false;
	 	   
	    } finally {
	    	if(urlConnection != null){
	    		urlConnection.disconnect();
	    	}
		} 
		return objectString;
	}

	public boolean getIsDone(){
		return isDone;
	}
	@Override
    protected void onPostExecute(String result) {
		if(isDone){
			listener.finish(result,SUCCESS);
		}else{
			listener.finish(result, FAIL);
		}
		
    }
	
	
	public interface ScriptFinishListener{
		public void finish(String result,int resultCode);
	}
}
