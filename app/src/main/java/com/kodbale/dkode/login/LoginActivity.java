package com.kodbale.dkode.login;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.kodbale.dkode.activities.BufferActivity;
import com.kodbale.dkode.database.StatusManager;
import com.kodbale.dkode.MainActivity;
import com.kodbale.dkode.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private  EditText groupPass,groupId;
    private Button btn;
    private FirebaseAuth mAuth;
    private String email,password;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        groupId = (EditText) findViewById(R.id.group_id);
        groupPass = (EditText) findViewById(R.id.group_pass);
        btn = (Button) findViewById(R.id.loginButton);
        progressBar = (ProgressBar) findViewById(R.id.loginProgress);
        btn.setOnClickListener(this);
    }
    
    @Override
    public void onClick(View v) {
        email = groupId.getText().toString();
        password = groupPass.getText().toString();
        progressBar.setVisibility(View.VISIBLE);
        if ( email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getApplicationContext(),"Fill this thing up!",Toast.LENGTH_SHORT).show();
            return;
        }
        if(!isNetworkAvailableAndConnected()) {
            Toast.makeText(getApplicationContext(),"top up first", Toast.LENGTH_SHORT).show();
            return ;
        }
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Check your creds!",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            makeLogin();
                            Intent intent = new Intent(getApplicationContext(), BufferActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            Log.i("i", "logging in");
                            finish();
                        }
                    }
                });
    }

    public void makeLogin() {
        StatusManager.get(getApplicationContext()).setAuth(FirebaseAuth.getInstance());
        StatusManager.get(getApplicationContext()).setUser(FirebaseAuth.getInstance().getCurrentUser());
        StatusManager.get(getApplicationContext()).setFirebaseDatabase(FirebaseDatabase.getInstance());
    }

    private boolean isNetworkAvailableAndConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;
        boolean isNetworkConnected = isNetworkAvailable && cm.getActiveNetworkInfo().isConnected();
        return isNetworkConnected;
    }
}
