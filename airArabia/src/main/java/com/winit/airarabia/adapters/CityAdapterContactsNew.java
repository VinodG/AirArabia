package com.winit.airarabia.adapters;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Vector;

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
import com.winit.airarabia.objects.CityDO;
import com.winit.airarabia.objects.CountriesDO;

public class CityAdapterContactsNew extends BaseAdapter {

	private ArrayList<CountriesDO> vecCountry;
	private ArrayList<CountriesDO> vecCountryTemp;
	private Context con;
	private String countryNameToCompare = "";
	private CountriesDO countryDO;
	Vector<CityDO> vecCityDO;
	private Vector<CityDO> vecCityTemp;
	private ListView list;
	private TextView tvnoitem;
	public static Typeface typefaceOpenSansSemiBold;
	
	public CityAdapterContactsNew(Context ctx,Vector<CityDO> vecCityDO,String countryNameToCompare ) 
	{
		this.con = ctx;
		this.vecCityDO = vecCityDO;
		this.vecCityTemp = (Vector<CityDO>) vecCityDO.clone();
		this.countryNameToCompare=countryNameToCompare;
		typefaceOpenSansSemiBold = Typeface.createFromAsset(ctx.getAssets(),"OpenSans-Semibold.ttf");
	}
	
	public CityAdapterContactsNew(Context ctx,Vector<CityDO> vecCityDO,String countryNameToCompare ,ListView list, TextView tvnoitem) 
	{
		this.con = ctx;
		this.vecCityDO = vecCityDO;
		this.list = list;
		this.tvnoitem = tvnoitem;
		this.vecCityTemp = (Vector<CityDO>) vecCityDO.clone();
		this.countryNameToCompare=countryNameToCompare;
		typefaceOpenSansSemiBold = Typeface.createFromAsset(ctx.getAssets(),"OpenSans-Semibold.ttf");
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

		convertView.setTag(R.string.add, vecCityTemp.get(position).name);
		
		viewHolder.tv_listCurrrency.setText(vecCityTemp.get(position).name);
		viewHolder.tv_listCurrrency.setTypeface(typefaceOpenSansSemiBold);
//		if (vecCityTemp.get(position).name.equalsIgnoreCase(countryNameToCompare)) {
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
	
	public void filter(String charText,String HeaderText) {
		charText = charText.toLowerCase(Locale.getDefault());
		vecCityTemp.clear();
		if (charText.length() == 0) {
			vecCityTemp.addAll(vecCityDO);
		} 
		else 
		{
			for (CityDO wp : vecCityTemp) 
			{
				if (!wp.equals("") && wp.name.toLowerCase().contains(charText)) 
				{
					vecCityTemp.add(wp);
				}
			}
		}
		if(vecCountryTemp == null){
			list.setVisibility(View.GONE);
			if(HeaderText.equalsIgnoreCase(con.getString(R.string.selectNationality)))
				tvnoitem.setText(con.getString(R.string.noNationality));
			else if(HeaderText.equalsIgnoreCase(con.getString(R.string.noCountry)))
				tvnoitem.setText(con.getString(R.string.noCountry));
			else if(HeaderText.equalsIgnoreCase(con.getString(R.string.noCity)))
				tvnoitem.setText(con.getString(R.string.noCity));
			else
				tvnoitem.setText(con.getString(R.string.noData));
			tvnoitem.setVisibility(View.VISIBLE);
			//listener.noItem();
		}else{
			list.setVisibility(View.VISIBLE);
			tvnoitem.setVisibility(View.GONE);
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
	public Vector<CityDO> getData()
	{
		
		return vecCityTemp;
	}
	
}