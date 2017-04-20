/**
 * Added by zhangyanhu C01012,2014-6-27
 */
package com.icatch.wcm2app.controller.sdkApi;

import android.util.Log;

import com.icatch.wcm2app.common.WriteLogToDevice;
import com.icatch.wificam.customer.ICatchWificamControl;
import com.icatch.wificam.customer.ICatchWificamInfo;
import com.icatch.wificam.customer.ICatchWificamPlayback;
import com.icatch.wificam.customer.ICatchWificamPreview;
import com.icatch.wificam.customer.ICatchWificamProperty;
import com.icatch.wificam.customer.ICatchWificamSession;
import com.icatch.wificam.customer.ICatchWificamState;
import com.icatch.wificam.customer.ICatchWificamVideoPlayback;
import com.icatch.wificam.customer.exception.IchInvalidArgumentException;
import com.icatch.wificam.customer.exception.IchInvalidPasswdException;
import com.icatch.wificam.customer.exception.IchInvalidSessionException;
import com.icatch.wificam.customer.exception.IchPtpInitFailedException;
import com.icatch.wificam.customer.exception.IchSocketException;
import com.icatch.wificam.customer.exception.IchTutkInitFailedption;


/**
 * Added by zhangyanhu C01012,2014-6-27
 */
public class SDKSession {
	private static ICatchWificamSession session;
	private static ICatchWificamPlayback photoPlayback;
	private static ICatchWificamControl cameraAction;
	private static ICatchWificamVideoPlayback videoPlayback;
	private static ICatchWificamPreview previewStream;
	private static ICatchWificamInfo cameraInfo;
	private static ICatchWificamProperty cameraProperty;
	private static ICatchWificamState cameraState;
	private static boolean sessionPrepared = false;

	public static boolean prepareSession() {
		// TODO Auto-generated constructor stub
		WriteLogToDevice.writeLog("[Normal] -- SDKSession: ", "start prepareSession()");
		sessionPrepared = true;
		session = new ICatchWificamSession();
	
		
			try {
				if (session.prepareSession("192.168.1.1", "anonymous", "anonymous@icatchtek.com") == false) {//6.142
					WriteLogToDevice.writeLog("[Error] -- SDKSession: ", "failed to prepareSession");
					sessionPrepared = false;
				} else {
					try {
						photoPlayback = session.getPlaybackClient();
						cameraAction = session.getControlClient();
						previewStream = session.getPreviewClient();
						videoPlayback = session.getVideoPlaybackClient();
						cameraProperty = session.getPropertyClient();
						Log.e("tigertiger","cameraProperty =="+cameraProperty);
						cameraInfo = session.getInfoClient();
						cameraState = session.getStateClient();
					} catch (IchInvalidSessionException e) {
						WriteLogToDevice.writeLog("[Error] -- SDKSession: ", "IchInvalidSessionException");
						sessionPrepared = false;
						e.printStackTrace();
					}
				}
			} catch (IchTutkInitFailedption e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IchInvalidPasswdException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IchPtpInitFailedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
		WriteLogToDevice.writeLog("[Normal] -- SDKSession: ", "end prepareSession() sessionPrepared = "+sessionPrepared);
		return sessionPrepared;
	}
	
	public static boolean prepareSession(String ip) {
		// TODO Auto-generated constructor stub
		WriteLogToDevice.writeLog("[Normal] -- SDKSession: ", "start prepareSession()");
		sessionPrepared = true;
		session = new ICatchWificamSession();
		
		
			try {
				if (session.prepareSession(ip, "anonymous", "anonymous@icatchtek.com") == false) {
					WriteLogToDevice.writeLog("[Error] -- SDKSession: ", "failed to prepareSession");
					sessionPrepared = false;
				} else {
					try {
						photoPlayback = session.getPlaybackClient();
						cameraAction = session.getControlClient();
						previewStream = session.getPreviewClient();
						videoPlayback = session.getVideoPlaybackClient();
						cameraProperty = session.getPropertyClient();
						Log.e("tigertiger","cameraProperty =="+cameraProperty);
						cameraInfo = session.getInfoClient();
						cameraState = session.getStateClient();
					} catch (IchInvalidSessionException e) {
						WriteLogToDevice.writeLog("[Error] -- SDKSession: ", "IchInvalidSessionException");
						sessionPrepared = false;
						e.printStackTrace();
					}
				}
			} catch (IchTutkInitFailedption e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IchInvalidPasswdException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IchPtpInitFailedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
		WriteLogToDevice.writeLog("[Normal] -- SDKSession: ", "end prepareSession() sessionPrepared = "+sessionPrepared);
		return sessionPrepared;
	}

	public static boolean isSessionOK() {
		return sessionPrepared;
	}

	public static ICatchWificamSession getSDKSession() {
		return session;
	}

	public static ICatchWificamPlayback getplaybackClient() {
		return photoPlayback;
	}

	public static ICatchWificamControl getcameraActionClient() {
		return cameraAction;
	}

	public static ICatchWificamVideoPlayback getVideoPlaybackClint() {
		return videoPlayback;
	}

	public static ICatchWificamPreview getpreviewStreamClient() {
		return previewStream;
	}

	public static ICatchWificamInfo getCameraInfoClint() {
		return cameraInfo;
	}

	public static ICatchWificamProperty getCameraPropertyClint() {
		return cameraProperty;
	}

	public static ICatchWificamState getCameraStateClint() {
		return cameraState;
	}

	public static boolean checkWifiConnection() {
		WriteLogToDevice.writeLog("[Normal] -- SDKSession: ", "begin checkWifiConnection ");
		boolean retValue = false;
		try {
			retValue = session.checkConnection();
		} catch (IchInvalidSessionException e) {
			WriteLogToDevice.writeLog("[Error] -- SDKSession: ", "checkWifiConnection: IchInvalidSessionException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- SDKSession: ", "end checkWifiConnection retValue=" + retValue);
		return retValue;
	}

	public static boolean destroySession() {
		WriteLogToDevice.writeLog("[Normal] -- SDKSession: ", "begin destroySession");
		Boolean retValue = false;
		try {
			retValue = session.destroySession();
		} catch (IchInvalidSessionException e) {
			WriteLogToDevice.writeLog("[Error] -- SDKSession: ", "IchInvalidSessionException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- SDKSession: ", "end destroySession retValue =" + retValue);
		return retValue;
	}

	public static boolean wakeUpCamera(String macAddress) {
		WriteLogToDevice.writeLog("[Normal] -- SDKSession: ", "begin wakeUpCamera macAddress macAddress =" + macAddress);

		boolean retValue = false;
		try {
			retValue = session.wakeUpCamera(macAddress);
		} catch (IchSocketException e) {
			// TODO Auto-generated catch block
			WriteLogToDevice.writeLog("[Error] -- SDKSession: ", "IchSocketException");
			e.printStackTrace();
		} catch (IchInvalidArgumentException e) {
			// TODO Auto-generated catch block
			WriteLogToDevice.writeLog("[Error] -- SDKSession: ", "IchInvalidArgumentException");
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- SDKSession: ", "end wakeUpCamera retValue =" + retValue);
		return retValue;
	}
}
