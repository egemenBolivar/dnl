package com.egemen.dinle.holder

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.egemen.dinle.R
import com.egemen.dinle.adapters.BegeniRecyclerAdapter
import com.egemen.dinle.models.Kitap

import com.egemen.dinle.utils.Constants.currentUser

class begeni_fragment  : Fragment() {
    lateinit var mRef: DatabaseReference
    lateinit var mAuth: FirebaseAuth
    lateinit var kullanicilar:ArrayList<Kitap>
    var isConnected:Boolean=true
    lateinit var childListener:ChildEventListener
    lateinit var recyclerView: androidx.recyclerview.widget.RecyclerView
    lateinit var recyclerAdapter: BegeniRecyclerAdapter
    lateinit var mDatabaseReference: DatabaseReference
    lateinit var fragmentView: View

    lateinit var mContext:Context

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentView = inflater?.inflate(R.layout.general_recycler, container, false)
        mContext= activity!!.applicationContext
        return fragmentView
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mRef = FirebaseDatabase.getInstance().reference
        mAuth = FirebaseAuth.getInstance()

        mDatabaseReference = FirebaseDatabase.getInstance().reference


    }





    private fun setupRecyclerView() {
        recyclerView = fragmentView.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recyclerview_chat)
        recyclerAdapter = BegeniRecyclerAdapter(activity, kullanicilar)

        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(mContext, RecyclerView.VERTICAL, false)
        recyclerView.adapter = recyclerAdapter



    }


    override fun onResume() {
        super.onResume()


            bilgileriGetir()

    }

    override fun onDestroy() {
        super.onDestroy()
        mRef.child("begenilerim").child(currentUser).removeEventListener(childListener)
    }
    private fun bilgileriGetir() {

        kullanicilar=ArrayList<Kitap>()
        childListener=  FirebaseDatabase.getInstance().getReference().child("begenilerim").child(currentUser).
        addChildEventListener(object :ChildEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }
            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                if(p0!!.getValue()!=null){
                    Log.e("yfysoftware","oku2 "+p0.childrenCount)
                    Log.e("yfysoftware", "userid " + p0.getValue())
                    var okunanUser = p0!!.getValue(Kitap::class.java)
                    // var userid = okunanUser!!.kitapuid
                    Log.e("yfysoftware", "userid " + p0.getValue())
                    kullanicilar.add(okunanUser!!)

                        setupRecyclerView()

                }


            }
            override fun onChildRemoved(p0: DataSnapshot) {
            }
        })


//        kullanicilar=ArrayList<Kitap>()
//        FirebaseDatabase.getInstance().getReference().child("begenilerim")
//                .addListenerForSingleValueEvent(object : ValueEventListener {
//                    override fun onCancelled(p0: DatabaseError) {
//
//                    }
//                    override fun onDataChange(p0: DataSnapshot) {
//                        if(p0!!.getValue()!=null){
//                            Log.e("yfysoftware","oku2 ")
//                            for(user in p0!!.children){
//                                var okunanUser = user!!.getValue(Kitap::class.java)
//                                var userid=okunanUser!!.kitapuid
//                                Log.e("yfysoftware","userid "+userid)
//                                kullanicilar.add(okunanUser)
//
//                            }
//                            setupRecyclerView()
//
//                        }
//
//
//
//                    }
//                })
    }
    private fun internetkontrol() {
        val cm = mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo

        isConnected = activeNetwork?.isConnected == true

    }
}