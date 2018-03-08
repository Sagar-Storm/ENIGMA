package com.kodbale.dkode.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.kodbale.dkode.Database.StatusManager;
import com.kodbale.dkode.R;

public class Logout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        exitApp();
    }

    public void onLogOutPressed(View v) {
        exitApp();
        int pid = android.os.Process.myPid();
        android.os.Process.killProcess(pid);
    }

    public void exitApp() {
        StatusManager.get(getApplication()).getAuth().signOut();
        StatusManager.get(getApplication()).setAuth(null);
        StatusManager.get(getApplicationContext()).setUser(null);

    }
}
