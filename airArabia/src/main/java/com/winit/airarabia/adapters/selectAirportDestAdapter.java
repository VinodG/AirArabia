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

import com.google.android.gms.wearable.NodeApi.GetLocalNodeResult;
import com.winit.airarabia.BaseActivity;
import com.winit.airarabia.R;
import com.winit.airarabia.objects.AirportsDestDO;

public class selectAirportDestAdapter extends BaseExpandableListAdapter {

	private Vector<AirportsDestDO> vecAirport;
	private Vector<AirportsDestDO> vecAirportTempAll;
	private Vector<AirportsDestDO> vecAirportTempClosAir;
	private Vector<AirportsDestDO> vecAirportTempRecent;
	private Vector<AirportsDestDO> vecAirportClosAir;
	private Vector<AirportsDestDO> vecAirportRecent;
	private ArrayList<String> listDataHeader;
	HashMap<String, Vector<AirportsDestDO>> listDataChild;
	
	private Context _context;
	   private ExpandableListView list;
	    private TextView noItem;

	public selectAirportDestAdapter(Context bookFlight,HashMap<String, Vector<AirportsDestDO>> hasmapplace, ExpandableListView list, TextView noItem,ArrayList<String> header) 
	{
		this._context = bookFlight;
		this.listDataChild = hasmapplace;
		this.vecAirportTempAll = (Vector<AirportsDestDO>) hasmapplace.get("All Airport");
		this.vecAirportTempClosAir= (Vector<AirportsDestDO>) hasmapplace.get("Favourite Airport");
		this.vecAirportTempRecent= (Vector<AirportsDestDO>) hasmapplace.get("Recent Airport");
		this.list=list;
		this.noItem=noItem;
		this.listDataHeader=header;
		vecAirport=(Vector<AirportsDestDO>) hasmapplace.get("All Airport").clone();
		this.vecAirportClosAir= (Vector<AirportsDestDO>) hasmapplace.get("Favourite Airport").clone();
		this.vecAirportRecent= (Vector<AirportsDestDO>) hasmapplace.get("Recent Airport").clone();
	}

	class ViewHolder {
		TextView text, textCountry;
		ImageView imgSep;
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
			for (AirportsDestDO wp : vecAirport) 
			{
				if (wp != null && wp.name.toLowerCase().contains(charText)) 
				{
					vecAirportTempAll.add(wp);
				}
			}
//			for (AirportsDestDO wp : vecAirportClosAir) 
//			{
//				if (wp != null && wp.name.toLowerCase().contains(charText)) 
//				{
//					vecAirportTempClosAir.add(wp);
//				}
//			}
//			for (AirportsDestDO wp : vecAirportRecent) 
//			{
//				if (wp != null && wp.name.toLowerCase().contains(charText)) 
//				{
//					vecAirportTempRecent.add(wp);
//				}
//			}
		}
		if(vecAirportTempAll.size()==0){
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
			vecAirportTempAll.clear();
			vecAirportTempAll.addAll(arrayListPa);
		}
		notifyDataSetChanged();
	}
	
	public Vector<AirportsDestDO> getAllData()
	{
		return vecAirportTempAll;
	}
	public Vector<AirportsDestDO> getcloseAirData()
	{
		return vecAirportTempClosAir;
	}
	public Vector<AirportsDestDO> getRecentData()
	{
		return vecAirportTempRecent;
	}

	@Override
	public int getGroupCount() {
		if(listDataHeader!= null)
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
		if(listDataHeader != null)
			return this.listDataHeader.get(groupPosition);
		else
			return null;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		if(listDataChild != null)
			return this.listDataChild.get(this.listDataHeader.get(groupPosition)).get(childPosition);
		else
			return null;
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
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.airportlistspinner_header, null);
        }
 
        TextView lblListHeader = (TextView) convertView.findViewById(R.id.tv_Header);
        
        
        lblListHeader.setTypeface(BaseActivity.typefaceOpenSansSemiBold);
        
        if(groupPosition == 0)
        {
        	lblListHeader.setText(R.string.favourite_Airport);
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
        else
        	lblListHeader.setText(headerTitle);
        
        if(getChildrenCount(groupPosition) == 0)
        	lblListHeader.setVisibility(View.GONE);
        else
        	lblListHeader.setVisibility(View.VISIBLE);
 
        return convertView;
    
	}


	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
			ViewGroup parent) {

        final AirportsDestDO objAirportsDestDO = (AirportsDestDO) getChild(groupPosition, childPosition);
        ViewHolder viewHolder;
		if(convertView == null)
		{
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(_context).inflate(R.layout.airportlistspinner_new, null);
			viewHolder.text = (TextView) convertView.findViewById(R.id.tv_listairportadaptext);
			viewHolder.textCountry = (TextView) convertView.findViewById(R.id.tv_listairportadaptextCountry);
			viewHolder.imgSep = (ImageView) convertView.findViewById(R.id.imgSep);
			
			
			convertView.setTag(viewHolder);
		}
		else
			viewHolder = (ViewHolder)convertView.getTag();

		
//		if(position == 0)
//			viewHolder.text.setText(con.getString(R.string.SelectAirport));
//		else
		/*	viewHolder.text.setText(vecAirportTemp.get(position).name);
			viewHolder.text.setText(_listDataChild.get((String)getCh)get(position).);
			viewHolder.textCountry.setText(vecAirportTemp.get(position).countryname);*/
			

			viewHolder.text.setText(objAirportsDestDO.name);
			viewHolder.textCountry.setText(objAirportsDestDO.countryname);

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
}