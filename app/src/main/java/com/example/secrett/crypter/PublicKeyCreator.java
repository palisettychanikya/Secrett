package com.example.secrett.crypter;

import android.util.*;

import com.google.firebase.installations.Utils;
import java.lang.Integer.*;

import java.util.Base64;
import javax.crypto.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;

import javax.crypto.KeyAgreement;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.DHPublicKeySpec;

public class PublicKeyCreator {


  byte[] hisPublicKey ;
  byte[] sharedKey;

  DH dh = new DH();



  public   String createPublic(){

    byte[] myPublicKey = dh.generatePublicKey();
    /* Send myPublicKey to other party, and get hisPublicKey in return */

    //byte[] sharedKey = dh.computeSharedKey(hisPublicKey);
    /* sharedKey is now 'shared' between both parties */
    return  myPublicKey.toString();
  }

  byte[] receiveSharedKey(String hisPublicKey){

    this.hisPublicKey = hisPublicKey.getBytes(StandardCharsets.UTF_8);
    sharedKey = dh.generatePublicKey();
    return sharedKey;
  }


  public class DH {

    private static final String TAG = "DH";

    private KeyPair keyPair;
    private KeyAgreement keyAgree;

    public byte[] generatePublicKey() {
      DHParameterSpec dhParamSpec;

      try {
        dhParamSpec = new DHParameterSpec(P, G);
        //Log.i(TAG, "P = " + P.toString(16));
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("DiffieHellman");
        keyPairGen.initialize(dhParamSpec);
        keyPair = keyPairGen.generateKeyPair();
        //Log.i(TAG, "Y = " + ((DHPublicKey) keyPair.getPublic()).getY().toString(16));
        keyAgree = KeyAgreement.getInstance("DiffieHellman");
        keyAgree.init(keyPair.getPrivate());

        BigInteger pubKeyBI = ((DHPublicKey) keyPair.getPublic()).getY();
        byte[] pubKeyBytes = pubKeyBI.toByteArray();
        //Log.i(TAG, String.format(TAG, "Y [%d] = %s", pubKeyBytes.length, Utils.toHexString(pubKeyBytes)));
        return pubKeyBytes;
      } catch (Exception e) {
        //Log.e(TAG, "generatePubKey(): " + e.getMessage());
        return null;
      }
    }

    public byte[] computeSharedKey(byte[] pubKeyBytes) {
      if (keyAgree == null) {
        //Log.e(TAG, "computeSharedKey(): keyAgree IS NULL!!");
        return null;
      }

      try {
        KeyFactory keyFactory = KeyFactory.getInstance("DiffieHellman");
        BigInteger pubKeyBI = new BigInteger(1, pubKeyBytes);
       // Log.i(TAG, "Y = " + pubKeyBI.toString(16));
        PublicKey pubKey = keyFactory.generatePublic(new DHPublicKeySpec(pubKeyBI, P, G));
        keyAgree.doPhase(pubKey, true);
        byte[] sharedKeyBytes = keyAgree.generateSecret();
       // Log.i(TAG, String.format("SHARED KEY[%d] = %s", sharedKeyBytes.length, Utils.toHexString(sharedKeyBytes)));
        return sharedKeyBytes;
      } catch (Exception e) {
       // Log.e(TAG, "computeSharedKey(): " + e.getMessage());
        return null;
      }
    }

    private  final byte P_BYTES[] = {
            (byte)0xF4, (byte)0x88, (byte)0xFD, (byte)0x58, (byte)0xE9, (byte)0x2F, (byte)0x78, (byte)0xC7
  };
  private  final BigInteger P = new BigInteger(1, P_BYTES);

  private  final BigInteger G = BigInteger.valueOf(2);
}

}
