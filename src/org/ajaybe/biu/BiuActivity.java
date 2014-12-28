package org.ajaybe.biu;

import java.util.List;

import org.ajaybe.biu.R;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class BiuActivity extends Activity implements SensorEventListener {
	private SensorManager mSensorManager;
	private Sensor        mMagnetic;
	private Sensor        mOrientation;

	private TextView      mText;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biu);
        
        mText = (TextView)findViewById(R.id.test);
        
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        for (Sensor sensor : sensors) {
        	Log.e("Sensor", sensor.getName());
        }
        mOrientation = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        mMagnetic = mSensorManager.getDefaultSensor(Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR);
        
        mText.setText("what");
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	    mSensorManager.registerListener(this, mOrientation, SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onPause() {
	   super.onPause();
	   mSensorManager.unregisterListener(this);
	}
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuary) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		float azimuth_angle = event.values[0];
	    float pitch_angle = event.values[1];
	    float roll_angle = event.values[2];
	    
	    Log.e("Sensor Changed", "x:" + pitch_angle + " y:" + roll_angle + " z:" + azimuth_angle);
	    mText.setText("x:" + pitch_angle + " y:" + roll_angle + " z:" + azimuth_angle);
	}
}
