package fr.antoinebaudot.lab1mad;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RequestsActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView ;
    private RecyclerView.Adapter mAdapter ;
    private RecyclerView.LayoutManager mLayoutManager ;
    private DatabaseReference mDatabase;
    private Toolbar myToolbar ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);


        myToolbar = (Toolbar) findViewById(R.id.myToolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setNavigationIcon(R.drawable.ic_action_open_nav);

        setUpDrawer();


        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mDatabase = FirebaseDatabase.getInstance().getReference().getRoot();

        displayRequests();

    }

    private void displayRequests() {
        final ArrayList<BookRequest> requests = new ArrayList<>();

        DatabaseReference ref = mDatabase.child("requests");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    BookRequest bk ;
                    for(DataSnapshot tmp : dataSnapshot.getChildren()){

                        bk = tmp.getValue(BookRequest.class);
                        requests.add(bk);


                    }


                }

                mAdapter = new RequestsListAdapter(RequestsActivity.this,requests);
                mRecyclerView.setAdapter(mAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


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


}
