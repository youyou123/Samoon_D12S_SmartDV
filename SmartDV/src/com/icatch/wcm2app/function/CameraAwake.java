/**
 * Added by zhangyanhu C01012,2014-6-16
 */
package com.icatch.wcm2app.function;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.icatch.wcm2app.adapter.CameraAwakeAdapter;
import com.icatch.wcm2app.common.GlobalApp;
import com.icatch.wcm2app.controller.sdkApi.CameraAction;
import com.icatch.wcm2app.controller.sdkApi.SDKSession;
import com.icatch.wcm2app.R;

/**
 * Added by zhangyanhu C01012,2014-6-16
 */
public class CameraAwake {
	private HashMap<String, String> ssidAndMac = new HashMap<String, String>();
	private ArrayList<String> ssidList = new ArrayList<String>();
	private Context context;
	protected Toast toast;
	private CameraAwakeAdapter cameraAwakeAdapter;
	private CameraAction cameraAction = new CameraAction();

	public CameraAwake(Context context) {
		this.context = context;
		readSsidList();
	}

	public void showCameraAwakeDialog() {
		Builder awakeDialog = new AlertDialog.Builder(context);
		//awakeDialog.setCancelable(false);
		awakeDialog.setTitle(R.string.camera_awake);
		cameraAwakeAdapter = new CameraAwakeAdapter(context, ssidList, awakeHandler);
		awakeDialog.setAdapter(cameraAwakeAdapter, null);
		awakeDialog.setPositiveButton(R.string.dialog_delete_all, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				ssidList.clear();
				ssidAndMac.clear();
				saveChangesToFile(ssidList, ssidAndMac);
			}
		});
		awakeDialog.setNegativeButton(R.string.dialog_awake_all, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.e("tigertiger","ssidList.isEmpty() ="+ssidList.isEmpty());
				if(ssidList.isEmpty() == false){
					Log.e("tigertiger","唤醒全部。。。。。");
					for(int ii = 0;ii < ssidList.size();ii++){
						//if(globalView.wakeUpCamera(ssidAndMac.get(ssidList.get(ii)))){
							awakeHandler.obtainMessage(GlobalApp.MESSAGE_AWAKE_ONE_CAMERA, ssidList.get(ii)).sendToTarget();
						//}
					}
				}else{
					return;
				}
			}
		});
		//AlertDialog dialog = builder.create();
		awakeDialog.create().show();
	}

	public void saveSsid() {
		try {
			WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			
			Log.e("tigertiger","ssidAndMac ="+ssidAndMac.size());
			if (ssidAndMac.containsKey(wifiManager.getConnectionInfo().getSSID())) {
				return;
			}
			FileOutputStream outStream = context.openFileOutput("a.txt", Context.MODE_APPEND);
			String ssid = wifiManager.getConnectionInfo().getSSID() + ";";
			//String macAddress = wifiManager.getConnectionInfo().getBSSID() + ";";
			String macAddress = cameraAction.getCameraMacAddress()+ ";";
			Log.e("tigertiger","macAddress ="+macAddress);
			try {
				outStream.write(ssid.getBytes());
				outStream.write(macAddress.getBytes());
				outStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// outStream.
	}

	public void readSsidList() {
		// outStream.
		FileInputStream inStream = null;
		try {
			// inStream = new FileInputStream("a.txt");
			inStream = context.openFileInput("a.txt");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(inStream == null){
			return;
		}
		try {
			if (inStream.available() <= 0) {
				return;
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
			return;
		}
		String str2 = new String(b);// 再将字节数组中的内容转化成字符串形式输出
		if(str2 == null){
			return;
		}
		String newStr = str2.replaceAll("\"","");
		String[] temp = newStr.split(";");
		if(temp.length%2 != 0 ){
			return;
		}
		//Log.d("tigertiger", "-----------newStr ="+newStr);
		for (int ii = 0; ii < temp.length; ii = ii + 2) {
			//Log.e("tigertiger", "temp[ii] = " + temp[ii]);
			if(ssidAndMac.containsKey(temp[ii]) == false){
				ssidList.add(temp[ii]);	
			}
			ssidAndMac.put(temp[ii], temp[ii + 1]);
		}
		//for test
/*		ssidList.add("forTest");
		ssidAndMac.put("forTest", "00:16:D3:46:31:E2");*/
	}

	public ArrayList<String> getCameraName() {
		return ssidList;
	}

	public HashMap<String, String> getCameraSsidAndMac() {
		return ssidAndMac;
	}

	public void saveChangesToFile(ArrayList<String> tempssidList, HashMap<String, String> tempssidMacMap) {
		Log.d("tigertiger", "saveChangesToFile");
		FileOutputStream outStream = null;
		try {
			outStream = context.openFileOutput("a.txt", Context.MODE_PRIVATE);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(tempssidList.isEmpty()){
			String cleanStr = ""; 
			try {
				outStream.write(cleanStr.getBytes());
				outStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}
		//Log.d("tigertiger", "tempssidList.size() =" + tempssidList.size());
		for (int ii = 0; ii < tempssidList.size(); ii++) {
			//Log.d("tigertiger", "tempssidList =" + tempssidList.get(ii));
			String ssid = tempssidList.get(ii) + ";";
			String macAddress = tempssidMacMap.get(ssid) + ";";

			try {
				outStream.write(ssid.getBytes());
				outStream.write(macAddress.getBytes());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			outStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private final Handler awakeHandler = new Handler() {
		public void handleMessage(Message msg) {
			Log.e("AppStart", "what =  " + msg.what);
			switch (msg.what) {
			case GlobalApp.MESSAGE_AWAKE_ONE_CAMERA:
				Log.d("tigertiger","receive AWAKE_ONE_CAMERA");
				String tempSsid = (String)msg.obj;
				//String temp;
				if(SDKSession.wakeUpCamera(ssidAndMac.get(tempSsid))){
					//temp = R.string.camera_awake_success;
					if(toast == null){
						toast = Toast.makeText(context,context.getResources().getString(R.string.camera_awake_success)+":"+tempSsid,Toast.LENGTH_SHORT);
					}else{
						toast.setText(context.getResources().getString(R.string.camera_awake_success)+":"+tempSsid);
						toast.setDuration(Toast.LENGTH_SHORT);
					}
					toast.show();
				}else{
					if(toast == null){
						toast = Toast.makeText(context,context.getResources().getString(R.string.camera_awake_failed)+":"+tempSsid,Toast.LENGTH_SHORT);
					}else{
						toast.setText(context.getResources().getString(R.string.camera_awake_failed)+":"+tempSsid);
						toast.setDuration(Toast.LENGTH_SHORT);
					}
					toast.show();
				}
				break;
			case GlobalApp.MESSAGE_REMOVE_CAMERA:
				//if()
				String ssid = (String)msg.obj;
				ssidList.remove(ssid);
				ssidAndMac.remove(ssid);
				cameraAwakeAdapter.notifyDataSetChanged();
				saveChangesToFile(ssidList, ssidAndMac);		
				break;
			}
		};
	};
}