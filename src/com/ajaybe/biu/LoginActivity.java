package com.ajaybe.biu;

import org.apache.http.Header;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
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
    	EditText edittext_username = (EditText) findViewById(R.id.username);
    	EditText edittext_password = (EditText) findViewById(R.id.password);
    	String username = edittext_username.getText().toString();
    	String password = edittext_password.getText().toString();
    	RequestParams params = new RequestParams();
    	params.put("username", username);
    	params.put("password", password);
    	
    	AsyncHttpClient client = new AsyncHttpClient();
    	client.post("http://106.187.100.252/login", params, new JsonHttpResponseHandler() {
    		@Override
    		public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
    	        Log.e("LOG", "onSuccess(int, Header[], JSONObject) callback was received");
    	    }
    		
    		@Override
    		public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
    	        Log.w("LOG", "onFailure(int, Header[], Throwable, JSONObject) callback was received", throwable);
    	    }
    	});
    	/*
    	client.post("http://106.187.100.252/login", params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				System.out.println("Success");
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				System.out.println("Fail");				
			}
    	});
    	*/
    }
}
