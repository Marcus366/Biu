package org.ajaybe.biu;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ajaybe.biu.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;


public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    public void Login(View view) {
    	
    	Intent intent = new Intent(this, MainActivity.class);
    	this.startActivity(intent);
		
		/*
    	EditText edittext_username = (EditText) findViewById(R.id.username);
    	EditText edittext_password = (EditText) findViewById(R.id.password);
    	String username = edittext_username.getText().toString();
    	String password = edittext_password.getText().toString();
    	RequestParams params = new RequestParams();
    	params.put("username", username);
    	params.put("password", password);
    	
    	AsyncHttpClient client = new AsyncHttpClient();
    	client.post("http://106.187.100.252/login", params, new AsyncHttpResponseHandler() {

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
									Intent intent = new Intent(LoginActivity.this, MainActivity.class);
									LoginActivity.this.startActivity(intent);
									LoginActivity.this.finish();
								} else if (response == 1) {
									Toast.makeText(LoginActivity.this, "用户名不存在", Toast.LENGTH_LONG).show();
								} else {
									Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_LONG).show();
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
				Toast.makeText(LoginActivity.this, "联网错误", Toast.LENGTH_LONG).show();
			}
    		
    	});
    	*/
    }
}
