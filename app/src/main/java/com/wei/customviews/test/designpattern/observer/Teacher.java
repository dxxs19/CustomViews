package com.wei.customviews.test.designpattern.observer;

import java.util.Observable;

/**
 * 定义：定义对象间一种一对多的依赖关系，使得每当一个对象改变状态，则所有依赖于它的对象都会得到通知并被自动更新。
 * 使用场景：关联行为场景，需要注意的是，关联行为是可拆分的，而不是“组合”关系；
 *           事件多级触发场景；
 *           跨系统的消息交换场景，如消息队列、事件总线的处理机制。
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
