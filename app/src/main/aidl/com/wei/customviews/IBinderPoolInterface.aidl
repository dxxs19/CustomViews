// IBinderPoolInterface.aidl
package com.wei.customviews;

// Declare any non-default types here with import statements

interface IBinderPoolInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
//    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
//            double aDouble, String aString);

    IBinder queryBinder(int binderCode);
}
