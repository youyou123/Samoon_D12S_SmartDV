/**
 * Added by zhangyanhu C01012,2014-6-23
 */
package com.icatch.wcm2app.controller;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.icatch.wcm2app.Data.AppReflectToUI;
import com.icatch.wcm2app.Data.BaseResource;
import com.icatch.wcm2app.Data.SDKReflectToUI;
import com.icatch.wcm2app.Data.UIInfo;
import com.icatch.wcm2app.Data.UIReflectToApp;
import com.icatch.wcm2app.Data.UIReflectToSDK;
import com.icatch.wcm2app.common.AppProperties;
import com.icatch.wcm2app.common.WriteLogToDevice;
import com.icatch.wcm2app.controller.sdkApi.CameraFixedInfo;
import com.icatch.wcm2app.controller.sdkApi.CameraProperties;
import com.icatch.wcm2app.globalValue.TimeLapseInterval;
import com.icatch.wcm2app.globalValue.TimeLapseMode;
import com.icatch.wcm2app.R;
import com.icatch.wificam.customer.type.ICatchCameraProperty;
import com.icatch.wificam.customer.type.ICatchImageSize;
import com.icatch.wificam.customer.type.ICatchMode;
import com.icatch.wificam.customer.type.ICatchVideoSize;

/**
 * Added by zhangyanhu C01012,2014-6-23
 */
public class UIDisplayResource {
	private CameraProperties cameraProperty;
	private CameraFixedInfo cameraFixedInfo;

	private String[] imageSizeArray;
	private String[] videoSizeArray;
	private String[] whitebalanceArray;
	private String[] frequencyArray;
	private String[] delayTimeArray;
	private String[] dateStampArray;
	private String[] burstNumArray;
	private String[] timeLapseInterval;
	private String[] timeLapseDuration;
	private String[] timeLapseMode;
	private String[] videoMicArray;
	private String[] videoStampArray;
	private String[] logoStampArray;
	private String[] carnumStampArray;
	private ArrayList<Integer> settingNameCaptureMode;
	private ArrayList<String> settingValueCaptureMode;
	private ArrayList<Integer> settingNameVideoMode;
	private ArrayList<String> settingValueVideoMode;
	private ArrayList<Integer> settingNameTimeLapseMode;
	private ArrayList<String> settingValueTimeLapseMode;
	private SDKReflectToUI sdkReflectToUI;
	private static UIDisplayResource uiDisplayResource;
	private BaseResource baseResource;

	// public UIDisplayResource() {
	//
	// }

	public static UIDisplayResource getinstance() {
		if (uiDisplayResource == null) {
			uiDisplayResource = new UIDisplayResource();
		}
		return uiDisplayResource;
	}

	public static void createInstance() {
		uiDisplayResource = new UIDisplayResource();
	}

	public void initUIDisplayResource() {
		settingNameCaptureMode = new ArrayList<Integer>();
		settingValueCaptureMode = new ArrayList<String>();
		settingNameVideoMode = new ArrayList<Integer>();
		settingValueVideoMode = new ArrayList<String>();
		settingNameTimeLapseMode = new ArrayList<Integer>();
		settingValueTimeLapseMode = new ArrayList<String>();
		SDKReflectToUI.createSDKReflectToUI();
		sdkReflectToUI = SDKReflectToUI.getInstance();
		baseResource = BaseResource.getInstance();
		cameraProperty = new CameraProperties();
		cameraFixedInfo = new CameraFixedInfo();
		baseResource.initBaseResource();
		sdkReflectToUI.initSDKReflectToUI();
		UIReflectToSDK.createUIReflectToSDK();
		UIReflectToSDK.getInstance().initUIReflectToSDK();
		UIReflectToApp.getInstance().initUIReflectToApp();
		AppReflectToUI.getInstance().initAppReflectToUI();
		initSettingMenu();
		initImageSizeUIString();
		initVideoSizeUIString();
		initBurstNumUIString();
		initDateStampUIString();
		initDelayTimeUIString();
		initEleFreUIString();
		initWhiteBalanceUIString();
		// initTimeLapseInterval();
		TimeLapseInterval.createTimeLapseInterval();
		TimeLapseInterval.getInstance().initTimeLapseInterval();
		initTimeLapseDuration();
		initTimeLapseMode();
		initVideoMicUIString();
		initVideoStampUIString();
		initLogoStampUIString();
		initCarNumStampUIString();
	}

