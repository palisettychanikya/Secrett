package com.example.secrett;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import java.lang.Object;
import com.example.secrett.crypter.Crypter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.Timestamp;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
//import java.security.Timestamp;
//import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Chat extends AppCompatActivity {
    private static final String FIREBASE_FIRESTORE_COLLECTION_NAME = "Messages";

    private final ArrayList<Message> messagesList = new ArrayList<>();

    private EditText messageBox;
    private RecyclerView chatRecyclerView;

    private MessageAdapter messageAdapter;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    String receiverUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        messageBox = findViewById(R.id.messageBox);
        chatRecyclerView = findViewById(R.id.chatRecyclerView);

        String name= getIntent().getStringExtra("name");
         receiverUid = getIntent().getStringExtra("uid");

        (Chat.this).getSupportActionBar().setTitle(name);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setReverseLayout(true);
        chatRecyclerView.setLayoutManager(linearLayoutManager);

        messageAdapter = new  MessageAdapter(messagesList);
        chatRecyclerView.setAdapter(messageAdapter);

        listenToMessagesChangesFromFirebase();


    }

    private void listenToMessagesChangesFromFirebase() {
        CollectionReference collectionReference = firebaseFirestore.collection(FIREBASE_FIRESTORE_COLLECTION_NAME);
        collectionReference.orderBy("date", Query.Direction.DESCENDING)
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
                                String deMessage = Crypter.decryptMessage(message);
                                String uid = (String) data.get("uid");
                                Timestamp serverDate = (Timestamp) data.get("date");

                               // assert serverDate != null;
                                Date date  = serverDate.toDate();
                                        // new    Date(serverDate.());

                                @SuppressLint("SimpleDateFormat")
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy - hh:mm a");

                                String userMessageDate = simpleDateFormat.format(date);

                                Message messageContent = new Message(email, userMessageDate, deMessage,uid);

                                messagesList.add(messageContent);
                                messageAdapter.notifyDataSetChanged();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
    }




    public void sendMessage(View view) {

        String userMessage = messageBox.getText().toString().trim();
        String senderUid= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        if (!userMessage.isEmpty()) {
            messageBox.setText("");

            String encryptedMessage = null;
            try {
                encryptedMessage = Crypter.encryptMessage(userMessage);

                FirebaseUser currentUser = firebaseAuth.getCurrentUser();

                Map<String, Object> message = new HashMap<>();
                assert currentUser != null;
                message.put("email", currentUser.getEmail());
                message.put("date", FieldValue.serverTimestamp());
                message.put("message", encryptedMessage);
                message.put("uid",senderUid);

                firebaseFirestore.collection(FIREBASE_FIRESTORE_COLLECTION_NAME)
                        .add(message)
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Chat.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                messageBox.setText(userMessage);
                            }
                        });
            } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

        }
    }

    }

