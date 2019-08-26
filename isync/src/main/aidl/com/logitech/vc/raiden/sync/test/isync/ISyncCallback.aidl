// ISyncCallback.aidl
package com.logitech.vc.raiden.sync.test.isync;

// Declare any non-default types here with import statements

interface ISyncCallback {
    void onBroadcast(in byte[] message);
    void onMessage(String messageId, in byte[] message);
}
