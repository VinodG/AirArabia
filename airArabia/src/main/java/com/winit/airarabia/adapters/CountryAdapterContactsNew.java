package com.winit.airarabia.adapters;

import java.util.ArrayList;
import java.util.Locale;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.winit.airarabia.BaseActivity;
import com.winit.airarabia.R;
import com.winit.airarabia.objects.CountriesDO;

public class CountryAdapterContactsNew extends BaseAdapter {

	private ArrayList<CountriesDO> vecCountry;
	private ArrayList<CountriesDO> vecCountryTemp;
	private Context con;
	private String countryNameToCompare = "";
	private ListView list;
	private  TextView tvNoItem;
	public static Typeface typefaceOpenSansSemiBold;

	public CountryAdapterContactsNew(Context ctx,ArrayList<CountriesDO> vecAirport2,String countryNameToCompare) 
	{
		this.con = ctx;
		this.vecCountry = vecAirport2;
		this.vecCountryTemp = (ArrayList<CountriesDO>) vecAirport2.clone();
		this.countryNameToCompare=countryNameToCompare;
		typefaceOpenSansSemiBold = Typeface.createFromAsset(ctx.getAssets(),"OpenSans-Semibold.ttf");   
	}
	public CountryAdapterContactsNew(Context ctx,ArrayList<CountriesDO> vecAirport2,String countryNameToCompare, ListView list, TextView tvNoItem) 
	{
		this.con = ctx;
		this.vecCountry = vecAirport2;
		this.vecCountryTemp = (ArrayList<CountriesDO>) vecAirport2.clone();
		this.countryNameToCompare=countryNameToCompare;
		this.list=list;
		this.tvNoItem=tvNoItem;
		typefaceOpenSansSemiBold = Typeface.createFromAsset(ctx.getAssets(),"OpenSans-Semibold.ttf");   
	}

	@Override
	public int getCount() 
	{
		if(vecCountryTemp != null && vecCountryTemp.size() > 0)
			return vecCountryTemp.size();
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

		convertView.setTag(R.string.add, vecCountryTemp.get(position).name);
		
		viewHolder.tv_listCurrrency.setText(vecCountryTemp.get(position).name);
		viewHolder.tv_listCurrrency.setTypeface(typefaceOpenSansSemiBold);
//		if (vecCountryTemp.get(position).name.equalsIgnoreCase(countryNameToCompare)) {
//			viewHolder.ivCurrencyChecked.setVisibility(View.VISIBLE);
//		}
//		else
			viewHolder.ivCurrencyChecked.setVisibility(View.GONE);
		return convertView;
	}

	class ViewHolder {
		TextView tv_listCurrrency;
		ImageView ivCurrencyChecked;
	}
	
	public void filter(String charText, String HeaderText) {
		charText = charText.toLowerCase(Locale.getDefault());
		vecCountryTemp.clear();
		if (charText.length() == 0) {
			vecCountryTemp.addAll(vecCountry);
		} 
		else 
		{
			for (CountriesDO wp : vecCountry) 
			{
				if (!wp.equals("") && wp.name.toLowerCase().contains(charText)) 
				{
					vecCountryTemp.add(wp);
				}
			}
		}
		if(vecCountryTemp.size()==0){
			list.setVisibility(View.GONE);
			if(HeaderText.equalsIgnoreCase(con.getString(R.string.selectNationality)))
				tvNoItem.setText(con.getString(R.string.noNationality));
			else if(HeaderText.equalsIgnoreCase(con.getString(R.string.noCountry)))
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
	
	public void refresh(ArrayList<CountriesDO> arrayListPa)
	{
		if (arrayListPa.size() > 0) {
			vecCountryTemp.clear();
			vecCountryTemp.addAll(arrayListPa);
		}
		notifyDataSetChanged();
	}
	public void refresh( String countryNameToCompare)
	{
		this.countryNameToCompare = countryNameToCompare;
		notifyDataSetChanged();
	}
	public ArrayList<CountriesDO> getData()
	{
		return vecCountryTemp;
	}
	
}