// ISync.aidl
package com.logitech.vc.raiden.sync.test.isync;

// Declare any non-default types here with import statements
import com.logitech.vc.raiden.sync.test.isync.ISyncCallback;

interface ISync {
   void registerClient(String clientId, ISyncCallback callback);

   void unregisterClient(String clientId);

   void sendMessage(String message);
}
