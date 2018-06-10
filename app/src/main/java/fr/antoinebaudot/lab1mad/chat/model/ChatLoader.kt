package fr.antoinebaudot.lab1mad.chat.model

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import fr.antoinebaudot.lab1mad.User

class ChatLoader (val reference : DatabaseReference, val user1 : String, val user2 : String){

    var messageLst : ArrayList<Message>? = null
    var keyForChatRecord : String? = null
    var objUser1 : User? = null
    var objUser2 : User? = null





    suspend fun loadChat(){


        reference.child("chat-users").child(user1).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(snap: DataSnapshot) {
                    if(snap.exists()) {

                        if (snap.hasChild(user2)) {

                            keyForChatRecord = snap.child(user2).value.toString()
                            reference.child("chat-record").child(keyForChatRecord!!).addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onCancelled(p0: DatabaseError) {
                                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                                }

                                override fun onDataChange(snap: DataSnapshot) {
                                    messageLst = arrayListOf()
                                    if (snap.exists()) {

                                        Log.d("ChatLoader","Creating MyDiffUtil")

                                        for (child in snap.children) {
                                            val message = child.getValue(Message::class.java)
                                            if (message != null) {
                                                messageLst!!.add(message)
                                            }
                                        }
                                    } else {
                                        Log.d("ChatLoader","No Messages to be load")


                                    }
                                }
                            })
                        }else {
                            //should be a never occuring case
                            reference.child("chat-users").child(user1).child(user2).setValue("$user1-$user2")
                            reference.child("chat-users").child(user2).child(user1).setValue("$user1-$user2")
                            keyForChatRecord = "$user1-$user2"
                            Log.d("ChatLoader","Should not occure")

                        }
                    }
                    else{
                        Log.d("ChatLoader","Create new Nodes")

                        reference.child("chat-users").child(user1).child(user2).setValue("$user1-$user2")
                        reference.child("chat-users").child(user2).child(user1).setValue("$user1-$user2")
                        keyForChatRecord = "$user1-$user2"
                    }

                reference.child("users").addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(snap: DataSnapshot) {

                        if(snap.exists()){

                            for (user in snap.children){
                                if(user.key == user1) {
                                    Log.d("KeyUser", "The key of User was  saved ${user.key}")
                                    objUser1 = user.getValue(User::class.java)
                                    break
                                }
                            }

                        }
                    }


                })

            }
        })
    if(messageLst == null){
        messageLst = arrayListOf()
    }


    }
}