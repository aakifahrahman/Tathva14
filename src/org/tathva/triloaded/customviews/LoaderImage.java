package org.tathva.triloaded.customviews;


import org.tathva.triloaded.mainmenu.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class LoaderImage extends View {

	
	Bitmap bitmap;
	int angle = 0;
	boolean isComplete;
	boolean isAnimating = false;
	int width;
	
	private Animation rotateAnimation;
	private Paint arcpaint;
	
    
	public LoaderImage(Context context, AttributeSet attrs) {
		super(context, attrs);	
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.anu_loader);
		rotateAnimation = AnimationUtils.loadAnimation(context, R.anim.rotate_animation);
		final int color = 0xffe5e5e5;
        arcpaint = new Paint();
        arcpaint.setAntiAlias(true);
        arcpaint.setColor(color);
         
	}
	
	public void setAngle (int angle){
		if(angle<=0){
			angle =0;
		}
		this.angle = angle;
		isAnimating = false;
		invalidate();
	}
	
	public void startAnimating(){
		angle =0;
		isAnimating = true;
		startAnimation(rotateAnimation);
	}
	public void stopAnimating(){
		angle =360;
		isAnimating = false;
		clearAnimation();
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		
		int specSize = MeasureSpec.getSize(widthMeasureSpec);
		setMeasuredDimension(specSize,specSize);
		width = specSize;
		
	}
	@Override
	protected void onDraw(Canvas canvas) {
		Paint paint = new Paint();
        // paint.setColor(Color.CYAN);
		if(!isAnimating){
			//canvas.drawBitmap(getRotatedBitmap(), 0, 0, paint);
			Bitmap image = Bitmap.createScaledBitmap(bitmap, width, width,true);
			Matrix matrix = new Matrix();
	        matrix.reset();
	        matrix.postTranslate(-width / 2, -width/ 2); // Centers image
	        matrix.postRotate(angle);
	        matrix.postTranslate(width/2, width/2);
	        canvas.drawBitmap(image, matrix, null);
	        RectF rectF = new RectF(-5,-5,width+5,width+5);
	        canvas.drawArc(rectF, 270+angle, 360-angle, true, arcpaint);
			
			
		}else{
			canvas.drawBitmap(getclip(), 0, 0, paint);
		}
		
	}
	
	 public Bitmap getclip() {
		 final Rect rect = new Rect(0, 0,width,width);
		 
         Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                 bitmap.getHeight(), Config.ARGB_8888);
         output = Bitmap.createScaledBitmap(bitmap, width, width,true);
         
        /* Canvas canvas = new Canvas(output);
         final int color = 0xffffffff;
         final Paint paint = new Paint();
         paint.setAntiAlias(true);
         paint.setColor(color);
         
         
         
         canvas.drawBitmap(bitmap, rect, rect, paint);
         
         RectF rectF = new RectF(rect);
         canvas.drawArc(rectF, 0, angle, true, paint);
         */
         return output;
     }
	 
	 public Bitmap getRotatedBitmap(){
		 final Rect rect = new Rect(0, 0,width,width);
         Bitmap image = Bitmap.createScaledBitmap(bitmap, width, width,true);
         
         Canvas canvas = new Canvas(image);
         canvas.drawARGB(0, 0, 0, 0);
         
         final int color = 0xffffffff;
         final Paint paint = new Paint();
         paint.setAntiAlias(true);
         paint.setColor(color);
         
         Matrix matrix = new Matrix();
         matrix.reset();
         matrix.postScale(width/bitmap.getWidth(), width/bitmap.getWidth());
         matrix.postTranslate(-width / 2, -width/ 2); // Centers image
         matrix.postRotate(angle);
         matrix.postTranslate(width/2, width/2);
         canvas.drawBitmap(bitmap, matrix, null);
         
         
        // canvas.drawBitmap(bitmap, rect, rect, paint);
         
        // RectF rectF = new RectF(rect);
        // canvas.drawArc(rectF, 0, angle, true, paint);
         //*/
         return image;
	 }

}
