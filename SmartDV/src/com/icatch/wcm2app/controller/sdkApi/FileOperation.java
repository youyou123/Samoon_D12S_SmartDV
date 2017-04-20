/**
 * Added by zhangyanhu C01012,2014-6-27
 */
package com.icatch.wcm2app.controller.sdkApi;

import java.util.List;

import com.icatch.wcm2app.common.WriteLogToDevice;
import com.icatch.wificam.customer.ICatchWificamPlayback;
import com.icatch.wificam.customer.exception.IchBufferTooSmallException;
import com.icatch.wificam.customer.exception.IchCameraModeException;
import com.icatch.wificam.customer.exception.IchDeviceException;
import com.icatch.wificam.customer.exception.IchInvalidSessionException;
import com.icatch.wificam.customer.exception.IchNoSuchFileException;
import com.icatch.wificam.customer.exception.IchNoSuchPathException;
import com.icatch.wificam.customer.exception.IchSocketException;
import com.icatch.wificam.customer.type.ICatchFile;
import com.icatch.wificam.customer.type.ICatchFileType;
import com.icatch.wificam.customer.type.ICatchFrameBuffer;

/**
 * Added by zhangyanhu C01012,2014-6-27
 */
public class FileOperation {
	public ICatchWificamPlayback cameraPlayback = SDKSession.getplaybackClient();

