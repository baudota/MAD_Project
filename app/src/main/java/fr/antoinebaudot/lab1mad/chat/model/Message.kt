package fr.antoinebaudot.lab1mad.chat.model

import android.graphics.Picture
import fr.antoinebaudot.lab1mad.User
import java.sql.Timestamp

public class Message(var user: User?, var text: String?, var timestamp: String?,var pictureUrl : String?) {

    /**
     * standard constructor, otherwise firebase does not work
     */
    constructor() : this(null,null,null,null)


    override fun toString(): String {
        return "$user \n $text \n $timestamp \n $pictureUrl"
    }
}
