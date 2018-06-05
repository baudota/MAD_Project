package fr.antoinebaudot.lab1mad;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.view.View.GONE;

public class RequestsActivity extends AppCompatActivity implements RequestsListAdapter.OnItemClickListener {

    private RecyclerView mRecyclerView ;
    private RecyclerView.Adapter mAdapter ;
    private RecyclerView.LayoutManager mLayoutManager ;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private Toolbar myToolbar ;
    private TextView filterInfo ;
    private PopupMenu popupFilter ;
    private ProgressBar myPb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);

        filterInfo = (TextView) findViewById(R.id.filterInfo);
        myPb = (ProgressBar) findViewById(R.id.pb);

        mAuth = FirebaseAuth.getInstance();

        myToolbar = (Toolbar) findViewById(R.id.myToolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setNavigationIcon(R.drawable.ic_action_open_nav);

        setUpDrawer();



        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mDatabase = FirebaseDatabase.getInstance().getReference().getRoot();

        displaySentRequests();

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(RequestsActivity.this, mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(final View view, int position) {
                TextView state = (TextView) view.findViewById(R.id.state);


                if (state.getText().toString().equals(RequestState.SENT.toString()) &&  filterInfo.getText().toString().equals(getResources().getString(R.string.recvReq))){
                    final PopupMenu changeState = new PopupMenu(view.getContext(),view);
                    changeState.inflate(R.menu.changestate_menu);

                    changeState.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            DatabaseReference ref ;
                            TextView keyTv;
                            String key;
                            switch (item.getItemId()){
                                case R.id.accept:
                                   // view.setBackgroundColor(getResources().getColor(R.color.accepted));
                                    keyTv = (TextView) view.findViewById(R.id.dbkey);
                                    key = keyTv.getText().toString();
                                    ref = mDatabase.getRoot().child("requests").child(key).child("state");
                                    ref.setValue(RequestState.ACCEPTED.toString());
                                    displayReceivedRequests();
                                    break;
                                case R.id.refuse:
                                   // view.setBackgroundColor(getResources().getColor(R.color.refused));
                                    keyTv = (TextView) view.findViewById(R.id.dbkey);
                                    key = keyTv.getText().toString();
                                    ref = mDatabase.getRoot().child("requests").child(key).child("state");
                                    ref.setValue(RequestState.REFUSED.toString());
                                    displayReceivedRequests();
                                    break ;
                            }


                            return false;
                        }
                    });


                    changeState.show();

                }
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));


    }



    private void displaySentRequests(){

        myPb.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(GONE);

        FirebaseUser user = mAuth.getCurrentUser();
        String userId = user.getUid();

        filterInfo.setText(getResources().getString(R.string.sentReq));

        Query query = mDatabase.getRoot().child("requests").orderByChild("userId").equalTo(userId);

        query.addListenerForSingleValueEvent(new ValueEventListener() {

            //HashMap<String,BookRequest> sentRequests = new HashMap<>();
            ArrayList<Pair<String,BookRequest>> sentRequests = new ArrayList<>();

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    for (DataSnapshot dt : dataSnapshot.getChildren()){

                        sentRequests.add(new Pair<String, BookRequest>(dt.getKey(),dt.getValue(BookRequest.class)));

                    }

                }
                myPb.setVisibility(GONE);
                mAdapter = new RequestsListAdapter(RequestsActivity.this,sentRequests);
                mRecyclerView.setAdapter(mAdapter);
                mRecyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void displayReceivedRequests(){

        FirebaseUser user = mAuth.getCurrentUser();
        String userId = user.getUid();

        myPb.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(GONE);

        filterInfo.setText(getResources().getString(R.string.recvReq));

        Query query = mDatabase.getRoot().child("requests").orderByChild("ownerId").equalTo(userId);

        query.addListenerForSingleValueEvent(new ValueEventListener() {

            ArrayList<Pair<String,BookRequest>> recvRequests = new ArrayList<>();

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    for (DataSnapshot dt : dataSnapshot.getChildren()){

                        recvRequests.add(new Pair<String, BookRequest>(dt.getKey(),dt.getValue(BookRequest.class)));

                    }

                }
                myPb.setVisibility(View.GONE);
                mAdapter = new RequestsListAdapter(RequestsActivity.this,recvRequests);
                mRecyclerView.setAdapter(mAdapter);
                mRecyclerView.setVisibility(View.VISIBLE);
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



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu_requestslist,menu);
        return true ;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.filter:

                popupFilter =  new PopupMenu(RequestsActivity.this,findViewById(R.id.filter) );
                popupFilter.inflate(R.menu.filter_menu);


                popupFilter.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()){
                            case R.id.sent :
                                displaySentRequests();
                                break ;
                            case R.id.received :
                                displayReceivedRequests();
                                break ;

                        }

                        return false;
                    }
                });


                popupFilter.show();


                break ;
        }
        return true ;


    }


    @Override
    public void onItemClick(View view,RequestState newState) {
        view.setBackgroundColor(getResources().getColor(R.color.accepted));
    }
}
