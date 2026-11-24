package com.example.laborator4;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImaginiAvioaneActivitate extends AppCompatActivity {

    private ListView lvImagini;
    private ArrayList<ImagineAvion> listaImagini = new ArrayList<>();
    private ImagineAvionAdapter adapter;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_imagini_avioane);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        lvImagini = findViewById(R.id.lvImagini);

        listaImagini.add(new ImagineAvion(
                "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b8/B-747_Iberia.jpg/330px-B-747_Iberia.jpg",
                "Boeing 747 - avionul de pasageri supranumit \"Regina cerului\"",
                "https://ro.wikipedia.org/wiki/Boeing_747"));

        listaImagini.add(new ImagineAvion(
                "https://upload.wikimedia.org/wikipedia/commons/thumb/0/09/A6-EDY_A380_Emirates_31_jan_2013_jfk_%288442269364%29_%28cropped%29.jpg/330px-A6-EDY_A380_Emirates_31_jan_2013_jfk_%288442269364%29_%28cropped%29.jpg",
                "Airbus A380 - cel mai mare avion de pasageri din lume, cu doua etaje",
                "https://ro.wikipedia.org/wiki/Airbus_A380"));

        listaImagini.add(new ImagineAvion(
                "https://upload.wikimedia.org/wikipedia/commons/thumb/e/eb/British_Airways_Concorde_G-BOAC_03.jpg/330px-British_Airways_Concorde_G-BOAC_03.jpg",
                "Concorde - avion supersonic de pasageri, retras in 2003",
                "https://ro.wikipedia.org/wiki/Concorde"));

        listaImagini.add(new ImagineAvion(
                "https://upload.wikimedia.org/wikipedia/commons/thumb/1/1e/F-22_Raptor_edit1_%28cropped%29.jpg/500px-F-22_Raptor_edit1_%28cropped%29.jpg",
                "Lockheed Martin F-22 Raptor - avion militar de vanatoare stealth",
                "https://ro.wikipedia.org/wiki/Lockheed_Martin_F-22_Raptor"));

        listaImagini.add(new ImagineAvion(
                "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b5/Antonov_Design_Bureau_Antonov_An-225_Mriya_at_Leipzig_Halle_%28EDDP-LEJ%29.jpg/330px-Antonov_Design_Bureau_Antonov_An-225_Mriya_at_Leipzig_Halle_%28EDDP-LEJ%29.jpg",
                "Antonov An-225 Mriya - cel mai greu avion de transport construit vreodata",
                "https://ro.wikipedia.org/wiki/Antonov_An-225"));

        adapter = new ImagineAvionAdapter(listaImagini, getApplicationContext(), R.layout.item_imagine_avion);
        lvImagini.setAdapter(adapter);

        lvImagini.setOnItemClickListener((parent, view, i, id) -> {
            Intent intent = new Intent(getApplicationContext(), WebViewActivitate.class);
            intent.putExtra("url", listaImagini.get(i).getUrlPagina());
            startActivity(intent);
        });

        descarcaImagini();
    }

    private void descarcaImagini() {
        executor.execute(() -> {
            for (ImagineAvion item : listaImagini) {
                Bitmap bitmap = descarcaBitmap(item.getUrlImagine());
                item.setImagine(bitmap);

                runOnUiThread(() -> adapter.notifyDataSetChanged());
            }
        });
    }

    private Bitmap descarcaBitmap(String adresa) {
        HttpURLConnection conexiune = null;
        try {
            URL url = new URL(adresa);
            conexiune = (HttpURLConnection) url.openConnection();
            conexiune.setRequestProperty("User-Agent", "Laborator4/1.0 (Android)");
            conexiune.setConnectTimeout(10000);
            conexiune.setReadTimeout(10000);
            conexiune.connect();

            if (conexiune.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conexiune.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                is.close();
                return bitmap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conexiune != null) {
                conexiune.disconnect();
            }
        }
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdownNow();
    }
}