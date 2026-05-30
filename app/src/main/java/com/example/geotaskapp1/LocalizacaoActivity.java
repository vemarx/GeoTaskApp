package com.example.geotaskapp1;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

public class LocalizacaoActivity extends AppCompatActivity {
    private Button btnVoltar;
    private TextView txtLatitude;
    private TextView txtLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_localizacao);
        txtLatitude = findViewById(R.id.txtLatitude);
        txtLongitude = findViewById(R.id.txtLongitude);

        double latitude =
                getIntent().getDoubleExtra(
                        "LATITUDE",
                        0
                );

        double longitude =
                getIntent().getDoubleExtra(
                        "LONGITUDE",
                        0
                );

        txtLatitude.setText(
                "Latitude: " + latitude
        );

        txtLongitude.setText(
                "Longitude: " + longitude

        );
        btnVoltar = findViewById(R.id.btnVoltar);

        btnVoltar.setOnClickListener(v -> {
            finish();
        });

    }
}