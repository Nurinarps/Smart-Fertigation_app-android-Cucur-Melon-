package com.example.cucurmelo.ui.monitoring.suhu_cahaya;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cucurmelo.data.models.sensors;
import com.example.cucurmelo.databinding.ActivitySuhuCahayaBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Suhu_CahayaActivity extends AppCompatActivity {

    private ActivitySuhuCahayaBinding binding;
    private DatabaseReference databaseReference;
    private TextView tvSuhuCahaya1; //tvSuhuCahaya2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySuhuCahayaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("/sensors");

        tvSuhuCahaya1 = binding.tvSuhuCahaya1;
       // tvSuhuCahaya2 = binding.tvSuhuCahaya2;

        // Mendapatkan dan menampilkan data suhu dan cahaya dari Firebase
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    sensors sensorData = dataSnapshot.getValue(sensors.class);

                    if (sensorData != null) {
                        tvSuhuCahaya1.setText("Suhu: " + sensorData.getTemperature() + " °C");
                        //tvSuhuCahaya2.setText("Cahaya: " + sensorData.getLux() + " lux");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle the error
            }
        });
    }
}