package org.nunocky.CamViewOverlay;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class AppSettingActivity extends PreferenceActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.appsetting);
	}
}
