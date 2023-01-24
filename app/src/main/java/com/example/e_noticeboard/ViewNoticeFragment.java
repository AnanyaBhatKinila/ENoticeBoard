package com.example.e_noticeboard;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.e_noticeboard.models.dbModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ViewNoticeFragment extends Fragment {
    TextView textView1, textView2;
    ImageView img;
    RecyclerView recyclerView;
    adapter adapter;
    ArrayList<dbModel> arrayList;
    Handler handler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

      View view=inflater.inflate(R.layout.fragment_view_notice, container, false);
        recyclerView = view.findViewById(R.id.recyclerFragment);
        arrayList = new ArrayList<>();
        adapter = new adapter(arrayList, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        textView1 = view.findViewById(R.id.heading);
        textView2 = view.findViewById(R.id.description);

        img = view.findViewById(R.id.imageView);
        Call<List<dbModel>> call = api_controller.getInstance().getapi().caller();
        call.enqueue(new Callback<List<dbModel>>() {
            @Override
            public void onResponse(Call<List<dbModel>> call, Response<List<dbModel>> response) {
                List<dbModel> obj = response.body();
                Log.i("title---", response.body().get(1).getTitle().toString());

                for (int i = 0; i < obj.size(); i++) {
                    dbModel ob = response.body().get(i);
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
      return view;
    }
}