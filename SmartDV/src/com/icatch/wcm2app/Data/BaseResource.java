/**
 * Added by zhangyanhu C01012,2014-6-25
 */
package com.icatch.wcm2app.Data;

import android.content.Context;

import com.icatch.wcm2app.common.GlobalApp;
import com.icatch.wcm2app.R;

/**
 * Added by zhangyanhu C01012,2014-6-25
 */
public class BaseResource{

	private  String[] wbArray;
	private  String[] freqArray;
	private  String[] burstArray;
	private  String[] delayTimeArray;
	private  String[] timeLapseInterval;
	private  String[] timeLapseDuration;
	private  String[] timeLapseMode;
	private  String[] slowMotion;
	private  String[] upside;
	private  String[] videoMicArray;  
	private  String[] videoStampArray; 
	private  String[] logoStampArray; 						
	private  String[] carNumStampArray; 	
	//private  String[] videoTimeLapse = {"ON","OFF"};
	
	/**
	 * tags:customized sample for adding item 
	 * Action: add a new item
	 * Added by zhangyanhu C01012,2014-7-31
	 */
	//icon res
	//private  Integer[] customizeItemIconArray = { R.drawable.xx1, R.drawable.xx2, R.drawable.xx3};
	/**
	 * tags:customized sample for adding item 
	 * Action: add a new item
	 * End added by zhangyanhu C01012,2014-7-31
	 */

	private  String version = "R1.2.15.18";

	private  Integer[] wbResArray = { R.drawable.awb_auto, R.drawable.awb_daylight, R.drawable.awb_cloudy, R.drawable.awb_fluoresecent,
			R.drawable.awb_incadescent };

	private  Integer[] burstResArray = { R.drawable.continuous_shot_1, R.drawable.continuous_shot_2, R.drawable.continuous_shot_3 };
	/**
	 * Sample No:C0001 tags:customized sample for properties; 
	 * Action: add a new value in parent item
	 * Added by zhangyanhu C01012,2014-7-31
	 */
	//should add new icon for 20 burst
	//private  Integer[] burstResArray = { R.drawable.continuous_shot_1, R.drawable.continuous_shot_2, R.drawable.continuous_shot_4 };
	/**
	 * Sample No:C0001 tags:customized sample for properties; 
	 * Action: add a new value in parent item
	 * End added by zhangyanhu C01012,2014-7-31
	 */

	private  String[] dateStamp;

	private static BaseResource baseResource;

	public BaseResource() {
		// TODO Auto-generated constructor stub
	}

	public static BaseResource getInstance(){
		if(baseResource == null){
			baseResource = new BaseResource();
		}
		return baseResource;
	}
	
