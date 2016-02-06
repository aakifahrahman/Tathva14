package org.tathva.triloaded.anubhava;

import java.io.File;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Vector;

import org.json.JSONArray;
import org.tathva.triloaded.anubhava.FetchImageTask.OnCompletionListener;
import org.tathva.triloaded.anubhava.ImagesDownloadTask.OnLoadFinishListener;
import org.tathva.triloaded.anubhava.ScriptRunner.ScriptFinishListener;
import org.tathva.triloaded.customviews.CustomListView;
import org.tathva.triloaded.customviews.CustomListView.OnLoadMoreListener;
import org.tathva.triloaded.customviews.CustomListView.OnRefreshListener;
import org.tathva.triloaded.customviews.LoaderImage;


import org.tathva.triloaded.mainmenu.R;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.sax.StartElementListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;


public class PostList {

	private Context context;
	private CustomListView list;
	private LayoutInflater inflator;
	private List<Photo> photos = new Vector<Photo>();
	private PhotoAdapter listAdapter;
	private AnubhavaDB anuDb;
	private LoaderImage loader;
	private boolean isUpdating = false;
	private boolean isLoadingMoreFromNet = false;
	private int loadCount;
	
	public PostList(Context c,CustomListView lv,LoaderImage loader,int count) {
		this.context = c;
		this.list = lv;
		this.loader = loader;
		this.loadCount = count;
		inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		anuDb = new AnubhavaDB(context);
		anuDb.deleteFromEnd();
		loader.startAnimating();
		loadFromDatabase();
		update();	
	}
	private void loadFromDatabase() {
		photos = anuDb.getPosts(null,loadCount);
		Log.i("debug","Database"+photos.size());
		if(photos.size() != 0){
			setData();
			loader.stopAnimating();
			loader.setVisibility(View.INVISIBLE);
		}
	}
	private void update() {
		
		if(isUpdating){
			Log.i("debug", "update called again while updating !! return!");
			return;
		}
		Log.i("debug", "update()--> checking for updates!");
		ScriptRunner executer = new ScriptRunner(new ScriptFinishListener() {
			@Override
			public void finish(String result, int resultCode) {
				if(resultCode == ScriptRunner.SUCCESS){
					if(!result.startsWith("null")){
						onScriptResult(result);
					}else{
						//No Updates!!!
					}
					
				}else{
					Log.i("debug", "Script Execution Fail.Check Internet Connection!");
					//try update again 
					// update();
				}
				isUpdating = false;
			}
		});
		
		String max = anuDb.getMaxUpdate();
		Log.i("debug", " Max id passed to getposts.php?maxid= "+max);
		executer.execute(AnubhavaUtils.SERVER+"getposts.php?maxid="+max);
		
	}
	
	private void onScriptResult(String result){
		
		Log.i("debug", "json :"+result);
		
		List<Photo> newphotos = AnubhavaUtils.getPhotos(result,context);
	    if(newphotos == null){
	    	//Json Exception thrown!! EXIT
	    	return;
	    }
	    
		int currentPostCount = anuDb.getCount();
		Log.i("tag", "current posts in db: "+currentPostCount);
		//downloadPhotos(newphotos);
		//photos.get(i).setBiengDownloaded(true);
		
		anuDb.updatePosts(newphotos);
		for(int i=0;i<newphotos.size();i++){
			photos.add(i, newphotos.get(i));
			photos.get(i).setBiengDownloaded(true);
		}
		downloadPhotos(newphotos);
		
		if(currentPostCount == 0){
			setData();
		}else{
			if(listAdapter!=null){
				listAdapter.notifyDataSetChanged();
			}
		}
		
	}
	
	
	private void updateOnRefresh() {
		
		if(isUpdating){
			Log.i("debug", "updateOnRefresh called again while updating !! return!");
			return;
		}
		Log.i("debug", "updating on refresh!!!");
		ScriptRunner executer = new ScriptRunner(new ScriptFinishListener() {
			
			@Override
			public void finish(String result, int resultCode) {
				if(resultCode == ScriptRunner.SUCCESS){
					
					if(!result.startsWith("null")){
						
						Log.i("debug", "json "+result);
						
						List<Photo> newphotos = AnubhavaUtils.getPhotos(result,context);
						if(newphotos == null){
					    	//Json Exception thrown!! EXIT
					    	return;
						}
						anuDb.updatePosts(newphotos);
						for(int i=0;i<newphotos.size();i++){
							photos.add(i, newphotos.get(i));
							photos.get(i).setBiengDownloaded(true);
						}
						downloadPhotos(newphotos);
						listAdapter.notifyDataSetChanged();
                        list.onRefreshComplete();
						
					}else{
						list.onRefreshComplete();
					}
					
				}else{
					Log.i("debug", "failed");
				}
				isUpdating = false;
			}
		});
		Log.i("debug", AnubhavaUtils.SERVER+"getposts.php?maxid="+anuDb.getMaxUpdate());
		executer.execute(AnubhavaUtils.SERVER+"getposts.php?maxid="+anuDb.getMaxUpdate());
		
	}

	
	
