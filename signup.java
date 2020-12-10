package com.example.easytime101;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class signup extends AppCompatActivity implements View.OnClickListener {
    String username, password, email;
    EditText etuser, etpass, etemail;
    ArrayList<String> UserNames = new ArrayList<>();
    Button login;
    int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        etuser=(EditText) findViewById(R.id.etusername);
        etemail=(EditText) findViewById(R.id.etemail);
        etpass=(EditText) findViewById(R.id.etpassword);
        login=(Button) findViewById(R.id.btnlogin);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        username = etuser.getText().toString();
        password = etpass.getText().toString();
        email = etemail.getText().toString();
        if (!etpass.getText().toString().isEmpty() && !etuser.getText().toString().isEmpty()
                && !TextUtils.isEmpty(etemail.getText().toString()) && Patterns.EMAIL_ADDRESS.matcher(etemail.getText().toString()).matches()
                && !isUserTaken(username)) {
            UsersList user = new UsersList(username,password,email);
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("username", user.getUsername());
            UserNames.add(user.getUsername());
            intent.putExtra("password", user.getPassword());
            intent.putExtra("email", user.getEmail());
            intent.putExtra("times", i);
            i++;
            Toast.makeText(this, "hi "+user.getUsername()+", we are happy you succefully joined easy time! :)", Toast.LENGTH_LONG).show();
            startActivity(intent);
        }
        if (etpass.getText().toString().isEmpty() || etuser.getText().toString().isEmpty() || TextUtils.isEmpty(etemail.getText().toString()))
                Toast.makeText(this, "must enter fields", Toast.LENGTH_SHORT).show();
        if (!Patterns.EMAIL_ADDRESS.matcher(etemail.getText().toString()).matches())
                Toast.makeText(this, "email isnt valid", Toast.LENGTH_SHORT).show();
        if (isUserTaken(username))
            Toast.makeText(this, "the username "+username+" is taken, please select a new username", Toast.LENGTH_SHORT).show();

    }

    public boolean isUserTaken(String s) {
        Intent getNames = getIntent();
        String listtoffnamess = getNames.getStringExtra("listtoffnamess");
        for (i=0; i<listtoffnamess.length(); i++) {
            if (listtoffnamess.contains(s)) {
                Toast.makeText(this, "the username "+username+" is taken, please select a new username", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return false;
    }
}