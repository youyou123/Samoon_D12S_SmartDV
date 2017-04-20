/**
 * Added by zhangyanhu C01012,2014-7-11
 */
package com.icatch.wcm2app.controller;

import android.os.Handler;

import com.icatch.wcm2app.common.GlobalApp;
import com.icatch.wcm2app.common.WriteLogToDevice;
import com.icatch.wcm2app.controller.sdkApi.CameraAction;
import com.icatch.wificam.customer.ICatchWificamListener;
import com.icatch.wificam.customer.type.ICatchEvent;
import com.icatch.wificam.customer.type.ICatchEventID;

/**
 * Added by zhangyanhu C01012,2014-7-11
 */
public class SDKEvent {
	private CameraAction cameraAction = new CameraAction();
	private Handler handler;
	private SdcardStateListener sdcardStateListener;
	private BatteryStateListener batteryStateListener;
	private CaptureStartListener captureStartListener;
	private CaptureDoneListener captureDoneListener;
	private VideoOffListener videoOffListener;
	private FileAddedListener fileAddedListener;
	private VideoOnListener videoOnListener;
	private ConnectionFailureListener connectionFailureListener;
	private TimeLapseStopListener timeLapseStopListener;
	private ServerStreamErrorListener serverStreamErrorListener;
	private VideoRecordingTimeStartListener videoRecordingTimeStartListener;

	// private Context context;
	public SDKEvent(Handler handler) {
		this.handler = handler;
		// this.context = context;
	}

	public void addEventListener(ICatchEventID iCatchEventID) {
		// switch(iCatchEventID){
		if (iCatchEventID == ICatchEventID.ICH_EVENT_SDCARD_FULL) {
			sdcardStateListener = new SdcardStateListener();
			cameraAction.addEventListener(ICatchEventID.ICH_EVENT_SDCARD_FULL, sdcardStateListener);
		}
		if (iCatchEventID == ICatchEventID.ICH_EVENT_BATTERY_LEVEL_CHANGED) {
			batteryStateListener = new BatteryStateListener();
			cameraAction.addEventListener(ICatchEventID.ICH_EVENT_BATTERY_LEVEL_CHANGED, batteryStateListener);
		}
		if (iCatchEventID == ICatchEventID.ICH_EVENT_CAPTURE_START) {
			captureStartListener = new CaptureStartListener();
			cameraAction.addEventListener(ICatchEventID.ICH_EVENT_CAPTURE_START, captureStartListener);
		}
		if (iCatchEventID == ICatchEventID.ICH_EVENT_CAPTURE_COMPLETE) {
			captureDoneListener = new CaptureDoneListener();
			cameraAction.addEventListener(ICatchEventID.ICH_EVENT_CAPTURE_COMPLETE, captureDoneListener);
		}
		if (iCatchEventID == ICatchEventID.ICH_EVENT_VIDEO_OFF) {
			videoOffListener = new VideoOffListener();
			cameraAction.addEventListener(ICatchEventID.ICH_EVENT_VIDEO_OFF, videoOffListener);
		}
		if (iCatchEventID == ICatchEventID.ICH_EVENT_FILE_ADDED) {
			fileAddedListener = new FileAddedListener();
			cameraAction.addEventListener(ICatchEventID.ICH_EVENT_FILE_ADDED, fileAddedListener);
		}
		if (iCatchEventID == ICatchEventID.ICH_EVENT_VIDEO_ON) {
			videoOnListener = new VideoOnListener();
			cameraAction.addEventListener(ICatchEventID.ICH_EVENT_VIDEO_ON, videoOnListener);
		}
		if (iCatchEventID == ICatchEventID.ICH_EVENT_CONNECTION_DISCONNECTED) {
			connectionFailureListener = new ConnectionFailureListener();
			cameraAction.addEventListener(ICatchEventID.ICH_EVENT_CONNECTION_DISCONNECTED, connectionFailureListener);
		}
		if (iCatchEventID == ICatchEventID.ICH_EVENT_TIMELAPSE_STOP) {
			timeLapseStopListener = new TimeLapseStopListener();
			cameraAction.addEventListener(ICatchEventID.ICH_EVENT_TIMELAPSE_STOP, timeLapseStopListener);
		}
		
		if (iCatchEventID == ICatchEventID.ICH_EVENT_SERVER_STREAM_ERROR) {
			serverStreamErrorListener = new ServerStreamErrorListener();
			cameraAction.addEventListener(ICatchEventID.ICH_EVENT_SERVER_STREAM_ERROR, serverStreamErrorListener);
		}
		
//		if (iCatchEventID == 0x5001) {
//			serverStreamErrorListener = new ServerStreamErrorListener();
//			cameraAction.addEventListener(ICatchEventID.ICH_EVENT_SERVER_STREAM_ERROR, serverStreamErrorListener);
//		}
		// ConnectionFailureListener
	}
	
