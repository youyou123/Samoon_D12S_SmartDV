package com.icatch.wcm2app.function;

import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.icatch.wcm2app.Activity.AppStartToMainActivity;
import com.icatch.wcm2app.adapter.WifiListAdapter;
import com.icatch.wcm2app.common.ExitApp;
import com.icatch.wcm2app.common.GlobalApp;
import com.icatch.wcm2app.common.WriteLogToDevice;
import com.icatch.wcm2app.controller.SDKEvent;
import com.icatch.wcm2app.controller.sdkApi.SDKSession;
import com.icatch.wcm2app.R;
import com.icatch.wificam.customer.type.ICatchEventID;

public class WifiCheck {
	// private GlobalView global = GlobalView.getInstance();
	private static final int CONNECT_SUCCESS = 0x01;
	private static final int CONNECT_FAILED = 0x02;
	private static final int IN_BACKGROUND = 0x03;
	private static final int RECONNECT_SUCCESS = 0x04;
	private static final int RECONNECT_FAILED = 0x05;
	private static final int WIFICIPHER_NOPASS = 0x06;
	private static final int WIFICIPHER_WEP = 0x07;
	private static final int WIFICIPHER_WAP = 0x08;
	private static final int RECONNECT_CAMERA = 0x09;
	private static int RECONNECT_WAITING = 10000;
	private static int RECONNECT_CHECKING_PERIOD = 5000;
	private GlobalApp globalApp = GlobalApp.getInstance();
	private Boolean isShowed = false;
	private Timer timer;
	private Timer connectTimer;
	private AlertDialog dialog;
	// 定义WifiManager对象
	private WifiManager mWifiManager;
	// 定义WifiInfo对象
	private WifiInfo mWifiInfo;
	// 扫描出的网络连接列表
	private List<ScanResult> mWifiList;
	// 网络连接列表
	private List<WifiConfiguration> mWifiConfiguration;
	// 定义一个WifiLock
	private WifiLock mWifiLock;
	private int reconnectTime = 0;
	private ProgressDialog progressDialog;
	private SDKEvent sdkEvent;
	private Timer reconnectTimer;
	protected AlertDialog reconnectDialog;

	public WifiCheck() {
		// 取得WifiManager对象
		mWifiManager = (WifiManager) (globalApp.getCurrentApp()).getSystemService(Context.WIFI_SERVICE);
		// 取得WifiInfo对象
		mWifiInfo = mWifiManager.getConnectionInfo();
	}

	public static boolean isSSStrong(Context context) {
		int strength = 0;

		WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		if (info.getBSSID() != null) {
			strength = WifiManager.calculateSignalLevel(info.getRssi(), 4);
		}
		Log.e("tigertiger", "strength......=" + strength);
		return strength > 1;
	}

	public static boolean isOnline(Context context) {
		ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		if (networkInfo != null && networkInfo.isConnected() == true) {
			return true;
		} else {
			return false;
		}

	}

	public void checkWifiPolicy() {
		ContentResolver resolver = GlobalApp.getInstance().getCurrentApp().getContentResolver();
		int value = Settings.System.getInt(resolver, Settings.System.WIFI_SLEEP_POLICY, Settings.System.WIFI_SLEEP_POLICY_DEFAULT);
		WriteLogToDevice.writeLog("tigertiger", " Settings.System.WIFI_SLEEP_POLICY =" + value);
		if (Settings.System.WIFI_SLEEP_POLICY_NEVER != value) {
			AlertDialog.Builder builder = new AlertDialog.Builder(GlobalApp.getInstance().getCurrentApp());
			builder.setMessage(GlobalApp.getInstance().getCurrentApp().getString(R.string.check_wifi_policy));
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			AlertDialog checkDialog = builder.create();
			checkDialog.setCancelable(true);
			checkDialog.show();
		}
	}

