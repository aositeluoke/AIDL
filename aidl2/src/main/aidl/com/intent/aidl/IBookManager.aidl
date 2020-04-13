// IBookManager.aidl
package com.intent.aidl;
import com.intent.aidl.IBookListener;
interface IBookManager {
void register(in IBookListener listener);
void unregister(in IBookListener listener);
}
