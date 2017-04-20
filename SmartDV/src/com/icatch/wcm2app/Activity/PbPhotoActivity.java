/*
 * This activity is afford to implement something like download or
 * delete or anything else.
 * And it's facility to implement those function while not using
 * the 3rd party app.
 */

package com.icatch.wcm2app.Activity;

import java.io.File;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
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
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.icatch.wcm2app.R;
import com.icatch.wcm2app.ExtendComponent.DragImageView;
import com.icatch.wcm2app.common.BitmapDecode;
import com.icatch.wcm2app.common.BitmapUtil;
import com.icatch.wcm2app.common.ExitApp;
import com.icatch.wcm2app.common.GlobalApp;
import com.icatch.wcm2app.common.MediaRefresh;
import com.icatch.wcm2app.common.SystemCheckTools;
import com.icatch.wcm2app.common.WriteLogToDevice;
import com.icatch.wcm2app.controller.sdkApi.CameraProperties;
import com.icatch.wcm2app.controller.sdkApi.FileOperation;
import com.icatch.wificam.customer.type.ICatchFile;
import com.icatch.wificam.customer.type.ICatchFileType;
import com.icatch.wificam.customer.type.ICatchFrameBuffer;


public class PbPhotoActivity extends Activity {

	private FileOperation fileOperation = new FileOperation();
	private ViewPager viewpager;
	private ArrayList<View> viewList;
	private LayoutInflater inflater;
	private MyViewPagerAdapter pagerAdapter;
	private int photoNums = 0;
	private MyHandler photoHandler;
	private Bitmap decodeBitmap;
	// private Bitmap curBitmap;
	private ICatchFile curFile;
	private int curPhotoIdx;
	private RelativeLayout photo_top_bar;
	private RelativeLayout photo_bottom_bar;
	private RelativeLayout bottom_bg;
	private TextView curIdxInfoView;
	private Button photo_info;
	private Button photo_download;
	private Button photo_link;
	private Button photo_del;
	private Button slide2left;
	private Button slide2right;
	private ExecutorService executor;
	private Future<Object> future;

	private int lastDoneItem = -1;
	private int lastItem = -1;
	private int tempLastItem = -1;
	private boolean isScrolling = false;

	private List<ICatchFile> list;
	private List<ICatchFile> imageList;
	private ProgressDialog ingFrag;
	// private Resources res;

	/** xiao add for weixin share 2014-10-27 */
	/**
	 * 有微信分享功能 在onPause()中不能执行 ExitApp.getInstance().exit(); 否则程序会在分享时自动退出
	 */
//	private IWXAPI api; // 第三方app和微信通讯的openapi接口
	public CameraProperties cameraProperties = new CameraProperties();
	String TAG = "PbPhotoActivity";

	private Future<Object> bitMapFuture;
	private boolean completeLoadBitmap = true;
	private int slideDirection = DIRECTION_RIGHT;
	public LinkedList<TaskInfo> taskList;
	private Integer[] bitmapDoneList;
	private final static int PRE_LOADING_COUNT = 1;

	private final static int _DOWNLOAD_PHOTO_DONE = 0x01;
	public final static int _DOWNLOAD_PHOTO_ERR = 0x02;
	public final static int _DOWNLOAD_PHOTO_TO_SD_DONE = 0x03;
	public final static int _DOWNLOAD_PHOTO_TO_SD_DONE_No_SHARE = 0x09;
	private final static int _DELETE_PHOTO_DONE = 0x04;
	private final static int _DOWNLOAD_PHOTO_PROGRESS_NOTIFY = 0x05;
	private final static int _CANCEL_PHOTO_DOWNLOAD_SUCCESS = 0x06;
	private final static int _DELETE_FAILED = 0x07;

	private final static int DIRECTION_RIGHT = 0x1;
	private final static int DIRECTION_LEFT = 0x2;
	private final static int DIRECTION_UNKNOWN = 0x4;

	private final static int MODE_NORMAL = 0x1;
	private final static int MODE_DELETE = 0x2;
	private final static int MODE_DOWNLOAD = 0x3;
	private static int curMode = MODE_NORMAL;

	private int window_width, window_height; // 控件宽度
	private DragImageView dragImageView; // 自定义控件
	private int state_height; // 状态栏的高度

	private ViewTreeObserver viewTreeObserver;
	public String downloadingFilename;
	public long downloadProcess;
	public long downloadingFilesize;
	protected Timer downloadProgressTimer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		WriteLogToDevice.writeLog("[Normal] -- PbPhotoActivity: ",
				"begin onCreate");
		super.onCreate(savedInstanceState);
		GlobalApp.getInstance().setCurrentApp(PbPhotoActivity.this);
		// 0048919 Add by zhangyanhu 2013.12.11
		// never sleep when run this activity
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		// 0048919 End add by zhangyanhu 2013.12.11
		getWindow().getDecorView().setSystemUiVisibility(
				View.SYSTEM_UI_FLAG_VISIBLE);
		setContentView(R.layout.pb_photo);

		if (list == null) {
			GlobalApp myApp = (GlobalApp) getApplication();
			list = myApp.getFileList();
			// photoNums = list.size();
		}
		// photoClient = GlobalView.getInstance().getplaybackClient();
		// res = getResources();

		// xiaoyang @20141111

		// if(GlobalApp.getProductId() == GlobalApp.Product_D12S){
//		if (cameraProperties.getCurrentWiFiMode() == GlobalApp.WiFi_STA_MODE) {
//			// regToWx();
//		}

		taskList = new LinkedList<TaskInfo>();
		bitmapDoneList = new Integer[3];
		for (int ii = 0; ii < 3; ii++) {
			bitmapDoneList[ii] = -1;
		}

		// The method getHeight() from the type Display is deprecated modify by
		// xiaoyang 201-10-25
		/*
		 * WindowManager manager = getWindowManager(); window_width =
		 * manager.getDefaultDisplay().getWidth(); window_height =
		 * manager.getDefaultDisplay().getHeight();
		 */
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		window_width = dm.widthPixels;
		window_height = dm.heightPixels;

