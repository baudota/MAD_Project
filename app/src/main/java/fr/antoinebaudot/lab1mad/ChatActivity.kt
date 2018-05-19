package fr.antoinebaudot.lab1mad

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.Toolbar
import android.view.Gravity
import android.view.View
import fr.antoinebaudot.lab1mad.R.id.drawerLayout
import fr.antoinebaudot.lab1mad.R.id.myToolbar
import kotlinx.android.synthetic.main.activity_chat.*

class ChatActivity : AppCompatActivity() {


    private lateinit var drawerFragment: NavigationDrawerFragment
    private lateinit var drawerLayout: DrawerLayout

    private lateinit var myToolbar  : Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        //Create Chat History


        setUpDrawer()



    }


    private fun setUpDrawer() {
        drawerFragment = NavigationDrawerFragment()
        drawerLayout = DrawerLayout(this)
        //val drawerFragment = supportFragmentManager.findFragmentById(R.id.nav_drawer_fragment) as NavigationDrawerFragment
        //val drawerLayout = findViewById<View>(R.id.drawerLayout) as DrawerLayout
        drawerFragment.setUpDrawer(R.id.nav_drawer_fragment, drawerLayout, myToolbar)
        myToolbar.setNavigationOnClickListener(View.OnClickListener { drawerLayout.openDrawer(Gravity.LEFT) })
    }

}
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
}*/