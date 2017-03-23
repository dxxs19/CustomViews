package com.wei.customviews.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.wei.customviews.IBinderPoolInterface;
import com.wei.customviews.model.modelImpl.ComputeImpl;
import com.wei.customviews.model.modelImpl.SecurityCenterImpl;

import java.util.concurrent.CountDownLatch;

/**
 * author: WEI
 * date: 2017/3/23
 */

public class BinderPool
{
    private final String TAG = getClass().getSimpleName();
    public static final int BINDER_NONE = -1;
    public static final int BINDER_COMPUTE = 0;
    public static final int BINDER_SECURITY_CENTER = 1;

    private Context mContext;
    private IBinderPoolInterface mBinderPoolInterface;
    private static volatile BinderPool sInstance;
    private CountDownLatch mConnectBinderPoolCountDownLatch;

    public BinderPool(Context context)
    {
        mContext = context.getApplicationContext();
        connectBinderPoolService();
    }

    public static BinderPool getInstance(Context context)
    {
        if (sInstance == null)
        {
            synchronized (BinderPool.class)
            {
                if (sInstance == null)
                {
                    sInstance = new BinderPool(context);
                }
            }
        }
        return sInstance;
    }

    private synchronized void connectBinderPoolService()
    {
        mConnectBinderPoolCountDownLatch = new CountDownLatch(1);
        Intent service = new Intent(mContext, BinderPoolService.class);
        mContext.bindService(service, mBinderPoolConnection, Context.BIND_AUTO_CREATE);

        try {
            mConnectBinderPoolCountDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public IBinder queryBinder(int binderCode)
    {
        IBinder binder = null;
        if (mBinderPoolInterface != null)
        {
            try {
                binder = mBinderPoolInterface.queryBinder(binderCode);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return binder;
    }

    private ServiceConnection mBinderPoolConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBinderPoolInterface = IBinderPoolInterface.Stub.asInterface(service);
            try {
                mBinderPoolInterface.asBinder().linkToDeath(mBinderPoolDeathRecipient, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            mConnectBinderPoolCountDownLatch.countDown();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private IBinder.DeathRecipient mBinderPoolDeathRecipient = new IBinder.DeathRecipient()
    {
        @Override
        public void binderDied() {
            Log.e(TAG, "binder died.");
            mBinderPoolInterface.asBinder().unlinkToDeath(mBinderPoolDeathRecipient, 0);
            mBinderPoolInterface = null;
            connectBinderPoolService();
        }
    };

    public static class BinderPoolImpl extends IBinderPoolInterface.Stub
    {
        public BinderPoolImpl()
        {
            super();
        }

        @Override
        public IBinder queryBinder(int binderCode) throws RemoteException {
            IBinder binder = null;
            switch (binderCode)
            {
                case BINDER_SECURITY_CENTER:
                    binder = new SecurityCenterImpl();
                    break;

                case BINDER_COMPUTE:
                    binder = new ComputeImpl();
                    break;
            }
            return binder;
        }
    }

}
