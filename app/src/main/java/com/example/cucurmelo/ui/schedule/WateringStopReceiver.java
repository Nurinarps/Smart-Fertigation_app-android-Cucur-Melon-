package com.example.cucurmelo.ui.schedule;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class WateringStopReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Mematikan solenoid valve
        DatabaseReference controlDatabaseReference = FirebaseDatabase.getInstance().getReference("control");
        DatabaseReference relayDatabaseReference = FirebaseDatabase.getInstance().getReference("relays");

        Map<String, Object> controlUpdate = new HashMap<>();
        controlUpdate.put("A", "close");
        controlUpdate.put("B", "close");
        controlUpdate.put("C", "close");
        controlDatabaseReference.updateChildren(controlUpdate);

        Map<String, Object> relayUpdate = new HashMap<>();
        relayUpdate.put("relay1", 0);
        relayUpdate.put("relay2", 0);
        relayUpdate.put("relay3", 0);
        relayDatabaseReference.updateChildren(relayUpdate);

        // Menampilkan notifikasi bahwa penyiraman telah selesai
        Toast.makeText(context, "Waktu penyiraman selesai!", Toast.LENGTH_SHORT).show();
    }
}
