/**
 * Added by zhangyanhu C01012,2014-5-6
 */
package com.icatch.wcm2app.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Environment;
import android.util.Log;

import com.icatch.wcm2app.Data.BaseResource;

/**
 * Added by zhangyanhu C01012,2014-5-6
 */
public class WriteLogToDevice {
	private static String writeFile;
	private static FileOutputStream out = null;
	private static boolean hasConfiguration = false;
	private static File writeLogFile = null;
	private static long maxFileSize = 1024 * 1024 * 50;

	public static void initConfiguration() {
		File directory = null;
		String fileName = null;
		String path = null;
		// if
		// (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
		// {
		Log.d("tigertiger", "....................initConfiguration.................");
		long time = System.currentTimeMillis();
		Date date = new Date(time);
		// System.out.println(date);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH@mm@ss");
		path = Environment.getExternalStorageDirectory().toString() + "/IcatchWifiCamera_APP_Log/";
		Log.d("tigertiger", ".....................................sdf.format(date) =" + sdf.format(date));
		Log.d("tigertiger", "path: " + path);
		if (path != null) {
			directory = new File(path);
			if (!directory.exists()) {
				directory.mkdirs();
			}
		}
		fileName = sdf.format(date) + ".log";
		File file = new File(directory, fileName);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		writeFile = path + fileName;
		writeLogFile = new File(writeFile);
		//Log.d("tigertiger", "writeFile: " + writeFile);
		if(out != null){
			closeWriteStream();
		}
		try {
			out = new FileOutputStream(writeFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		hasConfiguration = true;
		
		writeLog(sdf.format(date) + "\n");
		writeLog(BaseResource.getInstance().getAPPVersion() + "\n");
		Log.d("tigertiger", "out: " + out);
	}

	public static String getSystemDate() {
		long time = System.currentTimeMillis();
		Date date = new Date(time);
		// System.out.println(date);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:sss	");
		return sdf.format(date);
	}

	/**
	 * 追加文件：使用FileOutputStream，在构造FileOutputStream时，把第二个参数设为true
	 * 
	 * @param fileName
	 * @param content
	 */
	public static void writeLog(String conent) {
		if(!hasConfiguration){
			return;
		}
		if(writeLogFile.length() >= maxFileSize){
			initConfiguration();
		}
		String temp = getSystemDate()+":"+conent+"\n";
		try {
			out.write(temp.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}
	/**
	 * 
	 * Added by zhangyanhu C01012,2014-5-20
	 */
	public static void writeLog(String tag,String conent) {
		if(!hasConfiguration){
			return;
		}
		if(writeLogFile.length() >= maxFileSize){
			initConfiguration();
		}
		String temp = getSystemDate()+":"+tag+": "+conent+"\n";
//		Log.d("tigertiger",tag+":"+conent);
		try {
			//Log.e("tigertiger","out ======"+out);
			if(out != null){
				out.write(temp.getBytes());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}
	/**
	 * 
	 * Added by zhangyanhu C01012,2014-5-20
	 */
	public static void closeWriteStream() {
		try {
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}