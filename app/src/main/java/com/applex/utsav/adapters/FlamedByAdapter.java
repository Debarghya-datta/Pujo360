package com.applex.utsav.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.applex.utsav.ActivityProfile;
import com.applex.utsav.ActivityProfileCommittee;
import com.applex.utsav.ActivityProfileUser;
import com.applex.utsav.R;
import com.applex.utsav.models.FlamedModel;
import com.applex.utsav.utility.BasicUtility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FlamedByAdapter extends RecyclerView.Adapter<FlamedByAdapter.ProgrammingViewHolder> {
    private ArrayList<FlamedModel> itemDatalist;
    private Context mContext;


    public FlamedByAdapter(Context context, ArrayList<FlamedModel> itemDatalist) {
        this.mContext = context;
        this.itemDatalist = itemDatalist;
    }

    @NonNull
    @Override
    public ProgrammingViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View v = layoutInflater.inflate(R.layout.item_bottomsheet_flamed, viewGroup, false);
        return new ProgrammingViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProgrammingViewHolder holder, int position) {
        FlamedModel currentItem = itemDatalist.get(position);

        String timeAgo = BasicUtility.getTimeAgo(currentItem.getTs());
        holder.time.setText(timeAgo);
        if(timeAgo != null) {
            if(timeAgo.matches("just now")) {
                holder.time.setTextColor(Color.parseColor("#00C853"));
            }
        }
//        holder.PName.setText(currentItem.getFirstname()+" "+currentItem.getLastname());
        holder.PUsername.setText(currentItem.getUsername());
//        holder.PDescription.setText(currentItem.getDesc());

        String userimage_url = currentItem.getUserdp();
        if(userimage_url!=null){
            Picasso.get().load(userimage_url).placeholder(R.drawable.ic_account_circle_black_24dp).into(holder.userimage);
        }
        else{
            if(currentItem.getGender()!=null){
                if (currentItem.getGender().matches("Female") || currentItem.getGender().matches("মহিলা")){
                    holder.userimage.setImageResource(R.drawable.ic_female);
                }
                else if (currentItem.getGender().matches("Male") || currentItem.getGender().matches("পুরুষ")){
                    holder.userimage.setImageResource(R.drawable.ic_male);
                }
                else if (currentItem.getGender().matches("Others") || currentItem.getGender().matches("অন্যান্য")){
                    holder.userimage.setImageResource(R.drawable.ic_account_circle_black_24dp);
                }
            }
            else {
                holder.userimage.setImageResource(R.drawable.ic_account_circle_black_24dp);
            }
        }

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(currentItem.getType().matches("com")) {
//                    Intent intent = new Intent(mContext, ActivityProfileCommittee.class);
//                    intent.putExtra("uid", currentItem.getUid());
//                    mContext.startActivity(intent);
//                }
//                else {
//                    Intent intent = new Intent(mContext, ActivityProfileUser.class);
//                    intent.putExtra("uid", currentItem.getUid());
//                    mContext.startActivity(intent);
//                }
                Intent intent = new Intent(mContext, ActivityProfile.class);
                intent.putExtra("uid", currentItem.getUid());
                mContext.startActivity(intent);
            }
        });
        holder.PUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(currentItem.getType().matches("com")) {
//                    Intent intent = new Intent(mContext, ActivityProfileCommittee.class);
//                    intent.putExtra("uid", currentItem.getUid());
//                    mContext.startActivity(intent);
//                }
//                else {
//                    Intent intent = new Intent(mContext, ActivityProfileUser.class);
//                    intent.putExtra("uid", currentItem.getUid());
//                    mContext.startActivity(intent);
//                }
                Intent intent = new Intent(mContext, ActivityProfile.class);
                intent.putExtra("uid", currentItem.getUid());
                mContext.startActivity(intent);
            }
        });
        holder.userimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(currentItem.getType().matches("com")) {
//                    Intent intent = new Intent(mContext, ActivityProfileCommittee.class);
//                    intent.putExtra("uid", currentItem.getUid());
//                    mContext.startActivity(intent);
//                }
//                else {
//                    Intent intent = new Intent(mContext, ActivityProfileUser.class);
//                    intent.putExtra("uid", currentItem.getUid());
//                    mContext.startActivity(intent);
//                }
                Intent intent = new Intent(mContext, ActivityProfile.class);
                intent.putExtra("uid", currentItem.getUid());
                mContext.startActivity(intent);
            }
        });


    }



    @Override
    public int getItemCount() {
        return itemDatalist.size();
    }


    public static class ProgrammingViewHolder extends RecyclerView.ViewHolder{

        TextView PUsername,PInstitute,time;
        ImageView userimage;
        LinearLayout card;
        RecyclerView recyclerView;

        ProgrammingViewHolder(@NonNull View itemView) {
            super(itemView);
//            PName = itemView.findViewById(R.id.Profilename);
            PUsername = itemView.findViewById(R.id.Lusername);
//            PDescription = itemView.findViewById(R.id.Pdescription);
//            PInstitute = itemView.findViewById(R.id.Linstitute);
//            PDetaileddesc = itemView.findViewById(R.id.username);
            time = itemView.findViewById(R.id.time);
            userimage = itemView.findViewById(R.id.Ldp);
            recyclerView = itemView.findViewById(R.id.flamed_recycler);
            card = itemView.findViewById(R.id.profileCard1);
        }
    }

}
