package com.icatch.wcm2app.Activity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;

import com.icatch.wcm2app.common.ExitApp;
import com.icatch.wcm2app.common.GlobalApp;
import com.icatch.wcm2app.common.SystemCheckTools;
import com.icatch.wcm2app.common.UserMacPermition;
import com.icatch.wcm2app.common.WriteLogToDevice;
import com.icatch.wcm2app.controller.UIDisplayResource;
import com.icatch.wcm2app.controller.sdkApi.CameraProperties;
import com.icatch.wcm2app.controller.sdkApi.SDKSession;
import com.icatch.wcm2app.function.WifiCheck;
import com.icatch.wificam.customer.ICatchWificamConfig;
import com.icatch.wificam.customer.ICatchWificamLog;
import com.icatch.wificam.customer.exception.IchCameraModeException;
import com.icatch.wificam.customer.exception.IchDevicePropException;
import com.icatch.wificam.customer.exception.IchInvalidSessionException;
import com.icatch.wificam.customer.exception.IchSocketException;
import com.icatch.wificam.customer.type.ICatchLogLevel;
import com.icatch.wcm2app.R;
//import com.icatch.wificam.app.common.FileRS;

public class AppStartToMainActivity extends Activity {
	private ProgressDialog progressDialog;
	private CameraProperties cameraProperties;
	public static final int CONNECT_SUCCESS = 0;
	public static final int CONNECT_FAIL = 1;
	public static final int CONNECT_RETRY = 2;
	public static final int CHECK_CUSID_FAIL = 3;
	public static final int CHECK_MAC_FAIL = 4;
	
