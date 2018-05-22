//package com.winit.airarabia;
//
//import android.app.ActionBar.LayoutParams;
//import android.content.Intent;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.EditText;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import com.winit.airarabia.adapters.SpinnerairportAdapter;
//import com.winit.airarabia.adapters.SpinnerairportDestAdapter;
//import com.winit.airarabia.common.AppConstants;
//import com.winit.airarabia.objects.AirportsDO;
//
//public class SelectAirport extends BaseActivity {
//
//	private LinearLayout llSelectCountry;
//	private ListView list;
//	private SpinnerairportAdapter spinneradap;
//	private SpinnerairportDestAdapter destAdapter;
//	private EditText etSearch;
//	private AirportsDO airportsArrivalDo;
//	private TextView tvCancel, tvSelectAirportTitle, tvNoItem;
//
//	@Override
//	public void initilize() {
//		// TODO Auto-generated method stub
//		llSelectCountry = (LinearLayout) layoutInflater.inflate(
//				R.layout.select_airport, null);
//		// lltop.setVisibility(View.VISIBLE);
//		lltop.setVisibility(View.GONE);
//		llMiddleBase.addView(llSelectCountry, LayoutParams.MATCH_PARENT,
//				LayoutParams.MATCH_PARENT);
//
//		if (getIntent().hasExtra("Arrival_Object")) {
//			airportsArrivalDo = (AirportsDO) getIntent().getSerializableExtra("Arrival_Object");
//		} else
//			airportsArrivalDo = null;
//
//		list = (ListView) llSelectCountry.findViewById(R.id.list);
//		etSearch = (EditText) llSelectCountry.findViewById(R.id.etSearch);
//		tvCancel = (TextView) llSelectCountry.findViewById(R.id.tvCancel);
//		tvNoItem = (TextView) llSelectCountry.findViewById(R.id.tvNoItem);
//		tvSelectAirportTitle = (TextView) llSelectCountry
//				.findViewById(R.id.tvSelectAirportTitle);
//		if (airportsArrivalDo == null)
//			tvSelectAirportTitle.setText(getString(R.string.SelectOrigin));
//		else
//			tvSelectAirportTitle.setText(getString(R.string.SelectDestination));
//	}
//
//	@Override
//	public void bindingControl() {
//		// TODO Auto-generated method stub
//		updateSpinner();
//		etSearch.addTextChangedListener(new TextWatcher() {
//
//			@Override
//			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
//					int arg3) {
//				String text = etSearch.getText().toString();
//				if (airportsArrivalDo == null)
//					spinneradap.filter(text);
//				else
//					destAdapter.filter(text);
//			}
//
//			@Override
//			public void beforeTextChanged(CharSequence arg0, int arg1,
//					int arg2, int arg3) {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void afterTextChanged(Editable arg0) {
//				// String text =
//				// etSearch.getText().toString().toLowerCase(Locale.getDefault());
//				// spinneradap.filter(text);
//			}
//		});
//
//		tvCancel.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				finish();
//			}
//		});
//
//	}
//
//	private void updateSpinner() {
//		// AppConstants.allAirportsDO.vecAirport.remove(0);
//		if (airportsArrivalDo == null) {
//			spinneradap = new SpinnerairportAdapter(SelectAirport.this,
//					AppConstants.allAirportsDO.vecAirport, list, tvNoItem);
//			list.setAdapter(spinneradap);
//			list.setOnItemClickListener(new OnItemClickListener() {
//
//				@Override
//				public void onItemClick(AdapterView<?> arg0, View arg1,
//						int pos, long arg3) {
//					Intent intent = new Intent();
//					intent.putExtra("Departure_Airport", spinneradap.getData()
//							.get(pos));
//					setResult(RESULT_OK, intent);
//					finish();
//				}
//			});
//
//		} else {
//			destAdapter = new SpinnerairportDestAdapter(SelectAirport.this,
//					airportsArrivalDo.destList, list, tvNoItem);
//			list.setAdapter(destAdapter);
//			list.setOnItemClickListener(new OnItemClickListener() {
//
//				@Override
//				public void onItemClick(AdapterView<?> arg0, View arg1,
//						int pos, long arg3) {
//					Intent intent = new Intent();
//					intent.putExtra("Arrival_Airport", destAdapter.getData()
//							.get(pos));
//					setResult(RESULT_OK, intent);
//					finish();
//				}
//			});
//		}
//
//		// list.setOnItemSelectedListener(new OnItemSelectedListener() {
//		//
//		// @Override
//		// public void onItemSelected(AdapterView<?> arg0, View v,
//		// int pos, long arg3) {
//		// Toast.makeText(SelectAirport.this,
//		// ""+AppConstants.allAirportsDO.vecAirport.get(pos),
//		// Toast.LENGTH_SHORT).show();
//		// finish();
//		// }
//		//
//		// @Override
//		// public void onNothingSelected(AdapterView<?> arg0) {
//		// // TODO Auto-generated method stub
//		//
//		// }
//		// });
//		// if(isManageBook)
//		// {
//		// if(isOutBound)
//		// {
//		// for (int i = 1; i < AppConstants.allAirportsDO.vecAirport.size();
//		// i++) {
//		// if(AppConstants.allAirportsDO.vecAirport.get(i).code.equalsIgnoreCase(AppConstants.bookingFlightDO.myBookFlightDO.originLocationCode))
//		// {
//		// selPosDeparture = i;
//		// }
//		// }
//		// }
//		// else if(!isOutBound)
//		// {
//		// for (int i = 1; i < AppConstants.allAirportsDO.vecAirport.size();
//		// i++) {
//		// if(AppConstants.allAirportsDO.vecAirport.get(i).code.equalsIgnoreCase(AppConstants.bookingFlightDO.myBookFlightDOReturn.originLocationCode))
//		// {
//		// selPosDeparture = i;
//		// }
//		// }
//		// }
//		// spinnerDepartue.setSelection(selPosDeparture);
//		// }
//		// else
//		// {
//		// spinnerDepartue.setSelection(0);
//		// }
//		// hideLoader();
//	}
//}
