package fr.antoinebaudot.lab1mad.chat

import android.content.Intent
import android.graphics.*
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.squareup.picasso.Transformation
import fr.antoinebaudot.lab1mad.MainActivity
import fr.antoinebaudot.lab1mad.R
import fr.antoinebaudot.lab1mad.User
import kotlinx.android.synthetic.main.chat_record_item.view.*
import kotlin.collections.ArrayList





class ChatAdapter(val userHash : LinkedHashMap<String,User>) : RecyclerView.Adapter<CustomViewHolder>() {

    val userlst : ArrayList<User>
    init {
         userlst = ArrayList(userHash.values)
    }



    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): CustomViewHolder {
        // create a new view
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.chat_record_item, parent, false)
        // set the view's size, margins, paddings and layout parameters

        return CustomViewHolder(userHash,view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val user = userlst.get(position)
        holder.view.chat_text_view?.text = user.name
        // Picasso.with(activity).load(mayorShipImageLink).transform(CircleTransform()).into(ImageView)
        val imageView = holder.view.message_view_proifle_picture

        if(user.profilePicture != null) {
            val bitmap = MainActivity.decodeBase64(user.profilePicture)
            imageView.setImageBitmap(bitmap)
            // Picasso.get().load(user.profilePicture).into(image)
        }else
        {
            // Set tmp image
            // imageView.setImageIcon(holder.view.resources.getP)

        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = userlst.size



}

class CustomViewHolder(val userHash : LinkedHashMap<String,User>,val  view : View) : RecyclerView.ViewHolder(view), View.OnClickListener{



    override fun onClick(v: View?) {
        val name = v?.chat_text_view?.text
        userHash.keys.forEach {

            if(userHash[it]?.name?.equals(name)!!){

                v?.setBackgroundColor(Color.GRAY)

                val intent = Intent(view.context, ChatMessengerActivity::class.java)
                intent.putExtra("Activity", "ChatRecordActivity")
                intent.putExtra("User2",it)
                v?.context?.startActivity(intent)

        }
            }
        }

    init {
        view.setOnClickListener(this)

    }
}

