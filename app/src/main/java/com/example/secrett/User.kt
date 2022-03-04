package com.example.secrett

import com.example.secrett.crypter.PublicKeyCreator

class User {
   var name: String? = null
   var email:String? = null
   var uid:  String?=null
  // var publicKey : String? = null
    constructor(){

    }
    constructor(name: String?, email: String?, uid: String?){//, publicKey: String?){
        this.email=email
        this.name=name
        this.uid=uid
      //  this .publicKey= publicKey
    }

}