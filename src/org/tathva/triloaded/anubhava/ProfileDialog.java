package org.tathva.triloaded.anubhava;


import org.tathva.triloaded.customviews.ProfileImage;
import org.tathva.triloaded.customviews.ScalableImage;


import org.tathva.triloaded.mainmenu.R;

import android.app.Dialog;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfileDialog extends Dialog {
	
	ProfileImage profileImage;
	TextView username;

	public ProfileDialog(Context context) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		getWindow().setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		
		setContentView(R.layout.anubhva_profile_info);
		
		profileImage = (ProfileImage) findViewById(R.id.profile_image);
		String image_url = AnubhavaUtils.getImagesDirectory(context)+"/user_profile_image.jpg";
		
		profileImage.setProfileImage(
				AnubhavaUtils.loadImageFromStorage(image_url));
		
		username = (TextView) findViewById(R.id.user_name);
		username.setText(AnubhavaUtils.getFbUsername(context));
		
		Button logButton = (Button) findViewById(R.id.authButton);
		logButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismiss();
				
			}
		});
		
	}

}
