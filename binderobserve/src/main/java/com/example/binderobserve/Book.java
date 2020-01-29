package com.example.binderobserve;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @版本号：
 * @需求编号：
 * @功能描述：
 * @创建时间：2020-01-29 23:03
 * @创建人：常守达
 */
public class Book implements Parcelable {
    private int index;
    private String bookName;

    public Book(int index, String bookName) {
        this.index = index;
        this.bookName = bookName;
    }

    public int getIndex() {
        return index;
    }

    public String getBookName() {
        return bookName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.index);
        dest.writeString(this.bookName);
    }

    protected Book(Parcel in) {
        this.index = in.readInt();
        this.bookName = in.readString();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel source) {
            return new Book(source);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };
}
