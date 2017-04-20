/**
 * Added by zhangyanhu C01012,2014-7-1
 */
package com.icatch.wcm2app.function;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;

import com.icatch.wcm2app.common.WriteLogToDevice;
import com.icatch.wcm2app.controller.sdkApi.CameraAction;
import com.icatch.wcm2app.controller.sdkApi.CameraProperties;
import com.icatch.wcm2app.controller.sdkApi.PreviewStream;
import com.icatch.wcm2app.R;
import com.icatch.wificam.customer.type.ICatchCameraProperty;

/**
 * Added by zhangyanhu C01012,2014-7-1
 */
public class CameraCaptureThread implements Runnable {
	private PreviewStream previewStream = new PreviewStream();
	private Context context;
	private MediaPlayer stillCaptureStartBeep;
	private MediaPlayer delayBeep;
	private CameraProperties cameraProperties = new CameraProperties();
	private CameraAction cameraAction = new CameraAction();
	private Handler handler;

	public CameraCaptureThread(Context context, Handler handler) {
		this.context = context;
		this.handler = handler;
		stillCaptureStartBeep = MediaPlayer.create(context, R.raw.camera_click);
		delayBeep = MediaPlayer.create(context, R.raw.delay_beep);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		// photo capture
		WriteLogToDevice.writeLog("[Normal] -- CameraCaptureThread: ",
				"start CameraCaptureThread");
		// if(previewStream.stopMediaStream() == false){
		// WriteLogToDevice.writeLog("[Error] -- CameraCaptureThread: ",
		// "failed to stopMediaStream");
		// return;
		// }
		Log.d("tigertiger", "start CameraCaptureThread.....");
		int delayTime = cameraProperties.getCurrentCaptureDelay();
		int remainBurstNumer = 1;
		if (cameraProperties
				.hasFuction(ICatchCameraProperty.ICH_CAP_BURST_NUMBER) == true) {
			remainBurstNumer = cameraProperties.getCurrentAppBurstNum();
		} else {
			remainBurstNumer = 1;
			stillCaptureStartBeep.start();
		}
		// if (remainBurstNumer > 0) {
		// //CaptureAudioTask captureAudioTask = new
		// CaptureAudioTask(remainBurstNumer);
		// //Timer captureAudioTimer = new Timer(true);
		// // mainTimerHandler = new MyTimerHandler();
		// //captureAudioTimer.schedule(captureAudioTask, delayTime, 1000);
		// }
		// int count = delayTime/1000;
		// Timer delayTimer = new Timer(true);
		// DelayTimerTask delayTimerTask = new DelayTimerTask(count,delayTimer);
		// delayTimer.schedule(delayTimerTask,0,1000);
		//
		// Timer delayTimer1 = new Timer(true);
		// DelayTimerTask delayTimerTask1 = new
		// DelayTimerTask(count,delayTimer1);
		// delayTimer1.schedule(delayTimerTask1,delayTime/2,500);
		Log.d("tigertiger",
				"before cameraAction.triggerCapturePhoto(); in CameraCaptureThread.java");
		// cameraAction.triggerCapturePhoto();
		cameraAction.capturePhoto();
		// handler.obtainMessage(GlobalApp.MESSAGE_CAPTURE_COMPLETED).sendToTarget();
		// WriteLogToDevice.writeLog("[Normal] -- CameraCaptureThread: ",
		// "send message  Main.CAPTURE_COMPLETED.....");

		WriteLogToDevice.writeLog("[Normal] -- CameraCaptureThread: ",
				"end CameraCaptureThread");
	}

	private class CaptureAudioTask extends TimerTask {
		private int burstNumber;

		public CaptureAudioTask(int burstNumber) {
			this.burstNumber = burstNumber;
		}

		@Override
		public void run() {
			if (burstNumber > 0) {
				WriteLogToDevice.writeLog("[Normal] -- CameraCaptureThread: ",
						"CaptureAudioTask remainBurstNumer =" + burstNumber);
				stillCaptureStartBeep.start();
				burstNumber--;
			} else {
				cancel();
			}
		}
	}

	private class DelayTimerTask extends TimerTask {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.TimerTask#run()
		 */
		private int count;
		private Timer timer;

		public DelayTimerTask(int count, Timer timer) {
			this.count = count;
			this.timer = timer;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (count-- > 0) {
				delayBeep.start();
			} else {
				if (timer != null) {
					timer.cancel();
				}
			}
		}

	}
}
