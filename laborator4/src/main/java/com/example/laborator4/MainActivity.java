package com.example.laborator4;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private TextView tvRezultat;
    private Button btnDeschide;
    private Button btnDeschideLista;
    private ArrayList<Avion> listaAvioane = new ArrayList<>();

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

        tvRezultat = findViewById(R.id.tvRezultat);
        btnDeschide = findViewById(R.id.btnDeschideAdaugare);
        btnDeschideLista = findViewById(R.id.btnDeschideLista);

        ActivityResultLauncher<Intent> launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Avion avionPrimit = result.getData().getParcelableExtra("avion_key");

                        if (avionPrimit != null) {
                            listaAvioane.add(avionPrimit);
                            tvRezultat.setText(avionPrimit.toString());

                            Toast.makeText(this, "Obiect preluat cu succes!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        ActivityResultLauncher<Intent> launcherLista = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        ArrayList<Avion> listaActualizata = result.getData().getParcelableArrayListExtra("lista_avioane");

                        if (listaActualizata != null) {
                            listaAvioane.clear();
                            listaAvioane.addAll(listaActualizata);
                        }
                    }
                });

        btnDeschideLista.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ListaAvioaneActivitate.class);
            intent.putParcelableArrayListExtra("lista_avioane", listaAvioane);
            launcherLista.launch(intent);
        });

        btnDeschide.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AdaugareActivitate.class);
            launcher.launch(intent);
        });
    }
}