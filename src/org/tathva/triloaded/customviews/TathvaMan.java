package org.tathva.triloaded.customviews;

/*##################################

# Tathva man zoom In Animation 
# Tathva 2014
# Team Tathva Triloaded
# UI Team :P
# coder Anas M.
		
#####################################
*/

import org.tathva.triloaded.mainmenu.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;

public class TathvaMan extends View {

	private Bitmap tathvamanStatic;
	private int manSize;
	private int normal_manSize;
	private int maximum_manSize;
	
	private int timeElapsed =0;
	private int endTime = 5000;
	private int width;
	private boolean isAnimating = false;
	private boolean isFinished = false;
	private OnFinishListener onfinishlistener;
	private RectF manBoundary;
	
	public TathvaMan(Context context){
		super(context);
		init();
	}
	public TathvaMan(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	public void init(){
		tathvamanStatic = BitmapFactory.decodeResource(getResources(), R.drawable.white_tathva_man);
		manBoundary = new RectF(0,0,0,0);
	}
	public void setOnFinishListener(OnFinishListener onfinish){
		this.onfinishlistener = onfinish;
	}
	public void start(){
		isAnimating = true;
		invalidate();
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int specSize = MeasureSpec.getSize(widthMeasureSpec);
		setMeasuredDimension(specSize,specSize);
		width = specSize;
		maximum_manSize = width/3;
		normal_manSize = width/15;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		 
		 manBoundary.set(width/2 - manSize,width/2 - manSize,width/2 + manSize,width/2 + manSize);
		 
		 if(isFinished){
			 manSize = (int) sineEaseIn(timeElapsed, endTime, normal_manSize, maximum_manSize); 
	      	 canvas.drawBitmap(tathvamanStatic,null , manBoundary,null);
		 }
		
		 if(isAnimating){ 
			 timeElapsed += 100;
			 manSize = (int) sineEaseIn(timeElapsed, endTime, normal_manSize, maximum_manSize);
	      	 canvas.drawBitmap(tathvamanStatic,null , manBoundary,null);
	      	 invalidate();
	      	
	      	 if(timeElapsed >= endTime){
				 isAnimating = false;
				 if(onfinishlistener != null){
					 onfinishlistener.onFinish(getId());
					 isFinished = true;
				 }
			 }
		}
		
		
	}
	
	public float sineEaseOut(float currentTime,float endTime,int startValue, int changeValue){
		
		return (float) (changeValue*Math.sin((currentTime/endTime) * (Math.PI/2))+startValue);
	}
	
	public float sineEaseIn(float currentTime,float endTime,float startValue, float changeValue){
		
		return (float) (-changeValue*Math.cos((currentTime/endTime) * (Math.PI/2))+changeValue+startValue);
	}
	
	
}
