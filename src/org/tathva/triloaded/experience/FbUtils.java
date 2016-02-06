package org.tathva.triloaded.experience;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

public class FbUtils {
	
    public static final String FB_VARIABLES = "fbvariables";
    public static final String FB_ID = "id";
    private static final String FB_NAME = "name";
	
	public FbUtils() {
		
	}
	
	public static String keyHash(Context context){
		String key = "";
		try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    "org.tathva.triloaded.fb", 
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
            Log.i("anas", "fb id not found.");
            return "";
        }
        return registrationId;
    }
	
	public static void setFbDetails(Context context,String fb_id, String fb_name) {
		SharedPreferences fbInfo = context.getSharedPreferences(FB_VARIABLES, 0);
        SharedPreferences.Editor editor = fbInfo.edit();
        editor.putString(FB_ID, fb_id);
        editor.putString(FB_NAME, fb_name);
        editor.commit();
        
    }
	public static void clearFbDetails(Context context) {
		SharedPreferences fbInfo = context.getSharedPreferences(FB_VARIABLES, 0);
        SharedPreferences.Editor editor = fbInfo.edit();
        editor.clear();
        editor.commit();
        
    }
}
