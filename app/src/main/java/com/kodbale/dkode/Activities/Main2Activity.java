package com.kodbale.dkode.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.kodbale.dkode.Database.StatusManager;
import com.kodbale.dkode.R;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
    }

    public void onLogOutPressed(View v) {
        StatusManager.get(getApplication()).getAuth().signOut();
        StatusManager.get(getApplication()).setAuth(null);
        StatusManager.get(getApplicationContext()).setUser(null);
        finish();
    }
}
