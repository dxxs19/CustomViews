package com.wei.customviews.test.designpattern.observer.cusobserver;

/**
 * author: WEI
 * date: 2017/7/14
 */

public class Student implements Observer
{
    private String name;

    public Student(String name)
    {
        this.name = name;
    }

    @Override
    public void update() {
        System.out.println(name + "更新！");
    }
}
