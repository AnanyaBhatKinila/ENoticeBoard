package com.example.e_noticeboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.e_noticeboard.models.dbModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NoticeDisplay extends AppCompatActivity {
    TextView textView1, textView2;
    ImageView img;
    RecyclerView recyclerView;
    adapter adapter;
    ArrayList<dbModel> arrayList;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_display);

        recyclerView = findViewById(R.id.recyclerView);
        arrayList = new ArrayList<>();
        adapter = new adapter(arrayList, NoticeDisplay.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(NoticeDisplay.this));
        recyclerView.setAdapter(adapter);
        textView1 = findViewById(R.id.heading);
        textView2 = findViewById(R.id.description);

        img = findViewById(R.id.imageView);
        Call<List<dbModel>> call = api_controller.getInstance().getapi().caller();
        call.enqueue(new Callback<List<dbModel>>() {
            @Override
            public void onResponse(Call<List<dbModel>> call, Response<List<dbModel>> response) {
                List<dbModel> obj = response.body();
                Log.i("title---", response.body().get(1).getTitle().toString());

                for (int i = 0; i < obj.size(); i++) {
                    dbModel ob = response.body().get(i);
                    //textView1.setText(obj.get(i).getTitle().trim());
                    //Log.i("title---",obj.get(i).getTitle());
                    // textView2.setText(obj.get(i).getDescription().trim());
                    // Picasso.with(NoticeDisplay.this)
                    //    .load("http://192.168.1.43/enoticeboard/uploadimage/"+ obj.get(i).getImageFileName())
                    //   .into(img);
                    arrayList.add(ob);
                }

                adapter.notifyDataSetChanged();}

            @Override
            public void onFailure(Call<List<dbModel>> call, Throwable t) {
                Log.i("error---", t.getMessage());

            }
        });
        handler = new Handler();
        Runnable runnable = new Runnable() {
            int count = 0;

            @Override
            public void run() {
                if (count < arrayList.size()) {
                    recyclerView.scrollToPosition(count++);
                    handler.postDelayed(this, 3000);
                    if (count == arrayList.size()) {
                        count = 0;
                    }
                }
            }
        };
        handler.postDelayed(runnable, 3000);
    }
}