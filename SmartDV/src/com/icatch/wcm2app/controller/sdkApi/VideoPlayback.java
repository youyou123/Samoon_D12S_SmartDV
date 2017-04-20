/**
 * Added by zhangyanhu C01012,2014-6-27
 */
package com.icatch.wcm2app.controller.sdkApi;

import android.util.Log;

import com.icatch.wcm2app.common.WriteLogToDevice;
import com.icatch.wificam.customer.ICatchWificamVideoPlayback;
import com.icatch.wificam.customer.exception.IchAudioStreamClosedException;
import com.icatch.wificam.customer.exception.IchBufferTooSmallException;
import com.icatch.wificam.customer.exception.IchCameraModeException;
import com.icatch.wificam.customer.exception.IchInvalidArgumentException;
import com.icatch.wificam.customer.exception.IchInvalidSessionException;
import com.icatch.wificam.customer.exception.IchNoSuchFileException;
import com.icatch.wificam.customer.exception.IchPauseFailedException;
import com.icatch.wificam.customer.exception.IchPbStreamPausedException;
import com.icatch.wificam.customer.exception.IchResumeFailedException;
import com.icatch.wificam.customer.exception.IchSeekFailedException;
import com.icatch.wificam.customer.exception.IchSocketException;
import com.icatch.wificam.customer.exception.IchStreamNotRunningException;
import com.icatch.wificam.customer.exception.IchTryAgainException;
import com.icatch.wificam.customer.exception.IchVideoStreamClosedException;
import com.icatch.wificam.customer.type.ICatchFile;
import com.icatch.wificam.customer.type.ICatchFrameBuffer;

public class VideoPlayback {
	private ICatchWificamVideoPlayback videoPlayback = SDKSession.getVideoPlaybackClint();

