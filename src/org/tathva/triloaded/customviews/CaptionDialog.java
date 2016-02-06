package org.tathva.triloaded.customviews;


import org.tathva.triloaded.mainmenu.R;

import android.app.Dialog;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class CaptionDialog extends Dialog{

	private UploadListener listener;
	EditText et;
	Button bt;

	public CaptionDialog(Context context,UploadListener listen) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		getWindow().setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		
		this.listener = listen;
		setContentView(R.layout.anu_caption_dialog);
		et = (EditText) findViewById(R.id.editCaption);
	    bt = (Button) findViewById(R.id.upButton);
	    bt.setOnClickListener(new android.view.View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String s= et.getText().toString();
				listener.onUpload(s);
				dismiss();
			}
		});
		
	}
	
	public interface UploadListener{
		void onUpload(String Caption);
	}

}
