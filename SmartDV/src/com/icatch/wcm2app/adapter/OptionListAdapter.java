package com.icatch.wcm2app.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.icatch.wcm2app.function.SettingView;
import com.icatch.wcm2app.function.SettingView.SettingHandler;
import com.icatch.wcm2app.R;

/**
 * 
 * @author zhangyanhu_C01012
 * 
 */
public class OptionListAdapter extends BaseAdapter {
	private ArrayList<Integer> nameArray;
	private ArrayList<String> valueArray;
	private Context context;
	private SettingHandler settingHandler;

	public OptionListAdapter(Context context, SettingHandler settingHandler, ArrayList<Integer> menuItemNameArray, ArrayList<String> menuItemValueArray) {
		this.context = context;
		this.settingHandler = settingHandler;
		nameArray = menuItemNameArray;
		valueArray = menuItemValueArray;
		/*
		 * for (int i = 0; i < nameArray.size(); i++) { }
		 */
		Log.d("tiger7", "nameArray.length=" + nameArray.size());
	}

	@Override
	public int getCount() {
		return nameArray.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.setup_mainmenu_item, null);
			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.item_text);
			holder.text = (TextView) convertView.findViewById(R.id.item_value);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
//		Log.d("tigertiger", "-----------position =" + position);
//		Log.d("tigertiger", "-----------nameArray =" + nameArray.size());
//		Log.d("tigertiger", "-----------valueArray =" + valueArray.size());
		holder.title.setText(nameArray.get(position));
		holder.text.setText(valueArray.get(position));
		if (nameArray.get(position).equals(R.string.setting_app_version) || nameArray.get(position).equals(R.string.setting_product_name)
				|| nameArray.get(position).equals(R.string.setting_firmware_version)) {
			holder.title.setTextColor(context.getResources().getColor(R.color.gray));
		} else {
			holder.title.setTextColor(context.getResources().getColor(R.color.white));
		}
		holder.text.setTextColor(context.getResources().getColor(R.color.gray));
		final int tempPosition = position;
	/*	convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				switch (nameArray.get(tempPosition)) {
				case R.string.setting_burst:
					settingHandler.obtainMessage(SettingView.SETTING_OPTION_BURST).sendToTarget();
					break;
				case R.string.setting_awb:
					settingHandler.obtainMessage(SettingView.SETTING_OPTION_WHITE_BALANCE).sendToTarget();
					break;
				case R.string.setting_power_supply:
					settingHandler.obtainMessage(SettingView.SETTING_OPTION_ELETRICITY_FREQUENCY).sendToTarget();
					break;
				case R.string.setting_datestamp:
					settingHandler.obtainMessage(SettingView.SETTING_OPTION_DATE_STAMP).sendToTarget();
					break;
				case R.string.setting_format:
					settingHandler.obtainMessage(SettingView.SETTING_OPTION_FORAMT).sendToTarget();
					break;
				case R.string.setting_time_lapse_interval:
					settingHandler.obtainMessage(SettingView.SETTING_OPTION_TIME_LAPSE_INTERVAL).sendToTarget();
					break;
				case R.string.setting_time_lapse_duration:
					settingHandler.obtainMessage(SettingView.SETTING_OPTION_TIME_LAPSE_DURATION).sendToTarget();
					break;
				case R.string.timeLapse_mode:
					settingHandler.obtainMessage(SettingView.SETTING_OPTION_TIME_LAPSE_MODE).sendToTarget();
					break;
				case R.string.slowmotion:
					settingHandler.obtainMessage(SettingView.SETTING_OPTION_SLOW_MOTION).sendToTarget();
					break;
				case R.string.upside:
					settingHandler.obtainMessage(SettingView.SETTING_OPTION_UPSIDE).sendToTarget();
					break;
				case R.string.camera_wifi_configuration:
					settingHandler.obtainMessage(SettingView.SETTING_OPTION_CAMERA_CONFIGURATION).sendToTarget();
					break;
				}
			}
		});*/
		return convertView;
	}

	public final class ViewHolder {
		public TextView title;
		public TextView text;
	}
}
