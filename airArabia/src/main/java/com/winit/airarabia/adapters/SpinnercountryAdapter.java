package com.winit.airarabia.adapters;

import java.util.Vector;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.winit.airarabia.R;
import com.winit.airarabia.objects.CountriesDO;

public class SpinnercountryAdapter extends BaseAdapter {

	Context con;
	Vector<CountriesDO> vecCountry;
	public SpinnercountryAdapter(Context callcenters,
			Vector<CountriesDO> vecCountryDO)
	{
		this.con = callcenters;
		this.vecCountry= vecCountryDO;
	}

	@Override
	public int getCount() {
		return vecCountry.size();
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
		countrytext.setText(vecCountry.get(position).name);
		return convertView;
	}
}