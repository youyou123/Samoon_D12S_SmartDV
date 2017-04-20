package com.icatch.wcm2app.Activity;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.icatch.wcm2app.Data.SDKReflectToUI;
import com.icatch.wcm2app.ExtendComponent.Preview;
import com.icatch.wcm2app.ExtendComponent.ZoomBar;
import com.icatch.wcm2app.common.AppProperties;
import com.icatch.wcm2app.common.ExceptionCheck;
import com.icatch.wcm2app.common.ExitApp;
import com.icatch.wcm2app.common.GlobalApp;
import com.icatch.wcm2app.common.SystemCheckTools;
import com.icatch.wcm2app.common.WriteLogToDevice;
import com.icatch.wcm2app.controller.SDKEvent;
import com.icatch.wcm2app.controller.UIDisplayResource;
import com.icatch.wcm2app.controller.sdkApi.AppCfgParameter;
import com.icatch.wcm2app.controller.sdkApi.CameraAction;
import com.icatch.wcm2app.controller.sdkApi.CameraProperties;
import com.icatch.wcm2app.controller.sdkApi.CameraState;
import com.icatch.wcm2app.controller.sdkApi.FileOperation;
import com.icatch.wcm2app.controller.sdkApi.PreviewStream;
import com.icatch.wcm2app.controller.sdkApi.SDKSession;
import com.icatch.wcm2app.function.CameraCaptureThread;
import com.icatch.wcm2app.function.SettingView;
import com.icatch.wcm2app.function.WifiCheck;
import com.icatch.wcm2app.function.ZoomThread;
import com.icatch.wcm2app.globalValue.SlowMotion;
import com.icatch.wcm2app.globalValue.TimeLapseInterval;
import com.icatch.wcm2app.globalValue.TimeLapseMode;
import com.icatch.wcm2app.globalValue.Upside;
import com.icatch.wcm2app.R;

import com.icatch.wificam.customer.type.ICatchCameraProperty;
import com.icatch.wificam.customer.type.ICatchCustomerStreamParam;
import com.icatch.wificam.customer.type.ICatchDateStamp;
import com.icatch.wificam.customer.type.ICatchEventID;
import com.icatch.wificam.customer.type.ICatchFile;
import com.icatch.wificam.customer.type.ICatchFileType;
import com.icatch.wificam.customer.type.ICatchH264StreamParam;
import com.icatch.wificam.customer.type.ICatchMJPGStreamParam;
import com.icatch.wificam.customer.type.ICatchMode;
import com.icatch.wificam.customer.type.ICatchPreviewMode;

//import android.app.Activity;

public class Main extends FragmentActivity {

	private Preview preview;
	private RelativeLayout setupMainMenu;
	private ListView mainMenuList;
	private Button captureBtn;
	private Button setupBtn;
	private Button pbBtn;
	private Button videoToggle;
	private Button stillToggle;
	private Button timeLapseToggle;
	private RelativeLayout delay_captue_status;
	private RelativeLayout photo_hd_status;
	private RelativeLayout video_hd_status;
	private Button delay_capture_btn;
	private Button photo_hd_btn;
	private Button video_hd_btn;
	private ImageView wb_status;
	private ImageView burst_status;
	private TextView delay_capture_txt;
	private TextView photo_hd_txt;
	private TextView video_hd_txt;
	private ImageView wifiStatus;
	private ImageView batteryStatus;
	private ZoomBar zoomBar;
	private TextView zoomRate;
	private ImageButton zoomIn;
	private ImageButton zoomOut;
	private RelativeLayout zoomLayout;
	private ImageView timelapseMode;
	private ImageView slowMotion;
	private ImageView carMode;
	private TextView recordingTime;

	private WifiSSReceiver wifiSSReceiver;
	private int curMode;
	private GlobalApp globalApp;
	private WifiCheck wifiTool;

	private MediaPlayer videoCaptureStartBeep;
	private MediaPlayer modeSwitchBeep;
	// private MediaPlayer stillCaptureStartBeep;
	// private MediaPlayer delayBeep;

	private ExecutorService executor;
	private Future<Object> future;
	private MainHandler mainHandler;
	private Timer videoCaptureTimer;
	private Handler mainTimerHandler;
	private boolean videoCaptureTimerLamp = false;
	// private ProgressDialog captureDialog;
	private ProgressDialog progressDialog;
	private List<ICatchFile> fileList;
	private FileOperation fileOperation = new FileOperation();;
	private static final int APP_STATE_STILL_PREVIEW = 0x0001;
	private static final int APP_STATE_STILL_CAPTURE = 0x0002;
	private static final int APP_STATE_VIDEO_PREVIEW = 0x0003;
	private static final int APP_STATE_VIDEO_CAPTURE = 0x0004;
	private static final int APP_STATE_TIMELAPSE_VIDEO_CAPTURE = 0x0005;
	private static final int APP_STATE_TIMELAPSE_STILL_CAPTURE = 0x0006;
	private static final int APP_STATE_TIMELAPSE_PREVIEW_VIDEO = 0x0007;
	private static final int APP_STATE_TIMELAPSE_PREVIEW_STILL = 0x0008;
	// Added by zhangyanhu 2013.11.27
	private static final int MESSAGE_QUIT_APP = 0x0001;
	private static final int MESSAGE_ZOOM_BAR_DISAPPEAR = 0x0003;
	private static final int MESSAGE_UPDATE_RECORDING_TIME = 0X0004;
	// private static Boolean QUITFLAG = false;
	// End add by zhangyanhu 2013.11.27
	private static final int MESSAGE_VIDEO_CAPTURE_ON = 0x0002;

	private SDKEvent sdkEvent;
	private AlertDialog dialog;
	private boolean sdCardFullWarning;
	protected boolean intentLock = false;
	private Toast toastRecording = null;
	private Toast toastCapturing = null;
	private Toast toastWaitCapture = null;
	private Toast toastTimeLapse = null;
	// private Toast toastTimeLapse = null;
	private long lastCilckTime = 0;

	private boolean allowClickButtoms = true;
	public CameraProperties cameraProperties = new CameraProperties();
	private CameraAction cameraAction = new CameraAction();
	private SettingView settingView;
	// private CameraProperties cameraProperty = new CameraProperties();
	private UIDisplayResource uiDisplayResource = UIDisplayResource
			.getinstance();

	private CameraState cameraState = new CameraState();
	private PreviewStream previewStream = new PreviewStream();
	private int lastZoomRate;
	private Timer zoomTimer;
	// private TimerTask task;
	private static int zoomBarDisplayDuration = 5000;// ms
	private static int zoomMinRate = 10;
	private Boolean captureDelayMode = false;
	private Timer recordingTimer;
	private int lapseTime = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		WriteLogToDevice.writeLog("[Normal] -- Main: ", "start onCreate");
		// TODO: make sure session is created and in prepared state
		// getWindow().setFormat(PixelFormat.RGBA_8888);
		// never sleep when run this activity
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
		// WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		// do not display menu bar
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.main);
		
