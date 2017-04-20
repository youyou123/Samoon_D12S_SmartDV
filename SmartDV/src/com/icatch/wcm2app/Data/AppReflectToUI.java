/**
 * Added by zhangyanhu C01012,2014-8-28
 */
package com.icatch.wcm2app.Data;

import java.util.HashMap;

import com.icatch.wcm2app.common.WriteLogToDevice;
import com.icatch.wcm2app.globalValue.TimeLapseMode;

/**
 * Added by zhangyanhu C01012,2014-8-28
 */
public class AppReflectToUI {
	public static final int SETTING_UI_TIME_LAPSE_MODE = 1;;
	private static AppReflectToUI appReflectToUI;
	private HashMap<Integer, UIInfo> timeLapseModeMap = new HashMap<Integer, UIInfo>();
	private BaseResource baseResource = BaseResource.getInstance();
	
	public AppReflectToUI(){
		
	}
	
	public static AppReflectToUI getInstance(){
		if(appReflectToUI == null){
			appReflectToUI = new AppReflectToUI();
		}
		return appReflectToUI;
	}
	
	public void initAppReflectToUI(){
		initTimeLapseModeMap();
	}
	
	private void initTimeLapseModeMap(){
		String[] timeLapseMode = baseResource.getTimeLapseMode();
		timeLapseModeMap.put(TimeLapseMode.TIME_LAPSE_MODE_VIDEO,new UIInfo(timeLapseMode[0],"",0));
		timeLapseModeMap.put(TimeLapseMode.TIME_LAPSE_MODE_STILL,new UIInfo(timeLapseMode[1],"",0));
	}
	
	public UIInfo getReflectUIInfo(int settingID,int appValue){
		WriteLogToDevice.writeLog("[Normal] -- AppReflectToUI: ", "begin getReflectUIInfo settingID ="+settingID);
		WriteLogToDevice.writeLog("[Normal] -- AppReflectToUI: ", "uiValue ="+appValue);
		UIInfo uiInfo = null;
		switch(settingID){
			case SETTING_UI_TIME_LAPSE_MODE:
				if(timeLapseModeMap.containsKey(appValue)){
					uiInfo = timeLapseModeMap.get(appValue);
				}
				break;
		}
		if(uiInfo == null){
			uiInfo = new UIInfo("Undefined","",0);
		}
		WriteLogToDevice.writeLog("[Normal] -- AppReflectToUI: ", "end getReflectUIInfo uiInfo.uiStringInSetting ="+uiInfo.uiStringInSetting);
		WriteLogToDevice.writeLog("[Normal] -- AppReflectToUI: ", "uiInfo.uiStringInPreview ="+uiInfo.uiStringInPreview);
		return uiInfo;
	}
}
