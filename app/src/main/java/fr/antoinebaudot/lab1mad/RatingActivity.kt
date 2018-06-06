package fr.antoinebaudot.lab1mad

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.widget.Button
import android.widget.RatingBar
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.internal.FirebaseAppHelper
import fr.antoinebaudot.lab1mad.R


class RatingActivity : AppCompatActivity() {

    val mDatabase = FirebaseDatabase.getInstance().reference




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rating)

        val myToolbar = findViewById<Toolbar>(R.id.myToolbar)
        setSupportActionBar(myToolbar)


        val ratingBar = findViewById<RatingBar>(R.id.ratingBar)
        if (ratingBar != null) {
            val button = findViewById<Button>(R.id.button)
            button?.setOnClickListener {
                val rate = ratingBar.rating.toString()
                val id = intent.extras["USER_ID"]

                val mRef = mDatabase.root.child("rates").child(id.toString());

                mRef.push().setValue(rate)


                Toast.makeText(this@RatingActivity, "Thanks for your rate !", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }


}