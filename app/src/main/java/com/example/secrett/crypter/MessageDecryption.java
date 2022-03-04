package com.example.secrett.crypter;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class MessageDecryption extends PublicKeyCreator {

    String  deMessage;
    byte[] sharedKey;
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String dMessage(String message, String hisPublicKey){

             sharedKey=  receiveSharedKey(hisPublicKey);
        Base64.Decoder mimeDecoder = java.util.Base64.getMimeDecoder();

        byte[] mimeDecoded = mimeDecoder.decode(String.valueOf(message));
        deMessage = new String(mimeDecoded, StandardCharsets.UTF_8);
       return deMessage;
    }
}







