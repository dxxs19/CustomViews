package com.wei.customviews.model.modelImpl;

import android.os.RemoteException;

import com.wei.customviews.IComputeInterface;

/**
 * author: WEI
 * date: 2017/3/23
 */

public class ComputeImpl extends IComputeInterface.Stub {
    @Override
    public int add(int a, int b) throws RemoteException {
        return a + b;
    }
}
