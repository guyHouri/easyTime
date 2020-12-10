package com.example.easytime101;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

  Button login,register;
  SharedPreferences spCheckBox;
  boolean check=false;
  EditText etusername, etpassword;
  boolean correctUsernamePassword=false;
  String keepLoggedIn="", didpass="", StringOfNames="";
  List<UsersList> usersListApp;
  CheckBox checkBox;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    loadData();
    spCheckBox = getSharedPreferences("stam", MODE_PRIVATE);
    keepLoggedIn = spCheckBox.getString("isChecked", null);
    if (usersListApp == null) {
      usersListApp = new ArrayList<>();
      keepLoggedIn="false";
    }
    addUser();
    login = (Button) findViewById(R.id.btnlogin);
    login.setOnClickListener(this);
    register = (Button) findViewById(R.id.btnregister);
    register.setOnClickListener(this);
    etusername = (EditText) findViewById(R.id.etNameOfUser);
    etpassword = (EditText) findViewById(R.id.etUserPassword);
    etpassword.setText("");
    etusername.setText("");

    checkBox = (CheckBox) findViewById(R.id.cbKeepKoggedIn);
    checkBox.setOnClickListener(this);

    if (keepLoggedIn == null)
      keepLoggedIn = "";
    Intent intent = getIntent();
    if (intent.getExtras() != null)
      didpass = intent.getExtras().getString("logOut", null);
    if (didpass == null)
      didpass = "";
    if (didpass.equals("intentPassed")) {
      keepLoggedIn = "false";
      SharedPreferences.Editor editorCheckBox = spCheckBox.edit();
      editorCheckBox.putString("isChecked", "false");
      editorCheckBox.commit();
    }
    if (keepLoggedIn.equals("true")) {
      sendToApp();
    }
  }
  @Override
  public void onClick(View v) {
    if (v==checkBox) {
      if (checkBox.isChecked()) {
        SharedPreferences.Editor editorCheckBox=spCheckBox.edit();
        editorCheckBox.putString("isChecked","true");
        editorCheckBox.commit();
      }
    }
    if (v==login) {
      for (int i = 0; i <usersListApp.size(); i++) {
        Log.d("in for", "username in list: " + usersListApp.get(i).getUsername() + ", username that checks: "+etusername.getText().toString());
        if (usersListApp.get(i).getUsername().equals(etusername.getText().toString()) &&
                usersListApp.get(i).getPassword().equals(etpassword.getText().toString())) {
          Log.d("user", "username: " + usersListApp.get(i).getUsername() + ", password: " + usersListApp.get(i).getPassword());
          check=true;
          sendToApp();
        }
      }
      if (check==false)
        Toast.makeText(this, "name or pass is incorrect", Toast.LENGTH_LONG).show();
    }
    if (v==register){
      Intent toRegister = new Intent(this, signup.class);
      userNameStringList();
      toRegister.putExtra("listtoffnamess", StringOfNames);
      startActivity(toRegister);
    }
  }

  public void addUser() {
    Intent intent = getIntent();
    if (intent.getExtras()!=null) {
      if (intent.getStringExtra("username") != null) {
          UsersList user = new UsersList(intent.getExtras().get("username").toString(),
                  intent.getExtras().get("password").toString(), intent.getExtras().get("email").toString());
          usersListApp.add(user);
          saveData();
          loadData();
          Log.d("user saved", "username: " + intent.getExtras().get("username").toString() + ", password: " +  intent.getExtras().get("password").toString());
        }
    }
  }

  private void sendToApp() {
    Log.e("NameOfUser inMain", usersListApp.get(usersListApp.size()-1).getUsername());
    Intent toApp = new Intent(this, open2.class);
    toApp.putExtra("theNameOfTheUser", usersListApp.get(usersListApp.size()-1).getUsername());
    startActivity(toApp);
    correctUsernamePassword=false;
  }

  private void saveData() {
    SharedPreferences sharedPreferences2 = getSharedPreferences("shared preferences users", MODE_PRIVATE);
    SharedPreferences.Editor editor2 = sharedPreferences2.edit();
    Gson gson2 = new Gson();
    String sgson2 = gson2.toJson(usersListApp);
    editor2.putString("user list", sgson2);
    editor2.apply();
  }

  private void loadData() {
    SharedPreferences sharedPreferences2 = getSharedPreferences("shared preferences users", MODE_PRIVATE);
    Gson gson2 = new Gson();
    String sgson2 = sharedPreferences2.getString("user list", null);
    Type type2 = new TypeToken<ArrayList<UsersList>>() {}.getType();
    usersListApp = gson2.fromJson(sgson2, type2);
  }

  private void userNameStringList() {
    for (int i =0; i<usersListApp.size(); i++) {
      StringOfNames=StringOfNames+usersListApp.get(i).getUsername();
    }
  }

  }

