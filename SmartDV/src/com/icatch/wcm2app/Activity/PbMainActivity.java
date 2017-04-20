package com.icatch.wcm2app.Activity;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.LruCache;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.icatch.wcm2app.adapter.MpbPhotoAdapter;
import com.icatch.wcm2app.common.BitmapsLoad;
import com.icatch.wcm2app.common.ExitApp;
import com.icatch.wcm2app.common.GlobalApp;
import com.icatch.wcm2app.common.MediaRefresh;
import com.icatch.wcm2app.common.PbDownloadManager;
import com.icatch.wcm2app.common.SystemCheckTools;
import com.icatch.wcm2app.common.WriteLogToDevice;
import com.icatch.wcm2app.common.BitmapsLoad.ThreadInfo;
import com.icatch.wcm2app.controller.sdkApi.CameraProperties;
import com.icatch.wcm2app.controller.sdkApi.FileOperation;
import com.icatch.wcm2app.R;
import com.icatch.wificam.customer.type.ICatchFile;
import com.icatch.wificam.customer.type.ICatchFileType;

public class PbMainActivity extends FragmentActivity  {
	private FileOperation fileOperation = new FileOperation();
	private Button downPhotoBtn;
	private Button editToggle;
	private Button selectAll;
	private RelativeLayout downloadManager;
	//private TextView downloadTitle;
	private TextView totalNumText;
	private ProgressBar progressBar1;
	private static GridView photoWall;
	private ExecutorService executor;
	private Future<Object> getListFuture;
	private Future<Object> downloadFuture;
	private MpbPhotoAdapter photoWallAdapter;
	private static final String[] viewStyles = { "Photo", "Video" };
	private ArrayAdapter<String> adapter;
	private List<ICatchFile> fileList;
	private List<ICatchFile> actList;
	private LinkedList<ICatchFile> deleteFailedList;
	private LinkedList<ICatchFile> downloadTaskList;
	private Map<Integer, Boolean> uiMap;
	private ProgressDialog deleteProgressDialog;
	private Resources res;
	private static final int MESSAGE_GET_PHOTO_LIST_DONE = 0x1;
	private static final int MESSAGE_DOWNLOAD_DONE = 0x2;
	private static final int MESSAGE_DOWNLOAD_FAIL = 0x3;
	private static final int MESSAGE_DELETE_DONE = 0x4;
	private static final int MESSAGE_DELETE_FAIL = 0x5;
	public final static int MESSAGE_LOAD_BITMAP_SUCCESS = 10;
	public final static int MESSAGE_LOAD_BITMAP_FAILED = 11;
	public final static int MESSAGE_ALL_TASKS_DONE = 12;
	public static final int MESSAGE_SHARE_DOWNLOAD_COMPLETED = 13;

	private static final int ACTION_IDLE = 0x01;
	private static final int ACTION_DO_DELETE = 0x02;
	private static final int ACTION_DO_DOWNLOAD = 0x04;

	public static final int MODE_PLAYBACK = 0x01;
	public static final int MODE_OPERATION = 0x02;
	
	
	private static final int MEDIA_TYPE_VIDEO = 0X1;
	private static final int MEDIA_TYPE_IMAGE = 0X2;

	// Modified by zhangyanhu 2013.11.26
	private static int pbMode = MODE_PLAYBACK;
	// End modify by zhangyanhu 2013.11.26
	private static int curAction = ACTION_IDLE;
	private int width;
	private int fileHandle;

	private BitmapsLoad bitmapsLoad;
	private LruCache<Integer, ThreadInfo> bitmapMemeryCache;
	final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
	final int cacheSize = maxMemory / 8;

	private boolean selectAllChoose = false;
	//private HashMap<ICatchFile, DownloadInfo> downloadInfoMap;
	public CameraProperties cameraProperties = new CameraProperties();
	//private Button photo_share;
	private ArrayList<Uri> uriArrayList;

	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		WriteLogToDevice.writeLog("[Normal] -- PbMainActivity: ", "PbMainActivity:onCreate  ");
		GlobalApp.getInstance().setCurrentApp(PbMainActivity.this);
		// 0048919 Add by zhangyanhu 2013.12.11
		// never sleep when run this activity
	//	getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		// 0048919 End add by zhangyanhu 2013.12.11
		setContentView(R.layout.pb_main);
		// Add by zhangyanhu 2013.12.2
		
		DisplayMetrics dm = new DisplayMetrics();
		PbMainActivity.this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		width = dm.widthPixels;
		// End add by zhangyanhu 2013.12.2

		// zhangyanhu 2013.12.27
		bitmapMemeryCache = new LruCache<Integer, ThreadInfo>(cacheSize) {

			@Override
			protected int sizeOf(Integer key, ThreadInfo value) {
				return value.mBitmap.getByteCount() / 1024;
			}
		};

		uiMap = new HashMap<Integer, Boolean>();

		downloadManager = (RelativeLayout) findViewById(R.id.downloadManager);
		downPhotoBtn = (Button) findViewById(R.id.downPhoto);
		progressBar1 = (ProgressBar) findViewById(R.id.loadingBar);
		// deleted by zhangyanhu 2013.11.25
		photoWall = (GridView) findViewById(R.id.photo_wall);
		editToggle = (Button) findViewById(R.id.editToggle);
		selectAll = (Button) findViewById(R.id.select_all);
		totalNumText = (TextView) findViewById(R.id.totalNumText);
		//photo_share = (Button) findViewById(R.id.share);

		res = getResources();
	
