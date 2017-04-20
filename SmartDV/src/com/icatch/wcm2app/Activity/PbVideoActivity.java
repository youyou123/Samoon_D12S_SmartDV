package com.icatch.wcm2app.Activity;

import java.io.File;
import java.nio.ByteBuffer;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.icatch.wcm2app.common.ExitApp;
import com.icatch.wcm2app.common.GlobalApp;
import com.icatch.wcm2app.common.MediaRefresh;
import com.icatch.wcm2app.common.SystemCheckTools;
import com.icatch.wcm2app.common.WriteLogToDevice;
import com.icatch.wcm2app.controller.UIDisplayResource;
import com.icatch.wcm2app.controller.sdkApi.CameraAction;
import com.icatch.wcm2app.controller.sdkApi.FileOperation;
import com.icatch.wcm2app.controller.sdkApi.SDKSession;
import com.icatch.wcm2app.controller.sdkApi.VideoPlayback;
import com.icatch.wcm2app.R;
import com.icatch.wificam.customer.ICatchWificamListener;
import com.icatch.wificam.customer.ICatchWificamVideoPlayback;
import com.icatch.wificam.customer.exception.IchAudioStreamClosedException;
import com.icatch.wificam.customer.exception.IchBufferTooSmallException;
import com.icatch.wificam.customer.exception.IchCameraModeException;
import com.icatch.wificam.customer.exception.IchInvalidArgumentException;
import com.icatch.wificam.customer.exception.IchInvalidSessionException;
import com.icatch.wificam.customer.exception.IchPbStreamPausedException;
import com.icatch.wificam.customer.exception.IchSocketException;
import com.icatch.wificam.customer.exception.IchStreamNotRunningException;
import com.icatch.wificam.customer.exception.IchTryAgainException;
import com.icatch.wificam.customer.exception.IchVideoStreamClosedException;
import com.icatch.wificam.customer.type.ICatchEvent;
import com.icatch.wificam.customer.type.ICatchEventID;
import com.icatch.wificam.customer.type.ICatchFile;
import com.icatch.wificam.customer.type.ICatchFrameBuffer;

/**
 * 
 * Added by zhangyanhu C01012,2014-3-12
 */
public class PbVideoActivity extends Activity {
	private TextView timeLapsed;
	private TextView timeDuration;
	// private VideoPlayView videoView;
	private ImageView videoView;
	private SeekBar seekBar;
	private Button delete;
	private Button play;
	private Button stop;
	private Button download;
	// private Timer seekBarTimer;
	private VideoHandler handler = new VideoHandler();
	// private TimerTask task;
	private final int MODE_VIDEO_PLAY = 0X01;
	private final int MODE_VIDEO_IDLE = 0X02;
	private final int MODE_VIDEO_PAUSE = 0X03;
	private final int MODE_VIDEO_DOWNLOAD = 0X04;
	private final int MODE_VIDEO_DELETE = 0X05;
	private static final int MESSAGE_HAS_EXCEPTION = 0X01;
	private static final int ACTION_SET_BITMAP = 0X02;
	private static final int DOWNLOAD_COMPLETED = 0X03;
	private static final int DOWNLOAD_FAILED = 0X04;
	private static final int DELETE_DONE = 0x05;
	private static final int DELETE_FAILED = 0x06;
	public static final int EVENT_CACHE_STATE_CHANGED = 0x07;
	public static final int EVENT_CACHE_PROGRESS_NOTIFY = 0x08;
	public static final int DOWNLOAD_VIDEO_PROGRESS_NOTIFY = 0X09;
	public static final int CANCEL_VIDEO_DOWNLOAD_SUCCESS = 0X10;
	public static final int EVENT_VIDEO_PLAY_COMPLETED = 0X11;
	public static final double DEFAULT_PARA = -1.0;
	private int mode;
	private int videoDuration = 0;
	private Future videoFuture;
	private Future audioFuture;
	//private Future videoFuture;
	private ExecutorService executor;
	private Bitmap videoBitmap;
	private Bundle curIndexData;
	private int curIndex;
	private List<ICatchFile> list;
	private VideoPlayback videoPlayback = new VideoPlayback();
	ICatchWificamVideoPlayback videoPb = SDKSession.getVideoPlaybackClint();
	// controlClient;
	private ProgressDialog ingFrag;
	private CacheStateChangedListener cacheStateChangedListener;
	private CacheProgressListener cacheProgressListener;
	private VideoIsEndListener videoIsEndListener;
	private ProgressBar progressBar;
	private TextView loadPercent;
	private Boolean waitForCaching;
	private Toast toastForbid = null;
	private Toast toastFailed = null;
	private double currentTime = -1.0;
	private long lastSysTime = 0;
	private long curSysTime = 0;
	private long seekbarChangeInterval = 125;// ms
	public String downloadingFilename;
	public long downloadingFilesize;
	public long downloadProcess;
	protected Timer downloadProgressTimer;
	private FileOperation fileOperation = new FileOperation();
	private CameraAction cameraAction = new CameraAction();
	private UIDisplayResource uiDisplayResource = UIDisplayResource.getinstance();
	private int lastSeekPosition = 0;
	//private ICatchWificamPreview previewStreamControl;
	private AudioTrack audioTrack;
	//private PreviewStream previewStream = new PreviewStream();
	protected Future<Object> future;
	private boolean showFullScreen = false;
	private RelativeLayout topBar;
	private RelativeLayout bottomBar;
	private boolean cacheFlag = false;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		WriteLogToDevice.writeLog("[Normal] -- PbVideoActivity", "start oncreate.......");
		GlobalApp.getInstance().setCurrentApp(PbVideoActivity.this);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		// 0048919 End add by zhangyanhu 2013.12.11
		getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
		setContentView(R.layout.activity_pb_video);
		
