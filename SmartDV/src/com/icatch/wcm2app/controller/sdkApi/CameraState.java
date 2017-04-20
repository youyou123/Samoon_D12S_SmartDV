/**
 * Added by zhangyanhu C01012,2014-7-2
 */
package com.icatch.wcm2app.controller.sdkApi;

import com.icatch.wcm2app.common.WriteLogToDevice;
import com.icatch.wificam.customer.ICatchWificamState;
import com.icatch.wificam.customer.exception.IchInvalidSessionException;

/**
 * Added by zhangyanhu C01012,2014-7-2
 */
public class CameraState {
	private ICatchWificamState cameraState = SDKSession.getCameraStateClint();
	
	public boolean isMovieRecording(){
		WriteLogToDevice.writeLog("[Normal] -- CameraState: ", "begin isMovieRecording");
		boolean retValue = false;
		try {
			retValue = cameraState.isMovieRecording();
		} catch (IchInvalidSessionException e) {
			// TODO Auto-generated catch block
			WriteLogToDevice.writeLog("[Error] -- CameraState: ", "IchInvalidSessionException");
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- CameraState: ", "end isMovieRecording retValue="+retValue);
		return retValue;
	}
	
	public boolean isTimeLapseVideoOn(){
		WriteLogToDevice.writeLog("[Normal] -- CameraState: ", "begin isTimeLapseVideoOn");
		boolean retValue = false;
		try {
			retValue = cameraState.isTimeLapseVideoOn();
		} catch (IchInvalidSessionException e) {
			// TODO Auto-generated catch block
			WriteLogToDevice.writeLog("[Error] -- CameraState: ", "IchInvalidSessionException");
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- CameraState: ", "end isTimeLapseVideoOn retValue="+retValue);
		return retValue;
	}
	
	public boolean isTimeLapseStillOn(){
		WriteLogToDevice.writeLog("[Normal] -- CameraState: ", "begin isTimeLapseStillOn");
		boolean retValue = false;
		try {
			retValue = cameraState.isTimeLapseStillOn();
		} catch (IchInvalidSessionException e) {
			// TODO Auto-generated catch block
			WriteLogToDevice.writeLog("[Error] -- CameraState: ", "IchInvalidSessionException");
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- CameraState: ", "end isTimeLapseStillOn retValue="+retValue);
		return retValue;
	}
}
