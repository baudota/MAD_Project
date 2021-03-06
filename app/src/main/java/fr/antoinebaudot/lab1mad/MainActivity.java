package fr.antoinebaudot.lab1mad;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.FragmentActivity;
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
    private ImageView photoUser ;
    private String name, mail, bio  = "" ;
    static String NAME_KEY = "name" ;
    static String MAIL_KEY = "mail" ;
    static String BIO_KEY = "bio" ;
    static String PHOTO_KEY = "photo" ;
    private Toolbar myToolbar ;
    private Context context = this ;
    private Bitmap photo = null ;
    private DatabaseReference mDatabase;
    private ProgressBar pB ;


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
        pB = (ProgressBar)findViewById(R.id.pb);

        photoUser.setVisibility(View.GONE);
        pB.setVisibility(View.VISIBLE);


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
                    if (currentUser.getProfilePicture() != null){
                        photo = (Bitmap) decodeBase64(currentUser.getProfilePicture());
                    }
                    textName.setText(name);
                    textMail.setText(mail);
                    textBio.setText(bio);
                    photoUser.setImageBitmap(photo);
                    photoUser.setVisibility(View.VISIBLE);
                    pB.setVisibility(View.GONE);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });

        }


        myToolbar = (Toolbar) findViewById(R.id.showProfileToolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setNavigationIcon(R.drawable.ic_action_open_nav);

        setUpDrawer();

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



    private void setUpDrawer() {
        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.nav_drawer_fragment);
        final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerFragment.setUpDrawer(R.id.nav_drawer_fragment,drawerLayout, myToolbar);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
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


    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu_showprofile,menu);
        return true ;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.editProfileItem :
                editUser();
                break ;
        }
        return true ;


    }
}
