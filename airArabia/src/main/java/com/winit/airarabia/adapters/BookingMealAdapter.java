package com.winit.airarabia.adapters;

import java.util.Vector;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.winit.airarabia.BaseActivity;
import com.winit.airarabia.R;
import com.winit.airarabia.common.AppConstants;
import com.winit.airarabia.objects.FlightSegmentDO;
import com.winit.airarabia.objects.MealDO;
import com.winit.airarabia.objects.RequestDO;
import com.winit.airarabia.utils.StringUtils;

public class BookingMealAdapter extends BaseAdapter{

	private Vector<MealDO> vecbookMeal;
	private Context context;
	private Vector<RequestDO> vecRequestDOs;
	private FlightSegmentDO flightSegmentDO;
	private String travelerRefNumberRPHList;

	public BookingMealAdapter(Context context,String travelerRefNumberRPHList, FlightSegmentDO flightSegmentDO, final Vector<RequestDO> vecRequestDOsData,Vector<MealDO> vecbookMeal) {
		this.context = context;
		this.vecbookMeal = vecbookMeal;
		this.flightSegmentDO = flightSegmentDO;
		this.travelerRefNumberRPHList = travelerRefNumberRPHList;
		this.vecRequestDOs = vecRequestDOsData;
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
	public View getView(int position, View convertview, ViewGroup paramViewGroup) {
		MealDO mealDO = vecbookMeal.get(position);
		final ViewHolder viewHolder;
		if(convertview== null)
		{
			viewHolder = new ViewHolder();
			convertview = LayoutInflater.from(context).inflate(R.layout.popup_meal_sub_item_listcell, null);
			viewHolder.ivPlus = (ImageView)convertview.findViewById(R.id.ivPlus);
			viewHolder.ivMinus = (ImageView)convertview.findViewById(R.id.ivMinus);
			viewHolder.imgSlectediteminmeal = (ImageView)convertview.findViewById(R.id.imgSlectediteminmeal);
			viewHolder.tvTitleslectediteminmeal = (TextView)convertview.findViewById(R.id.tvTitleslectediteminmeal);
			viewHolder.tvQuantity = (TextView)convertview.findViewById(R.id.tvQuantity);
			viewHolder.tvPrice = (TextView)convertview.findViewById(R.id.tvPrice);
			viewHolder.tvDescMeal = (TextView)convertview.findViewById(R.id.tvDescMeal);
			convertview.setTag(viewHolder);
		}
		else
			viewHolder = (ViewHolder)convertview.getTag();
		
		viewHolder.tvTitleslectediteminmeal.setTypeface(AppConstants.typefaceOpenSansSemiBold);
		viewHolder.tvQuantity.setTag(position+"");
		viewHolder.ivPlus.setTag(position+"");
		viewHolder.ivMinus.setTag(position+"");
		viewHolder.tvQuantity.setText(StringUtils.getString(mealDO.mealCount));
		viewHolder.ivPlus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MealDO mealDO = vecbookMeal.get(StringUtils.getInt(v.getTag().toString()));

				boolean isAdded = false;
				if(vecRequestDOs.size() == 0)
				{
					mealDO.mealCount = 0;
					mealDO.mealCount += 1;
					RequestDO requestDONew = new RequestDO();
					requestDONew.departureDate = flightSegmentDO.departureDateTime;
					requestDONew.flightNumber = flightSegmentDO.flightNumber;
					requestDONew.flightRefNumberRPHList = flightSegmentDO.RPH;
					requestDONew.travelerRefNumberRPHList = travelerRefNumberRPHList;
					requestDONew.mealCode = mealDO.mealCode;
					requestDONew.mealQuantity = mealDO.mealCount+"";
					requestDONew.mealName = mealDO.mealName;
					requestDONew.mealCharge = StringUtils.getFloat(mealDO.mealCharge);
					vecRequestDOs.add(requestDONew);
				}
				else if(vecRequestDOs.size() > 0)
				{
					for (RequestDO requestDONew : vecRequestDOs) {
						if(requestDONew.mealCode.equalsIgnoreCase(mealDO.mealCode))
						{
							isAdded = true;
							mealDO.mealCount = StringUtils.getInt(requestDONew.mealQuantity);
							mealDO.mealCount += 1;
							requestDONew.mealCode = mealDO.mealCode;
							requestDONew.mealQuantity = mealDO.mealCount+"";
							requestDONew.mealName = mealDO.mealName;
							requestDONew.mealCharge = StringUtils.getFloat(mealDO.mealCharge);
						}
					}
					if(!isAdded)
					{
						mealDO.mealCount += 1;
						RequestDO requestDONew = new RequestDO();
						requestDONew.departureDate = flightSegmentDO.departureDateTime;
						requestDONew.flightNumber = flightSegmentDO.flightNumber;
						requestDONew.flightRefNumberRPHList = flightSegmentDO.RPH;
						requestDONew.travelerRefNumberRPHList = travelerRefNumberRPHList;
						requestDONew.mealCode = mealDO.mealCode;
						requestDONew.mealQuantity = mealDO.mealCount+"";
						requestDONew.mealName = mealDO.mealName;
						requestDONew.mealCharge = StringUtils.getFloat(mealDO.mealCharge);
						vecRequestDOs.add(requestDONew);
					}
				}
				if(v.getTag().toString().equalsIgnoreCase(viewHolder.tvQuantity.getTag().toString()))
					viewHolder.tvQuantity.setText(StringUtils.getString(mealDO.mealCount));
			}
		});
		viewHolder.ivMinus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MealDO mealDO = vecbookMeal.get(StringUtils.getInt(v.getTag().toString()));
				if(vecRequestDOs.size() > 0)
				{
					for (RequestDO requestDONew : vecRequestDOs) {
						if(requestDONew.mealCode.equalsIgnoreCase(mealDO.mealCode)
								&& travelerRefNumberRPHList.equalsIgnoreCase(requestDONew.travelerRefNumberRPHList)
								&& flightSegmentDO.flightNumber.equalsIgnoreCase(requestDONew.flightNumber))
						{
							mealDO.mealCount = StringUtils.getInt(requestDONew.mealQuantity);

							if(mealDO.mealCount > 0)
							{
								mealDO.mealCount -= 1;
								requestDONew.mealQuantity = mealDO.mealCount+"";
								if(requestDONew.mealQuantity.equalsIgnoreCase("0"))
									vecRequestDOs.remove(requestDONew);
							}
						}

					}
				}
				if(v.getTag().toString().equalsIgnoreCase(viewHolder.tvQuantity.getTag().toString()))
					viewHolder.tvQuantity.setText(StringUtils.getString(mealDO.mealCount));
			}
		});
		viewHolder.tvTitleslectediteminmeal.setText(mealDO.mealName);
		viewHolder.tvDescMeal.setText(mealDO.mealDescription);
		viewHolder.tvPrice.setText(context.getString(R.string.Price)+": "+ AppConstants.CurrencyCodeAfterExchange+" "+
				((BaseActivity) context).updateCurrencyByFactor(mealDO.mealCharge+"", 0)+"");
		Picasso.with(context).load(mealDO.mealImageLink).error(R.drawable.btn_languagebg).into(viewHolder.imgSlectediteminmeal);
		viewHolder.imgSlectediteminmeal.setBackgroundResource(0);
		/*UrlImageViewHelper.setUrlDrawable(viewHolder.imgSlectediteminmeal, mealDO.mealImageLink, R.drawable.btn_languagebg, new UrlImageViewCallback() 
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
		return convertview;
	}

	class ViewHolder{
		ImageView ivPlus,ivMinus,imgSlectediteminmeal;
		TextView tvTitleslectediteminmeal,tvQuantity,tvPrice,tvDescMeal;
	}
}