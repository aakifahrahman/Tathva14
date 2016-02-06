package org.tathva.triloaded.anubhava;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

import org.tathva.triloaded.anubhava.Uploader.ProgressCounter;
import org.tathva.triloaded.anubhava.Uploader.UploadFinishListener;
import org.tathva.triloaded.customviews.CaptionDialog;
import org.tathva.triloaded.customviews.CaptionDialog.UploadListener;
import org.tathva.triloaded.customviews.StampDialog;
import org.tathva.triloaded.customviews.StampDialog.OnClickStampListener;
import org.tathva.triloaded.mainmenu.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ImageFormat;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SharePhoto extends Activity implements OnClickListener, ProgressCounter, UploadFinishListener{

	ImageView iv;
	Bitmap bmp;
	Bitmap shown;

	final private int PICK_IMAGE = 1;

	final private int CAPTURE_IMAGE = 2;
	int picNumber;
	File dir;
	File originalFile;
	String imageFilePath = "";
	Uri imageFileUri;
	private Button takePic;
	private Button sharingButton;
	private Button gallery;
	private Button stampBtn;
	
	private TextView momentText;
	private TextView momentCaption;
		
	private Integer[] stamp_images = {
			R.drawable.stamp_1,
			R.drawable.stamp_2,
			R.drawable.stamp_3,
			R.drawable.stamp_4,
			R.drawable.stamp_5,
			R.drawable.stamp_6,
			R.drawable.stamp_7
		};
	private ProgressDialog progress;
	private long fileSize;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
			
	    progress = new ProgressDialog(this);
	    progress.setMessage("Uploading photo...");
	    progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	    progress.setIndeterminate(false);
	    
		setContentView(R.layout.anu_newsharelayout);
		
		momentText = (TextView) findViewById(R.id.tathvamomentText);
		Typeface fontastic = Typeface.createFromAsset(getAssets(), "fonts/fontastique.ttf");
		momentText.setTypeface(fontastic);
		momentCaption = (TextView) findViewById(R.id.momentCaption);
		Typeface reenie = Typeface.createFromAsset(getAssets(), "fonts/reenie.ttf");
		momentCaption.setTypeface(reenie);
		
		picNumber = loadSavePicsCountNumber();
		iv = (ImageView) findViewById(R.id.postImageView);
		takePic = (Button) findViewById(R.id.share_camera_btn);
		sharingButton = (Button) findViewById(R.id.share_upload_btn);
		gallery = (Button) findViewById(R.id.share_gallery_btn);
		stampBtn = (Button) findViewById(R.id.shareapp_stamp_btn);
		
		stampBtn.setOnClickListener(this);
		
		sharingButton.setVisibility(View.INVISIBLE);
		stampBtn.setVisibility(View.INVISIBLE);
		sharingButton.setOnClickListener(this);
		
		takePic.setOnClickListener(this);
		gallery.setOnClickListener(this);
		
		dir = new File(Environment.getExternalStorageDirectory()
				+ "/Tathva-14");
		if (!dir.exists()) {
			dir.mkdir();
		}
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.shareapp_stamp_btn:
			StampDialog stampdialog = new StampDialog(this,new OnClickStampListener() {
				
				@Override
				public void onClick(int n) {
					if(n==0){
						shown = bmp;
						iv.setImageBitmap(shown);
					}else{
						shown = addWaterMark(bmp, n);
						iv.setImageBitmap(shown);
					}
				}
			});
			stampdialog.show();
		
			break;
		case R.id.share_upload_btn:
			savePic(shown);
			File imgFile = new File(imageFilePath);
			fileSize = imgFile.length();
			Log.i("debug", "uploading the photo"+fileSize);
			
			CaptionDialog capDialog = new CaptionDialog(this, new UploadListener() {
				
				@Override
				public void onUpload(String caption) {
					
					try {
						String captionString = URLEncoder.encode(caption,"UTF-8");
						progress.setMax((int) fileSize);
						progress.show();
						Uploader upload = new Uploader();
						upload.postFields(AnubhavaUtils.getFbId(SharePhoto.this), 
								captionString, imageFilePath, SharePhoto.this,
								SharePhoto.this);
						
						
					} catch (UnsupportedEncodingException e) {
						Log.i("debug", "error String!!");
					}
				}
			});
			
			capDialog.show();
				
			break;
		
		case R.id.share_camera_btn:
			Log.i("debug", "camera");
			
			imageFilePath = "";
			imageFilePath = dir + File.separator + picNumber + ".jpg";
			originalFile = new File(imageFilePath);
			imageFileUri = Uri.fromFile(originalFile);
			
			Intent i = new Intent(
					android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			i.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageFileUri);
			startActivityForResult(i, CAPTURE_IMAGE);

			break;

		case R.id.share_gallery_btn:
			Log.i("debug", "gallery");
			/*Intent g = new Intent();
			g.setType("image/*");
			g.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(Intent.createChooser(g, "Pic Selection"), PICK_IMAGE);
			*/
			Intent in = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(in, PICK_IMAGE);
			break;

		}

	}

	void savePicsCountNumber(int picLastNumber) {
		SharedPreferences pref1 = getPreferences(MODE_PRIVATE);
		Editor editor = pref1.edit();
		editor.putInt("picLastNumber", picLastNumber);
		editor.commit();
	}

	int loadSavePicsCountNumber() {
		SharedPreferences preferences = getPreferences(MODE_PRIVATE);
		int val = preferences.getInt("picLastNumber", 1);
		return val;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == PICK_IMAGE) {
				Log.i("debug","resultactivity");
				imageFilePath = getAbsolutePath(data.getData());
				Log.i("debug","image"+imageFilePath);
				/*BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
				bmpFactoryOptions.inJustDecodeBounds = false;
				bmpFactoryOptions.inMutable=true;
				bmp = BitmapFactory
						.decodeFile(imageFilePath, bmpFactoryOptions);**/
				bmp = AnubhavaUtils.loadImageForSharing(imageFilePath);
				shown = addWaterMark(bmp, stamp_images[0]);
				iv.setImageBitmap(shown);
				
				onSelectionButtonStates();
				
			} else if (requestCode == CAPTURE_IMAGE) {

				bmp = AnubhavaUtils.loadImageForSharing(imageFilePath);
				shown = addWaterMark(bmp, stamp_images[0]);
				iv.setImageBitmap(shown);
				
				picNumber++;
				savePicsCountNumber(picNumber);
				onSelectionButtonStates();
				
			} else {
				
				
				
			}
		}else{
			//Bad Result!!!
			
			
		}
	}

	private void onSelectionButtonStates() {
		gallery.setVisibility(View.INVISIBLE);
		takePic.setVisibility(View.INVISIBLE);
		sharingButton.setVisibility(View.VISIBLE);
		momentCaption.setVisibility(View.INVISIBLE);
		iv.setVisibility(View.VISIBLE);
		stampBtn.setVisibility(View.VISIBLE);
	}

	public String getAbsolutePath(Uri uri) {
	
        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri,filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
		return  picturePath;
		
	}
	
	private Bitmap addWaterMark(Bitmap src, int resourceId){
		int w = src.getWidth();
		int h = src.getHeight();
		
		Bitmap result = Bitmap.createBitmap(w, h, src.getConfig());
		Canvas canvas = new Canvas(result);
		
		canvas.drawBitmap(src, 0, 0, null);
		
		Bitmap watermark =  BitmapFactory.decodeResource(this.getResources(),resourceId);
		canvas.drawBitmap(watermark, 0, 0, null);
		
		return result;
	}

	@Override
	public void uploaded(long num) {
		
		int percent = (int) (num*100/fileSize);
		Log.i("Hey!!! Upload", "++ "+ num+" size :"+fileSize+" per: "+percent);
		progress.setProgress((int) num);
		
		if(progress.getProgress()>=100){
			progress.setTitle("Upload Complete");
			Log.i("debug","UPLOAD COMPLETED!!!!!!!!");
			
		}
	}
	
	public String savePic(Bitmap bitmapImage){
        File mypath = new File(imageFilePath);
        int quality;
        if(mypath.length()>1000000){
        	quality = 50;
        }else if(mypath.length() >500000){
        	quality = 75;
        }else{
        	quality = 90;
        }
        FileOutputStream fos = null;
        try {           
            fos = new FileOutputStream(mypath);
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, quality, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mypath.getAbsolutePath();
    }

	@Override
	public void onFinish() {
	 
		progress.dismiss();
		finish();
	}
	
	
	
	


}
