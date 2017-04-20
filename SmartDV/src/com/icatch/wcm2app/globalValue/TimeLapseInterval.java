/**
 * Added by zhangyanhu C01012,2014-8-29
 */
package com.icatch.wcm2app.globalValue;

import java.util.ArrayList;
import java.util.List;

import com.icatch.wcm2app.common.WriteLogToDevice;
import com.icatch.wcm2app.controller.sdkApi.CameraProperties;
import com.icatch.wificam.customer.type.ICatchMode;

/**
 * Added by zhangyanhu C01012,2014-8-29
 */
public class TimeLapseInterval {
	public static final int TIME_LAPSE_INTERVAL_OFF = 0;
//	public static final int TIME_LAPSE_INTERVAL_2S = 2;
//	public static final int TIME_LAPSE_INTERVAL_5S = 5;
//	public static final int TIME_LAPSE_INTERVAL_10S = 10;
//	public static final int TIME_LAPSE_INTERVAL_20S = 20;
//	public static final int TIME_LAPSE_INTERVAL_30S = 30;
//	public static final int TIME_LAPSE_INTERVAL_1MIN = 60;
//	public static final int TIME_LAPSE_INTERVAL_5MIN = 300;
//	public static final int TIME_LAPSE_INTERVAL_10MIN = 600;
//	public static final int TIME_LAPSE_INTERVAL_30MIN = 1800;
//	public static final int TIME_LAPSE_INTERVAL_1HR = 3600;

	private String[] valueListString;
	private int[] valueListInt;
	private static TimeLapseInterval timeLapseInterval;

	private TimeLapseInterval(){
		
	}
	public static TimeLapseInterval getInstance(){
		if(timeLapseInterval == null){
			timeLapseInterval = new TimeLapseInterval();
		}
		return timeLapseInterval;
	}
	
	public static void createTimeLapseInterval(){
		timeLapseInterval = new TimeLapseInterval();
	}
	
	public String getCurrentValue() {
		return convertTimeLapseInterval(cameraProperty.getCurrentTimeLapseInterval());
	}

	public String[] getValueStringList() {
		return valueListString;
	}

	public int[] getValueStringInt() {
		return valueListInt;
	}
	
	private CameraProperties cameraProperty = new CameraProperties();
	
	public void initTimeLapseInterval() {
		WriteLogToDevice.writeLog("[Normal] -- TimeLapseInterval: ", "begin initTimeLapseInterval");
		
		if (cameraProperty.cameraModeSupport(ICatchMode.ICH_MODE_TIMELAPSE) == false) {
			return;
		}
		List<Integer> list = cameraProperty.getSupportedTimeLapseIntervals();
		int length = list.size();
		ArrayList<String> tempArrayList = new ArrayList<String>();
		valueListInt = new int[length];
		
		for (int ii = 0; ii < length; ii++) {
			tempArrayList.add(convertTimeLapseInterval(list.get(ii)));
			valueListInt[ii] = list.get(ii);
		}
		
		valueListString = new String[tempArrayList.size()];
		for(int ii = 0;ii < tempArrayList.size();ii++){
			valueListString[ii] = tempArrayList.get(ii);
		}	
		WriteLogToDevice.writeLog("[Normal] -- TimeLapseInterval: ", "end initTimeLapseInterval timeLapseInterval =" + valueListString.length);
	}
	
	public static String convertTimeLapseInterval(int value){
		if(value == 0){
			return "OFF";
		}
		String time = "";
		int h = value / 3600;
		int m = (value % 3600) / 60;
		int s = value % 60;
		if(h > 0){
			time = time + h+" HR ";
		}
		if(m > 0){
			time = time + m + " Min ";
		}
		if(s > 0){
			time = time + s + " Sec";
		}
		return time;
	}
}
