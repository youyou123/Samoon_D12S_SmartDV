/**
 * Added by zhangyanhu C01012,2014-6-23
 */
package com.icatch.wcm2app.Data;

import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;

import com.icatch.wcm2app.common.WriteLogToDevice;
import com.icatch.wcm2app.controller.sdkApi.CameraProperties;
import com.icatch.wcm2app.globalValue.SlowMotion;
import com.icatch.wcm2app.globalValue.TimeLapseDuration;
import com.icatch.wcm2app.globalValue.Upside;
import com.icatch.wcm2app.Data.UIInfo;
import com.icatch.wificam.customer.type.ICatchBurstNumber;
import com.icatch.wificam.customer.type.ICatchCaptureDelay;
import com.icatch.wificam.customer.type.ICatchDateStamp;
import com.icatch.wificam.customer.type.ICatchImageSize;
import com.icatch.wificam.customer.type.ICatchLightFrequency;
import com.icatch.wificam.customer.type.ICatchVideoSize;
import com.icatch.wificam.customer.type.ICatchWhiteBalance;

/**
 * Added by zhangyanhu C01012,2014-6-23
 */
public class SDKReflectToUI {
	private CameraProperties cameraProperty =  new CameraProperties();
	//private Context context;
	
	@SuppressLint("UseSparseArrays")
	private HashMap<Integer, UIInfo> burstMap = new HashMap<Integer, UIInfo>();
	@SuppressLint("UseSparseArrays")
	private HashMap<Integer, UIInfo> whiteBalanceMap = new HashMap<Integer, UIInfo>();
	@SuppressLint("UseSparseArrays")
	private HashMap<Integer, UIInfo> electricityFrequencyMap = new HashMap<Integer, UIInfo>();
	@SuppressLint("UseSparseArrays")
	private HashMap<Integer, UIInfo> dateStampMap = new HashMap<Integer, UIInfo>();
	@SuppressLint("UseSparseArrays")
	private HashMap<Integer, UIInfo> delayTimeMap = new HashMap<Integer, UIInfo>();
	private HashMap<String, UIInfo> imageSizeMap = new HashMap<String, UIInfo>();
	private HashMap<String, UIInfo> videoSizeMap = new HashMap<String, UIInfo>();
	@SuppressLint("UseSparseArrays")
	private HashMap<Integer, UIInfo> timeLapseIntervalMap = new HashMap<Integer, UIInfo>();
	private HashMap<Integer, UIInfo> timeLapseDurationMap = new HashMap<Integer, UIInfo>();
	
	private HashMap<Integer, UIInfo> slowMotionMap = new HashMap<Integer, UIInfo>();
	private HashMap<Integer, UIInfo> upsideMap = new HashMap<Integer, UIInfo>();
	private HashMap<Integer, UIInfo> videoMicMap = new HashMap<Integer, UIInfo>();  
	private HashMap<Integer, UIInfo> videoStampMap = new HashMap<Integer, UIInfo>(); 
	private HashMap<Integer, UIInfo> logoStampMap = new HashMap<Integer, UIInfo>();   	
	@SuppressLint("UseSparseArrays")
	private HashMap<Integer, UIInfo> carNumStampMap = new HashMap<Integer, UIInfo>(); 
	private static SDKReflectToUI sdkReflectToUI;
	private BaseResource baseResource = BaseResource.getInstance();
	
	public static final int SETTING_UI_BURST = 0;
	public static final int SETTING_UI_WHITE_BALANCE = 1;
	public static final int SETTING_UI_ELETRICITY_FREQUENCY = 2;
	public static final int SETTING_UI_DATE_STAMP = 3;
	public static final int SETTING_UI_IMAGE_SIZE = 4;
	public static final int SETTING_UI_VIDEO_SIZE = 5;
	public static final int SETTING_UI_DELAY_TIME = 6;
	public static final int SETTING_UI_CACHE_DURATION = 7;
	public static final int SETTING_UI_TIME_LAPSE_INTERVAL = 8;
	public static final int SETTING_UI_TIME_LAPSE_DURATION = 9;
	public static final int SETTING_UI_SLOW_MOTION = 10;
	public static final int SETTING_UI_UPSIDE = 11;
	public static final int SETTING_UI_VIDEO_MIC = 12; 
	public static final int SETTING_UI_VIDEO_STAMP = 13;  
	public static final int SETTING_UI_LOGO_STAMP = 14;  
	public static final int SETTING_UI_CARNUM_STAMP = 15;  