	public boolean cancelDownload() {
		WriteLogToDevice.writeLog("[Normal] -- FileOperation: ", "begin cancelDownload");
		boolean retValue = false;
			try {
				retValue = cameraPlayback.cancelFileDownload();
			} catch (IchSocketException e) {
				WriteLogToDevice.writeLog("[Error] -- FileOperation: ", "IchSocketException");
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IchCameraModeException e) {
				WriteLogToDevice.writeLog("[Error] -- FileOperation: ", "IchCameraModeException");
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IchInvalidSessionException e) {
				WriteLogToDevice.writeLog("[Error] -- FileOperation: ", "IchInvalidSessionException");
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IchDeviceException e) {
				WriteLogToDevice.writeLog("[Error] -- FileOperation: ", "IchDeviceException");
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		//}
		WriteLogToDevice.writeLog("[Normal] -- FileOperation: ", "end cancelDownload retValue =" + retValue);
		return retValue;
	}

	public List<ICatchFile> getFileList(ICatchFileType type) {
		WriteLogToDevice.writeLog("[Normal] -- FileOperation: ", "begin getFileList");
		List<ICatchFile> list = null;
		try {
			list = cameraPlayback.listFiles(type);
		} catch (IchSocketException e) {
			// TODO Auto-generated catch block
			WriteLogToDevice.writeLog("[Error] -- FileOperation: ", "IchSocketException");
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			// TODO Auto-generated catch block
			WriteLogToDevice.writeLog("[Error] -- FileOperation: ", "IchCameraModeException");
			e.printStackTrace();
		} catch (IchNoSuchPathException e) {
			// TODO Auto-generated catch block
			WriteLogToDevice.writeLog("[Error] -- FileOperation: ", "IchNoSuchPathException");
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			// TODO Auto-generated catch block
			WriteLogToDevice.writeLog("[Error] -- FileOperation: ", "IchInvalidSessionException");
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- FileOperation: ", "end getFileList list="+list);
		return list;
	}
	
	public boolean deleteFile(ICatchFile file){
		WriteLogToDevice.writeLog("[Normal] -- FileOperation: ", "begin deleteFile filename ="+file.getFileName());
		boolean retValue = false;
		try {
			retValue = cameraPlayback.deleteFile(file);// cameraPlayback.deleteFile(null)
		} catch (IchSocketException e) {
			// TODO Auto-generated catch block
			WriteLogToDevice.writeLog("[Error] -- FileOperation: ", "IchSocketException");
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			// TODO Auto-generated catch block
			WriteLogToDevice.writeLog("[Error] -- FileOperation: ", "IchCameraModeException");
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			// TODO Auto-generated catch block
			WriteLogToDevice.writeLog("[Error] -- FileOperation: ", "IchInvalidSessionException");
		} catch (IchNoSuchFileException e) {
			// TODO Auto-generated catch block
			WriteLogToDevice.writeLog("[Error] -- FileOperation: ", "IchNoSuchFileException");
			e.printStackTrace();
		} catch (IchDeviceException e) {
			// TODO Auto-generated catch block
			WriteLogToDevice.writeLog("[Error] -- FileOperation: ", "IchDeviceException");
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- FileOperation: ", "end deleteFile retValue="+retValue);
		return retValue;
	}
	
	public boolean downloadFile(ICatchFile file, String path){
		WriteLogToDevice.writeLog("[Normal] -- FileOperation: ", "begin downloadFile filename ="+file.getFileName());
		WriteLogToDevice.writeLog("[Normal] -- FileOperation: ", "begin downloadFile path ="+path);
		boolean retValue = false;
		try {
			retValue = cameraPlayback.downloadFile(file, path);
		} catch (IchSocketException e) {
			// TODO Auto-generated catch block
			WriteLogToDevice.writeLog("[Error] -- FileOperation: ", "IchSocketException");
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			// TODO Auto-generated catch block
			WriteLogToDevice.writeLog("[Error] -- FileOperation: ", "IchCameraModeException");
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			// TODO Auto-generated catch block
			WriteLogToDevice.writeLog("[Error] -- FileOperation: ", "IchInvalidSessionException");
		} catch (IchNoSuchFileException e) {
			// TODO Auto-generated catch block
			WriteLogToDevice.writeLog("[Error] -- FileOperation: ", "IchNoSuchFileException");
			e.printStackTrace();
		} catch (IchDeviceException e) {
			// TODO Auto-generated catch block
			WriteLogToDevice.writeLog("[Error] -- FileOperation: ", "IchDeviceException");
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- FileOperation: ", "end downloadFile retValue ="+retValue);
		return retValue;
	}

	/**
	 * Added by zhangyanhu C01012,2014-7-2
	 */
	public ICatchFrameBuffer downloadFile(ICatchFile curFile) {
		WriteLogToDevice.writeLog("[Normal] -- FileOperation: ", "begin downloadFile for buffer filename ="+curFile.getFileName());
		ICatchFrameBuffer buffer = null;
		try {
			buffer = cameraPlayback.downloadFile(curFile);
		} catch (IchSocketException e) {
			// TODO Auto-generated catch block
			WriteLogToDevice.writeLog("[Error] -- FileOperation: ", "IchSocketException");
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			// TODO Auto-generated catch block
			WriteLogToDevice.writeLog("[Error] -- FileOperation: ", "IchCameraModeException");
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			// TODO Auto-generated catch block
			WriteLogToDevice.writeLog("[Error] -- FileOperation: ", "IchInvalidSessionException");
		} catch (IchNoSuchFileException e) {
			// TODO Auto-generated catch block
			WriteLogToDevice.writeLog("[Error] -- FileOperation: ", "IchNoSuchFileException");
			e.printStackTrace();
		} catch (IchDeviceException e) {
			// TODO Auto-generated catch block
			WriteLogToDevice.writeLog("[Error] -- FileOperation: ", "IchDeviceException");
			e.printStackTrace();
		} catch (IchBufferTooSmallException e) {
			// TODO Auto-generated catch block
			WriteLogToDevice.writeLog("[Error] -- FileOperation: ", "IchBufferTooSmallException");
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- FileOperation: ", "end downloadFile for buffer, buffer ="+buffer);
		return buffer;
	}

	
	/**
	 * 
	 * Added by zhangyanhu C01012,2014-10-28
	 */
	
	public ICatchFrameBuffer getQuickview(ICatchFile curFile) {
		WriteLogToDevice.writeLog("[Normal] -- FileOperation: ", "begin getQuickview for buffer filename ="+curFile.getFileName());
		ICatchFrameBuffer buffer = null;
		try {
			buffer = cameraPlayback.getQuickview(curFile);
		} catch (IchSocketException e) {
			// TODO Auto-generated catch block
			WriteLogToDevice.writeLog("[Error] -- FileOperation: ", "IchSocketException");
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			// TODO Auto-generated catch block
			WriteLogToDevice.writeLog("[Error] -- FileOperation: ", "IchCameraModeException");
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			// TODO Auto-generated catch block
			WriteLogToDevice.writeLog("[Error] -- FileOperation: ", "IchInvalidSessionException");
		} catch (IchNoSuchFileException e) {
			// TODO Auto-generated catch block
			WriteLogToDevice.writeLog("[Error] -- FileOperation: ", "IchNoSuchFileException");
			e.printStackTrace();
		} catch (IchDeviceException e) {
			// TODO Auto-generated catch block
			WriteLogToDevice.writeLog("[Error] -- FileOperation: ", "IchDeviceException");
			e.printStackTrace();
		} catch (IchBufferTooSmallException e) {
			// TODO Auto-generated catch block
			WriteLogToDevice.writeLog("[Error] -- FileOperation: ", "IchBufferTooSmallException");
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- FileOperation: ", "end getQuickview for buffer, buffer ="+buffer);
		return buffer;
	}
	
	/**
	 * Added by zhangyanhu C01012,2014-7-2
	 */
	public ICatchFrameBuffer getThumbnail(ICatchFile file) {
		WriteLogToDevice.writeLog("[Normal] -- FileOperation: ", "begin getThumbnail");
		// TODO Auto-generated method stub
		ICatchFrameBuffer frameBuffer = null;
		try {
			frameBuffer = cameraPlayback.getThumbnail(file);
		} catch (IchSocketException e) {
			// TODO Auto-generated catch block
			WriteLogToDevice.writeLog("[Error] -- FileOperation: ", "IchSocketException");
			e.printStackTrace();
		} catch (IchCameraModeException e) {
			// TODO Auto-generated catch block
			WriteLogToDevice.writeLog("[Error] -- FileOperation: ", "IchCameraModeException");
			e.printStackTrace();
		} catch (IchInvalidSessionException e) {
			// TODO Auto-generated catch block
			WriteLogToDevice.writeLog("[Error] -- FileOperation: ", "IchInvalidSessionException");
		} catch (IchNoSuchFileException e) {
			// TODO Auto-generated catch block
			WriteLogToDevice.writeLog("[Error] -- FileOperation: ", "IchNoSuchFileException");
			e.printStackTrace();
		} catch (IchDeviceException e) {
			// TODO Auto-generated catch block
			WriteLogToDevice.writeLog("[Error] -- FileOperation: ", "IchDeviceException");
			e.printStackTrace();
		} catch (IchBufferTooSmallException e) {
			// TODO Auto-generated catch block
			WriteLogToDevice.writeLog("[Error] -- FileOperation: ", "IchBufferTooSmallException");
			e.printStackTrace();
		}
		WriteLogToDevice.writeLog("[Normal] -- FileOperation: ", "end getThumbnail frameBuffer="+frameBuffer);
		return frameBuffer;
	}
	
}
