package fr.antoinebaudot.lab1mad;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private EditText newUserMailEditText ;
    private EditText newUserPassWordEditText ;
    private EditText newUserPassWordCheckEditText ;
    private Button signUpButton ;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        newUserMailEditText = (EditText) findViewById(R.id.newUserMail);
        newUserPassWordEditText = (EditText) findViewById(R.id.newUserPassword);
        signUpButton = (Button) findViewById(R.id.createUserBtn);
        newUserPassWordCheckEditText = (EditText) findViewById(R.id.newUserPasswordCheck);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().getRoot();

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (newUserPassWordEditText.getText().toString().equals(newUserPassWordCheckEditText.getText().toString())){
                    createUser();
                } else {
                    Toast.makeText(SignUpActivity.this, getResources().getString(R.string.checkPassword) , Toast.LENGTH_LONG).show();
                }


            }
        });


    }

    public void createUser(){

        String email = newUserMailEditText.getText().toString();
        String password = newUserPassWordEditText.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    FirebaseUser user = mAuth.getCurrentUser();

                    Toast.makeText(SignUpActivity.this, "success "+ user.getUid(), Toast.LENGTH_SHORT).show();
                    DatabaseReference usersRef = mDatabase.child("users");

                    //usersRef.push().setValue(user.getUid());

                    Map<String, String> userMap = new HashMap<>();
                    userMap.put("email",user.getEmail().toString() );
                    userMap.put("name","test");
                    usersRef.child(user.getUid()).setValue(userMap);

                } else {
                    // If sign in fails, display a message to the user.

                    Toast.makeText(SignUpActivity.this, "failed.", Toast.LENGTH_SHORT).show();

                }

            }
        });
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("SIGNUP_MAIL_KEY",newUserMailEditText.getText().toString());
        outState.putString("SIGNUP_PASSWORDD_KEY",newUserPassWordEditText.getText().toString());
        outState.putString("SIGNUP_PASSWORD_CHECK_KEY",newUserPassWordCheckEditText.getText().toString());

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        newUserMailEditText.setText(savedInstanceState.getString("SIGNUP_MAIL_KEY"));
        newUserPassWordEditText.setText(savedInstanceState.getString("SIGNUP_PASSWORD_KEY"));
        newUserPassWordCheckEditText.setText(savedInstanceState.getString("SIGNUP_PASSWORD_CHECK_KEY"));

    }
}
