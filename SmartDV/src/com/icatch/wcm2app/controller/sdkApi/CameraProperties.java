/**
 * Added by zhangyanhu C01012,2014-6-23
 */
package com.icatch.wcm2app.controller.sdkApi;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import com.icatch.wcm2app.Data.FwToApp;
import com.icatch.wcm2app.common.WriteLogToDevice;
import com.icatch.wificam.customer.ICatchWificamControl;
import com.icatch.wificam.customer.ICatchWificamProperty;
import com.icatch.wificam.customer.ICatchWificamUtil;
import com.icatch.wificam.customer.exception.IchCameraModeException;
import com.icatch.wificam.customer.exception.IchDevicePropException;
import com.icatch.wificam.customer.exception.IchInvalidArgumentException;
import com.icatch.wificam.customer.exception.IchInvalidSessionException;
import com.icatch.wificam.customer.exception.IchNoSDCardException;
import com.icatch.wificam.customer.exception.IchSocketException;
import com.icatch.wificam.customer.type.ICatchLightFrequency;
import com.icatch.wificam.customer.type.ICatchMode;
import com.icatch.wificam.customer.type.ICatchVideoSize;

public class CameraProperties {
	private ICatchWificamProperty cameraConfiguration;
	private ICatchWificamControl cameraAction;
	private List<Integer> fuction;
	//private PreviewStream previewStream = new PreviewStream();
	private List<ICatchMode> modeList;
	public final int CmdProductId = 0xD701;
	public final int CmdSeamless = 0xD703;
	public final int CmdVideoDateStamp = 0xD704;
	public final int CmdVideoMic = 0xD705;
	public final int CmdTimeZone = 0xD706;
	public final int CmdCarNumStamp = 0xD707;
	public final int CmdSpeedStamp = 0xD708;
	public final int CmdLogoStamp = 0xD709;
	public final int CmdFrameWidth = 0xD70A;
	public final int CmdFrameHeight = 0xD70B;
	public final int CmdWiFiMode = 0xD70C;
	public final int CmdAirPlane = 0xD70D;
	
	/**A58S airplane project special cmd*/
	public final int CmdAirPlaneBatLvl = 0xD740;
	public final int CmdAirPlaneFlyTime = 0xD741;
	public final int CmdAirPlaneHeight = 0xD742;
	public final int CmdAirPlaneDistance = 0xD743;
	public final int CmdAirPlaneSpeed = 0xD744;
	public final int CmdAirPlaneLongitude = 0xD745;
	public final int CmdAirPlaneLatitude = 0xD746;
	public final int CmdAirPlaneMode = 0xD747;
	public final int CmdAirPlaneSatelliteNum= 0xD748;
	
	/**DV302 special cmd*/
	public final int CmdRecordingMode = 0xD750;
	public final int CmdParkingMode = 0xD751;
	public final int CmdGsensorX = 0xD752;
	public final int CmdGsensorY = 0xD753;
	public final int CmdGsensorZ = 0xD754;
	public final int CmdDST = 0xD755;
	public final int CmdDSTMode = 0xD756;
	public final int CmdDSTStartSunday = 0xD757;
	public final int CmdDSTStartMonth = 0xD758;
	public final int CmdDSTStopSunday = 0xD759;
	public final int CmdDSTStopMonth = 0xD75A;
	public final int CmdSpeedLimitEvent = 0xD75B;
	public final int CmdSpeedLimit = 0xD75C;

	/**add cmd id for string data common cmd*/
	public final int CmdDateTime = 0xD801;
	public final int CmdWiFiPwd = 0xD802;
	public final int CmdWiFiSSID = 0xD803;
	public final int CmdCarNum = 0xD804;
	/**A58S airplane project special cmd*/
	public final int CmdStaSSID = 0xD805;
	public final int CmdStaPwd = 0xD806;
	public final int CmdSetDate = 0xD807;
	public final int CmdSetTime = 0xD808;