		photo_top_bar = (RelativeLayout) findViewById(R.id.photo_top_bar);
		photo_bottom_bar = (RelativeLayout) findViewById(R.id.photo_bottom_bar);
		bottom_bg = (RelativeLayout) findViewById(R.id.bottom_bg);
		curIdxInfoView = (TextView) findViewById(R.id.curIdxInfo);
		photo_info = (Button) findViewById(R.id.photo_info);
		photo_download = (Button) findViewById(R.id.photo_download);
		photo_link = (Button) findViewById(R.id.photo_link);
		photo_del = (Button) findViewById(R.id.photo_del);
		slide2left = (Button) findViewById(R.id.slide2left);
		slide2right = (Button) findViewById(R.id.slide2right);
		viewpager = (ViewPager) findViewById(R.id.viewpager);

		photoHandler = new MyHandler();
		slideDirection = DIRECTION_UNKNOWN;

		Intent intent = getIntent();
		Bundle data = intent.getExtras();
		curPhotoIdx = data.getInt("curIdx");
		WriteLogToDevice.writeLog("[Normal] -- PbPhotoActivity", "curPhotoIdx"
				+ curPhotoIdx);

		viewList = new ArrayList<View>();
		inflater = getLayoutInflater();
		WriteLogToDevice.writeLog("[Normal] -- PbPhotoActivity: ",
				"begin:get photos from sdk,photoClient.listFiles");
		imageList = fileOperation.getFileList(ICatchFileType.ICH_TYPE_IMAGE);
		WriteLogToDevice.writeLog("[Normal] -- PbPhotoActivity: ",
				"end: get photos from sdk,photoClient.listFiles");
		for (int i = 0; i < imageList.size(); i++) {
			// only images can be added to viewpager,so delete the vedio
			viewList.add(i, null);
		}
		// WriteLogToDevice.writeLog("[Normal] -- PbPhotoActivity: ","end for(int i=0; i<imageList.size(); i++) {"+viewList.);

		photoNums = viewList.size();
		// End modify by zhangyanhu 2013.12.5
		pagerAdapter = new MyViewPagerAdapter(viewList);
		viewpager.setAdapter(pagerAdapter);
		viewpager.setCurrentItem(curPhotoIdx);
		ShowCurPageNum();
		WriteLogToDevice.writeLog("[Normal] -- PbPhotoActivity",
				"start LoadBitMapThread()");
		// 0048990 Add by zhangyanhu 2013.12.20
		executor = Executors.newSingleThreadExecutor();
		WriteLogToDevice.writeLog("[Normal] -- PbPhotoActivity: ",
				"startLoadBitmapThread,curPhotoIdx=" + curPhotoIdx);
		startLoadBitmapThread(curPhotoIdx);
		// 0048990 End add by zhangyanhu 2013.12.20

		viewpager
				.setOnPageChangeListener(new MyViewPagerOnPagerChangeListener());

		photo_info.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				int curIdx = viewpager.getCurrentItem();

				int curPhoto = curIdx + 1;
				AlertDialog.Builder builder = new AlertDialog.Builder(
						PbPhotoActivity.this);
				builder.setTitle("  " + curPhoto + " (" + photoNums + ")");
				builder.setMessage("EXIF Info.");
				builder.setPositiveButton("Exit",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});

				AlertDialog dialog = builder.create();
				dialog.setCancelable(false);
				dialog.show();
			}
		});

		photo_download.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				WriteLogToDevice
						.writeLog("[Normal] -- PbPhotoActivity: ",
								"onclick photo_download.........set curMode = MODE_DOWNLOAD");
				curMode = MODE_DOWNLOAD;
				showDownloadEnsureDialog();
			}
		});

		// photo_link.setVisibility(View.GONE);
