package fr.antoinebaudot.lab1mad;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.nio.BufferUnderflowException;
import java.util.HashMap;
import java.util.Map;

public class SendRequest extends AppCompatActivity  implements MyDatePickerFragment.DialogData{

    private Toolbar myToolbar ;
    private Button send ;
    private TextView start ;
    private TextView end ;
    private Button defStart ;
    private Button defEnd ;
    final int START = 0 ;
    final int END = 1 ;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_request);

        mDatabase = FirebaseDatabase.getInstance().getReference().getRoot();

        myToolbar = (Toolbar) findViewById(R.id.myToolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setNavigationIcon(R.drawable.ic_back_nav);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        send = (Button) findViewById(R.id.send);
        start = (TextView) findViewById(R.id.startDate);
        end = (TextView) findViewById(R.id.endDate);
        defStart = (Button) findViewById(R.id.defineStart);
        defEnd = (Button) findViewById(R.id.defineEnd);

        defStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker(view,START);
            }
        });

        defEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker(view,END);
            }
        });


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequest();
            }
        });

    }


    public void showDatePicker(View v,int val) {
        DialogFragment newFragment = new MyDatePickerFragment();


        Bundle args = new Bundle();
        args.putInt("VAL", val);
        newFragment.setArguments(args);

        newFragment.show(getSupportFragmentManager(), "date picker");
    }

    @Override
    public void getDialogData(String date,int val) {
        if (val == START) {
            start.setText(date);
        } else {
            end.setText(date);
        }

    }


    public void sendRequest(){

        String bookId = getIntent().getStringExtra("BOOK_ID");
        String userId = getIntent().getStringExtra("USER_ID");
        String title = getIntent().getStringExtra("TITLE");
        String ownerId = bookId.split("-")[1];

        String key = FirebaseDatabase.getInstance().getReference("requests").push().getKey();
        DatabaseReference ref = mDatabase.child("requests").child(key);

        Map<String, String> bookReq = new HashMap<>();
        bookReq.put("state",RequestState.SENT.toString());
        bookReq.put("ownerId",ownerId);
        bookReq.put("userId",userId);
        bookReq.put("start",start.getText().toString());
        bookReq.put("end",end.getText().toString());
        bookReq.put("bookId",bookId);
        bookReq.put("title",title);

        ref.setValue(bookReq);
        Toast.makeText(SendRequest.this,"Request sent!",Toast.LENGTH_SHORT).show();
        finish();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu_sendrequest,menu);
        return true ;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){

            case R.id.sendRequest:
                //START A CHAT WITH THE BOOK OWNER
                break;

            default :
                break ;
        }
        return true ;


    }



}
