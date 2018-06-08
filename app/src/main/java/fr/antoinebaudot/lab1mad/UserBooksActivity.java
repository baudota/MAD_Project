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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import fr.antoinebaudot.lab1mad.chat.ChatAdapter;

public class UserBooksActivity extends AppCompatActivity {

    private Toolbar myToolbar ;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_books);


        mRecyclerView = findViewById(R.id.user_book_recycleView);


        myToolbar = (Toolbar) findViewById(R.id.userBooksToolbar);
        setSupportActionBar(myToolbar);
        mLayoutManager = new GridLayoutManager(this, 1);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);



        setUpBooks();
        setUpDrawer();

    }

    /**
     * Display all Books
     */
    private void setUpBooks() {

        //mRecyclerView.setAdapter(new ChatAdapter());
        FirebaseAuth mauth = FirebaseAuth.getInstance();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        String uid = mauth.getUid();
        Query query = reference.child("books").orderByChild("owner").equalTo(uid);

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    ArrayList<Book> bookLst = new ArrayList<Book>();
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        String information = (String) issue.child("title").getValue() + "\n";
                        Log.d("BookActivity", "information of the book = " + information);
                        information = information.concat((String) issue.child("author").getValue());
                        System.out.println(information);
                        //lst.add(information);

                        //Book bk = issue.getValue(Book.class);
                        Book bk = new Book(issue.child("owner").getValue().toString(), issue.child("isbn").getValue().toString(),
                                issue.child("author").getValue().toString(),
                                issue.child("title").getValue().toString(),
                                issue.child("subtitle").getValue().toString(),
                                issue.child("description").getValue().toString(),
                                "");

                        System.out.println(bk.getTitle() + "  " + bk.getIsbn());
                        bookLst.add(bk);
                    }
                    Log.d("BookActivity", "size of bookLst " + bookLst.size());


                    //pB.setVisibility(View.GONE);
                    //resultsLayout.setVisibility(View.VISIBLE);
                    mAdapter = new Adapter(UserBooksActivity.this, bookLst);
                    mRecyclerView.setAdapter(mAdapter);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        query.addValueEventListener(listener);


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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu_userbooks,menu);
        return true ;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.addBook :
                Intent intent = new Intent(UserBooksActivity.this, AddBook.class);
                startActivity(intent);
                break ;
        }
        return true ;


    }

}
