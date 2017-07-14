package com.wei.customviews.test.designpattern.observer.cusobserver;

import java.util.Enumeration;
import java.util.Vector;

/**
 * author: WEI
 * date: 2017/7/14
 */

public  abstract class AbstractSubject implements Subject
{
    Vector<Observer> mObservers = new Vector<>();

    @Override
    public void add(Observer observer) {
        mObservers.add(observer);
    }

    @Override
    public void delete(Observer observer) {
        mObservers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        Enumeration<Observer> enumeration = mObservers.elements();
        while(enumeration.hasMoreElements())
        {
            enumeration.nextElement().update();
        }
    }

}
