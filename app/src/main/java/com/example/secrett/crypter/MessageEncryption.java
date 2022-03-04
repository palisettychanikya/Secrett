package com.example.secrett.crypter;


import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Base64;


public class MessageEncryption extends PublicKeyCreator {
    String  enMessage;
    byte[] sharedKey;
   @RequiresApi(api = Build.VERSION_CODES.O)
   public String enMessage(String message, String hisPublicKey){

      sharedKey= receiveSharedKey(hisPublicKey);
       Base64.Encoder mimeEncoder = java.util.Base64.getMimeEncoder();

       enMessage= mimeEncoder.encodeToString(message.getBytes());
       return enMessage;
       }

}





