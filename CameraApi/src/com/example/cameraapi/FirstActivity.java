package com.example.cameraapi;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.fragment.ImageFragment;
import com.example.model.Photo;
import com.example.utils.PictureUtils;
/**
 * 拍摄并处理照片
 * @author MV
 *
 */
public class FirstActivity extends Activity {

	private static final int REQUEST_PHOTO = 1;
	
	private ImageView imageView;
	private Photo photo;
	
	private static final String DIALOG_IMAGE = "image";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.fristactivity_main);
		
		Button button = (Button)findViewById(R.id.cirme_camera);
		imageView = (ImageView)findViewById(R.id.imageview);
		
		photo = new Photo();
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(FirstActivity.this, MainActivity.class);
				//startActivity(intent);
				startActivityForResult(intent, REQUEST_PHOTO);
				
			}
		});
		
		
		imageView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if (photo == null) {
					
					return;
				}
				
				FirstActivity.this.getFragmentManager();
//				FragmentManager fm = 
				String path = FirstActivity.this.getFileStreamPath(photo.getmFileName()).getAbsolutePath();
				
				//ImageFragment.newInstance(path).show(fm, DIALOG_IMAGE);
				
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == REQUEST_PHOTO) {
			if (data != null) {
				
				String fileName = data.getStringExtra(MainActivity.EXTRA_PHOTO_FILENAME);
				if (fileName != null && !fileName.equals("")) {
					
					Log.i("conosle", "fileName = "+fileName);
					photo.setmFileName(fileName);
					showPhoto();
				}
			}
			
		}
	}
	/**
	 * 将缩放后的图片设置给ImageView视图
	 */
	private void showPhoto(){
		
		String photFileString = photo.getmFileName();
		
		BitmapDrawable b = null;
		if (photFileString != null && !photFileString.equals("")) {
			
			String path = this.getFileStreamPath(photFileString).getAbsolutePath();
			
			b = PictureUtils.getScaledDrawable(this, path);
		}
		
		imageView.setImageDrawable(b);
	}
	
	/**
	 * 只要视图一出现在屏幕上，就调用showPhoto方法显示图片
	 */
	@Override
	protected void onStart() {
		super.onStart();
		
		showPhoto();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		PictureUtils.cleanImageView(imageView);
	}
}
