package fr.antoinebaudot.lab1mad;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserBooks extends AppCompatActivity {

    private Toolbar myToolbar ;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private ArrayList<Book> userBooks;
    private ProgressBar pb ;
    private LinearLayout resultsLayout ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_books);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mLayoutManager = new GridLayoutManager(this, 1);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().getRoot();

        myToolbar = (Toolbar) findViewById(R.id.userBooksToolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setNavigationIcon(R.drawable.ic_action_open_nav);

        setUpDrawer();

        setUserBooks();

        pb = (ProgressBar) findViewById(R.id.pb);
        resultsLayout = (LinearLayout) findViewById(R.id.results);
        pb.setVisibility(View.VISIBLE);
        resultsLayout.setVisibility(View.GONE);



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


    private void setUserBooks(){

        userBooks = new ArrayList<>();

        FirebaseUser user = mAuth.getCurrentUser();


        DatabaseReference ref = mDatabase.child("users").child(user.getUid()).child("books");
        final DatabaseReference booksRef = mDatabase.child("books");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
          @Override
          public void onDataChange(DataSnapshot dataSnapshot) {
             Log.i("COUNT",String.valueOf(dataSnapshot.getChildrenCount()));
             final ArrayList<String> bks = new ArrayList<>();

              Query query = booksRef;

             for (DataSnapshot dt : dataSnapshot.getChildren()){
                bks.add(dt.getValue().toString());
             }

             query.addListenerForSingleValueEvent(new ValueEventListener() {
                 @Override
                 public void onDataChange(DataSnapshot dataSnapshot) {
                     //Log.d("Books", String.valueOf(dataSnapshot.getChildrenCount()));
                    Book book ;
                     for (String bk : bks) {
                         book = dataSnapshot.child(bk).getValue(Book.class);
                         //System.out.println(book.getTitle());
                         userBooks.add(book);

                     }

                     mAdapter = new Adapter(getApplicationContext(), userBooks);
                     mRecyclerView.setAdapter(mAdapter);
                     pb.setVisibility(View.GONE);
                     resultsLayout.setVisibility(View.VISIBLE);


                 }

                 @Override
                 public void onCancelled(DatabaseError databaseError) {

                 }
             });

          }

          @Override
          public void onCancelled(DatabaseError databaseError) {

          }
      });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu_userbooks,menu);
        return true ;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.addBook :
                Intent intent = new Intent(UserBooks.this, AddBook.class);
                startActivity(intent);
                break ;
        }
        return true ;


    }

}
