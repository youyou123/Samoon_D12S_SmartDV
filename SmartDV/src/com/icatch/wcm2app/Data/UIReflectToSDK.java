/**
 * Added by zhangyanhu C01012,2014-6-23
 */
package com.icatch.wcm2app.Data;

import java.util.HashMap;
import java.util.List;

import android.util.Log;

import com.icatch.wcm2app.common.WriteLogToDevice;
import com.icatch.wcm2app.controller.sdkApi.CameraProperties;
import com.icatch.wcm2app.globalValue.SlowMotion;
import com.icatch.wcm2app.globalValue.TimeLapseDuration;
import com.icatch.wcm2app.globalValue.Upside;
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
public class UIReflectToSDK {

	private HashMap<String, Integer> burstMap = new HashMap<String, Integer>();
	private HashMap<String, Integer> whiteBalanceMap = new HashMap<String, Integer>();
	private HashMap<String, Integer> electricityFrequencyMap = new HashMap<String, Integer>();
	private HashMap<String, Integer> dateStampMap = new HashMap<String, Integer>();
	private HashMap<String, Integer> delayTimeMap = new HashMap<String, Integer>();
	private HashMap<String, String> imageSizeMap = new HashMap<String, String>();
	private HashMap<String, String> videoSizeMap = new HashMap<String, String>();
	private HashMap<String, Integer> timeLapseDurationMap = new HashMap<String, Integer>();
	private HashMap<String, Integer> timeLapseIntervalMap = new HashMap<String, Integer>();
	private HashMap<String, Integer> slowMotionMap = new HashMap<String, Integer>();
	private HashMap<String, Integer> upsideMap = new HashMap<String, Integer>();
	private HashMap<String, Integer> videoMicMap = new HashMap<String, Integer>();
	private HashMap<String, Integer> videoStampMap = new HashMap<String, Integer>();  
	private CameraProperties cameraProperty = new CameraProperties();

	private static UIReflectToSDK uiReflectToSDK = new UIReflectToSDK();
	private BaseResource baseResource = BaseResource.getInstance();
	
	
	public static final int SETTING_SDK_BURST = 0;
	public static final int SETTING_SDK_WHITE_BALANCE = 1;
	public static final int SETTING_SDK_ELETRICITY_FREQUENCY = 2;
	public static final int SETTING_SDK_DATE_STAMP = 3;
	public static final int SETTING_SDK_IMAGE_SIZE = 4;
	public static final int SETTING_SDK_VIDEO_SIZE = 5;
	public static final int SETTING_SDK_DELAY_TIME = 6;
	public static final int SETTING_SDK_CACHE_DURATION = 7;
	public static final int SETTING_SDK_TIME_LAPSE_INTERVAL = 8;
	public static final int SETTING_SDK_TIME_LAPSE_DURATION = 9;
	public static final int SETTING_SDK_SLOW_MOTION = 10;
	public static final int SETTING_SDK_UPSIDE = 11;
	public static final int SETTING_SDK_VIDEO_MIC = 12;   
	public static final int SETTING_SDK_VIDEO_STAMP = 13;  
	
	private static int INVALID_VALUE = -1;

	public UIReflectToSDK() {
	}

	public static UIReflectToSDK getInstance() {
		return uiReflectToSDK;
	}
	public static void createUIReflectToSDK(){
		uiReflectToSDK = new UIReflectToSDK();
	}
	public void initUIReflectToSDK(){
		initBurstMap();
		initWhiteBalanceMap();
		initElectricityFrequencyMap();
		initDateStampMap();
		initDelayTimeMap();
		initImageSizeMap();
		initVideoSizeMap();
		initTimeLapseDurationMap();
		initTimeLapseIntervalMap();
		initSlowMotion();
		initUpside();
		initVideoMicMap();
		initVideoStampMap();
	}
	public void initVideoStampMap() {
		String[] videostamp = baseResource.getVideoStampUIString();
		for(int i=0;i<videostamp.length;i++)
		{
			videoStampMap.put(videostamp[i],i);
		}

	}
	public void initVideoMicMap() {
		String[] videomic = baseResource.getVideoMicUIString();
		for(int i=0;i<videomic.length;i++)
		{
			videoMicMap.put(videomic[i], i);
		}
	}
	private void initSlowMotion() {
		// TODO Auto-generated method stub
		String[] slowMotion = baseResource.getSlowMotionUIString();
		slowMotionMap.put(slowMotion[0],SlowMotion.SLOW_MOTION_OFF);
		slowMotionMap.put(slowMotion[1],SlowMotion.SLOW_MOTION_ON);
	}
	
