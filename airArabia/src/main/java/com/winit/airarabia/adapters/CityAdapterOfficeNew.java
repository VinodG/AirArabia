package com.winit.airarabia.adapters;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Vector;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.winit.airarabia.R;
import com.winit.airarabia.objects.CountriesDO;
import com.winit.airarabia.objects.OfficeDO;

public class CityAdapterOfficeNew extends BaseAdapter {

	private ArrayList<OfficeDO> vecCountry;
	private ArrayList<OfficeDO> vecCountryTemp;
	private Context con;
	private String countryNameToCompare = "";
	private CountriesDO countryDO;
	Vector<OfficeDO> vecCityDO;
	private Vector<OfficeDO> vecCityTemp;
	private  ListView list;
	private  TextView tvNoItem;
	
	public CityAdapterOfficeNew(Context ctx,Vector<OfficeDO> vecCityDO, String countryNameToCompare) 
	{
		this.con = ctx;
		this.vecCityDO = vecCityDO;
		this.vecCityTemp = (Vector<OfficeDO>) vecCityDO.clone();
		this.countryNameToCompare=countryNameToCompare;
	}

	public CityAdapterOfficeNew(Context ctx, Vector<OfficeDO> vecCityDO, String countryNameToCompare2, ListView list, TextView tvNoItem) {
		// TODO Auto-generated constructor stub
		this.con = ctx;
		this.vecCityDO = vecCityDO;
		this.vecCityTemp = (Vector<OfficeDO>) vecCityDO.clone();
		this.countryNameToCompare=countryNameToCompare;
		this.list=list;
		this.tvNoItem=tvNoItem;
	}
	

	@Override
	public int getCount() 
	{
		if(vecCityTemp != null && vecCityTemp.size() > 0)
			return vecCityTemp.size();
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
			convertView = LayoutInflater.from(con).inflate(R.layout.currency_list, null);
			viewHolder.tv_listCurrrency = (TextView) convertView.findViewById(R.id.tv_listCurrency);
			viewHolder.ivCurrencyChecked = (ImageView) convertView.findViewById(R.id.ivCurrencyChecked);
			convertView.setTag(viewHolder);
		}
		else
			viewHolder = (ViewHolder)convertView.getTag();

		convertView.setTag(R.string.add, vecCityTemp.get(position).city);
		
		viewHolder.tv_listCurrrency.setText(vecCityTemp.get(position).city);
	
		if (vecCityTemp.get(position).city.equalsIgnoreCase(countryNameToCompare)) {
			viewHolder.ivCurrencyChecked.setVisibility(View.VISIBLE);
		}
		else
			viewHolder.ivCurrencyChecked.setVisibility(View.GONE);
		return convertView;
	}

	class ViewHolder {
		TextView tv_listCurrrency;
		ImageView ivCurrencyChecked;
	}
	
	public void filter(String charText, String HeaderText) {
		charText = charText.toLowerCase(Locale.getDefault());
		vecCityTemp.clear();
		if (charText.length() == 0) {
			vecCityTemp.addAll(vecCityDO);
		} 
		else 
		{
			for (OfficeDO wp : vecCityDO) 
			{
				if (!wp.equals("") && wp.city.toLowerCase().contains(charText)) 
				{
					vecCityTemp.add(wp);
				}
			}
		}
		if(vecCityTemp.size()==0){
			list.setVisibility(View.GONE);
			if(HeaderText.equalsIgnoreCase(con.getString(R.string.selectNationality)))
				tvNoItem.setText(con.getString(R.string.noNationality));
			else if(HeaderText.equalsIgnoreCase(con.getString(R.string.selectCountry)))
				tvNoItem.setText(con.getString(R.string.noCountry));
			else if(HeaderText.equalsIgnoreCase(con.getString(R.string.noCity)))
				tvNoItem.setText(con.getString(R.string.noCity));
			else
				tvNoItem.setText(con.getString(R.string.noData));
			tvNoItem.setVisibility(View.VISIBLE);
			//listener.noItem();
		}else{
			list.setVisibility(View.VISIBLE);
			tvNoItem.setVisibility(View.GONE);
		}
		notifyDataSetChanged();
	}
	
//	public void refresh(ArrayList<CountriesDO> arrayListPa)
//	{
//		if (arrayListPa.size() > 0) {
//			vecCityTemp.clear();
//			vecCityTemp.addAll(arrayListPa);
//		}
//		notifyDataSetChanged();
//	}
	public void refresh( String countryNameToCompare)
	{
		this.countryNameToCompare = countryNameToCompare;
		notifyDataSetChanged();
	}
	public Vector<OfficeDO> getData()
	{
		
		return vecCityTemp;
	}
	
}