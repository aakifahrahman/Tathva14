package org.tathva.triloaded.customviews;

import android.widget.ListView;

import java.util.Date;



import org.tathva.triloaded.mainmenu.R;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
 
 public class CustomListView extends ListView implements OnScrollListener {
 
     private final static int PULL_To_REFRESH = 1; // From the drop down to return to the no refresh status value
     private final static int REFRESHING = 2;// Refreshing the state value
     private final static int DONE = 3;

     private final static int RATIO = 3;
     private LayoutInflater inflater;

     private LinearLayout headerView;
     private RelativeLayout footerView;
     private LoaderImage loader;
     
     private int headerContentHeight;
     private int angle = 0;
  
    private int startY;
    private int state;
    
    private TextView nomoreposts;
    private Button loadmore;
    private ProgressBar progress;

     // For guarantee in a complete touch event is recorded only once the value of startY
     private boolean isRecored;

    private OnRefreshListener refreshListener;
    private OnLoadMoreListener loadmoreListener;
 
    private boolean isRefreshable;
	private int offset;
 
     public CustomListView (Context context) {
         super(context);
        init(context);
    }
 
     public CustomListView (Context context, AttributeSet attrs) {
         super(context, attrs);
        init(context);
     }
     
     public void setRestState(){
    	 Log.i("debug", "finished loading");
    	 progress.setVisibility(View.INVISIBLE);
		 loadmore.setVisibility(View.VISIBLE);
		 loadmore.setEnabled(true);
		 footerView.invalidate();
     }
     
     public void setLoadingState(){
    	 Log.i("debug", "loading state");
    	 progress.setVisibility(View.VISIBLE);
		 loadmore.setVisibility(View.INVISIBLE);
		 loadmore.setEnabled(false);
		 footerView.invalidate();
     }
     
     private void init(Context context) {
         inflater = LayoutInflater.from(context);
         headerView = (LinearLayout) inflater.inflate(R.layout.anu_lv_header, null);
         footerView = (RelativeLayout) inflater.inflate(R.layout.anu_lv_footer, null);
        
         nomoreposts = (TextView) footerView.findViewById(R.id.no_more_post);
         loadmore = (Button) footerView.findViewById(R.id.LoadMore);
         progress = (ProgressBar) footerView.findViewById(R.id.loadProgess);
         progress.setVisibility(View.INVISIBLE);
         
         nomoreposts.setVisibility(View.INVISIBLE);
         loadmore.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(loadmoreListener!=null){
					loadmoreListener.onLoadMore();
					
				}
			}
		});
         
        loader = (LoaderImage) headerView.findViewById(R.id.loader);
        measureView(headerView);
        headerContentHeight = headerView.getMeasuredHeight();
        offset = (int) (headerContentHeight*1.5);
        Log.i("debug", "h : "+headerContentHeight);
        headerView.setPadding(0, 0, 0, -1 * headerContentHeight);
        headerView.invalidate();
        
        addHeaderView(headerView, null, false);
        
        addFooterView(footerView);
        
        setOnScrollListener(this);
        
        state = DONE;
        isRefreshable = false;
        
        
        
     }
	 @Override
	public void onGlobalLayout() {
		
		super.onGlobalLayout();
	}
     @Override
     public boolean onTouchEvent(MotionEvent ev) {
         if (isRefreshable) {
             switch (ev.getAction()) {
             case MotionEvent.ACTION_DOWN:
                 if (!isRecored) {
                	 isRecored = true;
                     startY = (int) ev.getY();
                }
                 break;
             case MotionEvent.ACTION_UP:
                 if (state != REFRESHING) {
                         state = DONE;
                         changeHeaderViewByState();
                         loader.stopAnimating();
                 }
                 isRecored = false;
                 break;
 
             case MotionEvent.ACTION_MOVE:
                 int tempY = (int) ev.getY();
                 
                 if (!isRecored) {
                     isRecored = true;
                     startY = tempY;
                 }
                 
                 int angle = (int) Math.floor((tempY-offset-startY)/250.0 * 360);
                 		 
                 if (state != REFRESHING && isRecored) {
                    
                     if (state == PULL_To_REFRESH) {
                         setSelection(0);
                         headerView.setPadding(0,0, 0, -1 * headerContentHeight + (tempY - startY) / RATIO);
                         loader.setAngle(angle);
                                             
                       	if(angle >=360){
                        		state = REFRESHING;
                                changeHeaderViewByState();
                                onLvRefresh();
                                angle = 0;
                        	}
                      
                     }
                     else if (tempY - startY <= 0) {
                            state = DONE;
                            changeHeaderViewByState();
                     }else{
                    	 if (tempY - startY > 0) {
                             state = PULL_To_REFRESH;
                             changeHeaderViewByState();
                         }
                         headerView.setPadding(0,0, 0, -1 * headerContentHeight + (tempY - startY) / RATIO);
                     }
                 }
                
 
                 break;
 
             default:
                 break;
             }
         }
         return super.onTouchEvent(ev);
     }
 
    private void changeHeaderViewByState() {
         switch (state) {
         
         case PULL_To_REFRESH:
        	
             break;
 
         case REFRESHING:
        	 headerView.setPadding(0,0, 0, 0);
             loader.startAnimating();
             //loader.setVisibility(View.GONE);
             break;
         case DONE:
             headerView.setPadding(0,0, 0, -1*headerContentHeight);
             loader.stopAnimating();
            // loader.setVisibility(View.GONE);
             break;
         }
     }
 
     private void measureView(View child) {
         ViewGroup.LayoutParams params = child.getLayoutParams();
         if (params == null) {
             params = new ViewGroup.LayoutParams(
                     ViewGroup.LayoutParams.MATCH_PARENT,
                     ViewGroup.LayoutParams.WRAP_CONTENT);
         }
         int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0,
                 params.width);
         int lpHeight = params.height;
         int childHeightSpec;
         if (lpHeight > 0) {
             childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
                    MeasureSpec.EXACTLY);
         } else {
             childHeightSpec = MeasureSpec.makeMeasureSpec(0,
                     MeasureSpec.UNSPECIFIED);
         }
         child.measure(childWidthSpec, childHeightSpec);
     }
 
     public void setonRefreshListener(OnRefreshListener refreshListener) {
         this.refreshListener = refreshListener;
         isRefreshable = true;
     }
     public void setonLoadMoreListener(OnLoadMoreListener listener){
    	 this.loadmoreListener = listener;
     }
     public interface OnRefreshListener {
         public void onRefresh();
     }
     
     public interface OnLoadMoreListener{
    	 public void onLoadMore();
     }
 
     public void onRefreshComplete() {
         state = DONE;
        // lvHeaderLastUpdatedTv.setText("Recently updated:" + new Date().toLocaleString());
         changeHeaderViewByState();
     }
 
     private void onLvRefresh() {
         if (refreshListener != null) {
             refreshListener.onRefresh();
         }
     }
 
     public void setAdapter(ArrayAdapter adapter) {
        // lvHeaderLastUpdatedTv.setText("Recently updated:" + new Date().toLocaleString());
         super.setAdapter(adapter);
     }
 
    @Override
     public void onScrollStateChanged(AbsListView view, int scrollState) {
 
     }

    
     @Override
     public void onScroll(AbsListView view, int firstVisibleItem,
             int visibleItemCount, int totalItemCount) {
         if (firstVisibleItem == 0) {
            isRefreshable = true;
         } else {
            isRefreshable = false;
         }
 
    }
     
     public void showNomore(){
    	this.nomoreposts.setVisibility(View.VISIBLE);
    	loadmore.setVisibility(View.INVISIBLE);
    	progress.setVisibility(View.INVISIBLE);
     
     }
 
 }