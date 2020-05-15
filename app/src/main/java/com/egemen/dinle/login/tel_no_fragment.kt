package com.egemen.dinle.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId

import com.egemen.dinle.R
import com.egemen.dinle.enums.def
import com.egemen.dinle.holder.begeni_fragment
import com.egemen.dinle.models.Users
import com.egemen.dinle.utils.connectioncontrol
import com.egemen.dinle.utils.dbref.mRef
import com.egemen.dinle.utils.mSnackbar
import com.egemen.dinle.utils.shared
import com.egemen.dinle.utils.yaz
import java.lang.Exception
import java.util.concurrent.TimeUnit

class tel_no_fragment:Fragment() {
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
        fragmentView = inflater?.inflate(R.layout.tel_no_layout, container, false)

        return fragmentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        mAuth = FirebaseAuth.getInstance()
        val back = fragmentView.findViewById<View>(R.id.ic_back_register) as ImageView
        val onaylabtn = fragmentView.findViewById<View>(R.id.tel_no_onayla) as TextView
        val onayKodET = fragmentView.findViewById<View>(R.id.tel_no_onaykod) as EditText
        val telnoTV = fragmentView.findViewById<View>(R.id.tel_no_telefon) as TextView
        login_load_layout =fragmentView.findViewById<View>(R.id.tel_no_loading_layout) as ConstraintLayout
        val telefon =shared.gStr(activity!!, def.egemen_telefon)
         gelenKod =shared.gStr(activity!!,def.egemen_verificationcode)
        val userad=shared.gStr(activity!!,def.egemen_ad)
        val usersoyad=shared.gStr(activity!!,def.egemen_soyad)
        val usermail=shared.gStr(activity!!,def.egemen_mail)
        val usercinsiyet=shared.gStr(activity!!,def.egemen_cinsiyet)
        val usersifre=shared.gStr(activity!!,def.egemen_sifre)
        val adsoyad =userad+" "+usersoyad

        login_load_layout.setOnClickListener {}

