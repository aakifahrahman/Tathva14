package org.tathva.triloaded.customviews;


import org.tathva.triloaded.events.Event;
import org.tathva.triloaded.mainmenu.R;
import org.tathva.triloaded.navigation.Map;

import android.app.Dialog;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.view.View.OnClickListener;
public class NavigationDialog extends Dialog implements OnClickListener{

	public static final int DAY_ONE = 1;
	public static final int DAY_TWO = 2;
	public static final int DAY_THREE = 3;
	
	private Button dayone;
	private Button daytwo;
	private Button daythree;
	private Event event;
	private Context context;
	private OnClickPlaceListener listener;
	
	public NavigationDialog(Context context,Event e, OnClickPlaceListener listener) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		getWindow().setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		
		setContentView(R.layout.navigationdialog);
		this.listener = listener;
		this.context = context;
		
		dayone = (Button) findViewById(R.id.btndayOne);
		daytwo = (Button) findViewById(R.id.btnDayTwo);
		daythree = (Button) findViewById(R.id.btnDayThree);
		
		dayone.setOnClickListener(this);
		daytwo.setOnClickListener(this);
		daythree.setOnClickListener(this);
		
		if(e.venue_d1 == null ||e.venue_d1.equals("")){
			dayone.setVisibility(View.GONE);
		}
		if(e.venue_d2 == null || e.venue_d2.equals("")){
			daytwo.setVisibility(View.GONE);
		}
		if(e.venue_d3 == null || e.venue_d3.equals("")){
			daythree.setVisibility(View.GONE);
		}
		
	}

	@Override
	public void onClick(View v) {
		
		switch(v.getId()){
		case R.id.btndayOne: dismiss();listener.onClick(DAY_ONE);break;
		case R.id.btnDayTwo: dismiss();listener.onClick(DAY_TWO);break;
		case R.id.btnDayThree: dismiss();listener.onClick(DAY_THREE);break;
		}
		
	}
	
	
	public interface OnClickPlaceListener{
		void onClick(int n);
	}



}