	public void initSettingMenu() {
		WriteLogToDevice.writeLog("[Normal] -- UIDisplayResource: ",
				"begin initSettingMenu");
		settingNameCaptureMode.clear();
		settingNameVideoMode.clear();

		if (cameraProperty
				.hasFuction(ICatchCameraProperty.ICH_CAP_BURST_NUMBER) == true) {
			settingNameCaptureMode.add(R.string.setting_burst);
		}
		if (cameraProperty
				.hasFuction(ICatchCameraProperty.ICH_CAP_WHITE_BALANCE)) {
			settingNameCaptureMode.add(R.string.setting_awb);
		}
		if (cameraProperty.hasFuction(cameraProperty.CmdVideoMic)) {
			settingNameVideoMode.add(R.string.setting_video_mic);
		}
		if (cameraProperty.hasFuction(cameraProperty.CmdVideoDateStamp)) {
			settingNameVideoMode.add(R.string.setting_video_stamp);
		}
		if (cameraProperty.hasFuction(cameraProperty.CmdCarNumStamp)) {
			settingNameVideoMode.add(R.string.setting_carnum_stamp);
		}
		if (cameraProperty.hasFuction(cameraProperty.CmdLogoStamp)) {
			settingNameVideoMode.add(R.string.setting_logo_stamp);
		}
		if (cameraProperty.hasFuction(cameraProperty.CmdCarNum)) {
			settingNameVideoMode.add(R.string.setting_car_number);
		}
		if (cameraProperty.hasFuction(cameraProperty.CmdStaSSID)
				|| cameraProperty.hasFuction(cameraProperty.CmdStaPwd)) {
			settingNameVideoMode.add(R.string.setting_sta_wifi_ssid_pwd);
		}

		if (cameraProperty
				.hasFuction(ICatchCameraProperty.ICH_CAP_LIGHT_FREQUENCY)) {
			settingNameCaptureMode.add(R.string.setting_power_supply);
		}
		if (cameraProperty.hasFuction(ICatchCameraProperty.ICH_CAP_DATE_STAMP) == true) {
			settingNameCaptureMode.add(R.string.setting_datestamp);
		}
		settingNameCaptureMode.add(R.string.setting_format);

		/*
		 * if (cameraProperty.hasFuction(0xd615)) {
		 * settingNameCaptureMode.add(R.string.slowmotion); }
		 */

		if (cameraProperty.hasFuction(0xd614)) {
			settingNameCaptureMode.add(R.string.upside);
		}
		if (cameraProperty.hasFuction(0xd83c)) {// camera password and wifi
			settingNameCaptureMode.add(R.string.camera_wifi_configuration);
		}
		settingNameCaptureMode.add(R.string.setting_app_version);
		settingNameCaptureMode.add(R.string.setting_product_name);
		if (cameraProperty.hasFuction(ICatchCameraProperty.ICH_CAP_FW_VERSION)) {
			settingNameCaptureMode.add(R.string.setting_firmware_version);
		}

		if (cameraProperty
				.hasFuction(ICatchCameraProperty.ICH_CAP_WHITE_BALANCE)) {
			settingNameVideoMode.add(R.string.setting_awb);
			settingNameTimeLapseMode.add(R.string.setting_awb);
		}
		if (cameraProperty
				.hasFuction(ICatchCameraProperty.ICH_CAP_LIGHT_FREQUENCY)) {
			settingNameVideoMode.add(R.string.setting_power_supply);
			settingNameTimeLapseMode.add(R.string.setting_power_supply);
		}
		if (cameraProperty.hasFuction(ICatchCameraProperty.ICH_CAP_DATE_STAMP) == true) {
			settingNameVideoMode.add(R.string.setting_datestamp);
		}
		settingNameVideoMode.add(R.string.setting_format);
		settingNameTimeLapseMode.add(R.string.setting_format);
		if (cameraProperty.cameraModeSupport(ICatchMode.ICH_MODE_TIMELAPSE)) {
			// settingNameVideoMode.add(R.string.setting_time_lapse_interval);
			// settingNameVideoMode.add(R.string.setting_time_lapse_duration);
			settingNameTimeLapseMode.add(R.string.timeLapse_mode);
			settingNameTimeLapseMode.add(R.string.setting_time_lapse_interval);
			settingNameTimeLapseMode.add(R.string.setting_time_lapse_duration);
		}

		if (cameraProperty.hasFuction(0xd615)) {
			settingNameVideoMode.add(R.string.slowmotion);
			// settingNameTimeLapseMode.add(R.string.slowmotion);
		}
		if (cameraProperty.hasFuction(0xd614)) {
			settingNameVideoMode.add(R.string.upside);
			settingNameTimeLapseMode.add(R.string.upside);
		}
		if (cameraProperty.hasFuction(0xd83c)) {// camera password and wifi
			settingNameTimeLapseMode.add(R.string.camera_wifi_configuration);
			settingNameVideoMode.add(R.string.camera_wifi_configuration);
		}

		settingNameTimeLapseMode.add(R.string.setting_app_version);
		settingNameVideoMode.add(R.string.setting_app_version);
		settingNameVideoMode.add(R.string.setting_product_name);
		settingNameTimeLapseMode.add(R.string.setting_product_name);
		if (cameraProperty.hasFuction(ICatchCameraProperty.ICH_CAP_FW_VERSION)) {
			settingNameVideoMode.add(R.string.setting_firmware_version);
			settingNameTimeLapseMode.add(R.string.setting_firmware_version);
		}
		// if (cameraProperty.hasFuction(cameraProperty.CmdVideoMic)){
		// settingNameVideoMode.add(R.string.setting_video_mic);
		// }
		WriteLogToDevice.writeLog("[Normal] -- UIDisplayResource: ",
				"end initSettingMenu settingNameCaptureMode ="
						+ settingNameCaptureMode.size());
		WriteLogToDevice.writeLog("[Normal] -- UIDisplayResource: ",
				"end initSettingMenu settingNameVideoMode ="
						+ settingNameVideoMode.size());
	}

	public void initImageSizeUIString() {
		WriteLogToDevice.writeLog("[Normal] -- UIDisplayResource: ",
				"begin initImageSizeUIString");
		if (cameraProperty.hasFuction(ICatchCameraProperty.ICH_CAP_IMAGE_SIZE) == false) {
			return;
		}
		List<String> imageSizeList = cameraProperty.getSupportedImageSizes();
		List<Integer> convertImageSizeList = cameraProperty
				.getConvertSupportedImageSizes();
		imageSizeArray = new String[imageSizeList.size()];
		String temp = "Undefined";
		for (int ii = 0; ii < imageSizeList.size(); ii++) {
			if (convertImageSizeList.get(ii) == ICatchImageSize.ICH_IMAGE_SIZE_VGA) {
				temp = "VGA" + "(" + imageSizeList.get(ii) + ")";
			} else {
				temp = convertImageSizeList.get(ii) + "M" + "("
						+ imageSizeList.get(ii) + ")";
			}
			imageSizeArray[ii] = temp;
		}
		WriteLogToDevice.writeLog("[Normal] -- UIDisplayResource: ",
				"end initImageSizeUIString imageSizeArray ="
						+ imageSizeArray.length);
	}

