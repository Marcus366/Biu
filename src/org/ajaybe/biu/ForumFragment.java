package org.ajaybe.biu;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ForumFragment extends ListFragment {
	
	private SimpleAdapter adapter;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //adapter = new SimpleAdapter(getActivity(), getData(), R.layout.friend_list, new String[] {"portrait","user_name"}, new int[] {R.id.portrait, R.id.user_name});
        //setListAdapter(adapter);
        
        /*
        MainActivity mainActivity = (MainActivity) FriendFragment.this.getActivity();
        MenuItem menuItem = mainActivity.out_menu.findItem(R.id.btn_note);
		menuItem.setTitle("¸ü¸ÄActionBar°´Å¥");
		*/
    }
    /*
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
	*/	
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        ForumFragment.this.getActivity().getActionBar().setTitle("MESSAGES");
        return inflater.inflate(R.layout.fragment_forum, container, false);
    }	

    @Override  
    public void onListItemClick(ListView l, View v, int position, long id) {  
        super.onListItemClick(l, v, position, id);  
    }  
    
}

