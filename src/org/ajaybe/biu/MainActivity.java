package org.ajaybe.biu;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost.TabSpec;


public class MainActivity extends FragmentActivity {
	
	//public Menu out_menu;
	
	private FragmentTabHost mTabHost;
	private String texts[] = {"ForumFragment", "FriendFragment", "BiuFragment", "SettingFragment"};
	//private Class fragments[] = {ForumFragment.class, FriendFragment.class, BiuFragment.class, SettingFragment.class};
	private Class fragments[] = {BiuFragment.class, FriendFragment.class, BiuFragment.class, BiuFragment.class};
	private int drawables[] = {R.drawable.tab_forum, R.drawable.tab_fri, R.drawable.tab_biu, R.drawable.tab_set};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
		
		BiuApplication.initThirdParty(getApplicationContext());
	}
	
	private void init() {
		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.maincontent);
		
		for (int i = 0; i < texts.length; i++) {
			TabSpec tabSpec = mTabHost.newTabSpec(texts[i]).setIndicator(getTabItemView(i));
			mTabHost.addTab(tabSpec, fragments[i], null);
			mTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.tab_selector);
		}
	}
	
	private View getTabItemView(int i) {
		View view = View.inflate(MainActivity.this, R.layout.tab_item_view, null);
		
		//ImageView imageView = (ImageView) view.findViewById(R.id.tab_item_image);
		//imageView.setImageResource(drawables[i]);
		
		return view;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		//out_menu = menu;
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
}
