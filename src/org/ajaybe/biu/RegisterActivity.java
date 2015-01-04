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

public class RegisterActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.re_confirm) {
	    	EditText edittext_username = (EditText) findViewById(R.id.re_username);
	    	EditText edittext_password = (EditText) findViewById(R.id.re_password);
	    	final String re_username = edittext_username.getText().toString();
	    	final String re_password = edittext_password.getText().toString();
	    	RequestParams params = new RequestParams();
	    	params.put("username", re_username);
	    	params.put("nickname", "whatGhost");
	    	params.put("password", re_password);
	    	
	    	AsyncHttpClient client = new AsyncHttpClient();
	    	client.post("http://106.187.100.252/register", params, new AsyncHttpResponseHandler() {

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
										BiuApplication.setUsername(re_username);
										/*
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
										*/
										
										
										
								    	RequestParams paramslogin = new RequestParams();
								    	paramslogin.put("username", re_username);
								    	paramslogin.put("password", re_password);
								    	
								    	AsyncHttpClient clientlogin = new AsyncHttpClient();
								    	clientlogin.post("http://106.187.100.252/login", paramslogin, new AsyncHttpResponseHandler() {

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
																	File fileDir = RegisterActivity.this.getFilesDir();
																	File user_pass = new File(fileDir, "user_pass.txt");
																	try {
																		if (!user_pass.exists()) {
																			user_pass.createNewFile();
																		}
																			
																		try {
																			FileOutputStream fout = new FileOutputStream(user_pass);
																			byte[] outputdata = (re_username + "\n" + re_password).getBytes("UTF-8");
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
												Toast.makeText(RegisterActivity.this, "联网错误", Toast.LENGTH_LONG).show();
											}
								    		
								    	});
										
										Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
										RegisterActivity.this.startActivity(intent);
										RegisterActivity.this.finish();
									} else if (response == 1) {
										Toast.makeText(RegisterActivity.this, "用户名已存在", Toast.LENGTH_LONG).show();
									} else {
										Toast.makeText(RegisterActivity.this, "未知错误", Toast.LENGTH_LONG).show();
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
					Toast.makeText(RegisterActivity.this, "联网错误", Toast.LENGTH_LONG).show();
				}
	    		
	    	});
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
