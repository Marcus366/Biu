package org.ajaybe.biu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class FriendFragment extends ListFragment {
	
	//getMenuInflater().inflate(R.menu.main, menu);
	
	private SimpleAdapter adapter;
	private String TAG = FriendFragment.class.getName();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        adapter = new SimpleAdapter(getActivity(), getData(), R.layout.friend_list, new String[] {"portrait","user_name"}, new int[] {R.id.portrait, R.id.user_name});
        setListAdapter(adapter);
        
        /*
        MainActivity mainActivity = (MainActivity) FriendFragment.this.getActivity();
        MenuItem menuItem = mainActivity.out_menu.findItem(R.id.btn_note);
		menuItem.setTitle("¸ü¸ÄActionBar°´Å¥");
		*/
    }
    
	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("portrait", R.drawable.pt_boy);
		map.put("user_name", "ZengZhanPeng");
		data.add(map);
		
		map = new HashMap<String, Object>();
		map.put("portrait", R.drawable.pt_girl);
		map.put("user_name", "LiYuZu");
		data.add(map);
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
          
        System.out.println(l.getChildAt(position));  
        HashMap<String, Object> view= (HashMap<String, Object>) l.getItemAtPosition(position);  
        System.out.println(view.get("user_name").toString()+"+++++++++title");  
        
        Toast.makeText(getActivity(), TAG+l.getItemIdAtPosition(position), Toast.LENGTH_LONG).show();  
        System.out.println(v);  
          
        System.out.println(position);  
        
        String str = view.get("user_name").toString();
        if (str == "fuck") {
        	Intent intent = new Intent(FriendFragment.this.getActivity(), LoginActivity.class);
        	FriendFragment.this.getActivity().startActivity(intent);
        }
          
          
    }  
    
}
