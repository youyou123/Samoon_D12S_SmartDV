/**
 * Added by zhangyanhu C01012,2014-7-15
 */
package com.icatch.wcm2app.ExtendComponent;

import java.nio.ByteBuffer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.icatch.wcm2app.common.ScaleTool;
import com.icatch.wcm2app.common.WriteLogToDevice;
import com.icatch.wcm2app.controller.sdkApi.PreviewStream;
import com.icatch.wcm2app.controller.sdkApi.SDKSession;
import com.icatch.wificam.customer.ICatchWificamPreview;
import com.icatch.wificam.customer.exception.IchBufferTooSmallException;
import com.icatch.wificam.customer.exception.IchCameraModeException;
import com.icatch.wificam.customer.exception.IchInvalidArgumentException;
import com.icatch.wificam.customer.exception.IchInvalidSessionException;
import com.icatch.wificam.customer.exception.IchSocketException;
import com.icatch.wificam.customer.exception.IchStreamNotRunningException;
import com.icatch.wificam.customer.exception.IchTryAgainException;
import com.icatch.wificam.customer.exception.IchVideoStreamClosedException;
import com.icatch.wificam.customer.type.ICatchFrameBuffer;

/**
 * Added by zhangyanhu C01012,2014-7-15
 */
public class Preview extends SurfaceView implements SurfaceHolder.Callback {

	private int frameWidth;
	private int frameHeight;
	private byte[] pixelBuf;
	private ByteBuffer bmpBuf;
	private Rect drawFrameRect;
	private Bitmap videoFrameBitmap;
	//private Context context;
	private AudioTrack audioTrack;
	private PreviewStream previewStream = new PreviewStream();
	private SurfaceHolder holder;
	private VideoThread mySurfaceViewThread;
	private boolean hasSurface;
	private AudioThread audioThread;
	private boolean hasInit = false;
	private ICatchWificamPreview previewStreamControl;
	

