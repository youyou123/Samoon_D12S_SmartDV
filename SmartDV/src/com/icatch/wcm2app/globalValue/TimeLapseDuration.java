/**
 * Added by zhangyanhu C01012,2014-8-29
 */
package com.icatch.wcm2app.globalValue;

/**
 * Added by zhangyanhu C01012,2014-8-29
 */
public class TimeLapseDuration {
	public static final int TIME_LAPSE_DURATION_2MIN = 2;
	public static final int TIME_LAPSE_DURATION_5MIN = 5;
	public static final int TIME_LAPSE_DURATION_10MIN = 10;
	public static final int TIME_LAPSE_DURATION_15MIN = 15;
	public static final int TIME_LAPSE_DURATION_20MIN = 20;
	public static final int TIME_LAPSE_DURATION_30MIN = 30;
	public static final int TIME_LAPSE_DURATION_60MIN = 60;
	public static final int TIME_LAPSE_DURATION_UNLIMITED = 0xffff;
//	public static final int TIME_LAPSE_DURATION_2MIN = 1;
//	public static final int TIME_LAPSE_DURATION_5MIN = 2;
//	public static final int TIME_LAPSE_DURATION_10MIN = 3;
//	public static final int TIME_LAPSE_DURATION_15MIN = 4;
//	public static final int TIME_LAPSE_DURATION_20MIN = 5;
//	public static final int TIME_LAPSE_DURATION_30MIN = 6;
//	public static final int TIME_LAPSE_DURATION_60MIN = 7;
//	public static final int TIME_LAPSE_DURATION_UNLIMITED = 0xffff;
	
	public static String convertTimeLapseDuration(int value){
		String time = "";
		int h = value / 60;
		int m = value % 60;
		//int s = value % 60;
		if(h > 0){
			time = time + h+"HR";
		}
		if(m > 0){
			time = time + m + "Min";
		}
		return time;
	}
}
