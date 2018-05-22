package com.winit.airarabia.adapters;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Vector;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.winit.airarabia.R;
import com.winit.airarabia.objects.AirportsDestDO;

public class SpinnerairportDestAdapter extends BaseAdapter {

	private Vector<AirportsDestDO> vecAirport;
	private Context con;
	Vector<AirportsDestDO> vecAirportTemp;
	private ListView list;
    private TextView noItem;
	public SpinnerairportDestAdapter(Context bookFlight,Vector<AirportsDestDO> vecAirport2, ListView list, TextView noItem) 
	{
		this.con = bookFlight;
		this.vecAirport = vecAirport2;
		vecAirportTemp = (Vector<AirportsDestDO>) vecAirport2.clone();
		this.list=list;
		this.noItem=noItem;
	}

	@Override
	public int getCount() 
	{
		if(vecAirportTemp != null && vecAirportTemp.size() > 0)
			return vecAirportTemp.size();
		else
			return 0;
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
		ViewHolder viewHolder;
		if(convertView == null)
		{
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(con).inflate(R.layout.airportlistspinner, null);
			viewHolder.text = (TextView) convertView.findViewById(R.id.tv_listairportadaptext);
			viewHolder.textCountry = (TextView) convertView.findViewById(R.id.tv_listairportadaptextCountry);
			convertView.setTag(viewHolder);
		}
		else
			viewHolder = (ViewHolder)convertView.getTag();
		
		if(vecAirportTemp.get(position) != null )
		{
			viewHolder.text.setText(vecAirportTemp.get(position).name);
			viewHolder.textCountry.setText(vecAirportTemp.get(position).countryname);
		}
		return convertView;
	}

	class ViewHolder {
		TextView text, textCountry;
	}
	public void filter(String charText) {
		charText = charText.toLowerCase(Locale.getDefault());
		vecAirportTemp.clear();
		if (charText.length() == 0) {
			vecAirportTemp.addAll(vecAirport);
		} 
		else 
		{
			for (AirportsDestDO wp : vecAirport) 
			{
				if (wp != null && wp.name.toLowerCase().contains(charText)) 
				{
					vecAirportTemp.add(wp);
				}
			}
		}
		if(vecAirportTemp.size()==0){
			list.setVisibility(View.GONE);
			noItem.setVisibility(View.VISIBLE);
			//listener.noItem();
		}else{
			list.setVisibility(View.VISIBLE);
			noItem.setVisibility(View.GONE);
			notifyDataSetChanged();
			
		}
	}
	
	public void refresh(ArrayList<AirportsDestDO> arrayListPa)
	{
		if (arrayListPa.size() > 0) {
			vecAirportTemp.clear();
			vecAirportTemp.addAll(arrayListPa);
		}
		notifyDataSetChanged();
	}
	
	public Vector<AirportsDestDO> getData()
	{
		return vecAirportTemp;
	}
}