	public Preview(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	private void init() {
		frameWidth = previewStream.getVideoWidth();
		frameHeight = previewStream.getVideoHeigth();
		Log.d("1111", "start frameWidth = "+frameWidth);
		Log.d("1111", "start frameHeight = "+frameHeight);
		// The 'bmpBuf' container is used to get data from server
		pixelBuf = new byte[frameWidth * frameHeight * 4];
		bmpBuf = ByteBuffer.wrap(pixelBuf);

		// Trigger onDraw with those initialize parameters
		videoFrameBitmap = Bitmap.createBitmap(frameWidth, frameHeight, Config.ARGB_8888);
		drawFrameRect = new Rect(0, 0, frameWidth, frameHeight);
		// 创建一个新的SurfaceHolder， 并分配这个类作为它的回调(callback)
		holder = this.getHolder();
		holder.addCallback(this);
		holder.setFormat(PixelFormat.RGBA_8888);
		hasSurface = false;
		hasInit = true;
		previewStreamControl = SDKSession.getpreviewStreamClient();
	}

	public void start() {
		WriteLogToDevice.writeLog("[Normal] -- Preview: ", "start preview");
		if(hasInit == false){
			init();
		}
		// 创建和启动图像更新线程
		if (mySurfaceViewThread == null) {
			mySurfaceViewThread = new VideoThread();
			if (hasSurface == true)
				mySurfaceViewThread.start();
		}
		// 启动音频线程
		if(previewStream.supportAudio())
		{
			if(audioThread == null){
				audioThread = new AudioThread();
			}
		}
	}

	public boolean stop() {
		// 杀死图像更新线程
		WriteLogToDevice.writeLog("[Normal] -- Preview: ", "stop preview");
		if (mySurfaceViewThread != null) {
			mySurfaceViewThread.requestExitAndWait();
			mySurfaceViewThread = null;
		}
		// 杀死音频线程
		if (audioThread != null) {
			audioThread.requestExitAndWait();
			audioThread = null;
		}
		return true;
	}

	private class VideoThread extends Thread {
		private boolean done;

		VideoThread() {
			super();
			done = false;
		}

		@Override
		public void run() {
			WriteLogToDevice.writeLog("[Normal] -- Preview: ", "start running preview thread");
			SurfaceHolder surfaceHolder = holder;
			ICatchFrameBuffer buffer = new ICatchFrameBuffer(frameWidth*frameHeight*4);
//			ICatchFrameBuffer buffer = new ICatchFrameBuffer(720*400*4);
			buffer.setBuffer(pixelBuf);
			boolean temp = false;
			while (!done) {
				temp = false;
				try {
					temp = previewStreamControl.getNextVideoFrame(buffer);
				} catch (IchSocketException e) {
					WriteLogToDevice.writeLog("[Error] -- Preview: ", "IchSocketException");
					//need to close preview get next video frame
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
				} catch (IchBufferTooSmallException e) {
					WriteLogToDevice.writeLog("[Error] -- Preview: ", "IchBufferTooSmallException");
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
				} catch (IchCameraModeException e) {
					WriteLogToDevice.writeLog("[Error] -- Preview: ", "IchCameraModeException");
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
				} catch (IchInvalidSessionException e) {
					WriteLogToDevice.writeLog("[Error] -- Preview: ", "IchInvalidSessionException");
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
				} catch (IchTryAgainException e) {
					WriteLogToDevice.writeLog("[Error] -- Preview: ", "IchTryAgainException");
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IchStreamNotRunningException e) {
					WriteLogToDevice.writeLog("[Error] -- Preview: ", "IchStreamNotRunningException");
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
				} catch (IchInvalidArgumentException e) {
					WriteLogToDevice.writeLog("[Error] -- Preview: ", "IchInvalidArgumentException");
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
				} catch (IchVideoStreamClosedException e) {
					WriteLogToDevice.writeLog("[Error] -- Preview: ", "IchVideoStreamClosedException");
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
				}
				//WriteLogToDevice.writeLog("[Normal] -- PreviewStream: ", "end getNextVideoFrame retValue =" + retValue);
				if (temp == false) {
					WriteLogToDevice.writeLog("[Error] -- Preview: ", "if(temp == false)\n");
					continue;
				}
				// //Log.d("tiger1","end getNextFrame.....");
				if (buffer == null) {
					WriteLogToDevice.writeLog("[Error] -- Preview: ", "buffer == null\n");
					Log.e("Preview", "break");
					continue;
				}
				//long temm = buffer.getFrameSize();
				//Log.d("tigertiger", "buffer.getFrameSize() = "+buffer.getFrameSize());
				bmpBuf.rewind();
				videoFrameBitmap.copyPixelsFromBuffer(bmpBuf);
				// 锁定surface，并返回到要绘图的Canvas
				Canvas canvas = surfaceHolder.lockCanvas();
				// // 待实现：在Canvas上绘图
				int w = getWidth();
				int h = getHeight();
				//canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
				drawFrameRect = ScaleTool.getScaledPosition(frameWidth, frameHeight, w, h);
				canvas.drawBitmap(videoFrameBitmap, null, drawFrameRect, null);
				// // 解锁Canvas，并渲染当前图像
				surfaceHolder.unlockCanvasAndPost(canvas);
			}
		}

		public void requestExitAndWait() {
			// 把这个线程标记为完成，并合并到主程序线程
			done = true;
			try {
				join();
			} catch (InterruptedException ex) {
			}
		}

		public void onWindowResize(int w, int h) {
			// 处理可用的屏幕尺寸的改变
			Log.d("tigertiger","onWindowResize..............");
		}
	}

	private class AudioThread extends Thread {
		private boolean done = false;

		public void run() {

			int sampleRateInHz = 44100;
			int minBufferSize = AudioTrack.getMinBufferSize(sampleRateInHz, AudioFormat.CHANNEL_IN_STEREO, AudioFormat.ENCODING_PCM_16BIT);
			audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRateInHz, AudioFormat.CHANNEL_IN_STEREO, AudioFormat.ENCODING_PCM_16BIT,
					minBufferSize, AudioTrack.MODE_STREAM);

			audioTrack.play();

			byte[] audioBuffer = new byte[1024 * 512];
			ICatchFrameBuffer icatchBuffer = new ICatchFrameBuffer();
			icatchBuffer.setBuffer(audioBuffer);
			while (!done) {
				if (false == previewStream.getNextAudioFrame(icatchBuffer)) {
				}
				audioTrack.write(icatchBuffer.getBuffer(), 0, icatchBuffer.getFrameSize());
			}
			Log.d("audioTrack", "quit audio track thread.");
			audioTrack.stop();
			audioTrack.release();
		}

		public void requestExitAndWait() {
			// 把这个线程标记为完成，并合并到主程序线程
			done = true;
			try {
				join();
			} catch (InterruptedException ex) {
			}
		}

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.d("tigertiger", "surfaceCreated");
		hasSurface = true;
		if (mySurfaceViewThread != null) {
			mySurfaceViewThread.start();
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d("tigertiger", "surfaceDestroyed");
		hasSurface = false;
		stop();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		Log.d("tigertiger", "surfaceChanged");
		Log.d("tigertiger", "getWidth() ="+getWidth());
		Log.d("tigertiger", "getHeight() ="+getHeight());
		if(videoFrameBitmap != null){

				// 锁定surface，并返回到要绘图的Canvas
				Canvas canvas = holder.lockCanvas();
				// // 待实现：在Canvas上绘图
				//canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
				drawFrameRect = ScaleTool.getScaledPosition(frameWidth, frameHeight, getWidth(), getHeight());
				canvas.drawBitmap(videoFrameBitmap, null, drawFrameRect, null);
				// // 解锁Canvas，并渲染当前图像
				holder.unlockCanvasAndPost(canvas);
		}
	}
}

