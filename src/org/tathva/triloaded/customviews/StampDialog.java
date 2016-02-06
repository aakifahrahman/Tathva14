package org.tathva.triloaded.customviews;

import org.tathva.triloaded.mainmenu.R;

import android.app.Dialog;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class StampDialog extends Dialog implements android.view.View.OnClickListener{
	
	public static final int PARTICIATE = 0;
	public static final int SHARE = 1;
	public static final int SEND_MESSAGE = 2;
	
	OnClickStampListener listener;

	public StampDialog(Context context,OnClickStampListener listener){
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		getWindow().setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		
		this.listener = listener;
		setContentView(R.layout.anu_stampdialog);
		Button b1 = (Button) findViewById(R.id.stamp1);
		Button b2 = (Button) findViewById(R.id.stamp2);
		Button b3 = (Button) findViewById(R.id.stamp3);
		Button b4 = (Button) findViewById(R.id.stamp4);
		Button b5 = (Button) findViewById(R.id.stamp5);
		Button b6 = (Button) findViewById(R.id.stamp6);
		Button b7 = (Button) findViewById(R.id.stamp07);
		Button b8 = (Button) findViewById(R.id.stamp8);
		//Button b8 = (Button) findViewById(R.id.stamp1);
		b1.setOnClickListener(this);
		b2.setOnClickListener(this);
		b3.setOnClickListener(this);
		b4.setOnClickListener(this);
		b5.setOnClickListener(this);
		b6.setOnClickListener(this);
		b7.setOnClickListener(this);
		b8.setOnClickListener(this);
		}
	
	
	public interface OnClickStampListener{
		void onClick(int n);
	}


	@Override
	public void onClick(View v) {
		//listener.onClick(v.getId());
		switch (v.getId()) {
		case R.id.stamp1:listener.onClick(R.drawable.stamp_1);break;
		case R.id.stamp2:listener.onClick(R.drawable.stamp_2);break;
		case R.id.stamp3:listener.onClick(R.drawable.stamp_3);break;
		case R.id.stamp4:listener.onClick(R.drawable.stamp_4);break;
		case R.id.stamp5:listener.onClick(R.drawable.stamp_5);break;
		case R.id.stamp6:listener.onClick(R.drawable.stamp_6);break;
		case R.id.stamp07:listener.onClick(R.drawable.stamp_7);break;
		case R.id.stamp8:listener.onClick(0);break;
		
		}
		dismiss();
	}

}
