package com.wei.customviews.test.designpattern.strategy;

/**
 * 定义：策略模式定义了一系列的算法，并将每一个算法封装起来，而且使它们还可以相互替换。
 *       策略模式让算法独立于使用它的客户而独立变化。
 * 使用场景：针对同一类型问题的多种处理方式，仅仅是具体行为有差别时；
 *           需要安全地封装多种同一类型的操作时；
 *           出现同一抽象类有多个子类，而又需要使用if-else或者switch-case来选择具体子类时。
 * author: WEI
 * date: 2017/3/14
 */

public class TrafficCalculator
{
    CalculateStrategy mCalculateStrategy;

    public void setCalculateStrategy(CalculateStrategy calculateStrategy) {
        mCalculateStrategy = calculateStrategy;
    }

    public int calculatePrice(int km)
    {
        return mCalculateStrategy.calculatePrice(km);
    }

    public static void main(String[] args) {
        TrafficCalculator trafficCalculator = new TrafficCalculator();
        trafficCalculator.setCalculateStrategy(new MetroStrategy());
        System.out.println("地铁乘15公里的价格：" + trafficCalculator.calculatePrice(15));
//        trafficCalculator.setCalculateStrategy(new TaxiStrategy());
//        System.out.println("的士乘100公里的价格：" + trafficCalculator.calculatePrice(100));
    }
}
