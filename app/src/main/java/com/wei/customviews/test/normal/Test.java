package com.wei.customviews.test.normal;

import java.text.DecimalFormat;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

/**
 * author: WEI
 * date: 2017/3/17
 */

public class Test
{
    public static void main(String[] args) throws ClassNotFoundException {
        DecimalFormat decimalFormat = new DecimalFormat("0.000");
        float percent = 1.0254f;
        String percentStr = decimalFormat.format(percent);
        System.out.println(percentStr);

        ClassLoader loader = ClassLoader.getSystemClassLoader();
        // 仅是加载InitClass
        loader.loadClass("com.wei.customviews.test.normal.InitClass");
        System.out.println("系统加载InitClass类");
        // 真正初始化InitClass
        Class.forName("com.wei.customviews.test.normal.InitClass");

        Flowable.just("Hello world").subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                System.out.println(s);
            }
        });
    }

}

class InitClass
{
    static
    {
        System.out.println("InitClass类静态初始化块......");
    }
}
