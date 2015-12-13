package com.app.ppt.yousoft.radiotn;

import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class RadioActivity extends BaseActivity {



	private Button playButton;
	private Button pauseButton;
	private Button stopButton;
	private Button nextButton;
	private Button previousButton;
	private ImageView stationImageView;
	public ImageButton shut;
	public  ImageButton dead;


	private TextView titleTextView;
	private TextView statusTextView;
	private TextView timeTextView;

	private Intent bindIntent;
	private TelephonyManager telephonyManager;
	private boolean wasPlayingBeforePhoneCall = false;
	private RadioUpdateReceiver radioUpdateReceiver;
	public static RadioService radioService;


	private String STATUS_BUFFERING;
	private static final String TYPE_AAC = "aac";
	private static final String TYPE_MP3 = "mp3";
	private Handler handler;
	private AudioManager leftAm;

	public static int stationID = 0;
	public static boolean isStationChanged = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(com.app.ppt.yousoft.radiotn.R.layout.activity_radio);
		dead=(ImageButton)findViewById(com.app.ppt.yousoft.radiotn.R.id.detai);

		dead.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {


				Intent i = new Intent(RadioActivity.this, DetailsActivity.class);
				startActivity(i);

			}
		});

		shut = (ImageButton) this.findViewById(com.app.ppt.yousoft.radiotn.R.id.quit);



		shut.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				String title = "Quitter";
				String message = "Etes vous sure de vouloir quitter Radio-TN?";
				String buttonYesString = "Oui";
				String buttonNoString = "Non";


				isExitMenuClicked = true;

				AlertDialog.Builder ad = new AlertDialog.Builder(RadioActivity.this);
				ad.setTitle(title);
				ad.setMessage(message);
				ad.setCancelable(true);
				ad.setPositiveButton(buttonYesString,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {

								if (radioService != null) {
									radioService.exitNotification();
									radioService.stop();
									radioService.stopService(bindIntent);
									isExitMenuClicked = true;
									finish();
								}
							}
						});

				ad.setNegativeButton(buttonNoString, null);

				ad.show();



			}
		});

		// Bind to the service
		try {
			bindIntent = new Intent(this, RadioService.class);
			bindService(bindIntent, radioConnection, Context.BIND_AUTO_CREATE);
		} catch (Exception e) {

		}

		telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		if (telephonyManager != null) {
			telephonyManager.listen(phoneStateListener,
					PhoneStateListener.LISTEN_CALL_STATE);
		}

		handler = new Handler();
		initialize();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT
				|| newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

			try {
				setContentView(com.app.ppt.yousoft.radiotn.R.layout.activity_radio);
				handler.post(new Runnable() {
					@Override
					public void run() {
						initialize();

						if (radioService.getTotalStationNumber() <= 1) {
							nextButton.setEnabled(false);
							nextButton.setVisibility(View.INVISIBLE);
							previousButton.setEnabled(false);
							previousButton.setVisibility(View.INVISIBLE);
						}

						updateStatus();

					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void initialize() {
		try {



			STATUS_BUFFERING = getResources().getString(
					com.app.ppt.yousoft.radiotn.R.string.status_buffering);

			playButton = (Button) this.findViewById(com.app.ppt.yousoft.radiotn.R.id.PlayButton);
			pauseButton = (Button) this.findViewById(com.app.ppt.yousoft.radiotn.R.id.PauseButton);
			stopButton = (Button) this.findViewById(com.app.ppt.yousoft.radiotn.R.id.StopButton);
			nextButton = (Button) this.findViewById(com.app.ppt.yousoft.radiotn.R.id.NextButton);
			previousButton = (Button) this.findViewById(com.app.ppt.yousoft.radiotn.R.id.PreviousButton);
			pauseButton.setEnabled(false);
			pauseButton.setVisibility(View.INVISIBLE);
			stationImageView = (ImageView) findViewById(com.app.ppt.yousoft.radiotn.R.id.stationImageView);

			playButton.setEnabled(true);
			stopButton.setEnabled(false);

			titleTextView = (TextView) this.findViewById(com.app.ppt.yousoft.radiotn.R.id.titleTextView);

			statusTextView = (TextView) this.findViewById(com.app.ppt.yousoft.radiotn.R.id.statusTextView);
			timeTextView = (TextView) this.findViewById(com.app.ppt.yousoft.radiotn.R.id.timeTextView);









			startService(new Intent(this, RadioService.class));



		} catch (Exception e) {
			e.printStackTrace();
		}
	}






	public void updatePlayTimer() {
		timeTextView.setText(radioService.getPlayingTime());

		final Handler handler = new Handler();
		Timer timer = new Timer();
		TimerTask doAsynchronousTask = new TimerTask() {
			@Override
			public void run() {
				handler.post(new Runnable() {
					@Override
					public void run() {
						timeTextView.setText(radioService.getPlayingTime());
					}
				});
			}
		};
		timer.schedule(doAsynchronousTask, 0, 1000);
	}

	public void onClickPlayButton(View view) {
		radioService.play();
	}

	public void onClickPauseButton(View view) {
		radioService.pause();
	}

	public void onClickStopButton(View view) {
		radioService.stop();

		updateDefaultCoverImage();
	}

	public void onClickNextButton(View view) {

		playNextStation();
		updateDefaultCoverImage();
	}

	public void onClickPreviousButton(View view) {

		playPreviousStation();
		updateDefaultCoverImage();
	}

	public void playNextStation() {
		radioService.stop();
		radioService.setNextStation();

	}

	public void playPreviousStation() {
		radioService.stop();
		radioService.setPreviousStation();
	}

	public void updateDefaultCoverImage() {
		try {
			String mDrawableName = "station_"
					+ (radioService.getCurrentStationID() + 1);
			int resID = getResources().getIdentifier(mDrawableName, "drawable",
					getPackageName());
	
			int resID_default = getResources().getIdentifier("station_default",
					"drawable", getPackageName());
	
			if (resID > 0)
				stationImageView.setImageResource(resID);
			else
				stationImageView.setImageResource(resID_default);
	

		} catch(Exception e) {
			e.printStackTrace();
		}
	}







	@Override
	protected void onDestroy() {

		super.onDestroy();

		if (radioService != null) {
			if (!radioService.isPlaying() && !radioService.isPreparingStarted()) {

				radioService.stopService(bindIntent);
			}
		}



		if (telephonyManager != null) {
			telephonyManager.listen(phoneStateListener,
					PhoneStateListener.LISTEN_NONE);
		}

		unbindDrawables(findViewById(com.app.ppt.yousoft.radiotn.R.id.RootView));
		Runtime.getRuntime().gc();
	}

	private void unbindDrawables(View view) {
		try {
			if (view.getBackground() != null) {
				view.getBackground().setCallback(null);
			}
			if (view instanceof ViewGroup) {
				for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
					unbindDrawables(((ViewGroup) view).getChildAt(i));
				}
				((ViewGroup) view).removeAllViews();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (radioUpdateReceiver != null)
			unregisterReceiver(radioUpdateReceiver);

	}

	@Override
	protected void onResume() {
		super.onResume();


		if (radioUpdateReceiver == null)
			radioUpdateReceiver = new RadioUpdateReceiver();
		registerReceiver(radioUpdateReceiver, new IntentFilter(
				RadioService.MODE_CREATED));
		registerReceiver(radioUpdateReceiver, new IntentFilter(
				RadioService.MODE_DESTROYED));
		registerReceiver(radioUpdateReceiver, new IntentFilter(
				RadioService.MODE_STARTED));
		registerReceiver(radioUpdateReceiver, new IntentFilter(
				RadioService.MODE_CONNECTING));
		registerReceiver(radioUpdateReceiver, new IntentFilter(
				RadioService.MODE_START_PREPARING));
		registerReceiver(radioUpdateReceiver, new IntentFilter(
				RadioService.MODE_PREPARED));
		registerReceiver(radioUpdateReceiver, new IntentFilter(
				RadioService.MODE_PLAYING));
		registerReceiver(radioUpdateReceiver, new IntentFilter(
				RadioService.MODE_PAUSED));
		registerReceiver(radioUpdateReceiver, new IntentFilter(
				RadioService.MODE_STOPPED));
		registerReceiver(radioUpdateReceiver, new IntentFilter(
				RadioService.MODE_COMPLETED));
		registerReceiver(radioUpdateReceiver, new IntentFilter(
				RadioService.MODE_ERROR));
		registerReceiver(radioUpdateReceiver, new IntentFilter(
				RadioService.MODE_BUFFERING_START));
		registerReceiver(radioUpdateReceiver, new IntentFilter(
				RadioService.MODE_BUFFERING_END));
		registerReceiver(radioUpdateReceiver, new IntentFilter(
				RadioService.MODE_METADATA_UPDATED));


		if (wasPlayingBeforePhoneCall) {
			radioService.play();
			wasPlayingBeforePhoneCall = false;
		}

		if (radioService != null) {
			if (isStationChanged) {
				if (stationID != radioService.getCurrentStationID()) {
					radioService.stop();
					radioService.setCurrentStationID(stationID);

					updateDefaultCoverImage();
				}
				if (!radioService.isPlaying())
					radioService.play();
				isStationChanged = false;
			}
		}
	}

	/* Receive Broadcast Messages from RadioService */
	private class RadioUpdateReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {

			if (intent.getAction().equals(RadioService.MODE_CREATED)) {

			} else if (intent.getAction().equals(RadioService.MODE_DESTROYED)) {
				playButton.setEnabled(true);
				pauseButton.setEnabled(false);
				stopButton.setEnabled(false);
				playButton.setVisibility(View.VISIBLE);
				pauseButton.setVisibility(View.INVISIBLE);
				updateDefaultCoverImage();

				updateStatus();
			} else if (intent.getAction().equals(RadioService.MODE_STARTED)) {
				pauseButton.setEnabled(false);
				playButton.setVisibility(View.VISIBLE);
				pauseButton.setVisibility(View.INVISIBLE);

				playButton.setEnabled(true);
				stopButton.setEnabled(false);
				updateStatus();
			} else if (intent.getAction().equals(RadioService.MODE_CONNECTING)) {
				pauseButton.setEnabled(false);
				playButton.setVisibility(View.VISIBLE);
				pauseButton.setVisibility(View.INVISIBLE);
				playButton.setEnabled(false);
				stopButton.setEnabled(true);
				updateStatus();
			} else if (intent.getAction().equals(
					RadioService.MODE_START_PREPARING)) {
				pauseButton.setEnabled(false);
				playButton.setVisibility(View.VISIBLE);
				pauseButton.setVisibility(View.INVISIBLE);
				playButton.setEnabled(false);
				stopButton.setEnabled(true);
				updateStatus();
			} else if (intent.getAction().equals(RadioService.MODE_PREPARED)) {
				playButton.setEnabled(true);
				pauseButton.setEnabled(false);
				stopButton.setEnabled(false);
				playButton.setVisibility(View.VISIBLE);
				pauseButton.setVisibility(View.INVISIBLE);
				updateStatus();
			} else if (intent.getAction().equals(
					RadioService.MODE_BUFFERING_START)) {
				updateStatus();
			} else if (intent.getAction().equals(
					RadioService.MODE_BUFFERING_END)) {
				updateStatus();
			} else if (intent.getAction().equals(RadioService.MODE_PLAYING)) {
				if (radioService.getCurrentStationType().equals(TYPE_AAC)) {
					playButton.setEnabled(false);
					stopButton.setEnabled(true);
				} else {
					playButton.setEnabled(false);
					pauseButton.setEnabled(true);
					stopButton.setEnabled(true);
					playButton.setVisibility(View.INVISIBLE);
					pauseButton.setVisibility(View.VISIBLE);
				}
				updateStatus();
			} else if (intent.getAction().equals(RadioService.MODE_PAUSED)) {
				playButton.setEnabled(true);
				pauseButton.setEnabled(false);
				stopButton.setEnabled(true);
				playButton.setVisibility(View.VISIBLE);
				pauseButton.setVisibility(View.INVISIBLE);
				updateStatus();
			} else if (intent.getAction().equals(RadioService.MODE_STOPPED)) {
				playButton.setEnabled(true);
				pauseButton.setEnabled(false);
				stopButton.setEnabled(false);
				playButton.setVisibility(View.VISIBLE);
				pauseButton.setVisibility(View.INVISIBLE);
				updateStatus();
			} else if (intent.getAction().equals(RadioService.MODE_COMPLETED)) {
				playButton.setEnabled(true);
				pauseButton.setEnabled(false);
				stopButton.setEnabled(false);
				playButton.setVisibility(View.VISIBLE);
				pauseButton.setVisibility(View.INVISIBLE);

				updateStatus();
			} else if (intent.getAction().equals(RadioService.MODE_ERROR)) {
				playButton.setEnabled(true);
				pauseButton.setEnabled(false);
				stopButton.setEnabled(false);
				playButton.setVisibility(View.VISIBLE);
				pauseButton.setVisibility(View.INVISIBLE);
				updateStatus();
			}
		}
	}

	PhoneStateListener phoneStateListener = new PhoneStateListener() {
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			if (state == TelephonyManager.CALL_STATE_RINGING) {
				wasPlayingBeforePhoneCall = radioService.isPlaying();
				radioService.stop();
			} else if (state == TelephonyManager.CALL_STATE_IDLE) {
				if (wasPlayingBeforePhoneCall) {
					radioService.play();
				}
			} else if (state == TelephonyManager.CALL_STATE_OFFHOOK) {

				wasPlayingBeforePhoneCall = radioService.isPlaying();
				radioService.stop();
			}
			super.onCallStateChanged(state, incomingNumber);
		}
	};

	public void updateStatus() {
		String status = radioService.getStatus();
		if (radioService.getTotalStationNumber() > 1) {

			String title = radioService.getCurrentStationName();
			try {
				titleTextView.setText(title);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		try {
			statusTextView.setText(status);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Handles the connection between the service and activity
	private final ServiceConnection radioConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			radioService = ((RadioService.RadioBinder) service).getService();

			if (radioService.getTotalStationNumber() <= 1) {
				nextButton.setEnabled(false);
				nextButton.setVisibility(View.INVISIBLE);
				previousButton.setEnabled(false);
				previousButton.setVisibility(View.INVISIBLE);
			}

			updateStatus();

			updatePlayTimer();
			radioService.showNotification();


		}

		@Override
		public void onServiceDisconnected(ComponentName className) {
			radioService = null;
		}
	};

}