	private void initUpside() {
		// TODO Auto-generated method stub
		String[] upside = baseResource.getUpsideUIString();
		upsideMap.put(upside[0],Upside.UPSIDE_OFF);
		upsideMap.put(upside[1],Upside.UPSIDE_ON);
	}
	
	/**
	 * Added by zhangyanhu C01012,2014-8-21
	 */
	private void initTimeLapseDurationMap() {
		// TODO Auto-generated method stub
		String[] timeLapseDuration = baseResource.getTimeLapseDuration();
		timeLapseDurationMap.put(timeLapseDuration[0],TimeLapseDuration.TIME_LAPSE_DURATION_2MIN);
		timeLapseDurationMap.put(timeLapseDuration[1],TimeLapseDuration.TIME_LAPSE_DURATION_5MIN);
		timeLapseDurationMap.put(timeLapseDuration[2],TimeLapseDuration.TIME_LAPSE_DURATION_10MIN);
		timeLapseDurationMap.put(timeLapseDuration[3],TimeLapseDuration.TIME_LAPSE_DURATION_15MIN);
		timeLapseDurationMap.put(timeLapseDuration[4],TimeLapseDuration.TIME_LAPSE_DURATION_20MIN);
		timeLapseDurationMap.put(timeLapseDuration[5],TimeLapseDuration.TIME_LAPSE_DURATION_30MIN);
		timeLapseDurationMap.put(timeLapseDuration[6],TimeLapseDuration.TIME_LAPSE_DURATION_60MIN);
		timeLapseDurationMap.put(timeLapseDuration[7],TimeLapseDuration.TIME_LAPSE_DURATION_UNLIMITED);
	}
	
	/**
	 * Added by zhangyanhu C01012,2014-8-21
	 */
	private void initTimeLapseIntervalMap() {
		// TODO Auto-generated method stub
//		String[] videoTimeLapseInterval = baseResource.getTimeLapseInterval();
//		timeLapseIntervalMap.put(videoTimeLapseInterval[0],TimeLapseInterval.TIME_LAPSE_INTERVAL_OFF);
//		timeLapseIntervalMap.put(videoTimeLapseInterval[1],TimeLapseInterval.TIME_LAPSE_INTERVAL_2S);
//		timeLapseIntervalMap.put(videoTimeLapseInterval[2],TimeLapseInterval.TIME_LAPSE_INTERVAL_5S);
//		timeLapseIntervalMap.put(videoTimeLapseInterval[3],TimeLapseInterval.TIME_LAPSE_INTERVAL_10S);
//		timeLapseIntervalMap.put(videoTimeLapseInterval[4],TimeLapseInterval.TIME_LAPSE_INTERVAL_20S);
//		timeLapseIntervalMap.put(videoTimeLapseInterval[5],TimeLapseInterval.TIME_LAPSE_INTERVAL_30S);
//		timeLapseIntervalMap.put(videoTimeLapseInterval[6],TimeLapseInterval.TIME_LAPSE_INTERVAL_1MIN);
//		timeLapseIntervalMap.put(videoTimeLapseInterval[7],TimeLapseInterval.TIME_LAPSE_INTERVAL_5MIN);
//		timeLapseIntervalMap.put(videoTimeLapseInterval[8],TimeLapseInterval.TIME_LAPSE_INTERVAL_10MIN);
//		timeLapseIntervalMap.put(videoTimeLapseInterval[9],TimeLapseInterval.TIME_LAPSE_INTERVAL_30MIN);
//		timeLapseIntervalMap.put(videoTimeLapseInterval[10],TimeLapseInterval.TIME_LAPSE_INTERVAL_1HR);
	}
	
