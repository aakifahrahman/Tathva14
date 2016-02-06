package org.tathva.triloaded.customviews;

import org.tathva.triloaded.mainmenu.R;

import android.app.Dialog;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class ShareDailog extends Dialog implements android.view.View.OnClickListener{
	
	public static final int PARTICIATE = 0;
	public static final int SHARE = 1;
	public static final int SEND_MESSAGE = 2;
	
	OnClickEventListener listener;

	public ShareDailog(Context context,boolean canShow,boolean canMessage,
			OnClickEventListener listener){
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		getWindow().setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		
		
		this.listener = listener;
		setContentView(R.layout.sharedailog);
		Button participate = (Button) findViewById(R.id.btn_participate);
		Button share = (Button) findViewById(R.id.btn_fb_share);
		Button send = (Button) findViewById(R.id.btn_sendmsg);
		participate.setOnClickListener(this);
		share.setOnClickListener(this);
		send.setOnClickListener(this);
		
		if(!canShow){
			participate.setVisibility(View.GONE);
		}
		if(!canMessage){
			send.setVisibility(View.GONE);
		}
		
		
	}
	
	
	public interface OnClickEventListener{
		void onClick(int n);
	}


	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btn_participate:
			dismiss();
			listener.onClick(PARTICIATE);
			break;
		case R.id.btn_fb_share:
			dismiss();
			listener.onClick(SHARE);
			break;
		case R.id.btn_sendmsg:
			dismiss();
			listener.onClick(SEND_MESSAGE);
			break;
		}
		
	}

}
