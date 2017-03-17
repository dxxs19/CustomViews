package com.wei.customviews.test.normal;

import java.text.DecimalFormat;

/**
 * author: WEI
 * date: 2017/3/17
 */

public class Test
{
    public static void main(String[] args) {
        DecimalFormat decimalFormat = new DecimalFormat("0.000");
        float percent = 1.0254f;
        String percentStr = decimalFormat.format(percent);
        System.out.println(percentStr);
    }
}
