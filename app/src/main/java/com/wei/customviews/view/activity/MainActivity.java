package com.wei.customviews.view.activity;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.Toast;

import com.wei.customviews.IComputeInterface;
import com.wei.customviews.IOnNewBookArrivedListener;
import com.wei.customviews.ISecurityCenterInterface;
import com.wei.customviews.db.AppAdvsDao;
import com.wei.customviews.model.Book;
import com.wei.customviews.IBookManager;
import com.wei.customviews.R;
import com.wei.customviews.db.UserContentProvider;
import com.wei.customviews.db.UserDAO;
import com.wei.customviews.model.modelImpl.ComputeImpl;
import com.wei.customviews.model.modelImpl.SecurityCenterImpl;
import com.wei.customviews.service.BinderPool;
import com.wei.customviews.service.DownloadService;
import com.wei.customviews.service.MessengerService;
import com.wei.customviews.test.designpattern.observer.SMSObserver;
import com.wei.customviews.view.AppBaseActivity;
import com.wei.customviews.view.adapter.RecyclerAdapter;
import com.wei.customviews.view.fragment.SlidingConflictFragment;
import com.wei.customviews.view.viewinterface.BookViewInterface;
import com.wei.utillibrary.net.MultiThreadDownload;
import com.wei.utillibrary.net.OnLoadingListener;
import com.wei.utillibrary.utils.LogUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppBaseActivity implements SlidingConflictFragment.OnFragmentInteractionListener, BookViewInterface
{
    @ViewById(R.id.rc_view)
    RecyclerView mRecyclerView;
    @ViewById(R.id.imgView)
    ImageView mImageView;
    private RecyclerAdapter mRecyclerAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<String> mData = new ArrayList<>();
    private UserDAO mUserDAO;
    private AppAdvsDao mAppAdvsDao;
    private ContentResolver mContentResolver;
    private Messenger mService;
    private IBookManager mRemoteBookManager;

    @AfterViews
    void initView()
    {
        TAG = getClass().getSimpleName();

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        for (int i = 0; i < 10; i ++)
        {
            mData.add("Recycler" + i);
        }

        mRecyclerAdapter = new RecyclerAdapter(mData);
        mRecyclerView.setAdapter(mRecyclerAdapter);

//        mUserDAO = UserDAO.getInstance(this);
//        mContentResolver = getContentResolver();

        mAppAdvsDao = AppAdvsDao.getInstance(this);
        downLoadData();

//        bindService(new Intent(this, MessengerService.class), mConnection, Context.BIND_AUTO_CREATE);
        bindIPCService();

        new Thread(new Runnable() {
            @Override
            public void run() {
                doWork();
            }
        }).start();

        String cacheDir = getCacheDir().getPath();
        String externalCacheDir = getExternalCacheDir().getPath();
        Log.e(TAG, "cacheDir = " + cacheDir + ", externalCacheDir = " + externalCacheDir);
    }

    ISecurityCenterInterface mSecurityCenterInterface;
    IComputeInterface mComputeInterface;
    private void doWork() {
        BinderPool binderPool = BinderPool.getInstance(this);
        IBinder securityBinder = binderPool.queryBinder(BinderPool.BINDER_SECURITY_CENTER);
        mSecurityCenterInterface = SecurityCenterImpl.asInterface(securityBinder);
        Log.e(TAG, "visit ISecurityCenter");
        String msg = "Hello Android!";
        try {
            String password = mSecurityCenterInterface.encrypt(msg);
            Log.e(TAG, "encrypt : " + password);
            Log.e(TAG, "decrypt : " + mSecurityCenterInterface.decrypt(password));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "visit ICompute");
        IBinder computeBinder = binderPool.queryBinder(BinderPool.BINDER_COMPUTE);
        mComputeInterface = ComputeImpl.asInterface(computeBinder);
        try {
            Log.e(TAG, " 3 + 5 = " + mComputeInterface.add(3, 5));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void bindIPCService()
    {
        Intent intent = new Intent();
        intent.setAction("com.wei.action.IPC_MESSAGE");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        PackageManager packageManager = getPackageManager();
        ResolveInfo resolveInfo = packageManager.resolveService(intent, 0);
        if (resolveInfo != null)
        {
            String pkName = resolveInfo.serviceInfo.packageName;
            String serviceName = resolveInfo.serviceInfo.name;
            Log.e(TAG, "packageName : " + pkName + ", serviceName : " + serviceName);
            ComponentName componentName = new ComponentName(pkName, serviceName);
            intent.setComponent(componentName);
            bindService(intent, mConnection, BIND_AUTO_CREATE);
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = new Messenger(service);
            Message message = Message.obtain(null, MessengerService.MSG_FROM_CLIENT);
            Bundle data = new Bundle();
            data.putString("msg", "Hello, this is client.");
            message.setData(data);

            // 通过该行代码把messenger传递给服务端，这时服务端才能向客户端发送消息
            message.replyTo = mGetReplyMessenger;

            try {
                mService.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

//            IBookManager iBookManager = IBookManager.Stub.asInterface(service);
//            mRemoteBookManager = iBookManager;
//            try {
//                List<Book> books = iBookManager.getBookList();
//                LogUtil.e(TAG, "query book list:" + books.toString());
//                Book book = new Book(3, "Android 开发艺术探索");
//                iBookManager.addBook(book);
//                List<Book> newBooks = iBookManager.getBookList();
//                LogUtil.e(TAG, "query book list:" + newBooks.toString());
//                iBookManager.registerListener(mIOnNewBookArrivedListener);
//            } catch (RemoteException e) {
//                e.printStackTrace();
//            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mRemoteBookManager = null;
            LogUtil.e(TAG, "binder died.");
        }
    };

    private IOnNewBookArrivedListener mIOnNewBookArrivedListener = new IOnNewBookArrivedListener.Stub()
    {

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public void onNewBookArrived(Book newBook) throws RemoteException {
            mHandler.obtainMessage(MESSAGE_NEWBOOK_ARRIVED, newBook).sendToTarget();
        }
    };

    private Messenger mGetReplyMessenger = new Messenger(new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what)
            {
                case MessengerService.MSG_FROM_SERVICE:
                    LogUtil.e(TAG, "receive msg from service:" + msg.getData().getString("reply"));
                    break;

                default:
                    super.handleMessage(msg);
            }
        }
    });

    @Override
    protected void onDestroy() {
        if (mRemoteBookManager != null && mRemoteBookManager.asBinder().isBinderAlive())
        {
            LogUtil.e(TAG, "unregister listener:" + mIOnNewBookArrivedListener);
            try {
                mRemoteBookManager.unregisterListener(mIOnNewBookArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
//        unbindService(mConnection);
        super.onDestroy();
    }

    private void downLoadData() {
        mRequestCallback = new AbstractRequestCallback() {
            @Override
            public void onSuccess(String content) {
                Log.e(TAG, "--- Success ---");
            }

            @Override
            public void onFail(String errorMsg) {
                // 需要特殊处理代码，否则不需要重写onFail方法
//                super.onFail(errorMsg);
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.add_item:
                addData();
                break;

            case R.id.remove_item:
                deleteData();
                break;

            case R.id.query_item:
                queryData();
                break;

            case R.id.update_item:
                updateData();
                break;

            case R.id.rx_item:
                testRx();
                break;
        }
        return true;
    }

    Observer mToastObserver = new Observer<String>() {
        @Override
        public void onCompleted() {
            Log.e(TAG, "完成！");
        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(String o) {
            Log.e(TAG, o);
            Toast.makeText(MainActivity.this, o, Toast.LENGTH_SHORT).show();
        }
    };

    private void testRx()
    {
        Subscriber subscriber=new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Log.i(TAG,"onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG,"onError");
            }

            @Override
            public void onNext(String s) {
                Log.i(TAG,"onNext"+s);
            }

            @Override
            public void onStart() {
                Log.i(TAG,"onStart");
            }
        };

        Observable observable;
        // 创建 Observable(被观察者)
        observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("杨影枫");
                subscriber.onNext("月眉儿");
                subscriber.onCompleted();
            }
        });
//        observable = Observable.just("cai", "xiang", "wei");

        observable.observeOn(AndroidSchedulers.mainThread());
        observable.subscribe(subscriber);
        observable.subscribe(mToastObserver);
    }

    private void updateData()
    {
        Uri uri = Uri.parse(UserContentProvider.CONTENT_URI + "/user/update");
        ContentValues values = new ContentValues();
        values.put("password", "ajdjfljadj");
        values.put("username", "大美女");
        int count = mContentResolver.update(uri, values, "_id = ?", new String[]{"3"});
        LogUtil.e(TAG, "更新了" + count);
    }

    private void deleteData()
    {
        Uri uri = Uri.parse(UserContentProvider.CONTENT_URI + "/user/delete");
        int count = mContentResolver.delete(uri, "_id = ?", new String[]{"1"});
        LogUtil.e(TAG, "删除了" + count);
    }

    private void queryData()
    {
//        Cursor cursor = mUserDAO.queryAllUsers();
        Uri uri = Uri.parse(UserContentProvider.CONTENT_URI + "/user/queryAll");
        Cursor cursor = mContentResolver.query(uri, new String[]{"_id", "password", "username"}, "_id > ?", new String[]{"0"}, "_id");

        if (cursor.moveToFirst())
        {
            do{
                int id = cursor.getInt(cursor.getColumnIndex("_id"));
                String name = cursor.getString(cursor.getColumnIndex("username"));
                String pass = cursor.getString(cursor.getColumnIndex("password"));
                LogUtil.e(TAG, "--- info --- " + id + ", " + name + ", " + pass);
            }
            while (cursor.moveToNext());
        }
    }

    private void addData()
    {
        ContentValues values = new ContentValues();
        values.put("username", "caixiangwei");
        values.put("password", "15458787");
//        mUserDAO.addUser(values);
        Uri uri = Uri.parse(UserContentProvider.CONTENT_URI + "/user/insert");
        uri = mContentResolver.insert(uri, values);
        long id = ContentUris.parseId(uri);
        LogUtil.e(TAG, "插入的id:" + id);
    }

    private String url = "http://imtt.dd.qq.com/16891/83BED463469515748C7324A1A49938D6.apk?fsname=com.wei.multicheck_1.0.0_1.apk&csr=1bbd";
    private String url2 = "http://imtt.dd.qq.com/16891/2149FDEB2935A9301F59F784E6E63859.apk?fsname=com.wei.autolinkwifi_1.0.1_2.apk&csr=1bbd";
    String urls[] = new String[]{url, url2};
    @Override
    protected void onResume() {
        super.onResume();
//        mHandler.obtainMessage();
//        mHandler.sendEmptyMessage(UPDATE);
        observer();

//        for (int i = 0; i < urls.length; i ++)
//        {
//            DownloadService.startActionFoo(this, urls[i], i + ".apk", i);
//        }
    }

    private void observer()
    {
        // 被观察的角色
//        Teacher teacher = new Teacher();
//
//        // 观察者
//        Student student1 = new Student("wei", "男");
//        Student student2 = new Student("Maggie", "女");
//        Student student3 = new Student("Sora", "女");
//
//        // 将观察者注册到可观察对象的观察者列表中
//        teacher.addObserver(student1);
//        teacher.addObserver(student2);
//        teacher.addObserver(student3);
//
//        // 发布消息
//        teacher.publishHomework("画出几何代数中的数轴！");

        getContentResolver().registerContentObserver(Uri.parse("content://sms"), true, new SMSObserver(new Handler(){
            @Override
            public void handleMessage(Message msg)
            {
                super.handleMessage(msg);
                LogUtil.e(TAG, msg.what + ", " + msg.obj + ", " + msg.toString());
            }
        }));
    }

    private final int UPDATE = 0x011;
    private final int MESSAGE_NEWBOOK_ARRIVED = 0x022;
    Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case UPDATE:
                    mImageView.setImageResource(R.mipmap.ic_launcher);
                    break;

                case MESSAGE_NEWBOOK_ARRIVED:
                    LogUtil.e(TAG, "receive new book:" + msg.obj);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        LogUtil.e(TAG, "--- onTouchEvent(MotionEvent event) ---");
//        Thread.dumpStack();
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                LogUtil.e(TAG, "--- ACTION_DOWN ---");
                break;

            case MotionEvent.ACTION_MOVE:
                LogUtil.e(TAG, "--- ACTION_MOVE ---");
                break;

            case MotionEvent.ACTION_UP:
                LogUtil.e(TAG, "--- ACTION_UP ---");
                break;

            case MotionEvent.ACTION_CANCEL:
                LogUtil.e(TAG, "--- ACTION_CANCEL ---");
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        LogUtil.e(TAG, "--- dispatchTouchEvent(MotionEvent ev) ---");
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        new MyTask().execute("wei");
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void showBooks(List<Book> books) {
        for (Book book:books)
        {
            LogUtil.e(TAG, book.bookName);
        }
    }

    @Override
    public void showLoading() {
        LogUtil.e(TAG, "--- showLoading ---");
    }

    @Override
    public void hideLoading() {
        LogUtil.e(TAG, "--- hideLoading ---");
    }

    class MyTask extends AsyncTask<String, Integer, String>
    {
        @Override
        protected String doInBackground(String... params)
        {
            return params[0];
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            Log.e(TAG, "已完成" + values);
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e(TAG, "result : " + s);
        }
    }
}
