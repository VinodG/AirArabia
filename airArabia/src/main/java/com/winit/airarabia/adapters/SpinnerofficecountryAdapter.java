package com.winit.airarabia.adapters;

import java.util.Vector;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.winit.airarabia.R;
import com.winit.airarabia.objects.OfficeLocationDO;

public class SpinnerofficecountryAdapter extends BaseAdapter{

	Context con;
	Vector<OfficeLocationDO> vecofficeloc;
	public SpinnerofficecountryAdapter(Context officeLocation,
			Vector<OfficeLocationDO> vecOfficeLocationDO) 
	{
		this.con = officeLocation;
		this.vecofficeloc = vecOfficeLocationDO;
	}

	@Override
	public int getCount() 
	{
		return vecofficeloc.size();
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
		tvcountriesinoffloc.setText(vecofficeloc.get(position).country);
		return convertView;
	}

}
