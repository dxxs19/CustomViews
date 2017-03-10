package com.wei.customviews.test.sort;

/**
 * author: WEI
 * date: 2017/2/27
 */

public class Sort extends BaseSort
{
    public static void main(String[] args)
    {
        Sort simpleSort = new Sort();
//        simpleSort.bubbleSort();
//        simpleSort.selectedSort();
//        simpleSort.insertionSort();
//        simpleSort.shellSort();
        simpleSort.quickSort();
    }

    private void quickSort()
    {
        recQuickSort(0, sortArray.length - 1);
    }

    private void recQuickSort(int left, int right)
    {
        outPutArray(sortArray);
        if (right - left <= 0)
        {
            return;
        }
        else
        {
            int pivot = sortArray[right];
            int partition = partitionIt(left, right, pivot);
            recQuickSort(left, partition-1);
            recQuickSort(partition + 1, right);
        }
        outPutArray(sortArray);
    }

    public int partitionIt(int left, int right, int pivot)
    {
        int leftPtr = left - 1;
        int rightPtr = right;
        while (true)
        {
            while (sortArray[++leftPtr] < pivot);
            while (rightPtr > 0 && sortArray[--rightPtr] > pivot);
            if (leftPtr >= rightPtr)
                break;
            else
            {
                swap(leftPtr, rightPtr);
            }
        }
        swap(leftPtr, right);
        return leftPtr;
    }

    /**
     * 希尔排序
     */
    private void shellSort()
    {
        outPutArray(sortArray);
        int inner, outer;
        int temp, size = sortArray.length;

        int h = 1;
        while (h <= size/3)
        {
            h = 3 * h + 1;
        }

        while (h > 0)
        {
            for (outer = h; outer < size; outer ++)
            {
                temp = sortArray[outer];
                inner = outer;
                while (inner > h-1 && sortArray[inner - h] >= temp)
                {
                    sortArray[inner] = sortArray[inner-h];
                    inner -= h;
                }
                sortArray[inner] = temp;
            }
            h = (h-1)/3;
        }
        outPutArray(sortArray);
    }

    /**
     * 归并排序
     * @param sortArray
     * @param low
     * @param high
     */
    private void mergeSort(int[] sortArray, int low, int high)
    {
        int mid = (high + low)/2;
        if (low < high)
        {
            // 左归并
            mergeSort(sortArray, low, mid);
            // 右归并
            mergeSort(sortArray, mid + 1, high);
            merge(sortArray, low, mid, high);
            outPutArray(sortArray);
        }
    }

    private void merge(int[] sortArray, int low, int mid, int high)
    {
        int[] mergeArray = new int[high-low + 1];
        int left = low; // 以中间为界，left表示左指针
        int right = mid + 1; // 以中间为界，right表示右指针
        int mergeIndex = 0;  // 合并数组的下标
        while (left <= mid && right <= high)
        {
            if (sortArray[left] < sortArray[right])
            {
                mergeArray[mergeIndex++] = sortArray[left++];
            }
            else
            {
                mergeArray[mergeIndex++] = sortArray[right++];
            }
        }

        while (left <= mid)
        {
            mergeArray[mergeIndex ++] = sortArray[left ++];
        }

        while (right <= high)
        {
            mergeArray[mergeIndex ++] = sortArray[right ++];
        }

        outPutArray(sortArray);
    }

    /**
     * 插入排序
     */
    private void insertionSort() {
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
    }

    /**
     * 选择排序
     */
    private void selectedSort()
    {
        outPutArray(sortArray);
        for (int i = 0; i < sortArray.length - 1; i ++)
        {
            int minIndex = i;
            for (int j = i + 1; j < sortArray.length; j ++)
            {
                if (sortArray[minIndex] > sortArray[j])
                {
                    minIndex = j;
                }
            }
            swap(i, minIndex);
        }
        outPutArray(sortArray);
    }

    /**
     * 冒泡排序
     */
    private void bubbleSort()
    {
        outPutArray(sortArray);
        for (int out = sortArray.length - 1; out >= 1; out --)
        {
            for (int inner = 0; inner < out; inner ++)
            {
                if (sortArray[inner] > sortArray[inner + 1] )
                {
                    swap(inner, inner + 1);
                }
            }
        }
        outPutArray(sortArray);
    }
}