	public void initVideoSizeUIString() {
		WriteLogToDevice.writeLog("[Normal] -- UIDisplayResource: ",
				"begin initVideoSizeUIString");
		if (cameraProperty.hasFuction(ICatchCameraProperty.ICH_CAP_VIDEO_SIZE) == false) {
			return;
		}
		List<String> videoSizeList = cameraProperty.getSupportedVideoSizes();
		List<ICatchVideoSize> convertVideoSizeList = cameraProperty
				.getConvertSupportedVideoSizes();
		videoSizeArray = new String[convertVideoSizeList.size()];
		for (int ii = 0; ii < convertVideoSizeList.size(); ii++) {
			Log.e("tigertiger", "convertVideoSizeList.get(ii) ="
					+ convertVideoSizeList.get(ii));
			if (convertVideoSizeList.get(ii) == ICatchVideoSize.ICH_VIDEO_SIZE_1080P_WITH_30FPS) {
				videoSizeArray[ii] = "1920x1080 30fps";
				// cs[1] = "FHD";
			} else if (convertVideoSizeList.get(ii) == ICatchVideoSize.ICH_VIDEO_SIZE_1080P_WITH_60FPS) {
				videoSizeArray[ii] = "1920x1080 60fps";
				// cs[1] = "FHD";
			} else if (convertVideoSizeList.get(ii) == ICatchVideoSize.ICH_VIDEO_SIZE_1440P_30FPS) {
				videoSizeArray[ii] = "1920x1440 30fps";
				// cs[1] = "FHD";
			} else if (convertVideoSizeList.get(ii) == ICatchVideoSize.ICH_VIDEO_SIZE_720P_120FPS) {
				videoSizeArray[ii] = "1280x720 120fps";
				// cs[1] = "HD";
			} else if (convertVideoSizeList.get(ii) == ICatchVideoSize.ICH_VIDEO_SIZE_720P_WITH_30FPS) {
				videoSizeArray[ii] = "1280x720 30fps";
				// cs[1] = "HD";
			} else if (convertVideoSizeList.get(ii) == ICatchVideoSize.ICH_VIDEO_SIZE_720P_WITH_60FPS) {
				videoSizeArray[ii] = "1280x720 60fps";
				// cs[1] = "HD";
			} else if (videoSizeList.get(ii).equals("1280x720 50")) {
				videoSizeArray[ii] = "1280x720 50fps";
				// cs[1] = "HD";
			} else if (videoSizeList.get(ii).equals("1280x720 25")) {
				videoSizeArray[ii] = "1280x720 25fps";
				// cs[1] = "HD";
			} else if (videoSizeList.get(ii).equals("1280x720 12")) {
				videoSizeArray[ii] = "1280x720 12fps";
			} else if (convertVideoSizeList.get(ii) == ICatchVideoSize.ICH_VIDEO_SIZE_960P_60FPS) {
				videoSizeArray[ii] = "1280x960 60fps";
			} else if (videoSizeList.get(ii).equals("1280x960 120")) {
				videoSizeArray[ii] = "1280x960 120fps";
			} else if (videoSizeList.get(ii).equals("1280x960 30")) {
				videoSizeArray[ii] = "1280x960 30fps";
			} else if (convertVideoSizeList.get(ii) == ICatchVideoSize.ICH_VIDEO_SIZE_VGA_120FPS) {
				videoSizeArray[ii] = "640x480 120fps";
			} else if (convertVideoSizeList.get(ii) == ICatchVideoSize.ICH_VIDEO_SIZE_640_360_240FPS) {
				videoSizeArray[ii] = "640x480 240fps";
			} else if (videoSizeList.get(ii).equals("1920x1080 24")) {
				videoSizeArray[ii] = "1920x1080 24fps";
			} else if (videoSizeList.get(ii).equals("1920x1080 50")) {
				videoSizeArray[ii] = "1920x1080 50fps";
			} else if (videoSizeList.get(ii).equals("1920x1080 25")) {
				videoSizeArray[ii] = "1920x1080 25fps";
			} else if (videoSizeList.get(ii).equals("2704x1524 15")) {
				videoSizeArray[ii] = "2704x1524 15fps";
			} else if (videoSizeList.get(ii).equals("3840x2160 10")) {
				videoSizeArray[ii] = "3840x2160 10fps";
			} else {
				WriteLogToDevice.writeLog(
						"[Error] -- UIDisplayResource: ",
						"failed to convert size ="
								+ convertVideoSizeList.get(ii));
				videoSizeArray[ii] = "Undefined"; // cs[1] = "undefine";
			}
		}
		WriteLogToDevice.writeLog("[Normal] -- UIDisplayResource: ",
				"end initVideoSizeUIString videoSizeArray ="
						+ videoSizeArray.length);
	}