	// End add by zhangyanhu 2013.11.27

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d("tigertiger", "AppStart onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_start_old);

		WriteLogToDevice.initConfiguration();// configure the app log default
												// set
		enableSDKLog();

		GlobalApp.getInstance().setAppContext(getApplicationContext());
		Log.d("tigertiger", "getApplicationContext =" + getApplicationContext());
		GlobalApp.getInstance().setCurrentApp(AppStartToMainActivity.this);
		// JIRA ICOM-1890 Begin:Delete by zhangyanhu C01012 2015-08-27
		// ExitApp.getInstance().addActivity(this);
		// JIRA ICOM-1890 End:Delete by zhangyanhu C01012 2015-08-27				
		
		progressDialog = new ProgressDialog(AppStartToMainActivity.this);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setMessage(AppStartToMainActivity.this.getString(R.string.dialog_connecting_to_cam));
		progressDialog.setCancelable(false);
		progressDialog.show();		
		if (SDKSession.prepareSession() == false) {
			redirectToExit(CONNECT_FAIL);
			return;
		}
		
	
		//if (cameraProperties.checkCustomerID(0xD613, 0x0100) == false) {
	/*	if (checkCustomerID(0xD613,0x3200) == false) {
			redirectToExit(CHECK_CUSID_FAIL);
			return;
		}
	*/
//		UserMacPermition.initUserMacPermition();
//		if (UserMacPermition.isAllowedMac(getLocalMacAddress()) == false) {
//			redirectToExit(CHECK_MAC_FAIL);
//			return;
//		}
	
		//redirectToExit(CONNECT_FAIL);
		UIDisplayResource.createInstance();
		UIDisplayResource.getinstance().initUIDisplayResource();
		cameraProperties = new CameraProperties();
		if (cameraProperties.hasFuction(0x5011)) {
			long time = System.currentTimeMillis();
			Date date = new Date(time);
			SimpleDateFormat myFmt = new SimpleDateFormat("yyyyMMdd HHmmss");
			String temp = myFmt.format(date);
			temp = temp.replaceAll(" ", "T");
			temp = temp + ".0";
			cameraProperties.setCameraDate(temp);
		}

		timer.schedule(task, 1000, 1000);

		// TimeLapseInterval.getInstance().initTimeLapseInterval();
		Log.d("AppStartAppStart", "end oncreate");
	}
	private void NoticeDemoApp() {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(AppStartToMainActivity.this);
		builder.setMessage(AppStartToMainActivity.this.getString(R.string.demosbc_notice));
		builder.setIcon(R.drawable.warning);
		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog1, int which) {
				dialog1.dismiss();
				redirectToMain();
			}
		});
		AlertDialog dialog1 = builder.create();
		dialog1.setCancelable(false);
		dialog1.show();
		Log.d("AppStart", "Notice SBC dialog.show()");
	}
	private boolean checkCustomerID(int CusIDPty, int CusID) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub		
		//int CusIDPty = 0xD613;
		int getCusIDPtyValue = 0 ; 		
		try {
			getCusIDPtyValue = SDKSession.getCameraPropertyClint().getCurrentPropertyValue(CusIDPty);
		} catch (IchInvalidSessionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchSocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchDevicePropException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
						
		if ((getCusIDPtyValue & 0xffff) == (CusID & 0xffff) ) {
			return true;
		} else {
			return false;
		}
	}
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		if (SystemCheckTools.isApplicationSentToBackground(AppStartToMainActivity.this) == true) {
			ExitApp.getInstance().exit();
		}
		super.onPause();
	}

	private int checkTimes = 0;
	private Timer timer = new Timer(true);
	private TimerTask task = new TimerTask() {

		@Override
		public void run() {

			Log.d("AppStartAppStart", "appstart run:");
			if (WifiCheck.isOnline(AppStartToMainActivity.this) == false) {
				Log.d("AppStartAppStart", "AppStartOldActivity.this) == false");
				timer.cancel();
				sendOkMsg(CONNECT_FAIL);
				return;
			}
			if (SDKSession.checkWifiConnection() == true) {
				timer.cancel();
				sendOkMsg(CONNECT_SUCCESS);
			} else {
				if (checkTimes == 3) {
					timer.cancel();
					sendOkMsg(CONNECT_FAIL);
					return;
				}
				// Added by zhangyanhu 2013.11.27
				checkTimes++;
				sendOkMsg(CONNECT_RETRY);
				// End add by zhangyanhu 2013.11.27
			}
			// }
		}
	};

	private final Handler appStartHandler = new Handler() {
		public void handleMessage(Message msg) {
			Log.e("AppStart", "what =  " + msg.what);
			switch (msg.what) {
			case CONNECT_SUCCESS:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				redirectToMain();
			//	NoticeDemoApp();
				break;
			case CONNECT_FAIL:
				Log.d("AppStartAppStart", "CONNECT_FAIL");
				redirectToExit(CONNECT_FAIL);
				break;
			// Added by zhangyanhu 2013.11.27
			case CONNECT_RETRY:
				Log.d("AppStartAppStart", "CONNECT_RETRY");
				if (progressDialog != null) {
					break;
				}
				Log.d("AppStartAppStart", "CONNECT_RETRY");
				break;
			// End add by zhangyanhu 2013.11.27
			}
		};
	};

	private void redirectToMain() {
//		WifiManager mWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
//		WifiInfo wifiInfo = mWifi.getConnectionInfo();
//		GlobalApp.getInstance().setSsid(wifiInfo.getSSID());
		// Log.e("tigertiger","wifiInfo.getSSID()="+wifiInfo.getSSID());
		// Intent intent = new Intent(this, DecodeActivity.class);
		Intent intent = new Intent(this, Main.class);
		startActivity(intent);
		finish();
	}

	private void redirectToExit(int FAILID) {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(AppStartToMainActivity.this);
		//builder.setMessage(AppStartToMainActivity.this.getString(R.string.dialog_timeout));
		//builder.setMessage(AppStartToMainActivity.this.getString(R.string.dialog_timeout));
		switch (FAILID) {
		case CONNECT_FAIL:
			builder.setMessage(AppStartToMainActivity.this.getString(R.string.dialog_timeout));
			break;
		case CHECK_CUSID_FAIL: 
			builder.setMessage(AppStartToMainActivity.this.getString(R.string.invalid_device));
			break;
		case CHECK_MAC_FAIL:			
			builder.setMessage(AppStartToMainActivity.this.getString(R.string.check_mac_fail) + " Wifi MAC addr:" + getLocalMacAddress().toLowerCase());
			break;
		} 
		builder.setPositiveButton(R.string.dialog_btn_exit, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				ExitApp.getInstance().exit();
			}
		});
		AlertDialog dialog = builder.create();
		dialog.setCancelable(false);
		dialog.show();
		Log.d("AppStart", "dialog.show()");
	}

	private void sendOkMsg(int what) {
		appStartHandler.obtainMessage(what).sendToTarget();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_HOME:
			Log.d("AppStart", "home");
			timer.cancel();
			finish();
			break;
		case KeyEvent.KEYCODE_BACK:
			Log.d("AppStart", "back");
			// timer.cancel();
			// finish();
			ExitApp.getInstance().exit();
			break;
		default:
			return super.onKeyDown(keyCode, event);
		}

		return true;
	}

	/**
	 * 
	 * Added by zhangyanhu C01012,2014-4-3
	 */
	public void enableSDKLog() {
		String path = null;
		path = Environment.getExternalStorageDirectory().toString() + "/IcatchWifiCamera_SDK_Log";
		Environment.getExternalStorageDirectory();
		ICatchWificamLog.getInstance().setFileLogPath(path);
		Log.d("AppStart", "path: " + path);
		if (path != null) {
			File file = new File(path);
			if (!file.exists()) {
				file.mkdirs();
			}
		}
		ICatchWificamLog.getInstance().setSystemLogOutput(true);
		ICatchWificamLog.getInstance().setFileLogOutput(true);
		ICatchWificamLog.getInstance().setRtpLog(true);
		ICatchWificamLog.getInstance().setPtpLog(true);
		ICatchWificamLog.getInstance().setRtpLogLevel(ICatchLogLevel.ICH_LOG_LEVEL_INFO);
		ICatchWificamLog.getInstance().setPtpLogLevel(ICatchLogLevel.ICH_LOG_LEVEL_INFO);
	}

	public String getLocalMacAddress() {
		WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		Log.d("AppStart", "getMacAddress: " + info.getMacAddress().toLowerCase());
		return info.getMacAddress().toLowerCase();		
	}
}
