package org.tathva.triloaded.customviews;

import org.json.JSONException;
import org.json.JSONObject;
import org.tathva.triloaded.events.Event;
import org.tathva.triloaded.mainmenu.R;

import com.google.android.gms.internal.ev;

import android.app.Dialog;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

public class ResultsDialog extends Dialog {

	TextView t1,c1,t2,c2,t3,c3;
	
	private Event event;
	
	public ResultsDialog(Context context, Event event) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		getWindow().setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		
		setContentView(R.layout.resultdialog);
		t1 = (TextView) findViewById(R.id.teamOne);
		c1 = (TextView) findViewById(R.id.teamCollegeone);
		t2 = (TextView) findViewById(R.id.team2);
		c2 = (TextView) findViewById(R.id.teamCollege2);
		t3 = (TextView) findViewById(R.id.team3);
		c3 = (TextView) findViewById(R.id.teamCollege3);
		
		
		this.event = event;
		String result = event.results;
		if(result!=null){
			try {
				JSONObject resultObject = new JSONObject(result);
				String teamOne = resultObject.getString("teamOne");
				String teamTwo = resultObject.getString("teamTwo");
				String teamThree = resultObject.getString("teamThree");
				String collegeOne = resultObject.getString("collegeOne");
				String collegeTwo = resultObject.getString("collegeTwo");
				String collegeThree = resultObject.getString("collegeThree");
				Log.i("debug", teamOne+teamTwo+teamThree
						+collegeOne+collegeTwo+collegeThree);
				c1.setText(collegeOne);
				c2.setText(collegeTwo);
				c3.setText(collegeThree);
				t1.setText("1. "+teamOne);
				t2.setText("2. "+teamTwo);
				t3.setText("3. "+teamThree);
				
			} catch (JSONException e) {
				t1.setText("Not updated");
				Log.i("debug", "Result Dialog::"+e.toString());
			}
		}else{
			t1.setText("Not updated");
		}
		
	}
	

}
