package com.wei.customviews.test.designpattern.observer.cusobserver;

/**
 * author: WEI
 * date: 2017/7/14
 */

public interface Subject
{
    void add(Observer observer);

    void delete(Observer observer);

    void notifyObservers();

    void operation();
}
