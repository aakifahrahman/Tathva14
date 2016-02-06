package org.tathva.triloaded.customviews;

import java.util.List;
import java.util.Vector;

import org.tathva.triloaded.mainmenu.R;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class Tile extends LinearLayout {
	
	private boolean open = false;
	private Context context;
	private int position;
	
	private TextView heading;
	private ListView listView;
	private ImageView image;
	private PlaceList placeAdapter;
	
	private List<String> stringList;
	private String headingText;
	
	private int height;
	private int expandedHeight;
	private int heightExtension = 420;
	
	
	private ResizeAnimation openAnim;
	private ResizeAnimation closeAnim;
	private OnExpandListener expandListener;
	private OnOptionClickListener optionlistener;
	
	public Tile(final Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(open){
					startAnimation(closeAnim);
					open = false;
				}else{
					expandListener.onExpand(position);
					startAnimation(openAnim);
					open = true;
				}
			}
		});
	}
	
	
	public Tile(Context context){
		super(context);
	}
	
	public void setStrings(List<String> list){
		this.stringList = list;
		placeAdapter = new PlaceList(context,stringList);
		listView.setAdapter(placeAdapter);
		//placeAdapter.notifyDataSetChanged();
	}
	
	public void setHeading(String s){
		this.headingText = s;
		heading.setText(headingText);
		//heading.invalidate();
	}
	public void setImage(int i) {
		image.setImageResource(i);
		
	}
	
	public boolean isOpen(){
		return open;
	}
	public void close(){
		if(open){
			startAnimation(closeAnim);
			open = false;
		}
	}
	
	public void open(){
		if(!open){
			startAnimation(openAnim);
			open = true;
		}
	}
	public void setOnExpandListener(OnExpandListener listener){
		this.expandListener = listener;
	}
	
	public void setOnOptionClickListener(OnOptionClickListener listener){
		this.optionlistener= listener;
	}
	
	public void setPosition(int p){
		this.position  = p;
	}
	
	public int getPosition(){
		return position;
	}
	
	@Override
	protected void onFinishInflate() {
	
		super.onFinishInflate();
		heading = (TextView) findViewById(R.id.headText);
		listView = (ListView) findViewById(R.id.tilelist);
		image = (ImageView) findViewById(R.id.tileImage);
		
		listView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				 v.getParent().requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int pos,
					long id) {
				if(optionlistener !=null){
					optionlistener.onOptionClick(stringList.get(pos));
				}
				
			}
		});
		
		//Log.i("anas", "Finish h:"+height+" exp: "+expandedHeight+" h "+this.getHeight());
		
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		
		if(height == 0){
			height = MeasureSpec.getSize(heightMeasureSpec);
			expandedHeight = height+heightExtension;
			//Log.i("anas", "Specs h:"+height+" exp: "+expandedHeight+" h "+this.getHeight());
			
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
			
			openAnim = new ResizeAnimation(Tile.this,height, expandedHeight);
			openAnim.setDuration(500);
			closeAnim = new ResizeAnimation(Tile.this,expandedHeight, height);
			closeAnim.setDuration(500);
		}else{
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}
	}
	
	/**Adapter***/
	
	private class PlaceList extends ArrayAdapter<String>{
		List<String> strings;
		public PlaceList(Context context,List<String> objects) {
			super(context,R.layout.placelistitem,objects);
			strings = objects;
		}
		public void setList(List<String> list){

			this.strings = list;
			notifyDataSetChanged();
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if(convertView == null){
				LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				 v = inflator.inflate(R.layout.placelistitem, null);
			}
			
			TextView tv = (TextView) v.findViewById(R.id.itemText);
			tv.setText(strings.get(position));;
			return v;
		}
		
	}
	/* Interface for handling open and close */
	public interface OnExpandListener{
		public void onExpand(int position);
	}
	
	public interface OnOptionClickListener{
		public void onOptionClick(String place);
	}
	
}
