package fr.antoinebaudot.lab1mad.chat

import android.content.Intent
import android.graphics.Color
import android.os.Parcel
import android.os.Parcelable
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.firebase.database.FirebaseDatabase
import fr.antoinebaudot.lab1mad.R
import fr.antoinebaudot.lab1mad.User
import kotlinx.android.synthetic.main.my_text_view.view.*


class ChatAdapter(val userLst : ArrayList<User>) : RecyclerView.Adapter<CustomViewHolder>() {

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): CustomViewHolder {
        // create a new view
        val textView = LayoutInflater.from(parent.context)
                .inflate(R.layout.my_text_view, parent, false)
        // set the view's size, margins, paddings and layout parameters

        return CustomViewHolder(userLst,textView)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = userLst.size

    override fun onBindViewHolder(holder: CustomViewHolder?, position: Int) {
        //holder?.view?.chat_text_view =
        val user = userLst.get(position)
        holder?.view?.chat_text_view?.text = user.name

        //set the attributes of icon and so on
    }


}

class CustomViewHolder(val userLst : ArrayList<User>,val  view : View) : RecyclerView.ViewHolder(view), View.OnClickListener{


    override fun onClick(v: View?) {
        val name = v?.chat_text_view?.text
        for (user in userLst) {
            if (user.name == name){
                view.setBackgroundColor(Color.GRAY)

                val intent = Intent(view.context , ChatMessengerActivity::class.java)
                intent.putExtra("Activity","ChatRecordActivity")
                intent.putExtra("user",user.email)
                view.context.startActivity(intent)

            }

        }



    }

    init {
        view.setOnClickListener(this)

    }
}