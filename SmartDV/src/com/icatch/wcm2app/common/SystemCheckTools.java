/**
 * Added by zhangyanhu C01012,2014-8-4
 */
package com.icatch.wcm2app.common;

import java.io.File;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

/**
 * Added by zhangyanhu C01012,2014-8-4
 */
public class SystemCheckTools {

	public static boolean isApplicationSentToBackground(final Context context) {
	    ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
	    List<RunningTaskInfo> tasks = am.getRunningTasks(1);
	    if (!tasks.isEmpty()) {
	        ComponentName topActivity = tasks.get(0).topActivity;
	        if (!topActivity.getPackageName().equals(context.getPackageName())) {
	        	WriteLogToDevice.writeLog("SystemCheckTools:[Error]","running in background");
	            return true;
	        }
	    }
	    Log.e("tigertiger","running in foreground");
	    return false;
	}
	
	public static long getSDFreeSize(){  
	     //ȡ��SD���ļ�·��  
	     File path = Environment.getExternalStorageDirectory();   
	     StatFs sf = new StatFs(path.getPath());   
	     //��ȡ������ݿ�Ĵ�С(Byte)  
	     long blockSize = sf.getBlockSize();   
	     //���е���ݿ������  
	     long freeBlocks = sf.getAvailableBlocks();  
	     //����SD�����д�С  
	     //return freeBlocks * blockSize;  //��λByte  
	     //return (freeBlocks * blockSize)/1024;   //��λKB  
	     return (freeBlocks * blockSize); //��λbyte  
	   }

}
