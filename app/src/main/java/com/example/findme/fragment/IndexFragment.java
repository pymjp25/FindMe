package com.example.findme.fragment;

import com.example.findme.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class IndexFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View indexLayout = inflater.inflate(R.layout.index_layout, container, false);
		return indexLayout;
	}
}
