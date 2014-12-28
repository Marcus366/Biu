package org.ajaybe.biu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ajaybe.biu.R;

public class BiuFragment extends Fragment{
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		BiuFragment.this.getActivity().getActionBar().setTitle("Biu!");
		
		return inflater.inflate(R.layout.fragment_biu, null);
	}
}
