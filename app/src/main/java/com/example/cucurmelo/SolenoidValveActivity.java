package com.example.cucurmelo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cucurmelo.databinding.ActivitySolenoidValveBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class SolenoidValveActivity extends AppCompatActivity {

    private ActivitySolenoidValveBinding binding;
    private DatabaseReference controlDatabaseReference;
    private DatabaseReference relayDatabaseReference;
    private DatabaseReference settingsDatabaseReference;
    private Switch switchA, switchB, switchC;
    private Button button300ml, button500ml, button0ml;
    private int thresholdWaterFlow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySolenoidValveBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Inisialisasi Firebase Database
        controlDatabaseReference = FirebaseDatabase.getInstance().getReference("control");
        relayDatabaseReference = FirebaseDatabase.getInstance().getReference("relays");
        settingsDatabaseReference = FirebaseDatabase.getInstance().getReference("settings");

        // Inisialisasi Switch
        switchA = binding.switchA;
        switchB = binding.switchB;
        switchC = binding.switchC;

        // Inisialisasi Button
        button300ml = binding.button300ml;
        button500ml = binding.button500ml;
        button0ml = binding.button0ml;

        // Mengambil nilai thresholdWaterFlow dari Firebase
        settingsDatabaseReference.child("thresholdWaterFlow").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                thresholdWaterFlow = dataSnapshot.getValue(Integer.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(SolenoidValveActivity.this, "Failed to retrieve settings", Toast.LENGTH_SHORT).show();
            }
        });

        // Mengambil status relay dari Firebase dan mengatur keadaan Switch
        DatabaseReference relayRef = FirebaseDatabase.getInstance().getReference("relays");

        relayRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Integer relay1Status = dataSnapshot.child("relay1").getValue(Integer.class);
                Integer relay2Status = dataSnapshot.child("relay2").getValue(Integer.class);
                Integer relay3Status = dataSnapshot.child("relay3").getValue(Integer.class);

                if (relay1Status != null && relay2Status != null && relay3Status != null) {
                    switchA.setChecked(relay1Status == 1);
                    switchB.setChecked(relay2Status == 1);
                    switchC.setChecked(relay3Status == 1);

                    // Log the status
                    Log.d("Firebase", "Relay1: " + relay1Status + ", Relay2: " + relay2Status + ", Relay3: " + relay3Status);
                } else {
                    Toast.makeText(SolenoidValveActivity.this, "Failed to retrieve relay status", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        });

        // Setup listener untuk setiap switch
        setupSwitchListener(switchA, "A", "relay1");
        setupSwitchListener(switchB, "B", "relay2");
        setupSwitchListener(switchC, "C", "relay3");

        // Setup listener untuk tombol 300ml
        button300ml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Memperbarui threshold water flow menjadi 1 (300ml)
                updateThresholdWaterFlow(1, "300ml update");
            }
        });

        // Setup listener untuk tombol 500ml
        button500ml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Memperbarui threshold water flow menjadi 2 (500ml)
                updateThresholdWaterFlow(2, "500ml update");
            }
        });

        // Setup listener untuk tombol 0ml
        button0ml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Memperbarui threshold water flow menjadi 0 (0ml)
                updateThresholdWaterFlow(0, "0ml update");
            }
        });
    }

    private void setupSwitchListener(Switch switchSensor, final String sensorName, final String relayName) {
        switchSensor.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                sendControlCommand(sensorName, "open");
                sendRelayCommand(relayName, 1);
            } else {
                sendControlCommand(sensorName, "close");
                sendRelayCommand(relayName, 0);
            }
        });
    }

    private void sendControlCommand(String sensorName, String command) {
        Map<String, Object> update = new HashMap<>();
        update.put(sensorName, command);
        controlDatabaseReference.updateChildren(update)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(SolenoidValveActivity.this, "Perintah Berhasil diKirim", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(SolenoidValveActivity.this, "Perintah gagal dikirim", Toast.LENGTH_SHORT).show();
                });
    }

    private void sendRelayCommand(String sensorName, Integer command) {
        Map<String, Object> update = new HashMap<>();
        update.put(sensorName, command);
        relayDatabaseReference.updateChildren(update)
                .addOnSuccessListener(aVoid -> {})
                .addOnFailureListener(e -> {});
    }

    private void updateThresholdWaterFlow(int newThresholdWaterFlow, String message) {
        settingsDatabaseReference.child("thresholdWaterFlow").setValue(newThresholdWaterFlow)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(SolenoidValveActivity.this, message, Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(SolenoidValveActivity.this, "Failed to update threshold Water Flow", Toast.LENGTH_SHORT).show();
                });
    }
}