	public ArrayList<String> getSettingValueCaptureMode() {
		WriteLogToDevice.writeLog("[Normal] -- UIDisplayResource: ",
				"begin getSettingValueCaptureMode");
		settingValueCaptureMode.clear();
		UIInfo uiInfo = null;
		if (cameraProperty
				.hasFuction(ICatchCameraProperty.ICH_CAP_BURST_NUMBER) == true) {
			uiInfo = sdkReflectToUI.getReflectUIInfo(
					SDKReflectToUI.SETTING_UI_BURST,
					cameraProperty.getCurrentBurstNum());
			settingValueCaptureMode.add(uiInfo.uiStringInSetting);// burst
		}
		if (cameraProperty
				.hasFuction(ICatchCameraProperty.ICH_CAP_WHITE_BALANCE)) {
			uiInfo = sdkReflectToUI.getReflectUIInfo(
					SDKReflectToUI.SETTING_UI_WHITE_BALANCE,
					cameraProperty.getCurrentWhiteBalance());
			settingValueCaptureMode.add(uiInfo.uiStringInSetting); // white
																	// balance
		}
		if (cameraProperty
				.hasFuction(ICatchCameraProperty.ICH_CAP_LIGHT_FREQUENCY)) {
			uiInfo = sdkReflectToUI.getReflectUIInfo(
					SDKReflectToUI.SETTING_UI_ELETRICITY_FREQUENCY,
					cameraProperty.getCurrentLightFrequency());
			settingValueCaptureMode.add(uiInfo.uiStringInSetting);// hz
		}
		if (cameraProperty.hasFuction(ICatchCameraProperty.ICH_CAP_DATE_STAMP) == true) {
			uiInfo = sdkReflectToUI.getReflectUIInfo(
					SDKReflectToUI.SETTING_UI_DATE_STAMP,
					cameraProperty.getCurrentDateStamp());
			settingValueCaptureMode.add(uiInfo.uiStringInSetting);
		}

		settingValueCaptureMode.add("");
		/*
		 * if (cameraProperty.hasFuction(0xd615)) { uiInfo =
		 * sdkReflectToUI.getReflectUIInfo
		 * (SDKReflectToUI.SETTING_UI_SLOW_MOTION,
		 * cameraProperty.getCurrentSlowMotion());
		 * settingValueCaptureMode.add(uiInfo.uiStringInSetting); }
		 */
		if (cameraProperty.hasFuction(0xd614)) {
			uiInfo = sdkReflectToUI.getReflectUIInfo(
					SDKReflectToUI.SETTING_UI_UPSIDE,
					cameraProperty.getCurrentUpsideDown());
			settingValueCaptureMode.add(uiInfo.uiStringInSetting);
		}
		if (cameraProperty.hasFuction(0xd83c)) {// camera password
			settingValueCaptureMode.add("");
		}
		settingValueCaptureMode.add(baseResource.getAPPVersion());
		settingValueCaptureMode.add(cameraFixedInfo.getCameraName());
		if (cameraProperty.hasFuction(ICatchCameraProperty.ICH_CAP_FW_VERSION)) {
			settingValueCaptureMode.add(cameraFixedInfo.getCameraVersion());
		}
		WriteLogToDevice.writeLog("[Normal] -- UIDisplayResource: ",
				"end getSettingValueCaptureMode settingValueCaptureMode ="
						+ settingValueCaptureMode.size());
		return settingValueCaptureMode;
	}

	public ArrayList<String> getSettingValueVideoMode() {
		WriteLogToDevice.writeLog("[Normal] -- UIDisplayResource: ",
				"begin getSettingValueVideoMode");
		settingValueVideoMode.clear();
		UIInfo uiInfo = null;
		if (cameraProperty
				.hasFuction(ICatchCameraProperty.ICH_CAP_WHITE_BALANCE)) {
			uiInfo = sdkReflectToUI.getReflectUIInfo(
					SDKReflectToUI.SETTING_UI_WHITE_BALANCE,
					cameraProperty.getCurrentWhiteBalance());
			settingValueVideoMode.add(uiInfo.uiStringInSetting);
		}
		if (cameraProperty
				.hasFuction(ICatchCameraProperty.ICH_CAP_LIGHT_FREQUENCY)) {
			uiInfo = sdkReflectToUI.getReflectUIInfo(
					SDKReflectToUI.SETTING_UI_ELETRICITY_FREQUENCY,
					cameraProperty.getCurrentLightFrequency());
			settingValueVideoMode.add(uiInfo.uiStringInSetting);
		}
		if (cameraProperty.hasFuction(cameraProperty.CmdVideoMic)) {
			uiInfo = sdkReflectToUI.getReflectUIInfo(
					SDKReflectToUI.SETTING_UI_VIDEO_MIC,
					cameraProperty.getCurrentVideoMic());
			settingValueVideoMode.add(uiInfo.uiStringInSetting);
		}
		if (cameraProperty.hasFuction(cameraProperty.CmdVideoDateStamp)) {
			uiInfo = sdkReflectToUI.getReflectUIInfo(
					SDKReflectToUI.SETTING_UI_VIDEO_STAMP,
					cameraProperty.getCurrentVideoStamp());
			settingValueVideoMode.add(uiInfo.uiStringInSetting);
		}
		if (cameraProperty.hasFuction(cameraProperty.CmdCarNumStamp)) {
			uiInfo = sdkReflectToUI.getReflectUIInfo(
					SDKReflectToUI.SETTING_UI_CARNUM_STAMP,
					cameraProperty.getCurrentCarNumStamp());
			settingValueVideoMode.add(uiInfo.uiStringInSetting);
		}
		if (cameraProperty.hasFuction(cameraProperty.CmdLogoStamp)) {
			uiInfo = sdkReflectToUI.getReflectUIInfo(
					SDKReflectToUI.SETTING_UI_LOGO_STAMP,
					cameraProperty.getCurrentLogoStamp());
			settingValueVideoMode.add(uiInfo.uiStringInSetting);
		}
		if (cameraProperty.hasFuction(cameraProperty.CmdCarNum)) {
			Log.i("UIDisplayResource",
					"car num =" + cameraProperty.getCurrentCarNum());
			settingValueVideoMode.add(cameraProperty.getCurrentCarNum());
			// settingValueVideoMode.add("********");
		}
		if (cameraProperty.hasFuction(cameraProperty.CmdStaSSID)||cameraProperty.hasFuction(cameraProperty.CmdStaPwd)){			//xiao add for station mode wifi ssid & pwd		
			Log.i("UIDisplayResource", "sta ssid ="+cameraProperty.getCurrentStaWiFiSSID());
			settingValueVideoMode.add(cameraProperty.getCurrentStaWiFiSSID());
			//settingValueVideoMode.add(cameraProperty.getCurrentStaWiFiSSID()+"   "+"pwd:*********");
		}
		if (cameraProperty.hasFuction(ICatchCameraProperty.ICH_CAP_DATE_STAMP) == true) {
			uiInfo = sdkReflectToUI.getReflectUIInfo(
					SDKReflectToUI.SETTING_UI_DATE_STAMP,
					cameraProperty.getCurrentDateStamp());
			settingValueVideoMode.add(uiInfo.uiStringInSetting);
		}
		settingValueVideoMode.add("");
		if (cameraProperty.hasFuction(0xd615)) {
			uiInfo = sdkReflectToUI.getReflectUIInfo(
					SDKReflectToUI.SETTING_UI_SLOW_MOTION,
					cameraProperty.getCurrentSlowMotion());
			settingValueVideoMode.add(uiInfo.uiStringInSetting);
		}
		if (cameraProperty.hasFuction(0xd614)) {
			uiInfo = sdkReflectToUI.getReflectUIInfo(
					SDKReflectToUI.SETTING_UI_UPSIDE,
					cameraProperty.getCurrentUpsideDown());
			settingValueVideoMode.add(uiInfo.uiStringInSetting);
		}
		if (cameraProperty.hasFuction(0xd83c)) {
			settingValueVideoMode.add("");
		}
		settingValueVideoMode.add(baseResource.getAPPVersion());
		settingValueVideoMode.add(cameraFixedInfo.getCameraName());
		if (cameraProperty.hasFuction(ICatchCameraProperty.ICH_CAP_FW_VERSION)) {
			settingValueVideoMode.add(cameraFixedInfo.getCameraVersion());
		}

		WriteLogToDevice.writeLog("[Normal] -- UIDisplayResource: ",
				"end getSettingValueVideoMode settingValueVideoMode ="
						+ settingValueVideoMode.size());
		return settingValueVideoMode;
	}