//		if (cameraProperties.getCurrentWiFiMode() == GlobalApp.WiFi_STA_MODE) {
//			photo_link.setVisibility(View.VISIBLE);
//		}
//		photo_link.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				int curphotoIdx = viewpager.getCurrentItem(); // 当前照片索引
//				Log.i("0000000000", "curIdx = " + curphotoIdx + ",name = "
//						+ list.get(curphotoIdx).getFileName());
//
//				File file = new File(Environment.getExternalStorageDirectory()
//						+ "/DCIM/" + list.get(curphotoIdx).getFileName());
//				NoShowMain show = new NoShowMain();
//				if (show.checkApkExist(getApplicationContext(),
//						"com.tencent.mm")) {
//					if (!file.exists()) // 判断本地是否存在图片
//					{
//
//						Toast.makeText(PbPhotoActivity.this,
//								R.string.sharing_pictures_platform,
//								Toast.LENGTH_LONG).show();
//						executor = Executors.newSingleThreadExecutor(); // 下载图片
//						future = executor.submit(new DownloadThread(), null);
//
//					} else {
//
//						String sharefilename = Environment
//								.getExternalStorageDirectory()
//								+ "/DCIM/"
//								+ list.get(curPhotoIdx).getFileName();
//						Bitmap bt = BitmapFactory.decodeFile(sharefilename);
//						final Uri uri = Uri.parse(MediaStore.Images.Media
//								.insertImage(getContentResolver(), bt, null,
//										null));
//						shareToFriendsLine(uri);
//					}
//				} else {
//					Toast.makeText(getApplicationContext(),
//							R.string.weiChat_instanll, Toast.LENGTH_LONG)
//							.show();
//				}
//			}
//		});

		photo_del.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				WriteLogToDevice.writeLog("[Normal] -- PbPhotoActivity: ",
						"onclick delete.........set curMode = MODE_DELETE");
				curMode = MODE_DELETE;
				showDeleteEnsureDialog();
			}
		});

		slide2left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				curPhotoIdx = viewpager.getCurrentItem();
				if (curPhotoIdx != 0) {
					WriteLogToDevice.writeLog("[Normal] -- PbPhotoActivity",
							"slide2left curPhotoIdx =" + 0);
					lastItem = curPhotoIdx;
					curPhotoIdx--;
					viewpager.setCurrentItem(curPhotoIdx);
					// 20140108
					if (bitMapFuture != null) {
						bitMapFuture.cancel(true);
					}
					startLoadBitmapThread(curPhotoIdx);
				}
			}
		});

		slide2right.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				curPhotoIdx = viewpager.getCurrentItem();
				if (curPhotoIdx != viewList.size() - 1) {
					WriteLogToDevice.writeLog("[Normal] -- PbPhotoActivity",
							"slide2right curPhotoIdx =" + curPhotoIdx);
					lastItem = curPhotoIdx;
					curPhotoIdx++;
					viewpager.setCurrentItem(curPhotoIdx);
					// 20140108
					if (bitMapFuture != null) {
						bitMapFuture.cancel(true);
					}
					startLoadBitmapThread(curPhotoIdx);
				}
			}
		});

		ExitApp.getInstance().addActivity(this);
	}

	@Override
	protected void onDestroy() {
		WriteLogToDevice.writeLog("[Normal] -- PbPhotoActivity", "onDestroy");
		/*
		 * if(mLocationClient.isStarted()){ mLocationClient.stop(); //
		 * xiaoyang@20141127 }
		 */
		super.onDestroy();
		if (executor != null) {
			executor.shutdown();
			try {
				if (!executor.awaitTermination(500, TimeUnit.MILLISECONDS)) {
					executor.shutdownNow();
				}
			} catch (InterruptedException e) {
				executor.shutdownNow();
				Thread.currentThread().interrupt();
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		WriteLogToDevice.writeLog("[Normal] -- PbPhotoActivity", "onKeyDown");
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
		case KeyEvent.KEYCODE_HOME:
			taskList.clear();
			if (future != null && !future.isDone()) {
				future.cancel(true);
			}
			if (executor != null) {
				executor.shutdown();
				try {
					if (!executor.awaitTermination(500, TimeUnit.MILLISECONDS)) {
						executor.shutdownNow();
					}
				} catch (InterruptedException e) {
					executor.shutdownNow();
					Thread.currentThread().interrupt();
				}
			}
			finish();
			break;
		default:
			break;
		}

		return super.onKeyDown(keyCode, event);
	}

	public class MyViewPagerAdapter extends PagerAdapter {

		private List<View> mListViews;

		public MyViewPagerAdapter(List<View> mListViews) {
			this.mListViews = mListViews;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			if (position < mListViews.size()) {
				container.removeView(mListViews.get(position));
				viewList.set(position, null);
			}
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			WriteLogToDevice.writeLog("[Normal] -- PbPhotoActivity: ",
					"instantiateItem viewpager position=" + position);
			View v = inflater.inflate(R.layout.pb_photo_item, null);

			v.findViewById(R.id.photo).setOnClickListener(
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							if (photo_top_bar.getVisibility() == View.GONE) {
								photo_top_bar.setVisibility(View.VISIBLE);
								photo_bottom_bar.setVisibility(View.VISIBLE);
								bottom_bg.setVisibility(View.VISIBLE);
							} else {
								photo_top_bar.setVisibility(View.GONE);
								photo_bottom_bar.setVisibility(View.GONE);
								bottom_bg.setVisibility(View.GONE);
							}
						}
					});
			photo_info.setClickable(false);
			photo_link.setClickable(false);
			photo_del.setClickable(false);
			photo_download.setClickable(false);
			viewList.set(position, v);
			container.addView(v, 0);
			return v;
		}

		@Override
		public int getCount() {
			return mListViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
	}

	private class MyViewPagerOnPagerChangeListener implements
			OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {

			switch (arg0) {
			case ViewPager.SCROLL_STATE_DRAGGING:
				isScrolling = true;
				tempLastItem = viewpager.getCurrentItem();
				break;
			case ViewPager.SCROLL_STATE_SETTLING:
				if (isScrolling == true && tempLastItem != -1
						&& tempLastItem != viewpager.getCurrentItem()) {
					lastItem = tempLastItem;
				}
				isScrolling = false;
				if (bitMapFuture != null) {
					bitMapFuture.cancel(true);
				}
				startLoadBitmapThread(viewpager.getCurrentItem());
				break;
			case ViewPager.SCROLL_STATE_IDLE:
				break;
			}

			ShowCurPageNum();
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int arg0) {
			ShowCurPageNum();

			// photo_info.setClickable(false);
			// photo_link.setClickable(false);
			// WriteLogToDevice.writeLog("[Normal] -- PbPhotoActivity",
			// "2222222222222222 photo_del.setClickable(false);");
			// photo_del.setClickable(false);
			// photo_download.setClickable(false);
		}
	};

	// 0048990 Add by zhangyanhu 2013.12.20
	private class LoadBitMapThread implements Runnable {
		private int curIdx;
		private int tempFileHandle;

		@Override
		public void run() {
			// the last operation is not complete,so wait because the sdk only
			// allow to load one file once
			WriteLogToDevice.writeLog("[Normal] -- PbPhotoActivity: ",
					"begin:LoadBitMapThread");
			TaskInfo taskInfo = null;
			while (taskList.isEmpty() == false) {
				while (completeLoadBitmap == false) {
					// wait......
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				// Add by zhangyanhu 2013.12.3
				// whether the file exits in the cache or not?
				boolean exit = false;
				while (exit == false) {
					if (taskList.isEmpty()) {
						return;
					}
					taskInfo = taskList.getFirst();
					curIdx = taskInfo.pageID;
					tempFileHandle = taskInfo.fileHandle;
					if (viewList.get(curIdx) != null) {
						exit = true;
						break;
					}
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				// End add by zhangyanhu 2013.12.3
				curFile = list.get(curIdx);

				WriteLogToDevice.writeLog("[Normal] -- PbPhotoActivity: ",
						"try to get bitmap ...... ");
				// start fetch photo from the ptp-ip camera
				ICatchFrameBuffer buffer = null;
				completeLoadBitmap = false;
				buffer = fileOperation.downloadFile(curFile);
				completeLoadBitmap = true;
				completeLoadBitmap = true;
				if (buffer == null || buffer.getFrameSize() <= 0) {
					WriteLogToDevice.writeLog("[Error] -- PbPhotoActivity: ",
							"buffer == null or buffer.getDataSize() <= 0 ");
					sendOkMsg(_DOWNLOAD_PHOTO_ERR, curIdx, 0, taskInfo);
					return;
				}

				decodeBitmap = BitmapDecode.decodeSampledBitmapFromByteArray(
						buffer.getBuffer(), 0, buffer.getFrameSize(),
						getResources().getDisplayMetrics().widthPixels / 2,
						getResources().getDisplayMetrics().heightPixels / 2);

				if (decodeBitmap == null) {
					WriteLogToDevice.writeLog("[Error] -- PbPhotoActivity: ",
							"decodeBitmap is null");
					sendOkMsg(_DOWNLOAD_PHOTO_ERR, curIdx, 0, taskInfo);
					return;
				}
				// Told UI thread
				sendOkMsg(_DOWNLOAD_PHOTO_DONE, curIdx, lastDoneItem, taskInfo);
			}
			WriteLogToDevice.writeLog("[Normal] -- PbPhotoActivity: ",
					"dowloading is ok,running is end");
		}
	}

	// 0048990 End add by zhangyanhu 2013.12.20
	// private class DownloadThread implements Runnable {
	public class DownloadThread implements Runnable {
		@Override
		public void run() {
			WriteLogToDevice
					.writeLog("PbPhotoActivity", "begin DownloadThread");
			GlobalApp.getInstance().setDownloadStatus(true);
			// String path =
			// Environment.getExternalStorageDirectory().toString() +
			// GlobalApp.DOWNLOAD_PATH;
			String path;
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				path = Environment.getExternalStorageDirectory().toString()
						+ GlobalApp.DOWNLOAD_PATH;
			} else {
				sendOkMsg(_DOWNLOAD_PHOTO_ERR, 0, 0, null);
				return;
			}

			String fileName = list.get(viewpager.getCurrentItem())
					.getFileName();
			WriteLogToDevice.writeLog("[Normal] -- PbPhotoActivity",
					"------------fileName =" + fileName);
			File file = new File(path);
			if (!file.exists()) {
				file.mkdirs();
			}
			WriteLogToDevice.writeLog("[Normal] -- PbPhotoActivity: ",
					"start downloadFile");
			downloadingFilename = path + fileName;
			downloadingFilesize = list.get(viewpager.getCurrentItem())
					.getFileSize();
			File tempFile = new File(downloadingFilename);
			if (tempFile.exists()) {
				try {
					MediaRefresh.scanFileAsync(PbPhotoActivity.this, path
							+ fileName);
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				sendOkMsg(_DOWNLOAD_PHOTO_TO_SD_DONE, 0, 0, null);
				return;
			}
			boolean temp = false;
			temp = fileOperation.downloadFile(
					list.get(viewpager.getCurrentItem()), downloadingFilename);
			if (temp == false) {
				sendOkMsg(_DOWNLOAD_PHOTO_ERR, 0, 0, null);
				GlobalApp.getInstance().setDownloadStatus(false);
				return;
			}
			MediaRefresh.scanFileAsync(PbPhotoActivity.this, path + fileName);
			WriteLogToDevice.writeLog("[Normal] -- PbPhotoActivity: ",
					"end downloadFile temp =" + temp);
			GlobalApp.getInstance().setDownloadStatus(false);
			sendOkMsg(_DOWNLOAD_PHOTO_TO_SD_DONE, 0, 0, null);
			WriteLogToDevice.writeLog("[Normal] -- PbPhotoActivity: ",
					"end DownloadThread");
		}
	}

	public class DownloadPictureThread implements Runnable {
		@Override
		public void run() {
			WriteLogToDevice
					.writeLog("PbPhotoActivity", "begin DownloadThread");
			GlobalApp.getInstance().setDownloadStatus(true);
			// String path =
			// Environment.getExternalStorageDirectory().toString() +
			// GlobalApp.DOWNLOAD_PATH;
			String path;
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				path = Environment.getExternalStorageDirectory().toString()
						+ GlobalApp.DOWNLOAD_PATH;
			} else {
				sendOkMsg(_DOWNLOAD_PHOTO_ERR, 0, 0, null);
				return;
			}

			String fileName = list.get(viewpager.getCurrentItem())
					.getFileName();
			WriteLogToDevice.writeLog("[Normal] -- PbPhotoActivity",
					"------------fileName =" + fileName);
			File file = new File(path);
			if (!file.exists()) {
				file.mkdirs();
			}
			WriteLogToDevice.writeLog("[Normal] -- PbPhotoActivity: ",
					"start downloadFile");
			downloadingFilename = path + fileName;
			downloadingFilesize = list.get(viewpager.getCurrentItem())
					.getFileSize();
			File tempFile = new File(downloadingFilename);
			if (tempFile.exists()) {
				try {
					MediaRefresh.scanFileAsync(PbPhotoActivity.this, path
							+ fileName);
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				sendOkMsg(_DOWNLOAD_PHOTO_TO_SD_DONE_No_SHARE, 0, 0, null);
				return;
			}
			boolean temp = false;
			temp = fileOperation.downloadFile(
					list.get(viewpager.getCurrentItem()), downloadingFilename);
			if (temp == false) {
				sendOkMsg(_DOWNLOAD_PHOTO_ERR, 0, 0, null);
				GlobalApp.getInstance().setDownloadStatus(false);
				return;
			}
			MediaRefresh.scanFileAsync(PbPhotoActivity.this, path + fileName);
			WriteLogToDevice.writeLog("[Normal] -- PbPhotoActivity: ",
					"end downloadFile temp =" + temp);
			GlobalApp.getInstance().setDownloadStatus(false);
			sendOkMsg(_DOWNLOAD_PHOTO_TO_SD_DONE_No_SHARE, 0, 0, null);
			WriteLogToDevice.writeLog("[Normal] -- PbPhotoActivity: ",
					"end DownloadThread");
		}
	}

	private class DeleteThread implements Runnable {
		@Override
		public void run() {
			curFile = list.get(viewpager.getCurrentItem());
			// fileOperation.deleteFile(curFile);
			Boolean retValue = false;
			retValue = fileOperation.deleteFile(curFile);
			if (retValue == false) {
				sendOkMsg(_DELETE_FAILED, 0, 0, null);
				WriteLogToDevice.writeLog("[Normal] -- PbPhotoActivity: ",
						"end DeleteThread");
				return;
			}
			sendOkMsg(_DELETE_PHOTO_DONE, 0, 0, null);
			WriteLogToDevice.writeLog("[Normal] -- PbPhotoActivity: ",
					"end DeleteThread");
		}
	}

	private class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {

			int what = msg.what;
			int arg1 = msg.arg1;
			TaskInfo taskInfo = (TaskInfo) msg.obj;
			View v;
			ProgressBar bar;

			switch (what) {
			case _DOWNLOAD_PHOTO_DONE:
				WriteLogToDevice.writeLog("[Normal] -- PbPhotoActivity: ",
						"receive _DOWNLOAD_PHOTO_DONE");
				lastDoneItem = arg1;
				WriteLogToDevice.writeLog("[Normal] -- PbPhotoActivity: ",
						"arg1 =" + arg1);

				// may be preloaded bitmaps
				if (arg1 >= 0 && taskList.contains(taskInfo)) {
					addToDoneList(arg1);
					taskList.remove(taskInfo);
					v = viewList.get(arg1);
					if (v != null) {
						WriteLogToDevice.writeLog(
								"[Normal] -- PbPhotoActivity: ",
								"imageView.setImageBitmap arg1=" + arg1);
						bar = (ProgressBar) v.findViewById(R.id.progressBar1);
						dragImageView = (DragImageView) v
								.findViewById(R.id.photo);
						dragImageView.setImageBitmap(BitmapUtil.getBitmap(
								decodeBitmap, window_width, window_height));
						dragImageView.setmActivity(PbPhotoActivity.this);
						viewTreeObserver = dragImageView.getViewTreeObserver();
						viewTreeObserver
								.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

									@Override
									public void onGlobalLayout() {
										if (state_height == 0) {
											// 获取状况栏高度
											Rect frame = new Rect();
											getWindow()
													.getDecorView()
													.getWindowVisibleDisplayFrame(
															frame);
											state_height = frame.top;
											dragImageView
													.setScreen_H(window_height
															- state_height);
											dragImageView
													.setScreen_W(window_width);
										}
									}
								});
						bar.setVisibility(View.GONE);
					}
				}
				photo_info.setClickable(true);
				photo_link.setClickable(true);
				photo_del.setClickable(true);
				photo_download.setClickable(true);
				break;
			case _DOWNLOAD_PHOTO_ERR:
				WriteLogToDevice.writeLog("[Normal] -- PbPhotoActivity: ",
						"receive _DOWNLOAD_PHOTO_ERR");
				if (ingFrag != null) {
					ingFrag.dismiss();
				}
				Toast.makeText(PbPhotoActivity.this, "Download failed",
						Toast.LENGTH_SHORT).show();
				break;
			case _DOWNLOAD_PHOTO_TO_SD_DONE:
				Log.w("[Normal] -- PbPhotoActivity",
						"receive _DOWNLOAD_PHOTO_TO_SD_DONE");
				if (ingFrag != null) {
					ingFrag.dismiss();
				}
				// Toast.makeText(PbPhotoActivity.this,
				// "Downloaded to"+GlobalApp.DOWNLOAD_PATH,
				// Toast.LENGTH_SHORT).show();
				// modify by xiaoyang 修改下载完成提示路径及文件名 2014-10-25
				Toast.makeText(
						PbPhotoActivity.this,
						"Downloaded to" + GlobalApp.DOWNLOAD_PATH
								+ list.get(curPhotoIdx).getFileName(),
						Toast.LENGTH_SHORT).show();
			//	NoShowMain show = new NoShowMain();
//				if (show.checkApkExist(getApplicationContext(),
//						"com.tencent.mm")) {
//
//					String sharefilename = Environment
//							.getExternalStorageDirectory()
//							+ GlobalApp.DOWNLOAD_PATH
//							+ list.get(curPhotoIdx).getFileName();
//					Bitmap bt = BitmapFactory.decodeFile(sharefilename);
//					final Uri uri = Uri.parse(MediaStore.Images.Media
//							.insertImage(getContentResolver(), bt, null, null));
//					shareToFriendsLine(uri);
//				} else {
////					Toast.makeText(getApplicationContext(),
////							R.string.weiChat_instanll, Toast.LENGTH_LONG)
////							.show();
//				}

				break;
			case _DOWNLOAD_PHOTO_TO_SD_DONE_No_SHARE:
				Log.w("[Normal] -- PbPhotoActivity",
						"receive  _DOWNLOAD_PHOTO_TO_SD_DONE_No_SHARE");
				if (ingFrag != null) {
					ingFrag.dismiss();
				}
				Toast.makeText(
						PbPhotoActivity.this,
						"Downloaded to" + GlobalApp.DOWNLOAD_PATH
								+ list.get(curPhotoIdx).getFileName(),
						Toast.LENGTH_SHORT).show();
				break;
			case _DELETE_FAILED:
				WriteLogToDevice.writeLog("[Normal] -- PbPhotoActivity: ",
						"receive:_DELETE_FAILED");
				if (ingFrag != null) {
					ingFrag.dismiss();
				}
				Toast.makeText(PbPhotoActivity.this,
						R.string.dialog_delete_failed_single,
						Toast.LENGTH_SHORT).show();
				break;
			case _DELETE_PHOTO_DONE:
				WriteLogToDevice.writeLog("[Normal] -- PbPhotoActivity: ",
						"receive:_DELETE_PHOTO_DONE");
				if (ingFrag != null) {
					ingFrag.dismiss();
				}

				// delete from list
				list.remove(viewpager.getCurrentItem());
				GlobalApp myApp = (GlobalApp) getApplication();
				myApp.setFileList(list);

				// //0048990 Add by zhangyanhu 2013.12.20
				curPhotoIdx = viewpager.getCurrentItem();
				viewList.remove(curPhotoIdx);
				pagerAdapter.notifyDataSetChanged();
				viewpager.setAdapter(pagerAdapter);
				WriteLogToDevice.writeLog("[Normal] -- PbPhotoActivity: ",
						"curPhotoIdx =  " + curPhotoIdx);

				photoNums = viewList.size();
				if (photoNums == 0) {
					finish();
					break;
				} else {
					WriteLogToDevice.writeLog("[Normal] -- PbPhotoActivity: ",
							"photoNums =  " + photoNums);
					ShowCurPageNum();
					if (photoNums == 1) {
						curPhotoIdx = 0;
					} else if (curPhotoIdx == photoNums) {// the last photo
						WriteLogToDevice.writeLog(
								"[Normal] -- PbPhotoActivity: ",
								"curPhotoIdx == photoNums  ");
						curPhotoIdx = curPhotoIdx - 1;
					}
				}

				WriteLogToDevice.writeLog("[Normal] -- PbPhotoActivity: ",
						"delete:curPhotoIdx  " + curPhotoIdx);
				viewpager.setCurrentItem(curPhotoIdx);
				if (bitMapFuture != null) {
					bitMapFuture.cancel(true);
				}
				for (int ii = 0; ii < 3; ii++) {
					bitmapDoneList[ii] = -1;
				}
				WriteLogToDevice.writeLog("[Normal] -- PbPhotoActivity: ",
						"startLoadBitmapThread curPhotoIdx = " + curPhotoIdx);
				startLoadBitmapThread(curPhotoIdx);
				// 0048990 End add by zhangyanhu 2013.12.20
				// End modify by zhangyanhu 2013.12.5
				break;
			case _DOWNLOAD_PHOTO_PROGRESS_NOTIFY:
				WriteLogToDevice.writeLog("[Normal] -- PbPhotoActivity: ",
						"receive _DOWNLOAD_PHOTO_PROGRESS_NOTIFY");
				if (curMode == MODE_DOWNLOAD) {
					ingFrag.setProgress(msg.arg1);
				}
				break;
			case _CANCEL_PHOTO_DOWNLOAD_SUCCESS:
				WriteLogToDevice.writeLog("[Normal] -- PbPhotoActivity: ",
						"receive DOWNLOAD_COMPLETED");
				ingFrag.dismiss();
				if (downloadProgressTimer != null) {
					downloadProgressTimer.cancel();
				}
				curMode = MODE_NORMAL;
				Toast.makeText(PbPhotoActivity.this,
						R.string.dialog_cancel_downloading_succeeded,
						Toast.LENGTH_SHORT).show();
				break;
			default:
				super.handleMessage(msg);
				break;
			}

		}
	}

	private void sendOkMsg(int what, int arg1, int arg2, Object obj) {
		photoHandler.obtainMessage(what, arg1, arg2, obj).sendToTarget();
	}

	private void ShowCurPageNum() {
		int curPhoto;
		// 0048975 Modify by zhangyanhu 2013.12.19
		if (photoNums == 0) {
			curPhoto = 0;
		} else {
			curPhoto = viewpager.getCurrentItem() + 1;
		}
		// 0048975 End Modify by zhangyanhu 2013.12.19
		curIdxInfoView.setText("" + curPhoto + " / " + photoNums);

	}

	@Override
	protected void onStart() {
		super.onStart();
		GlobalApp.getInstance().setAppContext(getApplicationContext());
		IntentFilter filter = new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		if (SystemCheckTools
				.isApplicationSentToBackground(PbPhotoActivity.this) == true) {
//			if (GlobalApp.getProductId() != GlobalApp.Product_D12S) { // xiaoyang@20141112
//				// ExitApp.getInstance().exit();
//			}
		}
		super.onPause();
	}

	@Override
	protected void onStop() {
		/*
		 * if(mLocationClient.isStarted()){ mLocationClient.stop(); //
		 * xiaoyang@20141127 }
		 */
		super.onStop();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

		} else {

		}
	}

	/**
	 * Added by zhangyanhu C01012,2014-1-9
	 */
	public int getSlideDirection(int nextPageID) {
		if (lastItem == -1) {
			slideDirection = DIRECTION_RIGHT;// by default
		} else if (lastItem > nextPageID) {
			slideDirection = DIRECTION_LEFT;
		} else if (lastItem < nextPageID) {
			slideDirection = DIRECTION_RIGHT;
		}
		return slideDirection;
	}

	/**
	 * Added by zhangyanhu C01012,2014-1-9
	 */
	public void clearViewPagerBitmap(int startItem, int count) {
		View v;
		ProgressBar bar;
		ImageView imageView;
		for (int i = startItem + 1; i < viewList.size() && i >= 0; i++) {
			v = viewList.get(i);
			bar = (ProgressBar) v.findViewById(R.id.progressBar1);
			imageView = (ImageView) v.findViewById(R.id.photo);
			imageView.setImageBitmap(null);
			bar.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * Added by zhangyanhu C01012,2014-1-9
	 */
	public void clearItem(int id) {

		if (id >= 0 && id < viewList.size()) {
			View v;
			ProgressBar bar;
			DragImageView imageView;
			v = viewList.get(id);
			if (v != null) {
				bar = (ProgressBar) v.findViewById(R.id.progressBar1);
				imageView = (DragImageView) v.findViewById(R.id.photo);
				imageView.setImageBitmap(null);
				bar.setVisibility(View.VISIBLE);
			}
		}
	}

	public void preLoadBitmaps(int startPosition) {

	}

	/**
	 * Added by zhangyanhu C01012,2014-1-10
	 */
	public void startLoadBitmapThread(int currentID) {
		if (currentID >= viewList.size() || currentID < 0) {
			return;
		}
		WriteLogToDevice.writeLog("[Normal] -- PbPhotoActivity",
				"startLoadBitmapThread  currentID=" + currentID);
		int slideDirection = getSlideDirection(currentID);
		// clear all tasks;
		taskList.clear();
		adjustDoneList();
		if (slideDirection == DIRECTION_RIGHT) {
			// clear viewpager
			clearItem(currentID - 2);
			// add task,and preload neibour 2 images
			TaskInfo taskInfo = new TaskInfo();
			taskInfo.pageID = currentID;
			taskInfo.fileHandle = list.get(currentID).getFileHandle();

			if (isExsitInDoneList(taskInfo.pageID) == false) {
				WriteLogToDevice.writeLog("[Normal] -- PbPhotoActivity",
						"isExsitInDoneList == false");
				taskList.addLast(taskInfo);
			} else {
				WriteLogToDevice.writeLog("[Normal] -- PbPhotoActivity",
						"isExsitInDoneList == true");
				photo_info.setClickable(true);
				photo_link.setClickable(true);
				photo_del.setClickable(true);
				photo_download.setClickable(true);
			}
			for (int ii = currentID + PRE_LOADING_COUNT; ii >= currentID
					- PRE_LOADING_COUNT; ii--) {
				if (ii == currentID) {
					continue;
				}
				if (ii >= 0 && ii < viewList.size()) {
					TaskInfo tempTaskInfo = new TaskInfo();
					tempTaskInfo.pageID = ii;
					tempTaskInfo.fileHandle = list.get(ii).getFileHandle();
					if (isExsitInDoneList(tempTaskInfo.pageID) == true) {
						continue;
					}
					taskList.addLast(tempTaskInfo);
				}
			}
		} else if (slideDirection == DIRECTION_LEFT) {
			// clear viewpager
			clearItem(currentID + 2);
			// add task,and preload neibour 2 images
			TaskInfo taskInfo = new TaskInfo();
			taskInfo.pageID = currentID;
			taskInfo.fileHandle = list.get(currentID).getFileHandle();
			if (isExsitInDoneList(taskInfo.pageID) == false) {
				WriteLogToDevice.writeLog("[Normal] -- PbPhotoActivity",
						"isExsitInDoneList == false");
				taskList.addLast(taskInfo);
			} else {
				WriteLogToDevice.writeLog("[Normal] -- PbPhotoActivity",
						"isExsitInDoneList == true");
				photo_info.setClickable(true);
				photo_link.setClickable(true);
				photo_del.setClickable(true);
				photo_download.setClickable(true);
			}
			for (int ii = currentID - PRE_LOADING_COUNT; ii <= currentID
					+ PRE_LOADING_COUNT; ii++) {
				if (ii == currentID) {
					continue;
				}
				if (ii >= 0 && ii < viewList.size()) {
					TaskInfo tempTaskInfo = new TaskInfo();
					tempTaskInfo.pageID = ii;
					tempTaskInfo.fileHandle = list.get(ii).getFileHandle();
					if (isExsitInDoneList(tempTaskInfo.pageID) == true) {
						// adjustDoneList(tempTaskInfo.pageID);
						continue;
					}
					taskList.addLast(tempTaskInfo);
				}
			}
		}

		if (taskList.isEmpty() == true) {
			return;
		}
		if (bitMapFuture == null || bitMapFuture.isDone()
				|| bitMapFuture.isCancelled()) {
			WriteLogToDevice.writeLog("[Normal] -- PbPhotoActivity",
					"new bitMapFuture ");
			bitMapFuture = executor.submit(new LoadBitMapThread(), null);
		} else {
			// do nothing
		}
	}

	/**
	 * Added by zhangyanhu C01012,2014-1-10
	 */
	public class TaskInfo {
		public int pageID = -1;
		public int fileHandle = -1;
	}

	/**
	 * Added by zhangyanhu C01012,2014-1-14
	 */
	public void adjustDoneList() {
		int temp = viewpager.getCurrentItem();
		// delete
		for (int ii = 0; ii < 3; ii++) {
			if (Math.abs(bitmapDoneList[ii] - temp) > 1
					&& bitmapDoneList[ii] >= 0) {
				bitmapDoneList[ii] = -1;
			}
		}
	}

	/**
	 * Added by zhangyanhu C01012,2014-3-3
	 */
	public void addToDoneList(int pageID) {
		for (int ii = 0; ii < 3; ii++) {
			if (bitmapDoneList[ii] == -1) {
				bitmapDoneList[ii] = pageID;
				return;
			}
		}
	}

	/**
	 * Added by zhangyanhu C01012,2014-1-14
	 */
	public boolean isExsitInDoneList(int pageID) {
		for (int ii = 0; ii < 3; ii++) {
			if (bitmapDoneList[ii] == pageID) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 读取本地资源的图片
	 * 
	 * @param context
	 * @param resId
	 * @returnregToWx()
	 */
	public static Bitmap ReadBitmapById(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// 获取资源图片
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

	/**
	 * 
	 * Added by zhangyanhu C01012,2014-5-19
	 */
	class DownloadProcessTask extends TimerTask {

		@Override
		public void run() {
			File file = null;
			if (downloadingFilename != null) {
				file = new File(downloadingFilename);
			}
			if (file != null) {
				downloadProcess = file.length() * 100 / downloadingFilesize;
			} else {
				downloadProcess = 0;
			}
			WriteLogToDevice.writeLog("[Normal] -- PbPhotoActivity",
					"downloadProcess = " + downloadProcess);
			sendOkMsg(_DOWNLOAD_PHOTO_PROGRESS_NOTIFY, (int) downloadProcess,
					0, null);
		}
	}

	/**
	 * 
	 * Added by zhangyanhu C01012,2014-5-19
	 */
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
			ingFrag.setButton(
					getResources().getString(R.string.gallery_cancel),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub
							WriteLogToDevice.writeLog(
									"[Normal] -- PbPhotoActivity",
									"cancel downloading");
							if (fileOperation.cancelDownload()) {
								sendOkMsg(_CANCEL_PHOTO_DOWNLOAD_SUCCESS, 0, 0,
										null);
							}
						}
					});
		}
		ingFrag.show();
	}

	/**
	 * 
	 * Added by zhangyanhu C01012,2014-5-19
	 */
	public void showDownloadEnsureDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				PbPhotoActivity.this);
		builder.setCancelable(false);
		builder.setTitle(R.string.dialog_downloading_single);
		long videoFileSize = 0;
		videoFileSize = list.get(curPhotoIdx).getFileSize() / 1024 / 1024;
		double minute = videoFileSize / 60.0;
		DecimalFormat df = new DecimalFormat("#.##");
		CharSequence what = getResources()
				.getString(R.string.gallery_download_with_vid_msg)
				.replace("$1$", "1").replace("$2$", df.format(minute));
		builder.setMessage(what);
		builder.setNegativeButton(R.string.gallery_download,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						WriteLogToDevice.writeLog(
								"[Normal] -- PbPhotoActivity: ",
								"showProgressDialog");
						downloadProcess = 0;
						showProgressDialog(R.string.dialog_downloading_single);
						executor = Executors.newSingleThreadExecutor();
						future = executor.submit(new DownloadPictureThread(),
								null);
						downloadProgressTimer = new Timer();
						downloadProgressTimer.schedule(
								new DownloadProcessTask(), 0, 1000);

					}
				});
		builder.setPositiveButton(R.string.gallery_cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						curMode = MODE_NORMAL;
						dialog.dismiss();
					}
				});
		builder.create().show();
	}

	/**
	 * 
	 * Added by zhangyanhu C01012,2014-5-19
	 */
	public void showDeleteEnsureDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				PbPhotoActivity.this);
		builder.setCancelable(false);
		builder.setTitle(R.string.image_delete_des);
		builder.setNegativeButton(R.string.gallery_delete,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// 这里添加点击确定后的逻辑
						showProgressDialog(R.string.dialog_deleting);
						taskList.clear();
						future = executor.submit(new DeleteThread(), null);
					}
				});
		builder.setPositiveButton(R.string.gallery_cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// 这里添加点击确定后的逻辑
						curMode = MODE_NORMAL;
						dialog.dismiss();
					}
				});
		builder.create().show();
	}

	// public void regToWx() {
	// api = WXAPIFactory.createWXAPI(this, GlobalApp.appId, true); //
	// 获得IWXAPI实例
	// api.registerApp(GlobalApp.appId); //注册aapid
	// }

	// 分享图片
