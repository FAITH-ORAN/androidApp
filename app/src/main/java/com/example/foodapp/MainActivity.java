package com.example.foodapp;

import android.Manifest;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.Models.FoodItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private RecyclerView recyclerView;
    private FoodAdapter foodAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_main);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        foodAdapter = new FoodAdapter();
        recyclerView.setAdapter(foodAdapter);

        if (isNetworkAvailable()) {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://the-mexican-food-db.p.rapidapi.com/")
                    .get()
                    .addHeader("X-RapidAPI-Key", "df80df2dfcmshf88176e2bffb0cep12e07ajsneff1218044ca")
                    .addHeader("X-RapidAPI-Host", "the-mexican-food-db.p.rapidapi.com")
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    runOnUiThread(() -> Toast.makeText(MainActivity.this, "Erreur lors de la récupération des données", Toast.LENGTH_SHORT).show());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String responseData = response.body().string();
                        Log.d(TAG, "Réponse de l'API : " + responseData);

                        List<FoodItem> foodItems = parseFoodItems(responseData);

                        runOnUiThread(() -> {
                            foodAdapter.setFoodItems(foodItems);
                        });
                    } else {
                        Log.d(TAG, "Code de réponse : " + response.code());
                        runOnUiThread(() -> Toast.makeText(MainActivity.this, "Erreur lors de la récupération des données", Toast.LENGTH_SHORT).show());
                    }
                }
            });
        } else {
            Toast.makeText(this, "Pas de connexion Internet", Toast.LENGTH_SHORT).show();
        }
    }

    private List<FoodItem> parseFoodItems(String responseData) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<FoodItem>>(){}.getType();
        return gson.fromJson(responseData, listType);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }
}
