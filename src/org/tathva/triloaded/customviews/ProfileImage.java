package org.tathva.triloaded.customviews;



import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;

public class ProfileImage extends View{
		
		Bitmap bitmap;
	    int width;
        public ProfileImage(Context context, AttributeSet attrs) {
        	super(context, attrs);
        	/*bitmap = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.glossy_overlay);*/
        }
        
        public void setProfileImage(Bitmap bitmap){
        	this.bitmap = bitmap;
        }
        @Override
    	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    		
    		int specSize = MeasureSpec.getSize(widthMeasureSpec);
    		setMeasuredDimension(specSize,specSize);
    		width = specSize;
    		
    	}
        @Override
        public void onDraw(Canvas canvas) {
        	
            Paint paint = new Paint();
            // paint.setColor(Color.CYAN);
            if(bitmap != null){
            	canvas.drawBitmap(getclip(), 0, 0, paint);
            }
        }

        public Bitmap getclip() {
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                    bitmap.getHeight(), Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
            final int color = 0xff424242;
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(),
                    bitmap.getHeight());

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            // paint.setColor(color);
            canvas.drawCircle(bitmap.getWidth() / 2,
                    bitmap.getHeight() / 2, bitmap.getWidth() / 2, paint);
            paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);
           
            Bitmap image = Bitmap.createScaledBitmap(output, width, width, true);
            return image;
        }
 }