		//previewStreamControl = SDKSession.getpreviewStreamClient();
		topBar = (RelativeLayout)findViewById(R.id.video_top_bar); 
		bottomBar = (RelativeLayout)findViewById(R.id.video_bottom_bar); 
		videoView = (ImageView) findViewById(R.id.play_view);
		timeLapsed = (TextView) findViewById(R.id.time_lapsed);
		timeDuration = (TextView) findViewById(R.id.time_duration);
		seekBar = (SeekBar) findViewById(R.id.seekBar);
		delete = (Button) findViewById(R.id.video_del);
		play = (Button) findViewById(R.id.video_play);
		stop = (Button) findViewById(R.id.video_stop);
		download = (Button) findViewById(R.id.video_download);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		loadPercent = (TextView) findViewById(R.id.loadPercent);
		seekBar.setMax(0);
		setLoadingProgress(0);
		waitForCaching = false;
		mode = MODE_VIDEO_IDLE;
		Intent intent = getIntent();
		curIndexData = intent.getExtras();
		curIndex = curIndexData.getInt("curIndex");
		executor = Executors.newSingleThreadExecutor();
		if (list == null) {
			GlobalApp myApp = (GlobalApp) getApplication();
			list = myApp.getFileList();
			// photoNums = list.size();
		}
		
		
		//一开始就播放
		WriteLogToDevice.writeLog("[Normal] -- PbVideoActivity", "start play video");
		ICatchFile file = list.get(curIndex);
		if (videoPlayback.startPlaybackStream(file) == false) {
			if (toastFailed == null) {
				toastFailed = Toast.makeText(PbVideoActivity.this, R.string.dialog_failed, Toast.LENGTH_SHORT);
			} else {
				toastFailed.setText(R.string.dialog_failed);
				toastFailed.setDuration(Toast.LENGTH_SHORT);
			}
			toastFailed.show();
			WriteLogToDevice.writeLog("[Error] -- PbVideoActivity", "failed to startPlaybackStream");
			return;
		}
		WriteLogToDevice.writeLog("[Normal] -- PbVideoActivity", "seekBar.getProgress() =" + seekBar.getProgress());
		//videoPlayback.videoSeek(seekBar.getProgress() / 100.0);
		int tempDuration = videoPlayback.getVideoDuration();
		WriteLogToDevice.writeLog("[Normal] -- PbVideoActivity", "end getLength = " + tempDuration);
		play.setBackgroundResource(R.drawable.pause);
		seekBar.setMax(tempDuration);
		timeDuration.setText(uiDisplayResource.secondsToHours(tempDuration / 100));
		videoDuration = tempDuration;// temp attemp to avoid sdk
										// error
		executor = Executors.newFixedThreadPool(2);
		videoFuture = executor.submit(new GetVideoFrameThread(), null);
		audioFuture = executor.submit(new AudioThread(), null);
		WriteLogToDevice.writeLog("[Normal] -- PbVideoActivity", "has start the GetVideoFrameThread() to get play video");
		mode = MODE_VIDEO_PLAY;
	
		
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			/**
			 * 当用户结束对滑块滑动时,调用该方法
			 */
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				lastSeekPosition = seekBar.getProgress();
				timeLapsed.setText(uiDisplayResource.secondsToHours(seekBar.getProgress() / 100));
				//seekBar.setSecondaryProgress(0);
				//if (mode == MODE_VIDEO_PLAY) {
					if (videoPlayback.videoSeek(seekBar.getProgress() / 100.0)) {
						currentTime = seekBar.getProgress() / 100.0;
					} else {
						seekBar.setProgress(lastSeekPosition);
						if (toastFailed == null) {
							toastFailed = Toast.makeText(PbVideoActivity.this, R.string.dialog_failed, Toast.LENGTH_SHORT);
						} else {
							toastFailed.setText(R.string.dialog_failed);
							toastFailed.setDuration(Toast.LENGTH_SHORT);
						}
						toastFailed.show();
						WriteLogToDevice.writeLog("[Error] -- PbVideoActivity", "seek error");
						return;
					}
				//}
			}

			/**
			 * 当用户开始滑动滑块时调用该方法
			 */
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}

			/**
			 * 当进度条发生变化时调用该方法
			 */
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				// TODO Auto-generated method stub
				timeLapsed.setText(uiDisplayResource.secondsToHours(progress / 100));
			}
		});

		play.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (mode == MODE_VIDEO_IDLE) {
					WriteLogToDevice.writeLog("[Normal] -- PbVideoActivity", "start play video");
					ICatchFile file = list.get(curIndex);
					if (videoPlayback.startPlaybackStream(file) == false) {
						if (toastFailed == null) {
							toastFailed = Toast.makeText(PbVideoActivity.this, R.string.dialog_failed, Toast.LENGTH_SHORT);
						} else {
							toastFailed.setText(R.string.dialog_failed);
							toastFailed.setDuration(Toast.LENGTH_SHORT);
						}
						toastFailed.show();
						WriteLogToDevice.writeLog("[Error] -- PbVideoActivity", "failed to startPlaybackStream");
						return;
					}
					WriteLogToDevice.writeLog("[Normal] -- PbVideoActivity", "seekBar.getProgress() =" + seekBar.getProgress());
					//videoPlayback.videoSeek(seekBar.getProgress() / 100.0);
					int tempDuration = videoPlayback.getVideoDuration();
					WriteLogToDevice.writeLog("[Normal] -- PbVideoActivity", "end getLength = " + tempDuration);
					play.setBackgroundResource(R.drawable.pause);
					seekBar.setMax(tempDuration);
					timeDuration.setText(uiDisplayResource.secondsToHours(tempDuration / 100));
					videoDuration = tempDuration;// temp attemp to avoid sdk
													// error
					executor = Executors.newFixedThreadPool(2);
					videoFuture = executor.submit(new GetVideoFrameThread(), null);
					audioFuture = executor.submit(new AudioThread(), null);
					WriteLogToDevice.writeLog("[Normal] -- PbVideoActivity", "has start the GetVideoFrameThread() to get play video");
					mode = MODE_VIDEO_PLAY;
				} else if (mode == MODE_VIDEO_PAUSE) {
					WriteLogToDevice.writeLog("[Normal] -- PbVideoActivity", "mode == MODE_VIDEO_PAUSE");
					if (videoPlayback.resumePlayback() == false) {
						if (toastFailed == null) {
							toastFailed = Toast.makeText(PbVideoActivity.this, R.string.dialog_failed, Toast.LENGTH_SHORT);
						} else {
							toastFailed.setText(R.string.dialog_failed);
							toastFailed.setDuration(Toast.LENGTH_SHORT);
						}
						toastFailed.show();
						WriteLogToDevice.writeLog("[Error] -- PbVideoActivity", "failed to resumePlayback");
						return;
					}
					play.setBackgroundResource(R.drawable.pause);
					//executor = Executors.newFixedThreadPool(2);
					//videoFuture = executor.submit(new GetVideoFrameThread(), null);
					//audioFuture = executor.submit(new AudioThread(), null);
					mode = MODE_VIDEO_PLAY;
				} else if (mode == MODE_VIDEO_PLAY) {
					WriteLogToDevice.writeLog("[Normal] -- PbVideoActivity", "begin pause the playing");
					if (videoPlayback.pausePlayback() == false) {
						if (toastFailed == null) {
							toastFailed = Toast.makeText(PbVideoActivity.this, R.string.dialog_failed, Toast.LENGTH_SHORT);
						} else {
							toastFailed.setText(R.string.dialog_failed);
							toastFailed.setDuration(Toast.LENGTH_SHORT);
						}
						toastFailed.show();
						WriteLogToDevice.writeLog("[Error] -- PbVideoActivity", "failed to pausePlayback");
						return;
					}
					//stopThread();
					play.setBackgroundResource(R.drawable.play);
					mode = MODE_VIDEO_PAUSE;
				}
			}
		});

		stop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				WriteLogToDevice.writeLog("[Normal] -- PbVideoActivity", "begin stop the playing");
				// TODO Auto-generated method stub
				if (mode == MODE_VIDEO_IDLE) {
					return;
				}
				if (videoPlayback.stopPlaybackStream() == false) {
					if (toastFailed == null) {
						toastFailed = Toast.makeText(PbVideoActivity.this, R.string.dialog_failed, Toast.LENGTH_SHORT);
					} else {
						toastFailed.setText(R.string.dialog_failed);
						toastFailed.setDuration(Toast.LENGTH_SHORT);
					}
					toastFailed.show();
					return;
				}
				stopThread();
				play.setBackgroundResource(R.drawable.play);
				seekBar.setProgress(0);
				seekBar.setSecondaryProgress(0);
				showLoadingCircle(false);
				mode = MODE_VIDEO_IDLE;
			}

		});

		download.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				WriteLogToDevice.writeLog("[Normal] -- PbVideoActivity", "start downloading, mode =" + mode);
				if (mode != MODE_VIDEO_IDLE) {
					if (toastForbid == null) {
						toastForbid = Toast.makeText(PbVideoActivity.this, "Operation do not be allowed!Please stop playing video!", Toast.LENGTH_SHORT);
					}
					toastForbid.show();
					return;
				}
				// TODO Auto-generated method stub
				mode = MODE_VIDEO_DOWNLOAD;
				showDownloadEnsureDialog();
			}

		});

		delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (mode != MODE_VIDEO_IDLE) {
					if (toastForbid == null) {
						toastForbid = Toast.makeText(PbVideoActivity.this, "Operation do not be allowed!Please stop playing video!", Toast.LENGTH_SHORT);
					}
					toastForbid.show();
					return;
				}
				// TODO Auto-generated method stub
				mode = MODE_VIDEO_DELETE;
				showDeleteEnsureDialog();
			}

		});
		
		videoView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(showFullScreen == true){
					topBar.setVisibility(View.VISIBLE);
					bottomBar.setVisibility(View.VISIBLE);
					timeLapsed.setVisibility(View.VISIBLE);
					timeDuration.setVisibility(View.VISIBLE);
					seekBar.setVisibility(View.VISIBLE);
					showFullScreen = false;
				}else{
					topBar.setVisibility(View.GONE);
					bottomBar.setVisibility(View.GONE);
					timeLapsed.setVisibility(View.GONE);
					timeDuration.setVisibility(View.GONE);
					seekBar.setVisibility(View.GONE);
					showFullScreen = true; 
				}
			}
		});
		
		
		ExitApp.getInstance().addActivity(this);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		WriteLogToDevice.writeLog("[Normal] -- PbVideoActivity", "onKeyDown");
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			videoPlayback.stopPlaybackStream();
			stopThread();
			if (cacheStateChangedListener != null) {
				cameraAction.delEventListener(ICatchEventID.ICH_EVENT_VIDEO_PLAYBACK_CACHING_CHANGED, cacheStateChangedListener);
			}
			if (cacheProgressListener != null) {
				cameraAction.delEventListener(ICatchEventID.ICH_EVENT_VIDEO_PLAYBACK_CACHING_PROGRESS, cacheProgressListener);
			}
			if (videoIsEndListener != null) {
				cameraAction.delEventListener(ICatchEventID.ICH_EVENT_VIDEO_STREAM_PLAYING_ENDED, videoIsEndListener);
			}
			finish();
			break;
		default:
			return super.onKeyDown(keyCode, event);
		}
		return true;
	}

	private class VideoHandler extends Handler {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ACTION_SET_BITMAP:
				WriteLogToDevice.writeLog("[Normal] -- PbVideoActivity", "receive ACTION_SET_BITMAP");
				if (mode != MODE_VIDEO_PLAY) {
					break;
				}
				videoView.setImageBitmap(videoBitmap);

				Log.d("tigertiger","currentTime =="+currentTime);
				int temp = new Double(currentTime * 100).intValue();
				seekBar.setProgress(temp);
				break;
			case DOWNLOAD_COMPLETED:
				WriteLogToDevice.writeLog("[Normal] -- PbVideoActivity", "receive DOWNLOAD_COMPLETED");
				if (mode != MODE_VIDEO_DOWNLOAD) {
					break;
				}
				ingFrag.dismiss();
				if (downloadProgressTimer != null) {
					downloadProgressTimer.cancel();
				}
				mode = MODE_VIDEO_IDLE;
				Toast.makeText(PbVideoActivity.this, "Downloaded to"+GlobalApp.DOWNLOAD_PATH, Toast.LENGTH_SHORT).show();
				break;
			case DOWNLOAD_FAILED:
				WriteLogToDevice.writeLog("[Normal] -- PbVideoActivity", "receive DOWNLOAD_FAILED");
				if (mode != MODE_VIDEO_DOWNLOAD) {
					break;
				}
				ingFrag.dismiss();
				if (downloadProgressTimer != null) {
					downloadProgressTimer.cancel();
				}
				mode = MODE_VIDEO_IDLE;
				Toast.makeText(PbVideoActivity.this, "Download failed", Toast.LENGTH_SHORT).show();
				break;
			case DELETE_DONE:
				WriteLogToDevice.writeLog("[Normal] -- PbVideoActivity", "receive DELETE_DONE");
				Toast.makeText(PbVideoActivity.this, "Deleting is ok", Toast.LENGTH_SHORT).show();
				list.remove(curIndex);
				ingFrag.dismiss();
				mode = MODE_VIDEO_IDLE;
				finish();
				break;
			case DELETE_FAILED:
				WriteLogToDevice.writeLog("[Normal] -- PbVideoActivity", "receive DELETE_FAILED");
				ingFrag.dismiss();
				mode = MODE_VIDEO_IDLE;
				Toast.makeText(PbVideoActivity.this, R.string.dialog_delete_failed_single, Toast.LENGTH_SHORT).show();
				break;
			case EVENT_CACHE_STATE_CHANGED:
				WriteLogToDevice.writeLog("[Normal] -- PbVideoActivity", "receive EVENT_CACHE_STATE_CHANGED");
				WriteLogToDevice.writeLog("[Normal] -- PbVideoActivity", "EVENT_CACHE_STATE_CHANGED ---------msg.arg1 = " + msg.arg1);
				if(cacheFlag == false){
					return;
				}
				if (msg.arg1 == 1) {
					showLoadingCircle(true);
					waitForCaching = true;
				} else if (msg.arg1 == 2) {
					showLoadingCircle(false);
					waitForCaching = false;
				}
				break;
			case EVENT_CACHE_PROGRESS_NOTIFY:
				WriteLogToDevice.writeLog("[Normal] -- PbVideoActivity", "receive EVENT_CACHE_PROGRESS_NOTIFY  cacheFlag="+cacheFlag);
				if(cacheFlag == false){
					return;
				}
				if (mode == MODE_VIDEO_IDLE) {
					break;
				}
				WriteLogToDevice.writeLog("[Normal] -- PbVideoActivity", "---------showLoadingCircle = " + msg.arg1);
				WriteLogToDevice.writeLog("[Normal] -- PbVideoActivity", "---------msg.arg2 = " + msg.arg2);
				WriteLogToDevice.writeLog("[Normal] -- PbVideoActivity", "---------waitForCaching = " + waitForCaching);
				if (waitForCaching) {
					// showLoadingCircle(true);
					setLoadingProgress(msg.arg1);
				}
				seekBar.setSecondaryProgress(msg.arg2);
				break;
			case DOWNLOAD_VIDEO_PROGRESS_NOTIFY:
				WriteLogToDevice.writeLog("[Normal] -- PbVideoActivity", "receive DOWNLOAD_VIDEO_PROGRESS_NOTIFY progress = " + msg.arg1);
				
				if (mode == MODE_VIDEO_DOWNLOAD) {
					ingFrag.setProgress(msg.arg1);
				}
				break;
			case CANCEL_VIDEO_DOWNLOAD_SUCCESS:
				WriteLogToDevice.writeLog("[Normal] -- PbVideoActivity", "receive CANCEL_VIDEO_DOWNLOAD_SUCCESS");
				ingFrag.dismiss();
				if (downloadProgressTimer != null) {
					downloadProgressTimer.cancel();
				}
				mode = MODE_VIDEO_IDLE;
				Toast.makeText(PbVideoActivity.this, R.string.dialog_cancel_downloading_succeeded, Toast.LENGTH_SHORT).show();
				break;
			case EVENT_VIDEO_PLAY_COMPLETED:
				WriteLogToDevice.writeLog("[Normal] -- PbVideoActivity", "receive EVENT_VIDEO_PLAY_COMPLETED");
				WriteLogToDevice.writeLog("[Normal] -- PbVideoActivity", "---------------VIDEO_PLAY_COMPLETED-----------------");
				if (mode == MODE_VIDEO_PLAY) {
					cacheFlag = false;
					showLoadingCircle(false);
					timeLapsed.setText(uiDisplayResource.secondsToHours(videoDuration / 100));
					videoPlayback.stopPlaybackStream();
					stopThread();
					play.setBackgroundResource(R.drawable.play);
					seekBar.setProgress(0);
					seekBar.setSecondaryProgress(0);
					mode = MODE_VIDEO_IDLE;
				}
				break;
			case MESSAGE_HAS_EXCEPTION:
				WriteLogToDevice.writeLog("[Normal] -- PbVideoActivity", "receive MESSAGE_HAS_EXCEPTION");
				stopThread();
				break;
			}
		}
	}

	private class GetVideoFrameThread implements Runnable {
		private byte[] pixelBuf;
		private ByteBuffer bmpBuf;
		private final int frameWidth = 640;
		private final int frameHeight = 360;

		public GetVideoFrameThread() {
			//pixelBuf = new byte[frameWidth * frameHeight * 4];
			
			pixelBuf = new byte[640 * 480 * 4];
			bmpBuf = ByteBuffer.wrap(pixelBuf);
			videoBitmap = Bitmap.createBitmap(frameWidth, frameHeight, Config.ARGB_8888);
			cacheFlag = true;
			showLoadingCircle(true);
			waitForCaching = true;			
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			WriteLogToDevice.writeLog("[Normal] -- PbVideoActivity", "start GetVideoFrameThread.....");
			ICatchFrameBuffer buffer = new ICatchFrameBuffer();
			buffer.setBuffer(pixelBuf);
			boolean temp = false;
			currentTime = -1.0;
			boolean isException = false; 
			while (!videoFuture.isCancelled() && !Thread.currentThread().isInterrupted()) {
				WriteLogToDevice.writeLog("[Normal] -- GetVideoFrameThread", "start getNextFrame");
				isException = false;
				temp = false;
				try {
					temp = videoPb.getNextVideoFrame(buffer);
				} catch (IchSocketException e) {
					WriteLogToDevice.writeLog("[Error] -- GetVideoFrameThread: ", "IchSocketException");
					// TODO Auto-generated catch block
					e.printStackTrace();
					isException = true;
					//return;
				} catch (IchCameraModeException e) {
					WriteLogToDevice.writeLog("[Error] -- GetVideoFrameThread: ", "IchCameraModeException");
					// TODO Auto-generated catch block
					e.printStackTrace();
					isException = true;
					//return;
				} catch (IchInvalidSessionException e) {
					WriteLogToDevice.writeLog("[Error] -- GetVideoFrameThread: ", "IchInvalidSessionException");
					// TODO Auto-generated catch block
					e.printStackTrace();
					isException = true;
					//return;
				} catch (IchStreamNotRunningException e) {
					WriteLogToDevice.writeLog("[Error] -- GetVideoFrameThread: ", "IchStreamNotRunningException");
					// TODO Auto-generated catch block
					e.printStackTrace();
					isException = true;
					//return;
				} catch (IchBufferTooSmallException e) {
					// TODO Auto-generated catch block
					WriteLogToDevice.writeLog("[Error] -- GetVideoFrameThread: ", "IchBufferTooSmallException");
					e.printStackTrace();
					isException = true;
					//return;
				} catch (IchTryAgainException e) {
					// TODO Auto-generated catch block
					WriteLogToDevice.writeLog("[Error] -- GetVideoFrameThread: ", "IchTryAgainException");
					e.printStackTrace();
					continue;
				} catch (IchInvalidArgumentException e) {
					// TODO Auto-generated catch block
					WriteLogToDevice.writeLog("[Error] -- GetVideoFrameThread: ", "IchInvalidArgumentException");
					e.printStackTrace();
					isException = true;
					//return;
				} catch (IchVideoStreamClosedException e) {
					WriteLogToDevice.writeLog("[Error] -- GetVideoFrameThread: ", "IchVideoStreamClosedException");
					// TODO Auto-generated catch block
					e.printStackTrace();
					cacheFlag = false;
					showLoadingCircle(false);
					return;
				} catch (IchPbStreamPausedException e) {
					// TODO Auto-generated catch block
					WriteLogToDevice.writeLog("[Error] -- GetVideoFrameThread: ", "IchPbStreamPausedException");
					e.printStackTrace();					
				}
				if(isException == true){
					//handler.obtainMessage(ACTION_SET_BITMAP).sendToTarget();
					WriteLogToDevice.writeLog("[Normal] -- GetVideoFrameThread", "isException == true,end GetVideoFrameThread");
					//cacheFlag = false;
					return;
				}
				//WriteLogToDevice.writeLog("[Normal] -- PbVideoActivity", "end getNextFrame temp =" + temp);
				if (temp == false) {
					WriteLogToDevice.writeLog("[Normal] -- GetVideoFrameThread", "end getNextFrame temp =" + temp);
					continue;
				}
				if (buffer == null) {
					WriteLogToDevice.writeLog("[Normal] -- GetVideoFrameThread", "buffer == null");
					WriteLogToDevice.writeLog("PbVideoActivity", "break");
					break;
				} else {
					currentTime = buffer.getPresentationTime();
					WriteLogToDevice.writeLog("[Normal] -- GetVideoFrameThread", "currentTime buffer.getPresentationTime()" + buffer.getPresentationTime());
				}

				/**
				 * rewinds this buffer
				 * 
				 * The position is set to zero, and the mark is cleared. The
				 * content of this buffer is not changed.
				 */
				bmpBuf.rewind();
				videoBitmap.copyPixelsFromBuffer(bmpBuf);
				handler.obtainMessage(ACTION_SET_BITMAP).sendToTarget();
				WriteLogToDevice.writeLog("[Normal] -- PbVideoActivity", "end getNextFrame");
			}
		}

	}

	private class AudioThread extends Thread {
		//private boolean done = false;

		public void run() {
			WriteLogToDevice.writeLog("[Normal] -- PbVideoActivity", "start AudioThread.....");
			if(videoPlayback.containsAudioStream() == false){
				return;
			}

			int sampleRateInHz = 44100;
			int minBufferSize = AudioTrack.getMinBufferSize(sampleRateInHz, AudioFormat.CHANNEL_IN_STEREO, AudioFormat.ENCODING_PCM_16BIT);
			audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRateInHz, AudioFormat.CHANNEL_IN_STEREO, AudioFormat.ENCODING_PCM_16BIT,
					minBufferSize, AudioTrack.MODE_STREAM);

			audioTrack.play();

			byte[] audioBuffer = new byte[1024 * 512];
			ICatchFrameBuffer icatchBuffer = new ICatchFrameBuffer();
			icatchBuffer.setBuffer(audioBuffer);
			boolean temp = false;
			WriteLogToDevice.writeLog("[Normal] -- getNextAudioFrame", "start getNextAudioFrame");
			while (true) {
				temp = false;
				try {
					temp = videoPb.getNextAudioFrame(icatchBuffer);
				} catch (IchSocketException e) {
					WriteLogToDevice.writeLog("[Error] -- AudioThread: ", "IchSocketException");
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
				} catch (IchCameraModeException e) {
					WriteLogToDevice.writeLog("[Error] -- AudioThread: ", "IchCameraModeException");
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
				} catch (IchInvalidSessionException e) {
					WriteLogToDevice.writeLog("[Error] -- AudioThread: ", "IchInvalidSessionException");
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
				} catch (IchStreamNotRunningException e) {
					WriteLogToDevice.writeLog("[Error] -- AudioThread: ", "IchStreamNotRunningException");
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
				} catch (IchBufferTooSmallException e) {
					// TODO Auto-generated catch block
					WriteLogToDevice.writeLog("[Error] -- AudioThread: ", "IchBufferTooSmallException");
					e.printStackTrace();
					return;
				} catch (IchTryAgainException e) {
					// TODO Auto-generated catch block
					WriteLogToDevice.writeLog("[Error] -- AudioThread: ", "IchTryAgainException");
					e.printStackTrace();
					continue;
				} catch (IchInvalidArgumentException e) {
					// TODO Auto-generated catch block
					WriteLogToDevice.writeLog("[Error] -- AudioThread: ", "IchInvalidArgumentException");
					e.printStackTrace();
					return;
				} catch (IchAudioStreamClosedException e) {
					// TODO Auto-generated catch block
					WriteLogToDevice.writeLog("[Error] -- AudioThread: ", "IchAudioStreamClosedException");
					e.printStackTrace();
				} catch (IchPbStreamPausedException e) {
					// TODO Auto-generated catch block
					WriteLogToDevice.writeLog("[Error] -- AudioThread: ", "IchPbStreamPausedException");
					e.printStackTrace();
				}
				if (false == temp) {
					//Log.e("tigertiger", "icatchBuffer ="+icatchBuffer.getFrameSize());
					WriteLogToDevice.writeLog("[Error] -- AudioThread: ", "failed to getNextAudioFrame");
					continue;
				}
				if (icatchBuffer == null) {
					WriteLogToDevice.writeLog("[Normal] -- AudioThread", "buffer == null");
					WriteLogToDevice.writeLog("AudioThread", "break");
					continue;
				}
		
				audioTrack.write(icatchBuffer.getBuffer(), 0, icatchBuffer.getFrameSize());
			}
			//Log.d("AudioThread", "quit audio track thread.");
		}
	}
	
	public boolean stopThread() {
		WriteLogToDevice.writeLog("[Normal] -- PbVideoActivity", "begin stop all Thread");
		if (videoFuture != null && !videoFuture.isDone()) {
			videoFuture.cancel(true);
		}
		if (audioFuture != null && !audioFuture.isDone()) {
			audioFuture.cancel(true);
		}
		if (future != null && !future.isDone()) {
			future.cancel(true);
		}
		if(audioTrack != null){
			if(audioTrack.getPlayState() == AudioTrack.PLAYSTATE_PLAYING){
				audioTrack.stop();
			}
			audioTrack.release();
		}
		
		executor.shutdown();
		try {
			if (!executor.awaitTermination(500, TimeUnit.MILLISECONDS)) {
				executor.shutdownNow();
				if (!executor.awaitTermination(500, TimeUnit.MILLISECONDS)) {
				}
			}
		} catch (InterruptedException e) {
			executor.shutdownNow();
			Thread.currentThread().interrupt();
		}
		WriteLogToDevice.writeLog("[Normal] -- PbVideoActivity", "end stop stop all Thread");
		return true;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		// stopThread();
		super.onDestroy();
		// ExitApp.getInstance().removeActivity(this);
		stopThread();
		// System.exit(0);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		
		super.onPause();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		GlobalApp.getInstance().setAppContext(getApplicationContext());
		cacheStateChangedListener = new CacheStateChangedListener();
		cacheProgressListener = new CacheProgressListener();
		videoIsEndListener = new VideoIsEndListener();
		cameraAction.addEventListener(ICatchEventID.ICH_EVENT_VIDEO_PLAYBACK_CACHING_CHANGED, cacheStateChangedListener);
		cameraAction.addEventListener(ICatchEventID.ICH_EVENT_VIDEO_PLAYBACK_CACHING_PROGRESS, cacheProgressListener);
		cameraAction.addEventListener(ICatchEventID.ICH_EVENT_VIDEO_STREAM_PLAYING_ENDED, videoIsEndListener);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if(SystemCheckTools.isApplicationSentToBackground(PbVideoActivity.this) == true){
			ExitApp.getInstance().exit();
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);

	}

	class DownloadThread implements Runnable {
		private String path;

		@Override
		public void run() {

			WriteLogToDevice.writeLog("[Normal] -- PbVideoActivity", "begin DownloadThread");
			downloadProcess = 0;
			GlobalApp.getInstance().setDownloadStatus(true);
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				path = Environment.getExternalStorageDirectory().toString() + GlobalApp.DOWNLOAD_PATH;
			} else {
				handler.obtainMessage(DOWNLOAD_FAILED).sendToTarget();
				return;
			}

			File file = new File(path);
			if (!file.exists()) {
				file.mkdirs();
			}

			ICatchFile videoFile = list.get(curIndex);
			Boolean retValue = false;
			String fileName = null;
			// different methods to get files decide by types:video or
			// image
			// need support API from sdk to get filename(not ok yet),
			// now it is temporary resolution
			fileName = videoFile.getFileName();
			downloadingFilename = path + fileName;
			downloadingFilesize = videoFile.getFileSize();
			File tempFile = new File(downloadingFilename);
			if (tempFile.exists()) {
				try {
					MediaRefresh.scanFileAsync(PbVideoActivity.this,path+fileName);
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				handler.obtainMessage(DOWNLOAD_COMPLETED).sendToTarget();
				return;
			}
			WriteLogToDevice.writeLog("[Normal] -- PbVideoActivity", "start downloadFile: ");
			retValue = fileOperation.downloadFile(videoFile, path + fileName);
			WriteLogToDevice.writeLog("[Normal] -- PbVideoActivity", "end downloadFile: retValue = " + retValue); 

			GlobalApp.getInstance().setDownloadStatus(false);
			if (retValue == true) {
				MediaRefresh.scanFileAsync(PbVideoActivity.this,path+fileName);
				handler.obtainMessage(DOWNLOAD_COMPLETED).sendToTarget();
			} else {
				WriteLogToDevice.writeLog("[Error] -- PbVideoActivity", "send message: DOWNLOAD_FAILED");
				handler.obtainMessage(DOWNLOAD_FAILED).sendToTarget();
			}
		}
	}

	private class DeleteThread implements Runnable {
		@Override
		public void run() {
			WriteLogToDevice.writeLog("[Normal] -- PbVideoActivity", "begin DeleteThread");
			ICatchFile curFile = list.get(curIndex);
			Boolean retValue = false;
			retValue = fileOperation.deleteFile(curFile); 
			if (retValue == false) {
				handler.obtainMessage(DELETE_FAILED).sendToTarget();
				WriteLogToDevice.writeLog("[Normal] -- PbVideoActivity", "end DeleteThread");
				return;
			}
			handler.obtainMessage(DELETE_DONE).sendToTarget();
			WriteLogToDevice.writeLog("[Normal] -- PbVideoActivity", "end DeleteThread");
		}
	}

	public void showDownloadEnsureDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(PbVideoActivity.this);
		builder.setCancelable(false);
		builder.setTitle(R.string.dialog_downloading_single);
		long videoFileSize = 0;
		videoFileSize = list.get(curIndex).getFileSize() / 1024 / 1024;
		WriteLogToDevice.writeLog("[Normal] -- PbVideoActivity", "totalFileSize =" + videoFileSize);
		double minute = videoFileSize / 60.0;
		DecimalFormat df = new DecimalFormat("#.##");
		CharSequence what = getResources().getString(R.string.gallery_download_with_vid_msg).replace("$1$", "1").replace("$2$", df.format(minute));
		builder.setMessage(what);
		builder.setNegativeButton(R.string.gallery_download, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				stopThread();
				WriteLogToDevice.writeLog("[Normal] -- PbVideoActivity", "showProgressDialog");
				downloadProcess = 0;
				showProgressDialog(R.string.dialog_downloading_single);
				executor = Executors.newSingleThreadExecutor();
				future = executor.submit(new DownloadThread(), null);//
				downloadProgressTimer = new Timer();
				downloadProgressTimer.schedule(new DownloadProcessTask(), 0, 1000);
			}
		});
		builder.setPositiveButton(R.string.gallery_cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				mode = MODE_VIDEO_IDLE;
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	public void showDeleteEnsureDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(PbVideoActivity.this);
		builder.setCancelable(false);
		builder.setTitle(getResources().getString(R.string.gallery_delete_des).replace("$1$", "1"));
		builder.setNegativeButton(R.string.gallery_delete, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				stopThread();
				// WriteLogToDevice.writeLog("[Normal] -- PbVideoActivity",
				// "showProgressDialog");
				showProgressDialog(R.string.dialog_deleting);
				executor = Executors.newSingleThreadExecutor();
				// WriteLogToDevice.writeLog("[Normal] -- PbVideoActivity",
				// "submit(new DeleteThread(), null)");
				future = executor.submit(new DeleteThread(), null);
				// WriteLogToDevice.writeLog("[Normal] -- PbVideoActivity",
				// "end submit(new DeleteThread(), null)");
			}
		});
		builder.setPositiveButton(R.string.gallery_cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				mode = MODE_VIDEO_IDLE;
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	public void showProgressDialog(int resource) {
		ingFrag = new ProgressDialog(this);
		ingFrag.setCancelable(false);
		// CharSequence cs = getResources().getString(resource);
		ingFrag.setTitle(resource);
		if (resource == R.string.dialog_deleting) {
			ingFrag.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		} else if (resource == R.string.dialog_downloading_single) {
			ingFrag.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			ingFrag.setMax(100);
			ingFrag.setButton(getResources().getString(R.string.gallery_cancel), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					WriteLogToDevice.writeLog("[Normal] -- PbVideoActivity", "--------------video pb: cancel download ---------------");
					if (fileOperation.cancelDownload()) {
						// cancelDownloadFlag = true;
						handler.obtainMessage(CANCEL_VIDEO_DOWNLOAD_SUCCESS, 0, 0, null).sendToTarget();
					}
				}
			});
		}
		ingFrag.show();
	}

	public class CacheStateChangedListener implements ICatchWificamListener {
		@Override
		public void eventNotify(ICatchEvent arg0) {
			// TODO Auto-generated method stub
			// WriteLogToDevice.writeLog("[Normal] -- PbVideoActivity",
			// "--------------CacheStateChangedListener arg0.getIntValue2() =" +
			// arg0.getIntValue2());
			handler.obtainMessage(EVENT_CACHE_STATE_CHANGED, arg0.getIntValue1(), 0).sendToTarget();
		}
	}

	public class CacheProgressListener implements ICatchWificamListener {
		@Override
		public void eventNotify(ICatchEvent arg0) {
			// TODO Auto-generated method stub
			// WriteLogToDevice.writeLog("[Normal] -- PbVideoActivity",
			// "--------------CacheProgressListener arg0.getIntValue2() =" +
			// arg0.getIntValue2()*100);
			int temp = new Double(arg0.getDoubleValue1() * 100).intValue();
			handler.obtainMessage(EVENT_CACHE_PROGRESS_NOTIFY, arg0.getIntValue1(), temp).sendToTarget();
		}
	}

	/**
	 * 
	 * Added by zhangyanhu C01012,2014-5-22
	 */
	public class VideoIsEndListener implements ICatchWificamListener {
		@Override
		public void eventNotify(ICatchEvent arg0) {
			// TODO Auto-generated method stub
			WriteLogToDevice.writeLog("[Normal] -- PbVideoActivity", "--------------receive VideoIsEndListener ----------------");
			handler.obtainMessage(EVENT_VIDEO_PLAY_COMPLETED, 0, 0).sendToTarget();
		}
	}

	public void showLoadingCircle(boolean showFlag) {
		WriteLogToDevice.writeLog("[Normal] -- PbVideoActivity", "showLoadingCircle showFlag ="+showFlag);
		if (showFlag == true) {
			progressBar.setVisibility(View.VISIBLE);
			loadPercent.setVisibility(View.VISIBLE);
			loadPercent.setText("0%");
		} else {
			progressBar.setVisibility(View.GONE);
			loadPercent.setVisibility(View.GONE);
		}
	}

	public void setLoadingProgress(int percentage) {
		WriteLogToDevice.writeLog("[Normal] -- PbVideoActivity", "---------setSecondaryProgress = " + percentage);
		String temp = percentage + "%";
		loadPercent.setText(temp);
	}

	/**
	 * 
	 * Added by zhangyanhu C01012,2014-5-15
	 */
	class DownloadProcessTask extends TimerTask {

		@Override
		public void run() {
			WriteLogToDevice.writeLog("[Normal] -- PbVideoActivity", "begin DownloadProcessTask");
			File file = null;
			if (downloadingFilename != null) {
				file = new File(downloadingFilename);
			}
			if (file != null) {
				downloadProcess = file.length() * 100 / downloadingFilesize;
			} else {
				downloadProcess = 0;
			}
			// WriteLogToDevice.writeLog("[Normal] -- PbVideoActivity",
			// "downloadProcess = " + downloadProcess);
			// sendOkMsg(_UPDATE_LOADING_PROCESS, isDownloadNum, isDownloadNum +
			// downloadTaskList.size(), null);
			handler.obtainMessage(DOWNLOAD_VIDEO_PROGRESS_NOTIFY, (int) downloadProcess, 0).sendToTarget();
			WriteLogToDevice.writeLog("[Normal] -- PbVideoActivity", "end DownloadProcessTask");
		}
	}
}