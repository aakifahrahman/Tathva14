package org.tathva.triloaded.gcm;

import org.tathva.triloaded.mainmenu.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class NotificationActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notificationlayout);
		TextView tv = (TextView) findViewById(R.id.jsonText);
		tv.setText(getIntent().getExtras().getString("jsonData").toString());
	}
}
