package org.ajaybe.biu;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

import android.app.Application;
import android.content.Context;
import android.util.Log;

public class BiuApplication extends Application {
	
	static private String TAG = "BiuApplication";
	
	static private LocationClient sLocationClient;
	
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
			
			/* initialize JPush */
			JPushInterface.setDebugMode(true);
			JPushInterface.init(ctx);
			JPushInterface.setAlias(ctx, "pjb", new TagAliasCallback() {

				@Override
				public void gotResult(int statusCode, String alias, Set<String> tags) {
					if (statusCode == 0) {
						Log.e(TAG, "setAlias OK: " + alias);
					} else {
						Log.e(TAG, "setAlias Fail");
					}
				}
				
			});
		}
	}

	static public void registerListener(BDLocationListener listener) {
		sLocationClient.registerLocationListener(listener);
	}

	static public void unregisterLisntener(BDLocationListener listener) {
		sLocationClient.unRegisterLocationListener(listener);
	}
	
}