	private void setData(){
		//loader.stopAnimating();
		//loader.setVisibility(View.INVISIBLE);
		listAdapter = new PhotoAdapter(context, photos);
		list.setAdapter(listAdapter);
		Log.i("debug", "photos"+photos.size());
		listAdapter.notifyDataSetChanged();
		
		
		list.setonRefreshListener(new OnRefreshListener() {
			             @Override
			             public void onRefresh() {
			                        	 updateOnRefresh();
			                     }
		
			         });
		
		list.setonLoadMoreListener(new OnLoadMoreListener() {
			
			@Override
			public void onLoadMore() {
				list.setLoadingState();
				String minId = photos.get(photos.size()-1).getId();
				Log.i("debug", "Load More: Min Id :"+minId);
				//if minid is lowest in db , we need to download posts
				if(anuDb.getMinId().equals(minId)){
					Log.i("debug","loading more posts from net");
					//if already loading return
					if(isLoadingMoreFromNet){
						return;
					}
					ScriptRunner r = new ScriptRunner(new ScriptFinishListener() {
							
							@Override
							public void finish(String result, int resultCode) {
								
								if(resultCode == ScriptRunner.SUCCESS){
									
									if(!result.startsWith("null")){
										//there are posts
										Log.i("debug", "result on loadmorenet:  "+result);
										List<Photo> newphotos = AnubhavaUtils.getPhotos(result,context);
										if(newphotos == null){
									    	//Json Exception thrown!! EXIT
									    	return;
										}
										anuDb.updatePosts(newphotos);
										int n = photos.size();
										for(int i=0;i<newphotos.size();i++){
											photos.add(n+i, newphotos.get(i));
											photos.get(n+i).setBiengDownloaded(true);
										}
										downloadPhotos(newphotos);
										
										listAdapter.notifyDataSetChanged();
										
									}else{
										//no more posts
										Log.i("debug", "Show No More");
										
										list.showNomore();
									}
									
									isLoadingMoreFromNet = false;
									
								}
							}
						});
						
						r.execute(AnubhavaUtils.SERVER+"getposts.php?minid="+minId);
						isLoadingMoreFromNet = true;
				 
				 }else{
					 //there are more posts saved in the db
//					 List<Photo> morephotos = anuDb.getPosts(minId);
					 List<Photo> morephotos = anuDb.getPosts(minId,0);
					 Log.i("debug", "Loading More from db : count: "+morephotos.size());
					 int n = photos.size();
					 for(int i=0;i<morephotos.size();i++){
						 photos.add(n+i, morephotos.get(i));
					 }
					 listAdapter.notifyDataSetChanged();
					 list.setRestState();
				 }
				
			}
		});
	
	}

	public Photo getItem(int position){
		return photos.get(position-1);
	}
	
	public void setSelection(String id){
		for(int i=0;i<photos.size();i++){
			if(photos.get(i).getId().equals(id)){
				Log.i("debug", "got it"+i);
				list.setSelection(i);
			}
			
		}
	}
	
	private class PhotoAdapter extends ArrayAdapter<Photo>{
		
