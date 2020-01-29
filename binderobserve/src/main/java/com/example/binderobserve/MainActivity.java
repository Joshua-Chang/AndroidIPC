package com.example.binderobserve;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    // 客户端需要注册IOnNewBookArrivedListener到远程服务端，当有新书时服务端才能通知客户端

    IBookManager mBinder;
    ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBinder = IBookManager.Stub.asInterface(service);
            Book newBook = new Book(3, "数学之美");
            try {
                mBinder.addBook(newBook);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBinder=null;
//            重连
//            Intent intent = new Intent(MainActivity.this, BookManagerService.class);
//            bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(MainActivity.this, BookManagerService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    public void start(View view) {
        try {
            List<Book> bookList = mBinder.getBookList();
//            Log.d(">>>", "onClick: " + bookList.getClass().getCanonicalName());
            for (Book book : bookList) {
                Log.d(">>>", "onClick: " + book.getIndex() + " " + book.getBookName());
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

/*
    服务端调用客户端的onNewBookArrived()，这个方法会在客户端的Binder线程池中执行，如果需要进行更新UI的操作，那么必须要切换线程。
*/
    IOnNewBookArrivedListener mListener = new IOnNewBookArrivedListener.Stub() {
        @Override
        public void onNewBookArrived(final Book newBook) throws RemoteException {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d(">>>", "onNewBookArrived: " + newBook.getIndex() + " " + newBook.getBookName());
                }
            });
        }
    };

    public void register(View view) {
        try {
            mBinder.registerListener(mListener);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void un_register(View view) {
        try {
            if (mBinder != null && mBinder.asBinder().isBinderAlive()) {
                try {
                    mBinder.unregisterListener(mListener);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            unbindService(mConnection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            if (mBinder == null) {
                return;
            }
            mBinder.asBinder().unlinkToDeath(mDeathRecipient, 0);
            mBinder = null;
            Intent intent = new Intent(MainActivity.this, BookManagerService.class);
            bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        }
    };
}
