package org.ajaybe.biu;

import java.util.List;

import org.apache.http.Header;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.ajaybe.biu.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class BiuFragment extends Fragment implements SensorEventListener, OnClickListener {
	private SensorManager mSensorManager;
	private Sensor mSensor;
	private float direction;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		
		BiuFragment.this.getActivity().getActionBar().setTitle("Biu!");
		
		
		mSensorManager = (SensorManager)getActivity().getSystemService(Context.SENSOR_SERVICE);		
		View view = inflater.inflate(R.layout.fragment_biu, null);
		
		mSensor = (Sensor)mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		
		return view;
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		direction = event.values[2];
		Log.e("BiuFragment", "direction changed: " + direction);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onClick(View v) {
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		
		params.put("username", BiuApplication.getUsername());
		params.put("direction", direction);
		client.post("http://106.187.100.252/search", params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				Intent intent = new Intent(getActivity(), BiuActivity.class);
				startActivity(intent);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				Toast.makeText(BiuFragment.this.getActivity(), "ÁªÍø´íÎó", Toast.LENGTH_LONG).show();
			}
			
		});
	}

	
}



