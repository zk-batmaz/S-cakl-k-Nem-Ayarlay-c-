package com.qbra.micromobil.view;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qbra.micromobil.R;
import com.qbra.micromobil.model.Feeds;
import com.qbra.micromobil.model.TemperatureHumidityResponse;
import com.qbra.micromobil.service.TempAndHumAPI;
import com.qbra.micromobil.model.PostResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    ArrayList<Feeds> feeds;
    private String BASE_URL = "https://api.thingspeak.com/channels/";
    private String POST_URL = "https://api.thingspeak.com/";
    Retrofit retrofit, postRetrofit;
    Handler handler;
    Runnable runnable;
    int delay = 3000;

    TextView tempText;
    TextView humText;
    TextView tempText2;
    TextView humText2;
    EditText tempInput;
    EditText humInput;
    Button sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tempText = findViewById(R.id.tempTextView);
        humText = findViewById(R.id.humTextView);
        tempText2 = findViewById(R.id.tempTextView2);
        humText2 = findViewById(R.id.humTextView2);
        tempInput = findViewById(R.id.tempInputValue);
        humInput = findViewById(R.id.humInputValue);
        sendButton = findViewById(R.id.sendButton);

        Gson gson = new GsonBuilder().setLenient().create();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        postRetrofit = new Retrofit.Builder()
                .baseUrl(POST_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                loadData();
                handler.postDelayed(this, delay);
            }
        };

        handler.post(runnable); // İlk çağrı

        sendButton.setOnClickListener(v -> {
            String tempValueStr = tempInput.getText().toString();
            String humValueStr = humInput.getText().toString();

            if (!TextUtils.isEmpty(tempValueStr) && !TextUtils.isEmpty(humValueStr)) {
                int tempValue = Integer.parseInt(tempValueStr);
                int humValue = Integer.parseInt(humValueStr);
                if ((tempValue >= 0 && tempValue <= 40) && (humValue >= 20 && humValue <= 100)) {
                    if (humValue % 5 == 0) {
                        sendData(tempValue, humValue);
                    } else {
                        Toast.makeText(MainActivity.this, "Lütfen nem değerini 5'in katı olacak şekilde girin.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Lütfen sıcaklık için 0-40, nem için 20-100 arasında bir değer girin.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MainActivity.this, "Lütfen sıcaklık ve nem değerlerini girin.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadData() {
        TempAndHumAPI tempAndHumAPI = retrofit.create(TempAndHumAPI.class);
        Call<TemperatureHumidityResponse> call = tempAndHumAPI.getData();
        call.enqueue(new Callback<TemperatureHumidityResponse>() {
            @Override
            public void onResponse(Call<TemperatureHumidityResponse> call, Response<TemperatureHumidityResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    feeds = new ArrayList<>(response.body().getFeeds());

                    if (!feeds.isEmpty()) {
                        Feeds latestFeed = feeds.get(feeds.size() - 1);
                        tempText.setText("İç Sıcaklık: " + latestFeed.getSicaklik());
                        humText.setText("İç Nem: " + latestFeed.getNem());
                        tempText2.setText("Dış Sıcaklık: " + latestFeed.getSicaklik2());
                        humText2.setText("Dış Nem: " + latestFeed.getNem2());
                    }
                } else {
                    System.out.println("Response not successful or body is null");
                }
            }

            @Override
            public void onFailure(Call<TemperatureHumidityResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void sendData(int tempValue, int humValue) {
        TempAndHumAPI tempAndHumAPI = postRetrofit.create(TempAndHumAPI.class);
        Call<PostResponse> call = tempAndHumAPI.postData("6D6FLZ3OXI1WDW4H", tempValue, humValue);
        call.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(MainActivity.this, "Değer gönderildi: " + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Gönderim başarısız.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(MainActivity.this, "Değer Gönderildi", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable); // Aktivite yok edilirken runnable'ı durdur
    }
}