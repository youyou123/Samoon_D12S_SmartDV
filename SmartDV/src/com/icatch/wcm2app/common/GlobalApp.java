package com.icatch.wcm2app.common;

import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.icatch.wificam.customer.type.ICatchFile;

public class GlobalApp extends Application {

	// files from sdk
	private List<ICatchFile> fileList;
	private Context context;
	private static GlobalApp instance = new GlobalApp();
	private Activity activity;
	private boolean isDownloading = false;
	private String ssid;
//	public static final String DOWNLOAD_PATH = "/DCIM/IcatchWifiCam_download/"; 
	public static final String DOWNLOAD_PATH = "/DCIM/WCMAPP2/"; 
	
	public static final int EVENT_BATTERY_ELETRIC_CHANGED = 0x1001;
	public static final int EVENT_SD_CARD_FULL = 0x1002;
	public static final int EVENT_VIDEO_OFF = 0x1003;
	public static final int EVENT_CAPTURE_COMPLETED = 0x1004;
	public static final int EVENT_CAPTURE_START = 0x1005;
	public static final int EVENT_FILE_ADDED = 0x1006;
	public static final int EVENT_VIDEO_ON = 0x1007;
	public static final int EVENT_CONNECTION_FAILURE = 0x1008;
	public static final int EVENT_TIME_LAPSE_STOP = 0x1009;
	public static final int EVENT_SERVER_STREAM_ERROR = 0x100A;
	public static final int EVENT_VIDEO_RECORDING_TIME = 0x100B;
	
	public static final int MESSAGE_AWAKE_ONE_CAMERA = 0x2001;
	public static final int MESSAGE_REMOVE_CAMERA = 0x2002;
	public static final int MESSAGE_REMOVE_ALL_CAMERA = 0x2003;
	public static final int MESSAGE_AWAKE_ALL_CAMERA = 0x2004;
	public static final int MESSAGE_UPDATE_UI_BURST_ICON = 0x2005;
	public static final int MESSAGE_UPDATE_UI_WHITE_BALANCE_ICON = 0x2006;
	public static final int MESSAGE_UPDATE_UI_IMAGE_SIZE = 0x2007;
	public static final int MESSAGE_UPDATE_UI_VIDEO_SIZE = 0x2008;
	public static final int MESSAGE_UPDATE_UI_CAPTURE_DELAY = 0x2009;
	public static final int MESSAGE_FORMAT_SUCCESS = 0x200A;
	public static final int MESSAGE_FORMAT_FAILED = 0x200B;
	public static final int MESSAGE_FORMAT_SD_START = 0x200C;
	//public static final int MESSAGE_CAPTURE_COMPLETED = 0x200D;
	public static final int MESSAGE_SETTING_TIMELAPSE_STILL_MODE = 0x200F;
	public static final int MESSAGE_SETTING_TIMELAPSE_VIDEO_MODE = 0x2010;
	public static final int MESSAGE_ZOOM_COMPLETED = 0x2011;
	public static final int MESSAGE_GET_NEW_CAMERA = 0x2012;
	public static final int MESSAGE_UPDATE_UI_SLOW_MOTION = 0x2013;
	public static final int MESSAGE_UPDATE_UI_UPSIDE_DOWN = 0x2014;
	
	public static final String CAMERA_FILTER = "WDV8000";
//	public static final String CAMERA_FILTER = "Cansonic";
//	public static final String CAMERA_PASSWORD = "88888888";
	public static final String CAMERA_PASSWORD = "1234567890";
	//private String ssid;

	public static GlobalApp getInstance() {
		return instance;
	}

	// 0049004 Modify by zhangyanhu 2013.12.23
	public List<ICatchFile> getFileList() {
		if (fileList == null) {
			return null;
		} else {
			return fileList;
		}
	}

	// 0049004 End modify by zhangyanhu 2013.12.23
	public boolean setFileList(List<ICatchFile> fileList) {
		this.fileList = fileList;
		return true;
	}

	public void resetFileList() {
		fileList.clear();
	}

	public void setAppContext(Context context) {
		//Log.e("tigertiger", "setAppContext(Context context) ="+context);
		this.context = context;
	}

	public Context getAppContext() {
		return context;
	}

	/**
	 * Added by zhangyanhu C01012,2014-2-13
	 */
	public void setCurrentApp(Activity activity) {
		this.activity = activity;
	}

	/**
	 * Added by zhangyanhu C01012,2014-2-13
	 */
	public Activity getCurrentApp() {
		return activity;
	}
	/**
	 * 
	 * Added by zhangyanhu C01012,2014-5-15
	 */
	public boolean isDownloading() {
		return isDownloading;
		//return false;
	}
	/**
	 * 
	 * Added by zhangyanhu C01012,2014-5-15
	 * @return 
	 */
	public void setDownloadStatus(boolean isDownloading) {
		this.isDownloading = isDownloading;
	}	
	/**
	 * 
	 * Added by zhangyanhu C01012,2014-10-22
	 */
	public void setSsid(String ssid) {
		this.ssid = ssid;
	}	
	
	/**
	 * 
	 * Added by zhangyanhu C01012,2014-10-22
	 */
	public String getSsid() {
		return ssid;
	}
}
