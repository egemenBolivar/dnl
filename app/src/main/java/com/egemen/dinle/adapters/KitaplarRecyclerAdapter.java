package com.egemen.dinle.adapters;

import android.app.Dialog;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import com.egemen.dinle.enums.def;
import com.egemen.dinle.R;
import com.egemen.dinle.UniversalImageLoader;
import com.egemen.dinle.models.Kitap;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.egemen.dinle.utils.shared;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class KitaplarRecyclerAdapter extends RecyclerView.Adapter<KitaplarRecyclerAdapter.MyViewHolder>{
    DatabaseReference mRef;
    ArrayList<Kitap> mDataList;
    LayoutInflater inflater;
    Context mcontext;


    public KitaplarRecyclerAdapter(Context context, ArrayList<Kitap> data){

        inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater=LayoutInflater.from(context);
        mcontext = context;
        this.mDataList=data;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View v=inflater.inflate(R.layout.kitaplar_recycler_view, parent,false);
        MyViewHolder holder=new MyViewHolder(v);
        return holder;

    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Kitap veri=mDataList.get(position);
        mRef= FirebaseDatabase.getInstance().getReference();
        holder.setData(veri, position);
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shared.INSTANCE.pStr(mcontext, def.egemen_userid,veri.getKitapuid());
                shared.INSTANCE.pStr(mcontext, def.egemen_userimage,veri.getKitapResim());
                shared.INSTANCE.pStr(mcontext, def.egemen_useradsoyad,veri.getKitapAdi());
            }
        });


    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView kitapResim , bookMark ;
        TextView kitapAdi,kitapBilgi;
        RatingBar kitapPuan;
        LinearLayout parentLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            parentLayout = itemView.findViewById(R.id.user_search_recycler_layout);
            kitapAdi=itemView.findViewById(R.id.kitapAdi);
            kitapBilgi=itemView.findViewById(R.id.kitapBilgi);
            kitapPuan = itemView.findViewById(R.id.kitapPuani);
            kitapResim = itemView.findViewById(R.id.kitapResmi);
            bookMark = itemView.findViewById(R.id.bookMarkButton);

        }

        public void setData(final Kitap post, int position) {
            kitapAdi.setText(post.getKitapAdi());
            kitapBilgi.setText(post.getKitapBilgisi());

           if(!post.getKitapResim().equals("") && !post.getKitapResim().equals("null")){
               UniversalImageLoader.Companion.setImage(post.getKitapResim(),kitapResim,null,"");
           }
           else {
               kitapResim.setImageResource(R.drawable.ic_broken_image_black_24dp);
           }

            kitapPuan.setRating(post.getKitapPuani());
            kitapPuan.setClickable(false);
            kitapPuan.setActivated(false);
            begeniKontrol(post);
           final MediaPlayer mediaPlayer = new MediaPlayer();
            kitapResim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final Dialog d = new Dialog(mcontext);
                    d.setContentView(R.layout.player_dialog);
                    d.setCancelable(false);
                    d.show();

                        final ImageView playPause = d.findViewById(R.id.ic_playpause);
                        ImageView cancel = d.findViewById(R.id.cancel_action);
                        playPause.setImageResource(R.drawable.ic_stop_black_24dp);

                   try  {
                       mediaPlayer.setDataSource(mcontext, Uri.parse(post.getKitapSes()));
                       mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                       mediaPlayer.prepare();

                   }
                   catch (Exception e) { e.printStackTrace(); }
                    playPause.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {






                                if(mediaPlayer.isPlaying()) {
                                    mediaPlayer.pause();
                                    playPause.setImageResource(R.drawable.ic_pause_circle_filled_black_24dp);
                                }else {
                                    playPause.setImageResource(R.drawable.ic_play_circle_filled_black_24dp);
                                    mediaPlayer.start();
                                }


                        }
                    });
                   cancel.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View view) {
                           d.dismiss();
                           mediaPlayer.stop();

                       }
                   });


                }
            });

            bookMark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final DatabaseReference mRef= FirebaseDatabase.getInstance().getReference();
                    final String userid=FirebaseAuth.getInstance().getCurrentUser().getUid();
                    mRef.child("likes").child(post.getKitapuid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot p0) {

                            if(p0.hasChild(userid)){
                                mRef.child("likes").child(post.getKitapuid()).child(userid).removeValue();
                                mRef.child("begenilerim").child(userid).child(post.getKitapuid()).removeValue();
                                bookMark.setImageResource(R.drawable.ic_bookmark_border_black_24dp);
                            }
                            else {
                                mRef.child("likes").child(post.getKitapuid()).child(userid).setValue(userid);
                                mRef.child("begenilerim").child(userid).child(post.getKitapuid()).setValue(post);
                                bookMark.setImageResource(R.drawable.ic_bookmark_black_24dp);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });


                }
            });








        }


        private void begeniKontrol(Kitap post){
            final String userid= FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference mRef=FirebaseDatabase.getInstance().getReference();
            mRef.child("likes").child(post.getKitapuid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot p0) {


                    if(p0.hasChild(userid)){
                        bookMark.setImageResource(R.drawable.ic_bookmark_black_24dp);
                    }
                    else{
                        bookMark.setImageResource(R.drawable.ic_bookmark_border_black_24dp);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });

        }


    }



}

