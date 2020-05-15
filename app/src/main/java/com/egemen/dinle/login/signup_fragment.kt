package com.egemen.dinle.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.egemen.dinle.R
import com.egemen.dinle.enums.def
import com.egemen.dinle.utils.*
import java.util.regex.Pattern


class signup_fragment: Fragment() {
    lateinit var fragmentView: View
    lateinit var navController: NavController
    lateinit var cinsiyet:String
    lateinit var mAuth: FirebaseAuth
    lateinit var mAuthListener : FirebaseAuth.AuthStateListener
    lateinit var  login_load_layout : ConstraintLayout
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private var storedVerificationId: String? = ""
    private var gelenKod =""
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentView = inflater?.inflate(R.layout.create_profile_layout, container, false)

        return fragmentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        mAuth=FirebaseAuth.getInstance()
        val back = fragmentView.findViewById<View>(R.id.ic_back_register) as ImageView
        val kayitOlButon = fragmentView.findViewById<View>(R.id.signup_button) as TextView
        val hesabimVarButon = fragmentView.findViewById<View>(R.id.login_text_button) as TextView
        var mailET= fragmentView.findViewById<View>(R.id.signup_email) as EditText
        var adET= fragmentView.findViewById<View>(R.id.signup_ad) as EditText
        var soyadET= fragmentView.findViewById<View>(R.id.signup_soyad) as EditText
        var cinsiyetET= fragmentView.findViewById<View>(R.id.signup_cinsiyet) as Spinner
        var telefonET= fragmentView.findViewById<View>(R.id.signup_phone_number) as EditText
        var sifreET= fragmentView.findViewById<View>(R.id.signup_password_1) as EditText
        var sifreikiET= fragmentView.findViewById<View>(R.id.signup_password_2) as EditText
        var touCheckBox =fragmentView.findViewById<View>(R.id.signup_tou_checkbox) as CheckBox
        var touText = fragmentView.findViewById<View>(R.id.signup_tou_text) as TextView
        login_load_layout =fragmentView.findViewById<View>(R.id.login_screen_loading_layout) as ConstraintLayout
        login_load_layout.setOnClickListener {}

        setupCallBack()
        back.setOnClickListener {
            activity!!.onBackPressed()
        }
        hesabimVarButon.setOnClickListener {
            activity!!.onBackPressed()
        }
        kayitOlButon.setOnClickListener {
            var ad = adET.text.toString()
            var soyad=soyadET.text.toString()
            var telefon=telefonET.text.toString()
            var sifre=sifreET.text.toString()
            var sifreiki=sifreikiET.text.toString()
            var mail=mailET.text.toString()
          //  cinsiyet
            if(!ad.isEmpty()){
              if(!soyad.isEmpty()){
                if(!mail.isEmpty() && mailvalidate.isEmailValid(mail))
                if(!telefon.isEmpty() ){
                 if(!sifre.isEmpty()){
                  if(!sifreiki.isEmpty()){
                    if(sifre.equals(sifreiki)){
                      if(touCheckBox.isChecked) {
                          shared.pStr(activity!!, def.egemen_ad, ad)
                          shared.pStr(activity!!, def.egemen_soyad, soyad)
                          shared.pStr(activity!!, def.egemen_mail, mail)
                          shared.pStr(activity!!, def.egemen_sifre, sifre)
                          telefon = "+9"+telefon
                          shared.pStr(activity!!, def.egemen_telefon, telefon)
                          shared.pStr(activity!!, def.egemen_sifre, sifre)
                          shared.pStr(activity!!, def.egemen_cinsiyet, cinsiyet)
                          val isim = ad + " " + soyad
                            //TODO yöntem 1 sms servisiyle numara verify ediyoruz.
                          navController.navigate(R.id.action_signup_fragment_to_tel_no_fragment) //TODO yöntem 2 firebase sms onay servisiyle numara onaylıyoruz.
                      }else{
                          mSnackbar.snackbar(it,"Devam etmek için kullanım sözleşmesini onaylamanız gerekmektedir.")
                      }
                    }  else{
                        mSnackbar.snackbar(it,"Şifreler birbiriyle eşleşmiyor ,kontrol ediniz")}//sifreler esit mi
                  }   else{
                      mSnackbar.snackbar(it,"Şifre alanı boş bırakılmamalıdır")}//sifre2
                 }   else{
                     mSnackbar.snackbar(it,"Şifre alanı boş bırakılmamalıdır")}//sifre1
                }  else{
                    mSnackbar.snackbar(it,"Telefon alanı boş bırakılmamalıdır")}//telefon
              }  else{
                  mSnackbar.snackbar(it,"Soyad alanı boş bırakılmamalıdır")}//soyad
            }else{
                mSnackbar.snackbar(it,"Ad alanı boş bırakılmamalıdır")}//ad

        }
        touText.setOnClickListener {
            startActivity(Intent(activity, TermsOfUse::class.java))
        }

        val adapter = ArrayAdapter(activity!!, android.R.layout.simple_spinner_item, listOf("Erkek", "Kadın"))
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
         cinsiyetET.adapter = adapter
                cinsiyetET.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                // either one will work as well
                // val item = parent.getItemAtPosition(position) as String
                val item = adapter.getItem(position)
                cinsiyet=adapter.getItem(position).toString()
                yaz.Companion.yaz("item "+cinsiyet);
            }
        }
    }


   private fun setupCallBack() {
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                try{
                gelenKod = credential.smsCode!!
                yaz.yaz("gelen kod"+gelenKod)
                }catch (e:java.lang.Exception){
                    e.printStackTrace();

                }
              }

            override fun onVerificationFailed(e: FirebaseException) {

            }

            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                storedVerificationId = verificationId
                yaz.yaz("storedVerificationID "+storedVerificationId+" token"+token!!)
            }
        }
    }

}