package com.example.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.Display;
import android.widget.ImageView;

/**
 * ͼ����
 * ��ߴ��ͼƬ�����׺ľ�Ӧ���ڵ��ڴ棬��ˣ�����ͼƬǰ����Ҫ��д������СͼƬ
 * ͼƬʹ����ϣ�Ҳ��Ҫ��д��������ɾ����
 * @author MV
 *
 */
public class PictureUtils {

	/**
	 * ��ͼƬ���ŵ��豸Ĭ�ϵ���ʾ�ߴ�
	 * @param a
	 * @param path
	 * @return
	 */
	public static BitmapDrawable getScaledDrawable(Activity a,String path){
		Display display = a.getWindowManager().getDefaultDisplay();
		
		float destWidth = display.getWidth();
		float destHeight = display.getHeight();
		
		BitmapFactory.Options options = new BitmapFactory.Options();
		//�ص�
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
		//�ص�
		options.inSampleSize = inSampleSize;
		
		
		Bitmap bitmap = BitmapFactory.decodeFile(path,options);
		
		return new BitmapDrawable(a.getResources(),bitmap);
	}
	
	/**
	 * ����ж��ͼƬ
	 */
	public static void cleanImageView(ImageView imageView){
		
		if (!(imageView.getDrawable() instanceof BitmapDrawable)) {
			
			return;
		}
		
		BitmapDrawable b = (BitmapDrawable)imageView.getDrawable();
		//�ͷ�bitmapռ�õ�ԭʼ�洢�ռ�,�Ա�����ܵ��ڴ�ľ�����
		b.getBitmap().recycle();
		imageView.setImageDrawable(null);
	}
}
