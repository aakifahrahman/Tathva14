package org.tathva.triloaded.customviews;

/*##################################

# CUSTOM MENU 
# Tathva 2014
# Team Tathva Triloaded
# UI Team :P
# coder Anas M.
		
#####################################
*/

import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;

import org.tathva.triloaded.mainmenu.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Handler;
import android.text.style.LineHeightSpan.WithDensity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;

public class MenuCircle extends View {
	
	public static final int ANNOUNCEMENTS = 1;
	public static final int EXPERIENCE = 2;
	public static final int CLUELESS = 3;
	public static final int EVENTS = 4;
	public static final int NAVIGATION = 5;
	public static final int INFO = 6;
	
	
	private static float DefaultMenuTextSize = 40;
	private int sweepAngle = 60;
	
	
	public static final HashMap<Integer, Integer> sectionColors = new HashMap<Integer, Integer>(){
		{
			put(ANNOUNCEMENTS, 0xFF6B4390);
			put(EXPERIENCE, 0xFFFF4444);
			put(CLUELESS, 0xFFF37020);
			put(EVENTS, 0xFFFFC20F);
			put(NAVIGATION, 0xFF99CC00);
			put(INFO, 0xFF0292CE);
		}
	};
	
	public static final HashMap<Integer, Integer> sectionHoverColors = new HashMap<Integer, Integer>(){
		{
			put(ANNOUNCEMENTS, 0xFF462366);
			put(EXPERIENCE, 0xFFD10A0A);
			put(CLUELESS, 0xFFC76C14);
			put(EVENTS, 0xFFCE9C08);
			put(NAVIGATION, 0xFF7DA112);
			put(INFO, 0xFF165E9B);
		}
	};
	private HashMap<Integer, Float> startAngles = new HashMap<Integer, Float>(){
		{
			put(ANNOUNCEMENTS, (float) 0.0);
			put(EXPERIENCE, (float) 60.0);
			put(CLUELESS, (float) 120.0);
			put(EVENTS, (float) 180.0);
			put(NAVIGATION, (float) 240.0);
			put(INFO, (float) 300.0);
		}
	};
	
	private HashMap<Integer, Float> iconCenterAngles = new HashMap<Integer, Float>(){
		{
			put(ANNOUNCEMENTS, (float) 30);
			put(EXPERIENCE, (float) 90);
			put(CLUELESS, (float) 150);
			put(EVENTS, (float) 210);
			put(NAVIGATION,(float) 270 );
			put(INFO, (float) 330);
			
		}
	};
	
	private HashMap<Integer, Float> iconRotateAngles = new HashMap<Integer, Float>(){
		{
			put(ANNOUNCEMENTS, (float) 120);
			put(EXPERIENCE, (float) 180);
			put(CLUELESS, (float) 240);
			put(EVENTS, (float) 300);
			put(NAVIGATION, (float) 0);
			put(INFO, (float) 60);
			
		}
	};
	
	private HashMap<Integer, Vector<Double>> textCoOrdinates = new HashMap<Integer, Vector<Double>>(){
		{
			put(EVENTS, new Vector<Double>(Arrays.asList(0.1,0.35)));
			put(NAVIGATION, new Vector<Double>(Arrays.asList(0.35,0.15)));
			put(INFO, new Vector<Double>(Arrays.asList(0.67,0.35)));
			put(ANNOUNCEMENTS, new Vector<Double>(Arrays.asList(0.65,0.68)));
			put(EXPERIENCE, new Vector<Double>(Arrays.asList(0.35,0.87)));
			put(CLUELESS, new Vector<Double>(Arrays.asList(0.08,0.69)));
		}
	};
	public static final HashMap<Integer, String> textValues = new HashMap<Integer, String>(){
		{
			put(ANNOUNCEMENTS, "Announcements");
			put(EXPERIENCE, "Anubhava");
			put(CLUELESS, "Clueless");
			put(EVENTS, "Events");
			put(NAVIGATION, "Navigation");
			put(INFO, "Info");
			
		}
	};
	
	
	
	private HashMap<Integer, Bitmap> iconImages = new HashMap<Integer, Bitmap>();
	
	
	
	private int iconRadius;
	private int iconSize = 100;
	
	
	private int midBoxColor = 0xFF223355;
	
	
	private Paint menuPaint;
	private Paint menuTextPaint;
	private RectF boundary;
	private RectF manBoundary;
	