	/**
	 * Added by zhangyanhu C01012,2014-2-17
	 */
	public void cancelConnectCheck() {
		if (timer != null) {
			timer.cancel();
		}
	}

	/**
	 * check the wifi connect to camera periodly Added by zhangyanhu
	 * C01012,2014-2-13
	 */
	public void openConnectCheck() {
		sdkEvent = new SDKEvent(wifiCheckHandler);
		sdkEvent.addEventListener(ICatchEventID.ICH_EVENT_CONNECTION_DISCONNECTED);
	}

	private final Handler wifiCheckHandler = new Handler() {
		public void handleMessage(Message msg) {
			Log.e("tigertiger", "handleMessage");
			switch (msg.what) {
			case CONNECT_FAILED:
				Log.e("tigertiger", "CONNECT_FAILED");
				/*
				 * if (isBackground(globalApp.getCurrentApp()) == true) {
				 * Log.e("tigertiger", ".........exit...........");
				 * ExitApp.getInstance().exit(); return; }
				 */
				//showWarningDlg(globalApp.getCurrentApp());
				break;
			case GlobalApp.EVENT_CONNECTION_FAILURE:
				WriteLogToDevice.writeLog("tigertiger", " receive EVENT_CONNECTION_FAILURE");
				showWarningDlg(globalApp.getCurrentApp());
//				reconnectTimer = new Timer(true);
//				ReconnectTask task = new ReconnectTask();
//				reconnectTimer.schedule(task, 10000,RECONNECT_CHECKING_PERIOD);
//				wifiCheckHandler.obtainMessage(RECONNECT_CAMERA).sendToTarget();
				break;
			case IN_BACKGROUND:
				Log.e("tigertiger", ".........exit...........");
				ExitApp.getInstance().exit();
				break;
			case RECONNECT_SUCCESS:
				ExitApp.getInstance().finishAllActivity();
				Intent intent = new Intent();
				intent.setClass(globalApp.getCurrentApp(), AppStartToMainActivity.class);
				globalApp.getCurrentApp().startActivity(intent);
				progressDialog.dismiss();
				if (connectTimer != null) {
					connectTimer.cancel();
				}
				break;
			case RECONNECT_FAILED:
				ExitApp.getInstance().finishAllActivity();
				Intent intent1 = new Intent();
				intent1.setClass(globalApp.getCurrentApp(), AppStartToMainActivity.class);
				globalApp.getCurrentApp().startActivity(intent1);
				progressDialog.dismiss();
				if (connectTimer != null) {
					connectTimer.cancel();
				}
				break;
			case RECONNECT_CAMERA:
				Log.e("tigertiger","receive RECONNECT_CAMERA");
				if(dialog != null){
					dialog.dismiss();
				}
				AlertDialog.Builder builder = new AlertDialog.Builder(globalApp.getCurrentApp());
				builder.setIcon(R.drawable.warning).setTitle(R.string.dialog_btn_reconnect).setMessage(R.string.message_reconnect);
				builder.setPositiveButton(R.string.dialog_btn_exit, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Log.e("tigertiger", "ExitApp.getInstance().exit()");
						ExitApp.getInstance().exit();
					}
				});
				reconnectDialog = builder.create();
				reconnectDialog.setCancelable(false);
				reconnectDialog.show();
				break;
			}
		}
	};
	private WifiManager wifiManager;
	private List<ScanResult> list;

	private void showWarningDlg(Context context) {
		// final Context tempContext = context;
		if (isShowed == true) {
			return;
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setIcon(R.drawable.warning).setTitle("Warning").setMessage(R.string.dialog_timeout);
		builder.setPositiveButton(R.string.dialog_btn_exit, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.e("tigertiger", "ExitApp.getInstance().exit()");
				ExitApp.getInstance().exit();
			}
		});
		builder.setNegativeButton(R.string.dialog_btn_reconnect, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.e("tigertiger", "重新连接。。。。。");
				reconnectTimer = new Timer(true);
				ReconnectTask task = new ReconnectTask();
				reconnectTimer.schedule(task, 0,RECONNECT_CHECKING_PERIOD);
				wifiCheckHandler.obtainMessage(RECONNECT_CAMERA).sendToTarget();
//				reconnectTimer = new Timer(true);
//				ReconnectTask task = new ReconnectTask();
//				reconnectTimer.schedule(task, 10000,RECONNECT_CHECKING_PERIOD);
//				wifiCheckHandler.obtainMessage(RECONNECT_CAMERA).sendToTarget();
			}
		});
		if (dialog == null) {
			dialog = builder.create();
			dialog.setCancelable(false);
			Log.e("tigertiger", "start dialog.show()");
			dialog.show();
			Log.e("tigertiger", "end dialog.show()");
		}
	}

	// 打开WIFI
	public void openWifi() {
		if (!mWifiManager.isWifiEnabled()) {
			mWifiManager.setWifiEnabled(true);
		}
	}

	// 关闭WIFI
	public void closeWifi() {
		if (mWifiManager.isWifiEnabled()) {
			mWifiManager.setWifiEnabled(false);
		}
	}

	// 检查当前WIFI状态
	public int checkState() {
		return mWifiManager.getWifiState();
	}

	// 锁定WifiLock
	public void acquireWifiLock() {
		mWifiLock.acquire();
	}

	// 解锁WifiLock
	public void releaseWifiLock() {
		// 判断时候锁定
		if (mWifiLock.isHeld()) {
			mWifiLock.acquire();
		}
	}

	// 创建一个WifiLock
	public void creatWifiLock() {
		mWifiLock = mWifiManager.createWifiLock("Test");
	}

	// 得到配置好的网络
	public List<WifiConfiguration> getConfiguration() {
		return mWifiConfiguration;
	}

	// 指定配置好的网络进行连接
	public void connectConfiguration(int index) {
		// 索引大于配置好的网络索引返回
		if (index > mWifiConfiguration.size()) {
			return;
		}
		// 连接配置好的指定ID的网络
		mWifiManager.enableNetwork(mWifiConfiguration.get(index).networkId, true);
	}

	public void startScan() {
		mWifiManager.startScan();
		// 得到扫描结果
		mWifiList = mWifiManager.getScanResults();
		// 得到配置好的网络连接
		mWifiConfiguration = mWifiManager.getConfiguredNetworks();
	}

	// 得到网络列表