	public void initBaseResource(){
		GlobalApp globalApp = GlobalApp.getInstance();
		Context context = globalApp.getAppContext();

		wbArray = new String[5];
		wbArray[0] = context.getResources().getString(R.string.wb_auto);
		wbArray[1] = context.getResources().getString(R.string.wb_daylight);
		wbArray[2] = context.getResources().getString(R.string.wb_cloudy);
		wbArray[3] = context.getResources().getString(R.string.wb_fluorescent);
		wbArray[4] = context.getResources().getString(R.string.wb_incandescent);
		
		
		
		freqArray = new String[2];
		freqArray[0] = context.getResources().getString(R.string.frequency_50HZ);
		freqArray[1] = context.getResources().getString(R.string.frequency_60HZ);

		/**
		 * Sample No:C0001 tags:customized sample for properties; 
		 * Action: add a new value in parent item
		 * Added by zhangyanhu C01012,2014-7-31
		 */
		//burstArray = new String[6];
		/**
		 * Sample No:C0001 tags:customized sample for properties; 
		 * Action: add a new value in parent item
		 * End added by zhangyanhu C01012,2014-7-31
		 */
		burstArray = new String[5];
		burstArray[0] = context.getResources().getString(R.string.burst_off);
		burstArray[1] = context.getResources().getString(R.string.burst_3);
		burstArray[2] = context.getResources().getString(R.string.burst_5);
		burstArray[3] = context.getResources().getString(R.string.burst_10);
		burstArray[4] = context.getResources().getString(R.string.burst_hs);
		/**
		 * Sample No:C0001 tags:customized sample for properties; 
		 * Action: add a new value in parent item
		 * Added by zhangyanhu C01012,2014-7-31
		 */
		//burstArray[5] = context.getResources().getString(R.string.burst_20);
		/**
		 * Sample No:C0001 tags:customized sample for properties; 
		 * Action: add a new value in parent item
		 * End added by zhangyanhu C01012,2014-7-31
		 */
		
		delayTimeArray = context.getResources().getStringArray(R.array.setting_delaytime_list_values);
		
		dateStamp = new String[3];
		dateStamp[0] = context.getResources().getString(R.string.dateStamp_off);
		dateStamp[1] = context.getResources().getString(R.string.dateStamp_date);
		dateStamp[2] = context.getResources().getString(R.string.dateStamp_date_and_time);
				
		timeLapseDuration = new String[8];
		timeLapseDuration[0] = context.getResources().getString(R.string.setting_time_lapse_duration_2M);
		timeLapseDuration[1] = context.getResources().getString(R.string.setting_time_lapse_duration_5M);
		timeLapseDuration[2] = context.getResources().getString(R.string.setting_time_lapse_duration_10M);
		timeLapseDuration[3] = context.getResources().getString(R.string.setting_time_lapse_duration_15M);
		timeLapseDuration[4] = context.getResources().getString(R.string.setting_time_lapse_duration_20M);
		timeLapseDuration[5] = context.getResources().getString(R.string.setting_time_lapse_duration_30M);
		timeLapseDuration[6] = context.getResources().getString(R.string.setting_time_lapse_duration_60M);
		timeLapseDuration[7] = context.getResources().getString(R.string.setting_time_lapse_duration_unlimit);
		
		timeLapseInterval = new String[11];
		timeLapseInterval[0] = context.getResources().getString(R.string.setting_time_lapse_interval_off);
		timeLapseInterval[1] = context.getResources().getString(R.string.setting_time_lapse_interval_2s);
		timeLapseInterval[2] = context.getResources().getString(R.string.setting_time_lapse_interval_5s);
		timeLapseInterval[3] = context.getResources().getString(R.string.setting_time_lapse_interval_10s);
		timeLapseInterval[4] = context.getResources().getString(R.string.setting_time_lapse_interval_20s);
		timeLapseInterval[5] = context.getResources().getString(R.string.setting_time_lapse_interval_30s);
		timeLapseInterval[6] = context.getResources().getString(R.string.setting_time_lapse_interval_1M);
		timeLapseInterval[7] = context.getResources().getString(R.string.setting_time_lapse_interval_5M);
		timeLapseInterval[8] = context.getResources().getString(R.string.setting_time_lapse_interval_10M);
		timeLapseInterval[9] = context.getResources().getString(R.string.setting_time_lapse_interval_30M);
		timeLapseInterval[10] = context.getResources().getString(R.string.setting_time_lapse_interval_1HR);
		
		timeLapseMode = new String[2];
		timeLapseMode[0] = context.getResources().getString(R.string.timeLapse_video_mode);
		timeLapseMode[1] = context.getResources().getString(R.string.timeLapse_capture_mode);
		
		slowMotion = new String[2];
		slowMotion[0]  = context.getResources().getString(R.string.setting_off);
		slowMotion[1]  = context.getResources().getString(R.string.setting_on);
		
		upside = new String[2];
		upside[0]  = context.getResources().getString(R.string.setting_off);
		upside[1]  = context.getResources().getString(R.string.setting_on);
		videoMicArray = context.getResources().getStringArray(R.array.setting_mic_list_values);   
		videoStampArray = context.getResources().getStringArray(R.array.setting_mic_list_values);  
		logoStampArray = context.getResources().getStringArray(R.array.setting_mic_list_values);         	
		carNumStampArray = context.getResources().getStringArray(R.array.setting_mic_list_values); 
	}
	public  String[] getWhiteBalanceUIString() {
		return wbArray;
	}
	
	public  Integer[] getWhiteBalanceIconID() {
		return wbResArray;
	}

	public  String[] getFreValueUIString() {
		return freqArray;
	}

	public  String[] getDateStampUIString() {
		return dateStamp;
	}

	public  String[] getBurstNumUIString() {
		return burstArray;
	}
	
	public  Integer[] getBurstNumIconID() {
		return burstResArray;
	}
	
	public  String[] getDelayUIString() {
		return delayTimeArray;
	}
	
	public  String getAPPVersion(){
		return version;
	}
	
	public String[] getTimeLapseDuration(){
		return timeLapseDuration;
	}
	
	public String[] getTimeLapseInterval(){
		return timeLapseInterval;
	}
	
	public String[] getTimeLapseMode(){
		return timeLapseMode;
	}
	
	public  String[] getSlowMotionUIString() {
		return slowMotion;
	}
	
	public String[] getUpsideUIString(){
		return upside;
	}
	public  String[] getVideoMicUIString() {
		return videoMicArray;
	}
	public  String[] getVideoStampUIString() {
		return videoStampArray;
	}
	public String[] getLogoStampUIString() {
		return logoStampArray;
	}
	public String[] getCarNumStampUIString() {
		return carNumStampArray;
	}
}
