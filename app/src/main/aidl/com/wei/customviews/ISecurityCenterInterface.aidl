// ISecurityCenterInterface.aidl
package com.wei.customviews;

// Declare any non-default types here with import statements

interface ISecurityCenterInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
//    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
//            double aDouble, String aString);

    String encrypt(String content);
    String decrypt(String password);
}
