package fr.antoinebaudot.lab1mad;

        import android.content.Intent;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.graphics.Matrix;
        import android.net.Uri;
        import android.os.Bundle;
        import android.os.Environment;
        import android.os.Handler;
        import android.os.ResultReceiver;
        import android.provider.MediaStore;
        import android.support.design.widget.TextInputEditText;
        import android.support.design.widget.TextInputLayout;
        import android.support.v4.content.FileProvider;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.Toolbar;
        import android.util.Base64;
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

        import com.google.android.gms.vision.Frame;
        import com.google.android.gms.vision.barcode.Barcode;
        import com.google.android.gms.vision.barcode.BarcodeDetector;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;

        import java.io.ByteArrayOutputStream;
        import java.io.File;
        import java.io.IOException;
        import java.text.SimpleDateFormat;
        import java.util.Date;
        import java.util.HashMap;
        import java.util.Map;


        import static android.widget.TextView.BufferType.EDITABLE;

public class AddBook extends AppCompatActivity {
    static final int ORIENTATION_PORTRAIT = 1;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    DownloadTask jsonIsbn;
    private Toolbar myToolbar ;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private Button btnBarCode ;
    private Button btnIsbn;
    private Button btnClear;
    private TextInputEditText isbnEditText ;
    private TextInputEditText authorEditText ;
    private TextInputEditText titleEditText ;
    private TextInputEditText subtitleEditText ;
    private TextInputEditText descriptionEditText ;
    private String coverURL = null ;
    private String isbn ;
    private String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // recovering the instance state
        if (savedInstanceState != null) {
            mCurrentPhotoPath = savedInstanceState.getString("path");
        }

        setContentView(R.layout.activity_addbook);
        btnBarCode= (Button) findViewById(R.id.btn_bar);
        btnIsbn = (Button) findViewById(R.id.internet);
        btnClear = (Button) findViewById(R.id.clear);
        isbnEditText = (TextInputEditText) findViewById(R.id.isbn);
        authorEditText = (TextInputEditText) findViewById(R.id.authorId);
        titleEditText = (TextInputEditText) findViewById(R.id.titleId);
        subtitleEditText = (TextInputEditText) findViewById(R.id.subtitleId);
        descriptionEditText = (TextInputEditText) findViewById(R.id.description);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().getRoot();

        myToolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(myToolbar);

        btnBarCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });


        btnIsbn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isbn = isbnEditText.getText().toString();
                Log.i("isbn written", isbn);
                String surl = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn;
                jsonIsbn = new DownloadTask(AddBook.this);
                jsonIsbn.execute(surl);
                //isbnEditText.setText(isbn);
            }
        });


        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clear();
            }
        });
    }




    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "fr.antoinebaudot.lab1mad.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap imageBitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            Bitmap imageRotated = Bitmap.createBitmap(imageBitmap, 0, 0, imageBitmap.getWidth(), imageBitmap.getHeight(), matrix, true);
            decodeBarCode(imageRotated);
        }
    }

    private void decodeBarCode(Bitmap img) {
        BarcodeDetector detector =
                new BarcodeDetector.Builder(getApplicationContext())
                        .setBarcodeFormats(Barcode.EAN_13 | Barcode.EAN_8)
                        .build();

        TextView text = (TextView) findViewById(R.id.isbn);
        if (!detector.isOperational()) {
            text.setText("Could not set up the detector!");
            return;
        }

        Frame frame = new Frame.Builder().setBitmap(img).build();
        SparseArray<Barcode> barcodes = detector.detect(frame);
        detector.release();

        try {
            Barcode thisCode = barcodes.valueAt(0);
            String isbn = thisCode.rawValue;
            text.setText(isbn, EDITABLE);
            String surl = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn;
            jsonIsbn = new DownloadTask(this);
            jsonIsbn.execute(surl);
        } catch (ArrayIndexOutOfBoundsException e) {
            clear();
            text.setText("No bar code detected");
        } finally {
            File picture = new File(mCurrentPhotoPath);
            Boolean deleted = picture.delete();
        }
    }

    protected void setParameters(String json) {
        clear();
        jsonIsbn.cancel(true);
        isbnEditText.setText(isbn);
        setAuthor(json);
        setTitle(json);
        setSubtitle(json);
        setDescription(json);
        getImage(json);
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
        isbnEditText.setText(null);
        titleEditText.setText(null);
        subtitleEditText.setText(null);
        authorEditText.setText(null);
        descriptionEditText.setText(null);

    }

    private void getImage(String json)  {

        String imageLink = new String("");
        // URL coverURL ;

        try {
            int indexImageLink = json.indexOf("thumbnail");
            int endIndexImageLink = json.indexOf(",",indexImageLink);
            //Integer indexF = endIndexImageLink;
            //Integer indexD = indexImageLink;
            imageLink = json.substring(indexImageLink+13,endIndexImageLink-1);

            CoverReceiver myCoverReceiver = new CoverReceiver(new Handler());

            Intent service = new Intent(AddBook.this,GetBookCoverService.class);
            service.putExtra("COVER_LINK",imageLink);

            service.putExtra("COVER_RECEIVER",myCoverReceiver);
            Log.i("MAIN","STARTING SERVICE");
            startService(service);

        } catch (IndexOutOfBoundsException e ) {
            Log.i("imageLink","NO IMAGE LINK IN JSON FILE");
            // setDefaultCover();
        }
    }

    private void setCover(Bitmap cover) {
        ImageView bookCover = (ImageView) findViewById(R.id.image_bar);
        bookCover.setImageBitmap(cover);
        bookCover.setVisibility(View.VISIBLE);
        Log.i("COVER","COVER SET");

        coverURL = encodeToBase64(cover, Bitmap.CompressFormat.PNG,100);

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
                String isbn = isbnEditText.getText().toString();
                String title = titleEditText.getText().toString();
                String subtitle = subtitleEditText.getText().toString();
                String author = authorEditText.getText().toString();
                String description = descriptionEditText.getText().toString();
                saveBook(isbn,title,subtitle,author,description);
                break ;
        }
        return true ;

    }

    private void saveBook(String isbn,String title,String subtitle,String author,String description){
        FirebaseUser user = mAuth.getCurrentUser();


        DatabaseReference booksRef = mDatabase.child("books");
        DatabaseReference userBooksRef = mDatabase.child("users").child(user.getUid()).child("books");

        Map<String, String> bookMap = new HashMap<>();
        bookMap.put("isbn", isbn);
        bookMap.put("author",author);
        bookMap.put("title",title);
        bookMap.put("subtitle",subtitle);
        bookMap.put("description",description);
        bookMap.put("owner",user.getUid());
        bookMap.put("cover",coverURL);

        Log.i("BOOKMAP","ISBN : " + isbn);

        if (!isbn.equals("")) {
            isbnEditText.setText(isbn);
            booksRef.child(isbn + "-" + user.getUid()).setValue(bookMap);
            userBooksRef.push().setValue(isbn+"-"+user.getUid());
        }

        Intent intent = new Intent(AddBook.this, UserBooks.class);
        startActivity(intent);
        finish();


    }


    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality)
    {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    public class CoverReceiver extends ResultReceiver {

        public CoverReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            byte[] bytesImg = resultData.getByteArray("COVER_BYTE_ARRAY");
            Bitmap thumbnail = BitmapFactory.decodeByteArray(bytesImg,0,bytesImg.length);
            // coverURL = resultData.getString("COVER_URL");

            //Bitmap receivedCover = (Bitmap) resultData.get("COVER_BITMAP");
            setCover(thumbnail);
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putString("path", mCurrentPhotoPath);
        super.onSaveInstanceState(outState);
    }


}