	public void initBurstMap() {
		String[] burstNum = baseResource.getBurstNumUIString();
		burstMap.put(burstNum[0],ICatchBurstNumber.ICH_BURST_NUMBER_OFF);
		burstMap.put(burstNum[1],ICatchBurstNumber.ICH_BURST_NUMBER_3);
		burstMap.put(burstNum[2],ICatchBurstNumber.ICH_BURST_NUMBER_5);
		burstMap.put(burstNum[3],ICatchBurstNumber.ICH_BURST_NUMBER_10);
		burstMap.put(burstNum[4],ICatchBurstNumber.ICH_BURST_NUMBER_HS);
	}

	public void initWhiteBalanceMap() {
		String[] whiteBalance = baseResource.getWhiteBalanceUIString();
		whiteBalanceMap.put(whiteBalance[0],ICatchWhiteBalance.ICH_WB_AUTO);
		whiteBalanceMap.put(whiteBalance[1],ICatchWhiteBalance.ICH_WB_DAYLIGHT);
		whiteBalanceMap.put(whiteBalance[2],ICatchWhiteBalance.ICH_WB_CLOUDY);
		whiteBalanceMap.put(whiteBalance[3],ICatchWhiteBalance.ICH_WB_FLUORESCENT);
		whiteBalanceMap.put(whiteBalance[4],ICatchWhiteBalance.ICH_WB_TUNGSTEN);
	}

	public void initElectricityFrequencyMap() {
		String[] electricityFrequency = baseResource.getFreValueUIString();
		electricityFrequencyMap.put(electricityFrequency[0],ICatchLightFrequency.ICH_LIGHT_FREQUENCY_50HZ);
		electricityFrequencyMap.put(electricityFrequency[1],ICatchLightFrequency.ICH_LIGHT_FREQUENCY_60HZ);
	}

	public void initDateStampMap() {
		String[] dateStamp = baseResource.getDateStampUIString();
		dateStampMap.put(dateStamp[0],ICatchDateStamp.ICH_DATE_STAMP_OFF);
		dateStampMap.put(dateStamp[1],ICatchDateStamp.ICH_DATE_STAMP_DATE);
		dateStampMap.put(dateStamp[2],ICatchDateStamp.ICH_DATE_STAMP_DATE_TIME);
	}
	
