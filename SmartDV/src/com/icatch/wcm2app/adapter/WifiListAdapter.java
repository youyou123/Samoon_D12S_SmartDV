/**
 * Added by zhangyanhu C01012,2014-4-4
 */
package com.icatch.wcm2app.adapter;

import java.util.List;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.icatch.wcm2app.R;

/**
 * Added by zhangyanhu C01012,2014-4-4
 */
public class WifiListAdapter extends BaseAdapter {  
	  
    LayoutInflater inflater;  
    List<ScanResult> list;  
    private Context context;
    public WifiListAdapter(Context context, List<ScanResult> list) {  
        // TODO Auto-generated constructor stub  
        this.inflater = LayoutInflater.from(context);  
        this.list = list; 
        this.context = context;
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
        View view = null;  
        view = inflater.inflate(R.layout.wifi_status, null);  
        ScanResult scanResult = list.get(position);  
        TextView wifiName = (TextView) view.findViewById(R.id.wifi_name);  
        wifiName.setText(scanResult.SSID);  
        //TextView signalStrenth = (TextView) view.findViewById(R.id.signal_strenth);  
        //signalStrenth.setText(String.valueOf(Math.abs(scanResult.level)));  
        ImageView imageView = (ImageView) view.findViewById(R.id.wifi_signal);  
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		int level = wifiManager.calculateSignalLevel(scanResult.level,5);
        //判断信号强度，显示对应的指示图标  
        if (level == 4) {  
            imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.wifi_4));  
        } else if (level == 3) {  
            imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.wifi_3));  
        } else if (level <= 2) {  
            imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.wifi_2));  
        }
//        } else if (level == 1) {  
//            imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.wifi_1));  
//        } 
        return view;  
    }

	/**
	 * Added by zhangyanhu C01012,2014-4-4
	 */
	private WifiManager getSystemService(String wifiService) {
		// TODO Auto-generated method stub
		return null;
	}  
}
