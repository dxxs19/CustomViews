package com.wei.customviews.test.designpattern.abstractfactory;

/**
 * 定义：为创建一组相关或者是相互依赖的对象提供一个接口，而不需要指定它们的具体类。
 * 使用场景：一个对象族有相同的约束时可以使用抽象工厂模式。举个例子：Android、iOS、WP下都有短信软件
 *           和拨号软件，两者都属于Software软件的范畴，但是，它们所在的操作系统平台不一样，即便是同
 *           一家公司出品的软件，其代码的实现逻辑也是不同的，这时候就可以考虑使用抽象工厂方法模式来
 *           产生Android、iOS、WP下的短信软件和拨号软件。
 * author: WEI
 * date: 2017/3/14
 */

public class Client
{
    public static void main(String[] args) {
        CarFactory Q3 = new Q3Factory();
        Q3.createTire().tire();
        Q3.createEngine().engine();
        Q3.createBrake().brake();

        System.out.println("-----------------------------");

        CarFactory Q7 = new Q7Factory();
        Q7.createTire().tire();
        Q7.createEngine().engine();
        Q7.createBrake().brake();
    }
}