	public ArrayList<Integer> getSettingNameVideoMode() {
		return settingNameVideoMode;
	}

	public ArrayList<String> getSettingValueTimeLapseMode() {
		WriteLogToDevice.writeLog("[Normal] -- UIDisplayResource: ",
				"begin getSettingValueTimeLapseMode");
		settingValueTimeLapseMode.clear();
		UIInfo uiInfo = null;
		if (cameraProperty
				.hasFuction(ICatchCameraProperty.ICH_CAP_WHITE_BALANCE)) {
			uiInfo = sdkReflectToUI.getReflectUIInfo(
					SDKReflectToUI.SETTING_UI_WHITE_BALANCE,
					cameraProperty.getCurrentWhiteBalance());
			settingValueTimeLapseMode.add(uiInfo.uiStringInSetting);
		}
		if (cameraProperty
				.hasFuction(ICatchCameraProperty.ICH_CAP_LIGHT_FREQUENCY)) {
			uiInfo = sdkReflectToUI.getReflectUIInfo(
					SDKReflectToUI.SETTING_UI_ELETRICITY_FREQUENCY,
					cameraProperty.getCurrentLightFrequency());
			settingValueTimeLapseMode.add(uiInfo.uiStringInSetting);
		}
		settingValueTimeLapseMode.add("");
		if (cameraProperty.cameraModeSupport(ICatchMode.ICH_MODE_TIMELAPSE)) {
			if (AppProperties.getInstanse().getTimeLapseMode() == TimeLapseMode.TIME_LAPSE_MODE_VIDEO) {
				settingValueTimeLapseMode
						.add(baseResource.getTimeLapseMode()[0]);
			} else if (AppProperties.getInstanse().getTimeLapseMode() == TimeLapseMode.TIME_LAPSE_MODE_STILL) {
				settingValueTimeLapseMode
						.add(baseResource.getTimeLapseMode()[1]);
			}
			// uiInfo =
			// sdkReflectToUI.getReflectUIInfo(SDKReflectToUI.SETTING_UI_TIME_LAPSE_INTERVAL,
			// cameraProperty.getCurrentTimeLapseInterval());
			settingValueTimeLapseMode.add(TimeLapseInterval.getInstance()
					.getCurrentValue());
			uiInfo = sdkReflectToUI.getReflectUIInfo(
					SDKReflectToUI.SETTING_UI_TIME_LAPSE_DURATION,
					cameraProperty.getCurrentTimeLapseDuration());
			settingValueTimeLapseMode.add(uiInfo.uiStringInSetting);
		}
		/*
		 * if (cameraProperty.hasFuction(0xd615)) { uiInfo =
		 * sdkReflectToUI.getReflectUIInfo
		 * (SDKReflectToUI.SETTING_UI_SLOW_MOTION,
		 * cameraProperty.getCurrentSlowMotion());
		 * settingValueTimeLapseMode.add(uiInfo.uiStringInSetting); }
		 */
		if (cameraProperty.hasFuction(0xd614)) {
			uiInfo = sdkReflectToUI.getReflectUIInfo(
					SDKReflectToUI.SETTING_UI_UPSIDE,
					cameraProperty.getCurrentUpsideDown());
			settingValueTimeLapseMode.add(uiInfo.uiStringInSetting);
		}
		if (cameraProperty.hasFuction(0xd83c)) {
			settingValueTimeLapseMode.add("");
		}
		settingValueTimeLapseMode.add(baseResource.getAPPVersion());
		settingValueTimeLapseMode.add(cameraFixedInfo.getCameraName());
		if (cameraProperty.hasFuction(ICatchCameraProperty.ICH_CAP_FW_VERSION)) {
			settingValueTimeLapseMode.add(cameraFixedInfo.getCameraVersion());
		}
		WriteLogToDevice.writeLog("[Normal] -- UIDisplayResource: ",
				"end getSettingValueVideoMode getSettingValueTimeLapseMode ="
						+ settingValueVideoMode.size());
		return settingValueTimeLapseMode;
	}

	public ArrayList<Integer> getsettingNameTimeLapseMode() {
		return settingNameTimeLapseMode;
	}

	public String[] getImageSize() {
		return imageSizeArray;
	}

	public String[] getVideoSize() {
		return videoSizeArray;
	}

