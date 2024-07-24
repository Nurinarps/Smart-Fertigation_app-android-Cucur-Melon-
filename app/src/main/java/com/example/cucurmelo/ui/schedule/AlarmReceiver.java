package com.example.cucurmelo.ui.schedule;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Waktunya menyiram tanaman!", Toast.LENGTH_SHORT).show();

        // Mengaktifkan solenoid valve
        DatabaseReference controlDatabaseReference = FirebaseDatabase.getInstance().getReference("control");
        DatabaseReference relayDatabaseReference = FirebaseDatabase.getInstance().getReference("relays");

        Map<String, Object> controlUpdate = new HashMap<>();
        controlUpdate.put("A", "open");
        controlUpdate.put("B", "open");
        controlUpdate.put("C", "open");
        controlDatabaseReference.updateChildren(controlUpdate);

        Map<String, Object> relayUpdate = new HashMap<>();
        relayUpdate.put("relay1", 1);
        relayUpdate.put("relay2", 1);
        relayUpdate.put("relay3", 1);
        relayDatabaseReference.updateChildren(relayUpdate);

        // Mengambil nilai thresholdWaterFlow dari Firebase dan mengatur alarm berdasarkan nilai tersebut
        DatabaseReference settingsDatabaseReference = FirebaseDatabase.getInstance().getReference("settings");
        settingsDatabaseReference.child("thresholdWaterFlow").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Integer thresholdWaterFlow = dataSnapshot.getValue(Integer.class);
                if (thresholdWaterFlow != null) {
                    long durationInMillis;
                    switch (thresholdWaterFlow) {
                        case 1: // 300ml
                            durationInMillis = 2 * 60 * 1000; // contoh durasi 2 menit
                            break;
                        case 2: // 500ml
                            durationInMillis = 4 * 60 * 1000; // contoh durasi 3 menit
                            break;
                        case 0: // 0ml
                        default:
                            durationInMillis = 0; // tidak menyalakan alarm
                            break;
                    }
                    if (durationInMillis > 0) {
                        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                        Intent stopIntent = new Intent(context, WateringStopReceiver.class);
                        PendingIntent stopPendingIntent = PendingIntent.getBroadcast(context, 0, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + durationInMillis, stopPendingIntent);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, "Failed to retrieve thresholdWaterFlow", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
