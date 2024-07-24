package com.example.cucurmelo.ui.rekapdata;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.cucurmelo.data.models.sensors;
import com.example.cucurmelo.databinding.FragmentRekapdataBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RekapDataFragment extends Fragment {

    private FragmentRekapdataBinding binding;
    private DatabaseReference databaseReference;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentRekapdataBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Inisialisasi referensi Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("sensors");

        // Mengambil data dari Firebase saat pertama kali
        fetchSensorData();

        // Menambahkan listener untuk tombol reload
        binding.buttonReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Memanggil fungsi untuk mereset tampilan ke nilai awal
                resetDisplayValues();

                // Memanggil kembali fungsi untuk mengambil data dari Firebase
                fetchSensorData();

                // Memanggil fungsi untuk mereset data di Firebase
                resetFirebaseData();
            }
        });

        return root;
    }

    private void fetchSensorData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sensors sensorData = dataSnapshot.getValue(sensors.class);

                if (sensorData != null) {
                    double rataRataKelembapan = (sensorData.getMoisture1() + sensorData.getMoisture2() + sensorData.getMoisture3() +
                            sensorData.getMoisture4() + sensorData.getMoisture5() + sensorData.getMoisture6()) / 6.0;

                    double rataRataDebitAir = (sensorData.getFlowRate1() + sensorData.getFlowRate2() + sensorData.getFlowRate3()) / 3.0;

                    double rataRataSuhu = sensorData.getTemperature(); // Menggunakan nilai suhu tunggal

                    //double rataRataCahaya = sensorData.getLux(); // Menggunakan nilai lux tunggal

                    // Mendapatkan waktu dan tanggal saat ini
                    Calendar calendar = Calendar.getInstance();
                    String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(calendar.getTime());
                    String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(calendar.getTime());

                    String tanggal = "Tanggal: " + currentDate;
                    String waktu = "Jam: " + currentTime;
                    String kelembapan = "Kelembapan Tanah: " + rataRataKelembapan + " %";
                    String suhu = "Suhu: " + rataRataSuhu + " °C";
                    String debitAir = "Debit Air: " + rataRataDebitAir + " ml/detik";
                   // String cahaya = "Cahaya: " + rataRataCahaya + " lux";

                    // Set text to UI components
                    binding.textTanggal.setText(tanggal);
                    binding.textWaktu.setText(waktu);
                    binding.textKelembapan.setText(kelembapan);
                    binding.textSuhu.setText(suhu);
                    binding.textDebitAir.setText(debitAir);
                    //binding.textCahaya.setText(cahaya);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Tangani kemungkinan kesalahan.
            }
        });
    }

    private void resetDisplayValues() {
        binding.textTanggal.setText("Tanggal: -");
        binding.textWaktu.setText("Jam: -");
        binding.textKelembapan.setText("Kelembapan Tanah: - %");
        binding.textSuhu.setText("Suhu: - °C");
        binding.textDebitAir.setText("Debit Air: - ml/detik");
        //binding.textCahaya.setText("Cahaya: - lux");
    }

    private void resetFirebaseData() {
        databaseReference.setValue(null);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
