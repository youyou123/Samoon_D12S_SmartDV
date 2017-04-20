package com.icatch.wcm2app.common;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;
import android.widget.GridView;

import com.icatch.wcm2app.Activity.PbMainActivity;
import com.icatch.wcm2app.controller.sdkApi.FileOperation;
import com.icatch.wificam.customer.type.ICatchFile;
import com.icatch.wificam.customer.type.ICatchFileType;
import com.icatch.wificam.customer.type.ICatchFrameBuffer;

/**
 * 
 * @author zhangyanhu_C01012 2013.12.26
 * 
 */
public class BitmapsLoad {
	private Bitmap bitmap;
	private boolean isLoadComplete;
	private static BitmapsLoad instance = new BitmapsLoad();
	private FileOperation fileOperation = new FileOperation();
	private ExecutorService executor;
	private Future<Object> future;
	public LinkedList<ThreadInfo> taskList;
	private HashMap<Integer, Boolean> taskMap;
	public Handler bitMapHandler;
	private boolean isAllowLoad = true;
	private static GridView gridView;

	public static BitmapsLoad getInstance() {
		return instance;
	}

	public static void setGridView(GridView View) {
		gridView = View;
	}

	public void startLoadOneBitmap(int fileHandle, Handler handler, int position) {
		Log.d("tiger1", "StartLoadOneBitmap");
		if (isAllowLoad == false) {
			return;
		}
		ThreadInfo threadInfo = new ThreadInfo();
		threadInfo.mFileHandle = fileHandle;
		threadInfo.mHandler = handler;
		threadInfo.position = position;
		if (taskMap.containsKey(fileHandle) != true) {
			taskMap.put(fileHandle, true);
			taskList.add(threadInfo);
			if (future == null || future.isDone() || future.isCancelled()) {
				future = executor.submit(new LoadOneBitMapThread(), null);
			}
		}else{
		}
	}

	public void cancelAllTasks() {
		taskList.clear();
		taskMap.clear();
		/*
		 * if (future != null && !future.isDone()) { future.cancel(true); }
		 */
		fileOperation.cancelDownload();
	}

	public class LoadOneBitMapThread implements Runnable {

		private int fileHandle = 0;
		private Handler handler = null;
		ThreadInfo mThreadInfo;

		@Override
		public void run() {
			WriteLogToDevice.writeLog("[Normal] -- BitmapsLoad:", "start LoadOneBitMapThread");
			Thread.currentThread().setPriority(6);
			while (taskList.isEmpty() == false) {
				while (isLoadComplete == false) {
					// wait for last loading complete
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (taskList.isEmpty() == true) {
					return;
				}
				mThreadInfo = taskList.removeFirst();
				fileHandle = mThreadInfo.mFileHandle;
				taskMap.remove(fileHandle);
				handler = mThreadInfo.mHandler;
				WriteLogToDevice.writeLog("[Normal] -- BitmapsLoad:", "start load...fileHandle =" + fileHandle);
				bitmap = null;
				ICatchFile file = new ICatchFile(fileHandle);
				isLoadComplete = false;
				ICatchFrameBuffer buffer = fileOperation.getThumbnail(file);
				isLoadComplete = true;
				if (buffer == null) {
					// Log.d("tiger1", "buffer == null");
					WriteLogToDevice.writeLog("[Error] -- BitmapsLoad:", "buffer == null  send _LOAD_BITMAP_FAILED");
					handler.obtainMessage(PbMainActivity.MESSAGE_LOAD_BITMAP_FAILED, mThreadInfo).sendToTarget();
					continue;
				}

				int datalength = buffer.getFrameSize();
				if (datalength > 0) {
					bitmap = BitmapFactory.decodeByteArray(buffer.getBuffer(), 0, datalength);
					if (bitmap == null) {
						// Log.d("tiger1", "bitmap == null");
						// isLoadComplete = true;
						WriteLogToDevice.writeLog("[Error] -- BitmapsLoad:", "bitmap == null send _LOAD_BITMAP_FAILED");
						handler.obtainMessage(PbMainActivity.MESSAGE_LOAD_BITMAP_FAILED, mThreadInfo).sendToTarget();
						continue;
					}
				} else {
					WriteLogToDevice.writeLog("[Error] -- BitmapsLoad:", "datalength <= 0  send _LOAD_BITMAP_FAILED");
					handler.obtainMessage(PbMainActivity.MESSAGE_LOAD_BITMAP_FAILED, mThreadInfo).sendToTarget();
					continue;
				}
				isLoadComplete = true;
					mThreadInfo.mBitmap = bitmap;
					//Log.d("tigertiger", "file.getFileType() =" + file.getFileType());
					WriteLogToDevice.writeLog("[Normal] -- BitmapsLoad:", "send _LOAD_BITMAP_SUCCESS");
					handler.obtainMessage(PbMainActivity.MESSAGE_LOAD_BITMAP_SUCCESS, mThreadInfo).sendToTarget();
					//Log.d("tiger1", "_LOAD_BITMAP_SUCCESS");
			}
			handler.obtainMessage(PbMainActivity.MESSAGE_ALL_TASKS_DONE, fileHandle).sendToTarget();
		}
	}

	public class ThreadInfo {
		public int mFileHandle;
		public Handler mHandler;
		public Bitmap mBitmap;
		public int position;
		public ICatchFileType fileType;

		public ThreadInfo() {
			mFileHandle = 0;
			mHandler = null;
			mBitmap = null;
			position = 0;
			fileType = ICatchFileType.ICH_TYPE_UNKNOWN;
		}
	}

	public void killLoadThread() {
		WriteLogToDevice.writeLog("[Normal] -- BitmapsLoad:", "killLoadThread");
		executor.shutdown(); // Disable new tasks from being submitted
		try {
			if (!executor.awaitTermination(1, TimeUnit.SECONDS)) {
				Log.e("PbMainActivity", "shutdownNow");
				executor.shutdownNow(); // Cancel currently executing tasks
			}
		} catch (InterruptedException e) {
			// (Re-)cancel if current thread also interrupted
			executor.shutdownNow();
			// Preserve interrupt status
			Thread.currentThread().interrupt();
		}
		isAllowLoad = false;
	}

	public void initData() {
		isAllowLoad = true;
		executor = Executors.newFixedThreadPool(1);
		fileOperation = new FileOperation();
		isLoadComplete = true;
		taskList = new LinkedList<ThreadInfo>();
		taskMap = new HashMap<Integer, Boolean>();
		future = null;
	}
}