	public SDKReflectToUI() {
		
	}


	public static SDKReflectToUI getInstance() {
		if(sdkReflectToUI == null){
			sdkReflectToUI = new SDKReflectToUI();
		}
		return sdkReflectToUI;
	}
	
	public static void createSDKReflectToUI(){
		sdkReflectToUI = new SDKReflectToUI();
	}
	
	public void initSDKReflectToUI(){
		initBurstMap();
		initWhiteBalanceMap();
		initElectricityFrequencyMap();
		initDateStampMap();
		initDelayTimeMap();
		initImageSizeMap();
		initVideoSizeMap();
		initTimeLapseInterval();
		initTimeLapseDuration();
		initSlowMotion();
		initUpside();
		initVideoMicMap();
		initVideoStampMap();
		initLogoStampMap();
		initCarNumStampMap();
	}
	
	private void initSlowMotion() {
		// TODO Auto-generated method stub
		String[] slowMotion = baseResource.getSlowMotionUIString();
		slowMotionMap.put(SlowMotion.SLOW_MOTION_OFF, new UIInfo(slowMotion[0],"", 0));
		slowMotionMap.put(SlowMotion.SLOW_MOTION_ON, new UIInfo(slowMotion[1], "",0));
	}
	
	private void initUpside() {
		// TODO Auto-generated method stub
		String[] upside = baseResource.getUpsideUIString();
		upsideMap.put(Upside.UPSIDE_OFF, new UIInfo(upside[0],"", 0));
		upsideMap.put(Upside.UPSIDE_ON, new UIInfo(upside[1], "",0));
	}
	/**
	 * Added by zhangyanhu C01012,2014-8-21
	 */
	private void initTimeLapseDuration() {
		// TODO Auto-generated method stub
		String[] timeLapseDuration = baseResource.getTimeLapseDuration();
		timeLapseIntervalMap.put(TimeLapseDuration.TIME_LAPSE_DURATION_2MIN, new UIInfo(timeLapseDuration[0],"", 0));
		timeLapseIntervalMap.put(TimeLapseDuration.TIME_LAPSE_DURATION_5MIN, new UIInfo(timeLapseDuration[1], "",0));
		timeLapseIntervalMap.put(TimeLapseDuration.TIME_LAPSE_DURATION_10MIN, new UIInfo(timeLapseDuration[2],"",0));
		timeLapseIntervalMap.put(TimeLapseDuration.TIME_LAPSE_DURATION_15MIN, new UIInfo(timeLapseDuration[3],"",0));
		timeLapseIntervalMap.put(TimeLapseDuration.TIME_LAPSE_DURATION_20MIN, new UIInfo(timeLapseDuration[4],"",0));
		timeLapseIntervalMap.put(TimeLapseDuration.TIME_LAPSE_DURATION_30MIN, new UIInfo(timeLapseDuration[5],"",0));
		timeLapseIntervalMap.put(TimeLapseDuration.TIME_LAPSE_DURATION_60MIN, new UIInfo(timeLapseDuration[6],"",0));
		timeLapseIntervalMap.put(TimeLapseDuration.TIME_LAPSE_DURATION_UNLIMITED, new UIInfo(timeLapseDuration[7],"",0));
	}
	