	public void addCustomizeEvent(int eventID){
		switch(eventID){
			case 0x5001:
				videoRecordingTimeStartListener = new VideoRecordingTimeStartListener();
				cameraAction.addCustomEventListener(eventID, videoRecordingTimeStartListener);
				break;
		}
		
	}
	
	
	public void delCustomizeEventListener(int eventID){
		switch(eventID){
			case 0x5001:
				if(videoRecordingTimeStartListener != null){
					cameraAction.addCustomEventListener(eventID, videoRecordingTimeStartListener);
				}
				break;
		}
		
	}
	
	public void delEventListener(ICatchEventID iCatchEventID) {
		// switch(iCatchEventID){
		if (iCatchEventID == ICatchEventID.ICH_EVENT_SDCARD_FULL && sdcardStateListener != null) {
			cameraAction.delEventListener(ICatchEventID.ICH_EVENT_SDCARD_FULL, sdcardStateListener);
		}
		if (iCatchEventID == ICatchEventID.ICH_EVENT_BATTERY_LEVEL_CHANGED && batteryStateListener != null) {
			cameraAction.delEventListener(ICatchEventID.ICH_EVENT_BATTERY_LEVEL_CHANGED, batteryStateListener);
		}
		if (iCatchEventID == ICatchEventID.ICH_EVENT_CAPTURE_COMPLETE && captureDoneListener != null) {
			cameraAction.delEventListener(ICatchEventID.ICH_EVENT_CAPTURE_COMPLETE, captureDoneListener);
		}
		if (iCatchEventID == ICatchEventID.ICH_EVENT_CAPTURE_START && captureStartListener != null) {
			cameraAction.delEventListener(ICatchEventID.ICH_EVENT_CAPTURE_START, captureStartListener);
		}
		if (iCatchEventID == ICatchEventID.ICH_EVENT_VIDEO_OFF && videoOffListener != null) {
			cameraAction.delEventListener(ICatchEventID.ICH_EVENT_VIDEO_OFF, videoOffListener);
		}
		if (iCatchEventID == ICatchEventID.ICH_EVENT_FILE_ADDED && fileAddedListener != null) {
			cameraAction.delEventListener(ICatchEventID.ICH_EVENT_FILE_ADDED, fileAddedListener);
		}
		if (iCatchEventID == ICatchEventID.ICH_EVENT_VIDEO_ON && videoOnListener != null) {
			cameraAction.delEventListener(ICatchEventID.ICH_EVENT_VIDEO_ON, videoOnListener);
		}
		if (iCatchEventID == ICatchEventID.ICH_EVENT_CONNECTION_DISCONNECTED && connectionFailureListener != null) {
			cameraAction.delEventListener(ICatchEventID.ICH_EVENT_CONNECTION_DISCONNECTED, connectionFailureListener);
		}
		if (iCatchEventID == ICatchEventID.ICH_EVENT_TIMELAPSE_STOP &&  timeLapseStopListener != null) {
			cameraAction.delEventListener(ICatchEventID.ICH_EVENT_TIMELAPSE_STOP, timeLapseStopListener);
		}
		
		if (iCatchEventID == ICatchEventID.ICH_EVENT_SERVER_STREAM_ERROR &&  serverStreamErrorListener != null) {
			cameraAction.delEventListener(ICatchEventID.ICH_EVENT_SERVER_STREAM_ERROR, serverStreamErrorListener);
		}
	}

	public class SdcardStateListener implements ICatchWificamListener {

		@Override
		public void eventNotify(ICatchEvent arg0) {
			// TODO Auto-generated method stub
			handler.obtainMessage(GlobalApp.EVENT_SD_CARD_FULL).sendToTarget();
			WriteLogToDevice.writeLog("[Normal] -- Main: ", "event: EVENT_SD_CARD_FULL");
		}
	}

	/**
	 * 
	 * Added by zhangyanhu C01012,2014-3-7
	 */
	public class BatteryStateListener implements ICatchWificamListener {

