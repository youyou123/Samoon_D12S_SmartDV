/**
 * Added by zhangyanhu C01012,2014-6-20
 */
package com.icatch.wcm2app.common;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.icatch.wcm2app.ExtendComponent.DownloadDialog;
import com.icatch.wcm2app.adapter.DownloadManagerAdapter;
import com.icatch.wcm2app.controller.sdkApi.FileOperation;
import com.icatch.wcm2app.R;
import com.icatch.wificam.customer.type.ICatchFile;
import com.icatch.wificam.customer.type.ICatchFileType;

/**
 * Added by zhangyanhu C01012,2014-6-20
 */
public class PbDownloadManager {
	private ExecutorService executor;
	private static final int UPDATE_LOADING_PROGRESS = 0x1;
	public static final int CANCEL_DOWNLOAD_SINGLE = 0x2;
	public static final int CANCEL_DOWNLOAD_ALL = 0x3;
	public static final int UPDATE_TOTAL_PROGRESS = 0x4;
	public static final int ALL_TASK_COMPLETED = 0x5;
	private static final int MEDIA_TYPE_VIDEO = 0X1;
	private static final int MEDIA_TYPE_IMAGE = 0X2;
	private boolean downloadCompleted = false;
	private int hasDownload = 0;
	private LinkedList<ICatchFile> downloadTaskList;
	private LinkedList<ICatchFile> downloadChooseList;
	private DownloadManagerAdapter downloadManagerAdapter;
	private LinkedList<DownloadInfo> downloadProgressList;
	private AlertDialog.Builder builder;
	private DownloadDialog downloadManagerDialog;
	private Context context;
	private HashMap<ICatchFile, DownloadInfo> downloadInfoMap = new HashMap<ICatchFile, DownloadInfo>();
	private Future<Object> downloadFuture;
	private ICatchFile currentDownloadFile;
	private Timer downloadProgressTimer;
	private int downloadFailed = 0;

	public PbDownloadManager(Context context, LinkedList<ICatchFile> downloadList) {
		this.context = context;
		this.downloadTaskList = downloadList;
		downloadProgressList = new LinkedList<DownloadInfo>();
		downloadChooseList = new LinkedList<ICatchFile>();
		downloadChooseList.addAll(downloadList);
		for (int ii = 0; ii < downloadTaskList.size(); ii++) {
			DownloadInfo downloadInfo = new DownloadInfo(downloadTaskList.get(ii), downloadTaskList.get(ii).getFileSize(), 0, 0, false);
			downloadInfoMap.put(downloadTaskList.get(ii), downloadInfo);
		}
	}

	public void show() {
		showDownloadManagerDialog();
		executor = Executors.newSingleThreadExecutor();
		downloadFuture = executor.submit(new DownloadThread(), null);
		downloadProgressTimer = new Timer();
		downloadProgressTimer.schedule(new DownloadProgressTask(), 0, 100);
	}