	/**
	 * Added by zhangyanhu C01012,2014-8-21
	 */
	private void initTimeLapseInterval() {
		// TODO Auto-generated method stub
//		String[] timeLapseInterval = baseResource.getTimeLapseInterval();
//		timeLapseDurationMap.put(TimeLapseInterval.TIME_LAPSE_INTERVAL_OFF, new UIInfo(timeLapseInterval[0],"", 0));
//		timeLapseDurationMap.put(TimeLapseInterval.TIME_LAPSE_INTERVAL_2S, new UIInfo(timeLapseInterval[1], "",0));
//		timeLapseDurationMap.put(TimeLapseInterval.TIME_LAPSE_INTERVAL_5S, new UIInfo(timeLapseInterval[2],"",0));
//		timeLapseDurationMap.put(TimeLapseInterval.TIME_LAPSE_INTERVAL_10S, new UIInfo(timeLapseInterval[3],"",0));
//		timeLapseDurationMap.put(TimeLapseInterval.TIME_LAPSE_INTERVAL_20S, new UIInfo(timeLapseInterval[4],"",0));
//		timeLapseDurationMap.put(TimeLapseInterval.TIME_LAPSE_INTERVAL_30S, new UIInfo(timeLapseInterval[5],"",0));
//		timeLapseDurationMap.put(TimeLapseInterval.TIME_LAPSE_INTERVAL_1MIN, new UIInfo(timeLapseInterval[6],"",0));
//		timeLapseDurationMap.put(TimeLapseInterval.TIME_LAPSE_INTERVAL_5MIN, new UIInfo(timeLapseInterval[7],"",0));
//		timeLapseDurationMap.put(TimeLapseInterval.TIME_LAPSE_INTERVAL_10MIN, new UIInfo(timeLapseInterval[8],"",0));
//		timeLapseDurationMap.put(TimeLapseInterval.TIME_LAPSE_INTERVAL_30MIN, new UIInfo(timeLapseInterval[9],"",0));
//		timeLapseDurationMap.put(TimeLapseInterval.TIME_LAPSE_INTERVAL_1HR, new UIInfo(timeLapseInterval[10],"",0));
	}
	
	public void initBurstMap() {
		String[] burstNum = baseResource.getBurstNumUIString();
		Integer[] burstNumIconID = baseResource.getBurstNumIconID();
		burstMap.put(ICatchBurstNumber.ICH_BURST_NUMBER_OFF, new UIInfo(burstNum[0],"", 0));
		burstMap.put(ICatchBurstNumber.ICH_BURST_NUMBER_3, new UIInfo(burstNum[1], "",burstNumIconID[0]));
		burstMap.put(ICatchBurstNumber.ICH_BURST_NUMBER_5, new UIInfo(burstNum[2],"", burstNumIconID[1]));
		burstMap.put(ICatchBurstNumber.ICH_BURST_NUMBER_10, new UIInfo(burstNum[3],"", burstNumIconID[2]));
		burstMap.put(ICatchBurstNumber.ICH_BURST_NUMBER_HS, new UIInfo(burstNum[4],"", 0));
		/**
		 * Sample No:C0001 tags:customized sample for properties; 
		 * Action: add a new value in parent item
		 * Added by zhangyanhu C01012,2014-7-31
		 */
		//need sdk value or fw value of 20 burst
		//burstMap.put(ICatchBurstNumber.BURST_NUMBER_20, new UIInfo(burstNum[5],"", burstNumIconID[3]));
		/**
		 * Sample No:C0001 tags:customized sample for properties; 
		 * Action: add a new value in parent item
		 * End added by zhangyanhu C01012,2014-7-31
		 */
	}

	public void initWhiteBalanceMap() {
		String[] whiteBalance = baseResource.getWhiteBalanceUIString();
		Integer[] whiteBalanceIconID = baseResource.getWhiteBalanceIconID();
		whiteBalanceMap.put(ICatchWhiteBalance.ICH_WB_AUTO, new UIInfo(whiteBalance[0], "",whiteBalanceIconID[0]));
		whiteBalanceMap.put(ICatchWhiteBalance.ICH_WB_DAYLIGHT, new UIInfo(whiteBalance[1], "",whiteBalanceIconID[1]));
		whiteBalanceMap.put(ICatchWhiteBalance.ICH_WB_CLOUDY, new UIInfo(whiteBalance[2], "",whiteBalanceIconID[2]));
		whiteBalanceMap.put(ICatchWhiteBalance.ICH_WB_FLUORESCENT, new UIInfo(whiteBalance[3],"", whiteBalanceIconID[3]));
		whiteBalanceMap.put(ICatchWhiteBalance.ICH_WB_TUNGSTEN, new UIInfo(whiteBalance[4],"", whiteBalanceIconID[4]));
	}

