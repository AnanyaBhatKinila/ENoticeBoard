package com.example.e_noticeboard;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_noticeboard.models.UserModel;
import com.example.e_noticeboard.models.dbModel;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class recyclerUserAdapter extends RecyclerView.Adapter<recyclerUserAdapter.myviewholder> {
    ArrayList<UserModel> eventHolder;

    Context context;

    public recyclerUserAdapter(ArrayList<UserModel> eventHolder, Context context) {
        this.eventHolder = eventHolder;
        this.context = context;
    }

    private adapter.onItemClickListener listener;

    public interface onItemClickListener {
        void onDelete(int position);

    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_layout, parent, false);
        return new recyclerUserAdapter.myviewholder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {
        holder.textView1.setText(eventHolder.get(position).getName());
        holder.textView2.setText(eventHolder.get(position).getUsername());
        holder.textView3.setText(eventHolder.get(position).getEmail());
        holder.row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.customdialog);
                TextInputEditText textInputEditText1 = dialog.findViewById(R.id.dialogName);
                TextInputEditText textInputEditText2 = dialog.findViewById(R.id.dialogEmailID);
                TextInputEditText textInputEditText3 = dialog.findViewById(R.id.dialogUsername);
                TextInputEditText textInputEditText4 = dialog.findViewById(R.id.autoPassword);
                Button save = dialog.findViewById(R.id.save_button);
                TextView textView = dialog.findViewById(R.id.UserAction);
                textView.setText("UPDATE USER");
                textInputEditText1.setText(eventHolder.get(holder.getAdapterPosition()).getName());
              //  textInputEditText2.setText(eventHolder.get(holder.getAdapterPosition()).getEmail());
               // textInputEditText3.setText(eventHolder.get(holder.getAdapterPosition()).getUsername());
              //  textInputEditText1.setText(eventHolder.get(holder.getAdapterPosition()).getPassword());
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = "", email = "", username = "", password = "";
                        if (!textInputEditText1.getEditableText().toString().equals("")) {
                            name = textInputEditText1.getEditableText().toString();
                            Log.i("name-------", name);
                        } else {
                            Toast.makeText(context, "Please enter Name", Toast.LENGTH_SHORT).show();
                        }
                        if (!textInputEditText2.getEditableText().toString().equals("")) {
                            email = textInputEditText2.getEditableText().toString();
                            Log.i("email-------", email);
                        } else {
                            Toast.makeText(context, "Please enter valid Email ID", Toast.LENGTH_SHORT).show();
                        }
                        if (!textInputEditText3.getEditableText().toString().equals("")) {
                            username = textInputEditText3.getEditableText().toString();
                        } else {
                            Toast.makeText(context, "Please enter Username", Toast.LENGTH_SHORT).show();
                        }
                        if (!textInputEditText4.getEditableText().toString().equals("")) {
                            password = textInputEditText4.getEditableText().toString();
                        } else {
                            Toast.makeText(context, "Please enter valid password", Toast.LENGTH_SHORT).show();
                        }
                       // eventHolder.set(holder.getAdapterPosition(), new UserModel(name, username, password, email));
                        Call<UserModel> call= user_apicontroller.getinstance().getApi().update(name,username,password,email);
                        call.enqueue((new Callback<UserModel>() {
                            @Override
                            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                                Toast.makeText(context, "User details updated successfully", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<UserModel> call, Throwable t) {

                                Log.i("ERROR----------",t.getMessage());
                            }
                        }));
                        notifyItemChanged(holder.getAdapterPosition());

                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        holder.row.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context)
                        .setTitle("DELETE?")
                        .setMessage("Are you sure you want to delete user?")
                        .setIcon(R.drawable.ic_baseline_delete_24)
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                int position=holder.getAdapterPosition();
                                Call<UserModel> call = user_apicontroller.getinstance().getApi().delete(eventHolder.get(position).getName());
                           call.enqueue(new Callback<UserModel>() {
                               @Override
                               public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                                   Toast.makeText(context, "USER REMOVED", Toast.LENGTH_SHORT).show();
                                   eventHolder.remove(holder.getAdapterPosition());
                                   notifyItemRemoved(holder.getAdapterPosition());
                               }

                               @Override
                               public void onFailure(Call<UserModel> call, Throwable t) {

                                   Log.i("ERROR----------",t.getMessage());
                               }
                           });
                              //  eventHolder.remove(holder.getAdapterPosition());
                          //  notifyItemRemoved(holder.getAdapterPosition());

                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                builder.show();
                return true;
            }
        });

    }

    public void setOnItemClickListener(adapter.onItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return eventHolder.size();
    }

    public class myviewholder extends RecyclerView.ViewHolder {

        TextView textView1, textView2, textView3;
        LinearLayout row;

        public myviewholder(@NonNull View itemView, adapter.onItemClickListener listener) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.nametext);
            textView2 = itemView.findViewById(R.id.usernametext);
            textView3 = itemView.findViewById(R.id.emailidtext);
            row = itemView.findViewById(R.id.llrow);
        }
    }
}