	Handler downloadManagerHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			// super.handleMessage(msg);
			switch (msg.what) {
			case UPDATE_LOADING_PROGRESS:
				ICatchFile icatchFile = ((DownloadInfo) msg.obj).file;
				downloadInfoMap.put(icatchFile, (DownloadInfo) msg.obj);
				
				
				downloadManagerAdapter.notifyDataSetChanged();
				break;
			case UPDATE_TOTAL_PROGRESS:
				//Log.e("tigertiger", "receive _UPDATE_LOADING_PROCESS hasDownload =" + hasDownload);
				String message1 = context.getResources().getString(R.string.download_progress).replace("$1$", String.valueOf(hasDownload))
						.replace("$2$", String.valueOf(downloadTaskList.size())).replace("$3$", String.valueOf(downloadFailed));
				downloadManagerDialog.setMessage(message1);
				break;
			case CANCEL_DOWNLOAD_ALL:
				WriteLogToDevice.writeLog("[Normal] -- PbDownloadManager:", "receive CANCEL_DOWNLOAD_ALL");
				alertForQuitDownload();
				//downloadTaskList.clear();
				break;
			case CANCEL_DOWNLOAD_SINGLE:
				ICatchFile temp = (ICatchFile) msg.obj;
				WriteLogToDevice.writeLog("[Normal] -- PbDownloadManager:", "receive CANCEL_DOWNLOAD_SINGLE");
				if (currentDownloadFile == temp) {
					if (fileOperation.cancelDownload() == false) {
						Toast.makeText(context, R.string.dialog_cancel_downloading_failed, Toast.LENGTH_SHORT).show();
						break;
					}
				}
				Toast.makeText(context, R.string.dialog_cancel_downloading_succeeded, Toast.LENGTH_SHORT).show();
				//Log.e("tigertiger", "CANCEL_DOWNLOAD_SINGLE: ICatchFile) msg.obj = " + temp.getFileName());
				//downloadTaskList.remove(temp);
				downloadInfoMap.remove(temp);
				downloadChooseList.remove(temp);
				downloadTaskList.remove(temp);
				//downloadTaskList.removeFirst();
				//downloadProgressList.remove(downloadIcatchFile);

				downloadManagerAdapter.notifyDataSetChanged();
				break;
			case ALL_TASK_COMPLETED:
				if(downloadManagerDialog != null){
					downloadManagerDialog.dismiss();
				}
				if(downloadProgressTimer != null){
					downloadProgressTimer.cancel();
				}
				downloadCompleted();
				break;
			}
		}
	};
	public long downloadProgress;
	public FileOperation fileOperation = new FileOperation();

	public void showDownloadManagerDialog() {
		downloadManagerAdapter = new DownloadManagerAdapter(context, downloadInfoMap, downloadChooseList, downloadManagerHandler);
		downloadManagerDialog = new DownloadDialog(context);
		downloadManagerDialog.setCancelable(false);
		//downloadManagerDialog.setTitle(context.getResources().getString(R.string.download_manager));
		downloadManagerDialog.show();
		downloadManagerDialog.setTitle(context.getResources().getString(R.string.download_manager));
		String message = context.getResources().getString(R.string.download_progress).replace("$1$", String.valueOf(hasDownload))
				.replace("$2$", String.valueOf(downloadTaskList.size())).replace("$3$", String.valueOf(downloadFailed));
		downloadManagerDialog.setMessage(message);
		downloadManagerDialog.setAdapter(downloadManagerAdapter);
		downloadManagerDialog.getDrawBackButton().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				downloadManagerHandler.obtainMessage(CANCEL_DOWNLOAD_ALL).sendToTarget();
			}

		});
		downloadManagerDialog.setOnKeyListener(new android.content.DialogInterface.OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub
				if (arg1 == KeyEvent.KEYCODE_BACK) {
					WriteLogToDevice.writeLog("[Normal] -- PbDownloadManager:", "receive KEYCODE_BACK to quit download manager");
					alertForQuitDownload();
					//downloadTaskList.clear();
				}
				return false;
			}
		});
	}

	class DownloadThread implements Runnable {

		@Override
		public void run() {
			downloadCompleted = false;
			hasDownload = 0;
			downloadFailed = 0;
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
				currentDownloadFile = downloadIcatchFile;
				DownloadInfo downloadInfo = new DownloadInfo(downloadIcatchFile, downloadIcatchFile.getFileSize(), 0, 0, false);
				downloadProgressList.addLast(downloadInfo);
				Log.e("tigertiger", "addLast downloadIcatchFile.getFileName()=" + downloadIcatchFile.getFileName());
				File tempFile = new File(path + downloadIcatchFile.getFileName());
				if (tempFile.exists()) {
					if (tempFile.length() == downloadIcatchFile.getFileSize()) {
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						downloadTaskList.removeFirst();
						hasDownload++;
						downloadManagerHandler.obtainMessage(UPDATE_TOTAL_PROGRESS).sendToTarget();
						Log.e("tigertiger", "hasDownload =" + hasDownload);
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
					downloadProgressList.remove(downloadIcatchFile);
					Log.e("tigertiger", "temp == false");
					downloadFailed++;
					downloadManagerHandler.obtainMessage(UPDATE_TOTAL_PROGRESS).sendToTarget();
				} else {
					downloadTaskList.removeFirst();
					downloadInfoMap.get(downloadIcatchFile).done = true;
					hasDownload++;
					downloadManagerHandler.obtainMessage(UPDATE_TOTAL_PROGRESS).sendToTarget();
					Log.e("tigertiger", "hasDownload =" + hasDownload);
					if(mediaType == MEDIA_TYPE_VIDEO){
						MediaRefresh.addMediaToDB(context,path+downloadIcatchFile.getFileName(),"video/mov");
					}else{
						MediaRefresh.scanFileAsync(context,path+downloadIcatchFile.getFileName());
					}
				}
			}
			GlobalApp.getInstance().setDownloadStatus(false);
			downloadCompleted = true;
			downloadManagerHandler.obtainMessage(ALL_TASK_COMPLETED).sendToTarget();
			WriteLogToDevice.writeLog("[Normal] -- PbDownloadManager:", "complete downloading");
		}
	}

	class DownloadProgressTask extends TimerTask {

		@Override
		public void run() {

			if (downloadProgressList.isEmpty()) {
				return;
			}
			String path;
			path = Environment.getExternalStorageDirectory().toString() + GlobalApp.DOWNLOAD_PATH;
			ICatchFile iCatchFile = downloadProgressList.getFirst().file;
			if (downloadInfoMap.containsKey(iCatchFile) == false) {
				downloadProgressList.removeFirst();
				return;
			}
			File file = new File(path + iCatchFile.getFileName());
			WriteLogToDevice.writeLog("[Normal] -- DownloadProgressTask:", "filename = " + file);
			//Log.d("tigertiger", "filename = " + file);
			// WriteLogToDevice.writeLog("[Normal] -- PbDownloadManager:",
			// "file.length() = " + file.length());
			// WriteLogToDevice.writeLog("[Normal] -- PbDownloadManager:",
			// "iCatchFile.getFileSize() = " + iCatchFile.getFileSize());
			
			long fileLength = file.length();
//			Log.d("tigertiger", ": fileLength = " + fileLength);
//			Log.d("tigertiger", ": file.getPath() = " + path + iCatchFile.getFileName());
			if (file != null) {
				if (fileLength == iCatchFile.getFileSize()) {
					downloadProgress = 100;
					downloadProgressList.removeFirst();
				} else {
					downloadProgress = file.length() * 100 / iCatchFile.getFileSize();
				}
				// tempProcess = file.length() * 100 / downloadingFilesize;
			} else {
				downloadProgress = 0;
			}
			if (downloadInfoMap.containsKey(iCatchFile) == false) {
				return;
			}
			//Log.d("tigertiger", ": downloadProgress = " + downloadProgress);
			DownloadInfo downloadInfo = downloadInfoMap.get(iCatchFile);
			downloadInfo.curFileLength = fileLength;
			downloadInfo.progress = (int) downloadProgress;
			WriteLogToDevice.writeLog("[Normal] -- DownloadProgressTask:", "downloadProgress = " + downloadProgress);
			downloadManagerHandler.obtainMessage(UPDATE_LOADING_PROGRESS, (int) downloadProgress, 0, downloadInfo).sendToTarget();
		}
	}

	public void alertForQuitDownload() {
		if (builder != null) {
			return;
		}
		builder = new AlertDialog.Builder(context);
		builder.setIcon(R.drawable.warning).setTitle(R.string.dialog_btn_exit).setMessage(R.string.downloading_quit);
		builder.setPositiveButton(R.string.dialog_btn_exit, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				downloadTaskList.clear();
				if (fileOperation.cancelDownload() == false) {
					Toast.makeText(context, R.string.dialog_cancel_downloading_failed, Toast.LENGTH_SHORT).show();
					return;
				} else {
					downloadManagerDialog.dismiss();
					if(downloadProgressTimer != null){
						downloadProgressTimer.cancel();
					}
					Toast.makeText(context, R.string.dialog_cancel_downloading_succeeded, Toast.LENGTH_SHORT).show();
				}
				WriteLogToDevice.writeLog("[Normal] -- PbDownloadManager:", "cancel download task and quit download manager");
			}
		});

		builder.setNegativeButton(R.string.gallery_cancel, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				builder = null;
			}
		});
		AlertDialog dialog = builder.create();
		dialog.setCancelable(false);
		//WriteLogToDevice.writeLog("[Normal] -- PbDownloadManager:", "start dialog.show()");
		dialog.show();
	}

	public void downloadCompleted() {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(context.getResources().getString(R.string.download_manager));
		String message = context.getResources().getString(R.string.download_complete_result).replace("$1$", String.valueOf(hasDownload))
				.replace("$2$", String.valueOf(downloadFailed));
		builder.setMessage(message);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		AlertDialog dialog = builder.create();
		dialog.setCancelable(false);
		//WriteLogToDevice.writeLog("[Normal] -- PbDownloadManager:", "start dialog.show()");
		dialog.show();
	}
	
	public class DownloadInfo {
		public ICatchFile file = null;
		public long fileSize = 0;
		public long curFileLength = 0;
		public int progress = 0;
		public boolean done = false;

		public DownloadInfo(ICatchFile file, long fileSize, long curFileLength, int progress, boolean done) {
			this.file = file;
			this.fileSize = fileSize;
			this.curFileLength = curFileLength;
			this.progress = progress;
			this.done = done;
		}
	}
}
