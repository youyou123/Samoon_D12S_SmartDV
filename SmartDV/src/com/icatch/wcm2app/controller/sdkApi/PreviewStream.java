/**
 * Added by zhangyanhu C01012,2014-6-27
 */
package com.icatch.wcm2app.controller.sdkApi;

import android.util.Log;

import com.icatch.wcm2app.common.WriteLogToDevice;
import com.icatch.wificam.customer.ICatchWificamPreview;
import com.icatch.wificam.customer.exception.IchAudioStreamClosedException;
import com.icatch.wificam.customer.exception.IchBufferTooSmallException;
import com.icatch.wificam.customer.exception.IchCameraModeException;
import com.icatch.wificam.customer.exception.IchInvalidArgumentException;
import com.icatch.wificam.customer.exception.IchInvalidSessionException;
import com.icatch.wificam.customer.exception.IchSocketException;
import com.icatch.wificam.customer.exception.IchStreamNotRunningException;
import com.icatch.wificam.customer.exception.IchStreamNotSupportException;
//import com.icatch.wificam.customer.exception.IchStreamNotSupportException;
import com.icatch.wificam.customer.exception.IchTryAgainException;
import com.icatch.wificam.customer.exception.IchVideoStreamClosedException;
import com.icatch.wificam.customer.type.ICatchCustomerStreamParam;
import com.icatch.wificam.customer.type.ICatchFrameBuffer;
import com.icatch.wificam.customer.type.ICatchH264StreamParam;
import com.icatch.wificam.customer.type.ICatchMJPGStreamParam;
import com.icatch.wificam.customer.type.ICatchPreviewMode;

