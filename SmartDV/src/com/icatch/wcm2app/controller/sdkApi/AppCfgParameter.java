/**
 * Added by zhangyanhu C01012,2014-8-5
 */
package com.icatch.wcm2app.controller.sdkApi;

import com.icatch.wcm2app.common.WriteLogToDevice;
import com.icatch.wificam.customer.ICatchWificamConfig;

/**
 * Added by zhangyanhu C01012,2014-8-5
 */
public class AppCfgParameter {
	private ICatchWificamConfig configuration = ICatchWificamConfig.getInstance();
	public int getPreviewCacheTime() {
		WriteLogToDevice.writeLog("[Normal] -- AppCfgParameter: ", "begin getPreviewCacheTime");
		int retVal = 0;
		retVal = configuration.getPreviewCacheTime();
		WriteLogToDevice.writeLog("[Normal] -- AppCfgParameter: ", "end getPreviewCacheTime retVal =" + retVal);
		return retVal;
	}
	
	public void setPreviewCacheParam(int cacheTimeInMs) {
		WriteLogToDevice.writeLog("[Normal] -- AppCfgParameter: ", "begin setPreviewCacheParam cacheTimeInMs ="+cacheTimeInMs);
		configuration.setPreviewCacheParam(cacheTimeInMs, 200);
		WriteLogToDevice.writeLog("[Normal] -- AppCfgParameter: ", "end setPreviewCacheParam");
	}
	
	public void setConnectionCheckParam(int ptpTimeoutCheckCount,int rtpTimeoutInSecs) {
		WriteLogToDevice.writeLog("[Normal] -- AppCfgParameter: ", "begin setConnectionCheckParam " + 
				"ptpTimeoutCheckCount =" + ptpTimeoutCheckCount +  
				"rtpTimeoutInSecs =" + rtpTimeoutInSecs);
		configuration.setConnectionCheckParam(ptpTimeoutCheckCount, rtpTimeoutInSecs);
		WriteLogToDevice.writeLog("[Normal] -- AppCfgParameter: ", "end setConnectionCheckParam");
	}
	
}
