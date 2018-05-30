package fr.antoinebaudot.lab1mad;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import fr.antoinebaudot.lab1mad.chat.ChatMessengerActivity;

public class BookActivity extends AppCompatActivity {

    private Toolbar myToolbar ;
    private ImageView bookImage;
    private Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);


        //String isbn = savedInstanceState.getString("ISBN");
        /*myToolbar = findViewById(R.id.bookActivityToolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setNavigationIcon(R.drawable.ic_action_open_nav);*/

        Intent i = getIntent();
        book = i.getParcelableExtra("Book");

        System.out.println("UID " + book.getUid());
        System.out.println("Author " + book.getAuthor());
        System.out.println("Title " + book.getTitle());

        myToolbar = findViewById(R.id.myToolbar_book_activity);
        setSupportActionBar(myToolbar);

        setupBook(book);
  //      setUpDrawer();


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu_chatprofile,menu);
        return true ;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.enterChatRoom:
                Intent intent = new Intent(this, ChatMessengerActivity.class);
                intent.putExtra("Book", book);
                intent.putExtra("Activity","BookActivity");
                startActivity(intent);
        }
        return true;
    }


    private void setupBook(Book book) {

        //String author = book.
        ImageView pictureView = findViewById(R.id.bookPicture);
        TextView authorText = findViewById(R.id.Author);
        TextView describtion = findViewById(R.id.Description);
        TextView isbn = findViewById(R.id.ISBN);
        TextView title = findViewById(R.id.Title);
        String url = book.getCoverUrl();
        if( url == null || url.equals("")){
            url = "http://i.imgur.com/DvpvklR.png";
        }
        Picasso.get().load(url).into(pictureView);

        authorText.setText(book.getAuthor());
        describtion.setText(book.getDescription());
        isbn.setText(book.getIsbn());
        title.setText(book.getTitle());
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
