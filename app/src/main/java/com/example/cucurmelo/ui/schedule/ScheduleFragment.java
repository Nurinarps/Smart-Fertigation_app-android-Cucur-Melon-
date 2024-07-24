package com.example.cucurmelo.ui.schedule;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.cucurmelo.databinding.FragmentScheduleBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ScheduleFragment extends Fragment {

    private FragmentScheduleBinding binding;
    private TextView textClock, textDate;
    private EditText editTextTimerHour1, editTextTimerMinute1,
            editTextTimerHour2, editTextTimerMinute2,
            editTextTimerHour3, editTextTimerMinute3,
            editTextTimerHour4, editTextTimerMinute4,
            editTextTimerHour5, editTextTimerMinute5;
    private Handler handler = new Handler();
    private DatabaseReference databaseReference;

    private Runnable runnable;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentScheduleBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Inisialisasi Firebase Database
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Inisialisasi TextViews dan EditTexts
        textClock = binding.clok;
        textDate = binding.date;

        editTextTimerHour1 = binding.editTextTimerHour1;
        editTextTimerMinute1 = binding.editTextTimerMinute1;

        editTextTimerHour2 = binding.editTextTimerHour2;
        editTextTimerMinute2 = binding.editTextTimerMinute2;

        editTextTimerHour3 = binding.editTextTimerHour3;
        editTextTimerMinute3 = binding.editTextTimerMinute3;

        editTextTimerHour4 = binding.editTextTimerHour4;
        editTextTimerMinute4 = binding.editTextTimerMinute4;

        editTextTimerHour5 = binding.editTextTimerHour5;
        editTextTimerMinute5 = binding.editTextTimerMinute5;

        // Update waktu dan tanggal setiap detik
        runnable = new Runnable() {
            @Override
            public void run() {
                updateDateTime();
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(runnable);

        // Mengambil jadwal penyiraman dari Firebase
        DatabaseReference scheduleRef = FirebaseDatabase.getInstance().getReference("relayOnTimes");

        scheduleRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String key = snapshot.getKey();
                    int hour = snapshot.child("hour").getValue(Integer.class);
                    int minute = snapshot.child("minute").getValue(Integer.class);

                    // Tampilkan nilai hour dan minute di EditText yang sesuai
                    switch (key) {
                        case "0":
                            editTextTimerHour1.setText(String.valueOf(hour));
                            editTextTimerMinute1.setText(String.valueOf(minute));
                            break;
                        case "1":
                            editTextTimerHour2.setText(String.valueOf(hour));
                            editTextTimerMinute2.setText(String.valueOf(minute));
                            break;
                        case "2":
                            editTextTimerHour3.setText(String.valueOf(hour));
                            editTextTimerMinute3.setText(String.valueOf(minute));
                            break;
                        case "3":
                            editTextTimerHour4.setText(String.valueOf(hour));
                            editTextTimerMinute4.setText(String.valueOf(minute));
                            break;
                        case "4":
                            editTextTimerHour5.setText(String.valueOf(hour));
                            editTextTimerMinute5.setText(String.valueOf(minute));
                            break;
                        default:
                            break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to read value.", Toast.LENGTH_SHORT).show();
            }
        });

        // Set listener untuk tombol update
        Button updateButton = binding.updateButton;
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSchedule();
            }
        });

        return root;
    }

    private void updateDateTime() {
        // Format jam
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String currentTime = timeFormat.format(new Date());
        textClock.setText(currentTime);

        // Format tanggal
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy", Locale.getDefault());
        String currentDate = dateFormat.format(new Date());
        textDate.setText(currentDate);
    }

    private void updateSchedule() {
        Map<String, Object> scheduleMap = new HashMap<>();

        addScheduleToMap(scheduleMap, "0", editTextTimerHour1, editTextTimerMinute1);
        addScheduleToMap(scheduleMap, "1", editTextTimerHour2, editTextTimerMinute2);
        addScheduleToMap(scheduleMap, "2", editTextTimerHour3, editTextTimerMinute3);
        addScheduleToMap(scheduleMap, "3", editTextTimerHour4, editTextTimerMinute4);
        addScheduleToMap(scheduleMap, "4", editTextTimerHour5, editTextTimerMinute5);

        databaseReference.child("relayOnTimes").updateChildren(scheduleMap).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getContext(), "Jadwal berhasil diperbarui", Toast.LENGTH_SHORT).show();
                setAlarms();
            } else {
                Toast.makeText(getContext(), "Gagal memperbarui jadwal", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addScheduleToMap(Map<String, Object> scheduleMap, String key, EditText hourEditText, EditText minuteEditText) {
        try {
            int hour = Integer.parseInt(hourEditText.getText().toString());
            int minute = Integer.parseInt(minuteEditText.getText().toString());
            Map<String, Integer> timeMap = new HashMap<>();
            timeMap.put("hour", hour);
            timeMap.put("minute", minute);
            scheduleMap.put(key, timeMap);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private void setAlarms() {
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);

        setAlarm(alarmManager, "0", editTextTimerHour1, editTextTimerMinute1);
        setAlarm(alarmManager, "1", editTextTimerHour2, editTextTimerMinute2);
        setAlarm(alarmManager, "2", editTextTimerHour3, editTextTimerMinute3);
        setAlarm(alarmManager, "3", editTextTimerHour4, editTextTimerMinute4);
        setAlarm(alarmManager, "4", editTextTimerHour5, editTextTimerMinute5);
    }

    private void setAlarm(AlarmManager alarmManager, String requestCode, EditText hourEditText, EditText minuteEditText) {
        try {
            int hour = Integer.parseInt(hourEditText.getText().toString());
            int minute = Integer.parseInt(minuteEditText.getText().toString());

            Intent intent = new Intent(getContext(), AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), Integer.parseInt(requestCode), intent, PendingIntent.FLAG_UPDATE_CURRENT);

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, 0);

            if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
                calendar.add(Calendar.DAY_OF_YEAR, 1);
            }

            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacks(runnable);
        binding = null;
    }
}
