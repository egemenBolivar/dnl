package com.egemen.dinle.holder

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.egemen.dinle.R
import com.egemen.dinle.adapters.KitaplarRecyclerAdapter
import com.egemen.dinle.models.Kitap


class kitaplar_fragment : Fragment() {
    var isConnected:Boolean=true
    lateinit var fragmentView: View
    lateinit var arrayList:ArrayList<Kitap>
    lateinit var arrayList1:ArrayList<String>
    lateinit var login_load_layout: ConstraintLayout
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentView = inflater?.inflate(R.layout.kitaplar_fragment, container, false)
        login_load_layout =fragmentView.findViewById<View>(R.id.login_screen_loading_layout) as ConstraintLayout
        login_load_layout.visibility = View.VISIBLE


        return fragmentView
    }

    override fun onResume() {
        super.onResume()


            bilgileriGetir()



    }

    private fun bilgileriGetir() {
        arrayList1 = ArrayList<String>()
        arrayList=ArrayList<Kitap>()
        FirebaseDatabase.getInstance().getReference().child("Kitaplar")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        login_load_layout.visibility = View.GONE
                    }
                    override fun onDataChange(p0: DataSnapshot) {
                        if(p0!!.getValue()!=null){
                            Log.e("yfysoftware","oku2 ")
                            for(user in p0!!.children){
                                var okunanUser = user!!.getValue(Kitap::class.java)
                                var userid=okunanUser!!.kitapuid
                                Log.e("yfysoftware","userid "+userid)
                                arrayList.add(okunanUser)

                            }
                            setupRecyclerView()
//                            for(i in 0..tumDoktorlar.size-1) {
//                                var kullaniciID = tumDoktorlar.get(i)
//                                FirebaseDatabase.getInstance().getReference().child("uzmanlar").child(kullaniciID!!)
//                                        .addListenerForSingleValueEvent(object : ValueEventListener {
//                                            override fun onCancelled(p0: DatabaseError) {
//                                                login_load_layout.visibility = View.GONE
//                                            }
//                                            override fun onDataChange(p0: DataSnapshot) {
//                                                if (p0!!.getValue() != null) {
//
//                                                    var okunanuser=p0.getValue(Uzmanlar::class.java)
//                                                    var eklenecekuser =Uzmanlar()
//                                                    var userName=okunanuser!!.adi_soyadi
//                                                    var email=okunanuser!!.email
//                                                    Log.e("yfysoftware","okunan "+email)
//                                                    kullanicilar.add(okunanuser)
//
//                                                }
//
//                                                setupRecyclerView()
//                                            }
//                                        })
//                            }
                        }



                    }
                })
    }

    private fun setupRecyclerView() {
        if(activity!=null) {
            var recyclerView = fragmentView.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recyclerview_doktorlar)
            var recyclerAdapter = KitaplarRecyclerAdapter(activity, arrayList)

            recyclerView.adapter = recyclerAdapter
            recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity, androidx.recyclerview.widget.LinearLayoutManager.VERTICAL, false)
            login_load_layout.visibility = View.GONE
        }
    }
    private fun internetkontrol() {

        val cm = activity!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo

        isConnected = activeNetwork?.isConnected == true

    }

    override fun onStart() {
        super.onStart()

    }

    override fun onStop() {
        super.onStop()

    }

}