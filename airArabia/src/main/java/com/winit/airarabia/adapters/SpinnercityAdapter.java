package com.winit.airarabia.adapters;

import java.util.Vector;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.winit.airarabia.R;
import com.winit.airarabia.objects.CityDO;

public class SpinnercityAdapter extends BaseAdapter {

	Context con;
	Vector<CityDO> vecCity;
	public SpinnercityAdapter(Context callcenters, Vector<CityDO> vecCityDO) 
	{
		this.con = callcenters;
		this.vecCity = vecCityDO;

	}

	@Override
	public int getCount() 
	{
		return vecCity.size();
	}

	@Override
	public Object getItem(int position) 
	{
		return position;
	}

	@Override
	public long getItemId(int position) 
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		TextView citytext;
		if(convertView == null)
		{
			convertView = LayoutInflater.from(con).inflate(R.layout.citylistspinner, null);
		}
		citytext = (TextView) convertView.findViewById(R.id.tv_listcityadaptext);
		if (position==0) 
		{
			citytext.setText(con.getString(R.string.all));
		} else
		{
			citytext.setText(vecCity.get(position).name);
		}


		return convertView;
	}

}
