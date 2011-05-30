package org.nunocky.CamViewOverlay;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CamView extends SurfaceView implements SurfaceHolder.Callback  {

	private static final String TAG = "CamViewOverlay";
	private SurfaceHolder mHolder;
	private Camera  mCamera;
	private ImageProcess mIP;

	public CamView(Context context, ImageProcess ip) {
		super(context);
		mHolder = getHolder();
		mHolder.addCallback(this);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		mIP = ip;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		Log.i(TAG, "::surfaceChanged");
		mCamera.stopPreview();  
		mCamera.setPreviewCallback(mIP);  
		mCamera.startPreview(); 
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.i(TAG, "CamView::surfaceCreated");
		try {  
			mCamera = Camera.open();  
			mCamera.setPreviewDisplay(mHolder);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}  		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.i(TAG, "CamView::surfaceDestroyed");
		mCamera.stopPreview();  
		mCamera.setPreviewCallback(null);  
		mCamera.release();  
		mCamera = null;  
	}

}
