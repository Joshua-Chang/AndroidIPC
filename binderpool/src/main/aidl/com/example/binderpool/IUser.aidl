// IUser.aidl
package com.example.binderpool;

// Declare any non-default types here with import statements

interface IUser {


    void login(String userName,String passwork);
    void logout(String username);
}
