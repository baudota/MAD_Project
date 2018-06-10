package fr.antoinebaudot.lab1mad.chat

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import fr.antoinebaudot.lab1mad.NavigationDrawerFragment
import fr.antoinebaudot.lab1mad.R
import fr.antoinebaudot.lab1mad.R.id.messageRecyclerView
import fr.antoinebaudot.lab1mad.R.id.showProfileToolbar
import fr.antoinebaudot.lab1mad.User
import kotlinx.android.synthetic.main.activity_chat.*
import android.support.v7.widget.DividerItemDecoration
import android.view.Gravity
import android.view.View


class ChatRecordActivity : AppCompatActivity() {


    private lateinit var drawerFragment: NavigationDrawerFragment
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var mFirebase: DatabaseReference
    private lateinit var mFirebaseUser: FirebaseUser


    private lateinit var myToolbar: Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        myToolbar = showProfileToolbar as Toolbar
        setSupportActionBar(myToolbar)

        //setUpDrawer()

        //DataSet = ChatHistory

        //val viewAdapter = ChatAdapter(array!!)
        messageRecyclerView.layoutManager = LinearLayoutManager(this)
        //messageRecyclerView.setBackgroundColor(Color.BLUE)

        mFirebase = FirebaseDatabase.getInstance().reference.root
        mFirebaseUser = FirebaseAuth.getInstance().currentUser!!

        val userLst: ArrayList<User> = arrayListOf()
        val userIdLst: HashSet<String> = hashSetOf()

        //read DataBase
        mFirebase.child("chat-users").child(mFirebaseUser.uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(snap: DataSnapshot) {
                if (snap.exists()) {

                    for (item in snap.children) {
                        if (item != null) {
                            Log.d("MychatTag", "Add value to userIDlst ${item.key}")


                            userIdLst.add(item.key!!)
                        }
                    }
                    Log.d("MychatTag", "Size of userIDLst First ${userIdLst.size}")

                    readHistoryRecord(userIdLst, userLst, mFirebase)
                    Log.d("MychatTag", "Size of userLst First ${userLst.size}")

                    ///push the History with the name to the next Activity!


                }
            }
        })


    }

    private fun readHistoryRecord(userIDLst: HashSet<String>, userLst: ArrayList<User>, ref: DatabaseReference) {

        val userHashMap: LinkedHashMap<String, User> = LinkedHashMap()

        ref.child("users").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {
                    Log.d("MychatTag", "Gehe in die if abfrage")

                    for (item in snapshot.children) {
                        // Log.d("MychatTag" ,"item key von dem snapshot  = ${item.key}")
                        Log.d("MychatTag", "item key = ${item.key} ist true? ${userIDLst.contains(item.key)}  ")
                        if (userIDLst.contains(item.key)) {
                            val user = item.getValue(User::class.java)
                            Log.d("MychatTag", "User is != null ? ? = ${user != null}")
                            if (user != null && item.key != null) {
                                //set The User for all
                                userHashMap.set(item.key!!, user)
                            }
                        }
                    }
                    Log.d("MychatTag", "After the loop , userLst = ${userLst.size} ")

                    messageRecyclerView.apply {

                        setHasFixedSize(true)

                        // use a linear layout manager
                        layoutManager = LinearLayoutManager(this@ChatRecordActivity)

                        // specify an viewAdapter (see also next example)
                        adapter = ChatAdapter(userHashMap)


                        //setUpDrawer()
                    }

                }

            }
        })

        Log.d("MychatTag", "Wurde die Userliste aktualisiert? size  = ${userLst.size} ")

    }
}

/*
    private fun setUpDrawer() {
        val drawerFragment = supportFragmentManager.findFragmentById(R.id.nav_drawer_fragment) as NavigationDrawerFragment
        val drawerLayout = findViewById<View>(R.id.drawerLayout) as DrawerLayout
        drawerFragment.setUpDrawer(R.id.nav_drawer_fragment, drawerLayout, myToolbar)
        myToolbar.setNavigationOnClickListener { drawerLayout.openDrawer(Gravity.LEFT) }
    }
    */

    /*
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
        */



