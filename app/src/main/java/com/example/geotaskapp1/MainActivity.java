package com.example.geotaskapp1;
import android.annotation.SuppressLint;
import android.Manifest;
import android.location.Location;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.room.Room;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    private AppDatabase database;
    private DatabaseReference firebaseRef;
    private FusedLocationProviderClient fusedLocationClient;
    private TextView txtInfo;
    private EditText edtTask;
    private Button btnSalvar;
    private Button btnLocalizacao;
    private double latitude;
    private double longitude;
    private ListView listTasks;
    private ArrayAdapter<String> adapter;
    private List<String> tarefasLista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtInfo = findViewById(R.id.txtInfo);
        edtTask = findViewById(R.id.edtTask);
        btnSalvar = findViewById(R.id.btnSalvar);
        btnLocalizacao = findViewById(R.id.btnLocalizacao);

        listTasks = findViewById(R.id.listTasks);

        tarefasLista = new ArrayList<>();

        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                tarefasLista
        );

        listTasks.setAdapter(adapter);


        // PERMISSÃO LOCALIZAÇÃO
        ActivityCompat.requestPermissions(
                this,
                new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION
                },
                1
        );

        // ROOM
        database = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class,
                "tarefas-db"
        ).allowMainThreadQueries().build();

        List<Task> tarefasSalvas =
                database.taskDao().listar();

        for (Task task : tarefasSalvas) {
            tarefasLista.add("✔ " + task.titulo);
        }

        adapter.notifyDataSetChanged();

        // FIREBASE
        firebaseRef = FirebaseDatabase
                .getInstance()
                .getReference("tarefas");

        // GEOLOCALIZAÇÃO
        fusedLocationClient =
                LocationServices.getFusedLocationProviderClient(this);

        // LOGIN GOOGLE
        if (GoogleSignIn.getLastSignedInAccount(this) != null) {
            txtInfo.setText(
                    getString(
                            R.string.usuario_autenticado_google
                    )
            );
        }

        // OBTER LOCALIZAÇÃO
        obterLocalizacao();

        btnLocalizacao.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            MainActivity.this,
                            LocalizacaoActivity.class
                    );

            intent.putExtra("LATITUDE", latitude);
            intent.putExtra("LONGITUDE", longitude);

            startActivity(intent);

        });

        // BOTÃO SALVAR
        btnSalvar.setOnClickListener(v -> {

            String tarefaTexto =
                    edtTask.getText().toString().trim();

            if (tarefaTexto.isEmpty()) {
                txtInfo.setText(
                        getString(
                                R.string.digite_tarefa_aviso
                        )
                );
                return;
            }

            Task task = new Task();
            task.titulo = tarefaTexto;

            // SALVAR ROOM
            database.taskDao().inserir(task);

            // SALVAR FIREBASE
            firebaseRef.push().setValue(task);

            txtInfo.setText(
                    getString(
                            R.string.tarefa_salva_sucesso
                    )
            );

            tarefasLista.add("✔ " + tarefaTexto);
            adapter.notifyDataSetChanged();

            edtTask.setText("");

        });
    }
    @SuppressLint("MissingPermission")
    private void obterLocalizacao() {

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {

                    if (location != null) {
                        mostrarLocalizacao(location);
                    }

                });
    }

    private void mostrarLocalizacao(@NonNull Location location) {

        latitude = location.getLatitude();
        longitude = location.getLongitude();

        String texto =
                getString(
                        R.string.latitude_format,
                        latitude
                )
                        + "\n"
                        + getString(
                        R.string.longitude_format,
                        longitude
                );

        txtInfo.append("\n" + texto);
    }
}