//	public void sendShareImageReq(String filename) {
//		// 分享图片
//		Log.d("sendReq", "分享图片");
//		WXImageObject imageObject = new WXImageObject(); // 用WXImageObject对象初始化一个WXMediaMessage对象
//		imageObject.imagePath = Environment.getExternalStorageDirectory()
//				+ "/DCIM/" + filename;
//
//		WXMediaMessage msg = new WXMediaMessage();
//		msg.mediaObject = imageObject;
//		// 构造一个Req
//		SendMessageToWX.Req req = new SendMessageToWX.Req();
//		req.message = msg;
//		// req.transaction = buildTransaction("text");
//		req.transaction = String.valueOf(System.currentTimeMillis()); // 唯一字段，标识一个请求
//		req.scene = SendMessageToWX.Req.WXSceneTimeline; // WXSceneTimeline： 朋友圈
//															// ；WXSceneSession：
//															// 朋友
//		if (api.sendReq(req)) {
//			Log.d("sendReq", "分享图片成功");
//		} else {
//			Log.d("sendReq", "分享图片失败");
//		}
//
//	}

	// 分享文字
//	public void sendShareMessageReq() {
//		WXTextObject textObject = new WXTextObject();
//		textObject.text = "123456789";
//		WXMediaMessage msg = new WXMediaMessage(); // 用WXTextObject对象初始化一个WXMediaMessage对象
//		msg.mediaObject = textObject;
//		msg.description = textObject.text;
//
//		SendMessageToWX.Req req = new SendMessageToWX.Req(); // 构造一个Req
//		req.message = msg;
//		req.transaction = String.valueOf(System.currentTimeMillis()); // 唯一字段，标识一个请求
//		req.scene = SendMessageToWX.Req.WXSceneTimeline; // WXSceneTimeline： 朋友圈
//															// ；WXSceneSession：
//															// 朋友
//		if (api.sendReq(req)) {
//			Log.d("sendReq", "分享信息成功");
//		} else {
//			Log.d("sendReq", "分享信息失败");
//		}
//	}

	// 分享链接 图片+文字
