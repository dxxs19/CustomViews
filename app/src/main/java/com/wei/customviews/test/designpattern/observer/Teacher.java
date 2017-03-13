package com.wei.customviews.test.designpattern.observer;

import java.util.Observable;

/**
 * author: WEI
 * date: 2017/3/10
 */

public class Teacher extends Observable
{
    public void publishHomework(String content)
    {
        // 标识状态或者内容发生改变
        setChanged();
        // 通知所有观察者
        notifyObservers(content);
    }

    public static void main(String[] args) {
        // 被观察的角色
        Teacher teacher = new Teacher();

        // 观察者
        Student student1 = new Student("wei", "男");
        Student student2 = new Student("Maggie", "女");
        Student student3 = new Student("Sora", "女");

        // 将观察者注册到可观察对象的观察者列表中
        teacher.addObserver(student1);
        teacher.addObserver(student2);
        teacher.addObserver(student3);

        // 发布消息
        teacher.publishHomework("画出几何代数中的数轴！");
    }
}