		executor = Executors.newSingleThreadExecutor();
		bitmapsLoad = BitmapsLoad.getInstance();
		bitmapsLoad.initData();
		//BitmapsLoad.setGridView(photoWall);

		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, viewStyles);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// End deleted by zhangyanhu 2013.11.25

		// Added by zhangyanhu 2013.11.25
		// Display the images and video by default
		photoWall.setVisibility(View.VISIBLE);
		WriteLogToDevice.writeLog("[Normal] -- PbMainActivity: ", "PbMainActivity:Create GetFileListThread  ");
		// to ensure get the filelist first,then can enter edit ui
		editToggle.setVisibility(View.GONE);
		downPhotoBtn.setVisibility(View.GONE);
		selectAll.setVisibility(View.GONE);
		//photo_share.setVisibility(View.GONE);
		if (getListFuture == null && !executor.isShutdown()) {
			getListFuture = executor.submit(new GetFileListThread(), null);
		}
	

//		photo_share.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				if(actList.isEmpty()) {
//					Toast.makeText(PbMainActivity.this, R.string.gallery_no_file_selected, Toast.LENGTH_SHORT).show();
//					}
//				//ICatchFile tempFile = list.get(curPhotoIdx);
//				String path = Environment.getExternalStorageDirectory().getPath()+"/temp/";
//				AlertDialog.Builder builder = new AlertDialog.Builder(PbMainActivity.this);
//				builder.setCancelable(false);
//				builder.setMessage("分享首先会下载文件到本地，请确认要分享吗？");
//				builder.setPositiveButton(res.getString(R.string.gallery_cancel), new DialogInterface.OnClickListener() {
//
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						dialog.dismiss();
//					}
//				});
//				builder.setNegativeButton(res.getString(R.string.ok), new DialogInterface.OnClickListener() {
//
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						dialog.dismiss();
//
//						selectAll.setVisibility(View.GONE);
//						photo_share.setVisibility(View.GONE);
//						DownloadThread downloadThread = new DownloadThread();
//						downloadThread.start();
//						progressDialog = new ProgressDialog(PbMainActivity.this);
//						progressDialog.setTitle("分享。。。。。。。。");
//						progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//						progressDialog.setMessage("正在下载到本地。。。。");
//						progressDialog.setCancelable(false);
//						progressDialog.show();
//					}
//				});
//				builder.create().show();
//			}
//		});
		selectAll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (pbMode == MODE_PLAYBACK) {
					pbMode = MODE_OPERATION;
					editToggle.setBackgroundResource(R.drawable.ic_delete);
				}

				if (selectAllChoose == false) {
					if (actList != null) {
						actList.clear();
					}
					actList.addAll(fileList);
					if (uiMap != null) {
						uiMap.clear();
					}

					for (int ii = 0; ii < fileList.size(); ii++) {
						uiMap.put(fileList.get(ii).getFileHandle(), true);
					}
					ArrayList<View> visibleView = photoWall.getTouchables();
					for (int ii = 0; ii < visibleView.size(); ii++) {
						visibleView.get(ii).findViewById(R.id.edit_icon).setBackgroundResource(R.drawable.image_confirm);
						visibleView.get(ii).findViewById(R.id.edit_icon).setVisibility(View.VISIBLE);
						visibleView.get(ii).findViewById(R.id.photo_bg).setVisibility(View.VISIBLE);
					}
					selectAllChoose = true;
					selectAll.setBackgroundResource(R.drawable.cancel);
				} else if (selectAllChoose == true) {
					if (actList != null) {
						actList.clear();
					}
					if (uiMap != null) {
						uiMap.clear();
					}

					ArrayList<View> visibleView = photoWall.getTouchables();
					for (int ii = 0; ii < visibleView.size(); ii++) {
						visibleView.get(ii).findViewById(R.id.edit_icon).setBackgroundResource(R.drawable.image_unconfirm);
						visibleView.get(ii).findViewById(R.id.edit_icon).setVisibility(View.VISIBLE);
						visibleView.get(ii).findViewById(R.id.photo_bg).setVisibility(View.GONE);
					}
					selectAllChoose = false;
					selectAll.setBackgroundResource(R.drawable.select_all);
				}

				CharSequence cs = res.getString(R.string.gallery_selection_count).replace("$1$", String.valueOf(actList.size()))
						.replace("$2$", String.valueOf(fileList.size()));
				totalNumText.setText(cs);
			}

		});
		// End add by zhangyanhu 2013.11.25
		// Download all files by default
		downPhotoBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				WriteLogToDevice.writeLog("[Normal] -- PbMainActivity: ", "press downPhotoBtn......");
				// Download all files by default
				// preview mode
				if (pbMode == MODE_PLAYBACK) {
					if (fileList.size() <= 0) {
						Toast.makeText(PbMainActivity.this, R.string.gallery_no_image, Toast.LENGTH_SHORT).show();
						return;
					}
					curAction = ACTION_DO_DOWNLOAD;
					//actList.clear();
					// get actList,full filled actList
					downloadTaskList.clear();
					for (int i = 0; i < fileList.size(); i++) {
						downloadTaskList.add(fileList.get(i));
					}
					long totalFileSize = 0;
					for (int ii = 0; ii < downloadTaskList.size(); ii++) {
						totalFileSize += downloadTaskList.get(ii).getFileSize();
					}
					if(SystemCheckTools.getSDFreeSize() <= totalFileSize){
						Toast.makeText(PbMainActivity.this, R.string.dialog_card_full, Toast.LENGTH_LONG).show();
						curAction = ACTION_IDLE;
						return;
					}
					totalFileSize = totalFileSize/1024/1024;
					long minute = totalFileSize / 60;
					long seconds = totalFileSize % 60;
					Log.d("1111","minute ="+minute);
					Log.d("1111","seconds ="+seconds);
					CharSequence what = res.getString(R.string.gallery_download_with_vid_msg).replace("$1$", String.valueOf(downloadTaskList.size()))
							.replace("$3$", String.valueOf(seconds))
							.replace("$2$", String.valueOf(minute));
					AlertDialog.Builder builder = new AlertDialog.Builder(PbMainActivity.this);
					builder.setCancelable(false);
					builder.setMessage(what);
					builder.setPositiveButton(res.getString(R.string.gallery_cancel), new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
					builder.setNegativeButton(res.getString(R.string.gallery_download), new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();

							selectAll.setVisibility(View.GONE);
							//photo_share.setVisibility(View.GONE);
							PbDownloadManager pbDownloadManager = new PbDownloadManager(PbMainActivity.this,downloadTaskList);
							pbDownloadManager.show();
						}
					});
					builder.create().show();
				} else if (actList.isEmpty()) {
					Toast.makeText(PbMainActivity.this, R.string.gallery_no_file_selected, Toast.LENGTH_SHORT).show();
				} else {
					curAction = ACTION_DO_DOWNLOAD;
					long totalFileSize = 0;
					for (int ii = 0; ii < actList.size(); ii++) {
						totalFileSize += actList.get(ii).getFileSize();
						//WriteLogToDevice.writeLog("[Normal] -- PbMainActivity: ", "totalFileSize =" + totalFileSize);
					}
					if(SystemCheckTools.getSDFreeSize() <= totalFileSize){
						Toast.makeText(PbMainActivity.this, R.string.dialog_card_full, Toast.LENGTH_LONG).show();
						curAction = ACTION_IDLE;
						return;
					}
					totalFileSize = totalFileSize /1024 /1024;
					long minute = totalFileSize / 60;
					long seconds = totalFileSize % 60;
					Log.d("1111","minute ="+minute);
					Log.d("1111","seconds ="+seconds);
					Log.d("1111","downloadTaskList.size() ="+downloadTaskList.size());
					CharSequence what = res.getString(R.string.gallery_download_with_vid_msg).replace("$1$", String.valueOf(actList.size()))
							.replace("$3$", String.valueOf(seconds))
							.replace("$2$", String.valueOf(minute));
					AlertDialog.Builder builder = new AlertDialog.Builder(PbMainActivity.this);
					builder.setCancelable(false);
					builder.setMessage(what);
					builder.setPositiveButton(res.getString(R.string.gallery_cancel), new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
					builder.setNegativeButton(res.getString(R.string.gallery_download), new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							downloadTaskList.clear();
							//downloadInfoMap.clear();
							for (int ii = 0; ii < actList.size(); ii++) {
								downloadTaskList.addLast(actList.get(ii));
							}
							selectAll.setVisibility(View.GONE);
							//photo_share.setVisibility(View.GONE);
							PbDownloadManager pbDownloadManager = new PbDownloadManager(PbMainActivity.this,downloadTaskList);
							pbDownloadManager.show();
						}
					});
					builder.create().show();
				}

			}
		});
		// End modify by zhangyanhu 2013.11.26
		editToggle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (pbMode == MODE_PLAYBACK) {
					pbMode = MODE_OPERATION;
					editToggle.setBackgroundResource(R.drawable.ic_delete);
					CharSequence cs = res.getString(R.string.gallery_selection_count).replace("$1$", String.valueOf(actList.size()))
							.replace("$2$", String.valueOf(fileList.size()));
					totalNumText.setText(cs);
					selectAll.setVisibility(View.VISIBLE);
					//photo_share.setVisibility(View.VISIBLE);
					if (actList != null) {
						actList.clear();
					}
				} else {
					if (actList.isEmpty()) {
						Toast.makeText(PbMainActivity.this, R.string.gallery_no_file_selected, Toast.LENGTH_SHORT).show();
					} else {
						curAction = ACTION_DO_DELETE;

						CharSequence what = res.getString(R.string.gallery_delete_des).replace("$1$", String.valueOf(actList.size()));
						AlertDialog.Builder builder = new AlertDialog.Builder(PbMainActivity.this);
						builder.setCancelable(false);
						builder.setMessage(what);
						builder.setPositiveButton(res.getString(R.string.gallery_cancel), new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						});
						
						builder.setNegativeButton(res.getString(R.string.gallery_delete), new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
								deleteProgressDialog = new ProgressDialog(PbMainActivity.this);
								deleteProgressDialog.setCancelable(false);
								deleteProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
								deleteProgressDialog.setTitle(res.getString(R.string.dialog_deleting));
								deleteProgressDialog.show();
								executor.submit(new DeleteThread(), null);		
							}
						});
						builder.create().show();
					}
				}
			}
		});

		progressBar1.setVisibility(View.VISIBLE);

		photoWall.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				
				enableButtom(false);
				if (pbMode == MODE_PLAYBACK) {
					// Added by zhangyanhu 20131125
					// Judge for Image or Video,Video preview is not supported
					WriteLogToDevice.writeLog("[Normal] -- PbMainActivity: ", "photoWall.setOnItemClickListener");
					if (fileList.get(arg2).getFileType() == ICatchFileType.ICH_TYPE_VIDEO) {
						//Log.e("tigertiger","start cameraProperties.supportVideoPlayback()");
					    File file = new File(Environment.getExternalStorageDirectory()+GlobalApp.DOWNLOAD_PATH+fileList.get(arg2).getFileName());  
					    if(!file.exists())    								// 判断本地是否存在同名影片  ,不存在提示，存在调用本地播放器播放
					    {
							if (cameraProperties.supportVideoPlayback() == false) {
								Toast.makeText(PbMainActivity.this, R.string.gallery_cannot_view_video, Toast.LENGTH_SHORT).show();
								enableButtom(true);
								return;
							}
					    }else {
					    	
					    	String strPath;
							strPath = Environment.getExternalStorageDirectory()+GlobalApp.DOWNLOAD_PATH+fileList.get(arg2).getFileName();
						
							Intent intent = new Intent("android.intent.action.VIEW");   
							intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);   
							intent.putExtra ("oneshot", 0);   
							intent.putExtra ("configchange", 0); 
							Uri uri = Uri.fromFile(new File(strPath)); 
							
							intent.setDataAndType (uri, "video/*");   
							startActivity(intent); 
							
							return;
						}
					    /*
						if (cameraProperties.supportVideoPlayback() == false) {
							Toast.makeText(PbMainActivity.this, R.string.gallery_cannot_view_video, Toast.LENGTH_SHORT).show();
							enableButtom(true);
							return;
						}
						*/
						//Log.e("tigertiger","end cameraProperties.supportVideoPlayback()");
						Intent intent = new Intent();
						//Log.e("tigertiger","start bitmapsLoad.cancelAllTasks");
						bitmapsLoad.cancelAllTasks();
						//Log.e("tigertiger","end bitmapsLoad.cancelAllTasks");
						intent.putExtra("curIndex", arg2);
						intent.setClass(PbMainActivity.this, PbVideoActivity.class);
						startActivity(intent);
						WriteLogToDevice.writeLog("[Normal] -- PbMainActivity: ", "start intent PbVideoActivity.class");
						return;
					} else if (fileList.get(arg2).getFileType() == ICatchFileType.ICH_TYPE_IMAGE) {
						bitmapsLoad.cancelAllTasks();
						Intent intent = new Intent();
						Bundle data = new Bundle();
						data.putInt("curIdx", arg2);
						intent.putExtras(data);
						intent.setClass(PbMainActivity.this, PbPhotoActivity.class);
						startActivity(intent);
						WriteLogToDevice.writeLog("[Normal] -- PbMainActivity: ", "start intent PbPhotoActivity.class");
						return;
					}
					// End add by zhangyanhu 20131125
				} else {
					markView(arg1, arg2);
				}
				enableButtom(true);
			}
		});

		photoWall.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

				if (pbMode == MODE_PLAYBACK) {
					pbMode = MODE_OPERATION;
					editToggle.setBackgroundResource(R.drawable.ic_delete);
					CharSequence cs = res.getString(R.string.gallery_selection_count).replace("$1$", String.valueOf(actList.size()))
							.replace("$2$", String.valueOf(fileList.size()));
					totalNumText.setText(cs);
					selectAll.setVisibility(View.VISIBLE);
					//photo_share.setVisibility(View.VISIBLE);
				}
				markView(arg1, arg2);

				// Make sure it return true.
				return true;
			}
		});

		actList = new ArrayList<ICatchFile>();
		//downloadInfoMap = new HashMap<ICatchFile, DownloadInfo>();
		downloadTaskList = new LinkedList<ICatchFile>();
		uriArrayList = new ArrayList<Uri>();
		ExitApp.getInstance().addActivity(this);
		WriteLogToDevice.writeLog("[Normal] -- PbMainActivity: ", "end onCreate");
	}

	private void markView(View v, int idx) {
		// Modify by zhangyanhu 2013.12.04
		fileHandle = fileList.get(idx).getFileHandle();
		boolean b = uiMap.containsKey(fileHandle);
		if (b && uiMap.get(fileHandle)) {
			WriteLogToDevice.writeLog("[Normal] -- PbMainActivity: ", "GONE");
			v.findViewById(R.id.edit_icon).setBackgroundResource(R.drawable.image_unconfirm);
			v.findViewById(R.id.photo_bg).setVisibility(View.GONE);
			actList.remove(fileList.get(idx));
			uiMap.remove(fileHandle);// to avoid same keys
			uiMap.put(fileHandle, false);

		} else {
			WriteLogToDevice.writeLog("[Normal] -- PbMainActivity: ", "VISIBLE");
			v.findViewById(R.id.edit_icon).setBackgroundResource(R.drawable.image_confirm);
			v.findViewById(R.id.edit_icon).setVisibility(View.VISIBLE);
			v.findViewById(R.id.photo_bg).setVisibility(View.VISIBLE);
			actList.add(fileList.get(idx));
			uiMap.put(fileHandle, true);
		}
		// End modify by zhangyanhu 2013.12.04
		CharSequence cs = res.getString(R.string.gallery_selection_count).replace("$1$", String.valueOf(actList.size()))
				.replace("$2$", String.valueOf(fileList.size()));
		totalNumText.setText(cs);
	}

	// Add by zhangyanhu 2014.01.03
	private void cancelMarkView() {
		int selectFileHandle;
		ImageView selectImageView;
		View selectView;
		for (int i = 0; i < actList.size(); i++) {
			selectFileHandle = actList.get(i).getFileHandle();
			selectImageView = (ImageView) photoWall.findViewWithTag(selectFileHandle);
			if (selectImageView == null) {
				continue;
			}
			selectView = (View) selectImageView.getParent();
			if (selectView == null) {
				continue;
			}
			selectView.findViewById(R.id.edit_icon).setVisibility(View.GONE);
			selectView.findViewById(R.id.photo_bg).setVisibility(View.GONE);
		}
		actList.clear();
		uiMap.clear();
	}

	// Add by zhangyanhu 2014.01.03

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if (downloadManager.getVisibility() == View.VISIBLE) {
				//alertForQuitDownload();
				break;
			}
			if (pbMode == MODE_OPERATION) {
				WriteLogToDevice.writeLog("[Normal] -- PbMainActivity: ", "pbMode = " + pbMode);
				// Modified by zhangyanhu 2013.11.26
				totalNumText.setText("");
				// End modify by zhangyanhu 2013.11.26
				switch2Nor();
			} else {
				bitmapsLoad.cancelAllTasks();
				bitmapsLoad.killLoadThread();
				bitmapMemeryCache.evictAll();
				finish();
			}

			break;
