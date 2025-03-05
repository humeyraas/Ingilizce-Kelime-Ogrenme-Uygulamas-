package com.example.deneme1;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ArticleActivity extends AppCompatActivity {

    private TextView txtTitle, txtSummary;
    private Button btnNewArticle, btnFavorite;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);


        txtTitle = findViewById(R.id.txtTitle);
        txtSummary = findViewById(R.id.txtSummary);
        btnNewArticle = findViewById(R.id.btnNewArticle);
        btnFavorite = findViewById(R.id.btnFavorite);


        fetchRandomArticle();


        btnNewArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchRandomArticle();
            }
        });
    }

    private void fetchRandomArticle() {
        executorService.execute(() -> {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://en.wikipedia.org/api/rest_v1/page/random/summary")
                    .build();
            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful() && response.body() != null) {
                    String responseBody = response.body().string();
                    JSONObject jsonObject = new JSONObject(responseBody);
                    String title = jsonObject.getString("title");
                    String extract = jsonObject.getString("extract");

                    runOnUiThread(() -> {
                        txtTitle.setText(title);
                        txtSummary.setText(extract);
                    });
                }
            } catch (IOException | org.json.JSONException e) {
                e.printStackTrace();
                runOnUiThread(() ->
                        Toast.makeText(ArticleActivity.this, "Makale yüklenemedi", Toast.LENGTH_SHORT).show());
            }
        });
    }
}
