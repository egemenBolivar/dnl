package com.egemen.dinle.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.iid.FirebaseInstanceId

import com.egemen.dinle.MainHolder
import com.egemen.dinle.R
import com.egemen.dinle.enums.def
import com.egemen.dinle.utils.dbref.mRef
import com.egemen.dinle.models.Users
import com.egemen.dinle.utils.*
import java.lang.Exception
import java.util.regex.Pattern

class login_fragment : Fragment(){

    lateinit var fragmentView: View
    lateinit var navController: NavController
    lateinit var mAuth: FirebaseAuth
    lateinit var mAuthListener :FirebaseAuth.AuthStateListener
    lateinit var viewGlobal: View
    lateinit var login_load_layout: ConstraintLayout
    lateinit var mContext :Context
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentView = inflater?.inflate(R.layout.login_layout, container, false)

        return fragmentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        mAuth=FirebaseAuth.getInstance()
        val signUpButton = fragmentView.findViewById<View>(R.id.login_kayitol_buton) as TextView
        val girisYapButton = fragmentView.findViewById<View>(R.id.login_girisyap_buton) as TextView
        var emailTV = fragmentView.findViewById<View>(R.id.login_email) as EditText
        var passwordTV= fragmentView.findViewById<View>(R.id.login_sifre) as EditText
        login_load_layout =fragmentView.findViewById<View>(R.id.login_screen_loading_layout) as ConstraintLayout


        setupAuthListener()

        signUpButton.setOnClickListener {
        navController.navigate(R.id.action_login_fragment_to_signup_fragment)
        }

        girisYapButton.setOnClickListener {
            var email_edt: String
            var psw1_edt: String
            email_edt = emailTV.text.toString()
            psw1_edt = passwordTV.text.toString()
            if(connectioncontrol.isConnected(activity!!)){
                if(!email_edt.isEmpty()  ){
                  if(mailvalidate.isEmailValid(email_edt)){
                    if(psw1_edt.length >= 6){
                        viewGlobal = it
                        login_load_layout.visibility=View.VISIBLE;
                        oturumAc(email_edt,psw1_edt,it)
                    } else {
                        // Toast.makeText(this, "gecersiz sifre", Toast.LENGTH_SHORT).show()
                        mSnackbar.snackbar(it, "Geçersiz Şifre")
                    }//sifre
                    }else{
                        mSnackbar.snackbar(it, "Geçersiz Mail Adresi")
                    }
                }
                else {
                    //Toast.makeText(this, "gecersiz mail", Toast.LENGTH_SHORT).show()
                    mSnackbar.snackbar(it, "Mail Alanını Doldurunuz")

                }//mailadresi
            }else{
                mSnackbar.Rsnackbar(it, "İnternet  yok ")
            }//internet
        }
        login_load_layout.setOnClickListener {

        }
        //val back = fragmentView.findViewById<View>(R.id.ic_signup_step1_back) as ImageView
       /* back.setOnClickListener {
            activity!!.onBackPressed()
        }*/
    }
    private fun yeniTokenVeriTabaninaKaydet(yeniToken:String) {
        if(FirebaseAuth.getInstance().currentUser !=null){
            FirebaseDatabase.getInstance().getReference()
                    .child("users")
                    .child(FirebaseAuth.getInstance().currentUser!!.uid)
                    .child("fcm_token").setValue(yeniToken) }
    }
    private fun setupAuthListener() {
        mAuthListener=object :FirebaseAuth.AuthStateListener{
            override fun onAuthStateChanged(p0: FirebaseAuth) {
                var user=FirebaseAuth.getInstance().currentUser
                if(user!=null){




                        val a = Intent(mContext, MainHolder::class.java)
                        startActivity(a)
                      login_load_layout.visibility=View.GONE
                     //   finish()

                }

            }

        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context;

    }
    private fun isNumberValid(phone:String):Boolean{
         val REG = "^(\\+91[\\-\\s]?)?[0]?(91)?[789]\\d{9}\$"
        var PATTERN: Pattern = Pattern.compile(REG)
        return  PATTERN.matcher(phone).find()
    //   CharSequence.isPhoneNumber() : Boolean = PATTERN.matcher(this).find()
    }

    private fun bilgileriSharedeKaydet(okunanKullanici: Users){
        var ad=okunanKullanici.adi_soyadi!!.split(" ").get(0)
        var soyad =okunanKullanici.adi_soyadi!!.split(" ").get(1)
        shared.pStr(activity!!, def.egemen_ad,ad)
        shared.pStr(activity!!, def.egemen_soyad,soyad)
        shared.pStr(activity!!, def.egemen_mail, okunanKullanici.email.toString())
        shared.pStr(activity!!, def.egemen_telefon,okunanKullanici.telefon.toString())

    }
    private fun oturumAc(okunanKullanici: String, psw:String, view:View){
        val girisyapacakEmail= okunanKullanici

        mAuth.signInWithEmailAndPassword(girisyapacakEmail,psw)
                .addOnCompleteListener ( object : OnCompleteListener<AuthResult> {
                    override fun onComplete(p0: Task<AuthResult>) {
                        if(p0!!.isSuccessful){

                            fcmTokenKaydet()


                                kullaniciDenetle(girisyapacakEmail, psw) // Giriş yapıldı burda kullanıcın bilgilerini indiriyoruz

                            }
                        else{
                            // Toast.makeText(this@LogIn,"Email Veya Sifre Yanlis",Toast.LENGTH_SHORT).show()
                            mSnackbar.Rsnackbar(view, "Email Veya Sifre Yanlis")
                            login_load_layout.visibility=View.GONE;

                        }
                    }
                } )
    }

    override fun onResume() {
        super.onResume()
        mAuth.addAuthStateListener (mAuthListener)
    }
    private fun kullaniciDenetle(mail: String, psw: String) {
      /*  if(mAuthListener !=null){ // Hesap üretilince direk login yapmasını engellemek için kontrollü giriş sağlıyoruz
            mAuth.removeAuthStateListener (mAuthListener)
        }*/
        var isTrue=false;
        mRef.child("users").orderByChild("email").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(p0: DataSnapshot) {
              try {
                if(p0!!.getValue()!=null) {
                    login_load_layout.visibility=View.VISIBLE;
                    //TODO Visible
                    yaz.yaz("ne bu mail "+mail)
                    for (ds in p0!!.children) {
                        var okunanKullanici = ds.getValue(Users::class.java)
                        if (okunanKullanici!!.email!!.toString().trim().equals(mail)) {

                            isTrue=true;

                            //Burda Sharede Bilgileri Yaziyoruz
                            bilgileriSharedeKaydet(okunanKullanici)



                            break
                        }
                    }
                }
            }
            catch (e:Exception){e.printStackTrace()
            }
            }
        })
    }
    private fun fcmTokenKaydet() {
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
            var token=it.token
            yeniTokenVeriTabaninaKaydet(token)
        }
    }

}
