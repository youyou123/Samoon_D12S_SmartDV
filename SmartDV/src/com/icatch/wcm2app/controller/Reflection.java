/**
 * Added by zhangyanhu C01012,2014-6-25
 */
package com.icatch.wcm2app.controller;

import com.icatch.wcm2app.Data.AppReflectToUI;
import com.icatch.wcm2app.Data.SDKReflectToUI;
import com.icatch.wcm2app.Data.UIInfo;
import com.icatch.wcm2app.Data.UIReflectToApp;
import com.icatch.wcm2app.Data.UIReflectToSDK;

/**
 * Added by zhangyanhu C01012,2014-6-25
 */
public class Reflection {
	private SDKReflectToUI sdkReflectToUI = SDKReflectToUI.getInstance();
	private UIReflectToSDK uiReflectToSDK = UIReflectToSDK.getInstance();
	private UIReflectToApp  uiReflectToAPP = UIReflectToApp.getInstance();
	private AppReflectToUI  appReflectToUI = AppReflectToUI.getInstance();

	public Object refecltFromUItoSDK(int settingID,String uiString){
		return uiReflectToSDK.getSDKValue(settingID, uiString);
	}
	
	public UIInfo refecltFromSDKToUI(int settingID,int sdkValue){
		return sdkReflectToUI.getReflectUIInfo(settingID, sdkValue);		
	}
	
	public UIInfo refecltFromSDKToUI(int settingID,String sdkValue){
		return sdkReflectToUI.getReflectUIInfo(settingID, sdkValue);		
	}
	
	public UIInfo refecltFromAppToUI(int settingID,int AppValue){
		return appReflectToUI.getReflectUIInfo(settingID, AppValue);		
	}
	public Integer refecltFromUIToApp(int settingID,String uiString){
		return uiReflectToAPP.getAppValue(settingID, uiString);		
	}
}
