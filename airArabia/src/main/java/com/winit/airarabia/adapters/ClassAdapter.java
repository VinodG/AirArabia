package com.winit.airarabia.adapters;

import java.util.ArrayList;
import java.util.Locale;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.winit.airarabia.R;
import com.winit.airarabia.common.AppConstants;

public class ClassAdapter extends BaseAdapter {

	private ArrayList<String> vecCurrency;
	private ArrayList<String> arrListCurrencyTemp;
	private Context con;
	private String selectedLanguageFullName="",selectedLang="";
	private String currencyNameToCompare = "";

	public ClassAdapter(Context ctx,ArrayList<String> vecAirport2, String classNameToCompare) 
	{
		this.con = ctx;
		this.vecCurrency = vecAirport2;
		this.arrListCurrencyTemp = (ArrayList<String>) vecAirport2.clone();
		this.currencyNameToCompare = classNameToCompare;
	}
	
	/*public ClassAdapter(Context ctx,ArrayList<String> vecAirport2, String selectedLang) 
	{
		this.con = ctx;
		this.vecCurrency = vecAirport2;
		this.arrListCurrencyTemp = (ArrayList<String>) vecAirport2.clone();
		this.selectedLang = selectedLang;
		if (selectedLang.equalsIgnoreCase(AppConstants.LANG_EN)) {
			selectedLanguageFullName= con.getString(R.string.english);
		}
		else if (selectedLang.equalsIgnoreCase(AppConstants.LANG_FR)) {
			selectedLanguageFullName= con.getString(R.string.french);
		}
		else if (selectedLang.equalsIgnoreCase(AppConstants.LANG_AR)) {
			selectedLanguageFullName= con.getString(R.string.arabic);
		}
	}*/

	@Override
	public int getCount() 
	{
		if(arrListCurrencyTemp != null && arrListCurrencyTemp.size() > 0)
			return arrListCurrencyTemp.size();
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
		
		viewHolder.tv_listCurrrency.setTypeface(AppConstants.typefaceOpenSansSemiBold);
		convertView.setTag(R.string.add, arrListCurrencyTemp.get(position));

		/*if (selectedLanguageCode.equalsIgnoreCase(AppConstants.LANG_AR)) {
			selectedLanguage = 3;
		} else if (selectedLanguageCode.equalsIgnoreCase(AppConstants.LANG_FR)) {
			selectedLanguage = 2;
		} else if (selectedLanguageCode.equalsIgnoreCase(AppConstants.LANG_EN)) {
			selectedLanguage = 1;
		}*/
		try{
			viewHolder.tv_listCurrrency.setText(arrListCurrencyTemp.get(position).toString());
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
			if (arrListCurrencyTemp.get(position).equalsIgnoreCase(currencyNameToCompare)) {
				viewHolder.ivCurrencyChecked.setVisibility(View.GONE);
			}
			else
				viewHolder.ivCurrencyChecked.setVisibility(View.GONE);
		return convertView;
	}

	class ViewHolder {
		TextView tv_listCurrrency;
		ImageView ivCurrencyChecked;
	}
	
	public void filter(String charText) {
		
		charText = charText.toLowerCase(Locale.getDefault());
		arrListCurrencyTemp.clear();
		if (charText.length() == 0) {
			arrListCurrencyTemp.addAll(vecCurrency);
		} 
		else 
		{
			for (String wp : vecCurrency) 
			{
				if (!wp.equals("") && wp.toLowerCase().contains(charText)) 
				{
					arrListCurrencyTemp.add(wp);
				}
			}
		}
		notifyDataSetChanged();
	}
	
	public void refresh(ArrayList<String> arrayListPa)
	{
		if (arrayListPa.size() > 0) {
			arrListCurrencyTemp.clear();
			arrListCurrencyTemp.addAll(arrayListPa);
		}
		if (vecCurrency.size()<=0) {
			vecCurrency.clear();
			vecCurrency.addAll(arrayListPa);
		}
		notifyDataSetChanged();
	}
	
	public void refresh(String countryNameToCompare)
	{
		this.currencyNameToCompare = countryNameToCompare;
		notifyDataSetChanged();
	}
	
	public ArrayList<String> getData()
	{
		return arrListCurrencyTemp;
	}
	
}