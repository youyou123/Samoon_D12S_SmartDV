package com.icatch.wcm2app.Activity;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.icatch.wcm2app.adapter.LocalPhotoAdapter;
import com.icatch.wcm2app.common.FileTools;
import com.icatch.wcm2app.common.GlobalApp;
import com.icatch.wcm2app.R;

public class LocalPhotoWallActivity extends Activity {
	private GridView mGridView;
	private LocalPhotoAdapter mGridViewAdapter;
	private int width;
	private TextView totalNum;
	private String fileList[];
	private ImageView imageView;
	private File file;
	//private GridViewAdapter photoWallAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_local_photo_wall);
		mGridView = (GridView)findViewById(R.id.gridview);
		totalNum = (TextView)findViewById(R.id.totalNumText);
		init();
		
		
		
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				imageView = (ImageView) arg1.findViewById(R.id.imageView);
				Log.d("1111","...............arg1 ="+(String)imageView.getTag());
				Intent intent = new Intent(Intent.ACTION_VIEW);
				//Uri mUri = Uri.parse("file://" + picFile.getPath());Android3.0以后最好不要通过该方法，存在一些小Bug
				file = new File((String)imageView.getTag());
				//File file = new File(Environment.getExternalStorageDirectory().toString()+GlobalApp.DOWNLOAD_PATH);
				intent.setDataAndType(Uri.fromFile(file), "image/*");
				startActivity(intent);
			}
		});
	}
	
	private void init(){
		DisplayMetrics dm = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		width = dm.widthPixels;
		fileList = FileTools.getUrls(GlobalApp.DOWNLOAD_PATH);
		if(fileList != null && fileList.length > 0){
			mGridViewAdapter = new LocalPhotoAdapter(this,fileList, mGridView,width);
			mGridView.setAdapter(mGridViewAdapter);
			totalNum.setText(fileList.length+" files");	
		}else{
			totalNum.setText("0 files");
		}
		//mGridViewAdapter.notifyDataSetInvalidated();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		//init();
		Log.d("1111","on restart");
		DisplayMetrics dm = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		width = dm.widthPixels;
		fileList = FileTools.getUrls(GlobalApp.DOWNLOAD_PATH);
		mGridViewAdapter = null;
		Log.d("1111","new LocalPhotoAdapter");
			mGridViewAdapter = new LocalPhotoAdapter(LocalPhotoWallActivity.this,fileList, mGridView,width);
			//mGridViewAdapter.notifyDataSetInvalidated();
			mGridView.setAdapter(mGridViewAdapter);
			mGridViewAdapter.notifyDataSetInvalidated();
			totalNum.setText(fileList.length+" files");	
			mGridView.invalidate();
		
	}

	//取消所有的下载任务
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(mGridViewAdapter !=null){
			mGridViewAdapter.cancelAllTasks();
		}
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// Add by zhangyanhu 2013.12.2
		super.onConfigurationChanged(newConfig);
//		DisplayMetrics dm = new DisplayMetrics();
//		this.getWindowManager().getDefaultDisplay().getMetrics(dm);
//		width = dm.widthPixels;
		//mGridViewAdapter = new LocalPhotoAdapter(this, 0, FileTools.getUrls(GlobalApp.DOWNLOAD_PATH), mGridView, width);
		//mGridView.setAdapter(mGridViewAdapter);
//		try {
//			Thread.currentThread().sleep(5000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		DisplayMetrics dm = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		width = dm.widthPixels;
		fileList = FileTools.getUrls(GlobalApp.DOWNLOAD_PATH);
		mGridViewAdapter = null;
		Log.d("1111","new LocalPhotoAdapter");
			mGridViewAdapter = new LocalPhotoAdapter(LocalPhotoWallActivity.this,fileList, mGridView,width);
			//mGridViewAdapter.notifyDataSetInvalidated();
			mGridView.setAdapter(mGridViewAdapter);
			mGridViewAdapter.notifyDataSetInvalidated();
			totalNum.setText(fileList.length+" files");	
			mGridView.invalidate();
	
	}
}
