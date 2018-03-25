package com.willconjo.turfspnt;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class TurfWidgetService extends Service {
    public TurfWidgetService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.



        return START_STICKY;
    }
}
