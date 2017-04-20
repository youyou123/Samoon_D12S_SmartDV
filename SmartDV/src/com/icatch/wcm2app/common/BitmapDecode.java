/**
 * Added by zhangyanhu C01012,2014-7-30
 */
package com.icatch.wcm2app.common;

//import com.icatch.wificam.app.test.test;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

/**
 * Added by zhangyanhu C01012,2014-7-30
 */
public class BitmapDecode {
	private static Bitmap bitmap;

	public static Bitmap decodeSampledBitmapFromByteArray(byte[] data, int offset, int length, int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
//		Log.d("1111","length ="+length);
//		Log.d("1111","reqWidth ="+reqWidth);
//		Log.d("1111","reqHeight ="+reqHeight);
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(data, offset, length, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		Log.e("tigertiger","options.inSampleSize ="+options.inSampleSize);
		// Decode decodeBitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		
//		 if(bitmap != null &&bitmap.isRecycled()==false) //���û�л���         
//			 bitmap.recycle();  
		try { 
			 bitmap =  BitmapFactory.decodeByteArray(data, offset, length, options);
			 Log.d("1111","do have exception?");
          }catch (OutOfMemoryError e) { 
                  e.printStackTrace(); 
                  // TODO: handle exception 
                  WriteLogToDevice.writeLog("[Error] -- BitmapDecode: ", "out of memery  ");
          } 
		//test.saveImage(bitmap);
		return zoomBitmap(bitmap,reqWidth,reqHeight);
	}

	//����Ŵ���Сֵ
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}
	
	 public static Bitmap zoomBitmap(Bitmap bitmap,float width,float heigth) {
		 float zoomRate = 1.0f;
		 if(bitmap.getWidth() >= width || bitmap.getHeight() >=  heigth){
		 }else if(width*bitmap.getHeight() > heigth*bitmap.getWidth()){
			 zoomRate = heigth/bitmap.getHeight();
		 }else if(width*bitmap.getHeight() <= heigth*bitmap.getWidth()){
			 zoomRate = width/bitmap.getWidth();
		 }
		 Log.d("tigertiger","zoomRate ="+zoomRate);
		  Matrix matrix = new Matrix(); 
		  matrix.postScale(zoomRate,zoomRate); //���Ϳ�Ŵ���С�ı���
		  Bitmap resizeBmp = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
		  return resizeBmp;
		 }

}
