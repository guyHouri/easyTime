package com.example.easytime101;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Stack;

public class missions extends AppCompatActivity {

    public void todayDate() {
        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        tvDate.setText(currentDate);
    }
    int yearForCheck, monthForCheck, dayOfMonthForCheck;
    String event;
    ImageView iv_check_box;
    TextView tvDate;
    Long longDate;
    Button calander, btnReminder, settings;
    CustomAdapter adapter;
    Boolean isUrgent=false, isImportant=false;
    int radioid=-1;
    TextView tvName;
    int tag, Date, Time;
    String dateprint="";
    ArrayList<UserModel> newUsers ;
    DatePickerDialog.OnDateSetListener dateSetListener;

    public void start() {
        Intent intent = new Intent(this, open2.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_missions);

        ListView listView = (ListView) findViewById(R.id.listView);
        adapter = new CustomAdapter(newUsers, this);
        loadData();
        listView.setAdapter(adapter);
        tvDate = findViewById(R.id.DateInMis);
        todayDate();
        Intent intentName = getIntent();
        final String name = intentName.getStringExtra("theNameOfTheUser");
        if (newUsers==null)
            newUsers = new ArrayList<UserModel>();
        for (int i=0; i<newUsers.size();i++) {
            if (newUsers.get(i).getUrgent()) {
                UserModel u = newUsers.get(i);
                newUsers.remove(i);
                newUsers.add(0,u);
            }
        }
        saveData();
        loadData();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserModel u = newUsers.get(position);
                if (u.getImageResource()==R.drawable.check) {
                    u.setImageResource(R.drawable.checked);
                    u.setTitleResource(R.drawable.missunderline);
                    if (u.getImportant())
                        u.setWhatWas("important");
                    if (u.getUrgent())
                        u.setWhatWas("urgent");
                    u.setImportant(false);
                    u.setUrgent(false);
                    newUsers.remove(position);
                    newUsers.add(u);
                }
                else {
                    u.setImageResource(R.drawable.check);
                    u.setTitleResource(R.drawable.login_shape);
                    if (u.getWhatWas()!=null) {
                        if (u.getWhatWas().equals("important")) {
                            u.setImportant(true);
                            newUsers.remove(position);
                            addIfNotUrgent(u);
                        }
                        else {
                            u.setUrgent(true);
                            newUsers.remove(position);
                            newUsers.add(0, u);
                        }
                    }
                }
                saveData();
                loadData();
            }
        });
        Log.e("TAG", "onCreate: "+newUsers.toString() );
        settings = (Button) findViewById(R.id.btnSettingInMis);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toSettings = new Intent(missions.this, newSettings.class);
                toSettings.putExtra("theNameOfTheUser", name);
                startActivity(toSettings);
            }
        });
        btnReminder = (Button) findViewById(R.id.btnReminderInMis);
        btnReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toCal = new Intent(missions.this, Reminder.class);
                toCal.putExtra("theNameOfTheUser", name);
                saveData();
                loadData();
                startActivity(toCal);
            }
        });
        tvName = (TextView) findViewById(R.id.tvNameInMis);
        tvName.setText("hello "+name);

        calander= (Button) findViewById(R.id.btncal);
        calander.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toCal = new Intent(missions.this, open2.class);
                toCal.putExtra("theNameOfTheUser", name);
                saveData();
                loadData();
                startActivity(toCal);
            }
        });

        FloatingActionButton addmis = findViewById(R.id.addmis);
        addmis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(missions.this);
                View mView = getLayoutInflater().inflate(R.layout.addmissdialog, null);
                final CheckBox cbImportant = (CheckBox) mView.findViewById(R.id.cbImportant);
                final CheckBox cbUrgent = (CheckBox) mView.findViewById(R.id.cbUrgent);
                final EditText ettitle = mView.findViewById(R.id.ettitle);
                final TextView tvdate = mView.findViewById(R.id.etdate);
                Button btnsave = mView.findViewById(R.id.btnsave);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                tvdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar cal = Calendar.getInstance();
                        int year = cal.get(Calendar.YEAR);
                        int month = cal.get(Calendar.MONTH);
                        int day = cal.get(Calendar.DAY_OF_MONTH);
                        DatePickerDialog datePickerDialog = new DatePickerDialog(missions.this, R.style.Theme_AppCompat_DayNight_Dialog_MinWidth,
                                dateSetListener, year, month, day);
                        datePickerDialog.show();
                    }
                });

                dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        String date = dayOfMonth + "/" + month + "/" + year;;
                        tvdate.setText(date);
                        Date = turnNumbersToOne(dayOfMonth,month);
                        Date = turnNumbersToOne(Date,year);
                        dateprint = dayOfMonth + "/" + month + "/" + year;
                        yearForCheck = year; monthForCheck = month-1; dayOfMonthForCheck = dayOfMonth;

                    }
                };

                cbUrgent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (cbUrgent.isChecked()==true) {
                                isUrgent = true;
                        }
                        if (cbUrgent.isChecked()==false) {
                            isUrgent = false;
                        }
                        Log.e("missions", "cbUrgentClick: "+isUrgent );
                    }
                });

                cbImportant.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (cbImportant.isChecked()==true) {
                               isImportant = true;
                        }
                        if (cbImportant.isChecked()==false) {
                                isImportant=false;
                        }
                        Log.e("missions", "ccbImportantClick: "+isImportant);
                    }
                });

                btnsave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!ettitle.getText().toString().isEmpty() && !dateprint.equals("") && (isUrgent || isImportant)
                                && isDateOkay(yearForCheck, monthForCheck, dayOfMonthForCheck)) {
                            tag=Date;
                            UserModel u = new UserModel( isUrgent, ettitle.getText() + "   " + tvdate.getText(), tag, isImportant, dateprint);
                            creatNotif(u);
                            if (u.getUrgent() && u.getImportant())
                                u.setUrgent(false);
                            if (u.getUrgent()==true) {
                                Log.e("missions", "u added in: first");
                                newUsers.add(0, u);
                            }
                            else
                                addIfNotUrgent(u);
                            isImportant=false;
                            isUrgent=false;
                            saveData();
                            loadData();
                            dialog.dismiss();
                            Toast.makeText(missions.this, "mission saved", Toast.LENGTH_SHORT).show();
                        }  else if (!isDateOkay(yearForCheck, monthForCheck, dayOfMonthForCheck))
                            Toast.makeText(missions.this, "date is not valid", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(missions.this, "please fill any empty fields", Toast.LENGTH_SHORT).show();
                    }
                });



            }
        });

    }

    private void saveData() {
        SharedPreferences sharedPreferences1 = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor1 = sharedPreferences1.edit();
        Gson gson1 = new Gson();
        String sgson1 = gson1.toJson(newUsers);
        editor1.putString("task list", sgson1);
        editor1.apply();
    }

    private void loadData() {
        SharedPreferences sharedPreferences1 = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson1 = new Gson();
        String sgson1 = sharedPreferences1.getString("task list", null);
        Type type1 = new TypeToken<ArrayList<UserModel>>() {}.getType();
        newUsers = gson1.fromJson(sgson1, type1);
        adapter.updateRecords(newUsers);
        if (newUsers==null)
            newUsers = new ArrayList<>();
        else {
            for (int i=0; i<newUsers.size();i++) {
                Log.e("printTagInMission", "miss: "+newUsers.get(i).getUsername()+"isUrgent: "+newUsers.get(i).getUrgent()+
                        ", is important: "+newUsers.get(i).getImportant());
            }
        }

    }

    private int hofechMispar(int x) {
        String y="";
        if (x==0)
            return 00;
        if (x<10) {
            y="0"+""+x;
            return Integer.parseInt(y);
        }
        while (x>=1) {
            y=y+""+x%10;
            x=x/10;
        }
        Log.d("the frak", y);
        return Integer.parseInt(y);
    }

    private int turnNumbersToOne(int time, int date) {
        int x = hofechMispar(time);
        int z = hofechMispar(date);
        String y="";

        while (z>=1) {
            y = y+""+z%10;
            z=z/10;
        }
        while (x>=1) {
            y = y+""+x%10;
            x=x/10;
        }
        Log.d("the freak", y);
        return Integer.parseInt(y);
    }

    public void addIfNotUrgent(UserModel u) {
        // u is important
        Boolean listHasNoUrgent=true, hegati=false;
        if (newUsers.size()==0)
            newUsers.add(u);
        else if (newUsers.size()==1) {
            if (!newUsers.get(0).getUrgent())
                newUsers.add(0,u);
            else
                newUsers.add(u);
        }
        else  for (int i=0; i<newUsers.size();i++) {
            if (i==0 && !newUsers.get(0).getUrgent())
                newUsers.add(0,u);
            else if (newUsers.get(i-1).getUrgent() && !newUsers.get(i).getUrgent())
                newUsers.add(i,u);
            if (newUsers.get(i).getUrgent()) {
                listHasNoUrgent = false;
                hegati=true;
            }
        }
        if (listHasNoUrgent==true && hegati==true)
            newUsers.add(0, u);
    }


    public void creatNotif (UserModel u) {
        // Builds your notification

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {
            CharSequence name = "remember mission:  "+u.getUsername()+"?";
            String description = "it is due to "+u.getDate();
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notifyUser", name, importance);
            channel.setDescription(description);
        }
        Intent intent = new Intent(missions.this, missNotif.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(missions.this, 0,intent, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Long timeaAtButtonClick = System.currentTimeMillis();


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("remember mission:  "+u.getUsername()+"?")
                .setContentText("it is due to "+u.getDate());

        // Creates the intent needed to show the notification
        Intent notificationIntent = new Intent(this, missions.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }

    private Boolean isDateOkay(int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
// set the calendar to start of today
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        long todayInMillis = c.getTimeInMillis();
        Date today = c.getTime();

        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        long dayChosenByUser = c.getTimeInMillis();
        Date dateSpecified = c.getTime();

        if (dateSpecified.before(today))
            return false;
        else
            return true;
    }

}