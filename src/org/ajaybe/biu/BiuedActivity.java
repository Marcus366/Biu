package org.ajaybe.biu;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class BiuedActivity extends Activity implements SensorEventListener {
	private SensorManager mSensorManager;
	private Sensor mSensor;
	private float mDirection = 225;

	private String mUsername;
	
	private Button   mConfirmBtn;
	private ImageView mArrowView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_biued);

		mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);	
		mSensor = (Sensor)mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		
		mArrowView = (ImageView)findViewById(R.id.arrow_incline);
		mConfirmBtn = (Button)findViewById(R.id.btn_confirm);
		
		mUsername = getIntent().getStringExtra("username");
		getActionBar().setTitle(mUsername);
		
		mConfirmBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				RequestParams params = new RequestParams();
		    	params.put("username", BiuApplication.getUsername());
		    	params.put("target", mUsername);
		    	
		    	AsyncHttpClient client = new AsyncHttpClient();
		    	client.post("http://106.187.100.252/add", params, new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] responseBody) {

					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] responseBody, Throwable error) {

					}
		    	});
			}
			
		});
		
	}

	@Override
	public void onResume() {
		mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
		super.onResume();
	}
	
	@Override
	public void onPause() {
		mSensorManager.unregisterListener(this);
		super.onPause();
	}
	
	@Override
	public void onSensorChanged(SensorEvent event) {
		float direction = event.values[0];
		mArrowView.setRotation(((mDirection - direction) + 135) % 360);
		mArrowView.invalidate();
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	

}
