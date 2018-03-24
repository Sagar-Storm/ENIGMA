package com.kodbale.dkode;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class TimerService extends Service {
    public TimerService() {
    }

    public static final String COUNTDOWN_BR = "com.kodbale.dkode.countdown_br";
    Intent bi = new Intent(COUNTDOWN_BR);
    long TIME_REMAINING = 1000000;

    CountDownTimer cdt;

    @Override
    public void onCreate() {
        super.onCreate();
        cdt = new CountDownTimer(TIME_REMAINING,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                bi.putExtra("countdown",millisUntilFinished);
                sendBroadcast(bi);
            }

            @Override
            public void onFinish() {

            }
        };
        cdt.start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        TIME_REMAINING = (intent.getLongExtra("timeToEnd",0));
        return null;
    }

}