	public void initTimeLapseInterval() {
		WriteLogToDevice.writeLog("[Normal] -- UIDisplayResource: ",
				"begin initTimeLapseInterval");
		if (cameraProperty.cameraModeSupport(ICatchMode.ICH_MODE_TIMELAPSE) == false) {
			return;
		}
		List<Integer> list = cameraProperty.getSupportedTimeLapseIntervals();
		int length = list.size();
		ArrayList<String> tempArrayList = new ArrayList<String>();
		String temp;

		for (int ii = 0; ii < length; ii++) {
			Log.d("tigertiger",
					"cameraProperty.getSupportedTimeLapseIntervals list.get(ii) ="
							+ list.get(ii));
			temp = sdkReflectToUI
					.getReflectUIInfo(
							SDKReflectToUI.SETTING_UI_TIME_LAPSE_INTERVAL,
							list.get(ii)).uiStringInSetting;
			if (temp.equals("Undefined")) {
				continue;
			}
			tempArrayList.add(temp);
		}

		timeLapseInterval = new String[tempArrayList.size()];
		for (int ii = 0; ii < tempArrayList.size(); ii++) {
			timeLapseInterval[ii] = tempArrayList.get(ii);
		}

		WriteLogToDevice.writeLog("[Normal] -- UIDisplayResource: ",
				"end initTimeLapseInterval timeLapseInterval ="
						+ timeLapseInterval.length);
	}

	public String[] getTimeLapseInterval() {
		return timeLapseInterval;
	}

	public void initTimeLapseMode() {
		timeLapseMode = baseResource.getTimeLapseMode();
	}

	public String[] getTimeLapseMode() {
		return timeLapseMode;
	}

	public void initTimeLapseDuration() {
		WriteLogToDevice.writeLog("[Normal] -- UIDisplayResource: ",
				"begin initTimeLapseDuration");
		if (cameraProperty.cameraModeSupport(ICatchMode.ICH_MODE_TIMELAPSE) == false) {
			return;
		}
		List<Integer> list = cameraProperty.getSupportedTimeLapseDurations();
		int length = list.size();
		ArrayList<String> tempArrayList = new ArrayList<String>();
		String temp;

		for (int ii = 0; ii < length; ii++) {
			Log.d("tigertiger",
					"cameraProperty.getSupportedTimeLapseDuration list.get(ii) ="
							+ list.get(ii));
			temp = sdkReflectToUI
					.getReflectUIInfo(
							SDKReflectToUI.SETTING_UI_TIME_LAPSE_DURATION,
							list.get(ii)).uiStringInSetting;
			if (temp.equals("Undefined")) {
				continue;
			}
			tempArrayList.add(temp);
		}

		timeLapseDuration = new String[tempArrayList.size()];
		for (int ii = 0; ii < tempArrayList.size(); ii++) {
			timeLapseDuration[ii] = tempArrayList.get(ii);
		}
		WriteLogToDevice.writeLog("[Normal] -- UIDisplayResource: ",
				"end initTimeLapseDuration timeLapseDuration ="
						+ timeLapseDuration.length);
	}

	public String[] getTimeLapseDuration() {
		return timeLapseDuration;
	}

	public String[] getSlowMotion() {
		return baseResource.getSlowMotionUIString();
	}

	public String[] getUpside() {
		return baseResource.getUpsideUIString();
	}

	public void initWhiteBalanceUIString() {
		WriteLogToDevice.writeLog("[Normal] -- UIDisplayResource: ",
				"begin initWhiteBalanceUIString");
		if (cameraProperty
				.hasFuction(ICatchCameraProperty.ICH_CAP_WHITE_BALANCE) == false) {
			return;
		}
		List<Integer> list = cameraProperty.getSupportedWhiteBalances();
		int length = list.size();

		ArrayList<String> tempArrayList = new ArrayList<String>();
		String temp;

		for (int ii = 0; ii < length; ii++) {
			Log.d("tigertiger",
					"cameraProperty.getSupportedWhiteBalance list.get(ii) ="
							+ list.get(ii));
			temp = sdkReflectToUI.getReflectUIInfo(
					SDKReflectToUI.SETTING_UI_WHITE_BALANCE, list.get(ii)).uiStringInSetting;
			if (temp.equals("Undefined")) {
				continue;
			}
			tempArrayList.add(temp);
		}

		whitebalanceArray = new String[tempArrayList.size()];
		for (int ii = 0; ii < tempArrayList.size(); ii++) {
			whitebalanceArray[ii] = tempArrayList.get(ii);
		}
		WriteLogToDevice.writeLog("[Normal] -- UIDisplayResource: ",
				"end initWhiteBalanceUIString whitebalanceArray ="
						+ whitebalanceArray.length);
	}

	public String[] getWhiteBalanceUIString() {
		return whitebalanceArray;
	}

	public void initEleFreUIString() {
		WriteLogToDevice.writeLog("[Normal] -- UIDisplayResource: ",
				"begin initEleFreUIString");
		if (cameraProperty
				.hasFuction(ICatchCameraProperty.ICH_CAP_LIGHT_FREQUENCY) == false) {
			return;
		}
		List<Integer> list = cameraProperty.getSupportedLightFrequencys();
		int length = list.size();

		ArrayList<String> tempArrayList = new ArrayList<String>();
		String temp;

		for (int ii = 0; ii < length; ii++) {
			Log.d("tigertiger", "cameraProperty.getSupportedFre list.get(ii) ="
					+ list.get(ii));
			temp = sdkReflectToUI.getReflectUIInfo(
					SDKReflectToUI.SETTING_UI_ELETRICITY_FREQUENCY,
					list.get(ii)).uiStringInSetting;
			if (temp.equals("Undefined")) {
				continue;
			}
			tempArrayList.add(temp);
		}

		frequencyArray = new String[tempArrayList.size()];
		for (int ii = 0; ii < tempArrayList.size(); ii++) {
			frequencyArray[ii] = tempArrayList.get(ii);
		}
		WriteLogToDevice.writeLog("[Normal] -- UIDisplayResource: ",
				"end initEleFreUIString frequencyArray ="
						+ frequencyArray.length);
	}

	public String[] getEleFreValueUIString() {
		return frequencyArray;
	}

