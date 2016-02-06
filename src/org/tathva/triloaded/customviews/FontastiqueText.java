package org.tathva.triloaded.customviews;

/*##################################

# Push up/out text Animation 
# Tathva 2014
# Team Tathva Triloaded
# UI Team :P
# coder Anas M.
		
#####################################
*/

import org.tathva.triloaded.mainmenu.R;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class FontastiqueText extends TextView {
	
	private boolean isAnimating = false;
	private boolean isFinished = false;
	private OnFinishListener onfinishlistener;
	
	private Animation pushIn;
	private Animation pushOut;
	
	private String[] strings = new String[]{"ideate.","innovate.","revolutionize."};
	private int currentString =0;
	
	
	private Animation.AnimationListener listenerIn = new AnimationListener() {
		@Override
		public void onAnimationStart(Animation animation) {}
		@Override
		public void onAnimationRepeat(Animation animation) {}
		@Override
		public void onAnimationEnd(Animation animation) {
			
			if(currentString == 2){
				if(onfinishlistener != null){
					onfinishlistener.onFinish(getId());
				}
			}else{
				startAnimation(pushOut);
			}
		}
	};
	private Animation.AnimationListener listenerOut = new AnimationListener() {
		@Override
		public void onAnimationStart(Animation animation) {}
		@Override
		public void onAnimationRepeat(Animation animation) {}
		@Override
		public void onAnimationEnd(Animation animation) {
			switch (currentString) {
			case 0: setText(strings[1]);
					currentString++;
					startAnimation(pushIn);
				break;
			case 1: setText(strings[2]);
					currentString++;
					startAnimation(pushIn);
			break;
			case 2: 
				
			break;

			default:
				break;
			}
		}
	};
	private int width;
	private int height;
	private int textSize;
	
	
	public FontastiqueText(Context context){
		super(context);
		pushIn = AnimationUtils.loadAnimation(context, R.anim.push_up_in);
		pushOut= AnimationUtils.loadAnimation(context, R.anim.push_up_out);
		init();
	}
	
	public FontastiqueText(Context context, AttributeSet attrs) {
		super(context, attrs);
		pushIn = AnimationUtils.loadAnimation(context, R.anim.push_up_in);
		pushOut = AnimationUtils.loadAnimation(context, R.anim.push_up_out);
		
		init();
	}
	public void init(){
		Typeface font = Typeface.createFromAsset(getContext().getAssets(),"fonts/fontastique.ttf");
		this.setTypeface(font);	
		pushIn.setAnimationListener(listenerIn);
		pushOut.setAnimationListener(listenerOut);
		setTextColor(0xFFEEEEEE);
	}
	@Override
	protected void onDraw(Canvas canvas) {
		
		super.onDraw(canvas);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		width = MeasureSpec.getSize(widthMeasureSpec);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		//Log.i("anas", " Fwidth :"+width + "height : "+height);
		if(width>= 200){
			textSize = 20;
		}else if(width>=150){
			textSize = 15;
		}else{
			textSize = 12;
		}
		setTextSize(textSize);
		
	}
	
	public void setOnFinishListener(OnFinishListener onfinish){
		this.onfinishlistener = onfinish;
	}
	public void start(){
			setText(strings[0]);
			startAnimation(pushIn);
	}
	


}
