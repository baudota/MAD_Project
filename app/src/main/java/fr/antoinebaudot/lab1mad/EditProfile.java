package fr.antoinebaudot.lab1mad;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.File;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class EditProfile extends AppCompatActivity {


    private EditText editName ;
    private EditText editMail ;
    private EditText editBio ;
    private ImageView saveBtn ;
    private ImageView editBtn ;
    private ImageView photoUser ;
    static String NAME_KEY = "name" ;
    static String MAIL_KEY = "mail" ;
    static String BIO_KEY = "bio" ;
    static String PHOTO_KEY = "photo" ;
    private Context context = this ;
    private int REQUEST_PHOTO = 2 ;
    private Bitmap image ;
    private Boolean saveImage = false ;
    private DatabaseReference mDatabase ;



    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (savedInstanceState != null) {
            if (savedInstanceState.getByteArray(PHOTO_KEY) != null) {
                image = BitmapFactory.decodeByteArray(savedInstanceState.getByteArray(PHOTO_KEY), 0, savedInstanceState.getByteArray(PHOTO_KEY).length);
            }
        }



        setContentView(R.layout.activity_edit_profile);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(false);
        ab.setDisplayShowTitleEnabled(false);

        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.actionbar, null);
        ab.setCustomView(mCustomView);
        ab.setDisplayShowCustomEnabled(true);


        editName = (EditText) findViewById(R.id.editTextName);
        editMail = (EditText) findViewById(R.id.editTextMail);
        editBio = (EditText) findViewById(R.id.editTextBio);
        photoUser = (ImageView) findViewById(R.id.userPhoto);
        saveBtn = (ImageView) findViewById(R.id.saveAction);
        editBtn = (ImageView) findViewById(R.id.editAction);
        saveBtn.setVisibility(View.VISIBLE);
        editBtn.setVisibility(View.GONE);


        editName.setText(getIntent().getExtras().getString(NAME_KEY));
        editMail.setText(getIntent().getExtras().getString(MAIL_KEY));
        editBio.setText(getIntent().getExtras().getString(BIO_KEY));
        if (getIntent().getExtras().getBundle(PHOTO_KEY).getParcelable(PHOTO_KEY) != null) {
            image = (Bitmap) getIntent().getExtras().getBundle(PHOTO_KEY).getParcelable(PHOTO_KEY) ;
            photoUser.setImageBitmap(image);

        }

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = editName.getText().toString();
                String mail = editMail.getText().toString();
                String bio = editBio.getText().toString();
                String encoded ;




                if (name.equals("") || mail.equals("") || bio.equals("")) {
                    Toast.makeText(context,getResources().getString(R.string.missingInformations),Toast.LENGTH_LONG).show();
                } else {

                    //we save the new informations into the database
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    mDatabase.child("users").child(user.getUid()).child("name").setValue(name);
                    mDatabase.child("users").child(user.getUid()).child("email").setValue(mail);
                    mDatabase.child("users").child(user.getUid()).child("shortBio").setValue(bio);
                    if (saveImage) {
                        encoded = encodeToBase64(image, Bitmap.CompressFormat.JPEG,100);
                        mDatabase.child("users").child(user.getUid()).child("profilePicture").setValue(encoded);
                    }


                    returnHome(name,mail,bio);
                }

            }
        });


        photoUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //file = dispatchTakePictureIntent();
                dispatchTakePictureIntent();
            }
        });

    }
    public void returnHome(String name,String mail, String bio){
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra(NAME_KEY,name);
        intent.putExtra(MAIL_KEY,mail);
        intent.putExtra(BIO_KEY,bio);

        Bundle extras = new Bundle();
        extras.putParcelable(PHOTO_KEY, image);
        intent.putExtra(PHOTO_KEY,extras);

        setResult(RESULT_OK,intent);
        finish();
    }




    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_PHOTO);
        }
    }

   @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_PHOTO && resultCode == RESULT_OK) {
            saveImage = true ;
            Bundle extras = data.getExtras();
            image = (Bitmap) extras.get("data");
            photoUser.setImageBitmap(image);
        }
    }

    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality)
    {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {

        editName.setText(savedInstanceState.getString(NAME_KEY));
        editMail.setText(savedInstanceState.getString(MAIL_KEY));
        editBio.setText(savedInstanceState.getString(BIO_KEY));

        if (savedInstanceState.getByteArray(PHOTO_KEY) != null) {
            Bitmap bmp = BitmapFactory.decodeByteArray(savedInstanceState.getByteArray(PHOTO_KEY), 0, savedInstanceState.getByteArray(PHOTO_KEY).length);
            photoUser.setImageBitmap(bmp);
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (editName != null ) {
            outState.putString(NAME_KEY,editName.getText().toString());
        }

        if (editMail!=  null) {
            outState.putString(MAIL_KEY,editMail.getText().toString());
        }

        if (editBio !=  null) {
            outState.putString(BIO_KEY,editBio.getText().toString());
        }

       if (image != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            outState.putByteArray(PHOTO_KEY,byteArray);

        }

        // call superclass to save any view hierarchy

    }

}
