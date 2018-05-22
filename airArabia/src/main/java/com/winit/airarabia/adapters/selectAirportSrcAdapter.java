package com.winit.airarabia.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Vector;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.winit.airarabia.BaseActivity;
import com.winit.airarabia.R;
import com.winit.airarabia.objects.AirportsDO;

public class selectAirportSrcAdapter extends BaseExpandableListAdapter {

	private Vector<AirportsDO> vecAirport;
	private Vector<AirportsDO> vecAirportTempAll;
	private Vector<AirportsDO> vecAirportTempClosAir;
	private Vector<AirportsDO> vecAirportTempRecent;

	private Vector<AirportsDO> vecAirportClosAir;
	private Vector<AirportsDO> vecAirportRecent;
	private ArrayList<String> listDataHeader;
	private HashMap<String, Vector<AirportsDO>> listDataChild;

	private Context context;
	private ExpandableListView list;
	private TextView noItem;

	public selectAirportSrcAdapter(Context bookFlight,HashMap<String, Vector<AirportsDO>> hasmapplace, ExpandableListView list, TextView noItem,ArrayList<String> header) 
	{
		this.context 				= bookFlight;
		this.listDataChild 			= hasmapplace;
		this.vecAirportTempAll 		= (Vector<AirportsDO>) hasmapplace.get("All Airport");
		this.vecAirportTempClosAir	= (Vector<AirportsDO>) hasmapplace.get("Closest Airport");
		this.vecAirportTempRecent	= (Vector<AirportsDO>) hasmapplace.get("Recent Airport");
		this.list					=list;
		this.noItem					=noItem;
		this.listDataHeader			=header;
		vecAirport					=(Vector<AirportsDO>) hasmapplace.get("All Airport").clone();
		this.vecAirportClosAir		= (Vector<AirportsDO>) hasmapplace.get("Closest Airport").clone();
		this.vecAirportRecent		= (Vector<AirportsDO>) hasmapplace.get("Recent Airport").clone();
		
	}

	
	
	public void filter(String charText) {
		charText = charText.toLowerCase(Locale.getDefault());
		vecAirportTempAll.clear();
		vecAirportTempClosAir.clear();
		vecAirportTempRecent.clear();
		
		if (charText.length() == 0) {
			vecAirportTempAll.addAll(vecAirport);
			vecAirportTempClosAir.addAll(vecAirportClosAir);
			vecAirportTempRecent.addAll(vecAirportRecent);
		} 
		else 
		{
			for (AirportsDO wp : vecAirport) 
			{
				if (wp != null && wp.name.toLowerCase().contains(charText)) 
				{
					vecAirportTempAll.add(wp);
				}
			}
//			for (AirportsDO zp : vecAirportClosAir) 
//			{
//				if (zp != null && zp.name.toLowerCase().contains(charText)) 
//				{
//					vecAirportTempClosAir.add(zp);
//				}
//			}
//			for (AirportsDO xp : vecAirportRecent) 
//			{
//				if (xp != null && xp.name.toLowerCase().contains(charText)) 
//				{
//					vecAirportTempRecent.add(xp);
//				}
//			}
		}
		if(vecAirportTempAll.size()==0){
			list.setVisibility(View.GONE);
			noItem.setVisibility(View.VISIBLE);
			//listener.noItem();
		}
		else{
			list.setVisibility(View.VISIBLE);
			noItem.setVisibility(View.GONE);
			notifyDataSetChanged();
			
		}
	}
	
	public void refresh(ArrayList<AirportsDO> arrayListPa)
	{
		if (arrayListPa.size() > 0) {
			vecAirportTempAll.clear();
			vecAirportTempAll.addAll(arrayListPa);
		}
		notifyDataSetChanged();
	}
	
	public Vector<AirportsDO> getAllData()
	{
		return vecAirportTempAll;
	}
	public Vector<AirportsDO> getcloseAirData()
	{
		return vecAirportTempClosAir;
	}
	public Vector<AirportsDO> getRecentData()
	{
		return vecAirportTempRecent;
	}

	@Override
	public int getGroupCount() {
		if(listDataHeader != null)
			return this.listDataHeader.size();
		else
			return 0;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		if(listDataChild != null)
			return this.listDataChild.get(this.listDataHeader.get(groupPosition)).size();
		else
			return 0;
	}

	@Override
	public Object getGroup(int groupPosition) {
		if(listDataHeader !=null)
			return this.listDataHeader.get(groupPosition);
		else
			return null;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		 return this.listDataChild.get(this.listDataHeader.get(groupPosition)).get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		       return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

		ExpandableListView mExpandableListView = (ExpandableListView) parent;
		mExpandableListView.expandGroup(groupPosition);
		
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.airportlistspinner_header, null);
        }
 
        TextView lblListHeader = (TextView) convertView.findViewById(R.id.tv_Header);
        lblListHeader.setText(headerTitle);
        lblListHeader.setTypeface(BaseActivity.typefaceOpenSansSemiBold);
        
        if(groupPosition == 0)
        {
        	lblListHeader.setText(R.string.closest_Airport);
        	//        	lblListHeader.setText(headerTitle);
        }
        else if(groupPosition ==1)
        {
        	lblListHeader.setText(R.string.recent_Airport);
        }
        else if(groupPosition == 2)
        {
        	lblListHeader.setText(R.string.all_Airport);
        }
        
        if(getChildrenCount(groupPosition) == 0)
        	lblListHeader.setVisibility(View.GONE);
        else
        	lblListHeader.setVisibility(View.VISIBLE);

        return convertView;
    
	}


	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
			ViewGroup parent) {

        final AirportsDO objAirportsDO = (AirportsDO) getChild(groupPosition, childPosition);
        ViewHolder viewHolder;
//        viewHolder = new ViewHolder();
//        if(convertView== null)
//		{
//			convertView = LayoutInflater.from(context).inflate(R.layout.airportlistspinner_new, null);
//			convertView.setTag(viewHolder);
//
//		}
//        else
//			viewHolder = (ViewHolder)convertView.getTag();
//        
//        viewHolder.text = (TextView) convertView.findViewById(R.id.tv_listairportadaptext);
//		viewHolder.textCountry = (TextView) convertView.findViewById(R.id.tv_listairportadaptextCountry);
//		
		
		if(convertView == null)
		{
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.airportlistspinner_new, null);
			viewHolder.text = (TextView) convertView.findViewById(R.id.tv_listairportadaptext);
			viewHolder.textCountry = (TextView) convertView.findViewById(R.id.tv_listairportadaptextCountry);
			viewHolder.imgSep = (ImageView) convertView.findViewById(R.id.imgSep);
			convertView.setTag(viewHolder);
		}
		else
			viewHolder = (ViewHolder)convertView.getTag();
        
			viewHolder.text.setText(objAirportsDO.name);
			viewHolder.textCountry.setText(objAirportsDO.countryname);
			
			viewHolder.text.setTypeface(BaseActivity.typefaceOpenSansSemiBold);
			viewHolder.textCountry.setTypeface(BaseActivity.typefaceOpenSansSemiBold);
			
			if (childPosition < getChildrenCount(groupPosition)-1) {
				viewHolder.imgSep.setVisibility(View.VISIBLE);
			}
			
			if (childPosition == getChildrenCount(groupPosition)-1) {
				viewHolder.imgSep.setVisibility(View.INVISIBLE);
			}
			
		return convertView;
        
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}
	
	class ViewHolder {
		TextView text, textCountry;
		ImageView imgSep;
	}
}