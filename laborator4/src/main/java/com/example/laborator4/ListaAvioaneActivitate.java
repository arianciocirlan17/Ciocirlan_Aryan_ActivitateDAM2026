package com.example.laborator4;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class ListaAvioaneActivitate extends AppCompatActivity {

    private ListView lvAvioane;
    private ArrayList<Avion> listaAvioane;
    private int idModificat = 0;
    private AvionAdapter adapter = null;
    private ActivityResultLauncher<Intent> launcherModificare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lista_avioane_activitate);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        lvAvioane = findViewById(R.id.lvAvioane);

        listaAvioane = getIntent().getParcelableArrayListExtra("lista_avioane");
        if (listaAvioane == null) {
            listaAvioane = new ArrayList<>();
        }

        adapter = new AvionAdapter(listaAvioane, getApplicationContext(), R.layout.item_avion);
        lvAvioane.setAdapter(adapter);

        launcherModificare = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Avion avionModificat = result.getData().getParcelableExtra("avion_key");

                        if (avionModificat != null) {
                            listaAvioane.set(idModificat, avionModificat);
                            adapter.notifyDataSetChanged();
                            trimiteListaInapoi();

                            Toast.makeText(this, "Avion modificat!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        lvAvioane.setOnItemClickListener((parent, view, i, id) -> {
            Intent intentModifica = new Intent(getApplicationContext(), AdaugareActivitate.class);
            intentModifica.putExtra("avion", listaAvioane.get(i));
            idModificat = i;
            launcherModificare.launch(intentModifica);
        });

        lvAvioane.setOnItemLongClickListener((parent, view, position, id) -> {
            listaAvioane.remove(position);
            adapter.notifyDataSetChanged();
            trimiteListaInapoi();

            Toast.makeText(this, "Avion șters!", Toast.LENGTH_SHORT).show();
            return true;
        });
    }

    private void trimiteListaInapoi() {
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra("lista_avioane", listaAvioane);
        setResult(RESULT_OK, intent);
    }
}