package com.example.secrett

import com.example.secrett.crypter.Crypter
import java.io.UnsupportedEncodingException
import java.security.NoSuchAlgorithmException

class Message {


    private var email: String? = null
    private var date: String? = null
    var message:String?=null
    var senderId:String?=null

    constructor(){}

    constructor(email: String?, date:String, message: String?,senderId:String?){
        this.message=message
        this.senderId=senderId
        this.email = email;
        this.date = date;
        if (message != null) {
            setMessage(message)
        };
    }

    fun getEmail(): String? {
        return email
    }

    fun getDate(): String? {
        return date
    }
    fun getSentUid(): String?{
        return senderId

    }

    @JvmName("getMessage1")
    fun getMessage(): String? {
        return message
    }



    @JvmName("setMessage1")
    private fun setMessage(message: String) {
        try {
            this.message = Crypter.decryptMessage(message)
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
    }

}