	private int radius;
	private int width;
	private int xCenter;
	private int yCenter;
	private int xTopCorner;
	private int yTopCorner;
	private int xBottomCorner;
	private int yBottomCorner;
	
	private float mouse_x;
	private float mouse_y;
	private boolean isClick = false;
	private boolean isPointerOnCircle;
	private boolean isPointerGoneOut = false;
	
	private int clickedSection;
	
	private onClickMenu clickListener;	
	private onHoverMenu hoverListener;
	
	
	private float animStartAngle;
	private float animSweepAngle;
	private boolean isClickAnimating = false;
	private boolean isAnimationFinished = true;
	
	
	private int endTime = 1000;
	private int timeElapsed = 0;
	private int timeIncrement = 75;
	
	private int manSize = 75;
	private static int normal_manSize = 75;
	private static int max_changeSize = 0;
	private Bitmap tathvamanStatic;
	
	/* Rotation */
	private float rot_ref_x;
	private float rot_ref_y;
	private float mouse_down_x;
	private float mouse_down_y;
	
	
	
	
	
	public MenuCircle(Context context) {
		super(context);
		initValues();
	}
	
	public MenuCircle(Context context,AttributeSet attrs) {
		super(context, attrs);	
		/*TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MenuArc);
        int style = a.getInt(R.styleable.MenuArc_customstyle,0);
        int mode = a.getInt(R.styleable.MenuArc_mode, 0);
        a.recycle();*/
		
        initValues();
	}
	
	private void initValues(){
		
	
		menuPaint = new Paint();
		menuPaint.setAntiAlias(true);
		menuPaint.setStyle(Paint.Style.FILL); 
		
        menuTextPaint  = new Paint();
        menuTextPaint.setAntiAlias(true);
        menuTextPaint.setTextSize(40);
        menuTextPaint.setTypeface(Typeface.SERIF);
        menuTextPaint.setTextAlign(Paint.Align.LEFT);
        menuTextPaint.setColor(0xFFFFFFFF);
        
        tathvamanStatic = BitmapFactory.decodeResource(getResources(), R.drawable.white_tathva_man);
        
        iconImages.put(NAVIGATION, BitmapFactory.decodeResource(getResources(), R.drawable.menu_nav));
        iconImages.put(INFO, BitmapFactory.decodeResource(getResources(), R.drawable.menu_info));
        iconImages.put(CLUELESS, BitmapFactory.decodeResource(getResources(), R.drawable.menu_clueless));
        iconImages.put(ANNOUNCEMENTS, BitmapFactory.decodeResource(getResources(), R.drawable.menu_announce));
        iconImages.put(EXPERIENCE, BitmapFactory.decodeResource(getResources(), R.drawable.menu_anubhava));
        iconImages.put(EVENTS, BitmapFactory.decodeResource(getResources(), R.drawable.menu_event));
       
        //tathvaman = Bitmap.createScaledBitmap(tathvamanStatic, 180, 180, true);
        
        boundary = new RectF(0,0,width,width);
        manBoundary = new RectF(0,0,0,0);
       
	}
	
	public void refreshState(){
		
		isAnimationFinished = true;
		isClickAnimating = false;
		clickedSection = 0;
		manSize = normal_manSize; 
		timeElapsed =0;
		invalidate();
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		
		int specSize = MeasureSpec.getSize(widthMeasureSpec);
		setMeasuredDimension(specSize,specSize);
		
		width = specSize;
		iconRadius = width*3/8;
		boundary.set(0, 0, width, width);
		/* Setting Values depending on ScreenSize */
		/** Anju's Point !!! */
		//Log.i("info","Width :"+width);
		
		if(width>= 650){
			normal_manSize = 75;
			max_changeSize = 75;
			iconSize = 100;
		}else if(width>=480){
			normal_manSize = 50;
			max_changeSize = 50;
			iconSize = 75;
		}else{
			normal_manSize = 40;
			max_changeSize = 40;
			iconSize = 55;
		}
		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		mouse_x = event.getX();
		mouse_y = event.getY();
		isPointerOnCircle = checkPointerOnCircle(mouse_x, mouse_y);
		
		if(isAnimationFinished){
			
			switch (event.getAction()) {
		
				case MotionEvent.ACTION_UP:
					if(clickedSection != 0 && isClick){
						clickEvent(clickedSection);	
					}else if(!isClick && !isPointerGoneOut && isPointerOnCircle){
						clickedSection = 0;
						hoverListener.onHover(0);
					}
					
					break;	
				case MotionEvent.ACTION_DOWN:
					clickedSection = determineSection(mouse_x, mouse_y);
					if(clickedSection != 0){
						isClick = true;
						mouse_down_x = mouse_x;
						mouse_down_y = mouse_y;
						rot_ref_x = mouse_x;
						rot_ref_y = mouse_y;
						hoverListener.onHover(clickedSection);
						isPointerGoneOut = false;
					}
					break;
				case MotionEvent.ACTION_MOVE:
					if(!checkIfInClickRange()){
						isClick = false;
					}
					
					if(isPointerOnCircle){
						float angleRef = angleRefToCenter(rot_ref_x, rot_ref_y);
						float angleNow = angleRefToCenter(mouse_x, mouse_y);
						rot_ref_x = mouse_x;
						rot_ref_y = mouse_y;
						float angleChange = angleNow - angleRef;
						rotateMe(angleChange);
					}else{
						if(!isPointerGoneOut){
							hoverListener.onHover(0);
							isPointerGoneOut = true;
							
						}
					}
					break;	
			}

			invalidate();
		}
		return true;
	}
	
	
	private void clickEvent(int section) {
		
		animSweepAngle = sweepAngle;
		isClickAnimating = true;
		isAnimationFinished = false;
		timeElapsed =0;
		manSize = normal_manSize;
		
	}
	
