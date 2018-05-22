package com.winit.airarabia.adapters;

import java.util.Vector;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.winit.airarabia.BaseActivity;
import com.winit.airarabia.R;
import com.winit.airarabia.common.AppConstants;
import com.winit.airarabia.objects.BaggageDO;

public class SpinnerBaggageAdapter extends BaseAdapter {

	private Vector<BaggageDO> vecBaggage;
	private Context con;
	private int pos;

	public SpinnerBaggageAdapter(Context bookFlight,Vector<BaggageDO> vecBaggage2) 
	{
		this.con = bookFlight;
		this.vecBaggage = vecBaggage2;
	}

	@Override
	public int getCount() 
	{
		if(vecBaggage != null && vecBaggage.size() > 0)
			return vecBaggage.size();
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

	public void refreshPos(int position)
	{
		this.pos = position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		ViewHolder viewHolder;
		if(convertView == null)
		{
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(con).inflate(R.layout.baggagelist_spinner_item, null);
			viewHolder.text = (TextView) convertView.findViewById(R.id.tv_list_baggage_adaptext);
			viewHolder.ivTick = (ImageView) convertView.findViewById(R.id.ivTic);
			convertView.setTag(viewHolder);
		}
		else
			viewHolder = (ViewHolder)convertView.getTag();

		if(pos == position)
		{
//			viewHolder.text.setBackgroundColor(con.getResources().getColor(R.color.light_gray));
			viewHolder.ivTick.setVisibility(View.VISIBLE);
		}
		else
		{
			viewHolder.text.setBackgroundColor(con.getResources().getColor(R.color.transparent_color));
			viewHolder.ivTick.setVisibility(View.GONE);
		}
		viewHolder.text.setTypeface(AppConstants.typefaceOpenSansRegular);
		if(vecBaggage.get(position).baggageCode.equalsIgnoreCase("No Bag") || vecBaggage.get(position).baggageCode.equalsIgnoreCase(con.getString(R.string.Select)))
			viewHolder.text.setText(vecBaggage.get(position).baggageCode);
		else
			viewHolder.text.setText(vecBaggage.get(position).baggageCode+"= "+AppConstants.CurrencyCodeAfterExchange+" "+((BaseActivity)con).updateCurrencyByFactor(vecBaggage.get(position).baggageCharge, 0));
		
		if(vecBaggage.get(position).baggageCode.equalsIgnoreCase(con.getString(R.string.Select)))
		{
			viewHolder.text.setTextSize(18);
			viewHolder.ivTick.setVisibility(View.INVISIBLE);
		}
		
//		if (AppConstants.bookingFlightDO.vecBaggageRequestDOs.size() <= position && vecBaggage!=null ) {
//			if (vecBaggage.get(position).baggageCode.equalsIgnoreCase(AppConstants.bookingFlightDO.vecBaggageRequestDOs.get(position).baggageCode) 
//				&& !AppConstants.bookingFlightDO.vecBaggageRequestDOs.get(position).baggageCode.equalsIgnoreCase("select")) {
//				
//			}
//			viewHolder.ivTick.setVisibility(View.VISIBLE);
//		}
		
//		if(vecBaggage.get(position).baggageCode.equalsIgnoreCase("No Bag") || vecBaggage.get(position).baggageCode.equalsIgnoreCase(con.getString(R.string.Select)))
//			viewHolder.text.setText(vecBaggage.get(position).baggageCode);
//		else if (vecBaggage.get(position).baggageCode.contains("30 Kg Economy Class CAI")||vecBaggage.get(position).baggageCode.equalsIgnoreCase("30 Kg Economy Cla...")) {
//			viewHolder.text.setText("30 Kg Economy Cla...");
//			
//		}else
//			viewHolder.text.setText(vecBaggage.get(position).baggageCode+"- "+AppConstants.CurrencyCodeAfterExchange+" "+((BaseActivity)con).updateCurrencyByFactor(vecBaggage.get(position).baggageCharge, 0));
		
		return convertView;
	
	}

	class ViewHolder {
		TextView text;
		ImageView ivTick;
	}
}