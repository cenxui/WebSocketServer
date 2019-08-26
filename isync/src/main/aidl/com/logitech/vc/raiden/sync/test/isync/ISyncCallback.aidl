// ISyncCallback.aidl
package com.logitech.vc.raiden.sync.test.isync;

// Declare any non-default types here with import statements

interface ISyncCallback {
    byte[] onMessage(in byte[] message);
}