	public void initElectricityFrequencyMap() {
		String[] electricityFrequency = baseResource.getFreValueUIString();
		electricityFrequencyMap.put(ICatchLightFrequency.ICH_LIGHT_FREQUENCY_50HZ, new UIInfo(electricityFrequency[0],"", 0));
		electricityFrequencyMap.put(ICatchLightFrequency.ICH_LIGHT_FREQUENCY_60HZ, new UIInfo(electricityFrequency[1], "",0));
	}

	public void initDateStampMap() {
		String[] dateStamp = baseResource.getDateStampUIString();
		dateStampMap.put(ICatchDateStamp.ICH_DATE_STAMP_OFF, new UIInfo(dateStamp[0],"", 0));
		dateStampMap.put(ICatchDateStamp.ICH_DATE_STAMP_DATE, new UIInfo(dateStamp[1], "",0));
		dateStampMap.put(ICatchDateStamp.ICH_DATE_STAMP_DATE_TIME, new UIInfo(dateStamp[2],"", 0));
	}

	public void initDelayTimeMap() {
		String[] delayTime = baseResource.getDelayUIString();
		delayTimeMap.put(ICatchCaptureDelay.ICH_CAP_DELAY_NO, new UIInfo(delayTime[0],delayTime[0], 0));
		delayTimeMap.put(ICatchCaptureDelay.ICH_CAP_DELAY_2S, new UIInfo(delayTime[1], delayTime[1],0));
		delayTimeMap.put(5000, new UIInfo("5s", "5s",0));
		delayTimeMap.put(ICatchCaptureDelay.ICH_CAP_DELAY_10S, new UIInfo(delayTime[2], delayTime[2],0));
	}
	
	
	public void initImageSizeMap(){
		WriteLogToDevice.writeLog("[Normal] -- SDKReflectToUI: ", "begin initImageSizeMap");
		List<String> imageSizeList = cameraProperty.getSupportedImageSizes();
		List<Integer> convertImageSizeList = cameraProperty.getConvertSupportedImageSizes();
		String temp = "Undefined";
		String temp1 = "Undefined";
		for(int ii = 0 ;ii < imageSizeList.size(); ii++){
			if(convertImageSizeList.get(ii) == ICatchImageSize.ICH_IMAGE_SIZE_VGA){
				temp = "VGA"+"("+imageSizeList.get(ii)+")";
				imageSizeMap.put(imageSizeList.get(ii), new UIInfo(temp, "VGA",0));
			}else{
				temp = convertImageSizeList.get(ii)+"M"+"("+imageSizeList.get(ii)+")";
				temp1 =convertImageSizeList.get(ii)+"M";
				imageSizeMap.put(imageSizeList.get(ii), new UIInfo(temp, temp1,0));
			}
			WriteLogToDevice.writeLog("[Normal] -- SDKReflectToUI: ", "imageSize ="+temp);
		}
		WriteLogToDevice.writeLog("[Normal] -- SDKReflectToUI: ", "end initImageSizeMap imageSizeMap ="+imageSizeMap.size());
	}
	
