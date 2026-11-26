package com.example.laborator9;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VremeActivitate extends AppCompatActivity {

    private static final String API_KEY = "cheia_ta";

    private EditText etOras;
    private Spinner spZile;
    private Button btnCauta;
    private TextView tvRezultat;

    // Executor = fir secundar pentru internet; Handler = intoarcere in firul principal
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vreme_activitate);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etOras = findViewById(R.id.etOras);
        spZile = findViewById(R.id.spZile);
        btnCauta = findViewById(R.id.btnCauta);
        tvRezultat = findViewById(R.id.tvRezultat);

        // Partea a III-a: spinner cu 1 zi / 5 zile
        String[] optiuni = {"1 zi", "5 zile"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, optiuni);
        spZile.setAdapter(adapter);

        btnCauta.setOnClickListener(v -> {
            String oras = etOras.getText().toString().trim();
            if (oras.isEmpty()) {
                etOras.setError("Introdu un oras!");
                return;
            }
            // singurul lucru care difera intre 1 si 5 zile e numele metodei din link
            String metoda = spZile.getSelectedItemPosition() == 0 ? "1day" : "5day";
            tvRezultat.setText("Se incarca...");
            cautaVremea(oras, metoda);
        });
    }

    private void cautaVremea(String oras, String metoda) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    // ---------- Partea I: City Search -> cheia orasului ----------
                    String linkOras = "https://dataservice.accuweather.com/locations/v1/cities/search?apikey="
                            + API_KEY + "&q=" + URLEncoder.encode(oras, "UTF-8");

                    String raspunsOras = citesteDeLaLink(linkOras);

                    // raspunsul este un VECTOR de orase -> JSONArray
                    JSONArray vector = new JSONArray(raspunsOras);
                    if (vector.length() == 0) {
                        afiseaza("Orasul nu a fost gasit.");
                        return;
                    }
                    JSONObject obiect = vector.getJSONObject(0);
                    String cheieOras = obiect.getString("Key");
                    String numeOras = obiect.getString("LocalizedName");

                    Log.d("VREME", "Cheie oras: " + cheieOras);
                    handler.post(() -> Toast.makeText(VremeActivitate.this,
                            "Cheie oras: " + cheieOras, Toast.LENGTH_SHORT).show());

                    // ---------- Partea a II-a / a III-a: Daily Forecast ----------
                    // metric=true -> grade Celsius (implicit vin in Fahrenheit)
                    String linkVreme = "https://dataservice.accuweather.com/forecasts/v1/daily/"
                            + metoda + "/" + cheieOras
                            + "?apikey=" + API_KEY + "&metric=true";

                    String raspunsVreme = citesteDeLaLink(linkVreme);

                    // raspunsul este un OBIECT care contine vectorul DailyForecasts
                    JSONObject radacina = new JSONObject(raspunsVreme);
                    JSONArray zile = radacina.getJSONArray("DailyForecasts");

                    StringBuilder rezultat = new StringBuilder();
                    rezultat.append(numeOras).append(" (cheie: ").append(cheieOras).append(")\n\n");

                    // pentru 1 zi bucla ruleaza o data, pentru 5 zile de 5 ori
                    for (int i = 0; i < zile.length(); i++) {
                        JSONObject zi = zile.getJSONObject(i);
                        String data = zi.getString("Date").substring(0, 10);

                        JSONObject temperatura = zi.getJSONObject("Temperature");
                        double minim = temperatura.getJSONObject("Minimum").getDouble("Value");
                        double maxim = temperatura.getJSONObject("Maximum").getDouble("Value");

                        rezultat.append(data)
                                .append("\n   Minima: ").append(minim).append(" °C")
                                .append("\n   Maxima: ").append(maxim).append(" °C\n\n");
                    }

                    afiseaza(rezultat.toString());

                } catch (Exception e) {
                    Log.e("VREME", "Eroare", e);
                    afiseaza("Eroare: " + e.getMessage());
                }
            }
        });
    }

    // face cererea GET si intoarce tot textul primit (JSON-ul)
    private String citesteDeLaLink(String link) throws Exception {
        URL url = new URL(link);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(10000);

        int cod = conn.getResponseCode();
        if (cod != HttpURLConnection.HTTP_OK) {
            conn.disconnect();
            throw new Exception("Server a raspuns cu codul " + cod);
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder builder = new StringBuilder("");
        String linie;
        while ((linie = reader.readLine()) != null) {
            builder.append(linie);
        }
        reader.close();
        conn.disconnect();
        return builder.toString();
    }

    // TextView-ul se poate modifica doar din firul principal -> handler.post
    private void afiseaza(String text) {
        handler.post(() -> tvRezultat.setText(text));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdown();
    }
}