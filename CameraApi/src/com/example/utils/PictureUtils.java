package com.example.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.Display;
import android.widget.ImageView;

/**
 * 图像处理
 * 大尺寸的图片很容易耗尽应用于的内存，因此，加载图片前，需要编写代码缩小图片
 * 图片使用完毕，也需要编写代码清理删除它
 * @author MV
 *
 */
public class PictureUtils {

	/**
	 * 将图片缩放到设备默认的显示尺寸
	 * @param a
	 * @param path
	 * @return
	 */
	public static BitmapDrawable getScaledDrawable(Activity a,String path){
		Display display = a.getWindowManager().getDefaultDisplay();
		
		float destWidth = display.getWidth();
		float destHeight = display.getHeight();
		
		BitmapFactory.Options options = new BitmapFactory.Options();
		//重点
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		
		float srcWidth = options.outWidth;
		float srcHeigth = options.outHeight;
		
		int inSampleSize = 1;
		if (srcHeigth > destHeight || srcWidth > destWidth) {
			
			if (srcWidth > srcHeigth) {
				
				inSampleSize = Math.round(srcHeigth / destHeight);
			}else {
				
				inSampleSize = Math.round(srcWidth / destWidth);
			}
		}
		
		options = new BitmapFactory.Options();
		//重点
		options.inSampleSize = inSampleSize;
		
		
		Bitmap bitmap = BitmapFactory.decodeFile(path,options);
		
		return new BitmapDrawable(a.getResources(),bitmap);
	}
	
	/**
	 * 清理卸载图片
	 */
	public static void cleanImageView(ImageView imageView){
		
		if (!(imageView.getDrawable() instanceof BitmapDrawable)) {
			
			return;
		}
		
		BitmapDrawable b = (BitmapDrawable)imageView.getDrawable();
		//释放bitmap占用的原始存储空间,以避免可能的内存耗尽问题
		b.getBitmap().recycle();
		imageView.setImageDrawable(null);
	}
}
