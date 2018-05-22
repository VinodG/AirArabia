package com.winit.airarabia.adapters;

import java.util.ArrayList;
import java.util.Locale;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.winit.airarabia.BaseActivity;
import com.winit.airarabia.R;
import com.winit.airarabia.objects.CountryDO;

public class CountryAdapter extends BaseAdapter {

	private ArrayList<CountryDO> vecCountry;
	private ArrayList<CountryDO> vecCountryTemp;
	private Context con;
	private String countryNameToCompare = "";
    private ListView list;
    private TextView noItem;


	public CountryAdapter(Context ctx,ArrayList<CountryDO> vecAirport2, ListView list, TextView noItem, String selectedItem) 
	{
		this.con = ctx;
		this.vecCountry = vecAirport2;
		this.vecCountryTemp = (ArrayList<CountryDO>) vecAirport2.clone();
		this.list=list;
		this.noItem=noItem;
		this.countryNameToCompare = selectedItem;
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
			viewHolder.imgSeprator = (ImageView) convertView.findViewById(R.id.imgSeprator);
			convertView.setTag(viewHolder);
		}
		else
			viewHolder = (ViewHolder)convertView.getTag();

		convertView.setTag(R.string.add, vecCountryTemp.get(position).CountryName);
		
		viewHolder.tv_listCurrrency.setText(vecCountryTemp.get(position).CountryName);
		if (vecCountryTemp.get(position).CountryName.equalsIgnoreCase(countryNameToCompare)) {
			viewHolder.ivCurrencyChecked.setVisibility(View.VISIBLE);
		}
		else
			viewHolder.ivCurrencyChecked.setVisibility(View.GONE);
		
		if(position == getCount()-1)
			viewHolder.imgSeprator.setVisibility(View.INVISIBLE);
		else
			viewHolder.imgSeprator.setVisibility(View.VISIBLE);
		
		viewHolder.tv_listCurrrency.setTypeface(BaseActivity.typefaceOpenSansSemiBold);
		
		return convertView;
	}

	class ViewHolder {
		TextView tv_listCurrrency;
		ImageView ivCurrencyChecked;
		ImageView imgSeprator;
	}
	
	public void filter(String charText, String HeaderText) {
		charText = charText.toLowerCase(Locale.getDefault());
		vecCountryTemp.clear();
		if (charText.length() == 0) {
			vecCountryTemp.addAll(vecCountry);
		}
		else 
		{
			for (CountryDO wp : vecCountry) 
			{
				if (!wp.equals("") && wp.CountryName.toLowerCase().contains(charText)) 
				{
					vecCountryTemp.add(wp);
				}
			}
		}
		
		if(vecCountryTemp.size()==0){
			list.setVisibility(View.GONE);
			if(HeaderText.equalsIgnoreCase(con.getString(R.string.selectNationality)))
				noItem.setText(con.getString(R.string.noNationality));
			else if(HeaderText.equalsIgnoreCase(con.getString(R.string.selectCountry)))
				noItem.setText(con.getString(R.string.noCountry));
			else
				noItem.setText(con.getString(R.string.noData));
			noItem.setVisibility(View.VISIBLE);
			//listener.noItem();
		}else{
			list.setVisibility(View.VISIBLE);
			noItem.setVisibility(View.GONE);
		}
		notifyDataSetChanged();
	}
	
	public void refresh(ArrayList<CountryDO> arrayListPa)
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
	public ArrayList<CountryDO> getData()
	{
		return vecCountryTemp;
	}
	
	
	
//	public NoItem listener=null;
//	
//	public void setNoItemListener(NoItem listener){
//		this.listener=listener;
//	}
//	
//	
//	public static interface NoItem{
//		public void noItem();
//	}
	
}