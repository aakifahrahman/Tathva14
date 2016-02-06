package org.tathva.triloaded.gcm;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.tathva.triloaded.gcm.ScriptRunner.ScriptFinishListener;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.internal.ct;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;


public class RegisterApp extends AsyncTask<Void, Void, String> {

	//NOTE :: API KEY =  AIzaSyAt8h5VedQVpzuFEBXNJimr5bZ-FX7Qs9s
	
	
    public static final String TAG = "GCMRelated";
    Context ctx;
    GoogleCloudMessaging gcm;
    String SENDER_ID = "203367190577";
    String regid = null; 
    private int appVersion;
   
    public RegisterApp(Context ctx, GoogleCloudMessaging gcm, int appVersion) {
        this.ctx = ctx;
        this.gcm = gcm;
        this.appVersion = appVersion;
    }
 
 
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected String doInBackground(Void... arg0) {
        String msg = "";
        try {
            if (gcm == null) {
                gcm = GoogleCloudMessaging.getInstance(ctx);
            }
            regid = gcm.register(SENDER_ID);
            msg = "Device registered, registration ID=" + regid;
            Log.i("anas", "id="+msg);
            // Sending the registration ID to server over HTTP, so it can use GCM/HTTP or CCS to send messages to the app.
           
            sendRegistrationIdToBackend();

            // Persist the regID - no need to register again.
           // storeRegistrationId(ctx, regid);
            
            GcmUtils.setRegistrationId(ctx, regid, appVersion);
        }
        catch (Exception ex) {
            msg = ex.getMessage();
            Log.i("anas", " oo "+ex.toString());
            ex.printStackTrace();
            
            
            // If there is an error, don't just keep trying to register.
            // Require the user to click a button again, or perform
            // exponential back-off.
        }
        return msg;
    }



    private void sendRegistrationIdToBackend() {
      
    	ScriptRunner executer = new ScriptRunner(new ScriptFinishListener() {
			
			@Override
			public void finish(String result, int resultCode) {
				if(resultCode == ScriptRunner.SUCCESS){
					//Toast.makeText(ctx, "storing id on server failed", Toast.LENGTH_SHORT).show();
				}
				
			}
		});
		//executer.execute("http://tri.comule.com/gcm/addID.php?RegID="+regid);
    	executer.execute("http://kr.comze.com/gcm/addID.php?RegID="+regid);
		
    	
    }


    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if(result == GoogleCloudMessaging.ERROR_SERVICE_NOT_AVAILABLE){
        	//Toast.makeText(ctx, "make Sure play services enabled!!", Toast.LENGTH_SHORT).show();
        	
        }else{
        	//Toast.makeText(ctx, "Registration Completed. Now you can see the notifications", Toast.LENGTH_SHORT).show();
        	Log.i(TAG,"result "+ result);
        }
    }
}