	public void initDateStampUIString() {
		WriteLogToDevice.writeLog("[Normal] -- UIDisplayResource: ",
				"begin initDateStampUIString");
		if (cameraProperty.hasFuction(ICatchCameraProperty.ICH_CAP_DATE_STAMP) == false) {
			return;
		}
		List<Integer> list = cameraProperty.getsupportedDateStamps();
		int length = list.size();

		ArrayList<String> tempArrayList = new ArrayList<String>();
		String temp;

		for (int ii = 0; ii < length; ii++) {
			Log.d("tigertiger",
					"cameraProperty.getSupportedStamp list.get(ii) ="
							+ list.get(ii));
			temp = sdkReflectToUI.getReflectUIInfo(
					SDKReflectToUI.SETTING_UI_DATE_STAMP, list.get(ii)).uiStringInSetting;
			if (temp.equals("Undefined")) {
				continue;
			}
			tempArrayList.add(temp);
		}

		dateStampArray = new String[tempArrayList.size()];
		for (int ii = 0; ii < tempArrayList.size(); ii++) {
			dateStampArray[ii] = tempArrayList.get(ii);
		}
		WriteLogToDevice.writeLog("[Normal] -- UIDisplayResource: ",
				"end initDateStampUIString dateStampArray ="
						+ dateStampArray.length);
	}

	public String[] getDateStampUIString() {
		return dateStampArray;
	}

	public void initBurstNumUIString() {
		WriteLogToDevice.writeLog("[Normal] -- UIDisplayResource: ",
				"begin initBurstNumUIString");
		if (cameraProperty
				.hasFuction(ICatchCameraProperty.ICH_CAP_BURST_NUMBER) == false) {
			return;
		}
		List<Integer> list = cameraProperty.getsupportedBurstNums();
		int length = list.size();

		ArrayList<String> tempArrayList = new ArrayList<String>();
		String temp;

		for (int ii = 0; ii < length; ii++) {
			Log.d("tigertiger",
					"cameraProperty.getSupportedBurst list.get(ii) ="
							+ list.get(ii));
			temp = sdkReflectToUI.getReflectUIInfo(
					SDKReflectToUI.SETTING_UI_BURST, list.get(ii)).uiStringInSetting;
			if (temp.equals("Undefined")) {
				continue;
			}
			tempArrayList.add(temp);
		}

		burstNumArray = new String[tempArrayList.size()];
		for (int ii = 0; ii < tempArrayList.size(); ii++) {
			burstNumArray[ii] = tempArrayList.get(ii);
		}
		WriteLogToDevice.writeLog("[Normal] -- UIDisplayResource: ",
				"end initBurstNumUIString burstNumArray =" + burstNumArray);
	}

	public String[] getBurstNumUIString() {
		return burstNumArray;
	}

	public void initDelayTimeUIString() {
		WriteLogToDevice.writeLog("[Normal] -- UIDisplayResource: ",
				"begin initDelayTimeUIString");
		if (cameraProperty
				.hasFuction(ICatchCameraProperty.ICH_CAP_CAPTURE_DELAY) == false) {
			return;
		}
		List<Integer> list = cameraProperty.getSupportedCaptureDelays();
		int length = list.size();
		delayTimeArray = new String[length];
		for (int ii = 0; ii < length; ii++) {
			delayTimeArray[ii] = sdkReflectToUI.getReflectUIInfo(
					SDKReflectToUI.SETTING_UI_DELAY_TIME, list.get(ii)).uiStringInSetting;
		}

		ArrayList<String> tempArrayList = new ArrayList<String>();
		String temp;

		for (int ii = 0; ii < length; ii++) {
			Log.d("tigertiger",
					"cameraProperty.getSupportedDelayTime list.get(ii) ="
							+ list.get(ii));
			temp = sdkReflectToUI.getReflectUIInfo(
					SDKReflectToUI.SETTING_UI_DELAY_TIME, list.get(ii)).uiStringInSetting;
			if (temp.equals("Undefined")) {
				continue;
			}
			tempArrayList.add(temp);
		}

		delayTimeArray = new String[tempArrayList.size()];
		for (int ii = 0; ii < tempArrayList.size(); ii++) {
			delayTimeArray[ii] = tempArrayList.get(ii);
		}
		WriteLogToDevice.writeLog("[Normal] -- UIDisplayResource: ",
				"end initDelayTimeUIString delayTimeArray ="
						+ delayTimeArray.length);
	}

	public String[] getDelayTimeUIString() {
		return delayTimeArray;
	}

	public ArrayList<Integer> getSettingNameCaptureMode() {
		return settingNameCaptureMode;
	}

	public UIInfo getCurrentImageSize() {
		WriteLogToDevice.writeLog("[Normal] -- UIDisplayResource: ",
				"begin initDelayTimeUIString");
		String imageSize = cameraProperty.getCurrentImageSize();
		UIInfo uiInfo = sdkReflectToUI.getReflectUIInfo(
				SDKReflectToUI.SETTING_UI_IMAGE_SIZE, imageSize);
		WriteLogToDevice.writeLog("[Normal] -- UIDisplayResource: ",
				"end getCurrentImageSize");
		return uiInfo;
	}

	public UIInfo getCurrentVideoSize() {
		WriteLogToDevice.writeLog("[Normal] -- UIDisplayResource: ",
				"begin getCurrentVideoSize");
		String videoSize = cameraProperty.getCurrentVideoSize();
		UIInfo uiInfo = sdkReflectToUI.getReflectUIInfo(
				SDKReflectToUI.SETTING_UI_VIDEO_SIZE, videoSize);
		WriteLogToDevice.writeLog("[Normal] -- UIDisplayResource: ",
				"end getCurrentVideoSize");
		return uiInfo;
	}

	public UIInfo getCurrentWhiteBalance() {
		WriteLogToDevice.writeLog("[Normal] -- UIDisplayResource: ",
				"begin getCurrentWhiteBalance");
		int whiteBlance = cameraProperty.getCurrentWhiteBalance();
		UIInfo uiInfo = sdkReflectToUI.getReflectUIInfo(
				SDKReflectToUI.SETTING_UI_WHITE_BALANCE, whiteBlance);
		WriteLogToDevice.writeLog("[Normal] -- UIDisplayResource: ",
				"end getCurrentWhiteBalance");
		return uiInfo;
	}

