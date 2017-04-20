/**
 * Added by zhangyanhu C01012,2014-6-25
 */
package com.icatch.wcm2app.function;

import java.lang.reflect.Field;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.icatch.wcm2app.Data.AppReflectToUI;
import com.icatch.wcm2app.Data.SDKReflectToUI;
import com.icatch.wcm2app.Data.UIInfo;
import com.icatch.wcm2app.Data.UIReflectToApp;
import com.icatch.wcm2app.Data.UIReflectToSDK;
import com.icatch.wcm2app.adapter.OptionListAdapter;
import com.icatch.wcm2app.common.AppProperties;
import com.icatch.wcm2app.common.GlobalApp;
import com.icatch.wcm2app.common.WriteLogToDevice;
import com.icatch.wcm2app.controller.Reflection;
import com.icatch.wcm2app.controller.UIDisplayResource;
import com.icatch.wcm2app.controller.sdkApi.AppCfgParameter;
import com.icatch.wcm2app.controller.sdkApi.CameraAction;
import com.icatch.wcm2app.controller.sdkApi.CameraProperties;
import com.icatch.wcm2app.globalValue.TimeLapseInterval;
import com.icatch.wcm2app.globalValue.TimeLapseMode;
import com.icatch.wcm2app.R;



/**
 * Added by zhangyanhu C01012,2014-6-25
 */
public class SettingView {
	public static final int SETTING_OPTION_BURST = 0;
	public static final int SETTING_OPTION_WHITE_BALANCE = 1;
	public static final int SETTING_OPTION_ELETRICITY_FREQUENCY = 2;
	public static final int SETTING_OPTION_DATE_STAMP = 3;
	public static final int SETTING_OPTION_IMAGE_SIZE = 4;
	public static final int SETTING_OPTION_VIDEO_SIZE = 5;
	public static final int SETTING_OPTION_FORAMT = 6;
	public static final int SETTING_OPTION_CAMERA_AWAKE = 7;
	public static final int SETTING_OPTION_CAMERA_SLEEP = 8;
	public static final int SETTING_OPTION_DELAY_TIME = 9;
	public static final int SETTING_OPTION_TIME_LAPSE_INTERVAL = 11;
	public static final int SETTING_OPTION_TIME_LAPSE_DURATION = 12;
	public static final int SETTING_OPTION_TIME_LAPSE_MODE = 13;
	public static final int SETTING_OPTION_SLOW_MOTION = 14;
	public static final int SETTING_OPTION_UPSIDE = 15;
	public static final int SETTING_OPTION_CAMERA_CONFIGURATION = 16;
	// ==============add menu
	public static final int SETTING_OPTION_VIDEO_MIC = 17;
	public static final int SETTING_OPTION_VIDEO_STAMP = 18;
	public static final int SETTING_OPTION_LOGO_STAMP = 19;
	public static final int SETTING_OPTION_CARNUM_STAMP = 20;
	public static final int SETTING_OPTION_CAR_NUM = 21;
	public static final int SETTING_OPTION_STA_WIFI_SSID_PWD = 22;  

	// add===============menu

	public static final int CAPTURE_SETTING_MENU = 1;
	public static final int VIDEO_SETTING_MENU = 2;
	public static final int TIMELAPSE_SETTING_MENU = 3;
	// public static final int TIMELAPSE_SETTING_MENU = 4;

	public static final int REQUEST_FORMAT_SD_CARD = 1;

	private int type;
	private ListView settingView;
	private Context context;
	private UIDisplayResource uiDisplayResource = UIDisplayResource
			.getinstance();
	private SettingHandler settingHandler = new SettingHandler();
	private OptionListAdapter optionListAdapter = null;
	private CameraProperties cameraProperties = new CameraProperties();
	private CameraAction cameraAction = new CameraAction();
	private AppCfgParameter appCfgParameter = new AppCfgParameter();
	private AlertDialog optionDialog = null;
	private Resources res = null;
	private Reflection reflection = new Reflection();
	private Handler previewHandler;
	private ArrayList<Integer> settingNameList = null;
	private ArrayList<String> settingValueList = null;
	private AlertDialog dialog;
	private MyHander handler = new MyHander();
	ProgressDialog progressDialog;
	private Reflection refection = new Reflection();

	public SettingView(Context context, ListView settingView, int type,
			Handler previewHandler) {
		this.settingView = settingView;
		this.context = context;
		this.type = type;
		this.previewHandler = previewHandler;
		res = context.getResources();
	}

	public SettingView(Context context, Handler previewHandler) {
		this.context = context;
		this.previewHandler = previewHandler;
		res = context.getResources();
	}

