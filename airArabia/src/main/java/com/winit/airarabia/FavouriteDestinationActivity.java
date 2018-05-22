//package com.winit.airarabia;
//
//import java.util.ArrayList;
//
//import android.app.ActionBar.LayoutParams;
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import com.winit.airarabia.common.SharedPreferenceStrings;
//import com.winit.airarabia.utils.SharedPrefUtils;
//
//public class FavouriteDestinationActivity extends BaseActivity{
//private LinearLayout llFav,llAddFav;
//private ArrayList<String> listFavourite;
//private ListView listViewFav;
//private FavouriteAdapter favAdapter;
//private TextView tvEdit,tvCancel, tvClearAll, tvSelectCurrency;
//private ImageView sepTop;
//private SharedPreferences  mPrefs ;
//private String Email_LoggedInPrevious = "";
//	@Override
//	public void initilize() {
//		// TODO Auto-generated method stub
//		llFav = (LinearLayout) layoutInflater.inflate(R.layout.activity_favourite_destination, null);
//		lltop.setVisibility(View.VISIBLE);
//		llMiddleBase.addView(llFav, LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
//		llAddFav=(LinearLayout) llFav.findViewById(R.id.llAddFav);
//		listViewFav=(ListView) llFav.findViewById(R.id.lvFavItems);
//		tvEdit= (TextView) llFav.findViewById(R.id.tvEdit);
//		tvCancel= (TextView) llFav.findViewById(R.id.tvCancel);
//		tvClearAll= (TextView) llFav.findViewById(R.id.tvClearAll);
//		tvSelectCurrency=(TextView) llFav.findViewById(R.id.tvSelectCurrency);
//		sepTop=(ImageView) llFav.findViewById(R.id.sepTop);
//		tvSelectCurrency.setTypeface(typefaceOpenSansSemiBold);
//		lltop.setVisibility(View.GONE);
//		listFavourite=new ArrayList<String>();
//		mPrefs = this.getSharedPreferences(SharedPreferenceStrings.APP_PREFERENCES, Context.MODE_PRIVATE);
//		Email_LoggedInPrevious = mPrefs.getString("Email_LoggedInPrevious", "");
////        bindingControl();
//		lockDrawer("Favourite Destinations");
//	}
//
//
//	@Override
//	public void bindingControl() {
//		btnSubmitNext.setVisibility(View.VISIBLE);
//		btnSubmitNext.setText(getString(R.string.done));
//		btnSubmitNext.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				finish();
//				
//			}
//		});
//		llAddFav.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				Intent intent=new Intent(FavouriteDestinationActivity.this,FavouriteListActivity.class);
//				startActivity(intent);
//			}
//		});
//		
//		tvEdit.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				llAddFav.performClick();
//			}
//		});
//		tvCancel.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				finish();
//			}
//		});
//		tvClearAll.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				listFavourite.clear();
//				favAdapter.refreshListView(listFavourite);
//				tvClearAll.setVisibility(View.GONE);
//				sepTop.setVisibility(View.GONE);
//				SharedPrefUtils.saveArrayInPreference(listFavourite, SharedPreferenceStrings.FAVLIST, FavouriteDestinationActivity.this);
//			}
//		});
//		
//		listViewFav.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view, int pos,
//					long arg3) {
//				listFavourite.remove(pos);
//				SharedPrefUtils.saveArrayInPreference(listFavourite, SharedPreferenceStrings.FAVLIST, FavouriteDestinationActivity.this);
//				favAdapter.refreshListView(listFavourite);
//				if (listFavourite.size()==0) {
//					tvClearAll.setVisibility(View.GONE);
//					sepTop.setVisibility(View.GONE);
//				}else{
//					tvClearAll.setVisibility(View.VISIBLE);
//					sepTop.setVisibility(View.VISIBLE);
//				}
//			}
//		});
//	}
//
//	@Override
//		protected void onResume() {
//			// TODO Auto-generated method stub
//			super.onResume();
////			if (AppConstants.currentUserEmail.toString().equalsIgnoreCase(Email_LoggedInPrevious)) 
//				listFavourite=SharedPrefUtils.loadArrayFromPreference(SharedPreferenceStrings.FAVLIST, FavouriteDestinationActivity.this);
//			
//			if(listFavourite!=null && listFavourite.size()>0){
//				favAdapter=new FavouriteAdapter(listFavourite);
//				listViewFav.setAdapter(favAdapter);
//				tvClearAll.setVisibility(View.VISIBLE);
//				sepTop.setVisibility(View.VISIBLE);
//				
//			}else{
//		
//				favAdapter=new FavouriteAdapter(listFavourite);
//				listViewFav.setAdapter(null);
//				tvClearAll.setVisibility(View.GONE);
//				sepTop.setVisibility(View.GONE);
//			}
//			
//			
//			favAdapter.refreshListView();
//			
//		}
//	class FavouriteAdapter extends BaseAdapter {
//		ArrayList<String> favDestination;
//
//		public FavouriteAdapter(ArrayList<String> favDestination) {
//			// TODO Auto-generated constructor stub
//			this.favDestination = favDestination;
//		}
//
//		public void refreshListView(ArrayList<String> tempList) {
//			// TODO Auto-generated method stub
//			this.favDestination = tempList;
//			notifyDataSetChanged();
//		}
//		public void refreshListView() {
//			notifyDataSetChanged();
//		}
//
//		@Override
//		public int getCount() {
//			// TODO Auto-generated method stub
//			if (favDestination != null && favDestination.size() > 0) {
//				return favDestination.size();
//			} else
//				return 0;
//		}
//
//		@Override
//		public Object getItem(int arg0) {
//			// TODO Auto-generated method stub
//			return favDestination.get(arg0);
//		}
//
//		@Override
//		public long getItemId(int arg0) {
//			// TODO Auto-generated method stub
//			return 0;
//		}
//
//		@Override
//		public View getView(int pos, View convertView, ViewGroup arg2) {
//			ViewHolderItem viewHolder;
//			if (convertView == null) {
//
//	
//				LayoutInflater inflater = getLayoutInflater();
//				convertView = inflater.inflate(R.layout.favourite_item, null);
//
//				viewHolder = new ViewHolderItem();
//				viewHolder.tvFavItem = (TextView) convertView
//						.findViewById(R.id.tvFavItem);
//				viewHolder.ivFavImage = (ImageView) convertView
//						.findViewById(R.id.ivFavItem);
//				viewHolder.imgSeprator = (ImageView) convertView
//						.findViewById(R.id.imgSeprator);
//	
//				convertView.setTag(viewHolder);
//
//			} else {
//				viewHolder = (ViewHolderItem) convertView.getTag();
//			}
//		
//				viewHolder.tvFavItem.setText(favDestination.get(pos) + "");
//				viewHolder.ivFavImage.setImageResource(R.drawable.img_redcircleminus);
//			if(getCount() ==1 && pos == 0)
//				viewHolder.imgSeprator.setVisibility(View.GONE);
//			else if(pos == getCount() -1 )
//				viewHolder.imgSeprator.setVisibility(View.GONE);
//			else
//				viewHolder.imgSeprator.setVisibility(View.VISIBLE);
//			return convertView;
//		}
//		
//	}
//
//	class ViewHolderItem {
//		TextView tvFavItem;
//		ImageView ivFavImage,imgSeprator;
//	}
//
//	
//	
//}
