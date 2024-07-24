package com.example.cucurmelo.ui.monitoring.detail_debit_air;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cucurmelo.data.models.sensors;
import com.example.cucurmelo.databinding.ActivityDebitAirBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DebitAirActivity extends AppCompatActivity {

    private ActivityDebitAirBinding binding;
    private DatabaseReference databaseReference;
    private TextView tvDebitAir, tvDebitAir2, tvDebitAir3;
    private Button buttonReload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDebitAirBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("/sensors");

        // Inisialisasi TextView dari layout menggunakan binding
        tvDebitAir = binding.tvDebitAir;
        tvDebitAir2 = binding.tvDebitAir2;
        tvDebitAir3 = binding.tvDebitAir3;
        buttonReload = binding.buttonReload;

        // Menambahkan listener untuk tombol reload
        buttonReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetDebitAirValues();
            }
        });

        // Mendapatkan dan menampilkan data debit air dari Firebase
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    sensors sensorData = dataSnapshot.getValue(sensors.class);

                    if (sensorData != null) {
                        tvDebitAir.setText("Sensor A: " + sensorData.getFlowRate1() + " ml/detik");
                        tvDebitAir2.setText("Sensor B: " + sensorData.getFlowRate2() + " ml/detik");
                        tvDebitAir3.setText("Sensor C: " + sensorData.getFlowRate3() + " ml/detik");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle the error
            }
        });
    }

    private void resetDebitAirValues() {
        tvDebitAir.setText("Sensor A: 0 ml/detik");
        tvDebitAir2.setText("Sensor B: 0 ml/detik");
        tvDebitAir3.setText("Sensor C: 0 ml/detik");

        // Update data di Firebase menjadi 0
        databaseReference.child("flowRate1").setValue(0);
        databaseReference.child("flowRate2").setValue(0);
        databaseReference.child("flowRate3").setValue(0);
    }
}
