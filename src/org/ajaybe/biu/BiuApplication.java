package org.ajaybe.biu;

import java.util.Set;

import org.apache.http.Header;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class BiuApplication extends Application implements BDLocationListener {
	
	static private String TAG = "BiuApplication";
	
	static private String sUsername;
	
	static private LocationClient sLocationClient;
	static private double sLatitude;
	static private double sLongitude;
	
	private static Handler sHandler;
	
	static public void initThirdParty(Context ctx) {
		if (sLocationClient == null) {
			/* initialize location client option */
			LocationClientOption option = new LocationClientOption();
			option.setLocationMode(LocationMode.Hight_Accuracy);	//设置定位模式
			option.setCoorType("bd09ll");							//返回的定位结果是百度经纬度,默认值gcj02
			option.setScanSpan(5000);								//设置发起定位请求的间隔时间为5000ms
			option.setIsNeedAddress(false);							//返回的定位结果不包含地址信息
			option.setNeedDeviceDirect(true);						//返回的定位结果包含手机机头的方向
			
			/* initialize location client */
			sLocationClient = new LocationClient(ctx);
			sLocationClient.setLocOption(option);
			sLocationClient.registerLocationListener((BiuApplication)ctx.getApplicationContext());
			sLocationClient.start();
			
			
			/* initialize JPush */
			JPushInterface.setDebugMode(true);
			JPushInterface.init(ctx);
			JPushInterface.setAlias(ctx, sUsername, new TagAliasCallback() {

				@Override
				public void gotResult(int statusCode, String alias, Set<String> tags) {
					if (statusCode == 0) {
						Log.e(TAG, "setAlias OK: " + alias);
					} else {
						Log.e(TAG, "setAlias Fail");
					}
				}
				
			});
			
			sHandler = new Handler();
			
			/* heart beat */
			new Thread() {
				
				@Override
				public void run() {
					for (;;) {
						BiuApplication.requestLocation();
						
						/* latitude and longitude zero means uninitlized */
						if (sLatitude != 0 && sLongitude != 0) {
							sHandler.post(new HeartBeat());
						}
						
						try { sleep(5000); } catch (InterruptedException e) {}
					}
				}
			}.start();
		}
	}

	static public void registerListener(BDLocationListener listener) {
		sLocationClient.registerLocationListener(listener);
	}

	static public void unregisterLisntener(BDLocationListener listener) {
		sLocationClient.unRegisterLocationListener(listener);
	}
	
	static public int requestLocation() {
		return sLocationClient.requestLocation();
	}
	
	static public void setUsername(String username) {
		sUsername = username;
	}

	static public String getUsername() {
		return sUsername;
	}
	
	@Override
	public void onReceiveLocation(BDLocation location) {
		if (location == null) {
			// repost request
			requestLocation();
		}
		
		sLatitude = location.getLatitude();
		sLongitude = location.getLongitude();
		Log.e("BiuApplication", "received location: latitude" + sLatitude + " longitude" + sLongitude);
	}
	
	public static class HeartBeat implements Runnable {
		@Override
		public void run() {
			AsyncHttpClient client = new AsyncHttpClient();
			RequestParams params = new RequestParams();
			params.put("username", sUsername);
			params.put("latitude", sLatitude);
			params.put("longitude", sLongitude);
			client.post("http://106.187.100.252/heartbeat", params, new AsyncHttpResponseHandler() {

				@Override
				public void onSuccess(int statusCode,
						Header[] headers, byte[] responseBody) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onFailure(int statusCode,
						Header[] headers, byte[] responseBody,
						Throwable error) {
					// TODO Auto-generated method stub
					
				}
				
			});
		}
	}

	
	
}
