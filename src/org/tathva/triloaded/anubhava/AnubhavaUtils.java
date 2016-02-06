package org.tathva.triloaded.anubhava;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;





import java.util.List;
import java.util.Vector;

import org.json.JSONArray;
import org.tathva.triloaded.anubhava.FetchImageTask.OnCompletionListener;
import org.tathva.triloaded.anubhava.ScriptRunner.ScriptFinishListener;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Request.GraphUserCallback;
import com.facebook.model.GraphUser;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

public class AnubhavaUtils {
	
	public static final String SERVER = "http://kr.comze.com/";
	
    public static final String FB_VARIABLES = "fbvariables";
    public static final String FB_ID = "id";
    private static final String FB_NAME = "name";
    private static final String IS_FIRST_TIME = "firsttime";
	
    
	public AnubhavaUtils() {
		
	}
		
	public static void newUser(final Context c,Session session) {
		
		Request user = Request.newMeRequest(session, new GraphUserCallback() {
			
			@Override
			public void onCompleted(GraphUser user, Response response) {
				if(user!= null){

					final String name = user.getName();
					final String id = user.getId();
					final String profilePicUrl ="https://graph.facebook.com/"+id+"/picture?width=250&height=250" ;
					
					ScriptRunner.ScriptFinishListener listener = new ScriptFinishListener() {
						
						@Override
						public void finish(String result, int resultCode) {	
							if(resultCode == ScriptRunner.SUCCESS){
								setFbDetails(c,id, name);
								
								
								FetchImageTask profileImageTask = new FetchImageTask(new OnCompletionListener() {
									@Override
									public void onComplete(Bitmap bitmap) {
										if(bitmap != null){
											AnubhavaUtils.saveToInternalSorage(bitmap, "user_profile_image.jpg",c);
										
										}
									}
								});
								
							 profileImageTask.execute(profilePicUrl);
								
							}else{
							
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
					executer.execute(SERVER+"addUser.php?user_id="+
								codedId+"&user_name="+codedName);
						
				}
				
			}
		});
				
		user.executeAsync();
	}
	
	
	
	
	public static String keyHash(Context context){
		String key = "";
		try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    "org.tathva.triloaded.anubhava", 
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.d("anas", key);
                }
        } catch (NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
		
		return key;
	}
	
	public static String getFbId(Context context) {
		SharedPreferences fbInfo = context.getSharedPreferences(FB_VARIABLES, 0);
        String registrationId = fbInfo.getString(FB_ID, "");
        if (registrationId.isEmpty()) {
            return "";
        }
        return registrationId;
    }
	public static String getFbUsername(Context context) {
		SharedPreferences fbInfo = context.getSharedPreferences(FB_VARIABLES, 0);
        String username = fbInfo.getString(FB_NAME, "");
        if (username.isEmpty()) {
            return "";
        }
        return username;
    }
	
	public static void setFbDetails(Context context,String fb_id, String fb_name) {
		SharedPreferences fbInfo = context.getSharedPreferences(FB_VARIABLES, 0);
        SharedPreferences.Editor editor = fbInfo.edit();
        editor.putString(FB_ID, fb_id);
        editor.putString(FB_NAME, fb_name);
        editor.putString(IS_FIRST_TIME, "yes");
        editor.commit();
    }
	public static boolean isLoginFirst(Context context) {
		SharedPreferences fbInfo = context.getSharedPreferences(FB_VARIABLES, 0);
        String loginfirst = fbInfo.getString(IS_FIRST_TIME, "yes");
        if (loginfirst.equals("yes")) {
            return true;
        }
        return false;
    }
	public static void setIsLogin(Context context , String s) {
		SharedPreferences fbInfo = context.getSharedPreferences(FB_VARIABLES, 0);
        SharedPreferences.Editor editor = fbInfo.edit();
        editor.putString(IS_FIRST_TIME,s);
        editor.commit();
    }
	
	public static void clearFbDetails(Context context) {
		SharedPreferences fbInfo = context.getSharedPreferences(FB_VARIABLES, 0);
        SharedPreferences.Editor editor = fbInfo.edit();
        editor.clear();
        editor.commit();  
    }
	
	
	
	public static String saveToInternalSorage(Bitmap bitmapImage, String filename,Context context){
        File mypath = new File(getImagesDirectory(context),filename);
        FileOutputStream fos = null;
        try {           
            fos = new FileOutputStream(mypath);
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mypath.getAbsolutePath();
    }
	
	public static File getImagesDirectory(Context context){
		 ContextWrapper cw = new ContextWrapper(context);
	     File directory = cw.getDir("images", Context.MODE_PRIVATE);
	     return directory;
	}

	public static Bitmap loadImageFromStorage(String path)
	{
		Bitmap bmp = null;
	    try {
	        File f = new File(path);
	        
	        final BitmapFactory.Options options = new BitmapFactory.Options();
		    options.inJustDecodeBounds = true;
		    //BitmapFactory.decodeResource(res, resId, options);
		    BitmapFactory.decodeStream(new FileInputStream(f),null,options);   
	        
		    // Calculate inSampleSize
		    options.inSampleSize = calculateInSampleSize(options, 200, 200);
		    
		    // Decode bitmap with inSampleSize set
		    options.inJustDecodeBounds = false;
		    bmp = BitmapFactory.decodeStream(new FileInputStream(f),null, options);
	        //BitmapFactory.decodeS
	    } 
	    catch (FileNotFoundException e) 
	    {
	        e.printStackTrace();
	    }
	    return bmp;

	}
	public static List<Photo> getPhotos(String result, Context context){
		List<Photo> photos = new Vector<Photo>();
		try{
			JSONArray jArray = new JSONArray(result);
			
	 	   for(int i=0;i<jArray.length();i++){
	 		   photos.add(new Photo(jArray.getJSONObject(i),context));
	 	   }
	 	   return photos;
		
		}catch(Exception e){
    	   Log.e("debug","Photos : JSON exception :"+e.toString());
    	   return null;
		} 
	}
	
	public static Bitmap loadImageForSharing(String path)
	{
		Bitmap bmp = null;
	    try {
	        File f = new File(path);
	        
	        final BitmapFactory.Options options = new BitmapFactory.Options();
		    options.inJustDecodeBounds = true;
		    //BitmapFactory.decodeResource(res, resId, options);
		    BitmapFactory.decodeStream(new FileInputStream(f),null,options);   
	        
		    // Calculate inSampleSize
		    options.inSampleSize = calculateInSampleSize(options, 300, 300);
		    
		    // Decode bitmap with inSampleSize set
		    options.inJustDecodeBounds = false;
		    bmp = BitmapFactory.decodeStream(new FileInputStream(f),null, options);
	        //BitmapFactory.decodeS
	    } 
	    catch (FileNotFoundException e) 
	    {
	        e.printStackTrace();
	    }
	    return bmp;

	}
	
	
	public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
	    // Raw height and width of image
	    final int height = options.outHeight;
	    final int width = options.outWidth;
	    int inSampleSize = 1;
	
	    if (height > reqHeight || width > reqWidth) {
	
	        final int halfHeight = height / 2;
	        final int halfWidth = width / 2;
	
	        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
	        // height and width larger than the requested height and width.
	        while ((halfHeight / inSampleSize) > reqHeight
	                && (halfWidth / inSampleSize) > reqWidth) {
	            inSampleSize *= 2;
	        }
	    }

	    return inSampleSize;
	}
	
	public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
	        int reqWidth, int reqHeight) {

	    // First decode with inJustDecodeBounds=true to check dimensions
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeResource(res, resId, options);

	    // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;
	    return BitmapFactory.decodeResource(res, resId, options);
	}
	
	/*mImageView.setImageBitmap(
		    decodeSampledBitmapFromResource(getResources(), R.id.myimage, 100, 100));

	*/
	
	
}
