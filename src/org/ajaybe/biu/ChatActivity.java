package org.ajaybe.biu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.ajaybe.biu.ChatReceiver.IObserver;
import org.apache.http.Header;
import org.apache.http.util.EncodingUtils;
import org.json.JSONException;
import org.json.JSONObject;

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
	private boolean pre_type = false;
	private Menu out_menu;
	private int pre_itemId;
	

	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		Intent intent = getIntent();
		targetname = intent.getStringExtra("title");
		initlocaldata();
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
		
		File fileDir = this.getFilesDir();
		File targetTXT = new File(fileDir, BiuApplication.getUsername() + "+" + targetname + ".txt");
		try {
			if (!targetTXT.exists()) {
				targetTXT.createNewFile();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			FileOutputStream fout = new FileOutputStream(targetTXT, true);
			byte[] enter = "\n".getBytes("UTF-8");
			byte[] bytes = targetname.getBytes("UTF-8");
			fout.write(bytes);
			fout.write(enter);
			bytes = entity.getDate().getBytes("UTF-8");
			fout.write(bytes);
			fout.write(enter);
			bytes = entity.getMessage().getBytes("UTF-8");
			fout.write(bytes);
			fout.write(enter);
		
			fout.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		mDataArrays.add(entity);
		mAdapter.notifyDataSetChanged();
		mListView.setSelection(mListView.getCount() - 1);
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
	
	public void initlocaldata() {
		File fileDir = this.getFilesDir();
		
		File targetTXT = new File(fileDir, BiuApplication.getUsername() + "+" + targetname + ".txt");
		try {
			if (!targetTXT.exists()) {
				targetTXT.createNewFile();
				String[] name = {BiuApplication.getUsername(), targetname};
				
				try {
					FileOutputStream fout = new FileOutputStream(targetTXT);
					byte[] enter = "\n".getBytes("UTF-8");
					for (int i = 0; i < COUNT; ++i) {
						byte[] bytes = name[i % 2].getBytes("UTF-8");
						fout.write(bytes);
						fout.write(enter);
						bytes = dataArray[i].getBytes("UTF-8");
						fout.write(bytes);
						fout.write(enter);
						bytes = msgArray[i].getBytes("UTF-8");
						fout.write(bytes);
						fout.write(enter);
					}
					fout.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

	/**
	 * ģ�������Ϣ��ʷ��ʵ�ʿ������Դ����ݿ��ж���
	 */
	public void initData() {
		
		File fileDir = this.getFilesDir();
		File targetTXT = new File(fileDir, BiuApplication.getUsername() + "+" + targetname + ".txt");
		
		if (targetTXT.exists()) {
			try {
				FileInputStream fis = new FileInputStream(targetTXT);
				try {
					InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
					BufferedReader br = new BufferedReader(isr);
					String line = "";
					String[] res = new String[3];
					int count = 0;
					try {
						while ((line = br.readLine()) != null) {
							res[count] = line;
							++count;
							if (count == 3) {
								ChatEntity entity = new ChatEntity();
								entity.setName(res[0]);
								if (res[0].equals(targetname)) {
									Log.e("LOG", res[0]);
									entity.setMsgType(true);
								}
								else {
									entity.setMsgType(false);
								}
								entity.setDate(res[1]);
								entity.setMessage(res[2]);
								mDataArrays.add(entity);
								count = 0;
							}
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					br.close();
					isr.close();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				fis.close();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			try {
				targetTXT.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		/*
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
			for (int j = 0; j < k; ++j) {
				msgArray[i] += res[j];
				msgArray[i] += "qqq";
			}
				
			entity.setMessage(msgArray[i]);
			mDataArrays.add(entity);
		}
		*/
		/*
		String res = null;
		try {
			FileInputStream fos = new FileInputStream(a);
			int length = fos.available();
			byte[] buffer = new byte[length];
			fos.read(buffer);
			//res = buffer.toString();
			res = EncodingUtils.getString(buffer, "UTF-8");
			Log.e("LOG", res);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
				
		mAdapter = new ChatAdapter(this, mDataArrays);
		mListView.setAdapter(mAdapter);

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

			File fileDir = this.getFilesDir();
			File targetTXT = new File(fileDir, BiuApplication.getUsername() + "+" + targetname + ".txt");
			try {
				if (!targetTXT.exists()) {
					targetTXT.createNewFile();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				FileOutputStream fout = new FileOutputStream(targetTXT, true);
				byte[] enter = "\n".getBytes("UTF-8");
			
				byte[] bytes = BiuApplication.getUsername().getBytes("UTF-8");
				fout.write(bytes);
				fout.write(enter);
				bytes = getDate().getBytes("UTF-8");
				fout.write(bytes);
				fout.write(enter);
				bytes = contString.getBytes("UTF-8");
				fout.write(bytes);
				fout.write(enter);

				fout.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

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
						try {
							Log.e("ChatActivity", new String(responseBody, "gb2312"));
							JSONObject jsonObject = new JSONObject(new String(responseBody, "gb2312"));
							Iterator<String> jsonIter = jsonObject.keys();
							while (jsonIter.hasNext()) {
								String key = jsonIter.next();
								
							}
						} catch (JSONException e) {
							
						} catch (UnsupportedEncodingException e) {
							
						}
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
		out_menu = menu;
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.btn_saveornormal) {
			if (!pre_type) {
				
		        MenuItem menuItem = ChatActivity.this.out_menu.findItem(R.id.btn_saveornormal);
				menuItem.setTitle("����ģʽ");
				pre_type = true;
				ChatActivity.this.setpre_type();
				
				mListView.setOnTouchListener(new OnTouchListener() {

					@Override
					public boolean onTouch(View view, MotionEvent event) {
						int rawX = (int) event.getRawX();
						int rawY = (int) event.getRawY();
						Log.e("LOG", "x" + rawX + " y" + rawY);
						if (rawX > 230) {
							switch(event.getAction()) {
							case MotionEvent.ACTION_DOWN:
								int lListViewPosition = mListView.pointToPosition((int) event.getX(), (int) event.getY());
								int lItemId = (int) mListView.getItemIdAtPosition(lListViewPosition);
								ChatEntity entity = mDataArrays.get(lItemId);
								entity.setSave(false);
								mDataArrays.remove(lItemId);
								mDataArrays.add(lItemId, entity);
								pre_itemId = lItemId;
								mAdapter.notifyDataSetChanged();
								break;
							case MotionEvent.ACTION_MOVE:
								lListViewPosition = mListView.pointToPosition((int) event.getX(), (int) event.getY());
								lItemId = (int) mListView.getItemIdAtPosition(lListViewPosition);
								if (lItemId != pre_itemId) {
									entity = mDataArrays.get(pre_itemId);
									entity.setSave(true);
									mDataArrays.remove(pre_itemId);
									mDataArrays.add(pre_itemId, entity);
									entity = mDataArrays.get(lItemId);
									entity.setSave(false);
									mDataArrays.remove(lItemId);
									mDataArrays.add(lItemId, entity);
									pre_itemId = lItemId;
									mAdapter.notifyDataSetChanged();
								}
								break;
							case MotionEvent.ACTION_UP:
								entity = mDataArrays.get(pre_itemId);
								entity.setSave(true);
								mDataArrays.remove(pre_itemId);
								mDataArrays.add(pre_itemId, entity);
								mAdapter.notifyDataSetChanged();
								break;
							}
							return true;
						}
						else {
							switch(event.getAction()) {
							case MotionEvent.ACTION_DOWN:
								int lListViewPosition = mListView.pointToPosition(230, 70);
								int lItemId = (int) mListView.getItemIdAtPosition(lListViewPosition);
								ChatEntity entity = mDataArrays.get(lItemId);
								entity.setSave(false);
								mDataArrays.remove(lItemId);
								mDataArrays.add(lItemId, entity);
								pre_itemId = lItemId;
								mAdapter.notifyDataSetChanged();
								break;
							case MotionEvent.ACTION_MOVE:
								lListViewPosition = mListView.pointToPosition(230,70);
								lItemId = (int) mListView.getItemIdAtPosition(lListViewPosition);
								if (lItemId != pre_itemId) {
									entity = mDataArrays.get(pre_itemId);
									entity.setSave(true);
									mDataArrays.remove(pre_itemId);
									mDataArrays.add(pre_itemId, entity);
									entity = mDataArrays.get(lItemId);
									entity.setSave(false);
									mDataArrays.remove(lItemId);
									mDataArrays.add(lItemId, entity);
									pre_itemId = lItemId;
									mAdapter.notifyDataSetChanged();
								}
								break;
							case MotionEvent.ACTION_UP:
								entity = mDataArrays.get(pre_itemId);
								entity.setSave(true);
								mDataArrays.remove(pre_itemId);
								mDataArrays.add(pre_itemId, entity);
								mAdapter.notifyDataSetChanged();
								break;
							}
							return false;
						}
					}	
				});
				
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
				
			} else {
		        MenuItem menuItem = ChatActivity.this.out_menu.findItem(R.id.btn_saveornormal);
				menuItem.setTitle("��ȫģʽ");
				pre_type = false;
				ChatActivity.this.setpre_type();
				
				mListView.setOnTouchListener(new OnTouchListener() {
					@Override
					public boolean onTouch(View arg0, MotionEvent arg1) {
						// TODO Auto-generated method stub
						return false;
					}
				});
			}
		}
		return super.onOptionsItemSelected(item);
	}	
	
	public void setpre_type() {
		for (int i = 0; i < mDataArrays.size(); ++i) {
			ChatEntity entity = mDataArrays.get(i);
			if (entity.getSave()) {
				entity.setSave(false);
			} else {
				entity.setSave(true);
			}
			mDataArrays.remove(i);
			mDataArrays.add(i, entity);
			mAdapter.notifyDataSetChanged();
		}
	}
}


