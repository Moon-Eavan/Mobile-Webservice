package com.example.photoviewer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    TextView textView;

    FetchPostsTask taskDownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        textView = findViewById(R.id.textView);
    }

    public void onClickDownload(View v) {
        if (taskDownload != null && taskDownload.getStatus() == AsyncTask.Status.RUNNING) {
            taskDownload.cancel(true);
        }

        taskDownload = new FetchPostsTask();
        taskDownload.execute(ApiConfig.BASE_URL + "/api_root/Post/");
        Toast.makeText(getApplicationContext(), "Download", Toast.LENGTH_LONG).show();
    }

    public void onClickUpload(View v) {
        // Phase 7에서 Upload UI(상 난이도) 구현 예정
        Toast.makeText(getApplicationContext(), "Upload", Toast.LENGTH_LONG).show();
    }

    private static class FetchResult {
        boolean success;
        List<Bitmap> images;
        String errorMessage;
    }

    private class FetchPostsTask extends AsyncTask<String, Void, FetchResult> {
        @Override
        protected FetchResult doInBackground(String... urls) {
            FetchResult result = new FetchResult();
            HttpURLConnection conn = null;

            try {
                String apiUrl = urls[0];
                URL urlAPI = new URL(apiUrl);
                conn = (HttpURLConnection) urlAPI.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream is = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    StringBuilder resultText = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        resultText.append(line);
                    }
                    is.close();

                    String strJson = resultText.toString();
                    JSONArray aryJson = new JSONArray(strJson);

                    List<Bitmap> bitmapList = new ArrayList<>();

                    for (int i = 0; i < aryJson.length(); i++) {
                        JSONObject post = aryJson.getJSONObject(i);
                        String author = post.optString("author");
                        String title = post.optString("title");
                        String text = post.optString("text");
                        String createdDate = post.optString("created_date");
                        String publishedDate = post.optString("published_date");
                        String imageUrl = post.optString("image");

                        Log.d(TAG, "post[" + i + "] author=" + author
                                + ", title=" + title
                                + ", text=" + text
                                + ", created_date=" + createdDate
                                + ", published_date=" + publishedDate
                                + ", image=" + imageUrl);

                        // 게시물 1건의 이미지 다운로드가 실패해도 나머지 목록 처리는 계속한다.
                        if (imageUrl != null && !imageUrl.isEmpty()) {
                            HttpURLConnection imgConn = null;
                            try {
                                URL myImageUrl = new URL(imageUrl);
                                imgConn = (HttpURLConnection) myImageUrl.openConnection();
                                imgConn.setConnectTimeout(5000);
                                imgConn.setReadTimeout(5000);

                                InputStream imgStream = imgConn.getInputStream();
                                Bitmap imageBitmap = BitmapFactory.decodeStream(imgStream);
                                imgStream.close();

                                if (imageBitmap != null) {
                                    bitmapList.add(imageBitmap); // 이미지 리스트에 추가
                                } else {
                                    Log.e(TAG, "이미지 디코딩 실패: " + imageUrl);
                                }
                            } catch (IOException e) {
                                Log.e(TAG, "이미지 다운로드 실패: " + imageUrl, e);
                            } finally {
                                if (imgConn != null) {
                                    imgConn.disconnect();
                                }
                            }
                        }
                    }

                    result.success = true;
                    result.images = bitmapList;
                } else {
                    result.success = false;
                    result.errorMessage = "HTTP " + responseCode;
                    Log.e(TAG, "GET 실패: HTTP " + responseCode);
                }
            } catch (IOException | JSONException e) {
                result.success = false;
                result.errorMessage = e.getMessage();
                Log.e(TAG, "게시물 목록을 불러오는 중 오류 발생", e);
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }

            return result;
        }

        @Override
        protected void onPostExecute(FetchResult result) {
            if (result.success) {
                List<Bitmap> images = result.images;
                if (images == null || images.isEmpty()) {
                    textView.setText("불러올 이미지가 없습니다.");
                } else {
                    textView.setText("이미지 로드 성공! (" + images.size() + "개)");
                    RecyclerView recyclerView = findViewById(R.id.recyclerView);
                    ImageAdapter adapter = new ImageAdapter(images);
                    recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    recyclerView.setAdapter(adapter);
                }
            } else {
                textView.setText("불러오기 실패: " + result.errorMessage);
            }
        }
    }
}