//		case KeyEvent.KEYCODE_HOME:
//			WriteLogToDevice.writeLog("[Normal] -- PbMainActivity: ", "KEYCODE_HOME ");
//			ExitApp.getInstance().exit();
		default:
			return super.onKeyDown(keyCode, event);
		}
		return true;
	}

	@Override
	public void onDestroy() {
		WriteLogToDevice.writeLog("[Normal] -- PbMainActivity: ", "onDestroy.....");
		super.onDestroy();

		if (getListFuture != null && !getListFuture.isDone()) {
			WriteLogToDevice.writeLog("[Normal] -- PbMainActivity: ", "cancel photo thread");
			getListFuture.cancel(true);
		}
		if (downloadFuture != null && !downloadFuture.isDone()) {
			downloadFuture.cancel(true);
		}

		executor.shutdown(); // Disable new tasks from being submitted
		WriteLogToDevice.writeLog("[Normal] -- PbMainActivity: ", "wait executor shutdown accomplish");
		try {
			if (!executor.awaitTermination(1, TimeUnit.SECONDS)) {
				WriteLogToDevice.writeLog("[Normal] -- PbMainActivity: ", "shutdownNow");
				executor.shutdownNow(); // Cancel currently executing tasks
			}
		} catch (InterruptedException e) {
			// (Re-)cancel if current thread also interrupted
			executor.shutdownNow();
			// Preserve interrupt status
			Thread.currentThread().interrupt();
		}
		WriteLogToDevice.writeLog("[Normal] -- PbMainActivity: ", "accomplish executor shutdown");

		// close all the AsyncTasks if it exist
		bitmapsLoad.cancelAllTasks();
	}

	@Override
	protected void onStart() {
		super.onStart();
		WriteLogToDevice.writeLog("[Normal] -- PbMainActivity: ", "onStart");
		GlobalApp.getInstance().setAppContext(getApplicationContext());
		bitmapsLoad.initData();
	}

	@Override
	protected void onResume() {
		super.onResume();
		WriteLogToDevice.writeLog("[Normal] -- PbMainActivity: ", "onResume");
		enableButtom(true);
		GlobalApp myApp = (GlobalApp) getApplication();
		fileList = myApp.getFileList();
//		Toast.makeText(getApplicationContext(), fileList.size()+":::::::::::", 0).show();	
		if (fileList != null) {
			WriteLogToDevice.writeLog("[Normal] -- PbMainActivity: ", " -- invalidate");
			/**
			 * Those two ways works well all in this place.
			 */
			if (photoWallAdapter != null) {
				photoWallAdapter.initFirstIn(true);
				photoWallAdapter.notifyDataSetChanged();
				photoWall.invalidate();
		
			}
		
			// Update total photo numbers to show.
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		WriteLogToDevice.writeLog("[Normal] -- PbMainActivity: ", "onStop");
		if(SystemCheckTools.isApplicationSentToBackground(PbMainActivity.this) == true){
			if (getListFuture != null && !getListFuture.isDone()) {
				WriteLogToDevice.writeLog("[Normal] -- PbMainActivity: ", "cancel photo thread");
				getListFuture.cancel(true);
			}
			if (downloadFuture != null && !downloadFuture.isDone()) {
				downloadFuture.cancel(true);
			}

			executor.shutdown(); // Disable new tasks from being submitted
			WriteLogToDevice.writeLog("[Normal] -- PbMainActivity: ", "wait executor shutdown accomplish");
			try {
				if (!executor.awaitTermination(1, TimeUnit.SECONDS)) {
					WriteLogToDevice.writeLog("[Normal] -- PbMainActivity: ", "shutdownNow");
					executor.shutdownNow(); // Cancel currently executing tasks
				}
			} catch (InterruptedException e) {
				// (Re-)cancel if current thread also interrupted
				executor.shutdownNow();
				// Preserve interrupt status
				Thread.currentThread().interrupt();
			}
			WriteLogToDevice.writeLog("[Normal] -- PbMainActivity: ", "accomplish executor shutdown");

			// close all the AsyncTasks if it exist
			bitmapsLoad.cancelAllTasks();
			ExitApp.getInstance().exit();
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		WriteLogToDevice.writeLog("[Normal] -- PbMainActivity: ", "onPause");
		if(SystemCheckTools.isApplicationSentToBackground(PbMainActivity.this) == true){
			if (getListFuture != null && !getListFuture.isDone()) {
				WriteLogToDevice.writeLog("[Normal] -- PbMainActivity: ", "cancel photo thread");
				getListFuture.cancel(true);
			}
			if (downloadFuture != null && !downloadFuture.isDone()) {
				downloadFuture.cancel(true);
			}

			executor.shutdown(); // Disable new tasks from being submitted
			WriteLogToDevice.writeLog("[Normal] -- PbMainActivity: ", "wait executor shutdown accomplish");
			try {
				if (!executor.awaitTermination(1, TimeUnit.SECONDS)) {
					WriteLogToDevice.writeLog("[Normal] -- PbMainActivity: ", "shutdownNow");
					executor.shutdownNow(); // Cancel currently executing tasks
				}
			} catch (InterruptedException e) {
				// (Re-)cancel if current thread also interrupted
				executor.shutdownNow();
				// Preserve interrupt status
				Thread.currentThread().interrupt();
			}
			WriteLogToDevice.writeLog("[Normal] -- PbMainActivity: ", "accomplish executor shutdown");

			// close all the AsyncTasks if it exist
			bitmapsLoad.cancelAllTasks();
			ExitApp.getInstance().exit();
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// Add by zhangyanhu 2013.12.2
		WriteLogToDevice.writeLog("[Normal] -- PbMainActivity: ", "onConfigurationChanged");
		super.onConfigurationChanged(newConfig);
		DisplayMetrics dm = new DisplayMetrics();
		PbMainActivity.this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		width = dm.widthPixels;
		photoWallAdapter = new MpbPhotoAdapter(PbMainActivity.this, processHandler, fileList, width, bitmapMemeryCache, actList,photoWall);
		WriteLogToDevice.writeLog("[Normal] -- PbMainActivity: ", "onConfigurationChanged()  MpbPhotoAdapter =" + fileList.size());
		photoWallAdapter.initFirstIn(true);
		photoWall.setAdapter(photoWallAdapter);
		photoWallAdapter.notifyDataSetInvalidated();
	
	}

	class GetFileListThread implements Runnable {

		@Override
		public void run() {
			WriteLogToDevice.writeLog("[Normal] -- PbMainActivity: ", "begin: GetFileListThread");
			GlobalApp myApp = (GlobalApp) getApplication();
			myApp.setFileList(null);
			LinkedList<ICatchFile> imageFileList = new LinkedList<ICatchFile>();
			List<ICatchFile> videoFileList = new LinkedList<ICatchFile>();
			// Modified by zhangyanhu 2013.11.25
			WriteLogToDevice.writeLog("[Normal] -- PbMainActivity: ", "GetFileListThread---start try get TYPE_ALLlist");
			fileList = fileOperation.getFileList(ICatchFileType.ICH_TYPE_ALL);
			
			WriteLogToDevice.writeLog("[Normal] -- PbMainActivity: ", "GetFileListThread---end try get TYPE_ALL list.size =" + fileList.size());
			for (int ii = 0; ii < fileList.size(); ii++) {
				if (fileList.get(ii).getFileType() == ICatchFileType.ICH_TYPE_IMAGE) {
					imageFileList.add(fileList.get(ii));
				} else if (fileList.get(ii).getFileType() == ICatchFileType.ICH_TYPE_VIDEO) {
					videoFileList.add(fileList.get(ii));
				}
			}
			// End modify by zhangyanhu 2013.11.25
			fileList.clear();
			if (imageFileList.size() > 0) {
				//Collections.reverse(imageFileList);
				fileList.addAll(imageFileList);
			}
			if (videoFileList.size() > 0) {
				//Collections.reverse(videoFileList);
				fileList.addAll(videoFileList);
			}
			// End add by zhangyanhu 2013.11.25
			myApp.setFileList(fileList);
			sendOkMsg(MESSAGE_GET_PHOTO_LIST_DONE, 0, 0, null);
			WriteLogToDevice.writeLog("[Normal] -- PbMainActivity: ", "GetFileListThread---fileList.size() =" + fileList.size());
		}
	}

	class DeleteThread implements Runnable {

		@Override
		public void run() {
			WriteLogToDevice.writeLog("[Normal] -- PbMainActivity: ", "DeleteThread");
			if(deleteFailedList == null){
				deleteFailedList = new LinkedList<ICatchFile> ();
			}else{
				deleteFailedList.clear();
			}
			for (ICatchFile f : actList) {
				WriteLogToDevice.writeLog("[Normal] -- PbMainActivity: ", "deleteFile f.getFileHandle =" + f.getFileHandle());
				if(fileOperation.deleteFile(f) == false){
					deleteFailedList.add(f);
					Log.e("tigertiger","delete failed");
				}
			}
			
			sendOkMsg(MESSAGE_DELETE_DONE, 0, 0, null);
			WriteLogToDevice.writeLog("[Normal] -- PbMainActivity: ", "send _DELETE_DONE");
		}
	}


	Handler processHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			BitmapsLoad.ThreadInfo threadInfo;

			switch (what) {
			case MESSAGE_GET_PHOTO_LIST_DONE:
				WriteLogToDevice.writeLog("[Normal] -- PbMainActivity: ", "receive _GET_PHOTO_LIST_DONE");
				// totalNumText.setText(fileList.size() + " files");
				if (fileList.size() <= 0) {
					progressBar1.setVisibility(View.GONE);
					editToggle.setVisibility(View.VISIBLE);
					break;
				}

				photoWallAdapter = new MpbPhotoAdapter(PbMainActivity.this, processHandler, fileList, width, bitmapMemeryCache, actList,photoWall);
				photoWall.setAdapter(photoWallAdapter);
				editToggle.setVisibility(View.VISIBLE);
				downPhotoBtn.setVisibility(View.VISIBLE);
				break;
			case MESSAGE_DOWNLOAD_DONE:
				WriteLogToDevice.writeLog("[Normal] -- PbMainActivity: ", "receive _DOWNLOAD_DONE");
				WriteLogToDevice.writeLog("[Normal] -- PbMainActivity: ", "_DOWNLOAD_DONE");
				if (curAction != ACTION_DO_DOWNLOAD) {
					break;
				}
				//downloadProgressDialog.dismiss();
				Toast.makeText(PbMainActivity.this, "Download to /DCIM/Camera/.", Toast.LENGTH_LONG).show();
				curAction = ACTION_IDLE;
				break;
			case MESSAGE_DOWNLOAD_FAIL:
				WriteLogToDevice.writeLog("[Normal] -- PbMainActivity: ", "receive _DOWNLOAD_FAIL");
				if (curAction != ACTION_DO_DOWNLOAD) {
					break;
				}
				//downloadProgressDialog.dismiss();
				curAction = ACTION_IDLE;
				Toast.makeText(PbMainActivity.this, R.string.function_not_impl, Toast.LENGTH_SHORT).show();
				break;
			case MESSAGE_DELETE_DONE:
				WriteLogToDevice.writeLog("[Normal] -- PbMainActivity: ", "receive _DELETE_DONE");
				deleteProgressDialog.dismiss();
				LinkedList<ICatchFile> deleteSuccess = new LinkedList<ICatchFile>();
				deleteSuccess.addAll(actList);
				uiMap.clear();
				if(deleteFailedList.isEmpty() == false){
					deleteSuccess.removeAll(deleteFailedList);
					for(int ii = 0; ii < deleteFailedList.size();ii++){
						uiMap.put(deleteFailedList.get(ii).getFileHandle(), true);
					}
				}
				Log.e("tigertiger","deleteSuccess.size() ="+deleteSuccess.size());
				fileList.removeAll(deleteSuccess);
				GlobalApp myApp = (GlobalApp) getApplication();
				myApp.setFileList(fileList);
				if(deleteSuccess.isEmpty() == false){
					actList.removeAll(deleteSuccess);
				}
				WriteLogToDevice.writeLog("[Normal] -- PbMainActivity: ", "notifyDataSetChanged");
				photoWallAdapter.notifyDataSetChanged();
				CharSequence cs1 = res.getString(R.string.gallery_selection_count).replace("$1$", String.valueOf(actList.size()))
						.replace("$2$", String.valueOf(fileList.size()));
				totalNumText.setText(cs1);
				curAction = ACTION_IDLE;
				if(deleteFailedList.isEmpty() == false){
					showDeleteFailedDialog();
				}
				break;
			// End modify by zhangyanhu 2013.12.4
			case MESSAGE_DELETE_FAIL:
				WriteLogToDevice.writeLog("[Normal] -- PbMainActivity: ", "receive _DELETE_FAIL");
				deleteProgressDialog.dismiss();

				Toast.makeText(PbMainActivity.this, R.string.dialog_delete_failed_single, Toast.LENGTH_SHORT).show();
				break;
			// one bitmap has loaded,set to gridview
			case MESSAGE_LOAD_BITMAP_SUCCESS:
				WriteLogToDevice.writeLog("[Normal] -- PbMainActivity: ", "receive BitmapsLoad._LOAD_BITMAP_SUCCESS:");
				threadInfo = (BitmapsLoad.ThreadInfo) msg.obj;
				WriteLogToDevice.writeLog("[Normal] -- PbMainActivity: ", "_LOAD_BITMAP_SUCCESS fileHandle = " + threadInfo.mFileHandle);
				ImageView imageView = (ImageView) photoWall.findViewWithTag(threadInfo.mFileHandle);// (threadInfo.position);
				if (imageView != null && threadInfo.mBitmap != null) {
					addThreadInfoToMemoryCache(threadInfo.mFileHandle, threadInfo);
					imageView.setImageBitmap(threadInfo.mBitmap);
				}
				break;
			case MESSAGE_LOAD_BITMAP_FAILED:
				WriteLogToDevice.writeLog("[Normal] -- PbMainActivity: ", "receive _LOAD_BITMAP_FAILED");
				break;
			case MESSAGE_ALL_TASKS_DONE:
				WriteLogToDevice.writeLog("[Normal] -- PbMainActivity: ", "receive _ALL_TASKS_DONE");
				progressBar1.setVisibility(View.GONE);
				editToggle.setVisibility(View.VISIBLE);
				break;
			case MESSAGE_SHARE_DOWNLOAD_COMPLETED:
				WriteLogToDevice.writeLog("[Normal] -- PbMainActivity: ", "receive MESSAGE_SHARE_DOWNLOAD_COMPLETED");
				for(int ii = 0;ii < uriArrayList.size();ii++){
					Log.d("tigertiger","uriArrayList.get(ii)"+uriArrayList.get(ii));
				}
				if(progressDialog != null){
					progressDialog.dismiss();
				}
				 Intent intent=new Intent(Intent.ACTION_SEND);
	                
	               //intent.setType("video/* | image/*");  
	               intent.setType("image/*");  
	               //intent.setType("image/*"); 
	               intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriArrayList);
	               //intent.putExtra(Intent.EXTRA_STREAM, uriArrayList.get(0)); 
	              
	               //intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"test@test.com"});
	               intent.putExtra(Intent.EXTRA_SUBJECT, "share");
	               intent.putExtra(Intent.EXTRA_TEXT, "hello,for test");
	               intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
	               startActivity(Intent.createChooser(intent, "share to"));
				//progressBar1.setVisibility(View.GONE);
				//editToggle.setVisibility(View.VISIBLE);
				break;
				
			default:
				super.handleMessage(msg);
				break;
			}

		}

	};

	private void sendOkMsg(int what, int arg1, int arg2, Object obj) {
		processHandler.obtainMessage(what, arg1, arg2, obj).sendToTarget();
	}

	private void switch2Nor() {
		pbMode = MODE_PLAYBACK;
		cancelMarkView();
		actList.clear();
		uiMap.clear();

		if (curAction == ACTION_DO_DELETE) {
			fileList.removeAll(actList);
			GlobalApp myApp = (GlobalApp) getApplication();
			myApp.setFileList(fileList);
			// End modify by zhangyanhu 2013.11.26
		}
		selectAllChoose = false;
		selectAll.setVisibility(View.GONE);
		//photo_share.setVisibility(View.GONE);
		editToggle.setBackgroundResource(R.drawable.ic_select);
	}

	// To add data to cache
	private void addThreadInfoToMemoryCache(int fileHandle, ThreadInfo threadInfo) {
		if (getThreadInfoFromMemoryCache(fileHandle) == null) {
			bitmapMemeryCache.put(fileHandle, threadInfo);
		}
	}

	public ThreadInfo getThreadInfoFromMemoryCache(Integer fileHandle) {
		return bitmapMemeryCache.get(fileHandle);
	}

	public ICatchFileType getFileTypeFromMemoryCache(Integer fileHandle) {
		return bitmapMemeryCache.get(fileHandle).fileType;
	}

	public ICatchFileType getFileTypeByHandle(int fHandle) {
		for (int ii = 0; ii < fileList.size(); ii++) {
			if (fileList.get(ii).getFileHandle() == fHandle) {
				return fileList.get(ii).getFileType();
			}
		}
		return null;
	}

	/**
	 * 
	 * Added by zhangyanhu C01012,2014-4-11
	 */
	private void enableButtom(boolean bool) {
		downPhotoBtn.setEnabled(bool);
		editToggle.setEnabled(bool);
		selectAll.setEnabled(bool);
	}

