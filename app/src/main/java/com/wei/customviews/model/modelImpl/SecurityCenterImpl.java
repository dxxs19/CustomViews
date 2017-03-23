package com.wei.customviews.model.modelImpl;

import android.os.RemoteException;

import com.wei.customviews.ISecurityCenterInterface;

/**
 * author: WEI
 * date: 2017/3/23
 */

public class SecurityCenterImpl extends ISecurityCenterInterface.Stub
{
    private static final char SECURITY_CODE = '^';

    @Override
    public String encrypt(String content) throws RemoteException {
        char[] chars = content.toCharArray();
        for (int i = 0; i < chars.length; i ++)
        {
            chars[i] ^= SECURITY_CODE;
        }
        return new String(chars);
    }

    @Override
    public String decrypt(String password) throws RemoteException {
        return encrypt(password);
    }
}
