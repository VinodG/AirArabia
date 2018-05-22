package com.winit.airarabia;



import java.util.ArrayList;

import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.winit.airarabia.adapters.ClassAdapter;

public class SelectClassType extends BaseActivity{

	private LinearLayout llSelectCurrency;
	private ListView list;
	private ClassAdapter currencyAdapter;
	private EditText etSearch;
	private TextView tvCancel,tvSelectCurrencyTitle, tvDone;
	private ArrayList<String> arrListCurrency;
	private int position = -1;
	private String classNameToCompare = "";
	@Override
	public void initilize() {
		llSelectCurrency = (LinearLayout) layoutInflater.inflate(R.layout.select_currency, null);
		lltop.setVisibility(View.GONE);
		tvHeaderTitle.setText(getString(R.string.selectClass));
		llMiddleBase.addView(llSelectCurrency, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		
		list = (ListView) llSelectCurrency.findViewById(R.id.list);
		etSearch = (EditText) llSelectCurrency.findViewById(R.id.etSearch);
		tvCancel = (TextView) llSelectCurrency.findViewById(R.id.tvCancel);
		tvDone = (TextView) llSelectCurrency.findViewById(R.id.tvDone);
		tvSelectCurrencyTitle = (TextView) llSelectCurrency.findViewById(R.id.tvSelectCurrency);
		
		if(getIntent().hasExtra("classNameToCompare"))
			classNameToCompare = getIntent().getStringExtra("classNameToCompare");
		tvSelectCurrencyTitle.setText(getString(R.string.selectClass));
		etSearch.setVisibility(View.GONE);
		tvDone.setText(getString(R.string.Cancel));
	}

	@Override
	public void bindingControl() {
		
		arrListCurrency = new ArrayList<String>();
		arrListCurrency.add(getString(R.string.economy));
		arrListCurrency.add(getString(R.string.business));
		
		currencyAdapter = new ClassAdapter(SelectClassType.this, arrListCurrency, classNameToCompare);
		list.setAdapter(currencyAdapter);
		list.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int pos,
					long arg3) {
				position = pos;
//				classNameToCompare = view.getTag(R.string.add).toString();
//				if (!classNameToCompare.isEmpty() && !classNameToCompare.equalsIgnoreCase(""))
//					currencyAdapter.refresh(classNameToCompare);
				if (position >= 0 
						&& currencyAdapter != null 
						&& currencyAdapter.getData() != null 
						&& position < currencyAdapter.getData().size()) {
					Intent intent = new Intent();
					intent.putExtra("Currency_Selected",currencyAdapter.getData().get(position));
					setResult(RESULT_OK, intent);        
					finish();
				}
				else
				{
					Intent intent = new Intent();
					intent.putExtra("Currency_Selected",classNameToCompare);
					setResult(RESULT_OK, intent);        
					finish();				}
			
			}
		});
		etSearch.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				String text = etSearch.getText().toString();
				currencyAdapter.filter(text);
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
			}
		});
		
		tvCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		tvDone.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				if (position >= 0 
//						&& currencyAdapter != null 
//						&& currencyAdapter.getData() != null 
//						&& position < currencyAdapter.getData().size()) {
//					Intent intent = new Intent();
//					intent.putExtra("Currency_Selected",currencyAdapter.getData().get(position));
//					setResult(RESULT_OK, intent);        
//					finish();
//				}
//				else
//				{
//					Intent intent = new Intent();
//					intent.putExtra("Currency_Selected",classNameToCompare);
//					setResult(RESULT_OK, intent);        
//					finish();				}
				finish();
		}
		});
		
	}

}
