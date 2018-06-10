package fr.antoinebaudot.lab1mad.chat

import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.text.format.Time
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import fr.antoinebaudot.lab1mad.MainActivity.decodeBase64
import fr.antoinebaudot.lab1mad.R
import fr.antoinebaudot.lab1mad.chat.model.Message
import fr.antoinebaudot.lab1mad.chat.model.MyDiffUtil
import kotlinx.android.synthetic.main.chat_record_item.view.*
import kotlinx.android.synthetic.main.message_view.view.*
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.ImageView
import fr.antoinebaudot.lab1mad.MainActivity
import fr.antoinebaudot.lab1mad.User


class ChatMessengerAdapter(private var chatHistory: ArrayList<Message>,var objUser: User?) : RecyclerView.Adapter<CustomChatViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomChatViewHolder {
        val textView = LayoutInflater.from(parent.context)
                .inflate(R.layout.message_view, parent, false)

        return CustomChatViewHolder(textView)
    }

    override fun getItemCount() : Int = chatHistory.size


    override fun onBindViewHolder(holder: CustomChatViewHolder, position: Int) {

        val textView = holder.view.message_view_message_text
        val imageView = holder.view.proflePicture
        val timeStampText = holder.view.message_view_timestampOfMessage


        val colorUser = "#2874A6"
        val colorOtherUser = "#44d7da"
        val tmp = chatHistory[position].timestamp
        timeStampText?.text = tmp?.removeRange(tmp.length-6,tmp.length)
        textView?.text = chatHistory[position].text
        val user = chatHistory[position].user
        Log.d("UserAdapter", "User =  ${user.toString()}")
        Log.d("UserAdapter", "Position =  ${position}")

        Log.d("UserAdapter", "Der cut des Timestamps ${tmp?.removeRange(tmp.length-6,tmp.length)}")
        Log.d("UserAdapter", "User email ${user?.email}")
        if(user != null) {
            if (user.email == objUser?.email) {


                textView?.setBackgroundColor(Color.parseColor(colorUser))
                timeStampText?.setBackgroundColor(Color.parseColor(colorUser))

                //imageView.setBackgroundColor(Color.parseColor(colorUser))
            } else {

                textView?.setBackgroundColor(Color.parseColor(colorOtherUser))
                timeStampText?.setBackgroundColor(Color.parseColor(colorOtherUser))


            }
            if(user.profilePicture != null){
                val bitmap = MainActivity.decodeBase64(user.profilePicture)
                imageView.setImageBitmap(bitmap)

            }else {
                val icon = ContextCompat.getDrawable(holder.view.context, R.mipmap.ic_launcher_round)
                imageView.setImageDrawable(icon)
            }
        }
    }

     fun updateList(newChatHistory : ArrayList<Message>, user : User?){
        val diffResult : DiffUtil.DiffResult = DiffUtil.calculateDiff(MyDiffUtil(chatHistory,newChatHistory))
        diffResult.dispatchUpdatesTo(this)
        chatHistory = newChatHistory
         objUser = user

    }


}


class CustomChatViewHolder (val view : View) :  RecyclerView.ViewHolder(view) , View.OnClickListener{
    override fun onClick(v: View?) {

        Toast.makeText(view.context,"You clicked on the message", Toast.LENGTH_LONG).show()
    }

    init {
        view.setOnClickListener(this)
    }

}