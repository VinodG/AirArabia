package com.winit.airarabia;

import java.util.Vector;

import android.app.ActionBar.LayoutParams;
import android.app.Dialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.winit.airarabia.common.SharedPreferenceStrings;
import com.winit.airarabia.objects.OtherPassengerDo;
import com.winit.airarabia.objects.SavedPassengerDO;


public class SavedPassengerActivity extends BaseActivity {

	private LinearLayout llSaveProfile, llAddNewSavedPassenger;
	private ListView listView;
	private SavedPassengerAdapter adapter;
	private OtherPassengerDo otherPassengerDo;
	private Vector<SavedPassengerDO> vecSavedPassengerDo = new Vector<SavedPassengerDO>();
	private SavedPassengerDO savedPassengerDo;
	private TextView tvCancel, tvSelectHeader, tvDone;
	private String tempPassengerType ="";
	
	@Override
	public void initilize() {
		llSaveProfile = (LinearLayout) layoutInflater.inflate(R.layout.activity_saved_passenger, null);
		llMiddleBase.addView(llSaveProfile, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		listView 	= (ListView) llSaveProfile.findViewById(R.id.listView);
		llAddNewSavedPassenger 	= (LinearLayout) llSaveProfile.findViewById(R.id.llAddNewSavedPassenger);
		tvCancel 		= (TextView) llSaveProfile.findViewById(R.id.tvCancel);
		tvSelectHeader 	= (TextView) llSaveProfile.findViewById(R.id.tvSelectHeader);
		tvDone 	= (TextView) llSaveProfile.findViewById(R.id.tvDone);
		
		lltop.setVisibility(View.GONE);
		
		showLoader("");
		otherPassengerDo = gettingObjecFromSharedPrerenceOtherPassengerDo(SharedPreferenceStrings.otherPassengerDo);
		hideLoader();
		
		
		tvCancel.setTypeface(typefaceOpenSansSemiBold);
		tvSelectHeader.setTypeface(typefaceOpenSansSemiBold);
		tvDone.setTypeface(typefaceOpenSansSemiBold);
		tvSelectHeader.setText(getString(R.string.saved_passengers));
		tvDone.setText(getString(R.string.edit));
		
		adapter = new SavedPassengerAdapter(vecSavedPassengerDo); 
		bindingControl();
	}

	@Override
	public void bindingControl() {
		
		listView.setAdapter(adapter);
		adapter.refreshListView(vecSavedPassengerDo);
		
		llAddNewSavedPassenger.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				showSelectYourPassengerType(resources.getStringArray(R.array.passenger_type));
				
				///AddNewPassenger move there
			}
		});
		
		tvCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
		tvDone.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent in = new Intent(SavedPassengerActivity.this, EditSavedPassengerActivity.class);
				startActivity(in);
			}
		});
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				Intent i = new Intent(SavedPassengerActivity.this, UpdateNewPassenger.class);
				for (int tempJ = 0; tempJ < otherPassengerDo.vecSavedPassengerDoAdult.size(); tempJ++) {
					if(otherPassengerDo.vecSavedPassengerDoAdult.get(tempJ).firstName.equals(vecSavedPassengerDo.get(pos).firstName)
							&& otherPassengerDo.vecSavedPassengerDoAdult.get(tempJ).lastName.equals(vecSavedPassengerDo.get(pos).lastName)
									&& otherPassengerDo.vecSavedPassengerDoAdult.get(tempJ).dob.equals(vecSavedPassengerDo.get(pos).dob))
					{
						tempPassengerType = "Adult";
					}
				}
				for (int tempK = 0; tempK < otherPassengerDo.vecSavedPassengerDoChild.size(); tempK++) {
					if(otherPassengerDo.vecSavedPassengerDoChild.get(tempK).firstName.equals(vecSavedPassengerDo.get(pos).firstName)
							&& otherPassengerDo.vecSavedPassengerDoChild.get(tempK).lastName.equals(vecSavedPassengerDo.get(pos).lastName)
									&& otherPassengerDo.vecSavedPassengerDoChild.get(tempK).dob.equals(vecSavedPassengerDo.get(pos).dob))
					{
						tempPassengerType = "Child";
					}
				}
				for (int tempL = 0; tempL < otherPassengerDo.vecSavedPassengerDoInfant.size(); tempL++) {
					if(otherPassengerDo.vecSavedPassengerDoInfant.get(tempL).firstName.equals(vecSavedPassengerDo.get(pos).firstName)
							&& otherPassengerDo.vecSavedPassengerDoInfant.get(tempL).lastName.equals(vecSavedPassengerDo.get(pos).lastName)
									&& otherPassengerDo.vecSavedPassengerDoInfant.get(tempL).dob.equals(vecSavedPassengerDo.get(pos).dob))
					{
						tempPassengerType = "Infant";
					}
				}
				i.putExtra("PassengerObject", vecSavedPassengerDo.get(pos));
				i.putExtra("PassengerType", tempPassengerType.toString());
				startActivity(i);
			}
		});
		
		btnBack.setTypeface(typefaceOpenSansSemiBold);
		tvDone.setTypeface(typefaceOpenSansSemiBold);
		tvHeaderTitle.setTypeface(typefaceOpenSansSemiBold);
		
	}
	
	class SavedPassengerAdapter extends BaseAdapter
	{
		private Vector<SavedPassengerDO> vecSavedPassengerDo;
		public SavedPassengerAdapter(Vector<SavedPassengerDO> vecSavedPassengerDo) {
			this.vecSavedPassengerDo = vecSavedPassengerDo;			
		}

		@Override
		public int getCount() {
			if(vecSavedPassengerDo != null && vecSavedPassengerDo.size()>= 0)
				return vecSavedPassengerDo.size();
			else
				return 0;
		}

		@Override
		public Object getItem(int pos) {
			return vecSavedPassengerDo.get(pos);
		}

		@Override
		public long getItemId(int pos) {
			return pos;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {
			ViewHolder viewHolder;
			
			if (convertView == null) {
				LayoutInflater inflater = getLayoutInflater();
				convertView = inflater.inflate(R.layout.saved_passanger_item, null);


				viewHolder = new ViewHolder();
				viewHolder.tvSavedPassengerName = (TextView) convertView.findViewById(R.id.tvSavedPassengerName);
				viewHolder.imgSeprator = (ImageView) convertView.findViewById(R.id.imgSeprator);

				convertView.setTag(viewHolder);

			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			
			viewHolder.tvSavedPassengerName.setText(((SavedPassengerDO)getItem(position)).firstName +" "+ ((SavedPassengerDO)getItem(position)).lastName);
			viewHolder.tvSavedPassengerName.setTypeface(typefaceOpenSansRegular);
			
			if(position < vecSavedPassengerDo.size()-1)
			{
				viewHolder.imgSeprator.setVisibility(View.VISIBLE);
			}
			

			return convertView;
		}
		
		public void refreshListView(Vector<SavedPassengerDO> vecSavedPassenger) {
			// TODO Auto-generated method stub
			this.vecSavedPassengerDo = vecSavedPassenger;
			notifyDataSetChanged();
		}
		
	}
	
	class ViewHolder
	{
		TextView tvSavedPassengerName;
		ImageView imgSeprator;
	}

	private void showSelectYourPassengerType(String[] arr_passenger_type) {
		LinearLayout ll = (LinearLayout) layoutInflater.inflate(R.layout.popup_titles, null);
		TextView tvTitleHeader = (TextView) ll.findViewById(R.id.tvTitleHeader);
		tvTitleHeader.setTypeface(typefaceOpenSansSemiBold);
		tvTitleHeader.setText(getString(R.string.Please_select_passenger_type));
		final Dialog dialog = new Dialog(this, R.style.Theme_Dialog_No_Title);
		dialog.setContentView(ll);
		setTypefaceOpenSansRegular(ll);
		tvTitleHeader.setTypeface(typefaceOpenSansSemiBold);
		LinearLayout llPopupTitleMain = (LinearLayout) ll.findViewById(R.id.llPopupTitleMain);
		for (int i = 0; i < arr_passenger_type.length; i++) {
			LinearLayout llTitle = (LinearLayout) layoutInflater.inflate(
					R.layout.popup_title_item, null);

			final TextView tvTitleItem = (TextView) llTitle.findViewById(R.id.tvTitleItem);
//			tvTitleItem.setTypeface(typeFaceOpenSansLight); Mrs
			llPopupTitleMain.addView(llTitle);

			tvTitleItem.setText(arr_passenger_type[i]);
			tvTitleItem.setTag(arr_passenger_type[i]);

			tvTitleItem.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if(!(tvTitleItem.getText().toString()).equalsIgnoreCase("")){
						Intent i = new Intent(SavedPassengerActivity.this, AddNewPassenger.class);
//						i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
						i.putExtra("PassengerType", tvTitleItem.getText().toString());
						startActivity(i);
					}
					tvTitleItem.setBackgroundColor(resources.getColor(R.color.ash_color));
					dialog.dismiss();
				}
			});
		}
		dialog.show();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		showLoader("");
		otherPassengerDo = gettingObjecFromSharedPrerenceOtherPassengerDo(SharedPreferenceStrings.otherPassengerDo);
		intializeData();
		adapter.refreshListView(vecSavedPassengerDo);
		hideLoader();
		super.onResume();
	}
	
	private void intializeData()
	{
		vecSavedPassengerDo.clear();
		for (int i = 0; i < otherPassengerDo.vecSavedPassengerDoAdult.size(); i++) {
			savedPassengerDo = new SavedPassengerDO();
			savedPassengerDo = (SavedPassengerDO) otherPassengerDo.vecSavedPassengerDoAdult.get(i);
			vecSavedPassengerDo.add(savedPassengerDo);
		}
		
		for (int i = 0; i < otherPassengerDo.vecSavedPassengerDoChild.size(); i++) {
			savedPassengerDo = new SavedPassengerDO();
			savedPassengerDo = (SavedPassengerDO) otherPassengerDo.vecSavedPassengerDoChild.get(i);
			vecSavedPassengerDo.add(savedPassengerDo);
		}
		
		for (int i = 0; i < otherPassengerDo.vecSavedPassengerDoInfant.size(); i++) {
			savedPassengerDo = new SavedPassengerDO();
			savedPassengerDo = (SavedPassengerDO) otherPassengerDo.vecSavedPassengerDoInfant.get(i);
			vecSavedPassengerDo.add(savedPassengerDo);
		}
		
		if(vecSavedPassengerDo.size() >0)
			tvDone.setVisibility(View.VISIBLE);
	}
	
}
