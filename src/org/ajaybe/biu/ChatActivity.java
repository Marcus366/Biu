package org.ajaybe.biu;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.ajaybe.biu.ChatReceiver.IObserver;
import org.apache.http.Header;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class ChatActivity extends Activity implements OnClickListener, IObserver {
	private Button mBtnSend;// ����btn
	private EditText mEditTextContent;
	private ListView mListView;	
	private ChatAdapter mAdapter;// ��Ϣ��ͼ��Adapter
	private ArrayList<ChatEntity> mDataArrays = new ArrayList<ChatEntity>();// ��Ϣ��������
	private String targetname;
	

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		
		Intent intent = getIntent();
		targetname = intent.getStringExtra("title");

		initView();// ��ʼ��view

		initData();// ��ʼ������
		mListView.setSelection(mAdapter.getCount() - 1);

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
		entity.setName(targetname);
		mDataArrays.add(entity);
		mAdapter.notifyDataSetChanged();
		mListView.setSelection(mListView.getCount() - 1);
	}
	
	
	/**
	 * ��ʼ��view
	 */
	public void initView() {
		mListView = (ListView) findViewById(R.id.listviewview);
		mBtnSend = (Button) findViewById(R.id.btn_send);
		mBtnSend.setOnClickListener(this);
		mEditTextContent = (EditText) findViewById(R.id.et_sendmessage);
	}

	private String[] msgArray = new String[] { "�д���", "�У����أ�", "��Ҳ��", "���ϰ�",
			"�򰡣���Ŵ󰡣�", "��TMզ���Ŵ��أ���������ͷ����CAO�������B", "2B������", "���...",
			"����ȥ���ɰ�ҹ�ɣ�", "��ëƬ��", "����һ��Ѱ�~����ûƬ��", "OK,���𣡣�" };

	private String[] dataArray = new String[] { "2014-12-22 18:00:02",
			"2014-12-22 18:10:22", "2014-12-22 18:11:24",
			"2014-12-22 18:20:23", "2014-12-22 18:30:31",
			"2014-12-22 18:35:37", "2014-12-22 18:40:13",
			"2014-12-22 18:50:26", "2014-12-22 18:52:57",
			"2014-12-22 18:55:11", "2014-12-22 18:56:45",
			"2014-12-22 18:57:33", };
	private final static int COUNT = 12;// ��ʼ����������

	/**
	 * ģ�������Ϣ��ʷ��ʵ�ʿ������Դ����ݿ��ж���
	 */
	public void initData() {
		for (int i = 0; i < COUNT; i++) {
			ChatEntity entity = new ChatEntity();
			entity.setDate(dataArray[i]);
			if (i % 2 == 0) {
				entity.setName(targetname);
				entity.setMsgType(true);// �յ�����Ϣ
			} else {
				entity.setName(BiuApplication.getUsername());
				entity.setMsgType(false);// �Լ����͵���Ϣ
			}
			entity.setMessage(msgArray[i]);
			mDataArrays.add(entity);
		}

		mAdapter = new ChatAdapter(this, mDataArrays);
		mListView.setAdapter(mAdapter);
		/*
		mListView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View view, MotionEvent event) {
				// TODO Auto-generated method stub
				switch(event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						Log.e("LOG", "an");
						break;
					case MotionEvent.ACTION_UP:
						Log.e("LOG", "qi");
						break;
				}
				int lListViewPosition = mListView.pointToPosition((int) event.getX(), (int) event.getY());
				int lItemId = (int) mListView.getItemIdAtPosition(lListViewPosition);
				ChatEntity entity = mDataArrays.get(lItemId);
				Log.e("LOG", entity.getMessage());
				int x = (int) event.getX();
				int y = (int) event.getY();
				int rawX = (int) event.getRawX();
				int rawY = (int) event.getRawY();
				Log.e("homer", "x = " + x + "; y = " + y + "; rawX = " + rawX + "; rawY = " + rawY);
				
				//return true;
				if (rawX > 230) {
					return true;
				} else {
					return false;
				}
			}

			
		});
		*/
		/*
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				ChatEntity entity = mDataArrays.get((int) id);
				Log.e("LOG", entity.getMessage());
				Toast.makeText(ChatActivity.this, "�û���������", Toast.LENGTH_LONG).show();
				entity.setSave(false);
				mDataArrays.remove((int) id);
				mDataArrays.add((int) id, entity);
				entity = mDataArrays.get((int) id);
				Log.e("LOG", entity.getMessage());
				mAdapter.notifyDataSetChanged();
			}
			
		});
		*/
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_send:// ���Ͱ�ť����¼�
			send();
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
			entity.setName(BiuApplication.getUsername());
			entity.setDate(getDate());
			entity.setMessage(contString);
			entity.setMsgType(false);

			mDataArrays.add(entity);
			mAdapter.notifyDataSetChanged();// ֪ͨListView�������ѷ����ı�

			mEditTextContent.setText("");// ��ձ༭������

			mListView.setSelection(mListView.getCount() - 1);// ����һ����Ϣʱ��ListView��ʾѡ�����һ��
			
			RequestParams params = new RequestParams();
	    	params.put("username", BiuApplication.getUsername());
	    	params.put("target", targetname);
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chat, menu);
		ChatActivity.this.getActionBar().setTitle(targetname);
		//out_menu = menu;
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		return super.onOptionsItemSelected(item);
	}	
	
}