	/*************** Circle GeoMetry **************/
	
	private int determineSection(float x, float y){
		int Section =0;
		if(!checkPointerOnCircle(x, y)){
			return 0;
		}else{
			Section = clickedSection;
			double angDegrees = angleRefToCenter(x, y);
			
			if(checkIfInSection(angDegrees, ANNOUNCEMENTS)){
				Section = ANNOUNCEMENTS;
			}else if(checkIfInSection(angDegrees, EXPERIENCE)){
				Section = EXPERIENCE;
			}else if(checkIfInSection(angDegrees, CLUELESS)){
				Section = CLUELESS;
			}else if(checkIfInSection(angDegrees, INFO)){
				Section = INFO;
			}else if(checkIfInSection(angDegrees, NAVIGATION)){
				Section = NAVIGATION;
			}else if(checkIfInSection(angDegrees, EVENTS)){
				Section = EVENTS;
			}
			
		}
		return Section;
	}
	private boolean checkIfInSection(double angle, int section){
		
		double start = startAngles.get(section)%360;
		if(start<0){
			start+=360;
		}
		if(angle>=start&&angle<sweepAngle+start){
			return true;
		}else{
			return false;
		}
	}
	private float angleRefToCenter(float x, float y){
		float deltaX = x - xCenter;
		float deltaY = y - yCenter;
		float angRad = (float) Math.atan2(deltaY, deltaX);
		float angDegrees = (float) Math.toDegrees(angRad);
		if(angDegrees<0){
			angDegrees = angDegrees +360;
		}
		
		return angDegrees;
	}
	
	private float getDistanceBetween(float x1, float y1, float x2, float y2 ){
		float d = (float) Math.sqrt(Math.pow(x1-x2,2) + Math.pow(y1-y2,2));
		return d;
	}
	
	private boolean checkPointerOnCircle(float x, float y){
		
		if(getDistanceBetween(x, y, xCenter, yCenter) <= radius){
			return true;
		}else{
			return false;
		}
	}
	
	private boolean checkIfInClickRange(){
		float angleRef = angleRefToCenter(mouse_down_x, mouse_down_y);
		float angleNow = angleRefToCenter(mouse_x, mouse_y);
		float angleChange = angleNow - angleRef;
		if(angleChange>= -15 && angleChange <= 15){
			return true;
		}
		return false;
	}
	
	private void rotateMe(float angleChange) {
		
		changeAngle(angleChange, NAVIGATION);
		changeAngle(angleChange, ANNOUNCEMENTS);
		changeAngle(angleChange, INFO);
		changeAngle(angleChange, EVENTS);
		changeAngle(angleChange, EXPERIENCE);
		changeAngle(angleChange, CLUELESS);
	}
	private void changeAngle(float angleChange, int section){
		float initial = startAngles.get(section);
		float finalAngle = (initial+angleChange);
		
		startAngles.put(section, finalAngle);
		initial = iconCenterAngles.get(section);
		finalAngle = (initial+angleChange);
		iconCenterAngles.put(section, finalAngle);
		
		initial = iconRotateAngles.get(section);
		finalAngle = (initial+angleChange);
		iconRotateAngles.put(section, finalAngle);

	}
	
