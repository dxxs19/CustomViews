package com.wei.customviews.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import com.wei.customviews.Book;
import com.wei.customviews.IBookManager;
import com.wei.utillibrary.utils.LogUtil;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MessengerService extends Service
{
    public final static int MSG_FROM_CLIENT = 0x11;
    public final static int MSG_FROM_SERVICE = 0x12;
    private static final String TAG = "MessengerService";
    private CopyOnWriteArrayList<Book> mBooks = new CopyOnWriteArrayList<>();

    private Binder mBinder = new IBookManager.Stub() {
        @Override
        public List<Book> getBookList() throws RemoteException {
            return mBooks;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            mBooks.add(book);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        mBooks.add(new Book(1, "Android"));
        mBooks.add(new Book(2, "IOS"));
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
}
