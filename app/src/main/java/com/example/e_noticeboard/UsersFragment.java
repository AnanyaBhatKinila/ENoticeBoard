package com.example.e_noticeboard;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e_noticeboard.models.UserModel;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UsersFragment extends Fragment {
    RecyclerView recyclerView;
ArrayList<UserModel> arrayList;
TextInputEditText textInputEditText1,textInputEditText2,textInputEditText3,textInputEditText4;
    recyclerUserAdapter recyclerUserAdapter;
    FloatingActionButton add;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_users, container, false);
        recyclerView=view.findViewById(R.id.recyclerUser);
        arrayList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerUserAdapter=new recyclerUserAdapter(arrayList,getContext());
        recyclerView.setAdapter(recyclerUserAdapter);
        add=view.findViewById(R.id.addfab);
        Call<List<UserModel>> call=user_apicontroller.getinstance().getApi().caller();
        call.enqueue((new Callback<List<UserModel>>() {
            @Override
            public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
                List<UserModel> list1=response.body();
                Log.i("Response---------",list1.toString());
                for(int i=0;i<list1.size();i++)
                {
                    UserModel obj=new UserModel(list1.get(i).getName(),list1.get(i).getUsername(),list1.get(i).getPassword(),list1.get(i).getEmail());
                    arrayList.add(obj);
                    recyclerUserAdapter.notifyDataSetChanged();
                    Log.i("LOG-----",list1.get(i).getEmail());
                }
                Log.i("log",arrayList.toString());
            }

            @Override
            public void onFailure(Call<List<UserModel>> call, Throwable t) {
                Log.i("error message-----",t.getMessage());
            }
        }));

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog=new Dialog(getContext());
                dialog.setContentView(R.layout.customdialog);
                textInputEditText1=dialog.findViewById(R.id.dialogName);
                textInputEditText2=dialog.findViewById(R.id.dialogEmailID);
                textInputEditText3=dialog.findViewById(R.id.dialogUsername);
                textInputEditText4=dialog.findViewById(R.id.autoPassword);
                Button save=dialog.findViewById(R.id.save_button);
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name="",email="",username="",password="";
                        if(!textInputEditText1.getEditableText().toString().equals("")) {
                             name = textInputEditText1.getEditableText().toString();
                            Log.i("name-------",name);
                        }
                        else
                        {
                            Toast.makeText(getContext(), "Please enter Name", Toast.LENGTH_SHORT).show();
                        }
                        if(!textInputEditText2.getEditableText().toString().equals("")) {
                            email = textInputEditText2.getEditableText().toString();
                            Log.i("email-------",email);
                        }
                        else
                        {
                            Toast.makeText(getContext(), "Please enter valid Email ID", Toast.LENGTH_SHORT).show();
                        }
                        if(!textInputEditText3.getEditableText().toString().equals("")){
                        username=textInputEditText3.getEditableText().toString();}
                        else
                        {
                            Toast.makeText(getContext(), "Please enter Username", Toast.LENGTH_SHORT).show();
                        }
                        if(!textInputEditText4.getEditableText().toString().equals("")){
                         password=textInputEditText4.getEditableText().toString();}
                        else
                        {
                            Toast.makeText(getContext(), "Please enter valid password", Toast.LENGTH_SHORT).show();
                        }
                       // arrayList.add(new UserModel(name,username,password,email));
                      //  UserModel obj=new UserModel(name,username,password,email);
                        Call<UserModel> call= user_apicontroller.getinstance().getApi().call(name,username,password,email);
                        call.enqueue((new Callback<UserModel>() {
                            @Override
                            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                                Toast.makeText(getContext(), "User Entered", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<UserModel> call, Throwable t) {

                                Log.i("ERROR----------",t.getMessage());
                            }
                        }));
                        recyclerUserAdapter.notifyDataSetChanged();
                        recyclerView.scrollToPosition(arrayList.size()-1);
                        dialog.dismiss();
                    }
                });
dialog.show();
            }
        });
        return view;
    }
}