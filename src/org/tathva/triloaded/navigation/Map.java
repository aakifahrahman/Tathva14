package org.tathva.triloaded.navigation;



import com.google.android.gms.maps.GoogleMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.tathva.triloaded.mainmenu.R;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.provider.Settings;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.model.PolylineOptions;


public class Map extends Activity implements android.location.LocationListener {
  
	  int request_code = 1;
	  private GoogleMap map;
	  String pos,place;
	  Marker RequestedLocation;
	  MapDB  db=new MapDB(this);
	  Location loc;
	  LocationManager locationManager ;
	  android.location.Location mylocation;
	  LatLng pl,mypos;
	  final int RQS_GooglePlayServices = 1;
	  double src_lat;
	  double src_long;
	  double dest_lat;
	  double dest_long;
	  
	  private boolean isClosed = false;
  
	  @Override
	  protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
	   
	    setContentView(R.layout.activity_map);
	    
	    locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
	
		Intent i=getIntent();
		place=i.getExtras().getString("place");
		loc=db.getPlace(place);
		if(loc == null){
			Toast.makeText(this, "Some error occured!!", 2000);
			return;
		}
		pos = loc.getName();
		dest_lat = Double.parseDouble(loc.lat);
		dest_long = Double.parseDouble(loc.lon);
		pl = new LatLng(dest_lat,dest_long);
		
	    map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
	            .getMap();
	    map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
	        
	    map.setMyLocationEnabled(true);
	    
