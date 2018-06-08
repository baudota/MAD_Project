package fr.antoinebaudot.lab1mad.chat

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.recyclerview.R.attr.layoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.Layout
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import fr.antoinebaudot.lab1mad.Book
import fr.antoinebaudot.lab1mad.R
import fr.antoinebaudot.lab1mad.R.id.messageRecyclerView
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


        Log.d("Activity Name", activity)
        Log.d("ChatMessengerActivity", "User1 = $user")
        Log.d("ChatMessengerActivity", "User2 = $user2")

        this.mMessageEditText = findViewById(R.id.messageEditText)
        this.mSendButton = findViewById(R.id.sendButton)


        launch {
            messageLst.clear()
            val loader = ChatLoader(mFirebaseDatabaseReference, user, user2)
            loader.loadChat()
            delay(1, TimeUnit.SECONDS)

            keyForChatRecord = loader.keyForChatRecord
            messageLst = loader.messageLst!!
            if(keyForChatRecord != null) {
                refChat = mFirebaseDatabaseReference.child("chat-record").child(keyForChatRecord.toString())

            }
            Log.d("ChatMessengerActivity", "messageLst = ${messageLst.size}")
            Log.d("ChatMessengerActivity", "keyForChatRecord = $keyForChatRecord")

            //load the user Data


            refChat?.addChildEventListener(object : ChildEventListener{
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onChildAdded(snap: DataSnapshot, p1: String?) {
                    if(snap.exists()){
                        Log.d("AddingChildren", "old size = ${messageLst.size}")

                        val message = Message(snap.child("user").value.toString(),snap.child("text").value.toString(),snap.child("timestamp").value.toString(),snap.child("pictureUrl").value.toString())
                        messageLst.add(message)
                        Log.d("AddingChildren", "message = ${message}")

                        Log.d("AddingChildren", "new size = ${messageLst.size}")


                        mViewAdapter.updateList(messageLst)
                        mViewAdapter.notifyItemInserted(messageLst.size)
                        mRecyclerView.scrollToPosition(mViewAdapter.getItemCount() -1);

                    }

                }

                override fun onChildRemoved(p0: DataSnapshot) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }


            })

        }

        mRecyclerView.apply {

            layoutManager = LinearLayoutManager(this@ChatMessengerActivity)
            mViewAdapter = ChatMessengerAdapter(messageLst, user)
            adapter = mViewAdapter


        }


        mMessageEditText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(charSequ: CharSequence?, start: Int, before: Int, count: Int) {

                if (keyForChatRecord != null) {
                    mSendButton.isEnabled = charSequ.toString().trim().isNotEmpty()
                }

            }

        })

        mSendButton.setOnClickListener({


            // val userConnection = user1 + user2

            val timeStamp = SimpleDateFormat("yyyy.MM.dd.HH").format(Date().time)

            val message = Message(user, mMessageEditText.text.toString(), timeStamp, null)
            //Get User2
            refChat?.push()?.setValue(message)

            mMessageEditText.text.clear()


        })

    }


    /**
     * TODo save data
     *
     *
     */

}







