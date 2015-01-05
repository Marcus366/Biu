package org.ajaybe.biu;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;

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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;


public class AddActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.add_confirm) {
			
	    	EditText edittext_target = (EditText) findViewById(R.id.add_target);
	    	String target = edittext_target.getText().toString();
	    	RequestParams params = new RequestParams();
	    	params.put("username", BiuApplication.getUsername());
	    	params.put("target", target);
	    	
	    	AsyncHttpClient client = new AsyncHttpClient();
	    	client.post("http://106.187.100.252/add", params, new AsyncHttpResponseHandler() {

				@Override
				public void onSuccess(int statusCode, Header[] headers,
						byte[] responseBody) {
					Log.e("LOG", "onSuccess(int, Header[], JSONObject) callback was received");
					if (statusCode == 200) {
						try {
							JSONObject jsonObject = new JSONObject(new String(responseBody, "gb2312"));
							Iterator<String> jsonIter = jsonObject.keys();
							while (jsonIter.hasNext()) {
								String key = jsonIter.next();
								if (key.equals("code")) {
									int response = jsonObject.getInt(key);
									if (response == 0) {
										AddActivity.this.finish();
									} else if (response == 1) {
										Toast.makeText(AddActivity.this, "用户名不存在", Toast.LENGTH_LONG).show();
									}
								}
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
					Toast.makeText(AddActivity.this, "联网错误", Toast.LENGTH_LONG).show();
				}
	    		
	    	});
			
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
