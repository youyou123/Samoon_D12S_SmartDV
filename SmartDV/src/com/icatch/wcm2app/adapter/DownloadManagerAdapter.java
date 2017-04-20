/**
 * Added by zhangyanhu C01012,2014-5-28
 */
package com.icatch.wcm2app.adapter;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.icatch.wcm2app.common.PbDownloadManager;
import com.icatch.wcm2app.common.WriteLogToDevice;
import com.icatch.wcm2app.common.PbDownloadManager.DownloadInfo;
import com.icatch.wcm2app.R;
import com.icatch.wificam.customer.type.ICatchFile;


/**
 * Added by zhangyanhu C01012,2014-5-28
 */
public class DownloadManagerAdapter extends BaseAdapter{
	private Context context;
	private HashMap<ICatchFile,PbDownloadManager.DownloadInfo> chooseListMap;
	private List<ICatchFile> actList;
	private Handler handler;
	private boolean isDownloadComplete = false;
	public DownloadManagerAdapter(Context context,HashMap<ICatchFile,PbDownloadManager.DownloadInfo> downloadDataList,List<ICatchFile> actList,Handler handler){
		this.context = context;
		this.chooseListMap = downloadDataList;
		this.actList = actList;
		this.handler = handler;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return chooseListMap.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.download, null);
		}
		if(position >= actList.size()){
			return convertView;
		}
		isDownloadComplete = false;
		final ImageButton button = (ImageButton)convertView.findViewById(R.id.doAction);
		TextView fileName  = (TextView)convertView.findViewById(R.id.fileName);
		TextView downloadStatus  = (TextView)convertView.findViewById(R.id.downloadStatus);
		
		fileName.setText(actList.get(position).getFileName());
		ProgressBar processBar  = (ProgressBar)convertView.findViewById(R.id.progressBar);
		final ICatchFile downloadFile = actList.get(position);
		DownloadInfo downloadInfo = chooseListMap.get(downloadFile);
		processBar.setProgress(downloadInfo.progress);
		DecimalFormat df = new DecimalFormat("#.#");
		String curFileLength = df.format(downloadInfo.curFileLength/1024.0/1024)+"M";
		String fileSize = df.format(downloadInfo.fileSize/1024.0/1024)+"M";
		
		if(downloadInfo.progress >= 100){

			downloadStatus.setText(curFileLength+"/"+fileSize);
			button.setBackgroundResource(R.drawable.ok);
			isDownloadComplete = true;
		}else if(downloadInfo.progress <= 0){
			downloadStatus.setText(curFileLength+"/"+fileSize);
			button.setBackgroundResource(R.drawable.cancel_task);
			isDownloadComplete = false;
		}else {
			downloadStatus.setText(curFileLength+"/"+fileSize);
			button.setBackgroundResource(R.drawable.cancel_task);
			isDownloadComplete = false;
		}
		//Log.d("tigertiger", "downloadInfo.file.getFileName() = " + downloadInfo.file.getFileName());
		//Log.d("tigertiger", "downloadInfo.progress = " + downloadInfo.progress);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(isDownloadComplete == false){
					WriteLogToDevice.writeLog("tigertiger","for test test....... ");
					handler.obtainMessage(PbDownloadManager.CANCEL_DOWNLOAD_SINGLE, 0,0,downloadFile).sendToTarget();
				}
			}
		});
		return convertView;
	}
}