package org.tathva.triloaded.customviews;

/*##################################

# Tathva Text Animation 
# Tathva 2014
# Team Tathva Triloaded
# UI Team :P
# coder Anas M.
		
#####################################
*/


import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;



public class TathvaText extends View {

	
	private int width;
	private int height;
	private RectF boundary;
	private Paint textPaint;
	private int textSize;
	
	private Typeface font;
	private int timeElapsed = 0;
	private int endTime = 5000;
	private String textValue = "";
	private boolean isAnimating = false;
	private boolean isFinished = false;
	
	private OnFinishListener onfinishlistener;
	private Paint rectPaint;
	
	public TathvaText(Context context){
		super(context);
		font = Typeface.createFromAsset(getContext().getAssets(),"fonts/fontastique.ttf");
		init();
	}
	public TathvaText(Context context, AttributeSet attrs) {
		super(context, attrs);
		font = Typeface.createFromAsset(getContext().getAssets(),"fonts/fontastique.ttf");
		init();
	}
	public void init(){

		textPaint  = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTypeface(font);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(0xFFFFFFFF);
        
        rectPaint = new Paint();
        rectPaint.setAntiAlias(true);
		rectPaint.setStyle(Paint.Style.FILL); 
		rectPaint.setColor(0xCCFF0000);
		boundary = new RectF(0,0,0,0);
	
	}
	public void start(){
		isAnimating = true;
		invalidate();
	}
	public void setOnFinishListener(OnFinishListener onfinish){
		this.onfinishlistener = onfinish;
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		width = MeasureSpec.getSize(widthMeasureSpec);
		height = MeasureSpec.getSize(heightMeasureSpec); 
		setMeasuredDimension(width,height);
		
		if(width>= 400){
			textSize = 100;
		}else if(width>=300){
			textSize = 75;
		}else{
			textSize = 60;
		}
		textPaint.setTextSize(textSize);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		
		 
		 boundary.set(0,0,width,height);
		 //canvas.drawRect(r, rectPaint);
		 if(isFinished){
			 canvas.drawText(textValue ,width/2 , height/2, textPaint);
			 invalidate();
		 }
		 if(isAnimating){
			 timeElapsed +=50 ;
			 switch(timeElapsed){
				 case 500: textValue += "t"; break;
				 case 1000: textValue += "a"; break;
				 case 1500: textValue += "t"; break;
				 case 2000: textValue += "h"; break;
				 case 2500: textValue += "v"; break;
				 case 3000: textValue += "a"; break;
				 case 3500: textValue += "'"; break;
				 case 4000: textValue += "1"; break;
				 case 4500: textValue += "4"; break;
			 
			 }
			 if(timeElapsed >= endTime){
				 Log.i("anas", "finish");
				 isAnimating = false;
				 isFinished = true;
				 if(onfinishlistener != null){
					 onfinishlistener.onFinish(getId());
				 }
			 }
			 
			 canvas.drawText(textValue , width/2 , height/2, textPaint);
			 invalidate();
		 }
      	 
     
      	 
	}
	
}
