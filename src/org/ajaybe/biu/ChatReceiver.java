package org.ajaybe.biu;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import cn.jpush.android.api.JPushInterface;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class ChatReceiver extends BroadcastReceiver {
	
	private static String TAG = "ChatReceiver";
	private static LinkedList<IObserver> mObservers = new LinkedList<IObserver>();

	@Override
	public void onReceive(Context ctx, Intent intent) {
		Bundle bundle = intent.getExtras();
        Log.d(TAG, "onReceive - " + intent.getAction());

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
        }else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
        	
            System.out.println("�յ����Զ�����Ϣ����Ϣ�����ǣ�" + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            // �Զ�����Ϣ����չʾ��֪ͨ������ȫҪ������д����ȥ����
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
        	String title   = bundle.getString(JPushInterface.EXTRA_TITLE);
        	String message = bundle.getString(JPushInterface.EXTRA_ALERT);
        	String extras  = bundle.getString(JPushInterface.EXTRA_EXTRA);
        	String type    = bundle.getString(JPushInterface.EXTRA_CONTENT_TYPE);
        	
        	Log.e("ChatReceiver", title + " " + message + " " + extras + " " + type);
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        	ChatEntity entity = new ChatEntity("ʲô��", sdf.format(new Date()), message, false);
			for (IObserver ob : mObservers) {
				ob.onReceive(entity);
			}
            System.out.println("�յ���֪ͨ");
            // �����������Щͳ�ƣ�������Щ��������
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            System.out.println("�û��������֪ͨ");
            // ����������Լ�д����ȥ�����û���������Ϊ
            Intent i = new Intent(ctx, ChatActivity.class);  //�Զ���򿪵Ľ���
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ctx.startActivity(i);
        } else {
            Log.d(TAG, "Unhandled intent - " + intent.getAction());
        }

	}

	public static void registerObserver(IObserver ob) {
		mObservers.add(ob);
	}
	
	public static void unregisterObserver(IObserver ob) {
		mObservers.remove(ob);
	}
	
	public interface IObserver {
		public void onReceive(ChatEntity entity);
	}
	
}
