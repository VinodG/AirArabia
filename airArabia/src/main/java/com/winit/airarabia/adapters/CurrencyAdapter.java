package com.winit.airarabia.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.winit.airarabia.R;
import com.winit.airarabia.common.AppConstants;
import com.winit.airarabia.objects.CurrencyDo;

public class CurrencyAdapter extends BaseAdapter {

	private ArrayList<CurrencyDo> arrListCurrency;
	private Context con;
	private String selectedLanguageFullName="",selectedLang="";
	private String currencyNameToCompare = "";
	public static Typeface typefaceOpenSansSemiBold;
	private ListView list;
	private TextView noItem;
	public CurrencyAdapter(Context ctx,ArrayList<CurrencyDo> vecAirport2, ListView list, TextView noItem, String selectedCurrency) 
	{
		this.con = ctx;
		this.arrListCurrency = (ArrayList<CurrencyDo>) vecAirport2.clone();
		this.list=list;
		this.noItem=noItem;
		this.currencyNameToCompare = selectedCurrency;
		typefaceOpenSansSemiBold = Typeface.createFromAsset(ctx.getAssets(),"OpenSans-Semibold.ttf");  
	}
	
	public CurrencyAdapter(Context ctx,ArrayList<CurrencyDo> vecAirport2, String selectedLang) 
	{
		this.con = ctx;
		this.arrListCurrency = (ArrayList<CurrencyDo>) vecAirport2.clone();
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
		else if (selectedLang.equalsIgnoreCase(AppConstants.LANG_RU)) {
			selectedLanguageFullName= con.getString(R.string.russion);
		}
	}

	@Override
	public int getCount() 
	{
		if(arrListCurrency != null && arrListCurrency.size() > 0)
			return arrListCurrency.size();
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
		
		
		
		convertView.setTag(R.string.add, arrListCurrency.get(position).code);
		viewHolder.tv_listCurrrency.setTypeface(typefaceOpenSansSemiBold);
//		if (selectedLanguageCode.equalsIgnoreCase(AppConstants.LANG_AR)) {
//			selectedLanguage = 3;
//		} else if (selectedLanguageCode.equalsIgnoreCase(AppConstants.LANG_FR)) {
//			selectedLanguage = 2;
//		} else if (selectedLanguageCode.equalsIgnoreCase(AppConstants.LANG_EN)) {
//			selectedLanguage = 1;
//		}
			viewHolder.tv_listCurrrency.setText(arrListCurrency.get(position).code.toString());
			if (arrListCurrency.get(position).code.equalsIgnoreCase(currencyNameToCompare)) {
				viewHolder.ivCurrencyChecked.setVisibility(View.VISIBLE);
			}
			else
				viewHolder.ivCurrencyChecked.setVisibility(View.GONE);
			
			if(position == getCount()-1)
				viewHolder.imgSeprator.setVisibility(View.INVISIBLE);
			else
				viewHolder.imgSeprator.setVisibility(View.VISIBLE);
			
		return convertView;
	}

	class ViewHolder {
		TextView tv_listCurrrency;
		ImageView ivCurrencyChecked;
		ImageView imgSeprator;
	}
	
//	public void filter(String charText) {
//		synchronized ("currency") 
//		{
//			charText = charText.toLowerCase(Locale.getDefault());
//			arrListCurrencyTemp.clear();
//			if (charText.length() == 0) {
//				arrListCurrencyTemp.addAll(vecCurrency);
//			} 
//			else 
//			{
//				for (CurrencyDo wp : vecCurrency) 
//				{
//					if (!wp.equals("") && wp.code.toLowerCase().contains(charText)) 
//					{
//						arrListCurrencyTemp.add(wp);
//					}
//				}
//			}
//			if(arrListCurrencyTemp.size()==0){
//				list.setVisibility(View.GONE);
//				noItem.setVisibility(View.VISIBLE);
//				//listener.noItem();
//			}else{
//				list.setVisibility(View.VISIBLE);
//				noItem.setVisibility(View.GONE);
//				notifyDataSetChanged();
//				
//			}
//		}
//		
//	}
	
	public void refresh(ArrayList<CurrencyDo> arrayListPa)
	{
		if (arrayListPa.size() > 0) {
			arrListCurrency.clear();
			arrListCurrency.addAll(arrayListPa);
		}
		notifyDataSetChanged();
	}
	
	public void refresh( String countryNameToCompare)
	{
		this.currencyNameToCompare = countryNameToCompare;
		notifyDataSetChanged();
	}
	
	public ArrayList<CurrencyDo> getData()
	{
		if (arrListCurrency.size()>0) {
			return arrListCurrency;
		}
		else
			return new ArrayList<CurrencyDo>();
	}
	
}