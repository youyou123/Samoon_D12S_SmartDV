/**
 * Added by zhangyanhu C01012,2014-8-20
 */
package com.icatch.wcm2app.function;

import android.os.Handler;

import com.icatch.wcm2app.common.GlobalApp;
import com.icatch.wcm2app.controller.sdkApi.CameraAction;

/**
 * Added by zhangyanhu C01012,2014-8-20
 */
public class FormatSDCard extends Thread{
	private CameraAction cameraAction;
	private Handler handler;
	FormatSDCard(Handler handler){
		this.handler = handler;
		cameraAction = new CameraAction();
	}
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		handler.obtainMessage(GlobalApp.MESSAGE_FORMAT_SD_START).sendToTarget();
		try {
			sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(cameraAction.formatStorage()){
			handler.obtainMessage(GlobalApp.MESSAGE_FORMAT_SUCCESS).sendToTarget();
		}else{
			handler.obtainMessage(GlobalApp.MESSAGE_FORMAT_FAILED).sendToTarget();
		}
	}

}
