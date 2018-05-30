package fr.antoinebaudot.lab1mad.chat

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.recyclerview.R.attr.layoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
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
import kotlinx.android.synthetic.main.activity_chat_messenger.*
import java.text.SimpleDateFormat
import java.util.*

import kotlin.collections.ArrayList


class ChatMessengerActivity : AppCompatActivity() {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var mMessageEditText: EditText
    private lateinit var mSendButton: Button
    private lateinit var mFirebaseAuth: FirebaseAuth
    private lateinit var mFirebaseUser: FirebaseUser
    private lateinit var mFirebaseDatabaseReference: DatabaseReference;
    private lateinit var userID2: String;
    private lateinit var messageLst : ArrayList<Message>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_messenger)

        //When they write a message_view
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().reference.root
        mFirebaseAuth = FirebaseAuth.getInstance()
        mFirebaseUser = mFirebaseAuth.currentUser!!
        val user = mFirebaseUser.uid
        var otherUserUid : String = ""
        //get Second User


        val activity : String = intent.extras["Activity"].toString()
        val book : Book;
        var myUser : User?
        var otherUser : User?
        messageLst = ArrayList()
        myUser = null
        otherUser = null
        //has to be change





        Log.d("Activity Name",activity)
        when (activity){
            //Two activities can call this activity
            "BookActivity" -> {
                book = intent.getParcelableExtra("Book")
                otherUserUid = book.uid
                //ToDo creates always a new node, does not update. Problem!
                //check if Database has already an entry.
                mFirebaseDatabaseReference.child("chat-users").child(user).child(otherUserUid).setValue("$user-$otherUserUid")
                mFirebaseDatabaseReference.child("chat-users").child(otherUserUid).child(user).setValue("$user-$otherUserUid")
            }
                //Look into the activity
            "ChatRecordActivity" -> {
                println("Load chat record")

            }

        }

        val node = "$user-$otherUserUid"

        //create User1 and User2
        mFirebaseDatabaseReference.child("users").addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(snap: DataSnapshot?) {
                if(snap!!.exists()) {
                    //first user
                    for (item in snap.children) {
                        if (item.key.equals(user)) {
                            myUser = item.getValue(User::class.java)!!
                        }
                        if (item.key.equals(otherUserUid))
                            otherUser = item.getValue(User::class.java)!!
                    }
                    if(myUser != null && otherUser != null){
                        Toast.makeText(this@ChatMessengerActivity,"HatGeklappt",Toast.LENGTH_SHORT).show()

                    }
                }
            }


        })


        ///Listener for every Element in the chatRoom from the user



        var refChat = mFirebaseDatabaseReference.child("chat-record").child(node)
        var chat = ""

        //FirstLoading of the chatRecord
        refChat.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError?) {

                println("Funktioniert nicht!!!!!!")
            }

            override fun onDataChange(snap: DataSnapshot?) {
                if(snap!!.exists()){

                    for (item in snap.children){

                        val value = item.value!!
                        if(value.equals("$user-$otherUserUid")) {
                        }
                        else
                            if (!value.equals("$otherUserUid-$user")){
                                chat = value.toString()
                            }
                    }

                }
                Log.d("MychatTag" ,"Der node von Chat sieht so aus - ${chat}")
                //Toast.makeText(this@ChatMessengerActivity,"test",Toast.LENGTH_SHORT).show()
            }
        })
        //Load History
        //val node = loadChatRecord()


        //load the user Data
        this.mMessageEditText = findViewById(R.id.messageEditText)
        this.mSendButton = findViewById(R.id.sendButton)


        mMessageEditText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(charSequ: CharSequence?, start: Int, before: Int, count: Int) {

                mSendButton.isEnabled = charSequ.toString().trim().isNotEmpty()

            }

        })

        //val user = readUser()







        mSendButton.setOnClickListener({


            // val userConnection = user1 + user2

            val timeStamp = SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(Date().time)

            val message = Message(myUser!!, mMessageEditText.text.toString(), timeStamp,null)
            //Get User2
            refChat.push().setValue(message)
            Log.d("MyUserTag" ,"My User - ${myUser!!.name}")


            mMessageEditText.text.clear()

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


        setTheRecycleView(refChat)




    }


    private fun setTheRecycleView(refChat : DatabaseReference) : ArrayList<Message> {

        refChat.addValueEventListener(object : ValueEventListener{

            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(snap: DataSnapshot?) {
                if(snap!!.exists()){
                    if(messageLst.size == 0){
                        for (item in snap.children){
                            val message = item.getValue(Message::class.java)

                            if (message != null) {

                                messageLst.add(message)
                            }
                        }


                    }else {
                        val message = snap.children.last().getValue(Message::class.java)
                        if (message != null) {
                            messageLst.add(message)
                        }
                    }
                }
            }


        })


        messageChatRecyclerView.apply {


            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = LinearLayoutManager(this@ChatMessengerActivity)

            // specify an viewAdapter (see also next example)
            adapter = ChatMessengerAdapter(messageLst)


            //setUpDrawer()

        }



        return messageLst

    }


}






