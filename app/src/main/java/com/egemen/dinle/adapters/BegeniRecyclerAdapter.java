package com.egemen.dinle.adapters;


import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.egemen.dinle.models.Kitap;

import com.egemen.dinle.R;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class BegeniRecyclerAdapter extends RecyclerView.Adapter<BegeniRecyclerAdapter.MyViewHolder>  {
    DatabaseReference mRef;

    ArrayList<Kitap> mDataList;
    LayoutInflater inflater;
    Context mcontext;

    public BegeniRecyclerAdapter(Context context, ArrayList<Kitap> data ){

        inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater=LayoutInflater.from(context);
        mcontext = context;
        this.mDataList=data;


    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v;

        v=inflater.inflate(R.layout.kitap_recycler_view, parent,false);

        MyViewHolder holder=new MyViewHolder(v);
        return holder;





    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Kitap veri=mDataList.get(position);
        mRef= FirebaseDatabase.getInstance().getReference();
        holder.setData(veri, position);


    }




    @Override
    public int getItemCount() {
        return mDataList.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView kitapAdi,kitapAciklama;

        public MyViewHolder(View itemView ) {
            super(itemView);
             kitapAdi = itemView.findViewById(R.id.kitapAdi);
            kitapAciklama = itemView.findViewById(R.id.kitapAciklama);
        }


        public void setData(final Kitap kitap, int position) {

            kitapAdi.setText(kitap.getKitapAdi());
            kitapAciklama.setText(kitap.getKitapBilgisi());
            }





        }


    }

