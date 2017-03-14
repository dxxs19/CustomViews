package com.wei.customviews.test.designpattern.facade;

/**
 * 定义：要求一个子系统的外部与其内部的通信必须通过一个统一的对象进行。门面模式提供一个高层次的接口，
 *       使得子系统更易于使用。
 * author: WEI
 * date: 2017/3/14
 */

public class Test
{
    public static void main(String[] args) {
        MobilePhone mobilePhone = new MobilePhone();
        mobilePhone.takePicture();
        mobilePhone.videoChat();
    }
}
