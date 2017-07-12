package com.wei.customviews.test.normal;

import java.text.DecimalFormat;

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
    }

}

class InitClass
{
    static
    {
        System.out.println("InitClass类静态初始化块......");
    }
}
