package com.example.geotaskapp1;

import android.annotation.SuppressLint;
import android.Manifest;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.room.Room;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;

public class MainActivity extends AppCompatActivity implements TaskAdapter.OnTaskActionListener {

    private AppDatabase database;
    private DatabaseReference firebaseRef;
    private FusedLocationProviderClient fusedLocationClient;
    private TextView txtInfo;
    private EditText edtTask;
    private Button btnSalvar;
    private TaskAdapter adapter;
    private List<Task> tarefasLista;
    private Task tarefaSelecionada;
    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtInfo = findViewById(R.id.txtInfo);
        edtTask = findViewById(R.id.edtTask);
        btnSalvar = findViewById(R.id.btnSalvar);
        Button btnLocalizacao = findViewById(R.id.btnLocalizacao);
        ListView listTasks = findViewById(R.id.listTasks);

        tarefasLista = new ArrayList<>();
        adapter = new TaskAdapter(this, tarefasLista, this);
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

        // Carregar tarefas do banco
        tarefasLista.addAll(database.taskDao().listar());
        adapter.notifyDataSetChanged();

        // FIREBASE
        firebaseRef = FirebaseDatabase.getInstance().getReference("tarefas");

        // GEOLOCALIZAÇÃO
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // LOGIN GOOGLE
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
            txtInfo.setText(getString(R.string.usuario_label, email != null ? email : "N/A"));
        }

        // OBTER LOCALIZAÇÃO
        obterLocalizacao();

        btnLocalizacao.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LocalizacaoActivity.class);
            intent.putExtra("LATITUDE", latitude);
            intent.putExtra("LONGITUDE", longitude);
            startActivity(intent);
        });

        // BOTÃO SALVAR / ATUALIZAR (CREATE/UPDATE)
        btnSalvar.setOnClickListener(v -> {
            String tarefaTexto = edtTask.getText().toString().trim();

            if (tarefaTexto.isEmpty()) {
                txtInfo.setText(getString(R.string.digite_tarefa_aviso));
                return;
            }

            if (tarefaSelecionada == null) {
                // MODO INSERIR (CREATE)
                Task task = new Task();
                task.titulo = tarefaTexto;

                // Salvar no Room primeiro para obter o ID
                long id = database.taskDao().inserir(task);
                task.id = (int) id;

                // Salvar no Firebase
                DatabaseReference newRef = firebaseRef.push();
                task.firebaseKey = newRef.getKey();
                newRef.setValue(task);
                
                // Atualizar o registro no Room com a chave do Firebase
                database.taskDao().atualizar(task);

                tarefasLista.add(task);
                Toast.makeText(this, R.string.tarefa_salva_sucesso, Toast.LENGTH_SHORT).show();
            } else {
                // MODO ATUALIZAR (UPDATE)
                tarefaSelecionada.titulo = tarefaTexto;
                database.taskDao().atualizar(tarefaSelecionada);

                // Atualizar no Firebase
                if (tarefaSelecionada.firebaseKey != null) {
                    firebaseRef.child(tarefaSelecionada.firebaseKey).setValue(tarefaSelecionada);
                }

                Toast.makeText(this, "Tarefa atualizada!", Toast.LENGTH_SHORT).show();
            }

            adapter.notifyDataSetChanged();
            limparFormulario();
        });
    }

    @Override
    public void onEdit(Task task) {
        tarefaSelecionada = task;
        edtTask.setText(tarefaSelecionada.titulo);
        btnSalvar.setText(R.string.atualizar_tarefa);
        edtTask.requestFocus();
    }

    @Override
    public void onDelete(Task task) {
        // Excluir do Room
        database.taskDao().excluir(task);
        
        // Excluir do Firebase
        if (task.firebaseKey != null) {
            firebaseRef.child(task.firebaseKey).removeValue();
        }

        tarefasLista.remove(task);
        adapter.notifyDataSetChanged();
        
        if (tarefaSelecionada != null && tarefaSelecionada.id == task.id) {
            limparFormulario();
        }
        
        Toast.makeText(this, "Tarefa removida", Toast.LENGTH_SHORT).show();
    }

    private void limparFormulario() {
        tarefaSelecionada = null;
        edtTask.setText("");
        btnSalvar.setText(R.string.salvar_tarefa);
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

        String texto = getString(R.string.latitude_format, latitude)
                        + "\n"
                        + getString(R.string.longitude_format, longitude);

        txtInfo.append("\n" + texto);
    }
}
