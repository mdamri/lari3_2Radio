
package com.app.ppt.yousoft.radiotn;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

public class BaseActivity extends Activity {

	private Intent bindIntent;
	private RadioService radioService;


	public static boolean isExitMenuClicked;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		isExitMenuClicked = false;

		// Bind to the service
		bindIntent = new Intent(this, RadioService.class);
		bindService(bindIntent, radioConnection, Context.BIND_AUTO_CREATE);


	}

	@Override
	protected void onResume() {
		super.onResume();
		if (isExitMenuClicked == true)
			finish();
	}







	// Handles the connection between the service and activity
	private final ServiceConnection radioConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			radioService = ((RadioService.RadioBinder) service).getService();
		}

		@Override
		public void onServiceDisconnected(ComponentName className) {
			radioService = null;
		}
	};

}
