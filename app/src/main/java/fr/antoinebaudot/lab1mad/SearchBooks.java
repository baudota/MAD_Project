package fr.antoinebaudot.lab1mad;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.provider.ContactsContract;
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
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.google.android.gms.vision.text.Line;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class SearchBooks extends AppCompatActivity {

    private Toolbar myToolbar ;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private EditText editKeyword;
    private Spinner mSpinner;
    private String[] spinnerOpt = new String[]{"title", "author", "ISBN"};
    private ProgressBar pB ;
    private LinearLayout resultsLayout ;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_search_books);

        editKeyword = (EditText) findViewById(R.id.editTextKeyword);
        pB = (ProgressBar) findViewById(R.id.pb);
        resultsLayout = (LinearLayout) findViewById(R.id.results);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().getRoot();

        mSpinner = findViewById(R.id.spin);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerOpt);
        mSpinner.setAdapter(adapter);
        myToolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setNavigationIcon(R.drawable.ic_action_open_nav);
        setUpDrawer();

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);


        mRecyclerView.setHasFixedSize(true);
        int marginBottom = 40;
        int marginRight = 20;
        mRecyclerView.addItemDecoration(new ItemOffsetDecoration(marginBottom, marginRight,this));
        mLayoutManager = new GridLayoutManager(this, 1);
        mRecyclerView.setLayoutManager(mLayoutManager);


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
        getMenuInflater().inflate(R.menu.toolbar_menu_searchbooks , menu);
        return true ;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        String keyword;
        String searchType;
        switch(item.getItemId()){
            case R.id.search :
                //HERE WE SEARCH FOR BOOKS FROM THE DATABASE
                resultsLayout.setVisibility(View.GONE);
                pB.setVisibility(View.VISIBLE);

                keyword = editKeyword.getText().toString();
                Log.w("Search case", keyword);
                searchType = mSpinner.getSelectedItem().toString();
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

                Query query = reference.child("books").orderByChild(searchType).equalTo(keyword);


                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {

                            ArrayList<Book> lst = new ArrayList<>();



                           for (DataSnapshot issue : dataSnapshot.getChildren()) {
                                String information = (String) issue.child("title").getValue() + "\n";
                                information = information.concat((String) issue.child("author").getValue());
                                System.out.println(information);
                                //lst.add(information);

                                Book bk = issue.getValue(Book.class);
                                /*Book bk = new Book(null, issue.child("isbn").getValue().toString(),
                                        issue.child("author").getValue().toString(),
                                        issue.child("title").getValue().toString(),
                                        issue.child("subtitle").getValue().toString(),
                                        issue.child("description").getValue().toString(),
                                        "" );
*/
                                System.out.println(bk.getTitle() + "  " + bk.getIsbn());

                                lst.add(bk);

                            }

                            pB.setVisibility(View.GONE);
                           resultsLayout.setVisibility(View.VISIBLE);
                            mAdapter = new Adapter(getApplicationContext(), lst);
                            mRecyclerView.setAdapter(mAdapter);




                        }
                        }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                break ;
        }
        return true ;


    }



    private class ItemOffsetDecoration extends RecyclerView.ItemDecoration {
        private int itemMarginTop;
        private int ItemMarginRight;
        private Context context;

        public ItemOffsetDecoration(int itemT, int itemR, Context c) {
            itemMarginTop = itemT;
            ItemMarginRight = itemR;
            context = c;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            if (parent != null && view != null) {

                int itemPosition = parent.getChildAdapterPosition(view);
                int totalCount = parent.getAdapter().getItemCount();

                if (itemPosition >= 0 && itemPosition < totalCount - 1) {
                    outRect.bottom = itemMarginTop;
                    outRect.right = ItemMarginRight;
                }
            }
        }
    }


}