	public void initDelayTimeMap() {
		String[] delayTime = baseResource.getDelayUIString();
		delayTimeMap.put(delayTime[0],ICatchCaptureDelay.ICH_CAP_DELAY_NO);
		delayTimeMap.put(delayTime[1],ICatchCaptureDelay.ICH_CAP_DELAY_2S);
		delayTimeMap.put("5s",5000);
		delayTimeMap.put(delayTime[2],ICatchCaptureDelay.ICH_CAP_DELAY_10S);
	}
	
	
	public void initImageSizeMap(){
		WriteLogToDevice.writeLog("[Normal] -- UIReflectToSDK: ", "begin initImageSizeMap");
		List<String> imageSizeList = cameraProperty.getSupportedImageSizes();
		List<Integer> convertImageSizeList = cameraProperty.getConvertSupportedImageSizes();
		String temp = "Undefined";
		for(int ii = 0 ;ii < imageSizeList.size(); ii++){
			if(convertImageSizeList.get(ii) == ICatchImageSize.ICH_IMAGE_SIZE_VGA){
				temp = "VGA"+"("+imageSizeList.get(ii)+")";
				imageSizeMap.put(temp,imageSizeList.get(ii));
			}else{
				temp = convertImageSizeList.get(ii)+"M"+"("+imageSizeList.get(ii)+")";
				imageSizeMap.put(temp,imageSizeList.get(ii));
			}
			WriteLogToDevice.writeLog("[Normal] -- UIReflectToSDK: ", "imagesize = "+temp);
		}
		WriteLogToDevice.writeLog("[Normal] -- UIReflectToSDK: ", "end initImageSizeMap imageSizeMap ="+imageSizeMap.size());
	}
	public void initVideoSizeMap(){
		WriteLogToDevice.writeLog("[Normal] -- UIReflectToSDK: ", "begin initVideoSizeMap");
		List<String> videoSizeList = cameraProperty.getSupportedVideoSizes();
		List<ICatchVideoSize> convertVideoSizeList = cameraProperty.getConvertSupportedVideoSizes();
		for (int ii = 0; ii < convertVideoSizeList.size(); ii++) {
			Log.d("tigertiger", "videoSizeList.get(ii) = " + videoSizeList.get(ii));
			if (convertVideoSizeList.get(ii) == ICatchVideoSize.ICH_VIDEO_SIZE_1080P_WITH_30FPS) {
				videoSizeMap.put("1920x1080 30fps", videoSizeList.get(ii));
			} else if (convertVideoSizeList.get(ii) == ICatchVideoSize.ICH_VIDEO_SIZE_1080P_WITH_60FPS) {
				videoSizeMap.put("1920x1080 60fps", videoSizeList.get(ii));
			} else if (convertVideoSizeList.get(ii) == ICatchVideoSize.ICH_VIDEO_SIZE_1440P_30FPS) {
				videoSizeMap.put("1920x1440 30fps", videoSizeList.get(ii));
			} else if (convertVideoSizeList.get(ii) == ICatchVideoSize.ICH_VIDEO_SIZE_720P_120FPS) {
				videoSizeMap.put("1280x720 120fps", videoSizeList.get(ii));
			} else if (convertVideoSizeList.get(ii) == ICatchVideoSize.ICH_VIDEO_SIZE_720P_WITH_30FPS) {
				videoSizeMap.put("1280x720 30fps", videoSizeList.get(ii));
			} else if (convertVideoSizeList.get(ii) == ICatchVideoSize.ICH_VIDEO_SIZE_720P_WITH_60FPS) {
				videoSizeMap.put("1280x720 60fps", videoSizeList.get(ii));
			} else if (videoSizeList.get(ii).equals("1280x720 50")) {
				videoSizeMap.put("1280x720 50fps", videoSizeList.get(ii));
			} else if (videoSizeList.get(ii).equals("1280x720 25")) {
				videoSizeMap.put("1280x720 25fps", videoSizeList.get(ii));
			} else if (videoSizeList.get(ii).equals("1280x720 12")) {
				videoSizeMap.put("1280x720 12fps", videoSizeList.get(ii));
			} else if (convertVideoSizeList.get(ii) == ICatchVideoSize.ICH_VIDEO_SIZE_960P_60FPS) {
				videoSizeMap.put("1280x960 60fps", videoSizeList.get(ii));
			} else if (videoSizeList.get(ii).equals("1280x960 120")) {
				videoSizeMap.put("1280x960 120fps", videoSizeList.get(ii));
			} else if (videoSizeList.get(ii).equals("1280x960 30")) {
				videoSizeMap.put("1280x960 30fps", videoSizeList.get(ii));
			} else if (convertVideoSizeList.get(ii) == ICatchVideoSize.ICH_VIDEO_SIZE_VGA_120FPS) {
				videoSizeMap.put("640x480 120fps", videoSizeList.get(ii));
			} else if (convertVideoSizeList.get(ii) == ICatchVideoSize.ICH_VIDEO_SIZE_640_360_240FPS) {
				videoSizeMap.put("640x480 240fps", videoSizeList.get(ii));
			} else if (videoSizeList.get(ii).equals("1920x1080 24")) {
				videoSizeMap.put("1920x1080 24fps", videoSizeList.get(ii));
			}else if (videoSizeList.get(ii).equals("1920x1080 50")) {
				videoSizeMap.put("1920x1080 50fps", videoSizeList.get(ii));
			} else if (videoSizeList.get(ii).equals("1920x1080 25")) {
				videoSizeMap.put("1920x1080 25fps", videoSizeList.get(ii));
			} else if (videoSizeList.get(ii).equals("2704x1524 15")) {
				videoSizeMap.put("2704x1524 15fps", videoSizeList.get(ii));
			} else if (videoSizeList.get(ii).equals("3840x2160 10")) {
				videoSizeMap.put("3840x2160 10fps", videoSizeList.get(ii));
			} else {
				videoSizeMap.put("Undefined", videoSizeList.get(ii));
			}
		}	
		WriteLogToDevice.writeLog("[Normal] -- UIReflectToSDK: ", "end initVideoSizeMap ="+videoSizeMap.size());
	}
	
	
	public Object getSDKValue(int settingID,String uiString){
		WriteLogToDevice.writeLog("[Normal] -- UIReflectToSDK: ", "begin getReflectUIInfo settingID ="+settingID);
		WriteLogToDevice.writeLog("[Normal] -- UIReflectToSDK: ", "uiString ="+uiString);
		switch(settingID){
			case SETTING_SDK_BURST:
				if(burstMap.containsKey(uiString)){
					WriteLogToDevice.writeLog("[Normal] -- UIReflectToSDK: ", "SDKValue ="+burstMap.get(uiString));
					return burstMap.get(uiString);
				}
				break;
			case SETTING_SDK_WHITE_BALANCE:
				if(whiteBalanceMap.containsKey(uiString)){
					WriteLogToDevice.writeLog("[Normal] -- UIReflectToSDK: ", "SDKValue ="+burstMap.get(uiString));
					return whiteBalanceMap.get(uiString);
				}
				break;
			case SETTING_SDK_ELETRICITY_FREQUENCY:
				if(electricityFrequencyMap.containsKey(uiString)){
					WriteLogToDevice.writeLog("[Normal] -- UIReflectToSDK: ", "SDKValue ="+burstMap.get(uiString));
					return electricityFrequencyMap.get(uiString);
				}
				break;
			case SETTING_SDK_DATE_STAMP:
				if(dateStampMap.containsKey(uiString)){
					WriteLogToDevice.writeLog("[Normal] -- UIReflectToSDK: ", "SDKValue ="+burstMap.get(uiString));
					return dateStampMap.get(uiString);
				}
				break;
			case SETTING_SDK_IMAGE_SIZE:
				if(imageSizeMap.containsKey(uiString)){
					WriteLogToDevice.writeLog("[Normal] -- UIReflectToSDK: ", "SDKValue ="+burstMap.get(uiString));
					return imageSizeMap.get(uiString);
				}
				break;
			case SETTING_SDK_VIDEO_SIZE:
				if(videoSizeMap.containsKey(uiString)){
					WriteLogToDevice.writeLog("[Normal] -- UIReflectToSDK: ", "SDKValue ="+burstMap.get(uiString));
					return videoSizeMap.get(uiString);
				}
				break;
			case SETTING_SDK_DELAY_TIME:
				if(delayTimeMap.containsKey(uiString)){
					WriteLogToDevice.writeLog("[Normal] -- UIReflectToSDK: ", "SDKValue ="+burstMap.get(uiString));
					return delayTimeMap.get(uiString);
				}
				break;
			case SETTING_SDK_TIME_LAPSE_INTERVAL:
				if(timeLapseIntervalMap.containsKey(uiString)){
					WriteLogToDevice.writeLog("[Normal] -- UIReflectToSDK: ", "SDKValue ="+timeLapseIntervalMap.get(uiString));
					return timeLapseIntervalMap.get(uiString);
				}
				break;
			case SETTING_SDK_TIME_LAPSE_DURATION:
				if(timeLapseDurationMap.containsKey(uiString)){
					WriteLogToDevice.writeLog("[Normal] -- UIReflectToSDK: ", "SDKValue ="+timeLapseDurationMap.get(uiString));
					return timeLapseDurationMap.get(uiString);
				}
				break;
			case SETTING_SDK_SLOW_MOTION:
				if(slowMotionMap.containsKey(uiString)){
					WriteLogToDevice.writeLog("[Normal] -- UIReflectToSDK: ", "SDKValue ="+slowMotionMap.get(uiString));
					return slowMotionMap.get(uiString);
				}
				break;
			case SETTING_SDK_UPSIDE:
				if(upsideMap.containsKey(uiString)){
					WriteLogToDevice.writeLog("[Normal] -- UIReflectToSDK: ", "SDKValue ="+upsideMap.get(uiString));
					return upsideMap.get(uiString);
				}
				break;
			case SETTING_SDK_VIDEO_MIC:   																
				if(videoMicMap.containsKey(uiString)){
					return videoMicMap.get(uiString);
				}
			case SETTING_SDK_VIDEO_STAMP:   																
				if(videoStampMap.containsKey(uiString)){
					return videoStampMap.get(uiString);
				}
				break;
				
		}
		WriteLogToDevice.writeLog("[Error] -- UIReflectToSDK: ", "Don't find match sdk value!!");
		return INVALID_VALUE;
	}

}
