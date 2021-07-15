package com.example.census_user;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    ArrayList<Userqueries> mlist;
    Context context;
    public MyAdapter(Context context, ArrayList<Userqueries> mlist){
        this.mlist=mlist;
        this.context=context;
    }
    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return new MyViewHolder(v);
    }
    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {
        Userqueries model= mlist.get(position);
        holder.Title.setText(model.getTitle());
        holder.Query.setText(model.getQuery());
        holder.status.setText(model.getStatus());
        String sts=model.getStatus();
        if(sts.equals("solved"))
        {
               holder.status.setTextColor(Color.parseColor("#006400"));
        }
        holder.status.setText(model.getStatus());
        holder.Feedback.setText(model.getFeedback());
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView Title,Query,status,Feedback;

        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            Title=itemView.findViewById(R.id.titleofquery);
            Query=itemView.findViewById(R.id.querytext);
            status=itemView.findViewById(R.id.query_status);
            Feedback=itemView.findViewById(R.id.query_feedback);
        }
    }
}