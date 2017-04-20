package com.icatch.wcm2app.adapter;

import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.icatch.wcm2app.Activity.PbMainActivity;
import com.icatch.wcm2app.common.BitmapsLoad;
import com.icatch.wcm2app.common.BitmapsLoad.ThreadInfo;
import com.icatch.wcm2app.R;
import com.icatch.wificam.customer.type.ICatchFile;
import com.icatch.wificam.customer.type.ICatchFileType;

public class MpbPhotoAdapter extends BaseAdapter implements OnScrollListener {

	private List<ICatchFile> list;
	private Context context;

	private int width;
	private LruCache<Integer, ThreadInfo> cache;
	BitmapsLoad bitmapsLoad;
	private Handler photoLoadHandler;
	private List<ICatchFile> actList;
	//private GridView photoWall;
	private int firstPosition = 0;
	//private int lastPosition = 0;
	private int visibleCount = 0;
	private boolean firstIn = true;

	public MpbPhotoAdapter(Context context, Handler photoLoadHandler, List<ICatchFile> list, int width, LruCache<Integer, ThreadInfo> cache,
			List<ICatchFile> actList, GridView photoWall) {

		this.context = context;
		this.list = list;
		this.width = width;
		this.photoLoadHandler = photoLoadHandler;
		this.actList = actList;
		this.cache = cache;
		//this.photoWall = photoWall;
		bitmapsLoad = BitmapsLoad.getInstance();
		bitmapsLoad.initData();
		photoWall.setOnScrollListener(this);
	}

	@Override
	public int getCount() {
		return list == null ? 0 : list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// return arg0;
		return list.get(arg0).getFileHandle();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = new ViewHolder();
		final int fileHandle = list.get(position).getFileHandle();
		ImageView imageView;
		Log.d("tiger1", "fileHandle == " + fileHandle);
		if (convertView == null) {
			Log.d("tiger1", "convertView == null");
			convertView = LayoutInflater.from(context).inflate(R.layout.photo_layout, null);

			// Add by zhangyanhu 2013.12.2
			// set the size for photo and video
			imageView = (ImageView) convertView.findViewById(R.id.photo_pb);

			ViewGroup.LayoutParams photoLayoutParams = imageView.getLayoutParams();
			photoLayoutParams.width = width / 9 * 3;
			photoLayoutParams.height = width / 9 * 2;
			imageView.setLayoutParams(photoLayoutParams);

			// set the selected_fram size for photo and video
			ImageView imageViewBg = (ImageView) convertView.findViewById(R.id.photo_bg);
			ViewGroup.LayoutParams photoLayoutParamsBg = imageViewBg.getLayoutParams();
			photoLayoutParamsBg.width = width / 9 * 2 + 5;
			photoLayoutParamsBg.height = width / 9 * 2 + 5;
			imageViewBg.setLayoutParams(photoLayoutParamsBg);

			// set the video icon location
			ImageView videoIcon = (ImageView) convertView.findViewById(R.id.video_icon);
			RelativeLayout.LayoutParams videoIconLayoutParams = (RelativeLayout.LayoutParams) videoIcon.getLayoutParams();
			// int videoLeftMargin = 0;
			int videoLeftMargin = (width / 4 - width / 9 * 2) / 2;
			videoIconLayoutParams.leftMargin = videoLeftMargin;
			videoIcon.setLayoutParams(videoIconLayoutParams);

			// set selected_item location
			ImageView editIcon = (ImageView) convertView.findViewById(R.id.edit_icon);
			RelativeLayout.LayoutParams editIconLayoutParams = (RelativeLayout.LayoutParams) editIcon.getLayoutParams();
			int editRightMargin = 0;
			editRightMargin = (width / 4 - width / 9 * 2) / 2;
			editIconLayoutParams.rightMargin = editRightMargin;
			editIcon.setLayoutParams(editIconLayoutParams);
			// End add by zhangyanhu 2013.12.2

		} else {
			convertView.findViewById(R.id.edit_icon).setVisibility(View.GONE);
			convertView.findViewById(R.id.photo_bg).setVisibility(View.GONE);
			convertView.findViewById(R.id.video_icon).setVisibility(View.GONE);

			imageView = (ImageView) convertView.findViewById(R.id.photo_pb);
		}

		imageView.setImageResource(R.drawable.empty_photo);
		holder.bg = imageView;
		holder.bg.setTag(fileHandle);
		convertView.setTag(holder);

		if (list.get(position).getFileType() == ICatchFileType.ICH_TYPE_VIDEO) {
			convertView.findViewById(R.id.video_icon).setVisibility(View.VISIBLE);
		}
		if (actList.isEmpty() == false) {
			for (int ii = 0; ii < actList.size(); ii++) {
				if (fileHandle == actList.get(ii).getFileHandle()) {
					convertView.findViewById(R.id.edit_icon).setBackgroundResource(R.drawable.image_confirm);
					convertView.findViewById(R.id.edit_icon).setVisibility(View.VISIBLE);
					convertView.findViewById(R.id.photo_bg).setVisibility(View.VISIBLE);
					break;
				} else if (PbMainActivity.getOperationStatus() == PbMainActivity.MODE_OPERATION) {
					convertView.findViewById(R.id.edit_icon).setBackgroundResource(R.drawable.image_unconfirm);
					convertView.findViewById(R.id.edit_icon).setVisibility(View.VISIBLE);
				}
			}
		} else if (PbMainActivity.getOperationStatus() == PbMainActivity.MODE_OPERATION) {
			convertView.findViewById(R.id.edit_icon).setBackgroundResource(R.drawable.image_unconfirm);
			convertView.findViewById(R.id.edit_icon).setVisibility(View.VISIBLE);
		}
		Log.d("tigertiger","------fileHandle ="+fileHandle);
		ThreadInfo threadInfo = cache.get(fileHandle);
		if (threadInfo != null) {
			if (threadInfo.mBitmap != null) {
				imageView.setImageBitmap(threadInfo.mBitmap);
			}
		}
		return convertView;
	}

