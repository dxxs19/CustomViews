package com.wei.customviews.test.sort;

/**
 * author: WEI
 * date: 2017/2/27
 */

public class BaseSort
{
    protected int[] sortArray = {12, 4, 57, 89, 5, 69, 874, 36, 88, 5, 49, 68, 74};

    protected void swap(int i , int j)
    {
        int temp = sortArray[j];
        sortArray[j] = sortArray[i];
        sortArray[i] = temp;

    }

    protected void outPut(Object target)
    {
        System.out.print(target + "");
    }

    protected void outPutArray(int[] array)
    {
        for (int i = 0; i < array.length; i ++)
        {
            outPut(array[i] + ",");
            if (i == array.length - 1)
            {
                outPut("\n");
            }
        }
    }
}