		public PhotoAdapter(Context context,List<Photo> objects) {
			super(context,R.layout.anubhava_photo_post,objects);
			//Log.i("debug", "Adapter created");
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			//Log.i("debug", "Adapter called");
			View v = convertView;
			Photo photo = photos.get(position);
			
			if(convertView == null){
				 v = inflator.inflate(R.layout.anubhava_photo_post, null);
				
			}
			 AnubhavaPhoto tile = (AnubhavaPhoto) v.findViewById(R.id.fbPost);
			 tile.setData(photo.getUser_name(), photo.getCaption());
			 //tile.setBlank();//sets white background
			 
			 if(!photo.isBiengDownloaded()){
				 if(photo.checkPostExits()){
					 Log.i("debug", "getView() file present!!");	
					 //loadBitmap(photos.get(position).getLocal_post_url(), tile.getPostImage());
					 tile.getPostImage().setImageBitmap(AnubhavaUtils.loadImageFromStorage(photo.getLocal_post_url()));
					 tile.stopLoading();
				 }else{
					 //failed download earlier
					 Log.i("debug", "getView() didnt download properly :starting download!!");	
					 photo.setBiengDownloaded(true);
					 DownloadSingle(photo);
					 tile.setBlank();
					 tile.showLoading();
					// Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.whitebackground);
					// tile.setPostImage(bm);
				 }
			 }else{
				 Log.i("debug", "getView(): Photo being downloaded!!");	
				 tile.showLoading();
				 tile.setBlank();
				// Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.whitebackground);
				 //tile.setPostImage(bm);
			 }
			 
			 
			 if(photo.checkProfileExits()){
				 tile.setProfileImage(AnubhavaUtils.loadImageFromStorage(photos.get(position).getLocal_profile_url()));
			 }else{
				 Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.anu_profile_pic_loading);
				 tile.setProfileImage(bm);	
			 }
			 return v;
		}
		
	}
	
	public int getLoadCount(){
		return photos.size();
	}
	
	
	
	private void downloadPhotos(final List<Photo> newphotos) {
		
		
	    ImagesDownloadTask task = new ImagesDownloadTask(newphotos,context,new OnLoadFinishListener() {
				
			@Override
			public void onFinish() {
				listAdapter.notifyDataSetChanged();
				list.setRestState();
			}
		});
	    task.execute();
		
	}
	
	private void DownloadSingle(Photo photo){
		List<Photo> newpotos = new Vector<Photo>();
		newpotos.add(photo);
		 ImagesDownloadTask task = new ImagesDownloadTask(newpotos,context,new OnLoadFinishListener() {
				
				@Override
				public void onFinish() {
					listAdapter.notifyDataSetChanged();
				}
			});
		    task.execute();
		
	}
	
	
	private void SetupImages() {
		
	};
	
	
	/* Memory Management **/
	
	public void loadBitmap(String url, ImageView imageView) {
	    if (cancelPotentialWork(url, imageView)) {
	    	Bitmap mPlaceHolderBitmap= BitmapFactory.decodeResource(context.getResources(), R.drawable.anu_whitebackground);
	        final BitmapWorkerTask task = new BitmapWorkerTask(imageView);
	        final AsyncDrawable asyncDrawable =
	                new AsyncDrawable(context.getResources(), mPlaceHolderBitmap, task);
	        imageView.setImageDrawable(asyncDrawable);
	        imageView.setVisibility(View.VISIBLE);
	        task.execute(url);
	    }
	}
	
	public static boolean cancelPotentialWork(String url, ImageView imageView) {
	    final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

	    if (bitmapWorkerTask != null) {
	        final String bitmapData = bitmapWorkerTask.url;
	        // If bitmapData is not yet set or it differs from the new data
	        if (bitmapData.equals("") || bitmapData != url) {
	            // Cancel previous task
	            bitmapWorkerTask.cancel(true);
	        } else {
	            // The same work is already in progress
	            return false;
	        }
	    }
	    // No task associated with the ImageView, or an existing task was cancelled
	    return true;
	}
	
	private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
		   if (imageView != null) {
		       final Drawable drawable = imageView.getDrawable();
		       if (drawable instanceof AsyncDrawable) {
		           final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
		           return asyncDrawable.getBitmapWorkerTask();
		       }
		    }
		    return null;
		}
	static class AsyncDrawable extends BitmapDrawable {
	    private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;

	    public AsyncDrawable(Resources res, Bitmap bitmap,
	            BitmapWorkerTask bitmapWorkerTask) {
	        super(res, bitmap);
	        bitmapWorkerTaskReference =
	            new WeakReference<BitmapWorkerTask>(bitmapWorkerTask);
	    }

	    public BitmapWorkerTask getBitmapWorkerTask() {
	        return bitmapWorkerTaskReference.get();
	    }
	}
	
	
	
}