	@Override
	public void notifyDataSetInvalidated() {
		// TODO Auto-generated method stub
		super.notifyDataSetInvalidated();
	}

	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		firstPosition = firstVisibleItem;
		visibleCount = visibleItemCount;
		if(firstIn){
			if(visibleCount <= 0 ){
				return;
			}
			Log.e("tigertiger","onScroll   cancelAllTasks");
			//bitmapsLoad.cancelAllTasks();
			for(int ii = firstPosition; ii < (visibleCount + firstPosition) && ii < list.size();ii++){
				if(cache.get(list.get(ii).getFileHandle()) != null){
					continue;
				}
				bitmapsLoad.startLoadOneBitmap(list.get(ii).getFileHandle(), photoLoadHandler, ii);
			}
			firstIn = false;
		}
	}

	public void onScrollStateChanged(AbsListView view, int scrollState) {
		switch (scrollState) {
		case OnScrollListener.SCROLL_STATE_IDLE: // Idle态，进行实际数据的加载显示
			if(visibleCount <= 0 ){
				break;
			}
			Log.e("tigertiger","onScrollStateChanged   cancelAllTasks");
			bitmapsLoad.cancelAllTasks();
			//ThreadInfo threadInfo;
			for(int ii = firstPosition; ii < (visibleCount + firstPosition) && ii < list.size();ii++){
				if(cache.get(list.get(ii).getFileHandle()) != null){
					continue;
				}
				bitmapsLoad.startLoadOneBitmap(list.get(ii).getFileHandle(), photoLoadHandler, ii);
			}
			break;
		case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL://滑动且触摸
			break;
		case OnScrollListener.SCROLL_STATE_FLING://滑动非触摸
			break;
		default:
			break;
		}
	}

	public class ViewHolder {
		public ImageView bg;
	}
	
	public void initFirstIn(boolean value){
		firstIn = true;
	}
/*	public interface OnStateChangedListener {
		public void OnChanged(boolean isDone);
	}*/
}