public class PreviewStream {
	private ICatchWificamPreview previewStreamControl;
	public PreviewStream(){
		previewStreamControl = SDKSession.getpreviewStreamClient();
	}
	public boolean stopMediaStream() {
		WriteLogToDevice.writeLog("[Normal] -- PreviewStream: ", "begin stopMediaStream");
		boolean retValue = false;
		try {
			retValue = previewStreamControl.stop();
		} catch (IchSocketException e) {
			WriteLogToDevice.writeLog("[Error] -- PreviewStream: ", "IchSocketException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			WriteLogToDevice.writeLog("[Error] -- PreviewStream: ", "IchCameraModeException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			WriteLogToDevice.writeLog("[Error] -- PreviewStream: ", "IchInvalidSessionException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.d("PreviewStream", "end stopMediaStream =" + retValue);
		return retValue;
	}

	public boolean supportAudio() {
		WriteLogToDevice.writeLog("[Normal] -- PreviewStream: ", "begin supportAudio");
		boolean retValue = false;
		try {
			retValue = previewStreamControl.containsAudioStream();
		} catch (IchSocketException e) {
			WriteLogToDevice.writeLog("[Error] -- PreviewStream: ", "IchSocketException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			WriteLogToDevice.writeLog("[Error] -- PreviewStream: ", "IchCameraModeException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			WriteLogToDevice.writeLog("[Error] -- PreviewStream: ", "IchInvalidSessionException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchStreamNotRunningException e) {
			WriteLogToDevice.writeLog("[Error] -- PreviewStream: ", "IchStreamNotRunningException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- PreviewStream: ", "end supportAudio retValue =" + retValue);
		return retValue;
	}

	/**
	 * Added by zhangyanhu C01012,2014-7-2
	 */
	public boolean getNextVideoFrame(ICatchFrameBuffer buffer) {
		//WriteLogToDevice.writeLog("[Normal] -- PreviewStream: ", "begin getNextVideoFrame");
		boolean retValue = false;
		//Log.d("tigertiger","previewStream = "+previewStream);
		try {
			retValue = previewStreamControl.getNextVideoFrame(buffer);
		} catch (IchSocketException e) {
			WriteLogToDevice.writeLog("[Error] -- PreviewStream: ", "IchSocketException");
			//need to close preview get next video frame
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchBufferTooSmallException e) {
			WriteLogToDevice.writeLog("[Error] -- PreviewStream: ", "IchBufferTooSmallException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			WriteLogToDevice.writeLog("[Error] -- PreviewStream: ", "IchCameraModeException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			WriteLogToDevice.writeLog("[Error] -- PreviewStream: ", "IchInvalidSessionException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchTryAgainException e) {
			WriteLogToDevice.writeLog("[Error] -- PreviewStream: ", "IchTryAgainException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchStreamNotRunningException e) {
			WriteLogToDevice.writeLog("[Error] -- PreviewStream: ", "IchStreamNotRunningException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidArgumentException e) {
			WriteLogToDevice.writeLog("[Error] -- PreviewStream: ", "IchInvalidArgumentException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchVideoStreamClosedException e) {
			WriteLogToDevice.writeLog("[Error] -- PreviewStream: ", "IchVideoStreamClosedException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//WriteLogToDevice.writeLog("[Normal] -- PreviewStream: ", "end getNextVideoFrame retValue =" + retValue);
		return retValue;
	}

	/**
	 * Added by zhangyanhu C01012,2014-7-2
	 */
	public boolean getNextAudioFrame(ICatchFrameBuffer icatchBuffer) {
		//WriteLogToDevice.writeLog("[Normal] -- PreviewStream: ", "begin getNextAudioFrame");
		boolean retValue = false;
		try {
			retValue = previewStreamControl.getNextAudioFrame(icatchBuffer);
		} catch (IchSocketException e) {
			WriteLogToDevice.writeLog("[Error] -- PreviewStream: ", "IchSocketException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchBufferTooSmallException e) {
			WriteLogToDevice.writeLog("[Error] -- PreviewStream: ", "IchBufferTooSmallException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			WriteLogToDevice.writeLog("[Error] -- PreviewStream: ", "IchCameraModeException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			WriteLogToDevice.writeLog("[Error] -- PreviewStream: ", "IchInvalidSessionException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchTryAgainException e) {
			WriteLogToDevice.writeLog("[Error] -- PreviewStream: ", "IchTryAgainException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchStreamNotRunningException e) {
			WriteLogToDevice.writeLog("[Error] -- PreviewStream: ", "IchStreamNotRunningException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidArgumentException e) {
			WriteLogToDevice.writeLog("[Error] -- PreviewStream: ", "IchInvalidArgumentException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchAudioStreamClosedException e) {
			WriteLogToDevice.writeLog("[Error] -- PreviewStream: ", "IchAudioStreamClosedException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//WriteLogToDevice.writeLog("[Normal] -- PreviewStream: ", "end getNextAudioFrame retValue =" + retValue);
		return retValue;
	}

	/**
	 * Added by zhangyanhu C01012,2014-7-2
	 */
	public boolean startMediaStream(ICatchMJPGStreamParam param, ICatchPreviewMode previewMode) {
		// TODO Auto-generated method stub
		WriteLogToDevice.writeLog("[Normal] -- PreviewStream: ", "begin startMediaStream");
		boolean retValue = false;
		try {
			retValue = previewStreamControl.start(param, previewMode);
		} catch (IchSocketException e) {
			WriteLogToDevice.writeLog("[Error] -- PreviewStream: ", "IchSocketException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			WriteLogToDevice.writeLog("[Error] -- PreviewStream: ", "IchCameraModeException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			WriteLogToDevice.writeLog("[Error] -- PreviewStream: ", "IchInvalidSessionException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidArgumentException e) {
			WriteLogToDevice.writeLog("[Error] -- PreviewStream: ", "IchInvalidArgumentException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchStreamNotSupportException e) {
			// TODO Auto-generated catch block
			WriteLogToDevice.writeLog("[Error] -- PreviewStream: ", "IchStreamNotSupportException");
			e.printStackTrace();	
		}
		WriteLogToDevice.writeLog("[Normal] -- PreviewStream: ", "end startMediaStream retValue =" + retValue);
		return retValue;
	}
	
	public boolean startMediaStream(ICatchMJPGStreamParam param, ICatchPreviewMode previewMode, boolean withoutAudio) {
		// TODO Auto-generated method stub
		WriteLogToDevice.writeLog("[Normal] -- PreviewStream: ", "begin startMediaStream");
		boolean retValue = false;
		try {
			retValue = previewStreamControl.start(param, previewMode,withoutAudio);
		} catch (IchSocketException e) {
			WriteLogToDevice.writeLog("[Error] -- PreviewStream: ", "IchSocketException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			WriteLogToDevice.writeLog("[Error] -- PreviewStream: ", "IchCameraModeException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			WriteLogToDevice.writeLog("[Error] -- PreviewStream: ", "IchInvalidSessionException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidArgumentException e) {
			WriteLogToDevice.writeLog("[Error] -- PreviewStream: ", "IchInvalidArgumentException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchStreamNotSupportException e) {
			// TODO Auto-generated catch block
			WriteLogToDevice.writeLog("[Error] -- PreviewStream: ", "IchStreamNotSupportException");
			e.printStackTrace();
	
		}
		WriteLogToDevice.writeLog("[Normal] -- PreviewStream: ", "end startMediaStream retValue =" + retValue);
		return retValue;
	}
	public boolean startMediaStream(ICatchCustomerStreamParam param, ICatchPreviewMode previewMode) {
		// TODO Auto-generated method stub
		WriteLogToDevice.writeLog("[Normal] -- PreviewStream: ", "begin startMediaStream");
		boolean retValue = false;
		try {
			retValue = previewStreamControl.start(param, previewMode);
		} catch (IchSocketException e) {
			WriteLogToDevice.writeLog("[Error] -- PreviewStream: ", "IchSocketException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			WriteLogToDevice.writeLog("[Error] -- PreviewStream: ", "IchCameraModeException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			WriteLogToDevice.writeLog("[Error] -- PreviewStream: ", "IchInvalidSessionException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidArgumentException e) {
			WriteLogToDevice.writeLog("[Error] -- PreviewStream: ", "IchInvalidArgumentException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchStreamNotSupportException e) {			
			// TODO Auto-generated catch block
			WriteLogToDevice.writeLog("[Error] -- PreviewStream: ", "IchStreamNotSupportException");			
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- PreviewStream: ", "end startMediaStream retValue =" + retValue);
		return retValue;
	}
	/**
	 * Added by zhangyanhu C01012,2014-7-2
	 */
	public boolean startMediaStream(ICatchH264StreamParam param, ICatchPreviewMode previewMode) {
		// TODO Auto-generated method stub
		WriteLogToDevice.writeLog("[Normal] -- PreviewStream: ", "begin startMediaStream");
		boolean retValue = false;
		try {
			retValue = previewStreamControl.start(param, previewMode);
		} catch (IchSocketException e) {
			WriteLogToDevice.writeLog("[Error] -- PreviewStream: ", "IchSocketException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			WriteLogToDevice.writeLog("[Error] -- PreviewStream: ", "IchCameraModeException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			WriteLogToDevice.writeLog("[Error] -- PreviewStream: ", "IchInvalidSessionException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidArgumentException e) {
			WriteLogToDevice.writeLog("[Error] -- PreviewStream: ", "IchInvalidArgumentException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchStreamNotSupportException e) {
			// TODO Auto-generated catch block
			WriteLogToDevice.writeLog("[Error] -- PreviewStream: ", "IchStreamNotSupportException");
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- PreviewStream: ", "end startMediaStream retValue =" + retValue);
		return retValue;
	}
	
	public  int getVideoWidth() {
		// TODO Auto-generated method stub
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "begin getVideoWidth");
		int retValue = 0;

		try {
			retValue = previewStreamControl.getVideoFormat().getVideoW();
		} catch (IchSocketException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchSocketException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchCameraModeException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchInvalidSessionException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchStreamNotRunningException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchStreamNotRunningException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "end getVideoWidth retValue =" + retValue);
		return retValue;
	}
	
	public  int getVideoHeigth() {
		// TODO Auto-generated method stub
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "begin getVideoHeigth");
		int retValue = 0;

		try {
			retValue = previewStreamControl.getVideoFormat().getVideoH();
		} catch (IchSocketException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchSocketException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchCameraModeException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchInvalidSessionException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchStreamNotRunningException e) {
			// TODO Auto-generated catch block
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchStreamNotRunningException");
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "end getVideoWidth retValue =" + retValue);
		return retValue;
	}
}
