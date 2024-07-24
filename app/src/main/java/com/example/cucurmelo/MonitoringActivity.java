package com.example.cucurmelo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cucurmelo.data.models.sensors;
import com.example.cucurmelo.databinding.ActivityMonitoringBinding;
import com.example.cucurmelo.ui.monitoring.detail_debit_air.DebitAirActivity;
import com.example.cucurmelo.ui.monitoring.detail_kelembapan.DetailKelembapanActivity;
import com.example.cucurmelo.ui.monitoring.suhu_cahaya.Suhu_CahayaActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MonitoringActivity extends AppCompatActivity {

    private ActivityMonitoringBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMonitoringBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("/sensor");
        

        Button btnKelembapanTanah = binding.buttonKelembapanTanah;

        btnKelembapanTanah.setOnClickListener(view -> {
            Intent intent = new Intent(MonitoringActivity.this, DetailKelembapanActivity.class);
            startActivity(intent);
        });

        Button btnDebitAir = binding.buttonDebitAir;

        btnDebitAir.setOnClickListener(view -> {
            Intent intent = new Intent(MonitoringActivity.this, DebitAirActivity.class);
            startActivity(intent);
        });

        Button btnSuhuCahaya = binding.buttonSuhuCahaya;

        btnSuhuCahaya.setOnClickListener(view -> {
            Intent intent = new Intent(MonitoringActivity.this, Suhu_CahayaActivity.class);
            startActivity(intent);
        });
    }
}