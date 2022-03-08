/*package com.example.secrett

//import com.example.secrett.Message.message
//import com.example.secrett.Message.getSentUid
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.example.secrett.R
import com.example.secrett.MessageAdapter.SentViewHolder
import com.example.secrett.MessageAdapter.ReceiverViewHolder
import com.google.firebase.auth.FirebaseAuth
import android.widget.TextView
import java.util.ArrayList

class MessageAdapter(  //private static boolean isDarkBackground = false;
    private val messageList: ArrayList<Message>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var ITEM_RECEIVE = 2
    var ITEM_SENT = 1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return if (viewType == 1) {
            val view = layoutInflater.inflate(R.layout.sent, parent, false)
            SentViewHolder(view)
        } else {
            val view = layoutInflater.inflate(R.layout.receive, parent, false)
            ReceiverViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = messageList[position]
        if (holder.javaClass == SentViewHolder::class.java) {
            val viewHolder = holder as SentViewHolder
            // viewHolder.sMessage =currentMessage.getMessage();
            viewHolder.sentMessage.text = currentMessage.message
        } else {
            val viewHolder = holder as ReceiverViewHolder
            viewHolder.receivedMessage.text = currentMessage.message
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageList[position]
        return if (FirebaseAuth.getInstance().currentUser!!.uid == currentMessage.getSentUid()) {
            ITEM_SENT
        } else {
            ITEM_RECEIVE
        }
    }

    internal inner class SentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var sentMessage = itemView.findViewById<View>(R.id.txt_sent_message) as TextView
    }

    internal inner class ReceiverViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var receivedMessage = itemView.findViewById<View>(R.id.txt_sent_message) as TextView
    }
}

 */

package com.example.secrett

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(val context :Context ,val messageList:ArrayList<Message>) :RecyclerView.Adapter<RecyclerView.ViewHolder> (){

    val ITEM_RECEIVE = 1
    val ITEM_SENT =2


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType==1){
            val view : View = LayoutInflater.from(context).inflate(R.layout.receive,parent,false)
            return ReceiveViewHolder(view)
        }else{
            val view : View = LayoutInflater.from(context).inflate(R.layout.sent,parent,false)
            return SentViewHolder(view)
        }



    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val currentMessage = messageList[position]

        if(holder.javaClass==SentViewHolder::class.java){


            val viewHolder = holder as SentViewHolder

            viewHolder.sentMessage.text= currentMessage.message

        }else{

            val viewHolder = holder as ReceiveViewHolder
            viewHolder.receiveMessage.text= currentMessage.message

        }

    }



    override fun getItemCount(): Int {
        return messageList.size

    }

    override fun getItemViewType(position: Int): Int {

        val  currentMessage = messageList[position]

        if(FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)){
            return ITEM_SENT
        }else{
            return ITEM_RECEIVE
        }
    }

    class SentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val sentMessage = itemView.findViewById<TextView>(R.id.txt_sent_message)

    }
    class ReceiveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val receiveMessage = itemView.findViewById<TextView>(R.id.txt_receive_message)
    }
}