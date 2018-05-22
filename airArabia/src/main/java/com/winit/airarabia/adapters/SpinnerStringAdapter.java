package com.winit.airarabia.adapters;

import java.util.Vector;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.winit.airarabia.R;

public class SpinnerStringAdapter extends BaseAdapter {

	Context con;
	Vector<String> vecString;
	public SpinnerStringAdapter(Context callcenters,
			Vector<String> vecString)
	{
		this.con = callcenters;
		this.vecString= vecString;
	}

	@Override
	public int getCount() {
		return vecString.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		TextView countrytext;
		if(convertView == null)
		{
			convertView = LayoutInflater.from(con).inflate(R.layout.citylistspinner, null);
		}
		countrytext = (TextView) convertView.findViewById(R.id.tv_listcityadaptext);
		countrytext.setText(vecString.get(position));
		return convertView;
	}
}
