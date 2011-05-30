package org.nunocky.CamViewOverlay;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;

public class ImageProcess implements Camera.PreviewCallback, Runnable{
	@SuppressWarnings("unused")
	private static final String TAG = "CamViewOverlay";

	private Overlay mOverlay;

	private Object dataLock = new Object();

	private byte[] mData;
	private int mWidth, mHeight, mFormat;

	public ImageProcess(Overlay ov) {
		mOverlay = ov;
	}

	@Override
	public void onPreviewFrame(byte[] data, Camera camera) {
		synchronized(dataLock) {
			if (mData == null) {
				// [MEMO] mData == null -> スレッドが処理可能
				mData = data;
				Parameters params = camera.getParameters();
				mFormat = params.getPreviewFormat();
				mWidth  = params.getPreviewSize().width;
				mHeight = params.getPreviewSize().height;
			}
		}
	}

	
	@Override
	public void run() {
		byte[] rawdata;
		int w, h, fmt;
		Paint paint = new Paint();
		boolean done = false;
		while (!done && !Thread.currentThread().isInterrupted()) {
			//Log.i(TAG, "image thread");
			synchronized(dataLock) {
				rawdata = mData;
				w       = mWidth;
				h       = mHeight;
				fmt     = mFormat;
			}
			
			if(w*h <= 0 || rawdata == null) {
				try {
					Thread.sleep(1000/30);
				} catch (InterruptedException e) {
					done = true;
				}
				continue;
			}

			// [MEMO] ここで rawdata, w, h, fmtをもとに何かしらの処理を行う。
			//        この後 Bitmapを生成して Overlayに渡しているが、Bitmap
			//        生成処理は Overlayに任せて、必要なデータだけを Overlayへ
			//        渡すようにしても良い
			Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(bmp);
			
	        canvas.drawColor(Color.TRANSPARENT);
	        paint.setStyle(Paint.Style.STROKE);
	        paint.setStrokeWidth(4);
	        paint.setColor(Color.RED);
	        canvas.drawRect(new Rect(10, 10, w-20, h-20), paint);
			
			mOverlay.setBitmap(bmp);

			// [MEMO] 一枚分の処理が終わったら次のフレームを受け付ける
			synchronized(dataLock) {
				mData    = null;
			}

		}
	}

	

}
