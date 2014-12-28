package org.ajaybe.biu;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.ajaybe.biu.ChatReceiver.IObserver;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class ChatActivity extends Activity implements OnClickListener, IObserver {
	private Button mBtnSend;// ����btn
	private Button mBtnBack;// ����btn
	private EditText mEditTextContent;
	private ListView mListView;	
	private ChatAdapter mAdapter;// ��Ϣ��ͼ��Adapter
	private List<ChatEntity> mDataArrays = new ArrayList<ChatEntity>();// ��Ϣ��������

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);

		initView();// ��ʼ��view

		initData();// ��ʼ������
		mListView.setSelection(mAdapter.getCount() - 1);

		BiuApplication.initThirdParty(getApplicationContext());
	}

	@Override
	protected void onPause() {
		ChatReceiver.unregisterObserver(this);
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		ChatReceiver.registerObserver(this);
		super.onResume();
	}
	
	public void onReceive(ChatEntity entity) {
		mDataArrays.add(entity);
		mAdapter.notifyDataSetChanged();
		mListView.setSelection(mListView.getCount() - 1);
	}
	
	
	/**
	 * ��ʼ��view
	 */
	public void initView() {
		mListView = (ListView) findViewById(R.id.listview);
		mBtnSend = (Button) findViewById(R.id.btn_send);
		mBtnSend.setOnClickListener(this);
		mBtnBack = (Button) findViewById(R.id.btn_back);
		mBtnBack.setOnClickListener(this);
		mEditTextContent = (EditText) findViewById(R.id.et_sendmessage);
	}

	private String[] msgArray = new String[] { "�д���", "�У����أ�", "��Ҳ��", "���ϰ�",
			"�򰡣���Ŵ󰡣�", "��TMզ���Ŵ��أ���������ͷ����CAO�������B", "2B������", "���...",
			"����ȥ���ɰ�ҹ�ɣ�", "��ëƬ��", "����һ��Ѱ�~����ûƬ��", "OK,���𣡣�" };

	private String[] dataArray = new String[] { "2012-09-22 18:00:02",
			"2012-09-22 18:10:22", "2012-09-22 18:11:24",
			"2012-09-22 18:20:23", "2012-09-22 18:30:31",
			"2012-09-22 18:35:37", "2012-09-22 18:40:13",
			"2012-09-22 18:50:26", "2012-09-22 18:52:57",
			"2012-09-22 18:55:11", "2012-09-22 18:56:45",
			"2012-09-22 18:57:33", };
	private final static int COUNT = 12;// ��ʼ����������

	/**
	 * ģ�������Ϣ��ʷ��ʵ�ʿ������Դ����ݿ��ж���
	 */
	public void initData() {
		for (int i = 0; i < COUNT; i++) {
			ChatEntity entity = new ChatEntity();
			entity.setDate(dataArray[i]);
			if (i % 2 == 0) {
				entity.setName("ФB");
				entity.setMsgType(true);// �յ�����Ϣ
			} else {
				entity.setName("�ذ�");
				entity.setMsgType(false);// �Լ����͵���Ϣ
			}
			entity.setMessage(msgArray[i]);
			mDataArrays.add(entity);
		}

		mAdapter = new ChatAdapter(this, mDataArrays);
		mListView.setAdapter(mAdapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_send:// ���Ͱ�ť����¼�
			send();
			break;
		case R.id.btn_back:// ���ذ�ť����¼�
			finish();// ����,ʵ�ʿ����У����Է���������
			break;
		}
	}

	/**
	 * ������Ϣ
	 */
	private void send() {
		String contString = mEditTextContent.getText().toString();
		if (contString.length() > 0) {
			ChatEntity entity = new ChatEntity();
			entity.setName("�ذ�");
			entity.setDate(getDate());
			entity.setMessage(contString);
			entity.setMsgType(false);

			mDataArrays.add(entity);
			mAdapter.notifyDataSetChanged();// ֪ͨListView�������ѷ����ı�

			mEditTextContent.setText("");// ��ձ༭������

			mListView.setSelection(mListView.getCount() - 1);// ����һ����Ϣʱ��ListView��ʾѡ�����һ��
			
			RequestParams params = new RequestParams();
	    	params.put("username", "pjb");
	    	params.put("target", "zzq");
	    	params.put("msg", contString);
			
			AsyncHttpClient client = new AsyncHttpClient();
	    	client.post("http://106.187.100.252/send", params, new AsyncHttpResponseHandler() {

				@Override
				public void onSuccess(int statusCode, Header[] headers,
						byte[] responseBody) {
					Log.e("LOG", "onSuccess(int, Header[], JSONObject) callback was received");
					if (statusCode == 200) {
						
					}
				}

				@Override
				public void onFailure(int statusCode, Header[] headers,
						byte[] responseBody, Throwable error) {
					Log.w("LOG", "onFailure(int, Header[], Throwable, JSONObject) callback was received");
					
				}
	    		
	    	});
		}
	}

	/**
	 * ������Ϣʱ����ȡ��ǰ�¼�
	 * 
	 * @return ��ǰʱ��
	 */
	private String getDate() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		return format.format(new Date());
	}
}
