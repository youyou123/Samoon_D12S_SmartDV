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
public class UIReflectToApp {
	public static final int SETTING_APP_TIME_LAPSE_MODE = 1;
	private static UIReflectToApp uiReflectToApp;
	private HashMap<String,Integer> timeLapseModeMap = new HashMap<String,Integer>();
	private BaseResource baseResource = BaseResource.getInstance();
	
	public UIReflectToApp(){
		
	}
	
	public static UIReflectToApp getInstance(){
		if(uiReflectToApp == null){
			uiReflectToApp = new UIReflectToApp();
		}
		return uiReflectToApp;
	}
	
	public void initUIReflectToApp(){
		initTimeLapseModeMap();
	}
	
	private void initTimeLapseModeMap(){
		String[] timeLapseMode = baseResource.getTimeLapseMode();
		timeLapseModeMap.put(timeLapseMode[0],TimeLapseMode.TIME_LAPSE_MODE_VIDEO);
		timeLapseModeMap.put(timeLapseMode[1],TimeLapseMode.TIME_LAPSE_MODE_STILL);
	}
	
	public Integer getAppValue(int settingID,String uiString){
		WriteLogToDevice.writeLog("[Normal] -- UIReflectToApp: ", "begin getReflectUIInfo settingID ="+settingID);
		WriteLogToDevice.writeLog("[Normal] -- UIReflectToApp: ", "uiString ="+uiString);
		switch(settingID){
			case SETTING_APP_TIME_LAPSE_MODE:
				if(timeLapseModeMap.containsKey(uiString)){
					WriteLogToDevice.writeLog("[Normal] -- UIReflectToApp: ", "SDKValue ="+timeLapseModeMap.get(uiString));
					return timeLapseModeMap.get(uiString);
				}
				break;
		}
		WriteLogToDevice.writeLog("[Error] -- UIReflectToApp: ", "Don't find match sdk value!!");
		return 0;
	}
}