		@Override
		public void eventNotify(ICatchEvent arg0) {
			// TODO Auto-generated method stub
			handler.obtainMessage(GlobalApp.EVENT_BATTERY_ELETRIC_CHANGED).sendToTarget();
		}
	}

	public class CaptureDoneListener implements ICatchWificamListener {

		@Override
		public void eventNotify(ICatchEvent arg0) {
			// TODO Auto-generated method stub
			WriteLogToDevice.writeLog("[Normal] -- SDKEvent: ", "--------------receive event:capture done");
			handler.obtainMessage(GlobalApp.EVENT_CAPTURE_COMPLETED).sendToTarget();
		}
	}

	public class CaptureStartListener implements ICatchWificamListener {

		@Override
		public void eventNotify(ICatchEvent arg0) {
			// TODO Auto-generated method stub
			WriteLogToDevice.writeLog("[Normal] -- SDKEvent: ", "--------------receive event:capture start");
			handler.obtainMessage(GlobalApp.EVENT_CAPTURE_START).sendToTarget();
		}
	}

	/**
	 * 
	 * Added by zhangyanhu C01012,2014-3-10
	 */
	public class VideoOffListener implements ICatchWificamListener {
		@Override
		public void eventNotify(ICatchEvent arg0) {
			// TODO Auto-generated method stub
			WriteLogToDevice.writeLog("[Normal] -- SDKEvent: ", "--------------receive event:videooff");
			handler.obtainMessage(GlobalApp.EVENT_VIDEO_OFF).sendToTarget();
		}
	}

	/**
	 * 
	 * Added by zhangyanhu C01012,2014-3-10
	 */
	public class VideoOnListener implements ICatchWificamListener {
		@Override
		public void eventNotify(ICatchEvent arg0) {
			// TODO Auto-generated method stub
			WriteLogToDevice.writeLog("[Normal] -- SDKEvent: ", "--------------receive event:videoON");
			handler.obtainMessage(GlobalApp.EVENT_VIDEO_ON).sendToTarget();
		}
	}

	/**
	 * 
	 * Added by zhangyanhu C01012,2014-4-1
	 */
	public class FileAddedListener implements ICatchWificamListener {
		@Override
		public void eventNotify(ICatchEvent arg0) {
			// TODO Auto-generated method stub
			WriteLogToDevice.writeLog("[Normal] -- SDKEvent: ", "--------------receive event:FileAddedListener");
			handler.obtainMessage(GlobalApp.EVENT_FILE_ADDED).sendToTarget();
		}
	}

	public class ConnectionFailureListener implements ICatchWificamListener {
		@Override
		public void eventNotify(ICatchEvent arg0) {
			// TODO Auto-generated method stub
			WriteLogToDevice.writeLog("[Normal] -- SDKEvent: ", "--------------receive event:ConnectionFailureListener");
			handler.obtainMessage(GlobalApp.EVENT_CONNECTION_FAILURE).sendToTarget();
			// sendOkMsg(EVENT_FILE_ADDED);
		}
	}
	
	public class TimeLapseStopListener implements ICatchWificamListener {
		@Override
		public void eventNotify(ICatchEvent arg0) {
			// TODO Auto-generated method stub
			WriteLogToDevice.writeLog("[Normal] -- SDKEvent: ", "--------------receive event:TimeLapseStopListener");
			handler.obtainMessage(GlobalApp.EVENT_TIME_LAPSE_STOP).sendToTarget();
			// sendOkMsg(EVENT_FILE_ADDED);
		}
	}
	
	public class ServerStreamErrorListener implements ICatchWificamListener {
		@Override
		public void eventNotify(ICatchEvent arg0) {
			// TODO Auto-generated method stub
			WriteLogToDevice.writeLog("[Normal] -- SDKEvent: ", "--------------receive event:TimeLapseStopListener");
			handler.obtainMessage(GlobalApp.EVENT_SERVER_STREAM_ERROR).sendToTarget();
			// sendOkMsg(EVENT_FILE_ADDED);
		}
	}

	public class VideoRecordingTimeStartListener implements ICatchWificamListener {
		@Override
		public void eventNotify(ICatchEvent arg0) {
			// TODO Auto-generated method stub
			WriteLogToDevice.writeLog("[Normal] -- SDKEvent: ", "--------------receive VideoRecordingTimeStartListener");
			handler.obtainMessage(GlobalApp.EVENT_VIDEO_RECORDING_TIME).sendToTarget();
			// sendOkMsg(EVENT_FILE_ADDED);
		}
	}
}
