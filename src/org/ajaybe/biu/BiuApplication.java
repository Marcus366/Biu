package org.ajaybe.biu;

import cn.jpush.android.api.JPushInterface;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

import android.app.Application;
import android.content.Context;

public class BiuApplication extends Application {
	static private LocationClient sLocationClient;
	
	static public void initThirdParty(Context ctx) {
		if (sLocationClient == null) {
			/* initialize location client option */
			LocationClientOption option = new LocationClientOption();
			option.setLocationMode(LocationMode.Hight_Accuracy);	//���ö�λģʽ
			option.setCoorType("bd09ll");							//���صĶ�λ����ǰٶȾ�γ��,Ĭ��ֵgcj02
			option.setScanSpan(5000);								//���÷���λ����ļ��ʱ��Ϊ5000ms
			option.setIsNeedAddress(false);							//���صĶ�λ�����������ַ��Ϣ
			option.setNeedDeviceDirect(true);						//���صĶ�λ��������ֻ���ͷ�ķ���
			
			/* initialize location client */
			sLocationClient = new LocationClient(ctx);
			sLocationClient.setLocOption(option);
			
			/* initialize JPush */
			JPushInterface.setDebugMode(true);
			JPushInterface.init(ctx);
		}
	}

	static public void registerListener(BDLocationListener listener) {
		sLocationClient.registerLocationListener(listener);
	}

	static public void unregisterLisntener(BDLocationListener listener) {
		sLocationClient.unRegisterLocationListener(listener);
	}
	
}
