package com.winit.airarabia.utils;

import java.util.ArrayList;
import java.util.Vector;

import com.winit.airarabia.objects.AirportNamesDO;
import com.winit.airarabia.objects.AirportsDO;
import com.winit.airarabia.objects.CurrencyDo;

public class QuickSortCurrencyDo
{
	private ArrayList<CurrencyDo> arrListCurrencyDo;
	private ArrayList<String> arrListCurrencyDo1;
    private int length;
    private Vector<AirportsDO> airportsDOs;
 
    public void sort(ArrayList<CurrencyDo> arrList) 
    {
        if (arrList == null || arrList.size() == 0) {
            return;
        }
        this.arrListCurrencyDo = arrList;
        length = arrList.size();
        quickSortObject(0, length - 1);
    }
    public void sort1(ArrayList<String> arrList) 
    {
        if (arrList == null || arrList.size() == 0) {
            return;
        }
        this.arrListCurrencyDo1 = arrList;
        length = arrList.size();
        quickSortObject(0, length - 1);
    }
 
    private void quickSortObject(int lowerIndex, int higherIndex) 
    {
         
        int i = lowerIndex;
        int j = higherIndex;
        // calculate pivot, I am taking pivot as middle index number
        CurrencyDo pivot = arrListCurrencyDo.get(lowerIndex+(higherIndex-lowerIndex)/2);
        while (i < j) {
            /**
             * In each iteration, we will identify a number from left side which
             * is greater then the pivot value, and also we will identify a number
             * from right side which is less then the pivot value. Once the search
             * is done, then we exchange both numbers.
             */
            while (pivot.code.compareTo(arrListCurrencyDo.get(i).code) > 0) 
            {
                i++;
            }
            while (arrListCurrencyDo.get(j).code.compareTo(pivot.code) >0) 
            {
                j--;
            }
            if (i <= j) {
            	exchangeObject(i, j);
                //move index to next position on both sides
                i++;
                j--;
            }
        }
        // Here calling quickSort() method recursively
        if (lowerIndex < j)
        	quickSortObject(lowerIndex, j);
        if (i < higherIndex)
        	quickSortObject(i, higherIndex);
    }
 
    // Here Exchanging objects on the basis of Pivot
    private void exchangeObject(int i, int j) 
    {
    	CurrencyDo arrListCurrencyDoTemp = arrListCurrencyDo.get(i);
        arrListCurrencyDo.set(i, arrListCurrencyDo.get(j));
        arrListCurrencyDo.set(j, arrListCurrencyDoTemp);
    }
     
    // AirPortDO...
    
    public void sortAirPorts(Vector<AirportsDO> arrList) 
    {
        if (arrList == null || arrList.size() == 0) {
            return;
        }
        this.airportsDOs = arrList;
        length = arrList.size();
        quickSortObjectAirPort(0, length - 1);
    }
    
    private void quickSortObjectAirPort(int lowerIndex, int higherIndex) 
    {
         
        int i = lowerIndex;
        int j = higherIndex;
        // calculate pivot, I am taking pivot as middle index number
        AirportsDO pivot = airportsDOs.get(lowerIndex+(higherIndex-lowerIndex)/2);
        while (i < j) {
            /**
             * In each iteration, we will identify a number from left side which
             * is greater then the pivot value, and also we will identify a number
             * from right side which is less then the pivot value. Once the search
             * is done, then we exchange both numbers.
             */
            while (pivot.code.compareTo(airportsDOs.get(i).code) > 0) 
            {
                i++;
            }
            while (airportsDOs.get(j).code.compareTo(pivot.code) >0) 
            {
                j--;
            }
            if (i <= j) {
            	exchangeObjectAirPort(i, j);
                //move index to next position on both sides
                i++;
                j--;
            }
        }
        // Here calling quickSort() method recursively
        if (lowerIndex < j)
        	quickSortObjectAirPort(lowerIndex, j);
        if (i < higherIndex)
        	quickSortObjectAirPort(i, higherIndex);
    }
 
    // Here Exchanging objects on the basis of Pivot
    private void exchangeObjectAirPort(int i, int j) 
    {
    	AirportsDO arrListCurrencyDoTemp = airportsDOs.get(i);
    	airportsDOs.set(i, airportsDOs.get(j));
    	airportsDOs.set(j, arrListCurrencyDoTemp);
    }
    
    
    // Sorting Airport Names...
    
    private Vector<AirportNamesDO> airportNamesDOs;
    
    public void sortAirPortNames(Vector<AirportNamesDO> arrList) 
    {
        if (arrList == null || arrList.size() == 0) {
            return;
        }
        this.airportNamesDOs = arrList;
        length = arrList.size();
        quickSortObjectAirPortNames(0, length - 1);
    }
    
    private void quickSortObjectAirPortNames(int lowerIndex, int higherIndex) 
    {
         
        int i = lowerIndex;
        int j = higherIndex;
        // calculate pivot, I am taking pivot as middle index number
        AirportNamesDO pivot = airportNamesDOs.get(lowerIndex+(higherIndex-lowerIndex)/2);
        while (i < j) {
            /**
             * In each iteration, we will identify a number from left side which
             * is greater then the pivot value, and also we will identify a number
             * from right side which is less then the pivot value. Once the search
             * is done, then we exchange both numbers.
             */
            while (pivot.code.compareTo(airportNamesDOs.get(i).code) > 0) 
            {
                i++;
            }
            while (airportNamesDOs.get(j).code.compareTo(pivot.code) >0) 
            {
                j--;
            }
            if (i <= j) {
            	exchangeObjectAirPortNames(i, j);
                //move index to next position on both sides
                i++;
                j--;
            }
        }
        // Here calling quickSort() method recursively
        if (lowerIndex < j)
        	quickSortObjectAirPortNames(lowerIndex, j);
        if (i < higherIndex)
        	quickSortObjectAirPortNames(i, higherIndex);
    }
 
    // Here Exchanging objects on the basis of Pivot
    private void exchangeObjectAirPortNames(int i, int j) 
    {
    	AirportNamesDO arrListCurrencyDoTemp = airportNamesDOs.get(i);
    	airportNamesDOs.set(i, airportNamesDOs.get(j));
    	airportNamesDOs.set(j, arrListCurrencyDoTemp);
    }
    
    
}
