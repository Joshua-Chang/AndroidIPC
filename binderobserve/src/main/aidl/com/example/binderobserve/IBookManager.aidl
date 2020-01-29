// IBookManager.aidl
package com.example.binderobserve;

// Declare any non-default types here with import statements
import com.example.binderobserve.Book;
import com.example.binderobserve.IOnNewBookArrivedListener;
interface IBookManager {
    List<Book> getBookList();
    void addBook(in Book book);
    void registerListener(IOnNewBookArrivedListener listener);
    void unregisterListener(IOnNewBookArrivedListener listener);
}
