package com.winit.airarabia.adapters;

import java.util.ArrayList;
import java.util.Locale;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.winit.airarabia.BaseActivity;
import com.winit.airarabia.R;
import com.winit.airarabia.common.AppConstants;

public class SelectLanguageAdapter extends BaseAdapter {

	private ArrayList<String> vecCurrency;
	private ArrayList<String> arrListCurrencyTemp;
	private Context con;
	private String selectedLanguageFullName="",selectedLang="";
private ListView list;
private TextView tvNotFound;
	public SelectLanguageAdapter(Context ctx,ArrayList<String> vecAirport2) 
	{
		this.con = ctx;
		this.vecCurrency = vecAirport2;
		this.arrListCurrencyTemp = (ArrayList<String>) vecAirport2.clone();
	}
	
	public SelectLanguageAdapter(Context ctx,ArrayList<String> vecAirport2, String selectedLang,ListView list,TextView tvNotFound) 
	{
		this.con = ctx;
		this.vecCurrency = vecAirport2;
		this.arrListCurrencyTemp = (ArrayList<String>) vecAirport2.clone();
		this.selectedLang = selectedLang;
		this.list=list;
		this.tvNotFound=tvNotFound;
		if (selectedLang.equalsIgnoreCase(AppConstants.LANG_EN)) {
			selectedLanguageFullName= con.getString(R.string.english);
		}
		else if (selectedLang.equalsIgnoreCase(AppConstants.LANG_FR)) {
			selectedLanguageFullName= con.getString(R.string.french);
		}
		else if (selectedLang.equalsIgnoreCase(AppConstants.LANG_RU)) {
			selectedLanguageFullName= con.getString(R.string.russion);
		}
		else if (selectedLang.equalsIgnoreCase(AppConstants.LANG_AR)) {
			selectedLanguageFullName= con.getString(R.string.arabic);
		}
	}

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
			viewHolder.imgSeprator = (ImageView) convertView.findViewById(R.id.imgSeprator);
			convertView.setTag(viewHolder);
		}
		else
			viewHolder = (ViewHolder)convertView.getTag();

//		if (selectedLanguageCode.equalsIgnoreCase(AppConstants.LANG_AR)) {
//			selectedLanguage = 3;
//		} else if (selectedLanguageCode.equalsIgnoreCase(AppConstants.LANG_FR)) {
//			selectedLanguage = 2;
//		} else if (selectedLanguageCode.equalsIgnoreCase(AppConstants.LANG_EN)) {
//			selectedLanguage = 1;
//		}
		viewHolder.tv_listCurrrency.setText(arrListCurrencyTemp.get(position).toString());
			if (!TextUtils.isEmpty(selectedLanguageFullName) &&selectedLanguageFullName.equalsIgnoreCase(arrListCurrencyTemp.get(position).toString())) {
				viewHolder.ivCurrencyChecked.setVisibility(View.VISIBLE);
			}
			else
				viewHolder.ivCurrencyChecked.setVisibility(View.GONE);
			
			
//			if(position == getCount()-1)
//				viewHolder.imgSeprator.setVisibility(View.INVISIBLE);
//			else
//				viewHolder.imgSeprator.setVisibility(View.VISIBLE);
				viewHolder.imgSeprator.setVisibility(View.VISIBLE);
				
				viewHolder.tv_listCurrrency.setTypeface(BaseActivity.typefaceOpenSansRegular);
				
		return convertView;
	}

	class ViewHolder {
		TextView tv_listCurrrency;
		ImageView ivCurrencyChecked;
		ImageView imgSeprator;
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
		if(arrListCurrencyTemp.size()==0){
			list.setVisibility(View.GONE);
			tvNotFound.setVisibility(View.VISIBLE);
			tvNotFound.setText(R.string.noLanguageFound);
			//listener.noItem();
		}else{
			list.setVisibility(View.VISIBLE);
			tvNotFound.setVisibility(View.GONE);
			notifyDataSetChanged();
		}
	}
	
	public void refresh(ArrayList<String> arrayListPa)
	{
		if (arrayListPa.size() > 0) {
			arrListCurrencyTemp.clear();
			arrListCurrencyTemp.addAll(arrayListPa);
		}
		notifyDataSetChanged();
	}
	
	public ArrayList<String> getData()
	{
		return arrListCurrencyTemp;
	}
	
}