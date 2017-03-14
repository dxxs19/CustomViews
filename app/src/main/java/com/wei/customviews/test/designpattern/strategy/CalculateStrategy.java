package com.wei.customviews.test.designpattern.strategy;

/**
 * author: WEI
 * date: 2017/3/14
 */

public interface CalculateStrategy
{
    /**
     * 按距离来计算价格
     * @param km 公里
     * @return 返回价格
     */
    int calculatePrice(int km);
}
