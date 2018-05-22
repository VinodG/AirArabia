package com.winit.airarabia.adapters;

import java.util.Vector;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.winit.airarabia.R;
import com.winit.airarabia.common.AppConstants;

public class BookingMealMainAdapter extends BaseAdapter{

	private Vector<String> vecbookMeal;
	private Vector<String> vecMealcategoryImageUrls;
	private Context context;
	public BookingMealMainAdapter(Context context,Vector<String> vecbookMeal, Vector<String> vecMealcategoryImageUrls) {
		this.context = context;
		this.vecbookMeal = vecbookMeal;
		this.vecMealcategoryImageUrls = vecMealcategoryImageUrls;
	}

	@Override
	public int getCount() {
		if(vecbookMeal != null && vecbookMeal.size() > 0)
			return vecbookMeal.size();
		else return 0;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertview, ViewGroup paramViewGroup) {
		if(convertview== null)
		{
			convertview = LayoutInflater.from(context).inflate(R.layout.popup_meal_mainmenu_gridcell, null);
		}

		ImageView ivGridmealmenu = (ImageView)convertview.findViewById(R.id.ivGridmealmenu);
		TextView tvGridmealmenu = (TextView)convertview.findViewById(R.id.tvGridmealmenu);
		Picasso.with(context).load(vecMealcategoryImageUrls.get(position)).error(R.drawable.btn_languagebg).into(ivGridmealmenu);
		ivGridmealmenu.setBackgroundResource(0);
		tvGridmealmenu.setTypeface(AppConstants.typefaceOpenSansSemiBold);
		/*UrlImageViewHelper.setUrlDrawable(ivGridmealmenu, vecMealcategoryImageUrls.get(position), R.drawable.btn_languagebg, new UrlImageViewCallback() 
		{
			@Override
			public void onLoaded(ImageView imageView, Bitmap loadedBitmap, String url, boolean loadedFromCache) 
			{
				if (!loadedFromCache) {
					ScaleAnimation scale = new ScaleAnimation(0, 1, 0, 1, ScaleAnimation.RELATIVE_TO_SELF, .5f, ScaleAnimation.RELATIVE_TO_SELF, .5f);
					scale.setDuration(300);
					scale.setInterpolator(new OvershootInterpolator());
					imageView.startAnimation(scale);
				}
			}
		});*/

		tvGridmealmenu.setText(vecbookMeal.get(position));

		return convertview;
	}

	class ViewHolder {
		ImageView ivGridmealmenu;
		TextView tvGridmealmenu;
	}
}
