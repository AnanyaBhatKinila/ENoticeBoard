package com.example.e_noticeboard;

import static java.security.AccessController.getContext;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Path;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.e_noticeboard.models.dbModel;

import net.gotev.uploadservice.MultipartUploadRequest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Multipart;

@RequiresApi(api = Build.VERSION_CODES.O)
public class NoticeActivity extends AppCompatActivity {

    EditText editText1, editText2;
    ImageView img;
    Button button1, button2;
    Uri selectedImageUri;
    String fullFilePath, filename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        editText1 = findViewById(R.id.editView1);
        editText2 = findViewById(R.id.editView2);
        img = findViewById(R.id.imageView);
        button1 = findViewById(R.id.chooseButton);
        button2 = findViewById(R.id.submitButton);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageChoice();
            }

        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPath(selectedImageUri);
                Log.i("filepath....", fullFilePath);
                String uploadId = UUID.randomUUID().toString();
                String title = editText1.getText().toString().trim();
                String description = editText2.getText().toString().trim();
                Call<dbModel> call = api_controller.getInstance().getapi().call(title, description, getFilename(selectedImageUri, getApplicationContext()));
                call.enqueue(new Callback<dbModel>() {
                    @Override
                    public void onResponse(Call<dbModel> call, Response<dbModel> response) {
                        try {

                            try {
                                new MultipartUploadRequest(NoticeActivity.this, uploadId, "http://192.168.1.9/enoticeboard/upload.php?add=333")
                                        .setMethod("POST")
                                        .addFileToUpload(fullFilePath, "upload")
                                        .setMaxRetries(3)
                                        .startUpload();
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Intent intent=new Intent(getApplicationContext(),NoticeDisplay.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<dbModel> call, Throwable t) {

                    }
                });
            }
        });
    }

    void imageChoice() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        launchSomeActivity.launch(intent);
    }

    ActivityResultLauncher<Intent> launchSomeActivity
            = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode()
                        == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null
                            && data.getData() != null) {
                        selectedImageUri = data.getData();
                        Bitmap selectedImageBitmap;
                        try {
                            selectedImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                            img.setImageBitmap(selectedImageBitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
            });

    @SuppressLint("Range")
    private void getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media._ID + "=?", new String[]{document_id}, null);
        if (cursor != null) {
            cursor.moveToFirst();
            fullFilePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            cursor.close();

        }
    }

    @SuppressLint("Range")
    String getFilename(Uri uri, Context context) {
        String res = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {

                    res=cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }
            finally {
                cursor.close();
            }

            if(res==null)
            {
                res=uri.getPath();
                int cut=res.lastIndexOf('/');
                if(cut!=-1)
                {
                    res=res.substring(cut + 1);
                }
            }

        }
        return res;
    }

}
