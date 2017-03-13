package com.wei.customviews.test.designpattern.factory;

/**
 * 定义：定义一个用于创建对象的接口，让子类决定实例化哪个类。
 * 使用场景：在任何需要生成复杂对象的地方，都可以使用工厂方法模式。复杂对象适合使用工厂模式，用new
 *           就可以完成创建的对象无需使用工厂模式。
 * author: WEI
 * date: 2017/3/13
 */

public class Client
{
    public static void main(String[] args) {
        AudiFactory audiFactory = new AudiCarFactory();
        AudiQ3 audiQ3 = audiFactory.createAudiCar(AudiQ3.class);
        audiQ3.drive();
        audiQ3.selfNavigation();

        AudiQ7 audiQ7 = audiFactory.createAudiCar(AudiQ7.class);
        audiQ7.drive();
        audiQ7.selfNavigation();
    }
}