//	cameraAction.capturePhoto();
	//fileOperation.deleteFile();
	
		mainHandler = new MainHandler();
		
		videoCaptureTimer = new Timer(false);

		preview = (Preview) findViewById(R.id.preview);
		setupMainMenu = (RelativeLayout) findViewById(R.id.setupMainMenu);
		mainMenuList = (ListView) findViewById(R.id.listView);
		setupBtn = (Button) findViewById(R.id.settingBtnToggle);
		pbBtn = (Button) findViewById(R.id.pb);
		captureBtn = (Button) findViewById(R.id.doCapture);
		videoToggle = (Button) findViewById(R.id.videoToggle);
		stillToggle = (Button) findViewById(R.id.stillToggle);
		timeLapseToggle = (Button) findViewById(R.id.timeLapseToggle);
		delay_captue_status = (RelativeLayout) findViewById(R.id.delay_captue_status);
		photo_hd_status = (RelativeLayout) findViewById(R.id.photo_hd_status);
		video_hd_status = (RelativeLayout) findViewById(R.id.video_hd_status);
		delay_capture_btn = (Button) findViewById(R.id.delay_capture_btn);
		photo_hd_btn = (Button) findViewById(R.id.photo_hd_btn);
		video_hd_btn = (Button) findViewById(R.id.video_hd_btn);
		wb_status = (ImageView) findViewById(R.id.wb_status);
		burst_status = (ImageView) findViewById(R.id.burst_status);
		delay_capture_txt = (TextView) findViewById(R.id.delay_capture_text);
		Log.d("1111", "delay_capture_txt ==" + delay_capture_txt);
		photo_hd_txt = (TextView) findViewById(R.id.photo_hd_txt);
		video_hd_txt = (TextView) findViewById(R.id.video_hd_txt);
		wifiStatus = (ImageView) findViewById(R.id.wifi_status);
		batteryStatus = (ImageView) findViewById(R.id.battery_status);
		zoomBar = (ZoomBar) findViewById(R.id.zoomBar);
		zoomRate = (TextView) findViewById(R.id.zoom_rate);
		zoomIn = (ImageButton) findViewById(R.id.zoom_in);
		zoomOut = (ImageButton) findViewById(R.id.zoom_out);
		zoomLayout = (RelativeLayout) findViewById(R.id.zoom_layout);
		timelapseMode = (ImageView) findViewById(R.id.timelapse_mode);
		slowMotion = (ImageView) findViewById(R.id.slow_motion);
		carMode = (ImageView) findViewById(R.id.car_mode);
		recordingTime = (TextView) findViewById(R.id.recording_time);

		// audio for video capture,camera capture
		videoCaptureStartBeep = MediaPlayer.create(Main.this,
				R.raw.camera_timer);
		// stillCaptureStartBeep = MediaPlayer.create(Main.this,
		// R.raw.camera_click);
		modeSwitchBeep = MediaPlayer.create(Main.this, R.raw.focusbeep);
		// delayBeep = MediaPlayer.create(Main.this, R.raw.delay_beep);
		// preview.setBackgroundColor(Color.BLACK);

		initStatus();

		/**
		 * To check SD card is ready Added by zhangyanhu C01012,2014-2-9
		 */
		if (cameraProperties.isSDCardExist() == false) {
			sdCardIsNotReadyAlert(Main.this
					.getString(R.string.dialog_card_removed));
		}
		// set component's listener
		pbBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				WriteLogToDevice.writeLog("[Normal] -- Main: ",
						"pbBtn is clicked");
				if (allowClickButtoms == false) {
					WriteLogToDevice.writeLog("[Abnormal] -- Main: ",
							"do not allow to response button clicking");
					return;
				}
				allowClickButtoms = false;
				WriteLogToDevice.writeLog("[Normal] -- Main: ", "curMode ="
						+ curMode);
				if (intentLock == true) {
					WriteLogToDevice
							.writeLog("[Abnormal] -- Main: ",
									"intentLock is true,maybe has started another one!");
					return;
				}
				if (curMode == APP_STATE_STILL_PREVIEW
						|| curMode == APP_STATE_VIDEO_PREVIEW
						|| curMode == APP_STATE_TIMELAPSE_PREVIEW_VIDEO
						|| curMode == APP_STATE_TIMELAPSE_PREVIEW_STILL) {
					if (preview.stop() == false) {
						WriteLogToDevice.writeLog("[Error] -- Main: ",
								"stopping preview returns false!");
						allowClickButtoms = true;
						return;
					}
					if (stopMediaStream() == false) {
						WriteLogToDevice.writeLog("[Error] -- Main: ",
								"failed to stopMediaStream");
						return;
					}
					
					sdkEvent.delEventListener(ICatchEventID.ICH_EVENT_SDCARD_FULL);
					sdkEvent.delEventListener(ICatchEventID.ICH_EVENT_BATTERY_LEVEL_CHANGED);
					sdkEvent.delEventListener(ICatchEventID.ICH_EVENT_CAPTURE_COMPLETE);
					sdkEvent.delEventListener(ICatchEventID.ICH_EVENT_CAPTURE_START);
					sdkEvent.delEventListener(ICatchEventID.ICH_EVENT_VIDEO_OFF);
					sdkEvent.delEventListener(ICatchEventID.ICH_EVENT_FILE_ADDED);
					sdkEvent.delEventListener(ICatchEventID.ICH_EVENT_VIDEO_ON);
					sdkEvent.delEventListener(ICatchEventID.ICH_EVENT_CONNECTION_DISCONNECTED);
					sdkEvent.delEventListener(ICatchEventID.ICH_EVENT_TIMELAPSE_STOP);
					sdkEvent.delCustomizeEventListener(0x5001);
				
					Intent intent = new Intent();
					WriteLogToDevice.writeLog("[Normal] -- Main: ",
							"intent:start PbMainActivity.class");
					intent.setClass(Main.this, PbMainActivity.class);
					startActivity(intent);
					intentLock = true;
					WriteLogToDevice.writeLog("[Normal] -- Main: ",
							"intent:end start PbMainActivity.class");
					return;
				} else if (curMode == APP_STATE_VIDEO_CAPTURE
						|| curMode == APP_STATE_TIMELAPSE_VIDEO_CAPTURE) {
					// 0048931 Modify by zhangyanhu 2013.12.10
					if (toastRecording == null) {
						toastRecording = Toast.makeText(Main.this,
								R.string.stream_error_recording,
								Toast.LENGTH_SHORT);
					} else {
						toastRecording.setText(R.string.stream_error_recording);
						toastRecording.setDuration(Toast.LENGTH_SHORT);
					}
					toastRecording.show();
					// 0048931 End modify by zhangyanhu 2013.12.10
				} else if (curMode == APP_STATE_STILL_CAPTURE
						|| curMode == APP_STATE_TIMELAPSE_STILL_CAPTURE) {
					if (toastCapturing == null) {
						toastCapturing = Toast.makeText(Main.this,
								R.string.stream_error_capturing,
								Toast.LENGTH_SHORT);
					} else {
						toastCapturing.setText(R.string.stream_error_capturing);
						toastCapturing.setDuration(Toast.LENGTH_SHORT);
					}
					toastCapturing.show();
				}
				allowClickButtoms = true;
				WriteLogToDevice.writeLog("[Normal] -- Main: ",
						"end processing for responsing pbBtn clicking");
			}
		});

		captureBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				WriteLogToDevice.writeLog("[Normal] -- Main: ",
						"captureBtn is clicked");
				Log.d("tigertiger", "captureBtn.setOnClickListener click");
				if (allowClickButtoms == false) {
					WriteLogToDevice.writeLog("[Abnormal] -- Main: ",
							"do not allow to response button clicking");
					return;
				}
				allowClickButtoms = false;
				long timeInterval = System.currentTimeMillis() - lastCilckTime;
				if (timeInterval < 1000) {
					WriteLogToDevice.writeLog("[Abnormal] -- Main: ",
							"repeat click: timeInterval < 1000");
					allowClickButtoms = true;
					return;
				} else {
					lastCilckTime = System.currentTimeMillis();
				}

				// no recording,so start to record
				if (curMode == APP_STATE_VIDEO_PREVIEW) {
					if (cameraProperties.isSDCardExist() == false) {
						WriteLogToDevice.writeLog("[Abnormal] -- Main: ",
								"sd card is removed");
						sdCardIsNotReadyAlert(Main.this
								.getString(R.string.dialog_card_removed));
						allowClickButtoms = true;
						return;
					} else if (cameraProperties.getRecordingRemainTime() <= 0) {
						WriteLogToDevice.writeLog("[Abnormal] -- Main: ",
								"sd card is full");
						sdCardFullWarning = false;
						sdCardIsFullAlert();
						allowClickButtoms = true;
						return;
					}
					curMode = APP_STATE_VIDEO_CAPTURE;

					videoCaptureStartBeep.start();
					if (startMoiveRecording() == false) {
						WriteLogToDevice.writeLog("[Error] -- Main: ",
								"failed to start startVideoCapture");
						curMode = APP_STATE_VIDEO_PREVIEW;
						allowClickButtoms = true;
						return;
					}
					TimerTask task = new TimerTask() {
						@Override
						public void run() {
							if (videoCaptureTimerLamp) {
								videoCaptureTimerLamp = false;
								sendTimerMsg(MESSAGE_VIDEO_CAPTURE_ON, 1);

							} else {
								videoCaptureTimerLamp = true;
								sendTimerMsg(MESSAGE_VIDEO_CAPTURE_ON, 0);
							}
						}
					};
					if (videoCaptureTimer != null) {
						videoCaptureTimer.cancel();
					}
					videoCaptureTimer = new Timer(true);
					videoCaptureTimer.schedule(task, 0, 1000);

				} else if (curMode == APP_STATE_VIDEO_CAPTURE) {

					videoCaptureStartBeep.start();
					if (stopMoiveRecording() == false) {
						WriteLogToDevice.writeLog("[Error] -- Main: ",
								"failed to stopMoiveRecording");
						allowClickButtoms = true;
						return;
					}
					if (videoCaptureTimer != null) {
						videoCaptureTimer.cancel();
					}
					if (recordingTimer != null) {
						recordingTimer.cancel();
					}
					recordingTime.setVisibility(View.GONE);
					video_hd_txt.setText(uiDisplayResource
							.getRecordingRemainTime());
					captureBtn
							.setBackgroundResource(R.drawable.video_recording_btn_on);
					curMode = APP_STATE_VIDEO_PREVIEW;

				} else if (curMode == APP_STATE_STILL_PREVIEW) {
					if (cameraProperties.isSDCardExist() == false) {
						WriteLogToDevice.writeLog("[Abnormal] -- Main: ",
								"sd card is removed");
						sdCardIsNotReadyAlert(Main.this
								.getString(R.string.dialog_card_removed));
						allowClickButtoms = true;
						return;
					} else if (forbidePhotoCapture() == true) {
						WriteLogToDevice.writeLog("[Abnormal] -- Main: ",
								"sd card is full");
						sdCardFullWarning = false;
						sdCardIsFullAlert();
						allowClickButtoms = true;
						return;
					}
					// photo capture
					captureBtn.setEnabled(false);
					captureBtn
							.setBackgroundResource(R.drawable.still_capture_btn_off);
					// if (preview.stop() == false) {
					// Toast.makeText(Main.this, R.string.stream_capture_failed,
					// Toast.LENGTH_SHORT);
					// }
					curMode = APP_STATE_STILL_CAPTURE;
					Log.d("tigertiger",
							"submit(new CameraCaptureThread(Main.this, mainHandler), null);");
					zoomLayout.setVisibility(View.GONE);
					if (captureDelayMode == true) {
						TimerTask task = new TimerTask() {
							@Override
							public void run() {
								if (preview.stop() == false) {
									// Toast.makeText(Main.this,
									// R.string.stream_capture_failed,
									// Toast.LENGTH_SHORT);

									return;
								}
								Log.d("tigertiger", "preview.stop() = ");
								if (previewStream.stopMediaStream() == false) {
									// WriteLogToDevice.writeLog("[Error] -- CameraCaptureThread: ",
									// "failed to stopMediaStream");
									return;
								}
								Log.d("tigertiger",
										"previewStream.stopMediaStream() ");
							}
						};
						Timer timer = new Timer(true);
						int delayTime = cameraProperties
								.getCurrentCaptureDelay();
						Log.d("tigertiger", "delayTime = " + delayTime);
						if (delayTime >= 1000) {
							timer.schedule(task, delayTime - 500);
						} else {
							// timer.schedule(task, 0);
							Log.d("tigertiger", "before stop preview");
							preview.stop();
							Log.d("tigertiger",
									"preview.stop finish, before previewStream.stopMediaStream ");
							previewStream.stopMediaStream();
							Log.d("tigertiger",
									"previewStream.stopMediaStream finish ");

						}
					} else {
						Log.d("tigertiger", "before stop preview 2 ");
						preview.stop();
						Log.d("tigertiger",
								"preview.stop finish, before previewStream.stopMediaStream ");
						previewStream.stopMediaStream();
						Log.d("tigertiger",
								"previewStream.stopMediaStream finish ");

					}
					Log.d("tigertiger", "start executor.submit ");

					future = executor.submit(new CameraCaptureThread(Main.this,
							mainHandler), null);
					Log.d("tigertiger", "finish executor.submit ");

					// cameraAction.startTimeLapse();
				} else if (curMode == APP_STATE_TIMELAPSE_PREVIEW_STILL) {
					if (cameraProperties.isSDCardExist() == false) {
						WriteLogToDevice.writeLog("[Abnormal] -- Main: ",
								"sd card is removed");
						sdCardIsNotReadyAlert(Main.this
								.getString(R.string.dialog_card_removed));
						allowClickButtoms = true;
						return;
					} else if (forbidePhotoCapture() == true) {
						WriteLogToDevice.writeLog("[Abnormal] -- Main: ",
								"sd card is full");
						sdCardFullWarning = false;
						sdCardIsFullAlert();
						allowClickButtoms = true;
						return;
					}
					if (cameraProperties.getCurrentTimeLapseInterval() == TimeLapseInterval.TIME_LAPSE_INTERVAL_OFF) {
						if (toastTimeLapse == null) {
							toastTimeLapse = Toast.makeText(Main.this,
									R.string.timeLapse_not_allow,
									Toast.LENGTH_SHORT);
						}
						toastTimeLapse.show();
						allowClickButtoms = true;
						return;
					}
					WriteLogToDevice.writeLog("[Abnormal] -- Main: ",
							"curMode == APP_STATE_TIMELAPSE_PREVIEW_STILL");
					// captureBtn.setEnabled(false);
					captureBtn
							.setBackgroundResource(R.drawable.still_capture_btn_off);
					// if (preview.stop() == false) {
					// Toast.makeText(Main.this, R.string.stream_capture_failed,
					// Toast.LENGTH_SHORT);
					// }
					curMode = APP_STATE_TIMELAPSE_STILL_CAPTURE;
					zoomLayout.setVisibility(View.GONE);
					Log.d("tigertiger", "startTimeLapse");
					if (cameraAction.startTimeLapse() == false) {
						WriteLogToDevice.writeLog("[Error] -- Main: ",
								"failed to start startTimeLapse");
						curMode = APP_STATE_TIMELAPSE_PREVIEW_STILL;
						allowClickButtoms = true;
						return;
					}
				} else if (curMode == APP_STATE_TIMELAPSE_STILL_CAPTURE) {
					WriteLogToDevice.writeLog("[Normal] -- Main: ",
							"curMode == APP_STATE_TIMELAPSE_STILL_CAPTURE");
					if (cameraAction.stopTimeLapse() == false) {
						WriteLogToDevice.writeLog("[Error] -- Main: ",
								"failed to stopTimeLapse");
						allowClickButtoms = true;
						return;
					}
					if (toastTimeLapse == null) {
						toastTimeLapse = Toast.makeText(Main.this,
								R.string.timeLapse_stop, Toast.LENGTH_SHORT);
					} else {
						toastTimeLapse.setText(R.string.timeLapse_stop);
						toastTimeLapse.setDuration(Toast.LENGTH_SHORT);
					}
					toastTimeLapse.show();
					photo_hd_txt.setText(uiDisplayResource.getRemainImageNum());
					captureBtn
							.setBackgroundResource(R.drawable.still_capture_btn);
					curMode = APP_STATE_TIMELAPSE_PREVIEW_STILL;
				} else if (curMode == APP_STATE_TIMELAPSE_PREVIEW_VIDEO) {
					WriteLogToDevice.writeLog("[Normal] -- Main: ",
							"curMode == APP_STATE_TIMELAPSE_PREVIEW_VIDEO");
					if (cameraProperties.isSDCardExist() == false) {
						WriteLogToDevice.writeLog("[Abnormal] -- Main: ",
								"sd card is removed");
						sdCardIsNotReadyAlert(Main.this
								.getString(R.string.dialog_card_removed));
						allowClickButtoms = true;
						return;
					} else if (forbidePhotoCapture() == true) {
						WriteLogToDevice.writeLog("[Abnormal] -- Main: ",
								"sd card is full");
						sdCardFullWarning = false;
						sdCardIsFullAlert();
						allowClickButtoms = true;
						return;
					}
					if (cameraProperties.getCurrentTimeLapseInterval() == TimeLapseInterval.TIME_LAPSE_INTERVAL_OFF) {
						WriteLogToDevice
								.writeLog("[Normal] -- Main: ",
										"time lapse is not allowed because of timelapse interval is OFF");
						if (toastTimeLapse == null) {
							toastTimeLapse = Toast.makeText(Main.this,
									R.string.timeLapse_not_allow,
									Toast.LENGTH_SHORT);
						} else {
							toastTimeLapse
									.setText(R.string.timeLapse_not_allow);
							toastTimeLapse.setDuration(Toast.LENGTH_SHORT);
						}
						toastTimeLapse.show();
						allowClickButtoms = true;
						return;
					}

					curMode = APP_STATE_TIMELAPSE_VIDEO_CAPTURE;

					videoCaptureStartBeep.start();
					if (cameraAction.startTimeLapse() == false) {
						WriteLogToDevice.writeLog("[Error] -- Main: ",
								"failed to start startTimeLapse");
						curMode = APP_STATE_TIMELAPSE_PREVIEW_VIDEO;
						allowClickButtoms = true;
						return;
					}
					TimerTask task = new TimerTask() {
						@Override
						public void run() {
							if (videoCaptureTimerLamp) {
								videoCaptureTimerLamp = false;
								sendTimerMsg(MESSAGE_VIDEO_CAPTURE_ON, 1);

							} else {
								videoCaptureTimerLamp = true;
								sendTimerMsg(MESSAGE_VIDEO_CAPTURE_ON, 0);
							}
						}
					};
					if (videoCaptureTimer != null) {
						videoCaptureTimer.cancel();
					}
					videoCaptureTimer = new Timer(true);
					videoCaptureTimer.schedule(task, 0, 1000);
				} else if (curMode == APP_STATE_TIMELAPSE_VIDEO_CAPTURE) {
					WriteLogToDevice.writeLog("[Normal] -- Main: ",
							"curMode == APP_STATE_TIMELAPSE_VIDEO_CAPTURE");
					videoCaptureStartBeep.start();
					if (cameraAction.stopTimeLapse() == false) {
						WriteLogToDevice.writeLog("[Error] -- Main: ",
								"failed to stopTimeLapse");
						allowClickButtoms = true;
						return;
					}
					if (videoCaptureTimer != null) {
						videoCaptureTimer.cancel();
					}
					video_hd_txt.setText(uiDisplayResource
							.getRecordingRemainTime());
					captureBtn
							.setBackgroundResource(R.drawable.video_recording_btn_on);
					curMode = APP_STATE_TIMELAPSE_PREVIEW_VIDEO;
				}

				allowClickButtoms = true;
				WriteLogToDevice.writeLog("[Normal] -- Main: ",
						"end processing for responsing pbBtn clicking");
			}
		});

		setupBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				WriteLogToDevice.writeLog("[Normal] -- Main: ",
						"setupBtn is clicked:allowClickButtoms="
								+ allowClickButtoms);
				if (allowClickButtoms == false) {
					return;
				}
				allowClickButtoms = false;
				if (curMode == APP_STATE_VIDEO_CAPTURE) {
					// 0048931 Modify by zhangyanhu 2013.12.10
					if (toastRecording == null) {
						toastRecording = Toast.makeText(Main.this,
								R.string.stream_error_recording,
								Toast.LENGTH_SHORT);
					} else {
						toastRecording.setText(R.string.stream_error_recording);
						toastRecording.setDuration(Toast.LENGTH_SHORT);
					}
					toastRecording.show();
					// 0048931 End modify by zhangyanhu 2013.12.10
				} else if (curMode == APP_STATE_STILL_CAPTURE) {
					if (toastCapturing == null) {
						toastCapturing = Toast.makeText(Main.this,
								R.string.stream_error_capturing,
								Toast.LENGTH_SHORT);
					} else {
						toastCapturing.setText(R.string.stream_error_capturing);
						toastCapturing.setDuration(Toast.LENGTH_SHORT);
					}
					toastCapturing.show();
				} else if (curMode == APP_STATE_STILL_PREVIEW) {
					settingView = new SettingView(Main.this, mainMenuList,
							SettingView.CAPTURE_SETTING_MENU, mainHandler);
					settingView.showSettingMainMenu();
					setupMainMenu.setVisibility(View.VISIBLE);
					setupMainMenu.requestFocus();
				} else if (curMode == APP_STATE_VIDEO_PREVIEW) {
					settingView = new SettingView(Main.this, mainMenuList,
							SettingView.VIDEO_SETTING_MENU, mainHandler);
					settingView.showSettingMainMenu(); // video preview
					setupMainMenu.setVisibility(View.VISIBLE);
					setupMainMenu.requestFocus();
				} else if (curMode == APP_STATE_TIMELAPSE_PREVIEW_STILL
						|| curMode == APP_STATE_TIMELAPSE_PREVIEW_VIDEO) {
					settingView = new SettingView(Main.this, mainMenuList,
							SettingView.TIMELAPSE_SETTING_MENU, mainHandler);
					settingView.showSettingMainMenu(); // video preview
					setupMainMenu.setVisibility(View.VISIBLE);
					setupMainMenu.requestFocus();
				} else if (curMode == APP_STATE_TIMELAPSE_VIDEO_CAPTURE) {
					if (toastCapturing == null) {
						toastCapturing = Toast.makeText(Main.this,
								R.string.stream_error_recording,
								Toast.LENGTH_SHORT);
					} else {
						toastCapturing.setText(R.string.stream_error_capturing);
						toastCapturing.setDuration(Toast.LENGTH_SHORT);
					}
					toastCapturing.show();
				} else if (curMode == APP_STATE_TIMELAPSE_STILL_CAPTURE) {
					if (toastCapturing == null) {
						toastCapturing = Toast.makeText(Main.this,
								R.string.stream_error_capturing,
								Toast.LENGTH_SHORT);
					} else {
						toastCapturing.setText(R.string.stream_error_capturing);
						toastCapturing.setDuration(Toast.LENGTH_SHORT);
					}
					toastCapturing.show();
				}
				allowClickButtoms = true;
			}
		});

		timeLapseToggle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (allowClickButtoms == false) {
					return;
				}
				allowClickButtoms = false;
				WriteLogToDevice.writeLog("[Normal] -- Main: ", "curMode ="
						+ curMode);
				if (curMode == APP_STATE_VIDEO_PREVIEW
						|| curMode == APP_STATE_STILL_PREVIEW) {
					if (preview.stop() == false) {
						WriteLogToDevice.writeLog("[Error] -- Main: ",
								"Failed to stop preview");
						return;
					}
					stopMediaStream();

					if (AppProperties.getInstanse().getTimeLapseMode() == TimeLapseMode.TIME_LAPSE_MODE_STILL) {
						curMode = APP_STATE_TIMELAPSE_PREVIEW_STILL;
						changeCameraMode(ICatchPreviewMode.ICH_STILL_PREVIEW_MODE);
						// AppProperties.getInstanse().setTimeLapseMode(TimeLapseMode.TIME_LAPSE_MODE_STILL);
					} else {
						AppProperties.getInstanse().setTimeLapseMode(
								TimeLapseMode.TIME_LAPSE_MODE_VIDEO);
						curMode = APP_STATE_TIMELAPSE_PREVIEW_VIDEO;
						changeCameraMode(ICatchPreviewMode.ICH_VIDEO_PREVIEW_MODE);
					}

					preview.start();
					initUiInterface();
					modeSwitchBeep.start();
				}
				// 0048931 Add by zhangyanhu 2013.12.10
				if (curMode == APP_STATE_VIDEO_CAPTURE
						|| curMode == APP_STATE_STILL_CAPTURE) {
					if (toastRecording == null) {
						toastRecording = Toast.makeText(Main.this,
								R.string.stream_error_recording,
								Toast.LENGTH_SHORT);
					} else {
						toastRecording.setText(R.string.stream_error_recording);
						toastRecording.setDuration(Toast.LENGTH_SHORT);
					}
					toastRecording.show();
				}
				// 0048931 End add by zhangyanhu 2013.12.10
				allowClickButtoms = true;
			}
		});

		stillToggle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (allowClickButtoms == false) {
					return;
				}

				allowClickButtoms = false;
				WriteLogToDevice.writeLog("[Normal] -- Main: ", "curMode ="
						+ curMode);
				if (curMode == APP_STATE_VIDEO_PREVIEW
						|| curMode == APP_STATE_TIMELAPSE_PREVIEW_STILL
						|| curMode == APP_STATE_TIMELAPSE_PREVIEW_VIDEO) {
					if (preview.stop() == false) {
						WriteLogToDevice.writeLog("[Error] -- Main: ",
								"Failed to stop preview");
						return;
					}
					curMode = APP_STATE_STILL_PREVIEW;
					stopMediaStream();
					changeCameraMode(ICatchPreviewMode.ICH_STILL_PREVIEW_MODE);
					preview.start();
					initUiInterface();
					modeSwitchBeep.start();
				}
				// 0048931 Add by zhangyanhu 2013.12.10
				if (curMode == APP_STATE_VIDEO_CAPTURE
						|| curMode == APP_STATE_TIMELAPSE_VIDEO_CAPTURE) {
					if (toastRecording == null) {
						toastRecording = Toast.makeText(Main.this,
								R.string.stream_error_recording,
								Toast.LENGTH_SHORT);
					} else {
						toastRecording.setText(R.string.stream_error_recording);
						toastRecording.setDuration(Toast.LENGTH_SHORT);
					}
					toastRecording.show();
				} else if (curMode == APP_STATE_TIMELAPSE_STILL_CAPTURE) {
					if (toastCapturing == null) {
						toastCapturing = Toast.makeText(Main.this,
								R.string.stream_error_recording,
								Toast.LENGTH_SHORT);
					} else {
						toastCapturing.setText(R.string.stream_error_capturing);
						toastCapturing.setDuration(Toast.LENGTH_SHORT);
					}
					toastCapturing.show();
				}
				// 0048931 End add by zhangyanhu 2013.12.10
				allowClickButtoms = true;
			}
		});

		videoToggle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (allowClickButtoms == false) {
					return;
				}
				allowClickButtoms = false;
				WriteLogToDevice.writeLog("[Normal] -- Main: ", "curMode ="
						+ curMode);
				if (curMode == APP_STATE_STILL_PREVIEW
						|| curMode == APP_STATE_TIMELAPSE_PREVIEW_STILL
						|| curMode == APP_STATE_TIMELAPSE_PREVIEW_VIDEO) {
					if (preview.stop() == false) {
						return;
					}
					curMode = APP_STATE_VIDEO_PREVIEW;
					stopMediaStream();

					changeCameraMode(ICatchPreviewMode.ICH_VIDEO_PREVIEW_MODE);
					preview.start();

					initUiInterface();
					modeSwitchBeep.start();
				} else if (curMode == APP_STATE_STILL_CAPTURE
						|| curMode == APP_STATE_TIMELAPSE_STILL_CAPTURE) {
					if (toastCapturing == null) {
						toastCapturing = Toast.makeText(Main.this,
								R.string.stream_error_capturing,
								Toast.LENGTH_SHORT);
					} else {
						toastCapturing.setText(R.string.stream_error_capturing);
						toastCapturing.setDuration(Toast.LENGTH_SHORT);
					}
					toastCapturing.show();
				} else if (curMode == APP_STATE_TIMELAPSE_VIDEO_CAPTURE) {
					if (toastCapturing == null) {
						toastCapturing = Toast.makeText(Main.this,
								R.string.stream_error_recording,
								Toast.LENGTH_SHORT);
					} else {
						toastCapturing.setText(R.string.stream_error_capturing);
						toastCapturing.setDuration(Toast.LENGTH_SHORT);
					}
					toastCapturing.show();
				}
				allowClickButtoms = true;
			}
		});

		photo_hd_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (allowClickButtoms == false) {
					return;
				}
				allowClickButtoms = false;
				WriteLogToDevice.writeLog("[Normal] -- Main: ",
						"photo_hd_btn.setOnClickListener");
				if (curMode == APP_STATE_STILL_PREVIEW
						|| curMode == APP_STATE_TIMELAPSE_PREVIEW_STILL) {
					if (settingView == null) {
						settingView = new SettingView(Main.this, mainHandler);
					}
					preview.stop();
					// stopMediaStream();
					settingView
							.showSettingDialog(SettingView.SETTING_OPTION_IMAGE_SIZE);
					// showVideoHdOptionDialog();
				} else if (curMode == APP_STATE_STILL_CAPTURE
						|| curMode == APP_STATE_TIMELAPSE_STILL_CAPTURE) {
					if (toastRecording == null) {
						toastRecording = Toast.makeText(Main.this,
								R.string.stream_error_capturing,
								Toast.LENGTH_SHORT);
					} else {
						toastRecording.setText(R.string.stream_error_capturing);
						toastRecording.setDuration(Toast.LENGTH_SHORT);
					}
					toastRecording.show();
				}
				allowClickButtoms = true;
			}
		});

		video_hd_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (allowClickButtoms == false) {
					return;
				}
				allowClickButtoms = false;
				// 0048931 Modify by zhangyanhu 2013.12.10
				if (curMode == APP_STATE_VIDEO_PREVIEW
						|| curMode == APP_STATE_TIMELAPSE_PREVIEW_VIDEO) {
					if (settingView == null) {
						settingView = new SettingView(Main.this, mainHandler);
					}
					preview.stop();
					// stopMediaStream();
					settingView
							.showSettingDialog(SettingView.SETTING_OPTION_VIDEO_SIZE);
					// showVideoHdOptionDialog();
				} else if (curMode == APP_STATE_VIDEO_CAPTURE
						|| curMode == APP_STATE_TIMELAPSE_VIDEO_CAPTURE) {
					if (toastRecording == null) {
						toastRecording = Toast.makeText(Main.this,
								R.string.stream_error_recording,
								Toast.LENGTH_SHORT);
					} else {
						toastRecording.setText(R.string.stream_error_recording);
						toastRecording.setDuration(Toast.LENGTH_SHORT);
					}
					toastRecording.show();
				}
				allowClickButtoms = true;
				// 0048931 End modify by zhangyanhu 2013.12.10
			}
		});

		delay_capture_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (allowClickButtoms == false) {
					return;
				}
				allowClickButtoms = false;
				if (curMode == APP_STATE_STILL_CAPTURE) {
					if (toastCapturing == null) {
						toastCapturing = Toast.makeText(Main.this,
								R.string.stream_error_capturing,
								Toast.LENGTH_SHORT);
					} else {
						toastCapturing.setText(R.string.stream_error_capturing);
						toastCapturing.setDuration(Toast.LENGTH_SHORT);
					}
					toastCapturing.show();
				} else {
					if (settingView == null) {
						settingView = new SettingView(Main.this, mainHandler);
					}
					settingView
							.showSettingDialog(SettingView.SETTING_OPTION_DELAY_TIME);
				}
				allowClickButtoms = true;
			}
		});

		zoomBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			/**
			 * 当用户结束对滑块滑动时,调用该方法
			 */
			public void onStopTrackingTouch(SeekBar seekBar) {
				ZoomBar zoomBar = (ZoomBar) seekBar;
				Log.d("tigertiger", "----------lastZoomRate = " + lastZoomRate);
				Log.d("tigertiger",
						"----------------seekBar.getZoomProgress = "
								+ (zoomBar.getZoomProgress()));

				ZoomThread zoomThread = new ZoomThread(mainHandler,
						lastZoomRate, zoomBar);
				zoomThread.start();
				progressDialog = new ProgressDialog(Main.this);
				progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				progressDialog.setMessage(Main.this
						.getString(R.string.action_processing));
				progressDialog.setCancelable(false);
				progressDialog.show();
			}

			/**
			 * 当用户开始滑动滑块时调用该方法
			 */
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

				if (allowClickButtoms == false) {
					WriteLogToDevice.writeLog("[Abnormal] -- Main: ",
							"do not allow to response button clicking");
					return;
				}
				if (zoomTimer != null) {
					zoomTimer.cancel();
				}
				allowClickButtoms = true;
			}

			/**
			 * 当进度条发生变化时调用该方法
			 */
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				// if(zoomTimer != null){
				// zoomTimer.cancel();
				// }
			}

		});

		zoomIn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (allowClickButtoms == false) {
					WriteLogToDevice.writeLog("[Abnormal] -- Main: ",
							"do not allow to response button clicking");
					return;
				}

				if (zoomTimer != null) {
					zoomTimer.cancel();
				}
				if (curMode == APP_STATE_STILL_CAPTURE
						|| curMode == APP_STATE_TIMELAPSE_STILL_CAPTURE) {
					if (toastWaitCapture == null) {
						toastWaitCapture = Toast.makeText(Main.this,
								R.string.stream_exit_wait, Toast.LENGTH_SHORT);
					} else {
						toastWaitCapture.setText(R.string.stream_exit_wait);
						toastWaitCapture.setDuration(Toast.LENGTH_SHORT);
					}
					toastWaitCapture.show();
					allowClickButtoms = true;
					return;
				}

				if (lastZoomRate >= cameraProperties.getMaxZoomRatio()) {
					zoomTimer = new Timer(true);
					zoomTimer.schedule(new zoomTimerTask(),
							zoomBarDisplayDuration);
					allowClickButtoms = true;
					return;
				}
				cameraAction.zoomIn();
				lastZoomRate = cameraProperties.getCurrentZoomRatio();
				zoomBar.setProgress(lastZoomRate);
				zoomRate.setText("x" + lastZoomRate / 10.0f);
				zoomTimer = new Timer(true);
				zoomTimer.schedule(new zoomTimerTask(), zoomBarDisplayDuration);
				allowClickButtoms = true;
			}

		});

		zoomOut.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (allowClickButtoms == false) {
					WriteLogToDevice.writeLog("[Abnormal] -- Main: ",
							"do not allow to response button clicking");
					return;
				}

				if (zoomTimer != null) {
					zoomTimer.cancel();
				}

				if (curMode == APP_STATE_STILL_CAPTURE
						|| curMode == APP_STATE_TIMELAPSE_STILL_CAPTURE) {
					if (toastWaitCapture == null) {
						toastWaitCapture = Toast.makeText(Main.this,
								R.string.stream_exit_wait, Toast.LENGTH_SHORT);
					} else {
						toastWaitCapture.setText(R.string.stream_exit_wait);
						toastWaitCapture.setDuration(Toast.LENGTH_SHORT);
					}
					toastWaitCapture.show();
					allowClickButtoms = true;
					return;
				}

				if (lastZoomRate <= zoomMinRate) {
					zoomTimer = new Timer(true);
					zoomTimer.schedule(new zoomTimerTask(),
							zoomBarDisplayDuration);
					allowClickButtoms = true;
					return;
				}
				cameraAction.zoomOut();
				lastZoomRate = cameraProperties.getCurrentZoomRatio();
				zoomBar.setProgress(lastZoomRate);
				zoomRate.setText("x" + lastZoomRate / 10.0f);
				zoomTimer = new Timer(true);
				zoomTimer.schedule(new zoomTimerTask(), zoomBarDisplayDuration);
				allowClickButtoms = true;
			}

		});

		preview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (allowClickButtoms == false) {
					WriteLogToDevice.writeLog("[Abnormal] -- Main: ",
							"do not allow to response button clicking");
					return;
				}
				if (zoomTimer != null) {
					zoomTimer.cancel();
				}
				if (curMode == APP_STATE_STILL_CAPTURE
						|| curMode == APP_STATE_TIMELAPSE_STILL_CAPTURE
						|| curMode == APP_STATE_TIMELAPSE_VIDEO_CAPTURE
						|| (cameraProperties
								.hasFuction(ICatchCameraProperty.ICH_CAP_DATE_STAMP) == true && ICatchDateStamp.ICH_DATE_STAMP_OFF != cameraProperties
								.getCurrentDateStamp())) {
					zoomLayout.setVisibility(View.GONE);
					allowClickButtoms = true;
					return;
				}

				if (cameraProperties
						.hasFuction(ICatchCameraProperty.ICH_CAP_DIGITAL_ZOOM)) {
					zoomLayout.setVisibility(View.VISIBLE);
				}
				zoomTimer = new Timer(true);
				zoomTimer.schedule(new zoomTimerTask(), zoomBarDisplayDuration);
				allowClickButtoms = true;
			}

		});

	}

	private void initStatus() {
		// set default UI to display
		WriteLogToDevice.writeLog("[Normal] -- Main: ", "initStatus");
		sdCardFullWarning = false;
		ExitApp.getInstance().addActivity(this);
		executor = Executors.newSingleThreadExecutor();

		mainTimerHandler = new MyTimerHandler();
		globalApp = GlobalApp.getInstance();
		globalApp.setAppContext(getApplicationContext());
		globalApp.setCurrentApp(Main.this);

		if (cameraProperties.cameraModeSupport(ICatchMode.ICH_MODE_VIDEO) == false) {
			videoToggle.setVisibility(View.GONE);
			curMode = APP_STATE_STILL_PREVIEW;
		} else {
			curMode = APP_STATE_VIDEO_PREVIEW;
		}
		// zoomBar.setMinValue(zoomMinRate);
		zoomLayout.setVisibility(View.GONE);
		zoomBar.setMinValue(zoomMinRate);
		zoomBar.setMax(cameraProperties.getMaxZoomRatio());
		lastZoomRate = cameraProperties.getCurrentZoomRatio();
		zoomBar.setProgress(lastZoomRate);
		Log.d("tigertiger", "first get lastZoomRate = " + lastZoomRate);
		Log.d("tigertiger",
				"first get zoomBar.getZoomProgress = "
						+ zoomBar.getZoomProgress());
		setBatteryLevelIcon();

		if (cameraProperties.cameraModeSupport(ICatchMode.ICH_MODE_TIMELAPSE) == false) {
			timeLapseToggle.setVisibility(View.GONE);
			RelativeLayout.LayoutParams stillToggleLayoutParams = (RelativeLayout.LayoutParams) stillToggle
					.getLayoutParams();
			stillToggleLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			stillToggleLayoutParams.rightMargin = 5;
			stillToggle.setLayoutParams(stillToggleLayoutParams);
		}
		if (cameraProperties
				.hasFuction(ICatchCameraProperty.ICH_CAP_SLOW_MOTION)
				&& cameraProperties.getCurrentSlowMotion() == SlowMotion.SLOW_MOTION_ON
				&& curMode == APP_STATE_VIDEO_PREVIEW) {
			slowMotion.setVisibility(View.VISIBLE);
		}
		if (cameraProperties
				.hasFuction(ICatchCameraProperty.ICH_CAP_UPSIDE_DOWN)
				&& cameraProperties.getCurrentUpsideDown() == Upside.UPSIDE_ON) {
			carMode.setVisibility(View.VISIBLE);
		}
		initUiInterface();
		sdkEvent = new SDKEvent(mainHandler);
		wifiTool = new WifiCheck();
		wifiTool.openConnectCheck();
		// for test
		// load Menu data and set listener
		// wifiTool.checkWifiPolicy();
		ExceptionCheck exceptionCheck = new ExceptionCheck();
		exceptionCheck.startExceptionCheck();
		if (cameraProperties.hasFuction(0xD7F0)) {
			if (cameraProperties.setCaptureDelayMode(1))
				captureDelayMode = true;
		}

		AppCfgParameter configuration = new AppCfgParameter();
		configuration.setPreviewCacheParam(132);
		WriteLogToDevice.writeLog("[Normal] -- Main: ", "end initStatus");
	}

	private class MainHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			// Added by zhangyanhu 2013.11.27
			case MESSAGE_QUIT_APP:
				WriteLogToDevice.writeLog("[Normal] -- Main: ",
						"receive QUIT_APP");
				if (cameraState.isMovieRecording() == false) {
					startMoiveRecording();
				}
				sdkEvent.delEventListener(ICatchEventID.ICH_EVENT_SDCARD_FULL);
				sdkEvent.delEventListener(ICatchEventID.ICH_EVENT_BATTERY_LEVEL_CHANGED);
				sdkEvent.delEventListener(ICatchEventID.ICH_EVENT_CAPTURE_COMPLETE);
				sdkEvent.delEventListener(ICatchEventID.ICH_EVENT_CAPTURE_START);
				sdkEvent.delEventListener(ICatchEventID.ICH_EVENT_VIDEO_OFF);
				sdkEvent.delEventListener(ICatchEventID.ICH_EVENT_FILE_ADDED);
				sdkEvent.delEventListener(ICatchEventID.ICH_EVENT_VIDEO_ON);
				sdkEvent.delEventListener(ICatchEventID.ICH_EVENT_CONNECTION_DISCONNECTED);
				sdkEvent.delEventListener(ICatchEventID.ICH_EVENT_TIMELAPSE_STOP);
				sdkEvent.delCustomizeEventListener(0x5001);

				preview.stop();
				stopMediaStream();
				SDKSession.destroySession();
				// finish();
				// stopMediaStream();
				ExitApp.getInstance().exit();
				break;
			// case GlobalApp.MESSAGE_CAPTURE_COMPLETED:
			// WriteLogToDevice.writeLog("[Normal] -- Main: ",
			// "receive CAPTURE_COMPLETED");
			// Log.d("tigertiger", "receive CAPTURE_COMPLETED");
			// ICatchMJPGStreamParam param = new ICatchMJPGStreamParam();
			// previewStream.startMediaStream(param,
			// ICatchPreviewMode.ICH_STILL_PREVIEW_MODE);
			// preview.start();
			// captureBtn.setEnabled(true);
			// captureBtn.setBackgroundResource(R.drawable.camera_start);
			// /**
			// * Added by zhangyanhu C01012,2014-2-9
			// */
			// photo_hd_txt.setText(uiDisplayResource.getRemainImageNum());
			// curMode = APP_STATE_STILL_PREVIEW;
			// Log.d("tigertiger", "end CAPTURE_COMPLETED");
			// WriteLogToDevice.writeLog("[Normal] -- Main: ",
			// "end receive CAPTURE_COMPLETED");
			// break;
			case GlobalApp.EVENT_BATTERY_ELETRIC_CHANGED:
				WriteLogToDevice.writeLog("[Normal] -- Main: ",
						"receive EVENT_BATTERY_ELETRIC_CHANGED power ="
								+ msg.arg1);
				setBatteryLevelIcon();
				break;
			case GlobalApp.EVENT_CONNECTION_FAILURE:
				WriteLogToDevice.writeLog("[Normal] -- Main: ",
						"receive EVENT_CONNECTION_FAILURE");
				if (preview != null) {
					preview.stop();
				}
				break;
			case GlobalApp.EVENT_SD_CARD_FULL:
				WriteLogToDevice.writeLog("[Normal] -- Main: ",
						"receive EVENT_SD_CARD_FULL");
				if (curMode == APP_STATE_VIDEO_CAPTURE
						|| curMode == APP_STATE_TIMELAPSE_VIDEO_CAPTURE) {
					Toast.makeText(Main.this, R.string.dialog_card_full,
							Toast.LENGTH_SHORT).show();
				} else if (curMode == APP_STATE_VIDEO_PREVIEW
						|| curMode == APP_STATE_TIMELAPSE_PREVIEW_VIDEO) {
					sdCardIsFullAlert();
				} else if (curMode == APP_STATE_STILL_PREVIEW
						|| curMode == APP_STATE_TIMELAPSE_PREVIEW_STILL) {
					if (forbidePhotoCapture() == true) {
						sdCardIsFullAlert();
					}
				}
				break;
			case GlobalApp.EVENT_VIDEO_OFF:
				WriteLogToDevice.writeLog("[Normal] -- Main: ",
						"receive EVENT_VIDEO_OFF:curMode=" + curMode);
				if (curMode == APP_STATE_VIDEO_CAPTURE
						|| curMode == APP_STATE_TIMELAPSE_VIDEO_CAPTURE) {
					if (videoCaptureTimer != null) {
						videoCaptureTimer.cancel();
					}

					cameraAction.stopVideoCapture();
					// changeCameraMode(ICatchPreviewMode.ICH_VIDEO_PREVIEW_MODE);
					video_hd_txt.setText(uiDisplayResource
							.getRecordingRemainTime());
					captureBtn
							.setBackgroundResource(R.drawable.video_recording_btn_on);
					curMode = APP_STATE_VIDEO_PREVIEW;
				}
				break;
			case GlobalApp.EVENT_VIDEO_ON:
				WriteLogToDevice.writeLog("[Normal] -- Main: ",
						"receive EVENT_VIDEO_ON:curMode =" + curMode);
				// video from camera when file exceeds 4g
				if (curMode == APP_STATE_VIDEO_PREVIEW) {
					curMode = APP_STATE_VIDEO_CAPTURE;
					// changeCameraMode(ICatchPreviewMode.ICH_VIDEO_PREVIEW_MODE);
					TimerTask task = new TimerTask() {
						@Override
						public void run() {
							if (videoCaptureTimerLamp) {
								videoCaptureTimerLamp = false;
								sendTimerMsg(MESSAGE_VIDEO_CAPTURE_ON, 1);

							} else {
								videoCaptureTimerLamp = true;
								sendTimerMsg(MESSAGE_VIDEO_CAPTURE_ON, 0);
							}
						}
					};
					if (videoCaptureTimer != null) {
						videoCaptureTimer.cancel();
					}
					videoCaptureTimer = new Timer(true);
					videoCaptureTimer.schedule(task, 0, 1000);
				} else if (curMode == APP_STATE_TIMELAPSE_PREVIEW_VIDEO) {

					curMode = APP_STATE_TIMELAPSE_VIDEO_CAPTURE;
					// changeCameraMode(ICatchPreviewMode.ICH_VIDEO_PREVIEW_MODE);
					TimerTask task = new TimerTask() {
						@Override
						public void run() {
							if (videoCaptureTimerLamp) {
								videoCaptureTimerLamp = false;
								sendTimerMsg(MESSAGE_VIDEO_CAPTURE_ON, 1);

							} else {
								videoCaptureTimerLamp = true;
								sendTimerMsg(MESSAGE_VIDEO_CAPTURE_ON, 0);
							}
						}
					};
					if (videoCaptureTimer != null) {
						videoCaptureTimer.cancel();
					}
					videoCaptureTimer = new Timer(true);
					videoCaptureTimer.schedule(task, 0, 1000);
				}
				break;
			case GlobalApp.EVENT_CAPTURE_START:
				WriteLogToDevice.writeLog("[Normal] -- Main: ",
						"receive EVENT_CAPTURE_START:curMode=" + curMode);
				if (curMode != APP_STATE_TIMELAPSE_STILL_CAPTURE) {
					return;
				}
				// if (captureDialog != null) {
				// captureDialog.dismiss();
				// }

				// stillCaptureStartBeep.start();
				if (toastTimeLapse == null) {
					toastTimeLapse = Toast.makeText(Main.this,
							R.string.capture_start, Toast.LENGTH_SHORT);
				} else {
					toastTimeLapse.setText(R.string.capture_start);
					toastTimeLapse.setDuration(Toast.LENGTH_SHORT);
				}
				toastTimeLapse.show();
				break;
			case GlobalApp.EVENT_CAPTURE_COMPLETED:
				WriteLogToDevice.writeLog("[Normal] -- Main: ",
						"receive EVENT_CAPTURE_COMPLETED:curMode=" + curMode);
				if (curMode == APP_STATE_STILL_CAPTURE) {
					Log.d("tigertiger", "receive CAPTURE_COMPLETED");
					changeCameraMode(ICatchPreviewMode.ICH_STILL_PREVIEW_MODE);
					preview.start();
					captureBtn.setEnabled(true);
					captureBtn
							.setBackgroundResource(R.drawable.still_capture_btn);
					/**
					 * Added by zhangyanhu C01012,2014-2-9
					 */
					photo_hd_txt.setText(uiDisplayResource.getRemainImageNum());
					curMode = APP_STATE_STILL_PREVIEW;
					Log.d("tigertiger", "end CAPTURE_COMPLETED");
					return;
				}
				if (curMode == APP_STATE_TIMELAPSE_STILL_CAPTURE) {
					captureBtn.setEnabled(true);
					captureBtn
							.setBackgroundResource(R.drawable.still_capture_btn);
					/**
					 * Added by zhangyanhu C01012,2014-2-9
					 */
					photo_hd_txt.setText(uiDisplayResource.getRemainImageNum());

					if (toastTimeLapse == null) {
						toastTimeLapse = Toast.makeText(Main.this,
								R.string.capture_completed, Toast.LENGTH_SHORT);
					} else {
						toastTimeLapse.setText(R.string.capture_completed);
						toastTimeLapse.setDuration(Toast.LENGTH_SHORT);
					}
					toastTimeLapse.show();
				}
				break;
			case GlobalApp.EVENT_FILE_ADDED:
				WriteLogToDevice.writeLog("[Normal] -- Main: ",
						"EVENT_FILE_ADDED");
				if (curMode == APP_STATE_TIMELAPSE_STILL_CAPTURE) {
					lapseTime = 0;
				}
				break;

			case GlobalApp.EVENT_TIME_LAPSE_STOP:
				WriteLogToDevice.writeLog("[Normal] -- Main: ",
						"receive EVENT_TIME_LAPSE_STOP:curMode=" + curMode);
				if (curMode == APP_STATE_TIMELAPSE_VIDEO_CAPTURE) {
					if (videoCaptureTimer != null) {
						videoCaptureTimer.cancel();
					}

					cameraAction.stopTimeLapse();
					// changeCameraMode(ICatchPreviewMode.ICH_VIDEO_PREVIEW_MODE);
					video_hd_txt.setText(uiDisplayResource
							.getRecordingRemainTime());
					captureBtn
							.setBackgroundResource(R.drawable.video_recording_btn_on);
					curMode = APP_STATE_TIMELAPSE_PREVIEW_VIDEO;
				} else if (curMode == APP_STATE_TIMELAPSE_STILL_CAPTURE) {
					cameraAction.stopTimeLapse();
					// changeCameraMode(ICatchPreviewMode.ICH_VIDEO_PREVIEW_MODE);
					photo_hd_txt.setText(uiDisplayResource.getRemainImageNum());
					curMode = APP_STATE_TIMELAPSE_PREVIEW_STILL;
				}
				break;
			case GlobalApp.MESSAGE_UPDATE_UI_IMAGE_SIZE:
				photo_hd_txt.setText(uiDisplayResource.getRemainImageNum());
				photo_hd_btn
						.setText(uiDisplayResource.getCurrentImageSize().uiStringInPreview);

				// changeCameraMode(ICatchPreviewMode.ICH_STILL_PREVIEW_MODE);
				preview.start();
				Toast.makeText(Main.this, R.string.stream_set_complete,
						Toast.LENGTH_SHORT).show();
				break;
			case GlobalApp.MESSAGE_UPDATE_UI_VIDEO_SIZE:
				video_hd_txt
						.setText(uiDisplayResource.getRecordingRemainTime());
				video_hd_btn
						.setText(uiDisplayResource.getCurrentVideoSize().uiStringInPreview);

				// changeCameraMode(ICatchPreviewMode.ICH_VIDEO_PREVIEW_MODE);
				preview.start();
				Toast.makeText(Main.this, R.string.stream_set_complete,
						Toast.LENGTH_SHORT).show();
				break;
			case GlobalApp.MESSAGE_UPDATE_UI_CAPTURE_DELAY:
				WriteLogToDevice.writeLog("[Normal] -- Main: ",
						"receive MESSAGE_UPDATE_UI_CAPTURE_DELAY");
				delay_capture_txt.setText(uiDisplayResource
						.getCurrentCaptureDelay().uiStringInPreview);
				if (cameraProperties
						.hasFuction(ICatchCameraProperty.ICH_CAP_BURST_NUMBER)) {
					// WriteLogToDevice.writeLog("[Normal] -- debug: ",
					// "begin burst_status.setBackgroundResource(uiDisplayResource.getCurrentBurstNum().iconID);");
					burst_status.setBackgroundResource(uiDisplayResource
							.getCurrentBurstNum().iconID);
					// WriteLogToDevice.writeLog("[Normal] -- debug: ",
					// "end burst_status.setBackgroundResource(uiDisplayResource.getCurrentBurstNum().iconID);");
				}
				Toast.makeText(Main.this, R.string.stream_set_complete,
						Toast.LENGTH_SHORT).show();
				break;
			case GlobalApp.MESSAGE_SETTING_TIMELAPSE_STILL_MODE:
				WriteLogToDevice.writeLog("[Normal] -- Main: ",
						"receive MESSAGE_SETTING_TIMELAPSE_CAPTURE_MODE");
				if (preview.stop() == false) {
					return;
				}
				curMode = APP_STATE_TIMELAPSE_PREVIEW_STILL;
				stopMediaStream();

				changeCameraMode(ICatchPreviewMode.ICH_STILL_PREVIEW_MODE);
				preview.start();
				// curMode = APP_STATE_TIMELAPSE_PREVIEW_STILL;
				captureBtn.setBackgroundResource(R.drawable.still_capture_btn);
				break;
			case GlobalApp.MESSAGE_SETTING_TIMELAPSE_VIDEO_MODE:
				WriteLogToDevice.writeLog("[Normal] -- Main: ",
						"receive MESSAGE_SETTING_TIMELAPSE_VIDEO_MODE");
				if (preview.stop() == false) {
					return;
				}
				curMode = APP_STATE_TIMELAPSE_PREVIEW_VIDEO;
				stopMediaStream();

				changeCameraMode(ICatchPreviewMode.ICH_VIDEO_PREVIEW_MODE);
				preview.start();
				// curMode = APP_STATE_TIMELAPSE_PREVIEW_VIDEO;
				captureBtn
						.setBackgroundResource(R.drawable.video_recording_btn_on);
				break;
			case MESSAGE_ZOOM_BAR_DISAPPEAR:
				zoomLayout.setVisibility(View.GONE);
				break;
			case GlobalApp.MESSAGE_ZOOM_COMPLETED:
				WriteLogToDevice.writeLog("[Normal] -- Main: ",
						"receive MESSAGE_ZOOM_COMPLETED");
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				lastZoomRate = msg.arg1;
				Log.d("tigertiger", "------------lastZoomRate =" + lastZoomRate);
				Log.d("tigertiger", "------------msg.arg2 =" + msg.arg2);
				zoomBar.setProgress(lastZoomRate);
				zoomRate.setText("x" + lastZoomRate / 10.0f);
				if (zoomTimer != null) {
					zoomTimer.cancel();
				}
				zoomTimer = new Timer(true);
				zoomTimer.schedule(new zoomTimerTask(), zoomBarDisplayDuration);
				break;
			case GlobalApp.MESSAGE_UPDATE_UI_UPSIDE_DOWN:
				if (cameraProperties.getCurrentUpsideDown() == Upside.UPSIDE_OFF) {
					carMode.setVisibility(View.GONE);
				} else if (cameraProperties.getCurrentUpsideDown() == Upside.UPSIDE_ON) {
					carMode.setVisibility(View.VISIBLE);
				}
				// video_hd_txt.setText(uiDisplayResource.getRecordingRemainTime());
				// video_hd_btn.setText(uiDisplayResource.getCurrentVideoSize().uiStringInPreview);

				// changeCameraMode(ICatchPreviewMode.ICH_VIDEO_PREVIEW_MODE);
				// preview.start();
				// Toast.makeText(Main.this, R.string.stream_set_complete,
				// Toast.LENGTH_SHORT).show();
				break;
			case GlobalApp.MESSAGE_UPDATE_UI_SLOW_MOTION:
				if (cameraProperties.getCurrentSlowMotion() == SlowMotion.SLOW_MOTION_OFF) {
					slowMotion.setVisibility(View.GONE);
				} else if (cameraProperties.getCurrentSlowMotion() == SlowMotion.SLOW_MOTION_ON
						&& curMode == APP_STATE_VIDEO_PREVIEW) {
					slowMotion.setVisibility(View.VISIBLE);
				}

				// video_hd_txt.setText(uiDisplayResource.getRecordingRemainTime());
				// video_hd_btn.setText(uiDisplayResource.getCurrentVideoSize().uiStringInPreview);

				// changeCameraMode(ICatchPreviewMode.ICH_VIDEO_PREVIEW_MODE);
				// preview.start();
				// Toast.makeText(Main.this, R.string.stream_set_complete,
				// Toast.LENGTH_SHORT).show();
				break;
			case GlobalApp.EVENT_VIDEO_RECORDING_TIME:
				Log.d("1111", "receive UIDisplayResource.secondsToHours");
				startMovieRecordingTimer(0);
				/*
				 * if(recordingTimer != null){ recordingTimer.cancel(); }
				 * if(curMode == APP_STATE_TIMELAPSE_VIDEO_CAPTURE){ lapseTime =
				 * 0;
				 * recordingTime.setText(UIDisplayResource.secondsToHours(lapseTime
				 * )); recordingTimer = new Timer(true);
				 * 
				 * TimerTask timerTask = new TimerTask(){
				 * 
				 * @Override public void run() { // TODO Auto-generated method
				 * stub
				 * //recordingTime.setText(UIDisplayResource.secondsToHours(
				 * lapseTime)); lapseTime++;
				 * mainHandler.obtainMessage(MESSAGE_UPDATE_RECORDING_TIME
				 * ,lapseTime,0).sendToTarget(); }
				 * 
				 * }; recordingTimer.schedule(timerTask, 0,1000); }
				 */
				break;
			case MESSAGE_UPDATE_RECORDING_TIME:
				Log.d("1111",
						"receive MESSAGE_UPDATE_RECORDING_TIME msg.arg1 ="
								+ msg.arg1);
				if (curMode == APP_STATE_VIDEO_CAPTURE) {
					recordingTime.setText(UIDisplayResource
							.secondsToHours(msg.arg1));
					recordingTime.setVisibility(View.VISIBLE);
				}
				break;
			default:
				super.handleMessage(msg);
				break;
			}
		}
	};

	private class MyTimerHandler extends Handler {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE_VIDEO_CAPTURE_ON:
				if (curMode == APP_STATE_VIDEO_CAPTURE
						|| curMode == APP_STATE_TIMELAPSE_VIDEO_CAPTURE) {
					if (msg.arg1 == 1) {
						captureBtn
								.setBackgroundResource(R.drawable.video_recording_btn_on);
					} else {
						captureBtn
								.setBackgroundResource(R.drawable.video_recording_btn_off);
					}
				}
				break;
			default:
				break;
			}
		};
	};

	private void sendOkMsg(int what) {
		mainHandler.obtainMessage(what).sendToTarget();
	}

	private void sendTimerMsg(int what, int arg1) {
		mainTimerHandler.obtainMessage(what, arg1, 0).sendToTarget();
	}

	@Override
	protected void onDestroy() {
		WriteLogToDevice.writeLog("[Normal] -- Main: ", "main:onDestroy");
		super.onDestroy();
		stopMediaStream();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		// preview.setSurfaceViewArea();
	}

	@Override
	protected void onStart() {
		super.onStart();
		
		WriteLogToDevice.writeLog("[Normal] -- Main: ", "onStart");
		globalApp.setAppContext(getApplicationContext());
		sdCardFullWarning = false;
		lastCilckTime = 0;
		allowClickButtoms = true;
		intentLock = false;
		photo_hd_txt.setText(uiDisplayResource.getRemainImageNum());
		video_hd_txt.setText(uiDisplayResource.getRecordingRemainTime());
		mainTimerHandler = new MyTimerHandler();

		if (cameraState.isMovieRecording() == true) {
			// camera is in movie recording state, app should in movie
			// recording state
			WriteLogToDevice
					.writeLog("[Normal] -- Main: ", "restart recording");
			curMode = APP_STATE_VIDEO_CAPTURE;
			initUiInterface();
			changeCameraMode(ICatchPreviewMode.ICH_VIDEO_PREVIEW_MODE);
			TimerTask task = new TimerTask() {
				@Override
				public void run() {
					if (videoCaptureTimerLamp) {
						videoCaptureTimerLamp = false;
						sendTimerMsg(MESSAGE_VIDEO_CAPTURE_ON, 1);

					} else {
						videoCaptureTimerLamp = true;
						sendTimerMsg(MESSAGE_VIDEO_CAPTURE_ON, 0);
					}
				}
			};
			mainTimerHandler = new MyTimerHandler();
			videoCaptureTimer = new Timer(true);
			videoCaptureTimer.schedule(task, 0, 1000);

			startMovieRecordingTimer(cameraProperties.getVideoRecordingTime());

			// recordingTime.setText(UIDisplayResource.secondsToHours(cameraProperties.getRecordingRemainTime()));
		} else if (cameraState.isTimeLapseVideoOn()) {
			WriteLogToDevice.writeLog("[Normal] -- Main: ",
					"restart  time lapse recording");
			curMode = APP_STATE_TIMELAPSE_VIDEO_CAPTURE;
			initUiInterface();
			AppProperties.getInstanse().setTimeLapseMode(
					TimeLapseMode.TIME_LAPSE_MODE_VIDEO);
			changeCameraMode(ICatchPreviewMode.ICH_VIDEO_PREVIEW_MODE);
			TimerTask task = new TimerTask() {
				@Override
				public void run() {
					if (videoCaptureTimerLamp) {
						videoCaptureTimerLamp = false;
						sendTimerMsg(MESSAGE_VIDEO_CAPTURE_ON, 1);

					} else {
						videoCaptureTimerLamp = true;
						sendTimerMsg(MESSAGE_VIDEO_CAPTURE_ON, 0);
					}
				}
			};
			mainTimerHandler = new MyTimerHandler();
			videoCaptureTimer = new Timer(true);
			videoCaptureTimer.schedule(task, 0, 1000);
			// initUiInterface();
		} else if (cameraState.isTimeLapseStillOn()) {
			WriteLogToDevice.writeLog("[Normal] -- Main: ",
					"restart time lapse still");
			AppProperties.getInstanse().setTimeLapseMode(
					TimeLapseMode.TIME_LAPSE_MODE_STILL);
			changeCameraMode(ICatchPreviewMode.ICH_STILL_PREVIEW_MODE);
			curMode = APP_STATE_TIMELAPSE_STILL_CAPTURE;
			initUiInterface();
		} else if (curMode == APP_STATE_VIDEO_PREVIEW) {
			WriteLogToDevice.writeLog("[Normal] -- Main: ",
					"curMode == APP_STATE_VIDEO_PREVIEW");
			changeCameraMode(ICatchPreviewMode.ICH_VIDEO_PREVIEW_MODE);
			// normal state, app show preview
		} else if (curMode == APP_STATE_TIMELAPSE_PREVIEW_VIDEO) {
			WriteLogToDevice.writeLog("[Normal] -- Main: ",
					"curMode == APP_STATE_TIMELAPSE_PREVIEW_VIDEO");
			AppProperties.getInstanse().setTimeLapseMode(
					TimeLapseMode.TIME_LAPSE_MODE_VIDEO);
			changeCameraMode(ICatchPreviewMode.ICH_VIDEO_PREVIEW_MODE);
			// normal state, app show preview
		} else if (curMode == APP_STATE_TIMELAPSE_PREVIEW_STILL) {
			WriteLogToDevice.writeLog("[Normal] -- Main: ",
					"curMode == APP_STATE_TIMELAPSE_PREVIEW_STILL");
			AppProperties.getInstanse().setTimeLapseMode(
					TimeLapseMode.TIME_LAPSE_MODE_STILL);
			changeCameraMode(ICatchPreviewMode.ICH_STILL_PREVIEW_MODE);
			// normal state, app show preview
		} else if (curMode == APP_STATE_STILL_PREVIEW) {
			WriteLogToDevice.writeLog("[Normal] -- Main: ",
					"curMode == ICH_STILL_PREVIEW_MODE");
			changeCameraMode(ICatchPreviewMode.ICH_STILL_PREVIEW_MODE);
		}
		preview.start();

		IntentFilter wifiSSFilter = new IntentFilter(
				WifiManager.RSSI_CHANGED_ACTION);
		wifiSSReceiver = new WifiSSReceiver();
		this.registerReceiver(wifiSSReceiver, wifiSSFilter);
		
		sdkEvent.addEventListener(ICatchEventID.ICH_EVENT_SDCARD_FULL);
		sdkEvent.addEventListener(ICatchEventID.ICH_EVENT_BATTERY_LEVEL_CHANGED);
		sdkEvent.addEventListener(ICatchEventID.ICH_EVENT_VIDEO_OFF);
		sdkEvent.addEventListener(ICatchEventID.ICH_EVENT_VIDEO_ON);
		sdkEvent.addEventListener(ICatchEventID.ICH_EVENT_CAPTURE_START);
		sdkEvent.addEventListener(ICatchEventID.ICH_EVENT_CAPTURE_COMPLETE);
		sdkEvent.addEventListener(ICatchEventID.ICH_EVENT_FILE_ADDED);
		sdkEvent.addEventListener(ICatchEventID.ICH_EVENT_CONNECTION_DISCONNECTED);
		sdkEvent.addEventListener(ICatchEventID.ICH_EVENT_TIMELAPSE_STOP);
		sdkEvent.addCustomizeEvent(0x5001);// vidoe recording event
	
	}

	@Override
	protected void onStop() {
		super.onStop();
		WriteLogToDevice.writeLog("[Normal] -- Main: ", "onStop");
		if (wifiSSReceiver != null) {
			this.unregisterReceiver(wifiSSReceiver);
		}
		if (videoCaptureTimer != null) {
			videoCaptureTimer.cancel();
		}
		if (SystemCheckTools.isApplicationSentToBackground(Main.this) == true) {
			Log.d("tigertiger", "onPause......destroy the app");
			ExitApp.getInstance().exit();
		}
		// if(WifiCheck.isBackground(Main.this) == true){
		// ExitApp.getInstance().exit();
		// }
	}

	@Override
	protected void onPause() {
		WriteLogToDevice.writeLog("[Normal] -- Main: ", "onPause");
		super.onPause();
		preview.stop();
		Log.d("tigertiger", "onPause......");

	}

	@Override
	protected void onRestart() {
		super.onRestart();
		// pbBtn.setClickable(true);
		WriteLogToDevice.writeLog("[Normal] -- Main: ", "onRestart");
	}

	@Override
	protected void onResume() {
		super.onResume();
		intentLock = false;
		allowClickButtoms = true;
		lastCilckTime = 0;

		WriteLogToDevice.writeLog("[Normal] -- Main: ", "onResume");
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		WriteLogToDevice.writeLog("[Normal] -- Main: ", "onKeyDown keyCode ="
				+ keyCode);
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			WriteLogToDevice.writeLog("[Normal] -- Main: ", "KEYCODE_BACK");
			if (setupMainMenu.getVisibility() == View.VISIBLE) {
				setupMainMenu.setVisibility(View.GONE);
				setupMainMenu.clearFocus();
				initUiInterface();
				break;
			}
			// Modified by zhangyanhu 2013.11.27
			// if (curMode == APP_STATE_STILL_CAPTURE) {
			// if (toastWaitCapture == null) {
			// toastWaitCapture = Toast.makeText(Main.this,
			// R.string.stream_exit_wait, Toast.LENGTH_SHORT);
			// } else {
			// toastWaitCapture.setText(R.string.stream_exit_wait);
			// toastWaitCapture.setDuration(Toast.LENGTH_SHORT);
			// }
			// toastWaitCapture.show();
			// // toastExitCapture.makeText(context, text, duration)
			// } else {
			// display a progressDialog and quit
			// ProgressDialog progressDialog = new ProgressDialog(Main.this);
			// progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			// progressDialog.setMessage(Main.this.getString(R.string.stream_exiting));
			// progressDialog.setCancelable(false);
			// progressDialog.show();
			// // QUITFLAG = true;
			// quitTimer.schedule(quitTask, 1000);
			AlertDialog.Builder builder = new AlertDialog.Builder(Main.this);
			builder.setTitle(R.string.app_name);
			builder.setIcon(R.drawable.logo_wificameras);
			builder.setMessage(R.string.dialog_btn_exit_app);
			builder.setPositiveButton("No",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			builder.setNegativeButton("Yes",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							Log.d("tigertiger", "want to quit the app");
							dialog.dismiss();
							ProgressDialog progressDialog = new ProgressDialog(
									Main.this);
							progressDialog
									.setProgressStyle(ProgressDialog.STYLE_SPINNER);
							progressDialog.setMessage(Main.this
									.getString(R.string.stream_exiting));
							progressDialog.setCancelable(false);
							progressDialog.show();
							// QUITFLAG = true;
							quitTimer.schedule(quitTask, 1000);

						}
					});
			// AlertDialog dialog = builder.create();
			// // dialog.setCancelable(true);
			// dialog.show();
			builder.show();

			// finish();
			// }
			// End modify by zhangyanhu 2013.11.27
			break;
		case KeyEvent.KEYCODE_HOME:
			WriteLogToDevice.writeLog("[Normal] -- Main: ", "KEYCODE_HOME");
			Toast.makeText(Main.this, R.string.app_exit, Toast.LENGTH_SHORT)
					.show();
			finish();
			break;
		case KeyEvent.KEYCODE_MENU:
			/*
			 * if (curMode == APP_STATE_STILL_CAPTURE || curMode ==
			 * APP_STATE_VIDEO_CAPTURE) { break; } if
			 * (setupMainMenu.getVisibility() == View.GONE) {
			 * setupMainMenu.setVisibility(View.VISIBLE); } else {
			 * setupMainMenu.setVisibility(View.GONE); }
			 */
			break;
		default:
			WriteLogToDevice.writeLog("[Normal] -- Main: ", "default");
			return super.onKeyDown(keyCode, event);
		}
		return true;
	}

	private class WifiSSReceiver extends BroadcastReceiver {
		private WifiManager wifi;

		public WifiSSReceiver() {
			super();

			wifi = (WifiManager) getSystemService(WIFI_SERVICE);
			changeWifiStatusIcon();
		}

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			changeWifiStatusIcon();
		}

		private void changeWifiStatusIcon() {
			WifiInfo info = wifi.getConnectionInfo();
			if (info.getBSSID() != null) {
				int strength = WifiManager.calculateSignalLevel(info.getRssi(),
						8);

				WriteLogToDevice.writeLog("Main", "SS: " + strength);
				switch (strength) {
				case 0:
				case 1:
					wifiStatus.setImageResource(R.drawable.wifi_1);
					break;
				case 2:
				case 3:
					wifiStatus.setImageResource(R.drawable.wifi_2);
					break;
				case 4:
				case 5:
					wifiStatus.setImageResource(R.drawable.wifi_3);
					break;
				case 6:
				case 7:
					wifiStatus.setImageResource(R.drawable.wifi_4);
					break;
				default:
					break;
				}
			}
		}
	}

	// Added by zhangyanhu 2013.11.27
	private Timer quitTimer = new Timer(true);
	private TimerTask quitTask = new TimerTask() {
		@Override
		public void run() {
			sendOkMsg(MESSAGE_QUIT_APP);
		}
	};

	// End add by zhangyanhu 2013.11.27
	/**
	 * Added by zhangyanhu C01012,2014-1-23
	 */
	private boolean changeCameraMode(ICatchPreviewMode previewMode) {
		WriteLogToDevice.writeLog("[Normal] -- Main: ",
				"changeCameraMode previewMode =" + previewMode);
		ICatchMJPGStreamParam param = new ICatchMJPGStreamParam();
		boolean b = false;
		int ii = 0;
		while ((b == false) && (ii < 3)) {
			WriteLogToDevice.writeLog("[Normal] -- Main: ",
					"begin start media stream");
			b = previewStream.startMediaStream(param, previewMode, true); // withoutAudio
																			// =
																			// true,
																			// for
																			// SPC02
			WriteLogToDevice.writeLog("[Normal] -- Main: ",
					"end  start media stream ret = " + b);
			ii++;
		}
		WriteLogToDevice.writeLog("[Normal] -- Main: ",
				"end  changeCameraMode ret = " + b);
		return b;
	}

	/*
	 * private boolean changeCameraMode(ICatchPreviewMode previewMode) {
	 * WriteLogToDevice.writeLog("[Normal] -- Main: ",
	 * "changeCameraMode previewMode =" + previewMode);
	 * ICatchCustomerStreamParam param = new
	 * ICatchCustomerStreamParam(554,"03"); boolean b = false; int ii = 0; while
	 * ((b == false) && (ii < 3)) {
	 * WriteLogToDevice.writeLog("[Normal] -- Main: ",
	 * "begin start media stream"); b = previewStream.startMediaStream(param,
	 * previewMode); WriteLogToDevice.writeLog("[Normal] -- Main: ",
	 * "end  start media stream ret = " + b); ii++; }
	 * WriteLogToDevice.writeLog("[Normal] -- Main: ",
	 * "end  changeCameraMode ret = " + b); return b; }
	 */

	/**
	 * Added by zhangyanhu C01012,2014-1-23
	 */
	private boolean stopMediaStream() {
		boolean b = false;
		int ii = 0;
		while (b == false && ii < 3) {
			WriteLogToDevice.writeLog("[Normal] -- Main: ",
					"begin stop media stream");
			b = previewStream.stopMediaStream();
			WriteLogToDevice.writeLog("[Normal] -- Main: ",
					"end stop media stream =" + b);
			ii++;
		}
		WriteLogToDevice.writeLog("[Normal] -- Main: ",
				"end  stopMediaStream ret = " + b);
		return b;
	}

	/**
	 * SD card is full? Added by zhangyanhu C01012,2014-2-9
	 */
	public boolean forbidePhotoCapture() {
		if (cameraProperties.getRemainImageNum() < 1) {
			WriteLogToDevice.writeLog("[Normal] -- Main: ",
					"forbidePhotoCapture return true");
			return true;
		}
		WriteLogToDevice.writeLog("[Normal] -- Main: ",
				"forbidePhotoCapture return false");
		return false;
	}

	/**
	 * show alert if sd card is full Added by zhangyanhu C01012,2014-2-9
	 */
	public void sdCardIsFullAlert() {
		WriteLogToDevice.writeLog("[Normal] -- Main: ",
				"begin show sdCardIsFullAlert");
		if (sdCardFullWarning == true) {
			return;
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(Main.this);
		builder.setMessage(Main.this
				.getString(R.string.dialog_recording_card_full_canceled));
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		if (dialog != null) {
			dialog.dismiss();
		}
		dialog = builder.create();
		dialog.setCancelable(true);
		dialog.show();
		sdCardFullWarning = true;
		WriteLogToDevice.writeLog("[Normal] -- Main: ",
				"end show sdCardIsFullAlert");
	}

	/**
	 * sd card is not ready Added by zhangyanhu C01012,2014-2-9
	 */
	public void sdCardIsNotReadyAlert(String errorInfo) {
		WriteLogToDevice.writeLog("[Normal] -- Main: ",
				"begin show sdCardIsNotReadyAlert");
		AlertDialog.Builder builder = new AlertDialog.Builder(Main.this);
		builder.setMessage(errorInfo);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		AlertDialog dialog = builder.create();
		dialog.setCancelable(true);
		dialog.show();
		WriteLogToDevice.writeLog("[Normal] -- Main: ",
				"end show sdCardIsNotReadyAlert");
	}

	/**
	 * set icon of battery level in preview Added by zhangyanhu C01012,2014-2-10
	 */
	public void setBatteryLevelIcon() {
		int current = cameraProperties.getBatteryElectric();
		WriteLogToDevice.writeLog("[Normal] -- Main: ",
				"current setBatteryLevelIcon= " + current);
		if (current < 33 && current >= 0) {
		//	showWarningDlg(Main.this);
			batteryStatus.setImageResource(R.drawable.battery_0);
		} else if (current == 33) {
			batteryStatus.setImageResource(R.drawable.battery_1);
		} else if (current == 66) {
			batteryStatus.setImageResource(R.drawable.battery_2);
		} else if (current == 100) {
			batteryStatus.setImageResource(R.drawable.battery_3);
		} else if (current > 100) {
			batteryStatus.setImageResource(R.drawable.battery_4);
		}
	}

	/**
	 * battery low warning Added by zhangyanhu C01012,2014-2-20
	 */
	private void showWarningDlg(Context context) {
		if (dialog != null) {
			dialog.dismiss();
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setIcon(R.drawable.warning).setTitle("Warning")
				.setMessage(R.string.low_battery);
		builder.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// do nothing;
					}
				});

		dialog = builder.create();
		dialog.setCancelable(false);
		dialog.show();
	}

	/**
	 * Added by zhangyanhu C01012,2014-2-17
	 */
	public class CaptureShowTask extends TimerTask {

		@Override
		public void run() {
			if (videoCaptureTimerLamp) {
				videoCaptureTimerLamp = false;
				sendTimerMsg(MESSAGE_VIDEO_CAPTURE_ON, 1);

			} else {
				videoCaptureTimerLamp = true;
				sendTimerMsg(MESSAGE_VIDEO_CAPTURE_ON, 0);
			}
		}
	}

	/**
	 * 
	 * Added by zhangyanhu C01012,2014-4-29
	 */
	private void initUiInterface() {
		zoomLayout.setVisibility(View.GONE);
		zoomRate.setText("x" + (cameraProperties.getCurrentZoomRatio() / 10.0f));
		if (curMode == APP_STATE_STILL_PREVIEW) {
			captureBtn.setBackgroundResource(R.drawable.still_capture_btn);
			stillToggle.setBackgroundResource(R.drawable.capture_toggle_btn_on);
			videoToggle.setBackgroundResource(R.drawable.video_toggle_btn_off);
			timeLapseToggle
					.setBackgroundResource(R.drawable.timelapse_toggle_btn_off);
			if (cameraProperties
					.hasFuction(ICatchCameraProperty.ICH_CAP_CAPTURE_DELAY)) {
				delay_captue_status.setVisibility(View.VISIBLE);
				Log.d("tigertiger",
						"delay_capture_txt ="
								+ uiDisplayResource.getCurrentCaptureDelay().uiStringInPreview);
				delay_capture_txt.setText(uiDisplayResource
						.getCurrentCaptureDelay().uiStringInPreview);
			}
			if (cameraProperties
					.hasFuction(ICatchCameraProperty.ICH_CAP_IMAGE_SIZE)) {
				photo_hd_status.setVisibility(View.VISIBLE);
				photo_hd_txt.setText(uiDisplayResource.getRemainImageNum());
				photo_hd_btn
						.setText(uiDisplayResource.getCurrentImageSize().uiStringInPreview);
			}
			if (cameraProperties
					.hasFuction(ICatchCameraProperty.ICH_CAP_BURST_NUMBER)) {
				burst_status.setVisibility(View.VISIBLE);
				burst_status.setBackgroundResource(uiDisplayResource
						.getCurrentBurstNum().iconID);
			}
			if (cameraProperties
					.hasFuction(ICatchCameraProperty.ICH_CAP_WHITE_BALANCE)) {
				wb_status.setBackgroundResource(uiDisplayResource
						.getCurrentWhiteBalance().iconID);
			}
			video_hd_status.setVisibility(View.GONE);
			timelapseMode.setVisibility(View.GONE);
			slowMotion.setVisibility(View.GONE);
		} else if (curMode == APP_STATE_VIDEO_PREVIEW) {
			captureBtn.setBackgroundResource(R.drawable.video_recording_btn_on);
			stillToggle
					.setBackgroundResource(R.drawable.capture_toggle_btn_off);
			videoToggle.setBackgroundResource(R.drawable.video_toggle_btn_on);
			timeLapseToggle
					.setBackgroundResource(R.drawable.timelapse_toggle_btn_off);
			delay_captue_status.setVisibility(View.GONE);
			photo_hd_status.setVisibility(View.GONE);
			burst_status.setVisibility(View.GONE);
			if (cameraProperties
					.hasFuction(ICatchCameraProperty.ICH_CAP_VIDEO_SIZE)) {
				video_hd_status.setVisibility(View.VISIBLE);
				video_hd_txt
						.setText(uiDisplayResource.getRecordingRemainTime());
				video_hd_btn
						.setText(uiDisplayResource.getCurrentVideoSize().uiStringInPreview);
			}
			if (cameraProperties
					.hasFuction(ICatchCameraProperty.ICH_CAP_WHITE_BALANCE)) {
				wb_status.setBackgroundResource(uiDisplayResource
						.getCurrentWhiteBalance().iconID);
			}
			if (cameraProperties
					.hasFuction(ICatchCameraProperty.ICH_CAP_SLOW_MOTION)
					&& cameraProperties.getCurrentSlowMotion() == SlowMotion.SLOW_MOTION_ON
					&& curMode == APP_STATE_VIDEO_PREVIEW) {
				slowMotion.setVisibility(View.VISIBLE);
			}
			timelapseMode.setVisibility(View.GONE);
		} else if (curMode == APP_STATE_TIMELAPSE_PREVIEW_VIDEO) {
			captureBtn.setBackgroundResource(R.drawable.video_recording_btn_on);
			stillToggle
					.setBackgroundResource(R.drawable.capture_toggle_btn_off);
			videoToggle.setBackgroundResource(R.drawable.video_toggle_btn_off);
			timeLapseToggle
					.setBackgroundResource(R.drawable.timelapse_toggle_btn_on);
			delay_captue_status.setVisibility(View.GONE);
			photo_hd_status.setVisibility(View.GONE);
			burst_status.setVisibility(View.GONE);
			if (cameraProperties
					.hasFuction(ICatchCameraProperty.ICH_CAP_VIDEO_SIZE)) {
				video_hd_status.setVisibility(View.VISIBLE);
				video_hd_txt
						.setText(uiDisplayResource.getRecordingRemainTime());
				video_hd_btn
						.setText(uiDisplayResource.getCurrentVideoSize().uiStringInPreview);
			}
			if (cameraProperties
					.hasFuction(ICatchCameraProperty.ICH_CAP_WHITE_BALANCE)) {
				wb_status.setBackgroundResource(uiDisplayResource
						.getCurrentWhiteBalance().iconID);
			}
			timelapseMode
					.setBackgroundResource(R.drawable.flag_timelapse_video);
			timelapseMode.setVisibility(View.VISIBLE);
			slowMotion.setVisibility(View.GONE);
			slowMotion.setVisibility(View.GONE);
		} else if (curMode == APP_STATE_TIMELAPSE_PREVIEW_STILL) {
			captureBtn.setBackgroundResource(R.drawable.still_capture_btn);
			stillToggle
					.setBackgroundResource(R.drawable.capture_toggle_btn_off);
			videoToggle.setBackgroundResource(R.drawable.video_toggle_btn_off);
			timeLapseToggle
					.setBackgroundResource(R.drawable.timelapse_toggle_btn_on);
			delay_captue_status.setVisibility(View.GONE);
			if (cameraProperties
					.hasFuction(ICatchCameraProperty.ICH_CAP_IMAGE_SIZE)) {
				photo_hd_status.setVisibility(View.VISIBLE);
				photo_hd_txt.setText(uiDisplayResource.getRemainImageNum());
				photo_hd_btn
						.setText(uiDisplayResource.getCurrentImageSize().uiStringInPreview);
			}
			burst_status.setVisibility(View.GONE);
			if (cameraProperties
					.hasFuction(ICatchCameraProperty.ICH_CAP_WHITE_BALANCE)) {
				wb_status.setBackgroundResource(uiDisplayResource
						.getCurrentWhiteBalance().iconID);
			}
			video_hd_status.setVisibility(View.GONE);
			timelapseMode
					.setBackgroundResource(R.drawable.flag_timelapse_capture);
			timelapseMode.setVisibility(View.VISIBLE);
			slowMotion.setVisibility(View.GONE);
		} else if (curMode == APP_STATE_TIMELAPSE_STILL_CAPTURE) {
			captureBtn.setBackgroundResource(R.drawable.still_capture_btn);
			stillToggle
					.setBackgroundResource(R.drawable.capture_toggle_btn_off);
			videoToggle.setBackgroundResource(R.drawable.video_toggle_btn_off);
			timeLapseToggle
					.setBackgroundResource(R.drawable.timelapse_toggle_btn_on);
			delay_captue_status.setVisibility(View.GONE);
			if (cameraProperties
					.hasFuction(ICatchCameraProperty.ICH_CAP_IMAGE_SIZE)) {
				photo_hd_status.setVisibility(View.VISIBLE);
				photo_hd_txt.setText(uiDisplayResource.getRemainImageNum());
				photo_hd_btn
						.setText(uiDisplayResource.getCurrentImageSize().uiStringInPreview);
			}
			burst_status.setVisibility(View.GONE);
			if (cameraProperties
					.hasFuction(ICatchCameraProperty.ICH_CAP_WHITE_BALANCE)) {
				wb_status.setBackgroundResource(uiDisplayResource
						.getCurrentWhiteBalance().iconID);
			}
			video_hd_status.setVisibility(View.GONE);
			timelapseMode
					.setBackgroundResource(R.drawable.flag_timelapse_capture);
			timelapseMode.setVisibility(View.VISIBLE);
			slowMotion.setVisibility(View.GONE);
		} else if (curMode == APP_STATE_TIMELAPSE_VIDEO_CAPTURE) {
			captureBtn.setBackgroundResource(R.drawable.video_recording_btn_on);
			stillToggle
					.setBackgroundResource(R.drawable.capture_toggle_btn_off);
			videoToggle.setBackgroundResource(R.drawable.video_toggle_btn_off);
			timeLapseToggle
					.setBackgroundResource(R.drawable.timelapse_toggle_btn_off);
			delay_captue_status.setVisibility(View.GONE);
			photo_hd_status.setVisibility(View.GONE);
			burst_status.setVisibility(View.GONE);
			if (cameraProperties
					.hasFuction(ICatchCameraProperty.ICH_CAP_VIDEO_SIZE)) {
				video_hd_status.setVisibility(View.VISIBLE);
				video_hd_txt
						.setText(uiDisplayResource.getRecordingRemainTime());
				video_hd_btn
						.setText(uiDisplayResource.getCurrentVideoSize().uiStringInPreview);
			}
			if (cameraProperties
					.hasFuction(ICatchCameraProperty.ICH_CAP_WHITE_BALANCE)) {
				wb_status.setBackgroundResource(uiDisplayResource
						.getCurrentWhiteBalance().iconID);
			}
			timelapseMode
					.setBackgroundResource(R.drawable.flag_timelapse_video);
			timelapseMode.setVisibility(View.VISIBLE);
			slowMotion.setVisibility(View.GONE);
		}
	}

	private boolean startMoiveRecording() {
		boolean retValue = false;
		// if(cameraProperties.getCurrentTimeLapseInterval() != 0x001){ //if
		// time lapse is not OFF
		// retValue = cameraAction.startMovieRecordTimeLapse();
		// }else{
		retValue = cameraAction.startMovieRecord();
		// }
		return retValue;
	}

	private boolean stopMoiveRecording() {
		boolean retValue = false;
		// if(cameraProperties.getCurrentTimeLapseInterval() != 0x001){ //if
		// time lapse is not OFF
		// retValue = cameraAction.stopMovieRecordTimeLapse();
		// }else{
		retValue = cameraAction.stopVideoCapture();
		// }
		return retValue;
	}

	private class zoomTimerTask extends TimerTask {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.TimerTask#run()
		 */
		@Override
		public void run() {
			// TODO Auto-generated method stub
			// zoomLayout.setVisibility(View.GONE);
			sendOkMsg(MESSAGE_ZOOM_BAR_DISAPPEAR);
		}

	}

	private void startMovieRecordingTimer(int startTime) {
		if (curMode == APP_STATE_VIDEO_CAPTURE) {
			if (recordingTimer != null) {
				recordingTimer.cancel();
			}
			lapseTime = startTime;
			recordingTime.setText(UIDisplayResource.secondsToHours(lapseTime));
			recordingTime.setVisibility(View.VISIBLE);
			recordingTimer = new Timer(true);

			TimerTask timerTask = new TimerTask() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					// recordingTime.setText(UIDisplayResource.secondsToHours(lapseTime));
					lapseTime++;
					Log.d("1111",
							"send MESSAGE_UPDATE_RECORDING_TIME lapseTime ="
									+ lapseTime);
					mainHandler.obtainMessage(MESSAGE_UPDATE_RECORDING_TIME,
							lapseTime, 0).sendToTarget();
				}

			};
			Log.d("1111", "start recordingTimer lapseTime =" + lapseTime);
			recordingTimer.schedule(timerTask, 0, 1000);
		}
	}

	/*
	 * class ICatchH264StreamParamCustomize extends ICatchCustomerStreamParam{
	 * 
	 * }
	 */
}
