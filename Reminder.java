package com.example.easytime101;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.easytime101.adapter.alarmDialogVariable;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Reminder extends AppCompatActivity {

    private int whichIsClosect() {
        Calendar c = Calendar.getInstance();
        Long positionOfCloset = null;
        if (!models.get(0).getHasStopped())
            positionOfCloset=models.get(0).getTimeInMillies();
        int position=0;
        for (int i=0;i<models.size();i++) {
            if (positionOfCloset!=null) {
                if (models.get(i).getTimeInMillies() > c.getTimeInMillis() && models.get(i).getTimeInMillies() < positionOfCloset
                        && !models.get(i).getHasStopped()) {
                    positionOfCloset = models.get(i).getTimeInMillies();
                    position = i;
                }
            } else if (models.get(i).getTimeInMillies() > c.getTimeInMillis() && !models.get(i).getHasStopped()) {
                positionOfCloset = models.get(i).getTimeInMillies();
                position = i;
            }
        }
        return position;
    }

    private boolean isThereAlert() {
        for (int i=0; i<models.size(); i++) {
            if (models.get(i).getHasHappened() && !models.get(i).getHasStopped())
                return true;
        }
        return false;
    }

    private void startAlert (modelAlarm modelalarm) {
        Log.e("service", "startAlert: got to start allert, timeOfAlert: " + modelalarm.getTime());
        Intent myIntent = new Intent(this.context, AlarmReceiver.class);
        myIntent.putExtra("extra", "yes");
        pending_intent = PendingIntent.getBroadcast(Reminder.this, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (!modelalarm.getHasHappened()) {
            savePos = models.indexOf(modelalarm);
            Log.e("service", "startAlert: started allert, timeOfAlert: " + modelalarm.getTime());
            alarmManager.set(AlarmManager.RTC_WAKEUP, modelalarm.getCal(), pending_intent);
            modelalarm.setHasHappened(true);
            models.set(models.indexOf(modelalarm), modelalarm);
            saveData(); loadData();
    }
        SharedPreferences.Editor editor = settings.edit();
        String uriString = myIntent.toUri(0);
        editor.putString("Contacts_app"+modelalarm.getTimeInMillies()+"", uriString);
        editor.commit();
    }

    public void stopAlert (modelAlarm modelAlarm) {
        Log.e("service", "got to stopalert timeOfAlert: " + modelAlarm.getTime());
        Intent myIntent = new Intent(this.context, AlarmReceiver.class);
        myIntent.putExtra("extra", "no");
        Intent telApp = new Intent();
        String contactsApp = settings.getString("Contacts_app"+modelAlarm.getTimeInMillies()+"", null);
        if (contactsApp!=null) {
            try {
                telApp = Intent.parseUri(contactsApp, 0);
            } catch (URISyntaxException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        PendingIntent pendingIntent = PendingIntent.getBroadcast(Reminder.this, 0, telApp, 0);
        if (pendingIntent!=null && modelAlarm.getHasHappened() && !modelAlarm.getHasStopped()) {
            modelAlarm.setHasStopped(true);
            if (savePos!=-1)
                models.set(savePos, modelAlarm);
            saveData();
            loadData();
            Log.e("service", "startAlert: stopped allert, timeOfAlert: " + modelAlarm.getTime());
            alarmManager.cancel(pendingIntent);
            sendBroadcast(myIntent);
            Toast.makeText(this, "alarm stopped", Toast.LENGTH_SHORT).show();
            if (!isThereAlert())
                startAlert(models.get(whichIsClosect()));
        }
        //setAlarmText("You clicked a " + " canceled");
    }

    public void todayDate() {
        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        tvDate.setText(currentDate);
    }

    Button btnAddAlarm, btnCal, btnMis, btnSettings;
    SharedPreferences settings;
    int yearForCheck, monthForCheck, dayOfMonthForCheck;
    int minuteForCheck, hourOfDayForCheck; Long timeInMili;
    AlarmManager alarmManager;
    private PendingIntent pending_intent;
    Context context;
    long cal;
    TextView tvDate, tvName;
    SharedPreferences sp; Boolean alarmFromSave=false;
    String dateOfToday, sDate,sTime, name;
    int todayDate, TodayDateTime, whereIsAdded;
    Reminder inst;
    int savePos=-1;
    boolean caved=false;
    RecyclerView mRecyclerView, mNewRecyclerView;
    alarmAdapter mAlarmAdapter;
    RecyclerView.LayoutManager mNewLayoutManager;
    myModelCardViewAdapter myModelCardViewAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    ArrayList<modelAlarm> models, emptyList;
    String daysToRepeat = "";
    ArrayList<alarmDialogVariable>  alarmDialogVariable;
    int Time,Date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        if (models==null) {
            models = new ArrayList<modelAlarm>();
        }
        loadData();
        for (int i=0; i<models.size();i++) {
            Log.e("models", "check list, name"+models.get(i).getName());
        }
        buildRecyclerView();
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        settings = getSharedPreferences("PREFERENCES", 0);
        this.context = this;
        tvName = findViewById(R.id.tvNameInAlarm);
        StartName();
        tvDate = findViewById(R.id.tvDateInAlarm);
        todayDate();
        sp=getSharedPreferences("alarmFromSave",0);
        btnAddAlarm = (Button) findViewById(R.id.btnAddAlarm);
        btnAddAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("add alarm", "onClick: " );
                inflateDialogLayout();
            }
        });

        btnCal = (Button) findViewById(R.id.btnCallanderInAlarm);
        btnMis = (Button) findViewById(R.id.btnMissionsInAlarm);
        btnCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentGetName = getIntent();
                Intent intent = new Intent(Reminder.this, open2.class);
                final String name = intentGetName.getExtras().getString("theNameOfTheUser", null);
                intent.putExtra("theNameOfTheUser", name);
                startActivity(intent);
            }
        });
        btnMis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentGetName = getIntent();
                Intent intent = new Intent(Reminder.this, missions.class);
                final String name = intentGetName.getExtras().getString("theNameOfTheUser", null);
                intent.putExtra("theNameOfTheUser", name);
                startActivity(intent);
            }
        });
        btnSettings = (Button) findViewById(R.id.etSetting);
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toSettings = new Intent(Reminder.this, newSettings.class);
                toSettings.putExtra("theNameOfTheUser", name);
                startActivity(toSettings);
            }
        });
    }

    public void buildRecyclerView() {
        mNewRecyclerView = findViewById(R.id.recycler_view);
        mNewRecyclerView.setHasFixedSize(true);
        mNewLayoutManager = new LinearLayoutManager(this);
        mAlarmAdapter = new alarmAdapter(models);
        mNewRecyclerView.setLayoutManager(mNewLayoutManager);
        mNewRecyclerView.setAdapter(mAlarmAdapter);
        mAlarmAdapter.setOnAlarmClickListener(new alarmAdapter.OnAlarmClickListener() {
            @Override
            public void onAlarmClick(int position) {
                modelAlarm mModelAlarm = models.get(position);
                inflateDialogLayoutBecauseClickedItem(mModelAlarm);
            }

            @Override
            public void onRemoveAlarmClick(int position) {
                modelAlarm mModelAlarm = models.get(position);
                if (sp != null)
                    alarmFromSave = sp.getBoolean("alarmFromSave", false);
                    saveData();
                    loadData();
                    Log.e("alarm", "stooped alarm in itenOnCkick started alarm in itenOnCkick while alarmfromsave is false");
                    stopAlert(mModelAlarm);
            }
        });

    }

    public void startRecyclerView () {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        myModelCardViewAdapter = new myModelCardViewAdapter(this, emptyList);
        myModelCardViewAdapter.notifyDataSetChanged();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        myModelCardViewAdapter = new myModelCardViewAdapter(this, models);
        myModelCardViewAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(myModelCardViewAdapter);
        myModelCardViewAdapter.setOnItemClickListener(new myModelCardViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                modelAlarm mModelAlarm = models.get(position);
                inflateDialogLayoutBecauseClickedItem(mModelAlarm);
            }

            @Override
            public void onDeleteClick(int position) {

            }
        });
    }


    public void inflateDialogLayout() {
        Log.e("add alarm", "inflate dialog layout: " );
        final AlertDialog.Builder Builder = new AlertDialog.Builder(Reminder.this);
        View mView = getLayoutInflater().inflate(R.layout.addalarmdialog, null);
        final EditText alarmName = (EditText) mView.findViewById(R.id.etAlarmName);
        final TextView selectDT = (TextView) mView.findViewById(R.id.tvChageDateTime);
        final Button deleteAlarm = (Button) mView.findViewById(R.id.btnDeleteAlarm);
        final Button saveAlarm = (Button) mView.findViewById(R.id.btnSaveAlarm);

        Builder.setView(mView);
        final AlertDialog dialog = Builder.create();
        dialog.show();


        deleteAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        selectDT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimeDialog(selectDT);
            }
        });

        saveAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!selectDT.getText().toString().equals("select date and time") && !alarmName.getText().toString().isEmpty()
                        && isDateOkay(yearForCheck, monthForCheck, dayOfMonthForCheck)
                        && isTimeOkey(hourOfDayForCheck, minuteForCheck)) {
                    modelAlarm modelAlarm = new modelAlarm();
                    todayDate();
                    String saveId = ""+Date+Time;
                    modelAlarm.setDateTimeInNumber(Integer.parseInt(saveId));
                    modelAlarm.setCal(cal);
                    if (caved == true)
                        sDate = daysToRepeat;
                    caved = false;
                    Log.d("modelAlarm", "sDate here: " + sDate);
                    modelAlarm.setDate(sDate);
                    Log.d("modelalrem", "time here: " + sTime);
                    modelAlarm.setTime(sTime);
                    sTime="";
                    modelAlarm.setTimeInMillies(timeInMili);
                    modelAlarm.setName(alarmName.getText().toString());
                    SharedPreferences.Editor editor=sp.edit();
                    editor.putBoolean("alarmFromSave",true);
                    editor.commit();
                    Log.e("dateTime", "date time is: "+modelAlarm.getDateTimeInNumber() );
                    addToList(modelAlarm);
                    if (models.indexOf(modelAlarm)==0)
                        startAlert(modelAlarm);
                    Log.d("added to list", models.get(models.size() - 1).getName());
                    saveData();
                    loadData();
                    mAlarmAdapter.notifyDataSetChanged();
                    dialog.dismiss();
                } else if (!isTimeOkey(hourOfDayForCheck, minuteForCheck))
                    Toast.makeText(Reminder.this, "time is not valid", Toast.LENGTH_SHORT).show();
                else if (!isDateOkay(yearForCheck, monthForCheck, dayOfMonthForCheck))
                    Toast.makeText(Reminder.this, "date is not valid", Toast.LENGTH_SHORT).show();
                else if (selectDT.getText().toString().equals("select date and time")) {
                    Toast.makeText(Reminder.this, "select time and date", Toast.LENGTH_SHORT).show();
                }
                else if (alarmName.getText().toString().isEmpty()) {
                    Toast.makeText(Reminder.this, "select name for alarm", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    // add models to arrayList
    public ArrayList<modelAlarm> addToList (modelAlarm p) {
        int dateTimeP = p.getDateTimeInNumber(); // for instance 16:20 may 16 2020 wiil be 20205161620
        Log.d("gotToAdd", "got to addToList");
        if (models==null) {
            models = new ArrayList<>();
            mAlarmAdapter.notifyDataSetChanged();
            buildRecyclerView();
            models.add(p);
            return models;
        } else if( models.size()==0) {
            models.add(p);
            mAlarmAdapter.notifyDataSetChanged();
            buildRecyclerView();
            return models;
        } else if (models.size()==1) {
            if (dateTimeP > models.get(0).getDateTimeInNumber()) {
                models.add(p);
                mAlarmAdapter.notifyDataSetChanged();
                buildRecyclerView();
                return models;
            }
            if (dateTimeP < models.get(0).getDateTimeInNumber()) {
                models.add(0, p);
                mAlarmAdapter.notifyDataSetChanged();
                buildRecyclerView();
                return models;
            }
        } else {
            for (int i = 0; i < models.size(); i++) {
                if (i==0 && dateTimeP<models.get(0).getDateTimeInNumber()) {
                    models.add(0, p);
                    mAlarmAdapter.notifyDataSetChanged();
                    buildRecyclerView();
                    return models;
                }
                if (i >= 1 && dateTimeP > models.get(i-1).getDateTimeInNumber() && dateTimeP < models.get(i).getDateTimeInNumber()) {
                    models.add(i, p);
                    mAlarmAdapter.notifyDataSetChanged();
                    buildRecyclerView();
                    return models;
                }
                if (i==models.size()-1 && dateTimeP > models.get(i).getDateTimeInNumber()) {
                    models.add(p);
                    mAlarmAdapter.notifyDataSetChanged();
                    buildRecyclerView();
                    return models;
                }
            }
        }
        return models;
    }

    private void showDateTimeDialog(final TextView date_time_in) {
        final Calendar calendar=Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String passDate="";
                if (month<10)
                    passDate = "0"+month+dayOfMonth;
                if (dayOfMonth<10)
                    passDate = passDate+month+"0"+dayOfMonth;
                Date = Integer.parseInt(passDate);
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                yearForCheck = year; monthForCheck = month; dayOfMonthForCheck = dayOfMonth;
                sDate= DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

                TimePickerDialog.OnTimeSetListener timeSetListener =new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Calendar datetime = Calendar.getInstance();
                        datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        datetime.set(Calendar.MINUTE, minute);
                        timeInMili = datetime.getTimeInMillis();
                        Log.d("millies", "chosen are: "+timeInMili);
                        Calendar c = Calendar.getInstance();
                        Log.d("millies", "millis now are: "+c.getTimeInMillis());
                        Log.d("what is Time", "minute: "+minute+", hour: "+hourOfDay);
                        String saveTime="";

                        if (hourOfDay<10 && minute<10) {
                            sTime = "0" + hourOfDay + ":" + "0" + minute;
                            saveTime ="0"+hourOfDay+"0"+minute;
                        }
                        if (hourOfDay<10 && minute>9) {
                            sTime = "0" + hourOfDay + ":" + minute;
                            saveTime ="0"+hourOfDay+minute;
                        }
                        if (hourOfDay>9 && minute<10) {
                            sTime = hourOfDay + ":0" + minute;
                            saveTime =""+hourOfDay+"0"+minute;
                        }
                        if (hourOfDay>9 && minute>9) {
                            sTime = "" + hourOfDay + ":" + minute;
                            saveTime =""+hourOfDay+minute;
                        }
                        Time = Integer.parseInt(saveTime);
                        calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        calendar.set(Calendar.MINUTE,minute);
                        hourOfDayForCheck = hourOfDay; minuteForCheck = minute;
                        cal =  calendar.getTimeInMillis();
                        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH:mm EE, MM/dd/yy");
                        date_time_in.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                };

                new TimePickerDialog(Reminder.this,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true).show();
            }
        };

        new DatePickerDialog(Reminder.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();

    }

    public void inflateDialogLayoutBecauseClickedItem(final modelAlarm mModelAlarm) {
        final modelAlarm alarmHolder = mModelAlarm;
        final int position = models.indexOf(mModelAlarm);
        final AlertDialog.Builder Builder = new AlertDialog.Builder(Reminder.this);
        View mView = getLayoutInflater().inflate(R.layout.addalarmdialog, null);
        final EditText alarmName = (EditText) mView.findViewById(R.id.etAlarmName);
        alarmName.setText(mModelAlarm.getName());
        final TextView selectDT = (TextView) mView.findViewById(R.id.tvChageDateTime);
        selectDT.setText(mModelAlarm.getTime()+" "+mModelAlarm.getDate());
        final Button saveAlarm = (Button) mView.findViewById(R.id.btnSaveAlarm);
        final Button deleteAlarm = (Button) mView.findViewById(R.id.btnDeleteAlarm);
        Builder.setView(mView);
        final AlertDialog dialog = Builder.create();
        dialog.show();


        deleteAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                models.remove(mModelAlarm);
                saveData();
                loadData();
                mAlarmAdapter.notifyDataSetChanged();
                buildRecyclerView();
                dialog.dismiss();
                stopAlert(mModelAlarm);
            }
        });

        selectDT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimeDialog(selectDT);
            }
        });

        saveAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!selectDT.getText().toString().equals("select date and time") && !alarmName.getText().toString().isEmpty()
                        && isDateOkay(yearForCheck, monthForCheck, dayOfMonthForCheck)
                        && isTimeOkey(hourOfDayForCheck, minuteForCheck)) {
                    String saveId = ""+Date+Time;
                    mModelAlarm.setDateTimeInNumber(Integer.parseInt(saveId));
                    if (caved)
                        sDate=daysToRepeat;
                    caved=false;
                    mModelAlarm.setName(alarmName.getText().toString());
                    mModelAlarm.setDate(sDate);
                    Log.e("sdate", "s date: "+sDate+", sTime ="+sTime);
                    mModelAlarm.setTime(sTime);
                    sTime="";
                    mModelAlarm.setTimeInMillies(timeInMili);
                    SharedPreferences.Editor editor=sp.edit();
                    editor.putBoolean("alarmFromSave",true);
                    models.remove(position);
                    addToList(mModelAlarm);
                    if (alarmHolder.getHasHappened() && !alarmHolder.getHasStopped()) {
                        stopAlert(alarmHolder);
                    }
                    else if (whichIsClosect()==models.indexOf(mModelAlarm)) {
                        for (int i=0; i<models.size(); i++) {
                            if (!models.get(i).getHasStopped() && models.get(i).getHasHappened())
                                startAlert(models.get(i));
                        }
                    }
                    saveData();
                    loadData();
                    mAlarmAdapter.notifyDataSetChanged();
                    buildRecyclerView();
                    dialog.dismiss();
                } else if (!isTimeOkey(hourOfDayForCheck, minuteForCheck))
                    Toast.makeText(Reminder.this, "time is not valid", Toast.LENGTH_SHORT).show();
                else if (!isDateOkay(yearForCheck, monthForCheck, dayOfMonthForCheck))
                    Toast.makeText(Reminder.this, "date is not valid", Toast.LENGTH_SHORT).show();
                else if (selectDT.getText().toString().equals("select date and time")) {
                    Toast.makeText(Reminder.this, "select time and date", Toast.LENGTH_SHORT).show();
                }
                else if (alarmName.getText().toString().isEmpty()) {
                    Toast.makeText(Reminder.this, "select name for alarm", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void saveData() {
        SharedPreferences sharedPreferences3 = getSharedPreferences("shared preferences alarms", MODE_PRIVATE);
        SharedPreferences.Editor editor3 = sharedPreferences3.edit();
        Gson gson3 = new Gson();
        String sgson3 = gson3.toJson(models);
        editor3.putString("user list", sgson3);
        editor3.apply();
    }

    private void loadData() {
        SharedPreferences sharedPreferences2 = getSharedPreferences("shared preferences alarms", MODE_PRIVATE);
        Gson gson2 = new Gson();
        String sgson2 = sharedPreferences2.getString("user list", null);
        Type type2 = new TypeToken<ArrayList<modelAlarm>>() {}.getType();
        models = gson2.fromJson(sgson2, type2);
        if (models==null)
            models = new ArrayList<>();
    }

    public void StartName() {
        Intent intentName = getIntent();
        name = intentName.getStringExtra("theNameOfTheUser");
        tvName.setText("hello "+name);
    }

    @Override
    public void onStart() {
        super.onStart();
        inst = this;
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

    private Boolean isTimeOkey (int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        Calendar datetime = Calendar.getInstance();
        datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        datetime.set(Calendar.MINUTE, minute);
        if(datetime.getTimeInMillis() > c.getTimeInMillis()){
            return true;
        }else{
            return false;
        }
    }



}
