package com.example.secrett

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class UserAdapter(val context: Context,val userList: ArrayList<User>):RecyclerView.Adapter<UserAdapter.UserViewHolder>(){


    class UserViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){

        val textName = itemView.findViewById<TextView>(R.id.txt_name)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {

        val currentUser= userList[position]
        holder.textName.text=currentUser.name

        holder.itemView.setOnClickListener{
            val intent =Intent(context,Chat::class.java)
            intent.putExtra("name",currentUser.name)
            intent.putExtra("uid", currentUser.uid)
            //intent.putExtra("receiverPublicKey",currentUser.publicKey)
            context.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
       val view : View = LayoutInflater.from(context).inflate(R.layout.user_layout,parent,false)
     return UserViewHolder(view)
    }

    override fun getItemCount(): Int {
  return  userList.size
    }
}