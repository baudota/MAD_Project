package fr.antoinebaudot.lab1mad;



import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import static android.widget.TextView.BufferType.EDITABLE;

public class AddBook extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    DownloadTask jsonIsbn;
    private Toolbar myToolbar ;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private Button btnBarCode ;
    private Button btnIsbn;
    private Button btnClear;
    private EditText isbnEditText ;
    private EditText authorEditText ;
    private EditText titleEditText ;
    private EditText subtitleEditText ;
    private EditText descriptionEditText ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addbook);

        isbnEditText = (EditText) findViewById(R.id.isbn);
        authorEditText = (EditText) findViewById(R.id.authorId);
        titleEditText = (EditText) findViewById(R.id.titleId);
        subtitleEditText = (EditText) findViewById(R.id.subtitleId);
        descriptionEditText = (EditText) findViewById(R.id.description);
        btnBarCode= (Button) findViewById(R.id.btn_bar);
        btnIsbn = (Button) findViewById(R.id.internet);
        btnClear = (Button) findViewById(R.id.clear);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().getRoot();

        myToolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(myToolbar);


      btnBarCode.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              launchCamera();
          }
      });



        btnIsbn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String isbn = isbnEditText.getText().toString();
                Log.i("isbn written", isbn);
                String surl = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn;
                jsonIsbn = new DownloadTask(AddBook.this);
                jsonIsbn.execute(surl);
            }
        });


        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clear();
            }
        });
    }


    private void launchCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            ImageView img = (ImageView) findViewById(R.id.image_bar);
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            img.setImageBitmap(imageBitmap);
            decodeBarCode(imageBitmap);
        }
    }

    private void decodeBarCode(Bitmap img) {
        BarcodeDetector detector =
                new BarcodeDetector.Builder(getApplicationContext())
                        .setBarcodeFormats(Barcode.EAN_13 | Barcode.EAN_8)
                        .build();


        if (!detector.isOperational()) {
            isbnEditText.setText("Could not set up the detector!");
            return;
        }

        Frame frame = new Frame.Builder().setBitmap(img).build();
        SparseArray<Barcode> barcodes = detector.detect(frame);
        detector.release();

        try {
            Barcode thisCode = barcodes.valueAt(0);
            String isbn = thisCode.rawValue;
            isbnEditText.setText(isbn, EDITABLE);
            String surl = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn;
            jsonIsbn = new DownloadTask(this);
            jsonIsbn.execute(surl);
        } catch (ArrayIndexOutOfBoundsException e) {
            clear();
            isbnEditText.setText("No bar code detected");
        }
    }

    protected void setParameters(String json) {
        clear();
        jsonIsbn.cancel(true);
        setAuthor(json);
        setTitle(json);
        setSubtitle(json);
        setDescription(json);
    }

    private void setAuthor(String json) {
        EditText authorText = (EditText) findViewById(R.id.authorId);
        String author;
        try {
            int indexAuthor = json.indexOf("authors");
            int endIndexAuthor = json.indexOf("]", indexAuthor);
            Integer indexF = endIndexAuthor;
            Integer indexD = indexAuthor;
            Log.i("index debut", indexD.toString());
            Log.i("index fin", indexF.toString());
            author = json.substring(indexAuthor, endIndexAuthor);
            String[] authors = author.split("\"");
            Log.i("auteur", authors[2]);
            authorText.setText(authors[2], EDITABLE);
        } catch (IndexOutOfBoundsException e) {
            authorText.setText("No author found", EDITABLE);
        }
    }

    private void setTitle(String json) {
        EditText titleText = (EditText) findViewById(R.id.titleId);
        String title;
        try {
            int indexTitle = json.indexOf("title");
            int endIndexTitle = json.indexOf(",", indexTitle);
            Integer indexF = endIndexTitle;
            Integer indexD = indexTitle;
            Log.i("index debut", indexD.toString());
            Log.i("index fin", indexF.toString());
            title = json.substring(indexTitle, endIndexTitle);
            String[] titleExtract = title.split("\"");
            Log.i("title", titleExtract[2]);
            titleText.setText(titleExtract[2], EDITABLE);
        } catch (IndexOutOfBoundsException e) {
            titleText.setText("No title found", EDITABLE);
        }
    }

    private void setSubtitle(String json) {
        EditText subtitleText = (EditText) findViewById(R.id.subtitleId);
        String subtitle;
        try {
            int indexSubtitle = json.indexOf("subtitle");
            int endIndexSubtitle = json.indexOf(",", indexSubtitle);
            Integer indexF = endIndexSubtitle;
            Integer indexD = indexSubtitle;
            Log.i("index debut", indexD.toString());
            Log.i("index fin", indexF.toString());
            subtitle = json.substring(indexSubtitle, endIndexSubtitle);
            String[] subtitleExtract = subtitle.split("\"");
            Log.i("subtitle", subtitleExtract[2]);
            subtitleText.setText(subtitleExtract[2], EDITABLE);
        } catch (IndexOutOfBoundsException e) {
            subtitleText.setText("No subtitle found", EDITABLE);
        }
    }

    private void setDescription(String json) {
        EditText subtitleText = (EditText) findViewById(R.id.description);
        String description;
        try {
            int indexDes = json.indexOf("description");
            int endIndexDes = json.indexOf("\",", indexDes);
            Integer indexF = endIndexDes;
            Integer indexD = indexDes;
            Log.i("index debut", indexD.toString());
            Log.i("index fin", indexF.toString());
            description = json.substring(indexDes, endIndexDes);
            String[] desExtract = description.split("\"");
            description = "";
            for (int i = 2; i < desExtract.length; i++) {
                description += desExtract[i];
            }
            Log.i("description", description);
            subtitleText.setText(description, EDITABLE);
        } catch (IndexOutOfBoundsException e) {
            subtitleText.setText("No description found", EDITABLE);
        }
    }

    private void clear() {
        isbnEditText.setText("");
        authorEditText.setText("");
        titleEditText.setText("");
        subtitleEditText.setText("");
        descriptionEditText.setText("");

        /*RelativeLayout activityLayout = (RelativeLayout) findViewById(R.id.mainLayout);
        int nbField = activityLayout.getChildCount();
        for (int i = 0; i < nbField; i++) {
            if (activityLayout.getChildAt(i) instanceof EditText) {
                ((EditText) activityLayout.getChildAt(i)).setText(null);
            }
        }*/
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu_editprofile,menu);
        return true ;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.saveProfileItem :
                saveBook();
                break ;
        }
        return true ;

    }

    private void saveBook(){

        String isbn = isbnEditText.getText().toString();
        String title = titleEditText.getText().toString();
        String subtitle = subtitleEditText.getText().toString();
        String author = authorEditText.getText().toString();
        String description = descriptionEditText.getText().toString();

        DatabaseReference booksRef = mDatabase.child("books");
        FirebaseUser user = mAuth.getCurrentUser();

        Map<String, String> bookMap = new HashMap<>();
        bookMap.put("isbn", isbn);
        bookMap.put("author",author);
        bookMap.put("title",title);
        bookMap.put("subtitle",subtitle);
        bookMap.put("description",description);
        bookMap.put("owner",user.getUid());



        booksRef.child(isbn + "-" + user.getUid()).setValue(bookMap);


        Intent intent = new Intent(AddBook.this, UserBooks.class);
        startActivity(intent);
        finish();


    }


}
