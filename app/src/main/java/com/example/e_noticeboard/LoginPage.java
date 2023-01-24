package com.example.e_noticeboard;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AlertDialogLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.e_noticeboard.models.responsemodel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginPage extends AppCompatActivity {
    String[] usertype = {"Principal", "User"};
    String[] departments = {"Electronics", "Computer Science", "Information Science"};
    TextInputEditText username, pwd;
    TextInputLayout textInputLayout;
    Button loginButton, resetButton;
    String name;
    AutoCompleteTextView userType, Departments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        textInputLayout = findViewById(R.id.dept);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        userType = findViewById(R.id.autoCompleteTextView);
        Departments = findViewById(R.id.autoCompleteTextView2);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item, usertype);
        userType.setAdapter(adapter);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, R.layout.list_item, departments);
        Departments.setAdapter(adapter2);

        userType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                name = adapter.getItem(position);
                if (name == usertype[0]) {
                    textInputLayout.setVisibility(View.GONE);
                }
            }
        });
        Departments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        username = (TextInputEditText) findViewById(R.id.edit1);
        pwd = (TextInputEditText) findViewById(R.id.edit2);
        loginButton = findViewById(R.id.button1);
        resetButton = findViewById(R.id.button2);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                String user=username.getEditableText().toString();
                String password=pwd.getEditableText().toString();
                Log.i("username..........",user);
                Log.i("pawd--------",password);
                if (username.getEditableText().length() > 0 && pwd.getEditableText().length() > 0)
                    {Call<responsemodel> call=user_apicontroller
                                .getinstance()
                                .getApi()
                                .verifyuser(username.getEditableText().toString(),pwd.getEditableText().toString());

                        call.enqueue(new Callback<responsemodel>() {
                                         @Override
                                         public void onResponse(Call<responsemodel> call, Response<responsemodel> response) {
                                             responsemodel obj = response.body();
                                             String output = obj.getMessage();
                                             Log.i("OUTPUT-----",output);
                                             if (output.equals("failed")) {
                                                 username.setText("");
                                                 pwd.setText("");
                                                 Toast.makeText(LoginPage.this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
                                             }
                                             if (output.equals("exist")) {
                                                 Toast.makeText(LoginPage.this, "WELCOME "+ user, Toast.LENGTH_SHORT).show();
                                                 Intent intent;
                                                 if (name.equals(usertype[0])) {
                                                     intent = new Intent(LoginPage.this, PrincipalActivity.class);
                                                 }
                                                 else {
                                                     intent = new Intent(LoginPage.this, NoticeDisplay.class);
                                                 }
                                                 startActivity(intent);

                                             }

                                         }
                                         @Override
                                         public void onFailure(Call<responsemodel> call, Throwable t) {

                                         }
                        });
                    }

                else {
                    String toastMessage = "Username or Password are not populated";
                    Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_SHORT).show();
                }
            }
        });
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username.setText("");
                pwd.setText("");
                userType.clearListSelection();
            }
        });
    }
}