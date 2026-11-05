package com.example.laborator4;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AdaugareActivitate extends AppCompatActivity {

    private EditText et;
    private CheckBox cb;
    private RadioGroup rg;
    private Spinner sp;
    private RatingBar rb;
    private Switch sw;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_adaugare_activitate);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //leg variabilele de interfata grafica
        et = findViewById(R.id.etModel);
        cb = findViewById(R.id.cbReactie);
        rg = findViewById(R.id.rgUtilizare);
        sp = findViewById(R.id.spinnerProducator);
        rb = findViewById(R.id.ratingBar);
        sw = findViewById(R.id.swInServiciu);
        btn = findViewById(R.id.btnSalvare);

        //construiesc lista derulanta a spinner ului
        String[] producatori = {"Boeing", "Airbus", "Embraer", "Bombardier", "Lockheed Martin"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, producatori);
        sp.setAdapter(adapter);

        //verific daca am primit un obiect pentru modificare
        Intent it = getIntent();
        if (it.hasExtra("avion")) {
            Avion avion = it.getParcelableExtra("avion");

            //populez campurile cu datele obiectului primit
            et.setText(avion.getModel());
            cb.setChecked(avion.getAreMotorReactie());
            sw.setChecked(avion.isEsteInServiciu());
            rb.setRating(avion.getRating());

            RadioButton rbMilitar = findViewById(R.id.rbMilitar);
            if (avion.getTipUtilizare().equals(rbMilitar.getText().toString())) {
                rg.check(R.id.rbMilitar);
            } else {
                rg.check(R.id.rbCivil);
            }

            for (int i = 0; i < producatori.length; i++) {
                if (producatori[i].equals(avion.getProducator())) {
                    sp.setSelection(i);
                    break;
                }
            }

            btn.setText("Salvează modificările");
        }

        btn.setOnClickListener(v -> {

            String model = et.getText().toString().trim();
            if (model.isEmpty()) {
                et.setError("Câmp obligatoriu!");
                return;
            }

            boolean areMotorReactie = cb.isChecked();
            boolean esteInServiciu = sw.isChecked();
            float rating = rb.getRating();

            String producator = sp.getSelectedItem().toString();

            int idSelectat = rg.getCheckedRadioButtonId();
            RadioButton rbSelectat = findViewById(idSelectat);
            String utilizare = rbSelectat.getText().toString();

            Avion avion = new Avion(model, areMotorReactie, utilizare, producator, rating, esteInServiciu);

            Intent intent = new Intent();
            intent.putExtra("avion_key", avion);
            setResult(RESULT_OK, intent);
            finish();
        });
    }
}