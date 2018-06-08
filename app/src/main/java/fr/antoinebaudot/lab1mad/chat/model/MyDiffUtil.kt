package fr.antoinebaudot.lab1mad.chat.model

import android.support.v7.util.DiffUtil

class MyDiffUtil(val newMessageLst: List<Message>, val oldMessageLst: List<Message>) : DiffUtil.Callback() {


    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return newMessageLst.get(newItemPosition).timestamp == oldMessageLst.get(oldItemPosition).timestamp
    }

    override fun getOldListSize() = oldMessageLst.size

    override fun getNewListSize() = newMessageLst.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return newMessageLst.get(newItemPosition).equals(oldMessageLst.get(oldItemPosition))
    }


}