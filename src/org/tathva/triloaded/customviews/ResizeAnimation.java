package org.tathva.triloaded.customviews;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class ResizeAnimation extends Animation {

	private int finalHeight;
	private int initialHeight;
	private View view;
	
	public ResizeAnimation(View v,int initialHeight, int finalHeight){
		view = v;
		this.initialHeight= initialHeight;
		this.finalHeight = finalHeight;
	}
	
	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		int newHeight = initialHeight + (int) ((finalHeight - initialHeight)*interpolatedTime);
		view.getLayoutParams().height = newHeight;
		view.requestLayout();
		super.applyTransformation(interpolatedTime, t);
	}
	
	
	@Override
	public void initialize(int width, int height, int parentWidth,
			int parentHeight) {
		
		super.initialize(width, height, parentWidth, parentHeight);
	}
	@Override
	public boolean willChangeBounds() {
		return true;
	}
}

