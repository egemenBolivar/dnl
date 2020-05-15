package com.egemen.dinle.models

class Users {
    var adi_soyadi:String?=null
    var email:String?=null
    var cinsiyet:String?=null
    var telefon:String?=null
    var user_id:String?=null
    constructor(){}
    constructor(adi_soyadi: String?, email: String?, cinsiyet: String?, telefon: String?,  user_id: String?) {
        this.adi_soyadi = adi_soyadi
        this.email = email
        this.cinsiyet = cinsiyet
        this.telefon = telefon
        this.user_id = user_id

    }


}