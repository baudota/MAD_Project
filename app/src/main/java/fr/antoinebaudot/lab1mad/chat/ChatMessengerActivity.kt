package fr.antoinebaudot.lab1mad.chat

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.recyclerview.R.attr.layoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.text.Editable
import android.text.Layout
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import fr.antoinebaudot.lab1mad.Book
import fr.antoinebaudot.lab1mad.R
import fr.antoinebaudot.lab1mad.R.id.messageRecyclerView
import fr.antoinebaudot.lab1mad.R.id.showChatToolbar
import fr.antoinebaudot.lab1mad.User
import fr.antoinebaudot.lab1mad.chat.model.ChatLoader
import fr.antoinebaudot.lab1mad.chat.model.Message
import kotlinx.android.synthetic.main.activity_addbook.*
import kotlinx.android.synthetic.main.activity_addbook.view.*
import kotlinx.android.synthetic.main.activity_chat_messenger.*
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

import kotlin.collections.ArrayList
import kotlin.coroutines.experimental.CoroutineContext


class ChatMessengerActivity : AppCompatActivity() {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mViewAdapter: ChatMessengerAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var mMessageEditText: EditText
    private lateinit var mSendButton: Button
    private lateinit var mFirebaseAuth: FirebaseAuth
    private lateinit var mFirebaseUser: FirebaseUser
    private lateinit var mFirebaseDatabaseReference: DatabaseReference;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_messenger)

        val myToolbar = findViewById<Toolbar>(R.id.showChatToolbar);
        setSupportActionBar(myToolbar);
        myToolbar.navigationIcon = resources.getDrawable(R.drawable.ic_back_nav);
        myToolbar?.setNavigationOnClickListener(View.OnClickListener {
            finish();
        });
        var objUser1 : User? = null
        var objUser2 : User? = null
        //When they write a message_view
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().reference.root
        mFirebaseAuth = FirebaseAuth.getInstance()
        mFirebaseUser = mFirebaseAuth.currentUser!!
        mRecyclerView = messageChatRecyclerView
        val user = mFirebaseUser.uid
        var otherUserUid: String = ""
        //get Second User
        var keyForChatRecord: String? = null
        var messageLst: ArrayList<Message> = arrayListOf()
        var refChat: DatabaseReference? = null
        val activity: String = intent.extras["Activity"].toString()
        var user2: String = ""
        var valueChange: Boolean = false


        if (activity.equals("BookActivity")) {
            user2 = intent.getParcelableExtra<Book>("Book").owner

        } else {
            //chatRoom
            user2 = intent.getStringExtra("User2")
        }



        val loader = ChatLoader(mFirebaseDatabaseReference, user, user2)
        Log.d("Activity Name", activity)
        Log.d("ChatMessengerActivity", "User1 = $user")
        Log.d("ChatMessengerActivity", "User2 = $user2")

        this.mMessageEditText = findViewById(R.id.messageEditText)
        this.mSendButton = findViewById(R.id.sendButton)



        launch {
            messageLst.clear()

            loader.loadChat()
            delay(1, TimeUnit.SECONDS)

            keyForChatRecord = loader.keyForChatRecord
            //Log.d("SizeOfLst" , "Size of Lst after loader ${messageLst.size}")
            objUser1 = loader.objUser1
            objUser2 = loader.objUser2


            Log.d("MessengerLst", "The size of the within the coroutine $messageLst")
            if(keyForChatRecord != null) {
                refChat = mFirebaseDatabaseReference.child("chat-record").child(keyForChatRecord.toString())

            }
            Log.d("ChatMessengerActivity", "messageLst = ${messageLst.size}")
            Log.d("ChatMessengerActivity", "keyForChatRecord = $keyForChatRecord")

            //load the user Data


            refChat?.addChildEventListener(object : ChildEventListener{
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                }

                override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                }

                override fun onChildAdded(snap: DataSnapshot, p1: String?) {
                    if(snap.exists()){
                        Log.d("AddingChildren", "old size = ${messageLst.size}")

                        Log.d("MessengerLst", "The size of the within the coroutine $messageLst")

                        val message = Message(snap.child("user").getValue(User::class.java),snap.child("text").value.toString(),snap.child("timestamp").value.toString(),snap.child("pictureUrl").value.toString())
                        messageLst.add(message)
                        Log.d("MessengerLst", "The size of the within the coroutine $messageLst")

                        Log.d("AddingChildren", "message = ${message}")

                        Log.d("AddingChildren", "new size = ${messageLst.size}")



                            mViewAdapter.updateList(messageLst, objUser1)
                            //mViewAdapter.objUser = objUser1

                            mViewAdapter.notifyItemInserted(messageLst.size)
                            mRecyclerView.scrollToPosition(mViewAdapter.itemCount - 1);



                    }

                }

                override fun onChildRemoved(p0: DataSnapshot) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }


            })

        }





        mMessageEditText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(charSequ: CharSequence?, start: Int, before: Int, count: Int) {

                if (keyForChatRecord != null &&  loader.objUser1 != null) {
                    mViewAdapter.objUser = objUser1
                    mSendButton.isEnabled = charSequ.toString().trim().isNotEmpty()
                }

            }

        })

        mSendButton.setOnClickListener({


            // val userConnection = user1 + user2

            val timeStamp = SimpleDateFormat("yyyy.MM.dd.HH.SS").format(Date().time)

            val message = Message(objUser1, mMessageEditText.text.toString(), timeStamp, null)
            //Get User2
            refChat?.push()?.setValue(message)

            mMessageEditText.text.clear()



        })



        mRecyclerView.apply {

            layoutManager = LinearLayoutManager(this@ChatMessengerActivity)
            mViewAdapter = ChatMessengerAdapter(messageLst, objUser1)
            adapter = mViewAdapter
        }


    }


    /**
     * TODo save data
     *
     *
     */

}







