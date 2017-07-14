package com.wei.customviews.test.designpattern.observer.cusobserver;

/**
 * author: WEI
 * date: 2017/7/14
 */

public class Teacher extends AbstractSubject
{

    @Override
    public void operation()
    {
        System.out.println("update self!");
        notifyObservers();
    }
}
