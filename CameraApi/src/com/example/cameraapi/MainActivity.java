package com.example.cameraapi;

import java.io.FileOutputStream;
import java.util.List;
import java.util.UUID;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

/**
 * 拍摄并处理照片
 * @author MV
 *
 */
public class MainActivity extends Activity implements SurfaceHolder.Callback{
	
	private Camera  mCamear;
	
	private SurfaceView mSurfaceView;
	
	private Button mBtnTake;
	
	private View mProgressContainer;
	
	public static final String EXTRA_PHOTO_FILENAME = "com.example.photo.filename";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.activity_main);
		
		mBtnTake = (Button)findViewById(R.id.cirme_camera_take);
		mSurfaceView = (SurfaceView)findViewById(R.id.crime_camera_surfaceView);
		mProgressContainer = findViewById(R.id.crime_camera_progress);
		mProgressContainer.setVisibility(View.INVISIBLE);
		
		
		SurfaceHolder holder = mSurfaceView.getHolder();
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		holder.addCallback(this);
		mBtnTake.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {

				if (mCamear != null) {
					
					mCamear.takePicture(mShutterCallback, null, mJpegCallback);
				}
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			
			mCamear = Camera.open(0);
		}else{
			
			mCamear = Camera.open();
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if (mCamear != null) {
			
			mCamear.release();
			mCamear = null;
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		if (mCamear == null) {
			
			return;
		}
		Camera.Parameters parameters = mCamear.getParameters();
		//确定预览界面大小
		Size size = getBestSupportedSize(parameters.getSupportedPreviewSizes(), width, height);
		parameters.setPreviewSize(size.width, size.height);
		
		//设置图片尺寸大小
		size = getBestSupportedSize(parameters.getSupportedPictureSizes(), width, height);
	    parameters.setPictureSize(size.width, size.height);
	    
	    
		mCamear.setParameters(parameters);
		try {
			mCamear.setDisplayOrientation(90);
			mCamear.startPreview();
		} catch (Exception e) {
			mCamear.release();
			mCamear = null;
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		
		try {
			if (mCamear != null) {
				
				mCamear.setPreviewDisplay(holder);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		
		if (mCamear != null) {
			
			mCamear.stopPreview();
		}
	}

	/**
	 * 找出设备支持的最佳尺寸
	 * @param sizes
	 * @param width
	 * @param height
	 * @return
	 */
	
	private Size getBestSupportedSize(List<Size> sizes,int width,int height){
		
		Size bestSize = sizes.get(0);
		int largestArea = bestSize.width * bestSize.height;
		for (Size size : sizes) {
			
			int area = size.width * size.height;
			if (area > largestArea) {
				
				bestSize = size;
				largestArea = area;
			}
		}
		return bestSize;
	}
	
	/**
	 * 相机捕获图像时调用
	 */
	private Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback() {
		
		@Override
		public void onShutter() {
			
			mProgressContainer.setVisibility(View.VISIBLE);
		}
	};
	
	private Camera.PictureCallback mJpegCallback = new Camera.PictureCallback() {
		
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			
			String filenameString = UUID.randomUUID().toString() + ".jpg";
			
			FileOutputStream os = null;
			
			boolean success = true;
			
			try {
				os = MainActivity.this.openFileOutput(filenameString, Context.MODE_APPEND);
				os.write(data);
			} catch (Exception e) {
				e.printStackTrace();
				success = false;
			}finally{
				
				try {
					if (os != null) {
						
						os.close();
					}
				} catch (Exception e) {
					
					success = false;
				}
			}
			
			if (success) {
				
				Log.i("console", "JPEG saved at "+filenameString);
				Intent intent = new Intent();
				intent.putExtra(EXTRA_PHOTO_FILENAME, filenameString);
				MainActivity.this.setResult(RESULT_OK,intent);
			}else {
				
				MainActivity.this.setResult(RESULT_CANCELED);
			}
			
			MainActivity.this.finish();
			
		}
	};
}
