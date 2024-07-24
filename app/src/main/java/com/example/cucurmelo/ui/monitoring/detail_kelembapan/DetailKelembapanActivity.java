package com.example.cucurmelo.ui.monitoring.detail_kelembapan;

import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cucurmelo.data.models.sensors;
import com.example.cucurmelo.databinding.ActivityDetailKelembapanBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetailKelembapanActivity extends AppCompatActivity {

    private ActivityDetailKelembapanBinding binding;
    private DatabaseReference databaseReference;
    private TextView tvDetailKelembapan, tvDetailKelembapan2, tvDetailKelembapan3, tvDetailKelembapan4, tvDetailKelembapan5, tvDetailKelembapan6;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailKelembapanBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("/sensors");

        // Inisialisasi TextView dari layout menggunakan binding
        tvDetailKelembapan = binding.tvDetailKelembapan;
        tvDetailKelembapan2 = binding.tvDetailKelembapan2;
        tvDetailKelembapan3 = binding.tvDetailKelembapan3;
        tvDetailKelembapan4 = binding.tvDetailKelembapan4;
        tvDetailKelembapan5 = binding.tvDetailKelembapan5;
        tvDetailKelembapan6 = binding.tvDetailKelembapan6;

        // Mendapatkan dan menampilkan data kelembapan tanah dari Firebase
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Ambil data dari dataSnapshot sesuai dengan struktur data di Firebase
                    sensors sensorData = dataSnapshot.getValue(sensors.class);

                    if (sensorData != null) {
                        // Menampilkan data ke TextView yang sesuai
                        tvDetailKelembapan.setText("Titik A-43: " + sensorData.getMoisture1() + "%");
                        tvDetailKelembapan2.setText("Titik A-38: " + sensorData.getMoisture2() + "%");
                        tvDetailKelembapan3.setText("Titik B-43: " + sensorData.getMoisture3() + "%");
                        tvDetailKelembapan4.setText("Titik B-35: " + sensorData.getMoisture4() + "%");
                        tvDetailKelembapan5.setText("Titik C-43: " + sensorData.getMoisture5() + "%");
                        tvDetailKelembapan6.setText("Titik C-30: " + sensorData.getMoisture6() + "%");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors
            }
        });
    }
}