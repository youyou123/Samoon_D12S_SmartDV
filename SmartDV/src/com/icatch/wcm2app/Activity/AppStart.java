package com.icatch.wcm2app.Activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.icatch.wcm2app.common.ExitApp;
import com.icatch.wcm2app.common.FileTools;
import com.icatch.wcm2app.common.GlobalApp;
import com.icatch.wcm2app.common.SystemCheckTools;
import com.icatch.wcm2app.common.WriteLogToDevice;
import com.icatch.wcm2app.controller.UIDisplayResource;
import com.icatch.wcm2app.controller.sdkApi.CameraProperties;
import com.icatch.wcm2app.controller.sdkApi.SDKSession;
import com.icatch.wcm2app.function.WifiCheck;
import com.icatch.wcm2app.R;
//import com.icatch.wificam.app.test.test;
import com.icatch.wificam.customer.ICatchWificamLog;
import com.icatch.wificam.customer.type.ICatchLogLevel;

public class AppStart extends Activity {
	private CameraProperties cameraProperties;
	public static final int CONNECT_SUCCESS = 0;
	public static final int CONNECT_FAIL = 1;
	public static final int CONNECT_RETRY = 2;
	public static final int CONNECT_NEW_WIFI_SUCCESS = 3;
	public static final int CONNECT_NEXT_WIFI = 4;
	public static final int NO_WIFI_CONNECTTED = 5;
	private List<ScanResult> wifiList = null;
	private int reconnectTime = 0;
	private static final int RETRY_TIMES = 6;
	private WifiCheck wifiCheck;
	private boolean autoConnection = true;
	private String[] ssidAndPassword;
	private ProgressBar progressBar;
	private TextView connectTextView;
	private RelativeLayout connect_relativeLayout;
	private TextView noFilesFind;
	private ImageView photo;
	private Bitmap loadBitmap = null;

