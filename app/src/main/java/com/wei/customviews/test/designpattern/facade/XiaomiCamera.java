package com.wei.customviews.test.designpattern.facade;

/**
 * author: WEI
 * date: 2017/3/14
 */

public class XiaomiCamera implements Camera {
    @Override
    public void open() {
        System.out.println("打开相机");
    }

    @Override
    public void takePicture() {
        System.out.println("拍照");
    }

    @Override
    public void close() {
        System.out.println("关闭相机");
    }
}
