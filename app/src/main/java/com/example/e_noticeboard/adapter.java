package com.example.e_noticeboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_noticeboard.models.dbModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class adapter extends RecyclerView.Adapter<adapter.myviewholder> {
    ArrayList<dbModel> eventHolder;
    Context context;

    public adapter(ArrayList<dbModel> eventHolder, Context context) {
        this.eventHolder = eventHolder;
        this.context = context;
    }

    private onItemClickListener listener;

    public interface onItemClickListener {
        void onDelete(int position);

    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler, parent, false);

        return new myviewholder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {
        holder.textView1.setText(eventHolder.get(position).getTitle().toString());
        holder.textView2.setText(eventHolder.get(position).getDescription().toString());
        Picasso.with(context.getApplicationContext())
                .load("http://192.168.1.9/enoticeboard/uploadimage/" + eventHolder.get(position).getImageFileName())
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return eventHolder.size();
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        this.listener = listener;
    }

    public class myviewholder extends RecyclerView.ViewHolder {
        TextView textView1, textView2;
        ImageView imageView;


        public myviewholder(@NonNull View itemView, final onItemClickListener listener) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.heading);
            textView2 = itemView.findViewById(R.id.description);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }


}
