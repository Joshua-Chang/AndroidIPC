package com.example.binderpool.aidl_impl;

import android.os.RemoteException;
import android.util.Log;

import com.example.binderpool.IUser;

/**
 * Created by >>> on 2017/8/26.
 * 用户模块的实现
 */

public class IUserImpl extends IUser.Stub {


    @Override
    public void login(String userName, String password) throws RemoteException {
        Log.i(">>>","IUserImpl  login, userName="+userName+",password="+password);
    }

    @Override
    public void logout(String username) throws RemoteException {
        Log.i(">>>","IUserImpl  logout, userName="+username);
    }
}