	/**xiao add for date time frmat yyyy-MM-dd HH:mm:ss 24-hour yyyy-MM-dd hh:mm:ss 12-hour*/
	SimpleDateFormat  sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINESE);   
	
	public CameraProperties(){
		cameraConfiguration = SDKSession.getCameraPropertyClint();
		cameraAction = SDKSession.getcameraActionClient();
	}
	public int hasFunctionUpdateAirPlaneInfo(){
		int value=0xff;
		try {
			/** 1:A58S 2:DV302 3:D12S*/
	        value = cameraConfiguration.getCurrentPropertyValue(CmdAirPlane); 
		} catch (IchInvalidSessionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchSocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchDevicePropException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return value;
	}
	
	public List<String> getSupportedImageSizes() {
		List<String> list = null;
		try {
			list = cameraConfiguration.getSupportedImageSizes();
		} catch (IchSocketException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchSocketException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchCameraModeException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchInvalidSessionException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchDevicePropException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchDevicePropException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "end getSupportedImageSizes list.size =" + list.size());
		return list;
	}

	public List<String> getSupportedVideoSizes() {
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "begin getSupportedVideoSizes");
		List<String> list = null;
		try {
			list = cameraConfiguration.getSupportedVideoSizes();
		} catch (IchSocketException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchSocketException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchCameraModeException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchInvalidSessionException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchDevicePropException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchDevicePropException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "begin getSupportedVideoSizes size =" + list.size());
		return list;
	}

	public List<Integer> getSupportedWhiteBalances() {
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "begin getSupportedWhiteBalances");
		List<Integer> list = null;
		try {
			list = cameraConfiguration.getSupportedWhiteBalances();
		} catch (IchSocketException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchSocketException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchCameraModeException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchInvalidSessionException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchDevicePropException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchDevicePropException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "end getSupportedWhiteBalances list.size() =" + list.size());
		return list;
	}

	public List<Integer> getSupportedCaptureDelays() {
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "begin getSupportedCaptureDelays");
		List<Integer> list = null;
		try {
			list = cameraConfiguration.getSupportedCaptureDelays();
		} catch (IchSocketException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchSocketException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchCameraModeException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchInvalidSessionException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchDevicePropException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchDevicePropException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "end getSupportedCaptureDelays list.size() =" + list.size());
		return list;
	}

	public List<Integer> getSupportedLightFrequencys() {
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "begin getSupportedLightFrequencys");
		List<Integer> list = null;

		try {
			list = cameraConfiguration.getSupportedLightFrequencies();
		} catch (IchSocketException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchSocketException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchCameraModeException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchInvalidSessionException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchDevicePropException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchDevicePropException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// delete LIGHT_FREQUENCY_AUTO because UI don't need this option
		for (int ii = 0; ii < list.size(); ii++) {
			if (list.get(ii) == ICatchLightFrequency.ICH_LIGHT_FREQUENCY_AUTO) {
				list.remove(ii);
			}
		}
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "end getSupportedLightFrequencys list.size() =" + list.size());
		return list;
	}

	public boolean setImageSize(String value) {
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "begin setImageSize set value =" + value);
		boolean retVal = false;

		try {
			retVal = cameraConfiguration.setImageSize(value);
		} catch (IchSocketException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchSocketException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchCameraModeException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchInvalidSessionException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchDevicePropException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchDevicePropException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "end setImageSize retVal=" + retVal);
		return retVal;
	}

	public boolean setVideoSize(String value) {
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "begin setVideoSize set value =" + value);
		boolean retVal = false;

		try {
			retVal = cameraConfiguration.setVideoSize(value);
		} catch (IchSocketException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchSocketException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchCameraModeException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchInvalidSessionException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchDevicePropException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchDevicePropException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "end setVideoSize retVal=" + retVal);
		return retVal;
	}

	public boolean setWhiteBalance(int value) {
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "begin setWhiteBalanceset value =" + value);
		boolean retVal = false;
		if (value < 0 || value == 0xff) {
			return false;
		}
		try {
			retVal = cameraConfiguration.setWhiteBalance(value);
		} catch (IchSocketException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchSocketException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchCameraModeException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchInvalidSessionException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchDevicePropException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchDevicePropException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "end setWhiteBalance retVal=" + retVal);
		return retVal;
	}

	public boolean setLightFrequency(int value) {
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "begin setLightFrequency set value =" + value);
		boolean retVal = false;
		if (value < 0 || value == 0xff) {
			return false;
		}
		try {
			retVal = cameraConfiguration.setLightFrequency(value);
		} catch (IchSocketException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchSocketException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchCameraModeException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchInvalidSessionException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchDevicePropException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchDevicePropException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "end setLightFrequency retVal=" + retVal);
		return retVal;
	}

	public String getCurrentImageSize() {
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "begin getCurrentImageSize");
		String value = "unknown";

		try {
			value = cameraConfiguration.getCurrentImageSize();
		} catch (IchSocketException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchSocketException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchCameraModeException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchInvalidSessionException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchDevicePropException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchDevicePropException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "end getCurrentImageSize value =" + value);
		return value;
	}

	public String getCurrentVideoSize() {
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "begin getCurrentVideoSize");
		String value = "unknown";

		try {
			value = cameraConfiguration.getCurrentVideoSize();
		} catch (IchSocketException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchSocketException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchCameraModeException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchInvalidSessionException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchDevicePropException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchDevicePropException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "end getCurrentVideoSize value =" + value);
		return value;
	}

	public int getCurrentWhiteBalance() {
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "begin getCurrentWhiteBalance");
		int value = 0xff;
		try {
			WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "******value=   " + value);
			value = cameraConfiguration.getCurrentWhiteBalance();
		} catch (IchSocketException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchSocketException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchCameraModeException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchInvalidSessionException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchDevicePropException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchDevicePropException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "end getCurrentWhiteBalance retvalue =" + value);
		return value;
	}

	public int getCurrentLightFrequency() {
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "begin getCurrentLightFrequency");
		int value = 0xff;
		try {
			value = cameraConfiguration.getCurrentLightFrequency();
		} catch (IchSocketException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchSocketException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchCameraModeException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchInvalidSessionException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchDevicePropException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchDevicePropException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "end getCurrentLightFrequency value =" + value);
		return value;
	}

	public boolean setCaptureDelay(int value) {
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "begin setCaptureDelay set value =" + value);
		boolean retVal = false;

		try {
			WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "start setCaptureDelay ");
			retVal = cameraConfiguration.setCaptureDelay(value);
			WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "end setCaptureDelay ");
		} catch (IchSocketException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchSocketException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchCameraModeException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchInvalidSessionException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchDevicePropException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchDevicePropException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "end setCaptureDelay retVal =" + retVal);
		return retVal;
	}

	public int getCurrentCaptureDelay() {
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "begin getCurrentCaptureDelay");
		int retVal = 0;
		
		try {
			retVal = cameraConfiguration.getCurrentCaptureDelay();
		} catch (IchSocketException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchSocketException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchCameraModeException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchInvalidSessionException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchDevicePropException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchDevicePropException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "end getCurrentCaptureDelay retVal =" + retVal);
		return retVal;
	}

	public int getCurrentDateStamp() {
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "begin getCurrentDateStampType");
		int retValue = 0;
		try {
			retValue = cameraConfiguration.getCurrentDateStamp();
		} catch (IchSocketException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchSocketException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchCameraModeException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchInvalidSessionException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchDevicePropException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchDevicePropException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "getCurrentDateStampType retValue =" + retValue);
		return retValue;
	}

	/**
	 * 
	 * Added by zhangyanhu C01012,2014-4-1
	 */
	public boolean setDateStamp(int dateStamp) {
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "begin setDateStampType set value = " + dateStamp);
		Boolean retValue = false;
		try {
			retValue = cameraConfiguration.setDateStamp(dateStamp);
		} catch (IchSocketException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchSocketException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchCameraModeException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchInvalidSessionException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchDevicePropException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchDevicePropException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "end getCurrentVideoSize retValue =" + retValue);
		return retValue;
	}

	/**
	 * 
	 * Added by zhangyanhu C01012,2014-4-1
	 */
	public List<Integer> getDateStampList() {
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "begin getDateStampList");
		List<Integer> list = null;
		try {
			list = cameraConfiguration.getSupportedDateStamps();
		} catch (IchSocketException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchSocketException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchCameraModeException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchInvalidSessionException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchDevicePropException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchDevicePropException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "end getDateStampList list.size ==" + list.size());
		return list;
	}

	public List<Integer> getSupportFuction() {
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "begin getSupportFuction");
		List<Integer> fuction = null;
		// List<Integer> temp = null;
		try {
			fuction = cameraConfiguration.getSupportedProperties();
		} catch (IchSocketException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchSocketException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchCameraModeException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchInvalidSessionException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchDevicePropException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchDevicePropException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "end getSupportFuction fuction.size() =" + fuction.size());
		return fuction;
	}

	/**
	 * to prase the burst number Added by zhangyanhu C01012,2014-2-10
	 */
	public int getCurrentBurstNum() {
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "begin getCurrentBurstNum");
		int number = 0xff;
		try {
			number = cameraConfiguration.getCurrentBurstNumber();
		} catch (IchSocketException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchSocketException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchCameraModeException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchInvalidSessionException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchDevicePropException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchDevicePropException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "getCurrentBurstNum num =" + number);
		return number;
	}
	
	public int getCurrentAppBurstNum() {
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "begin getCurrentAppBurstNum");
		int number = 0xff;
		try {
			number = cameraConfiguration.getCurrentBurstNumber();
		} catch (IchSocketException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchSocketException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchCameraModeException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchInvalidSessionException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchDevicePropException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchDevicePropException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		number = FwToApp.getInstance().getAppBurstNum(number);
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "getCurrentAppBurstNum num =" + number);
		return number;
	}

	public boolean setCurrentBurst(int burstNum) {
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "begin setCurrentBurst set value = " + burstNum);
		if (burstNum < 0 || burstNum == 0xff) {
			return false;
		}
		boolean retValue = false;
		try {
			retValue = cameraConfiguration.setBurstNumber(burstNum);
		} catch (IchSocketException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchSocketException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchCameraModeException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchInvalidSessionException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchDevicePropException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchDevicePropException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "end setCurrentBurst retValue =" + retValue);
		return retValue;
	}

	public int getRemainImageNum() {
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "begin getRemainImageNum");
		int num = 0;
		try {
			num = cameraAction.getFreeSpaceInImages();
		} catch (IchSocketException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchSocketException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchCameraModeException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchInvalidSessionException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchDevicePropException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchDevicePropException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchNoSDCardException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchNoSDCardException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "end getRemainImageNum num =" + num);
		return num;
	}


	public int getRecordingRemainTime() {
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "begin getRecordingRemainTimeInt");
		int recordingTime = 0;

		try {
			recordingTime = cameraAction.getRemainRecordingTime();
		} catch (IchSocketException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchSocketException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchCameraModeException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchInvalidSessionException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchDevicePropException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchDevicePropException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchNoSDCardException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "end getRecordingRemainTimeInt recordingTime =" + recordingTime);
		return recordingTime;
	}


	public boolean isSDCardExist() {
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "begin isSDCardExist");
		Boolean isReady = false;

		try {
			isReady = cameraAction.isSDCardExist();
		} catch (IchSocketException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchSocketException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchCameraModeException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchInvalidSessionException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "end isSDCardExist isReady =" + isReady);
		return isReady;
	}

	public int getBatteryElectric() {
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "start getBatteryElectric");
		int electric = 0;
		try {
			electric = cameraAction.getCurrentBatteryLevel();
		} catch (IchSocketException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchSocketException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchCameraModeException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchInvalidSessionException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchDevicePropException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchDevicePropException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "end getBatteryElectric electric =" + electric);
		return electric;
	}

	public boolean supportVideoPlayback() {
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "begin hasVideoPlaybackFuction");
		boolean retValue = false;
		try {
			retValue = cameraAction.supportVideoPlayback();
		} catch (IchSocketException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchSocketException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchCameraModeException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchInvalidSessionException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchDevicePropException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchDevicePropException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchNoSDCardException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchNoSDCardException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "hasVideoPlaybackFuction retValue =" + retValue);
		return retValue;
		//return false;
	}

	public boolean cameraModeSupport(ICatchMode mode) {
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "begin hasVideoRecordFuction");
		Boolean retValue = false;
		if(modeList == null){
			modeList = getSupportedModes();
		}
		if(modeList.contains(mode)){
			retValue = true;
		}
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "end hasVideoRecordFuction retValue =" + retValue);
		return retValue;
	}

	public String getCameraMacAddress() {
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "begin getCameraMacAddress macAddress macAddress ");
		String macAddress = cameraAction.getMacAddress();
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "end getCameraMacAddress macAddress =" + macAddress);
		return macAddress;
	}

	public List<Integer> getConvertSupportedImageSizes() {
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "begin getConvertSupportedImageSizes");
		List<String> list = getSupportedImageSizes();
		List<Integer> convertImageSizeList = null;
		// ICatchWificamUtil sizeAss = new ICatchWificamUtil();
		try {
			convertImageSizeList = ICatchWificamUtil.convertImageSizes(list);
		} catch (IchInvalidArgumentException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchInvalidSessionException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "end getConvertSupportedImageSizes size =" + convertImageSizeList.size());
		return convertImageSizeList;
	}

	public List<ICatchVideoSize> getConvertSupportedVideoSizes() {
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "begin getConvertSupportedVideoSizes");
		List<String> list = getSupportedVideoSizes();
		List<ICatchVideoSize> convertVideoSizeList = null;
		// ICatchWificamUtil sizeAss = new ICatchWificamUtil();
		try {
			convertVideoSizeList = ICatchWificamUtil.convertVideoSizes(list);
		} catch (IchInvalidArgumentException e) {
			// TODO Auto-generated catch block
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchInvalidArgumentException");
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "end getConvertSupportedVideoSizes convertVideoSizeList.size() =" + convertVideoSizeList.size());
		return convertVideoSizeList;
	}

	public boolean hasFuction(int fuc) {
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "begin hasFuction query fuction = " + fuc);
		if (fuction == null) {
			fuction = getSupportFuction();
		}
		Boolean retValue = false;
		if (fuction.contains(fuc) == true) {
			retValue = true;
		}
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "end hasFuction retValue =" + retValue);
		return retValue;
	}

	/**
	 * Added by zhangyanhu C01012,2014-7-4
	 */
	public List<Integer> getsupportedDateStamps() {
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "begin getsupportedDateStamps");
		List<Integer> list = null;

		try {
			list = cameraConfiguration.getSupportedDateStamps();
		} catch (IchSocketException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchSocketException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchCameraModeException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchInvalidSessionException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchDevicePropException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchDevicePropException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "end getsupportedDateStamps list.size() =" + list.size());
		return list;
	}

	public List<Integer> getsupportedBurstNums() {
		// TODO Auto-generated method stub
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "begin getsupportedBurstNums");
		List<Integer> list = null;

		try {
			list = cameraConfiguration.getSupportedBurstNumbers();
		} catch (IchSocketException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchSocketException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchCameraModeException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchInvalidSessionException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchDevicePropException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchDevicePropException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "end getsupportedBurstNums list.size() =" + list.size());
		return list;
	}

	/**
	 * Added by zhangyanhu C01012,2014-7-4
	 */
	public List<Integer> getSupportedFrequencies() {
		// TODO Auto-generated method stub
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "begin getSupportedFrequencies");
		List<Integer> list = null;
		try {
			list = cameraConfiguration.getSupportedLightFrequencies();
		} catch (IchSocketException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchSocketException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchCameraModeException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchInvalidSessionException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchDevicePropException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchDevicePropException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "end getSupportedFrequencies list.size() =" + list.size());
		return list;
	}
	
	/**
	 * 
	 * Added by zhangyanhu C01012,2014-8-21
	 * @return 
	 */
	public  List<ICatchMode> getSupportedModes() {
		WriteLogToDevice.writeLog("[Normal] -- CameraAction: ", "begin getSupportedModes");

		List<ICatchMode> list = null;
		try {
			list = cameraAction.getSupportedModes();
		} catch (IchSocketException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraAction: ", "IchSocketException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraAction: ", "IchCameraModeException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraAction: ", "IchInvalidSessionException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- CameraAction: ", "end getSupportedModes list ="+list.size());
		return list;
	}
	
//	public List<Integer> getSupportedTimeLapseStillDurations() {
//		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "begin getSupportedTimeLapseStillDurations");
//		List<Integer> list = null;
//		//boolean retValue = false;
//		try {
//			list  = cameraConfiguration.getSupportedTimeLapseStillDurations();
//		} catch (IchSocketException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IchCameraModeException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IchDevicePropException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IchInvalidSessionException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "end getSupportedTimeLapseStillDurations list =" + list.size());
//		return list;
//	}
//	
//	public List<Integer> getSupportedTimeLapseStillintervals() {
//		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "begin getSupportedTimeLapseStillintervals");
//		List<Integer> list = null;
//		//boolean retValue = false;
//		try {
//			list  = cameraConfiguration.getSupportedTimeLapseStillIntervals();
//		} catch (IchSocketException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IchCameraModeException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IchDevicePropException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IchInvalidSessionException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "end getSupportedTimeLapseStillintervals list =" + list.size());
//		return list;
//	}
	
	public List<Integer> getSupportedTimeLapseDurations() {
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "begin getSupportedTimeLapseDurations");
		List<Integer> list = null;
		//boolean retValue = false;
		try {
			list  = cameraConfiguration.getSupportedTimeLapseDurations();
		} catch (IchSocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchDevicePropException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(); 
		}
		for(int ii = 0; ii < list.size();ii++){
			WriteLogToDevice.writeLog("[Normal] -- CameraProperties:","list.get(ii) ="+list.get(ii));
		}
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "end getSupportedTimeLapseDurations list =" + list.size());
		return list;
	}
	
	public List<Integer> getSupportedTimeLapseIntervals() {
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "begin getSupportedTimeLapseIntervals");
		List<Integer> list = null;
		//boolean retValue = false;
		try {
			list  = cameraConfiguration.getSupportedTimeLapseIntervals();
		} catch (IchSocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchDevicePropException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(int ii = 0; ii < list.size();ii++){
			WriteLogToDevice.writeLog("[Normal] -- CameraProperties:","list.get(ii) ="+list.get(ii));
		}
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "end getSupportedTimeLapseIntervals list =" + list.size());
		return list;
	}
	
	public boolean setTimeLapseDuration(int timeDuration) {
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "begin setTimeLapseDuration videoDuration =" + timeDuration);
		boolean retVal = false;
		if (timeDuration < 0 || timeDuration == 0xff) {
			return false;
		}
		try {
			retVal = cameraConfiguration.setTimeLapseDuration(timeDuration);
		} catch (IchSocketException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchSocketException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchCameraModeException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchInvalidSessionException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchDevicePropException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchDevicePropException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "end setTimeLapseDuration retVal=" + retVal);
		return retVal;
	}
	
	public  int getCurrentTimeLapseDuration() {
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "begin getCurrentTimeLapseDuration");
		int retVal = 0xff;
		try {
			retVal = cameraConfiguration.getCurrentTimeLapseDuration();
		} catch (IchSocketException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchSocketException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchCameraModeException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchInvalidSessionException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchDevicePropException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchDevicePropException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "end getCurrentTimeLapseDuration retVal=" + retVal);
		return retVal;
	}
	
	public boolean setTimeLapseInterval(int timeInterval) {
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "begin setTimeLapseInterval videoDuration =" + timeInterval);
		boolean retVal = false;
		if (timeInterval < 0 || timeInterval == 0xff) {
			return false;
		}
		try {
			retVal = cameraConfiguration.setTimeLapseInterval(timeInterval);
		} catch (IchSocketException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchSocketException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchCameraModeException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchInvalidSessionException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchDevicePropException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchDevicePropException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "end setTimeLapseInterval retVal=" + retVal);
		return retVal;
	}
	
	public  int getCurrentTimeLapseInterval() {
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "begin getCurrentTimeLapseInterval");
		int retVal = 0xff;
		try {
			retVal = cameraConfiguration.getCurrentTimeLapseInterval();
		} catch (IchSocketException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchSocketException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchCameraModeException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchInvalidSessionException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchDevicePropException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchDevicePropException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "end getCurrentTimeLapseInterval retVal=" + retVal);
		return retVal;
	}
	
	public int getMaxZoomRatio() {
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "start getMaxZoomRatio");
		int retValue = 0;
		try {
			retValue = cameraConfiguration.getMaxZoomRatio();
		} catch (IchSocketException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchSocketException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchCameraModeException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchInvalidSessionException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchDevicePropException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchDevicePropException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "end getMaxZoomRatio retValue =" + retValue);
		return retValue;
	}
	
	public int getCurrentZoomRatio() {
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "start getCurrentZoomRatio");
		int retValue = 0;
		try {
			retValue = cameraConfiguration.getCurrentZoomRatio();
		} catch (IchSocketException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchSocketException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchCameraModeException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchInvalidSessionException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchDevicePropException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchDevicePropException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "end getCurrentZoomRatio retValue =" + retValue);
		return retValue;
	}
	
	public int getCurrentUpsideDown() {
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "start getCurrentUpsideDown");
		int retValue = 0;
		try {
			retValue = cameraConfiguration.getCurrentUpsideDown();
		} catch (IchSocketException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchSocketException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchCameraModeException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchInvalidSessionException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchDevicePropException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchDevicePropException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "end getCurrentUpsideDown retValue =" + retValue);
		return retValue;
	}
	
	public boolean setUpsideDown(int upside) {
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "start setUpsideDown upside = "+upside);
		boolean retValue = false;
		try {
			retValue = cameraConfiguration.setUpsideDown(upside);
		} catch (IchSocketException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchSocketException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchCameraModeException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchInvalidSessionException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchDevicePropException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchDevicePropException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "end setUpsideDown retValue =" + retValue);
		return retValue;
	}
	
	public int getCurrentSlowMotion() {
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "start getCurrentSlowMotion");
		int retValue = 0;
		try {
			retValue = cameraConfiguration.getCurrentSlowMotion();
		} catch (IchSocketException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchSocketException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchCameraModeException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchInvalidSessionException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchDevicePropException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchDevicePropException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "end getCurrentSlowMotion retValue =" + retValue);
		return retValue;
	}
	
	public boolean setSlowMotion(int slowMotion) {
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "start setSlowMotion slowMotion = "+slowMotion);
		boolean retValue = false;
		try {
			retValue = cameraConfiguration.setSlowMotion(slowMotion);
		} catch (IchSocketException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchSocketException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchCameraModeException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchInvalidSessionException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchDevicePropException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchDevicePropException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "end setSlowMotion retValue =" + retValue);
		return retValue;
	}
	
	public boolean setCameraDate(String date) {
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "start setCameraDate date = "+date);
		boolean retValue = false;
		try{
			retValue = cameraConfiguration.setStringPropertyValue(0x5011,date);
		} catch (IchSocketException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchSocketException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchCameraModeException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchInvalidSessionException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchDevicePropException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchDevicePropException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "end setCameraDate retValue =" + retValue);
		return retValue;
	}
	
	public boolean setCameraSsid(String ssid) {
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "start setCameraSsid date = "+ssid);
		boolean retValue = false;
		try{
			retValue = cameraConfiguration.setStringPropertyValue(0xD83C,ssid);
		} catch (IchSocketException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchSocketException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchCameraModeException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchInvalidSessionException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchDevicePropException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchDevicePropException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "end setCameraSsid retValue =" + retValue);
		return retValue;
	}
	
	public String getCameraSsid() {
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "start getCameraSsid date = ");
		String retValue = null;
		try{
			retValue = cameraConfiguration.getCurrentStringPropertyValue(0xD83C);
		} catch (IchSocketException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchSocketException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchCameraModeException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchInvalidSessionException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchDevicePropException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchDevicePropException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "end getCameraSsid retValue =" + retValue);
		return retValue;
	}
	
	public boolean setCameraPassword(String password) {
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "start setCameraSsid date = "+password);
		boolean retValue = false;
		try{
			retValue = cameraConfiguration.setStringPropertyValue(0xD83D,password);
		} catch (IchSocketException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchSocketException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchCameraModeException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchInvalidSessionException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchDevicePropException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchDevicePropException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "end setCameraSsid retValue =" + retValue);
		return retValue;
	}
	
	public String getCameraPassword() {
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "start getCameraPassword date = ");
		String retValue = null;
		try{
			retValue = cameraConfiguration.getCurrentStringPropertyValue(0xD83D);
		} catch (IchSocketException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchSocketException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchCameraModeException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchInvalidSessionException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchDevicePropException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchDevicePropException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "end getCameraPassword retValue =" + retValue);
		return retValue;
	}
	
	public boolean setCaptureDelayMode(int  value) {
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "start setCaptureDelayMode value = "+value);
		boolean retValue = false;
		try{
			retValue = cameraConfiguration.setPropertyValue(0xD7F0,value);
		} catch (IchSocketException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchSocketException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchCameraModeException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchInvalidSessionException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchDevicePropException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchDevicePropException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "end setCaptureDelayMode retValue =" + retValue);
		return retValue;
	}
	
	public int getVideoRecordingTime() {
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "start getRecordingTime");
		int retValue = 0;
		try{
			retValue = cameraConfiguration.getCurrentPropertyValue(0xD7FD);
		} catch (IchSocketException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchSocketException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchCameraModeException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchInvalidSessionException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchDevicePropException e) {
			WriteLogToDevice.writeLog("[Error] -- CameraProperties: ", "IchDevicePropException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- CameraProperties: ", "end getRecordingTime retValue =" + retValue);
		return retValue;
	}
	public boolean setVideoMic(int value){	
		boolean returnvalue =false;
		try {
	        returnvalue = cameraConfiguration.setPropertyValue(CmdVideoMic, value); 
		} catch (IchInvalidSessionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchSocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchDevicePropException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnvalue;
	}
	public int getCurrentVideoMic() {
		int number = 0xff;
		try {
			number = cameraConfiguration.getCurrentPropertyValue(CmdVideoMic);
		} catch (IchSocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchDevicePropException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return number;
	}
	
	public List<Integer> getsupportedVideoMic() {
		List<Integer> list = null;
		try {
			list = cameraConfiguration.getSupportedPropertyValues(CmdVideoMic);
		} catch (IchSocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchDevicePropException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	public boolean setVideoStamp(int value){	
		boolean returnvalue =false;
		try {
	        returnvalue = cameraConfiguration.setPropertyValue(CmdVideoDateStamp, value); 
		} catch (IchInvalidSessionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchSocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchDevicePropException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnvalue;
	}
	public int getCurrentVideoStamp() {
		int number = 0xff;
		try {
			number = cameraConfiguration.getCurrentPropertyValue(CmdVideoDateStamp);
		} catch (IchSocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchDevicePropException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return number;
	}
	public List<Integer> getsupportedVideoStamp() {
		List<Integer> list = null;
		try {
			list = cameraConfiguration.getSupportedPropertyValues(CmdVideoDateStamp);
		} catch (IchSocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchDevicePropException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	public boolean setCarNumStamp(int value){	
		boolean returnvalue =false;
		try {
	        returnvalue = cameraConfiguration.setPropertyValue(CmdCarNumStamp, value); 
		} catch (IchInvalidSessionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchSocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchDevicePropException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnvalue;
	}
	public int getCurrentCarNumStamp() {
		int number = 0xff;
		try {
			number = cameraConfiguration.getCurrentPropertyValue(CmdCarNumStamp);
		} catch (IchSocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchDevicePropException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return number;
	}
	public List<Integer> getsupportedCarNumStamp() {
		List<Integer> list = null;
		try {
			list = cameraConfiguration.getSupportedPropertyValues(CmdCarNumStamp);
		} catch (IchSocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchDevicePropException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	public boolean setLogoStamp(int value){	
		boolean returnvalue =false;
		try {
	        returnvalue = cameraConfiguration.setPropertyValue(CmdLogoStamp, value); 
		} catch (IchInvalidSessionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchSocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchDevicePropException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnvalue;
	}
	public int getCurrentLogoStamp() {
		int number = 0xff;
		try {
			number = cameraConfiguration.getCurrentPropertyValue(CmdLogoStamp);
		} catch (IchSocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchDevicePropException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return number;
	}
	public List<Integer> getsupportedLogoStamp() {
		List<Integer> list = null;
		try {
			list = cameraConfiguration.getSupportedPropertyValues(CmdLogoStamp);
		} catch (IchSocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchDevicePropException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	public boolean setCarNum(String carnum){	
		boolean returnvalue =false;
		try {		
	        returnvalue = cameraConfiguration.setStringPropertyValue(CmdCarNum, carnum); 
		} catch (IchInvalidSessionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchSocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchDevicePropException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnvalue;
	}
	public String getCurrentCarNum(){	
		String returnvalue ="CARNUMunknown";
		try {
	        returnvalue = cameraConfiguration.getCurrentStringPropertyValue(CmdCarNum); 
		} catch (IchInvalidSessionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchSocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchDevicePropException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnvalue;
	}
	public boolean setStaWiFiSSID(String SSID){	
		boolean returnvalue =false;
		try {			
	        returnvalue = cameraConfiguration.setStringPropertyValue(CmdStaSSID, SSID); 
		} catch (IchInvalidSessionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchSocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchDevicePropException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnvalue;
	}
	
	public boolean setStaWiFiPwd(String pwd){	
		boolean returnvalue =false;
		try {		
	        returnvalue = cameraConfiguration.setStringPropertyValue(CmdStaPwd, pwd); 
		} catch (IchInvalidSessionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchSocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchDevicePropException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnvalue;
	}
	public String getCurrentStaWiFiSSID(){	
		String returnvalue ="STASSIDunknown";
		try {
	        returnvalue = cameraConfiguration.getCurrentStringPropertyValue(CmdStaSSID); 
		} catch (IchInvalidSessionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchSocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchDevicePropException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnvalue;
	}
	
	public String getCurrentStaWiFiPwd(){	
		String returnvalue ="STAPWDunknown";;
		try {
	        returnvalue = cameraConfiguration.getCurrentStringPropertyValue(CmdStaPwd); 
		} catch (IchInvalidSessionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchSocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IchDevicePropException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnvalue;
	}
}
