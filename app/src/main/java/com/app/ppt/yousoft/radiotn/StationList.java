package com.app.ppt.yousoft.radiotn;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;

import android.widget.TextView;

public class StationList extends BaseActivity implements OnItemClickListener {
	private Handler handler;
	private ListView listView;
	private TextView empty;

	private DrawableListAdapter mAdapter;
	private FrameLayout layout;




	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		handler = new Handler();
		
		new Thread(new Runnable() {
			@Override
			public void run() {				
				handler.post(new Runnable() {
					@Override
					public void run() {
						try {
						setContentView(com.app.ppt.yousoft.radiotn.R.layout.activity_station_list);
						loadList();
						} catch (Exception e) {
							finish();
							e.printStackTrace();
						}
					}
				});
			}
		}).start();
	}

	public void loadList() {
		listView = (ListView) findViewById(com.app.ppt.yousoft.radiotn.R.id.list);

		empty = (TextView) findViewById(com.app.ppt.yousoft.radiotn.R.id.empty);
		layout = (FrameLayout) findViewById(com.app.ppt.yousoft.radiotn.R.id.list_show);
		listView.setOnItemClickListener(this);

		layout.setVisibility(View.GONE);

		new Thread(new Runnable() {
			@Override
			public void run() {
				mAdapter = new DrawableListAdapter(StationList.this);

				handler.post(new Runnable() {
					@Override
					public void run() {
						listView.setAdapter(mAdapter);

						layout.setVisibility(View.VISIBLE);
						if (mAdapter.getCount() == 0) {
							listView.setVisibility(View.GONE);
							empty.setVisibility(View.VISIBLE);
						}
					}
				});
			}
		}).start();
	}

	@Override
	public void onItemClick(AdapterView<?> a, View v, int position, long l) {
		RadioActivity.stationID = position;
		RadioActivity.isStationChanged = true;
		Intent intent = new Intent(this, RadioActivity.class);
		startActivity(intent);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();


		Runtime.getRuntime().gc();
	}


}