	// End add by zhangyanhu 2013.11.27

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		WriteLogToDevice.writeLog("[Normal] -- Appstart: ", "AppStart onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start);

		WriteLogToDevice.initConfiguration();// configure the app log default
												// set
		enableSDKLog();
		GlobalApp.getInstance().setAppContext(getApplicationContext());
		GlobalApp.getInstance().setCurrentApp(AppStart.this);
		ExitApp.getInstance().addActivity(this);
		ImageView setting = (ImageView) findViewById(R.id.setting);
		noFilesFind = (TextView) findViewById(R.id.no_file_found);
		progressBar = (ProgressBar) findViewById(R.id.progressBar_connect);
		connect_relativeLayout = (RelativeLayout) findViewById(R.id.connect_relativeLayout);
		setting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showCameraConfigurationDialog();
			}
		});
		photo = (ImageView) findViewById(R.id.local_photo);

		photo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				WriteLogToDevice.writeLog("[Normal] -- AppStart: ", "intent:start LocalPhotoWallActivity");
				intent.setClass(AppStart.this, LocalPhotoWallActivity.class);
				startActivity(intent);
			}
		});

		connectTextView = (TextView) findViewById(R.id.connect);
		connectTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				WriteLogToDevice.writeLog("[Normal] -- Appstart: ", "connectTextView clicked");
				connectTextView.setEnabled(false);
				// 增加代码去判断当前连接的ssid的正确性，如果不正确，需要重连到其他包含关键字的camera
				WifiManager mWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
				WifiInfo wifiInfo = mWifi.getConnectionInfo();
				wifiCheck = new WifiCheck();
				progressBar.setVisibility(View.VISIBLE);
				WriteLogToDevice.writeLog("[Normal] -- Appstart: ", "current wifiInfo.getSSID() =" + wifiInfo.getSSID());
				if (autoConnection) {
					ssidAndPassword = readSsidAndPassword();
					if (ssidAndPassword != null) {
						if (wifiCheck.isWifiConnect(GlobalApp.getInstance().getCurrentApp())
								&& wifiInfo.getSSID().contains(ssidAndPassword[0]) == true) {
							initAPP(wifiInfo.getSSID());
						} else {
							wifiList = wifiCheck.getWifiListByFilter(ssidAndPassword[0]);
							connectOtherWifi(ssidAndPassword[1]);
						}
					} else {
						initAPP(wifiInfo.getSSID());
					}
				} else {
					initAPP(wifiInfo.getSSID());
				}
			}
		});

		WriteLogToDevice.writeLog("[Normal] -- Appstart: ", "end oncreate");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		if (SystemCheckTools.isApplicationSentToBackground(AppStart.this) == true) {
			ExitApp.getInstance().exit();
		}
		super.onPause();
	}

	private int checkTimes = 0;
	private Timer timer = new Timer(true);
	private TimerTask connectTask = new TimerTask() {

		@Override
		public void run() {

			WriteLogToDevice.writeLog("[Normal] -- Appstart: ", "appstart run:");
			if (WifiCheck.isOnline(AppStart.this) == false) {
				WriteLogToDevice.writeLog("[Normal] -- Appstart: ", "AppStart.this) == false");
				timer.cancel();
				sendOkMsg(CONNECT_FAIL);
				return;
			}
			if (SDKSession.checkWifiConnection() == true) {
				timer.cancel();
				sendOkMsg(CONNECT_SUCCESS);
			} else {
				if (checkTimes == 5) {
					timer.cancel();
					sendOkMsg(CONNECT_FAIL);
					return;
				}
				// Added by zhangyanhu 2013.11.27
				checkTimes++;
				sendOkMsg(CONNECT_RETRY);
				// End add by zhangyanhu 2013.11.27
			}
		}
	};

	private final Handler appStartHandler = new Handler() {
		public void handleMessage(Message msg) {
			Log.e("AppStart", "what =  " + msg.what);
			switch (msg.what) {
			case CONNECT_SUCCESS:
				WriteLogToDevice.writeLog("[Normal] -- Appstart: ", "CONNECT_SUCCESS");
				progressBar.setVisibility(View.GONE);
				redirectToMain();
				break;
			case CONNECT_FAIL:
				WriteLogToDevice.writeLog("[Normal] -- Appstart: ", "CONNECT_FAIL");
				progressBar.setVisibility(View.GONE);
				redirectToExit();
				break;
			case CONNECT_RETRY:
				WriteLogToDevice.writeLog("[Normal] -- Appstart: ", "CONNECT_RETRY");
				break;
			case CONNECT_NEW_WIFI_SUCCESS:
				WriteLogToDevice.writeLog("[Normal] -- Appstart: ", "receive CONNECT_NEW_WIFI_SUCCESS");
				initAPP(wifiList.get(0).SSID);
				break;
			case CONNECT_NEXT_WIFI:
				WriteLogToDevice.writeLog("[Normal] -- Appstart: ", "CONNECT_NEXT_WIFI ssid ="+ssidAndPassword[0]);
				connectOtherWifi(ssidAndPassword[1]);
				break;
			case NO_WIFI_CONNECTTED:
				WriteLogToDevice.writeLog("[Normal] -- Appstart: ", "NO_WIFI_CONNECTTED");
				progressBar.setVisibility(View.GONE);
				redirectToExit();
				break;
			}
		};
	};

	private void redirectToMain() {
		WifiManager mWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = mWifi.getConnectionInfo();
		GlobalApp.getInstance().setSsid(wifiInfo.getSSID());
		Intent intent = new Intent(this, Main.class);
		startActivity(intent);
	}

	private void redirectToExit() {
		AlertDialog.Builder builder = new AlertDialog.Builder(AppStart.this);
		builder.setMessage(AppStart.this.getString(R.string.dialog_timeout));
		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				// ExitApp.getInstance().exit()
				connectTextView.setEnabled(true);
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
			if (timer != null)
				timer.cancel();
			finish();
			break;
		case KeyEvent.KEYCODE_BACK:
			Log.d("AppStart", "back");
			ExitApp.getInstance().exit();
			break;
		default:
			return super.onKeyDown(keyCode, event);
		}

		return true;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		connectTextView.setEnabled(true);
		DisplayMetrics dm = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		loadBitmap = FileTools.getBitmapFromFolder(GlobalApp.DOWNLOAD_PATH, dm.widthPixels);
		Log.e("tigertiger", "Bitmap  == " + loadBitmap);

		if (loadBitmap != null) {
			photo.setImageBitmap(loadBitmap);
			noFilesFind.setVisibility(View.GONE);
		} else {
			photo.setEnabled(false);
			noFilesFind.setVisibility(View.VISIBLE);
		}

		WriteLogToDevice.writeLog("[Normal] -- Appstart: ", "loadBitmap ="+loadBitmap);
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

	private void initAPP(String ssid) {
		WriteLogToDevice.writeLog("[Normal] -- Appstart: ", "isWifiConnect() == true");
		connectTextView.setTextColor(Color.WHITE);
		connectTextView.setText("CONNECT TO \n" + ssid);
		if (SDKSession.prepareSession() == false) {
			progressBar.setVisibility(View.GONE);
			redirectToExit();
			return;
		}
		UIDisplayResource.createInstance();
		UIDisplayResource.getinstance();
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
		timer = new Timer(true);
		connectTask = new TimerTask() {

			@Override
			public void run() {

				WriteLogToDevice.writeLog("[Normal] -- Appstart: ", "appstart run:");
				if (WifiCheck.isOnline(AppStart.this) == false) {
					WriteLogToDevice.writeLog("[Normal] -- Appstart: ", "AppStart.this) == false");
					timer.cancel();
					sendOkMsg(CONNECT_FAIL);
					return;
				}
				if (SDKSession.checkWifiConnection() == true) {
					timer.cancel();
					sendOkMsg(CONNECT_SUCCESS);
				} else {
					if (checkTimes == 5) {
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
		timer.schedule(connectTask, 1000, 1000);
		return;
	}

	private void connectOtherWifi(String password) {
		WriteLogToDevice.writeLog("[Normal] -- Appstart: ", "connectOtherWifi");
		if (wifiList == null || wifiList.isEmpty()) {
			WriteLogToDevice.writeLog("[Normal] -- Appstart: ", "wifiList isEmpty()");
			sendOkMsg(NO_WIFI_CONNECTTED);
			return;
		}
		Log.e("tigertiger", "current wifi ssid=" + wifiList.get(0).SSID);
		// progressDialog.setMessage(AppStart.this.getString(R.string.dialog_connecting_to_cam)
		// + wifiList.get(0).SSID);
		connectTextView.setTextColor(Color.WHITE);
		connectTextView.setText("CONNECT TO \n" + wifiList.get(0).SSID);
		wifiCheck.connectWifi(wifiList.get(0).SSID, password, wifiCheck.getSecurity(wifiList.get(0)));
		reconnectTime = 0;
		final Timer connectTimer = new Timer();
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				if (wifiCheck.isWifiConnected(AppStart.this, ssidAndPassword[0]) == true) {
					WriteLogToDevice.writeLog("[Normal] -- Appstart: ", "isWifiConnect() == true");
					sendOkMsg(CONNECT_NEW_WIFI_SUCCESS);
					if (connectTimer != null) {
						connectTimer.cancel();
					}
					return;
				} else {
					WriteLogToDevice.writeLog("[Normal] -- Appstart: ", "isWifiConnect() == false  reconnectTime =" + reconnectTime);
					reconnectTime++;
					if (reconnectTime >= RETRY_TIMES) {
						if (connectTimer != null) {
							connectTimer.cancel();
						}
						wifiList.remove(0);
						sendOkMsg(CONNECT_NEXT_WIFI);
						return;
					}
				}
			}
		};
		connectTimer.schedule(task, 0, 3000);
	}

	private void showCameraConfigurationDialog() {
		// TODO Auto-generated method stub

		String[] tempStirng = readSsidAndPassword();
		if (tempStirng == null) {
			tempStirng = new String[2];
			tempStirng[0] = "";
			tempStirng[1] = "";
		}
		LayoutInflater factory = LayoutInflater.from(AppStart.this);
		View textEntryView = factory.inflate(R.layout.camera_name_password_set, null);
		final EditText cameraName = (EditText) textEntryView.findViewById(R.id.camera_name);
		final String name = tempStirng[0];
		cameraName.setText(name);
		final EditText cameraPassword = (EditText) textEntryView.findViewById(R.id.wifi_password);
		final String password = tempStirng[1];
		cameraPassword.setText(password);
		AlertDialog.Builder ad1 = new AlertDialog.Builder(AppStart.this);
		ad1.setTitle(R.string.camera_wifi_configuration);
		ad1.setIcon(android.R.drawable.ic_dialog_info);
		ad1.setView(textEntryView);
		ad1.setCancelable(true);
		ad1.setPositiveButton(R.string.camera_configuration_set, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int arg1) {

				String temp1 = cameraName.getText().toString();
				if (temp1.length() > 20) {
					Toast.makeText(AppStart.this, R.string.camera_name_limit, Toast.LENGTH_LONG).show();
					// do not allow dialog close
					try {
						Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
						field.setAccessible(true);
						field.set(dialog, false);

					} catch (Exception e) {
						e.printStackTrace();
					}
					return;
				}
				String temp = cameraPassword.getText().toString();
				if (temp.length() > 10 || temp.length() < 8) {
					Toast.makeText(AppStart.this, R.string.password_limit, Toast.LENGTH_LONG).show();
					// do not allow dialog close
					try {
						Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
						field.setAccessible(true);
						field.set(dialog, false);

					} catch (Exception e) {
						e.printStackTrace();
					}
					return;
				}

				// allow dialog close
				try {
					Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
					field.setAccessible(true);
					field.set(dialog, true);
				} catch (Exception e) {
					e.printStackTrace();
				}

				if (name.equals(cameraName.getText().toString()) == false) {
					// cameraProperties.setCameraSsid(cameraName.getText().toString());
				}
				if (password.equals(temp) == false) {
					// cameraProperties.setCameraPassword(cameraPassword.getText().toString());
				}
				saveSsidAndPassword(temp1, temp);
			}
		});
		ad1.show();
	}

	public void saveSsidAndPassword(String ssid, String password) {
		WriteLogToDevice.writeLog("[Normal] -- Appstart: ", "saveSsidAndPassword ssid ="+ssid+"  password="+password);
		FileOutputStream outStream = null;
		String tempSsid = null;
		String tempPassword = null;
		try {
			outStream = openFileOutput("ssidAndPassword.txt", Context.MODE_PRIVATE);
			tempSsid = ssid + ";";
			tempPassword = password + ";";
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			WriteLogToDevice.writeLog("[Error] -- Appstart: ", "open outStream  FileNotFoundException");
			e.printStackTrace();
		}

		try {
			outStream.write(tempSsid.getBytes());
			outStream.write(tempPassword.getBytes());
			outStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			WriteLogToDevice.writeLog("[Error] -- Appstart: ", "open outStream IOException");
			e.printStackTrace();
		}
		// outStream.
	}

	public String[] readSsidAndPassword() {
		// outStream.
		WriteLogToDevice.writeLog("[Normal] -- Appstart: ", "start readSsidAndPassword");
		FileInputStream inStream = null;
		try {
			inStream = openFileInput("ssidAndPassword.txt");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block`
			WriteLogToDevice.writeLog("[Error] -- Appstart: ", "FileNotFoundException");
			e1.printStackTrace();
		}
		if (inStream == null) {
			
			return null;
		}
		try {
			if (inStream.available() <= 0) {
				return null;
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		byte[] b = null;
		try {
			b = new byte[inStream.available()];
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// 新建一个字节数组
		try {
			inStream.read(b);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// 将文件中的内容读取到字节数组中
		try {
			inStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (b == null) {
			return null;
		}
		String str2 = new String(b);// 再将字节数组中的内容转化成字符串形式输出
		if (str2 == null) {
			return null;
		}
		String newStr = str2.replaceAll("\"", "");
		WriteLogToDevice.writeLog("[Normal] -- Appstart: ", "------newStr =" + newStr);
		String[] temp = newStr.split(";");
		if (temp.length % 2 != 0) {
			return null;
		}
		String[] ssidAndPsd = new String[2];
		// WriteLogToDevice.writeLog("[Normal] -- Appstart: ",
		// "-----------newStr ="+newStr);
		for (int ii = 0; ii < 1; ii = ii + 2) {
			// Log.e("tigertiger", "temp[ii] = " + temp[ii]);
			// if(ssidAndMac.containsKey(temp[ii]) == false){
			// ssidList.add(temp[ii]);
			// }
			// ssidAndMac.put(temp[ii], temp[ii + 1]);

		}
		ssidAndPsd[0] = temp[0];
		ssidAndPsd[1] = temp[1];
		WriteLogToDevice.writeLog("[Normal] -- Appstart: ", "end readSsidAndPassword ssid ="+ssidAndPsd[0]+"  password="+ssidAndPsd[1]);
		return ssidAndPsd;
		// for test
		// ssidList.add("forTest");
		// ssidAndMac.put("forTest", "00:16:D3:46:31:E2");
	}
}