	private class MyHander extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GlobalApp.MESSAGE_FORMAT_SD_START:
				progressDialog = new ProgressDialog(context);
				progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				progressDialog.setMessage(context
						.getString(R.string.setting_format));
				progressDialog.setCancelable(false);
				progressDialog.show();
				// if (cameraAction.formatStorage()) {
				// Toast.makeText(context, R.string.setting_formatted,
				// Toast.LENGTH_SHORT).show();
				// //previewHandler.obtainMessage(GlobalApp.MESSAGE_UPDATE_UI_FORMAT_SUCCESS).sendToTarget();
				// } else {
				// Toast.makeText(context, R.string.dialog_failed,
				// Toast.LENGTH_SHORT).show();
				// //previewHandler.obtainMessage(GlobalApp.MESSAGE_UPDATE_UI_FORMAT_FAILED).sendToTarget();
				// }
				// progressDialog.dismiss();
				break;
			case GlobalApp.MESSAGE_FORMAT_SUCCESS:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				Toast.makeText(context, R.string.setting_formatted,
						Toast.LENGTH_SHORT).show();
				break;
			case GlobalApp.MESSAGE_FORMAT_FAILED:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				Toast.makeText(context, R.string.dialog_failed,
						Toast.LENGTH_SHORT).show();
				break;
			}
		}
	}

	public void showSettingMainMenu() {
		switch (type) {
		case CAPTURE_SETTING_MENU:
			settingNameList = uiDisplayResource.getSettingNameCaptureMode();
			settingValueList = uiDisplayResource.getSettingValueCaptureMode();
			break;
		case VIDEO_SETTING_MENU:
			settingNameList = uiDisplayResource.getSettingNameVideoMode();
			settingValueList = uiDisplayResource.getSettingValueVideoMode();
			break;
		case TIMELAPSE_SETTING_MENU:
			settingNameList = uiDisplayResource.getsettingNameTimeLapseMode();
			settingValueList = uiDisplayResource.getSettingValueTimeLapseMode();
			Log.d("tigertiger",
					"settingValueList.size =" + settingValueList.size());
			break;
		// case TIMELAPSE_CAPTURE_SETTING_MENU:
		// settingNameList = uiDisplayResource.getsettingNameTimeLapseMode();
		// settingValueList = uiDisplayResource.getSettingValueTimelapseMode();
		// break;
		}
		optionListAdapter = new OptionListAdapter(context, settingHandler,
				settingNameList, settingValueList);
		if (optionListAdapter != null) {
			settingView.setAdapter(optionListAdapter);
		}
		settingView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				switch (settingNameList.get(arg2)) {
				case R.string.setting_burst:
					settingHandler.obtainMessage(
							SettingView.SETTING_OPTION_BURST).sendToTarget();
					break;
				case R.string.setting_awb:
					settingHandler.obtainMessage(
							SettingView.SETTING_OPTION_WHITE_BALANCE)
							.sendToTarget();
					break;
				case R.string.setting_power_supply:
					settingHandler.obtainMessage(
							SettingView.SETTING_OPTION_ELETRICITY_FREQUENCY)
							.sendToTarget();
					break;
				case R.string.setting_datestamp:
					settingHandler.obtainMessage(
							SettingView.SETTING_OPTION_DATE_STAMP)
							.sendToTarget();
					break;
				case R.string.setting_format:
					settingHandler.obtainMessage(
							SettingView.SETTING_OPTION_FORAMT).sendToTarget();
					break;
				case R.string.setting_time_lapse_interval:
					settingHandler.obtainMessage(
							SettingView.SETTING_OPTION_TIME_LAPSE_INTERVAL)
							.sendToTarget();
					break;
				case R.string.setting_time_lapse_duration:
					settingHandler.obtainMessage(
							SettingView.SETTING_OPTION_TIME_LAPSE_DURATION)
							.sendToTarget();
					break;
				case R.string.timeLapse_mode:
					settingHandler.obtainMessage(
							SettingView.SETTING_OPTION_TIME_LAPSE_MODE)
							.sendToTarget();
					break;
				case R.string.slowmotion:
					settingHandler.obtainMessage(
							SettingView.SETTING_OPTION_SLOW_MOTION)
							.sendToTarget();
					break;
				case R.string.upside:
					settingHandler.obtainMessage(
							SettingView.SETTING_OPTION_UPSIDE).sendToTarget();
					break;
				case R.string.camera_wifi_configuration:
					settingHandler.obtainMessage(
							SettingView.SETTING_OPTION_CAMERA_CONFIGURATION)
							.sendToTarget();
					break;

				case R.string.setting_video_mic:
					settingHandler.obtainMessage(
							SettingView.SETTING_OPTION_VIDEO_MIC)
							.sendToTarget();
					break;

				case R.string.setting_video_stamp:
					settingHandler.obtainMessage(
							SettingView.SETTING_OPTION_VIDEO_STAMP)
							.sendToTarget();
					break;
				case R.string.setting_logo_stamp:
					settingHandler.obtainMessage(
							SettingView.SETTING_OPTION_LOGO_STAMP)
							.sendToTarget();
					break;
				case R.string.setting_carnum_stamp:
					settingHandler.obtainMessage(
							SettingView.SETTING_OPTION_CARNUM_STAMP)
							.sendToTarget();
					break;

				case R.string.setting_car_number:
					settingHandler.obtainMessage(
							SettingView.SETTING_OPTION_CAR_NUM).sendToTarget();
					break;
				case R.string.setting_sta_wifi_ssid_pwd: 																
					settingHandler.obtainMessage(SettingView.SETTING_OPTION_STA_WIFI_SSID_PWD).sendToTarget();
					break;
				}
			}
		});
	}

	public class SettingHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			showSettingDialog(msg.what);
			// super.handleMessage(msg);
		}

	}

	public void showSettingDialog(int type) {
		WriteLogToDevice.writeLog("[Normal] -- SettingView: ",
				"showSettingDialog type=" + type);
		switch (type) {
		case SETTING_OPTION_BURST:
			showBurstOptionDialog();
			break;
		case SETTING_OPTION_WHITE_BALANCE:
			showWhiteBalanceOptionDialog();
			break;
		case SETTING_OPTION_ELETRICITY_FREQUENCY:
			showElectricityFrequencyOptionDialog();
			break;
		case SETTING_OPTION_DATE_STAMP:
			showDateStampOptionDialog();
			break;
		case SETTING_OPTION_IMAGE_SIZE:
			showImageSizeOptionDialog();
			break;
		case SETTING_OPTION_VIDEO_SIZE:
			showVideoSizeOptionDialog();
			break;
		case SETTING_OPTION_DELAY_TIME:
			showDelayTimeOptionDialog();
			break;
		case SETTING_OPTION_FORAMT:
			if (cameraProperties.isSDCardExist() == false) {
				sdCardIsNotReadyAlert();
				break;
			}
			showFormatConfirmDialog();
			break;
		case SETTING_OPTION_TIME_LAPSE_INTERVAL:
			showTimeLapseIntervalDialog();
			break;
		case SETTING_OPTION_TIME_LAPSE_DURATION:
			showTimeLapseDurationDialog();
			break;
		case SETTING_OPTION_TIME_LAPSE_MODE:
			showTimeLapseModeDialog();
			break;
		case SETTING_OPTION_SLOW_MOTION:
			showSlowMotionDialog();
			break;
		case SETTING_OPTION_UPSIDE:
			showUpsideDialog();
			break;
		case SETTING_OPTION_CAMERA_CONFIGURATION:
			showCameraConfigurationDialog();
			break;

		case SETTING_OPTION_VIDEO_MIC:
			showVideoMicDialog();
			break;
		case SETTING_OPTION_VIDEO_STAMP:

			showVideoStampDialog();
			break;
		case SETTING_OPTION_LOGO_STAMP:
			showLogoStampDialog();
			break;
		case SETTING_OPTION_CARNUM_STAMP:
			showCarNumStampDialog();
			break;
		case SETTING_OPTION_CAR_NUM:
			showCarNumDialog();
			break;
		case SETTING_OPTION_STA_WIFI_SSID_PWD:	
			showStaWiFiSSIDPwdDialog();
			break;
		}
	}

	/**
	 * 
	 */
	private void showStaWiFiSSIDPwdDialog() {
		LayoutInflater factory = LayoutInflater.from(context);
        final View textEntryView = factory.inflate(R.layout.alert_dialog_sta_ssid_pwd, null);
        AlertDialog.Builder builder =  new AlertDialog.Builder(context);
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setTitle(R.string.setting_sta_wifi_ssid_pwd);
        builder.setView(textEntryView);
        builder.setPositiveButton(R.string.setting_ok, new DialogInterface.OnClickListener() {
			            @Override  
			            public void onClick(DialogInterface arg0, int arg1) { 
			            	EditText sta_ssid = (EditText)textEntryView.findViewById(R.id.sta_ssid_edit);
			            	EditText sta_pwd = (EditText)textEntryView.findViewById(R.id.sta_pwd_edit);
			            	String strSSID = sta_ssid.getText().toString();
			            	String strPwd = sta_pwd.getText().toString();
			            	Log.d("showStaWiFiSSIDDialog", "STA_SSID="+strSSID+"length="+strSSID.length());
			            	Log.d("showStaWiFiSSIDDialog", "STA_PWD="+strPwd+"length="+strPwd.length());
			            	if((strSSID.length() >= 1 && strSSID.length()<=18)&&(strPwd.length() >= 8 && strPwd.length()<=16)){        		
	                    		Toast toast = Toast.makeText(context,R.string.stream_set_complete, Toast.LENGTH_SHORT);
	                    		toast.setGravity(Gravity.CENTER, 0, 0);
	                    		toast.show();
	                    		cameraProperties.setStaWiFiSSID(strSSID);
	                    		cameraProperties.setStaWiFiPwd(strPwd);
		        				arg0.dismiss();
		        				settingValueList = getSettingValue();
		        				if (optionListAdapter == null) {
		        					return;
		        				}
		        				optionListAdapter.notifyDataSetChanged();
	                    	}else {
	                    		Toast toast = Toast.makeText(context,R.string.stream_set_error,Toast.LENGTH_SHORT);
	                    		toast.setGravity(Gravity.CENTER, 0, 0);
	                    		toast.show();
							}
			            }  
			        })
		.setNegativeButton(R.string.setting_cancel, null).show();
	}
	private void showCarNumDialog() {
		LayoutInflater factory = LayoutInflater.from(context);
		final View textEntryView = factory.inflate(
				R.layout.alert_dialog_carnum, null);
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setTitle(R.string.setting_car_number);
		builder.setView(textEntryView);
		builder.setPositiveButton(R.string.setting_ok,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						EditText carnum = (EditText) textEntryView
								.findViewById(R.id.carnum_edit);
						String strCarNum = carnum.getText().toString();
						Log.i("showWiFiPwdDialog", "pwd=" + strCarNum
								+ "length=" + strCarNum.length());
						if (strCarNum.length() <= 15) {
							cameraProperties.setCarNum(strCarNum);
							Toast toast = Toast.makeText(context,
									R.string.stream_set_complete,
									Toast.LENGTH_SHORT);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
							arg0.dismiss();
							settingValueList = getSettingValue();
							if (optionListAdapter == null) {
								return;
							}
							optionListAdapter.notifyDataSetChanged();
						} else {
							Toast toast = Toast.makeText(context,
									R.string.stream_set_error,
									Toast.LENGTH_SHORT);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
						}
					}
				}).setNegativeButton(R.string.setting_cancel, null).show();
	}

	private void showCarNumStampDialog() {
		CharSequence title = res.getString(R.string.setting_carnum_stamp);

		final String[] carNumStampUIString = uiDisplayResource
				.getCarNumStampUIString();
		if (carNumStampUIString == null) {
			return;
		}
		int length = carNumStampUIString.length;
		int curIdx = 0;
		UIInfo uiInfo = refection.refecltFromSDKToUI(
				SDKReflectToUI.SETTING_UI_CARNUM_STAMP,
				cameraProperties.getCurrentCarNumStamp());
		// Log.i("showDstModeDialog",
		// "====================cameraProperties.getCurrentCarNumStamp()="+cameraProperties.getCurrentCarNumStamp());
		for (int i = 0; i < length; i++) {
			if (carNumStampUIString[i].equals(uiInfo.uiStringInSetting)) {
				curIdx = i;
			}
		}

		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// Log.i("showDstModeDialog", "====================arg1="+arg1);
				cameraProperties.setCarNumStamp(arg1);
				arg0.dismiss();
				settingValueList = getSettingValue();
				if (optionListAdapter == null) {
					return;
				}
				optionListAdapter.notifyDataSetChanged();
			}
		};
		showOptionDialog(title, carNumStampUIString, curIdx, listener, true);
	}

	private void showLogoStampDialog() {
		CharSequence title = res.getString(R.string.setting_logo_stamp);

		final String[] logoStampUIString = uiDisplayResource
				.getLogoStampUIString();
		if (logoStampUIString == null) {
			Log.i("showLogoStampDialog",
					"====================gSensorZUIString == null");
			return;
		}
		int length = logoStampUIString.length;
		int curIdx = 0;
		UIInfo uiInfo = refection.refecltFromSDKToUI(
				SDKReflectToUI.SETTING_UI_LOGO_STAMP,
				cameraProperties.getCurrentLogoStamp());
		// Log.i("showDstModeDialog",
		// "====================cameraProperties.getCurrentLogoStamp()="+cameraProperties.getCurrentLogoStamp());
		for (int i = 0; i < length; i++) {
			if (logoStampUIString[i].equals(uiInfo.uiStringInSetting)) {
				curIdx = i;
			}
		}

		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				cameraProperties.setLogoStamp(arg1);
				arg0.dismiss();
				settingValueList = getSettingValue();
				if (optionListAdapter == null) {
					return;
				}
				optionListAdapter.notifyDataSetChanged();
			}
		};
		showOptionDialog(title, logoStampUIString, curIdx, listener, true);
	}

	private void showVideoStampDialog() {
		CharSequence title = res.getString(R.string.setting_video_stamp);

		final String[] vidoeStampUIString = uiDisplayResource
				.getVideoStampUIString();
		if (vidoeStampUIString == null) {
			return;
		}
		int length = vidoeStampUIString.length;
		int curIdx = 0;
		UIInfo uiInfo = refection.refecltFromSDKToUI(
				SDKReflectToUI.SETTING_UI_VIDEO_STAMP,
				cameraProperties.getCurrentVideoStamp());
		// Log.i("showVideoStampDialog",
		// "====================cameraProperties.getCurrentVideoStamp()="+cameraProperties.getCurrentVideoStamp());
		for (int i = 0; i < length; i++) {
			if (vidoeStampUIString[i].equals(uiInfo.uiStringInSetting)) {
				curIdx = i;
			}
		}

		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				int value = (Integer) refection.refecltFromUItoSDK(
						UIReflectToSDK.SETTING_SDK_VIDEO_STAMP,
						vidoeStampUIString[arg1]);
				cameraProperties.setVideoStamp(value);
				// cameraProperties.setVideoStamp(arg1);
				arg0.dismiss();
				settingValueList = getSettingValue();
				if (optionListAdapter == null) {
					return;
				}
				optionListAdapter.notifyDataSetChanged();
			}
		};
		showOptionDialog(title, vidoeStampUIString, curIdx, listener, true);
	}

	private void showVideoMicDialog() {
		CharSequence title = res.getString(R.string.setting_video_mic);

		final String[] vidoeMicUIString = uiDisplayResource
				.getVideoMicUIString();
		if (vidoeMicUIString == null) {
			return;
		}
		int length = vidoeMicUIString.length;
		Log.i("showVideoMicDialog", "====================length=" + length);
		int curIdx = 0;
		UIInfo uiInfo = refection.refecltFromSDKToUI(
				SDKReflectToUI.SETTING_UI_VIDEO_MIC,
				cameraProperties.getCurrentVideoMic());
		// Log.i("showVideoMicDialog",
		// "====================cameraProperties.getCurrentVideoMic()="+cameraProperties.getCurrentVideoMic());
		for (int i = 0; i < length; i++) {
			if (vidoeMicUIString[i].equals(uiInfo.uiStringInSetting)) {
				curIdx = i;
			}
		}

		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				cameraProperties.setVideoMic(arg1);
				arg0.dismiss();
				settingValueList = getSettingValue();
				if (optionListAdapter == null) {
					return;
				}
				optionListAdapter.notifyDataSetChanged();
			}
		};
		showOptionDialog(title, vidoeMicUIString, curIdx, listener, true);
	}

	private void showCameraConfigurationDialog() {
		// TODO Auto-generated method stub

		LayoutInflater factory = LayoutInflater.from(context);
		View textEntryView = factory.inflate(R.layout.camera_name_password_set,
				null);
		final EditText cameraName = (EditText) textEntryView
				.findViewById(R.id.camera_name);
		final String name = cameraProperties.getCameraSsid();
		cameraName.setText(name);
		final EditText cameraPassword = (EditText) textEntryView
				.findViewById(R.id.wifi_password);
		final String password = cameraProperties.getCameraPassword();
		cameraPassword.setText(password);
		AlertDialog.Builder ad1 = new AlertDialog.Builder(context);
		ad1.setTitle(R.string.camera_wifi_configuration);
		ad1.setIcon(android.R.drawable.ic_dialog_info);
		ad1.setView(textEntryView);
		ad1.setCancelable(true);
		ad1.setPositiveButton(R.string.camera_configuration_set,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int arg1) {

						String temp1 = cameraName.getText().toString();
						if (temp1.length() > 20) {
							Toast.makeText(context, R.string.camera_name_limit,
									Toast.LENGTH_LONG).show();
							// do not allow dialog close
							try {
								Field field = dialog.getClass().getSuperclass()
										.getDeclaredField("mShowing");
								field.setAccessible(true);
								field.set(dialog, false);

							} catch (Exception e) {
								e.printStackTrace();
							}
							return;
						}
						String temp = cameraPassword.getText().toString();
						if (temp.length() > 10 || temp.length() < 8) {
							Toast.makeText(context, R.string.password_limit,
									Toast.LENGTH_LONG).show();
							// do not allow dialog close
							try {
								Field field = dialog.getClass().getSuperclass()
										.getDeclaredField("mShowing");
								field.setAccessible(true);
								field.set(dialog, false);

							} catch (Exception e) {
								e.printStackTrace();
							}
							return;
						}

						// allow dialog close
						try {
							Field field = dialog.getClass().getSuperclass()
									.getDeclaredField("mShowing");
							field.setAccessible(true);
							field.set(dialog, true);
						} catch (Exception e) {
							e.printStackTrace();
						}

						if (name.equals(cameraName.getText().toString()) == false) {
							cameraProperties.setCameraSsid(cameraName.getText()
									.toString());
						}
						if (password.equals(temp) == false) {
							cameraProperties.setCameraPassword(cameraPassword
									.getText().toString());
						}
					}
				});
		ad1.show();
	}

	/**
	 * Added by zhangyanhu C01012,2014-9-2
	 */
	private void showUpsideDialog() {
		// TODO Auto-generated method stub
		CharSequence title = res.getString(R.string.upside);
		final String[] upsideUIString = uiDisplayResource.getUpside();
		if (upsideUIString == null) {
			WriteLogToDevice.writeLog("[Error] -- SettingView: ",
					"upsideUIString == null");
			return;
		}
		int length = upsideUIString.length;

		int curIdx = 0;
		UIInfo uiInfo = reflection.refecltFromSDKToUI(
				SDKReflectToUI.SETTING_UI_UPSIDE,
				cameraProperties.getCurrentUpsideDown());
		for (int i = 0; i < length; i++) {
			if (upsideUIString[i].equals(uiInfo.uiStringInSetting)) {
				curIdx = i;
			}
		}

		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				int value = (Integer) reflection
						.refecltFromUItoSDK(UIReflectToSDK.SETTING_SDK_UPSIDE,
								upsideUIString[arg1]);
				cameraProperties.setUpsideDown(value);
				previewHandler.obtainMessage(
						GlobalApp.MESSAGE_UPDATE_UI_UPSIDE_DOWN).sendToTarget();
				arg0.dismiss();
				settingValueList = getSettingValue();
				if (optionListAdapter == null) {
					return;
				}
				optionListAdapter.notifyDataSetChanged();
			}
		};
		showOptionDialog(title, upsideUIString, curIdx, listener, true);
	}

	/**
	 * Added by zhangyanhu C01012,2014-9-2
	 */
	private void showSlowMotionDialog() {
		// TODO Auto-generated method stub
		CharSequence title = res.getString(R.string.slowmotion);
		final String[] slowmotionUIString = uiDisplayResource.getSlowMotion();
		if (slowmotionUIString == null) {
			WriteLogToDevice.writeLog("[Error] -- SettingView: ",
					"slowmotionUIString == null");
			return;
		}
		int length = slowmotionUIString.length;

		int curIdx = 0;
		UIInfo uiInfo = reflection.refecltFromSDKToUI(
				SDKReflectToUI.SETTING_UI_SLOW_MOTION,
				cameraProperties.getCurrentSlowMotion());
		for (int i = 0; i < length; i++) {
			if (slowmotionUIString[i].equals(uiInfo.uiStringInSetting)) {
				curIdx = i;
			}
		}

		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				int value = (Integer) reflection.refecltFromUItoSDK(
						UIReflectToSDK.SETTING_SDK_SLOW_MOTION,
						slowmotionUIString[arg1]);
				cameraProperties.setSlowMotion(value);
				previewHandler.obtainMessage(
						GlobalApp.MESSAGE_UPDATE_UI_SLOW_MOTION).sendToTarget();
				arg0.dismiss();
				settingValueList = getSettingValue();
				if (optionListAdapter == null) {
					return;
				}
				optionListAdapter.notifyDataSetChanged();
			}
		};
		showOptionDialog(title, slowmotionUIString, curIdx, listener, true);
	}

	/**
	 * Added by zhangyanhu C01012,2014-8-27
	 */
	private void showTimeLapseModeDialog() {
		// TODO Auto-generated method stub
		CharSequence title = res.getString(R.string.timeLapse_mode);
		final String[] timeLapseModeString = uiDisplayResource
				.getTimeLapseMode();
		if (timeLapseModeString == null) {
			WriteLogToDevice.writeLog("[Error] -- SettingView: ",
					"timeLapseModeString == null");
			return;
		}
		int length = timeLapseModeString.length;
		int curIdx = 0;
		UIInfo uiInfo = reflection.refecltFromAppToUI(
				AppReflectToUI.SETTING_UI_TIME_LAPSE_MODE, AppProperties
						.getInstanse().getTimeLapseMode());
		Log.d("tigertiger", "uiInfo.uiStringInSetting ="
				+ uiInfo.uiStringInSetting);
		for (int i = 0; i < length; i++) {
			if (timeLapseModeString[i].equals(uiInfo.uiStringInSetting)) {
				Log.d("tigertiger", "timeLapseModeString[i] ="
						+ timeLapseModeString[i]);
				curIdx = i;
			}
		}

		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				int value = (Integer) reflection.refecltFromUIToApp(
						UIReflectToApp.SETTING_APP_TIME_LAPSE_MODE,
						timeLapseModeString[arg1]);
				AppProperties.getInstanse().setTimeLapseMode(value);
				arg0.dismiss();
				if (value == TimeLapseMode.TIME_LAPSE_MODE_VIDEO) {
					previewHandler.obtainMessage(
							GlobalApp.MESSAGE_SETTING_TIMELAPSE_VIDEO_MODE)
							.sendToTarget();
				} else if (value == TimeLapseMode.TIME_LAPSE_MODE_STILL) {
					previewHandler.obtainMessage(
							GlobalApp.MESSAGE_SETTING_TIMELAPSE_STILL_MODE)
							.sendToTarget();
				}
				settingValueList = getSettingValue();
				if (optionListAdapter == null) {
					return;
				}
				optionListAdapter.notifyDataSetChanged();
			}
		};
		showOptionDialog(title, timeLapseModeString, curIdx, listener, true);
	}

	/**
	 * Added by zhangyanhu C01012,2014-8-21
	 */
	private void showTimeLapseDurationDialog() {
		// TODO Auto-generated method stub
		CharSequence title = res
				.getString(R.string.setting_time_lapse_duration);
		final String[] videoTimeLapseDurationString = uiDisplayResource
				.getTimeLapseDuration();
		if (videoTimeLapseDurationString == null) {
			WriteLogToDevice.writeLog("[Error] -- SettingView: ",
					"videoTimeLapseDurationString == null");
			return;
		}
		int length = videoTimeLapseDurationString.length;

		int curIdx = 0;
		UIInfo uiInfo = reflection.refecltFromSDKToUI(
				SDKReflectToUI.SETTING_UI_TIME_LAPSE_DURATION,
				cameraProperties.getCurrentTimeLapseDuration());
		Log.d("tigertiger", "uiInfo.uiStringInSetting ="
				+ uiInfo.uiStringInSetting);
		for (int i = 0; i < length; i++) {
			if (videoTimeLapseDurationString[i]
					.equals(uiInfo.uiStringInSetting)) {
				Log.d("tigertiger", "videoTimeLapseDurationString[i] ="
						+ videoTimeLapseDurationString[i]);
				curIdx = i;
			}
		}

		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				int value = (Integer) reflection.refecltFromUItoSDK(
						UIReflectToSDK.SETTING_SDK_TIME_LAPSE_DURATION,
						videoTimeLapseDurationString[arg1]);
				cameraProperties.setTimeLapseDuration(value);
				arg0.dismiss();
				settingValueList = getSettingValue();
				if (optionListAdapter == null) {
					return;
				}
				optionListAdapter.notifyDataSetChanged();
			}
		};
		showOptionDialog(title, videoTimeLapseDurationString, curIdx, listener,
				true);
	}

	/**
	 * Added by zhangyanhu C01012,2014-8-21
	 */
	private void showTimeLapseIntervalDialog() {
		// TODO Auto-generated method stub
		Log.d("tigertiger", "showTimeLapseIntervalDialog");
		CharSequence title = res
				.getString(R.string.setting_time_lapse_interval);
		final String[] videoTimeLapseIntervalString = TimeLapseInterval
				.getInstance().getValueStringList();
		if (videoTimeLapseIntervalString == null) {
			WriteLogToDevice.writeLog("[Error] -- SettingView: ",
					"videoTimeLapseIntervalString == null");
			return;
		}
		int length = videoTimeLapseIntervalString.length;

		int curIdx = 0;
		// UIInfo uiInfo =
		// reflection.refecltFromSDKToUI(SDKReflectToUI.SETTING_UI_TIME_LAPSE_INTERVAL,
		// cameraProperties.getCurrentTimeLapseInterval());
		for (int i = 0; i < length; i++) {
			if (videoTimeLapseIntervalString[i].equals(TimeLapseInterval
					.getInstance().getCurrentValue())) {
				curIdx = i;
			}
		}

		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// int value = (Integer)
				// reflection.refecltFromUItoSDK(UIReflectToSDK.SETTING_SDK_TIME_LAPSE_INTERVAL,
				// videoTimeLapseIntervalString[arg1]);
				cameraProperties.setTimeLapseInterval((TimeLapseInterval
						.getInstance().getValueStringInt())[arg1]);
				arg0.dismiss();
				settingValueList = getSettingValue();
				if (optionListAdapter == null) {
					return;
				}
				optionListAdapter.notifyDataSetChanged();
			}
		};
		showOptionDialog(title, videoTimeLapseIntervalString, curIdx, listener,
				true);
	}

	/**
	 * Added by zhangyanhu C01012,2014-8-5
	 */

	/**
	 * Added by zhangyanhu C01012,2014-6-26
	 */
	private void showDelayTimeOptionDialog() {
		// TODO Auto-generated method stub
		CharSequence title = res.getString(R.string.stream_set_timer);
		final String[] delayTimeUIString = uiDisplayResource
				.getDelayTimeUIString();
		if (delayTimeUIString == null) {
			WriteLogToDevice.writeLog("[Error] -- SettingView: ",
					"delayTimeUIString == null");
			return;
		}
		int length = delayTimeUIString.length;

		int curIdx = 0;
		UIInfo uiInfo = reflection.refecltFromSDKToUI(
				SDKReflectToUI.SETTING_UI_DELAY_TIME,
				cameraProperties.getCurrentCaptureDelay());
		for (int i = 0; i < length; i++) {
			if (delayTimeUIString[i].equals(uiInfo.uiStringInSetting)) {
				curIdx = i;
			}
		}

		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				int value = (Integer) reflection.refecltFromUItoSDK(
						UIReflectToSDK.SETTING_SDK_DELAY_TIME,
						delayTimeUIString[arg1]);
				cameraProperties.setCaptureDelay(value);
				previewHandler.obtainMessage(
						GlobalApp.MESSAGE_UPDATE_UI_CAPTURE_DELAY)
						.sendToTarget();
				arg0.dismiss();
				settingValueList = getSettingValue();
				if (optionListAdapter == null) {
					return;
				}
				optionListAdapter.notifyDataSetChanged();
			}
		};
		showOptionDialog(title, delayTimeUIString, curIdx, listener, true);
	}

	/**
	 * Added by zhangyanhu C01012,2014-6-25
	 */
	private void showVideoSizeOptionDialog() {
		// TODO Auto-generated method stub
		CharSequence title = res.getString(R.string.stream_set_res_vid);

		final String[] videoSizeUIString = uiDisplayResource.getVideoSize();
		if (videoSizeUIString == null) {
			WriteLogToDevice.writeLog("[Error] -- SettingView: ",
					"videoSizeUIString == null");
			previewHandler
					.obtainMessage(GlobalApp.MESSAGE_UPDATE_UI_VIDEO_SIZE)
					.sendToTarget();
			return;
		}
		int length = videoSizeUIString.length;

		int curIdx = 0;
		UIInfo uiInfo = reflection.refecltFromSDKToUI(
				SDKReflectToUI.SETTING_UI_VIDEO_SIZE,
				cameraProperties.getCurrentVideoSize());
		for (int i = 0; i < length; i++) {
			if (videoSizeUIString[i].equals(uiInfo.uiStringInSetting)) {
				curIdx = i;
			}
		}

		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				final String value = (String) reflection.refecltFromUItoSDK(
						UIReflectToSDK.SETTING_SDK_VIDEO_SIZE,
						videoSizeUIString[arg1]);
				arg0.dismiss();
				if (value.equals("2704x1524 15")
						|| value.equals("3840x2160 10")) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							context);
					builder.setMessage(R.string.not_support_preview);
					builder.setNegativeButton(R.string.setting_no,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									previewHandler
											.obtainMessage(
													GlobalApp.MESSAGE_UPDATE_UI_VIDEO_SIZE)
											.sendToTarget();
								}
							});
					builder.setPositiveButton(R.string.setting_yes,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									dialog.dismiss();
									cameraProperties.setVideoSize(value);
									previewHandler
											.obtainMessage(
													GlobalApp.MESSAGE_UPDATE_UI_VIDEO_SIZE)
											.sendToTarget();
									settingValueList = getSettingValue();
									if (optionListAdapter == null) {
										return;
									}
									optionListAdapter.notifyDataSetChanged();
								}
							});
					builder.create().show();
				} else {
					cameraProperties.setVideoSize(value);
					previewHandler.obtainMessage(
							GlobalApp.MESSAGE_UPDATE_UI_VIDEO_SIZE)
							.sendToTarget();
					settingValueList = getSettingValue();
					if (optionListAdapter == null) {
						return;
					}
					optionListAdapter.notifyDataSetChanged();
				}
				/**/
			}
		};
		showOptionDialog(title, videoSizeUIString, curIdx, listener, false);
	}

	/**
	 * Added by zhangyanhu C01012,2014-6-25
	 */
	private void showImageSizeOptionDialog() {
		// TODO Auto-generated method stub
		CharSequence title = res.getString(R.string.stream_set_res_photo);

		final String[] imageSizeUIString = uiDisplayResource.getImageSize();
		if (imageSizeUIString == null) {
			WriteLogToDevice.writeLog("[Error] -- SettingView: ",
					"imageSizeUIString == null");
			previewHandler
					.obtainMessage(GlobalApp.MESSAGE_UPDATE_UI_IMAGE_SIZE)
					.sendToTarget();
			return;
		}
		int length = imageSizeUIString.length;

		int curIdx = 0;
		UIInfo uiInfo = reflection.refecltFromSDKToUI(
				SDKReflectToUI.SETTING_UI_IMAGE_SIZE,
				cameraProperties.getCurrentImageSize());

		for (int ii = 0; ii < length; ii++) {
			WriteLogToDevice.writeLog("[Normal] -- SettingView: ",
					"uiInfo.uiStringInSetting == " + uiInfo.uiStringInSetting);
			if (imageSizeUIString[ii].equals(uiInfo.uiStringInSetting)) {
				curIdx = ii;
			}
		}

		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {

				String value = (String) reflection.refecltFromUItoSDK(
						UIReflectToSDK.SETTING_SDK_IMAGE_SIZE,
						imageSizeUIString[arg1]);
				cameraProperties.setImageSize(value);
				previewHandler.obtainMessage(
						GlobalApp.MESSAGE_UPDATE_UI_IMAGE_SIZE).sendToTarget();
				arg0.dismiss();
				settingValueList = getSettingValue();
				if (optionListAdapter == null) {
					return;
				}
				optionListAdapter.notifyDataSetChanged();
			}
		};
		showOptionDialog(title, imageSizeUIString, curIdx, listener, false);
	}

	/**
	 * Added by zhangyanhu C01012,2014-6-25
	 */
	private void showFormatConfirmDialog() {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(R.string.setting_format_desc);
		builder.setNegativeButton(R.string.setting_no,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
		builder.setPositiveButton(R.string.setting_yes,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						// handler.obtainMessage(REQUEST_FORMAT_SD_CARD).sendToTarget();
						FormatSDCard formatSDCard = new FormatSDCard(handler);
						formatSDCard.start();
					}
				});
		dialog = builder.create();
		dialog.show();
	}

	/**
	 * Added by zhangyanhu C01012,2014-6-25
	 */
	private void showDateStampOptionDialog() {
		// TODO Auto-generated method stub
		CharSequence title = res.getString(R.string.setting_datestamp);
		final String[] dateStampUIString = uiDisplayResource
				.getDateStampUIString();
		if (dateStampUIString == null) {
			WriteLogToDevice.writeLog("[Error] -- SettingView: ",
					"dateStampUIString == null");
			return;
		}
		int length = dateStampUIString.length;

		int curIdx = 0;
		UIInfo uiInfo = reflection.refecltFromSDKToUI(
				SDKReflectToUI.SETTING_UI_DATE_STAMP,
				cameraProperties.getCurrentDateStamp());
		for (int i = 0; i < length; i++) {
			if (dateStampUIString[i].equals(uiInfo.uiStringInSetting)) {
				curIdx = i;
			}
		}

		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				int value = (Integer) reflection.refecltFromUItoSDK(
						UIReflectToSDK.SETTING_SDK_DATE_STAMP,
						dateStampUIString[arg1]);
				Log.d("tigertiger", "----------------value =" + value);
				cameraProperties.setDateStamp(value);
				arg0.dismiss();
				settingValueList = getSettingValue();
				if (optionListAdapter == null) {
					return;
				}
				optionListAdapter.notifyDataSetChanged();
			}
		};
		showOptionDialog(title, dateStampUIString, curIdx, listener, true);
	}

	/**
	 * Added by zhangyanhu C01012,2014-6-25
	 */
	private void showElectricityFrequencyOptionDialog() {
		// TODO Auto-generated method stub
		CharSequence title = res.getString(R.string.setting_power_supply);

		final String[] eleFreUIString = uiDisplayResource
				.getEleFreValueUIString();
		if (eleFreUIString == null) {
			WriteLogToDevice.writeLog("[Error] -- SettingView: ",
					"eleFreUIString == null");
			return;
		}
		int length = eleFreUIString.length;

		int curIdx = 0;
		UIInfo uiInfo = reflection.refecltFromSDKToUI(
				SDKReflectToUI.SETTING_UI_ELETRICITY_FREQUENCY,
				cameraProperties.getCurrentLightFrequency());
		for (int i = 0; i < length; i++) {
			if (eleFreUIString[i].equals(uiInfo.uiStringInSetting)) {
				curIdx = i;
			}
		}

		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				int value = (Integer) reflection.refecltFromUItoSDK(
						UIReflectToSDK.SETTING_SDK_ELETRICITY_FREQUENCY,
						eleFreUIString[arg1]);
				cameraProperties.setLightFrequency(value);
				arg0.dismiss();
				settingValueList = getSettingValue();
				if (optionListAdapter == null) {
					return;
				}
				optionListAdapter.notifyDataSetChanged();
			}
		};
		showOptionDialog(title, eleFreUIString, curIdx, listener, true);
	}

	/**
	 * Added by zhangyanhu C01012,2014-6-25
	 */
	private void showWhiteBalanceOptionDialog() {
		// TODO Auto-generated method stub
		CharSequence title = res.getString(R.string.setting_awb);

		final String[] whiteBalanceUIString = uiDisplayResource
				.getWhiteBalanceUIString();
		if (whiteBalanceUIString == null) {
			WriteLogToDevice.writeLog("[Error] -- SettingView: ",
					"whiteBalanceUIString == null");
			return;
		}
		int length = whiteBalanceUIString.length;

		int curIdx = 0;
		UIInfo uiInfo = reflection.refecltFromSDKToUI(
				SDKReflectToUI.SETTING_UI_WHITE_BALANCE,
				cameraProperties.getCurrentWhiteBalance());
		for (int i = 0; i < length; i++) {
			if (whiteBalanceUIString[i].equals(uiInfo.uiStringInSetting)) {
				curIdx = i;
			}
		}

		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				int value = (Integer) reflection.refecltFromUItoSDK(
						UIReflectToSDK.SETTING_SDK_WHITE_BALANCE,
						whiteBalanceUIString[arg1]);
				cameraProperties.setWhiteBalance(value);
				previewHandler.obtainMessage(
						GlobalApp.MESSAGE_UPDATE_UI_WHITE_BALANCE_ICON)
						.sendToTarget();
				arg0.dismiss();
				settingValueList = getSettingValue();
				if (optionListAdapter == null) {
					return;
				}
				optionListAdapter.notifyDataSetChanged();
			}
		};
		showOptionDialog(title, whiteBalanceUIString, curIdx, listener, true);
	}

	/**
	 * Added by zhangyanhu C01012,2014-6-25
	 */
	private void showBurstOptionDialog() {
		// TODO Auto-generated method stub
		CharSequence title = res.getString(R.string.setting_burst);

		final String[] burstUIString = uiDisplayResource.getBurstNumUIString();
		if (burstUIString == null) {
			WriteLogToDevice.writeLog("[Error] -- SettingView: ",
					"burstUIString == null");
			return;
		}
		int length = burstUIString.length;

		int curIdx = 0;
		UIInfo uiInfo = reflection.refecltFromSDKToUI(
				SDKReflectToUI.SETTING_UI_BURST,
				cameraProperties.getCurrentBurstNum());
		for (int i = 0; i < length; i++) {
			if (burstUIString[i].equals(uiInfo.uiStringInSetting)) {
				curIdx = i;
			}
		}

		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				int value = (Integer) reflection.refecltFromUItoSDK(
						UIReflectToSDK.SETTING_SDK_BURST, burstUIString[arg1]);
				cameraProperties.setCurrentBurst(value);
				previewHandler.obtainMessage(
						GlobalApp.MESSAGE_UPDATE_UI_BURST_ICON).sendToTarget();
				arg0.dismiss();
				settingValueList = getSettingValue();
				if (optionListAdapter == null) {
					return;
				}
				optionListAdapter.notifyDataSetChanged();
			}
		};
		showOptionDialog(title, burstUIString, curIdx, listener, true);
	}

	public void sdCardIsNotReadyAlert() {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(R.string.dialog_no_sd);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		AlertDialog dialog = builder.create();
		dialog.setCancelable(true);
		dialog.show();
	}

	private void showOptionDialog(CharSequence title, CharSequence[] items,
			int checkedItem, DialogInterface.OnClickListener listener,
			boolean cancelable) {
		if (optionDialog == null || optionDialog.isShowing() == false) {
			optionDialog = new AlertDialog.Builder(context).setTitle(title)
					.setSingleChoiceItems(items, checkedItem, listener)
					.create();
			optionDialog.setCancelable(cancelable);
			optionDialog.show();
		}
	}

	private ArrayList<String> getSettingValue() {
		ArrayList<String> tempSettingValueList = null;
		if (type == CAPTURE_SETTING_MENU) {
			tempSettingValueList = uiDisplayResource
					.getSettingValueCaptureMode();
		} else if (type == VIDEO_SETTING_MENU) {
			tempSettingValueList = uiDisplayResource.getSettingValueVideoMode();
		} else {
			tempSettingValueList = uiDisplayResource
					.getSettingValueTimeLapseMode();
		}
		return tempSettingValueList;
	}
}
