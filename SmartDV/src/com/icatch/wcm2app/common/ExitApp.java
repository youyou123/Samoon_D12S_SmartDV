package com.icatch.wcm2app.common;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

import com.icatch.wcm2app.controller.sdkApi.FileOperation;
import com.icatch.wcm2app.controller.sdkApi.PreviewStream;
import com.icatch.wcm2app.controller.sdkApi.SDKSession;
import com.icatch.wcm2app.controller.sdkApi.VideoPlayback;

public class ExitApp extends Application {

	private List<Activity> activityList = new LinkedList<Activity>();	
	private static ExitApp instance;
	public static ExitApp getInstance() {
		if(instance == null){
			instance = new ExitApp();
		}
		return instance;
	}

	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	public void removeActivity(Activity activity) {
		activityList.remove(activity);
	}

	public void exit() {
		if(SDKSession.isSessionOK() == false){
			for (Activity activity : activityList) {
				activity.finish();
			}
			WriteLogToDevice.writeLog("[Normal] -- ExitApp: ", "start System.exit");
			System.exit(0);
		}
		
		PreviewStream previewStream = new PreviewStream();
		FileOperation fileOperation = new FileOperation();
		VideoPlayback videoPlayback = new VideoPlayback();
		boolean temp = false;
		int ii = 0;
		Log.d("tigertiger","fileOperation = "+fileOperation);
		Log.d("tigertiger","fileOperation.cameraPlayback = "+fileOperation.cameraPlayback);
			while ((temp == false) && (ii < 3)) {
				WriteLogToDevice.writeLog("[Normal] -- ExitApp: ", "start start cancelDownload ");
				temp = fileOperation.cancelDownload();
				WriteLogToDevice.writeLog("[Normal] -- ExitApp: ", "end cancelDownload temp =" + temp);
				ii++;
			}

			temp = false;
			ii = 0;
			while ((temp == false) && (ii < 3)) {
				Log.d("tigertiger","-----------previewStream = "+previewStream);
				WriteLogToDevice.writeLog("[Normal] -- ExitApp: ", "start start stopMediaStream ");
				temp = previewStream.stopMediaStream();
				WriteLogToDevice.writeLog("[Normal] -- ExitApp: ", "start stopMediaStream temp =" + temp);
				ii++;
			}

			temp = false;
			ii = 0;
			while ((temp == false) && (ii < 3)) {
				WriteLogToDevice.writeLog("[Normal] -- ExitApp: ", "start start stopPlaybackStream ");
				temp = videoPlayback.stopPlaybackStream();
				WriteLogToDevice.writeLog("[Normal] -- ExitApp: ", "start stopPlaybackStream temp =" + temp);
				ii++;
			}

				// GlobalView.getInstance().getSession().
				temp = SDKSession.destroySession();
				WriteLogToDevice.writeLog("[Normal] -- ExitApp: ", "destroySession temp =" + temp);
				
		for (Activity activity : activityList) {
			activity.finish();
		}
		WriteLogToDevice.writeLog("[Normal] -- ExitApp: ", "start System.exit");
		System.exit(0);
		WriteLogToDevice.writeLog("[Normal] -- ExitApp: ", "end System.exit");
	}

	/**
	 * 
	 * Added by zhangyanhu C01012,2014-4-9
	 */
	public void finishAllActivity() {
		PreviewStream previewStream = new PreviewStream();
		boolean temp = false;
		int ii = 0;
			while ((temp == false) && (ii < 3)) {
				WriteLogToDevice.writeLog("[Normal] -- ExitApp: ", "start stopMediaStream ");
				temp = previewStream.stopMediaStream();
				WriteLogToDevice.writeLog("[Normal] -- ExitApp: ", "end stopMediaStream temp =" + temp);
				ii++;
			}

		WriteLogToDevice.writeLog("[Normal] -- ExitApp: ", "start destroySession temp =" + temp);
		temp = SDKSession.destroySession();
		WriteLogToDevice.writeLog("[Normal] -- ExitApp: ", "end destroySession temp =" + temp);
		for (Activity activity : activityList) {
			activity.finish();
		}
	}
}
