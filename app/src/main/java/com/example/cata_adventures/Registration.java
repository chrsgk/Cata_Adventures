package com.example.cata_adventures;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class Registration extends AppCompatActivity implements View.OnClickListener,OnCompleteListener {
    private FirebaseAuth mAuth;
    private TextView adventures;
    private EditText name, user_name, email_Input, password_input, password_Confirmation;
    private Button register, login_register;
    private ProgressBar processor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        adventures = (TextView) findViewById(R.id.text_view1);
        name = (EditText) findViewById(R.id.edittext_name);
        user_name =(EditText) findViewById(R.id.edittext_username);
        email_Input = (EditText) findViewById(R.id.edittext_email_registry);
        password_input= (EditText) findViewById(R.id.edittext_password_registry);
        password_Confirmation = (EditText) findViewById(R.id.edittext_password_confirmation);
        register =(Button) findViewById(R.id.registry_Btn2);
        login_register =(Button) findViewById(R.id.login_Btn2);
        processor =(ProgressBar) findViewById(R.id.progress_bar2);
        mAuth =FirebaseAuth.getInstance();

        adventures.setOnClickListener(this);
        name.setOnClickListener(this);
        user_name.setOnClickListener(this);
        email_Input.setOnClickListener(this);
        password_input.setOnClickListener(this);
        password_Confirmation.setOnClickListener(this);
        register.setOnClickListener(this);
        login_register.setOnClickListener(this);
        processor.setOnClickListener(this);

    }

    @Override
    public void onClick(View action2) {

        switch (action2.getId()) {

            case R.id.text_view1:
                Intent home = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(home);
                break;

            case R.id.login_Btn2:
                Intent main = new Intent(getApplicationContext(), Photo_Gallery.class);
                startActivity(main);
                break;

            case R.id.registry_Btn2:
                register();
                break;
         }
    }
    private void register() {
        String full_Name = name.getText().toString().trim();
        String user = user_name.getText().toString().trim();
        String mail = email_Input.getText().toString().trim();
        String strong_Pass = password_input.getText().toString().trim();
        String pass_confirm = password_Confirmation.getText().toString().trim();

        if(full_Name.isEmpty()){
            name.setError("Full name is required");
            name.requestFocus();
            return;
        }
        if(user.isEmpty()){
            user_name.setError("Please provide a username");
            user_name.requestFocus();
            return;
        }
        if (mail.isEmpty()){
            email_Input.setError("Valid email address is required");
            email_Input.requestFocus();
            return;
        }
        if(Patterns.EMAIL_ADDRESS.matcher((CharSequence)email_Input).matches()){
            email_Input.setError("Provide valid email address");
            email_Input.requestFocus();
            return;
        }
        if (strong_Pass.isEmpty()){
            password_input.setError("A strong password is required");
            password_input.requestFocus();
            return;
        }
        if(strong_Pass.length()<6){
            password_input.setError("Password provided is too short must be more than 6 characters!");
            password_input.requestFocus();
        }
        if (!pass_confirm.matches(strong_Pass)){
            password_Confirmation.setError("Password does not match");
            password_Confirmation.requestFocus();
            return;
        }

        processor.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(mail, strong_Pass);
    }

    @Override
    public void onComplete(@NonNull Task task) {
        FirebaseFirestore mDatabase;
        mDatabase = FirebaseFirestore.getInstance();
        if (task.isSuccessful()){
            User user = new User(name, user_name, email_Input, password_input);

            FirebaseDatabase.getInstance().getReference("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(Registration.this, "User has been registered successfully", Toast.LENGTH_LONG ).show();
                        processor.setVisibility(View.VISIBLE);

                    } else {
                        Toast.makeText(Registration.this, "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                        processor.setVisibility(View.GONE);
                    }
                }
            });

        } else {
            Toast.makeText(Registration.this, "Failed to register! Try again!", Toast.LENGTH_LONG).show();
            processor.setVisibility(View.GONE);
        }

    }
}




