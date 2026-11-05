package com.example.s2_dam2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

//se lucreaza exercitiile de la seminarul 2-3
public class ActivitateNoua extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_activitate_noua);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void deschideActivitate(View view) {
        //creez intent ul, in loc de getApplContext pot pune this
        Intent intent = new Intent(getApplicationContext(), ActivitateRezultat.class);
        //dam start activity ca sa imi deschida activitatea rezultat
        Bundle bundle = new Bundle(); //bundle - transmiti si pastrezi temporar date sub forma de perechi cheie valoare
        bundle.putInt("numar1", 6);
        bundle.putInt("numar2", 9);
        bundle.putString("mesaj", "Mesaj transmis din prima activitate");
        intent.putExtras(bundle);
        startActivityForResult(intent, 37);
    }

    //pot parsa datele din trecut folosint acel intent

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 37){
            if(resultCode == RESULT_OK){
                String mesaj = data.getStringExtra("mesaj");
                int suma = data.getIntExtra("suma", 0);
                Toast.makeText(this, mesaj+""+suma, Toast.LENGTH_SHORT);

            }
        }
    }
}