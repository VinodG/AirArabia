package com.winit.airarabia.adapters;

import java.util.Vector;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.winit.airarabia.R;
import com.winit.airarabia.objects.OfficeDO;

public class SpinnerofficecityAdapter extends BaseAdapter {

	Context con;
	Vector<OfficeDO> vecofficedeatils;
	public SpinnerofficecityAdapter(Context officeLocation,
			Vector<OfficeDO> vecOfficeDO) 
	{
		this.con = officeLocation;
		this.vecofficedeatils =vecOfficeDO;
	}

	@Override
	public int getCount()
	{

		return vecofficedeatils.size();
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
		TextView tvcountriesinoffloc;
		if(convertView== null)
		{
			convertView = LayoutInflater.from(con).inflate(R.layout.citylistspinner, null);	

		}
		tvcountriesinoffloc = (TextView) convertView.findViewById(R.id.tv_listcityadaptext);

		if (position==0) 
		{
				tvcountriesinoffloc.setText(con.getString(R.string.all));
		}
		else
		{
			tvcountriesinoffloc.setText(vecofficedeatils.get(position).city);	
		}

		return convertView;
	}

}
