package com.wei.customviews.test;

/**
 * author: WEI
 * date: 2017/2/27
 */

public class Search extends BaseSort
{
    public static void main(String[] args) {
        Search search = new Search();
        search.binarySearch();
    }

    private void binarySearch()
    {
        int[] sortArray = insertionSort();
        int targetIndex = binarySearch(sortArray, 0, sortArray.length - 1, 36);
        System.out.println("要查找的数字下标为：" + targetIndex);
    }

    /**
     * 插入排序
     */
    private int[] insertionSort() {
        outPutArray(sortArray);

        for (int out = 1; out < sortArray.length; out ++)
        {
            int temp = sortArray[out];
            int inner = out;
            while (inner > 0 && sortArray[inner - 1] >= temp)
            {
                sortArray[inner] = sortArray[inner - 1];
                inner -- ;
            }
            sortArray[inner] = temp;
        }

        outPutArray(sortArray);
        return sortArray;
    }

    private int binarySearch(int[] array, int start, int end, int key)
    {
        int mid = (start + end)/2;
        if (array[mid] == key)
        {
            return mid;
        }
        if (start >= end)
            return -1;
        else if (key > array[mid])
        {
            return binarySearch(array, mid + 1, end, key);
        }
        else if (key < array[mid])
        {
            return binarySearch(array, start, mid - 1, key);
        }
        return -1;
    }
}
