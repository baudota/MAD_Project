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

import java.nio.BufferUnderflowException;

public class SendRequest extends AppCompatActivity  implements MyDatePickerFragment.DialogData{

    private Toolbar myToolbar ;
    private Button send ;
    private TextView start ;
    private TextView end ;
    private Button defStart ;
    private Button defEnd ;
    final int START = 0 ;
    final int END = 1 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_request);

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
        BookRequest bkReq = new BookRequest();

        String bookId = getIntent().getStringExtra("BOOK_ID");
        String userId = getIntent().getStringExtra("USER_ID");
        bkReq.setBookId(bookId);
        bkReq.setStart(start.getText().toString());
        bkReq.setEnd(end.getText().toString());

        String ownerId = bookId.split("-")[1];
        bkReq.setOwnerID(ownerId);

        bkReq.setUserID(userId);

        //SEND TO THE DATABASE

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu_sendrequest,menu);
        return true ;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){

            default :
                break ;
        }
        return true ;


    }



}
