/**
 * Added by zhangyanhu C01012,2014-11-18
 */
package com.icatch.wcm2app.common;

import java.io.File;
import java.util.LinkedList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

/**
 * Added by zhangyanhu C01012,2014-11-18
 */
public class FileTools {
//private LinkedList<File> list;
//private LinkedList<String> fileList;
	private  static String[] Urls = null;
	public static String[] getUrls(String path){
		//if (Urls == null) {
			File folder = new File(Environment.getExternalStorageDirectory()
					.toString() + path);
			String[] temp = folder.list();
			LinkedList<String> tempList = new LinkedList<String>();
			tempList.clear();
			for (int ii = 0; ii < temp.length; ii++) {
				if(temp[ii].endsWith(".jpg") || temp[ii].endsWith(".png") || temp[ii].endsWith(".PNG") || temp[ii].endsWith(".JPG")){
					tempList.addLast(temp[ii]);
				}
//				Urls[ii] = Environment.getExternalStorageDirectory().toString()
//						+ "/DCIM/WifiCamMobileAppAlbum/" + Urls[ii];
			}
			Urls = new String[tempList.size()];
			for (int ii = 0; ii < tempList.size(); ii++) {
				Urls[ii] = Environment.getExternalStorageDirectory().toString()
						+ path + tempList.get(ii);
			}
			tempList = null;
			temp = null;
			Log.d("1111", "Urls.length ==" + Urls.length);
			// for()
		//}
		return Urls;
	}
private static void getAllFiles(File root){  
        
        File files[] = root.listFiles();  
        LinkedList<File> list =new LinkedList<File>();  
        if(files != null)  
        for(File f:files){  
          
            if(f.isDirectory()){  
                getAllFiles(f);  
            }  
            else{  
                list.addLast(f);  
            }  
        }  
    } 
		
	public static void GetFiles(String Path) //搜索目录，扩展名，是否进入子文件夹
	{
		Path = Environment.getExternalStorageDirectory().toString() + Path;
		Log.e("tigertiger","path ="+Path);
		//JpgFileFilter jpgFileFilter = new JpgFileFilter();
	    String[] files = new File(Path).list();
	    Log.e("tigertiger","f.getName() ="+files[0]);
	}
	
	public static Bitmap getBitmapFromFolder(String path,int reqWidth){
		WriteLogToDevice.writeLog("[Normal] -- FileTools: ", "getBitmapFromFolder path ="+path);
		path = Environment.getExternalStorageDirectory().toString() + path;
		Log.e("tigertiger","path ="+path);
		//JpgFileFilter jpgFileFilter = new JpgFileFilter();
		File temp = new File(path);
		if (!temp.exists()) {
			temp.mkdirs();
		}
	    String[] files = temp.list();
	    if(files == null || files.length  == 0){
	    	Log.e("tigertiger","files == null || files.length  == 0");
	    	WriteLogToDevice.writeLog("[Normal] -- FileTools: ", "no files found");
	    	return null;
	    }
	    path = path+files[0];
	    WriteLogToDevice.writeLog("[Normal] -- FileTools: ","get first file name ="+path);
	    Bitmap bitmap = null;
	    
	   /* try  
	    {  
	        File file = new File(path);  
	        if(file.exists())  
	        {  
	            bitmap = BitmapFactory.decodeFile(path);  
	        }  
	    } catch (Exception e)  
	    {  
	        // TODO: handle exception  
	    }
	      BitmapFactory.Options options = new BitmapFactory.Options();  
	        options.inSampleSize = 10;  
	        bitmap = BitmapFactory.decodeFile(path, options);
	       // Log.d("1111","downloadBitmap bitmap = "+bitmap);
	        Bitmap newBitmap = ThumbnailUtils.extractThumbnail(bitmap,  
	        		500, 500);  
	        //Log.d("1111","downloadBitmap newBitmap = "+newBitmap);
			if (bitmap != null) {
				bitmap.recycle();

			}
			//return newBitmap;
	    */
	   // Log.e("tigertiger","bitmap ==="+bitmap);
	    
	    WriteLogToDevice.writeLog("[Normal] -- FileTools: ","get first bitmap  ="+bitmap);
	    return getBitmapByWidth(path,reqWidth,5);
	//
	}
	
	
	 public static Bitmap getBitmapByWidth(String localImagePath, int width,
	            int addedScaling)
	    {
	        if (TextUtils.isEmpty(localImagePath))
	        {
	            return null;
	        }
	        
	        Bitmap temBitmap = null;
	        
	        try
	        {
	            BitmapFactory.Options outOptions = new BitmapFactory.Options();
	            
	            // 设置该属性为true，不加载图片到内存，只返回图片的宽高到options中。
	            outOptions.inJustDecodeBounds = true;
	            
	            // 加载获取图片的宽高
	            BitmapFactory.decodeFile(localImagePath, outOptions);
	            
	            int height = outOptions.outHeight;
	            
	            if (outOptions.outWidth > width)
	            {
	                // 根据宽设置缩放比例
	                outOptions.inSampleSize = outOptions.outWidth / width + 1
	                        + addedScaling;
	                outOptions.outWidth = width;
	                
	                // 计算缩放后的高度
	                height = outOptions.outHeight / outOptions.inSampleSize;
	                outOptions.outHeight = height;
	            }
	            
	            // 重新设置该属性为false，加载图片返回
	            outOptions.inJustDecodeBounds = false;
	            outOptions.inPurgeable = true;
	            outOptions.inInputShareable = true;
	            temBitmap = BitmapFactory.decodeFile(localImagePath, outOptions);
	        }
	        catch (Throwable t)
	        {
	            t.printStackTrace();
	        }
	        
	        return temBitmap;
	    }
}
