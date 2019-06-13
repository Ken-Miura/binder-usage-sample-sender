package com.example.bindersender;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public final class SenderService extends Service {

    private static final String TAG = SenderService.class.getCanonicalName();
    private static final String DEST_PACKAGE_NAME = "com.example.binderreceiver";
    private static final String DEST_CLASS_NAME = "ReceiverService";
    private static final int MSG_FROM_SENDER_SERVICE = 1;

    private final Object lock = new Object();
    private Messenger mMessenger = null;

    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG, "onServiceConnected");
            synchronized (lock) {
                mMessenger = new Messenger(service);
            }
            final Runnable r = new Runnable() {
                @Override
                public void run() {
                    final Message msg = Message.obtain(null, MSG_FROM_SENDER_SERVICE, 0, 0);
                    final Bundle bundle = new Bundle();
                    bundle.putLong("long", 23L);
                    bundle.putString("str", "!!!String!!!");
                    msg.setData(bundle);
                    try {
                        synchronized (lock) {
                            if (mMessenger == null) {
                                Log.w(TAG, "mMessenger == null");
                                return;
                            }
                            mMessenger.send(msg);
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            };
            new Thread(r).start();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "onServiceDisconnected");
            synchronized (lock) {
                mMessenger = null;
            }
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
        bindService(i, mServiceConnection, Service.BIND_AUTO_CREATE);
        return Service.START_STICKY;
    }
}
