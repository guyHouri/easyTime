package com.example.easytime101;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.example.easytime101.ExampleItemEvent;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class open2 extends AppCompatActivity {
    Button mis, cal, rem, settings, reminder;
    TextView tvDate;
    int yearForCheck, monthForCheck, dayOfMonthForCheck;
    int minuteForCheck, hourOfDayForCheck;
    String sDate,sTime;
    Context mContext = this;
    SharedPreferences sp;
    SQLiteDatabase sqLiteDatabaseObj;
    int hap = 0, Date, Time, first = 0, hour, minute;
    boolean isScouts=false,isFriends=false,isSchool=false;
    String saveTitle;
    String happened;
    long cal2;
    private int countEvent=0;
    ArrayList<ExampleItemEvent>  exampleItemEvents;
    private RecyclerView mRecyclerView;
    private ExampleEventAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static final int locationPermissionRequestCode = 1234;
    String titleholder, placeholder, dateholder,SQLiteDataBaseQueryHolder;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    DatePickerDialog.OnDateSetListener dateSetListener;

    private void getHourNow() {
        Calendar cal1 = Calendar.getInstance();
        minute=cal1.get(Calendar.MINUTE);
        hour=cal1.get(Calendar.HOUR_OF_DAY);
    }

    public void buildRecyclerView() {
        mRecyclerView = findViewById(R.id.recycler_view_event);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ExampleEventAdapter(exampleItemEvents);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        for (int i=0; i<exampleItemEvents.size(); i++) {
            Log.e("open2", "bodek id shel: "+exampleItemEvents.get(i).getTitle()+", id is: "+exampleItemEvents.get(i).getId() );
        }
        mAdapter.setOnItemClickListener(new ExampleEventAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onDeleteClick(int position) {
                Toast.makeText(open2.this, "event "+exampleItemEvents.get(position).getTitle()+" has been deleted",
                        Toast.LENGTH_SHORT).show();
                exampleItemEvents.remove(position);
                saveData();
                loadData();
                mAdapter.notifyDataSetChanged();
                buildRecyclerView();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open2);
        if (exampleItemEvents==null) {
            exampleItemEvents = new ArrayList<>();
        }
        loadData();
        buildRecyclerView();
        sp=getSharedPreferences("details1",0);
        Intent intentName = getIntent();
        final String name = intentName.getExtras().getString("theNameOfTheUser", null);
        getHourNow();
        tvDate = (TextView) findViewById(R.id.tvDateOfToday);
        todayDate();
        settings = (Button) findViewById(R.id.btnSettings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toSettings = new Intent(open2.this, newSettings.class);
                toSettings.putExtra("theNameOfTheUser", name);
                startActivity(toSettings);
            }
        });
        reminder = (Button) findViewById(R.id.btnReminder);
        reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toReminder = new Intent(open2.this, Reminder.class);
                toReminder.putExtra("theNameOfTheUser", name);
                startActivity(toReminder);
            }
        });

        final TextView tvName = (TextView) findViewById(R.id.tvName);
        Log.d("theNameOfTheUser1", "name "+name);
        if (name!=null)  {
            Log.d("IntentNameOfTheUser", "got to if");
            tvName.setText("hello "+name);
        }
        final ExampleItemEvent event = new ExampleItemEvent("", 0, "", "", "");

        final FloatingActionButton addevent = findViewById(R.id.addevent);
        final Intent intentplace = getIntent();
        happened = intentplace.getExtras().getString("happened", null);
        Log.e("check value", "hap = " + hap + "happened = " + happened);
        if (happened!=null) {
            if (happened.equals("true")) {
                Log.d("intent passed", "addevent performed clicked");
                addevent.post(new Runnable() {
                    @Override
                    public void run() {
                        addevent.performClick();
                    }
                });
            }
        }
        hap++;
        mis = (Button) findViewById(R.id.btnmis);
        mis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == mis) {

                    Intent intent = new Intent(open2.this, missions.class);
                    intent.putExtra("theNameOfTheUser", name);
                    startActivity(intent);
                }
            }
        });

        addevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final RadioButton radioButton;
                final AlertDialog.Builder Builder = new AlertDialog.Builder(open2.this);
                View mView = getLayoutInflater().inflate(R.layout.addeventdialog, null);
                final TextView place = mView.findViewById(R.id.place);
                final EditText eteventtitle = mView.findViewById(R.id.eteventtitle);
                final CheckBox cbScouts = (CheckBox) mView.findViewById(R.id.cbScouts);
                final CheckBox cbSchool = (CheckBox) mView.findViewById(R.id.cbSchool);
                final CheckBox cbFriends = (CheckBox) mView.findViewById(R.id.cbFriends);
                Log.d("before saved title", "title: "+saveTitle);;
                Button btnsaveevent = mView.findViewById(R.id.btnSaveEvent);
                Button btnDeleteEvent = mView.findViewById(R.id.btnDeleteEvent);
                final TextView date = mView.findViewById(R.id.date);

                if (happened!=null) {
                    if (happened.equals("true")) {
                        sp=getSharedPreferences("details1",0);
                        if (sp!= null) {
                            if (sp.getString("theNameOfTheUser", null)!=null)
                                tvName.setText(sp.getString("theNameOfTheUser", null));
                        }
                        saveTitle = sp.getString("title",null);
                        Log.d("intent passed", "atext was set");
                        place.setText(intentplace.getStringExtra("location"));
                        eteventtitle.setText(sp.getString("title",null));
                        cbFriends.setChecked(sp.getBoolean("isFriends", false));
                        isFriends = sp.getBoolean("isFriends", false);
                        cbSchool.setChecked(sp.getBoolean("isSchool", false));
                        isSchool = sp.getBoolean("isSchool", false);
                        cbScouts.setChecked(sp.getBoolean("isScouts", false));
                        isScouts = sp.getBoolean("isScouts", false);
                        happened="false";
                    }
                }
                Builder.setView(mView);
                final AlertDialog dialog = Builder.create();
                dialog.show();

                btnDeleteEvent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position=0;
                        for (int i=0;i<exampleItemEvents.size();i++) {
                            if ((event.getTitle()+event.getDate()+event.getPlace()).
                                    equals(exampleItemEvents.get(i).getTitle()+exampleItemEvents.get(i).getDate()+exampleItemEvents.get(i).getPlace()))
                                   position = i;
                        }
                        exampleItemEvents.remove(position);
                    }
                });

                cbScouts.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (cbScouts.isChecked()==true)
                            isScouts = true;
                        else
                            isScouts = false;
                    }
                });
                cbSchool.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (cbSchool.isChecked()==true)
                            isSchool = true;
                        else
                            isSchool = false;
                    }
                });
                cbFriends.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (cbFriends.isChecked()==true)
                            isFriends = true;
                        else
                            isFriends = false;
                    }
                });

                place.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences.Editor editor=sp.edit();
                        editor.putString("title",eteventtitle.getText().toString());
                        Log.e("checkbox", "onClick, isSccouts: "+isScouts+", isSchool: "+isSchool+", isFriends: "+isFriends );
                        editor.putBoolean("isScouts", isScouts);
                        editor.putBoolean("isSchool", isSchool);
                        editor.putBoolean("isFriends", isFriends);
                        editor.putString("theNameOfTheUser", tvName.getText().toString());
                        editor.commit();
                        Log.d("svae title", "title: "+saveTitle);
                        if (ContextCompat.checkSelfPermission(getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                                && ContextCompat.checkSelfPermission(getApplicationContext(),
                                COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            if (isServicesOk()) {
                                Intent intentmap = new Intent(open2.this, map.class);
                                startActivity(intentmap);
                            }
                        } else {
                            getLocationPermission();
                            if (first > 1) {
                                Toast.makeText(open2.this, "cant open map services without location permission",
                                        Toast.LENGTH_LONG).show();
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);
                            } else
                                getLocationPermission();
                        }
                    }
                });

                date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDateTimeDialog(date);
                    }
                });

                btnsaveevent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!eteventtitle.getText().toString().isEmpty() && !place.getText().toString().isEmpty() && ((isFriends && !isSchool && !isScouts)
                        || (!isFriends && !isSchool && isScouts) || (!isFriends && isSchool && !isScouts))
                                && !date.getText().equals("select date and time") && isDateOkay(yearForCheck, monthForCheck, dayOfMonthForCheck)
                                && isTimeOkey(hourOfDayForCheck, minuteForCheck)) {
                            event.setStartTime(sTime);
                            event.setDate(sDate);
                            Log.e("TAG", "get start time: "+event.getStartTime() );
                            setImageresource(event);
                            String saveId = ""+Date+Time;
                            event.setId(Integer.parseInt(saveId));
                            Log.e("event id", "onClick: event id"+event.getId() );
                            event.setPlace(place.getText().toString());
                            event.setTitle(eteventtitle.getText().toString());
                            isScouts=false;
                            isFriends=false;
                            isSchool=false;
                            addToList(event);
                            saveData();
                            loadData();
                            countEvent++;
                            dialog.dismiss();
                            Toast.makeText(open2.this, "event saved", Toast.LENGTH_SHORT).show();
                        } else if (!isTimeOkey(hourOfDayForCheck, minuteForCheck))
                            Toast.makeText(open2.this, "time is not valid", Toast.LENGTH_SHORT).show();
                        else if (!isDateOkay(yearForCheck, monthForCheck, dayOfMonthForCheck))
                            Toast.makeText(open2.this, "date is not valid", Toast.LENGTH_SHORT).show();
                        else if (!(isFriends && !isSchool && !isScouts)
                                || (!isFriends && !isSchool && isScouts) || (!isFriends && isSchool && !isScouts))
                            Toast.makeText(open2.this, "please select one theme", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(open2.this, "please fill any empty fields", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        super.onOptionsItemSelected(item);
        return true;
    }

    public Boolean getLocationPermission() {
        Log.e("permission", "got to getLocationPermission");
        first++;
        final String[] permissions = {android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.getApplicationContext(), COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.d("location permission", "location permission");
            return true;
        }
        if (!ActivityCompat.shouldShowRequestPermissionRationale(open2.this, FINE_LOCATION) && 0==1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("This app need to access to device location.");
            builder.setTitle("location permission needed");
            builder.setPositiveButton("allow", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions(open2.this, permissions, locationPermissionRequestCode);
                }
            });
            builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                    dialogInterface.dismiss();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this.getApplicationContext(), COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Log.d("location permission", "location permission");
                return true;
            }
            return false;
        } else {
            ActivityCompat.requestPermissions(this, permissions, locationPermissionRequestCode);
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this.getApplicationContext(), COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                return true;
        }
        return false;
    }

    // checks if can run google map
    public boolean isServicesOk() {
        Log.d("isSeviceOk", "isSeviceOk");
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        if (available == ConnectionResult.SUCCESS) {
            Log.d("isSeviceOk", "ServiceOk");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            Log.d("isSeviceOk", "an error accord but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(this, available, 9001);
            dialog.show();
        } else {
            Toast.makeText(this, "dont have google maps service", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    public int findPosition(ExampleItemEvent event) {
        for (int i=0;i<exampleItemEvents.size();i++) {
            if ((event.getTitle()+event.getDate()+event.getPlace()).
                    equals(exampleItemEvents.get(i).getTitle()+exampleItemEvents.get(i).getDate()+exampleItemEvents.get(i).getPlace()))
                return i;
        }
        return 0;
    }

    public void insertItem(ExampleItemEvent event) {

    }


    private void showDateTimeDialog(final TextView date_time_in) {
        final Calendar calendar=Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                yearForCheck = year; monthForCheck = month; dayOfMonthForCheck = dayOfMonth;
                sDate= DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
                Log.e("open2", "onDateSet, day of month: "+dayOfMonth+", month: "+month);
                String passDate="";
                if (month<10 && dayOfMonth<10)
                    passDate = "0"+month+"0"+dayOfMonth;
                if (month<10 && dayOfMonth>9)
                    passDate = "0"+month+dayOfMonth;
                if (month>9 && dayOfMonth<10)
                    passDate = month+"0"+dayOfMonth;
                if (month>9 && dayOfMonth>9)
                    passDate = ""+month+dayOfMonth;
                Date = Integer.parseInt(passDate);

                TimePickerDialog.OnTimeSetListener timeSetListener =new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Log.d("what is Time", "on time set, minute: "+minute+", hour: "+hourOfDay);
                        calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        calendar.set(Calendar.MINUTE,minute);
                        hourOfDayForCheck = hourOfDay; minuteForCheck = minute;
                        String saveTime="";
                        if (hourOfDay<10 && minute<10)
                              saveTime ="0"+hourOfDay+"0"+minute;
                        if (hourOfDay<10 && minute>9)
                            saveTime ="0"+hourOfDay+minute;
                        if (hourOfDay>9 && minute<10)
                            saveTime =hourOfDay+"0"+minute;
                        if (hourOfDay>9 && minute>9)
                            saveTime ="0"+hourOfDay+minute;
                        Time = Integer.parseInt(saveTime);
                        if (hourOfDay<10 && minute<10)
                           sTime="0"+hourOfDay+":"+"0"+minute;
                        if (hourOfDay<10 && minute>9)
                            sTime="0"+hourOfDay+":"+minute;
                        if (hourOfDay>9 && minute<10)
                            sTime=hourOfDay+":0"+minute;
                        if (hourOfDay>9 && minute>9)
                            sTime=""+hourOfDay+minute;
                        Log.e("open2", "sTime "+sTime);
                        Log.e("open2", "on time set, minute: "+minute+", hour: "+hourOfDay);
                        cal2 =  calendar.getTimeInMillis();
                        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH:mm EE, MM/dd/yy");
                        date_time_in.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                };

                new TimePickerDialog(open2.this,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true).show();
            }
        };

        new DatePickerDialog(open2.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();

    }


    public void todayDate() {
        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        tvDate.setText(currentDate);
    }

    public void setImageresource (ExampleItemEvent event) {
        if (isScouts)
            event.setmImageResource(R.drawable.scoutsicon);
        if (isSchool)
            event.setmImageResource(R.drawable.schooloficon);
        if (isFriends)
            event.setmImageResource(R.drawable.friendicon);
    }

    public ArrayList<ExampleItemEvent> addToList (ExampleItemEvent p) {
        int dateTimeP = p.getId(); // for instance 16:20 may 16 2020 wiil be 20205161620
        Log.d("gotToAdd", "got to addToList");
        if (exampleItemEvents==null) {
            exampleItemEvents = new ArrayList<>();
            mAdapter.notifyDataSetChanged();
            buildRecyclerView();
            exampleItemEvents.add(p);
            return exampleItemEvents;
        } else if( exampleItemEvents.size()==0) {
            exampleItemEvents.add(p);
            mAdapter.notifyDataSetChanged();
            buildRecyclerView();
            return exampleItemEvents;
        } else if (exampleItemEvents.size()==1) {
            if (dateTimeP > exampleItemEvents.get(0).getId()) {
                exampleItemEvents.add(p);
                mAdapter.notifyDataSetChanged();
                buildRecyclerView();
                return exampleItemEvents;
            }
            if (dateTimeP < exampleItemEvents.get(0).getId()) {
                exampleItemEvents.add(0, p);
                mAdapter.notifyDataSetChanged();
                buildRecyclerView();
                return exampleItemEvents;
            }
        } else {
            for (int i = 0; i < exampleItemEvents.size(); i++) {
                if (i==0 && dateTimeP<exampleItemEvents.get(0).getId()) {
                    exampleItemEvents.add(0, p);
                    mAdapter.notifyDataSetChanged();
                    buildRecyclerView();
                    return exampleItemEvents;
                }
                if (i >= 1 && dateTimeP > exampleItemEvents.get(i-1).getId() && dateTimeP < exampleItemEvents.get(i).getId()) {
                    exampleItemEvents.add(i, p);
                    mAdapter.notifyDataSetChanged();
                    buildRecyclerView();
                    return exampleItemEvents;
                }
                if (i==exampleItemEvents.size()-1 && dateTimeP > exampleItemEvents.get(i).getId()) {
                    exampleItemEvents.add(p);
                    mAdapter.notifyDataSetChanged();
                    buildRecyclerView();
                    return exampleItemEvents;
                }
            }
        }
        return exampleItemEvents;
    }
    private void saveData() {
        SharedPreferences sharedPreferences4 = getSharedPreferences("shared preferences events", MODE_PRIVATE);
        SharedPreferences.Editor editor4 = sharedPreferences4.edit();
        Gson gson4 = new Gson();
        String sgson4 = gson4.toJson(exampleItemEvents);
        editor4.putString("user list", sgson4);
        editor4.apply();
    }

    private void loadData() {
            SharedPreferences sharedPreferences2 = getSharedPreferences("shared preferences events", MODE_PRIVATE);
            Gson gson2 = new Gson();
            String sgson2 = sharedPreferences2.getString("user list", null);
            Type type2 = new TypeToken<ArrayList<ExampleItemEvent>>() {
            }.getType();
            exampleItemEvents = gson2.fromJson(sgson2, type2);
            if (exampleItemEvents==null){
                exampleItemEvents = new ArrayList<>();
        }

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
        Calendar datetime = Calendar.getInstance();
        Calendar c = Calendar.getInstance();
        datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        datetime.set(Calendar.MINUTE, minute);
        if(datetime.getTimeInMillis() > c.getTimeInMillis()){
            return true;
        }else{
            return false;
        }
    }

}
