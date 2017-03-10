package com.wei.customviews.test.designmode;

import java.util.Observable;
import java.util.Observer;

/**
 * author: WEI
 * date: 2017/3/10
 */

public class Student implements Observer
{
    private String name;
    private String sex;

    public Student(String name, String sex) {
        this.name = name;
        this.sex = sex;
    }

    @Override
    public void update(Observable o, Object arg) {
        System.out.println(name + "收到作业通知，作业内容：" + arg);
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                '}';
    }
}
