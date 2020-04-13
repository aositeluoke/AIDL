package com.intent.aidl;
import com.intent.aidl.Book;
interface IBookListener{
void onNewBook(in Book book);
}