	public void initVideoSizeMap(){
		WriteLogToDevice.writeLog("[Normal] -- SDKReflectToUI: ", "begin initVideoSizeMap");
		List<String> videoSizeList = cameraProperty.getSupportedVideoSizes();
		List<ICatchVideoSize> convertVideoSizeList = cameraProperty.getConvertSupportedVideoSizes();
		//videoSizeArray = new String[convertVideoSizeList.size()];
		for (int ii = 0; ii < convertVideoSizeList.size(); ii++) {
			if (convertVideoSizeList.get(ii) == ICatchVideoSize.ICH_VIDEO_SIZE_1080P_WITH_30FPS) {
				videoSizeMap.put(videoSizeList.get(ii), new UIInfo("1920x1080 30fps", "FHD30", 0));
				// cs[1] = "FHD";
			} else if (convertVideoSizeList.get(ii) == ICatchVideoSize.ICH_VIDEO_SIZE_1080P_WITH_60FPS) {
				videoSizeMap.put(videoSizeList.get(ii), new UIInfo("1920x1080 60fps", "FHD60", 0));
				// cs[1] = "FHD";
			} else if (convertVideoSizeList.get(ii) == ICatchVideoSize.ICH_VIDEO_SIZE_1440P_30FPS) {
				videoSizeMap.put(videoSizeList.get(ii), new UIInfo("1920x1440 30fps", "1440P", 0));
				// cs[1] = "FHD";
			} else if (convertVideoSizeList.get(ii) == ICatchVideoSize.ICH_VIDEO_SIZE_720P_120FPS) {
				videoSizeMap.put(videoSizeList.get(ii), new UIInfo("1280x720 120fps", "HD120", 0));
				// cs[1] = "HD";
			} else if (convertVideoSizeList.get(ii) == ICatchVideoSize.ICH_VIDEO_SIZE_720P_WITH_30FPS) {
				videoSizeMap.put(videoSizeList.get(ii), new UIInfo("1280x720 30fps", "HD30", 0));
				// cs[1] = "HD";
			} else if (convertVideoSizeList.get(ii) == ICatchVideoSize.ICH_VIDEO_SIZE_720P_WITH_60FPS) {
				videoSizeMap.put(videoSizeList.get(ii), new UIInfo("1280x720 60fps", "HD60", 0));
				// cs[1] = "HD";
			} else if (videoSizeList.get(ii).equals("1280x720 50")) {
				videoSizeMap.put(videoSizeList.get(ii), new UIInfo("1280x720 50fps", "HD50", 0));
				// cs[1] = "HD";
			} else if (videoSizeList.get(ii).equals("1280x720 25")) {
				videoSizeMap.put(videoSizeList.get(ii), new UIInfo("1280x720 25fps", "HD25", 0));
				// cs[1] = "HD";
			} else if (videoSizeList.get(ii).equals("1280x720 12")) {
				videoSizeMap.put(videoSizeList.get(ii), new UIInfo("1280x720 12fps", "HD12", 0));
			} else if (convertVideoSizeList.get(ii) == ICatchVideoSize.ICH_VIDEO_SIZE_960P_60FPS) {
				videoSizeMap.put(videoSizeList.get(ii), new UIInfo("1280x960 60fps", "960P", 0));
			} else if (videoSizeList.get(ii).equals("1280x960 120")) {
				videoSizeMap.put(videoSizeList.get(ii), new UIInfo("1280x960 120fps", "960P", 0));
			} else if (videoSizeList.get(ii).equals("1280x960 30")) {
				videoSizeMap.put(videoSizeList.get(ii), new UIInfo("1280x960 30fps", "960P", 0));
			} else if (convertVideoSizeList.get(ii) == ICatchVideoSize.ICH_VIDEO_SIZE_VGA_120FPS) {
				videoSizeMap.put(videoSizeList.get(ii), new UIInfo("640x480 120fps", "VGA120", 0));
				// cs[1] = "VGA";
			} else if (convertVideoSizeList.get(ii) == ICatchVideoSize.ICH_VIDEO_SIZE_640_360_240FPS) {
				videoSizeMap.put(videoSizeList.get(ii), new UIInfo("640x480 240fps", "VGA240", 0));
				// cs[1] = "VGA";
			} else if (videoSizeList.get(ii).equals("1920x1080 24")) {
				videoSizeMap.put(videoSizeList.get(ii), new UIInfo("1920x1080 24fps", "FHD24", 0));
				// cs[1] = "VGA";
			} else if (videoSizeList.get(ii).equals("1920x1080 50")) {
				videoSizeMap.put(videoSizeList.get(ii), new UIInfo("1920x1080 50fps", "FHD50", 0));
				// cs[1] = "VGA";
			} else if (videoSizeList.get(ii).equals("1920x1080 25")) {
				videoSizeMap.put(videoSizeList.get(ii), new UIInfo("1920x1080 25fps", "FHD25", 0));
				// cs[1] = "VGA";
			} else if (videoSizeList.get(ii).equals("2704x1524 15")) {
				videoSizeMap.put(videoSizeList.get(ii), new UIInfo("2704x1524 15fps", "2.7K", 0));
			} else if (videoSizeList.get(ii).equals("3840x2160 10")) {
				videoSizeMap.put(videoSizeList.get(ii), new UIInfo("3840x2160 10fps", "4K", 0));
			}	
		}
		WriteLogToDevice.writeLog("[Normal] -- SDKReflectToUI: ", "end initVideoSizeMap videoSizeList ="+videoSizeList.size());
	}
	public void initVideoMicMap() {
		String[] videomic = baseResource.getVideoMicUIString();
		for(int i=0;i<videomic.length;i++)
		{
			videoMicMap.put(i, new UIInfo(videomic[i], "", 0));
		}
	}
	public void initVideoStampMap() {
		String[] videostamp = baseResource.getVideoStampUIString();
		for(int i=0;i<videostamp.length;i++)
		{
			videoStampMap.put(i, new UIInfo(videostamp[i], "", 0));
		}
	}
	public void initLogoStampMap() {
		String[] logoStamp = baseResource.getLogoStampUIString();
		for(int i=0;i<logoStamp.length;i++)
		{
			logoStampMap.put(i, new UIInfo(logoStamp[i],"", 0));
		}
	}
	public void initCarNumStampMap() {
		String[] carnumStamp = baseResource.getCarNumStampUIString();
		for(int i=0;i<carnumStamp.length;i++)
		{
			carNumStampMap.put(i, new UIInfo(carnumStamp[i],"", 0));
		}
	}
	