//	/**
//	 * 
//	 * Added by zhangyanhu C01012,2014-5-28
//	 */
//	public class DownloadInfo {
//		public ICatchFile file = null;
//		public long fileSize = 0;
//		public long curFileLength = 0;
//		public int progress = 0;
//		public DownloadInfo(ICatchFile file,long fileSize,long curFileLength,int progress){
//			this.file = file;
//			this.fileSize = fileSize;
//			this.curFileLength = curFileLength;
//			this.progress = progress;
//		}
//	}
	public static int getOperationStatus() {
		return pbMode;
	}
	
	public void showDeleteFailedDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(PbMainActivity.this);
		builder.setCancelable(false);
		builder.setMessage(res.getString(R.string.dialog_deleting_failed_multi).replace("$1$", String.valueOf(deleteFailedList.size())));
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}
	
	class DownloadThread extends Thread {

		@Override
		public void run() {
			downloadTaskList.clear();
			uriArrayList.clear();
			//downloadInfoMap.clear();
			for (int ii = 0; ii < actList.size(); ii++) {
				downloadTaskList.addLast(actList.get(ii));
			}
			GlobalApp.getInstance().setDownloadStatus(true);
			// cancelDownloadFlag = false;
			WriteLogToDevice.writeLog("[Normal] -- PbDownloadManager:", "DownloadThread");
			String path;
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				path = Environment.getExternalStorageDirectory().toString() + GlobalApp.DOWNLOAD_PATH;
			} else {
				//sendOkMsg(_DOWNLOAD_PHOTO_ERR, 0, 0, null);
				return;
			}
			WriteLogToDevice.writeLog("[Normal] -- PbDownloadManager:", "path: " + path);

			File file = new File(path);
			if (!file.exists()) {
				file.mkdirs();
			}
			int mediaType = 0;
			WriteLogToDevice.writeLog("[Normal] -- PbDownloadManager:", "count of download files =" + downloadTaskList.size());
			while (downloadTaskList.isEmpty() == false) {
				WriteLogToDevice.writeLog("[Normal] -- PbDownloadManager:", "downloadTaskList.size() =" + downloadTaskList.size());
				ICatchFile downloadIcatchFile = downloadTaskList.getFirst();
				//DownloadInfo downloadInfo = new DownloadInfo(downloadIcatchFile, downloadIcatchFile.getFileSize(), 0, 0, false);
				Log.e("tigertiger", "addLast downloadIcatchFile.getFileName()=" + downloadIcatchFile.getFileName());
				File tempFile = new File(path + downloadIcatchFile.getFileName());
				if (tempFile.exists()) {
					if (tempFile.length() == downloadIcatchFile.getFileSize()) {
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							WriteLogToDevice.writeLog("[Normal] -- PbDownloadManager:", "InterruptedException");
							e.printStackTrace();
						}
						if (downloadIcatchFile.getFileType() == ICatchFileType.ICH_TYPE_IMAGE) {
							uriArrayList.add(Uri.fromFile(tempFile));
							downloadTaskList.removeFirst();
						}
						continue;
					}

				}
				String fileName = null;
				Boolean temp = false;
				if (downloadIcatchFile.getFileType() == ICatchFileType.ICH_TYPE_VIDEO) {
					mediaType = MEDIA_TYPE_VIDEO;
					//mediaType = "video/mov";
					fileName = downloadIcatchFile.getFileName();
					WriteLogToDevice.writeLog("[Normal] -- PbDownloadManager:", "begin downloadFile: =" + path+ fileName);
					//Log.d("tigertiger", "TYPE_VIDEO start downloadFile=" + path + fileName);
					temp = fileOperation.downloadFile(downloadIcatchFile, path + fileName);
//					tempFile = new File(path + fileName);
//					uriArrayList.add(Uri.fromFile(tempFile));
					//Log.d("tigertiger", "TYPE_VIDEO end downloadFile temp =" + temp);
					WriteLogToDevice.writeLog("[Normal] -- PbDownloadManager:", "end downloadFile retvalue =" + temp);
				} else if (downloadIcatchFile.getFileType() == ICatchFileType.ICH_TYPE_IMAGE) {
					mediaType = MEDIA_TYPE_IMAGE;
					fileName = downloadIcatchFile.getFileName();
					WriteLogToDevice.writeLog("[Normal] -- PbDownloadManager:", "start downloadFile=" + path+ fileName);
					//Log.d("tigertiger", "start downloadFile=" + path + fileName);
					temp = fileOperation.downloadFile(downloadIcatchFile, path + fileName);
					//Log.d("tigertiger", "end downloadFile temp =" + temp);
					WriteLogToDevice.writeLog("[Normal] -- PbDownloadManager:", "end downloadFile retvalue =" + temp);
				}
				if (temp == false) {
					downloadTaskList.removeFirst();
					Log.e("tigertiger", "temp == false");
					WriteLogToDevice.writeLog("[Normal] -- PbDownloadManager:", "return false:download file....");
					
				} else {
					tempFile = new File(path + fileName);
					if (mediaType == MEDIA_TYPE_IMAGE) {
						uriArrayList.add(Uri.fromFile(tempFile));
						downloadTaskList.removeFirst();
					}
					if(mediaType == MEDIA_TYPE_VIDEO){
						MediaRefresh.addMediaToDB(PbMainActivity.this,path+downloadIcatchFile.getFileName(),"video/mov");
					}else{
						MediaRefresh.scanFileAsync(PbMainActivity.this,path+downloadIcatchFile.getFileName());
					}
				}
			}
			GlobalApp.getInstance().setDownloadStatus(false);
			sendOkMsg(MESSAGE_SHARE_DOWNLOAD_COMPLETED, 0, 0, null);
			WriteLogToDevice.writeLog("[Normal] -- PbDownloadManager:", "complete downloading");
		}
	}
}
