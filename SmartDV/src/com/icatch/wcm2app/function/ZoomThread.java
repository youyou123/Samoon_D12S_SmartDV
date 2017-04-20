/**
 * Added by zhangyanhu C01012,2014-9-18
 */
package com.icatch.wcm2app.function;

import android.os.Handler;

import com.icatch.wcm2app.ExtendComponent.ZoomBar;
import com.icatch.wcm2app.common.GlobalApp;
import com.icatch.wcm2app.controller.sdkApi.CameraAction;
import com.icatch.wcm2app.controller.sdkApi.CameraProperties;

/**
 * Added by zhangyanhu C01012,2014-9-18
 */
public class ZoomThread extends Thread{
	private Handler handler;
	private int lastZoomRate;
	private ZoomBar zoomBar;
	private int zoomMinRate = 10;
	private CameraAction cameraAction = new CameraAction();
	private CameraProperties cameraProperties = new CameraProperties();
	public ZoomThread(Handler handler,int lastZoomRate,ZoomBar zoomBar){
		this.handler = handler;
		this.lastZoomRate = lastZoomRate;
		this.zoomBar = zoomBar;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		int maxZoomCount = 100;
		while (lastZoomRate > (zoomBar.getZoomProgress()) && lastZoomRate > zoomMinRate && maxZoomCount-- > 0) {
			cameraAction .zoomOut();
			lastZoomRate = cameraProperties .getCurrentZoomRatio();
//			Log.d("tigertiger", "maxZoomCount = " + maxZoomCount);
//			Log.d("tigertiger", "lastZoomRate = " + lastZoomRate);
//			Log.d("tigertiger", "seekBar.getZoomProgress = " + (zoomBar.getZoomProgress()));
		}

		while (lastZoomRate < (zoomBar.getZoomProgress()) && lastZoomRate < cameraProperties.getMaxZoomRatio() && maxZoomCount-- > 0) {
			cameraAction.zoomIn();
			lastZoomRate = cameraProperties.getCurrentZoomRatio();
//			Log.d("tigertiger", "maxZoomCount = " + maxZoomCount);
//			Log.d("tigertiger", "lastZoomRate = " + lastZoomRate);
//			Log.d("tigertiger", "seekBar.getZoomProgress = " + (zoomBar.getZoomProgress()));
		}
		handler.obtainMessage(GlobalApp.MESSAGE_ZOOM_COMPLETED,lastZoomRate,0).sendToTarget();
	}

}
