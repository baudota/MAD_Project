package fr.antoinebaudot.lab1mad;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;

public class MainActivity extends AppCompatActivity {


    private TextView textName ;
    private TextView textMail ;
    private TextView textBio ;
    private ImageView editAction ;
    private ImageView photoUser ;
    private String name, mail, bio  = "" ;
    static String NAME_KEY = "name" ;
    static String MAIL_KEY = "mail" ;
    static String BIO_KEY = "bio" ;
    static String PHOTO_KEY = "photo" ;


    private Context context = this ;
    private Bitmap photo = null ;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDatabase = FirebaseDatabase.getInstance().getReference();


        if (savedInstanceState != null) {
            name = savedInstanceState.getString(NAME_KEY);
            mail = savedInstanceState.getString(MAIL_KEY);
            bio = savedInstanceState.getString(BIO_KEY);

            if (savedInstanceState.getByteArray(PHOTO_KEY) != null) {
                photo = BitmapFactory.decodeByteArray(savedInstanceState.getByteArray(PHOTO_KEY), 0, savedInstanceState.getByteArray(PHOTO_KEY).length);
            }

        }

        setContentView(R.layout.activity_main);


        textName = (TextView) findViewById(R.id.textName);
        textMail = (TextView) findViewById(R.id.textMail);
        textBio = (TextView) findViewById(R.id.textBio);
        photoUser = (ImageView) findViewById(R.id.photoUser);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            final String uid = user.getUid();
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User currentUser = dataSnapshot.child("users").child(uid).getValue(User.class);
                    name = currentUser.getName();
                    mail = currentUser.getEmail();
                    bio = currentUser.getShortBio();
                    textName.setText(name);
                    textMail.setText(mail);
                    textBio.setText(bio);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });

        }


        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(false);
        ab.setDisplayShowTitleEnabled(false);

        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.actionbar, null);
        ab.setCustomView(mCustomView);
        ab.setDisplayShowCustomEnabled(true);


        editAction = (ImageView) findViewById(R.id.editAction);


     /*   if (textName != null || textMail != null | textBio != null) {
            textName.setText(name);
            textMail.setText(mail);
            textBio.setText(bio);
        }*/




        editAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editUser();
            }
        });


    }

    public void editUser() {
        Intent intent = new Intent(this,EditProfile.class);
        intent.putExtra(NAME_KEY,name);
        intent.putExtra(MAIL_KEY,mail);
        intent.putExtra(BIO_KEY,bio);
        Bundle extras = new Bundle();
        extras.putParcelable(PHOTO_KEY, photo);
        intent.putExtra(PHOTO_KEY,extras);
        startActivityForResult(intent,1);
    }




    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {

        textName.setText(savedInstanceState.getString(NAME_KEY));
        textMail.setText(savedInstanceState.getString(MAIL_KEY));
        textBio.setText(savedInstanceState.getString(BIO_KEY));

        if (savedInstanceState.getByteArray(PHOTO_KEY) != null) {
            Bitmap bmp = BitmapFactory.decodeByteArray(savedInstanceState.getByteArray(PHOTO_KEY), 0, savedInstanceState.getByteArray(PHOTO_KEY).length);
            photoUser.setImageBitmap(bmp);
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (textName != null ) {
            outState.putString(NAME_KEY,textName.getText().toString());
        }

        if (textMail!=  null) {
            outState.putString(MAIL_KEY,textMail.getText().toString());
        }

        if (textBio !=  null) {
            outState.putString(BIO_KEY,textBio.getText().toString());
        }

        if (photo != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            outState.putByteArray(PHOTO_KEY,byteArray);

        }



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1 ) {
            if (resultCode == RESULT_OK) {
                super.onActivityResult(requestCode, resultCode, data);
                Bundle result =  data.getExtras() ;
                name = result.getString(NAME_KEY);
                mail = result.getString(MAIL_KEY);
                bio = result.getString(BIO_KEY);
               photo = (Bitmap) result.getBundle(PHOTO_KEY).getParcelable(PHOTO_KEY) ;


                textName.setText(name);
                textMail.setText(mail);
                textBio.setText(bio);
                photoUser.setImageBitmap(photo);



            }
        }


    }

}
