package org.ajaybe.biu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ForumFragment extends ListFragment {
	
	private SimpleAdapter adapter;
    String[] cvst_list = new String[10];
    String myname = BiuApplication.getUsername();
    int count = 0;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        adapter = new SimpleAdapter(getActivity(), getData(), R.layout.cvst_list, new String[] {"portrait","user_name", "text"}, new int[] {R.id.cvst_portrait, R.id.cvst_name, R.id.cvst_text});
        setListAdapter(adapter);
        /*
        MainActivity mainActivity = (MainActivity) FriendFragment.this.getActivity();
        MenuItem menuItem = mainActivity.out_menu.findItem(R.id.btn_note);
		menuItem.setTitle("¸ü¸ÄActionBar°´Å¥");
		*/
    }
    
    @Override
    public void onResume() {
    	super.onResume();
        File fileDir = this.getActivity().getFilesDir();
        File[] listFile = fileDir.listFiles();
        for (int i = 0; i < listFile.length; ++i) {
        	if (listFile[i].isFile()) {
        		String t_filename = listFile[i].getName();
        		boolean check = true;
        		for (int j = 0; j < myname.length(); ++j) {
        			if (!(myname.charAt(j) == t_filename.charAt(j))) {
        				check = false;
        				break;
        			}
        		}
        		if (check) {
        			cvst_list[count] = t_filename;
        			++count;
        		}
        	}
        	if (count == 10)
        		break;
        }
        adapter.notifyDataSetChanged();
    }
    
	private List<Map<String, Object>> getData() {
		
		String[] res = new String[3];
		String targetname = "";
		int cnt_res = 0;
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		Map<String, Object> map;
		
		File fileDir = ForumFragment.this.getActivity().getFilesDir();
		for (int i = 0; i < count; ++i) {
			Log.e("LOG", cvst_list[i]);
			for (int j = myname.length() + 1; j < cvst_list[i].length(); ++j) {
				Log.e("LOG", targetname);
				if (cvst_list[i].charAt(j) != '.') {
					targetname += cvst_list[i].charAt(j);
				} else {
					break;
				}
			}
			File targetTXT = new File(fileDir, cvst_list[i]);
			if (targetTXT.exists()) {
				try {
					FileInputStream fis = new FileInputStream(targetTXT);
					try {
						InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
						BufferedReader br = new BufferedReader(isr);
						String line = "";
						try {
							while ((line = br.readLine()) != null) {
								res[cnt_res] = line;
								++cnt_res;
								if (cnt_res == 3) 
									cnt_res = 0;
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
			}
			map = new HashMap<String, Object>();
			map.put("portrait", R.drawable.pt_boy);
			map.put("user_name", targetname);
			map.put("text", res[2]);
			data.add(map);
			targetname = "";
		}
		return data;
	}
	
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        ForumFragment.this.getActivity().getActionBar().setTitle("MESSAGES");
        return inflater.inflate(R.layout.fragment_forum, container, false);
    }	

    @Override      
    public void onListItemClick(ListView l, View v, int position, long id) {  
        super.onListItemClick(l, v, position, id);  
        Intent intent = new Intent(ForumFragment.this.getActivity(), ChatActivity.class);
        HashMap<String, Object> view= (HashMap<String, Object>) l.getItemAtPosition(position);  
        intent.putExtra("title", view.get("user_name").toString());
        ForumFragment.this.getActivity().startActivity(intent);
    }  
    
}

