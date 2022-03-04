package com.example.secrett.crypter;


import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

/*
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
    /* Send myPublicKey to other party, and get hisPublicKey in return

    //byte[] sharedKey = dh.computeSharedKey(hisPublicKey);
    /* sharedKey is now 'shared' between both parties

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;

import javax.crypto.KeyAgreement;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.DHPublicKeySpec;return  myPublicKey.toString();
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

}*/

public class PublicKeyCreator {

    public static String encryptMessage(String message) throws UnsupportedEncodingException, NoSuchAlgorithmException {
      // generates unique id
      String publicIdentity = UUID.randomUUID().toString();

      // for random encryption
      int privateNumberKey = generatePrivateNumberKey(publicIdentity) + getRandomNumber();

      // for "Are messages equals?" control
      String sha256 = toSHA256(message);

      // for XOR algorithm
      int createdNumber = (privateNumberKey + publicIdentity.length()) * (message.length() + sha256.length());
      String encryptedMessage = encryptMessageWithXOR(message, createdNumber);

      String[] result = new String[]{encryptedMessage, sha256, publicIdentity};
      return Arrays.toString(result);
    }

    public static String decryptMessage(String message) throws UnsupportedEncodingException, NoSuchAlgorithmException {
      String[] messageContents = parseMessageToArray(message);

      String encryptedMessage = messageContents[0];
      String sha256 = messageContents[1];
      String publicIdentity = messageContents[2];

      return findMessage(encryptedMessage, sha256, publicIdentity);
    }

    private static String[] parseMessageToArray(String message) {
      String temp = message.substring(1, message.length() - 1);
      int messageAreaIndex = temp.indexOf("], ") + 1;

      String messageText = temp.substring(0, messageAreaIndex);

      String secondArea = temp.substring(messageAreaIndex + 2);
      String[] shaAndPublicIdentity = secondArea.split(", ");

      String sha256Text = shaAndPublicIdentity[0];
      String publicIdentityText = shaAndPublicIdentity[1];

      return new String[]{messageText, sha256Text, publicIdentityText};
    }

    private static String toSHA256(final String base) throws NoSuchAlgorithmException, UnsupportedEncodingException {
      final MessageDigest digest = MessageDigest.getInstance("SHA-256");
      final byte[] hash = digest.digest(base.getBytes(StandardCharsets.UTF_8));

      final StringBuilder hexString = new StringBuilder();
      for (byte b : hash) {
        final String hex = Integer.toHexString(0xff & b);

        if (hex.length() == 1)
          hexString.append('0');
        hexString.append(hex);
      }

      return hexString.toString();
    }

    private static int generatePrivateNumberKey(String publicIdentity) {
      char[] privateKeyCharArray = publicIdentity.toCharArray();
      Set<Integer> uniquePrivateKeyNumbers = new HashSet<>();

      for (char character : privateKeyCharArray) {
        if (Character.isDigit(character)) {
          uniquePrivateKeyNumbers.add((int) character);
        }
      }

      int sumOfUniqueNumbers = 0;
      for (int number : uniquePrivateKeyNumbers) {
        sumOfUniqueNumbers += number;
      }

      return sumOfUniqueNumbers / uniquePrivateKeyNumbers.size();
    }

    private static int getRandomNumber() {
      Random random = new Random();
      return random.nextInt(100);
    }

    private static String encryptMessageWithXOR(String message, int createdNumber) {
      int[] result = new int[message.length()];

      for (int i = 0; i < message.length(); i++) {
        result[i] = createdNumber ^ message.charAt(i);
      }

      return Arrays.toString(result);
    }

    private static String findMessage(String encryptedMessage, String messageSHA256, String publicIdentity)
            throws UnsupportedEncodingException, NoSuchAlgorithmException {
      int publicIdentityLength = publicIdentity.length();
      int encryptedMessageLength = getEncryptedMessageLength(encryptedMessage);
      int messageSHA256Length = messageSHA256.length();

      int generatedNumberKey = generatePrivateNumberKey(publicIdentity);

      int i = 0;
      while (i < 100) {
        int privateNumberKey = generatedNumberKey + i;
        int tempCreatedNumber = (privateNumberKey + publicIdentityLength) * (encryptedMessageLength + messageSHA256Length);

        String decryptedMessage = decryptMessageWithXOR(encryptedMessage, tempCreatedNumber);
        String tempSHA256 = toSHA256(decryptedMessage);

        if (tempSHA256.equals(messageSHA256)) {
          return decryptedMessage;
        }

        i++;
      }

      return "Error! Message couldn't decrypted.";
    }

    private static int getEncryptedMessageLength(String encryptedMessage) {
      String temp = encryptedMessage.substring(1, encryptedMessage.length() - 1);
      temp = temp.replaceAll(", ", "-");
      String[] messageArray = temp.split("-");

      return messageArray.length;
    }

    private static String decryptMessageWithXOR(String encryptedMessage, int createdNumber) {
      String temp = encryptedMessage.substring(1, encryptedMessage.length() - 1);
      temp = temp.replaceAll(", ", "-");

      String[] messageArray = temp.split("-");

      StringBuilder result = new StringBuilder();
      for (String s : messageArray) {
        int firstDecrypt = createdNumber ^ Integer.parseInt(s) ^ createdNumber;
        int secondDecrypt = createdNumber ^ firstDecrypt;

        result.append((char) secondDecrypt);
      }

      return result.toString();
    }

  }




