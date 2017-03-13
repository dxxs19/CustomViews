package com.wei.customviews.test.designpattern.singleton;

/**
 * 定义：确保某一个类只有一个实例，而且自行实例化并向整个系统提供这个实例。
 * 使用场景：确保某个类有且只有一个对象的场景，避免产生多个对象消耗过多的资源，或者某种类型的对象只
 *           应该有且只有一个。例如，创建一个对象需要消耗的资源过多，如要访问IO和数据库等资源，这时
 *           就要考虑使用单例模式。
 * author: WEI
 * date: 2017/3/13
 */

public class Singleton
{
    private static Singleton sInstance;

    public Singleton()
    {

    }

    public static Singleton getInstance()
    {
        if (null == sInstance)
        {
            synchronized (Singleton.class)
            {
                if (null == sInstance)
                {
                    sInstance = new Singleton();
                }
            }
        }
        return sInstance;
    }

    public static void main(String[] args) {
        Singleton singleton1 = Singleton.getInstance();
        Singleton singleton2 = Singleton.getInstance();

        System.out.println(singleton1 + ", " + singleton2 + ", " + (singleton1 == singleton2));
    }
}
