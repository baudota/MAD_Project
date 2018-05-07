package fr.antoinebaudot.lab1mad;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_search_books);

        editKeyword = (EditText) findViewById(R.id.editTextKeyword);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().getRoot();

        mSpinner = findViewById(R.id.spin);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerOpt);
        mSpinner.setAdapter(adapter);
        myToolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setNavigationIcon(R.drawable.ic_action_open_nav);
        setUpDrawer();

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
                keyword = editKeyword.getText().toString();
                Log.w("Search case", keyword);
                searchType = mSpinner.getSelectedItem().toString();
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

                Query query = reference.child("books").orderByChild(searchType).equalTo(keyword);


                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Intent intent = new Intent(SearchBooks.this, ListActivity.class);

                            List<String> lst = new ArrayList<>();
                            for (DataSnapshot issue : dataSnapshot.getChildren()) {
                                String information = (String) issue.child("title").getValue() + "\n";
                                information = information.concat((String) issue.child("author").getValue());
                                lst.add(information);
                            }
                            Bundle b = new Bundle();
                            String[] lstString = new String[lst.size()];
                            for (int i = 0; i < lstString.length; i++){
                                lstString[i] = lst.get(i);
                            }

                            b.putStringArray("key",lstString);
                            intent.putExtras(b);

                            startActivity(intent);
                            finish();



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


}
