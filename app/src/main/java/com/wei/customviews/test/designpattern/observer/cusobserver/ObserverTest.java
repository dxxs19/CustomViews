package com.wei.customviews.test.designpattern.observer.cusobserver;

/**
 * author: WEI
 * date: 2017/7/14
 */

public class ObserverTest
{
    public static void main(String[] args) {
        Subject subject = new Teacher();
        Observer observer1 = new Student("甲");
        Observer observer2 = new Student("乙");
        Observer observer3 = new Student("丙");
        subject.add(observer1);
        subject.add(observer2);
        subject.add(observer3);
        subject.operation();

        subject.delete(observer3);
        subject.operation();
    }
}