	/***********************************************************/
	
	/***** Drawing *********************************************/
	
	@Override
	protected void onDraw(Canvas canvas) {
		
	      xTopCorner = getLeft();
	      yTopCorner = getTop();
	      xBottomCorner = getRight();
	      yBottomCorner = getBottom();
	      xCenter = (xBottomCorner - xTopCorner)/2;
	      yCenter = (yBottomCorner - yTopCorner)/2;
	      radius = width/2;
		
	      drawNormalState(canvas); 
	  
	      if(checkPointerOnCircle(mouse_x, mouse_y) && !isClickAnimating && clickedSection != 0){   
	    	  drawHoverState(canvas); 
	      }
     
	      menuPaint.setColor(0xFFEEEEEE);
	      //drawMenuText(canvas);
	      drawIcons(canvas);
    
     
	     if(!isAnimationFinished){
	    	 if(animSweepAngle == 360 && isClickAnimating){
	    		 if(clickListener!= null){
	    			 clickListener.onClick(clickedSection);	
	    		 }
	       		isClickAnimating = false;
	       	}
	    	 	 drawHoverState(canvas); 
	    	     drawAnimArc(canvas, clickedSection);
		      	
	    	     manSize = (int) sineEaseOut(timeElapsed, endTime, normal_manSize, max_changeSize);
	    	     manBoundary.set(width/2 - manSize,width/2 - manSize,width/2 + manSize,width/2 + manSize);
		      	 canvas.drawBitmap(tathvamanStatic,null , manBoundary,menuPaint);
		      		
		  }else{
			     
			     manBoundary.set(width/2 - manSize,width/2 - manSize,width/2 + manSize,width/2 + manSize);
			     canvas.drawBitmap(tathvamanStatic, null, manBoundary,menuPaint);
			      
		  }
	      	
  }
	
	private void drawIcons(Canvas canvas) {
		
		drawIcon(canvas, iconImages.get(NAVIGATION),NAVIGATION);
		drawIcon(canvas, iconImages.get(ANNOUNCEMENTS), ANNOUNCEMENTS);
		drawIcon(canvas, iconImages.get(CLUELESS), CLUELESS);
		drawIcon(canvas, iconImages.get(EVENTS), EVENTS);
		drawIcon(canvas, iconImages.get(EXPERIENCE), EXPERIENCE);
		drawIcon(canvas, iconImages.get(INFO), INFO);
			
	}
	
	private void drawIcon(Canvas canvas, Bitmap bitmap, int section){
		
		float centerAngle = iconCenterAngles.get(section);
		float centerX = (float) (iconRadius*Math.cos(Math.toRadians(centerAngle)));
		float centerY = (float) (iconRadius*Math.sin(Math.toRadians(centerAngle)));
		
		Bitmap scaled =Bitmap.createScaledBitmap(bitmap, iconSize, iconSize, true);
		float rotateAngle = iconRotateAngles.get(section);
		
		canvas.drawBitmap(rotatedBitmap(scaled, rotateAngle, iconSize/2, iconSize/2,iconSize), 
				centerX+width/2-iconSize/2,centerY+width/2-iconSize/2, menuPaint);
	}
	
	private Bitmap rotatedBitmap(Bitmap image, float angle, int px, int py, int size){
		Matrix rotateMatrix = new Matrix();
		rotateMatrix.setRotate(angle, px, py);
		Bitmap icon = Bitmap.createBitmap(image,0,0,size,size,rotateMatrix,true);
		return icon;
	}
	
	private void drawNormalState(Canvas canvas) {
		
		 menuPaint.setColor(sectionColors.get(ANNOUNCEMENTS));
		 canvas.drawArc(boundary, startAngles.get(ANNOUNCEMENTS), sweepAngle, true, menuPaint); 
         
		 menuPaint.setColor(sectionColors.get(EXPERIENCE));
         canvas.drawArc(boundary, startAngles.get(EXPERIENCE), sweepAngle, true, menuPaint);
        
         menuPaint.setColor(sectionColors.get(CLUELESS));
         canvas.drawArc(boundary, startAngles.get(CLUELESS), sweepAngle, true, menuPaint);
         
         menuPaint.setColor(sectionColors.get(EVENTS));
         canvas.drawArc(boundary, startAngles.get(EVENTS), sweepAngle, true, menuPaint);
         
         menuPaint.setColor(sectionColors.get(NAVIGATION));
         canvas.drawArc(boundary, startAngles.get(NAVIGATION), sweepAngle, true, menuPaint);
         
         menuPaint.setColor(sectionColors.get(INFO));
         canvas.drawArc(boundary, startAngles.get(INFO), sweepAngle, true, menuPaint);
         
         menuPaint.setColor(midBoxColor);
         canvas.drawCircle(xCenter, yCenter, width/5, menuPaint);
		
	}
	
