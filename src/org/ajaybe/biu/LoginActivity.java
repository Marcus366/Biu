package org.ajaybe.biu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;


public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    	EditText edittext_username = (EditText) findViewById(R.id.username);
    	EditText edittext_password = (EditText) findViewById(R.id.password);
    	String name = null;
    	String word = null;
    	File fileDir = this.getFilesDir();
		File user_pass = new File(fileDir, "user_pass.txt");
		if (user_pass.exists()) {
			try {
				FileInputStream fis = new FileInputStream(user_pass);
				try {
					InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
					BufferedReader br = new BufferedReader(isr);
					String line = "";
					int count = 0;
					try {
						while ((line = br.readLine()) != null) {
							if (count == 0) {
								name = line;
								++count;
							}
							else
								word = line;
						}	} catch (IOException e) {
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
		}
		if (name != null && word != null) {
			edittext_username.setText(name);
			edittext_password.setText(word);
		}
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
        return super.onOptionsItemSelected(item);
    }
    
    public void Register(View view) {
    	Intent intent = new Intent(this, RegisterActivity.class);
    	this.startActivity(intent);
    	this.finish();    	
    }
    
    public void Login(View view) {
    	
    	/*
    	Intent intent = new Intent(this, MainActivity.class);
    	this.startActivity(intent);
    	this.finish();
    	*/
		
		
    	EditText edittext_username = (EditText) findViewById(R.id.username);
    	EditText edittext_password = (EditText) findViewById(R.id.password);
    	final String username = edittext_username.getText().toString();
    	final String password = edittext_password.getText().toString();
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
									BiuApplication.setUsername(username);

									//Intent intent = new Intent(LoginActivity.this, BiuedActivity.class);
									
									File fileDir = LoginActivity.this.getFilesDir();
									File user_pass = new File(fileDir, "user_pass.txt");
									try {
										if (!user_pass.exists()) {
											user_pass.createNewFile();
										}
											
										try {
											FileOutputStream fout = new FileOutputStream(user_pass);
											byte[] outputdata = (username + "\n" + password).getBytes("UTF-8");
											fout.write(outputdata);
											fout.close();
										} catch (FileNotFoundException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										} catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									
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
    	
    }
}
