package com.example.secrett;

import android.widget.TextView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;


public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    int  ITEM_RECEIVE = 2;
    int ITEM_SENT =1;

//private static boolean isDarkBackground = false;
private ArrayList<Message> messageList;




    public MessageAdapter(ArrayList<Message> messages) {
        this.messageList = messages;
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        if (viewType==1) {
            View view = layoutInflater.inflate(R.layout.sent, parent, false);
            return new SentViewHolder(view);
        }else {
            View view = layoutInflater.inflate(R.layout.receive, parent, false);
            return  new ReceiverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
      Message currentMessage = messageList.get(position);
      if(holder.getClass()==SentViewHolder.class) {
          SentViewHolder viewHolder = (SentViewHolder)holder;
         // viewHolder.sMessage =currentMessage.getMessage();
          viewHolder.sentMessage.setText(currentMessage.getMessage());

      }else {
     ReceiverViewHolder  viewHolder = (ReceiverViewHolder) holder;
          viewHolder.receivedMessage.setText(currentMessage.getMessage());
      }

        }

@Override
public int getItemCount() {
        return messageList.size();
        }



 @Override
 public int getItemViewType(int position){

     Message currentMessage = messageList.get(position);

     if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(currentMessage.getSentUid())){
         return ITEM_SENT;
     }else{
         return ITEM_RECEIVE;
     }
    }

    class SentViewHolder extends RecyclerView.ViewHolder{


        public SentViewHolder(@NonNull View itemView) {
            super(itemView);
        }
        TextView sentMessage = (TextView) itemView.findViewById(R.id.txt_sent_message);
       // String sMessage;
    }

    class ReceiverViewHolder extends RecyclerView.ViewHolder{

        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
        }
        TextView receivedMessage = (TextView) itemView.findViewById(R.id.txt_sent_message);
       // String rMessage;
    }


}

