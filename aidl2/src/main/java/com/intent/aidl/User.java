package com.intent.aidl;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 类描述:
 * 作者:xues
 * 时间:2020年04月13日
 */
public class User implements Parcelable {
    private int age;
    private String name;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    public User(int age, String name) {
        this.age = age;
        this.name = name;
    }

    public User() {
    }

    public User(Parcel in) {
        age = in.readInt();
        name = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
