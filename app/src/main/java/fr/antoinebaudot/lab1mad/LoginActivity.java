package fr.antoinebaudot.lab1mad;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private EditText loginMailEditText ;
    private EditText passwdEdittext ;
    private Button loginButton ;
    private Button signUpButton ;
    private FirebaseAuth mAuth ;
    private String email, password ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        loginMailEditText = (EditText) findViewById(R.id.loginMail);
        passwdEdittext = (EditText) findViewById(R.id.loginPassword);
        loginButton = (Button) findViewById(R.id.loginBtn);
        signUpButton = (Button) findViewById(R.id.signUpBtn);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        mAuth = FirebaseAuth.getInstance();

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //start new user activity
                Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
                intent.putExtra("MAIL_KEY",loginMailEditText.getText().toString());
                startActivity(intent);

            }
        });

       loginButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                //DatabaseReference ref = database.getReference("test");
                //ref.setValue("loginTest");
                email = loginMailEditText.getText().toString();
                password = passwdEdittext.getText().toString();

                if (email.equals("") || password.equals("")) {
                    Toast.makeText(LoginActivity.this,getResources().getString(R.string.missingInformations),Toast.LENGTH_LONG).show();
                } else {
                    signIn();
                }



            }
        });

    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }


    public void signIn(){
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    FirebaseUser user = mAuth.getCurrentUser();
                    String userUID = user.getUid();
                    Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.authFailed), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("LOGIN_MAIL_KEY",loginMailEditText.getText().toString());
        outState.putString("LOGIN_PASSWORD_KEY",passwdEdittext.getText().toString());

    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        loginMailEditText.setText(savedInstanceState.getString("LOGIN_MAIL_KEY"));
        passwdEdittext.setText(savedInstanceState.getString("LOGIN_PASSWORD_KEY"));
    }
}
