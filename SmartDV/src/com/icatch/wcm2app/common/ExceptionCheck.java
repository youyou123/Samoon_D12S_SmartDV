/**
 * Added by zhangyanhu C01012,2014-9-22
 */
package com.icatch.wcm2app.common;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.icatch.wcm2app.controller.SDKEvent;
import com.icatch.wcm2app.R;
import com.icatch.wificam.customer.type.ICatchEventID;

/**
 * Added by zhangyanhu C01012,2014-9-22
 */
public class ExceptionCheck {
	private  Handler exceptionHandler;

	public void startExceptionCheck() {
		
		exceptionHandler = new Handler() {

			public void handleMessage(Message msg) {
				Log.e("tigertiger", "handleMessage");
				switch (msg.what) {
				case GlobalApp.EVENT_SERVER_STREAM_ERROR:
					AlertDialog.Builder builder = new AlertDialog.Builder(GlobalApp.getInstance().getCurrentApp());
					builder.setIcon(R.drawable.warning).setTitle("Warning").setMessage(R.string.event_stream_abnormal);
					builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							//Log.e("tigertiger", "ExitApp.getInstance().exit()");
							//ExitApp.getInstance().exit();
						}
					});

					builder.setCancelable(false);
					builder.show();
					break;
				}
			}
		};
		
		SDKEvent sdkEvent = new SDKEvent(exceptionHandler);
		sdkEvent.addEventListener(ICatchEventID.ICH_EVENT_SERVER_STREAM_ERROR);
	}

}
