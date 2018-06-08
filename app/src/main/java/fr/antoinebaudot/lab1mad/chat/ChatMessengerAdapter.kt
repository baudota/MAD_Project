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
import android.widget.ImageView


class ChatMessengerAdapter(var chatHistory: ArrayList<Message>,val user: String) : RecyclerView.Adapter<CustomChatViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomChatViewHolder {
        val textView = LayoutInflater.from(parent.context)
                .inflate(R.layout.message_view, parent, false)

        return CustomChatViewHolder(textView)
    }

    override fun getItemCount() : Int = chatHistory.size


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: CustomChatViewHolder, position: Int) {

        val textView = holder.view.message_view_message_text
        val imageView = holder.view.message_view_proifle_picture
        val timeStampText = holder.view.message_view_timestampOfMessage
        //val colorUser = holder.view.context.getColor(R.color.colorPrimary)
        val corner = holder.view.message_corner as ImageView
        val colorUser = "#2874A6"
        val colorOtherUser = "#44d7da"
        val con = holder.view.context.getDrawable(R.drawable.rounded_rect)
        timeStampText?.text = chatHistory[position].timestamp
        textView?.text = chatHistory[position].text

        val drawableTmp = corner.background.mutate()

        if(chatHistory[position].user == user){

           /* drawableTmp.setColorFilter(Color.parseColor(colorUser),
                    PorterDuff.Mode.MULTIPLY)*/
            textView?.setBackgroundColor(Color.parseColor(colorUser))
            timeStampText?.setBackgroundColor(Color.parseColor(colorUser))
//            imageView.setBackgroundColor(Color.parseColor(colorUser))
        }else
        {
           // drawableTmp.setColorFilter(Color.parseColor(colorOtherUser), PorterDuff.Mode.MULTIPLY)

            textView?.setBackgroundColor(Color.parseColor(colorOtherUser))
            timeStampText?.setBackgroundColor(Color.parseColor(colorOtherUser))


        }

        //Loading Picture
        val pictureUrl = chatHistory[position].pictureUrl
        if(pictureUrl != null ) {
            val bitmap = decodeBase64(pictureUrl)
            imageView?.setImageBitmap(bitmap)
        }
        //You have to change the Message
        //
    }

     fun updateList(newChatHistory : ArrayList<Message>){
        val diffResult : DiffUtil.DiffResult = DiffUtil.calculateDiff(MyDiffUtil(chatHistory,newChatHistory))
        diffResult.dispatchUpdatesTo(this)
        chatHistory = newChatHistory

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