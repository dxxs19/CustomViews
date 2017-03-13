package com.wei.customviews.test.designpattern.builder;

/**
 * 定义：将一个复杂对象的构建与它的表示分离，使得同样的构建过程可以创建不同的表示。
 * 使用场景：1.相同的方法，不同的执行顺序，产生不同的事件结果时；
 *           2.多个部件或零件，都可以装配到一个对象中，但是产生的运行结果又不相同时；
 *           3.产品类非常复杂，或者产品类中的调用顺序不同产生了不同的作用，这个时候使用建造者模式非常合适。
 *           4.当初始化一个对象特别复杂，如参数多，且很多参数都具有默认值时。
 * author: WEI
 * date: 2017/3/13
 */

public class Girl
{
    Builder mBuilder;

    public Girl(Builder builder)
    {
        this.mBuilder = builder;
    }

    public static class Builder
    {
        private String name;
        private String sex;
        private String favorite;
        private int age;
        private float height;
        private float weight;

        public Builder name(String name)
        {
            this.name = name;
            return this;
        }

        public Builder sex(String sex)
        {
            this.sex = sex;
            return this;
        }

        public Builder favorite(String favorite)
        {
            this.favorite = favorite;
            return this;
        }

        public Builder age(int age)
        {
            this.age = age;
            return this;
        }

        public Builder height(float height)
        {
            this.height = height;
            return this;
        }

        public Builder weight(float weight)
        {
            this.weight = weight;
            return this;
        }

        public Girl create()
        {
            return new Girl(this);
        }
    }

    public static void main(String[] args) {
        Girl girl = new Builder()
                .name("xiaowei")
                .sex("girl")
                .weight(45)
                .height(167)
                .age(22)
                .favorite("sing")
                .create();

        Builder builder = girl.mBuilder;
        System.out.println(builder.name + ", " + builder.sex + ", " + builder.favorite + ", " +
                "" + builder.height + ", " + builder.weight + ", " + builder.age);
    }
}
