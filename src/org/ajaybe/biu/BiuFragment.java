package org.ajaybe.biu;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.AnimationDrawable;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class BiuFragment extends Fragment implements SensorEventListener, OnClickListener {
	private SensorManager mSensorManager;
	private Sensor mSensor;
	private float mDirection;
	
	private ImageView mArrowView;
	private Animation mArrowAnimation;
	private AnimationDrawable mChordAnimation;

	private ProgressDialog mDialog;
	
	private static boolean sBiu;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		BiuFragment.this.getActivity().getActionBar().setTitle("BIU!");
		
		mSensorManager = (SensorManager)getActivity().getSystemService(Context.SENSOR_SERVICE);	
		View view = inflater.inflate(R.layout.fragment_biu, null);
		
		mSensor = (Sensor)mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		
		View bow = view.findViewById(R.id.bow);
		view.setOnClickListener(this);
		
		mArrowView = (ImageView)view.findViewById(R.id.arrow);
		mArrowAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.animation_arrow);
		
		View chord = view.findViewById(R.id.chord);
		mChordAnimation = (AnimationDrawable)chord.getBackground();
		
		mDialog = new ProgressDialog(getActivity());
		mDialog.setCancelable(true);
		mDialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				sBiu = false;
			}
			
		});
		
		sBiu = false;
		
		return view;
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
		mDirection = event.values[0];
		Log.e("BiuFragment", "direction changed: " + mDirection);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onClick(View v) {
		
		mChordAnimation.start();
		mArrowView.postDelayed(new Runnable() {
			@Override
			public void run() {
				mArrowView.startAnimation(mArrowAnimation);
				mDialog.show();
			}
		}, 250);
		
		sBiu = true;
		
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();

		params.put("username", BiuApplication.getUsername());
		params.put("direction", mDirection);
		client.post("http://106.187.100.252/search", params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				if (sBiu == false) {
					return;
				}
				
				mDialog.dismiss();
				if (statusCode == 200) {
					Intent intent = new Intent(getActivity(), BiuActivity.class);
					try {
						int count = 0;
						String[] names = null;
						JSONObject jsonObject = new JSONObject(new String(responseBody, "gb2312"));
						Iterator<String> jsonIter = jsonObject.keys();
						while (jsonIter.hasNext()) {
							String key = jsonIter.next();
							if (key.equals("count")) {
								count = jsonObject.getInt(key);
								if (count != 0) {
									intent.putExtra("count", count);
									names = new String[count];
								}
							} else if (key.equals("code")) {
								int code = jsonObject.getInt(key);
								if (code != 0) {
									Toast.makeText(getActivity(), "ÍøÂç´íÎó", Toast.LENGTH_LONG).show();
								}
							} else if (key.equals("users")) {
								JSONArray jsonArray = jsonObject.getJSONArray("users");
								for (int i = 0; i < count; ++i) {
									JSONObject object = (JSONObject)jsonArray.get(i);
									String name = object.getString("nickname");
									names[i] = name;
								}
								if (count != 0) {
									intent.putExtra("users", names);
								}
							}
						}
						
						if (count == 0) {
							Toast.makeText(getActivity(), "Biu fail", Toast.LENGTH_LONG).show();
						} else {
							startActivity(intent);
						}
					} catch (JSONException e) {
						
					} catch (UnsupportedEncodingException e) {
						
					}
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				mDialog.dismiss();
				Toast.makeText(BiuFragment.this.getActivity(), "ÁªÍø´íÎó", Toast.LENGTH_LONG).show();
			}
			
		});
	}

	public class BowString extends View {

		public BowString(Context context) {
			super(context);
		}
		
		@Override
		protected void onDraw(Canvas canvas) {
			int height = canvas.getHeight();
			int width = canvas.getWidth();
			Paint paint = new Paint();
			paint.setColor(Color.BLACK);
			paint.setStrokeWidth((float) 1.0); 

			canvas.drawLine(0, height / 2, width, height / 2, paint);
		}
		
	}
}



