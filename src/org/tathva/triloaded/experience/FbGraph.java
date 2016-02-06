package org.tathva.triloaded.experience;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.tathva.triloaded.gcm.ScriptRunner.ScriptFinishListener;
import org.tathva.triloaded.gcm.ScriptRunner;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Request.Callback;
import com.facebook.Request.GraphUserCallback;
import com.facebook.RequestBatch;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphUser;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
public class FbGraph {
	
	public static final int SUCCESS = 1;
	public static final int ERROR = 2;
	
	public static final int USER_OPERATION = 1;
	public static final int SHARE_OPERATION = 2;
	public static final int PHOTOPOST_OPERATION = 3;
	public static final int GET_POSTS_OPERATION = 4;
	
	
	
	public static void newUser(final Context c,Session session, final OperationCallback returnCall) {
		
		Request user = Request.newMeRequest(session, new GraphUserCallback() {
			
			@Override
			public void onCompleted(GraphUser user, Response response) {
				if(user!= null){

					final String name = user.getName();
					final String id = user.getId();
		
					ScriptRunner.ScriptFinishListener listener = new ScriptFinishListener() {
						
						@Override
						public void finish(String result, int resultCode) {	
							if(resultCode == ScriptRunner.SUCCESS){
								FbUtils.setFbDetails(c,id, name);
								returnCall.callback(SUCCESS,USER_OPERATION);
							}else{
								returnCall.callback(ERROR, USER_OPERATION);
							}
						}
					};
					String codedName=user.getName();
					String codedId =user.getId();
					try {
						codedName = URLEncoder.encode(user.getName(),"UTF-8");
						codedId = URLEncoder.encode(user.getId(),"UTF-8");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					ScriptRunner executer = new ScriptRunner(listener);
					executer.execute(
						"http://tri.comule.com/fb/addUser.php?id="+codedId+"&name="+codedName);
					
				}
				
			}
		});
				
		user.executeAsync();
	}
	
	
	public static void share(Session session,final Context c) {
	     
	        Bundle postParams = new Bundle();
	        postParams.putString("name", "Facebook SDK for Android");
	        postParams.putString("caption", "Build great social apps and get more installs.");
	        postParams.putString("description", "The Facebook SDK for Android makes it easier and faster to develop Facebook integrated Android apps.");
	        postParams.putString("link", "https://developers.facebook.com/android");
	        postParams.putString("picture", "https://raw.github.com/fbsamples/ios-3.x-howtos/master/Images/iossdk_logo.png");

	        Request.Callback callback= new Request.Callback() {
	            public void onCompleted(Response response) {
	                JSONObject graphResponse = response
	                                           .getGraphObject()
	                                           .getInnerJSONObject();
	                String postId = null;
	                try {
	                    postId = graphResponse.getString("id");
	                } catch (JSONException e) {
	                    Log.i("debug", "JSON error "+ e.getMessage());
	                }
	                FacebookRequestError error = response.getError();
	                if (error != null) {
	                    Toast.makeText(c,error.getErrorMessage(),
	                         Toast.LENGTH_SHORT).show();
	                    } else {
	                        Toast.makeText(c, postId,
	                             Toast.LENGTH_LONG).show();      
	                }
	            }

				
	        };

	        Request request = new Request(session, "me/feed", postParams, 
	                              HttpMethod.POST, callback);

	        RequestAsyncTask task = new RequestAsyncTask(request);
	        task.execute();
	    

	}
	
	
	public static boolean isSubsetOf(Collection<String> subset, Collection<String> superset) {
	    for (String string : subset) {
	        if (!superset.contains(string)) {
	            return false;
	        }
	    }
	    return true;
	}
	
	public interface OperationCallback{
		
		public void callback(int result,int operation);
	
	}

	

	
}
