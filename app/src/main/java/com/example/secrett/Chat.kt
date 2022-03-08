



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




class Chat : AppCompatActivity() {

    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messageBox:EditText
    private lateinit var sendButton: Button
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>
    private lateinit var mDbRef : DatabaseReference
    lateinit var  newMessage: Message
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
                       // val decryptM = MessageDecryption()
                        val deMessage= Crypter.decryptMessage(message?.message)
                         newMessage = Message(deMessage, message?.senderId)
                        messageList.add(newMessage)
                    }
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

        sendButton.setOnClickListener{

            val message=messageBox.text.toString().trim()
            //todo add encryption here


            val enMessage= Crypter.encryptMessage(message)
            val messageObjects = Message(enMessage,senderUid)

            mDbRef.child("chats").child(senderRoom!!).child("messages").push()
                .setValue(messageObjects).addOnSuccessListener {
                    mDbRef.child("chats").child(receiverRoom!!).child("messages").push()
                        .setValue(messageObjects)
                }
            messageBox.setText("")
        }

    }
}



/*
import androidx.appcompat.app.AppCompatActivity
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.example.secrett.MessageAdapter
import com.google.firebase.auth.FirebaseAuth
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.secrett.R
import com.google.firebase.database.FirebaseDatabase
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.secrett.Chat
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.example.secrett.crypter.Crypter
import com.google.firebase.auth.FirebaseUser
import com.google.android.gms.tasks.OnSuccessListener
import java.io.UnsupportedEncodingException
import java.security.NoSuchAlgorithmException
import java.util.*

//import java.security.Timestamp;
//import java.sql.*;
class Chat : AppCompatActivity() {
    private val messagesList = ArrayList<Message>()
    val FIREBASE_FIRESTORE_COLLECTION_NAME = "Messages"
    //  ProgressBar loadingPB;
    private var messageBox: EditText? = null
    private var chatRecyclerView: RecyclerView? = null
    var btnsend: Button? = null
    var databaseReference: DatabaseReference? = null
    var receiverRoom: String? = null
    var senderRoom: String? = null
    private var messageAdapter: MessageAdapter? = null

    // private FirebaseFirestore firebaseFirestore;
    private var firebaseAuth: FirebaseAuth? = null
    var receiverUid: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        //  firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference
        messageBox = findViewById(R.id.messageBox)
        chatRecyclerView = findViewById(R.id.chatRecyclerView)
        btnsend = findViewById(R.id.sentButton)
        val name = intent.getStringExtra("name")
        receiverUid = intent.getStringExtra("uid")
        val senderUid =FirebaseAuth.getInstance().currentUser?.uid
        receiverRoom = senderUid + receiverUid
        senderRoom = receiverUid + senderUid
        supportActionBar?.title=name
        messageAdapter = MessageAdapter(this, messagesList)
        chatRecyclerView.layoutManager=LinearLayoutManager(this)
        chatRecyclerView.adapter= messageAdapter

        databaseReference!!.child("chats").child(senderRoom!!).child(FIREBASE_FIRESTORE_COLLECTION_NAME).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                messagesList.clear()
                for(postSnapShot in snapshot.children){
                    val message = postSnapShot.getValue(Message::class.java)
                    //todo add decryption here
                    //val decryptM = MessageDecryption()
                   // val deMessage= decryptM.dMessage(message?.message,receiverPublicKey)
                    val newMessage = Message(message , message?.senderId)
                    messagesList.add(newMessage)
                }
                messageAdapter!!.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
        //  CollectionReference collectionReference = firebaseFirestore.collection(FIREBASE_FIRESTORE_COLLECTION_NAME);
        /* collectionReference.orderBy("date", Query.Direction.DESCENDING)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.e(String.valueOf(error),"did not get snapshot");
                        return;
                    }

                    messagesList.clear();

                    if (value != null && !value.isEmpty()) {
                        try {
                            for (DocumentSnapshot data : value.getDocuments()) {
                                String email = (String) data.get("email");
                                String message = (String) data.get("message");
                                //String deMessage = Crypter.decryptMessage(message);
                                String uid = (String) data.get("uid");
                                / *Timestamp serverDate = (Timestamp) data.get("date");
                                assert serverDate != null;
                                Date date  = serverDate.toDate();


                                @SuppressLint("SimpleDateFormat")
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy - hh:mm a");

                                String userMessageDate = simpleDateFormat.format(date);


                                Message messageContent = new Message(email, message,uid);

                                messagesList.add(messageContent);

                                messageAdapter.notifyDataSetChanged();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });


        firebaseFirestore.collection(FIREBASE_FIRESTORE_COLLECTION_NAME).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                     try {
                         if (!queryDocumentSnapshots.isEmpty()) {
                           //  loadingPB.setVisibility(View.GONE);
                             List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                             for (DocumentSnapshot d : list) {
                                 // after getting this list we are passing
                                 // that list to our object class.
                                 //Courses c = d.toObject(Courses.class);

                                 // and we will pass this object class
                                 // inside our arraylist which we have
                                 // created for recycler view.
                                 String email = (String) d.get("email");
                                 String message = (String) d.get("message");
                                 String uid = (String) d.get("uid");
                                 Message messageContent = new Message(email,message,uid);// new Message(email, message,uid);
                                 messagesList.add(messageContent);
                                 messageAdapter.notifyDataSetChanged();
                             }
                         }
                     }catch (Exception e) {
                         e.printStackTrace();
                     }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Chat.this, "Fail to get the data.", Toast.LENGTH_SHORT).show();
            }
        });


        */btnsend!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                val userMessage = messageBox.getText().toString().trim()
                val senderUid = FirebaseAuth.getInstance().currentUser.uid
                if (!userMessage.isEmpty()) {
                    var encryptedMessage: String? = null
                    try {
                        encryptedMessage = Crypter.encryptMessage(userMessage)
                        val currentUser = firebaseAuth!!.currentUser
                        val email = currentUser!!.email
                        val messageObjects = Message(email, encryptedMessage, senderUid)
                        databaseReference!!.child("chats").child(senderRoom!!).child(
                            FIREBASE_FIRESTORE_COLLECTION_NAME
                        ).push().setValue(messageObjects).addOnSuccessListener(
                            OnSuccessListener {
                                databaseReference!!.child("chats").child(receiverRoom!!)
                                    .child("messages").push()
                                    .setValue(messageObjects)
                            })
                    } catch (e: UnsupportedEncodingException) {
                        e.printStackTrace()
                    } catch (e: NoSuchAlgorithmException) {
                        e.printStackTrace()
                    }
                    messageBox.setText("")
                }
            }
        })
    }


}
*/