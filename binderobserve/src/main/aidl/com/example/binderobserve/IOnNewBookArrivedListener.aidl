// IOnNewBookArrivedListener.aidl
package com.example.binderobserve;

// Declare any non-default types here with import statements
import com.example.binderobserve.Book;

interface IOnNewBookArrivedListener {
        void onNewBookArrived(in Book newBook);
}
