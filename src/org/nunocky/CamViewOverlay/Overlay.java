package org.nunocky.CamViewOverlay;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.util.Log;
import android.view.View;

public class Overlay extends View {
	private static final String TAG = "CamViewOverlay";

	private Timer   mTimer   = new Timer(false);
	private Handler mHandler = new Handler();

	private Bitmap mBitmap;
	private Bitmap mBitmap_bak = null;

	private Object mBitmapLock = new Object();

	public Overlay(Context context) {
		super(context);
		Log.d(TAG, "Overlay");
	}

	@Override
	public void onDraw(Canvas canvas) {
		Paint paint = new Paint();

		canvas.drawColor(Color.TRANSPARENT,  PorterDuff.Mode.CLEAR);

		if (mBitmap != null) {
			// mBitmapのサイズを画面に合わせている
			canvas.scale((float)getWidth()/mBitmap.getWidth(), (float)getHeight()/mBitmap.getHeight());
			canvas.drawBitmap(mBitmap, 0, 0, paint);
		}
		
		synchronized(mBitmapLock) {
			mBitmap_bak = null;
		}
	}
	
	/**
	 * 
	 * @param bmp
	 */
	public void setBitmap(Bitmap bmp) {
		// 別のスレッドから非同期に呼ばれるので排他処理が必要
		synchronized(mBitmapLock) {
			if (mBitmap_bak == null) {

				mBitmap_bak  = bmp;

				// メインスレッドに対して再描画をリクエストする
				mTimer.schedule(new TimerTask() {
					@Override
					public void run() {
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								update();
							}
						});
					}}, 0);
			}
		}
	}

	/**
	 * 
	 */
	public void update() {
		synchronized(mBitmapLock) {
			if (mBitmap_bak != null) {
				mBitmap = mBitmap_bak;
				invalidate();
			}
		}
	}

}
