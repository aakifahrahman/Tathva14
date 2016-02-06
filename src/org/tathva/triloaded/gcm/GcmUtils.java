package org.tathva.triloaded.gcm;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

public class GcmUtils {
	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String EXTRA_MESSAGE = "message";
    public static final String GCM_VARIABLES = "gcmconstants";
    public static final String GCM_REG_ID = "registration_id";
    private static final String GCM_APP_VERSION = "appVersion";
	
	
	public static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } 
        catch (NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }
	public static String getRegistrationId(Context context) {
		SharedPreferences gcmInfo = context.getSharedPreferences(GCM_VARIABLES, 0);
        String registrationId = gcmInfo.getString(GCM_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i("anas", "Registration not found.");
            return "";
        }
     
        // Check if app was updated; if so, it must clear the registration ID, since the existing regID is not guaranteed to work with the new app version.
        int registeredVersion = gcmInfo.getInt(GCM_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context.getApplicationContext());
        if (registeredVersion != currentVersion) {
            Log.i("anas", "App version changed.");
            return "";
        }
        return registrationId;
    }
	
	public static void setRegistrationId(Context context,String regid, int appVersion) {
		SharedPreferences gcmInfo = context.getSharedPreferences(GCM_VARIABLES, 0);
        SharedPreferences.Editor editor = gcmInfo.edit();
        editor.putString(GCM_REG_ID, regid);
        editor.putInt(GCM_APP_VERSION, appVersion);
        editor.commit();
        
    }
	
	
	
}
