package fr.antoinebaudot.lab1mad.chat

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import fr.antoinebaudot.lab1mad.R


class ChatMessengerAdapter(val chatHistory: ArrayList<Message>) : RecyclerView.Adapter<CustomChatViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomChatViewHolder {
        val textView = LayoutInflater.from(parent.context)
                .inflate(R.layout.message_view, parent, false)

        return CustomChatViewHolder(textView)
    }

    override fun getItemCount() : Int = chatHistory.size


    override fun onBindViewHolder(holder: CustomChatViewHolder?, position: Int) {

        var textView = holder?.view?.findViewById<TextView>(R.id.message_text)
        textView?.text = chatHistory.get(position).text
        //You have to change the Message
        //
    }



}


class CustomChatViewHolder (val view : View) :  RecyclerView.ViewHolder(view) , View.OnClickListener{
    override fun onClick(v: View?) {
        Toast.makeText(view.context,"You clicked on the message", Toast.LENGTH_LONG).show()
    }


}