	private void drawHoverState(Canvas canvas) {
		 switch (clickedSection) {
			case ANNOUNCEMENTS:drawHoverArc(canvas, clickedSection);break;
			case EXPERIENCE:drawHoverArc(canvas, clickedSection);break;
			case CLUELESS:drawHoverArc(canvas, clickedSection);break;
			case EVENTS:drawHoverArc(canvas, clickedSection);break;
			case NAVIGATION:drawHoverArc(canvas, clickedSection);break;
			case INFO:drawHoverArc(canvas, clickedSection);break;
			default:break;
		}
	}
	
	public void drawHoverArc(Canvas canvas,int section){
		 menuPaint.setColor(sectionHoverColors.get(section));
		 canvas.drawArc(boundary, startAngles.get(section), sweepAngle, true, menuPaint);
		 canvas.drawCircle(xCenter, yCenter, width/5, menuPaint);
	}
	
	public void drawAnimArc(Canvas canvas,int section){
		
		timeElapsed += timeIncrement ;
		
		animSweepAngle  = sineEaseOut(timeElapsed, endTime, 0, 450);
		float angleRise = animSweepAngle-60;	
      	if(animSweepAngle>=360){
      		animSweepAngle = 360; 		
      	}
      	animStartAngle = startAngles.get(section) - angleRise/2;
      	 
      	 menuPaint.setColor(sectionHoverColors.get(section));
		 canvas.drawArc(boundary, animStartAngle, animSweepAngle, true, menuPaint);
		 canvas.drawCircle(xCenter, yCenter, width/5, menuPaint);
		 
		 drawIcon(canvas, iconImages.get(section), section);
		 invalidate();
      	
	}
	
	/** For Drawing Texting in Circle **/
	
	public void drawMenuText(Canvas canvas){
		 canvas.drawText(textValues.get(EVENTS), getTextXco(EVENTS), getTextYco(EVENTS), menuTextPaint);
	     canvas.drawText(textValues.get(ANNOUNCEMENTS), getTextXco(ANNOUNCEMENTS), getTextYco(ANNOUNCEMENTS), menuTextPaint);
	     canvas.drawText(textValues.get(INFO), getTextXco(INFO), getTextYco(INFO), menuTextPaint);
	     canvas.drawText(textValues.get(NAVIGATION), getTextXco(NAVIGATION), getTextYco(NAVIGATION), menuTextPaint);
	     canvas.drawText(textValues.get(EXPERIENCE), getTextXco(EXPERIENCE), getTextYco(EXPERIENCE), menuTextPaint);
	     canvas.drawText(textValues.get(CLUELESS), getTextXco(CLUELESS), getTextYco(CLUELESS), menuTextPaint);
	}
	public float getTextXco(int section){
		float f = (float) (width * textCoOrdinates.get(section).get(0));
		return f;
	}
	public float getTextYco(int section){
		float f = (float) (width * textCoOrdinates.get(section).get(1));
		return f;
	}
	
	/**************************************/
	
	/** Event Handling ***/
	
	public void setOnClickMenu(onClickMenu clickObject){
		this.clickListener = clickObject;
	}
	
	public void setHoverListener(onHoverMenu hoverListener){
		this.hoverListener = hoverListener;
	}
	
	public interface onClickMenu{
		void onClick (int SectionCode);
	}
	
	public interface onHoverMenu{
		void onHover(int section);
	}
	
	/*************************************/
	
	
	/** Ease Functions **/
	
	public float sineEaseOut(float currentTime,float endTime,int startValue, int changeValue){
		
		return (float) (changeValue*Math.sin((currentTime/endTime) * (Math.PI/2))+startValue);
	}
	
	public float sineEaseIn(float currentTime,float endTime,float startValue, float changeValue){
		
		return (float) (-changeValue*Math.cos((currentTime/endTime) * (Math.PI/2))+changeValue+startValue);
	}
	
	/*********************/
}
