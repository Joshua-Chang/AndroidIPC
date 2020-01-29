package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.IAddMangerService;
import com.example.binder.IMyAidlInterface;

public class MainActivity extends AppCompatActivity {

    private IAddMangerService mInterface;
    private ServiceConnection conn=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e(TAG, "connect success");
            Log.e(TAG, "a、跨进程service");
            mInterface = IAddMangerService.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e(TAG, "connect error");
        }
    };
    private String TAG=">>>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent=new Intent();
        intent.setAction("BinderServer_Action");
        intent.setPackage("com.example.binder");
        bindService(intent,conn,BIND_AUTO_CREATE);

        if (getIntent() != null) {
            int i = getIntent().getIntExtra("result", 0);
            ((TextView) findViewById(R.id.tv)).setText("result="+i);
        }
    }

    public void start(View view) {
        if (mInterface != null) {
            try {
                Log.e(TAG, "add跨进程service调用开始--------------------------");
                mInterface.add(10);
                Log.e(TAG, "add跨进程service调用结束--------------------------");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
