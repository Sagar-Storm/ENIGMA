package com.kodbale.dkode.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.kodbale.dkode.Database.StatusManager;
import com.kodbale.dkode.R;

public class Logout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        //exitApp();
    }

    public void onLogOutPressed(View v) {
        exitApp();
        finish();
        int pid = android.os.Process.myPid();
        android.os.Process.killProcess(pid);
    }



    public void exitApp() {
            StatusManager.get(getApplication()).getAuth().signOut();

            if(StatusManager.get(getApplicationContext()).getAuth().getCurrentUser() == null) {
                Log.i("after signing out", "i have logged out");
            }

            StatusManager.get(getApplication()).setAuth(null);
            Log.i("before logout", StatusManager.get(getApplicationContext()).getUser().getEmail());
            StatusManager.get(getApplicationContext()).setUser(null);
            StatusManager.get(getApplicationContext()).setFirebaseDatabase(null);

            if(StatusManager.get(getApplicationContext()).getUser() == null) {
                Log.i("i", "successfully set it");
            } else {
                Log.i("i", "not set it to null");
            }
    }

}
