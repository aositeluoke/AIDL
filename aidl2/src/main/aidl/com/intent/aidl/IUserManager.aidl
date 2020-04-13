// IUserManager.aidl
package com.intent.aidl;
import com.intent.aidl.User;

// Declare any non-default types here with import statements

interface IUserManager {
   void createUser(in User user);
}
