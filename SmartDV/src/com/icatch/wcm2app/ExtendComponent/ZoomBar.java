/**
 * Added by zhangyanhu C01012,2014-9-11
 */
package com.icatch.wcm2app.ExtendComponent;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.SeekBar;

/**
 * Added by zhangyanhu C01012,2014-9-11
 */
public class ZoomBar extends SeekBar{
	private static int minValue = 0;
	/**
	 * Added by zhangyanhu C01012,2014-9-11
	 */
	public ZoomBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	public void setMinValue(int minValue){
		this.minValue = minValue;
	}
	public synchronized int getZoomProgress() {
		// TODO Auto-generated method stub
		return super.getProgress()+minValue;
	}
	@Override
	public synchronized void setMax(int max) {
		// TODO Auto-generated method stub
		Log.d("tigertiger","---max = "+max);
		Log.d("tigertiger","---max-minValue = "+(max-minValue));
		super.setMax(max-minValue);
	}
	@Override
	public synchronized void setProgress(int progress) {
		// TODO Auto-generated method stub
		Log.d("tigertiger","---progress = "+progress);
		Log.d("tigertiger","---progress-minValue = "+(progress-minValue));
		super.setProgress(progress-minValue);
	}
}
