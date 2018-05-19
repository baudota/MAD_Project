package fr.antoinebaudot.lab1mad

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class ChatMessengerActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var  mMessageEditText : EditText
    private lateinit var mSendButton : Button
    private lateinit var mFirebaseAuth: FirebaseAuth
    private lateinit var mFirebaseUser: FirebaseUser
    private lateinit var mFirebaseDatabaseReference : DatabaseReference;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_messenger)

        //When they write a message
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().reference
        mFirebaseAuth = FirebaseAuth.getInstance()
        mFirebaseUser = mFirebaseAuth.currentUser!!
        //get Second User
        val user2 = intent.extras["user"].toString()


        //load the user Data
        this.mMessageEditText = findViewById(R.id.messageEditText)
        this.mSendButton = findViewById(R.id.sendButton)
        mMessageEditText.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onTextChanged(charSequ: CharSequence?, start: Int, before: Int, count: Int) {

                mSendButton.isEnabled = charSequ.toString().trim().isNotEmpty()

            }

        })

        mSendButton.setOnClickListener({


            // val userConnection = user1 + user2
            val message = Message(mFirebaseUser.uid, user2, mMessageEditText.text.toString(), "")
            //Get User2
            var userNode = mFirebaseDatabaseReference.child("users");
            userNode.orderByChild("email").equalTo(user2)

        })
            //Check user1 - user2
          /*  if(){

            }
            //check user2 - user1
            else if(){

            }
            //create new Data
            else

            })*/



/*
        recyclerView = findViewById<RecyclerView>(R.id.my_recycler_view).apply {
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        setHasFixedSize(true)

        // use a linear layout manager
        layoutManager = viewManager

        // specify an viewAdapter (see also next example)
        adapter = viewAdapter


            //sendButton create a new Messenge

    } */
    }


}


