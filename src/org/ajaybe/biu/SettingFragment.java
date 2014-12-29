package org.ajaybe.biu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SettingFragment extends Fragment{
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		SettingFragment.this.getActivity().getActionBar().setTitle("SETTING");
		
		return inflater.inflate(R.layout.fragment_setting, null);
	}
}