      setupCallBack()
        telnoTV.setText(telefon)
       PhoneAuthProvider.getInstance().verifyPhoneNumber(
                telefon, // Phone number to verify
                60, // Timeout duration
                TimeUnit.SECONDS, // Unit of timeout
                activity!!, // Activity (for callback binding)
                callbacks) // OnVerificationStateChangedCallbacks
        yaz.yaz("telefon numra "+telefon)
        onaylabtn.setOnClickListener {
            val kaydedilecekKullanici = Users(adsoyad,usermail,usercinsiyet,telefon,"")
            val onaykod=onayKodET.text.toString()
            if(gelenKod.equals(onaykod.trim())){
                if(connectioncontrol.isConnected(activity!!)){
                    login_load_layout.visibility=View.VISIBLE
                    mAuth.createUserWithEmailAndPassword(usermail, usersifre)
                            .addOnCompleteListener(object : OnCompleteListener<AuthResult> {
                                override fun onComplete(p0: Task<AuthResult>) {
                                    if (p0!!.isSuccessful) {
                                        try {
                                            var userid = mAuth.currentUser!!.uid
                                            mRef.child("users").child(userid).setValue(kaydedilecekKullanici)
                                                    .addOnFailureListener(object: OnFailureListener{
                                                        override fun onFailure(p0: Exception) {
                                                            mAuth.currentUser!!.delete()        //kaydedilemediyse kullanicinin authunu siliyoruz
                                                                    .addOnCompleteListener {
                                                                        object : OnCompleteListener<Void> {
                                                                            override fun onComplete(p0: Task<Void>) {
                                                                                if (p0!!.isSuccessful) {
                                                                                    yaz.yaz("kayit yapilamadi ,tekrar dene")
                                                                                    login_load_layout.visibility = View.GONE
                                                                                    mSnackbar.Rsnackbar(fragmentView, "kayit yapilamadi ,tekrar dene ")
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                        }
                                                    })
                                            mRef.child("users").child(mAuth.currentUser!!.uid).child("user_id").setValue(userid)
                                                    .addOnCompleteListener(object : OnCompleteListener<Void> {
                                                        override fun onComplete(p0: Task<Void>) {
                                                            if (p0!!.isSuccessful) {
                                                                fcmTokenKaydet()
                                                                yaz.yaz("tamadir kayıt oldu")
                                                                login_load_layout.visibility = View.GONE
                                                                //  basildi = true;
                                                                // if ((kisiFoto != null) && !kisiFoto!!.equals("null")) {
                                                                //
                                                                //bekle
                                                                //  } else {


                                                                /*  val a = Intent(activity!!, AnaSayfaMusteri::class.java)
                                                                a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                                                startActivity(a)
                                                                login_load_layout.visibility=View.GONE
                                                                activity!!.finish()*/

                                                                //   }
                                                                setupAuthListener()

                                                            } else {
                                                                mAuth.currentUser!!.delete()        //kaydedilemediyse kullanicinin authunu siliyoruz
                                                                        .addOnCompleteListener {
                                                                            object : OnCompleteListener<Void> {
                                                                                override fun onComplete(p0: Task<Void>) {
                                                                                    if (p0!!.isSuccessful) {
                                                                                        yaz.yaz("kayit yapilamadi ,tekrar dene")
                                                                                        login_load_layout.visibility = View.GONE
                                                                                        mSnackbar.Rsnackbar(fragmentView, "kayit yapilamadi ,tekrar dene ")
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                            }


                                                        }


                                                    })

                                        }catch (e:Exception){e.printStackTrace()}
                                    } else {
                                        login_load_layout.visibility=View.GONE
                                        yaz.yaz("Kayıt Yapılamadi " + p0!!.exception)
                                        mSnackbar.Rsnackbar(fragmentView,"Kayıt Yapılamadi \n" + p0!!.exception)
                                        activity!!.onBackPressed()
                                    }
                                }

                            })
                            .addOnFailureListener(object : OnFailureListener {
                                override fun onFailure(p0: Exception) {
                                    login_load_layout.visibility=View.GONE
                                    yaz.yaz("Kayıt Yapılamadi " + p0!!.message)
                                    activity!!.onBackPressed()
                                }

                            })


                }else{
                    mSnackbar.Rsnackbar(it,"Bağlantı Sorunu")
                }

            }else{
                mSnackbar.Rsnackbar(it,"Hatalı Kod")

            }
        }


    }
    private fun fcmTokenKaydet() {
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
            var token=it.token
            yeniTokenVeriTabaninaKaydet(token)
        }
    }
    private fun yeniTokenVeriTabaninaKaydet(yeniToken:String) {
        if(FirebaseAuth.getInstance().currentUser !=null){
            FirebaseDatabase.getInstance().getReference()
                    .child("users")
                    .child(FirebaseAuth.getInstance().currentUser!!.uid)
                    .child("fcm_token").setValue(yeniToken) }
    }
    private fun setupCallBack() {
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                if(!credential.smsCode.isNullOrEmpty())
                gelenKod = credential.smsCode!!

                yaz.yaz("gelen kod"+gelenKod)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                e.printStackTrace();
              mSnackbar.Rsnackbar(fragmentView,"Çok fazla deneme yapıldığından numara blocklanmıştır lütfen daha sonra tekrar deneyin",lenght = Snackbar.LENGTH_LONG)
              navController.navigate(R.id.action_tel_no_fragment_to_login_fragment)
                // activity!!.onBackPressed()
            }

            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                storedVerificationId = verificationId
                yaz.yaz("storedVerificationID "+storedVerificationId+" token"+token!!)
            }
        }
    }
    private fun setupAuthListener() {
        mAuthListener=object :FirebaseAuth.AuthStateListener{
            override fun onAuthStateChanged(p0: FirebaseAuth) {
                var user=FirebaseAuth.getInstance().currentUser
                if(user!=null){




                    val a = Intent(context, begeni_fragment::class.java)
                    startActivity(a)
                    login_load_layout.visibility=View.GONE
                    //   finish()

                }

            }

        }
    }
}