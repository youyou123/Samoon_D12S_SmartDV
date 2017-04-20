/**
 * Added by zhangyanhu C01012,2014-6-27
 */
package com.icatch.wcm2app.controller.sdkApi;

import com.icatch.wcm2app.common.WriteLogToDevice;
import com.icatch.wificam.customer.ICatchWificamInfo;
import com.icatch.wificam.customer.exception.IchInvalidSessionException;

/**
 * Added by zhangyanhu C01012,2014-6-27
 */
public class CameraFixedInfo {
	private ICatchWificamInfo cameraFixedInfo = SDKSession.getCameraInfoClint();
	/**
	 * Added by zhangyanhu C01012,2014-1-23
	 */
	public String getCameraName() {
		WriteLogToDevice.writeLog("[Normal] -- CameraFixedInfo: ", "begin getCameraName");
		String name = "";
		try {
			name = cameraFixedInfo.getCameraProductName();
		} catch (IchInvalidSessionException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraFixedInfo: ", "IchInvalidSessionException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- CameraFixedInfo: ", "end getCameraName name =" + name);
		return name;
	}

	/**
	 * Added by zhangyanhu C01012,2014-1-23
	 */
	public String getCameraVersion() {
		WriteLogToDevice.writeLog("[Normal] -- CameraFixedInfo: ", "begin getCameraVersion");
		String version = "";
		try {
			version = cameraFixedInfo.getCameraFWVersion();
		} catch (IchInvalidSessionException e) {
			// TODO Auto-generated catch block
			WriteLogToDevice.writeLog("[Error] -- CameraFixedInfo: ", "IchInvalidSessionException");
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- CameraFixedInfo: ", "end getCameraVersion version =" + version);
		return version;
	}

}
