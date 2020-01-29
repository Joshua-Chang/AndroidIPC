package com.example.androidipc;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


/**
 * @版本号：
 * @需求编号：
 * @功能描述：
 * @创建时间：2020-01-26 17:45
 * @创建人：常守达
 */
public class MessageService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new Messenger(new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message message) {
                Log.e(">>>","MessageService服务端接收到--: "+message.getData().get("msg").toString());
                Messenger replyTo = message.replyTo;
                try {
                    Message msg = Message.obtain(null,200);            //创建一个what值为Constants.CLENT_MESSAGE的message
                    Bundle bundle = new Bundle();
                    bundle.putString("msg","我是来自服务端的回复");
                    msg.setData(bundle);
                    replyTo.send(msg);
                    Log.e(">>>","reply...");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                return true;
            }
        })).getBinder();
    }
}
