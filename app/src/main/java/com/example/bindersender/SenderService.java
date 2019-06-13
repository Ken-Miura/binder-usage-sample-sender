package com.example.bindersender;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

public final class SenderService extends Service {

    private static final String TAG = SenderService.class.getCanonicalName();
    private static final String DEST_PACKAGE_NAME = "com.example.binderreceiver";
    private static final String DEST_CLASS_NAME = "ReceiverService";

    private final ServiceConnection merviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG, "onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "onServiceDisconnected");
        }
    };

    public SenderService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final Intent i = new Intent();
        i.setClassName(DEST_PACKAGE_NAME, DEST_PACKAGE_NAME + "." + DEST_CLASS_NAME);
        bindService(i, merviceConnection, Service.BIND_AUTO_CREATE);
        return Service.START_STICKY;
    }
}