	public UIInfo getReflectUIInfo(int settingID,int sdkValue){
		WriteLogToDevice.writeLog("[Normal] -- SDKReflectToUI: ", "begin getReflectUIInfo settingID ="+settingID);
		WriteLogToDevice.writeLog("[Normal] -- SDKReflectToUI: ", "sdkValue ="+sdkValue);
		UIInfo uiInfo = null;
		switch(settingID){
			case SETTING_UI_BURST:
				if(burstMap.containsKey(sdkValue)){
					uiInfo = burstMap.get(sdkValue);
				}
				break;
			case SETTING_UI_WHITE_BALANCE:
				if(whiteBalanceMap.containsKey(sdkValue)){
					uiInfo = whiteBalanceMap.get(sdkValue);
				}
				break;
			case SETTING_UI_ELETRICITY_FREQUENCY:
				if(electricityFrequencyMap.containsKey(sdkValue)){
					uiInfo = electricityFrequencyMap.get(sdkValue);
				}
				break;
			case SETTING_UI_DATE_STAMP:
				if(dateStampMap.containsKey(sdkValue)){
					uiInfo = dateStampMap.get(sdkValue);
				}
				break;
			case SETTING_UI_IMAGE_SIZE:
				if(imageSizeMap.containsKey(sdkValue)){
					uiInfo = imageSizeMap.get(sdkValue);
				}
				break;
			case SETTING_UI_VIDEO_SIZE:
				if(videoSizeMap.containsKey(sdkValue)){
					uiInfo = videoSizeMap.get(sdkValue);
				}
				break;
			case SETTING_UI_DELAY_TIME:
				if(delayTimeMap.containsKey(sdkValue)){
					uiInfo = delayTimeMap.get(sdkValue);
				}
				break;
			case SETTING_UI_TIME_LAPSE_INTERVAL:
				if(timeLapseDurationMap.containsKey(sdkValue)){
					uiInfo = timeLapseDurationMap.get(sdkValue);
				}
				break;
			case SETTING_UI_TIME_LAPSE_DURATION:
				if(timeLapseIntervalMap.containsKey(sdkValue)){
					uiInfo = timeLapseIntervalMap.get(sdkValue);
				}
				break;
			case SETTING_UI_SLOW_MOTION:
				if(slowMotionMap.containsKey(sdkValue)){
					uiInfo = slowMotionMap.get(sdkValue);
				}
				break;
			case SETTING_UI_UPSIDE:
				if(upsideMap.containsKey(sdkValue)){
					uiInfo = upsideMap.get(sdkValue);
				}
				break;
			case SETTING_UI_VIDEO_MIC:   																
				if(videoMicMap.containsKey(sdkValue)){
					uiInfo =videoMicMap.get(sdkValue);
				}
			case SETTING_UI_VIDEO_STAMP:   																
				if(videoStampMap.containsKey(sdkValue)){
					uiInfo =videoStampMap.get(sdkValue);
				}
				break;
			case SETTING_UI_LOGO_STAMP:   																
				if(logoStampMap.containsKey(sdkValue)){
					uiInfo =logoStampMap.get(sdkValue);
				}
				break;
			case SETTING_UI_CARNUM_STAMP:   																
				if(carNumStampMap.containsKey(sdkValue)){
					uiInfo =carNumStampMap.get(sdkValue);
				}
				break;
			
		}
		if(uiInfo == null){
			uiInfo = new UIInfo("Undefined","",0);
		}
		WriteLogToDevice.writeLog("[Normal] -- SDKReflectToUI: ", "end getReflectUIInfo uiInfo.uiStringInSetting ="+uiInfo.uiStringInSetting);
		WriteLogToDevice.writeLog("[Normal] -- SDKReflectToUI: ", "uiInfo.uiStringInPreview ="+uiInfo.uiStringInPreview);
		return uiInfo;
	}
	
