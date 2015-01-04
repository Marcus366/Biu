package org.ajaybe.biu;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class FriendFragment extends ListFragment {
	
	//getMenuInflater().inflate(R.menu.main, menu);
	
	private SimpleAdapter adapter;
	public String TAG = FriendFragment.class.getName();
	public Map<String, Object> map = new HashMap<String, Object>();
	private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
	private int cnt_fri;
	//private int[] fri_pt = {R.drawable.pt_girl, R.drawable.pt_boy, R.drawable.pt_0, R.drawable.pt_1, R.drawable.pt_2, R.drawable.pt_3, R.drawable.pt_4};
	private String[] fri = new String[10];
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        adapter = new SimpleAdapter(getActivity(), getData(), R.layout.friend_list, new String[] {"portrait","user_name"}, new int[] {R.id.portrait, R.id.user_name});
        setListAdapter(adapter);
        
        
        /*
        MainActivity mainActivity = (MainActivity) FriendFragment.this.getActivity();
        MenuItem menuItem = mainActivity.out_menu.findItem(R.id.btn_note);
		menuItem.setTitle("更改ActionBar按钮");
		*/
    }
    
	private List<Map<String, Object>> getData() {
    	RequestParams params = new RequestParams();
    	params.put("username", BiuApplication.getUsername());
    	
    	AsyncHttpClient client = new AsyncHttpClient();
    	client.post("http://106.187.100.252/friends", params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				Log.e("LOG", "onSuccess(int, Header[], JSONObject) callback was received");
				if (statusCode == 200) {
					try {
						Log.e("LOG", new String(responseBody, "gb2312"));
						JSONObject jsonObject = new JSONObject(new String(responseBody, "gb2312"));
						Iterator<String> jsonIter = jsonObject.keys();
						while (jsonIter.hasNext()) {
							String key = jsonIter.next();
							if (key.equals("count")) {
								cnt_fri = jsonObject.getInt(key);
							}
							if (key.equals("users")) {
								JSONArray jsonArray = jsonObject.getJSONArray(key);
								for (int i = 0; i < cnt_fri; ++i) {
									JSONObject tempobject = jsonArray.getJSONObject(i);
									fri[i] = tempobject.getString("nickname");
								}
							}
						}
						
						for (int i = 0; i < cnt_fri; ++i) {
							
							map = new HashMap<String, Object>();
							map.put("portrait", R.drawable.pt_boy);
							map.put("user_name", fri[i]);
							data.add(map);
						}
						((SimpleAdapter) FriendFragment.this.getListAdapter()).notifyDataSetChanged();
					} catch (JSONException e) {
						
					} catch (UnsupportedEncodingException e) {
						
					}
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				Log.w("LOG", "onFailure(int, Header[], Throwable, JSONObject) callback was received");
				Toast.makeText(FriendFragment.this.getActivity(), "联网错误", Toast.LENGTH_LONG).show();
			}
    	});        
    	
		return data;
	}	
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_friend, container, false);
        FriendFragment.this.getActivity().getActionBar().setTitle("FRIENDS");
        return v;
    }	

    @Override  
    public void onListItemClick(ListView l, View v, int position, long id) {  
        super.onListItemClick(l, v, position, id);  
        Intent intent = new Intent(FriendFragment.this.getActivity(), ChatActivity.class);
        HashMap<String, Object> view= (HashMap<String, Object>) l.getItemAtPosition(position);  

		intent.putExtra("title", view.get("user_name").toString());
		FriendFragment.this.getActivity().startActivity(intent);
          
          

    }  
    
}
