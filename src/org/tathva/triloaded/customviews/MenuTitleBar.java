package org.tathva.triloaded.customviews;


/*##################################

# Title Animation 
# Tathva 2014
# Team Tathva Triloaded
# UI Team :P
# coder Anas M.
		
#####################################
*/


import org.tathva.triloaded.mainmenu.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;

public class MenuTitleBar extends View{
	
	
	
	
	private Paint bgPaint;
	private Paint textPaint;
	private Typeface font;
	private int textSize;
	private int sectionCode;
	private int width;
	private int height;
	
	private Animation flyIn;
	private Animation flyOut;
	private AnimationListener flyOutListener = new AnimationListener() {
		
		@Override
		public void onAnimationStart(Animation animation) {	
		}
		@Override
		public void onAnimationRepeat(Animation animation) {
		}
		@Override
		public void onAnimationEnd(Animation animation) {	
			setVisibility(INVISIBLE);
		}
	};
	private RectF boundary;
	
	public MenuTitleBar(Context context) {
		super(context);
		
	}

	public MenuTitleBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	
		font = Typeface.createFromAsset(getContext().getAssets(),"fonts/fontastique.ttf");
		bgPaint = new Paint();
		bgPaint.setAntiAlias(true);
		bgPaint.setStyle(Paint.Style.FILL); 
		boundary = new RectF(0,0,0,0);
		textPaint = new Paint();
		textPaint.setAntiAlias(true);
		textPaint.setStyle(Paint.Style.FILL); 
		textPaint.setColor(0xFFEEEEEE);
		textPaint.setTypeface(font);
		textPaint.setTextAlign(Align.CENTER);
		
		 flyIn = AnimationUtils.loadAnimation(context, R.anim.title_in);
		 flyOut = AnimationUtils.loadAnimation(context, R.anim.title_out);
		 
		 setSectionCode(MenuCircle.EVENTS);
	}
	
	public void setSectionCode(int code){
		this.sectionCode = code;
		invalidate();
	}
	
	public void flyIn(){
		this.startAnimation(flyIn);
	}
	public void flyOut(){
		flyOut.setAnimationListener(flyOutListener);
		this.startAnimation(flyOut);
	}
	/*public void flyOutIn(final int section) {
		flyOut.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationEnd(Animation animation) {
				sectionCode = section;
				flyOut.setAnimationListener(flyOutListener);
				startAnimation(flyIn);
			}
			@Override
			public void onAnimationRepeat(Animation animation) {	
			}
			@Override
			public void onAnimationStart(Animation animation) {
			}
		});
		this.startAnimation(flyOut);
		
		
	}*/
	@Override
	protected void onDraw(Canvas canvas) {
		
		boundary.set(0, 0, width, height);
		bgPaint.setColor(MenuCircle.sectionHoverColors.get(sectionCode));
		canvas.drawRoundRect(boundary, 5, 5,bgPaint);
		canvas.drawText(MenuCircle.textValues.get(sectionCode), width/2, height*2/3, textPaint);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		
		width = MeasureSpec.getSize(widthMeasureSpec);
		height = MeasureSpec.getSize(heightMeasureSpec);
		setMeasuredDimension(width,height);
		/* Setting Values depending on ScreenSize */
		/** Anju's Point !!! */
		//Log.i("info","Width T :"+width+"Height T:"+height);
		
		if(width>= 350){
			textSize = 50;
		}else if(width>=250){
			textSize = 35;
		}else{
			textSize = 25;
		}
		textPaint.setTextSize(textSize);
	}

	

}