	public UIInfo getReflectUIInfo(int settingID,String sdkValue){
		WriteLogToDevice.writeLog("[Normal] -- SDKReflectToUI: ", "begin getReflectUIInfo settingID ="+settingID);
		WriteLogToDevice.writeLog("[Normal] -- SDKReflectToUI: ", "sdkValue ="+sdkValue);
		UIInfo uiInfo = null;
		switch(settingID){
			case SETTING_UI_BURST:
				if(burstMap.containsKey(sdkValue)){
					uiInfo = burstMap.get(sdkValue);
				}
				break;
			case SETTING_UI_WHITE_BALANCE:
				if(whiteBalanceMap.containsKey(sdkValue)){
					uiInfo = whiteBalanceMap.get(sdkValue);
				}
				break;
			case SETTING_UI_ELETRICITY_FREQUENCY:
				//Log.d("tigertiger","SETTING_UI_ELETRICITY_FREQUENCY[==========sdkValue======"+sdkValue);
				if(electricityFrequencyMap.containsKey(sdkValue)){
					uiInfo = electricityFrequencyMap.get(sdkValue);
				}
				break;
			case SETTING_UI_DATE_STAMP:
				if(dateStampMap.containsKey(sdkValue)){
					uiInfo = dateStampMap.get(sdkValue);
				}
				break;
			case SETTING_UI_IMAGE_SIZE:
				if(imageSizeMap.containsKey(sdkValue)){
					uiInfo = imageSizeMap.get(sdkValue);
				}
				break;
			case SETTING_UI_VIDEO_SIZE:
				if(videoSizeMap.containsKey(sdkValue)){
					uiInfo = videoSizeMap.get(sdkValue);
				}
				break;
			case SETTING_UI_DELAY_TIME:
				if(delayTimeMap.containsKey(sdkValue)){
					uiInfo = delayTimeMap.get(sdkValue);
				}
				break;
			case SETTING_UI_TIME_LAPSE_INTERVAL:
				if(timeLapseDurationMap.containsKey(sdkValue)){
					uiInfo = timeLapseDurationMap.get(sdkValue);
				}
				break;
			case SETTING_UI_TIME_LAPSE_DURATION:
				if(timeLapseIntervalMap.containsKey(sdkValue)){
					uiInfo = timeLapseIntervalMap.get(sdkValue);
				}
				break;
			case SETTING_UI_SLOW_MOTION:
				if(slowMotionMap.containsKey(sdkValue)){
					uiInfo = slowMotionMap.get(sdkValue);
				}
				break;
			case SETTING_UI_UPSIDE:
				if(upsideMap.containsKey(sdkValue)){
					uiInfo = upsideMap.get(sdkValue);
				}
				break;
		
		}
		if(uiInfo == null){
			uiInfo = new UIInfo("Undefined","",0);
		}
		WriteLogToDevice.writeLog("[Normal] -- SDKReflectToUI: ", "end getReflectUIInfo uiInfo.uiStringInSetting ="+uiInfo.uiStringInSetting);
		WriteLogToDevice.writeLog("[Normal] -- SDKReflectToUI: ", "uiInfo.uiStringInPreview ="+uiInfo.uiStringInPreview);
		return uiInfo;
	}
}
