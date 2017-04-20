/**
 * Added by zhangyanhu C01012,2014-6-16
 */
package com.icatch.wcm2app.adapter;

import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.icatch.wcm2app.common.GlobalApp;
import com.icatch.wcm2app.R;

/**
 * Added by zhangyanhu C01012,2014-6-16
 */
public class CameraAwakeAdapter extends BaseAdapter {  
	  
	private LayoutInflater inflater;  
    private List<String> list;  
    private Handler appStartHandler;
    public CameraAwakeAdapter(Context context, List<String> list,Handler appStartHandler) {  
        this.inflater = LayoutInflater.from(context);  
        this.list = list; 
        this.appStartHandler = appStartHandler;
    }  

    @Override  
    public int getCount() {  
        // TODO Auto-generated method stub  
        return list.size();  
    }  

    @Override  
    public Object getItem(int position) {  
        // TODO Auto-generated method stub  
        return position;  
    }  

    @Override  
    public long getItemId(int position) {  
        // TODO Auto-generated method stub  
        return position;  
    }  

    @Override  
    public View getView(int position, View convertView, ViewGroup parent) {  
        // TODO Auto-generated method stub 
    		convertView = inflater.inflate(R.layout.camera_awake, null);  
	        final String ssid = list.get(position);  
	        TextView cameraName = (TextView) convertView.findViewById(R.id.camera_ssid);  
	        cameraName.setText(ssid);  
	        Button cameraAwake = (Button) convertView.findViewById(R.id.camera_awake); 
	        Button cameraDelete = (Button) convertView.findViewById(R.id.camera_delete);
	        cameraAwake.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Log.d("tigertiger","send  AWAKE_ONE_CAMERA");
					appStartHandler.obtainMessage(GlobalApp.MESSAGE_AWAKE_ONE_CAMERA,0,0,ssid).sendToTarget();
				}
			});
	        cameraDelete.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					appStartHandler.obtainMessage(GlobalApp.MESSAGE_REMOVE_CAMERA,0,0,ssid).sendToTarget();
				}
			});
        return convertView;  
    }  
}