//	public void sendShareLinkReq(String filename) {
//		String sharefilename = Environment.getExternalStorageDirectory()
//				+ "/DCIM/" + filename;
//		WXWebpageObject webpage = new WXWebpageObject();
//		webpage.webpageUrl = "http://weixin.qq.com/"; // 要分享的链接地址
//		WXMediaMessage msg = new WXMediaMessage(webpage);
//		msg.title = "title"; // 要分享的链接标题
//		msg.description = "description"; // 要分享的链接描述
//		try {
//			Bitmap bmp = BitmapFactory.decodeFile(sharefilename);
//			// Bitmap bmp = BitmapFactory.decodeResource(getResources(),
//			// R.drawable.select_all); //分享本地资源图片
//			Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 320, 240, true);
//			// Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 512, 480, true);
//			bmp.recycle();
//			msg.setThumbImage(thumbBmp);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		SendMessageToWX.Req req = new SendMessageToWX.Req();
//		req.transaction = String.valueOf(System.currentTimeMillis());
//		req.message = msg;
//		req.scene = SendMessageToWX.Req.WXSceneTimeline;
//		api.sendReq(req);
//	}

	// 分享图片
	private void shareToFriendsLine(Uri uri) {
		Intent intent = new Intent();
		ComponentName comp = new ComponentName("com.tencent.mm",
				"com.tencent.mm.ui.tools.ShareToTimeLineUI");
		intent.setComponent(comp);
		intent.setAction("android.intent.action.SEND");
		intent.setType("image/*");
		// intent.setFlags(0x3000001);
		intent.putExtra(Intent.EXTRA_STREAM, uri);
		startActivity(intent);
	}

}