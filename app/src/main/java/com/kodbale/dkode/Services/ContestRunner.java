package com.kodbale.dkode.Services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by sagar on 3/9/18.
 */

public class ContestRunner extends IntentService {

    private static final String TAG = "ContestRunner";

    public static Intent newIntent(Context context) {
        return new Intent(context, ContestRunner.class);
    }

    public ContestRunner() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.i(TAG, "received an intent" + intent);
    }


}
