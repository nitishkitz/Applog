package com.example.applog;

import static com.example.applog.R.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.database.FirebaseDatabase;


public class register extends AppCompatActivity  implements View.OnClickListener{
    private FirebaseAuth mAuth;
    private TextView registerbutton;
    private ProgressBar progressbar;


    private EditText editTextusername,editTextemail,editTextphone,editTextage,editTextpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth=FirebaseAuth.getInstance();

        registerbutton= (Button) findViewById(R.id.registerbutton);
        registerbutton.setOnClickListener(this);

        editTextage=(EditText) findViewById(R.id.Age);
        editTextemail=(EditText) findViewById(R.id.email);
        editTextphone=(EditText) findViewById(R.id.phone);
        editTextusername=(EditText) findViewById(R.id.username);





    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.registerbutton:
                registerbutton();
            break;
        }




        }

    private void registerbutton() {
        String email=editTextemail.getText().toString().trim();
        String username=editTextusername.getText().toString().trim();
        String phone=editTextphone.getText().toString().trim();
        String age=editTextage.getText().toString().trim();
        String password=editTextpassword.getText().toString().trim();

        if (age.isEmpty()){
            editTextage.setError("Age is required");
            editTextage.requestFocus();
            return;
        }
        if (email.isEmpty()){
            editTextemail.setError("Email is required");
            editTextemail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextemail.setError("Please provide a valid EmailId");
            editTextemail.requestFocus();
            return;
        }

        if (phone.isEmpty()){
            editTextphone.setError("phone number is required");
            editTextphone.requestFocus();
            return;
        }
        if (password.isEmpty()){
            editTextpassword.setError("Password is required");
            editTextpassword.requestFocus();
            return;
        }
        if (username.isEmpty()){
            editTextusername.setError("UserName is required");
            editTextusername.requestFocus();
            return;
        }

       progressbar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user=new User(username,phone,age,email );
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(register.this, "User has been registered", Toast.LENGTH_LONG).show();
                                                progressbar.setVisibility(View.VISIBLE);
                                            }else {
                                                Toast.makeText(register.this, "TRY AGAIN", Toast.LENGTH_SHORT).show();
                                                progressbar.setVisibility(View.GONE);
                                            }
                                        }
                                    });
                        }






            }
        });

    }

}
