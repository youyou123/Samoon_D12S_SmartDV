/**
 * Added by zhangyanhu C01012,2014-6-27
 */
package com.icatch.wcm2app.controller.sdkApi;

import java.util.LinkedList;

import android.util.Log;

import com.icatch.wcm2app.common.WriteLogToDevice;
import com.icatch.wificam.customer.ICatchWificamControl;
import com.icatch.wificam.customer.ICatchWificamListener;
import com.icatch.wificam.customer.exception.IchCameraModeException;
import com.icatch.wificam.customer.exception.IchCaptureImageException;
import com.icatch.wificam.customer.exception.IchDeviceException;
import com.icatch.wificam.customer.exception.IchInvalidSessionException;
import com.icatch.wificam.customer.exception.IchListenerExistsException;
import com.icatch.wificam.customer.exception.IchListenerNotExistsException;
import com.icatch.wificam.customer.exception.IchSocketException;
import com.icatch.wificam.customer.exception.IchStorageFormatException;
import com.icatch.wificam.customer.type.ICatchEventID;

/**
 * Added by zhangyanhu C01012,2014-6-27
 */
public class CameraAction {
	private ICatchWificamControl cameraAction = SDKSession.getcameraActionClient();
	private LinkedList cameraSupportModeList;
	public boolean capturePhoto() {
		WriteLogToDevice.writeLog("[Normal] -- CameraAction: ", "begin doStillCapture");
		boolean ret = false;
		try {
			ret = cameraAction.capturePhoto();
		} catch (IchSocketException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraAction: ", "IchSocketException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraAction: ", "IchCameraModeException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCaptureImageException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraAction: ", "IchCaptureImageException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraAction: ", "IchInvalidSessionException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- CameraAction: ", "end doStillCapture ret = " + ret);
		return ret;
	}
	
	public boolean triggerCapturePhoto() {
		WriteLogToDevice.writeLog("[Normal] -- CameraAction: ", "begin triggerCapturePhoto");
		boolean ret = false;
		try {
			Log.d("tigertiger", "before cameraAction.triggerCapturePhoto(); in CameraAction.java");
			ret = cameraAction.triggerCapturePhoto();
			Log.d("tigertiger", "Finish cameraAction.triggerCapturePhoto(); in CameraAction.java");
			
		} catch (IchSocketException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraAction: ", "IchSocketException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraAction: ", "IchCameraModeException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCaptureImageException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraAction: ", "IchCaptureImageException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraAction: ", "IchInvalidSessionException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- CameraAction: ", "end triggerCapturePhoto ret = " + ret);
		return ret;
	}
	public boolean startMovieRecord() {
		WriteLogToDevice.writeLog("[Normal] -- CameraAction: ", "begin startVideoCapture");
		boolean ret = false;

		try {
			ret = cameraAction.startMovieRecord();
		} catch (IchSocketException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraAction: ", "IchSocketException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraAction: ", "IchCameraModeException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraAction: ", "IchInvalidSessionException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- CameraAction: ", "end startVideoCapture ret =" + ret);
		return ret;
	}
	
	public boolean startTimeLapse() {
		WriteLogToDevice.writeLog("[Normal] -- CameraAction: ", "begin startTimeLapse");
		boolean ret = false;

		try {
			ret = cameraAction.startTimeLapse();
		} catch (IchSocketException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraAction: ", "IchSocketException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraAction: ", "IchCameraModeException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraAction: ", "IchInvalidSessionException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- CameraAction: ", "end startTimeLapse ret =" + ret);
		return ret;
	}
	
	public boolean stopTimeLapse() {
		WriteLogToDevice.writeLog("[Normal] -- CameraAction: ", "begin stopMovieRecordTimeLapse");
		boolean ret = false;

		try {
			ret = cameraAction.stopTimeLapse();
		} catch (IchSocketException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraAction: ", "IchSocketException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraAction: ", "IchCameraModeException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraAction: ", "IchInvalidSessionException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- CameraAction: ", "end stopMovieRecordTimeLapse ret =" + ret);
		return ret;
	}
	
	public boolean stopVideoCapture() {
		WriteLogToDevice.writeLog("[Normal] -- CameraAction: ", "begin stopVideoCapture");
		boolean ret = false;

		try {
			ret = cameraAction.stopMovieRecord();
		} catch (IchSocketException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraAction: ", "IchSocketException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraAction: ", "IchCameraModeException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraAction: ", "IchInvalidSessionException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- CameraAction: ", "end stopVideoCapture ret ="+ret);
		return ret;
	}
	
	public boolean formatStorage() {
		WriteLogToDevice.writeLog("[Normal] -- CameraAction: ", "begin formatSD");
		boolean retVal = false;

		try {
			retVal = cameraAction.formatStorage();
		} catch (IchSocketException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraAction: ", "IchSocketException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraAction: ", "IchCameraModeException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraAction: ", "IchInvalidSessionException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchStorageFormatException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraAction: ", "IchStorageFormatException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- CameraAction: ", "begin formatSD retVal ="+retVal);
		return retVal;
	}
	
	public boolean sleepCamera(){
		WriteLogToDevice.writeLog("[Normal] -- CameraAction: ", "begin sleepCamera");
		boolean retValue = false;
		try {
			try {
				retValue = cameraAction.toStandbyMode();
			} catch (IchDeviceException e) {
				// TODO Auto-generated catch block
				WriteLogToDevice.writeLog("[Error] -- CameraAction: ", "IchDeviceException");
				e.printStackTrace();
			} catch (IchInvalidSessionException e) {
				WriteLogToDevice.writeLog("[Error] -- CameraAction: ", "IchInvalidSessionException");
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IchSocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- CameraAction: ", "end sleepCamera retValue =" + retValue);
		return retValue;
	}
	
	public boolean addCustomEventListener(int eventID,ICatchWificamListener listener){
		WriteLogToDevice.writeLog("[Normal] -- CameraAction: ", "begin addEventListener eventID="+eventID);
		boolean retValue = false;
			try {
				retValue = cameraAction.addCustomEventListener(0x5001, listener);
			} catch (IchListenerExistsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IchInvalidSessionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		WriteLogToDevice.writeLog("[Normal] -- CameraAction: ", "end addEventListener retValue = "+retValue);
		return retValue;
	}
	
	public boolean delCustomEventListener(int eventID,ICatchWificamListener listener){
		WriteLogToDevice.writeLog("[Normal] -- CameraAction: ", "begin delEventListener eventID="+eventID);
		boolean retValue = false;
			try {
				retValue = cameraAction.delCustomEventListener(eventID, listener);
			} catch (IchListenerNotExistsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IchInvalidSessionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		WriteLogToDevice.writeLog("[Normal] -- CameraAction: ", "end delEventListener retValue = "+retValue);
		return retValue;
	}
	
	public boolean addEventListener(ICatchEventID eventID,ICatchWificamListener listener){
		WriteLogToDevice.writeLog("[Normal] -- CameraAction: ", "begin addEventListener eventID="+eventID);
		boolean retValue = false;
		try {
			retValue = cameraAction.addEventListener(eventID, listener);
		} catch (IchListenerExistsException e) {
			// TODO Auto-generated catch block
			WriteLogToDevice.writeLog("[Error] -- CameraAction: ", "IchListenerExistsException");
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- CameraAction: ", "end addEventListener retValue = "+retValue);
		return retValue;
	}
	
	public boolean delEventListener(ICatchEventID eventID,ICatchWificamListener listener){
		WriteLogToDevice.writeLog("[Normal] -- CameraAction: ", "begin delEventListener eventID="+eventID);
		boolean retValue = false;
		try {
			retValue = cameraAction.delEventListener(eventID, listener);
		} catch (IchListenerNotExistsException e) {
			// TODO Auto-generated catch block
			WriteLogToDevice.writeLog("[Error] -- CameraAction: ", "IchListenerExistsException");
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- CameraAction: ", "end delEventListener retValue = "+retValue);
		return retValue;
	}

	/**
	 * Added by zhangyanhu C01012,2014-7-2
	 */
	public String getCameraMacAddress() {
		// TODO Auto-generated method stub
		String macAddress = "";
		macAddress = cameraAction.getMacAddress();
		return macAddress;
	}
	
	public boolean zoomIn(){
		WriteLogToDevice.writeLog("[Normal] -- CameraAction: ", "begin zoomIn");
		boolean retValue = false;
		try {
			retValue = cameraAction.zoomIn();
		} catch (IchSocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchStorageFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- CameraAction: ", "end zoomIn retValue = "+retValue);
		return retValue;
	}
	
	public boolean zoomOut(){
		WriteLogToDevice.writeLog("[Normal] -- CameraAction: ", "begin zoomOut");
		boolean retValue = false;
		try {
			retValue = cameraAction.zoomOut();
		} catch (IchSocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchStorageFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- CameraAction: ", "end zoomOut retValue = "+retValue);
		return retValue;
	}
}
