package com.winit.airarabia.utils;

import java.util.Vector;

import com.winit.airarabia.objects.FlightSegmentDO;
import com.winit.airarabia.objects.OriginDestinationOptionDO;


public class QuickSort 
{
    
    private int array[];
	private Vector<FlightSegmentDO> vector;
    private int length;
 
    public void sort(Vector<FlightSegmentDO> vector) 
    {
        if (vector == null || vector.size() == 0) {
            return;
        }
        this.vector = vector;
        length = vector.size();
        quickSortObject(0, length - 1);
    }
 
    private void quickSortObject(int lowerIndex, int higherIndex) 
    {
         
        int i = lowerIndex;
        int j = higherIndex;
        // calculate pivot number, I am taking pivot as middle index number
        FlightSegmentDO pivot = vector.get(lowerIndex+(higherIndex-lowerIndex)/2);
        // Divide into two arrays
        while (i <= j) {
            /**
             * In each iteration, we will identify a number from left side which
             * is greater then the pivot value, and also we will identify a number
             * from right side which is less then the pivot value. Once the search
             * is done, then we exchange both numbers.
             */
            while (vector.get(i).departureDateTimeInMillies < pivot.departureDateTimeInMillies) 
            {
                i++;
            }
            while (vector.get(j).departureDateTimeInMillies > pivot.departureDateTimeInMillies) 
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
        // call quickSort() method recursively
        if (lowerIndex < j)
        	quickSortObject(lowerIndex, j);
        if (i < higherIndex)
        	quickSortObject(i, higherIndex);
    }
 
    private void exchangeObject(int i, int j) 
    {
    	FlightSegmentDO temp = vector.get(i);
        vector.set(i, vector.get(j));
        vector.set(j, temp);
    }
     
   /***********************************Sorting OriginDestinationOptionDO***************************************************/
    private Vector<OriginDestinationOptionDO> vectorOriginDestinationOptionDO;
    public void sortOriginDestinationOptionDO(Vector<OriginDestinationOptionDO> vector) 
    {
        if (vector == null || vector.size() == 0) {
            return;
        }
        this.vectorOriginDestinationOptionDO = vector;
        length = vector.size();
        quickSortOriginDestinationOptionDO(0, length - 1);
    }
 
    private void quickSortOriginDestinationOptionDO(int lowerIndex, int higherIndex) 
    {
         
        int i = lowerIndex;
        int j = higherIndex;
        // calculate pivot number, I am taking pivot as middle index number
        OriginDestinationOptionDO pivot = vectorOriginDestinationOptionDO.get(lowerIndex+(higherIndex-lowerIndex)/2);
        // Divide into two arrays
        while (i <= j) {
            /**
             * In each iteration, we will identify a number from left side which
             * is greater then the pivot value, and also we will identify a number
             * from right side which is less then the pivot value. Once the search
             * is done, then we exchange both numbers.
             */
            while (vectorOriginDestinationOptionDO.get(i).vecFlightSegmentDOs.get(0).departureDateTimeInMillies < pivot.vecFlightSegmentDOs.get(0).departureDateTimeInMillies) 
            {
                i++;
            }
            while (vectorOriginDestinationOptionDO.get(j).vecFlightSegmentDOs.get(0).departureDateTimeInMillies > pivot.vecFlightSegmentDOs.get(0).departureDateTimeInMillies) 
            {
                j--;
            }
            if (i <= j) {
            	exchangeOriginDestinationOptionDO(i, j);
                //move index to next position on both sides
                i++;
                j--;
            }
        }
        // call quickSort() method recursively
        if (lowerIndex < j)
        	quickSortOriginDestinationOptionDO(lowerIndex, j);
        if (i < higherIndex)
        	quickSortOriginDestinationOptionDO(i, higherIndex);
    }
 
    private void exchangeOriginDestinationOptionDO(int i, int j) 
    {
    	OriginDestinationOptionDO temp = vectorOriginDestinationOptionDO.get(i);
    	vectorOriginDestinationOptionDO.set(i, vectorOriginDestinationOptionDO.get(j));
    	vectorOriginDestinationOptionDO.set(j, temp);
    }
}
