package org.tathva.triloaded.customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextFontastic extends TextView {

	public TextFontastic(Context context, AttributeSet attrs) {
		super(context, attrs);
		Typeface font = Typeface.createFromAsset(getContext().getAssets(),"fonts/fontastique.ttf");
		setTypeface(font);	
	}

}