//	public List<ScanResult> getWifiList() {
//		return mWifiList;
//	}

	// 查看扫描结果
	public StringBuilder lookUpScan() {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < mWifiList.size(); i++) {
			stringBuilder.append("Index_" + new Integer(i + 1).toString() + ":");
			// 将ScanResult信息转换成一个字符串包
			// 其中把包括：BSSID、SSID、capabilities、frequency、level
			stringBuilder.append((mWifiList.get(i)).toString());
			stringBuilder.append("/n");
		}
		return stringBuilder;
	}

	// 得到MAC地址
	public String getMacAddress() {
		return (mWifiInfo == null) ? "NULL" : mWifiInfo.getMacAddress();
	}

	// 得到接入点的BSSID
	public String getBSSID() {
		return (mWifiInfo == null) ? "NULL" : mWifiInfo.getBSSID();
	}

	// 得到IP地址
	public int getIPAddress() {
		return (mWifiInfo == null) ? 0 : mWifiInfo.getIpAddress();
	}

	// 得到连接的ID
	public int getNetworkId() {
		return (mWifiInfo == null) ? 0 : mWifiInfo.getNetworkId();
	}

	// 得到WifiInfo的所有信息包
	public String getWifiInfo() {
		return (mWifiInfo == null) ? "NULL" : mWifiInfo.toString();
	}

	// 添加一个网络并连接
	public void addNetwork(WifiConfiguration wcg) {
		int wcgID = mWifiManager.addNetwork(wcg);
		boolean b = mWifiManager.enableNetwork(wcgID, true);
		System.out.println("a--" + wcgID);
		System.out.println("b--" + b);
	}

	// 断开指定ID的网络
	public void disconnectWifi(int netId) {
		mWifiManager.disableNetwork(netId);
		mWifiManager.disconnect();
	}

	// wifi connection is ok
	public boolean isWifiConnect(Context context) {
		WriteLogToDevice.writeLog("[Normal] -- WifiCheck: ", "isWifiConnect?");
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mWiFiNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (mWiFiNetworkInfo != null) {
				WifiManager mWifi = (WifiManager) GlobalApp.getInstance().getCurrentApp().getSystemService(Context.WIFI_SERVICE);
				WifiInfo wifiInfo = mWifi.getConnectionInfo();
				//wifiCheck = new WifiCheck();
				//int temp = wifiInfo.getIpAddress();
				if(wifiInfo.getIpAddress() != 0 ){
					WriteLogToDevice.writeLog("[Normal] -- AppStart: ", "isWifiConnect? true!");
					return mWiFiNetworkInfo.isAvailable();
				}
				
			}
		}
		WriteLogToDevice.writeLog("[Normal] -- AppStart: ", "isWifiConnect? false");
		return false;
	}
	
	public boolean isWifiConnected(Context context,String nameFilter) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mWiFiNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (mWiFiNetworkInfo != null) {
				WifiManager mWifi = (WifiManager) GlobalApp.getInstance().getCurrentApp().getSystemService(Context.WIFI_SERVICE);
				WifiInfo wifiInfo = mWifi.getConnectionInfo();
				//wifiCheck = new WifiCheck();
				//int temp = wifiInfo.getIpAddress();
				Log.e("tigertiger","wifiInfo.getIpAddress(); =="+wifiInfo.getIpAddress());
				Log.d("tigertiger","unknow sucesss........ wifiInfo.getSSID() =="+ wifiInfo.getSSID());
				Log.d("tigertiger","unknow sucesss........ nameFilter =="+ nameFilter);
				if(wifiInfo.getIpAddress() != 0 && wifiInfo.getSSID().contains(nameFilter) == true){
					Log.d("tigertiger","sucesss........ wifiInfo.getSSID() =="+ wifiInfo.getSSID());
					return mWiFiNetworkInfo.isAvailable();
				}
				
			}
		}

		return false;
	} 

	 public static boolean isWifiContected(Context context){
         ConnectivityManager connectivityManager
              = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
          NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
             if(wifiNetworkInfo.isConnected())
             {
                     
                     Log.i(Thread.currentThread().getName(), "isWifiContected");
                 return true ;
             }
                 Log.i(Thread.currentThread().getName(), "isWifiDisconnected");
             return false ;

 }
	
	public int getSecurity(ScanResult result) {
		if (result.capabilities.contains("WEP")) {
			return WIFICIPHER_WEP;
		} else if (result.capabilities.contains("WPA")) {
			return WIFICIPHER_WAP;
		} else {
			return WIFICIPHER_NOPASS;
		}
		// return 1;
	}

	public WifiConfiguration CreateWifiInfo(String SSID, String Password, int Type) {
		WifiConfiguration config = new WifiConfiguration();
		config.allowedAuthAlgorithms.clear();
		config.allowedGroupCiphers.clear();
		config.allowedKeyManagement.clear();
		config.allowedPairwiseCiphers.clear();
		config.allowedProtocols.clear();
		config.SSID = "\"" + SSID + "\"";
		if (Type == WIFICIPHER_NOPASS) // Data.WIFICIPHER_NOPASS
		{
			config.wepKeys[0] = "";
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			config.wepTxKeyIndex = 0;
		}
		if (Type == WIFICIPHER_WEP) // Data.WIFICIPHER_WEP
		{
			config.hiddenSSID = true;
			config.wepKeys[0] = "\"" + Password + "\"";
			config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			config.wepTxKeyIndex = 0;
		}
		if (Type == WIFICIPHER_WAP) // Data.WIFICIPHER_WPA
		{
			config.preSharedKey = "\"" + Password + "\"";
			config.hiddenSSID = true;
			config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
			config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
			config.status = WifiConfiguration.Status.ENABLED;
		}
		return config;
	}

	public void connectWifi(String SSID, String Password, int Type) {
		WifiManager wifiManager = (WifiManager) globalApp.getCurrentApp().getSystemService(Context.WIFI_SERVICE);
		WifiConfiguration config = CreateWifiInfo(SSID, Password, Type);
		int netID = wifiManager.addNetwork(config);
		boolean bRet = wifiManager.enableNetwork(netID, true);
		Log.d("tigertiger", "-----------bRet =" + bRet);
	}

	// 分为三种情况：1没有密码2用wep加密3用wpa加密
	/**
	 * 
	 * Added by zhangyanhu C01012,2014-4-4
	 */
	public void wifiListDialog() {
		wifiManager = (WifiManager) globalApp.getCurrentApp().getSystemService(Context.WIFI_SERVICE);
		// openWifi();
		list = wifiManager.getScanResults();
		Log.d("tiger", "list.size(): " + list.size());
		for (int ii = 0; ii < list.size();) {
			Log.d("tiger", "wifiManager.calculateSignalLevel(list.get(ii).level,4): " + wifiManager.calculateSignalLevel(list.get(ii).level, 4));
			if (list.get(ii).SSID.equals("") == true) {
				list.remove(ii);
				continue;
			} else {
				ii++;
			}
		}
		Log.d("tiger", "list.size(): " + list.size());
		final Builder builder = new AlertDialog.Builder(globalApp.getCurrentApp());
		WifiListAdapter wifiListAdapter = new WifiListAdapter(globalApp.getCurrentApp(), list);
		builder.setTitle("Please select wifi:");
		builder.setCancelable(false);
		builder.setAdapter(wifiListAdapter, new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				passwordDialog(arg1);
			}

		});
		builder.create().show();
	}

	/**
	 * 
	 * Added by zhangyanhu C01012,2014-4-9
	 */
	public void passwordDialog(int arg2) {
		final int position = arg2;
		final Builder builder = new AlertDialog.Builder(globalApp.getCurrentApp());
		builder.setCancelable(false);
		builder.setTitle("Please input the password:");
		RelativeLayout passwordLayout = (RelativeLayout) (LayoutInflater.from(globalApp.getCurrentApp()).inflate(R.layout.input_password, null));
		final EditText password = (EditText) passwordLayout.findViewById(R.id.password);
		builder.setView(passwordLayout);
		builder.setPositiveButton("Submit", new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				InputMethodManager imm = (InputMethodManager) globalApp.getCurrentApp().getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(password.getWindowToken(), 0);

				String temp = "";
				temp = password.getText().toString();
				connectWifi(list.get(position).SSID, temp, getSecurity(list.get(position)));

				connectTimer = new Timer(true);
				reconnectTime = 0;
				TimerTask task = new TimerTask() {

					@Override
					public void run() {
//						if (isBackground(globalApp.getCurrentApp()) == true) {
//							wifiCheckHandler.obtainMessage(IN_BACKGROUND).sendToTarget();
//						}
						if (isWifiConnect(GlobalApp.getInstance().getCurrentApp()) == true) {
							wifiCheckHandler.obtainMessage(RECONNECT_SUCCESS).sendToTarget();
							Log.d("tigertiger", "isWifiConnect() == true");
							// isShowed = true;
						} else if (reconnectTime > 5) {
							wifiCheckHandler.obtainMessage(RECONNECT_FAILED).sendToTarget();
						}
						reconnectTime++;
					}
				};
				connectTimer.schedule(task, 0, 2000);
				progressDialog = new ProgressDialog(globalApp.getCurrentApp());
				progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				progressDialog.setMessage("connecting Wifi");
				progressDialog.setCancelable(false);
				progressDialog.show();
			}
		});
		builder.setNegativeButton("Cancel", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			}
		});
		builder.create().show();
	}
	
	private class ReconnectTask extends TimerTask{

		/* (non-Javadoc)
		 * @see java.util.TimerTask#run()
		 */
		@Override
		public void run() {
			// TODO Auto-generated method stub
			// 取得WifiManager对象
			mWifiManager = (WifiManager) (globalApp.getCurrentApp()).getSystemService(Context.WIFI_SERVICE);
			// 取得WifiInfo对象
			mWifiInfo = mWifiManager.getConnectionInfo();
			mWifiInfo.getSSID();
			Log.e("tigertiger","reconnect mWifiInfo.getSSID(); =="+mWifiInfo.getSSID());
			if(mWifiInfo.getSSID().equals(globalApp.getSsid())){
				if(reconnectTimer != null){
					reconnectTimer.cancel();
				}
				SDKSession.destroySession();				
//				try {
//					Thread.currentThread().sleep(RECONNECT_WAITING);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				TimerTask task = new TimerTask() {

					@Override
					public void run() {
						if(reconnectDialog != null){
							reconnectDialog.dismiss();
						}
						cancelConnectCheck();
						ExitApp.getInstance().finishAllActivity();
						// ICOM-1890 Start delete by b.jiang 2015-09-01
						/*
						Intent intent = new Intent();
						intent.setClass(globalApp.getCurrentApp(), AppStartToMainActivity.class);
						//intent.setClass(globalApp.getCurrentApp(), AppStartOldActivity.class);
						globalApp.getCurrentApp().startActivity(intent);
						*/
						// ICOM-1890 End delete by b.jiang 2015-09-01
					}
				};
				
			Timer tempTimer = new Timer(true);	
			tempTimer.schedule(task, RECONNECT_WAITING);
			}

		}
		
	}
	
	public List<ScanResult> getWifiListByFilter(String nameFilter){
		WifiManager wifiManager = (WifiManager) globalApp.getCurrentApp().getSystemService(Context.WIFI_SERVICE);
		List<ScanResult> wifiList = wifiManager.getScanResults();
		List<ScanResult> tempList = new LinkedList<ScanResult>();
		for(int ii = 0; ii < wifiList.size();ii++){
			if((wifiList.get(ii).SSID).contains(nameFilter) == false){
				tempList.add(wifiList.get(ii));
				Log.e("tigertiger", "****************current wifi ssid="+wifiList.get(ii).SSID);
				//ii--;
			}
		}
		wifiList.removeAll(tempList);
		for(int ii =0;ii < wifiList.size();ii++){
			Log.e("tigertiger", "-------------current wifi ssid="+wifiList.get(ii).SSID);
		}
		return wifiList;
	}
	
	public List<ScanResult> getWifiList(){
		WifiManager wifiManager = (WifiManager) globalApp.getCurrentApp().getSystemService(Context.WIFI_SERVICE);
		List<ScanResult> wifiList = wifiManager.getScanResults();
	/*	List<ScanResult> tempList = new LinkedList<ScanResult>();
		for (int ii = 0; ii < wifiList.size(); ii++) {
			tempList.add(wifiList.get(ii));
			Log.e("tigertiger", "****************current wifi ssid=" + wifiList.get(ii).SSID);
			// ii--;
		}
		wifiList.removeAll(tempList);
		for (int ii = 0; ii < wifiList.size(); ii++) {
			Log.e("tigertiger", "-------------current wifi ssid=" + wifiList.get(ii).SSID);
		}*/
		return wifiList;
	}
}
