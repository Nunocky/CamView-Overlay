package org.nunocky.CamViewOverlay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {

	private CamView mCamView;
	private Overlay mOverlay;
	private ImageProcess mIP;
	private Thread mThread;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		mOverlay = new Overlay(this);
		mIP = new ImageProcess(mOverlay);
		mCamView = new CamView(this, mIP);

		setContentView(mCamView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		addContentView(mOverlay, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

		mCamView.setFocusable(true);
	}

	@Override
	protected void onResume() { 
		super.onResume();
//		mHandler = new RepaintHandler();
		mThread = new Thread(mIP);
		mThread.start();
	}

	@Override
	protected void onPause() {
		mThread = null;
//		mHandler = null;
		super.onPause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, 0, Menu.NONE, "Preference");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean ret = true;
		switch (item.getItemId()) {
		case 0:
			Intent intent = new Intent(this, AppSettingActivity.class);
			startActivity(intent);
			ret = true;        	
			break;
		default:
			ret = super.onOptionsItemSelected(item);
			break;
		}
		return ret;
	}

}