	public UIInfo getCurrentBurstNum() {
		WriteLogToDevice.writeLog("[Normal] -- UIDisplayResource: ",
				"begin getCurrentBurstNum");
		int burstNum = cameraProperty.getCurrentBurstNum();

		UIInfo uiInfo = sdkReflectToUI.getReflectUIInfo(
				SDKReflectToUI.SETTING_UI_BURST, burstNum);
		WriteLogToDevice.writeLog("[Normal] -- UIDisplayResource: ",
				"end getCurrentBurstNum");
		return uiInfo;
	}

	public UIInfo getCurrentCaptureDelay() {
		WriteLogToDevice.writeLog("[Normal] -- UIDisplayResource: ",
				"begin getCurrentCaptureDelay");
		int captureDelay = cameraProperty.getCurrentCaptureDelay();
		UIInfo uiInfo = sdkReflectToUI.getReflectUIInfo(
				SDKReflectToUI.SETTING_UI_DELAY_TIME, captureDelay);
		WriteLogToDevice.writeLog("[Normal] -- UIDisplayResource: ",
				"end getCurrentCaptureDelay");
		return uiInfo;
	}

	public String getRemainImageNum() {
		WriteLogToDevice.writeLog("[Normal] -- UIDisplayResource: ",
				"begin getRemainImageNum");
		Integer num = 0;
		num = cameraProperty.getRemainImageNum();
		WriteLogToDevice.writeLog("[Normal] -- UIDisplayResource: ",
				"end getRemainImageNum num =" + num);
		return num.toString();
	}

	public String getRecordingRemainTime() {
		WriteLogToDevice.writeLog("[Normal] -- UIDisplayResource: ",
				"begin getRecordingRemainTime");
		int num = 0;
		num = cameraProperty.getRecordingRemainTime();
		WriteLogToDevice.writeLog("[Normal] -- UIDisplayResource: ",
				"end getRecordingRemainTime num =" + secondsToHours(num));
		return secondsToHours(num);
	}

	public void initVideoMicUIString() {
		if (cameraProperty.hasFuction(cameraProperty.CmdVideoMic) == false) {
			return;
		}
		List<Integer> list = cameraProperty.getsupportedVideoMic();
		int length = list.size();
		videoMicArray = new String[length];
		for (int ii = 0; ii < length; ii++) {
			// Log.d("initVideoMicUIString",
			// "initVideoMicUIString==========list.get(ii)=====" +
			// list.get(ii));
			videoMicArray[ii] = sdkReflectToUI.getReflectUIInfo(
					SDKReflectToUI.SETTING_UI_VIDEO_MIC, list.get(ii)).uiStringInSetting;
		}
	}

	public String[] getVideoMicUIString() {
		return videoMicArray;
	}

	/** video stamp menu */
	public void initVideoStampUIString() {
		if (cameraProperty.hasFuction(cameraProperty.CmdVideoDateStamp) == false) {
			return;
		}
		List<Integer> list = cameraProperty.getsupportedVideoStamp();
		int length = list.size();
		videoStampArray = new String[length];
		for (int ii = 0; ii < length; ii++) {
			// Log.d("initVideoStampUIString",
			// "initVideoStampUIString==========list.get(ii)=====" +
			// list.get(ii));
			videoStampArray[ii] = sdkReflectToUI.getReflectUIInfo(
					SDKReflectToUI.SETTING_UI_VIDEO_STAMP, list.get(ii)).uiStringInSetting;
		}
	}

	public String[] getVideoStampUIString() {
		return videoStampArray;
	}

	/** logo stamp menu */
	public void initLogoStampUIString() {
		if (cameraProperty.hasFuction(cameraProperty.CmdLogoStamp) == false) {
			return;
		}
		List<Integer> list = cameraProperty.getsupportedLogoStamp();
		int length = list.size();
		logoStampArray = new String[length];

		for (int ii = 0; ii < length; ii++) {
			// Log.d("tigertiger",
			// "initLogoStampUIString==========list.get(ii)=" + list.get(ii));
			logoStampArray[ii] = sdkReflectToUI.getReflectUIInfo(
					SDKReflectToUI.SETTING_UI_LOGO_STAMP, list.get(ii)).uiStringInSetting;
		}
	}

	public String[] getLogoStampUIString() {
		return logoStampArray;
	}

	/** carnum stamp menu */
	public void initCarNumStampUIString() {
		if (cameraProperty.hasFuction(cameraProperty.CmdCarNumStamp) == false) {
			return;
		}
		List<Integer> list = cameraProperty.getsupportedCarNumStamp();
		int length = list.size();
		carnumStampArray = new String[length];

		for (int ii = 0; ii < length; ii++) {
			// Log.d("tigertiger",
			// "initCarNumStampUIString==========list.get(ii)=" + list.get(ii));
			carnumStampArray[ii] = sdkReflectToUI.getReflectUIInfo(
					SDKReflectToUI.SETTING_UI_CARNUM_STAMP, list.get(ii)).uiStringInSetting;
		}
	}

	public String[] getCarNumStampUIString() {
		return carnumStampArray;
	}

	public static String secondsToHours(int remainTime) {
		String time = "";
		Integer h = remainTime / 3600;
		Integer m = (remainTime % 3600) / 60;
		Integer s = remainTime % 60;
		if (h < 10) {
			time = "0" + h.toString();
		} else {
			time = h.toString();
		}
		time = time + ":";
		if (m < 10) {
			time = time + "0" + m.toString();
		} else {
			time = time + m.toString();
		}
		time = time + ":";
		if (s < 10) {
			time = time + "0" + s.toString();
		} else {
			time = time + s.toString();
		}
		return time;
	}
}
