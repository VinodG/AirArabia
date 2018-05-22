package com.winit.airarabia.wheel.widget.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Adapter for string based wheel. Highlights the current value.
 */
public class DateArrayAdapter extends ArrayWheelAdapter<String> {
    // Index of current item
    int currentItem;
    // Index of item to be highlighted
    int currentValue;
    
    /**
     * Constructor
     */
    public DateArrayAdapter(Context context, String[] items, int current) {
        super(context, items);
        this.currentValue = current;
        setTextSize(16);
    }
    
    @Override
    protected void configureTextView(TextView view) {
        super.configureTextView(view);
        if (currentItem == currentValue) {
            view.setTextColor(Color.WHITE);
        }
        else
        {
        	view.setTextColor(Color.BLACK);
        }
        view.setTypeface(Typeface.SANS_SERIF);
    }
    
    @Override
    public View getItem(int index, View cachedView, ViewGroup parent) {
        currentItem = index;
        return super.getItem(index, cachedView, parent);
    }
}
