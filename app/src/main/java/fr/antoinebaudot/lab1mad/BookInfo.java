package fr.antoinebaudot.lab1mad;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class BookInfo extends AppCompatActivity {

    private Toolbar myToolbar ;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private Book bk ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info);


        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setNavigationIcon(R.drawable.ic_back_nav);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent() ;
        String bookId = intent.getStringExtra("BOOK_ID");


        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().getRoot();

        FirebaseUser user = mAuth.getCurrentUser();
        //String bookId = isbn + "-" + user.getUid();


        DatabaseReference ref = mDatabase.child("books").child(bookId);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                bk = dataSnapshot.getValue(Book.class);
                showBookInfo();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }


    private void showBookInfo() {

        TextView titleTv = (TextView) findViewById(R.id.title);
        TextView subtitleTv = (TextView) findViewById(R.id.subtitle);
        TextView authorTv = (TextView) findViewById(R.id.author);
        TextView descriptionTv = (TextView) findViewById(R.id.description);

        titleTv.setText(bk.getTitle());
        subtitleTv.setText(bk.getSubtitle());
        authorTv.setText(bk.getAuthor());
        descriptionTv.setText(bk.getDescription());


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu_bookinfo,menu);
        return true ;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.contactOwner:
                //OPEN A CHAT WITH THE BOOK OWNER
                break ;

            default :
                break ; 
        }
        return true ;


    }

}
