package com.example.cata_adventures;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnCompleteListener {
    FirebaseAuth mAuth;
    ImageView image_v;
    EditText userName, email_1, password;
    Button login, register;
    ProgressBar in_process;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        image_v = findViewById(R.id.image_viewer);
        userName = findViewById(R.id.editxt_username);
        email_1 = findViewById(R.id.editxt_email);
        password = findViewById(R.id.editxt_password);
        register = findViewById(R.id.registry_Btn);
        login = findViewById(R.id.login_Btn);
        in_process= findViewById(R.id.progress_bar);
        mAuth =FirebaseAuth.getInstance();

        image_v.setOnClickListener(this);
        userName.setOnClickListener(this);
        email_1.setOnClickListener(this);
        password.setOnClickListener(this);
        register.setOnClickListener(this);
        login.setOnClickListener(this);
        in_process.setOnClickListener(this);

    }

    @Override
    public void onClick(View action1) {
        switch (action1.getId()) {

            case R.id.registry_Btn:
                Intent register = new Intent(getApplicationContext(), Registration.class);
                startActivity(register);
                break;

            case R.id.login_Btn:
                login();
                break;
        }
    }

    private void login() {
        String username = userName.getText().toString().trim();
        String mail_1 = email_1.getText().toString().trim();
        String password_1 = password.getText().toString().trim();

        if(username.isEmpty()){
            userName.setError("User name is required");
            userName.requestFocus();
            return;
        }
        if(mail_1.isEmpty()){
            email_1.setError("Please provide valid email");
            email_1.requestFocus();
            return;
        }
        if(Patterns.EMAIL_ADDRESS.matcher((CharSequence) email_1).matches()){
            email_1.setError("Valid email address is required!");
            email_1.requestFocus();
            return;
        }
        if(password_1.isEmpty()){
            password.setError("Please fill out the password");
            password.requestFocus();
            return;
        }
        if (password_1.length()<6){
            password.setError("Password has to have more than 6 characters");
            password.requestFocus();
            return;
        }
        
        in_process.setVisibility(View.VISIBLE);
        
        mAuth.createUserWithEmailAndPassword(mail_1, password_1);
        
    }

    @Override
    public void onComplete(@NonNull Task task) {
        if (task.isSuccessful()){
            User user = new User(userName,email_1,password);
            FirebaseDatabase.getInstance().getReference("users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(MainActivity.this, "User has signed in successfully", Toast.LENGTH_SHORT).show();
                        in_process.setVisibility(View.VISIBLE);

                    } else {
                        Toast.makeText(MainActivity.this, "Failed to sign in! Try again!", Toast.LENGTH_SHORT).show();
                        in_process.setVisibility(View.GONE);
                    }
                    
                }
            });

        } else {
            Toast.makeText(MainActivity.this, "Failed to register! Try again!", Toast.LENGTH_SHORT).show();
        }
    }
}