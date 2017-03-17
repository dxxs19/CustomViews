package com.wei.customviews.view.activity;

import android.app.DownloadManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
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

import com.wei.customviews.IOnNewBookArrivedListener;
import com.wei.customviews.model.Book;
import com.wei.customviews.IBookManager;
import com.wei.customviews.R;
import com.wei.customviews.db.UserContentProvider;
import com.wei.customviews.db.UserDAO;
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

        mUserDAO = UserDAO.getInstance(this);
        mContentResolver = getContentResolver();

        downLoadData();

        bindService(new Intent(this, MessengerService.class), mConnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
//            mService = new Messenger(service);
//            Message message = Message.obtain(null, MessengerService.MSG_FROM_CLIENT);
//            Bundle data = new Bundle();
//            data.putString("msg", "Hello, this is client.");
//            message.setData(data);
//
//            // 通过该行代码把messenger传递给服务端，这时服务端才能向客户端发送消息
//            message.replyTo = mGetReplyMessenger;
//
//            try {
//                mService.send(message);
//            } catch (RemoteException e) {
//                e.printStackTrace();
//            }

            IBookManager iBookManager = IBookManager.Stub.asInterface(service);
            mRemoteBookManager = iBookManager;
            try {
                List<Book> books = iBookManager.getBookList();
                LogUtil.e(TAG, "query book list:" + books.toString());
                Book book = new Book(3, "Android 开发艺术探索");
                iBookManager.addBook(book);
                List<Book> newBooks = iBookManager.getBookList();
                LogUtil.e(TAG, "query book list:" + newBooks.toString());
                iBookManager.registerListener(mIOnNewBookArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
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
        unbindService(mConnection);
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
        }
        return true;
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

    private int count = 10;
    private String url = "http://imtt.dd.qq.com/16891/F7175C88B72B1691ECB9036C9479BD07.apk?fsname=com.tmall.wireless_5.30.3_1582.apk&csr=1bbd";
    @Override
    protected void onResume() {
        super.onResume();

//        mHandler.obtainMessage();
//        mHandler.sendEmptyMessage(UPDATE);
        observer();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                new MultiThreadDownload.Builder(MainActivity.this)
//                        .url(url)
//                        .threadSize(5)
//                        .fileName("天猫.apk")
//                        .localDir(Environment.getExternalStorageDirectory() + "/aaa")
//                        .setOnLoadingListener(new OnLoadingListener() {
//                            @Override
//                            public void onSuccess() {
//                                Log.e(TAG, "下载成功！");
//                            }
//
//                            @Override
//                            public void onFailure(String errorMsg) {
//                                Log.e(TAG, "下载失败！" + errorMsg);
//                            }
//
//                            @Override
//                            public void onLoading(float total, float current) {
//                                DecimalFormat decimalFormat = new DecimalFormat("0.00");
//                                float percent = current / total * 100;
//                                String percentStr = decimalFormat.format(percent);
//                                Log.e(TAG, "已下载：" + current + ", 即：" + percentStr + "%");
//                            }
//                        })
//                        .create()
//                        .download();
//            }
//        }).start();

        DownloadService.startActionFoo(this, url, "天猫.apk");
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
        Thread.dumpStack();
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
