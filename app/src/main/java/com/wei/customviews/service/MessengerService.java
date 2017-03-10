package com.wei.customviews.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteCallbackList;
import android.os.RemoteException;

import com.wei.customviews.IOnNewBookArrivedListener;
import com.wei.customviews.model.Book;
import com.wei.customviews.IBookManager;
import com.wei.utillibrary.utils.LogUtil;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class MessengerService extends Service
{
    public final static int MSG_FROM_CLIENT = 0x11;
    public final static int MSG_FROM_SERVICE = 0x12;
    private static final String TAG = "MessengerService";
    private AtomicBoolean mIsServiceDestroyed = new AtomicBoolean(false);
    private CopyOnWriteArrayList<Book> mBooks = new CopyOnWriteArrayList<>();
    private RemoteCallbackList<IOnNewBookArrivedListener> mArrivedListeners = new RemoteCallbackList<>();

    private Binder mBinder = new IBookManager.Stub() {
        @Override
        public List<Book> getBookList() throws RemoteException {
            return mBooks;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            mBooks.add(book);
        }

        @Override
        public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {
//            if (!mArrivedListeners.contains(listener))
//            {
//                mArrivedListeners.add(listener);
//            }
//            else
//            {
//                LogUtil.e(TAG, "already exist.");
//            }
//            LogUtil.e(TAG, "registerListener, size:" + mArrivedListeners.size());
            mArrivedListeners.register(listener);
        }

        @Override
        public void unregisterListener(IOnNewBookArrivedListener listener) throws RemoteException {
//            if (mArrivedListeners.contains(listener))
//            {
//                mArrivedListeners.remove(listener);
//                LogUtil.e(TAG, "unregisterlistener succeed.");
//            }
//            else
//            {
//                LogUtil.e(TAG, "not found, can not unregister.");
//            }
//            LogUtil.e(TAG, "unregisterListener, current size:" + mArrivedListeners.size());
            mArrivedListeners.unregister(listener);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        mBooks.add(new Book(1, "Android"));
        mBooks.add(new Book(2, "IOS"));
        // 观察者模式
//        new Thread(new ServiceWorker()).start();
    }

    public MessengerService() {
    }

    private static class MessengerHandler extends Handler
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case MSG_FROM_CLIENT:
                    LogUtil.e(TAG, "receive msg from Client:" + msg.getData().getString("msg"));
                    Messenger client = msg.replyTo; // 获取客户端传递过来的Messenger
                    Message replyMsg = Message.obtain(null, MSG_FROM_SERVICE);
                    Bundle bundle = new Bundle();
                    bundle.putString("reply", "您的消息我已经收到，稍后回复您！");
                    replyMsg.setData(bundle);
                    try {
                        client.send(replyMsg);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;

                default:
                    super.handleMessage(msg);
            }
        }
    }

    private final Messenger mMessenger = new Messenger(new MessengerHandler());

    @Override
    public IBinder onBind(Intent intent) {
//        return mMessenger.getBinder();
        return mBinder;
    }

    @Override
    public void onDestroy() {
        mIsServiceDestroyed.set(true);
        super.onDestroy();
    }

    private void onNewBookArrived(Book book) throws RemoteException {
        mBooks.add(book);
        int size = mArrivedListeners.beginBroadcast();
        LogUtil.e(TAG, "onNewBookArrived, notify listeners:" + size);
        for (int i = 0; i < size; i ++)
        {
            IOnNewBookArrivedListener listener = mArrivedListeners.getBroadcastItem(i);
            LogUtil.e(TAG, "onNewBookArrived, notify listener:" + listener);
            if (listener != null)
            {
                listener.onNewBookArrived(book);
            }
        }
        mArrivedListeners.finishBroadcast();
    }

    private class ServiceWorker implements Runnable
    {

        @Override
        public void run() {
            while (!mIsServiceDestroyed.get())
            {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int bookId = mBooks.size() + 1;
                Book newBook = new Book(bookId, "new Book # " + bookId);
                try {
                    onNewBookArrived(newBook);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
