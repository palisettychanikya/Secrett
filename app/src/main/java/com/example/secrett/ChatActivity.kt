package com.example.secrett

import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.example.secrett.crypter.*




class ChatActivity : AppCompatActivity() {

    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messageBox:EditText
    private lateinit var sendButton: Button
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>
    private lateinit var mDbRef : DatabaseReference

    var receiverRoom:String?=null
    var senderRoom:String? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)


        val name =intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("uid")
        //val receiverPublicKey = intent.getStringExtra("receiverPublicKey")


        val senderUid = FirebaseAuth.getInstance().currentUser?.uid

        mDbRef =FirebaseDatabase.getInstance().reference



        receiverRoom = senderUid + receiverUid

        senderRoom = receiverUid  +   senderUid

        supportActionBar?.title=name

        chatRecyclerView = findViewById(R.id.chatRecyclerView)
        messageBox = findViewById(R.id.messageBox)
        sendButton =findViewById(R.id.sentButton)
      messageList = ArrayList()
        messageAdapter = MessageAdapter(this, messageList)

        chatRecyclerView.layoutManager=LinearLayoutManager(this)
        chatRecyclerView.adapter= messageAdapter

        mDbRef.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object: ValueEventListener {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onDataChange(snapshot: DataSnapshot) {

                    messageList.clear()

                    for(postSnapShot in snapshot.children){
                        val message = postSnapShot.getValue(Message::class.java)
                        //todo add decryption here
                      //  val decryptM = MessageDecryption()
                       // val deMessage= decryptM.dMessage(message?.message,receiverPublicKey)
                      //  val newMessage = Message(deMessage, message?.senderId)
                        messageList.add(message!!)
                    }
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

        sendButton.setOnClickListener{

            val message=messageBox.text.toString()
            //todo add encryption here

           // val encryptM = MessageEncryption()
           // val deMessage= encryptM.enMessage(message,receiverPublicKey)
            val messageObjects = Message(message,senderUid)

            mDbRef.child("chats").child(senderRoom!!).child("messages").push()
                .setValue(messageObjects).addOnSuccessListener {
                    mDbRef.child("chats").child(receiverRoom!!).child("messages").push()
                        .setValue(messageObjects)
                }
               messageBox.setText("")
        }

    }
}