	public boolean stopPlaybackStream() {
		WriteLogToDevice.writeLog("[Normal] -- VideoPlayback: ", "start stopPlaybackStream ");
		if (videoPlayback == null) {
			return true;
		}
		boolean retValue = false;
		try {
			retValue = videoPlayback.stop();
		} catch (IchSocketException e) {
			WriteLogToDevice.writeLog("[Error] -- VideoPlayback: ", "IchSocketException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			WriteLogToDevice.writeLog("[Error] -- VideoPlayback: ", "IchCameraModeException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			WriteLogToDevice.writeLog("[Error] -- VideoPlayback: ", "IchInvalidSessionException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- VideoPlayback: ", "stopPlaybackStream =" + retValue);
		return retValue;
	}
	

	public boolean startPlaybackStream(ICatchFile file) {
		boolean retValue = false;
		WriteLogToDevice.writeLog("[Normal] -- VideoPlayback: ", "begin startPlaybackStream");
		try {
			retValue = videoPlayback.play(file);
		} catch (IchSocketException e) {
			WriteLogToDevice.writeLog("[Error] -- VideoPlayback: ", "IchSocketException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			WriteLogToDevice.writeLog("[Error] -- VideoPlayback: ", "IchCameraModeException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			WriteLogToDevice.writeLog("[Error] -- VideoPlayback: ", "IchInvalidSessionException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchNoSuchFileException e) {
			WriteLogToDevice.writeLog("[Error] -- VideoPlayback: ", "IchNoSuchFileException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- VideoPlayback: ", "-----------end startPlaybackStream retValue =" + retValue);
		return retValue;
	}

	public boolean pausePlayback() {
		WriteLogToDevice.writeLog("[Normal] -- VideoPlayback: ", "begin pausePlayback");
		boolean retValue = false;
		try {
			retValue = videoPlayback.pause();
		} catch (IchSocketException e) {
			WriteLogToDevice.writeLog("[Error] -- VideoPlayback: ", "IchSocketException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			WriteLogToDevice.writeLog("[Error] -- VideoPlayback: ", "IchCameraModeException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			WriteLogToDevice.writeLog("[Error] -- VideoPlayback: ", "IchInvalidSessionException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchPauseFailedException e) {
			WriteLogToDevice.writeLog("[Error] -- VideoPlayback: ", "IchPauseFailedException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchStreamNotRunningException e) {
			WriteLogToDevice.writeLog("[Error] -- VideoPlayback: ", "IchStreamNotRunningException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- VideoPlayback: ", "end pausePlayback =" + retValue);
		return retValue;
	}

	/**
	 * 
	 * Added by zhangyanhu C01012,2014-3-20
	 */
	public boolean resumePlayback() {
		WriteLogToDevice.writeLog("[Normal] -- VideoPlayback: ", "begin resumePlayback");
		boolean retValue = false;
		try {
			retValue = videoPlayback.resume();
		} catch (IchSocketException e) {
			WriteLogToDevice.writeLog("[Error] -- VideoPlayback: ", "IchSocketException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			WriteLogToDevice.writeLog("[Error] -- VideoPlayback: ", "IchCameraModeException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			WriteLogToDevice.writeLog("[Error] -- VideoPlayback: ", "IchInvalidSessionException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchResumeFailedException e) {
			WriteLogToDevice.writeLog("[Error] -- VideoPlayback: ", "IchResumeFailedException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchStreamNotRunningException e) {
			WriteLogToDevice.writeLog("[Error] -- VideoPlayback: ", "IchStreamNotRunningException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.d("VideoPlayback", "end resumePlayback retValue=" + retValue);
		return retValue;
	}

	public int getVideoDuration() {
		WriteLogToDevice.writeLog("[Normal] -- VideoPlayback: ", "begin getVideoDuration");
		double temp = 0;
		try {
			temp = videoPlayback.getLength();
		} catch (IchSocketException e) {
			WriteLogToDevice.writeLog("[Error] -- VideoPlayback: ", "IchSocketException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			WriteLogToDevice.writeLog("[Error] -- VideoPlayback: ", "IchCameraModeException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			WriteLogToDevice.writeLog("[Error] -- VideoPlayback: ", "IchInvalidSessionException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchStreamNotRunningException e) {
			// TODO Auto-generated catch block
			WriteLogToDevice.writeLog("[Error] -- VideoPlayback: ", "IchStreamNotRunningException");
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- VideoPlayback: ", "end getVideoDuration length =" + new Double(temp * 100).intValue());
		return (new Double(temp * 100).intValue());
	}

	public boolean videoSeek(double position) {
		WriteLogToDevice.writeLog("[Normal] -- VideoPlayback: ", "begin videoSeek position = " + position);
		boolean retValue = false;
		try {
			retValue = videoPlayback.seek(position);
		} catch (IchSocketException e) {
			WriteLogToDevice.writeLog("[Error] -- VideoPlayback: ", "IchSocketException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			WriteLogToDevice.writeLog("[Error] -- VideoPlayback: ", "IchCameraModeException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			WriteLogToDevice.writeLog("[Error] -- VideoPlayback: ", "IchInvalidSessionException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchSeekFailedException e) {
			WriteLogToDevice.writeLog("[Error] -- VideoPlayback: ", "IchSeekFailedException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchStreamNotRunningException e) {
			WriteLogToDevice.writeLog("[Error] -- VideoPlayback: ", "IchStreamNotRunningException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- VideoPlayback: ", "end videoSeek retValue=" + retValue);
		return retValue;
	}

	/**
	 * Added by zhangyanhu C01012,2014-7-2
	 */
	public boolean getNextVideoFrame(ICatchFrameBuffer buffer) {
		WriteLogToDevice.writeLog("[Normal] -- VideoPlayback: ", "begin getNextVideoFrame");
		boolean retValue = false;
		try {
			retValue = videoPlayback.getNextVideoFrame(buffer);
		} catch (IchSocketException e) {
			WriteLogToDevice.writeLog("[Error] -- VideoPlayback: ", "IchSocketException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			WriteLogToDevice.writeLog("[Error] -- VideoPlayback: ", "IchCameraModeException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			WriteLogToDevice.writeLog("[Error] -- VideoPlayback: ", "IchInvalidSessionException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchStreamNotRunningException e) {
			WriteLogToDevice.writeLog("[Error] -- VideoPlayback: ", "IchStreamNotRunningException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchBufferTooSmallException e) {
			// TODO Auto-generated catch block
			WriteLogToDevice.writeLog("[Error] -- VideoPlayback: ", "IchBufferTooSmallException");
			e.printStackTrace();
		} catch (IchTryAgainException e) {
			// TODO Auto-generated catch block
			WriteLogToDevice.writeLog("[Error] -- VideoPlayback: ", "IchTryAgainException");
			e.printStackTrace();
		} catch (IchInvalidArgumentException e) {
			// TODO Auto-generated catch block
			WriteLogToDevice.writeLog("[Error] -- VideoPlayback: ", "IchInvalidArgumentException");
			e.printStackTrace();
		} catch (IchVideoStreamClosedException e) {
			// TODO Auto-generated catch block
			WriteLogToDevice.writeLog("[Error] -- VideoPlayback: ", "IchVideoStreamClosedException");
			e.printStackTrace();
		} catch (IchPbStreamPausedException e) {
			// TODO Auto-generated catch block
			WriteLogToDevice.writeLog("[Error] -- VideoPlayback: ", "IchPbStreamPausedException");
				e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- VideoPlayback: ", "end getNextVideoFrame  retValue= " + retValue);
		return retValue;
	}
	
	public boolean getNextAudioFrame(ICatchFrameBuffer buffer) {
		WriteLogToDevice.writeLog("[Normal] -- VideoPlayback: ", "begin getNextAudioFrame");
		boolean retValue = false;
		try {
			retValue = videoPlayback.getNextAudioFrame(buffer);
		} catch (IchSocketException e) {
			WriteLogToDevice.writeLog("[Error] -- VideoPlayback: ", "IchSocketException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			WriteLogToDevice.writeLog("[Error] -- VideoPlayback: ", "IchCameraModeException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			WriteLogToDevice.writeLog("[Error] -- VideoPlayback: ", "IchInvalidSessionException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchStreamNotRunningException e) {
			WriteLogToDevice.writeLog("[Error] -- VideoPlayback: ", "IchStreamNotRunningException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchBufferTooSmallException e) {
			// TODO Auto-generated catch block
			WriteLogToDevice.writeLog("[Error] -- VideoPlayback: ", "IchBufferTooSmallException");
			e.printStackTrace();
		} catch (IchTryAgainException e) {
			// TODO Auto-generated catch block
			WriteLogToDevice.writeLog("[Error] -- VideoPlayback: ", "IchTryAgainException");
			e.printStackTrace();
		} catch (IchInvalidArgumentException e) {
			// TODO Auto-generated catch block
			WriteLogToDevice.writeLog("[Error] -- VideoPlayback: ", "IchInvalidArgumentException");
			e.printStackTrace();
		} catch (IchAudioStreamClosedException e) {
			// TODO Auto-generated catch block
			WriteLogToDevice.writeLog("[Error] -- VideoPlayback: ", "IchAudioStreamClosedException");
				e.printStackTrace();
		} catch (IchPbStreamPausedException e) {
			// TODO Auto-generated catch block
			WriteLogToDevice.writeLog("[Error] -- VideoPlayback: ", "IchPbStreamPausedException");
				e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- VideoPlayback: ", "end getNextAudioFrame  retValue= " + retValue);
		return retValue;
	}
	
	public boolean containsAudioStream() {
		WriteLogToDevice.writeLog("[Normal] -- VideoPlayback: ", "begin containsAudioStream");
		boolean retValue = false;
			try {
				retValue = videoPlayback.containsAudioStream();
			} catch (IchSocketException e) {
				WriteLogToDevice.writeLog("[Error] -- VideoPlayback: ", "IchSocketException");
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IchCameraModeException e) {
				WriteLogToDevice.writeLog("[Error] -- VideoPlayback: ", "IchCameraModeException");
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IchInvalidSessionException e) {
				WriteLogToDevice.writeLog("[Error] -- VideoPlayback: ", "IchInvalidSessionException");
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IchStreamNotRunningException e) {
				// TODO Auto-generated catch block
				WriteLogToDevice.writeLog("[Error] -- VideoPlayback: ", "IchStreamNotRunningException");
				e.printStackTrace();
			}
		WriteLogToDevice.writeLog("[Normal] -- VideoPlayback: ", "end containsAudioStream  retValue= " + retValue);
		return retValue;
	}

}