	    boolean enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	    
	    RequestedLocation = map.addMarker(new MarkerOptions().
	    	                                       position(pl).
	    	                                       title(pos).
	    	                                       icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
	    
	    RequestedLocation.showInfoWindow();
	    
	    map.moveCamera(CameraUpdateFactory.newLatLngZoom(pl, 18));
	    
	    if(!enabled){
	    	 AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
	    	 alertDialogBuilder
	    	 .setMessage("GPS is disabled on your device. Would you like to enable it to show route to destination?")
	    	 .setCancelable(true)
	    	 .setPositiveButton("Open Settings",
	    	     new DialogInterface.OnClickListener() {
	    	         public void onClick(DialogInterface dialog, int id) {
	    	             Intent callGPSSettingIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	    	             startActivityForResult(callGPSSettingIntent,1); 
	    	                	         }
	    	     });
	    	 alertDialogBuilder.setNegativeButton("Cancel",
	    	     new DialogInterface.OnClickListener() {
	    	         public void onClick(DialogInterface dialog, int id) {
	    	        	 dialog.cancel();
	    	        	 RequestedLocation.showInfoWindow();
	    	         }
	    	     });
	    	 AlertDialog alert = alertDialogBuilder.create();
	    	 alert.show();
	    }
	    else{
	    	
	   	 locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, Map.this);
	    }
	    
	  }
	  
	 @Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);	
		 locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, Map.this);
		}
	
	 @Override
	public void onLocationChanged(android.location.Location arg0) {
		Log.i("debug", "loc change");
		if(!isClosed){
			Log.i("debug", "came inside");
			positionAquired(arg0);
		}
		
	}
	 
	void positionAquired(android.location.Location loc){
		//Toast.makeText(this, "coordinates aque", Toast.LENGTH_LONG).show();
		 mylocation = loc;
		src_lat = mylocation.getLatitude();
		src_long = mylocation.getLongitude();
		mypos = new LatLng(src_lat,src_long);
		locationManager.removeUpdates(this);
		Marker MyLocation = map.addMarker(new MarkerOptions().
	            position(mypos).
	            title("You are here !").
	            icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
	
	
	   connectAsyncTask _connectAsyncTask = new connectAsyncTask();
	   _connectAsyncTask.execute(); 
	   map.moveCamera(CameraUpdateFactory.newLatLngZoom(mypos, 18));
	   
		MyLocation.showInfoWindow();
	}
	
	@Override
	protected void onResume() {
	    // TODO Auto-generated method stub
	    super.onResume();
	
	    int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
	
	    if (resultCode != ConnectionResult.SUCCESS){
	        GooglePlayServicesUtil.getErrorDialog(resultCode, this, RQS_GooglePlayServices);
	    }
	    isClosed = false;
	    
	}
	
	private class connectAsyncTask extends AsyncTask<Void, Void, Void>{
	    private ProgressDialog progressDialog;
	
	    @Override
	    protected void onPreExecute() {
	        // TODO Auto-generated method stub
	        super.onPreExecute();
	        progressDialog = new ProgressDialog(Map.this);
	        progressDialog.setMessage("Fetching route, Please wait...");
	        progressDialog.setIndeterminate(true);
	        progressDialog.show();
	    }
	    
	    @Override
	    protected Void doInBackground(Void... params) {
	        // TODO Auto-generated method stub
	        fetchData();
	        return null;
	    }
	    
	    @Override
	    protected void onPostExecute(Void result) {
	    // TODO Auto-generated method stub
	    super.onPostExecute(result);
	    if(doc!=null){
	        NodeList _nodelist = doc.getElementsByTagName("status");
	        Node node1 = _nodelist.item(0);
	        String _status1 = node1.getChildNodes().item(0).getNodeValue();
	        if(_status1.equalsIgnoreCase("OK")){
	            NodeList _nodelist_path = doc.getElementsByTagName("overview_polyline");
	            Node node_path = _nodelist_path.item(0);
	            Element _status_path = (Element)node_path;
	            NodeList _nodelist_destination_path = _status_path.getElementsByTagName("points");
	            Node _nodelist_dest = _nodelist_destination_path.item(0);
	            String _path = _nodelist_dest.getChildNodes().item(0).getNodeValue();
	            List<LatLng> directionPoint = decodePoly(_path);
	
	            PolylineOptions rectLine = new PolylineOptions().width(10).color(Color.RED);
	            for (int i = 0; i < directionPoint.size(); i++) {
	                rectLine.add(directionPoint.get(i));
	            }
	           
	            // Adding route on the map
	            map.addPolyline(rectLine);
	        }
	        else{
	            
	        }
	
	    }
	    else{
	        
	    }
	
	    progressDialog.dismiss();
	    }
	
	}
	
	Document doc = null;
	private void fetchData()
	{	
	    StringBuilder urlString = new StringBuilder();
	    urlString.append("http://maps.google.com/maps/api/directions/xml?origin=");
	    urlString.append(src_lat);
	    urlString.append(",");
	    urlString.append(src_long);
	    urlString.append("&destination=");//to
	    urlString.append(dest_lat);
	    urlString.append(",");
	    urlString.append(dest_long);
	    urlString.append("&sensor=true&mode=driving");
	    Log.d("url","::"+urlString.toString());
	    HttpURLConnection urlConnection= null;
	    URL url = null;
	    try
	    {
	        url = new URL(urlString.toString());
	        urlConnection=(HttpURLConnection)url.openConnection();
	        urlConnection.setRequestMethod("GET");
	        urlConnection.setDoOutput(true);
	        urlConnection.setDoInput(true);
	        urlConnection.connect();
	        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	        DocumentBuilder db = dbf.newDocumentBuilder();
	        doc = (Document) db.parse(urlConnection.getInputStream());//Util.XMLfromString(response);
	    }
	    catch (MalformedURLException e){
	        e.printStackTrace();
	    }
	    catch (IOException e){
	        e.printStackTrace();
	    }
	    catch (ParserConfigurationException e){
	        e.printStackTrace();
	    }
	    catch (SAXException e) {
	        // TODO Auto-generated catch block 
	        e.printStackTrace();
	    }
	}
	
	
	private ArrayList<LatLng> decodePoly(String encoded) {
	    ArrayList<LatLng> poly = new ArrayList<LatLng>();
	    int index = 0, len = encoded.length();
	    int lat = 0, lng = 0;
	    while (index < len) {
	    int b, shift = 0, result = 0;
	    do {
	        b = encoded.charAt(index++) - 63;
	        result |= (b & 0x1f) << shift;
	        shift += 5;
	    } 
	    while (b >= 0x20);
	    int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
	    lat += dlat;
	    shift = 0;
	    result = 0;
	    do {
	        b = encoded.charAt(index++) - 63;
	        result |= (b & 0x1f) << shift;
	        shift += 5;
	    } 
	    while (b >= 0x20);
	    int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
	    lng += dlng;
	
	    LatLng position = new LatLng((double) lat / 1E5, (double) lng / 1E5);
	    poly.add(position);
	    }
	    return poly;
	}
	
	
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
		protected void onStop() {
			isClosed = true;
			super.onStop();
		}
	
	 @Override
	    public void onBackPressed() {
	        // TODO Auto-generated method stub
	        super.onBackPressed();
	        overridePendingTransition (R.anim.activity_open_scale, R.anim.activity_close_translate);
	    }


} 